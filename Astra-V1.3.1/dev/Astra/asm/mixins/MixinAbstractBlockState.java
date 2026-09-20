package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.render.Ambience;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_4970.class_4971.class})
public class MixinAbstractBlockState {
   @Inject(
      method = {"getLuminance"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getLuminanceHook(CallbackInfoReturnable<Integer> cir) {
      if (!Module.nullCheck()) {
         if (Ambience.INSTANCE.customLuminance.getValue()) {
            cir.setReturnValue(Ambience.INSTANCE.luminance.getValueInt());
         }

      }
   }
}
