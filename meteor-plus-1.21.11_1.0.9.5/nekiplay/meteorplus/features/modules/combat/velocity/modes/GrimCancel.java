package nekiplay.meteorplus.features.modules.combat.velocity.modes;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import nekiplay.meteorplus.features.modules.combat.velocity.VelocityMode;
import nekiplay.meteorplus.features.modules.combat.velocity.VelocityModes;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2743;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_8143;
import net.minecraft.class_2846.class_2847;

public class GrimCancel extends VelocityMode {
   private boolean canCancel = false;

   public GrimCancel() {
      super(VelocityModes.Grim_Cancel);
   }

   public void onActivate() {
      this.canCancel = false;
   }

   public void onDeactivate() {
      this.canCancel = false;
   }

   public void onReceivePacket(PacketEvent.Receive event) {
      class_2596<?> packet = event.packet;
      if (packet instanceof class_8143 && ((class_8143)packet).comp_1267() == this.mc.field_1724.method_5628()) {
         this.canCancel = true;
      }

      if ((packet instanceof class_2743 && ((class_2743)packet).method_11818() == this.mc.field_1724.method_5628() || packet instanceof class_2664) && this.canCancel) {
         event.cancel();
         MeteorExecutor.execute(() -> {
            try {
               Thread.sleep(20L);
            } catch (Exception var2) {
            }

            this.mc.method_1562().method_52787(new class_2828.class_2830(this.mc.field_1724.method_23317(), this.mc.field_1724.method_23318(), this.mc.field_1724.method_23321(), this.mc.field_1724.method_36454(), this.mc.field_1724.method_36455(), this.mc.field_1724.method_24828(), this.mc.field_1724.field_5976));
            this.mc.method_1562().method_52787(new class_2846(class_2847.field_12973, this.mc.field_1724.method_24515(), this.mc.field_1724.method_5735().method_10153()));
            this.canCancel = false;
         });
      }

   }
}
