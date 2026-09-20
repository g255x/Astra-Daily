package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render2DEvent;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_1799;
import net.minecraft.class_308;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public class InventoryHUD extends Module {
   public final SliderSetting posX = this.add(new SliderSetting("PosX", (double)10.0F, (double)0.0F, (double)1920.0F, (double)1.0F));
   public final SliderSetting posY = this.add(new SliderSetting("PosY", (double)10.0F, (double)0.0F, (double)1080.0F, (double)1.0F));
   public final SliderSetting itemSize = this.add(new SliderSetting("ItemSize", (double)20.0F, (double)16.0F, (double)32.0F, (double)1.0F));
   public final SliderSetting itemSpacing = this.add(new SliderSetting("ItemSpacing", (double)2.0F, (double)0.0F, (double)10.0F, (double)0.5F));
   public final SliderSetting padding = this.add(new SliderSetting("Padding", (double)8.0F, (double)2.0F, (double)20.0F, (double)0.5F));
   public final SliderSetting columns = this.add(new SliderSetting("Columns", (double)9.0F, (double)3.0F, (double)12.0F, (double)1.0F));
   public final SliderSetting rows = this.add(new SliderSetting("Rows", (double)3.0F, (double)1.0F, (double)6.0F, (double)1.0F));
   public final ColorSetting bgColor = this.add(new ColorSetting("Background", new Color(Integer.MIN_VALUE, true)));
   public final ColorSetting borderColor = this.add(new ColorSetting("Border", new Color(-16711936, true)));
   public final BooleanSetting roundedCorners = this.add(new BooleanSetting("Rounded", true));
   public final SliderSetting cornerRadius = this.add(new SliderSetting("CornerRadius", (double)8.0F, (double)0.0F, (double)20.0F, (double)0.5F));
   public final SliderSetting borderThickness = this.add(new SliderSetting("BorderThickness", (double)2.0F, (double)0.0F, (double)5.0F, (double)0.5F));
   public final BooleanSetting enableBlur = this.add(new BooleanSetting("EnableBlur", true));
   public final SliderSetting blurStrength = this.add(new SliderSetting("BlurStrength", (double)5.0F, (double)1.0F, (double)20.0F, (double)0.5F));
   public final SliderSetting blurRadius = this.add(new SliderSetting("BlurRadius", (double)8.0F, (double)0.0F, (double)20.0F, (double)0.5F));
   public final ColorSetting blurOverlay = this.add(new ColorSetting("BlurOverlay", new Color(1610612736, true)));
   public final BooleanSetting showTitle = this.add(new BooleanSetting("ShowTitle", true));
   public final ColorSetting titleColor = this.add(new ColorSetting("TitleColor", new Color(-1, true)));
   public final SliderSetting titleScale = this.add(new SliderSetting("TitleScale", (double)1.0F, (double)0.5F, (double)2.0F, 0.1));
   public final BooleanSetting showItemCount = this.add(new BooleanSetting("ShowItemCount", true));
   public final ColorSetting countColor = this.add(new ColorSetting("CountColor", new Color(-1, true)));
   public final SliderSetting countScale = this.add(new SliderSetting("CountScale", 0.8, (double)0.5F, (double)1.5F, 0.1));

   public InventoryHUD() {
      super("InventoryHUD", Module.Category.Render);
      this.setChinese("背包HUD");
   }

   @EventListener
   public void onRender2D(Render2DEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_332 context = event.drawContext;
         class_4587 matrices = context.method_51448();
         float x = this.posX.getValueFloat();
         float y = this.posY.getValueFloat();
         int cols = (int)this.columns.getValueFloat();
         int rowsValue = (int)this.rows.getValueFloat();
         float itemSizeValue = this.itemSize.getValueFloat();
         float itemSpacingValue = this.itemSpacing.getValueFloat();
         float paddingValue = this.padding.getValueFloat();
         float totalWidth = (float)cols * itemSizeValue + (float)(cols - 1) * itemSpacingValue + paddingValue * 2.0F;
         float totalHeight = (float)rowsValue * itemSizeValue + (float)(rowsValue - 1) * itemSpacingValue + paddingValue * 2.0F;
         if (this.showTitle.getValue()) {
            float titleHeight = FontManager.ui.getFontHeight() * this.titleScale.getValueFloat();
            totalHeight += titleHeight + paddingValue;
         }

         if (this.enableBlur.getValue()) {
            this.drawBlurBackground(matrices, x, y, totalWidth, totalHeight);
         }

         this.drawRoundedBackground(matrices, x, y, totalWidth, totalHeight);
         float currentY = y;
         if (this.showTitle.getValue()) {
            currentY = y + this.drawTitle(matrices, x, y, totalWidth, paddingValue);
         }

         this.drawInventoryItems(context, matrices, x + paddingValue, currentY + paddingValue, cols, rowsValue, itemSizeValue, itemSpacingValue);
      }
   }

   private void drawBlurBackground(class_4587 matrices, float x, float y, float w, float h) {
      float radius = this.cornerRadius.getValueFloat();
      float blurStrengthValue = this.blurStrength.getValueFloat();

      for(int i = 0; (float)i < blurStrengthValue; ++i) {
         float offset = (float)i * 0.5F;
         float alpha = (float)((double)this.blurOverlay.getValue().getAlpha() / (double)255.0F * ((double)1.0F - (double)((float)i / blurStrengthValue)));
         Color blurColor = new Color(this.blurOverlay.getValue().getRed(), this.blurOverlay.getValue().getGreen(), this.blurOverlay.getValue().getBlue(), (int)(alpha * 255.0F));
         Render2DUtil.drawRoundedRect(matrices, x - offset, y - offset, w + offset * 2.0F, h + offset * 2.0F, radius + offset, blurColor);
      }

   }

   private void drawRoundedBackground(class_4587 matrices, float x, float y, float w, float h) {
      float radius = this.cornerRadius.getValueFloat();
      Render2DUtil.drawRoundedRect(matrices, x, y, w, h, radius, this.bgColor.getValue());
      if (this.borderThickness.getValueFloat() > 0.0F) {
         Render2DUtil.drawRoundedStroke(matrices, x, y, w, h, radius, this.borderColor.getValue(), 32);
      }

   }

   private float drawTitle(class_4587 matrices, float x, float y, float totalWidth, float padding) {
      String title = "Inv";
      float titleHeight = FontManager.ui.getFontHeight() * this.titleScale.getValueFloat();
      matrices.method_22903();
      matrices.method_46416(x, y + padding, 0.0F);
      matrices.method_22905(this.titleScale.getValueFloat(), this.titleScale.getValueFloat(), 1.0F);
      float scaledTextWidth = FontManager.ui.getWidth(title) * this.titleScale.getValueFloat();
      float centeredX = (totalWidth - scaledTextWidth) / (2.0F * this.titleScale.getValueFloat());
      FontManager.ui.drawString(matrices, title, (double)(centeredX + 1.0F), (double)1.0F, Integer.MIN_VALUE);
      FontManager.ui.drawString(matrices, title, (double)centeredX, (double)0.0F, this.titleColor.getValue().getRGB());
      matrices.method_22909();
      return titleHeight + padding;
   }

   private void drawInventoryItems(class_332 context, class_4587 matrices, float startX, float startY, int columns, int rows, float itemSize, float spacing) {
      if (mc.field_1724 != null) {
         int slotIndex = 0;

         for(int row = 0; row < rows; ++row) {
            for(int col = 0; col < columns; ++col) {
               float itemX = startX + (float)col * (itemSize + spacing);
               float itemY = startY + (float)row * (itemSize + spacing);
               int inventoryIndex = 9 + slotIndex;
               class_1799 stack;
               if (inventoryIndex < mc.field_1724.method_31548().method_5439() && !(stack = mc.field_1724.method_31548().method_5438(inventoryIndex)).method_7960()) {
                  this.drawItem(context, matrices, stack, itemX, itemY, itemSize);
               }

               ++slotIndex;
            }
         }

      }
   }

   private void drawItem(class_332 context, class_4587 matrices, class_1799 stack, float x, float y, float size) {
      if (!stack.method_7960()) {
         matrices.method_22903();
         float scale = size / 16.0F;
         float itemSize = 16.0F * scale;
         float offset = (size - itemSize) / 2.0F;
         matrices.method_46416(x + offset, y + offset, 0.0F);
         matrices.method_22905(scale, scale, 1.0F);
         class_308.method_24210();
         context.method_51427(stack, 0, 0);
         class_308.method_24211();
         matrices.method_22909();
         if (this.showItemCount.getValue() && stack.method_7947() > 1) {
            this.drawItemCount(matrices, stack, x, y, size);
         }

      }
   }

   private void drawItemCount(class_4587 matrices, class_1799 stack, float x, float y, float size) {
      String countText = String.valueOf(stack.method_7947());
      matrices.method_22903();
      matrices.method_46416(x + size - 2.0F, y + size - 2.0F, 0.0F);
      matrices.method_22905(this.countScale.getValueFloat(), this.countScale.getValueFloat(), 1.0F);
      float textWidth = FontManager.ui.getWidth(countText);
      matrices.method_46416(-textWidth, -FontManager.ui.getFontHeight(), 0.0F);
      FontManager.ui.drawString(matrices, countText, (double)1.0F, (double)1.0F, Integer.MIN_VALUE);
      FontManager.ui.drawString(matrices, countText, (double)0.0F, (double)0.0F, this.countColor.getValue().getRGB());
      matrices.method_22909();
   }

   public boolean onEnable() {
      super.onEnable();
      return false;
   }

   public void onDisable() {
      super.onDisable();
   }
}
