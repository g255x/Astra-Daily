package nekiplay.meteorplus.features.modules.world.timer;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import net.minecraft.class_310;

public class TimerMode {
   protected final class_310 mc = class_310.method_1551();
   protected final TimerPlus settings = (TimerPlus)Modules.get().get(TimerPlus.class);
   private final TimerModes type;
   protected Timer timer = (Timer)Modules.get().get(Timer.class);

   public TimerMode(TimerModes type) {
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
