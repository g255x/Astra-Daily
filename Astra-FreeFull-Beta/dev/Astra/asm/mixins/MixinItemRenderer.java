package dev.Astra.asm.mixins;

import dev.Astra.api.utils.render.SimpleItemModel;
import dev.Astra.mod.modules.impl.render.NoRender;
import net.minecraft.class_1087;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_811;
import net.minecraft.class_918;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_918.class})
public class MixinItemRenderer {
   @Unique
   private final SimpleItemModel flattenedModel = new SimpleItemModel();
   @Unique
   private class_811 renderMode;

   @Inject(
      method = {"method_23177*", "method_23178*", "method_23179*"},
      at = {@At("HEAD")}
   )
   private void getRenderType(class_1799 itemStack, class_811 transformationMode, boolean leftHand, class_4587 matrices, class_4597 vertexConsumers, int light, int overlay, class_1087 model, CallbackInfo ci) {
      this.renderMode = transformationMode;
   }

   @ModifyVariable(
      method = {"method_23182"},
      at = @At("HEAD"),
      index = 1,
      argsOnly = true,
      require = 0
   )
   private class_1087 replaceItemModelClass(class_1087 model, class_1087 arg, class_1799 stack, int light, int overlay, class_4587 matrices, class_4588 vertices) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fastItem.getValue() && !NoRender.INSTANCE.renderSidesOfItems.getValue() && !stack.method_7960() && !model.method_4712() && this.renderMode == class_811.field_4318) {
         this.flattenedModel.setItem(model);
         return this.flattenedModel;
      } else {
         return model;
      }
   }
}
