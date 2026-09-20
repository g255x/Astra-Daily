package nekiplay.meteorplus.features.modules.movement.nofall.modes;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IPlayerMoveC2SPacket;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallMode;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallModes;
import nekiplay.meteorplus.mixin.minecraft.entity.PlayerMoveC2SPacketAccessor;
import net.minecraft.class_1304;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2848;
import net.minecraft.class_2848.class_2849;

public class No_Ground_Elytra extends NoFallMode {
   public No_Ground_Elytra() {
      super(NoFallModes.No_Ground_Elytra);
   }

   public void onActivate() {
      FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
      if (!elytra.found()) {
         ChatUtils.error("Elytra not found, this bypass for working need elytra", new Object[0]);
      }

   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (this.mc.field_1724.field_6017 > (double)2.0F) {
         FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
         if (elytra.found()) {
            int slot = elytra.slot();
            if (this.mc.field_1724.method_6118(class_1304.field_6174).method_7909() != class_1802.field_8833) {
               InvUtils.move().from(slot).toArmor(2);
            }
         }
      }

   }

   public void onSendPacket(PacketEvent.Send event) {
      class_2596 var3 = event.packet;
      if (var3 instanceof IPlayerMoveC2SPacket move) {
         PlayerMoveC2SPacketAccessor move2 = (PlayerMoveC2SPacketAccessor)move;
         if (move2.getOnGround()) {
            this.mc.field_1724.field_3944.method_52787(new class_2848(this.mc.field_1724, class_2849.field_12982));
            move2.setOnGround(false);
         }
      }

   }
}
