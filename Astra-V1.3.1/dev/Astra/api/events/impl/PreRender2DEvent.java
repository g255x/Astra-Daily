package dev.Astra.api.events.impl;

import net.minecraft.class_332;

public class PreRender2DEvent {
   private static final PreRender2DEvent INSTANCE = new PreRender2DEvent();
   public class_332 drawContext;

   public static PreRender2DEvent get(class_332 drawContext) {
      INSTANCE.drawContext = drawContext;
      return INSTANCE;
   }
}
