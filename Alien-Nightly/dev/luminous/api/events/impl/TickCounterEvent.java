package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import dev.luminous.api.events.eventbus.Cancelable;

@Cancelable
public class TickCounterEvent extends Event {
   private float ticks;

   public float getTicks() {
      return this.ticks;
   }

   public void setTicks(float ticks) {
      this.ticks = ticks;
   }
}
