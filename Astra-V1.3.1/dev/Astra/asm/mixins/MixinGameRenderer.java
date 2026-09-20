package dev.Astra.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.api.utils.world.InteractUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.player.Freecam;
import dev.Astra.mod.modules.impl.player.InteractTweaks;
import dev.Astra.mod.modules.impl.render.AspectRatio;
import dev.Astra.mod.modules.impl.render.Chams;
import dev.Astra.mod.modules.impl.render.Fov;
import dev.Astra.mod.modules.impl.render.HighLight;
import dev.Astra.mod.modules.impl.render.NoRender;
import dev.Astra.mod.modules.impl.render.Zoom;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1675;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_5636;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_9779;
import net.minecraft.class_239.class_240;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_757.class})
public class MixinGameRenderer {
   @Shadow
   @Final
   class_310 field_4015;
   @Shadow
   private float field_4019;
   @Shadow
   private float field_3999;
   @Shadow
   private boolean field_4001;
   @Shadow
   private float field_4005;
   @Shadow
   private float field_3988;
   @Shadow
   private float field_4004;
   @Shadow
   private float field_4025;
   @Unique
   private class_1297 cameraEntity;
   @Unique
   private float originalYaw;
   @Unique
   private float originalPitch;

   @Shadow
   private static class_239 method_56154(class_239 hitResult, class_243 cameraPos, double interactionRange) {
      class_243 vec3d = hitResult.method_17784();
      if (!vec3d.method_24802(cameraPos, interactionRange)) {
         class_243 vec3d2 = hitResult.method_17784();
         class_2350 direction = class_2350.method_10142(vec3d2.field_1352 - cameraPos.field_1352, vec3d2.field_1351 - cameraPos.field_1351, vec3d2.field_1350 - cameraPos.field_1350);
         return class_3965.method_17778(vec3d2, direction, class_2338.method_49638(vec3d2));
      } else {
         return hitResult;
      }
   }

