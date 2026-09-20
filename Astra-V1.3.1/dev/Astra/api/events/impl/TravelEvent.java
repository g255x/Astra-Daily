package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_1657;

public class TravelEvent extends Event {
   private static final TravelEvent INSTANCE = new TravelEvent();
   private class_1657 entity;

   private TravelEvent() {
   }

   public static TravelEvent get(Event.Stage stage, class_1657 entity) {
      INSTANCE.entity = entity;
      INSTANCE.stage = stage;
      INSTANCE.setCancelled(false);
      return INSTANCE;
   }

   public class_1657 getEntity() {
      return this.entity;
   }
}
