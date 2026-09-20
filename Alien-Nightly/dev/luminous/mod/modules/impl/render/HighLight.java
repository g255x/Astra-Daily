package dev.luminous.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_265;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_239.class_240;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;

public class HighLight extends Module {
   public static HighLight INSTANCE;
   private final BooleanSetting depth = this.add(new BooleanSetting("Depth", true));
   private final ColorSetting fill = this.add((new ColorSetting("Fill", new Color(255, 0, 0, 50))).injectBoolean(true));
   private final ColorSetting boxColor = this.add((new ColorSetting("Box", new Color(255, 0, 0, 100))).injectBoolean(true));

   public HighLight() {
      super("HighLight", Module.Category.Render);
      INSTANCE = this;
      this.setChinese("方块高亮");
   }

   public void onRender3D(class_4587 matrixStack) {
      if (mc.field_1765.method_17783() == class_240.field_1332) {
         class_239 var3 = mc.field_1765;
         if (var3 instanceof class_3965) {
            class_3965 hitResult = (class_3965)var3;
            if (this.fill.booleanValue || this.boxColor.booleanValue) {
               class_265 shape = mc.field_1687.method_8320(hitResult.method_17777()).method_26218(mc.field_1687, hitResult.method_17777());
               if (shape == null) {
                  return;
               }

               if (shape.method_1110()) {
                  return;
               }

               class_238 box = shape.method_1107().method_996(hitResult.method_17777()).method_1014(0.001);
               box = box.method_997(mc.field_1773.method_19418().method_19326().method_22882());
               RenderSystem.enableBlend();
               if (!this.depth.getValue()) {
                  RenderSystem.disableDepthTest();
               } else {
                  RenderSystem.enableDepthTest();
               }

               Matrix4f matrix = matrixStack.method_23760().method_23761();
               if (this.fill.booleanValue) {
                  Color color = this.fill.getValue();
                  float a = (float)color.getAlpha() / 255.0F;
                  float r = (float)color.getRed() / 255.0F;
                  float g = (float)color.getGreen() / 255.0F;
                  float b = (float)color.getBlue() / 255.0F;
                  RenderSystem.setShader(class_757::method_34540);
                  class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  class_286.method_43433(bufferBuilder.method_60800());
               }

               if (this.depth.getValue()) {
                  RenderSystem.disableDepthTest();
               }

               if (this.boxColor.booleanValue) {
                  Color color = this.boxColor.getValue();
                  float a = (float)color.getAlpha() / 255.0F;
                  float r = (float)color.getRed() / 255.0F;
                  float g = (float)color.getGreen() / 255.0F;
                  float b = (float)color.getBlue() / 255.0F;
                  RenderSystem.setShader(class_757::method_34540);
                  class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_29344, class_290.field_1576);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
                  bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
                  class_286.method_43433(bufferBuilder.method_60800());
               }

               RenderSystem.enableDepthTest();
               RenderSystem.disableBlend();
            }
         }
      }

   }
}
