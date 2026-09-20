package dev.Astra.mod.modules.impl.client.hud;

import dev.Astra.Astra;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.gui.windows.impl.ItemSelectWindow;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_7923;

public class ItemCounterHudModule extends HudModule {
   private final class_1799 stack;
   private final IntSupplier countSupplier;
   private final boolean useManager;
   private final BooleanSetting edit;
   private final SliderSetting perRow;
   private final BooleanSetting showZero;

   public ItemCounterHudModule(String name, String chinese, class_1792 item, int defaultX, int defaultY) {
      this(name, chinese, item, defaultX, defaultY, () -> InventoryUtil.getItemCount(item));
   }

   public ItemCounterHudModule(String name, String chinese, class_1792 item, int defaultX, int defaultY, IntSupplier countSupplier) {
      super(name, chinese, defaultX, defaultY);
      this.stack = new class_1799(item);
      this.countSupplier = countSupplier;
      this.useManager = false;
      this.edit = null;
      this.perRow = null;
      this.showZero = null;
   }

   public ItemCounterHudModule(String name, String chinese, int defaultX, int defaultY) {
      super(name, chinese, defaultX, defaultY);
      this.stack = new class_1799(class_1802.field_8162);
      this.countSupplier = null;
      this.useManager = true;
      this.edit = this.add((new BooleanSetting("Edit", false)).injectTask(this::openGui));
      this.perRow = this.add(new SliderSetting("PerRow", 8, 1, 20));
      this.showZero = this.add(new BooleanSetting("ShowZero", false));
   }

   private void openGui() {
      this.edit.setValueWithoutTask(false);
      if (!nullCheck()) {
         ClickGuiScreen screen = ClickGuiScreen.getInstance();
         screen.openHudWindow(new ItemSelectWindow(Astra.HUD_ITEM));
         ClickGui clickGui = ClickGui.getInstance();
         if (clickGui != null && !clickGui.isOn()) {
            clickGui.enable();
         } else if (mc.field_1755 != screen) {
            mc.method_1507(screen);
         }
      }

   }

   private class_1792 resolveItem(String key) {
      String name = key.replace("block.minecraft.", "").replace("item.minecraft.", "");

      class_2960 id;
      try {
         id = name.contains(":") ? class_2960.method_60654(name) : class_2960.method_60655("minecraft", name);
      } catch (Exception var5) {
         return null;
      }

      class_1792 item = (class_1792)class_7923.field_41178.method_10223(id);
      return item == class_1802.field_8162 ? null : item;
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

         for(String key : Astra.HUD_ITEM.getList()) {
            class_1792 item = this.resolveItem(key);
            if (item != null) {
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
}
