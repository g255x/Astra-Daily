package dev.Astra.mod.modules.impl.l;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClickBlockEvent;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.math.FadeUtils;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.asm.accessors.IPlayerMoveC2SPacket;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.d.AutoAnchor;
import dev.Astra.mod.modules.impl.d.Criticals;
import dev.Astra.mod.modules.impl.e.Blink;
import dev.Astra.mod.modules.impl.i.ElytraFly;
import dev.Astra.mod.modules.settings.enums.Timing;
import dev.Astra.mod.modules.settings.enums.a;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import j2cc.Nativeify;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class PacketMine extends Module {
    public static PacketMine INSTANCE;
    public static BlockPos secondPos;
    public static double progress;
    private final FadeUtils animationTime = new FadeUtils(1000L);
    private final FadeUtils secondAnim = new FadeUtils(1000L);
    private final DecimalFormat df = new DecimalFormat("0.0");
    private final EnumSetting<Page> page = this.add(new EnumSetting<>("Page", Page.General));
    private final SliderSetting stopDelay = this.add(new SliderSetting("StopDelay", 50.0, 0.0, 500.0, 1.0, () -> this.page.is(Page.General)));
    private final SliderSetting startDelay = this.add(new SliderSetting("StartDelay", 200.0, 0.0, 500.0, 1.0, () -> this.page.is(Page.General)));
    private final SliderSetting damage = this.add(new SliderSetting("Damage", 0.7f, 0.0, 2.0, 0.01, () -> this.page.is(Page.General)));
    private final SliderSetting maxBreak = this.add(new SliderSetting("MaxBreak", 3.0, 0.0, 20.0, 1.0, () -> this.page.is(Page.General)));
    private final EnumSetting<Timing> timing = this.add(new EnumSetting<>("Timing", Timing.All, () -> this.page.getValue() == Page.General));
    private final BooleanSetting fastBypass = this.add(new BooleanSetting("FastBypass", false, () -> this.page.is(Page.General)));
    private final BooleanSetting instant = this.add(new BooleanSetting("Instant", false, () -> this.page.is(Page.General)));
    private final BooleanSetting wait = this.add(new BooleanSetting("Wait", true, () -> !this.instant.getValue() && this.page.is(Page.General)));
    private final BooleanSetting mineAir = this.add(new BooleanSetting("MineAir", true, () -> this.wait.getValue() && !this.instant.getValue() && this.page.is(Page.General)));
    private final BooleanSetting hotBar = this.add(new BooleanSetting("HotbarSwap", false, () -> this.page.is(Page.General)));
    private final BooleanSetting doubleBreak = this.add(new BooleanSetting("DoubleBreak", true, () -> this.page.is(Page.General))).setParent();
    public final BooleanSetting autoSwitch = this.add(new BooleanSetting("AutoSwitch", true, () -> this.page.is(Page.General) && this.doubleBreak.isOpen()));
    private final SliderSetting start = this.add(new SliderSetting("Start", 0.9f, 0.0, 2.0, 0.01, () -> this.page.is(Page.General) && this.doubleBreak.isOpen()));
    private final SliderSetting timeOut = this.add(new SliderSetting("TimeOut", 1.2f, 0.0, 2.0, 0.01, () -> this.page.is(Page.General) && this.doubleBreak.isOpen()));
    private final BooleanSetting setAir = this.add(new BooleanSetting("SetAir", false, () -> this.page.is(Page.General)));
    private final BooleanSetting swing = this.add(new BooleanSetting("Swing", true, () -> this.page.is(Page.General)));
    private final BooleanSetting endSwing = this.add(new BooleanSetting("EndSwing", false, () -> this.page.is(Page.General)));
    public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, 3.0, 10.0, 0.1, () -> this.page.is(Page.General)));
    private final EnumSetting<a> swingMode = this.add(new EnumSetting<>("SwingSide", a.All, () -> this.page.is(Page.General)));
    private final BooleanSetting unbreakableCancel = this.add(new BooleanSetting("UnbreakableCancel", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting checkWeb = this.add(new BooleanSetting("CheckWeb", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting checkGround = this.add(new BooleanSetting("CheckGround", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false, () -> this.page.is(Page.Check)).setParent());
    private final BooleanSetting allowOffhand = this.add(new BooleanSetting("AllowOffhand", true, () -> this.page.is(Page.Check) && this.usingPause.isOpen()));
    private final BooleanSetting bypassGround = this.add(new BooleanSetting("BypassGround", true, () -> this.page.is(Page.Check)));
    private final SliderSetting bypassTime = this.add(new SliderSetting("BypassTime", 400, 0, 2000, () -> this.bypassGround.getValue() && this.page.is(Page.Check)));
    private final BooleanSetting rotate = this.add(new BooleanSetting("StartRotate", true, () -> this.page.is(Page.Rotation)));
    private final BooleanSetting endRotate = this.add(new BooleanSetting("EndRotate", false, () -> this.page.is(Page.Rotation)));
    private final SliderSetting syncTime = this.add(new SliderSetting("Sync", 300, 0, 1000, () -> this.page.is(Page.Rotation)));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, () -> this.page.is(Page.Rotation)).setParent());
    private final BooleanSetting whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 20.0, 0.0, 360.0, 0.1, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 100, 0, 100, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    private final BooleanSetting checkDouble = this.add(new BooleanSetting("CheckDouble", false, () -> this.page.is(Page.Render)));
    private final EnumSetting<Animation> animation = this.add(new EnumSetting<>("Animation", Animation.Up, () -> this.page.is(Page.Render)));
    private final EnumSetting<Easing> ease = this.add(new EnumSetting<>("Ease", Easing.CubicInOut, () -> this.page.is(Page.Render)));
    private final EnumSetting<Easing> fadeEase = this.add(new EnumSetting<>("FadeEase", Easing.CubicInOut, () -> this.page.is(Page.Render)));
    private final SliderSetting expandLine = this.add(new SliderSetting("ExpandLine", 0.0, 0.0, 1.0, () -> this.page.is(Page.Render)));
    private final ColorSetting startColor = this.add(new ColorSetting("StartFill", new Color(255, 255, 255, 100), () -> this.page.is(Page.Render)));
    private final ColorSetting startOutlineColor = this.add(new ColorSetting("StartOutline", new Color(255, 255, 255, 100), () -> this.page.is(Page.Render)));
    private final ColorSetting endColor = this.add(new ColorSetting("EndFill", new Color(255, 255, 255, 100), () -> this.page.is(Page.Render)));
    private final ColorSetting endOutlineColor = this.add(new ColorSetting("EndOutline", new Color(255, 255, 255, 100), () -> this.page.is(Page.Render)));
    private final ColorSetting doubleColor = this.add(new ColorSetting("DoubleFill", new Color(88, 94, 255, 100), () -> this.doubleBreak.getValue() && this.page.is(Page.Render)));
    private final ColorSetting doubleOutlineColor = this.add(new ColorSetting("DoubleOutline", new Color(88, 94, 255, 100), () -> this.doubleBreak.getValue() && this.page.is(Page.Render)));
    private final BooleanSetting text = this.add(new BooleanSetting("Text", true, () -> this.page.is(Page.Render)));
    private final BooleanSetting box = this.add(new BooleanSetting("Box", true, () -> this.page.is(Page.Render)));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true, () -> this.page.is(Page.Render)));
    public final Timer mineTimer = new Timer();
    private final Timer sync = new Timer();
    private final Timer secondTimer = new Timer();
    private final Timer delayTimer = new Timer();
    private final Timer startTime = new Timer();
    public static boolean ghost;
    public static boolean complete;
    int lastSlot = -1;
    Vec3d directionVec = null;
    BlockPos breakPos;
    boolean startPacket = false;
    int breakNumber = 0;
    public double breakFinalTime;
    double secondFinalTime;
    boolean sendGroundPacket = false;
    boolean swapped = false;
    int mainSlot = 0;

    public PacketMine() {
        super("PacketMine", Module.Category.Player);
        this.setChinese("发包挖掘");
        INSTANCE = this;
    }

    public static BlockPos getBreakPos() {
        if (INSTANCE.isOn()) {
            return PacketMine.INSTANCE.breakPos;
        }
        return null;
    }

    @Override
    public String getInfo() {
        if (progress >= 1.0) {
            return "Done";
        }
        return this.df.format(progress * 100.0) + "%";
    }

    // ⚠️ @EventListener
    @EventListener
    public void onRotate(RotationEvent event) {
        if (this.rotate.getValue() && this.shouldYawStep() && this.directionVec != null && !this.sync.passedMs(this.syncTime.getValue())) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @Override
    public void onLogin() {
        this.startPacket = false;
        ghost = false;
        complete = false;
        this.breakPos = null;
        secondPos = null;
    }

    @Override
    public void onDisable() {
        this.startPacket = false;
        ghost = false;
        complete = false;
        this.breakPos = null;
    }

    // ⚠️ 大量 InventoryUtil + 附魔，不加 @Nativeify
    private void autoSwitch() {
        if (mc.player == null || mc.world == null) return;
        if (this.autoSwitch.getValue() && this.doubleBreak.getValue()) {
            int index = -1;
            if (secondPos != null) {
                float CurrentFastest = 1.0f;
                for (int i = 0; i < 9; ++i) {
                    ItemStack stack = mc.player.getInventory().getStack(i);
                    if (stack == ItemStack.EMPTY) continue;
                    float digSpeed = getEfficiencyLevel(stack);
                    float destroySpeed = stack.getMiningSpeedMultiplier(mc.world.getBlockState(secondPos));
                    if (digSpeed + destroySpeed <= CurrentFastest) continue;
                    CurrentFastest = digSpeed + destroySpeed;
                    index = i;
                }
            }
            if (index == -1 || mc.options.useKey.isPressed() || mc.options.attackKey.isPressed() || mc.player.isUsingItem() || !this.secondTimer.passedMs(this.getBreakTime(secondPos, index, this.start.getValue()))) {
                if (this.swapped) {
                    InventoryUtil.switchToSlot(this.mainSlot);
                    this.swapped = false;
                }
            } else if (index != mc.player.getInventory().selectedSlot) {
                this.mainSlot = mc.player.getInventory().selectedSlot;
                InventoryUtil.switchToSlot(index);
                this.swapped = true;
            }
        }
    }

    // ==================== onTick：调度 ====================
    // ⚠️ @EventListener，不能加 @Nativeify
    @EventListener
    public void onTick(ClientTickEvent event) {
        if (PacketMine.nullCheck()) return;
        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return;

        // --- 预处理 ---
        this.updateCompleteState();
        this.updateSecondPos();

        if (this.timing.is(Timing.Pre) && event.isPost() || this.timing.is(Timing.Post) && event.isPre()) return;
        if (player.isDead()) secondPos = null;

        this.autoSwitch();

        // --- 状态检查 ---
        if (player.isCreative()) {
            this.resetMiningState();
            return;
        }
        if (this.breakPos == null) {
            this.resetMiningState();
            return;
        }

        // --- 计算 ---
        int slot = this.getTool(this.breakPos);
        if (slot == -1) slot = player.getInventory().selectedSlot;

        this.breakFinalTime = this.getBreakTime(this.breakPos, slot);
        progress = (double) this.mineTimer.getMs() / this.breakFinalTime;
        if (this.isAir(this.breakPos)) this.breakNumber = 0;

        // --- 中止 / 不可破坏判断 ---
        if (this.shouldAbortMining()) return;
        if (PacketMine.unbreakable(this.breakPos)) {
            this.handleUnbreakable();
            return;
        }
        if (!this.passPreconditionChecks(player)) return;

        // --- 执行 ---
        if (this.startPacket) {
            this.executeStopPacket(player, slot);
        } else {
            this.executeStartPacket(player, slot);
        }
    }

    // ==================== onTick 拆分出来的 @Nativeify 方法 ====================

    @Nativeify
    private void updateCompleteState() {
        if (this.breakPos != null && mc.world != null && mc.world.isAir(this.breakPos)) {
            complete = true;
        }
    }

    @Nativeify
    private void updateSecondPos() {
        if (secondPos == null) return;
        if (mc.player == null || mc.world == null) return;

        int secondSlot = this.getTool(secondPos);
        if (secondSlot == -1) secondSlot = mc.player.getInventory().selectedSlot;
        this.secondFinalTime = this.getBreakTime(secondPos, secondSlot, 1.0);

        if (this.isAir(secondPos) || PacketMine.unbreakable(secondPos)) {
            secondPos = null;
        } else {
            double time = this.getBreakTime(secondPos, mc.player.getInventory().selectedSlot, 1.0);
            if (this.secondTimer.passedMs(time * this.timeOut.getValue())) {
                secondPos = null;
            }
        }
    }

    @Nativeify
    private void resetMiningState() {
        this.breakNumber = 0;
        this.startPacket = false;
        ghost = false;
        complete = false;
        this.breakPos = null;
        progress = 0.0;
    }

    @Nativeify
    private boolean shouldAbortMining() {
        // 超过最大挖掘次数
        if ((double) this.breakNumber > this.maxBreak.getValue() - 1.0
                && this.maxBreak.getValue() > 0.0
                && !complete) {
            this.abortCurrentMining();
            return true;
        }
        // 不等待且空气
        if (!this.wait.getValue()
                && this.isAir(this.breakPos)
                && !this.instant.getValue()) {
            this.abortCurrentMining();
            return true;
        }
        return false;
    }

    @Nativeify
    private void abortCurrentMining() {
        if (this.breakPos.equals(secondPos)) secondPos = null;
        this.startPacket = false;
        ghost = false;
        complete = false;
        this.breakNumber = 0;
        this.breakPos = null;
    }

    @Nativeify
    private void handleUnbreakable() {
        if (this.unbreakableCancel.getValue()) {
            this.breakPos = null;
            this.startPacket = false;
            ghost = false;
            complete = false;
        }
        this.breakNumber = 0;
    }

    @Nativeify
    private boolean passPreconditionChecks(PlayerEntity player) {
        if (MathHelper.sqrt((float) player.getEyePos().squaredDistanceTo(this.breakPos.toCenterPos())) > this.range.getValue()) return false;
        if (this.usingPause.getValue() && player.isUsingItem()
                && (!this.allowOffhand.getValue() || player.getActiveHand() == Hand.MAIN_HAND)) return false;
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) return false;
        if (this.breakPos.equals(AutoAnchor.INSTANCE.currentPos)
                && BlockUtil.getBlock(this.breakPos) instanceof RespawnAnchorBlock) return false;
        if (!this.hotBar.getValue() && !EntityUtil.inInventory()) return false;
        if (!this.delayTimer.passed((long) this.stopDelay.getValue())) return false;
        return true;
    }

    @Nativeify
    private void executeStopPacket(PlayerEntity player, int slot) {
        if (this.isAir(this.breakPos)) return;
        if (!this.mineTimer.passed((long) this.breakFinalTime)) return;

        if (this.endRotate.getValue() && this.shouldYawStep()
                && !this.faceVector(this.breakPos.toCenterPos().offset(BlockUtil.getClickSide(this.breakPos), 0.5))) {
            return;
        }

        int old = player.getInventory().selectedSlot;
        int targetSlot = slot;
        boolean shouldSwitch;
        if (this.hotBar.getValue()) {
            shouldSwitch = targetSlot != old;
        } else {
            if (targetSlot < 9) targetSlot += 36;
            shouldSwitch = old + 36 != targetSlot;
        }

        if (shouldSwitch) {
            if (this.hotBar.getValue()) {
                InventoryUtil.switchToSlot(targetSlot);
            } else {
                mc.interactionManager.clickSlot(player.currentScreenHandler.syncId, targetSlot, old, SlotActionType.SWAP, player);
            }
        }

        this.doStopPacket(player, old, targetSlot, shouldSwitch);
    }

    @Nativeify
    private void doStopPacket(PlayerEntity player, int old, int finalSlot, boolean shouldSwitch) {
        // 二次旋转校验，失败则回滚槽位
        if (this.endRotate.getValue()
                && !this.faceVector(this.breakPos.toCenterPos().offset(BlockUtil.getClickSide(this.breakPos), 0.5))) {
            if (shouldSwitch) {
                if (this.hotBar.getValue()) {
                    InventoryUtil.switchToSlot(old);
                } else {
                    mc.interactionManager.clickSlot(player.currentScreenHandler.syncId, finalSlot, old, SlotActionType.SWAP, player);
                    EntityUtil.syncInventory();
                }
            }
            return;
        }

        PacketMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(
                PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, this.breakPos,
                BlockUtil.getClickSide(this.breakPos), id));

        if (this.endSwing.getValue()) {
            EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
        }
        if (shouldSwitch) {
            if (this.hotBar.getValue()) {
                InventoryUtil.switchToSlot(old);
            } else {
                mc.interactionManager.clickSlot(player.currentScreenHandler.syncId, finalSlot, old, SlotActionType.SWAP, player);
                EntityUtil.syncInventory();
            }
        }

        ++this.breakNumber;
        this.delayTimer.reset();
        this.startTime.reset();

        if (this.setAir.getValue()) {
            mc.world.setBlockState(this.breakPos, Blocks.AIR.getDefaultState());
        }
        if (this.endRotate.getValue() && !this.shouldYawStep()) {
            Astra.ROTATION.snapBack();
        }
        ghost = true;
    }

    @Nativeify
    private void executeStartPacket(PlayerEntity player, int slot) {
        if (!this.startTime.passed(this.startDelay.getValueInt())) return;
        if (!this.mineAir.getValue() && this.isAir(this.breakPos)) return;

        Direction side = BlockUtil.getClickSide(this.breakPos);
        if (this.rotate.getValue()) {
            Vec3i vec3i = side.getVector();
            if (!this.faceVector(this.breakPos.toCenterPos().add(new Vec3d(
                    vec3i.getX() * 0.5, vec3i.getY() * 0.5, vec3i.getZ() * 0.5)))) {
                return;
            }
        }

        this.mineTimer.reset();
        this.animationTime.reset();

        if (this.swing.getValue()) {
            EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
        }

        if (this.doubleBreak.getValue() && (secondPos == null || this.isAir(secondPos))) {
            double breakTime = this.getBreakTime(this.breakPos, slot, 1.0);
            this.secondAnim.reset();
            this.secondAnim.setLength((long) breakTime);
            this.secondTimer.reset();
            secondPos = this.breakPos;
        }

        PacketMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(
                PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, this.breakPos, side, id));

        if (this.fastBypass.getValue()) {
            this.sendFastBypassPackets();
        }
        if (this.rotate.getValue() && !this.shouldYawStep()) {
            Astra.ROTATION.snapBack();
        }
        this.startTime.reset();
    }

    // ==================== onAttackBlock：调度 ====================
    // ⚠️ @EventListener，不能加 @Nativeify
    @EventListener
    public void onAttackBlock(ClickBlockEvent event) {
        if (PacketMine.nullCheck()) return;
        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return;
        if (player.isCreative()) return;

        event.cancel();
        BlockPos pos = event.getPos();

        if (!this.isValidAttackTarget(player, pos)) return;

        this.cancelPreviousPacket();
        this.resetAttackState(pos);

        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) return;

        Direction side = BlockUtil.getClickSide(this.breakPos);
        if (this.rotate.getValue()) {
            Vec3i vec3i = side.getVector();
            if (!this.faceVector(this.breakPos.toCenterPos().add(new Vec3d(
                    vec3i.getX() * 0.5, vec3i.getY() * 0.5, vec3i.getZ() * 0.5)))) {
                return;
            }
        }

        if (!this.startTime.passed(this.startDelay.getValueInt())) return;
        if (this.swing.getValue()) {
            EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
        }

        this.prepareDoubleBreak(player);

        int slot = this.getTool(this.breakPos);
        if (slot == -1) slot = player.getInventory().selectedSlot;
        this.breakFinalTime = this.getBreakTime(this.breakPos, slot);

        this.sendStartPacket(side);
    }

    // ==================== onAttackBlock 拆分出来的 @Nativeify 方法 ====================

    @Nativeify
    private boolean isValidAttackTarget(PlayerEntity player, BlockPos pos) {
        if (pos.equals(this.breakPos)) return false;
        if (PacketMine.unbreakable(pos)) return false;
        if (BlockUtil.getClickSideStrict(pos) == null) return false;
        if (MathHelper.sqrt((float) player.getEyePos().squaredDistanceTo(pos.toCenterPos())) > this.range.getValue()) return false;
        return true;
    }

    @Nativeify
    private void cancelPreviousPacket() {
        if (this.breakPos != null && this.startPacket) {
            Direction oldSide = BlockUtil.getClickSide(this.breakPos);
            if (oldSide != null) {
                PacketMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, this.breakPos, oldSide, id));
            }
            this.startPacket = false;
            ghost = false;
            complete = false;
        }
    }

    @Nativeify
    private void resetAttackState(BlockPos pos) {
        this.breakPos = pos;
        this.breakNumber = 0;
        this.startPacket = false;
        ghost = false;
        complete = false;
        this.mineTimer.reset();
        this.animationTime.reset();
        this.startTime.reset();
    }

    @Nativeify
    private void prepareDoubleBreak(PlayerEntity player) {
        if (!this.doubleBreak.getValue()) return;
        if (secondPos != null && !this.isAir(secondPos)) return;

        int slot2 = this.getTool(this.breakPos);
        if (slot2 == -1) slot2 = player.getInventory().selectedSlot;

        this.secondFinalTime = this.getBreakTime(this.breakPos, slot2, 1.0);
        this.secondAnim.reset();
        this.secondAnim.setLength((long) this.secondFinalTime);
        this.secondTimer.reset();
        secondPos = this.breakPos;
    }

    @Nativeify
    private void sendStartPacket(Direction side) {
        PacketMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(
                PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, this.breakPos, side, id));

        if (this.fastBypass.getValue()) {
            this.sendFastBypassPackets();
        }
        if (this.rotate.getValue() && !this.shouldYawStep()) {
            Astra.ROTATION.snapBack();
        }
        this.startTime.reset();
    }

    // ==================== 公共 API ====================

    public void mine(BlockPos pos) {
        if (PacketMine.nullCheck()) return;
        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return;
        if (player.isCreative()) {
            mc.interactionManager.attackBlock(pos, BlockUtil.getClickSide(pos));
            return;
        }
        if (this.isOff()) {
            mc.interactionManager.attackBlock(pos, BlockUtil.getClickSide(pos));
            return;
        }
        if (pos.equals(this.breakPos)) return;
        if (PacketMine.unbreakable(pos)) return;
        if (BlockUtil.getClickSideStrict(pos) == null) return;
        if (MathHelper.sqrt((float) player.getEyePos().squaredDistanceTo(pos.toCenterPos())) > this.range.getValue()) return;

        this.cancelPreviousPacket();

        this.breakPos = pos;
        this.breakNumber = 0;
        this.startPacket = false;
        ghost = false;
        complete = false;
        this.mineTimer.reset();
        this.animationTime.reset();
        this.startTime.reset();
    }

    public void stopMining() {
        if (this.breakPos != null && this.startPacket) {
            Direction oldSide = BlockUtil.getClickSide(this.breakPos);
            if (oldSide != null) {
                sendSequencedPacket(id -> new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, this.breakPos, oldSide, id));
            }
            this.startPacket = false;
            ghost = false;
            complete = false;
        }
        this.breakPos = null;
    }

    @Nativeify
    private void sendFastBypassPackets() {
        if (this.breakPos == null) return;
        BlockPos highPos = new BlockPos(this.breakPos.getX(), 400, this.breakPos.getZ());
        for (int i = 0; i < 3; i++) {
            final int heightOffset = i * 10;
            Direction[] directions = Direction.values();
            Direction randomDir = directions[(int) (Math.random() * directions.length)];
            PacketMine.sendSequencedPacket(id -> new PlayerActionC2SPacket(
                    PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, highPos.up(heightOffset), randomDir, id));
        }
    }

    // ==================== 工具方法 ====================

    @Nativeify
    boolean faceVector(Vec3d directionVec) {
        if (!this.shouldYawStep()) {
            Astra.ROTATION.lookAt(directionVec);
            return true;
        }
        this.sync.reset();
        this.directionVec = directionVec;
        if (Astra.ROTATION.inFov(directionVec, this.fov.getValueFloat())) {
            return true;
        }
        return !this.checkFov.getValue();
    }

    @Nativeify
    private static int getEfficiencyLevel(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        if (PacketMine.mc.world == null) return 0;
        RegistryEntry<Enchantment> entry = PacketMine.mc.world.getRegistryManager()
                .get(Enchantments.EFFICIENCY.getRegistryRef())
                .getEntry(Enchantments.EFFICIENCY)
                .orElseThrow();
        return EnchantmentHelper.getLevel(entry, stack);
    }

    // ==================== 渲染 ====================

    // ⚠️ @Override 渲染，每帧调用，不能加 @Nativeify
    @Override
    public void onRender3D(MatrixStack matrixStack) {
        if (mc.player == null || mc.world == null) return;
        if (this.breakPos != null && mc.world.isAir(this.breakPos)) {
            complete = true;
        }
        if (!mc.player.isCreative()) {
            double ease;
            if (secondPos != null) {
                if (this.isAir(secondPos)) {
                    secondPos = null;
                    return;
                }
                if (!this.checkDouble.getValue() || !secondPos.equals(this.breakPos)) {
                    this.secondAnim.setLength((long) this.secondFinalTime);
                    ease = this.secondAnim.ease(this.ease.getValue());
                    if (this.box.getValue()) {
                        Render3DUtil.drawFill(matrixStack, this.getFillBox(secondPos, ease), this.doubleColor.getValue());
                    }
                    if (this.outline.getValue()) {
                        Render3DUtil.drawBox(matrixStack, this.getOutlineBox(secondPos, ease), this.doubleOutlineColor.getValue());
                    }
                }
            }
            if (this.breakPos != null) {
                progress = (double) this.mineTimer.getMs() / this.breakFinalTime;
                this.animationTime.setLength((long) this.breakFinalTime);
                ease = this.animationTime.ease(this.ease.getValue());
                if (PacketMine.unbreakable(this.breakPos)) {
                    if (this.box.getValue()) {
                        Render3DUtil.drawFill(matrixStack, new Box(this.breakPos), this.startColor.getValue());
                    }
                    if (this.outline.getValue()) {
                        Render3DUtil.drawBox(matrixStack, new Box(this.breakPos), this.startOutlineColor.getValue());
                    }
                    return;
                }
                if (this.box.getValue()) {
                    Render3DUtil.drawFill(matrixStack, this.getFillBox(this.breakPos, ease), this.getColor(this.animationTime.ease(this.fadeEase.getValue())));
                }
                if (this.outline.getValue()) {
                    Render3DUtil.drawBox(matrixStack, this.getOutlineBox(this.breakPos, ease), this.getOutlineColor(this.animationTime.ease(this.fadeEase.getValue())));
                }
                if (this.text.getValue()) {
                    if (this.isAir(this.breakPos)) {
                        Render3DUtil.drawText3D("Waiting", this.breakPos.toCenterPos(), -1);
                    } else if ((double) ((int) this.mineTimer.getMs()) < this.breakFinalTime) {
                        Render3DUtil.drawText3D(this.df.format(progress * 100.0) + "%", this.breakPos.toCenterPos(), -1);
                    } else {
                        Render3DUtil.drawText3D("100.0%", this.breakPos.toCenterPos(), -1);
                    }
                }
            } else {
                progress = 0.0;
            }
        } else {
            progress = 0.0;
        }
    }

    @Nativeify
    private Box getFillBox(BlockPos pos, double ease) {
        switch (this.animation.getValue().ordinal()) {
            case 0:
                ease = (1.0 - ease) / 2.0;
                return new Box(pos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease);
            case 1:
                ease = (1.0 - ease) / 2.0;
                return new Box(pos).shrink(ease, 0.0, ease).shrink(-ease, 0.0, -ease);
            case 2:
                return new Box(pos.getX(), pos.getY(), pos.getZ(),
                        pos.getX() + 1, pos.getY() + ease, pos.getZ() + 1);
            case 3:
                return new Box(pos.getX(), pos.getY() + 1 - ease, pos.getZ(),
                        pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            case 4:
                return new Box(pos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease);
            default:
                return new Box(pos);
        }
    }

    @Nativeify
    private Box getOutlineBox(BlockPos pos, double ease) {
        ease = Math.min(ease + this.expandLine.getValue(), 1.0);
        switch (this.animation.getValue().ordinal()) {
            case 0:
                ease = (1.0 - ease) / 2.0;
                return new Box(pos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease);
            case 1:
                ease = (1.0 - ease) / 2.0;
                return new Box(pos).shrink(ease, 0.0, ease).shrink(-ease, 0.0, -ease);
            case 2:
                return new Box(pos.getX(), pos.getY(), pos.getZ(),
                        pos.getX() + 1, pos.getY() + ease, pos.getZ() + 1);
            case 3:
                return new Box(pos.getX(), pos.getY() + 1 - ease, pos.getZ(),
                        pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            case 4:
                return new Box(pos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease);
            default:
                return new Box(pos);
        }
    }

    // ==================== 发包 ====================

    // ⚠️ @EventListener
    @EventListener(priority = -200)
    public void onPacketSend(PacketEvent.Send event) {
        if (PacketMine.nullCheck()) return;
        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return;
        if (player.isCreative()) return;

        if (event.getPacket() instanceof PlayerMoveC2SPacket) {
            this.sendGroundPacket = this.shouldBypassGround();
            if (this.sendGroundPacket) {
                ((IPlayerMoveC2SPacket) event.getPacket()).setOnGround(true);
            }
        } else {
            Packet<?> packet = event.getPacket();
            if (packet instanceof UpdateSelectedSlotC2SPacket packet2) {
                if (packet2.getSelectedSlot() != this.lastSlot) {
                    this.lastSlot = packet2.getSelectedSlot();
                }
            } else {
                packet = event.getPacket();
                if (packet instanceof PlayerActionC2SPacket action) {
                    this.handlePlayerAction(action);
                }
            }
        }
    }

    @Nativeify
    private boolean shouldBypassGround() {
        if (mc.player == null || mc.world == null) return false;
        if (!this.bypassGround.getValue()) return false;
        if (mc.player.isFallFlying()) return false;
        if (this.breakPos == null) return false;
        if (this.isAir(this.breakPos)) return false;
        if (this.bypassTime.getValue() <= 0.0) return false;
        if (MathHelper.sqrt((float) this.breakPos.toCenterPos().squaredDistanceTo(mc.player.getEyePos())) > this.range.getValueFloat() + 2.0f) return false;

        double breakTime = this.breakFinalTime - this.bypassTime.getValue();
        return breakTime <= 0.0 || this.mineTimer.passed((long) breakTime);
    }

    @Nativeify
    private void handlePlayerAction(PlayerActionC2SPacket packet3) {
        if (packet3.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
            if (this.breakPos == null || !packet3.getPos().equals(this.breakPos)) return;
            this.startPacket = true;
        } else if (packet3.getAction() == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
            if (this.breakPos == null || !packet3.getPos().equals(this.breakPos)) return;
            if (!this.instant.getValue()) {
                this.startPacket = false;
                ghost = false;
                complete = false;
            }
        }
    }

    // ==================== 挖掘速度计算 ====================

    public static double getBreakTime(BlockPos pos) {
        if (INSTANCE == null || PacketMine.mc.player == null) return 0.0;
        int slot = INSTANCE.getTool(pos);
        if (slot == -1) {
            slot = PacketMine.mc.player.getInventory().selectedSlot;
        }
        return INSTANCE.getBreakTime(pos, slot);
    }

    double getBreakTime(BlockPos pos, int slot) {
        return this.getBreakTime(pos, slot, this.damage.getValue());
    }

    @Nativeify
    double getBreakTime(BlockPos pos, int slot, double damage) {
        if (mc.player == null || mc.world == null) return 0.0;
        return (double) (1.0f / this.getBlockStrength(pos, mc.player.getInventory().getStack(slot)) / 20.0f * 1000.0f) * damage;
    }

    @Nativeify
    float getBlockStrength(BlockPos position, ItemStack itemStack) {
        if (mc.player == null || mc.world == null) return 0.0f;
        BlockState state = mc.world.getBlockState(position);
        float hardness = state.getHardness(mc.world, position);
        if (hardness < 0.0f) return 0.0f;
        float i = !state.isToolRequired() || itemStack.isSuitableFor(state) ? 30.0f : 100.0f;
        return this.getDigSpeed(state, itemStack) / hardness / i;
    }

    @Nativeify
    float getDigSpeed(BlockState state, ItemStack itemStack) {
        if (mc.player == null || mc.world == null) return 0.0f;

        float digSpeed = this.getDestroySpeed(state, itemStack);
        if (digSpeed > 1.0f) {
            int efficiencyModifier = getEfficiencyLevel(itemStack);
            if (efficiencyModifier > 0 && !itemStack.isEmpty()) {
                digSpeed += (float) (StrictMath.pow(efficiencyModifier, 2.0) + 1.0);
            }
        }

        StatusEffectInstance haste = mc.player.getStatusEffect(StatusEffects.HASTE);
        if (haste != null) {
            digSpeed *= 1.0f + (float) (haste.getAmplifier() + 1) * 0.2f;
        }

        StatusEffectInstance fatigue = mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE);
        if (fatigue != null) {
            digSpeed *= switch (fatigue.getAmplifier()) {
                case 0 -> 0.3f;
                case 1 -> 0.09f;
                case 2 -> 0.0027f;
                default -> 8.1E-4f;
            };
        }

        if (mc.player.isSubmergedInWater()) {
            digSpeed *= (float) mc.player.getAttributeInstance(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED).getValue();
        }

        boolean inWeb = this.checkWeb.getValue()
                && Astra.PLAYER.isInWeb(mc.player)
                && this.breakPos != null
                && mc.world.getBlockState(this.breakPos).getBlock() == Blocks.COBWEB;

        if ((!mc.player.isOnGround() || inWeb)
                && PacketMine.INSTANCE.checkGround.getValue()
                && (Criticals.INSTANCE.mode.is(Criticals.Mode.Ground) && Criticals.INSTANCE.isOn()
                || mc.player.isFallFlying()
                || inWeb)) {
            digSpeed /= 5.0f;
        }
        return Math.max(digSpeed, 0.0f);
    }

    @Nativeify
    float getDestroySpeed(BlockState state, ItemStack itemStack) {
        float destroySpeed = 1.0f;
        if (itemStack != null && !itemStack.isEmpty()) {
            destroySpeed *= itemStack.getMiningSpeedMultiplier(state);
        }
        return destroySpeed;
    }

    // ==================== 渲染颜色 ====================

    @Nativeify
    Color getColor(double quad) {
        int sR = this.startColor.getValue().getRed();
        int sG = this.startColor.getValue().getGreen();
        int sB = this.startColor.getValue().getBlue();
        int sA = this.startColor.getValue().getAlpha();
        int eR = this.endColor.getValue().getRed();
        int eG = this.endColor.getValue().getGreen();
        int eB = this.endColor.getValue().getBlue();
        int eA = this.endColor.getValue().getAlpha();
        return new Color(
                (int) (sR + (double) (eR - sR) * quad),
                (int) (sG + (double) (eG - sG) * quad),
                (int) (sB + (double) (eB - sB) * quad),
                (int) (sA + (double) (eA - sA) * quad));
    }

    @Nativeify
    Color getOutlineColor(double quad) {
        int sR = this.startOutlineColor.getValue().getRed();
        int sG = this.startOutlineColor.getValue().getGreen();
        int sB = this.startOutlineColor.getValue().getBlue();
        int sA = this.startOutlineColor.getValue().getAlpha();
        int eR = this.endOutlineColor.getValue().getRed();
        int eG = this.endOutlineColor.getValue().getGreen();
        int eB = this.endOutlineColor.getValue().getBlue();
        int eA = this.endOutlineColor.getValue().getAlpha();
        return new Color(
                (int) (sR + (double) (eR - sR) * quad),
                (int) (sG + (double) (eG - sG) * quad),
                (int) (sB + (double) (eB - sB) * quad),
                (int) (sA + (double) (eA - sA) * quad));
    }

    // ==================== 工具查找 ====================

    @Nativeify
    int getTool(BlockPos pos) {
        if (mc.player == null || mc.world == null) return -1;

        if (this.hotBar.getValue()) {
            int index = -1;
            float CurrentFastest = 1.0f;
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack == ItemStack.EMPTY) continue;
                float digSpeed = getEfficiencyLevel(stack);
                float destroySpeed = stack.getMiningSpeedMultiplier(mc.world.getBlockState(pos));
                if (digSpeed + destroySpeed <= CurrentFastest) continue;
                CurrentFastest = digSpeed + destroySpeed;
                index = i;
            }
            return index;
        }

        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        float CurrentFastest = 1.0f;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().getItem() instanceof AirBlockItem) continue;
            float digSpeed = getEfficiencyLevel(entry.getValue());
            float destroySpeed = entry.getValue().getMiningSpeedMultiplier(mc.world.getBlockState(pos));
            if (digSpeed + destroySpeed <= CurrentFastest) continue;
            CurrentFastest = digSpeed + destroySpeed;
            slot.set(entry.getKey());
        }
        return slot.get();
    }

    @Nativeify
    private boolean shouldYawStep() {
        if (mc.player == null) return this.yawStep.getValue();
        if (!this.whenElytra.getValue()
                && (mc.player.isFallFlying() || ElytraFly.INSTANCE.isOn() && ElytraFly.INSTANCE.isFallFlying())) {
            return false;
        }
        return this.yawStep.getValue();
    }

    @Nativeify
    boolean isAir(BlockPos breakPos) {
        if (mc.world == null) return true;
        return mc.world.isAir(breakPos) || BlockUtil.getBlock(breakPos) == Blocks.FIRE && BlockUtil.hasCrystal(breakPos);
    }

    @Nativeify
    public static boolean unbreakable(BlockPos blockPos) {
        if (PacketMine.mc.world == null) return false;
        Block block = PacketMine.mc.world.getBlockState(blockPos).getBlock();
        return !(block instanceof AirBlock) && (block.getHardness() == -1.0f || block.getHardness() == 100.0f);
    }

    static {
        progress = 0.0;
        ghost = false;
        complete = false;
    }

    public enum Page {
        General,
        Check,
        Rotation,
        Render
    }

    private enum Animation {
        Center,
        Grow,
        Up,
        Down,
        Oscillation,
        None
    }
}