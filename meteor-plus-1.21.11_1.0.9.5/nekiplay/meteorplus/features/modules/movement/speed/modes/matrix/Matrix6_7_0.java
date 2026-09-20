package nekiplay.meteorplus.features.modules.movement.speed.modes.matrix;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedMode;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedModes;
import nekiplay.meteorplus.utils.MovementUtils;
import net.minecraft.class_2596;
import net.minecraft.class_2743;

public class Matrix6_7_0 extends SpeedMode {
   private int noVelocityY = 0;

   public Matrix6_7_0() {
      super(SpeedModes.Matrix_6dot7dot0);
   }

   public void onDeactivate() {
      this.mc.field_1724.method_31549().method_7248(0.02F);
   }

   public void onTickEventPre(TickEvent.Pre event) {
      this.work();
   }

   public void onTickEventPost(TickEvent.Post event) {
   }

   public void onReceivePacket(PacketEvent.Receive event) {
      class_2596 var3 = event.packet;
      if (var3 instanceof class_2743 velocity) {
         if (this.mc.field_1724 != null && this.mc.field_1687 != null && this.mc.field_1687.method_8469(velocity.method_11818()) != null && this.mc.field_1724 == this.mc.field_1687.method_8469(velocity.method_11818())) {
            this.noVelocityY = 10;
         }
      }

   }

   private void work() {
      if (!this.mc.field_1724.method_24828() && this.noVelocityY <= 0) {
         if (this.mc.field_1724.method_18798().field_1351 > (double)0.0F) {
            this.mc.field_1724.method_18798().method_1031((double)0.0F, -5.0E-4, (double)0.0F);
         }

         this.mc.field_1724.method_18798().method_1031((double)0.0F, -0.009400114514191982, (double)0.0F);
      }

      if (!this.mc.field_1724.method_24828() && this.noVelocityY < 8 && MovementUtils.getSpeed() < 0.2177 && this.noVelocityY < 8) {
         MovementUtils.strafe(0.2177F);
      }

      if ((double)Math.abs(this.mc.field_1724.method_31549().method_7252()) < 0.1) {
         this.mc.field_1724.method_31549().method_7248(0.026F);
      } else {
         this.mc.field_1724.method_31549().method_7248(0.0247F);
      }

      if (this.mc.field_1724.method_24828() && PlayerUtils.isMoving()) {
         this.mc.field_1690.field_1903.method_23481(false);
         this.mc.field_1724.method_6043();
         IVec3d v = (IVec3d)this.mc.field_1724.method_18798();
         v.meteor$setY(0.4105000114514192);
         if ((double)Math.abs(this.mc.field_1724.method_31549().method_7252()) < 0.1) {
            MovementUtils.strafe(MovementUtils.getSpeed());
         }
      }

      if (!PlayerUtils.isMoving()) {
         IVec3d v = (IVec3d)this.mc.field_1724.method_18798();
         v.meteor$setXZ((double)0.0F, (double)0.0F);
      }

   }
}
