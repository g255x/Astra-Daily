package nekiplay.meteorplus.features.modules.movement.speed.modes.vulcan;

import meteordevelopment.meteorclient.events.entity.player.JumpVelocityMultiplierEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedMode;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedModes;
import nekiplay.meteorplus.utils.MovementUtils;
import net.minecraft.class_1294;
import net.minecraft.class_243;

public class Vulcan_2_8_6 extends SpeedMode {
   private int ticks = 0;
   private int speedLevel = 0;
   private boolean jumped = false;

   public Vulcan_2_8_6() {
      super(SpeedModes.Vulcan_2dot8dot6);
   }

   public void onJump(JumpVelocityMultiplierEvent event) {
      this.ticks = 0;
      this.speedLevel = 0;
      this.jumped = true;
      if (this.mc.field_1724.method_6059(class_1294.field_5904)) {
         this.speedLevel = this.mc.field_1724.method_6112(class_1294.field_5904).method_5578();
      }

   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (this.jumped) {
         ++this.ticks;
         if (this.ticks == 1) {
            MovementUtils.strafe(0.3355 * ((double)1.0F + (double)this.speedLevel * 0.3819));
         }

         if (this.ticks == 2 && this.mc.field_1724.method_5624()) {
            MovementUtils.strafe(0.3284 * ((double)1.0F + (double)this.speedLevel * 0.355));
         }

         if (this.ticks == 4) {
            class_243 vel = this.mc.field_1724.method_73189();
            this.mc.field_1724.method_23327(vel.field_1352, vel.field_1351 - 0.376, vel.field_1350);
         }

         if (this.ticks == 6) {
            if ((double)this.mc.field_1724.field_28627 > 0.298) {
               MovementUtils.strafe(0.298);
            }

            this.jumped = false;
         }
      }

   }
}
