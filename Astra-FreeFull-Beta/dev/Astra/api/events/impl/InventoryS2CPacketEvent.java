package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_2649;

public class InventoryS2CPacketEvent extends Event {
   private static final InventoryS2CPacketEvent INSTANCE = new InventoryS2CPacketEvent();
   public class_2649 packet;

   private InventoryS2CPacketEvent() {
   }

   public static InventoryS2CPacketEvent get(class_2649 hand) {
      INSTANCE.packet = hand;
      INSTANCE.setCancelled(false);
      return INSTANCE;
   }
}
