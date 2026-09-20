package nekiplay.meteorplus.features.modules.integrations;

import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import nekiplay.meteorplus.MeteorPlusAddon;

public class WhereIsIt extends Module {
   public SettingGroup defaultGroup;
   public final Setting<Boolean> background;
   public final Setting<Boolean> suport_color_symbols;
   public final Setting<SettingColor> text_color;
   public final Setting<Double> y_offset;

   public WhereIsIt() {
      super(MeteorPlusAddon.CATEGORYMODS, "where-is-it", "ChestTracker modification.");
      this.defaultGroup = this.settings.getDefaultGroup();
      this.background = this.defaultGroup.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("draw-background")).defaultValue(true)).build());
      this.suport_color_symbols = this.defaultGroup.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("use-color-symbols")).defaultValue(true)).build());
      this.text_color = this.defaultGroup.add(((ColorSetting.Builder)((ColorSetting.Builder)(new ColorSetting.Builder()).name("text-color")).visible(() -> !(Boolean)this.suport_color_symbols.get())).build());
      this.y_offset = this.defaultGroup.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("y-offset")).description("change-y-offset.")).defaultValue((double)-1.0F).max((double)15.0F).min((double)-15.0F).sliderRange((double)-15.0F, (double)15.0F).build());
   }
}
