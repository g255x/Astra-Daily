package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.player.FreeLook;
import dev.Astra.mod.modules.impl.player.Freecam;
import dev.Astra.mod.modules.impl.render.CameraClip;
import dev.Astra.mod.modules.impl.render.MotionCamera;
import net.minecraft.class_1297;
import net.minecraft.class_1922;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({class_4184.class})
public abstract class MixinCamera {
   @Shadow
   private boolean field_18719;

   @Shadow
   protected abstract float method_19318(float var1);

   @ModifyArgs(
      method = {"update"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V",
   ordinal = 0
)
   )
   private void modifyCameraDistance(Args args) {
      if (CameraClip.INSTANCE.isOn()) {
         args.set(0, -this.method_19318((float)CameraClip.INSTANCE.getDistance()));
      }

   }

   @Inject(
      method = {"clipToSpace"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onClipToSpace(float f, CallbackInfoReturnable<Float> cir) {
      if (CameraClip.INSTANCE.isOn()) {
         cir.setReturnValue((float)CameraClip.INSTANCE.getDistance());
      }

   }

   @Inject(
      method = {"update"},
      at = {@At("TAIL")}
   )
   private void updateHook(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
      if (Freecam.INSTANCE.isOn()) {
         this.field_18719 = true;
      }

   }

   @ModifyArgs(
      method = {"update"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"
)
   )
   private void setRotationHook(Args args) {
      if (Freecam.INSTANCE.isOn()) {
         args.setAll(new Object[]{Freecam.INSTANCE.getFakeYaw(), Freecam.INSTANCE.getFakePitch()});
      } else if (FreeLook.INSTANCE.isOn()) {
         args.setAll(new Object[]{FreeLook.INSTANCE.getFakeYaw(), FreeLook.INSTANCE.getFakePitch()});
      }

   }

   @ModifyArgs(
      method = {"update"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"
)
   )
   private void setPosHook(Args args) {
      if (Freecam.INSTANCE.isOn()) {
         args.setAll(new Object[]{Freecam.INSTANCE.getFakeX(), Freecam.INSTANCE.getFakeY(), Freecam.INSTANCE.getFakeZ()});
      } else if (MotionCamera.INSTANCE.on()) {
         args.setAll(new Object[]{MotionCamera.INSTANCE.getFakeX(), MotionCamera.INSTANCE.getFakeY(), MotionCamera.INSTANCE.getFakeZ()});
      }

   }
}
