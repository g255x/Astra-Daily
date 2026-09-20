package dev.Astra.asm.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.Astra.mod.modules.impl.movement.NoSlow;
import dev.Astra.mod.modules.impl.render.Xray;
import net.minecraft.class_1922;
import net.minecraft.class_1935;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_2248.class})
public abstract class MixinBlock implements class_1935 {
   @Inject(
      at = {@At("HEAD")},
      method = {"getVelocityMultiplier()F"},
      cancellable = true
   )
   private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
      if (NoSlow.INSTANCE.soulSand() && cir.getReturnValueF() < 1.0F) {
         cir.setReturnValue(1.0F);
      }

   }

   @ModifyReturnValue(
      method = {"shouldDrawSide"},
      at = {@At("RETURN")}
   )
   private static boolean onShouldDrawSide(boolean original, class_2680 state, class_1922 world, class_2338 pos, class_2350 side, class_2338 blockPos) {
      Xray xray = Xray.INSTANCE;
      return xray.isOn() ? xray.modifyDrawSide(state, world, pos, side, original) : original;
   }
}
