package dev.luminous.api.events.impl;

import net.minecraft.class_1657;

public class TotemEvent {
   private static final TotemEvent INSTANCE = new TotemEvent();
   private class_1657 player;

   private TotemEvent() {
   }

   public static TotemEvent get(class_1657 player) {
      INSTANCE.player = player;
      return INSTANCE;
   }

   public class_1657 getPlayer() {
      return this.player;
   }
}
