package dev.Astra.asm.mixins;

import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.class_156;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4011;
import net.minecraft.class_4071;
import net.minecraft.class_425;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_425.class})
public abstract class MixinSplashOverlay extends class_4071 {
   @Shadow
   private long field_17771;
   @Shadow
   @Final
   private class_310 field_18217;
   @Unique
   private static final float SATURATION = 0.6F;
   @Unique
   private static final float BRIGHTNESS = 0.8F;
   @Unique
   private long startTime = 0L;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void initHook(class_310 var1, class_4011 var2, Consumer<?> var3, boolean var4, CallbackInfo ci) {
      this.startTime = System.currentTimeMillis();
   }

   @Inject(
      method = {"method_25394"},
      at = {@At(
   value = "INVOKE",
   target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableDepthTest()V",
   ordinal = 0,
   shift = Shift.BEFORE,
   remap = false
)}
   )
   private void renderHook(class_332 var1, int var2, int var3, float var4, CallbackInfo ci) {
      int width = this.field_18217.method_22683().method_4486();
      int height = this.field_18217.method_22683().method_4502();
      int alpha = class_3532.method_15386((1.0F - class_3532.method_15363((this.field_17771 > -1L ? (float)(class_156.method_658() - this.field_17771) / 1000.0F : -1.0F) - 1.0F, 0.0F, 1.0F)) * 255.0F);
      long elapsed = System.currentTimeMillis() - this.startTime;
      float hue = (float)(elapsed % 10000L) / 10000.0F;
      Color color = Color.getHSBColor(hue, 0.6F, 0.8F);
      int rgba = (new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha)).getRGB();
      var1.method_25294(0, 0, width, height, rgba);
   }
}
