package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.render.NoRender;
import net.minecraft.class_332;
import net.minecraft.class_374;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_374.class})
public class MixinToastManager {
   @Inject(
      method = {"method_1996"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookDraw(class_332 context, CallbackInfo ci) {
      if (NoRender.INSTANCE.guiToast.getValue()) {
         ci.cancel();
      }

   }
}
