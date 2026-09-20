package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import dev.luminous.api.events.eventbus.Cancelable;
import net.minecraft.class_1297;

@Cancelable
public class ElytraTransformEvent extends Event {
   private final class_1297 entity;

   public ElytraTransformEvent(class_1297 entity) {
      this.entity = entity;
   }

   public class_1297 getEntity() {
      return this.entity;
   }
}
