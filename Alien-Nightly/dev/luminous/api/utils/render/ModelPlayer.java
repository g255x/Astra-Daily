package dev.luminous.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.asm.accessors.ILivingEntity;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_5602;
import net.minecraft.class_5617;
import net.minecraft.class_591;
import net.minecraft.class_630;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ModelPlayer extends class_591<class_1657> {
   public final class_1657 player;
   private static final Vector4f pos1 = new Vector4f();
   private static final Vector4f pos2 = new Vector4f();
   private static final Vector4f pos3 = new Vector4f();
   private static final Vector4f pos4 = new Vector4f();

   public ModelPlayer(class_1657 player) {
      super((new class_5617.class_5618(Wrapper.mc.method_1561(), Wrapper.mc.method_1480(), Wrapper.mc.method_1541(), Wrapper.mc.method_1561().method_43336(), Wrapper.mc.method_1478(), Wrapper.mc.method_31974(), Wrapper.mc.field_1772)).method_32167(class_5602.field_27577), false);
      this.player = player;
      this.field_3482.field_3665 = false;
      this.field_3479.field_3665 = false;
      this.field_3484.field_3665 = false;
      this.field_3486.field_3665 = false;
      this.field_3483.field_3665 = false;
      this.field_3394.field_3665 = false;
      this.method_2838().method_41924(new Vector3f(-0.05F, -0.05F, -0.05F));
      this.field_3400 = player.method_18276();
   }

   public void render(class_4587 matrices, ColorSetting fill, ColorSetting line) {
      this.render(matrices, fill, line, (double)1.0F, (double)0.0F, (double)1.0F, (double)0.0F, false, false);
   }

   public void render(class_4587 matrices, ColorSetting fill, ColorSetting line, double alpha, double yOffset, double scale, double yaw, boolean noLimb, boolean forceSneaking) {
      if (forceSneaking) {
         this.field_3400 = true;
      }

      double x = this.player.method_23317() - Wrapper.mc.method_1561().field_4686.method_19326().method_10216();
      double y = this.player.method_23318() - Wrapper.mc.method_1561().field_4686.method_19326().method_10214() + yOffset;
      double z = this.player.method_23321() - Wrapper.mc.method_1561().field_4686.method_19326().method_10215();
      matrices.method_22903();
      matrices.method_46416((float)x, (float)y, (float)z);
      matrices.method_22907(class_7833.field_40716.rotation(MathUtil.rad(180.0F - this.player.field_6283 + (float)yaw)));
      this.field_3447 = this.player.method_6055(1.0F);
      float j = ((ILivingEntity)this.player).getLeaningPitch();
      if (this.player.method_6128()) {
         float k = this.player.method_36455();
         float l = (float)this.player.method_6003() + this.player.field_6283 + (float)yaw;
         float m = class_3532.method_15363(l * l / 100.0F, 0.0F, 1.0F);
         if (!this.player.method_6123()) {
            matrices.method_22907(class_7833.field_40714.rotationDegrees(m * (-90.0F - k)));
         }

         class_243 vec3d = this.player.method_5828(1.0F);
         class_243 vec3d2 = this.player.method_18798();
         double d = vec3d2.method_37268();
         double e = vec3d.method_37268();
         if (d > (double)0.0F && e > (double)0.0F) {
            double n = (vec3d2.field_1352 * vec3d.field_1352 + vec3d2.field_1350 * vec3d.field_1350) / Math.sqrt(d * e);
            double o = vec3d2.field_1352 * vec3d.field_1350 - vec3d2.field_1350 * vec3d.field_1352;
            matrices.method_22907(class_7833.field_40716.rotation((float)(Math.signum(o) * Math.acos(n))));
         }
      } else if (j > 0.0F) {
         float kx = this.player.method_36455();
         float lx = this.player.method_5799() ? -90.0F - kx : -90.0F;
         float mx = class_3532.method_16439(j, 0.0F, lx);
         matrices.method_22907(class_7833.field_40714.rotationDegrees(mx));
         if (this.player.method_20232()) {
            matrices.method_46416(0.0F, -1.0F, 0.3F);
         }
      }

      matrices.method_22905(-1.0F, -1.0F, 1.0F);
      matrices.method_46416(0.0F, -1.401F, 0.0F);
      matrices.method_22905((float)scale * 0.93F, (float)scale * 0.93F, (float)scale * 0.93F);
      this.method_17086(this.player, noLimb ? 0.0F : this.player.field_42108.method_48569(), noLimb ? 0.0F : this.player.field_42108.method_48566(), Wrapper.mc.method_60646().method_60637(true));
      this.method_17087(this.player, noLimb ? 0.0F : this.player.field_42108.method_48569(), noLimb ? 0.0F : this.player.field_42108.method_48566(), (float)this.player.field_6012, this.player.field_6241 - this.player.field_6283, this.player.method_36455());
      this.field_3449 = this.player.method_5765();
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      RenderSystem.setShader(class_757::method_34540);
      this.method_22946().forEach((modelPart) -> render(matrices, modelPart, fill, line, alpha, false));
      this.method_22948().forEach((modelPart) -> render(matrices, modelPart, fill, line, alpha, false));
      matrices.method_22909();
      RenderSystem.disableBlend();
      RenderSystem.enableDepthTest();
   }

   public static void render(class_4587 matrices, class_630 part, ColorSetting fill, ColorSetting line, double alpha, boolean texture) {
      if (part.field_3665 && (!part.field_3663.isEmpty() || !part.field_3661.isEmpty())) {
         matrices.method_22903();
         part.method_22703(matrices);

         for(class_630.class_628 cuboid : part.field_3663) {
            render(matrices, cuboid, fill, line, alpha, texture);
         }

         for(class_630 child : part.field_3661.values()) {
            render(matrices, child, fill, line, alpha, texture);
         }

         matrices.method_22909();
      }

   }

   public static void render(class_4587 matrices, class_630.class_628 cuboid, ColorSetting fill, ColorSetting line, double alpha, boolean texture) {
      Matrix4f matrix = matrices.method_23760().method_23761();

      for(class_630.class_593 quad : cuboid.field_3649) {
         pos1.set(quad.field_3502[0].field_3605.x / 16.0F, quad.field_3502[0].field_3605.y / 16.0F, quad.field_3502[0].field_3605.z / 16.0F, 1.0F);
         pos1.mul(matrix);
         pos2.set(quad.field_3502[1].field_3605.x / 16.0F, quad.field_3502[1].field_3605.y / 16.0F, quad.field_3502[1].field_3605.z / 16.0F, 1.0F);
         pos2.mul(matrix);
         pos3.set(quad.field_3502[2].field_3605.x / 16.0F, quad.field_3502[2].field_3605.y / 16.0F, quad.field_3502[2].field_3605.z / 16.0F, 1.0F);
         pos3.mul(matrix);
         pos4.set(quad.field_3502[3].field_3605.x / 16.0F, quad.field_3502[3].field_3605.y / 16.0F, quad.field_3502[3].field_3605.z / 16.0F, 1.0F);
         pos4.mul(matrix);
         if (fill.booleanValue) {
            Color color = fill.getValue();
            float a = (float)((double)((float)color.getAlpha() / 255.0F) * alpha);
            float r = (float)color.getRed() / 255.0F;
            float g = (float)color.getGreen() / 255.0F;
            float b = (float)color.getBlue() / 255.0F;
            class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, texture ? class_290.field_1575 : class_290.field_1576);
            buffer.method_22912(pos1.x, pos1.y, pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos2.x, pos2.y, pos2.z).method_22913(quad.field_3502[1].field_3604, quad.field_3502[1].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos2.x, pos2.y, pos2.z).method_22913(quad.field_3502[1].field_3604, quad.field_3502[1].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos3.x, pos3.y, pos3.z).method_22913(quad.field_3502[2].field_3604, quad.field_3502[2].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos3.x, pos3.y, pos3.z).method_22913(quad.field_3502[2].field_3604, quad.field_3502[2].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos4.x, pos4.y, pos4.z).method_22913(quad.field_3502[3].field_3604, quad.field_3502[3].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos1.x, pos1.y, pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos1.x, pos1.y, pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
            Render3DUtil.endBuilding(buffer);
         }

         if (line.booleanValue) {
            Color color = line.getValue();
            float a = (float)((double)((float)color.getAlpha() / 255.0F) * alpha);
            float r = (float)color.getRed() / 255.0F;
            float g = (float)color.getGreen() / 255.0F;
            float b = (float)color.getBlue() / 255.0F;
            class_287 buffer = class_289.method_1348().method_60827(class_5596.field_29344, texture ? class_290.field_1575 : class_290.field_1576);
            buffer.method_22912(pos1.x, pos1.y, pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos2.x, pos2.y, pos2.z).method_22913(quad.field_3502[1].field_3604, quad.field_3502[1].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos2.x, pos2.y, pos2.z).method_22913(quad.field_3502[1].field_3604, quad.field_3502[1].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos3.x, pos3.y, pos3.z).method_22913(quad.field_3502[2].field_3604, quad.field_3502[2].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos3.x, pos3.y, pos3.z).method_22913(quad.field_3502[2].field_3604, quad.field_3502[2].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos4.x, pos4.y, pos4.z).method_22913(quad.field_3502[3].field_3604, quad.field_3502[3].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos4.x, pos4.y, pos4.z).method_22913(quad.field_3502[3].field_3604, quad.field_3502[3].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(pos1.x, pos1.y, pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
            Render3DUtil.endBuilding(buffer);
         }
      }

   }
}
