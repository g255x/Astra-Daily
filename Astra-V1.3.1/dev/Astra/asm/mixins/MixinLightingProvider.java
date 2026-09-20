package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.render.NoRender;
import net.minecraft.class_2338;
import net.minecraft.class_310;
import net.minecraft.class_3568;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_3568.class})
public class MixinLightingProvider {
   private boolean shouldBlockLightUpdates() {
      if (NoRender.INSTANCE == null) {
         return false;
      } else if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.lightsUpdate.getValue()) {
         class_310 mc = class_310.method_1551();
         return mc != null && mc.method_18854();
      } else {
         return false;
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"checkBlock"},
      cancellable = true
   )
   public void checkBlock(class_2338 pos, CallbackInfo ci) {
      if (this.shouldBlockLightUpdates()) {
         ci.cancel();
      }

   }

   @Inject(
      at = {@At("HEAD")},
      method = {"doLightUpdates"},
      cancellable = true
   )
   public void doLightUpdates(CallbackInfoReturnable<Integer> cir) {
      if (this.shouldBlockLightUpdates()) {
         cir.setReturnValue(0);
         cir.cancel();
      }

   }
}
