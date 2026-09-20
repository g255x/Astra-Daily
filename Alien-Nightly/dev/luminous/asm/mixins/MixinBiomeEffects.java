package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.Ambience;
import java.awt.Color;
import net.minecraft.class_4763;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_4763.class})
public class MixinBiomeEffects {
   @Inject(
      method = {"method_30810"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetSkyColor(CallbackInfoReturnable<Integer> cir) {
      if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.sky.booleanValue) {
         Color sky = Ambience.INSTANCE.sky.getValue();
         cir.setReturnValue(sky.getRGB());
      }

   }
}
