package nekiplay.meteorplus.mixin.minecraft.entity;

import meteordevelopment.meteorclient.systems.modules.Modules;
import nekiplay.meteorplus.features.modules.movement.NoJumpDelay;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {class_1309.class},
   priority = 1001
)
public class LivingEntityMixin {
   @Shadow
   protected boolean field_6282;
   @Shadow
   private int field_6228;

   @Inject(
      method = {"method_6007"},
      at = {@At("HEAD")}
   )
   private void hookTickMovement(CallbackInfo ci) {
      Modules modules = Modules.get();
      if (modules != null) {
         NoJumpDelay noJumpDelay = (NoJumpDelay)modules.get(NoJumpDelay.class);
         if (noJumpDelay != null && noJumpDelay.isActive()) {
            this.field_6228 = 0;
         }
      }

   }
}
