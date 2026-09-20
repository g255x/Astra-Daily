package nekiplay.meteorplus.features.modules.combat.velocity.modes;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import nekiplay.meteorplus.features.modules.combat.velocity.VelocityMode;
import nekiplay.meteorplus.features.modules.combat.velocity.VelocityModes;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2846.class_2847;

public class GrimCancel_v2 extends VelocityMode {
   private int skip = 0;
   private boolean canCancel = false;

   public GrimCancel_v2() {
      super(VelocityModes.Grim_Cancel_v2);
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
      if ((packet instanceof class_2743 && ((class_2743)packet).method_11818() == this.mc.field_1724.method_5628() || packet instanceof class_2664) && this.canCancel) {
         event.cancel();
         this.canCancel = true;
      } else if (packet instanceof class_2708) {
         this.skip = 3;
      }

   }

   public void onSendPacket(PacketEvent.Send event) {
      class_2596<?> packet = event.packet;
      if (packet instanceof class_2828) {
         --this.skip;
         if (this.canCancel && this.skip <= 0) {
            this.mc.method_1562().method_52787(new class_2828.class_2830(this.mc.field_1724.method_23317(), this.mc.field_1724.method_23318(), this.mc.field_1724.method_23321(), this.mc.field_1724.method_36454(), this.mc.field_1724.method_36455(), this.mc.field_1724.method_24828(), this.mc.field_1724.field_5976));
            this.mc.method_1562().method_52787(new class_2846(class_2847.field_12973, this.mc.field_1724.method_24515(), class_2350.field_11036));
         }
      }

   }
}
