package dev.Astra.mod.modules.impl.movement;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.MoveEvent;
import dev.Astra.api.events.impl.MovedEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.path.BaritoneUtil;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_1690;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2708;
import net.minecraft.class_2743;

public class Speed extends Module {
   public static Speed INSTANCE;
   private final EnumSetting<Mode> mode;
   public final SliderSetting collideSpeed;
   private final BooleanSetting strict;
   private final BooleanSetting boat;
   public final SliderSetting boatExpand;
   public final SliderSetting boatSpeed;
   public final SliderSetting boatJump;
   private final BooleanSetting inWater;
   private final BooleanSetting inBlock;
   private final BooleanSetting airStop;
   private final SliderSetting lagTime;
   private final BooleanSetting jump;
   private final SliderSetting strafeSpeed;
   private final BooleanSetting explosions;
   private final BooleanSetting velocity;
   private final SliderSetting multiplier;
   private final SliderSetting vertical;
   private final SliderSetting coolDown;
   private final BooleanSetting slow;
   private final Timer expTimer;
   private final Timer lagTimer;
   private boolean stop;
   private double speed;
   private double getDistance;
   private int strictTicks;
   private int strafe;
   private int stage;
   private double lastExp;
   private boolean boost;

   public Speed() {
      super("Speed", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", Speed.Mode.Strafe));
      this.collideSpeed = this.add(new SliderSetting("CollideSpeed", 0.08, (double)0.0F, 0.08, 0.01, () -> this.mode.is(Speed.Mode.Grim)));
      this.strict = this.add(new BooleanSetting("Strict", true, () -> this.mode.is(Speed.Mode.Grim)));
      this.boat = this.add(new BooleanSetting("BoatLongJump", true, () -> this.mode.is(Speed.Mode.Grim)));
      this.boatExpand = this.add(new SliderSetting("BoatExpand", 0.2, (double)0.0F, (double)1.0F, 0.01, () -> this.mode.is(Speed.Mode.Grim)));
      this.boatSpeed = this.add(new SliderSetting("BoatSpeed", 0.2, (double)-2.0F, (double)2.0F, 0.01, () -> this.mode.is(Speed.Mode.Grim)));
      this.boatJump = this.add(new SliderSetting("BoatJump", 0.2, (double)0.0F, (double)2.0F, 0.01, () -> this.mode.is(Speed.Mode.Grim)));
      this.inWater = this.add(new BooleanSetting("InWater", false, () -> !this.mode.is(Speed.Mode.Grim)));
      this.inBlock = this.add(new BooleanSetting("InBlock", false, () -> !this.mode.is(Speed.Mode.Grim)));
      this.airStop = this.add(new BooleanSetting("AirStop", false, () -> !this.mode.is(Speed.Mode.Grim)));
      this.lagTime = this.add(new SliderSetting("LagTime", (double)500.0F, (double)0.0F, (double)1000.0F, (double)1.0F, () -> !this.mode.is(Speed.Mode.Grim)));
      this.jump = this.add(new BooleanSetting("Jump", true, () -> this.mode.is(Speed.Mode.Strafe)));
      this.strafeSpeed = this.add(new SliderSetting("Speed", 0.2873, (double)0.0F, (double)1.0F, 1.0E-4, () -> this.mode.is(Speed.Mode.Strafe)));
      this.explosions = this.add(new BooleanSetting("ExplosionsBoost", false, () -> this.mode.is(Speed.Mode.Strafe)));
      this.velocity = this.add(new BooleanSetting("VelocityBoost", true, () -> this.mode.is(Speed.Mode.Strafe)));
      this.multiplier = this.add(new SliderSetting("H-Factor", (double)1.0F, (double)0.0F, (double)5.0F, 0.01, () -> this.mode.is(Speed.Mode.Strafe)));
      this.vertical = this.add(new SliderSetting("V-Factor", (double)1.0F, (double)0.0F, (double)5.0F, 0.01, () -> this.mode.is(Speed.Mode.Strafe)));
      this.coolDown = this.add(new SliderSetting("CoolDown", (double)1000.0F, (double)0.0F, (double)5000.0F, (double)1.0F, () -> this.mode.is(Speed.Mode.Strafe)));
      this.slow = this.add(new BooleanSetting("Slowness", false, () -> this.mode.is(Speed.Mode.Strafe)));
      this.expTimer = new Timer();
      this.lagTimer = new Timer();
      this.strafe = 4;
      this.setChinese("加速");
      INSTANCE = this;
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   public void onEnable() {
      if (mc.field_1724 != null) {
         this.speed = MovementUtil.getSpeed(false);
         this.getDistance = MovementUtil.getDistance2D();
      }

      this.stage = 4;
   }

   @EventListener(
      priority = 100
   )
   public void invoke(PacketEvent.Receive event) {
      if (!BaritoneUtil.isActive()) {
         if (this.mode.is(Speed.Mode.Strafe)) {
            class_2596<?> packet = event.getPacket();
            if (packet instanceof class_2743) {
               class_2743 packet2 = (class_2743)packet;
               if (mc.field_1724 != null && packet2.method_11818() == mc.field_1724.method_5628() && this.velocity.getValue()) {
                  double speed = Math.sqrt(packet2.method_11815() * packet2.method_11815() + packet2.method_11819() * packet2.method_11819());
                  double d = this.lastExp = this.expTimer.passed((long)this.coolDown.getValueInt()) ? speed : speed - this.lastExp;
                  if (this.lastExp > (double)0.0F) {
                     this.expTimer.reset();
                     this.speed += this.lastExp * this.multiplier.getValue();
                     this.getDistance += this.lastExp * this.multiplier.getValue();
                     if (MovementUtil.getMotionY() > (double)0.0F && this.vertical.getValue() != (double)0.0F) {
                        MovementUtil.setMotionY(MovementUtil.getMotionY() * this.vertical.getValue());
                     }
                  }
               }
            } else {
               class_2596<?> speed = event.getPacket();
               if (speed instanceof class_2664) {
                  class_2664 packet3 = (class_2664)speed;
                  if (this.explosions.getValue()) {
                     class_243 vec3d = new class_243(packet3.method_11475(), packet3.method_11477(), packet3.method_11478());
                     if (mc.field_1724.method_19538().method_1022(vec3d) < (double)15.0F) {
                        double speed2 = Math.sqrt((double)(packet3.method_11472() * packet3.method_11472() + packet3.method_11474() * packet3.method_11474()));
                        double d = this.lastExp = this.expTimer.passed((long)this.coolDown.getValueInt()) ? speed2 : speed2 - this.lastExp;
                        if (this.lastExp > (double)0.0F) {
                           this.expTimer.reset();
                           this.speed += this.lastExp * this.multiplier.getValue();
                           this.getDistance += this.lastExp * this.multiplier.getValue();
                           if (MovementUtil.getMotionY() > (double)0.0F) {
                              MovementUtil.setMotionY(MovementUtil.getMotionY() * this.vertical.getValue());
                           }
                        }
                     }
                  }
               }
            }
         }

         if (event.getPacket() instanceof class_2708) {
            this.lagTimer.reset();
            this.resetStrafe();
         }

      }
   }

   @EventListener
   public void onMove(MovedEvent event) {
      if (!nullCheck()) {
         double dx = mc.field_1724.method_23317() - mc.field_1724.field_6014;
         double dz = mc.field_1724.method_23321() - mc.field_1724.field_5969;
         this.getDistance = Math.sqrt(dx * dx + dz * dz);
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.mode.is(Speed.Mode.Grim)) {
         if (!MovementUtil.isMoving()) {
            return;
         }

         int collisions = 0;
         class_238 box = this.strict.getValue() ? mc.field_1724.method_5829() : mc.field_1724.method_5829().method_1014((double)1.0F);

         for(class_1297 entity : Astra.THREAD.getEntities()) {
            class_238 entityBox = entity.method_5829();
            if (this.boat.getValue() && mc.field_1724.method_24828() && entity instanceof class_1690 && box.method_994(entityBox.method_1014(this.boatExpand.getValue()))) {
               double yaw = Math.toRadians((double)Sprint.getSprintYaw(mc.field_1724.method_36454()));
               double boost = this.boatSpeed.getValue();
               mc.field_1724.method_18800(-Math.sin(yaw) * boost, this.boatJump.getValue(), Math.cos(yaw) * boost);
               return;
            }

            if (box.method_994(entityBox) && this.canCauseSpeed(entity)) {
               ++collisions;
            }
         }

         double yaw = Math.toRadians((double)Sprint.getSprintYaw(mc.field_1724.method_36454()));
         double boost = this.collideSpeed.getValue() * (double)collisions;
         mc.field_1724.method_5762(-Math.sin(yaw) * boost, (double)0.0F, Math.cos(yaw) * boost);
      }

   }

   private boolean canCauseSpeed(class_1297 entity) {
      return entity != mc.field_1724 && entity instanceof class_1309 && !(entity instanceof class_1531);
   }

   @EventListener
   public void invoke(MoveEvent event) {
      if (!MovementUtil.isMoving() && this.airStop.getValue() && !this.mode.is(Speed.Mode.Grim)) {
         MovementUtil.setMotionX((double)0.0F);
         MovementUtil.setMotionZ((double)0.0F);
      }

      if ((this.inWater.getValue() || !mc.field_1724.method_5869() && !mc.field_1724.method_5799() && !mc.field_1724.method_5771()) && !mc.field_1724.method_3144() && !mc.field_1724.method_21754() && (this.inBlock.getValue() || !EntityUtil.isInsideBlock()) && !mc.field_1724.method_31549().field_7479 && !mc.field_1724.method_6128() && MovementUtil.isMoving()) {
         if (this.mode.is(Speed.Mode.Strafe)) {
            if (this.stop) {
               this.stop = false;
            } else if (this.lagTimer.passed((long)this.lagTime.getValueInt())) {
               if (this.stage == 1) {
                  this.speed = 1.35 * MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue()) - 0.01;
               } else if (this.stage != 2 || !mc.field_1724.method_24828() || !mc.field_1690.field_1903.method_1434() && !this.jump.getValue()) {
                  if (this.stage == 3) {
                     this.speed = this.getDistance - 0.66 * (this.getDistance - MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue()));
                     this.boost = !this.boost;
                  } else {
                     if ((BlockUtil.canCollide((class_1297)null, mc.field_1724.method_5829().method_989((double)0.0F, MovementUtil.getMotionY(), (double)0.0F)) || mc.field_1724.field_34927) && this.stage > 0) {
                        this.stage = 1;
                     }

                     this.speed = this.getDistance - this.getDistance / (double)159.0F;
                  }
               } else {
                  double yMotion = 0.3999 + MovementUtil.getJumpSpeed();
                  MovementUtil.setMotionY(yMotion);
                  event.setY(yMotion);
                  this.speed *= this.boost ? 1.6835 : 1.395;
               }

               this.speed = Math.min(this.speed, (double)10.0F);
               this.speed = Math.max(this.speed, MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue()));
               double n = (double)mc.field_1724.field_3913.field_3905;
               double n2 = (double)mc.field_1724.field_3913.field_3907;
               double n3 = (double)mc.field_1724.method_36454();
               if (n == (double)0.0F && n2 == (double)0.0F) {
                  event.setX((double)0.0F);
                  event.setZ((double)0.0F);
               } else if (n != (double)0.0F && n2 != (double)0.0F) {
                  n *= Math.sin((Math.PI / 4D));
                  n2 *= Math.cos((Math.PI / 4D));
               }

               event.setX((n * this.speed * -Math.sin(Math.toRadians(n3)) + n2 * this.speed * Math.cos(Math.toRadians(n3))) * 0.99);
               event.setZ((n * this.speed * Math.cos(Math.toRadians(n3)) - n2 * this.speed * -Math.sin(Math.toRadians(n3))) * 0.99);
               ++this.stage;
            }
         } else {
            double speedEffect = (double)1.0F;
            double slowEffect = (double)1.0F;
            if (mc.field_1724.method_6059(class_1294.field_5904)) {
               double amplifier = (double)mc.field_1724.method_6112(class_1294.field_5904).method_5578();
               speedEffect = (double)1.0F + 0.2 * (amplifier + (double)1.0F);
            }

            if (mc.field_1724.method_6059(class_1294.field_5909)) {
               double amplifier = (double)mc.field_1724.method_6112(class_1294.field_5909).method_5578();
               slowEffect = (double)1.0F + 0.2 * (amplifier + (double)1.0F);
            }

            double base = (double)0.2873F * speedEffect / slowEffect;
            float jumpEffect = 0.0F;
            if (mc.field_1724.method_6059(class_1294.field_5913)) {
               jumpEffect += (float)(mc.field_1724.method_6112(class_1294.field_5913).method_5578() + 1) * 0.1F;
            }

            if (this.mode.getValue() == Speed.Mode.StrafeStrict) {
               if (!this.lagTimer.passed((long)this.lagTime.getValueInt())) {
                  return;
               }

               if (this.strafe == 1) {
                  this.speed = (double)1.35F * base - (double)0.01F;
               } else if (this.strafe == 2) {
                  if (mc.field_1724.field_3913.field_3904 || !mc.field_1724.method_24828()) {
                     return;
                  }

                  float jump = 0.39999995F + jumpEffect;
                  event.setY((double)jump);
                  MovementUtil.setMotionY((double)jump);
                  this.speed *= 2.149;
               } else if (this.strafe == 3) {
                  double moveSpeed = 0.66 * (this.getDistance - base);
                  this.speed = this.getDistance - moveSpeed;
               } else {
                  if ((!mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989((double)0.0F, mc.field_1724.method_18798().method_10214(), (double)0.0F)) || mc.field_1724.field_5992) && this.strafe > 0) {
                     this.strafe = 1;
                  }

                  this.speed = this.getDistance - this.getDistance / (double)159.0F;
               }

               ++this.strictTicks;
               this.speed = Math.max(this.speed, base);
               double baseMax = 0.465 * speedEffect / slowEffect;
               double baseMin = 0.44 * speedEffect / slowEffect;
               this.speed = Math.min(this.speed, this.strictTicks > 25 ? baseMax : baseMin);
               if (this.strictTicks > 50) {
                  this.strictTicks = 0;
               }

               class_241 motion = this.handleStrafeMotion((float)this.speed);
               event.setX((double)motion.field_1343);
               event.setZ((double)motion.field_1342);
               ++this.strafe;
            }

         }
      } else {
         this.resetStrafe();
         this.stop = true;
      }
   }

   public class_241 handleStrafeMotion(float speed) {
      float forward = mc.field_1724.field_3913.field_3905;
      float strafe = mc.field_1724.field_3913.field_3907;
      float yaw = mc.field_1724.field_5982 + (mc.field_1724.method_36454() - mc.field_1724.field_5982) * mc.method_60646().method_60637(true);
      if (forward == 0.0F && strafe == 0.0F) {
         return class_241.field_1340;
      } else {
         if (forward != 0.0F) {
            if (strafe >= 1.0F) {
               yaw += forward > 0.0F ? -45.0F : 45.0F;
               strafe = 0.0F;
            } else if (strafe <= -1.0F) {
               yaw += forward > 0.0F ? 45.0F : -45.0F;
               strafe = 0.0F;
            }

            if (forward > 0.0F) {
               forward = 1.0F;
            } else if (forward < 0.0F) {
               forward = -1.0F;
            }
         }

         float rx = (float)Math.cos(Math.toRadians((double)yaw));
         float rz = (float)(-Math.sin(Math.toRadians((double)yaw)));
         return new class_241(forward * speed * rz + strafe * speed * rx, forward * speed * rx - strafe * speed * rz);
      }
   }

   public void resetStrafe() {
      this.strafe = 4;
      this.strictTicks = 0;
      this.speed = (double)0.0F;
      this.getDistance = (double)0.0F;
   }

   public static enum Mode {
      Strafe,
      StrafeStrict,
      Grim;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Strafe, StrafeStrict, Grim};
      }
   }
}
