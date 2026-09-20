package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.ClickBlockEvent;
import dev.Astra.api.events.impl.InteractBlockEvent;
import dev.Astra.api.events.impl.InteractItemEvent;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_3965;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_636.class})
public class MixinClientPlayerInteractionManager {
   @Shadow
   private class_1799 field_3718;

   @ModifyVariable(
      method = {"method_2922"},
      at = @At("STORE")
   )
   private class_1799 stack(class_1799 stack) {
      return stack;
   }

   @ModifyConstant(
      method = {"method_2902"},
      constant = {@Constant(
   intValue = 5
)}
   )
   private int MiningCooldownFix(int value) {
      return value;
   }

   @Inject(
      method = {"method_2919"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookInteractItem(class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
      InteractItemEvent event = InteractItemEvent.getPre(hand);
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         cir.setReturnValue(class_1269.field_5811);
      }

   }

   @Inject(
      method = {"method_2919"},
      at = {@At("RETURN")}
   )
   private void hookInteractItemReturn(class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
      Astra.EVENT_BUS.post(InteractItemEvent.getPost(hand));
   }

   @Inject(
      method = {"method_2896"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookInteractBlock(class_746 player, class_1268 hand, class_3965 hitResult, CallbackInfoReturnable<class_1269> cir) {
      InteractBlockEvent event = InteractBlockEvent.getPre(hand);
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         cir.setReturnValue(class_1269.field_5811);
      }

   }

   @Inject(
      method = {"method_2896"},
      at = {@At("RETURN")}
   )
   private void hookInteractBlockReturn(class_746 player, class_1268 hand, class_3965 hitResult, CallbackInfoReturnable<class_1269> cir) {
      Astra.EVENT_BUS.post(InteractBlockEvent.getPost(hand));
   }

   @Inject(
      method = {"method_2925"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookCancelBlockBreaking(CallbackInfo callbackInfo) {
   }

   @Inject(
      method = {"method_2910"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAttackBlock(class_2338 pos, class_2350 direction, CallbackInfoReturnable<Boolean> cir) {
      ClickBlockEvent event = ClickBlockEvent.get(pos, direction);
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         cir.setReturnValue(false);
      }

   }
}
