package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.LookDirectionEvent;
import dev.Astra.api.events.impl.SprintEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.mod.modules.impl.movement.Velocity;
import dev.Astra.mod.modules.impl.render.NoRender;
import dev.Astra.mod.modules.impl.render.ShaderModule;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({class_1297.class})
public abstract class MixinEntity {
   @Shadow
   private static final int field_29976 = 3;

   @Inject(
      method = {"method_5872"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
      if (class_1297.class.cast(this) == Wrapper.mc.field_1724) {
         LookDirectionEvent lookDirectionEvent = LookDirectionEvent.get((class_1297)class_1297.class.cast(this), cursorDeltaX, cursorDeltaY);
         Astra.EVENT_BUS.post(lookDirectionEvent);
         if (lookDirectionEvent.isCancelled()) {
            ci.cancel();
         }
      }

   }

   @Inject(
      at = {@At("HEAD")},
      method = {"method_5756(Lnet/minecraft/class_1657;)Z"},
      cancellable = true
   )
   private void onIsInvisibleCheck(class_1657 message, CallbackInfoReturnable<Boolean> cir) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.invisible.getValue()) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"method_5851"},
      at = {@At("HEAD")},
      cancellable = true
   )
   void isGlowingHook(CallbackInfoReturnable<Boolean> cir) {
      if (ShaderModule.INSTANCE.isOn()) {
         cir.setReturnValue(ShaderModule.INSTANCE.shouldRender((class_1297)class_1297.class.cast(this)));
      }

   }

   @ModifyArgs(
      method = {"method_5697"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1297;method_5762(DDD)V"
)
   )
   private void pushAwayFromHook(Args args) {
      if (class_1297.class.cast(this) == class_310.method_1551().field_1724 && Velocity.INSTANCE.isOn() && Velocity.INSTANCE.entityPush.getValue()) {
         args.set(0, (double)0.0F);
         args.set(1, (double)0.0F);
         args.set(2, (double)0.0F);
      }

   }

   @Inject(
      method = {"method_5809"},
      at = {@At("HEAD")},
      cancellable = true
   )
   void isOnFireHook(CallbackInfoReturnable<Boolean> cir) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fireEntity.getValue()) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"method_5728"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setSprintingHook(boolean sprinting, CallbackInfo ci) {
      if (class_1297.class.cast(this) == class_310.method_1551().field_1724) {
         SprintEvent event = SprintEvent.get();
         Astra.EVENT_BUS.post(event);
         if (event.isCancelled()) {
            ci.cancel();
            sprinting = event.isSprint();
            this.method_5729(3, sprinting);
         }
      }

   }

   @Shadow
   protected void method_5729(int index, boolean value) {
   }
}
