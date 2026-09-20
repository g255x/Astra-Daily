package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.render.ViewModel;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({class_759.class})
public abstract class MixinHeldItemRendererEatShake {
   @ModifyArgs(
      method = {"method_3218"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_4587;method_46416(FFF)V",
   ordinal = 0
)
   )
   private void onApplyEatOrDrinkTransformation(Args args) {
      if (ViewModel.INSTANCE != null && ViewModel.INSTANCE.isOn() && ViewModel.INSTANCE.eatShakeMultiplier.getValue() != (double)1.0F) {
         float currentY = (Float)args.get(1);
         args.set(1, currentY * (float)ViewModel.INSTANCE.eatShakeMultiplier.getValue());
      }

   }
}
