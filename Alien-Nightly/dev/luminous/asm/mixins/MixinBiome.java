package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.Ambience;
import java.awt.Color;
import net.minecraft.class_1959;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1959.class})
public class MixinBiome {
   @Inject(
      method = {"method_24376"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetFogColor(CallbackInfoReturnable<Integer> cir) {
      if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.dimensionColor.booleanValue) {
         Color color = Ambience.INSTANCE.dimensionColor.getValue();
         cir.setReturnValue(color.getRGB());
      }

   }
}
