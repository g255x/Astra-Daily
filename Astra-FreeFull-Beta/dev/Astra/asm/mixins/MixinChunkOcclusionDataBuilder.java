package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.render.Xray;
import net.minecraft.class_2338;
import net.minecraft.class_852;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_852.class})
public abstract class MixinChunkOcclusionDataBuilder {
   @Inject(
      method = {"method_3682"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onMarkClosed(class_2338 pos, CallbackInfo info) {
      Xray xray = Xray.INSTANCE;
      if (xray != null && xray.isOn()) {
         info.cancel();
      }

   }
}
