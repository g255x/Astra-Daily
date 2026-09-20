package dev.Astra.mod.modules.impl.hud;

import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_332;

public class ItemCounterHudModule extends HudModule {
   private final class_1799 stack;
   private final IntSupplier countSupplier;
   private final boolean useManager;
   private static final List<class_1792> FIXED_ITEMS;
   private final BooleanSetting showCrystal;
   private final BooleanSetting showObsidian;
   private final BooleanSetting showAnchor;
   private final BooleanSetting showGlowstone;
   private final BooleanSetting showTotem;
   private final BooleanSetting showGapple;
   private final BooleanSetting showGodApple;
   private final BooleanSetting showExp;
   private final BooleanSetting showFirework;
   private final SliderSetting perRow;
   private final BooleanSetting showZero;

   public ItemCounterHudModule(String name, String chinese, class_1792 item, int defaultX, int defaultY) {
      this(name, chinese, item, defaultX, defaultY, () -> InventoryUtil.getItemCount(item));
   }

   public ItemCounterHudModule(String name, String chinese, class_1792 item, int defaultX, int defaultY, IntSupplier countSupplier) {
      super(name, chinese, defaultX, defaultY);
      this.showCrystal = this.add(new BooleanSetting("Crystal", true));
      this.showObsidian = this.add(new BooleanSetting("Obsidian", true));
      this.showAnchor = this.add(new BooleanSetting("Anchor", true));
      this.showGlowstone = this.add(new BooleanSetting("Glowstone", true));
      this.showTotem = this.add(new BooleanSetting("Totem", true));
      this.showGapple = this.add(new BooleanSetting("Gapple", true));
      this.showGodApple = this.add(new BooleanSetting("GodApple", true));
      this.showExp = this.add(new BooleanSetting("Exp", true));
      this.showFirework = this.add(new BooleanSetting("Firework", true));
      this.perRow = this.add(new SliderSetting("PerRow", 8, 1, 20));
      this.showZero = this.add(new BooleanSetting("ShowZero", false));
      this.stack = new class_1799(item);
      this.countSupplier = countSupplier;
      this.useManager = false;
   }

   public ItemCounterHudModule(String name, String chinese, int defaultX, int defaultY) {
      super(name, chinese, defaultX, defaultY);
      this.showCrystal = this.add(new BooleanSetting("Crystal", true));
      this.showObsidian = this.add(new BooleanSetting("Obsidian", true));
      this.showAnchor = this.add(new BooleanSetting("Anchor", true));
      this.showGlowstone = this.add(new BooleanSetting("Glowstone", true));
      this.showTotem = this.add(new BooleanSetting("Totem", true));
      this.showGapple = this.add(new BooleanSetting("Gapple", true));
      this.showGodApple = this.add(new BooleanSetting("GodApple", true));
      this.showExp = this.add(new BooleanSetting("Exp", true));
      this.showFirework = this.add(new BooleanSetting("Firework", true));
      this.perRow = this.add(new SliderSetting("PerRow", 8, 1, 20));
      this.showZero = this.add(new BooleanSetting("ShowZero", false));
      this.stack = new class_1799(class_1802.field_8162);
      this.countSupplier = null;
      this.useManager = true;
   }

   public void onRender2D(class_332 drawContext, float tickDelta) {
      if (!this.useManager) {
         int count = this.countSupplier.getAsInt();
         if (count <= 0) {
            this.clearHudBounds();
         } else {
            this.stack.method_7939(count);
            int px = this.getHudRenderX(16);
            int py = this.getHudRenderY(16);
            this.setHudBounds(px, py, 16, 16);
            drawContext.method_51427(this.stack, px, py);
            if (HudSetting.useFont()) {
               String s = String.valueOf(count);
               int tx = px + 16 - (int)Math.ceil((double)FontManager.ui.getWidth(s));
               int ty = py + 16 - (int)Math.ceil((double)FontManager.ui.getFontHeight());
               drawContext.method_51448().method_22903();
               drawContext.method_51448().method_46416(0.0F, 0.0F, 200.0F);
               FontManager.ui.drawString(drawContext.method_51448(), s, (double)(tx + 1), (double)(ty + 1), -1, HudSetting.useShadow());
               drawContext.method_51448().method_22909();
            } else {
               drawContext.method_51431(mc.field_1772, this.stack, px, py);
            }

         }
      } else {
         List<class_1799> stacks = new ArrayList();
         List<Integer> counts = new ArrayList();

         for(int i = 0; i < FIXED_ITEMS.size(); ++i) {
            class_1792 item = (class_1792)FIXED_ITEMS.get(i);
            BooleanSetting switchSetting = this.getSwitchForIndex(i);
            if (switchSetting == null || switchSetting.getValue()) {
               int count = InventoryUtil.getItemCount(item);
               if (count > 0 || this.showZero.getValue()) {
                  class_1799 stack = new class_1799(item);
                  stack.method_7939(Math.max(1, count));
                  stacks.add(stack);
                  counts.add(count);
               }
            }
         }

         if (stacks.isEmpty()) {
            this.clearHudBounds();
         } else {
            int perRowValue = Math.max(1, this.perRow.getValueInt());
            int rows = (stacks.size() + perRowValue - 1) / perRowValue;
            int columns = Math.min(perRowValue, stacks.size());
            int width = columns * 18;
            int height = rows * 18;
            int px = this.getHudRenderX(width);
            int py = this.getHudRenderY(height);
            this.setHudBounds(px, py, width, height);

            for(int i = 0; i < stacks.size(); ++i) {
               int row = i / perRowValue;
               int col = i % perRowValue;
               int itemX = px + col * 18;
               int itemY = py + row * 18;
               class_1799 stack = (class_1799)stacks.get(i);
               int count = (Integer)counts.get(i);
               if (HudSetting.useFont()) {
                  drawContext.method_51427(stack, itemX, itemY);
                  String s = String.valueOf(count);
                  int tx = itemX + 16 - (int)Math.ceil((double)FontManager.ui.getWidth(s));
                  int ty = itemY + 16 - (int)Math.ceil((double)FontManager.ui.getFontHeight());
                  drawContext.method_51448().method_22903();
                  drawContext.method_51448().method_46416(0.0F, 0.0F, 200.0F);
                  FontManager.ui.drawString(drawContext.method_51448(), s, (double)(tx + 1), (double)(ty + 1), -1, HudSetting.useShadow());
                  drawContext.method_51448().method_22909();
               } else {
                  drawContext.method_51431(mc.field_1772, stack, itemX, itemY);
               }
            }

         }
      }
   }

   private BooleanSetting getSwitchForIndex(int index) {
      switch (index) {
         case 0 -> {
            return this.showCrystal;
         }
         case 1 -> {
            return this.showObsidian;
         }
         case 2 -> {
            return this.showAnchor;
         }
         case 3 -> {
            return this.showGlowstone;
         }
         case 4 -> {
            return this.showTotem;
         }
         case 5 -> {
            return this.showGapple;
         }
         case 6 -> {
            return this.showGodApple;
         }
         case 7 -> {
            return this.showExp;
         }
         case 8 -> {
            return this.showFirework;
         }
         default -> {
            return null;
         }
      }
   }

   static {
      FIXED_ITEMS = List.of(class_1802.field_8301, class_1802.field_8281, class_1802.field_23141, class_1802.field_8801, class_1802.field_8288, class_1802.field_8463, class_1802.field_8367, class_1802.field_8287, class_1802.field_8639);
   }
}
