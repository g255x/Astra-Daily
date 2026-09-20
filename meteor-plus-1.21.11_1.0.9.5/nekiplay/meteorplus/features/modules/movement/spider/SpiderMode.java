package nekiplay.meteorplus.features.modules.movement.spider;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.class_310;

public class SpiderMode {
   protected final class_310 mc = class_310.method_1551();
   protected final SpiderPlus settings = (SpiderPlus)Modules.get().get(SpiderPlus.class);
   private final SpiderModes type;

   public SpiderMode(SpiderModes type) {
      this.type = type;
   }

   public void onSendPacket(PacketEvent.Send event) {
   }

   public void onSentPacket(PacketEvent.Sent event) {
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
