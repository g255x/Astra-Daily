package dev.Astra.mod.gui.items.buttons;

import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.gui.items.Item;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.settings.Setting;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_7833;

public class ModuleButton extends Button {
   private final Module module;
   private List<Item> items = new ArrayList();
   public boolean subOpen;
   public double itemHeight;
   public final Animation animation = new Animation();
   private final Animation hoverAnimation = new Animation();
   private final Animation toggleAnimation = new Animation();
   private final HashMap<BooleanSetting, Animation> parentOpenAnimations = new HashMap();

   public ModuleButton(Module module) {
      super(module.getName());
      this.module = module;
      this.initSettings();
   }

   public void initSettings() {
      ArrayList<Item> newItems = new ArrayList();

      for(Setting setting : this.module.getSettings()) {
         if (setting instanceof Setting s) {
            newItems.add(new BooleanButton((BooleanSetting)s));
         }

         if (setting instanceof Setting s) {
            newItems.add(new BindButton((BindSetting)s));
         }

         if (setting instanceof Setting s) {
            newItems.add(new StringButton((StringSetting)s));
         }

         if (setting instanceof Setting s) {
            newItems.add(new SliderButton((SliderSetting)s));
         }

         if (setting instanceof Setting s) {
            newItems.add(new EnumButton((EnumSetting)s));
         }

         if (setting instanceof Setting s) {
            newItems.add(new PickerButton((ColorSetting)s));
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
      boolean pressed = this.getState();
      double hoverProgress = this.hoverAnimation.get(hovered ? (double)1.0F : (double)0.0F, 100L, Easing.CubicInOut);
      double toggleProgress = this.toggleAnimation.get(pressed ? (double)1.0F : (double)0.0F, 160L, Easing.CubicInOut);
      double baseDelay = this.getColorDelay();
      Color defaultColor = ClickGui.getInstance().defaultColor.getValue();
      Color hoverColor = ClickGui.getInstance().hoverColor.getValue();
      Color idleFill = new Color(defaultColor.getRed(), defaultColor.getGreen(), defaultColor.getBlue(), defaultColor.getAlpha());
      int baseA = ClickGui.getInstance().alpha.getValueInt();
      int hoverA = ClickGui.getInstance().hoverAlpha.getValueInt();
      int accentA = Math.max(0, Math.min(255, (int)Math.round((double)baseA + (double)(hoverA - baseA) * hoverProgress)));
      Color hoverFill = new Color(hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue(), hoverA);
      Color unpressedFill = ColorUtil.fadeColor(idleFill, hoverFill, hoverProgress);
      float h = (float)this.height - 0.5F;
      float radius = Math.min(10.0F, Math.min((float)this.width, h) / 2.0F);
      if (ClickGui.getInstance().colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
         if (toggleProgress >= 0.999) {
            Render2DUtil.drawLutRect(context.method_51448(), this.x, this.y, (float)this.width, h, ClickGui.getInstance().getSpectrumLutId(), ClickGui.getInstance().getSpectrumLutHeight(), accentA);
         } else {
            Render2DUtil.drawRect(context.method_51448(), this.x, this.y, (float)this.width, h, unpressedFill);
            int overlayA = (int)Math.round((double)accentA * toggleProgress);
            if (overlayA > 0) {
               Render2DUtil.drawLutRect(context.method_51448(), this.x, this.y, (float)this.width, h, ClickGui.getInstance().getSpectrumLutId(), ClickGui.getInstance().getSpectrumLutHeight(), overlayA);
            }
         }
      } else {
         Color accent = ClickGui.getInstance().getActiveColor(baseDelay);
         Color pressedFill = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), accentA);
         Color baseFill = ColorUtil.fadeColor(unpressedFill, pressedFill, toggleProgress);
         Render2DUtil.drawRect(context.method_51448(), this.x, this.y, (float)this.width, h, baseFill);
      }

      float textY = this.getCenteredTextY(this.y, (float)this.height - 0.5F);
      if (hovered && class_3675.method_15987(mc.method_22683().method_4490(), 340)) {
         this.drawString("Reset Default", (double)(this.x + 2.3F), (double)textY, enableTextColor);
      } else {
         this.drawString(this.module.getDisplayName(), (double)(this.x + 2.3F), (double)textY, this.getState() ? enableTextColor : defaultTextColor);
      }

      if (ClickGui.getInstance().gear.booleanValue) {
         if (!this.subOpen && !(this.itemHeight > (double)0.0F)) {
            boolean var98 = false;
         } else {
            boolean var10000 = true;
         }

         switch ((ClickGui.ExpandIcon)ClickGui.getInstance().expandIcon.getValue()) {
            case PlusMinus:
               this.drawString(this.subOpen ? "-" : "+", (double)(this.x + (float)this.width - 8.0F), (double)textY, ClickGui.getInstance().gear.getValue().getRGB());
               break;
            case Chevron:
               this.drawString(this.subOpen ? "v" : ">", (double)(this.x + (float)this.width - 8.0F), (double)textY, ClickGui.getInstance().gear.getValue().getRGB());
               break;
            case Gear:
               int gearColor = ClickGui.getInstance().gear.getValue().getRGB();
               float gearW = FontManager.icon.getWidth("d");
               float gearH = FontManager.icon.getFontHeight("d");
               float centerX = this.x + (float)this.width - radius;
               float centerY = this.y + h / 2.0F;
               float gearX = centerX - gearW / 2.0F;
               float gearY = centerY - gearH / 2.0F;
               int totalItemHeight = this.getItemHeight();
               float expandProgress = totalItemHeight <= 0 ? 0.0F : (float)Math.min((double)1.0F, Math.max((double)0.0F, this.itemHeight / (double)totalItemHeight));
               if (expandProgress > 0.001F) {
                  float angle = (float)(System.currentTimeMillis() % 2000L) / 2000.0F * 360.0F * expandProgress;
                  context.method_51448().method_22903();
                  context.method_51448().method_46416(centerX, centerY, 0.0F);
                  context.method_51448().method_22907(class_7833.field_40718.rotationDegrees(angle));
                  FontManager.icon.drawString(context.method_51448(), "d", (double)(-gearW / 2.0F), (double)(-gearH / 2.0F), gearColor);
                  context.method_51448().method_22909();
               } else {
                  FontManager.icon.drawString(context.method_51448(), "d", (double)gearX, (double)gearY, gearColor);
               }
         }
      }

      if (this.subOpen || this.itemHeight > (double)0.0F) {
         double totalItemHeight = this.getVisibleItemHeight();
         double visibleItemHeight = Math.max((double)0.0F, Math.min(this.itemHeight, totalItemHeight));
         float expandProgress = totalItemHeight <= (double)0.0F ? 0.0F : (float)(visibleItemHeight / totalItemHeight);
         if (this.subOpen && expandProgress < 0.9F) {
            for(Item item : this.items) {
               if (item instanceof SliderButton) {
                  ((SliderButton)item).resetFillAnimation();
               }
            }
         }

         float slide = (1.0F - expandProgress) * 6.0F;
         if (ClickGui.getInstance().line.getValue() && visibleItemHeight > 0.01) {
            float yTop = this.y + (float)this.height - 0.5F;
            float yBottom = (float)((double)(this.y + (float)this.height) + visibleItemHeight - (double)0.5F);
            float yBottomLine = (float)((double)(this.y + (float)this.height) + visibleItemHeight - (double)0.7F);
            double delayPerPixel = (double)0.25F;
            float leftX = this.x + 0.6F;
            float rightX = this.x + (float)this.width - 0.6F;
            int alpha = Math.min(160, ClickGui.getInstance().topAlpha.getValueInt());
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
            Render2DUtil.drawLine(context.method_51448(), leftX, yBottom, rightX, yBottomLine, bottomColor);
         }

         float height = (float)(this.height + 2);
         List<Setting> settings = this.module.getSettings();
         int i = 0;

         while(i < this.items.size() && i < settings.size()) {
            Setting setting = (Setting)settings.get(i);
            Item item = (Item)this.items.get(i);
            item.setHeight(this.height);
            double visibleH = item.getVisibleHeight();
            if (visibleH <= 0.01 && item.isHidden()) {
               ++i;
            } else {
               item.setLocation(this.x + 1.0F, this.y + height + slide);
               item.setWidth(this.width - 9);
               if (setting instanceof BooleanSetting && ((BooleanSetting)setting).hasParent()) {
                  item.drawScreen(context, mouseX, mouseY, partialTicks);
                  height += (float)(visibleH + (double)2.0F);
                  BooleanSetting parent = (BooleanSetting)setting;
                  double openProgress = this.getParentOpenProgress(parent);

                  int j;
                  for(j = i + 1; j < settings.size() && this.isChildOf(parent, (Setting)settings.get(j)); ++j) {
                  }

                  double childrenFull = (double)0.0F;

                  for(int k = i + 1; k < j && k < this.items.size(); ++k) {
                     if (this.isVisibleWithParentOpen(parent, (Setting)settings.get(k))) {
                        Item child = (Item)this.items.get(k);
                        child.setHeight(this.height);
                        double childVisibleH = child.getVisibleHeight();
                        if (!(childVisibleH <= 0.01) || !child.isHidden()) {
                           childrenFull += childVisibleH + (double)2.0F;
                        }
                     }
                  }

                  double childrenVisible = childrenFull * openProgress;
                  if (childrenVisible > 0.01) {
                     float childSlide = (1.0F - (float)openProgress) * 6.0F;
                     float childStartY = this.y + height + slide + childSlide;
                     float childX = this.x + 2.0F;
                     int childW = this.width - 11;
                     int scissorX1 = (int)childX - 1;
                     int scissorY1 = (int)childStartY - 1;
                     int scissorX2 = (int)(childX + (float)childW + 8.0F) + 1;
                     int scissorY2 = (int)Math.round((double)childStartY + childrenVisible) + 1;
                     context.method_44379(scissorX1, scissorY1, scissorX2, scissorY2);
                     float yOff = childStartY;

                     for(int k = i + 1; k < j && k < this.items.size(); ++k) {
                        if (this.isVisibleWithParentOpen(parent, (Setting)settings.get(k))) {
                           Item child = (Item)this.items.get(k);
                           child.setHeight(this.height);
                           double childVisibleH = child.getVisibleHeight();
                           if (!(childVisibleH <= 0.01) || !child.isHidden()) {
                              child.setLocation(childX, yOff);
                              child.setWidth(childW);
                              if (childVisibleH < (double)child.getHeight() - 0.01) {
                                 int cX1 = (int)child.getX() - 1;
                                 int cY1 = (int)child.getY() - 1;
                                 int cX2 = (int)(child.getX() + (float)child.getWidth() + 7.0F) + 1;
                                 int cY2 = (int)((double)child.getY() + childVisibleH) + 1;
                                 int iX1 = Math.max(scissorX1, cX1);
                                 int iY1 = Math.max(scissorY1, cY1);
                                 int iX2 = Math.min(scissorX2, cX2);
                                 int iY2 = Math.min(scissorY2, cY2);
                                 if (iX2 > iX1 && iY2 > iY1) {
                                    context.method_44379(iX1, iY1, iX2, iY2);
                                    child.drawScreen(context, mouseX, mouseY, partialTicks);
                                    context.method_44380();
                                 }
                              } else {
                                 child.drawScreen(context, mouseX, mouseY, partialTicks);
                              }

                              yOff += (float)(childVisibleH + (double)2.0F);
                           }
                        }
                     }

                     context.method_44380();
                  }

                  height += (float)childrenVisible;
                  i = j;
               } else {
                  if (visibleH < (double)item.getHeight() - 0.01) {
                     int scissorX1 = (int)item.getX() - 1;
                     int scissorY1 = (int)item.getY() - 1;
                     int scissorX2 = (int)(item.getX() + (float)item.getWidth() + 7.0F) + 1;
                     int scissorY2 = (int)((double)item.getY() + visibleH) + 1;
                     context.method_44379(scissorX1, scissorY1, scissorX2, scissorY2);
                     item.drawScreen(context, mouseX, mouseY, partialTicks);
                     context.method_44380();
                  } else {
                     item.drawScreen(context, mouseX, mouseY, partialTicks);
                  }

                  height += (float)(visibleH + (double)2.0F);
                  ++i;
               }
            }
         }
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && class_3675.method_15987(mc.method_22683().method_4490(), 340)) {
         this.resetSettingsToDefault();
         sound();
      } else {
         super.mouseClicked(mouseX, mouseY, mouseButton);
         if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
               this.subOpen = !this.subOpen;
               if (this.subOpen) {
                  for(Item item : this.items) {
                     if (item instanceof SliderButton) {
                        ((SliderButton)item).resetFillAnimation();
                     }
                  }
               }

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

   public double getVisibleItemHeight() {
      double height = (double)3.0F;
      List<Setting> settings = this.module.getSettings();
      int i = 0;

      while(i < this.items.size() && i < settings.size()) {
         Setting setting = (Setting)settings.get(i);
         Item item = (Item)this.items.get(i);
         item.setHeight(this.height);
         double visibleH = item.getVisibleHeight();
         if (visibleH <= 0.01 && item.isHidden()) {
            ++i;
         } else {
            height += visibleH + (double)2.0F;
            if (setting instanceof BooleanSetting && ((BooleanSetting)setting).hasParent()) {
               BooleanSetting parent = (BooleanSetting)setting;
               double openProgress = this.getParentOpenProgress(parent);

               int j;
               for(j = i + 1; j < settings.size() && this.isChildOf(parent, (Setting)settings.get(j)); ++j) {
               }

               double childrenFull = (double)0.0F;

               for(int k = i + 1; k < j && k < this.items.size(); ++k) {
                  if (this.isVisibleWithParentOpen(parent, (Setting)settings.get(k))) {
                     Item child = (Item)this.items.get(k);
                     child.setHeight(this.height);
                     double childVisibleH = child.getVisibleHeight();
                     if (!(childVisibleH <= 0.01) || !child.isHidden()) {
                        childrenFull += childVisibleH + (double)2.0F;
                     }
                  }
               }

               height += childrenFull * openProgress;
               i = j;
            } else {
               ++i;
            }
         }
      }

      return height;
   }

   public int getItemHeight() {
      return (int)Math.round(this.getVisibleItemHeight());
   }

   private void resetSettingsToDefault() {
      for(Setting setting : this.module.getSettings()) {
         this.resetSettingToDefault(setting);
      }

   }

   public void resetChildSettingsToDefault(BooleanSetting parent) {
      boolean originalOpen = parent.isOpen();

      for(Setting setting : this.module.getSettings()) {
         if (setting != parent) {
            parent.setOpen(true);
            boolean visibleWhenOpen = setting.isVisible();
            parent.setOpen(false);
            boolean visibleWhenClosed = setting.isVisible();
            parent.setOpen(originalOpen);
            if (visibleWhenOpen && !visibleWhenClosed) {
               if (setting instanceof BooleanSetting) {
                  BooleanSetting s = (BooleanSetting)setting;
                  if (s.hasParent()) {
                     this.resetChildSettingsToDefault(s);
                  }
               }

               this.resetSettingToDefault(setting);
            }
         }
      }

   }

   public void resetPageSettingsToDefault(EnumSetting<?> page) {
      Enum current = page.getValue();
      Enum[] values = (Enum[])current.getDeclaringClass().getEnumConstants();
      String originalName = current.name();
      page.setEnumValue(originalName);

      for(Setting setting : this.module.getSettings()) {
         if (setting != page) {
            page.setEnumValue(originalName);
            boolean visibleInCurrent = setting.isVisible();
            if (visibleInCurrent) {
               boolean visibleInAll = true;

               for(Enum e : values) {
                  page.setEnumValue(e.name());
                  if (!setting.isVisible()) {
                     visibleInAll = false;
                     break;
                  }
               }

               page.setEnumValue(originalName);
               if (!visibleInAll) {
                  if (setting instanceof BooleanSetting) {
                     BooleanSetting s = (BooleanSetting)setting;
                     if (s.hasParent()) {
                        this.resetChildSettingsToDefault(s);
                     }
                  }

                  this.resetSettingToDefault(setting);
               }
            }
         }
      }

      page.setEnumValue(originalName);
   }

   private double getParentOpenProgress(BooleanSetting parent) {
      Animation anim = (Animation)this.parentOpenAnimations.get(parent);
      if (anim == null) {
         anim = new Animation();
         this.parentOpenAnimations.put(parent, anim);
      }

      return anim.get(parent.isOpen() ? (double)1.0F : (double)0.0F, 200L, Easing.CubicInOut);
   }

   private boolean isChildOf(BooleanSetting parent, Setting setting) {
      boolean originalOpen = parent.isOpen();
      parent.setOpen(true);
      boolean visibleWhenOpen = setting.isVisible();
      parent.setOpen(false);
      boolean visibleWhenClosed = setting.isVisible();
      parent.setOpen(originalOpen);
      return visibleWhenOpen && !visibleWhenClosed;
   }

   private boolean isVisibleWithParentOpen(BooleanSetting parent, Setting setting) {
      boolean originalOpen = parent.isOpen();
      parent.setOpen(true);
      boolean visible = setting.isVisible();
      parent.setOpen(originalOpen);
      return visible;
   }

   private void resetSettingToDefault(Setting setting) {
      if (setting instanceof BooleanSetting s) {
         s.setValue(s.getDefaultValue());
         if (s.hasParent()) {
            s.setOpen(false);
         }

      } else if (setting instanceof SliderSetting s) {
         s.setValue(s.getDefaultValue());
      } else if (setting instanceof StringSetting s) {
         s.setValue(s.getDefaultValue());
      } else if (setting instanceof BindSetting s) {
         s.setValue(s.getDefaultValue());
         s.setHoldEnable(false);
         s.setPressed(false);
         s.holding = false;
      } else if (setting instanceof EnumSetting<?> s) {
         Enum defaultValue = s.getDefaultValue();
         s.setEnumValue(defaultValue.name());
      } else {
         if (setting instanceof ColorSetting s) {
            s.setValue(s.getDefaultValue());
            s.rainbow = s.getDefaultRainbow();
            if (s.injectBoolean) {
               s.booleanValue = s.getDefaultBooleanValue();
            }
         }

      }
   }

   public int getHeight() {
      if (this.subOpen) {
         int height = super.getHeight();

         for(Item item : this.items) {
            if (!item.isHidden()) {
               height += item.getHeight() + 1;
            }
         }

         return height + 2;
      } else {
         return super.getHeight();
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
}
