package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.Xray;
import net.minecraft.class_1087;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_5819;
import net.minecraft.class_778;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_778.class})
public abstract class MixinBlockModelRenderer {
   @Inject(
      method = {"method_3361", "method_3373"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderSmooth(class_1920 world, class_1087 model, class_2680 state, class_2338 pos, class_4587 matrices, class_4588 vertexConsumer, boolean cull, class_5819 random, long seed, int overlay, CallbackInfo info) {
      if (Xray.shouldBlock(state)) {
         info.cancel();
      }

   }
}
