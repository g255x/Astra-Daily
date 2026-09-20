package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_1671;

public class RemoveFireworkEvent extends Event {
   public static final RemoveFireworkEvent instance = new RemoveFireworkEvent();
   private class_1671 entity;

   private RemoveFireworkEvent() {
   }

   public static RemoveFireworkEvent get(class_1671 entity) {
      instance.entity = entity;
      instance.setCancelled(false);
      return instance;
   }

   public class_1671 getRocketEntity() {
      return this.entity;
   }
}
