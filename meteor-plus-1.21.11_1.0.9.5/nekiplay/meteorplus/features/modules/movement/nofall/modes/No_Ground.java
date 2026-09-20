package nekiplay.meteorplus.features.modules.movement.nofall.modes;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixininterface.IPlayerMoveC2SPacket;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallMode;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallModes;
import nekiplay.meteorplus.mixin.minecraft.entity.PlayerMoveC2SPacketAccessor;
import net.minecraft.class_2596;

public class No_Ground extends NoFallMode {
   public No_Ground() {
      super(NoFallModes.No_Ground);
   }

   public void onSendPacket(PacketEvent.Send event) {
      class_2596 var3 = event.packet;
      if (var3 instanceof IPlayerMoveC2SPacket move) {
         PlayerMoveC2SPacketAccessor move2 = (PlayerMoveC2SPacketAccessor)move;
         if (move2.getOnGround()) {
            move2.setOnGround(false);
         }
      }

   }
}
