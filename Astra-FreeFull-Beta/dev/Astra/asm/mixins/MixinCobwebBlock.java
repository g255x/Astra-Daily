package dev.Astra.asm.mixins;

import dev.Astra.api.utils.Wrapper;
import dev.Astra.mod.modules.impl.movement.FastWeb;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2560;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2560.class})
public class MixinCobwebBlock {
   @Inject(
      at = {@At("HEAD")},
      method = {"method_9548"},
      cancellable = true
   )
   private void onGetVelocityMultiplier(class_2680 state, class_1937 world, class_2338 pos, class_1297 entity, CallbackInfo ci) {
      if (FastWeb.INSTANCE.isOn() && (Wrapper.mc.field_1690.field_1832.method_1434() || !FastWeb.INSTANCE.onlySneak.getValue())) {
         if (FastWeb.INSTANCE.mode.is(FastWeb.Mode.Ignore)) {
            ci.cancel();
            entity.method_38785();
         } else if (FastWeb.INSTANCE.mode.is(FastWeb.Mode.Custom)) {
            ci.cancel();
            entity.method_5844(state, new class_243(FastWeb.INSTANCE.xZSlow.getValue() / (double)100.0F, FastWeb.INSTANCE.ySlow.getValue() / (double)100.0F, FastWeb.INSTANCE.xZSlow.getValue() / (double)100.0F));
         }
      }

   }
}
