package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_2586;

public class RenderBlockEntityEvent extends Event {
   private static final RenderBlockEntityEvent INSTANCE = new RenderBlockEntityEvent();
   public class_2586 blockEntity;

   public static RenderBlockEntityEvent get(class_2586 blockEntity) {
      INSTANCE.setCancelled(false);
      INSTANCE.blockEntity = blockEntity;
      return INSTANCE;
   }
}
