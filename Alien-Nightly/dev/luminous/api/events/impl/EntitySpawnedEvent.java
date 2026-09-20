package dev.luminous.api.events.impl;

import net.minecraft.class_1297;

public class EntitySpawnedEvent {
   private static final EntitySpawnedEvent INSTANCE = new EntitySpawnedEvent();
   private class_1297 entity;

   private EntitySpawnedEvent() {
   }

   public static EntitySpawnedEvent get(class_1297 player) {
      INSTANCE.entity = player;
      return INSTANCE;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
