package dev.Astra.mod.modules.impl.client;

import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;

public class ColorsModule extends Module {
   public static ColorsModule INSTANCE;
   public final EnumSetting<ColorMode> colorMode;
   public final SliderSetting rainbowSpeed;
   public final SliderSetting saturation;
   public final SliderSetting rainbowDelay;

   public ColorsModule() {
      super("Colors", Module.Category.Client);
      this.colorMode = this.add(new EnumSetting("ColorMode", ColorsModule.ColorMode.Rainbow));
      this.rainbowSpeed = this.add(new SliderSetting("RainbowSpeed", (double)4.0F, (double)1.0F, (double)10.0F, 0.1, () -> this.colorMode.getValue() == ColorsModule.ColorMode.Rainbow));
      this.saturation = this.add(new SliderSetting("Saturation", (double)130.0F, (double)1.0F, (double)255.0F, () -> this.colorMode.getValue() == ColorsModule.ColorMode.Rainbow));
      this.rainbowDelay = this.add(new SliderSetting("Delay", 350, 0, 1000, () -> this.colorMode.getValue() == ColorsModule.ColorMode.Rainbow));
      this.setChinese("颜色");
      INSTANCE = this;
   }

   public void enable() {
      this.state = true;
   }

   public void disable() {
      this.state = true;
   }

   public boolean isOn() {
      return true;
   }

   public Color getColor() {
      return this.getColor((double)0.0F);
   }

   public Color getColor(double delay) {
      if (this.colorMode.getValue() == ColorsModule.ColorMode.Rainbow) {
         double rainbowState = Math.ceil(((double)System.currentTimeMillis() * this.rainbowSpeed.getValue() + delay * this.rainbowDelay.getValue()) / (double)20.0F);
         return Color.getHSBColor((float)(rainbowState % (double)360.0F / (double)360.0F), this.saturation.getValueFloat() / 255.0F, 1.0F);
      } else {
         return new Color(255, 0, 0);
      }
   }

   public static enum ColorMode {
      Custom,
      Rainbow;

      // $FF: synthetic method
      private static ColorMode[] $values() {
         return new ColorMode[]{Custom, Rainbow};
      }
   }
}
