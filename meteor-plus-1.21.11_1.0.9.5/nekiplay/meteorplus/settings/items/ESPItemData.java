package nekiplay.meteorplus.settings.items;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WidgetScreen;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.GenericSetting;
import meteordevelopment.meteorclient.settings.IGeneric;
import meteordevelopment.meteorclient.utils.misc.IChangeable;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.class_1792;
import net.minecraft.class_2487;

public class ESPItemData implements IGeneric<ESPItemData>, IChangeable, IItemData<ESPItemData> {
   public ShapeMode shapeMode;
   public SettingColor lineColor;
   public SettingColor sideColor;
   public boolean tracer;
   public SettingColor tracerColor;
   private boolean changed;

   public ESPItemData(ShapeMode shapeMode, SettingColor lineColor, SettingColor sideColor, boolean tracer, SettingColor tracerColor) {
      this.shapeMode = shapeMode;
      this.lineColor = lineColor;
      this.sideColor = sideColor;
      this.tracer = tracer;
      this.tracerColor = tracerColor;
   }

   public WidgetScreen createScreen(GuiTheme theme, class_1792 block, ItemDataSetting<ESPItemData> setting) {
      return new ESPItemDataScreen(theme, this, block, setting);
   }

   public WidgetScreen createScreen(GuiTheme theme, GenericSetting<ESPItemData> setting) {
      return new ESPItemDataScreen(theme, this, setting);
   }

   public boolean isChanged() {
      return this.changed;
   }

   public void changed() {
      this.changed = true;
   }

   public void tickRainbow() {
      this.lineColor.update();
      this.sideColor.update();
      this.tracerColor.update();
   }

   public ESPItemData set(ESPItemData value) {
      this.shapeMode = value.shapeMode;
      this.lineColor.set(value.lineColor);
      this.sideColor.set(value.sideColor);
      this.tracer = value.tracer;
      this.tracerColor.set(value.tracerColor);
      this.changed = value.changed;
      return this;
   }

   public ESPItemData copy() {
      return new ESPItemData(this.shapeMode, new SettingColor(this.lineColor), new SettingColor(this.sideColor), this.tracer, new SettingColor(this.tracerColor));
   }

   public class_2487 toTag() {
      class_2487 tag = new class_2487();
      tag.method_10582("shapeMode", this.shapeMode.name());
      tag.method_10566("lineColor", this.lineColor.toTag());
      tag.method_10566("sideColor", this.sideColor.toTag());
      tag.method_10556("tracer", this.tracer);
      tag.method_10566("tracerColor", this.tracerColor.toTag());
      tag.method_10556("changed", this.changed);
      return tag;
   }

   public ESPItemData fromTag(class_2487 tag) {
      this.shapeMode = ShapeMode.valueOf((String)tag.method_10558("shapeMode").get());
      this.lineColor.fromTag((class_2487)tag.method_10562("lineColor").get());
      this.sideColor.fromTag((class_2487)tag.method_10562("sideColor").get());
      this.tracer = (Boolean)tag.method_10577("tracer").get();
      this.tracerColor.fromTag((class_2487)tag.method_10562("tracerColor").get());
      this.changed = (Boolean)tag.method_10577("changed").get();
      return this;
   }
}
