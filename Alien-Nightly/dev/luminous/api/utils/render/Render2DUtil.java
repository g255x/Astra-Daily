package dev.luminous.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.api.utils.Wrapper;
import java.awt.Color;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;

public class Render2DUtil implements Wrapper {
   public static void rect(class_4587 stack, float x1, float y1, float x2, float y2, int color) {
      rectFilled(stack, x1, y1, x2, y2, color);
   }

   public static void arrow(class_4587 matrixStack, float x, float y, Color color) {
      drawRectWithOutline(matrixStack, x - 1.0F, y - 1.0F, 2.0F, 2.0F, color, Color.BLACK);
   }

   public static void rectFilled(class_4587 matrix, float x1, float y1, float x2, float y2, int color) {
      float f = (float)(color >> 24 & 255) / 255.0F;
      float g = (float)(color >> 16 & 255) / 255.0F;
      float h = (float)(color >> 8 & 255) / 255.0F;
      float j = (float)(color & 255) / 255.0F;
      if (!((double)f <= 0.01)) {
         if (x1 < x2) {
            float i = x1;
            x1 = x2;
            x2 = i;
         }

         if (y1 < y2) {
            float i = y1;
            y1 = y2;
            y2 = i;
         }

         RenderSystem.enableBlend();
         RenderSystem.setShader(class_757::method_34540);
         class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
         bufferBuilder.method_22918(matrix.method_23760().method_23761(), x1, y2, 0.0F).method_22915(g, h, j, f);
         bufferBuilder.method_22918(matrix.method_23760().method_23761(), x2, y2, 0.0F).method_22915(g, h, j, f);
         bufferBuilder.method_22918(matrix.method_23760().method_23761(), x2, y1, 0.0F).method_22915(g, h, j, f);
         bufferBuilder.method_22918(matrix.method_23760().method_23761(), x1, y1, 0.0F).method_22915(g, h, j, f);
         class_286.method_43433(bufferBuilder.method_60800());
         RenderSystem.disableBlend();
      }

   }

