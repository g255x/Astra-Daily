package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public class Crosshair extends Module {
   public static Crosshair INSTANCE;
   public final SliderSetting length = this.add(new SliderSetting("Length", (double)5.0F, (double)0.0F, (double)20.0F, 0.1));
   public final SliderSetting thickness = this.add(new SliderSetting("Thickness", (double)2.0F, (double)0.0F, (double)20.0F, 0.1));
   public final SliderSetting interval = this.add(new SliderSetting("Interval", (double)2.0F, (double)0.0F, (double)20.0F, 0.1));
   private final ColorSetting color = this.add(new ColorSetting("Color"));

   public Crosshair() {
      super("Crosshair", Module.Category.Render);
      this.setChinese("准星");
      INSTANCE = this;
   }

   public void draw(class_332 context) {
      class_4587 matrixStack = context.method_51448();
      float centerX = (float)mc.method_22683().method_4486() / 2.0F;
      float centerY = (float)mc.method_22683().method_4502() / 2.0F;
      Render2DUtil.drawRect(matrixStack, centerX - this.thickness.getValueFloat() / 2.0F, centerY - this.length.getValueFloat() - this.interval.getValueFloat(), this.thickness.getValueFloat(), this.length.getValueFloat(), this.color.getValue());
      Render2DUtil.drawRect(matrixStack, centerX - this.thickness.getValueFloat() / 2.0F, centerY + this.interval.getValueFloat(), this.thickness.getValueFloat(), this.length.getValueFloat(), this.color.getValue());
      Render2DUtil.drawRect(matrixStack, centerX + this.interval.getValueFloat(), centerY - this.thickness.getValueFloat() / 2.0F, this.length.getValueFloat(), this.thickness.getValueFloat(), this.color.getValue());
      Render2DUtil.drawRect(matrixStack, centerX - this.interval.getValueFloat() - this.length.getValueFloat(), centerY - this.thickness.getValueFloat() / 2.0F, this.length.getValueFloat(), this.thickness.getValueFloat(), this.color.getValue());
   }
}
