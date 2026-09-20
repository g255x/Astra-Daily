package dev.luminous.mod.gui.items.buttons;

import dev.luminous.api.utils.math.Animation;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.mod.gui.ClickGuiScreen;
import dev.luminous.mod.gui.items.Item;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClickGui;
import dev.luminous.mod.modules.settings.Setting;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_332;

public class ModuleButton extends Button {
   private final Module module;
   private List<Item> items = new ArrayList();
   public boolean subOpen;
   public double itemHeight;
   public final Animation animation = new Animation();
   private static final float BORDER_SIZE = 1.0F;

   public ModuleButton(Module module) {
      super(module.getName());
      this.module = module;
      this.initSettings();
   }

   private void initSettings() {
      ArrayList<Item> newItems = new ArrayList();

      for(Setting setting : this.module.getSettings()) {
         if (setting instanceof BooleanSetting s) {
            newItems.add(new BooleanButton(s));
         } else if (setting instanceof BindSetting s) {
            newItems.add(new BindButton(s));
         } else if (setting instanceof StringSetting s) {
            newItems.add(new StringButton(s));
         } else if (setting instanceof SliderSetting s) {
            newItems.add(new SliderButton(s));
         } else if (setting instanceof EnumSetting<?> s) {
            newItems.add(new EnumButton(s));
         } else if (setting instanceof ColorSetting s) {
            newItems.add(new PickerButton(s));
         }
      }

      this.items = newItems;
   }

   public void update() {
      for(Item item : this.items) {
         item.update();
      }

   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
      boolean hovered = this.isHovering(mouseX, mouseY);
      boolean enabled = this.getState();
      Color accent = ClickGui.getInstance().color.getValue();
      this.drawBackground(context, enabled, hovered, accent);
      this.drawBorder(context, enabled, hovered, accent);
      this.drawText(context, accent);
      if (this.subOpen || this.itemHeight > (double)0.0F) {
         this.drawItems(context, mouseX, mouseY, partialTicks);
         this.drawContainerLines(context);
      }

   }

