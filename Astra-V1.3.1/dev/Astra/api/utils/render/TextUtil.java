package dev.Astra.api.utils.render;

import dev.Astra.api.utils.Wrapper;
import dev.Astra.core.impl.FontManager;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_243;
import net.minecraft.class_332;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class TextUtil implements Wrapper {
   public static final Matrix4f lastProjMat = new Matrix4f();
   public static final Matrix4f lastModMat = new Matrix4f();
   public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();

   public static float getWidth(String s) {
      return (float)mc.field_1772.method_1727(s);
   }

   public static float getHeight() {
      Objects.requireNonNull(mc.field_1772);
      return 9.0F;
   }

   public static void drawStringWithScale(class_332 drawContext, String text, float x, float y, int color, float scale) {
      class_4587 matrixStack = drawContext.method_51448();
      if (scale != 1.0F) {
         matrixStack.method_22903();
         matrixStack.method_22905(scale, scale, 1.0F);
         if (scale > 1.0F) {
            matrixStack.method_46416(-x / scale, -y / scale, 0.0F);
         } else {
            matrixStack.method_46416(x / scale / 2.0F, y / scale / 2.0F, 0.0F);
         }
      }

      drawString(drawContext, text, (double)x, (double)y, color);
      matrixStack.method_22909();
   }

   public static void drawStringScale(class_332 drawContext, String text, float x, float y, int color, float scale, boolean shadow) {
      class_4587 matrixStack = drawContext.method_51448();
      if (scale != 1.0F) {
         matrixStack.method_22903();
         matrixStack.method_22905(scale, scale, 1.0F);
         if (scale > 1.0F) {
            matrixStack.method_46416(-x / scale, -y / scale, 0.0F);
         } else {
            matrixStack.method_46416(x / scale / 2.0F, y / scale / 2.0F, 0.0F);
         }
      }

      drawContext.method_51433(mc.field_1772, text, (int)x, (int)y, color, shadow);
      matrixStack.method_22909();
   }

   public static void drawString(class_332 drawContext, String text, double x, double y, int color) {
      drawString(drawContext, text, x, y, color, false);
   }

   public static void drawString(class_332 drawContext, String text, double x, double y, int color, boolean customFont) {
      drawString(drawContext, text, x, y, color, customFont, true);
   }

   public static void drawString(class_332 drawContext, String text, double x, double y, int color, boolean customFont, boolean shadow) {
      if (customFont) {
         FontManager.ui.drawString(drawContext.method_51448(), text, (double)((int)x), (double)((int)y), color, shadow);
      } else {
         drawContext.method_51433(mc.field_1772, text, (int)x, (int)y, color, shadow);
      }

   }

   public static void drawStringPulse(class_332 drawContext, String text, double x, double y, Color startColor, Color endColor, double speed, int counter, boolean customFont) {
      char[] stringToCharArray = text.toCharArray();
      int index = 0;
      boolean color = false;
      String s = null;

      for(char c : stringToCharArray) {
         if (c == 167) {
            color = true;
         } else if (color) {
            s = c == 'r' ? null : "§" + c;
            color = false;
         } else {
            ++index;
            if (s != null) {
               drawString(drawContext, s + c, x, y, startColor.getRGB(), customFont);
            } else {
               drawString(drawContext, String.valueOf(c), x, y, ColorUtil.pulseColor(startColor, endColor, (double)index, counter, speed).getRGB(), customFont);
            }

            x += customFont ? (double)FontManager.ui.getWidth(String.valueOf(c)) : (double)mc.field_1772.method_1727(String.valueOf(c));
         }
      }

   }

   public static void drawStringPulse(class_332 drawContext, String text, double x, double y, Color startColor, Color endColor, double speed, int counter, boolean customFont, boolean shadow) {
      char[] stringToCharArray = text.toCharArray();
      int index = 0;
      boolean color = false;
      String s = null;

      for(char c : stringToCharArray) {
         if (c == 167) {
            color = true;
         } else if (color) {
            s = c == 'r' ? null : "§" + c;
            color = false;
         } else {
            ++index;
            if (s != null) {
               drawString(drawContext, s + c, x, y, startColor.getRGB(), customFont, shadow);
            } else {
               drawString(drawContext, String.valueOf(c), x, y, ColorUtil.pulseColor(startColor, endColor, (double)index, counter, speed).getRGB(), customFont, shadow);
            }

            x += customFont ? (double)FontManager.ui.getWidth(String.valueOf(c)) : (double)mc.field_1772.method_1727(String.valueOf(c));
         }
      }

   }

   public static class_243 worldSpaceToScreenSpace(class_243 pos) {
      class_4184 camera = mc.method_1561().field_4686;
      int displayHeight = mc.method_22683().method_4507();
      int[] viewport = new int[4];
      GL11.glGetIntegerv(2978, viewport);
      Vector3f target = new Vector3f();
      double deltaX = pos.field_1352 - camera.method_19326().field_1352;
      double deltaY = pos.field_1351 - camera.method_19326().field_1351;
      double deltaZ = pos.field_1350 - camera.method_19326().field_1350;
      Vector4f transformedCoordinates = (new Vector4f((float)deltaX, (float)deltaY, (float)deltaZ, 1.0F)).mul(lastWorldSpaceMatrix);
      Matrix4f matrixProj = new Matrix4f(lastProjMat);
      Matrix4f matrixModel = new Matrix4f(lastModMat);
      matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
      return new class_243((double)target.x / mc.method_22683().method_4495(), (double)((float)displayHeight - target.y) / mc.method_22683().method_4495(), (double)target.z);
   }
}
