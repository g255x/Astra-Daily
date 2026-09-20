package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.Event;
import dev.luminous.api.events.impl.JumpEvent;
import dev.luminous.api.events.impl.TravelEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.player.InteractTweaks;
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
      method = {"method_52558"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void poseNotCollide(class_4050 pose, CallbackInfoReturnable<Boolean> cir) {
      if (class_1657.class.cast(this) == mc.field_1724 && !ClientSetting.INSTANCE.crawl.getValue() && pose == class_4050.field_18079) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"method_55754"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getBlockInteractionRangeHook(CallbackInfoReturnable<Double> cir) {
      if (InteractTweaks.INSTANCE.reach()) {
         cir.setReturnValue(InteractTweaks.INSTANCE.blockRange.getValue());
      }

   }

   @Inject(
      method = {"method_55755"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getEntityInteractionRangeHook(CallbackInfoReturnable<Double> cir) {
      if (InteractTweaks.INSTANCE.reach()) {
         cir.setReturnValue(InteractTweaks.INSTANCE.entityRange.getValue());
      }

   }

   @Inject(
      method = {"method_6043"},
      at = {@At("HEAD")}
   )
   private void onJumpPre(CallbackInfo ci) {
      Alien.EVENT_BUS.post(JumpEvent.get(Event.Stage.Pre));
   }

   @Inject(
      method = {"method_6043"},
      at = {@At("RETURN")}
   )
   private void onJumpPost(CallbackInfo ci) {
      Alien.EVENT_BUS.post(JumpEvent.get(Event.Stage.Post));
   }

   @Inject(
      method = {"method_6091"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onTravelPre(class_243 movementInput, CallbackInfo ci) {
      class_1657 player = (class_1657)class_1657.class.cast(this);
      if (player == mc.field_1724) {
         TravelEvent event = TravelEvent.get(Event.Stage.Pre, player);
         Alien.EVENT_BUS.post(event);
         if (event.isCancelled()) {
            ci.cancel();
            event = TravelEvent.get(Event.Stage.Post, player);
            Alien.EVENT_BUS.post(event);
         }
      }

   }

   @Inject(
      method = {"method_6091"},
      at = {@At("RETURN")}
   )
   private void onTravelPost(class_243 movementInput, CallbackInfo ci) {
      class_1657 player = (class_1657)class_1657.class.cast(this);
      if (player == mc.field_1724) {
         TravelEvent event = TravelEvent.get(Event.Stage.Post, player);
         Alien.EVENT_BUS.post(event);
      }

   }
}
