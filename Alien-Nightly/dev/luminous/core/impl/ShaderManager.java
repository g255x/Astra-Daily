package dev.luminous.core.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.api.interfaces.IShaderEffectHook;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.impl.render.ShaderModule;
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
   public static ManagedShaderEffect PULSE_OUTLINE;
   public static ManagedShaderEffect SMOKE_OUTLINE;
   public static ManagedShaderEffect GRADIENT_OUTLINE;
   public static ManagedShaderEffect SNOW_OUTLINE;
   public static ManagedShaderEffect FLOW_OUTLINE;
   public static ManagedShaderEffect RAINBOW_OUTLINE;
   public static ManagedShaderEffect DEFAULT;
   public static ManagedShaderEffect PULSE;
   public static ManagedShaderEffect SMOKE;
   public static ManagedShaderEffect GRADIENT;
   public static ManagedShaderEffect SNOW;
   public static ManagedShaderEffect FLOW;
   public static ManagedShaderEffect RAINBOW;
   public float time = 0.0F;
   private MyFramebuffer shaderBuffer;

   public void renderShader(Runnable runnable, Shader mode) {
      tasks.add(new RenderTask(runnable, mode));
   }

   public void renderShaders() {
      tasks.forEach((t) -> this.applyShader(t.task(), t.shader()));
      tasks.clear();
   }

   public void applyShader(Runnable runnable, Shader mode) {
      if (!this.fullNullCheck()) {
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
         class_279 effect = shader.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).alienClient$addHook("bufIn", this.shaderBuffer);
         }

         class_276 outBuffer = shader.getShaderEffect().method_1264("bufOut");
         this.setupShader(mode, shader);
         this.shaderBuffer.method_1230(false);
         MCBuffer.method_1235(false);
         RenderSystem.enableBlend();
         RenderSystem.blendFuncSeparate(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ZERO, class_4534.ONE);
         RenderSystem.backupProjectionMatrix();
         outBuffer.method_22594(outBuffer.field_1482, outBuffer.field_1481, false);
         RenderSystem.restoreProjectionMatrix();
         RenderSystem.disableBlend();
      }

   }

   public ManagedShaderEffect getShader(@NotNull Shader mode) {
      ManagedShaderEffect var10000;
      switch (mode.ordinal()) {
         case 1 -> var10000 = PULSE;
         case 2 -> var10000 = SMOKE;
         case 3 -> var10000 = GRADIENT;
         case 4 -> var10000 = SNOW;
         case 5 -> var10000 = FLOW;
         case 6 -> var10000 = RAINBOW;
         default -> var10000 = DEFAULT;
      }

      return var10000;
   }

   public ManagedShaderEffect getShaderOutline(@NotNull Shader mode) {
      ManagedShaderEffect var10000;
      switch (mode.ordinal()) {
         case 1 -> var10000 = PULSE_OUTLINE;
         case 2 -> var10000 = SMOKE_OUTLINE;
         case 3 -> var10000 = GRADIENT_OUTLINE;
         case 4 -> var10000 = SNOW_OUTLINE;
         case 5 -> var10000 = FLOW_OUTLINE;
         case 6 -> var10000 = RAINBOW_OUTLINE;
         default -> var10000 = DEFAULT_OUTLINE;
      }

      return var10000;
   }

   private void safeSetUniformValue(ManagedShaderEffect effect, String name, float value) {
      try {
         effect.setUniformValue(name, value);
      } catch (Exception var5) {
      }

   }

   private void safeSetUniformValue(ManagedShaderEffect effect, String name, float a, float b) {
      try {
         effect.setUniformValue(name, a, b);
      } catch (Exception var6) {
      }

   }

   private void safeSetUniformValue(ManagedShaderEffect effect, String name, float a, float b, float c) {
      try {
         effect.setUniformValue(name, a, b, c);
      } catch (Exception var7) {
      }

   }

   private void safeSetUniformValue(ManagedShaderEffect effect, String name, float a, float b, float c, float d) {
      try {
         effect.setUniformValue(name, a, b, c, d);
      } catch (Exception var8) {
      }

   }

   private void safeSetUniformValue(ManagedShaderEffect effect, String name, int value) {
      try {
         effect.setUniformValue(name, value);
      } catch (Exception var5) {
      }

   }

   public void setupShader(Shader shader, ManagedShaderEffect effect) {
      if (effect != null && effect.getShaderEffect() != null) {
         try {
            ShaderModule module = ShaderModule.INSTANCE;
            Color color = module.fill.getValue();
            this.time = (float)timer.getMs() / 5.0F * module.speed.getValueFloat() * 0.004F;
            if (shader == ShaderManager.Shader.Rainbow) {
               this.safeSetUniformValue(effect, "alpha2", (float)color.getAlpha() / 255.0F);
               this.safeSetUniformValue(effect, "radius", module.radius.getValueFloat());
               this.safeSetUniformValue(effect, "quality", module.smoothness.getValueFloat());
               this.safeSetUniformValue(effect, "divider", module.divider.getValueFloat());
               this.safeSetUniformValue(effect, "maxSample", module.maxSample.getValueFloat());
               this.safeSetUniformValue(effect, "resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               this.safeSetUniformValue(effect, "time", this.time);
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Gradient) {
               this.safeSetUniformValue(effect, "alpha2", (float)color.getAlpha() / 255.0F);
               this.safeSetUniformValue(effect, "rgb", (float)module.smoke1.getValue().getRed() / 255.0F, (float)module.smoke1.getValue().getGreen() / 255.0F, (float)module.smoke1.getValue().getBlue() / 255.0F);
               this.safeSetUniformValue(effect, "rgb1", (float)module.smoke2.getValue().getRed() / 255.0F, (float)module.smoke2.getValue().getGreen() / 255.0F, (float)module.smoke2.getValue().getBlue() / 255.0F);
               this.safeSetUniformValue(effect, "rgb2", (float)module.smoke3.getValue().getRed() / 255.0F, (float)module.smoke3.getValue().getGreen() / 255.0F, (float)module.smoke3.getValue().getBlue() / 255.0F);
               this.safeSetUniformValue(effect, "rgb3", (float)module.smoke4.getValue().getRed() / 255.0F, (float)module.smoke4.getValue().getGreen() / 255.0F, (float)module.smoke4.getValue().getBlue() / 255.0F);
               this.safeSetUniformValue(effect, "step", module.step.getValueFloat() * 300.0F);
               this.safeSetUniformValue(effect, "radius", module.radius.getValueFloat());
               this.safeSetUniformValue(effect, "quality", module.smoothness.getValueFloat());
               this.safeSetUniformValue(effect, "divider", module.divider.getValueFloat());
               this.safeSetUniformValue(effect, "maxSample", module.maxSample.getValueFloat());
               this.safeSetUniformValue(effect, "resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               this.safeSetUniformValue(effect, "time", this.time * 300.0F);
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Smoke) {
               this.safeSetUniformValue(effect, "alpha1", (float)color.getAlpha() / 255.0F);
               this.safeSetUniformValue(effect, "radius", module.radius.getValueFloat());
               this.safeSetUniformValue(effect, "quality", module.smoothness.getValueFloat());
               this.safeSetUniformValue(effect, "divider", module.divider.getValueFloat());
               this.safeSetUniformValue(effect, "maxSample", module.maxSample.getValueFloat());
               this.safeSetUniformValue(effect, "first", (float)module.smoke1.getValue().getRed() / 255.0F, (float)module.smoke1.getValue().getGreen() / 255.0F, (float)module.smoke1.getValue().getBlue() / 255.0F, (float)module.smoke1.getValue().getAlpha() / 255.0F);
               this.safeSetUniformValue(effect, "second", (float)module.smoke2.getValue().getRed() / 255.0F, (float)module.smoke2.getValue().getGreen() / 255.0F, (float)module.smoke2.getValue().getBlue() / 255.0F);
               this.safeSetUniformValue(effect, "third", (float)module.smoke3.getValue().getRed() / 255.0F, (float)module.smoke3.getValue().getGreen() / 255.0F, (float)module.smoke3.getValue().getBlue() / 255.0F);
               this.safeSetUniformValue(effect, "oct", (int)module.octaves.getValue());
               this.safeSetUniformValue(effect, "resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               this.safeSetUniformValue(effect, "time", this.time);
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Solid) {
               this.safeSetUniformValue(effect, "mixFactor", (float)color.getAlpha() / 255.0F);
               this.safeSetUniformValue(effect, "minAlpha", module.alpha.getValueFloat() / 255.0F);
               this.safeSetUniformValue(effect, "radius", module.radius.getValueFloat());
               this.safeSetUniformValue(effect, "quality", module.smoothness.getValueFloat());
               this.safeSetUniformValue(effect, "divider", module.divider.getValueFloat());
               this.safeSetUniformValue(effect, "maxSample", module.maxSample.getValueFloat());
               this.safeSetUniformValue(effect, "color", (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
               this.safeSetUniformValue(effect, "resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Pulse) {
               this.safeSetUniformValue(effect, "mixFactor", (float)color.getAlpha() / 255.0F);
               this.safeSetUniformValue(effect, "minAlpha", module.alpha.getValueFloat() / 255.0F);
               this.safeSetUniformValue(effect, "radius", module.radius.getValueFloat());
               this.safeSetUniformValue(effect, "quality", module.smoothness.getValueFloat());
               this.safeSetUniformValue(effect, "divider", module.divider.getValueFloat());
               this.safeSetUniformValue(effect, "maxSample", module.maxSample.getValueFloat());
               this.safeSetUniformValue(effect, "color", (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
               Color color2 = module.pulse.getValue();
               this.safeSetUniformValue(effect, "color2", (float)color2.getRed() / 255.0F, (float)color2.getGreen() / 255.0F, (float)color2.getBlue() / 255.0F);
               this.safeSetUniformValue(effect, "time", this.time);
               this.safeSetUniformValue(effect, "size", module.pulseSpeed.getValueFloat());
               this.safeSetUniformValue(effect, "resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Snow) {
               this.safeSetUniformValue(effect, "color", (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
               this.safeSetUniformValue(effect, "radius", module.radius.getValueFloat());
               this.safeSetUniformValue(effect, "quality", module.smoothness.getValueFloat());
               this.safeSetUniformValue(effect, "divider", module.divider.getValueFloat());
               this.safeSetUniformValue(effect, "maxSample", module.maxSample.getValueFloat());
               this.safeSetUniformValue(effect, "resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               this.safeSetUniformValue(effect, "time", this.time);
               effect.render(mc.method_60646().method_60637(true));
            } else if (shader == ShaderManager.Shader.Flow) {
               this.safeSetUniformValue(effect, "mixFactor", (float)color.getAlpha() / 255.0F);
               this.safeSetUniformValue(effect, "radius", module.radius.getValueFloat());
               this.safeSetUniformValue(effect, "quality", module.smoothness.getValueFloat());
               this.safeSetUniformValue(effect, "divider", module.divider.getValueFloat());
               this.safeSetUniformValue(effect, "maxSample", module.maxSample.getValueFloat());
               this.safeSetUniformValue(effect, "resolution", (float)mc.method_22683().method_4486(), (float)mc.method_22683().method_4502());
               this.safeSetUniformValue(effect, "time", this.time);
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
      PULSE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/pulse.json"));
      DEFAULT_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/outline.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).alienClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).alienClient$addHook("bufOut", mc.field_1769.method_22990());
         }

      });
      PULSE_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/pulse.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).alienClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).alienClient$addHook("bufOut", mc.field_1769.method_22990());
         }

      });
      SMOKE_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/smoke.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).alienClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).alienClient$addHook("bufOut", mc.field_1769.method_22990());
         }

      });
      GRADIENT_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/gradient.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).alienClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).alienClient$addHook("bufOut", mc.field_1769.method_22990());
         }

      });
      SNOW_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/snow.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).alienClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).alienClient$addHook("bufOut", mc.field_1769.method_22990());
         }

      });
      FLOW_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/flow.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).alienClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).alienClient$addHook("bufOut", mc.field_1769.method_22990());
         }

      });
      RAINBOW_OUTLINE = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/rainbow.json"), (managedShaderEffect) -> {
         class_279 effect = managedShaderEffect.getShaderEffect();
         if (effect != null) {
            ((IShaderEffectHook)effect).alienClient$addHook("bufIn", mc.field_1769.method_22990());
            ((IShaderEffectHook)effect).alienClient$addHook("bufOut", mc.field_1769.method_22990());
         }

      });
   }

   public boolean fullNullCheck() {
      if (GRADIENT != null && SMOKE != null && DEFAULT != null && FLOW != null && RAINBOW != null && PULSE != null && PULSE_OUTLINE != null && GRADIENT_OUTLINE != null && SMOKE_OUTLINE != null && DEFAULT_OUTLINE != null && FLOW_OUTLINE != null && RAINBOW_OUTLINE != null && this.shaderBuffer != null) {
         return false;
      } else if (mc.method_1522() == null) {
         return true;
      } else {
         this.shaderBuffer = new MyFramebuffer(mc.method_1522().field_1482, mc.method_1522().field_1481);
         this.reloadShaders();
         return true;
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

   public static record RenderTask(Runnable task, Shader shader) {
   }

   public static enum Shader {
      Solid,
      Pulse,
      Smoke,
      Gradient,
      Snow,
      Flow,
      Rainbow;

      // $FF: synthetic method
      private static Shader[] $values() {
         return new Shader[]{Solid, Pulse, Smoke, Gradient, Snow, Flow, Rainbow};
      }
   }
}
