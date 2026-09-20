package dev.Astra.mod.gui.items;

import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.Mod;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.gui.items.buttons.Button;
import dev.Astra.mod.gui.items.buttons.ModuleButton;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClickGui;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_332;

public class Component extends Mod {
   private final List<ModuleButton> items = new ArrayList();
   private final Module.Category category;
   public boolean drag;
   protected class_332 context;
   private int x;
   private int y;
   private float animX;
   private float animY;
   private float mouseMoveOffsetX;
   private float mouseMoveOffsetY;
   private float pageOffsetX;
   private final Animation xAnimation = new Animation();
   private final Animation yAnimation = new Animation();
   private final Animation openAnimation = new Animation();
   private int x2;
   private int y2;
   private int width;
   private int height;
   private boolean open;
   private boolean hidden = false;

   public Component(String name, Module.Category category, int x, int y, boolean open) {
      super(name);
      this.category = category;
      this.setX(x);
      this.setY(y);
      this.animX = (float)this.x;
      this.animY = (float)this.y;
      if (ClickGui.getInstance() != null) {
         this.setWidth(ClickGui.getInstance().moduleButtonWidth.getValueInt());
         this.setHeight(ClickGui.getInstance().categoryBarHeight.getValueInt() + 5);
      } else {
         this.setWidth(93);
         this.setHeight(18);
      }

      this.open = open;
      this.setupItems();
   }

   public void setupItems() {
   }

   private void drag(int mouseX, int mouseY) {
      if (this.drag) {
         this.x = this.x2 + mouseX;
         this.y = this.y2 + mouseY;
         this.animX = (float)this.x;
         this.animY = (float)this.y;
      }
   }

   private void updatePosition() {
      if (ClickGui.getInstance().scrollAnim.getValue() && !this.drag) {
         int length = Math.max(1, ClickGui.getInstance().scrollAnimLength.getValueInt());
         Easing easing = ClickGui.getInstance().scrollAnimEasing.getValue();
         this.animX = (float)this.xAnimation.get((double)this.x, (long)length, easing);
         this.animY = (float)this.yAnimation.get((double)this.y, (long)length, easing);
      } else {
         this.animX = (float)this.x;
         this.animY = (float)this.y;
         this.xAnimation.from = (double)this.x;
         this.xAnimation.to = (double)this.x;
         this.yAnimation.from = (double)this.y;
         this.yAnimation.to = (double)this.y;
      }
   }

