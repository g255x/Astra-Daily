package nekiplay.meteorplus.features.modules.movement.nofall.modes;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.PlayerMoveC2SPacketAccessor;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallMode;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallModes;
import nekiplay.meteorplus.utils.MovementUtils;
import net.minecraft.class_2828;

public class Vulcan extends NoFallMode {
   private boolean vulCanNoFall = false;
   private boolean vulCantNoFall = false;
   private boolean nextSpoof = false;
   private boolean doSpoof = false;

   public Vulcan() {
      super(NoFallModes.Vulcan);
   }

   public void onActivate() {
      this.vulCanNoFall = false;
      this.vulCantNoFall = false;
      this.nextSpoof = false;
      this.doSpoof = false;
   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (!this.vulCanNoFall && this.mc.field_1724.field_6017 > (double)3.25F) {
         this.vulCanNoFall = true;
      }

      if (this.vulCanNoFall && this.mc.field_1724.method_24828() && this.vulCantNoFall) {
         this.vulCantNoFall = false;
      }

      if (!this.vulCantNoFall) {
         if (this.nextSpoof) {
            this.mc.field_1724.method_18798().method_1031((double)0.0F, -0.1, (double)0.0F);
            this.mc.field_1724.field_6017 = (double)-0.1F;
            MovementUtils.strafe(0.3F);
            this.nextSpoof = false;
         }

         if (this.mc.field_1724.field_6017 > (double)3.5625F) {
            this.mc.field_1724.field_6017 = (double)0.0F;
            this.doSpoof = true;
            this.nextSpoof = true;
         }

      }
   }

   public void onSendPacket(PacketEvent.Send event) {
      if (event.packet instanceof class_2828) {
         class_2828 packet = (class_2828)event.packet;
         PlayerMoveC2SPacketAccessor accessor = (PlayerMoveC2SPacketAccessor)packet;
         accessor.meteor$setOnGround(true);
         this.doSpoof = false;
         accessor.meteor$setY((double)Math.round(this.mc.field_1724.method_73189().field_1351 * (double)2.0F) / (double)2.0F);
         this.mc.field_1724.method_5814(this.mc.field_1724.method_73189().field_1352, ((class_2828)event.packet).method_12268(this.mc.field_1724.method_73189().field_1351), this.mc.field_1724.method_73189().field_1350);
      }

   }
}