   @Inject(
      method = {"showFloatingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onShowFloatingItem(class_1799 floatingItem, CallbackInfo info) {
      if (floatingItem.method_7909() == class_1802.field_8288 && NoRender.INSTANCE.isOn() && NoRender.INSTANCE.totem.getValue()) {
         info.cancel();
      }

   }

   @Redirect(
      method = {"renderWorld"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"
),
      require = 0
   )
   private float applyCameraTransformationsMathHelperLerpProxy(float delta, float first, float second) {
      return NoRender.INSTANCE.isOn() && NoRender.INSTANCE.nausea.getValue() ? 0.0F : class_3532.method_16439(delta, first, second);
   }

   @Inject(
      method = {"tiltViewWhenHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tiltViewWhenHurtHook(class_4587 matrices, float tickDelta, CallbackInfo ci) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.hurtCam.getValue()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"shouldRenderBlockOutline"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void hookOutline(CallbackInfoReturnable<Boolean> cir) {
      if (HighLight.INSTANCE.isOn()) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
   opcode = 180,
   ordinal = 0
)},
      method = {"renderWorld"}
   )
   void render3dHook(class_9779 tickCounter, CallbackInfo ci) {
      if (!Module.nullCheck()) {
         class_4184 camera = class_310.method_1551().field_1773.method_19418();
         class_4587 matrixStack = new class_4587();
         matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
         matrixStack.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
         TextUtil.lastProjMat.set(RenderSystem.getProjectionMatrix());
         TextUtil.lastModMat.set(RenderSystem.getModelViewMatrix());
         TextUtil.lastWorldSpaceMatrix.set(matrixStack.method_23760().method_23761());
         Astra.FPS.record();
         Astra.MODULE.render3D(matrixStack);
      }
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"renderWorld"}
   )
   void render3dTail(class_9779 tickCounter, CallbackInfo ci) {
      if (Chams.INSTANCE.isOn() && Chams.INSTANCE.hand.booleanValue) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   @Inject(
      method = {"getFov(Lnet/minecraft/client/render/Camera;FZ)D"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getFov(class_4184 camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> ci) {
      if (!this.field_4001 && (Fov.INSTANCE.isOn() || Zoom.on)) {
         double d = (double)70.0F;
         if (changingFov) {
            if (Fov.INSTANCE.isOn()) {
               double fov = Fov.INSTANCE.fov.getValue();
               if (Zoom.on) {
                  ci.setReturnValue(Math.min(Math.max(fov - Zoom.INSTANCE.currentFov, (double)1.0F), (double)177.0F));
               } else {
                  ci.setReturnValue(fov);
               }

               return;
            }

            d = (double)(Integer)this.field_4015.field_1690.method_41808().method_41753();
            d *= (double)class_3532.method_16439(tickDelta, this.field_3999, this.field_4019);
            if (Zoom.on) {
               d = Math.min(Math.max(d - Zoom.INSTANCE.currentFov, (double)1.0F), (double)177.0F);
            }
         } else if (Fov.INSTANCE.isOn()) {
            ci.setReturnValue(Fov.INSTANCE.itemFov.getValue());
            return;
         }

         if (camera.method_19331() instanceof class_1309 && ((class_1309)camera.method_19331()).method_29504()) {
            float f = Math.min((float)((class_1309)camera.method_19331()).field_6213 + tickDelta, 20.0F);
            d /= (double)((1.0F - 500.0F / (f + 500.0F)) * 2.0F + 1.0F);
         }

         class_5636 cameraSubmersionType;
         if ((cameraSubmersionType = camera.method_19334()) == class_5636.field_27885 || cameraSubmersionType == class_5636.field_27886) {
            d *= class_3532.method_16436((Double)this.field_4015.field_1690.method_42454().method_41753(), (double)1.0F, (double)0.85714287F);
         }

         ci.setReturnValue(d);
      }

   }

   @Inject(
      method = {"getNightVisionStrength"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void getNightVisionStrengthHook(class_1309 entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
      class_1293 statusEffectInstance = entity.method_6112(class_1294.field_5925);
      if (statusEffectInstance == null) {
         cir.setReturnValue(1.0F);
      }

   }

   @Inject(
      method = {"renderWorld"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V",
   shift = Shift.AFTER
)}
   )
   public void postRender3dHook(class_9779 tickCounter, CallbackInfo ci) {
      Astra.SHADER.renderShaders();
   }

   @Inject(
      method = {"getBasicProjectionMatrix"},
      at = {@At("TAIL")},
      cancellable = true
   )
   public void getBasicProjectionMatrixHook(double fov, CallbackInfoReturnable<Matrix4f> cir) {
      if (AspectRatio.INSTANCE.isOn()) {
         class_4587 matrixStack = new class_4587();
         matrixStack.method_23760().method_23761().identity();
         if (this.field_4005 != 1.0F) {
            matrixStack.method_46416(this.field_3988, -this.field_4004, 0.0F);
            matrixStack.method_22905(this.field_4005, this.field_4005, 1.0F);
         }

         matrixStack.method_23760().method_23761().mul((new Matrix4f()).setPerspective((float)(fov * (double)((float)Math.PI / 180F)), AspectRatio.INSTANCE.ratio.getValueFloat(), 0.05F, this.field_4025 * 4.0F));
         cir.setReturnValue(matrixStack.method_23760().method_23761());
      }

   }

   @Inject(
      method = {"findCrosshairTarget"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void findCrosshairTarget(class_1297 camera, double blockInteractionRange, double entityInteractionRange, float tickDelta, CallbackInfoReturnable<class_239> cir) {
      if (Freecam.INSTANCE.isOn()) {
         cir.setReturnValue(InteractUtil.getRtxTarget(Freecam.INSTANCE.getFakeYaw(), Freecam.INSTANCE.getFakePitch(), Freecam.INSTANCE.getFakeX(), Freecam.INSTANCE.getFakeY(), Freecam.INSTANCE.getFakeZ()));
      } else {
         double d = Math.max(blockInteractionRange, entityInteractionRange);
         double e = class_3532.method_33723(d);
         class_243 vec3d = camera.method_5836(tickDelta);
         InteractTweaks.INSTANCE.isActive = InteractTweaks.INSTANCE.ghostHand();
         class_239 hitResult = camera.method_5745(d, tickDelta, false);
         InteractTweaks.INSTANCE.isActive = false;
         double f = hitResult.method_17784().method_1025(vec3d);
         if (hitResult.method_17783() != class_240.field_1333) {
            e = f;
            d = Math.sqrt(f);
         }

         class_243 vec3d2 = camera.method_5828(tickDelta);
         class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * d, vec3d2.field_1351 * d, vec3d2.field_1350 * d);
         class_238 box = camera.method_5829().method_18804(vec3d2.method_1021(d)).method_1009((double)1.0F, (double)1.0F, (double)1.0F);
         if (!InteractTweaks.INSTANCE.noEntityTrace()) {
            class_3966 entityHitResult = class_1675.method_18075(camera, vec3d, vec3d3, box, (entity) -> !entity.method_7325() && entity.method_5863(), e);
            cir.setReturnValue(entityHitResult != null && entityHitResult.method_17784().method_1025(vec3d) < f ? method_56154(entityHitResult, vec3d, entityInteractionRange) : method_56154(hitResult, vec3d, blockInteractionRange));
         } else {
            cir.setReturnValue(method_56154(hitResult, vec3d, blockInteractionRange));
         }

      }
   }
}