   public static void horizontalGradient(class_4587 matrices, float x1, float y1, float x2, float y2, Color startColor, Color endColor) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrix, x1, y1, 0.0F).method_39415(startColor.getRGB());
      bufferBuilder.method_22918(matrix, x1, y2, 0.0F).method_39415(startColor.getRGB());
      bufferBuilder.method_22918(matrix, x2, y2, 0.0F).method_39415(endColor.getRGB());
      bufferBuilder.method_22918(matrix, x2, y1, 0.0F).method_39415(endColor.getRGB());
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static void horizontalGradient(class_4587 matrices, float x1, float y1, float x2, float y2, int startColor, int endColor) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrix, x1, y1, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, x1, y2, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, x2, y2, 0.0F).method_39415(endColor);
      bufferBuilder.method_22918(matrix, x2, y1, 0.0F).method_39415(endColor);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static void verticalGradient(class_4587 matrices, float x1, float y1, float x2, float y2, Color startColor, Color endColor) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrix, x1, y1, 0.0F).method_39415(startColor.getRGB());
      bufferBuilder.method_22918(matrix, x2, y1, 0.0F).method_39415(startColor.getRGB());
      bufferBuilder.method_22918(matrix, x2, y2, 0.0F).method_39415(endColor.getRGB());
      bufferBuilder.method_22918(matrix, x1, y2, 0.0F).method_39415(endColor.getRGB());
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static void verticalGradient(class_4587 matrices, float x1, float y1, float x2, float y2, int startColor, int endColor) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrix, x1, y1, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, x2, y1, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, x2, y2, 0.0F).method_39415(endColor);
      bufferBuilder.method_22918(matrix, x1, y2, 0.0F).method_39415(endColor);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static void drawLine(class_4587 matrices, float x, float y, float x1, float y1, int color) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_29344, class_290.field_1576);
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x1, y1, 0.0F).method_39415(color);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static void drawRectWithOutline(class_4587 matrices, float x, float y, float width, float height, Color c, Color c2) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      buffer.method_22918(matrix, x, y + height, 0.0F).method_39415(c.getRGB());
      buffer.method_22918(matrix, x + width, y + height, 0.0F).method_39415(c.getRGB());
      buffer.method_22918(matrix, x + width, y, 0.0F).method_39415(c.getRGB());
      buffer.method_22918(matrix, x, y, 0.0F).method_39415(c.getRGB());
      class_286.method_43433(buffer.method_60800());
      buffer = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      buffer.method_22918(matrix, x, y + height, 0.0F).method_39415(c2.getRGB());
      buffer.method_22918(matrix, x + width, y + height, 0.0F).method_39415(c2.getRGB());
      buffer.method_22918(matrix, x + width, y, 0.0F).method_39415(c2.getRGB());
      buffer.method_22918(matrix, x, y, 0.0F).method_39415(c2.getRGB());
      buffer.method_22918(matrix, x, y + height, 0.0F).method_39415(c2.getRGB());
      class_286.method_43433(buffer.method_60800());
      RenderSystem.disableBlend();
   }

   public static void drawRect(class_4587 matrices, float x, float y, float width, float height, int c) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrix, x, y + height, 0.0F).method_39415(c);
      bufferBuilder.method_22918(matrix, x + width, y + height, 0.0F).method_39415(c);
      bufferBuilder.method_22918(matrix, x + width, y, 0.0F).method_39415(c);
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(c);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static void drawRect(class_4587 matrices, float x, float y, float width, float height, Color c) {
      drawRect(matrices, x, y, width, height, c.getRGB());
   }

   public static void drawRect(class_332 drawContext, float x, float y, float width, float height, Color c) {
      drawRect(drawContext.method_51448(), x, y, width, height, c);
   }

   public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height) {
      return mouseX >= x && mouseX - width <= x && mouseY >= y && mouseY - height <= y;
   }

   public static void drawGlow(class_4587 matrices, float x, float y, float width, float height, int color) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      int startColor = ColorUtil.injectAlpha(color, 20);
      RenderSystem.setShader(class_757::method_34540);
      RenderSystem.disableCull();
      RenderSystem.enableBlend();
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      float halfWidth = width / 2.0F;
      float halfHeight = height / 2.0F;
      float centerX = x + halfWidth;
      float centerY = y + halfHeight;
      float x2 = x + width;
      float y2 = y + height;
      bufferBuilder.method_22918(matrix, centerX, centerY, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x, centerY, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, centerX, y, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, centerX, centerY, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, centerX, y, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, x2, y, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, x2, centerY, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, centerX, centerY, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x, centerY, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, x, y2, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, centerX, y2, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, centerX, centerY, 0.0F).method_39415(color);
      bufferBuilder.method_22918(matrix, x2, centerY, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, x2, y2, 0.0F).method_39415(startColor);
      bufferBuilder.method_22918(matrix, centerX, y2, 0.0F).method_39415(startColor);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
   }

   public static void drawCircle(class_4587 matrices, float cx, float cy, float r, Color c, int segments) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.disableCull();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);
      bufferBuilder.method_22918(matrix, cx, cy, 0.0F).method_39415(c.getRGB());

      for(int i = 0; i <= segments; ++i) {
         double a = (double)i * (Math.PI * 2D) / (double)segments;
         float x = (float)((double)cx + Math.cos(a) * (double)r);
         float y = (float)((double)cy + Math.sin(a) * (double)r);
         bufferBuilder.method_22918(matrix, x, y, 0.0F).method_39415(c.getRGB());
      }

      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   public static void drawPill(class_4587 matrices, float x, float y, float width, float height, Color c) {
      float r = height / 2.0F;
      drawRect(matrices, x + r, y, width - 2.0F * r, height, c);
      drawCircle(matrices, x + r, y + r, r, c, 64);
      drawCircle(matrices, x + width - r, y + r, r, c, 64);
   }

   public static void drawRoundedStroke(class_4587 matrices, float x, float y, float width, float height, float radius, Color c, int seg) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      float r = Math.min(radius, Math.min(width, height) / 2.0F);
      float x1 = x + r;
      float y1 = y + r;
      float x2 = x + width - r;
      float y2 = y + height - r;

      for(int i = 0; i <= seg; ++i) {
         double a = (-Math.PI / 2D) + (double)i * (Math.PI / 2D) / (double)seg;
         buffer.method_22918(matrix, (float)((double)x2 + Math.cos(a) * (double)r), (float)((double)y1 + Math.sin(a) * (double)r), 0.0F).method_39415(c.getRGB());
      }

      for(int i = 0; i <= seg; ++i) {
         double a = (double)0.0F + (double)i * (Math.PI / 2D) / (double)seg;
         buffer.method_22918(matrix, (float)((double)x2 + Math.cos(a) * (double)r), (float)((double)y2 + Math.sin(a) * (double)r), 0.0F).method_39415(c.getRGB());
      }

      for(int i = 0; i <= seg; ++i) {
         double a = (Math.PI / 2D) + (double)i * (Math.PI / 2D) / (double)seg;
         buffer.method_22918(matrix, (float)((double)x1 + Math.cos(a) * (double)r), (float)((double)y2 + Math.sin(a) * (double)r), 0.0F).method_39415(c.getRGB());
      }

      for(int i = 0; i <= seg; ++i) {
         double a = Math.PI + (double)i * (Math.PI / 2D) / (double)seg;
         buffer.method_22918(matrix, (float)((double)x1 + Math.cos(a) * (double)r), (float)((double)y1 + Math.sin(a) * (double)r), 0.0F).method_39415(c.getRGB());
      }

      class_286.method_43433(buffer.method_60800());
      RenderSystem.disableBlend();
   }

   public static void drawRainbowRoundedStroke(class_4587 matrices, float x, float y, float width, float height, float radius, int seg, float speed, int alpha) {
      Matrix4f matrix = matrices.method_23760().method_23761();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      float r = Math.min(radius, Math.min(width, height) / 2.0F);
      float x1 = x + r;
      float y1 = y + r;
      float x2 = x + width - r;
      float y2 = y + height - r;
      double t = (double)(System.currentTimeMillis() % (long)(1000.0F / Math.max(0.001F, speed))) / (double)(1000.0F / Math.max(0.001F, speed));

      for(int i = 0; i <= seg; ++i) {
         double a = (-Math.PI / 2D) + (double)i * (Math.PI / 2D) / (double)seg;
         float hue = (float)((a + (Math.PI * 2D)) / (Math.PI * 2D) + t) % 1.0F;
         int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
         int argb = alpha << 24 | rgb & 16777215;
         buffer.method_22918(matrix, (float)((double)x2 + Math.cos(a) * (double)r), (float)((double)y1 + Math.sin(a) * (double)r), 0.0F).method_39415(argb);
      }

      for(int i = 0; i <= seg; ++i) {
         double a = (double)0.0F + (double)i * (Math.PI / 2D) / (double)seg;
         float hue = (float)((a + (Math.PI * 2D)) / (Math.PI * 2D) + t) % 1.0F;
         int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
         int argb = alpha << 24 | rgb & 16777215;
         buffer.method_22918(matrix, (float)((double)x2 + Math.cos(a) * (double)r), (float)((double)y2 + Math.sin(a) * (double)r), 0.0F).method_39415(argb);
      }

      for(int i = 0; i <= seg; ++i) {
         double a = (Math.PI / 2D) + (double)i * (Math.PI / 2D) / (double)seg;
         float hue = (float)((a + (Math.PI * 2D)) / (Math.PI * 2D) + t) % 1.0F;
         int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
         int argb = alpha << 24 | rgb & 16777215;
         buffer.method_22918(matrix, (float)((double)x1 + Math.cos(a) * (double)r), (float)((double)y2 + Math.sin(a) * (double)r), 0.0F).method_39415(argb);
      }

      for(int i = 0; i <= seg; ++i) {
         double a = Math.PI + (double)i * (Math.PI / 2D) / (double)seg;
         float hue = (float)((a + (Math.PI * 2D)) / (Math.PI * 2D) + t) % 1.0F;
         int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
         int argb = alpha << 24 | rgb & 16777215;
         buffer.method_22918(matrix, (float)((double)x1 + Math.cos(a) * (double)r), (float)((double)y1 + Math.sin(a) * (double)r), 0.0F).method_39415(argb);
      }

      class_286.method_43433(buffer.method_60800());
      class_287 inner = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      float rIn = Math.max(0.5F, r - 1.2F);

      for(int i = 0; i <= seg; ++i) {
         double a = (-Math.PI / 2D) + (double)i * (Math.PI / 2D) / (double)seg;
         float hue = (float)((a + (Math.PI * 2D)) / (Math.PI * 2D) + t) % 1.0F;
         int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
         int argb = alpha << 24 | rgb & 16777215;
         inner.method_22918(matrix, (float)((double)x2 + Math.cos(a) * (double)rIn), (float)((double)y1 + Math.sin(a) * (double)rIn), 0.0F).method_39415(argb);
      }

      for(int i = 0; i <= seg; ++i) {
         double a = (double)0.0F + (double)i * (Math.PI / 2D) / (double)seg;
         float hue = (float)((a + (Math.PI * 2D)) / (Math.PI * 2D) + t) % 1.0F;
         int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
         int argb = alpha << 24 | rgb & 16777215;
         inner.method_22918(matrix, (float)((double)x2 + Math.cos(a) * (double)rIn), (float)((double)y2 + Math.sin(a) * (double)rIn), 0.0F).method_39415(argb);
      }

      for(int i = 0; i <= seg; ++i) {
         double a = (Math.PI / 2D) + (double)i * (Math.PI / 2D) / (double)seg;
         float hue = (float)((a + (Math.PI * 2D)) / (Math.PI * 2D) + t) % 1.0F;
         int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
         int argb = alpha << 24 | rgb & 16777215;
         inner.method_22918(matrix, (float)((double)x1 + Math.cos(a) * (double)rIn), (float)((double)y2 + Math.sin(a) * (double)rIn), 0.0F).method_39415(argb);
      }

      for(int i = 0; i <= seg; ++i) {
         double a = Math.PI + (double)i * (Math.PI / 2D) / (double)seg;
         float hue = (float)((a + (Math.PI * 2D)) / (Math.PI * 2D) + t) % 1.0F;
         int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
         int argb = alpha << 24 | rgb & 16777215;
         inner.method_22918(matrix, (float)((double)x1 + Math.cos(a) * (double)rIn), (float)((double)y1 + Math.sin(a) * (double)rIn), 0.0F).method_39415(argb);
      }

      class_286.method_43433(inner.method_60800());
      RenderSystem.disableBlend();
   }

   public static void drawRoundedRect(class_4587 matrices, float x, float y, float width, float height, float radius, Color c) {
      if (radius <= 0.0F) {
         drawRect(matrices, x, y, width, height, c);
      } else {
         float r = Math.min(radius, Math.min(width, height) / 2.0F);
         drawRect(matrices, x + r, y, width - 2.0F * r, height, c);
         drawRect(matrices, x, y + r, r, height - 2.0F * r, c);
         drawRect(matrices, x + width - r, y + r, r, height - 2.0F * r, c);
         Matrix4f matrix = matrices.method_23760().method_23761();
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.setShader(class_757::method_34540);
         int seg = 48;
         float cx = x + r;
         float cy = y + r;
         class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);
         buffer.method_22918(matrix, cx, cy, 0.0F).method_39415(c.getRGB());

         for(int i = 0; i <= seg; ++i) {
            double a = Math.PI + (double)i * (Math.PI / 2D) / (double)seg;
            buffer.method_22918(matrix, (float)((double)cx + Math.cos(a) * (double)r), (float)((double)cy + Math.sin(a) * (double)r), 0.0F).method_39415(c.getRGB());
         }

         class_286.method_43433(buffer.method_60800());
         cx = x + width - r;
         cy = y + r;
         buffer = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);
         buffer.method_22918(matrix, cx, cy, 0.0F).method_39415(c.getRGB());

         for(int i = 0; i <= seg; ++i) {
            double a = (Math.PI * 1.5D) + (double)i * (Math.PI / 2D) / (double)seg;
            buffer.method_22918(matrix, (float)((double)cx + Math.cos(a) * (double)r), (float)((double)cy + Math.sin(a) * (double)r), 0.0F).method_39415(c.getRGB());
         }

         class_286.method_43433(buffer.method_60800());
         cx = x + width - r;
         cy = y + height - r;
         buffer = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);
         buffer.method_22918(matrix, cx, cy, 0.0F).method_39415(c.getRGB());

         for(int i = 0; i <= seg; ++i) {
            double a = (double)0.0F + (double)i * (Math.PI / 2D) / (double)seg;
            buffer.method_22918(matrix, (float)((double)cx + Math.cos(a) * (double)r), (float)((double)cy + Math.sin(a) * (double)r), 0.0F).method_39415(c.getRGB());
         }

         class_286.method_43433(buffer.method_60800());
         cx = x + r;
         cy = y + height - r;
         buffer = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);
         buffer.method_22918(matrix, cx, cy, 0.0F).method_39415(c.getRGB());

         for(int i = 0; i <= seg; ++i) {
            double a = (Math.PI / 2D) + (double)i * (Math.PI / 2D) / (double)seg;
            buffer.method_22918(matrix, (float)((double)cx + Math.cos(a) * (double)r), (float)((double)cy + Math.sin(a) * (double)r), 0.0F).method_39415(c.getRGB());
         }

         class_286.method_43433(buffer.method_60800());
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
      }

   }

   public static void drawRound(class_4587 matrices, float x, float y, float width, float height, float radius, Color color) {
      drawRoundedRect(matrices, x, y, width, height, radius, color);
   }

   public static void drawRound(class_4587 matrices, int x, int y, float width, float height, float radius, Color color) {
      drawRound(matrices, (float)x, (float)y, width, height, radius, color);
   }

   public static void drawDropShadow(class_4587 matrices, float x, float y, float width, float height, float radius) {
   }

   public static void setupRender() {
      RenderSystem.enableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void endRender() {
      RenderSystem.disableBlend();
   }
}
