package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.PlaySoundEvent;
import net.minecraft.class_1113;
import net.minecraft.class_1140;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1140.class})
public class MixinSoundSystem {
   @Inject(
      method = {"method_4854(Lnet/minecraft/class_1113;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPlay(class_1113 soundInstance, CallbackInfo info) {
      PlaySoundEvent event = PlaySoundEvent.get(soundInstance);
      Alien.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         info.cancel();
      }

   }
}
