package nekiplay.meteorplus.features.modules.movement.elytrafly.modes;

import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import nekiplay.meteorplus.features.modules.movement.elytrafly.ElytraFlyMode;
import nekiplay.meteorplus.features.modules.movement.elytrafly.ElytraFlyModes;

public class Wasp extends ElytraFlyMode {
   private boolean moving;
   private float yaw;
   private float pitch;

   public Wasp() {
      super(ElytraFlyModes.Wasp);
   }

   public void onPlayerMove(PlayerMoveEvent event) {
      if (this.mc.field_1724.method_6128()) {
         this.updateWaspMovement();
         this.pitch = this.mc.field_1724.method_36455();
         double cos = Math.cos(Math.toRadians((double)(this.yaw + 90.0F)));
         double sin = Math.sin(Math.toRadians((double)(this.yaw + 90.0F)));
         double x = this.moving ? cos * (Double)this.elytraFly.horizontal_wasp.get() : (double)0.0F;
         double y = -(Double)this.elytraFly.fallSpeed_wasp.get();
         double z = this.moving ? sin * (Double)this.elytraFly.horizontal_wasp.get() : (double)0.0F;
         if ((Boolean)this.elytraFly.smartFall_wasp.get()) {
            y *= Math.abs(Math.sin(Math.toRadians((double)this.pitch)));
         }

         if (this.mc.field_1690.field_1832.method_1434() && !this.mc.field_1690.field_1903.method_1434()) {
            y = -(Double)this.elytraFly.down_wasp.get();
         }

         if (!this.mc.field_1690.field_1832.method_1434() && this.mc.field_1690.field_1903.method_1434()) {
            y = (Double)this.elytraFly.up_wasp.get();
         }

         ((IVec3d)event.movement).meteor$set(x, y, z);
         if ((Boolean)this.elytraFly.resetSpeed.get()) {
            this.mc.field_1724.method_18800((double)0.0F, (double)0.0F, (double)0.0F);
         }

      }
   }

   private void updateWaspMovement() {
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
