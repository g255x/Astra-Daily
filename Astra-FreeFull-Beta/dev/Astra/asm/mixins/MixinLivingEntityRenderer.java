package dev.Astra.asm.mixins;

import dev.Astra.core.impl.RotationManager;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_922.class})
public abstract class MixinLivingEntityRenderer<T extends class_1309, M extends class_583<T>> {
   @Unique
   private class_1309 lastEntity;
   @Unique
   private float originalYaw;
   @Unique
   private float originalHeadYaw;
   @Unique
   private float originalBodyYaw;
   @Unique
   private float originalPitch;
   @Unique
   private float originalPrevYaw;
   @Unique
   private float originalPrevHeadYaw;
   @Unique
   private float originalPrevBodyYaw;

   @Inject(
      method = {"method_3936*", "method_4054*"},
      at = {@At("HEAD")}
   )
   public void onRenderPre(T livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      if (class_310.method_1551().field_1724 != null && livingEntity == class_310.method_1551().field_1724 && ClientSetting.INSTANCE.rotations.getValue()) {
         this.originalYaw = livingEntity.method_36454();
         this.originalHeadYaw = livingEntity.field_6241;
         this.originalBodyYaw = livingEntity.field_6283;
         this.originalPitch = livingEntity.method_36455();
         this.originalPrevYaw = livingEntity.field_5982;
         this.originalPrevHeadYaw = livingEntity.field_6259;
         this.originalPrevBodyYaw = livingEntity.field_6220;
         livingEntity.method_36456(RotationManager.getRenderYawOffset());
         livingEntity.field_6241 = RotationManager.getRotationYawHead();
         livingEntity.field_6283 = RotationManager.getRenderYawOffset();
         livingEntity.method_36457(RotationManager.getRenderPitch());
         livingEntity.field_5982 = RotationManager.getPrevRenderYawOffset();
         livingEntity.field_6259 = RotationManager.getPrevRotationYawHead();
         livingEntity.field_6220 = RotationManager.getPrevRenderYawOffset();
         livingEntity.field_6004 = RotationManager.getPrevRenderPitch();
      }

      this.lastEntity = livingEntity;
   }

   @Inject(
      method = {"method_3936*", "method_4054*"},
      at = {@At("TAIL")}
   )
   public void onRenderPost(T livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      if (class_310.method_1551().field_1724 != null && livingEntity == class_310.method_1551().field_1724 && ClientSetting.INSTANCE.rotations.getValue()) {
         livingEntity.method_36456(this.originalYaw);
         livingEntity.field_6241 = this.originalHeadYaw;
         livingEntity.field_6283 = this.originalBodyYaw;
         livingEntity.method_36457(this.originalPitch);
         livingEntity.field_5982 = this.originalPrevYaw;
         livingEntity.field_6259 = this.originalPrevHeadYaw;
         livingEntity.field_6220 = this.originalPrevBodyYaw;
         livingEntity.field_6004 = this.originalPitch;
      }

   }
}
