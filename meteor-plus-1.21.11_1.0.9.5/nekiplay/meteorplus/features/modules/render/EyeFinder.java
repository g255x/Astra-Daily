package nekiplay.meteorplus.features.modules.render;

import java.util.HashMap;
import java.util.Iterator;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;

public class EyeFinder extends Module {
   private final HashMap<class_1297, class_239> resultMap = new HashMap();
   private final SettingGroup sgGeneral;
   private final SettingGroup sgBlock;
   private final Setting<ShapeMode> shapeMode;
   private final Setting<SettingColor> lineColor;
   private final Setting<SettingColor> sideColor;

   public EyeFinder() {
      super(Categories.Render, "eye-finder", "Find block player look.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.sgBlock = this.settings.createGroup("Block");
      this.shapeMode = this.sgBlock.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("shape-mode")).description("How the shapes are rendered.")).defaultValue(ShapeMode.Both)).build());
      this.lineColor = this.sgGeneral.add(((ColorSetting.Builder)((ColorSetting.Builder)(new ColorSetting.Builder()).name("line-color")).description("The line color.")).defaultValue(new SettingColor(255, 255, 255, 255)).build());
      this.sideColor = this.sgBlock.add(((ColorSetting.Builder)((ColorSetting.Builder)(new ColorSetting.Builder()).name("side-color")).description("The side color.")).defaultValue(new SettingColor(255, 255, 255, 50)).build());
   }

   public void drawLine(Render3DEvent event, class_1297 entity, class_239 result) {
      event.renderer.line(entity.method_33571().field_1352, entity.method_33571().field_1351, entity.method_33571().field_1350, result.method_17784().field_1352, result.method_17784().field_1351, result.method_17784().field_1350, (Color)this.lineColor.get());
   }

   public void renderBlock(Render3DEvent event, class_1297 entity, class_239 result) {
      if (result instanceof class_3965 blockHitResult) {
         if (blockHitResult.method_17783() == class_240.field_1332 || blockHitResult.method_17783() == class_240.field_1333) {
            class_2338 bp = new class_2338(blockHitResult.method_17777());
            class_2680 state = this.mc.field_1687.method_8320(bp);
            class_2350 side = blockHitResult.method_17780();
            class_265 shape = state.method_26218(this.mc.field_1687, bp);
            if (shape.method_1110()) {
               return;
            }

            class_238 box = shape.method_1107();
            if (side != class_2350.field_11036 && side != class_2350.field_11033) {
               if (side != class_2350.field_11035 && side != class_2350.field_11043) {
                  double x = side == class_2350.field_11039 ? box.field_1323 : box.field_1320;
                  event.renderer.sideVertical((double)bp.method_10263() + x, (double)bp.method_10264() + box.field_1322, (double)bp.method_10260() + box.field_1321, (double)bp.method_10263() + x, (double)bp.method_10264() + box.field_1325, (double)bp.method_10260() + box.field_1324, (Color)this.sideColor.get(), (Color)this.lineColor.get(), (ShapeMode)this.shapeMode.get());
               } else {
                  double z = side == class_2350.field_11043 ? box.field_1321 : box.field_1324;
                  event.renderer.sideVertical((double)bp.method_10263() + box.field_1323, (double)bp.method_10264() + box.field_1322, (double)bp.method_10260() + z, (double)bp.method_10263() + box.field_1320, (double)bp.method_10264() + box.field_1325, (double)bp.method_10260() + z, (Color)this.sideColor.get(), (Color)this.lineColor.get(), (ShapeMode)this.shapeMode.get());
               }
            } else {
               event.renderer.sideHorizontal((double)bp.method_10263() + box.field_1323, (double)bp.method_10264() + (side == class_2350.field_11033 ? box.field_1322 : box.field_1325), (double)bp.method_10260() + box.field_1321, (double)bp.method_10263() + box.field_1320, (double)bp.method_10260() + box.field_1324, (Color)this.sideColor.get(), (Color)this.lineColor.get(), (ShapeMode)this.shapeMode.get());
            }
         }
      }

   }

   @EventHandler
   public void tickEvent(TickEvent.Pre event) {
      if (this.mc.field_1687 != null) {
         Iterator<class_1297> entityIterator = this.mc.field_1687.method_18112().iterator();
         HashMap<class_1297, class_239> cachMap = new HashMap();

         while(entityIterator.hasNext()) {
            class_1297 entity = (class_1297)entityIterator.next();
            if (entity instanceof class_1657 && entity != this.mc.field_1724) {
               class_239 result = entity.method_5745((double)5.0F, this.mc.method_61966().method_60637(true), false);
               cachMap.put(entity, result);
            }
         }

         this.resultMap.clear();
         this.resultMap.putAll(cachMap);
      }

   }

   @EventHandler
   public void on3dRender(Render3DEvent event) {
      for(class_1297 entity : this.resultMap.keySet()) {
         class_239 r = (class_239)this.resultMap.get(entity);
         this.drawLine(event, entity, r);
         this.renderBlock(event, entity, r);
      }

   }
}
