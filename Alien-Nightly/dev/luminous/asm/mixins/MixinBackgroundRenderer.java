package dev.luminous.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.mod.modules.impl.render.Ambience;
import dev.luminous.mod.modules.impl.render.NoRender;
import dev.luminous.mod.modules.impl.render.Xray;
import java.awt.Color;
import net.minecraft.class_1291;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_4184;
import net.minecraft.class_638;
import net.minecraft.class_6880;
import net.minecraft.class_758;
import net.minecraft.class_758.class_4596;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_758.class})
public class MixinBackgroundRenderer {
   @Redirect(
      method = {"method_3210"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1309;method_6059(Lnet/minecraft/class_6880;)Z",
   ordinal = 0,
   remap = false
),
      require = 0
   )
   private static boolean nightVisionHook(class_1309 instance, class_6880<class_1291> effect) {
      return Ambience.INSTANCE.isOn() && Ambience.INSTANCE.fullBright.getValue() || instance.method_6059(effect);
   }

   @Inject(
      method = {"method_3211"},
      at = {@At("TAIL")}
   )
   private static void onApplyFog(class_4184 camera, class_758.class_4596 fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
      if (Ambience.INSTANCE.isOn()) {
         if (Ambience.INSTANCE.fog.booleanValue) {
            RenderSystem.setShaderFogColor((float)Ambience.INSTANCE.fog.getValue().getRed() / 255.0F, (float)Ambience.INSTANCE.fog.getValue().getGreen() / 255.0F, (float)Ambience.INSTANCE.fog.getValue().getBlue() / 255.0F, (float)Ambience.INSTANCE.fog.getValue().getAlpha() / 255.0F);
         }

         if (Ambience.INSTANCE.fogDistance.getValue()) {
            RenderSystem.setShaderFogStart(Ambience.INSTANCE.fogStart.getValueFloat());
            RenderSystem.setShaderFogEnd(Ambience.INSTANCE.fogEnd.getValueFloat());
         }
      }

      if ((NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fog.getValue() || Xray.INSTANCE.isOn()) && fogType == class_4596.field_20946) {
         RenderSystem.setShaderFogStart(viewDistance * 4.0F);
         RenderSystem.setShaderFogEnd(viewDistance * 4.25F);
      }

   }

   @Inject(
      method = {"method_3210"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void hookRender(class_4184 camera, float tickDelta, class_638 world, int viewDistance, float skyDarkness, CallbackInfo ci) {
      if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.dimensionColor.booleanValue) {
         Color color = Ambience.INSTANCE.dimensionColor.getValue();
         ci.cancel();
         RenderSystem.clearColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 0.0F);
      }

   }

   @Inject(
      method = {"method_42588(Lnet/minecraft/class_1297;F)Lnet/minecraft/class_758$class_7286;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetFogModifier(class_1297 entity, float tickDelta, CallbackInfoReturnable<Object> info) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.blindness.getValue()) {
         info.setReturnValue((Object)null);
      }

   }
}
