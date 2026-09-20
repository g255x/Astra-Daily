package nekiplay.meteorplus.features.modules.movement.elytrafly.modes;

import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import nekiplay.meteorplus.features.modules.movement.elytrafly.ElytraFlyMode;
import nekiplay.meteorplus.features.modules.movement.elytrafly.ElytraFlyModes;

public class Control extends ElytraFlyMode {
   private boolean moving;
   private float yaw;
   private float pitch;
   private float p;
   private double velocity;

   public Control() {
      super(ElytraFlyModes.Control);
   }

   public void onPlayerMove(PlayerMoveEvent event) {
      if (this.mc.field_1724.method_6128()) {
         this.updateControlMovement();
         this.pitch = 0.0F;
         boolean movingUp = false;
         if (!this.mc.field_1690.field_1832.method_1434() && this.mc.field_1690.field_1903.method_1434() && this.velocity > (Double)this.elytraFly.speed_control.get() * 0.4) {
            this.p = (float)Math.min((double)this.p + 0.1 * (double)(1.0F - this.p) * (double)(1.0F - this.p) * (double)(1.0F - this.p), (double)1.0F);
            this.pitch = Math.max(Math.max(this.p, 0.0F) * -90.0F, -90.0F);
            movingUp = true;
            this.moving = false;
         } else {
            this.velocity = (Double)this.elytraFly.speed_control.get();
            this.p = -0.2F;
         }

         this.velocity = this.moving ? (Double)this.elytraFly.speed_control.get() : Math.min(this.velocity + Math.sin(Math.toRadians((double)this.pitch)) * 0.08, (Double)this.elytraFly.speed_control.get());
         double cos = Math.cos(Math.toRadians((double)(this.yaw + 90.0F)));
         double sin = Math.sin(Math.toRadians((double)(this.yaw + 90.0F)));
         double x = this.moving && !movingUp ? cos * (Double)this.elytraFly.speed_control.get() : (movingUp ? this.velocity * Math.cos(Math.toRadians((double)this.pitch)) * cos : (double)0.0F);
         double y = this.pitch < 0.0F ? this.velocity * (Double)this.elytraFly.upMultiplier_control.get() * -Math.sin(Math.toRadians((double)this.pitch)) * this.velocity : -(Double)this.elytraFly.fallSpeed_control.get();
         double z = this.moving && !movingUp ? sin * (Double)this.elytraFly.speed_control.get() : (movingUp ? this.velocity * Math.cos(Math.toRadians((double)this.pitch)) * sin : (double)0.0F);
         y *= Math.abs(Math.sin(Math.toRadians(movingUp ? (double)this.pitch : (double)this.mc.field_1724.method_36455())));
         if (this.mc.field_1690.field_1832.method_1434() && !this.mc.field_1690.field_1903.method_1434()) {
            y = -(Double)this.elytraFly.downSpeed_control.get();
         }

         ((IVec3d)event.movement).meteor$set(x, y, z);
         if ((Boolean)this.elytraFly.resetSpeed.get()) {
            this.mc.field_1724.method_18800((double)0.0F, (double)0.0F, (double)0.0F);
         }

      }
   }

   private void updateControlMovement() {
      float yaw = this.mc.field_1724.method_36454();
      float forward = this.mc.field_1724.field_3913.method_3128().field_1342;
      float sideways = this.mc.field_1724.field_3913.method_3128().field_1343;
      if (forward > 0.0F) {
         this.moving = true;
         yaw += sideways > 0.0F ? -45.0F : (sideways < 0.0F ? 45.0F : 0.0F);
      } else if (forward < 0.0F) {
         this.moving = true;
         yaw += sideways > 0.0F ? -135.0F : (sideways < 0.0F ? 135.0F : 180.0F);
      } else {
         this.moving = sideways != 0.0F;
         yaw += sideways > 0.0F ? -90.0F : (sideways < 0.0F ? 90.0F : 0.0F);
      }

      this.yaw = yaw;
   }
}
