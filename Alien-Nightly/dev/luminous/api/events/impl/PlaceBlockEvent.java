package dev.luminous.api.events.impl;

import net.minecraft.class_2248;
import net.minecraft.class_2338;

public class PlaceBlockEvent {
   private static final PlaceBlockEvent INSTANCE = new PlaceBlockEvent();
   public class_2338 blockPos;
   public class_2248 block;

   private PlaceBlockEvent() {
   }

   public static PlaceBlockEvent get(class_2338 blockPos, class_2248 block) {
      INSTANCE.blockPos = blockPos;
      INSTANCE.block = block;
      return INSTANCE;
   }
}
