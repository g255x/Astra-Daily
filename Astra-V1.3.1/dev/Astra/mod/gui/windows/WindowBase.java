package dev.Astra.mod.gui.windows;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.utils.math.AnimateUtil;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.modules.impl.client.ClickGui;
import java.awt.Color;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class WindowBase {
   private final String name;
   private final class_2960 icon;
   private float x;
   private float y;
   private float width;
   private float height;
   private float dragX;
   private float dragY;
   private float scrollOffset;
   private float prevScrollOffset;
   private float maxElementsHeight;
   private boolean dragging;
   private boolean hoveringWindow;
   private boolean scaling;
   private boolean scrolling;
   private boolean visible = true;

   protected WindowBase(float x, float y, float width, float height, String name, class_2960 icon) {
      this.setX(x);
      this.setY(y);
      this.setWidth(width);
      this.setHeight(height);
      this.name = name;
      this.icon = icon;
   }

   public void render(class_332 context, int mouseX, int mouseY) {
      if (this.dragging && WindowsScreen.draggingWindow == this) {
         this.x = (float)mouseX - this.dragX;
         this.y = (float)mouseY - this.dragY;
      } else if (this.scaling && WindowsScreen.lastClickedWindow == this) {
         float nextWidth = (float)mouseX - this.dragX;
         float nextHeight = (float)mouseY - this.dragY;
         this.width = Math.max(120.0F, nextWidth);
         this.height = Math.max(80.0F, nextHeight);
      }

      this.prevScrollOffset = AnimateUtil.fast(this.prevScrollOffset, this.scrollOffset, 12.0F);
      ClickGui gui = ClickGui.getInstance();
      Color color2 = new Color(-983868581, true);
      RenderSystem.enableBlend();
      float headerH = gui != null ? (float)gui.categoryBarHeight.getValueInt() : 14.0F;
      float headerX = this.x;
      float headerY = this.y;
      float headerW = this.width;
      int topAlpha = gui != null ? gui.topAlpha.getValueInt() : 210;
      if (gui != null && gui.colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
         Render2DUtil.drawLutRect(context.method_51448(), headerX, headerY, headerW, headerH, gui.getSpectrumLutId(), gui.getSpectrumLutHeight(), topAlpha);
      } else {
         Color topColor = gui != null ? ColorUtil.injectAlpha(gui.getColor((double)headerY / (double)10.0F), topAlpha) : new Color(0, 120, 212, topAlpha);
         Render2DUtil.drawRect(context.method_51448(), headerX, headerY, headerW, headerH, topColor);
      }

      if (gui == null || gui.backgroundStyle.getValue() != ClickGui.BackgroundStyle.Transparent) {
         int outline = gui != null ? gui.hoverColor.getValue().getRGB() : (new Color(50, 50, 50, 200)).getRGB();
         Render2DUtil.drawRectWithOutline(context.method_51448(), headerX, headerY, headerW, headerH, new Color(0, 0, 0, 0), new Color(outline));
      }

      if (gui == null || gui.backGround.booleanValue) {
         int bgAlpha = gui != null ? gui.backgroundAlpha.getValueInt() : 236;
         Color bg = gui != null ? ColorUtil.injectAlpha(gui.backGround.getValue(), bgAlpha) : new Color(30, 30, 30, bgAlpha);
         Render2DUtil.drawRect(context.method_51448(), this.x, this.y + headerH, this.width, this.height - headerH, bg);
         int outline = gui != null ? gui.hoverColor.getValue().getRGB() : (new Color(50, 50, 50, 200)).getRGB();
         Render2DUtil.drawRectWithOutline(context.method_51448(), this.x, this.y + headerH, this.width, this.height - headerH, new Color(0, 0, 0, 0), new Color(outline));
      }

      float nameFontHeight = FontManager.isCustomFontEnabled() ? FontManager.ui.getFontHeight() : 9.0F;
      float nameY = headerY + (headerH - nameFontHeight) / 2.0F + (gui != null ? (float)gui.titleOffset.getValueInt() : 0.0F);
      FontManager.ui.drawString(context.method_51448(), this.name, (double)(this.x + 6.0F), (double)nameY, -1);
      float closeY = this.y + (headerH - 10.0F) / 2.0F;
      boolean hover1 = Render2DUtil.isHovered((double)mouseX, (double)mouseY, (double)(this.x + this.width - 14.0F), (double)closeY, (double)10.0F, (double)10.0F);
      int closeBg = gui != null ? (hover1 ? gui.hoverColor.getValue().getRGB() : gui.defaultColor.getValue().getRGB()) : (hover1 ? (new Color(-982026377, true)).getRGB() : (new Color(-984131753, true)).getRGB());
      Render2DUtil.rect(context.method_51448(), this.x + this.width - 14.0F, closeY, this.x + this.width - 4.0F, closeY + 10.0F, closeBg);
      float scrollTop = this.getScrollTop(headerH);
      float scrollH = this.getScrollHeight(headerH);
      float scrollBarW = this.getScrollBarWidth();
      float trackX = this.x + this.width - scrollBarW;
      float maxH = Math.max(1.0F, this.maxElementsHeight);
      float ratio = scrollH / maxH;
      float thumbH = Math.min(scrollH, scrollH * ratio);
      float thumbY = scrollTop - this.scrollOffset * ratio;
      float maxThumbY = scrollTop + scrollH - thumbH;
      if (thumbY < scrollTop) {
         thumbY = scrollTop;
      }

      if (thumbY > maxThumbY) {
         thumbY = maxThumbY;
      }

      boolean hover2 = Render2DUtil.isHovered((double)mouseX, (double)mouseY, (double)trackX, (double)scrollTop, (double)scrollBarW, (double)scrollH);
      Render2DUtil.drawRectWithOutline(context.method_51448(), trackX, scrollTop, scrollBarW, scrollH, hover2 ? new Color(1595085587, true) : new Color(1593835520, true), color2);
      Render2DUtil.drawRect(context.method_51448(), trackX, thumbY, scrollBarW, thumbH, new Color(-1590611663, true));
      float closeLeft = this.x + this.width - 14.0F;
      float closeRight = closeLeft + 10.0F;
      float closeBottom = closeY + 10.0F;
      String closeText = "x";
      float closeTextX = closeLeft + (10.0F - FontManager.ui.getWidth(closeText)) / 2.0F;
      float closeTextY = closeY + (10.0F - FontManager.ui.getFontHeight()) / 2.0F;
      FontManager.ui.drawString(context.method_51448(), closeText, (double)closeTextX, (double)closeTextY, -1);
      RenderSystem.disableBlend();
      if (this.scrolling) {
         float diff = ((float)mouseY - scrollTop) / scrollH;
         float scrollRange = this.getScrollRange(scrollH);
         this.scrollOffset = -(diff * scrollRange);
         this.scrollOffset = MathUtil.clamp(this.scrollOffset, -scrollRange, 0.0F);
      }

      this.hoveringWindow = Render2DUtil.isHovered((double)mouseX, (double)mouseY, (double)this.getX(), (double)this.getY(), (double)this.getWidth(), (double)this.getHeight());
      Render2DUtil.drawLine(context.method_51448(), this.getX() + this.getWidth(), this.getY() + this.getHeight() - 3.0F, this.getX() + this.getWidth() + 7.0F, this.getY() + this.getHeight() - 10.0F, color2.getRGB());
      Render2DUtil.drawLine(context.method_51448(), this.getX() + this.getWidth() + 5.0F, this.getY() + this.getHeight() - 3.0F, this.getX() + this.getWidth() + 7.0F, this.getY() + this.getHeight() - 5.0F, color2.getRGB());
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      ClickGui gui = ClickGui.getInstance();
      float headerH = gui != null ? (float)gui.categoryBarHeight.getValueInt() : 14.0F;
      float closeY = this.y + (headerH - 10.0F) / 2.0F;
      if (Render2DUtil.isHovered(mouseX, mouseY, (double)(this.x + this.width - 14.0F), (double)closeY, (double)10.0F, (double)10.0F)) {
         this.setVisible(false);
      } else if (Render2DUtil.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)headerH)) {
         if (WindowsScreen.draggingWindow == null) {
            this.dragging = true;
         }

         if (WindowsScreen.draggingWindow == null) {
            WindowsScreen.draggingWindow = this;
         }

         WindowsScreen.lastClickedWindow = this;
         this.dragX = (float)((int)(mouseX - (double)this.getX()));
         this.dragY = (float)((int)(mouseY - (double)this.getY()));
      } else if (Render2DUtil.isHovered(mouseX, mouseY, (double)(this.x + this.width), (double)(this.y + this.height - 10.0F), (double)10.0F, (double)10.0F)) {
         WindowsScreen.lastClickedWindow = this;
         this.dragX = (float)((int)(mouseX - (double)this.getWidth()));
         this.dragY = (float)((int)(mouseY - (double)this.getHeight()));
         this.scaling = true;
      } else {
         float scrollTop = this.getScrollTop(headerH);
         float scrollH = this.getScrollHeight(headerH);
         float scrollBarW = this.getScrollBarWidth();
         float trackX = this.x + this.width - scrollBarW;
         if (Render2DUtil.isHovered(mouseX, mouseY, (double)trackX, (double)scrollTop, (double)scrollBarW, (double)scrollH)) {
            WindowsScreen.lastClickedWindow = this;
            this.dragX = (float)((int)(mouseX - (double)this.getWidth()));
            this.dragY = (float)((int)(mouseY - (double)this.getHeight()));
            this.scrolling = true;
         }

      }
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
   }

   public void charTyped(char key, int keyCode) {
   }

   public void mouseScrolled(int i) {
      if (this.hoveringWindow) {
         this.scrollOffset += (float)(i * 16);
         ClickGui gui = ClickGui.getInstance();
         float headerH = gui != null ? (float)gui.categoryBarHeight.getValueInt() : 14.0F;
         float scrollH = this.getScrollHeight(headerH);
         float scrollRange = this.getScrollRange(scrollH);
         this.scrollOffset = MathUtil.clamp(this.scrollOffset, -scrollRange, 0.0F);
      }

   }

   protected float getScrollTop(float headerH) {
      return this.y + headerH + 3.0F;
   }

   protected float getScrollHeight(float headerH) {
      return Math.max(1.0F, this.getHeight() - headerH - 6.0F);
   }

   protected float getScrollRange(float scrollH) {
      return Math.max(0.0F, this.maxElementsHeight - scrollH);
   }

   protected float getScrollBarWidth() {
      return 6.0F;
   }

   public void mouseReleased(double mouseX, double mouseY, int button) {
      this.dragging = false;
      this.scaling = false;
      this.scrolling = false;
      WindowsScreen.draggingWindow = null;
   }

   public float getX() {
      return this.x;
   }

   public void setPosition(float x, float y) {
      this.setX(x);
      this.setY(y);
   }

   protected void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return this.y;
   }

   protected void setY(float y) {
      this.y = y;
   }

   public float getWidth() {
      return this.width;
   }

   protected void setWidth(float width) {
      this.width = width;
   }

   public float getHeight() {
      return this.height;
   }

   protected void setHeight(float height) {
      this.height = height;
   }

   protected float getScrollOffset() {
      return this.prevScrollOffset;
   }

   protected void resetScroll() {
      this.prevScrollOffset = 0.0F;
      this.scrollOffset = 0.0F;
   }

   protected void setMaxElementsHeight(float maxElementsHeight) {
      this.maxElementsHeight = maxElementsHeight;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public class_2960 getIcon() {
      return this.icon;
   }
}
