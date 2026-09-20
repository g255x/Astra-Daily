package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.Event;
import dev.Astra.api.events.impl.JumpEvent;
import dev.Astra.api.events.impl.TravelEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.player.InteractTweaks;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_4050;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1657.class})
public class MixinPlayerEntity implements Wrapper {
   @Inject(
      method = {"canChangeIntoPose"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void poseNotCollide(class_4050 pose, CallbackInfoReturnable<Boolean> cir) {
      if (class_1657.class.cast(this) == mc.field_1724 && !ClientSetting.INSTANCE.crawl.getValue() && pose == class_4050.field_18079) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"getBlockInteractionRange"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getBlockInteractionRangeHook(CallbackInfoReturnable<Double> cir) {
      if (InteractTweaks.INSTANCE.reach()) {
         cir.setReturnValue(InteractTweaks.INSTANCE.blockRange.getValue());
      }

   }

   @Inject(
      method = {"getEntityInteractionRange"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getEntityInteractionRangeHook(CallbackInfoReturnable<Double> cir) {
      if (InteractTweaks.INSTANCE.reach()) {
         cir.setReturnValue(InteractTweaks.INSTANCE.entityRange.getValue());
      }

   }

   @Inject(
      method = {"jump"},
      at = {@At("HEAD")}
   )
   private void onJumpPre(CallbackInfo ci) {
      Astra.EVENT_BUS.post(JumpEvent.get(Event.Stage.Pre));
   }

   @Inject(
      method = {"jump"},
      at = {@At("RETURN")}
   )
   private void onJumpPost(CallbackInfo ci) {
      Astra.EVENT_BUS.post(JumpEvent.get(Event.Stage.Post));
   }

   @Inject(
      method = {"travel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onTravelPre(class_243 movementInput, CallbackInfo ci) {
      class_1657 player = (class_1657)class_1657.class.cast(this);
      if (player == mc.field_1724) {
         TravelEvent event = TravelEvent.get(Event.Stage.Pre, player);
         Astra.EVENT_BUS.post(event);
         if (event.isCancelled()) {
            ci.cancel();
            event = TravelEvent.get(Event.Stage.Post, player);
            Astra.EVENT_BUS.post(event);
         }

      }
   }

   @Inject(
      method = {"travel"},
      at = {@At("RETURN")}
   )
   private void onTravelPost(class_243 movementInput, CallbackInfo ci) {
      class_1657 player = (class_1657)class_1657.class.cast(this);
      if (player == mc.field_1724) {
         TravelEvent event = TravelEvent.get(Event.Stage.Post, player);
         Astra.EVENT_BUS.post(event);
      }
   }
}
