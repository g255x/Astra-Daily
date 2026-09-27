package dev.Astra.mod.modules.impl.d;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.l.InstantReplenish;
import dev.Astra.mod.modules.impl.l.PacketMine;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import j2cc.Nativeify;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Map;

public class ShulkerReplenish extends Module {
    public static ShulkerReplenish INSTANCE;

    /** 放下后方块出现的最小 tick 延迟 */
    private static final int PLACE_TO_OPEN_DELAY_TICKS = 2;
    /** 放置最多尝试次数（每 2 tick 一次） */
    private static final int MAX_PLACE_ATTEMPTS = 20;
    /** 打开最多尝试次数（每 tick 一次） */
    private static final int MAX_OPEN_ATTEMPTS = 10;
    /** 打开尝试完毕后最大等待 tick 数 */
    private static final int MAX_WAIT_AFTER_OPEN_TICKS = 40;

    // ===== 设置 =====
    private final BooleanSetting inventory  = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting mine       = this.add(new BooleanSetting("Mine", true));
    private final BooleanSetting rotate     = this.add(new BooleanSetting("Rotate", true));

    // ===== 严格距离 =====
    private final SliderSetting range        = this.add(new SliderSetting("MaxRange", 4.0, 0.0, 4.5, 0.1));
    private final SliderSetting minRange     = this.add(new SliderSetting("MinRange", 1.0, 0.0, 3.0, 0.1));
    private final BooleanSetting strictRange = this.add(new BooleanSetting("StrictRange", true));

    // ===== Take 控制 =====
    private final BooleanSetting takeAll     = this.add(new BooleanSetting("TakeAll", false));

    // ===== 关闭时机 =====
    private final SliderSetting closeDelay    = this.add(new SliderSetting("CloseDelay", 20, 5, 60, 1));
    private final SliderSetting closingWait   = this.add(new SliderSetting("ClosingWait", 10, 0, 40, 1));
    private final SliderSetting openWaitTicks = this.add(new SliderSetting("OpenWaitTicks", 40, 10, 100, 1));
    private final BooleanSetting autoClose    = this.add(new BooleanSetting("AutoClose", true));

    public BlockPos placePos;

    private enum Stage {
        IDLE, PLACING, PLACED, OPENED, WAITING, CLOSING
    }

    private Stage stage = Stage.IDLE;
    private int placedTicks   = 0;
    private int openedTicks   = 0;
    private int waitingTicks  = 0;
    private int closingTicks  = 0;
    private int emptyTakeTicks = 0;
    private int placeAttempts = 0;
    private int openAttempts  = 0;

    // ❌ 构造方法不加 @Nativeify
    public ShulkerReplenish() {
        super("ShulkerReplenish", Category.Combat);
        this.setChinese("自动潜影盒补给");
        INSTANCE = this;
    }

    // ❌ 生命周期重写不加 @Nativeify
    @Override
    public void onEnable() {
        this.placePos = null;
        this.stage = Stage.IDLE;
        this.placedTicks = 0;
        this.openedTicks = 0;
        this.waitingTicks = 0;
        this.closingTicks = 0;
        this.emptyTakeTicks = 0;
        this.placeAttempts = 0;
        this.openAttempts = 0;

        if (mc.player == null) { disable(); return; }

        doPlaceOnce();
    }

    // ❌ 生命周期重写不加 @Nativeify
    @Override
    public void onDisable() {
        this.stage = Stage.IDLE;
        if (this.mine.getValue() && this.placePos != null
                && PacketMine.INSTANCE != null) {
            PacketMine.INSTANCE.mine(this.placePos);
        }
    }

