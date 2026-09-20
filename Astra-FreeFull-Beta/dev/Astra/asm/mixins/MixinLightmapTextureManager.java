package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.render.Ambience;
import java.awt.Color;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import net.minecraft.class_2874;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_638;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_757;
import net.minecraft.class_765;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_765.class})
public class MixinLightmapTextureManager {
   @Final
   @Shadow
   private class_1043 field_4138;
   @Final
   @Shadow
   private class_1011 field_4133;
   @Shadow
   private boolean field_4135;
   @Shadow
   private float field_21528;
   @Final
   @Shadow
   private class_757 field_4134;
   @Final
   @Shadow
   private class_310 field_4137;

   @Shadow
   private static void method_46557(Vector3f vec) {
      vec.set(class_3532.method_15363(vec.x, 0.0F, 1.0F), class_3532.method_15363(vec.y, 0.0F, 1.0F), class_3532.method_15363(vec.z, 0.0F, 1.0F));
   }

   @Shadow
   public static float method_23284(class_2874 type, int lightLevel) {
      float f = (float)lightLevel / 15.0F;
      float g = f / (4.0F - 3.0F * f);
      return class_3532.method_16439(type.comp_656(), g, 1.0F);
   }

   @Redirect(
      method = {"method_3313"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_746;method_6059(Lnet/minecraft/class_6880;)Z",
   ordinal = 0,
   remap = false
),
      require = 0
   )
   private boolean nightVisionHook(class_746 instance, class_6880<class_1291> registryEntry) {
      return Ambience.INSTANCE.isOn() && Ambience.INSTANCE.fullBright.getValue() ? true : instance.method_6059(registryEntry);
   }

   @Inject(
      method = {"method_3313"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void updateHook(float delta, CallbackInfo ci) {
      if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.worldColor.booleanValue) {
         ci.cancel();
         if (this.field_4135) {
            this.field_4135 = false;
            this.field_4137.method_16011().method_15396("lightTex");
            class_638 clientWorld = this.field_4137.field_1687;
            if (clientWorld != null) {
               float f = clientWorld.method_23783(1.0F);
               float g = clientWorld.method_23789() > 0 ? 1.0F : f * 0.95F + 0.05F;
               float h = ((Double)this.field_4137.field_1690.method_42472().method_41753()).floatValue();
               float i = this.method_42597(delta) * h;
               float j = this.method_42596(this.field_4137.field_1724, i, delta) * h;
               Vector3f vector3f = (new Vector3f(f, f, 1.0F)).lerp(new Vector3f(1.0F, 1.0F, 1.0F), 0.35F);
               float m = this.field_21528 + 1.5F;
               Vector3f vector3f2 = new Vector3f();

               for(int n = 0; n < 16; ++n) {
                  for(int o = 0; o < 16; ++o) {
                     float p = method_23284(clientWorld.method_8597(), n) * g;
                     float q = method_23284(clientWorld.method_8597(), o) * m;
                     float s = q * ((q * 0.6F + 0.4F) * 0.6F + 0.4F);
                     float t = q * (q * q * 0.6F + 0.4F);
                     vector3f2.set(q, s, t);
                     boolean bl = clientWorld.method_28103().method_28114();
                     if (bl) {
                        vector3f2.lerp(new Vector3f(0.99F, 1.12F, 1.0F), 0.25F);
                        method_46557(vector3f2);
                     } else {
                        Vector3f vector3f3 = (new Vector3f(vector3f)).mul(p);
                        vector3f2.add(vector3f3);
                        vector3f2.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
                        if (this.field_4134.method_3195(delta) > 0.0F) {
                           float u = this.field_4134.method_3195(delta);
                           Vector3f vector3f4 = (new Vector3f(vector3f2)).mul(0.7F, 0.6F, 0.6F);
                           vector3f2.lerp(vector3f4, u);
                        }
                     }

                     float v = Math.max(vector3f2.x(), Math.max(vector3f2.y(), vector3f2.z()));
                     if (v < 1.0F) {
                        this.field_4133.method_4305(o, n, (new Color(Ambience.INSTANCE.worldColor.getValue().getBlue(), Ambience.INSTANCE.worldColor.getValue().getGreen(), Ambience.INSTANCE.worldColor.getValue().getRed(), Ambience.INSTANCE.worldColor.getValue().getAlpha())).getRGB());
                     } else {
                        if (!bl) {
                           if (j > 0.0F) {
                              vector3f2.add(-j, -j, -j);
                           }

                           method_46557(vector3f2);
                        }

                        v = ((Double)this.field_4137.field_1690.method_42473().method_41753()).floatValue();
                        Vector3f vector3f5 = new Vector3f(this.method_23795(vector3f2.x), this.method_23795(vector3f2.y), this.method_23795(vector3f2.z));
                        vector3f2.lerp(vector3f5, Math.max(0.0F, v - i));
                        vector3f2.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
                        method_46557(vector3f2);
                        vector3f2.mul(255.0F);
                        int x = (int)vector3f2.x();
                        int y = (int)vector3f2.y();
                        int z = (int)vector3f2.z();
                        this.field_4133.method_4305(o, n, -16777216 | z << 16 | y << 8 | x);
                     }
                  }
               }

               this.field_4138.method_4524();
               this.field_4137.method_16011().method_15407();
            }
         }
      }

   }

   @Shadow
   private float method_23795(float x) {
      float f = 1.0F - x;
      return 1.0F - f * f * f * f;
   }

   @Shadow
   private float method_42597(float delta) {
      class_1293 statusEffectInstance = this.field_4137.field_1724.method_6112(class_1294.field_38092);
      return statusEffectInstance != null ? statusEffectInstance.method_55653(this.field_4137.field_1724, delta) : 0.0F;
   }

   @Shadow
   private float method_42596(class_1309 entity, float factor, float delta) {
      float f = 0.45F * factor;
      return Math.max(0.0F, class_3532.method_15362(((float)entity.field_6012 - delta) * (float)Math.PI * 0.025F) * f);
   }
}
