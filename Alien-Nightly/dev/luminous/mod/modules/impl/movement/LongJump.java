package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.Event;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ElytraTransformEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.PlayerMoveEvent;
import dev.luminous.api.events.impl.PlayerUpdateEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.string.EnumFormatter;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.DisablerModule;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1770;
import net.minecraft.class_1799;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_434;
import net.minecraft.class_2848.class_2849;

public class LongJump extends Module {
   private static LongJump INSTANCE;
   EnumSetting<JumpMode> modeConfig;
   SliderSetting boostConfig;
   BooleanSetting elytraBoostConfig;
   BooleanSetting autoDisableConfig;
   private int stage;
   private double distance;
   private double speed;
   private int airTicks;
   private int groundTicks;

   public LongJump() {
      super("LongJump", Module.Category.Movement);
      this.modeConfig = this.add(new EnumSetting("Mode", LongJump.JumpMode.NORMAL));
      this.boostConfig = this.add(new SliderSetting("Boost", (double)0.1F, (double)4.5F, (double)10.0F, () -> this.modeConfig.getValue() == LongJump.JumpMode.NORMAL));
      this.elytraBoostConfig = this.add(new BooleanSetting("VanillaBoost", false, () -> this.modeConfig.getValue() == LongJump.JumpMode.GRIM));
      this.autoDisableConfig = this.add(new BooleanSetting("AutoDisable", true));
      this.setChinese("远跳");
      INSTANCE = this;
   }

   public static LongJump getInstance() {
      return INSTANCE;
   }

   public String getModuleData() {
      return EnumFormatter.formatEnum(this.modeConfig.getValue());
   }

   public boolean onEnable() {
      this.groundTicks = 0;
      return false;
   }

   public void onDisable() {
      this.stage = 0;
      this.distance = (double)0.0F;
   }

