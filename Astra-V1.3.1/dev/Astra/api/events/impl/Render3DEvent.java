package dev.Astra.api.events.impl;

import dev.Astra.api.utils.render.Render3DUtil;
import java.awt.Color;
import net.minecraft.class_238;
import net.minecraft.class_4587;

public class Render3DEvent {
   private static final Render3DEvent INSTANCE = new Render3DEvent();
   public class_4587 matrixStack;
   public float tickDelta;

   public static Render3DEvent get(class_4587 matrixStack, float tickDelta) {
      INSTANCE.matrixStack = matrixStack;
      INSTANCE.tickDelta = tickDelta;
      return INSTANCE;
   }

   public void drawBox(class_238 box, Color color) {
      Render3DUtil.drawBox(this.matrixStack, box, color);
   }

   public void drawFill(class_238 box, Color color) {
      Render3DUtil.drawFill(this.matrixStack, box, color);
   }
}
