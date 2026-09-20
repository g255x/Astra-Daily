package nekiplay.meteorplus.features.modules.movement.jesus.modes;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import nekiplay.meteorplus.features.modules.movement.jesus.JesusMode;
import nekiplay.meteorplus.features.modules.movement.jesus.JesusModes;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;

public class MatrixZoom extends JesusMode {
   private final float range = 0.005F;

   public MatrixZoom() {
      super(JesusModes.Matrix_Zoom);
   }

   public void onTickEventPre(TickEvent.Pre event) {
      float yaw = this.mc.field_1724.method_36454();
      class_243 forward = class_243.method_1030(0.0F, yaw);
      class_243 right = class_243.method_1030(0.0F, yaw + 90.0F);
      double velX = (double)0.0F;
      double velZ = (double)0.0F;
      double s = (double)0.5F;
      double speedValue = (Double)this.settings.speed.get();
      if (this.mc.field_1690.field_1894.method_1434()) {
         velX += forward.field_1352 * s * speedValue;
         velZ += forward.field_1350 * s * speedValue;
      }

      if (this.mc.field_1690.field_1881.method_1434()) {
         velX -= forward.field_1352 * s * speedValue;
         velZ -= forward.field_1350 * s * speedValue;
      }

      if (this.mc.field_1690.field_1849.method_1434()) {
         velX += right.field_1352 * s * speedValue;
         velZ += right.field_1350 * s * speedValue;
      }

      if (this.mc.field_1690.field_1913.method_1434()) {
         velX -= right.field_1352 * s * speedValue;
         velZ -= right.field_1350 * s * speedValue;
      }

      if (this.mc.field_1687.method_8320(new class_2338(this.mc.field_1724.method_31477(), (int)((float)this.mc.field_1724.method_31478() + 0.005F), this.mc.field_1724.method_31479())).method_26204() == class_2246.field_10382 && !this.mc.field_1724.field_5976) {
         ((IVec3d)this.mc.field_1724.method_18798()).meteor$set(velX, (double)0.0F, velZ);
      }

   }
}
