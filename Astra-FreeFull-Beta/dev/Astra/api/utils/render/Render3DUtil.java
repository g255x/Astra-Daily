package dev.Astra.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_9799;
import net.minecraft.class_9801;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_327.class_6415;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Render3DUtil implements Wrapper {
   public static void endBuilding(class_287 bb) {
      class_9801 builtBuffer = bb.method_60800();
      if (builtBuffer != null) {
         class_286.method_43433(builtBuffer);
      }

   }

   public static class_4587 matrixFrom(double x, double y, double z) {
      class_4587 matrices = new class_4587();
      class_4184 camera = mc.field_1773.method_19418();
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
      matrices.method_22904(x - camera.method_19326().field_1352, y - camera.method_19326().field_1351, z - camera.method_19326().field_1350);
      return matrices;
   }

   public static void drawText3D(String text, class_243 vec3d, Color color) {
      drawText3D(class_2561.method_30163(text), vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, (double)0.0F, (double)0.0F, (double)1.0F, color.getRGB());
   }

   public static void drawText3D(String text, class_243 vec3d, int color) {
      drawText3D(class_2561.method_30163(text), vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, (double)0.0F, (double)0.0F, (double)1.0F, color);
   }

   public static void drawText3D(class_2561 text, class_243 vec3d, double offX, double offY, double scale, Color color) {
      drawText3D(text, vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, offX, offY, scale, color.getRGB());
   }

   public static void drawText3D(class_2561 text, double x, double y, double z, double offX, double offY, double scale, int color) {
      RenderSystem.disableDepthTest();
      class_4587 matrices = matrixFrom(x, y, z);
      class_4184 camera = mc.field_1773.method_19418();
      matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      RenderSystem.enableBlend();
      matrices.method_22904(offX, offY, (double)0.0F);
      matrices.method_22905(-0.025F * (float)scale, -0.025F * (float)scale, 1.0F);
      int halfWidth = mc.field_1772.method_27525(text) / 2;
      class_4597.class_4598 immediate = class_4597.method_22991(new class_9799(1536));
      mc.field_1772.method_27521(text.getString(), (float)(-halfWidth), 0.0F, -1, true, matrices.method_23760().method_23761(), immediate, class_6415.field_33994, 0, 15728880);
      immediate.method_22993();
      mc.field_1772.method_30882(text.method_27661(), (float)(-halfWidth), 0.0F, color, false, matrices.method_23760().method_23761(), immediate, class_6415.field_33994, 0, 15728880);
      immediate.method_22993();
      RenderSystem.disableBlend();
      RenderSystem.enableDepthTest();
   }

   public static void drawFill(class_4587 matrixStack, class_238 bb, Color fillColor) {
      draw3DBox(matrixStack, bb, fillColor, new Color(0, 0, 0, 0), false, true);
   }

   public static void drawBox(class_4587 matrixStack, class_238 bb, Color outlineColor) {
      draw3DBox(matrixStack, bb, new Color(0, 0, 0, 0), outlineColor, true, false);
   }

   public static void drawBox(class_4587 matrixStack, class_238 bb, Color outlineColor, float lineWidth) {
      draw3DBox(matrixStack, bb, new Color(0, 0, 0, 0), outlineColor, true, false, lineWidth);
   }

   public static void draw3DBox(class_4587 matrixStack, class_238 box, Color fillColor, Color outlineColor) {
      draw3DBox(matrixStack, box, fillColor, outlineColor, true, true);
   }

   public static void draw3DBox(class_4587 matrixStack, class_238 box, Color fillColor, Color outlineColor, boolean outline, boolean fill) {
      draw3DBox(matrixStack, box, fillColor, outlineColor, outline, fill, 1.5F);
   }

   public static void draw3DBox(class_4587 matrixStack, class_238 box, Color fillColor, Color outlineColor, boolean outline, boolean fill, float lineWidth) {
      box = box.method_997(mc.field_1773.method_19418().method_19326().method_22882());
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      Matrix4f matrix = matrixStack.method_23760().method_23761();
      if (outline) {
         float keyCodec = (float)outlineColor.getAlpha() / 255.0F;
         float r = (float)outlineColor.getRed() / 255.0F;
         float g = (float)outlineColor.getGreen() / 255.0F;
         float elementCodec = (float)outlineColor.getBlue() / 255.0F;
         RenderSystem.setShader(class_757::method_34540);
         RenderSystem.lineWidth(lineWidth);
         class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_29344, class_290.field_1576);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         class_286.method_43433(bufferBuilder.method_60800());
      }

      if (fill) {
         float keyCodec = (float)fillColor.getAlpha() / 255.0F;
         float r = (float)fillColor.getRed() / 255.0F;
         float g = (float)fillColor.getGreen() / 255.0F;
         float elementCodec = (float)fillColor.getBlue() / 255.0F;
         RenderSystem.setShader(class_757::method_34540);
         class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, elementCodec, keyCodec);
         bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, elementCodec, keyCodec);
         class_286.method_43433(bufferBuilder.method_60800());
      }

      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }

   public static void drawFadeFill(class_4587 stack, class_238 box, Color c, Color c1) {
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.setShader(class_757::method_34540);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      Matrix4f posMatrix = stack.method_23760().method_23761();
      float minX = (float)(box.field_1323 - mc.method_1561().field_4686.method_19326().method_10216());
      float minY = (float)(box.field_1322 - mc.method_1561().field_4686.method_19326().method_10214());
      float minZ = (float)(box.field_1321 - mc.method_1561().field_4686.method_19326().method_10215());
      float maxX = (float)(box.field_1320 - mc.method_1561().field_4686.method_19326().method_10216());
      float maxY = (float)(box.field_1325 - mc.method_1561().field_4686.method_19326().method_10214());
      float maxZ = (float)(box.field_1324 - mc.method_1561().field_4686.method_19326().method_10215());
      buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB());
      buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB());
      class_286.method_43433(buffer.method_60800());
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   public static void drawLine(class_243 start, class_243 end, Color color) {
      drawLine(start.field_1352, start.method_10214(), start.field_1350, end.method_10216(), end.method_10214(), end.method_10215(), color, 1.0F);
   }

   public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, Color color, float width) {
      RenderSystem.enableBlend();
      class_4587 matrices = matrixFrom(x1, y1, z1);
      RenderSystem.disableCull();
      RenderSystem.setShader(class_757::method_34535);
      RenderSystem.lineWidth(width);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27377, class_290.field_29337);
      vertexLine(matrices, buffer, (double)0.0F, (double)0.0F, (double)0.0F, (double)((float)(x2 - x1)), (double)((float)(y2 - y1)), (double)((float)(z2 - z1)), color);
      class_286.method_43433(buffer.method_60800());
      RenderSystem.enableCull();
      RenderSystem.lineWidth(1.0F);
      RenderSystem.disableBlend();
   }

   public static void vertexLine(class_4587 matrices, class_4588 buffer, double x1, double y1, double z1, double x2, double y2, double z2, Color lineColor) {
      Matrix4f model = matrices.method_23760().method_23761();
      class_4587.class_4665 entry = matrices.method_23760();
      Vector3f normalVec = getNormal((float)x1, (float)y1, (float)z1, (float)x2, (float)y2, (float)z2);
      buffer.method_22918(model, (float)x1, (float)y1, (float)z1).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x(), normalVec.y(), normalVec.z());
      buffer.method_22918(model, (float)x2, (float)y2, (float)z2).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x(), normalVec.y(), normalVec.z());
   }

   public static Vector3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2) {
      float xNormal = x2 - x1;
      float yNormal = y2 - y1;
      float zNormal = z2 - z1;
      float normalSqrt = class_3532.method_15355(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal);
      return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
   }

   public static void drawTargetEsp(class_4587 stack, @NotNull class_1297 target, Color color) {
      ArrayList<class_243> vecs = new ArrayList();
      ArrayList<class_243> vecs1 = new ArrayList();
      ArrayList<class_243> vecs2 = new ArrayList();
      double x = target.field_6014 + (target.method_23317() - target.field_6014) * (double)getTickDelta() - mc.method_1561().field_4686.method_19326().method_10216();
      double y = target.field_6036 + (target.method_23318() - target.field_6036) * (double)getTickDelta() - mc.method_1561().field_4686.method_19326().method_10214();
      double z = target.field_5969 + (target.method_23321() - target.field_5969) * (double)getTickDelta() - mc.method_1561().field_4686.method_19326().method_10215();
      double height = (double)target.method_17682();

      for(int i = 0; i <= 361; ++i) {
         double v = Math.sin(Math.toRadians((double)i));
         double u = Math.cos(Math.toRadians((double)i));
         class_243 vec = new class_243((double)((float)(u * (double)0.5F)), height, (double)((float)(v * (double)0.5F)));
         vecs.add(vec);
         double v1 = Math.sin(Math.toRadians((double)((i + 120) % 360)));
         double u1 = Math.cos(Math.toRadians((double)(i + 120)) % (double)360.0F);
         class_243 vec1 = new class_243((double)((float)(u1 * (double)0.5F)), height, (double)((float)(v1 * (double)0.5F)));
         vecs1.add(vec1);
         double v2 = Math.sin(Math.toRadians((double)((i + 240) % 360)));
         double u2 = Math.cos(Math.toRadians((double)((i + 240) % 360)));
         class_243 vec2 = new class_243((double)((float)(u2 * (double)0.5F)), height, (double)((float)(v2 * (double)0.5F)));
         vecs2.add(vec2);
         height -= (double)0.004F;
      }

      stack.method_22903();
      stack.method_22904(x, y, z);
      RenderSystem.enableBlend();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);
      Matrix4f matrix = stack.method_23760().method_23761();

      for(int j = 0; j < vecs.size() - 1; ++j) {
         float alpha = 1.0F - ((float)j + (float)(System.currentTimeMillis() - Astra.initTime) / 5.0F) % 360.0F / 60.0F;
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs.get(j)).field_1352, (float)((class_243)vecs.get(j)).field_1351, (float)((class_243)vecs.get(j)).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (double)((int)((float)j / 20.0F)), 10, (double)1.0F), (int)(alpha * 255.0F)).getRGB());
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs.get(j + 1)).field_1352, (float)((class_243)vecs.get(j + 1)).field_1351 + 0.1F, (float)((class_243)vecs.get(j + 1)).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (double)((int)((float)j / 20.0F)), 10, (double)1.0F), (int)(alpha * 255.0F)).getRGB());
      }

      endBuilding(bufferBuilder);
      RenderSystem.setShader(class_757::method_34540);
      bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

      for(int var31 = 0; var31 < vecs1.size() - 1; ++var31) {
         float alpha = 1.0F - ((float)var31 + (float)(System.currentTimeMillis() - Astra.initTime) / 5.0F) % 360.0F / 60.0F;
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs1.get(var31)).field_1352, (float)((class_243)vecs1.get(var31)).field_1351, (float)((class_243)vecs1.get(var31)).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (double)((int)((float)var31 / 20.0F)), 10, (double)1.0F), (int)(alpha * 255.0F)).getRGB());
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs1.get(var31 + 1)).field_1352, (float)((class_243)vecs1.get(var31 + 1)).field_1351 + 0.1F, (float)((class_243)vecs1.get(var31 + 1)).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (double)((int)((float)var31 / 20.0F)), 10, (double)1.0F), (int)(alpha * 255.0F)).getRGB());
      }

      endBuilding(bufferBuilder);
      RenderSystem.setShader(class_757::method_34540);
      bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

      for(int var32 = 0; var32 < vecs2.size() - 1; ++var32) {
         float alpha = 1.0F - ((float)var32 + (float)(System.currentTimeMillis() - Astra.initTime) / 5.0F) % 360.0F / 60.0F;
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs2.get(var32)).field_1352, (float)((class_243)vecs2.get(var32)).field_1351, (float)((class_243)vecs2.get(var32)).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (double)((int)((float)var32 / 20.0F)), 10, (double)1.0F), (int)(alpha * 255.0F)).getRGB());
         bufferBuilder.method_22918(matrix, (float)((class_243)vecs2.get(var32 + 1)).field_1352, (float)((class_243)vecs2.get(var32 + 1)).field_1351 + 0.1F, (float)((class_243)vecs2.get(var32 + 1)).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (double)((int)((float)var32 / 20.0F)), 10, (double)1.0F), (int)(alpha * 255.0F)).getRGB());
      }

      endBuilding(bufferBuilder);
      RenderSystem.enableCull();
      stack.method_22904(-x, -y, -z);
      RenderSystem.disableBlend();
      RenderSystem.enableDepthTest();
      stack.method_22909();
   }

   public static float getTickDelta() {
      return mc.method_60646().method_60637(true);
   }
}
