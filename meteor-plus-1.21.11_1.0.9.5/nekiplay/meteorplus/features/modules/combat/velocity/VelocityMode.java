package nekiplay.meteorplus.features.modules.combat.velocity;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.class_310;

public class VelocityMode {
   protected final class_310 mc = class_310.method_1551();
   protected final VelocityPlus settings = (VelocityPlus)Modules.get().get(VelocityPlus.class);
   private final VelocityModes type;

   public VelocityMode(VelocityModes type) {
      this.type = type;
   }

   public void onSendPacket(PacketEvent.Send event) {
   }

   public void onSentPacket(PacketEvent.Sent event) {
   }

   public void onReceivePacket(PacketEvent.Receive event) {
   }

   public void onTickEventPre(TickEvent.Pre event) {
   }

   public void onTickEventPost(TickEvent.Post event) {
   }

   public void onActivate() {
   }

   public void onDeactivate() {
   }
}
