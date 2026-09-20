package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.FireworkShooterRotationEvent;
import dev.Astra.api.events.impl.RemoveFireworkEvent;
import dev.Astra.api.utils.Wrapper;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1671;
import net.minecraft.class_2398;
import net.minecraft.class_243;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1671.class})
public class MixinFireworkRocketEntity implements Wrapper {
   @Shadow
   private int field_7613;

   @Inject(
      method = {"tick"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;updateRotation()V",
   shift = Shift.AFTER
)},
      cancellable = true
   )
   private void hookTickPre(CallbackInfo ci) {
      class_1671 rocketEntity = (class_1671)class_1671.class.cast(this);
      RemoveFireworkEvent removeFireworkEvent = RemoveFireworkEvent.get(rocketEntity);
      Astra.EVENT_BUS.post(removeFireworkEvent);
      if (removeFireworkEvent.isCancelled()) {
         ci.cancel();
         if (this.field_7613 == 0 && !rocketEntity.method_5701()) {
            mc.field_1687.method_43128((class_1657)null, rocketEntity.method_23317(), rocketEntity.method_23318(), rocketEntity.method_23321(), class_3417.field_14702, class_3419.field_15256, 3.0F, 1.0F);
         }

         ++this.field_7613;
         if (mc.field_1687.field_9236) {
            mc.field_1687.method_8406(class_2398.field_11248, rocketEntity.method_23317(), rocketEntity.method_23318(), rocketEntity.method_23321(), mc.field_1687.field_9229.method_43059() * 0.05, -rocketEntity.method_18798().field_1351 * (double)0.5F, mc.field_1687.field_9229.method_43059() * 0.05);
         }
      }

   }

   @Redirect(
      method = {"tick"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"
),
      require = 0
   )
   public class_243 hook(class_1309 instance) {
      FireworkShooterRotationEvent event = FireworkShooterRotationEvent.get(instance, instance.method_36454(), instance.method_36455());
      Astra.EVENT_BUS.post(event);
      return event.isCancelled() ? event.getRotationVector() : instance.method_5720();
   }
}
