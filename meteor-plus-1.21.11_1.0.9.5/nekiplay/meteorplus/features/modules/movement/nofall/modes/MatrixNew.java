package nekiplay.meteorplus.features.modules.movement.nofall.modes;

import java.util.Iterator;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixin.PlayerMoveC2SPacketAccessor;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallMode;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallModes;
import net.minecraft.class_265;
import net.minecraft.class_2828;

public class MatrixNew extends NoFallMode {
   private Timer timer;

   public MatrixNew() {
      super(NoFallModes.Matrix_New);
   }

   public void onDeactivate() {
      this.timer = (Timer)Modules.get().get(Timer.class);
      this.timer.setOverride((double)1.0F);
   }

   public void onSendPacket(PacketEvent.Send event) {
      if (event.packet instanceof class_2828) {
         class_2828 packet = (class_2828)event.packet;
         PlayerMoveC2SPacketAccessor accessor = (PlayerMoveC2SPacketAccessor)packet;
         this.timer = (Timer)Modules.get().get(Timer.class);
         if (!this.mc.field_1724.method_24828()) {
            if (this.mc.field_1724.field_6017 > 2.69) {
               this.timer.setOverride(0.3);
               accessor.meteor$setOnGround(true);
               this.mc.field_1724.field_6017 = (double)0.0F;
            }

            if (this.mc.field_1724.field_6017 > (double)3.5F) {
               this.timer.setOverride(0.3);
            } else {
               this.timer.setOverride((double)1.0F);
            }
         }

         Iterator<class_265> voxelShapeIterator = this.mc.field_1687.method_8600(this.mc.field_1724, this.mc.field_1724.method_5829().method_989((double)0.0F, this.mc.field_1724.method_18798().field_1351, (double)0.0F)).iterator();

         boolean isEmpty;
         class_265 shape;
         for(isEmpty = true; voxelShapeIterator.hasNext(); isEmpty = shape.method_1110()) {
            shape = (class_265)voxelShapeIterator.next();
         }

         if (!isEmpty && !((class_2828)event.packet).method_12273() && this.mc.field_1724.method_18798().field_1351 < -0.6) {
            accessor.meteor$setOnGround(true);
         }
      }

   }
}
