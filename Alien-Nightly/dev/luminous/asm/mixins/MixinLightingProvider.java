package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.NoRender;
import net.minecraft.class_2338;
import net.minecraft.class_3568;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_3568.class})
public class MixinLightingProvider {
   @Inject(
      at = {@At("HEAD")},
      method = {"method_15513"},
      cancellable = true
   )
   public void checkBlock(class_2338 pos, CallbackInfo ci) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.lightsUpdate.getValue()) {
         ci.cancel();
      }

   }

   @Inject(
      at = {@At("RETURN")},
      method = {"method_15516"},
      cancellable = true
   )
   public void doLightUpdates(CallbackInfoReturnable<Integer> cir) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.lightsUpdate.getValue()) {
         cir.setReturnValue(0);
      }

   }
}
