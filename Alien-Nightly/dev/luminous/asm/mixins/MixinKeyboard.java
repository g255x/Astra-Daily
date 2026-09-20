package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import net.minecraft.class_124;
import net.minecraft.class_309;
import net.minecraft.class_333;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_309.class})
public class MixinKeyboard implements Wrapper {
   @Inject(
      method = {"method_1466"},
      at = {@At("HEAD")}
   )
   private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
      try {
         if (action == 1) {
            Alien.MODULE.onKeyPressed(key);
         }

         if (action == 0) {
            Alien.MODULE.onKeyReleased(key);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            String var10000 = String.valueOf(class_124.field_1079);
            CommandManager.sendMessage(var10000 + "[ERROR] onKey " + var9.getMessage());
         }
      }

   }

   @Redirect(
      method = {"method_1466"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_333;method_1791()Z"
),
      require = 0
   )
   public boolean hook(class_333 instance) {
      return false;
   }
}
