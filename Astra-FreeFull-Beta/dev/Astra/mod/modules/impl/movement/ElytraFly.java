package dev.Astra.mod.modules.impl.movement;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.ElytraUpdateEvent;
import dev.Astra.api.events.impl.MoveEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.TravelEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.events.impl.UpdateRotateEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.asm.accessors.IFireworkRocketEntity;
import dev.Astra.asm.accessors.ILivingEntity;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1313;
import net.minecraft.class_1671;
import net.minecraft.class_1713;
import net.minecraft.class_1770;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import net.minecraft.class_2848;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_746;
import net.minecraft.class_2848.class_2849;

public class ElytraFly extends Module {
   public static ElytraFly INSTANCE;
   public final EnumSetting<Mode> mode;
   public final BooleanSetting packet;
   private final SliderSetting packetDelay;
   private final BooleanSetting setFlag;
   private final BooleanSetting firework;
   public final BindSetting fireWork;
   public final BooleanSetting packetInteract;
   public final BooleanSetting inventory;
   public final BooleanSetting onlyOne;
   private final BooleanSetting usingPause;
   public final BooleanSetting autoJump;
   public final SliderSetting upPitch;
   public final SliderSetting upFactor;
   public final SliderSetting downFactor;
   public final SliderSetting speed;
   public final BooleanSetting speedLimit;
   public final SliderSetting maxSpeed;
   public final BooleanSetting noDrag;
   public final Timer fireworkTimer;
   private final BooleanSetting autoStop;
   private final BooleanSetting sprint;
   private final SliderSetting pitch;
   private final BooleanSetting instantFly;
   private final BooleanSetting checkSpeed;
   public final SliderSetting minSpeed;
   private final SliderSetting delay;
   private final SliderSetting timeout;
   private final SliderSetting sneakDownSpeed;
   private final SliderSetting boost;
   private final BooleanSetting freeze;
   private final BooleanSetting motionStop;
   private final SliderSetting infiniteMaxSpeed;
   private final SliderSetting infiniteMinSpeed;
   private final SliderSetting infiniteMaxHeight;
   public final BooleanSetting releaseSneak;
   private final Timer instantFlyTimer;
   boolean prev;
   float prePitch;
   private boolean hasElytra;
   float yaw;
   float rotationPitch;
   boolean flying;
   int packetDelayInt;
   private boolean down;
   private float lastInfinitePitch;
   private float infinitePitch;
   private final FireWorkTweak fireWorkTweak;

