package dev.Astra.api.events.impl;

import net.minecraft.class_2680;

public class BlockActivateEvent {
   private static final BlockActivateEvent INSTANCE = new BlockActivateEvent();
   public class_2680 blockState;

   private BlockActivateEvent() {
   }

   public static BlockActivateEvent get(class_2680 blockState) {
      INSTANCE.blockState = blockState;
      return INSTANCE;
   }
}
