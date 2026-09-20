package nekiplay.main.events.packets;

import nekiplay.main.events.Cancellable;
import net.minecraft.class_2547;
import net.minecraft.class_2596;

public class PacketEvent {
   public static class Receive extends Cancellable {
      private static final Receive INSTANCE = new Receive();
      public class_2596<?> packet;
      public class_2547 packetListener;

      public static Receive get(class_2596<?> packet, class_2547 listener) {
         INSTANCE.setCancelled(false);
         INSTANCE.packet = packet;
         INSTANCE.packetListener = listener;
         return INSTANCE;
      }
   }

   public static class Send extends Cancellable {
      private static final Send INSTANCE = new Send();
      public class_2596<?> packet;

      public static Send get(class_2596<?> packet) {
         INSTANCE.setCancelled(false);
         INSTANCE.packet = packet;
         return INSTANCE;
      }
   }

   public static class Sent {
      private static final Sent INSTANCE = new Sent();
      public class_2596<?> packet;

      public static Sent get(class_2596<?> packet) {
         INSTANCE.packet = packet;
         return INSTANCE;
      }
   }
}
