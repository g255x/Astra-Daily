package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.ParticleEvent;
import net.minecraft.class_702;
import net.minecraft.class_703;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_702.class})
public class MixinParticleManager {
   @Inject(
      at = {@At("HEAD")},
      method = {"method_3058(Lnet/minecraft/class_703;)V"},
      cancellable = true
   )
   public void onAddParticle(class_703 particle, CallbackInfo ci) {
      ParticleEvent event = ParticleEvent.get(particle);
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }
}
