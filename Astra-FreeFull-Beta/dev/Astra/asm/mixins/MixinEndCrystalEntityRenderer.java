package dev.Astra.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.utils.render.ModelPlayer;
import dev.Astra.mod.modules.impl.render.Chams;
import net.minecraft.class_1511;
import net.minecraft.class_1921;
import net.minecraft.class_2338;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_5617;
import net.minecraft.class_5944;
import net.minecraft.class_630;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_892;
import net.minecraft.class_895;
import net.minecraft.class_897;
import net.minecraft.class_918;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_892.class})
public abstract class MixinEndCrystalEntityRenderer extends class_897<class_1511> {
   @Mutable
   @Final
   @Shadow
   private static class_1921 field_21736;
   @Shadow
   @Final
   private static class_2960 field_4663;
   @Unique
   private static final class_2960 BLANK = class_2960.method_60654("textures/blank.png");
   @Unique
   private static final class_1921 END_CRYSTAL_BLANK;
   @Unique
   private static final class_1921 END_CRYSTAL_CUSTOM;
   @Final
   @Shadow
   private static float field_21002;
   @Final
   @Shadow
   private class_630 field_21003;
   @Final
   @Shadow
   private class_630 field_21004;
   @Final
   @Shadow
   private class_630 field_21005;

   protected MixinEndCrystalEntityRenderer(class_5617.class_5618 ctx) {
      super(ctx);
   }

   @Unique
   private float yOffset(int age, float tickDelta, Chams module) {
      float f = ((float)age + tickDelta) * module.floatValue.getValueFloat();
      float g = class_3532.method_15374(f * 0.2F) / 2.0F + 0.5F;
      g = (g * g + g) * 0.4F * module.bounceHeight.getValueFloat();
      return g - 1.4F + module.floatOffset.getValueFloat();
   }

   @Inject(
      method = {"method_3908(Lnet/minecraft/class_1511;FFLnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void render(class_1511 endCrystalEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
      Chams module = Chams.INSTANCE;
      if (module.customCrystal()) {
         ci.cancel();
         int age = module.spinSync.getValue() ? module.age : endCrystalEntity.field_7034;
         float h = this.yOffset(age, g, module);
         float j = ((float)age + g) * 3.0F * module.spinValue.getValueFloat();
         matrixStack.method_22903();
         if (module.custom.getValue()) {
            class_5944 s = RenderSystem.getShader();
            if (module.depth.getValue()) {
               RenderSystem.enableDepthTest();
            }

            RenderSystem.enableBlend();
            if (module.chamsTexture.getValue()) {
               RenderSystem.setShaderTexture(0, field_4663);
               RenderSystem.setShader(class_757::method_34543);
            } else {
               RenderSystem.setShader(class_757::method_34540);
            }

            matrixStack.method_22903();
            matrixStack.method_22905(2.0F * module.scale.getValueFloat(), 2.0F * module.scale.getValueFloat(), 2.0F * module.scale.getValueFloat());
            matrixStack.method_46416(0.0F, -0.5F, 0.0F);
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
            matrixStack.method_46416(0.0F, 1.5F + h / 2.0F, 0.0F);
            matrixStack.method_22907((new Quaternionf()).setAngleAxis(((float)Math.PI / 3F), field_21002, 0.0F, field_21002));
            if (module.outerFrame.booleanValue) {
               ModelPlayer.render(matrixStack, this.field_21004, module.fill, module.line, (double)1.0F, module.chamsTexture.getValue());
            }

            matrixStack.method_22905(0.875F, 0.875F, 0.875F);
            matrixStack.method_22907((new Quaternionf()).setAngleAxis(((float)Math.PI / 3F), field_21002, 0.0F, field_21002));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
            if (module.innerFrame.booleanValue) {
               ModelPlayer.render(matrixStack, this.field_21004, module.fill, module.line, (double)1.0F, module.chamsTexture.getValue());
            }

            matrixStack.method_22905(0.875F, 0.875F, 0.875F);
            matrixStack.method_22907((new Quaternionf()).setAngleAxis(((float)Math.PI / 3F), field_21002, 0.0F, field_21002));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
            if (module.core.booleanValue) {
               ModelPlayer.render(matrixStack, this.field_21003, module.fill, module.line, (double)1.0F, module.chamsTexture.getValue());
            }

            matrixStack.method_22909();
            RenderSystem.setShader(() -> s);
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
         }

         class_4588 vertexConsumer = class_918.method_23181(vertexConsumerProvider, module.texture.getValue() ? END_CRYSTAL_CUSTOM : END_CRYSTAL_BLANK, false, module.glint.getValue());
         matrixStack.method_22903();
         matrixStack.method_22905(2.0F * module.scale.getValueFloat(), 2.0F * module.scale.getValueFloat(), 2.0F * module.scale.getValueFloat());
         matrixStack.method_46416(0.0F, -0.5F, 0.0F);
         int k = class_4608.field_21444;
         if (endCrystalEntity.method_6836()) {
            this.field_21005.method_22698(matrixStack, vertexConsumer, i, k);
         }

         matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
         matrixStack.method_46416(0.0F, 1.5F + h / 2.0F, 0.0F);
         matrixStack.method_22907((new Quaternionf()).setAngleAxis(((float)Math.PI / 3F), field_21002, 0.0F, field_21002));
         if (module.outerFrame.booleanValue) {
            this.field_21004.method_22699(matrixStack, vertexConsumer, i, k, module.outerFrame.getValue().getRGB());
         }

         matrixStack.method_22905(0.875F, 0.875F, 0.875F);
         matrixStack.method_22907((new Quaternionf()).setAngleAxis(((float)Math.PI / 3F), field_21002, 0.0F, field_21002));
         matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
         if (module.innerFrame.booleanValue) {
            this.field_21004.method_22699(matrixStack, vertexConsumer, i, k, module.innerFrame.getValue().getRGB());
         }

         matrixStack.method_22905(0.875F, 0.875F, 0.875F);
         matrixStack.method_22907((new Quaternionf()).setAngleAxis(((float)Math.PI / 3F), field_21002, 0.0F, field_21002));
         matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
         if (module.core.booleanValue) {
            this.field_21003.method_22699(matrixStack, vertexConsumer, i, k, module.core.getValue().getRGB());
         }

         matrixStack.method_22909();
         matrixStack.method_22909();
         class_2338 blockPos = endCrystalEntity.method_6838();
         if (blockPos != null) {
            float m = (float)blockPos.method_10263() + 0.5F;
            float n = (float)blockPos.method_10264() + 0.5F;
            float o = (float)blockPos.method_10260() + 0.5F;
            float p = (float)((double)m - endCrystalEntity.method_23317());
            float q = (float)((double)n - endCrystalEntity.method_23318());
            float r = (float)((double)o - endCrystalEntity.method_23321());
            matrixStack.method_46416(p, q, r);
            class_895.method_3917(-p, -q + h, -r, g, endCrystalEntity.field_7034, matrixStack, vertexConsumerProvider, i);
         }

         super.method_3936(endCrystalEntity, f, g, matrixStack, vertexConsumerProvider, i);
      }

   }

   static {
      END_CRYSTAL_BLANK = class_1921.method_23580(BLANK);
      END_CRYSTAL_CUSTOM = class_1921.method_23580(field_4663);
   }
}
