package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.NoRender;
import net.minecraft.class_1534;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_928;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_928.class})
public class MixinPaintingEntityRenderer {
   @Inject(
      at = {@At("HEAD")},
      method = {"method_4075(Lnet/minecraft/class_1534;FFLnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"},
      cancellable = true
   )
   public void render(class_1534 paintingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.lightsUpdate.getValue()) {
         ci.cancel();
      }

   }
}
