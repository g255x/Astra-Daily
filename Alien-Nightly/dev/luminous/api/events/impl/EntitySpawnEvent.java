package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_1297;

public class EntitySpawnEvent extends Event {
   private static final EntitySpawnEvent INSTANCE = new EntitySpawnEvent();
   private class_1297 entity;

   private EntitySpawnEvent() {
   }

   public static EntitySpawnEvent get(class_1297 entity) {
      INSTANCE.entity = entity;
      INSTANCE.setCancelled(false);
      return INSTANCE;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
