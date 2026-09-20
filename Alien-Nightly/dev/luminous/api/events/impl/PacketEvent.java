package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_2596;

public class PacketEvent extends Event {
   private final class_2596<?> packet;

   public PacketEvent(class_2596<?> packet, Event.Stage stage) {
      super(stage);
      this.packet = packet;
   }

   public class_2596<?> getPacket() {
      return this.packet;
   }

   public static class Receive extends PacketEvent {
      public Receive(class_2596<?> packet) {
         super(packet, Event.Stage.Pre);
      }
   }

   public static class Send extends PacketEvent {
      public Send(class_2596<?> packet) {
         super(packet, Event.Stage.Pre);
      }
   }

   public static class Sent extends PacketEvent {
      public Sent(class_2596<?> packet) {
         super(packet, Event.Stage.Post);
      }
   }
}
