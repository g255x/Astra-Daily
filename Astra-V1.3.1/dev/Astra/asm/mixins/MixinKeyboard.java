package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.core.impl.CommandManager;
import dev.Astra.mod.modules.impl.client.ClientSetting;
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
      method = {"onKey"},
      at = {@At("HEAD")}
   )
   private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
      try {
         if (action == 1) {
            Astra.MODULE.onKeyPressed(key);
         }

         if (action == 0) {
            Astra.MODULE.onKeyReleased(key);
         }
      } catch (Exception e) {
         e.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            String var10000 = String.valueOf(class_124.field_1079);
            CommandManager.sendMessage(var10000 + "[ERROR] onKey " + e.getMessage());
         }
      }

   }

   @Redirect(
      method = {"onKey"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/util/NarratorManager;isActive()Z"
),
      require = 0
   )
   public boolean hook(class_333 instance) {
      return false;
   }
}
