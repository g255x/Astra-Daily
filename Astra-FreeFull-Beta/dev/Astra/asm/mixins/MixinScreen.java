package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.core.impl.ShaderManager;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import java.awt.Color;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_434;
import net.minecraft.class_435;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_4587;
import net.minecraft.class_8030;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_437.class})
public class MixinScreen {
   @Shadow
   public int field_22789;
   @Shadow
   public int field_22790;
   @Shadow
   protected class_310 field_22787;

   @Inject(
      method = {"method_25420"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderInGameBackgroundHook(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      ci.cancel();
      if (this.field_22787.field_1687 == null) {
         boolean isMainMenu = this instanceof class_442;
         boolean isLoading = this instanceof class_435 || this instanceof class_434;
         if (isMainMenu || isLoading) {
            float w = (float)this.field_22789;
            float h = (float)this.field_22790;
            class_4587 m = context.method_51448();
            Render2DUtil.verticalGradient(m, 0.0F, 0.0F, w, h, new Color(40, 10, 60, 255), new Color(200, 20, 150, 255));
            long now = System.currentTimeMillis();
            float phase = (float)(now % 6000L) / 6000.0F;
            float angle = 0.523599F;
            float dx = (float)Math.tan((double)angle) * h;
            float base = -h;
            float spacing = 36.0F;
            float shift = phase * spacing * 4.0F;
            Color c1 = new Color(220, 60, 170, 52);
            Color c2 = new Color(160, 40, 130, 36);

            for(float i = base; i < w; i += spacing) {
               float x0 = i + shift;
               Render2DUtil.drawLine(m, x0, 0.0F, x0 + dx, h, c1.getRGB());
            }

            for(float var30 = base + spacing / 2.0F; var30 < w; var30 += spacing) {
               float x0 = var30 + shift * 0.8F;
               Render2DUtil.drawLine(m, x0, 0.0F, x0 + dx, h, c2.getRGB());
            }

            float hSpacing = 64.0F;
            Color c3 = new Color(255, 255, 255, 18);

            for(float y = 0.0F; y <= h; y += hSpacing) {
               Render2DUtil.drawLine(m, 0.0F, y, w, y, c3.getRGB());
            }

            if (!Astra.SHADER.fullNullCheck()) {
               try {
                  ManagedShaderEffect gradient = Astra.SHADER.getShader(ShaderManager.Shader.Gradient);
                  gradient.setUniformValue("alpha2", 0.2F);
                  gradient.setUniformValue("rgb", 0.78F, 0.05F, 0.59F);
                  gradient.setUniformValue("rgb1", 0.56F, 0.06F, 0.68F);
                  gradient.setUniformValue("rgb2", 0.93F, 0.12F, 0.63F);
                  gradient.setUniformValue("rgb3", 0.64F, 0.0F, 0.64F);
                  gradient.setUniformValue("step", 180.0F);
                  gradient.setUniformValue("radius", 2.0F);
                  gradient.setUniformValue("quality", 1.0F);
                  gradient.setUniformValue("divider", 150.0F);
                  gradient.setUniformValue("maxSample", 10.0F);
                  gradient.setUniformValue("resolution", w, h);
                  float t = (float)(now % 100000L) / 1000.0F;
                  gradient.setUniformValue("time", t * 300.0F);
                  gradient.render(this.field_22787.method_60646().method_60637(true));
               } catch (Exception var28) {
               }
            }

            return;
         }

         this.method_57728(context, delta);
      }

      if (ClientSetting.INSTANCE.darkening.getValue() && !(this instanceof class_442)) {
         this.method_57735(context);
      }

      if (this.field_22787.field_1687 != null && ClickGui.getInstance().tint.booleanValue) {
         context.method_25296(0, 0, this.field_22789, this.field_22790, ClickGui.getInstance().tint.getValue().getRGB(), ClickGui.getInstance().endColor.getValue().getRGB());
      }

   }

   @Shadow
   protected void method_57728(class_332 context, float delta) {
   }

   @Shadow
   protected void method_57735(class_332 context) {
   }

   @Shadow
   public void method_25419() {
   }

   @Shadow
   public class_8030 method_48202() {
      return null;
   }

   @Shadow
   protected <T extends class_364 & class_4068> T method_37063(T drawableElement) {
      return null;
   }
}
