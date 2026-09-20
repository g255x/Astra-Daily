package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.LerpToEvent;
import dev.luminous.api.events.impl.SprintEvent;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.NoSlow;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.impl.player.AntiEffects;
import dev.luminous.mod.modules.impl.render.ViewModel;
import net.minecraft.class_1291;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1320;
import net.minecraft.class_1322;
import net.minecraft.class_1324;
import net.minecraft.class_1937;
import net.minecraft.class_310;
import net.minecraft.class_5131;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1309.class})
public abstract class MixinLivingEntity extends class_1297 {
   @Final
   @Shadow
   private static class_1322 field_6231;
   @Unique
   private boolean previousElytra = false;
   @Unique
   private long lastLerp = 0L;

   public MixinLivingEntity(class_1299<?> type, class_1937 world) {
      super(type, world);
   }

   @Shadow
   public @Nullable class_1324 method_5996(class_6880<class_1320> attribute) {
      return this.method_6127().method_45329(attribute);
   }

   @Shadow
   public class_5131 method_6127() {
      return null;
   }

   @Shadow
   public abstract void method_5650(class_1297.class_5529 var1);

   @Inject(
      method = {"method_6028"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
      if (ViewModel.INSTANCE.isOn() && ViewModel.INSTANCE.slowAnimation.getValue()) {
         info.setReturnValue(ViewModel.INSTANCE.slowAnimationVal.getValueInt());
      }

   }

   @Inject(
      method = {"method_6128"},
      at = {@At("TAIL")},
      cancellable = true
   )
   public void recastOnLand(CallbackInfoReturnable<Boolean> cir) {
      boolean elytra = (Boolean)cir.getReturnValue();
      if (this.previousElytra && !elytra && ElytraFly.INSTANCE.isOn() && ElytraFly.INSTANCE.mode.is(ElytraFly.Mode.Bounce)) {
         cir.setReturnValue(ElytraFly.recastElytra(class_310.method_1551().field_1724));
      }

      this.previousElytra = elytra;
   }

   @Redirect(
      method = {"method_6091"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1309;method_6059(Lnet/minecraft/class_6880;)Z"
),
      require = 0
   )
   private boolean travelEffectHook(class_1309 instance, class_6880<class_1291> effect) {
      if (AntiEffects.INSTANCE.isOn()) {
         if (effect == class_1294.field_5906 && AntiEffects.INSTANCE.slowFalling.getValue()) {
            return false;
         }

         if (effect == class_1294.field_5902 && AntiEffects.INSTANCE.levitation.getValue()) {
            return false;
         }
      }

      return instance.method_6059(effect);
   }

   @Redirect(
      method = {"method_26318"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1309;method_6101()Z"
),
      require = 0
   )
   public boolean climbingHook(class_1309 instance) {
      return Velocity.INSTANCE.isOn() && Velocity.INSTANCE.noClimb.getValue() && class_1309.class.cast(this) == class_310.method_1551().field_1724 ? false : instance.method_6101();
   }

   @Redirect(
      method = {"method_18801"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1309;method_6101()Z"
),
      require = 0
   )
   public boolean climbingHook2(class_1309 instance) {
      return NoSlow.INSTANCE.climb() && class_1309.class.cast(this) == class_310.method_1551().field_1724 ? false : instance.method_6101();
   }

   @Inject(
      method = {"method_5759"},
      at = {@At("HEAD")}
   )
   private void lerpToHook(double x, double y, double z, float yRot, float xRot, int steps, CallbackInfo ci) {
      Alien.EVENT_BUS.post(LerpToEvent.get((class_1309)class_1309.class.cast(this), x, y, z, yRot, xRot, this.lastLerp));
      this.lastLerp = System.currentTimeMillis();
   }

   @Inject(
      method = {"method_5728"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setSprintingHook(boolean sprinting, CallbackInfo ci) {
      if (class_1309.class.cast(this) == class_310.method_1551().field_1724) {
         SprintEvent event = SprintEvent.get();
         Alien.EVENT_BUS.post(event);
         if (event.isCancelled()) {
            ci.cancel();
            sprinting = event.isSprint();
            super.method_5728(sprinting);
            class_1324 entityAttributeInstance = this.method_5996(class_5134.field_23719);
            entityAttributeInstance.method_6200(field_6231.comp_2447());
            if (sprinting) {
               entityAttributeInstance.method_26835(field_6231);
            }
         }
      }

   }
}
