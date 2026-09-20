package nekiplay.meteorplus.features.modules.movement.jesus.modes;

import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import nekiplay.meteorplus.features.modules.movement.jesus.JesusMode;
import nekiplay.meteorplus.features.modules.movement.jesus.JesusModes;
import net.minecraft.class_2189;
import net.minecraft.class_243;

public class NCP extends JesusMode {
   float newSpeed = 0.0F;

   public NCP() {
      super(JesusModes.NCP);
   }

   public void onPlayerMoveEvent(PlayerMoveEvent event) {
      this.mc.field_1724.method_5728(false);
      if (this.mc.field_1724.method_52535()) {
         class_243 velocity = this.mc.field_1724.method_18798();
         if (this.mc.field_1690.field_1903.method_1434() && !this.mc.field_1724.method_5715() && !(this.mc.field_1687.method_8320(this.mc.field_1724.method_24515().method_10069(0, 1, 0)).method_26204() instanceof class_2189)) {
            this.mc.field_1724.method_18800(velocity.field_1352, 0.12, velocity.field_1350);
         }

         velocity = this.mc.field_1724.method_18798();
         if (this.mc.field_1690.field_1832.method_1434()) {
            this.mc.field_1724.method_18800(velocity.field_1352, -0.12, velocity.field_1350);
         }

         velocity = this.mc.field_1724.method_18798();
         if (this.mc.field_1687.method_8320(this.mc.field_1724.method_24515().method_10069(0, 1, 0)).method_26204() instanceof class_2189 && this.mc.field_1690.field_1903.method_1434()) {
            this.mc.field_1724.method_5660(true);
            this.mc.field_1724.method_18800(velocity.field_1352, 0.12, velocity.field_1350);
         }

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

         if (velX >= (double)((Double)this.settings.limit_speed.get()).floatValue()) {
            velX = (double)((Double)this.settings.limit_speed.get()).floatValue();
         }

         if (velZ >= (double)((Double)this.settings.limit_speed.get()).floatValue()) {
            velZ = (double)((Double)this.settings.limit_speed.get()).floatValue();
         }

         ((IVec3d)this.mc.field_1724.method_18798()).meteor$set(velX, (double)0.0F, velZ);
         this.mc.field_1724.method_5660(true);
      }
   }

   public void onDeactivate() {
      this.newSpeed = 0.6F;
      super.onDeactivate();
   }

   public void onActivate() {
      this.newSpeed = 0.6F;
      this.newSpeed += ((Double)this.settings.speed.get()).floatValue();
      super.onActivate();
   }

   public void setMotion(double motion) {
      float forward = this.mc.field_1724.field_6250;
      float yaw = this.mc.field_1724.method_36454();
      if (forward == 0.0F) {
         ((IVec3d)this.mc.field_1724.method_18798()).meteor$set((double)0.0F, this.mc.field_1724.method_18798().field_1351, (double)0.0F);
      } else {
         double x = (double)forward * motion * Math.cos(Math.toRadians((double)(yaw + 90.0F))) * motion * Math.sin(Math.toRadians((double)(yaw + 90.0F)));
         double z = (double)forward * motion * Math.sin(Math.toRadians((double)(yaw + 90.0F))) * motion * Math.cos(Math.toRadians((double)(yaw + 90.0F)));
         ((IVec3d)this.mc.field_1724.method_18798()).meteor$set(x, this.mc.field_1724.method_18798().field_1351, z);
      }

   }
}
