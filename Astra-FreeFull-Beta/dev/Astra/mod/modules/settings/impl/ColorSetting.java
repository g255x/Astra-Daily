package dev.Astra.mod.modules.settings.impl;

import dev.Astra.api.utils.math.Timer;
import dev.Astra.mod.modules.impl.client.ColorsModule;
import dev.Astra.mod.modules.settings.Setting;
import java.awt.Color;
import java.util.function.BooleanSupplier;

public class ColorSetting extends Setting {
   public static final Timer timer = new Timer();
   public static final float effectSpeed = 4.0F;
   private final Color defaultValue;
   public boolean rainbow;
   public boolean injectBoolean;
   public boolean booleanValue;
   private Color value;
   private boolean defaultRainbow;
   private boolean defaultBooleanValue;
   private boolean allowClientColor;

   public ColorSetting(String name) {
      this(name, new Color(255, 255, 255));
      this.defaultRainbow = true;
   }

   public ColorSetting(String name, BooleanSupplier visibilityIn) {
      super(name, visibilityIn);
      this.rainbow = false;
      this.injectBoolean = false;
      this.booleanValue = false;
      this.defaultRainbow = false;
      this.defaultBooleanValue = false;
      this.allowClientColor = true;
      this.defaultValue = this.value = new Color(255, 255, 255);
      this.defaultRainbow = true;
   }

   public ColorSetting(String name, Color defaultValue) {
      super(name);
      this.rainbow = false;
      this.injectBoolean = false;
      this.booleanValue = false;
      this.defaultRainbow = false;
      this.defaultBooleanValue = false;
      this.allowClientColor = true;
      this.value = defaultValue;
      this.defaultValue = defaultValue;
   }

   public ColorSetting(String name, Color defaultValue, BooleanSupplier visibilityIn) {
      super(name, visibilityIn);
      this.rainbow = false;
      this.injectBoolean = false;
      this.booleanValue = false;
      this.defaultRainbow = false;
      this.defaultBooleanValue = false;
      this.allowClientColor = true;
      this.value = defaultValue;
      this.defaultValue = defaultValue;
   }

   public ColorSetting(String name, int defaultValue) {
      this(name, new Color(defaultValue, true));
   }

   public ColorSetting(String name, int defaultValue, BooleanSupplier visibilityIn) {
      this(name, new Color(defaultValue, true), visibilityIn);
   }

   public Color getValue() {
      if (this.rainbow) {
         ColorsModule colors = ColorsModule.INSTANCE;
         if (this.allowClientColor && colors != null && colors.colorMode.getValue() == ColorsModule.ColorMode.Rainbow) {
            double rainbowState = Math.ceil(((double)System.currentTimeMillis() * colors.rainbowSpeed.getValue() + colors.rainbowDelay.getValue()) / (double)20.0F);
            Color hsb = Color.getHSBColor((float)(rainbowState % (double)360.0F / (double)360.0F), colors.saturation.getValueFloat() / 255.0F, 1.0F);
            this.setValue(new Color(hsb.getRed(), hsb.getGreen(), hsb.getBlue(), this.value.getAlpha()));
         } else {
            float[] HSB = Color.RGBtoHSB(this.value.getRed(), this.value.getGreen(), this.value.getBlue(), (float[])null);
            float hue = (float)((double)timer.getMs() * 0.36 * (double)4.0F / (double)20.0F % (double)361.0F / (double)360.0F);
            Color preColor = Color.getHSBColor(hue, HSB[1], HSB[2]);
            this.setValue(new Color(preColor.getRed(), preColor.getGreen(), preColor.getBlue(), this.value.getAlpha()));
         }
      }

      return this.value;
   }

   public void setValue(Color value) {
      this.value = value;
   }

   public void setValue(int value) {
      this.value = new Color(value, true);
   }

   public void setRainbow(boolean rainbow) {
      this.rainbow = rainbow;
   }

   public ColorSetting defaultRainbow(boolean defaultRainbow) {
      this.defaultRainbow = defaultRainbow;
      this.rainbow = defaultRainbow;
      return this;
   }

   public ColorSetting injectBoolean(boolean value) {
      this.injectBoolean = true;
      this.defaultBooleanValue = value;
      this.booleanValue = value;
      return this;
   }

   public ColorSetting allowClientColor(boolean value) {
      this.allowClientColor = value;
      return this;
   }

   public Color getDefaultValue() {
      return this.defaultValue;
   }

   public boolean getDefaultBooleanValue() {
      return this.defaultBooleanValue;
   }

   public boolean getDefaultRainbow() {
      return this.defaultRainbow;
   }
}