   public ElytraFly() {
      super("ElytraFly", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", ElytraFly.Mode.Control));
      this.packet = this.add((new BooleanSetting("Packet", false)).setParent());
      BooleanSetting var10009 = this.packet;
      Objects.requireNonNull(var10009);
      this.packetDelay = this.add(new SliderSetting("PacketDelay", (double)0.0F, (double)0.0F, (double)20.0F, (double)1.0F, var10009::isOpen));
      this.setFlag = this.add(new BooleanSetting("SetFlag", false, () -> !this.mode.is(ElytraFly.Mode.Bounce)));
      this.firework = this.add((new BooleanSetting("Firework", false)).setParent());
      BooleanSetting var10006 = this.firework;
      Objects.requireNonNull(var10006);
      this.fireWork = this.add(new BindSetting("FireWorkBind", -1, var10006::isOpen));
      var10006 = this.firework;
      Objects.requireNonNull(var10006);
      this.packetInteract = this.add(new BooleanSetting("PacketInteract", true, var10006::isOpen));
      var10006 = this.firework;
      Objects.requireNonNull(var10006);
      this.inventory = this.add(new BooleanSetting("InventorySwap", true, var10006::isOpen));
      var10006 = this.firework;
      Objects.requireNonNull(var10006);
      this.onlyOne = this.add(new BooleanSetting("OnlyOne", true, var10006::isOpen));
      var10006 = this.firework;
      Objects.requireNonNull(var10006);
      this.usingPause = this.add(new BooleanSetting("UsingPause", true, var10006::isOpen));
      this.autoJump = this.add(new BooleanSetting("AutoJump", true, () -> this.mode.is(ElytraFly.Mode.Bounce)));
      this.upPitch = this.add(new SliderSetting("UpPitch", (double)0.0F, (double)0.0F, (double)90.0F, () -> this.mode.getValue() == ElytraFly.Mode.Control));
      this.upFactor = this.add(new SliderSetting("UpFactor", (double)1.0F, (double)0.0F, (double)10.0F, () -> this.mode.getValue() == ElytraFly.Mode.Control));
      this.downFactor = this.add(new SliderSetting("FallSpeed", (double)1.0F, (double)0.0F, (double)10.0F, () -> this.mode.getValue() == ElytraFly.Mode.Control));
      this.speed = this.add(new SliderSetting("Speed", (double)1.0F, 0.1, (double)10.0F, () -> this.mode.getValue() == ElytraFly.Mode.Control));
      this.speedLimit = this.add(new BooleanSetting("SpeedLimit", true, () -> this.mode.getValue() == ElytraFly.Mode.Control));
      this.maxSpeed = this.add(new SliderSetting("MaxSpeed", (double)2.5F, 0.1, (double)10.0F, () -> this.speedLimit.getValue() && this.mode.getValue() == ElytraFly.Mode.Control));
      this.noDrag = this.add(new BooleanSetting("NoDrag", false, () -> this.mode.getValue() == ElytraFly.Mode.Control));
      this.fireworkTimer = new Timer();
      this.autoStop = this.add(new BooleanSetting("AutoStop", true));
      this.sprint = this.add(new BooleanSetting("Sprint", true, () -> this.mode.is(ElytraFly.Mode.Bounce)));
      this.pitch = this.add(new SliderSetting("Pitch", (double)88.0F, (double)-90.0F, (double)90.0F, 0.1, () -> this.mode.is(ElytraFly.Mode.Bounce)));
      this.instantFly = this.add(new BooleanSetting("AutoStart", true, () -> !this.mode.is(ElytraFly.Mode.Bounce)));
      this.checkSpeed = this.add(new BooleanSetting("CheckSpeed", false, () -> !this.mode.is(ElytraFly.Mode.Bounce)));
      this.minSpeed = this.add(new SliderSetting("MinSpeed", (double)70.0F, 0.1, (double)200.0F, () -> !this.mode.is(ElytraFly.Mode.Bounce)));
      this.delay = this.add(new SliderSetting("Delay", (double)1000.0F, (double)0.0F, (double)20000.0F, (double)50.0F, () -> !this.mode.is(ElytraFly.Mode.Bounce)));
      this.timeout = this.add(new SliderSetting("Timeout", (double)0.0F, 0.1, (double)1.0F, 0.1, () -> !this.mode.is(ElytraFly.Mode.Bounce)));
      this.sneakDownSpeed = this.add(new SliderSetting("DownSpeed", (double)1.0F, 0.1, (double)10.0F, () -> this.mode.getValue() == ElytraFly.Mode.Control));
      this.boost = this.add(new SliderSetting("Boost", (double)1.0F, 0.1, (double)4.0F, () -> this.mode.getValue() == ElytraFly.Mode.Boost));
      this.freeze = this.add(new BooleanSetting("Freeze", false, () -> this.mode.is(ElytraFly.Mode.Rotation)));
      this.motionStop = this.add(new BooleanSetting("MotionStop", false, () -> this.mode.is(ElytraFly.Mode.Rotation)));
      this.infiniteMaxSpeed = this.add(new SliderSetting("InfiniteMaxSpeed", (double)150.0F, (double)50.0F, (double)170.0F, () -> this.mode.getValue() == ElytraFly.Mode.Pitch));
      this.infiniteMinSpeed = this.add(new SliderSetting("InfiniteMinSpeed", (double)25.0F, (double)10.0F, (double)70.0F, () -> this.mode.getValue() == ElytraFly.Mode.Pitch));
      this.infiniteMaxHeight = this.add(new SliderSetting("InfiniteMaxHeight", 200, -50, 360, () -> this.mode.getValue() == ElytraFly.Mode.Pitch));
      this.releaseSneak = this.add(new BooleanSetting("ReleaseSneak", false));
      this.instantFlyTimer = new Timer();
      this.hasElytra = false;
      this.yaw = 0.0F;
      this.rotationPitch = 0.0F;
      this.flying = false;
      this.packetDelayInt = 0;
      this.setChinese("鞘翅飞行");
      INSTANCE = this;
      this.fireWorkTweak = new FireWorkTweak();
      Astra.EVENT_BUS.subscribe(this.fireWorkTweak);
   }

   public void off() {
      if (!this.inventory.getValue() || EntityUtil.inInventory()) {
         if (this.onlyOne.getValue()) {
            for(class_1297 entity : Astra.THREAD.getEntities()) {
               if (entity instanceof class_1671 && ((IFireworkRocketEntity)entity).getShooter() == mc.field_1724) {
                  return;
               }
            }
         }

         INSTANCE.fireworkTimer.reset();
         if (mc.field_1724.method_6047().method_7909() == class_1802.field_8639) {
            if (this.packetInteract.getValue()) {
               sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
            } else {
               mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
            }
         } else {
            int firework;
            if (this.inventory.getValue() && (firework = InventoryUtil.findItemInventorySlot(class_1802.field_8639)) != -1) {
               InventoryUtil.inventorySwap(firework, mc.field_1724.method_31548().field_7545);
               if (this.packetInteract.getValue()) {
                  sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
               } else {
                  mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
               }

               InventoryUtil.inventorySwap(firework, mc.field_1724.method_31548().field_7545);
               EntityUtil.syncInventory();
            } else {
               firework = InventoryUtil.findItem(class_1802.field_8639);
               if (firework != -1) {
                  int old = mc.field_1724.method_31548().field_7545;
                  InventoryUtil.switchToSlot(firework);
                  if (this.packetInteract.getValue()) {
                     sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
                  } else {
                     mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
                  }

                  InventoryUtil.switchToSlot(old);
               }
            }
         }

      }
   }

   public static boolean recastElytra(class_746 player) {
      if (checkConditions(player) && ignoreGround(player)) {
         player.field_3944.method_52787(new class_2848(player, class_2849.field_12982));
         if (INSTANCE.setFlag.getValue()) {
            mc.field_1724.method_23669();
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean checkConditions(class_746 player) {
      class_1799 itemStack = player.method_6118(class_1304.field_6174);
      return !player.method_31549().field_7479 && !player.method_5765() && !player.method_6101() && itemStack.method_31574(class_1802.field_8833) && class_1770.method_7804(itemStack);
   }

   private static boolean ignoreGround(class_746 player) {
      if (!player.method_5799() && !player.method_6059(class_1294.field_5902)) {
         class_1799 itemStack = player.method_6118(class_1304.field_6174);
         if (itemStack.method_31574(class_1802.field_8833) && class_1770.method_7804(itemStack)) {
            player.method_23669();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   public void onEnable() {
      if (!nullCheck()) {
         this.hasElytra = false;
         this.yaw = mc.field_1724.method_36454();
         this.rotationPitch = mc.field_1724.method_36455();
      }
   }

   public void onDisable() {
      if (!nullCheck()) {
         if (this.releaseSneak.getValue()) {
            mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12984));
         }

         Astra.EVENT_BUS.unsubscribe(this.fireWorkTweak);
      }
   }

   private void boost() {
      if (this.hasElytra) {
         if (!this.isFallFlying()) {
            return;
         }

         float yaw = (float)Math.toRadians((double)mc.field_1724.method_36454());
         if (mc.field_1690.field_1894.method_1434()) {
            mc.field_1724.method_5762((double)(-class_3532.method_15374(yaw) * this.boost.getValueFloat() / 10.0F), (double)0.0F, (double)(class_3532.method_15362(yaw) * this.boost.getValueFloat() / 10.0F));
         }
      }

   }

   @EventListener(
      priority = -9999
   )
   public void onRotation(UpdateRotateEvent event) {
      if (!nullCheck()) {
         if (this.mode.is(ElytraFly.Mode.Rotation)) {
            if (this.isFallFlying()) {
               if (MovementUtil.isMoving()) {
                  if (mc.field_1690.field_1903.method_1434()) {
                     this.rotationPitch = -45.0F;
                  } else if (mc.field_1690.field_1832.method_1434()) {
                     this.rotationPitch = 45.0F;
                  } else {
                     this.rotationPitch = -1.9F;
                     if (this.motionStop.getValue()) {
                        this.setY((double)0.0F);
                     }
                  }
               } else if (mc.field_1690.field_1903.method_1434()) {
                  this.rotationPitch = -89.0F;
               } else if (mc.field_1690.field_1832.method_1434()) {
                  this.rotationPitch = 89.0F;
               } else if (this.motionStop.getValue()) {
                  this.setY((double)0.0F);
               }

               if (!mc.field_1690.field_1894.method_1434() && !mc.field_1690.field_1881.method_1434() && !mc.field_1690.field_1913.method_1434() && !mc.field_1690.field_1849.method_1434()) {
                  if (this.motionStop.getValue()) {
                     this.setX((double)0.0F);
                     this.setZ((double)0.0F);
                  }
               } else {
                  this.yaw = Sprint.getSprintYaw(mc.field_1724.method_36454());
               }

               event.setYaw(this.yaw);
               event.setPitch(this.rotationPitch);
            }
         } else if (this.mode.is(ElytraFly.Mode.Pitch)) {
            if (this.isFallFlying()) {
               event.setPitch(this.infinitePitch);
            }
         } else if (this.mode.is(ElytraFly.Mode.Bounce) && this.isFallFlying()) {
            event.setPitch(this.pitch.getValueFloat());
         }

      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.getInfinitePitch();
      this.flying = false;
      if (this.packet.getValue()) {
         this.hasElytra = InventoryUtil.findItemInventorySlot(class_1802.field_8833) != -1;
      } else {
         this.hasElytra = false;

         for(class_1799 is : mc.field_1724.method_5661()) {
            if (is.method_7909() instanceof class_1770) {
               this.hasElytra = true;
               break;
            }
         }

         if (this.mode.is(ElytraFly.Mode.Bounce)) {
            ((ILivingEntity)mc.field_1724).setLastJumpCooldown(0);
            return;
         }
      }

      double x = mc.field_1724.method_23317() - mc.field_1724.field_6014;
      double y = mc.field_1724.method_23318() - mc.field_1724.field_6036;
      double z = mc.field_1724.method_23321() - mc.field_1724.field_5969;
      double dist = Math.sqrt(x * x + z * z + y * y) / (double)1000.0F;
      double div = 1.388888888888889E-5;
      float timer = Astra.TIMER.get();
      double speed = dist / div * (double)timer;
      if (this.mode.getValue() == ElytraFly.Mode.Boost) {
         this.boost();
      }

      if (this.packet.getValue()) {
         if (!mc.field_1724.method_24828()) {
            ++this.packetDelayInt;
            if (!((double)this.packetDelayInt <= this.packetDelay.getValue())) {
               int elytra = InventoryUtil.findItem(class_1802.field_8833);
               if (elytra != -1) {
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 6, elytra, class_1713.field_7791, mc.field_1724);
                  mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
                  mc.field_1724.method_23669();
                  if ((!this.checkSpeed.getValue() || speed <= this.minSpeed.getValue()) && this.firework.getValue() && this.fireworkTimer.passed((long)this.delay.getValueInt()) && (MovementUtil.isMoving() || this.mode.is(ElytraFly.Mode.Rotation) && mc.field_1690.field_1903.method_1434()) && (!mc.field_1724.method_6115() || !this.usingPause.getValue()) && this.isFallFlying()) {
                     this.off();
                     this.fireworkTimer.reset();
                  }

                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 6, elytra, class_1713.field_7791, mc.field_1724);
                  this.packetDelayInt = 0;
               } else {
                  elytra = InventoryUtil.findItemInventorySlot(class_1802.field_8833);
                  if (elytra != -1) {
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, elytra, 0, class_1713.field_7790, mc.field_1724);
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 6, 0, class_1713.field_7790, mc.field_1724);
                     mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
                     mc.field_1724.method_23669();
                     if ((!this.checkSpeed.getValue() || speed <= this.minSpeed.getValue()) && this.firework.getValue() && this.fireworkTimer.passed((long)this.delay.getValueInt()) && (MovementUtil.isMoving() || this.mode.is(ElytraFly.Mode.Rotation) && mc.field_1690.field_1903.method_1434()) && (!mc.field_1724.method_6115() || !this.usingPause.getValue()) && this.isFallFlying()) {
                        this.off();
                        this.fireworkTimer.reset();
                     }

                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 6, 0, class_1713.field_7790, mc.field_1724);
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, elytra, 0, class_1713.field_7790, mc.field_1724);
                     this.packetDelayInt = 0;
                  }
               }

            }
         }
      } else {
         if ((!this.checkSpeed.getValue() || speed <= this.minSpeed.getValue()) && this.firework.getValue() && this.fireworkTimer.passed((long)this.delay.getValueInt()) && (MovementUtil.isMoving() || this.mode.is(ElytraFly.Mode.Rotation) && mc.field_1690.field_1903.method_1434()) && (!mc.field_1724.method_6115() || !this.usingPause.getValue()) && this.isFallFlying()) {
            this.off();
            this.fireworkTimer.reset();
         }

         if (!this.isFallFlying() && this.hasElytra) {
            this.fireworkTimer.setMs(99999999L);
            if (!mc.field_1724.method_24828() && this.instantFly.getValue() && mc.field_1724.method_18798().method_10214() < (double)0.0F) {
               if (!this.instantFlyTimer.passed((long)((double)1000.0F * this.timeout.getValue()))) {
                  return;
               }

               this.instantFlyTimer.reset();
               mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
               if (this.setFlag.getValue()) {
                  mc.field_1724.method_23669();
               }
            }
         }

      }
   }

