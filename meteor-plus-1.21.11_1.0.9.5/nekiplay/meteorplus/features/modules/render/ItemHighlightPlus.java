package nekiplay.meteorplus.features.modules.render;

import java.util.List;
import java.util.Map;
import meteordevelopment.meteorclient.settings.GenericSetting;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import nekiplay.meteorplus.settings.items.HighlightItemData;
import nekiplay.meteorplus.settings.items.ItemDataSetting;
import net.minecraft.class_1792;
import net.minecraft.class_1802;

public class ItemHighlightPlus extends Module {
   public final SettingGroup sgGeneral;
   public final Setting<List<class_1792>> whitelist;
   public final Setting<HighlightItemData> defaultBlockConfig;
   public final Setting<Map<class_1792, HighlightItemData>> itemsConfigs;

   public ItemHighlightPlus() {
      super(Categories.Render, "Item-Highlight+", "Highlights selected items when in inventorys and guis.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.whitelist = this.sgGeneral.add(((ItemListSetting.Builder)((ItemListSetting.Builder)(new ItemListSetting.Builder()).name("whitelist")).description("Items to highlight.")).defaultValue(new class_1792[]{class_1802.field_8833}).build());
      this.defaultBlockConfig = this.sgGeneral.add(((GenericSetting.Builder)((GenericSetting.Builder)((GenericSetting.Builder)(new GenericSetting.Builder()).name("whitelist-default-config")).description("Default item config.")).defaultValue(new HighlightItemData(new SettingColor(0, 255, 200, 25)))).build());
      this.itemsConfigs = this.sgGeneral.add(((ItemDataSetting.Builder)((ItemDataSetting.Builder)(new ItemDataSetting.Builder()).name("whitelist-items-configs")).description("Config for each highlight.")).defaultData(this.defaultBlockConfig).build());
   }
}
