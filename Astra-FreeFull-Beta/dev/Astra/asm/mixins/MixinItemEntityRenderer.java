package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.render.NoRender;
import net.minecraft.class_1087;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_5617;
import net.minecraft.class_5819;
import net.minecraft.class_811;
import net.minecraft.class_897;
import net.minecraft.class_916;
import net.minecraft.class_918;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_916.class})
public abstract class MixinItemEntityRenderer extends class_897<class_1542> {
   @Final
   @Shadow
   private class_918 field_4726;
   @Final
   @Shadow
   private class_5819 field_4725;

   protected MixinItemEntityRenderer(class_5617.class_5618 ctx) {
      super(ctx);
   }

   @Unique
   private static int getRenderedAmount(class_1799 stackSize) {
      int count = stackSize.method_7947();
      if (count <= 1) {
         return 1;
      } else if (count <= 16) {
         return 2;
      } else if (count <= 32) {
         return 3;
      } else {
         return count <= 48 ? 4 : 5;
      }
   }

   @Inject(
      method = {"method_3936*", "method_3996*"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void render(class_1542 itemEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fastItem.getValue()) {
         matrixStack.method_22903();
         class_1799 itemStack = itemEntity.method_6983();
         long j = itemStack.method_7960() ? 187L : (long)(class_1792.method_7880(itemStack.method_7909()) + itemStack.method_7919());
         this.field_4725.method_43052(j);
         class_1087 bakedModel = this.field_4726.method_4019(itemStack, itemEntity.method_37908(), (class_1309)null, itemEntity.method_5628());
         boolean hasDepth = bakedModel.method_4712();
         this.field_4673 = NoRender.INSTANCE.castShadow.getValue() ? 0.15F : 0.0F;
         float l = class_3532.method_15374(((float)itemEntity.method_6985() + g) / 10.0F + itemEntity.field_7203) * 0.1F + 0.1F;
         float m = bakedModel.method_4709().method_3503(class_811.field_4318).field_4285.y();
         matrixStack.method_46416(0.0F, l + 0.25F * m, 0.0F);
         matrixStack.method_22907(this.field_4676.method_24197());
         float o = bakedModel.method_4709().field_4303.field_4285.x();
         float p = bakedModel.method_4709().field_4303.field_4285.y();
         float q = bakedModel.method_4709().field_4303.field_4285.z();
         int renderedAmount = getRenderedAmount(itemStack);
         if (!hasDepth) {
            float r = -0.0F * (float)(renderedAmount - 1) * 0.5F * o;
            float s = -0.0F * (float)(renderedAmount - 1) * 0.5F * p;
            float t = -0.09375F * (float)(renderedAmount - 1) * 0.5F * q;
            matrixStack.method_46416(r, s, t);
         }

         for(int u = 0; u < renderedAmount; ++u) {
            matrixStack.method_22903();
            if (u > 0) {
               if (hasDepth) {
                  float s = (this.field_4725.method_43057() * 2.0F - 1.0F) * 0.15F;
                  float t = (this.field_4725.method_43057() * 2.0F - 1.0F) * 0.15F;
                  float v = (this.field_4725.method_43057() * 2.0F - 1.0F) * 0.15F;
                  matrixStack.method_46416(s, t, v);
               } else {
                  float s = (this.field_4725.method_43057() * 2.0F - 1.0F) * 0.15F * 0.5F;
                  float t = (this.field_4725.method_43057() * 2.0F - 1.0F) * 0.15F * 0.5F;
                  matrixStack.method_46416(s, t, 0.0F);
               }
            }

            this.field_4726.method_23179(itemStack, class_811.field_4318, false, matrixStack, vertexConsumerProvider, i, class_4608.field_21444, bakedModel);
            matrixStack.method_22909();
            if (!hasDepth) {
               matrixStack.method_46416(0.0F * o, 0.0F * p, 0.0425F * q);
            }
         }

         matrixStack.method_22909();
         super.method_3936(itemEntity, f, g, matrixStack, vertexConsumerProvider, i);
         ci.cancel();
      }
   }
}
