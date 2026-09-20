package dev.Astra.mod.modules.impl.render;

import dev.Astra.Astra;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2611;
import net.minecraft.class_2627;
import net.minecraft.class_4587;

public class Tracers extends Module {
   private final ColorSetting item = this.add((new ColorSetting("Item", new Color(255, 255, 255, 100))).injectBoolean(true));
   private final ColorSetting player = this.add((new ColorSetting("Player", new Color(255, 255, 255, 100))).injectBoolean(true));
   private final ColorSetting chest = this.add((new ColorSetting("Chest", new Color(255, 255, 255, 100))).injectBoolean(false));
   private final ColorSetting enderChest = this.add((new ColorSetting("EnderChest", new Color(255, 100, 255, 100))).injectBoolean(false));
   private final ColorSetting shulkerBox = this.add((new ColorSetting("ShulkerBox", new Color(15, 255, 255, 100))).injectBoolean(false));

   public Tracers() {
      super("Tracers", Module.Category.Render);
      this.setChinese("追踪者");
   }

   public void onRender3D(class_4587 matrixStack) {
      mc.field_1690.method_42448().method_41748(false);
      if (this.item.booleanValue || this.player.booleanValue) {
         for(class_1297 entity : Astra.THREAD.getEntities()) {
            if (entity instanceof class_1542 && this.item.booleanValue) {
               this.drawLine(entity.method_19538(), this.item.getValue());
            } else if (entity instanceof class_1657 && this.player.booleanValue && entity != mc.field_1724) {
               this.drawLine(entity.method_19538(), this.player.getValue());
            }
         }
      }

      for(class_2586 blockEntity : BlockUtil.getTileEntities()) {
         if (blockEntity instanceof class_2595 && this.chest.booleanValue) {
            this.drawLine(blockEntity.method_11016().method_46558(), this.chest.getValue());
         } else if (blockEntity instanceof class_2611 && this.enderChest.booleanValue) {
            this.drawLine(blockEntity.method_11016().method_46558(), this.enderChest.getValue());
         } else if (blockEntity instanceof class_2627 && this.shulkerBox.booleanValue) {
            this.drawLine(blockEntity.method_11016().method_46558(), this.shulkerBox.getValue());
         }
      }

   }

   private void drawLine(class_243 pos, Color color) {
      Render3DUtil.drawLine(pos, mc.field_1773.method_19418().method_19326().method_1019(class_243.method_1030(mc.field_1724.method_5695(mc.method_60646().method_60637(true)), mc.field_1724.method_5705(mc.method_60646().method_60637(true))).method_1021(0.2)), color);
   }
}
