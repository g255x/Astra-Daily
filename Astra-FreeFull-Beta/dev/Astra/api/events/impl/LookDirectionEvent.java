package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_1297;

public class LookDirectionEvent extends Event {
   private static final LookDirectionEvent instance = new LookDirectionEvent();
   private class_1297 entity;
   private double cursorDeltaX;
   private double cursorDeltaY;

   private LookDirectionEvent() {
   }

   public static LookDirectionEvent get(class_1297 entity, double cursorDeltaX, double cursorDeltaY) {
      instance.entity = entity;
      instance.cursorDeltaX = cursorDeltaX;
      instance.cursorDeltaY = cursorDeltaY;
      instance.setCancelled(false);
      return instance;
   }

   public class_1297 getEntity() {
      return this.entity;
   }

   public double getCursorDeltaX() {
      return this.cursorDeltaX;
   }

   public double getCursorDeltaY() {
      return this.cursorDeltaY;
   }
}
