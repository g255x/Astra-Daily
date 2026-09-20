package nekiplay.meteorplus.features.modules.movement.nofall.modes;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixin.PlayerMoveC2SPacketAccessor;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallMode;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallModes;
import net.minecraft.class_243;
import net.minecraft.class_2828;

public class Verus extends NoFallMode {
   public Verus() {
      super(NoFallModes.Verus);
   }

   public void onSendPacket(PacketEvent.Send event) {
      if (event.packet instanceof class_2828) {
         class_2828 packet = (class_2828)event.packet;
         PlayerMoveC2SPacketAccessor accessor = (PlayerMoveC2SPacketAccessor)packet;
         if (this.mc.field_1724.field_6017 > 3.35) {
            accessor.meteor$setOnGround(true);
            this.mc.field_1724.field_6017 = (double)0.0F;
            class_243 vel = this.mc.field_1724.method_18798();
            this.mc.field_1724.method_18800(vel.field_1352, (double)0.0F, vel.field_1350);
         }
      }

   }
}
