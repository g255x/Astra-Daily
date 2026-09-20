package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.core.impl.ShaderManager;
import dev.Astra.mod.modules.impl.player.Freecam;
import dev.Astra.mod.modules.impl.render.Chams;
import dev.Astra.mod.modules.impl.render.ShaderModule;
import net.minecraft.class_1297;
import net.minecraft.class_279;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_761;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_761.class})
public abstract class MixinWorldRenderer {
   @Unique
   boolean renderingChams = false;
   @Unique
   boolean renderingEntity = false;

   @Inject(
      method = {"renderEntity"},
      at = {@At("HEAD")}
   )
   private void injectChamsForEntity(class_1297 entity, double cameraX, double cameraY, double cameraZ, float tickDelta, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
      if (Chams.INSTANCE.isOn() && Chams.INSTANCE.throughWall.getValue()) {
         if (Chams.INSTANCE.chams(entity)) {
            if (this.renderingEntity) {
               Wrapper.mc.method_22940().method_23000().method_22993();
               this.renderingEntity = false;
            }

            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0F, -1000000.0F);
            this.renderingChams = true;
         } else {
            this.renderingEntity = true;
         }
      }

   }

   @Inject(
      method = {"renderEntity"},
      at = {@At("RETURN")}
   )
   private void injectChamsForEntityPost(class_1297 entity, double cameraX, double cameraY, double cameraZ, float tickDelta, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
      if (Chams.INSTANCE.isOn() && Chams.INSTANCE.throughWall.getValue() && this.renderingChams) {
         Wrapper.mc.method_22940().method_23000().method_22993();
         GL11.glPolygonOffset(1.0F, 1000000.0F);
         GL11.glDisable(32823);
         this.renderingChams = false;
      }

   }

   @Redirect(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V",
   ordinal = 0
),
      require = 0
   )
   void replaceShaderHook(class_279 instance, float tickDelta) {
      ShaderManager.Shader shaders = ShaderModule.INSTANCE.mode.getValue();
      if (ShaderModule.INSTANCE.isOn() && Wrapper.mc.field_1687 != null) {
         Astra.SHADER.setupShader(shaders, Astra.SHADER.getShaderOutline(shaders));
      } else {
         instance.method_1258(tickDelta);
      }

   }

   @ModifyArg(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"
),
      index = 3
   )
   private boolean renderSetupTerrainModifyArg(boolean spectator) {
      return Freecam.INSTANCE.isOn() || spectator;
   }
}
