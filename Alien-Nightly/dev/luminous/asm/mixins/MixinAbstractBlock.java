package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.AmbientOcclusionEvent;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3726;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_4970.class})
public abstract class MixinAbstractBlock {
   @Inject(
      method = {"method_9575"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetAmbientOcclusionLightLevel(class_2680 state, class_1922 world, class_2338 pos, CallbackInfoReturnable<Float> info) {
      AmbientOcclusionEvent event = (AmbientOcclusionEvent)Alien.EVENT_BUS.post(AmbientOcclusionEvent.get());
      if (event.lightLevel != -1.0F) {
         info.setReturnValue(event.lightLevel);
      }

   }

   @Inject(
      method = {"method_9549"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onComputeNextCollisionBox(class_2680 state, class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
      if (SpeedMine.INSTANCE != null && pos.equals(SpeedMine.getBreakPos()) && SpeedMine.INSTANCE.noCollide.getValue() && SpeedMine.ghost) {
         cir.setReturnValue(class_259.method_1073());
      }

   }
}
