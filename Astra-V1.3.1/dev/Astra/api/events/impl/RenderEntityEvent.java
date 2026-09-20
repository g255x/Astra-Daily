package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_1297;

public class RenderEntityEvent extends Event {
   private static final RenderEntityEvent INSTANCE = new RenderEntityEvent();
   private class_1297 entity;

   private RenderEntityEvent() {
   }

   public static RenderEntityEvent get(class_1297 entity) {
      INSTANCE.entity = entity;
      INSTANCE.setCancelled(false);
      return INSTANCE;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
