package dev.luminous.core.impl;

import dev.luminous.api.utils.Wrapper;
import net.minecraft.class_2960;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

public class BlurManager implements Wrapper {
   public static final ManagedShaderEffect BLUR = ShaderEffectManager.getInstance().manage(class_2960.method_60654("shaders/post/blurarea.json"));

   public void applyBlur(float radius, float startX, float startY, float width, float height) {
      float factor = (float)mc.method_22683().method_4495() / 2.0F;
      BLUR.setUniformValue("Radius", radius);
      BLUR.setUniformValue("BlurXY", startX * factor, (float)mc.method_22683().method_4507() / 2.0F - (startY + height) * factor);
      BLUR.setUniformValue("BlurCoord", width * factor, height * factor);
      BLUR.render(mc.method_60646().method_60637(true));
   }
}
