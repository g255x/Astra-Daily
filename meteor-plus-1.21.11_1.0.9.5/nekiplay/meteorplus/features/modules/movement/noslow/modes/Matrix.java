package nekiplay.meteorplus.features.modules.movement.noslow.modes;

import meteordevelopment.meteorclient.events.world.TickEvent;
import nekiplay.meteorplus.features.modules.movement.noslow.NoSlowMode;
import nekiplay.meteorplus.features.modules.movement.noslow.NoSlowModes;
import net.minecraft.class_243;

public class Matrix extends NoSlowMode {
   private int ticks = 0;

   public Matrix() {
      super(NoSlowModes.Matrix);
   }

   public void onActivate() {
      this.ticks = 0;
   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (this.mc.field_1724.method_6115() && this.mc.field_1724.method_24828() && this.ticks % 2 == 0) {
         float speed = 0.4F;
         class_243 vel = this.mc.field_1724.method_18798();
         double x = vel.method_10216() * (double)speed;
         double z = vel.method_10215() * (double)speed;
         this.mc.field_1724.method_18800(x, vel.method_10214(), z);
      }

      ++this.ticks;
   }
}
