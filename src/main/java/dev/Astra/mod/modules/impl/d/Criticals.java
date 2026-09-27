package dev.Astra.mod.modules.impl.d;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.asm.accessors.IPlayerMoveC2SPacket;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.e.Blink;
import dev.Astra.mod.modules.impl.e.BowBomb;
import dev.Astra.mod.modules.impl.e.Phase;
import dev.Astra.mod.modules.impl.i.ElytraFly;
import dev.Astra.mod.modules.impl.l.AutoPearl;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import j2cc.Nativeify;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Criticals extends Module {
    public static Criticals INSTANCE;
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<>("Mode", Mode.NCP));
    public final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true, () -> !this.mode.is(Mode.Ground) && !this.mode.is(Mode.Freeze)));
    private final BooleanSetting setOnGround = this.add(new BooleanSetting("SetNoGround", false, () -> this.mode.is(Mode.Ground)));
    private final BooleanSetting blockCheck = this.add(new BooleanSetting("BlockCheck", true, () -> this.mode.is(Mode.Ground)));
    private final BooleanSetting autoJump = this.add(new BooleanSetting("AutoJump", true, () -> this.mode.is(Mode.Ground)).setParent());
    private final BooleanSetting mini = this.add(new BooleanSetting("Mini", true, () -> this.mode.is(Mode.Ground) && this.autoJump.isOpen()));
    private final SliderSetting y = this.add(new SliderSetting("MotionY", 0.05, 0.0, 1.0, 0.001, () -> this.mode.is(Mode.Ground) && this.autoJump.isOpen()).setSuffix("blocks"));
    private final BooleanSetting flight = this.add(new BooleanSetting("Flight", false, () -> this.mode.is(Mode.Ground)));
    private final SliderSetting freezeDelay = this.add(new SliderSetting("FreezeDelay", 10.0, 0.0, 500.0, 1.0, () -> this.mode.is(Mode.Freeze)).setSuffix("ms"));

    // 所有模式通用设置
    private final BooleanSetting pauseOnMove = this.add(new BooleanSetting("PauseOnMove", true));
    private final BooleanSetting crawlPause = this.add(new BooleanSetting("CrawlPause", false));
    private final BooleanSetting onlyInBlock = this.add(new BooleanSetting("OnlyInBlock", true, () -> !this.mode.is(Mode.Ground) && !this.mode.is(Mode.Freeze)));
    private final BooleanSetting headBlock = this.add(new BooleanSetting("HeadBlock", false));

    // Ground 模式专用设置
    private final BooleanSetting onlyAura = this.add(new BooleanSetting("OnlyAura", true, () -> this.mode.is(Mode.Ground)));
    private final BooleanSetting pauseElytraFly = this.add(new BooleanSetting("PauseElytraFly", true, () -> this.mode.is(Mode.Ground)));

    boolean requireJump = false;
    private boolean paused = false;
    private boolean crawlingPaused = false;
    private boolean elytraPaused = false;
    private boolean frozen = false;
    private boolean freezeDelaying = false;
    private long freezeDelayStart = 0;
    private Vec3d freezePos = null;

    public Criticals() {
        super("Criticals", Module.Category.Combat);
        this.setChinese("刀刀暴击");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    // ==================== 卡墙检测（仅用于非 Ground 模式） ====================
    @Nativeify
    private boolean isInBlock() {
        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return false;
        double w = player.getWidth();
        double x = player.getX();
        double z = player.getZ();
        return isCornerStuck(x - w * 0.35, z + w * 0.35) ||
                isCornerStuck(x - w * 0.35, z - w * 0.35) ||
                isCornerStuck(x + w * 0.35, z - w * 0.35) ||
                isCornerStuck(x + w * 0.35, z + w * 0.35);
    }

    @Nativeify
    private boolean isCornerStuck(double x, double z) {
        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return false;

        BlockPos pos = BlockPos.ofFloored(x, player.getY(), z);
        if (wouldCollideAt(pos)) {
            double d = x - pos.getX();
            double e = z - pos.getZ();
            for (Direction dir : new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH}) {
                double g = dir.getAxis().choose(d, 0.0, e);
                double h = dir.getDirection() == Direction.AxisDirection.POSITIVE ? 1.0 - g : g;
                if (h < 0.0 || wouldCollideAt(pos.offset(dir))) continue;
                return true;
            }
        }
        return false;
    }

    @Nativeify
    private boolean wouldCollideAt(BlockPos pos) {
        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return false;

        Box box = player.getBoundingBox();
        Box box2 = new Box(pos.getX(), box.minY, pos.getZ(), pos.getX() + 1.0, box.maxY, pos.getZ() + 1.0).contract(1.0E-7);
        return mc.world.canCollide(player, box2);
    }

    // ==================== 头顶方块检测（检测 0.3 格上方的实体方块） ====================
    @Nativeify
    private boolean hasHeadBlock() {
        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return false;
        BlockPos headPos = BlockPos.ofFloored(
                player.getX(),
                player.getY() + player.getHeight() + 0.3,
                player.getZ()
        );
        return !mc.world.getBlockState(headPos).getCollisionShape(mc.world, headPos).isEmpty();
    }

    // ==================== Aura 目标检查 ====================
    private boolean isAuraTargetingPlayer() {
        return Aura.target instanceof PlayerEntity;
    }

    // ==================== Ground 模式总条件 ====================
    private boolean shouldGroundActivate() {
        if (!this.mode.is(Mode.Ground)) return false;
        if (this.paused || this.crawlingPaused || this.elytraPaused) return false;
        if (this.onlyAura.getValue() && !isAuraTargetingPlayer()) return false;
        if (this.pauseElytraFly.getValue() && ElytraFly.INSTANCE != null && ElytraFly.INSTANCE.isOn()) return false;
        if (this.headBlock.getValue() && !hasHeadBlock()) return false;
        return true;
    }

    // ⚠️ @EventListener，不能加 @Nativeify
    @EventListener
    public void onPacketSend(PacketEvent.Send event) {
        if (event.isCancelled()) return;
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) return;

        PlayerEntity player = mc.player;
        if (player == null || mc.world == null) return;

        if (this.mode.is(Mode.Freeze) && this.frozen) {
            if (event.getPacket() instanceof PlayerMoveC2SPacket) {
                event.cancel();
            }
            return;
        }

        if (!this.mode.is(Mode.Ground) && !this.mode.is(Mode.Freeze)) {
            boolean checkInBlock = this.onlyInBlock.getValue();
            boolean checkHead = this.headBlock.getValue();
            if (checkInBlock || checkHead) {
                boolean inBlock = isInBlock();
                boolean headBlocked = hasHeadBlock();
                if (!((checkInBlock && inBlock) || (checkHead && headBlocked))) {
                    return;
                }
            }
            if (this.paused || this.crawlingPaused) return;
        } else {
            if (!this.shouldGroundActivate()) return;
        }

        if (this.mode.is(Mode.Ground)) {
            if (BowBomb.send) return;
            if (AutoPearl.throwing || Phase.INSTANCE.isOn()) return;
            if (!this.setOnGround.getValue()) return;
            if (event.getPacket() instanceof PlayerMoveC2SPacket) {
                ((IPlayerMoveC2SPacket) event.getPacket()).setOnGround(false);
            }
            return;
        }

        Packet<?> packet2 = event.getPacket();
        if (packet2 instanceof PlayerInteractEntityC2SPacket packet) {
            if (Criticals.getInteractType(packet) == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
                Entity entity = Criticals.getEntity(packet);
                if (!(entity instanceof EndCrystalEntity) &&
                        (!this.onlyGround.getValue() || player.isOnGround() || player.getAbilities().flying) &&
                        !player.isInLava() && !player.isTouchingWater() && entity != null) {
                    player.addCritParticles(entity);
                    this.doCrit();
                }
            }
        }
    }

    @Override
    public void onEnable() {
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) return;
        this.requireJump = true;
        this.paused = false;
        this.crawlingPaused = false;
        this.elytraPaused = false;

        if (this.mode.is(Mode.Freeze)) {
            if (nullCheck()) return;
            PlayerEntity player = mc.player;
            if (player == null) return;
            this.freezePos = player.getPos();
            player.jump();
        } else {
            if (this.mode.is(Mode.Ground)) {
                if (nullCheck()) return;
                PlayerEntity player = mc.player;
                if (player == null) return;
                if (this.flight.getValue()) {
                    this.freezePos = player.getPos();
                }
                if (!this.shouldGroundActivate()) return;
                if (player.isOnGround() && this.autoJump.getValue() &&
                        (!this.blockCheck.getValue() || BlockUtil.canCollide(player, new Box(EntityUtil.getPlayerPos(true).up(2))))) {
                    this.jump();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        this.frozen = false;
        this.freezeDelaying = false;
        this.freezeDelayStart = 0;
        this.freezePos = null;
    }

    public void jump() {
        PlayerEntity player = mc.player;
        if (player == null) return;
        if (this.mini.getValue()) {
            MovementUtil.setMotionY(this.y.getValue());
        } else {
            player.jump();
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) return;

        PlayerEntity player = mc.player;
        if (player == null) return;

        // 1. 更新移动暂停标志
        boolean isMoving = MovementUtil.isMoving();
        if (this.pauseOnMove.getValue()) {
            if (isMoving && !this.paused) {
                this.paused = true;
                this.requireJump = false;
            } else if (!isMoving && this.paused) {
                this.paused = false;
                if (this.mode.is(Mode.Ground) && !this.crawlingPaused && !this.elytraPaused &&
                        (!this.onlyAura.getValue() || isAuraTargetingPlayer())) {
                    this.requireJump = true;
                    if (player.isOnGround() && this.autoJump.getValue() &&
                            (!this.blockCheck.getValue() || BlockUtil.canCollide(player, new Box(EntityUtil.getPlayerPos(true).up(2))))) {
                        this.jump();
                        this.requireJump = false;
                    }
                }
            }
        }

        // 2. 更新爬行暂停标志
        boolean isCrawling = player.isCrawling();
        if (this.crawlPause.getValue()) {
            if (isCrawling && !this.crawlingPaused) {
                this.crawlingPaused = true;
                this.requireJump = false;
            } else if (!isCrawling && this.crawlingPaused) {
                this.crawlingPaused = false;
                if (this.mode.is(Mode.Ground) && !this.paused && !this.elytraPaused &&
                        (!this.onlyAura.getValue() || isAuraTargetingPlayer())) {
                    this.requireJump = true;
                    if (player.isOnGround() && this.autoJump.getValue() &&
                            (!this.blockCheck.getValue() || BlockUtil.canCollide(player, new Box(EntityUtil.getPlayerPos(true).up(2))))) {
                        this.jump();
                        this.requireJump = false;
                    }
                }
            }
        }

        // 3. 更新滑翔翼暂停标志
        boolean isElytraFlying = (ElytraFly.INSTANCE != null && ElytraFly.INSTANCE.isOn());
        if (this.pauseElytraFly.getValue()) {
            if (isElytraFlying && !this.elytraPaused) {
                this.elytraPaused = true;
                this.requireJump = false;
            } else if (!isElytraFlying && this.elytraPaused) {
                this.elytraPaused = false;
                if (this.mode.is(Mode.Ground) && !this.paused && !this.crawlingPaused &&
                        (!this.onlyAura.getValue() || isAuraTargetingPlayer())) {
                    this.requireJump = true;
                    if (player.isOnGround() && this.autoJump.getValue() &&
                            (!this.blockCheck.getValue() || BlockUtil.canCollide(player, new Box(EntityUtil.getPlayerPos(true).up(2))))) {
                        this.jump();
                        this.requireJump = false;
                    }
                }
            }
        }

        // 非 Ground 模式
        if (!this.mode.is(Mode.Ground)) {
            if (this.mode.is(Mode.Freeze)) {
                if (!this.frozen) {
                    if (player.getVelocity().y < 0.0) {
                        if (!this.freezeDelaying) {
                            this.freezeDelaying = true;
                            this.freezeDelayStart = System.currentTimeMillis();
                        } else if (System.currentTimeMillis() - this.freezeDelayStart >= (long) this.freezeDelay.getValue()) {
                            this.frozen = true;
                            this.freezeDelaying = false;
                        }
                    }
                } else {
                    if (MovementUtil.isMoving()) {
                        this.toggle();
                        return;
                    }
                    player.setVelocity(0.0, 0.0, 0.0);
                    if (this.freezePos != null) {
                        player.setPosition(this.freezePos.x, this.freezePos.y, this.freezePos.z);
                    }
                }
            }
            return;
        }

        // Ground 模式自动跳跃逻辑
        if (!this.shouldGroundActivate()) return;

        if (this.flight.getValue() && player.fallDistance > 0.0f) {
            MovementUtil.setMotionY(0.0);
            MovementUtil.setMotionX(0.0);
            MovementUtil.setMotionZ(0.0);
            this.requireJump = false;
        } else if (this.blockCheck.getValue() && !BlockUtil.canCollide(player, new Box(EntityUtil.getPlayerPos(true).up(2)))) {
            this.requireJump = true;
        } else if (player.isOnGround() && this.autoJump.getValue() && (this.flight.getValue() || this.requireJump)) {
            this.jump();
            this.requireJump = false;
        }
    }

    @Nativeify
    public void doCrit() {
        PlayerEntity player = mc.player;
        if (player == null || mc.getNetworkHandler() == null) return;

        if (this.mode.getValue() == Mode.NCP) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX(), player.getY() + 2.71875E-7, player.getZ(), false));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX(), player.getY(), player.getZ(), false));
        } else if (this.mode.getValue() == Mode.GrimV3) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY(), player.getZ(),
                    Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch(), true));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY() + 0.0625, player.getZ(),
                    Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch(), false));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY() + 0.04535F, player.getZ(),
                    Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch(), false));
        }
    }

    // ⚠️ 不加 @Nativeify：只 1 行，太短
    public static Entity getEntity(PlayerInteractEntityC2SPacket packet) {
        return mc.world == null ? null : mc.world.getEntityById(((dev.Astra.asm.accessors.IPlayerInteractEntityC2SPacket) packet).getEntityId());
    }

    // ⚠️ 不加 @Nativeify：含匿名内部类，J2CC 不支持
    public static PlayerInteractEntityC2SPacket.InteractType getInteractType(PlayerInteractEntityC2SPacket packet) {
        final PlayerInteractEntityC2SPacket.InteractType[] result = new PlayerInteractEntityC2SPacket.InteractType[1];
        packet.handle(new PlayerInteractEntityC2SPacket.Handler() {
            @Override
            public void interact(net.minecraft.util.Hand hand) {
                result[0] = PlayerInteractEntityC2SPacket.InteractType.INTERACT;
            }

            @Override
            public void interactAt(net.minecraft.util.Hand hand, net.minecraft.util.math.Vec3d pos) {
                result[0] = PlayerInteractEntityC2SPacket.InteractType.INTERACT_AT;
            }

            @Override
            public void attack() {
                result[0] = PlayerInteractEntityC2SPacket.InteractType.ATTACK;
            }
        });
        return result[0];
    }

    public enum Mode {
        NCP,
        GrimV3,
        Ground,
        Freeze
    }
}