   protected final class_243 getRotationVector(float pitch, float yaw) {
      float f = pitch * ((float)Math.PI / 180F);
      float g = -yaw * ((float)Math.PI / 180F);
      float h = class_3532.method_15362(g);
      float i = class_3532.method_15374(g);
      float j = class_3532.method_15362(f);
      float k = class_3532.method_15374(f);
      return new class_243((double)(i * j), (double)(-k), (double)(h * j));
   }

   public class_243 getRotationVec(float tickDelta) {
      return this.getRotationVector(-this.upPitch.getValueFloat(), mc.field_1724.method_5705(tickDelta));
   }

   @EventListener
   private void onPlayerMove(MoveEvent event) {
      if (this.autoStop.getValue() && this.isFallFlying()) {
         int chunkX = (int)(mc.field_1724.method_23317() / (double)16.0F);
         int chunkZ = (int)(mc.field_1724.method_23321() / (double)16.0F);
         if (!mc.field_1687.method_2935().method_12123(chunkX, chunkZ)) {
            event.cancel();
         }
      }

   }

   @EventListener
   private void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (this.mode.is(ElytraFly.Mode.Bounce) && this.hasElytra) {
            if (this.autoJump.getValue()) {
               mc.field_1690.field_1903.method_23481(true);
            }

            if (event.isPost()) {
               if (!this.isFallFlying()) {
                  mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
               }

               if (checkConditions(mc.field_1724) && !this.sprint.getValue()) {
                  if (this.isFallFlying()) {
                     mc.field_1724.method_5728(mc.field_1724.method_24828());
                  } else {
                     mc.field_1724.method_5728(true);
                  }
               }
            } else if (checkConditions(mc.field_1724) && this.sprint.getValue()) {
               mc.field_1724.method_5728(true);
            }
         }

      }
   }

   @EventListener
   private void onPacketSend(PacketEvent.Send event) {
      if (!nullCheck()) {
         if (this.mode.is(ElytraFly.Mode.Bounce) && this.hasElytra && event.getPacket() instanceof class_2848 && ((class_2848)event.getPacket()).method_12365().equals(class_2849.field_12982) && !this.sprint.getValue()) {
            mc.field_1724.method_5728(true);
         }

      }
   }

   @EventListener
   private void onPacketReceive(PacketEvent.Receive event) {
      if (!nullCheck()) {
         if (this.mode.is(ElytraFly.Mode.Bounce) && this.hasElytra && event.getPacket() instanceof class_2708) {
            mc.field_1724.method_23670();
         }

      }
   }

   @EventListener
   public void travel(TravelEvent event) {
      if (!nullCheck()) {
         if (!AntiCheat.INSTANCE.movementSync()) {
            if (this.mode.is(ElytraFly.Mode.Bounce) && this.hasElytra) {
               if (event.isPre()) {
                  this.prev = true;
                  this.prePitch = mc.field_1724.method_36455();
                  mc.field_1724.method_36457(this.pitch.getValueFloat());
               } else if (this.prev) {
                  this.prev = false;
                  mc.field_1724.method_36457(this.prePitch);
               }
            } else if (this.mode.is(ElytraFly.Mode.Pitch) && this.isFallFlying()) {
               if (event.isPre()) {
                  this.prev = true;
                  this.prePitch = mc.field_1724.method_36455();
                  mc.field_1724.method_36457(this.lastInfinitePitch);
               } else if (this.prev) {
                  this.prev = false;
                  mc.field_1724.method_36457(this.prePitch);
               }
            }
         }

      }
   }

   @EventListener
   public void onMove(TravelEvent event) {
      if (!nullCheck() && this.hasElytra && this.isFallFlying() && !event.isPost()) {
         if ((this.mode.is(ElytraFly.Mode.Freeze) || this.mode.is(ElytraFly.Mode.Rotation) && this.freeze.getValue()) && !MovementUtil.isMoving() && !mc.field_1690.field_1903.method_1434() && !mc.field_1690.field_1832.method_1434()) {
            event.cancel();
         } else {
            if (this.mode.getValue() == ElytraFly.Mode.Control) {
               if (this.firework.getValue()) {
                  if (mc.field_1690.field_1832.method_1434() && mc.field_1724.field_3913.field_3904) {
                     this.setY((double)0.0F);
                  } else if (mc.field_1690.field_1832.method_1434()) {
                     this.setY(-this.sneakDownSpeed.getValue());
                  } else if (mc.field_1724.field_3913.field_3904) {
                     this.setY(this.upFactor.getValue());
                  } else {
                     this.setY(-3.0E-11 * this.downFactor.getValue());
                  }

                  double[] dir = MovementUtil.directionSpeed(this.speed.getValue());
                  this.setX(dir[0]);
                  this.setZ(dir[1]);
               } else {
                  class_243 lookVec = this.getRotationVec(mc.method_60646().method_60637(true));
                  double lookDist = Math.sqrt(lookVec.field_1352 * lookVec.field_1352 + lookVec.field_1350 * lookVec.field_1350);
                  double motionDist = Math.sqrt(this.getX() * this.getX() + this.getZ() * this.getZ());
                  if (mc.field_1724.field_3913.field_3903) {
                     this.setY(-this.sneakDownSpeed.getValue());
                  } else if (!mc.field_1724.field_3913.field_3904) {
                     this.setY(-3.0E-11 * this.downFactor.getValue());
                  }

                  if (mc.field_1724.field_3913.field_3904) {
                     if (motionDist > this.upFactor.getValue() / this.upFactor.getMax()) {
                        double rawUpSpeed = motionDist * 0.01325;
                        this.setY(this.getY() + rawUpSpeed * 3.2);
                        this.setX(this.getX() - lookVec.field_1352 * rawUpSpeed / lookDist);
                        this.setZ(this.getZ() - lookVec.field_1350 * rawUpSpeed / lookDist);
                     } else {
                        double[] dir = MovementUtil.directionSpeed(this.speed.getValue());
                        this.setX(dir[0]);
                        this.setZ(dir[1]);
                     }
                  }

                  if (lookDist > (double)0.0F) {
                     this.setX(this.getX() + (lookVec.field_1352 / lookDist * motionDist - this.getX()) * 0.1);
                     this.setZ(this.getZ() + (lookVec.field_1350 / lookDist * motionDist - this.getZ()) * 0.1);
                  }

                  if (!mc.field_1724.field_3913.field_3904) {
                     double[] dir = MovementUtil.directionSpeed(this.speed.getValue());
                     this.setX(dir[0]);
                     this.setZ(dir[1]);
                  }

                  if (!this.noDrag.getValue()) {
                     this.setY(this.getY() * (double)0.99F);
                     this.setX(this.getX() * (double)0.98F);
                     this.setZ(this.getZ() * (double)0.99F);
                  }

                  double finalDist = Math.sqrt(this.getX() * this.getX() + this.getZ() * this.getZ());
                  if (this.speedLimit.getValue() && finalDist > this.maxSpeed.getValue()) {
                     this.setX(this.getX() * this.maxSpeed.getValue() / finalDist);
                     this.setZ(this.getZ() * this.maxSpeed.getValue() / finalDist);
                  }

                  event.cancel();
                  mc.field_1724.method_5784(class_1313.field_6308, mc.field_1724.method_18798());
               }
            }

         }
      }
   }

   @EventListener
   public void onElytraUpdate(ElytraUpdateEvent event) {
      if (this.packet.getValue() && event.getEntity() == mc.field_1724) {
         event.cancel();
      }

   }

   private double getX() {
      return MovementUtil.getMotionX();
   }

   private void setX(double f) {
      MovementUtil.setMotionX(f);
   }

   private double getY() {
      return MovementUtil.getMotionY();
   }

   private void setY(double f) {
      MovementUtil.setMotionY(f);
   }

   private double getZ() {
      return MovementUtil.getMotionZ();
   }

   private void setZ(double f) {
      MovementUtil.setMotionZ(f);
   }

   private void getInfinitePitch() {
      this.lastInfinitePitch = this.infinitePitch;
      double currentPlayerSpeed = Math.hypot(mc.field_1724.method_23317() - mc.field_1724.field_6014, mc.field_1724.method_23321() - mc.field_1724.field_5969);
      if (mc.field_1724.method_23318() < this.infiniteMaxHeight.getValue()) {
         if (currentPlayerSpeed * (double)72.0F < this.infiniteMinSpeed.getValue() && !this.down) {
            this.down = true;
         }

         if (currentPlayerSpeed * (double)72.0F > this.infiniteMaxSpeed.getValue() && this.down) {
            this.down = false;
         }
      } else {
         this.down = true;
      }

      this.infinitePitch = this.down ? (this.infinitePitch += 3.0F) : (this.infinitePitch -= 3.0F);
      this.infinitePitch = MathUtil.clamp(this.infinitePitch, -40.0F, 40.0F);
   }

   public boolean isFallFlying() {
      return mc.field_1724.method_6128() || this.packet.getValue() && this.hasElytra && !mc.field_1724.method_24828() || this.flying;
   }

   public static enum Mode {
      Control,
      Boost,
      Bounce,
      Freeze,
      None,
      Rotation,
      Pitch;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Control, Boost, Bounce, Freeze, None, Rotation, Pitch};
      }
   }

   public class FireWorkTweak {
      boolean press;

      @EventListener
      public void onTick(ClientTickEvent event) {
         if (!Module.nullCheck() && !event.isPost()) {
            if (!ElytraFly.this.inventory.getValue() || EntityUtil.inInventory()) {
               if (Wrapper.mc.field_1755 == null) {
                  if (ElytraFly.this.fireWork.isPressed()) {
                     if (!this.press && ElytraFly.this.fireworkTimer.passed((long)ElytraFly.this.delay.getValueInt()) && (!Wrapper.mc.field_1724.method_6115() || !ElytraFly.this.usingPause.getValue()) && ElytraFly.this.isFallFlying()) {
                        ElytraFly.this.off();
                        ElytraFly.this.fireworkTimer.reset();
                     }

                     this.press = true;
                  } else {
                     this.press = false;
                  }
               } else {
                  this.press = false;
               }

            }
         }
      }
   }
}
