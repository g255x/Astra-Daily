package nekiplay.meteorplus.features.modules.combat.velocity.modes;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import nekiplay.meteorplus.features.modules.combat.velocity.VelocityMode;
import nekiplay.meteorplus.features.modules.combat.velocity.VelocityModes;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2743;
import net.minecraft.class_2828;
import net.minecraft.class_8143;

public class GrimSkip extends VelocityMode {
   private int skip = 0;
   private boolean canCancel = false;

   public GrimSkip() {
      super(VelocityModes.Grim_Skip);
   }

   public void onActivate() {
      this.canCancel = false;
      this.skip = 0;
   }

   public void onDeactivate() {
      this.canCancel = false;
      this.skip = 0;
   }

   public void onReceivePacket(PacketEvent.Receive event) {
      class_2596<?> packet = event.packet;
      if (packet instanceof class_8143 && ((class_8143)packet).comp_1267() == this.mc.field_1724.method_5628()) {
         this.canCancel = true;
      }

      if ((packet instanceof class_2743 && ((class_2743)packet).method_11818() == this.mc.field_1724.method_5628() || packet instanceof class_2664) && this.canCancel) {
         this.skip = 6;
         event.cancel();
      }

   }

   public void onSendPacket(PacketEvent.Send event) {
      class_2596<?> packet = event.packet;
      if (packet instanceof class_2828 && this.skip > 0) {
         --this.skip;
         event.cancel();
      }

   }
}
