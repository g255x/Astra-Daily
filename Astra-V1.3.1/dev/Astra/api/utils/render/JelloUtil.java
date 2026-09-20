package dev.Astra.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.utils.Wrapper;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;

public class JelloUtil implements Wrapper {
   private static float prevCircleStep;
   private static float circleStep;

   public static void drawJello(class_4587 matrix, class_1297 target, Color color) {
      double cs = (double)(prevCircleStep + (circleStep - prevCircleStep) * mc.method_60646().method_60637(true));
      double prevSinAnim = absSinAnimation(cs - (double)0.45F);
      double sinAnim = absSinAnimation(cs);
      double x = target.field_6014 + (target.method_23317() - target.field_6014) * (double)mc.method_60646().method_60637(true) - mc.method_1561().field_4686.method_19326().method_10216();
      double y = target.field_6036 + (target.method_23318() - target.field_6036) * (double)mc.method_60646().method_60637(true) - mc.method_1561().field_4686.method_19326().method_10214() + prevSinAnim * (double)target.method_17682();
      double z = target.field_5969 + (target.method_23321() - target.field_5969) * (double)mc.method_60646().method_60637(true) - mc.method_1561().field_4686.method_19326().method_10215();
      double nextY = target.field_6036 + (target.method_23318() - target.field_6036) * (double)mc.method_60646().method_60637(true) - mc.method_1561().field_4686.method_19326().method_10214() + sinAnim * (double)target.method_17682();
      matrix.method_22903();
      RenderSystem.enableBlend();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      class_289 tessellator = class_289.method_1348();
      class_287 bufferBuilder = tessellator.method_60827(class_5596.field_27380, class_290.field_1576);
      RenderSystem.setShader(class_757::method_34540);

      for(int i = 0; i <= 30; ++i) {
         float cos = (float)(x + Math.cos((double)i * 6.28 / (double)30.0F) * (target.method_5829().field_1320 - target.method_5829().field_1323 + (target.method_5829().field_1324 - target.method_5829().field_1321)) * (double)0.5F);
         float sin = (float)(z + Math.sin((double)i * 6.28 / (double)30.0F) * (target.method_5829().field_1320 - target.method_5829().field_1323 + (target.method_5829().field_1324 - target.method_5829().field_1321)) * (double)0.5F);
         bufferBuilder.method_22918(matrix.method_23760().method_23761(), cos, (float)nextY, sin).method_39415(color.getRGB());
         bufferBuilder.method_22918(matrix.method_23760().method_23761(), cos, (float)y, sin).method_39415(ColorUtil.injectAlpha(color, 0).getRGB());
      }

      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      RenderSystem.enableDepthTest();
      matrix.method_22909();
   }

   public static void updateJello() {
      prevCircleStep = circleStep;
      circleStep += 0.15F;
   }

   private static double absSinAnimation(double input) {
      return Math.abs((double)1.0F + Math.sin(input)) / (double)2.0F;
   }
}
