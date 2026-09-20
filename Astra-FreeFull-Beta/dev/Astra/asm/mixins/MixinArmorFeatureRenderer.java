package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.render.NoRender;
import net.minecraft.class_1309;
import net.minecraft.class_1741;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_572;
import net.minecraft.class_6880;
import net.minecraft.class_8053;
import net.minecraft.class_970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_970.class})
public class MixinArmorFeatureRenderer<T extends class_1309, A extends class_572<T>> {
   @Inject(
      method = {"method_23192"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderArmorParts(class_4587 matrices, class_4597 vertexConsumers, int light, A model, int i, class_2960 identifier, CallbackInfo ci) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.armorParts.getValue()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_48482"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderArmorTrim(class_6880<class_1741> armorMaterial, class_4587 matrices, class_4597 vertexConsumers, int light, class_8053 trim, A model, boolean leggings, CallbackInfo ci) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.armorTrim.getValue()) {
         ci.cancel();
      }

   }
}
