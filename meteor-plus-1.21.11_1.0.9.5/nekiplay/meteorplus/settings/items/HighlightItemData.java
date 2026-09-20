package nekiplay.meteorplus.settings.items;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WidgetScreen;
import meteordevelopment.meteorclient.settings.GenericSetting;
import meteordevelopment.meteorclient.settings.IGeneric;
import meteordevelopment.meteorclient.utils.misc.IChangeable;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.class_1792;
import net.minecraft.class_2487;

public class HighlightItemData implements IGeneric<HighlightItemData>, IChangeable, IItemData<HighlightItemData> {
   public SettingColor Color;
   private boolean changed;

   public HighlightItemData(SettingColor color) {
      this.Color = color;
   }

   public WidgetScreen createScreen(GuiTheme theme, class_1792 block, ItemDataSetting<HighlightItemData> setting) {
      return new HighlightItemDataScreen(theme, this, block, setting);
   }

   public WidgetScreen createScreen(GuiTheme theme, GenericSetting<HighlightItemData> setting) {
      return new HighlightItemDataScreen(theme, this, setting);
   }

   public boolean isChanged() {
      return this.changed;
   }

   public void changed() {
      this.changed = true;
   }

   public void tickRainbow() {
      this.Color.update();
   }

   public HighlightItemData set(HighlightItemData value) {
      this.Color.set(value.Color);
      this.changed = value.changed;
      return this;
   }

   public HighlightItemData copy() {
      return new HighlightItemData(new SettingColor(this.Color));
   }

   public class_2487 toTag() {
      class_2487 tag = new class_2487();
      tag.method_10566("color", this.Color.toTag());
      tag.method_10556("changed", this.changed);
      return tag;
   }

   public HighlightItemData fromTag(class_2487 tag) {
      this.Color.fromTag((class_2487)tag.method_10562("color").get());
      this.changed = (Boolean)tag.method_10577("changed").get();
      return this;
   }
}
