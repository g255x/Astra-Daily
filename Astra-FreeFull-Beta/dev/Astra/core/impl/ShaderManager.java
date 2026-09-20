package dev.Astra.core.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.interfaces.IShaderEffectHook;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.mod.modules.impl.render.ShaderModule;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_276;
import net.minecraft.class_279;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

public class ShaderManager implements Wrapper {
   static final Timer timer = new Timer();
   private static final List<RenderTask> tasks = new ArrayList();
   public static ManagedShaderEffect DEFAULT_OUTLINE;
   public static ManagedShaderEffect SMOKE_OUTLINE;
   public static ManagedShaderEffect GRADIENT_OUTLINE;
   public static ManagedShaderEffect SNOW_OUTLINE;
   public static ManagedShaderEffect FLOW_OUTLINE;
   public static ManagedShaderEffect RAINBOW_OUTLINE;
   public static ManagedShaderEffect DEFAULT;
   public static ManagedShaderEffect SMOKE;
   public static ManagedShaderEffect GRADIENT;
   public static ManagedShaderEffect SNOW;
   public static ManagedShaderEffect FLOW;
   public static ManagedShaderEffect RAINBOW;
   public static ManagedShaderEffect SKYBOX;
   public float time = 0.0F;
   private boolean shaderDisabled = false;
   private MyFramebuffer shaderBuffer;
   private static long lastReloadAttempt = 0L;

   public void renderShader(Runnable runnable, Shader mode) {
      if (!this.shaderDisabled) {
         tasks.add(new RenderTask(runnable, mode));
      }
   }

   public void renderShaders() {
      if (!this.shaderDisabled) {
         try {
            tasks.forEach((t) -> this.applyShader(t.task(), t.shader()));
            tasks.clear();
         } catch (Exception var2) {
            this.shaderDisabled = true;
            tasks.clear();
         }

      }
   }

   public void applyShader(Runnable runnable, Shader mode) {
      if (!this.shaderDisabled && !this.fullNullCheck()) {
         try {
            RenderSystem.assertOnRenderThreadOrInit();
            class_276 MCBuffer = class_310.method_1551().method_1522();
            if (this.shaderBuffer.field_1482 != MCBuffer.field_1482 || this.shaderBuffer.field_1481 != MCBuffer.field_1481) {
               this.shaderBuffer.method_1234(MCBuffer.field_1482, MCBuffer.field_1481, false);
            }

            GlStateManager._glBindFramebuffer(36009, this.shaderBuffer.field_1476);
            this.shaderBuffer.method_1235(true);
            runnable.run();
            this.shaderBuffer.method_1240();
            GlStateManager._glBindFramebuffer(36009, MCBuffer.field_1476);
            MCBuffer.method_1235(false);
            ManagedShaderEffect shader = this.getShader(mode);
            if (shader == null) {
               return;
            }

            class_279 effect = shader.getShaderEffect();
            if (effect == null) {
               return;
            }

            ((IShaderEffectHook)effect).frogClient$addHook("bufIn", this.shaderBuffer);
            class_276 outBuffer = effect.method_1264("bufOut");
            if (outBuffer == null) {
               return;
            }

            this.setupShader(mode, shader);
            this.shaderBuffer.method_1230(false);
            MCBuffer.method_1235(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ZERO, class_4534.ONE);
            RenderSystem.backupProjectionMatrix();
            outBuffer.method_22594(outBuffer.field_1482, outBuffer.field_1481, false);
            RenderSystem.restoreProjectionMatrix();
            RenderSystem.disableBlend();
         } catch (Exception var7) {
            this.shaderDisabled = true;
         }

      }
   }

   public ManagedShaderEffect getShader(@NotNull Shader mode) {
      ManagedShaderEffect var10000;
      switch (mode.ordinal()) {
         case 1 -> var10000 = SMOKE;
         case 2 -> var10000 = GRADIENT;
         case 3 -> var10000 = SNOW;
         case 4 -> var10000 = FLOW;
         case 5 -> var10000 = RAINBOW;
         default -> var10000 = DEFAULT;
      }

      return var10000;
   }

