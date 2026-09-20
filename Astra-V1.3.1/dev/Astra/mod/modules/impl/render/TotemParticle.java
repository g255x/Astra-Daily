package dev.Astra.mod.modules.impl.render;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.TotemParticleEvent;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.Random;

public class TotemParticle extends Module {
   public static TotemParticle INSTANCE;
   public final SliderSetting velocityXZ = this.add((new SliderSetting("VelocityXZ", (double)100.0F, (double)0.0F, (double)500.0F, (double)1.0F)).setSuffix("%"));
   public final SliderSetting velocityY = this.add((new SliderSetting("VelocityY", (double)100.0F, (double)0.0F, (double)500.0F, (double)1.0F)).setSuffix("%"));
   final Random random = new Random();
   private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 255)));
   private final ColorSetting color2 = this.add(new ColorSetting("Color2", new Color(0, 0, 0, 255)));

   public TotemParticle() {
      super("TotemParticle", Module.Category.Render);
      this.setChinese("自定义图腾粒子");
      INSTANCE = this;
   }

   @EventListener
   public void idk(TotemParticleEvent event) {
      event.cancel();
      event.velocityZ *= this.velocityXZ.getValue() / (double)100.0F;
      event.velocityX *= this.velocityXZ.getValue() / (double)100.0F;
      event.velocityY *= this.velocityY.getValue() / (double)100.0F;
      event.color = ColorUtil.fadeColor(this.color.getValue(), this.color2.getValue(), this.random.nextDouble());
   }
}
