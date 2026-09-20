package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.KeyboardInputEvent;
import net.minecraft.class_743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_743.class})
public class MixinKeyboardInput {
   @Inject(
      method = {"method_3129"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_743;field_3903:Z",
   shift = Shift.AFTER,
   opcode = 181
)},
      cancellable = true
   )
   private void keyInput(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
      KeyboardInputEvent event = KeyboardInputEvent.get();
      Alien.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }
}
