package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_1297;

public class TickEntityEvent extends Event {
   private static final TickEntityEvent INSTANCE = new TickEntityEvent();
   private class_1297 entity;

   private TickEntityEvent() {
   }

   public static TickEntityEvent get(class_1297 entity) {
      INSTANCE.entity = entity;
      INSTANCE.setCancelled(false);
      return INSTANCE;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
