package dev.Astra.api.events.impl;

import net.minecraft.class_1309;

public class ElytraUpdateEvent {
   private final class_1309 entity;
   private boolean cancelled;

   public ElytraUpdateEvent(class_1309 entity) {
      this.entity = entity;
   }

   public class_1309 getEntity() {
      return this.entity;
   }

   public void cancel() {
      this.cancelled = true;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }
}
