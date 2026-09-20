package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.misc.ShulkerViewer;
import java.util.List;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_2480;
import net.minecraft.class_2561;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2480.class})
public class MixinShulkerBoxBlock {
   @Inject(
      method = {"method_9568"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAppendTooltip(class_1799 stack, class_1792.class_9635 context, List<class_2561> tooltip, class_1836 options, CallbackInfo ci) {
      if (ShulkerViewer.INSTANCE.isOn() && ShulkerViewer.INSTANCE.toolTips.getValue()) {
         ci.cancel();
      }

   }
}
