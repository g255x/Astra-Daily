package nekiplay.meteorplus.features.modules.render;

import java.util.List;
import java.util.Map;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.GenericSetting;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.settings.items.ESPItemData;
import nekiplay.meteorplus.settings.items.ItemDataSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_3532;

public class ItemFrameEsp extends Module {
   private final SettingGroup sgGeneral;
   private final Setting<List<class_1792>> whitelist;
   private final Setting<ESPItemData> defaultBlockConfig;
   private final Setting<Map<class_1792, ESPItemData>> blockConfigs;

   public ItemFrameEsp() {
      super(Categories.Render, "ItemFrame-esp", "backlighting of the frames in which the selected items");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.whitelist = this.sgGeneral.add(((ItemListSetting.Builder)((ItemListSetting.Builder)(new ItemListSetting.Builder()).name("whitelist")).description("Which items to show esp.")).defaultValue(new class_1792[]{class_1802.field_8833}).build());
      this.defaultBlockConfig = this.sgGeneral.add(((GenericSetting.Builder)((GenericSetting.Builder)((GenericSetting.Builder)(new GenericSetting.Builder()).name("whitelist-default-config")).description("Default item config.")).defaultValue(new ESPItemData(ShapeMode.Lines, new SettingColor(0, 255, 200), new SettingColor(0, 255, 200, 25), false, new SettingColor(0, 255, 200, 125)))).build());
      this.blockConfigs = this.sgGeneral.add(((ItemDataSetting.Builder)((ItemDataSetting.Builder)(new ItemDataSetting.Builder()).name("whitelist-items-configs")).description("Config for each item.")).defaultData(this.defaultBlockConfig).build());
   }

   @EventHandler
   private void onRender2D(Render3DEvent event) {
      if (this.mc.field_1687 != null) {
         for(class_1297 entity : this.mc.field_1687.method_18112()) {
            double xl = entity.method_23317();
            double yl = entity.method_23318();
            double zl = entity.method_23321();
            if (entity instanceof class_1533) {
               class_1533 itemFrame = (class_1533)entity;
               class_1799 held = itemFrame.method_6940();
               if (((List)this.whitelist.get()).contains(held.method_7909())) {
                  ESPItemData espItemData = (ESPItemData)((Map)this.blockConfigs.get()).get(held.method_7909());
                  if (espItemData != null) {
                     if (espItemData.tracer) {
                        event.renderer.line(RenderUtils.center.field_1352, RenderUtils.center.field_1351, RenderUtils.center.field_1350, xl, yl, zl, espItemData.tracerColor);
                     }

                     double x = class_3532.method_16436((double)event.tickDelta, entity.field_6038, entity.method_23317()) - entity.method_23317();
                     double y = class_3532.method_16436((double)event.tickDelta, entity.field_5971, entity.method_23318()) - entity.method_23318();
                     double z = class_3532.method_16436((double)event.tickDelta, entity.field_5989, entity.method_23321()) - entity.method_23321();
                     class_238 box = entity.method_5829();
                     event.renderer.box(x + box.field_1323, y + box.field_1322, z + box.field_1321, x + box.field_1320, y + box.field_1325, z + box.field_1324, espItemData.sideColor, espItemData.lineColor, espItemData.shapeMode, 0);
                  } else {
                     if (((ESPItemData)this.defaultBlockConfig.get()).tracer) {
                        event.renderer.line(RenderUtils.center.field_1352, RenderUtils.center.field_1351, RenderUtils.center.field_1350, xl, yl, zl, ((ESPItemData)this.defaultBlockConfig.get()).tracerColor);
                     }

                     double x = class_3532.method_16436((double)event.tickDelta, entity.field_6038, entity.method_23317()) - entity.method_23317();
                     double y = class_3532.method_16436((double)event.tickDelta, entity.field_5971, entity.method_23318()) - entity.method_23318();
                     double z = class_3532.method_16436((double)event.tickDelta, entity.field_5989, entity.method_23321()) - entity.method_23321();
                     class_238 box = entity.method_5829();
                     event.renderer.box(x + box.field_1323, y + box.field_1322, z + box.field_1321, x + box.field_1320, y + box.field_1325, z + box.field_1324, ((ESPItemData)this.defaultBlockConfig.get()).sideColor, ((ESPItemData)this.defaultBlockConfig.get()).lineColor, ((ESPItemData)this.defaultBlockConfig.get()).shapeMode, 0);
                  }
               }
            }
         }

      }
   }
}
