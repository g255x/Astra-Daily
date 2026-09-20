package dev.luminous.mod.gui.items;

import dev.luminous.Alien;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.Mod;
import dev.luminous.mod.gui.ClickGuiScreen;
import dev.luminous.mod.gui.items.buttons.Button;
import dev.luminous.mod.gui.items.buttons.ModuleButton;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClickGui;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_332;

public class Component extends Mod {
   private final List<Item> items = new ArrayList();
   private final Module.Category category;
   public boolean drag;
   protected class_332 context;
   private int x;
   private int y;
   private int x2;
   private int y2;
   private int width;
   private int height;
   private boolean open;
   private boolean hidden = false;
   private String searchText = "";

   public Component(String name, Module.Category category, int x, int y, boolean open) {
      super(name);
      this.category = category;
      this.setX(x);
      this.setY(y);
      this.setWidth(93);
      this.setHeight(18);
      this.open = open;
      this.setupItems();
   }

   public void setupItems() {
   }

   public Module.Category getCategory() {
      return this.category;
   }

   private void drag(int mouseX, int mouseY) {
      if (this.drag) {
         this.x = this.x2 + mouseX;
         this.y = this.y2 + mouseY;
      }

   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
      this.context = context;
      this.drag(mouseX, mouseY);
      boolean shouldShow = this.shouldShow();
      if (!this.searchText.isEmpty()) {
         this.open = shouldShow;
      }

      if (shouldShow) {
         float totalItemHeight = this.open ? this.getTotalItemHeight() - 2.0F : 0.0F;
         int color = ColorUtil.injectAlpha(ClickGui.getInstance().color.getValue().getRGB(), ClickGui.getInstance().topAlpha.getValueInt());
         Render2DUtil.drawRect(context.method_51448(), (float)this.x, (float)this.y, (float)this.width, (float)this.height - 5.0F, new Color(color));
         Render2DUtil.drawRect(context.method_51448(), (float)this.x, (float)this.y, (float)this.width, (float)this.height - 5.0F, new Color(color));
         if (this.open) {
            if (ClickGui.getInstance().blur.getValue()) {
               Alien.BLUR.applyBlur(1.0F + (ClickGui.getInstance().radius.getValueFloat() - 1.0F) * (float)ClickGui.getInstance().alphaValue, (float)this.x, (float)this.y + (float)this.height - 5.0F, (float)this.width, totalItemHeight + 5.0F);
            }

            if (ClickGui.getInstance().backGround.booleanValue) {
               Render2DUtil.drawRect(context.method_51448(), (float)this.x, (float)this.y + (float)this.height - 5.0F, (float)this.width, (float)(this.y + this.height) + totalItemHeight - ((float)this.y + (float)this.height - 5.0F), ClickGui.getInstance().backGround.getValue());
               Render2DUtil.drawRect(context.method_51448(), (float)this.x, (float)this.y + (float)this.height - 5.0F, (float)this.width, (float)(this.y + this.height) + totalItemHeight - ((float)this.y + (float)this.height - 5.0F), ClickGui.getInstance().backGround.getValue());
            }

            if (ClickGui.getInstance().line.getValue()) {
               Render2DUtil.drawLine(context.method_51448(), (float)this.x + 0.2F, (float)(this.y + this.height) + totalItemHeight, (float)this.x + 0.2F, (float)this.y + (float)this.height - 5.0F, ColorUtil.injectAlpha(ClickGui.getInstance().color.getValue().getRGB(), ClickGui.getInstance().topAlpha.getValueInt()));
               Render2DUtil.drawLine(context.method_51448(), (float)(this.x + this.width), (float)(this.y + this.height) + totalItemHeight, (float)(this.x + this.width), (float)this.y + (float)this.height - 5.0F, ColorUtil.injectAlpha(ClickGui.getInstance().color.getValue().getRGB(), ClickGui.getInstance().topAlpha.getValueInt()));
               Render2DUtil.drawLine(context.method_51448(), (float)this.x, (float)(this.y + this.height) + totalItemHeight, (float)(this.x + this.width), (float)(this.y + this.height) + totalItemHeight, ColorUtil.injectAlpha(ClickGui.getInstance().color.getValue().getRGB(), ClickGui.getInstance().topAlpha.getValueInt()));
            }
         }

         FontManager.icon.drawString(context.method_51448(), this.category.getIcon(), (double)((float)this.x + 6.0F), (double)((float)this.y + 4.0F), Button.enableTextColor);
         this.drawString(this.getName(), (double)((float)this.x + 20.0F), (double)((float)this.y - 1.0F - (float)(-ClickGui.getInstance().titleOffset.getValueInt() - 6)), Button.enableTextColor);
         if (this.open) {
            float y = (float)(this.getY() + this.getHeight()) - 3.0F;

            for(Item item : this.getItems()) {
               if (!item.isHidden()) {
                  item.setLocation((float)this.x + 2.0F, y);
                  item.setWidth(this.getWidth() - 4);
                  if (item instanceof ModuleButton) {
                     ModuleButton moduleButton = (ModuleButton)item;
                     if (!(moduleButton.itemHeight > (double)0.0F) && !moduleButton.subOpen) {
                        item.drawScreen(context, mouseX, mouseY, partialTicks);
                     } else {
                        context.method_44379((int)item.x, (int)item.y, mc.method_22683().method_4486(), (int)((double)(y + (float)moduleButton.getButtonHeight() + 1.5F) + moduleButton.itemHeight));
                        item.drawScreen(context, mouseX, mouseY, partialTicks);
                        context.method_44380();
                     }

                     y += (float)moduleButton.getButtonHeight() + 1.5F + (float)moduleButton.itemHeight;
                  } else {
                     item.drawScreen(context, mouseX, mouseY, partialTicks);
                     y += (float)item.getHeight() + 1.5F;
                  }
               }
            }
         }

      }
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
         this.x2 = this.x - mouseX;
         this.y2 = this.y - mouseY;
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

   public void addItem(Item item) {
      this.items.add(item);
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
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

   public final List<Item> getItems() {
      return this.items;
   }

   private boolean isHovering(int mouseX, int mouseY) {
      return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - 5;
   }

   private float getTotalItemHeight() {
      float height = 0.0F;

      for(Item item : this.getItems()) {
         item.update();
         if (item instanceof ModuleButton moduleButton) {
            moduleButton.itemHeight = moduleButton.animation.get(moduleButton.subOpen ? (double)moduleButton.getItemHeight() : (double)0.0F, 200L, Easing.CubicInOut);
            height += (float)moduleButton.getButtonHeight() + 1.5F + (float)moduleButton.itemHeight;
         } else {
            height += (float)item.getHeight() + 1.5F;
         }
      }

      return height;
   }

   protected void drawString(String text, double x, double y, Color color) {
      this.drawString(text, x, y, color.hashCode());
   }

   protected void drawString(String text, double x, double y, int color) {
      if (ClickGui.getInstance().font.getValue()) {
         FontManager.ui.drawString(this.context.method_51448(), text, (double)((int)x), (double)((int)y), color, ClickGui.getInstance().shadow.getValue());
      } else {
         this.context.method_51433(mc.field_1772, text, (int)x, (int)y, color, ClickGui.getInstance().shadow.getValue());
      }

   }

   public void setSearchText(String searchText) {
      this.searchText = searchText != null ? searchText : "";
   }

   public boolean shouldShow() {
      return this.searchText.isEmpty() ? true : this.items.stream().anyMatch((item) -> item instanceof ModuleButton ? ((ModuleButton)item).isMatch(this.searchText) : true);
   }

   public String getSearchText() {
      return this.searchText;
   }
}
