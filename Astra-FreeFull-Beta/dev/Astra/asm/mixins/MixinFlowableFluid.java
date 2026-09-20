package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.movement.Velocity;
import java.util.Iterator;
import net.minecraft.class_2350;
import net.minecraft.class_3609;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({class_3609.class})
public class MixinFlowableFluid {
   @Redirect(
      method = {"method_15782"},
      at = @At(
   value = "INVOKE",
   target = "Ljava/util/Iterator;hasNext()Z",
   ordinal = 0
),
      require = 0
   )
   private boolean getVelocity_hasNext(Iterator<class_2350> var9) {
      return Velocity.INSTANCE.isOn() && Velocity.INSTANCE.waterPush.getValue() ? false : var9.hasNext();
   }
}