   @EventListener
   public void onTravel(TickEvent event) {
      if (!nullCheck()) {
         double dx = mc.field_1724.method_23317() - mc.field_1724.field_6014;
         double dz = mc.field_1724.method_23321() - mc.field_1724.field_5969;
         this.distance = Math.sqrt(dx * dx + dz * dz);
         if (this.modeConfig.getValue() == LongJump.JumpMode.GRIM) {
            class_238 bb = mc.field_1724.method_5829();
            boolean shouldFall = false;

            for(double i = (double)0.0F; i < (double)2.0F; i += 0.01) {
               if (!mc.field_1687.method_8587(mc.field_1724, bb.method_989((double)0.0F, -i, (double)0.0F))) {
                  shouldFall = true;
                  break;
               }
            }

            if (!shouldFall) {
               return;
            }

            int elytraSlot = -1;
            int i = 0;

            while(true) {
               if (i < 36) {
                  class_1799 stack = mc.field_1724.method_31548().method_5438(i);
                  if (!(stack.method_7909() instanceof class_1770)) {
                     ++i;
                     continue;
                  }

                  elytraSlot = i;
               }

               if (elytraSlot == -1) {
                  return;
               }

               if (mc.field_1724.method_24828()) {
                  mc.field_1724.method_6043();
               } else if (mc.field_1724.method_18798().field_1351 < (double)0.0F && !mc.field_1724.method_6128()) {
                  Alien.INVENTORY.click(elytraSlot < 9 ? elytraSlot + 36 : elytraSlot, 0, class_1713.field_7790);
                  Alien.INVENTORY.click(6, 0, class_1713.field_7790);
                  Alien.INVENTORY.click(elytraSlot < 9 ? elytraSlot + 36 : elytraSlot, 0, class_1713.field_7790);
                  Alien.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12982));
                  mc.field_1724.method_23669();
                  Alien.INVENTORY.click(6, 0, class_1713.field_7790);
                  Alien.INVENTORY.click(elytraSlot < 9 ? elytraSlot + 36 : elytraSlot, 0, class_1713.field_7790);
                  Alien.INVENTORY.click(6, 0, class_1713.field_7790);
               }

               if (this.elytraBoostConfig.getValue()) {
                  if (!mc.field_1724.method_6128() || mc.field_1724.method_5799() || mc.field_1724.method_5771() || (float)mc.field_1724.method_7344().method_7586() <= 6.0F) {
                     return;
                  }

                  if (mc.field_1724.method_18798().field_1351 < (double)0.0F) {
                     double d4 = (double)0.014F * Math.cos(Math.toRadians((double)(mc.field_1724.method_36454() + 90.0F)));
                     double d5 = (double)0.014F * Math.sin(Math.toRadians((double)(mc.field_1724.method_36454() + 90.0F)));
                     class_243 glide = new class_243(d4, (double)0.0F, d5);
                     class_243 motion = mc.field_1724.method_18798();
                     Alien.MOVEMENT.setMotionXZ(motion.field_1352 + glide.field_1352, motion.field_1350 + glide.field_1350);
                  }
               }
               break;
            }
         }

      }
   }

   @EventListener
   public void onElytraTransform(ElytraTransformEvent event) {
      if (this.modeConfig.getValue() == LongJump.JumpMode.GRIM && event.getEntity() == mc.field_1724) {
         event.cancel();
      }

   }

   @EventListener
   public void onPlayerMove(PlayerMoveEvent event) {
      if (this.modeConfig.getValue() == LongJump.JumpMode.NORMAL) {
         if (mc.field_1724 == null || mc.field_1687 == null || Fly.getInstance().onEnable() || DisablerModule.getInstance().grimFireworkCheck() || PacketFly.INSTANCE.onEnable() || !MovementUtil.isInputtingMovement()) {
            return;
         }

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
         if (this.stage == 0) {
            this.stage = 1;
            this.speed = this.boostConfig.getValue() * base - 0.01;
         } else if (this.stage == 1) {
            this.stage = 2;
            Alien.MOVEMENT.setMotionY(0.42);
            event.setY(0.42);
            this.speed *= 2.149;
         } else if (this.stage == 2) {
            this.stage = 3;
            double moveSpeed = 0.66 * (this.distance - base);
            this.speed = this.distance - moveSpeed;
         } else {
            if (!mc.field_1687.method_8587(mc.field_1724, mc.field_1724.method_5829().method_989((double)0.0F, mc.field_1724.method_18798().method_10214(), (double)0.0F)) || mc.field_1724.field_5992) {
               this.stage = 0;
            }

            this.speed = this.distance - this.distance / (double)159.0F;
         }

         this.speed = Math.max(this.speed, base);
         event.cancel();
         class_241 motion = Speed.INSTANCE.handleStrafeMotion((float)this.speed);
         event.setX((double)motion.field_1343);
         event.setZ((double)motion.field_1342);
      }

   }

   @EventListener
   public void onPlayerUpdate(PlayerUpdateEvent event) {
      if (Event.Stage.Pre == Event.Stage.Pre && this.modeConfig.getValue() == LongJump.JumpMode.GLIDE) {
         if (Fly.getInstance().onEnable() || mc.field_1724.method_6128() || mc.field_1724.method_21754() || mc.field_1724.method_5799()) {
            return;
         }

         if (mc.field_1724.method_24828()) {
            this.distance = (double)0.0F;
         }

         float direction = mc.field_1724.method_36454() + (float)(mc.field_1724.field_6250 < 0.0F ? 180 : 0) + (mc.field_1724.field_6212 > 0.0F ? -90.0F * (mc.field_1724.field_6250 < 0.0F ? -0.5F : (mc.field_1724.field_6250 > 0.0F ? 0.5F : 1.0F)) : 0.0F) - (mc.field_1724.field_6212 < 0.0F ? -90.0F * (mc.field_1724.field_6250 < 0.0F ? -0.5F : (mc.field_1724.field_6250 > 0.0F ? 0.5F : 1.0F)) : 0.0F);
         float dx = (float)Math.cos((double)(direction + 90.0F) * Math.PI / (double)180.0F);
         float dz = (float)Math.sin((double)(direction + 90.0F) * Math.PI / (double)180.0F);
         if (!mc.field_1724.field_5992) {
            ++this.airTicks;
            if (mc.field_1724.field_3913.field_3903) {
               mc.field_1724.field_3944.method_52787(new class_2828.class_2829((double)0.0F, (double)Integer.MAX_VALUE, (double)0.0F, false));
            }

            this.groundTicks = 0;
            if (!mc.field_1724.field_5992) {
               if (mc.field_1724.method_18798().field_1351 == -0.07190068807140403) {
                  Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.35F);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.10306193759436909) {
                  Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.55F);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.13395038817442878) {
                  Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.67F);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.16635183030382) {
                  Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.69F);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.19088711097794803) {
                  Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.71F);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.21121925191528862) {
                  Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.2F);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.11979897632390576) {
                  Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.93F);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.18758479151225355) {
                  Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.72F);
               }

               if (mc.field_1724.method_18798().field_1351 == -0.21075983825251726) {
                  Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.76F);
               }

               if (this.getJumpCollisions(mc.field_1724, (double)70.0F) < (double)0.5F) {
                  if (mc.field_1724.method_18798().field_1351 == -0.23537393014173347) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.03F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08531999505205401) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)-0.5F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.03659320313669756) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)-0.1F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.07481386749524899) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)-0.07F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.0732677700939672) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)-0.05F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.07480988066790395) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)-0.04F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.0784000015258789) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.1F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08608320193943977) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.1F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08683615560584318) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.05F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08265497329678266) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.05F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08245009535659828) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * (double)0.05F);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08244005633718426) {
                     Alien.MOVEMENT.setMotionY(-0.08243956442521608);
                  }

                  if (mc.field_1724.method_18798().field_1351 == -0.08243956442521608) {
                     Alien.MOVEMENT.setMotionY((double)-0.082440056F);
                  }

                  if (mc.field_1724.method_18798().field_1351 > -0.1 && mc.field_1724.method_18798().field_1351 < -0.08 && !mc.field_1724.method_24828() && mc.field_1724.field_3913.field_3910) {
                     Alien.MOVEMENT.setMotionY((double)-1.0E-4F);
                  }
               } else {
                  if (mc.field_1724.method_18798().field_1351 < -0.2 && mc.field_1724.method_18798().field_1351 > -0.24) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.7);
                  }

                  if (mc.field_1724.method_18798().field_1351 < (double)-0.25F && mc.field_1724.method_18798().field_1351 > -0.32) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.8);
                  }

                  if (mc.field_1724.method_18798().field_1351 < -0.35 && mc.field_1724.method_18798().field_1351 > -0.8) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.98);
                  }

                  if (mc.field_1724.method_18798().field_1351 < -0.8 && mc.field_1724.method_18798().field_1351 > -1.6) {
                     Alien.MOVEMENT.setMotionY(mc.field_1724.method_18798().field_1351 * 0.99);
                  }
               }
            }

            Alien.TICK.setClientTick(0.85F);
            double[] jumpFactor = new double[]{0.420606, 0.417924, 0.415258, 0.412609, 0.409977, 0.407361, 0.404761, 0.402178, 0.399611, 0.39706, 0.394525, 0.392, 0.3894, 0.38644, 0.383655, 0.381105, 0.37867, 0.37625, 0.37384, 0.37145, 0.369, 0.3666, 0.3642, 0.3618, 0.35945, 0.357, 0.354, 0.351, 0.348, 0.345, 0.342, 0.339, 0.336, 0.333, 0.33, 0.327, 0.324, 0.321, 0.318, 0.315, 0.312, 0.309, 0.307, 0.305, 0.303, 0.3, 0.297, 0.295, 0.293, 0.291, 0.289, 0.287, 0.285, 0.283, 0.281, 0.279, 0.277, 0.275, 0.273, 0.271, 0.269, 0.267, 0.265, 0.263, 0.261, 0.259, 0.257, 0.255, 0.253, 0.251, 0.249, 0.247, 0.245, 0.243, 0.241, 0.239, 0.237};
            if (mc.field_1724.field_3913.field_3910) {
               try {
                  Alien.MOVEMENT.setMotionXZ((double)dx * jumpFactor[this.airTicks - 1] * (double)3.0F, (double)dz * jumpFactor[this.airTicks - 1] * (double)3.0F);
               } catch (ArrayIndexOutOfBoundsException var7) {
               }

               return;
            }

            Alien.MOVEMENT.setMotionXZ((double)0.0F, (double)0.0F);
            return;
         }

         Alien.TICK.setClientTick(1.0F);
         this.airTicks = 0;
         ++this.groundTicks;
         Alien.MOVEMENT.setMotionXZ(mc.field_1724.method_18798().field_1352 / (double)13.0F, mc.field_1724.method_18798().field_1350 / (double)13.0F);
         if (this.groundTicks == 1) {
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828()));
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317() + 0.0624, mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828()));
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.419, mc.field_1724.method_23321(), mc.field_1724.method_24828()));
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317() + 0.0624, mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_24828()));
            mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.419, mc.field_1724.method_23321(), mc.field_1724.method_24828()));
         }

         if (this.groundTicks > 2) {
            this.groundTicks = 0;
            Alien.MOVEMENT.setMotionXZ((double)dx * 0.3, (double)dz * 0.3);
            Alien.MOVEMENT.setMotionY((double)0.424F);
         }
      }

   }

   @EventListener
   public void onPacketInbound(PacketEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null && !(mc.field_1755 instanceof class_434)) {
         if (event.getPacket() instanceof class_2708 && this.autoDisableConfig.getValue()) {
            this.disable();
         }

      }
   }

   private double getJumpCollisions(class_1657 player, double d) {
      return (double)1.0F;
   }

   public static enum JumpMode {
      NORMAL,
      GLIDE,
      GRIM;

      // $FF: synthetic method
      private static JumpMode[] $values() {
         return new JumpMode[]{NORMAL, GLIDE, GRIM};
      }
   }
}