   public ManagedShaderEffect getShaderOutline(@NotNull Shader mode) {
      ManagedShaderEffect var10000;
      switch (mode.ordinal()) {
         case 1 -> var10000 = SMOKE_OUTLINE;
         case 2 -> var10000 = GRADIENT_OUTLINE;
         case 3 -> var10000 = SNOW_OUTLINE;
         case 4 -> var10000 = FLOW_OUTLINE;
         case 5 -> var10000 = RAINBOW_OUTLINE;
         default -> var10000 = DEFAULT_OUTLINE;
      }

      return var10000;
   }

   public void setupShader(Shader shader, ManagedShaderEffect effect) {
      if (!this.shaderDisabled) {
         ShaderModule module = ShaderModule.INSTANCE;
         Color color = module.fill.getValue();
         this.time = (float)timer.getMs() / 5.0F * module.speed.getValueFloat() * 0.004F;

         try {
            if (shader == ShaderManager.Shader.Rainbow) {
               effect.setUniformValue("alpha2", (float)color.getAlpha() / 255.0F);
               effect.setUniformValue("radius", module.radius.getValueFloat());
               effect.setUniformValue("quality", module.smoothness.getValueFloat());
               effect.setUniformValue("divider", module.divider.getValueFloat());
               effect.setUniformValue("maxSample", module.maxSample.getValueFloat());
               effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               effect.setUniformValue("time", this.time);
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Gradient) {
               effect.setUniformValue("alpha2", (float)color.getAlpha() / 255.0F);
               effect.setUniformValue("rgb", (float)module.smoke1.getValue().getRed() / 255.0F, (float)module.smoke1.getValue().getGreen() / 255.0F, (float)module.smoke1.getValue().getBlue() / 255.0F);
               effect.setUniformValue("rgb1", (float)module.smoke2.getValue().getRed() / 255.0F, (float)module.smoke2.getValue().getGreen() / 255.0F, (float)module.smoke2.getValue().getBlue() / 255.0F);
               effect.setUniformValue("rgb2", (float)module.smoke3.getValue().getRed() / 255.0F, (float)module.smoke3.getValue().getGreen() / 255.0F, (float)module.smoke3.getValue().getBlue() / 255.0F);
               effect.setUniformValue("rgb3", (float)module.smoke4.getValue().getRed() / 255.0F, (float)module.smoke4.getValue().getGreen() / 255.0F, (float)module.smoke4.getValue().getBlue() / 255.0F);
               effect.setUniformValue("step", module.step.getValueFloat() * 300.0F);
               effect.setUniformValue("radius", module.radius.getValueFloat());
               effect.setUniformValue("quality", module.smoothness.getValueFloat());
               effect.setUniformValue("divider", module.divider.getValueFloat());
               effect.setUniformValue("maxSample", module.maxSample.getValueFloat());
               effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               effect.setUniformValue("time", this.time * 300.0F);
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Smoke) {
               effect.setUniformValue("alpha1", (float)color.getAlpha() / 255.0F);
               effect.setUniformValue("radius", module.radius.getValueFloat());
               effect.setUniformValue("quality", module.smoothness.getValueFloat());
               effect.setUniformValue("divider", module.divider.getValueFloat());
               effect.setUniformValue("maxSample", module.maxSample.getValueFloat());
               effect.setUniformValue("first", (float)module.smoke1.getValue().getRed() / 255.0F, (float)module.smoke1.getValue().getGreen() / 255.0F, (float)module.smoke1.getValue().getBlue() / 255.0F, (float)module.smoke1.getValue().getAlpha() / 255.0F);
               effect.setUniformValue("second", (float)module.smoke2.getValue().getRed() / 255.0F, (float)module.smoke2.getValue().getGreen() / 255.0F, (float)module.smoke2.getValue().getBlue() / 255.0F);
               effect.setUniformValue("third", (float)module.smoke3.getValue().getRed() / 255.0F, (float)module.smoke3.getValue().getGreen() / 255.0F, (float)module.smoke3.getValue().getBlue() / 255.0F);
               effect.setUniformValue("oct", (int)module.octaves.getValue());
               effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               effect.setUniformValue("time", this.time);
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Solid) {
               effect.setUniformValue("mixFactor", (float)color.getAlpha() / 255.0F);
               effect.setUniformValue("minAlpha", module.alpha.getValueFloat() / 255.0F);
               effect.setUniformValue("radius", module.radius.getValueFloat());
               effect.setUniformValue("quality", module.smoothness.getValueFloat());
               effect.setUniformValue("divider", module.divider.getValueFloat());
               effect.setUniformValue("maxSample", module.maxSample.getValueFloat());
               effect.setUniformValue("color", (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
               effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Snow) {
               effect.setUniformValue("color", (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
               effect.setUniformValue("radius", module.radius.getValueFloat());
               effect.setUniformValue("quality", module.smoothness.getValueFloat());
               effect.setUniformValue("divider", module.divider.getValueFloat());
               effect.setUniformValue("maxSample", module.maxSample.getValueFloat());
               effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               effect.setUniformValue("time", this.time);
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Flow) {
               effect.setUniformValue("mixFactor", (float)color.getAlpha() / 255.0F);
               effect.setUniformValue("radius", module.radius.getValueFloat());
               effect.setUniformValue("quality", module.smoothness.getValueFloat());
               effect.setUniformValue("divider", module.divider.getValueFloat());
               effect.setUniformValue("maxSample", module.maxSample.getValueFloat());
               effect.setUniformValue("resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               effect.setUniformValue("time", this.time);
               effect.render(mc.method_60646().method_60637(true));
            }
         } catch (Exception var6) {
         }

      }
   }

   public void reloadShaders() {
      DEFAULT = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/outline.json"));
      SMOKE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/smoke.json"));
      GRADIENT = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/gradient.json"));
      SNOW = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/snow.json"));
      FLOW = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/flow.json"));
      RAINBOW = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/rainbow.json"));
      SKYBOX = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/skybox.json"));
      DEFAULT_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/outline.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).frogClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).frogClient$addHook("bufOut", mc.field_1769.method_22990());
         }
      });
      SMOKE_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/smoke.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).frogClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).frogClient$addHook("bufOut", mc.field_1769.method_22990());
         }
      });
      GRADIENT_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/gradient.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).frogClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).frogClient$addHook("bufOut", mc.field_1769.method_22990());
         }
      });
      SNOW_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/snow.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).frogClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).frogClient$addHook("bufOut", mc.field_1769.method_22990());
         }
      });
      FLOW_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/flow.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).frogClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).frogClient$addHook("bufOut", mc.field_1769.method_22990());
         }
      });
      RAINBOW_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/rainbow.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).frogClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).frogClient$addHook("bufOut", mc.field_1769.method_22990());
         }
      });
   }

   public void renderSkyboxFullscreen(Color color, Color backgroundColor, float scale, float speed, float opacity) {
      if (!this.shaderDisabled && !this.fullNullCheck()) {
         try {
            if (!this.fullNullCheck()) {
               RenderSystem.assertOnRenderThreadOrInit();
               ManagedShaderEffect shader = SKYBOX;
               if (shader != null && shader.getShaderEffect() != null) {
                  class_276 MCBuffer = class_310.method_1551().method_1522();
                  class_279 effect = shader.getShaderEffect();
                  if (effect != null) {
                     try {
                        ((IShaderEffectHook)effect).frogClient$addHook("bufIn", MCBuffer);
                     } catch (Exception var13) {
                     }
                  }

                  class_276 outBuffer = shader.getShaderEffect().method_1264("bufOut");
                  if (outBuffer != null) {
                     float t = (float)timer.getMs() / 5.0F * speed * 0.004F;
                     this.safeSetUniformValue(shader, "mixFactor", opacity);
                     this.safeSetUniformValue(shader, "u_Color", (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
                     this.safeSetUniformValue(shader, "u_BackgroundColor", (float)backgroundColor.getRed() / 255.0F, (float)backgroundColor.getGreen() / 255.0F, (float)backgroundColor.getBlue() / 255.0F, (float)backgroundColor.getAlpha() / 255.0F);
                     this.safeSetUniformValue(shader, "u_Resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
                     this.safeSetUniformValue(shader, "u_Mouse", 0.0F, 0.0F);
                     this.safeSetUniformValue(shader, "u_Scale", scale);
                     this.safeSetUniformValue(shader, "u_Time", t);

                     try {
                        shader.render(mc.method_60646().method_60637(true));
                     } catch (Exception var12) {
                     }

                     MCBuffer.method_1235(false);
                     RenderSystem.enableBlend();
                     RenderSystem.blendFuncSeparate(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ZERO, class_4534.ONE);
                     RenderSystem.backupProjectionMatrix();
                     outBuffer.method_22594(outBuffer.field_1482, outBuffer.field_1481, false);
                     RenderSystem.restoreProjectionMatrix();
                     RenderSystem.disableBlend();
                  }
               }
            }
         } catch (Exception var14) {
            this.shaderDisabled = true;
         }

      }
   }

   private void safeSetUniformValue(ManagedShaderEffect shader, String name, float v1) {
      try {
         shader.setUniformValue(name, v1);
      } catch (Exception var5) {
      }

   }

   private void safeSetUniformValue(ManagedShaderEffect shader, String name, float v1, float v2) {
      try {
         shader.setUniformValue(name, v1, v2);
      } catch (Exception var6) {
      }

   }

   private void safeSetUniformValue(ManagedShaderEffect shader, String name, float v1, float v2, float v3, float v4) {
      try {
         shader.setUniformValue(name, v1, v2, v3, v4);
      } catch (Exception var8) {
      }

   }

   public boolean fullNullCheck() {
      if (GRADIENT != null && SMOKE != null && DEFAULT != null && FLOW != null && RAINBOW != null && SNOW != null && SKYBOX != null && GRADIENT_OUTLINE != null && SMOKE_OUTLINE != null && DEFAULT_OUTLINE != null && FLOW_OUTLINE != null && RAINBOW_OUTLINE != null && SNOW_OUTLINE != null && this.shaderBuffer != null) {
         return false;
      } else if (mc.method_1522() == null) {
         return true;
      } else {
         long now = System.currentTimeMillis();
         if (now - lastReloadAttempt < 5000L) {
            return true;
         } else {
            lastReloadAttempt = now;
            if (this.shaderBuffer != null) {
               this.shaderBuffer.method_1238();
            }

            this.shaderBuffer = new MyFramebuffer(mc.method_1522().field_1482, mc.method_1522().field_1481);
            this.reloadShaders();
            return true;
         }
      }
   }

   public static record RenderTask(Runnable task, Shader shader) {
   }

   public static enum Shader {
      Solid,
      Smoke,
      Gradient,
      Snow,
      Flow,
      Rainbow;

      // $FF: synthetic method
      private static Shader[] $values() {
         return new Shader[]{Solid, Smoke, Gradient, Snow, Flow, Rainbow};
      }
   }

   public static class MyFramebuffer extends class_276 {
      public MyFramebuffer(int width, int height) {
         super(false);
         RenderSystem.assertOnRenderThreadOrInit();
         this.method_1234(width, height, true);
         this.method_1236(0.0F, 0.0F, 0.0F, 0.0F);
      }
   }
}