   private void drawBackground(class_332 context, boolean enabled, boolean hovered, Color accent) {
      Color bgColor;
      if (enabled) {
         int alpha = Math.min(230, ClickGui.getInstance().hoverAlpha.getValueInt());
         bgColor = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), alpha);
      } else if (hovered) {
         Color hoverColor = ClickGui.getInstance().hoverColor.getValue();
         int alpha = ClickGui.getInstance().hoverAlpha.getValueInt();
         bgColor = new Color(hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue(), alpha);
      } else {
         bgColor = new Color(255, 255, 255, 0);
      }

      Render2DUtil.drawRect(context.method_51448(), this.x, this.y, (float)this.width, (float)this.height, bgColor);
   }

   private void drawBorder(class_332 context, boolean enabled, boolean hovered, Color accent) {
      if (enabled) {
         this.drawActiveBorder(context, accent);
      } else if (hovered) {
         this.drawHoveredBorder(context, accent);
      }

   }

   private void drawActiveBorder(class_332 context, Color accent) {
      float bx = this.x;
      float by = this.y;
      float bw = (float)this.width;
      float bh = (float)this.height;
      Color borderColor = ClickGui.getInstance().color.getValue();
      if (ClickGui.getInstance().verticalGradient.getValue()) {
         Render2DUtil.verticalGradient(context.method_51448(), bx, by, bx + bw, by + bh, new Color(255, 255, 255, 64), new Color(0, 0, 0, 56));
      }

      int highlightColor = ColorUtil.injectAlpha(borderColor.getRGB(), 80);
      Render2DUtil.drawLine(context.method_51448(), bx, by, bx + bw, by, highlightColor);
   }

   private void drawHoveredBorder(class_332 context, Color accent) {
      float bx = this.x;
      float by = this.y;
      float bw = (float)this.width;
      float bh = (float)this.height;
      int highlightColor = ColorUtil.injectAlpha(accent.getRGB(), 100);
      Render2DUtil.drawLine(context.method_51448(), bx, by, bx + bw, by, highlightColor);
   }

   private void drawText(class_332 context, Color accent) {
      int textColor = this.getState() ? enableTextColor : defaultTextColor;
      this.drawString(this.module.getDisplayName(), (double)(this.x + 2.3F), (double)(this.y - 2.0F - (float)ClickGuiScreen.getInstance().getTextOffset()), textColor);
      if (ClickGui.getInstance().gear.booleanValue && !this.items.isEmpty()) {
         this.drawString(this.subOpen ? "-" : "+", (double)(this.x + (float)this.width - 8.0F), (double)(this.y - 1.7F - (float)ClickGuiScreen.getInstance().getTextOffset()), ClickGui.getInstance().gear.getValue().getRGB());
      }

   }

   private void drawItems(class_332 context, int mouseX, int mouseY, float partialTicks) {
      float currentY = this.y + (float)this.height + 1.0F;

      for(Item item : this.items) {
         if (!item.isHidden()) {
            item.setHeight(this.height);
            item.setLocation(this.x + 1.0F, currentY);
            item.setWidth((int)((float)this.width - 9.0F));
            item.drawScreen(context, mouseX, mouseY, partialTicks);
            currentY += (float)item.getHeight() + 2.0F;
         }
      }

   }

   private void drawContainerLines(class_332 context) {
      if (ClickGui.getInstance().line.getValue()) {
         float bx = this.x;
         float by = this.y;
         float bw = (float)this.width;
         float itemHeight = (float)this.getItemHeight();
         float containerTop = by + (float)this.height;
         float containerBottom = by + (float)this.height + itemHeight;
         float leftLineX = bx + 0.1F;
         float rightLineX = bx + bw - 0.6F;
         Color baseColor = ClickGui.getInstance().color.getValue();
         int lineColor = ColorUtil.injectAlpha(baseColor.getRGB(), 160);
         float adjustedBottom = containerBottom - 2.0F;
         Render2DUtil.drawRect(context.method_51448(), leftLineX, adjustedBottom, 0.5F, containerTop - adjustedBottom, lineColor);
         Render2DUtil.drawRect(context.method_51448(), rightLineX, adjustedBottom, 0.5F, containerTop - adjustedBottom, lineColor);
         Render2DUtil.drawRect(context.method_51448(), leftLineX, adjustedBottom, rightLineX - leftLineX + 0.3F, 0.5F, lineColor);
      }
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (!this.items.isEmpty()) {
         if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.subOpen = !this.subOpen;
            sound();
         }

         if (this.subOpen) {
            for(Item item : this.items) {
               if (!item.isHidden()) {
                  item.mouseClicked(mouseX, mouseY, mouseButton);
               }
            }
         }

      }
   }

   public void onKeyTyped(char typedChar, int keyCode) {
      super.onKeyTyped(typedChar, keyCode);
      if (!this.items.isEmpty() && this.subOpen) {
         for(Item item : this.items) {
            if (!item.isHidden()) {
               item.onKeyTyped(typedChar, keyCode);
            }
         }
      }

   }

   public void onKeyPressed(int key) {
      super.onKeyPressed(key);
      if (!this.items.isEmpty() && this.subOpen) {
         for(Item item : this.items) {
            if (!item.isHidden()) {
               item.onKeyPressed(key);
            }
         }
      }

   }

   public int getButtonHeight() {
      return super.getHeight();
   }

   public int getItemHeight() {
      int height = 3;

      for(Item item : this.items) {
         if (!item.isHidden()) {
            height += item.getHeight() + 2;
         }
      }

      return height;
   }

   public int getHeight() {
      if (!this.subOpen) {
         return super.getHeight();
      } else {
         int height = super.getHeight();

         for(Item item : this.items) {
            if (!item.isHidden()) {
               height += item.getHeight() + 1;
            }
         }

         return height + 2;
      }
   }

   public Module getModule() {
      return this.module;
   }

   public void toggle() {
      this.module.toggle();
   }

   public boolean getState() {
      return this.module.isOn();
   }

   public boolean isMatch(String searchText) {
      if (searchText != null && !searchText.isEmpty()) {
         String search = searchText.toLowerCase();
         return this.module.getName().toLowerCase().contains(search) || this.module.getDisplayName().toLowerCase().contains(search);
      } else {
         return true;
      }
   }
}
