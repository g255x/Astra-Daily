package dev.Astra.mod.modules.impl.movement;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.SprintEvent;
import dev.Astra.api.events.impl.TickEvent;
import dev.Astra.api.events.impl.TickMovementEvent;
import dev.Astra.api.events.impl.UpdateRotateEvent;
import dev.Astra.api.utils.path.BaritoneUtil;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.player.Freecam;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import net.minecraft.class_1294;
import net.minecraft.class_2708;
import net.minecraft.class_3532;

public class Sprint extends Module {
   public static Sprint INSTANCE;
   public final EnumSetting<Mode> mode;
   public final BooleanSetting inWaterPause;
   public final BooleanSetting inWebPause;
   public final BooleanSetting sneakingPause;
   public final BooleanSetting blindnessPause;
   public final BooleanSetting usingPause;
   public final BooleanSetting lagPause;
   boolean pause;

   public Sprint() {
      super("Sprint", "Permanently keeps player in sprinting mode.", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", Sprint.Mode.Legit));
      this.inWaterPause = this.add(new BooleanSetting("InWaterPause", true));
      this.inWebPause = this.add(new BooleanSetting("InWebPause", true));
      this.sneakingPause = this.add(new BooleanSetting("SneakingPause", false));
      this.blindnessPause = this.add(new BooleanSetting("BlindnessPause", false));
      this.usingPause = this.add(new BooleanSetting("UsingPause", false));
      this.lagPause = this.add(new BooleanSetting("LagPause", true));
      this.pause = false;
      this.setChinese("强制疾跑");
      INSTANCE = this;
   }

   public static float getSprintYaw(float yaw) {
      if (mc.field_1690.field_1894.method_1434() && !mc.field_1690.field_1881.method_1434()) {
         if (mc.field_1690.field_1913.method_1434() && !mc.field_1690.field_1849.method_1434()) {
            yaw -= 45.0F;
         } else if (mc.field_1690.field_1849.method_1434() && !mc.field_1690.field_1913.method_1434()) {
            yaw += 45.0F;
         }
      } else if (mc.field_1690.field_1881.method_1434() && !mc.field_1690.field_1894.method_1434()) {
         yaw += 180.0F;
         if (mc.field_1690.field_1913.method_1434() && !mc.field_1690.field_1849.method_1434()) {
            yaw += 45.0F;
         } else if (mc.field_1690.field_1849.method_1434() && !mc.field_1690.field_1913.method_1434()) {
            yaw -= 45.0F;
         }
      } else if (mc.field_1690.field_1913.method_1434() && !mc.field_1690.field_1849.method_1434()) {
         yaw -= 90.0F;
      } else if (mc.field_1690.field_1849.method_1434() && !mc.field_1690.field_1913.method_1434()) {
         yaw += 90.0F;
      }

      return class_3532.method_15393(yaw);
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   @EventListener
   public void onPacket(PacketEvent.Receive event) {
      if (this.lagPause.getValue() && event.getPacket() instanceof class_2708) {
         this.pause = true;
      }

   }

   public boolean inWater() {
      return this.inWaterPause.getValue() && mc.field_1724.method_52535();
   }

   @EventListener
   public void onMove(TickMovementEvent event) {
      if (!BaritoneUtil.isPathing()) {
         if (!this.inWater()) {
            if (this.mode.getValue() == Sprint.Mode.PressKey) {
               mc.field_1690.field_1867.method_23481(true);
            } else {
               mc.field_1724.method_5728(this.shouldSprint());
            }

         }
      }
   }

   @EventListener
   public void tick(TickEvent event) {
      if (event.isPost()) {
         this.pause = false;
      }

   }

   @EventListener
   public void sprint(SprintEvent event) {
      if (!BaritoneUtil.isPathing() && !this.mode.is(Sprint.Mode.PressKey)) {
         if (!this.inWater()) {
            event.cancel();
            event.setSprint(this.shouldSprint());
         }
      }
   }

   private boolean shouldSprint() {
      if ((mc.field_1724.method_7344().method_7586() > 6 || mc.field_1724.method_7337()) && MovementUtil.isMoving() && !this.pause && (!mc.field_1724.method_5715() || !this.sneakingPause.getValue()) && (!Astra.PLAYER.isInWeb(mc.field_1724) || !this.inWebPause.getValue()) && (!mc.field_1724.method_6115() || !this.usingPause.getValue()) && !mc.field_1724.method_3144() && (!mc.field_1724.method_6059(class_1294.field_5919) || !this.blindnessPause.getValue())) {
         switch (((Mode)this.mode.getValue()).ordinal()) {
            case 1:
               if (AntiCheat.INSTANCE.movementSync()) {
                  return mc.field_1724.field_3913.field_3905 > 0.0F;
               }

               return HoleSnap.INSTANCE.isOn() || mc.field_1690.field_1894.method_1434() && class_3532.method_15356(mc.field_1724.method_36454(), Astra.ROTATION.rotationYaw) < 40.0F;
            case 2:
               return true;
            case 3:
               if (AntiCheat.INSTANCE.movementSync()) {
                  return mc.field_1724.field_3913.field_3905 > 0.0F;
               }

               return HoleSnap.INSTANCE.isOn() || class_3532.method_15356(getSprintYaw(mc.field_1724.method_36454()), Astra.ROTATION.rotationYaw) < 40.0F;
         }
      }

      return false;
   }

   @EventListener(
      priority = -100
   )
   public void rotate(UpdateRotateEvent event) {
      if (!BaritoneUtil.isPathing()) {
         if (((mc.field_1724.method_7344().method_7586() > 6 || mc.field_1724.method_7337()) && MovementUtil.isMoving() && !Freecam.INSTANCE.isOn() && !mc.field_1724.method_6128() && (!Astra.PLAYER.isInWeb(mc.field_1724) || !this.inWebPause.getValue()) && (!mc.field_1724.method_5715() || !this.sneakingPause.getValue()) && !mc.field_1724.method_3144() && (!mc.field_1724.method_6115() || !this.usingPause.getValue()) && !mc.field_1724.method_52535() && Freecam.INSTANCE.isOff() && !mc.field_1724.method_6059(class_1294.field_5919) || !this.blindnessPause.getValue()) && this.mode.is(Sprint.Mode.Rotation) && !event.isModified()) {
            event.setYaw(getSprintYaw(mc.field_1724.method_36454()));
         }

      }
   }

   public static enum Mode {
      PressKey,
      Legit,
      Rage,
      Rotation;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{PressKey, Legit, Rage, Rotation};
      }
   }
}