    // ⚠️ @EventListener，不能加 @Nativeify
    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null) return;

        switch (this.stage) {

            case IDLE:
                return;

            // ==================== 放置重试阶段 ====================
            case PLACING: {
                this.placedTicks++;

                boolean placed = this.placePos != null
                        && mc.world.getBlockState(this.placePos).getBlock() instanceof ShulkerBoxBlock;

                if (placed) {
                    this.stage = Stage.PLACED;
                    this.placedTicks = 0;
                    this.openAttempts = 0;
                    return;
                }

                if (this.placeAttempts >= MAX_PLACE_ATTEMPTS) {
                    disable();
                    return;
                }

                // 每 2 tick 重试一次，避免发包太猛
                if (this.placedTicks % 2 == 0) {
                    this.placeAttempts++;
                    this.attemptPlace();
                }
                return;
            }

            // ==================== 打开重试阶段 ====================
            case PLACED: {
                this.placedTicks++;

                // 已经打开了
                if (mc.currentScreen instanceof ShulkerBoxScreen) {
                    this.stage = Stage.OPENED;
                    this.openedTicks = 0;
                    this.emptyTakeTicks = 0;
                    return;
                }

                // 方块还在吗
                boolean placed = this.placePos != null
                        && mc.world.getBlockState(this.placePos).getBlock() instanceof ShulkerBoxBlock;
                if (!placed) {
                    this.stage = Stage.PLACING;
                    this.placedTicks = 0;
                    return;
                }

                if (this.placedTicks < PLACE_TO_OPEN_DELAY_TICKS) return;

                // 每 tick 尝试打开一次，最多 MAX_OPEN_ATTEMPTS 次
                if (this.openAttempts < MAX_OPEN_ATTEMPTS) {
                    this.openBlock(this.placePos);
                    this.openAttempts++;
                } else if (this.placedTicks > MAX_WAIT_AFTER_OPEN_TICKS) {
                    // 尝试完还没有界面，放弃
                    disable();
                }
                return;
            }

            // ==================== 已打开 ====================
            case OPENED: {
                if (!(mc.currentScreen instanceof ShulkerBoxScreen)) {
                    this.stage = Stage.IDLE;
                    disable();
                    return;
                }

                this.openedTicks++;

                boolean tookThisTick = this.doTake();

                if (tookThisTick) {
                    this.emptyTakeTicks = 0;
                } else {
                    this.emptyTakeTicks++;
                }

                if (this.emptyTakeTicks >= 5) {
                    if (this.autoClose.getValue()) {
                        this.stage = Stage.WAITING;
                        this.waitingTicks = 0;
                    } else {
                        return;
                    }
                }

                if (this.openedTicks >= 200) {
                    this.stage = Stage.WAITING;
                    this.waitingTicks = 0;
                }
                return;
            }

            case WAITING: {
                if (!(mc.currentScreen instanceof ShulkerBoxScreen)) {
                    this.stage = Stage.CLOSING;
                    this.closingTicks = 0;
                    this.waitingTicks = 0;
                    return;
                }

                if (this.doTake()) {
                    this.emptyTakeTicks = 0;
                    this.stage = Stage.OPENED;
                    return;
                }

                this.waitingTicks++;

                if (this.waitingTicks >= this.closeDelay.getValueInt()) {
                    this.closeScreenSafely();
                    this.stage = Stage.CLOSING;
                    this.closingTicks = 0;
                }
                return;
            }

            case CLOSING: {
                if (!(mc.currentScreen instanceof ShulkerBoxScreen)) {
                    this.closingTicks++;
                    if (this.closingTicks >= this.closingWait.getValueInt()) {
                        this.stage = Stage.IDLE;
                        disable();
                    }
                } else {
                    if (this.closingTicks % 10 == 0) {
                        this.closeScreenSafely();
                    }
                    this.closingTicks++;
                    if (this.closingTicks >= 60) {
                        this.stage = Stage.IDLE;
                        disable();
                    }
                }
                return;
            }
        }
    }

    // ✅ @Nativeify
    @Nativeify
    private void closeScreenSafely() {
        if (mc.player != null) {
            mc.player.closeHandledScreen();
        }
    }

    // ✅ @Nativeify
    @Nativeify
    private boolean doTake() {
        if (!(mc.player.currentScreenHandler instanceof ShulkerBoxScreenHandler shulker)) return false;

        int[] stealCount = calculateStealCount();
        boolean took = false;

        for (Slot slot : shulker.slots) {
            if (slot.id >= 27) continue;
            if (slot.getStack().isEmpty()) continue;

            Type type = needSteal(slot.getStack(), stealCount);
            if (type == Type.QuickMove) {
                mc.interactionManager.clickSlot(shulker.syncId, slot.id, 0,
                        SlotActionType.QUICK_MOVE, mc.player);
                took = true;
            } else if (type == Type.Stack) {
                for (int i = 0; i < 36; i++) {
                    ItemStack stack = mc.player.getInventory().getStack(i);
                    if (!stack.isEmpty() && stack.isStackable()
                            && stack.getItem() == slot.getStack().getItem()
                            && stack.getCount() < stack.getMaxCount()) {
                        int screenSlot = (i < 9 ? i + 36 : i) + 18;
                        mc.interactionManager.clickSlot(shulker.syncId, slot.id, 0,
                                SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(shulker.syncId, screenSlot, 0,
                                SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(shulker.syncId, slot.id, 0,
                                SlotActionType.PICKUP, mc.player);
                        took = true;
                        break;
                    }
                }
            }
        }
        return took;
    }

    // ✅ @Nativeify
    @Nativeify
    private int[] calculateStealCount() {
        int[] counts = new int[15];
        counts[0]  = getMissing(Items.END_CRYSTAL);
        counts[1]  = getMissing(Items.EXPERIENCE_BOTTLE);
        counts[2]  = getMissing(Items.TOTEM_OF_UNDYING);
        counts[3]  = getMissing(Items.ENCHANTED_GOLDEN_APPLE);
        counts[4]  = getMissing(Blocks.OBSIDIAN.asItem());
        counts[5]  = getMissing(Blocks.COBWEB.asItem());
        counts[6]  = getMissing(Blocks.GLOWSTONE.asItem());
        counts[7]  = getMissing(Blocks.RESPAWN_ANCHOR.asItem());
        counts[8]  = getMissing(Items.ENDER_PEARL);
        counts[9]  = getMissing(Blocks.PISTON.asItem());
        counts[10] = getMissing(Blocks.REDSTONE_BLOCK.asItem());
        counts[11] = getMissing(Blocks.WHITE_BED.asItem());
        return counts;
    }

    // ✅ @Nativeify
    @Nativeify
    private int getMissing(Item item) {
        Map<Integer, ItemStack> kit = InstantReplenish.getTargetSlots();
        int target = 0;
        for (ItemStack t : kit.values()) {
            if (t.getItem() == item) target += t.getCount();
        }
        if (target == 0) return 0;

        int have = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.getInventory().getStack(i);
            if (s.getItem() == item) have += s.getCount();
        }
        return Math.max(0, target - have);
    }

    // ✅ @Nativeify
    @Nativeify
    private Type needSteal(ItemStack i, int[] counts) {
        if (i.getItem() == Items.END_CRYSTAL && counts[0] > 0) {
            counts[0] -= i.getCount();
            return counts[0] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Items.EXPERIENCE_BOTTLE && counts[1] > 0) {
            counts[1] -= i.getCount();
            return counts[1] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Items.TOTEM_OF_UNDYING && counts[2] > 0) {
            counts[2] -= i.getCount();
            return counts[2] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Items.ENCHANTED_GOLDEN_APPLE && counts[3] > 0) {
            counts[3] -= i.getCount();
            return counts[3] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Blocks.OBSIDIAN.asItem() && counts[4] > 0) {
            counts[4] -= i.getCount();
            return counts[4] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Blocks.COBWEB.asItem() && counts[5] > 0) {
            counts[5] -= i.getCount();
            return counts[5] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Blocks.GLOWSTONE.asItem() && counts[6] > 0) {
            counts[6] -= i.getCount();
            return counts[6] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Blocks.RESPAWN_ANCHOR.asItem() && counts[7] > 0) {
            counts[7] -= i.getCount();
            return counts[7] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Items.ENDER_PEARL && counts[8] > 0) {
            counts[8] -= i.getCount();
            return counts[8] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Blocks.PISTON.asItem() && counts[9] > 0) {
            counts[9] -= i.getCount();
            return counts[9] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Blocks.REDSTONE_BLOCK.asItem() && counts[10] > 0) {
            counts[10] -= i.getCount();
            return counts[10] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.getItem() == Blocks.WHITE_BED.asItem() && counts[11] > 0) {
            counts[11] -= i.getCount();
            return counts[11] < 0 ? Type.Stack : Type.QuickMove;
        }
        return Type.None;
    }

    private enum Type { None, Stack, QuickMove }

    // ========== 一次性：搜索 + 首次放置 ==========

    // ✅ @Nativeify
    @Nativeify
    private void doPlaceOnce() {
        if (InstantReplenish.getTargetSlots().isEmpty()) {
            sendMessage("§4No kit loaded.");
            disable();
            return;
        }

        if (findShulker() == -1) {
            sendMessage("§4No shulker box in inventory.");
            disable();
            return;
        }

        BlockPos bestPos = findPlacePos();
        if (bestPos == null) {
            sendMessage("§4No place position.");
            disable();
            return;
        }

        this.placePos = bestPos;
        this.placeAttempts = 1;
        this.openAttempts = 0;
        this.placedTicks = 0;

        this.attemptPlace();
        this.stage = Stage.PLACING;
    }

    /** 换槽 + 放置一次 */
    // ✅ @Nativeify
    @Nativeify
    private void attemptPlace() {
        if (this.placePos == null) return;
        int slot = findShulker();
        if (slot == -1) return;

        int oldSlot = mc.player.getInventory().selectedSlot;
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, oldSlot);
            placeBlock(this.placePos);
            InventoryUtil.inventorySwap(slot, oldSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
            placeBlock(this.placePos);
            InventoryUtil.switchToSlot(oldSlot);
        }
    }

    // ✅ @Nativeify
    @Nativeify
    private BlockPos findPlacePos() {
        double distance = 100.0;
        BlockPos bestPos = null;
        double maxR = this.range.getValue();
        double minR = this.minRange.getValue();

        for (BlockPos pos : BlockUtil.getSphere((float) maxR)) {
            if (!mc.world.isAir(pos.up())) continue;
            if (mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST) continue;
            if (mc.world.getBlockState(pos).getBlock() instanceof ShulkerBoxBlock) continue;

            double dist = distanceTo(pos);
            if (dist > maxR) continue;
            if (dist < minR) continue;

            BlockPos belowPos = pos.down();
            var belowState = mc.world.getBlockState(belowPos);
            if (BlockUtil.clientCanPlace(pos, false)
                    && BlockUtil.isStrictDirection(belowPos, Direction.UP)
                    && BlockUtil.canClick(belowPos)
                    && !belowState.isAir()
                    && belowState.isFullCube(mc.world, belowPos)
                    && dist < distance) {
                distance = dist;
                bestPos = pos;
            }
        }
        return bestPos;
    }

    // ✅ @Nativeify
    @Nativeify
    private double distanceTo(BlockPos pos) {
        if (this.strictRange.getValue()) {
            return mc.player.getEyePos().distanceTo(pos.toCenterPos());
        }
        return Math.sqrt(mc.player.squaredDistanceTo(pos.toCenterPos()));
    }

    // ✅ @Nativeify
    @Nativeify
    private int findShulker() {
        if (this.inventory.getValue()) {
            for (int i = 0; i < 36; i++) {
                if (isShulkerBox(mc.player.getInventory().getStack(i))) {
                    return i < 9 ? i + 36 : i;
                }
            }
            return -1;
        } else {
            return InventoryUtil.findItem(Items.SHULKER_BOX);
        }
    }

    // ✅ @Nativeify
    @Nativeify
    private boolean isShulkerBox(ItemStack s) {
        if (s == null || s.isEmpty()) return false;
        if (s.getItem() instanceof BlockItem bi) {
            return bi.getBlock() instanceof ShulkerBoxBlock;
        }
        return false;
    }

    // ==================== 转头工具：仿 SelfTrap ====================

    /** ★ 打开潜影盒：手动 lookAt → clickBlock(false) → snapBack */
    // ✅ @Nativeify
    @Nativeify
    private void openBlock(BlockPos pos) {
        Direction side = BlockUtil.getClickSide(pos);
        if (side == null) side = Direction.UP;

        if (this.rotate.getValue()) {
            Vec3d directionVec = new Vec3d(
                    pos.getX() + 0.5 + side.getVector().getX() * 0.5,
                    pos.getY() + 0.5 + side.getVector().getY() * 0.5,
                    pos.getZ() + 0.5 + side.getVector().getZ() * 0.5
            );
            Astra.ROTATION.lookAt(directionVec);
            BlockUtil.clickBlock(pos, side, false);
            Astra.ROTATION.snapBack();
        } else {
            BlockUtil.clickBlock(pos, side, false);
        }
    }

    /** ★ 放置潜影盒：手动 lookAt → clickBlock(false) → snapBack */
    // ✅ @Nativeify
    @Nativeify
    private void placeBlock(BlockPos pos) {
        if (AntiRegear.INSTANCE != null) {
            AntiRegear.INSTANCE.addIgnored(pos);
        }
        BlockPos clickPos = pos.down();
        Direction side = Direction.UP;

        if (this.rotate.getValue()) {
            Vec3d directionVec = new Vec3d(
                    clickPos.getX() + 0.5 + side.getVector().getX() * 0.5,
                    clickPos.getY() + 0.5 + side.getVector().getY() * 0.5,
                    clickPos.getZ() + 0.5 + side.getVector().getZ() * 0.5
            );
            Astra.ROTATION.lookAt(directionVec);
            BlockUtil.clickBlock(clickPos, side, false);
            Astra.ROTATION.snapBack();
        } else {
            BlockUtil.clickBlock(clickPos, side, false);
        }
    }
}