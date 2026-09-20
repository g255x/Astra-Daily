package nekiplay.meteorplus.mixin.minecraft.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import meteordevelopment.meteorclient.MeteorClient;
import nekiplay.main.events.PlayerUseMultiplierEvent;
import net.minecraft.class_241;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
   value = {class_746.class},
   priority = 1003
)
public abstract class ClientPlayerEntityMixin {
   @WrapOperation(
      method = {"method_67270"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_241;method_35582(F)Lnet/minecraft/class_241;",
   ordinal = 1
)}
   )
   private class_241 hookCustomMultiplier(class_241 instance, float value, Operation<class_241> original) {
      PlayerUseMultiplierEvent playerUseMultiplier = new PlayerUseMultiplierEvent(value, value);
      MeteorClient.EVENT_BUS.post(playerUseMultiplier);
      return new class_241(instance.field_1343 * playerUseMultiplier.getSideways(), instance.field_1342 * playerUseMultiplier.getForward());
   }
}
