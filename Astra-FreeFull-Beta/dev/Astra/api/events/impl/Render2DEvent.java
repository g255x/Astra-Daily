package dev.Astra.api.events.impl;

import net.minecraft.class_332;

public class Render2DEvent {
   private static final Render2DEvent INSTANCE = new Render2DEvent();
   public class_332 drawContext;
   public float tickDelta;

   public static Render2DEvent get(class_332 drawContext, float tickDelta) {
      INSTANCE.drawContext = drawContext;
      INSTANCE.tickDelta = tickDelta;
      return INSTANCE;
   }
}
