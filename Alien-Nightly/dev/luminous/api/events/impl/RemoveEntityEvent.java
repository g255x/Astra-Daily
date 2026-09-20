package dev.luminous.api.events.impl;

import net.minecraft.class_1297;

public class RemoveEntityEvent {
   public static final RemoveEntityEvent instance = new RemoveEntityEvent();
   private class_1297 entity;
   private class_1297.class_5529 removalReason;

   private RemoveEntityEvent() {
   }

   public static RemoveEntityEvent get(class_1297 entity, class_1297.class_5529 removalReason) {
      instance.entity = entity;
      instance.removalReason = removalReason;
      return instance;
   }

   public class_1297 getEntity() {
      return this.entity;
   }

   public class_1297.class_5529 getRemovalReason() {
      return this.removalReason;
   }
}