   protected double getColorDelay() {
      return (double)this.getY() / (double)10.0F;
   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
      this.context = context;
      this.drag(mouseX, mouseY);
      this.updatePosition();
      int x = this.getX();
      int y = this.getY();
      float targetItemHeight = this.getTotalItemHeight() - 2.0F;
      double openProgress = this.openAnimation.get(this.open ? (double)1.0F : (double)0.0F, 200L, Easing.CubicInOut);
      float totalItemHeight = (float)((double)targetItemHeight * openProgress);
      int categoryWidth = ClickGui.getInstance().categoryWidth.getValueInt();
      float headerX = (float)x + ((float)this.width - (float)categoryWidth) / 2.0F;
      float headerY = (float)y;
      float headerW = (float)categoryWidth;
      float headerH = (float)this.height - 5.0F;
      double headerBaseDelay = this.getColorDelay();
      int topAlpha = ClickGui.getInstance().topAlpha.getValueInt();
      if (ClickGui.getInstance().colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
         Render2DUtil.drawLutRect(context.method_51448(), headerX, headerY, headerW, headerH, ClickGui.getInstance().getSpectrumLutId(), ClickGui.getInstance().getSpectrumLutHeight(), topAlpha);
      } else {
         Color topColor = ColorUtil.injectAlpha(ClickGui.getInstance().getColor(headerBaseDelay), topAlpha);
         Render2DUtil.drawRect(context.method_51448(), headerX, headerY, headerW, headerH, topColor);
      }

      if (ClickGui.getInstance().backgroundStyle.getValue() != ClickGui.BackgroundStyle.Transparent) {
         Render2DUtil.drawRectWithOutline(context.method_51448(), headerX, headerY, headerW, headerH, new Color(0, 0, 0, 0), new Color(ClickGui.getInstance().hoverColor.getValue().getRGB()));
      }

      if (openProgress > 0.01) {
         if (ClickGui.getInstance().backGround.booleanValue) {
            Render2DUtil.drawRect(context.method_51448(), (float)x, (float)y + (float)this.height - 5.0F, (float)this.width, (float)(y + this.height) + totalItemHeight - ((float)y + (float)this.height - 5.0F), ColorUtil.injectAlpha(ClickGui.getInstance().backGround.getValue(), ClickGui.getInstance().backgroundAlpha.getValueInt()));
            Render2DUtil.drawRectWithOutline(context.method_51448(), (float)x, (float)y + (float)this.height - 5.0F, (float)this.width, (float)(y + this.height) + totalItemHeight - ((float)y + (float)this.height - 5.0F), new Color(0, 0, 0, 0), new Color(ClickGui.getInstance().hoverColor.getValue().getRGB()));
         }

         if (ClickGui.getInstance().line.getValue()) {
            float yTop = (float)y + (float)this.height - 5.0F;
            float yBottom = (float)(y + this.height) + totalItemHeight;
            double delayPerPixel = (double)0.25F;
            float leftX = (float)x + 0.2F;
            float rightX = (float)(x + this.width);
            int alpha = ClickGui.getInstance().topAlpha.getValueInt();
            if (ClickGui.getInstance().colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
               float lineW = 1.0F;
               Render2DUtil.drawLutRect(context.method_51448(), leftX, yTop, lineW, yBottom - yTop, ClickGui.getInstance().getSpectrumLutId(), ClickGui.getInstance().getSpectrumLutHeight(), alpha);
               Render2DUtil.drawLutRect(context.method_51448(), rightX - lineW, yTop, lineW, yBottom - yTop, ClickGui.getInstance().getSpectrumLutId(), ClickGui.getInstance().getSpectrumLutHeight(), alpha);
            } else {
               float segment = 2.0F;

               for(float yy = yTop; yy < yBottom; yy += segment) {
                  float yy2 = Math.min(yy + segment, yBottom);
                  int c = ColorUtil.injectAlpha(ClickGui.getInstance().getColor((double)yy * delayPerPixel).getRGB(), alpha);
                  Render2DUtil.drawLine(context.method_51448(), leftX, yy2, leftX, yy, c);
                  Render2DUtil.drawLine(context.method_51448(), rightX, yy2, rightX, yy, c);
               }
            }

            int bottomColor = ColorUtil.injectAlpha(ClickGui.getInstance().getColor((double)yBottom * delayPerPixel).getRGB(), alpha);
            Render2DUtil.drawLine(context.method_51448(), (float)x, yBottom, (float)(x + this.width), yBottom, bottomColor);
         }
      }

      float iconY = headerY + (headerH - FontManager.icon.getFontHeight()) / 2.0F;
      FontManager.icon.drawString(context.method_51448(), this.category.getIcon(), (double)(headerX + 6.0F), (double)iconY, Button.enableTextColor);
      float nameFontHeight = FontManager.isCustomFontEnabled() ? FontManager.ui.getFontHeight() : 9.0F;
      float nameY = headerY + (headerH - nameFontHeight) / 2.0F + (float)ClickGui.getInstance().titleOffset.getValueInt();
      this.drawString(this.getName(), (double)(headerX + 20.0F), (double)nameY, Button.enableTextColor);
      if (openProgress > 0.01) {
         int panelX1 = x - 1;
         int panelY1 = (int)((float)y + (float)this.height - 6.0F);
         int panelX2 = x + this.width + 1;
         int panelY2 = (int)Math.round((double)(y + this.height) + (double)totalItemHeight) + 1;
         context.method_44379(panelX1, panelY1, panelX2, panelY2);
         float yOff = (float)(this.getY() + this.getHeight()) - 3.0F;

         for(ModuleButton item : this.getItems()) {
            if (!item.isHidden()) {
               item.setLocation((float)x + 2.0F, yOff);
               item.setWidth(this.getWidth() - 4);
               if (!(item.itemHeight > (double)0.0F) && !item.subOpen) {
                  item.drawScreen(context, mouseX, mouseY, partialTicks);
               } else {
                  int scissorX1 = (int)item.x - 1;
                  int scissorY1 = (int)item.y - 1;
                  int scissorX2 = (int)(item.x + (float)item.getWidth() + 1.0F);
                  int scissorY2 = (int)((double)(yOff + (float)item.getButtonHeight() + 1.5F) + item.itemHeight) + 1;
                  context.method_44379(scissorX1, scissorY1, scissorX2, scissorY2);
                  item.drawScreen(context, mouseX, mouseY, partialTicks);
                  context.method_44380();
               }

               yOff += (float)item.getButtonHeight() + 1.5F + (float)item.itemHeight;
            }
         }

         context.method_44380();
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
         this.setX(this.getX());
         this.setY(this.getY());
         this.animX = (float)this.x;
         this.animY = (float)this.y;
         this.xAnimation.from = (double)this.x;
         this.xAnimation.to = (double)this.x;
         this.yAnimation.from = (double)this.y;
         this.yAnimation.to = (double)this.y;
         this.x2 = this.getX() - mouseX;
         this.y2 = this.getY() - mouseY;
         ClickGuiScreen.getInstance().getComponents().forEach((component) -> {
            if (component.drag) {
               component.drag = false;
            }

         });
         this.drag = true;
      } else if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
         this.open = !this.open;
         Item.sound();
      } else if (this.open) {
         this.getItems().forEach((item) -> item.mouseClicked(mouseX, mouseY, mouseButton));
      }
   }

   public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
      if (releaseButton == 0) {
         this.drag = false;
      }

      if (this.open) {
         this.getItems().forEach((item) -> item.mouseReleased(mouseX, mouseY, releaseButton));
      }
   }

   public void onKeyTyped(char typedChar, int keyCode) {
      if (this.open) {
         this.getItems().forEach((item) -> item.onKeyTyped(typedChar, keyCode));
      }
   }

   public void onKeyPressed(int key) {
      if (this.open) {
         this.getItems().forEach((item) -> item.onKeyPressed(key));
      }
   }

   public void addButton(ModuleButton button) {
      this.items.add(button);
   }

   public int getX() {
      return (int)(this.animX + this.mouseMoveOffsetX + this.pageOffsetX);
   }

   public int getTargetX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return (int)(this.animY + this.mouseMoveOffsetY);
   }

   public int getTargetY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public void setHidden(boolean hidden) {
      this.hidden = hidden;
   }

   public boolean isOpen() {
      return this.open;
   }

   public final List<ModuleButton> getItems() {
      return this.items;
   }

   public void setMouseMoveOffset(float x, float y) {
      this.mouseMoveOffsetX = x;
      this.mouseMoveOffsetY = y;
   }

   public void setPageOffsetX(float x) {
      this.pageOffsetX = x;
   }

   public boolean isHovering(int mouseX, int mouseY) {
      int categoryWidth = ClickGui.getInstance().categoryWidth.getValueInt();
      int hx = Math.round((float)this.getX() + ((float)this.getWidth() - (float)categoryWidth) / 2.0F);
      int hy = this.getY();
      int hh = this.getHeight() - 5;
      return mouseX >= hx && mouseX <= hx + categoryWidth && mouseY >= hy && mouseY <= hy + hh;
   }

   public boolean isMouseOver(int mouseX, int mouseY) {
      if (this.isHovering(mouseX, mouseY)) {
         return true;
      } else if (!this.open) {
         return false;
      } else {
         float height = this.getTotalItemHeight() - 2.0F;
         int y = this.getY() + this.getHeight() - 5;
         return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && (float)mouseY >= (float)y && (float)mouseY <= (float)y + height;
      }
   }

   private float getTotalItemHeight() {
      float height = 0.0F;

      for(ModuleButton item : this.getItems()) {
         item.update();
         double openProgress = item.animation.get(item.subOpen ? (double)1.0F : (double)0.0F, 200L, Easing.CubicInOut);
         item.itemHeight = item.getVisibleItemHeight() * openProgress;
         height += (float)item.getButtonHeight() + 1.5F + (float)item.itemHeight;
      }

      return height;
   }

   protected void drawString(String text, double x, double y, Color color) {
      this.drawString(text, x, y, color.hashCode());
   }

   protected void drawString(String text, double x, double y, int color) {
      boolean shadow = FontManager.isShadowEnabled();
      if (FontManager.isCustomFontEnabled()) {
         FontManager.ui.drawString(this.context.method_51448(), text, (double)((int)x), (double)((int)y), color, shadow);
      } else {
         this.context.method_51433(mc.field_1772, text, (int)x, (int)y, color, shadow);
      }

   }
}
