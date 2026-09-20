package dev.Astra.mod.gui.windows.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.AnimateUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.core.Manager;
import dev.Astra.core.impl.CleanerManager;
import dev.Astra.core.impl.FontManager;
import dev.Astra.core.impl.HudItemManager;
import dev.Astra.core.impl.TradeManager;
import dev.Astra.core.impl.XrayManager;
import dev.Astra.mod.gui.items.buttons.StringButton;
import dev.Astra.mod.gui.windows.WindowBase;
import dev.Astra.mod.gui.windows.WindowsScreen;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.class_1074;
import net.minecraft.class_1792;
import net.minecraft.class_2248;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3544;
import net.minecraft.class_3675;
import net.minecraft.class_757;
import net.minecraft.class_7923;

public class ItemSelectWindow extends WindowBase {
   private final Manager manager;
   private final ArrayList<ItemPlate> itemPlates;
   private final ArrayList<ItemPlate> allItems;
   private final StringSetting searchSetting;
   private final StringButton searchButton;
   private boolean allTab;
   private float tabY;
   private float tabH;
   private float tabAllX;
   private float tabAllW;
   private float tabSelX;
   private float tabSelW;
   private float listTop;
   private float tabAnimX;
   private float tabAnimW;
   private boolean tabAnimInit;

   public ItemSelectWindow(Manager manager) {
      this((float)Wrapper.mc.method_22683().method_4486() / 2.0F - 100.0F, (float)Wrapper.mc.method_22683().method_4502() / 2.0F - 150.0F, 200.0F, 300.0F, manager);
   }

   public ItemSelectWindow(float x, float y, float width, float height, Manager manager) {
      super(x, y, width, height, getTitleLabel(), (class_2960)null);
      this.itemPlates = new ArrayList();
      this.allItems = new ArrayList();
      this.allTab = true;
      this.manager = manager;
      this.searchSetting = (new StringSetting(getSearchLabel(), "")).injectTask(this::refreshAllItems);
      this.searchButton = new StringButton(this.searchSetting);
      this.refreshItemPlates();
      float rowH = this.getRowHeight();
      int id1 = 0;

      for(class_2248 block : class_7923.field_41175) {
         this.allItems.add(new ItemPlate((float)id1, (float)id1 * rowH, block.method_8389(), block.method_9539()));
         ++id1;
      }

      for(class_1792 item : class_7923.field_41178) {
         this.allItems.add(new ItemPlate((float)id1, (float)id1 * rowH, item, item.method_7876()));
         ++id1;
      }

   }

   public void render(class_332 context, int mouseX, int mouseY) {
      this.layoutTabs();
      super.render(context, mouseX, mouseY);
      this.layoutSearchButton();
      this.searchButton.drawScreen(context, mouseX, mouseY, 0.0F);
      RenderSystem.setShader(class_757::method_34540);
      ClickGui gui = ClickGui.getInstance();
      boolean shadow = FontManager.isShadowEnabled();
      boolean hoverAll = Render2DUtil.isHovered((double)mouseX, (double)mouseY, (double)this.tabAllX, (double)this.tabY, (double)this.tabAllW, (double)this.tabH);
      boolean hoverSel = Render2DUtil.isHovered((double)mouseX, (double)mouseY, (double)this.tabSelX, (double)this.tabY, (double)this.tabSelW, (double)this.tabH);
      int defaultBg = gui != null ? gui.defaultColor.getValue().getRGB() : (new Color(30, 30, 30, 236)).getRGB();
      int hoverBg = gui != null ? gui.hoverColor.getValue().getRGB() : (new Color(50, 50, 50, 200)).getRGB();
      int defaultText = gui != null ? gui.defaultTextColor.getValue().getRGB() : (new Color(220, 220, 220)).getRGB();
      int enableText = gui != null ? gui.enableTextColor.getValue().getRGB() : -1;
      Render2DUtil.rect(context.method_51448(), this.tabAllX, this.tabY, this.tabAllX + this.tabAllW, this.tabY + this.tabH, hoverAll ? hoverBg : defaultBg);
      Render2DUtil.rect(context.method_51448(), this.tabSelX, this.tabY, this.tabSelX + this.tabSelW, this.tabY + this.tabH, hoverSel ? hoverBg : defaultBg);
      float targetX = this.allTab ? this.tabAllX : this.tabSelX;
      float targetW = this.allTab ? this.tabAllW : this.tabSelW;
      float dt = AnimateUtil.deltaTime();
      if (dt <= 0.0F) {
         dt = 0.016F;
      }

      float a = dt * 18.0F;
      if (a < 0.0F) {
         a = 0.0F;
      }

      if (a > 0.35F) {
         a = 0.35F;
      }

      boolean dragging = WindowsScreen.draggingWindow == this;
      if (this.tabAnimInit && !dragging) {
         this.tabAnimX += (targetX - this.tabAnimX) * a;
         this.tabAnimW += (targetW - this.tabAnimW) * a;
      } else {
         this.tabAnimX = targetX;
         this.tabAnimW = targetW;
         this.tabAnimInit = true;
      }

      boolean hoverActive = this.allTab ? hoverAll : hoverSel;
      int activeAlpha = gui != null ? (hoverActive ? gui.hoverAlpha.getValueInt() : gui.alpha.getValueInt()) : 200;
      if (gui != null && gui.colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
         Render2DUtil.drawLutRect(context.method_51448(), this.tabAnimX, this.tabY, this.tabAnimW, this.tabH, gui.getSpectrumLutId(), gui.getSpectrumLutHeight(), activeAlpha);
      } else {
         Color ac = gui != null ? gui.getActiveColor((double)this.tabY * (double)0.25F) : new Color(0, 120, 212);
         Render2DUtil.rect(context.method_51448(), this.tabAnimX, this.tabY, this.tabAnimX + this.tabAnimW, this.tabY + this.tabH, ColorUtil.injectAlpha(ac, activeAlpha).getRGB());
      }

      int allText = !this.allTab && !hoverAll ? defaultText : enableText;
      int selText = this.allTab && !hoverSel ? defaultText : enableText;
      float tabTextY = this.tabY + (this.tabH - FontManager.small.getFontHeight()) / 2.0F;
      String allLabel = getAllLabel();
      String selectedLabel = getSelectedLabel();
      FontManager.small.drawString(context.method_51448(), allLabel, (double)(this.tabAllX + (this.tabAllW - FontManager.small.getWidth(allLabel)) / 2.0F), (double)tabTextY, allText, shadow);
      FontManager.small.drawString(context.method_51448(), selectedLabel, (double)(this.tabSelX + (this.tabSelW - FontManager.small.getWidth(selectedLabel)) / 2.0F), (double)tabTextY, selText, shadow);
      if (!this.allTab && this.itemPlates.isEmpty()) {
         FontManager.ui.drawCenteredString(context.method_51448(), getEmptyLabel(), (double)(this.getX() + this.getWidth() / 2.0F), (double)(this.getY() + this.getHeight() / 2.0F), (new Color(12434877)).getRGB());
      }

      float rowH = this.getRowHeight();
      float iconSize = 16.0F;
      float leftPad = rowH * 0.3F;
      float textGap = rowH * 0.3F;
      float buttonSize = Math.max(8.0F, rowH * 0.5F);
      float rightPad = rowH * 0.25F + this.getScrollBarWidth();
      context.method_44379((int)this.getX(), (int)this.listTop, (int)(this.getX() + this.getWidth()), (int)(this.getY() + this.getHeight() - 1.0F));

      for(ItemPlate itemPlate : this.allTab ? this.allItems : this.itemPlates) {
         float itemY = itemPlate.offset + this.listTop + this.getScrollOffset();
         if (!(itemY > this.getY() + this.getHeight()) && !(itemY + rowH < this.getY())) {
            context.method_51448().method_22903();
            context.method_51448().method_46416(this.getX() + leftPad, itemY + (rowH - iconSize) / 2.0F, 0.0F);
            context.method_51427(itemPlate.item().method_7854(), 0, 0);
            context.method_51448().method_22909();
            float textY = itemY + (rowH - FontManager.ui.getFontHeight()) / 2.0F;
            float textX = this.getX() + leftPad + iconSize + textGap;
            FontManager.ui.drawString(context.method_51448(), class_1074.method_4662(itemPlate.key(), new Object[0]), (double)textX, (double)textY, (new Color(12434877)).getRGB(), shadow);
            float buttonX = this.getX() + this.getWidth() - rightPad - buttonSize;
            float buttonY = itemY + (rowH - buttonSize) / 2.0F;
            boolean hover2 = Render2DUtil.isHovered((double)mouseX, (double)mouseY, (double)buttonX, (double)buttonY, (double)buttonSize, (double)buttonSize);
            Render2DUtil.drawRect(context.method_51448(), buttonX, buttonY, buttonSize, buttonSize, hover2 ? new Color(-981828998, true) : new Color(-984131753, true));
            boolean selected = this.itemPlates.stream().anyMatch((sI) -> Objects.equals(sI.key, itemPlate.key));
            if (this.allTab && !selected) {
               FontManager.ui.drawString(context.method_51448(), "+", (double)(buttonX + (buttonSize - FontManager.ui.getWidth("+")) / 2.0F), (double)(buttonY + (buttonSize - FontManager.ui.getFontHeight()) / 2.0F), -1, shadow);
            } else {
               FontManager.ui.drawString(context.method_51448(), "-", (double)(buttonX + (buttonSize - FontManager.ui.getWidth("-")) / 2.0F), (double)(buttonY + (buttonSize - FontManager.ui.getFontHeight()) / 2.0F), -1, shadow);
            }
         }
      }

      this.setMaxElementsHeight((float)(this.allTab ? this.allItems : this.itemPlates).size() * rowH);
      context.method_44380();
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      super.mouseClicked(mouseX, mouseY, button);
      this.layoutSearchButton();
      this.layoutTabs();
      if (this.searchButton.isHovering((int)mouseX, (int)mouseY)) {
         this.searchButton.mouseClicked((int)mouseX, (int)mouseY, button);
      } else {
         if (Render2DUtil.isHovered(mouseX, mouseY, (double)this.tabAllX, (double)this.tabY, (double)this.tabAllW, (double)this.tabH)) {
            this.allTab = true;
            this.resetScroll();
         }

         if (Render2DUtil.isHovered(mouseX, mouseY, (double)this.tabSelX, (double)this.tabY, (double)this.tabSelW, (double)this.tabH)) {
            this.allTab = false;
            this.resetScroll();
         }

         float rowH = this.getRowHeight();
         float buttonSize = Math.max(8.0F, rowH * 0.5F);
         float rightPad = rowH * 0.25F + this.getScrollBarWidth();

         for(ItemPlate itemPlate : Lists.newArrayList(this.allTab ? this.allItems : this.itemPlates)) {
            float itemY = itemPlate.offset + this.listTop + this.getScrollOffset();
            if (!(itemY + rowH > this.getY() + this.getHeight())) {
               String name = itemPlate.key().replace("item.minecraft.", "").replace("block.minecraft.", "");
               float buttonX = this.getX() + this.getWidth() - rightPad - buttonSize;
               float buttonY = itemY + (rowH - buttonSize) / 2.0F;
               if (Render2DUtil.isHovered(mouseX, mouseY, (double)buttonX, (double)buttonY, (double)buttonSize, (double)buttonSize)) {
                  boolean selected = this.itemPlates.stream().anyMatch((sI) -> Objects.equals(sI.key(), itemPlate.key));
                  if (this.allTab && !selected) {
                     Manager manager = this.manager;
                     if (manager instanceof TradeManager) {
                        TradeManager m3 = (TradeManager)manager;
                        if (!m3.inWhitelist(name)) {
                           m3.add(name);
                           this.refreshItemPlates();
                        }
                     } else {
                        manager = this.manager;
                        if (manager instanceof CleanerManager) {
                           CleanerManager m2 = (CleanerManager)manager;
                           if (!m2.inList(name)) {
                              m2.add(name);
                              this.refreshItemPlates();
                           }
                        } else {
                           manager = this.manager;
                           if (manager instanceof HudItemManager) {
                              HudItemManager m4 = (HudItemManager)manager;
                              if (!m4.inList(name)) {
                                 m4.add(name);
                                 this.refreshItemPlates();
                              }
                           } else {
                              manager = this.manager;
                              XrayManager m;
                              if (manager instanceof XrayManager && !(m = (XrayManager)manager).inWhitelist(name)) {
                                 m.add(name);
                                 this.refreshItemPlates();
                              }
                           }
                        }
                     }
                  } else {
                     Manager manager = this.manager;
                     if (manager instanceof TradeManager) {
                        TradeManager m3 = (TradeManager)manager;
                        m3.remove(name);
                     } else {
                        manager = this.manager;
                        if (manager instanceof CleanerManager) {
                           CleanerManager m2 = (CleanerManager)manager;
                           m2.remove(name);
                        } else if (manager instanceof HudItemManager) {
                           HudItemManager m4 = (HudItemManager)manager;
                           m4.remove(name);
                        } else {
                           manager = this.manager;
                           if (manager instanceof XrayManager) {
                              XrayManager m = (XrayManager)manager;
                              m.remove(name);
                           }
                        }
                     }

                     this.refreshItemPlates();
                  }
               }
            }
         }

      }
   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode != 70 || !class_3675.method_15987(Wrapper.mc.method_22683().method_4490(), 341) && !class_3675.method_15987(Wrapper.mc.method_22683().method_4490(), 345)) {
         this.searchButton.onKeyPressed(keyCode);
      } else {
         this.searchButton.toggle();
      }
   }

   public void charTyped(char key, int keyCode) {
      if (class_3544.method_57175(key)) {
         this.searchButton.onKeyTyped(key, keyCode);
      }

   }

   private void refreshItemPlates() {
      this.itemPlates.clear();
      float rowH = this.getRowHeight();
      int id = 0;

      for(class_1792 item : class_7923.field_41178) {
         Manager manager = this.manager;
         if (manager instanceof TradeManager m2) {
            if (m2.inWhitelist(item.method_7876())) {
               this.itemPlates.add(new ItemPlate((float)id, (float)id * rowH, item.method_8389(), item.method_7876()));
               ++id;
            }
         } else {
            manager = this.manager;
            if (manager instanceof CleanerManager m3) {
               if (m3.inList(item.method_7876())) {
                  this.itemPlates.add(new ItemPlate((float)id, (float)id * rowH, item.method_8389(), item.method_7876()));
                  ++id;
               }
            } else {
               manager = this.manager;
               if (manager instanceof HudItemManager m4) {
                  if (m4.inList(item.method_7876())) {
                     this.itemPlates.add(new ItemPlate((float)id, (float)id * rowH, item.method_8389(), item.method_7876()));
                     ++id;
                  }
               } else {
                  manager = this.manager;
                  XrayManager m;
                  if (manager instanceof XrayManager && (m = (XrayManager)manager).inWhitelist(item.method_7876())) {
                     this.itemPlates.add(new ItemPlate((float)id, (float)id * rowH, item.method_8389(), item.method_7876()));
                     ++id;
                  }
               }
            }
         }
      }

   }

   private void refreshAllItems() {
      this.allItems.clear();
      this.resetScroll();
      float rowH = this.getRowHeight();
      int id1 = 0;

      for(class_1792 item : class_7923.field_41178) {
         String search = this.searchSetting.getValue();
         if (search.isEmpty() || item.method_7876().contains(search) || item.method_7848().getString().toLowerCase().contains(search.toLowerCase())) {
            this.allItems.add(new ItemPlate((float)id1, (float)id1 * rowH, item, item.method_7876()));
            ++id1;
         }
      }

   }

   private float getRowHeight() {
      float iconSize = 16.0F;
      float fontH = FontManager.ui.getFontHeight();
      return Math.max(iconSize, fontH) + Math.max(4.0F, fontH * 0.4F);
   }

   private void layoutSearchButton() {
      ClickGui gui = ClickGui.getInstance();
      float headerH = gui != null ? (float)gui.categoryBarHeight.getValueInt() : 14.0F;
      float boxW = Math.min(this.getWidth() * 0.4F, this.getWidth() - headerH - headerH * 0.5F);
      int buttonW = Math.max((int)(boxW * 0.85F), (int)(headerH * 2.2F));
      int buttonH = Math.max(9, (int)(headerH * 0.7F));
      this.searchButton.setWidth(buttonW);
      this.searchButton.setHeight(buttonH);
      this.searchButton.setLocation(this.getX() + this.getWidth() - headerH - boxW, this.getY() + (headerH - (float)buttonH) / 2.0F);
   }

   private void layoutTabs() {
      ClickGui gui = ClickGui.getInstance();
      float headerH = gui != null ? (float)gui.categoryBarHeight.getValueInt() : 14.0F;
      float gapX = 0.0F;
      float gapY = Math.max(2.0F, headerH * 0.25F);
      float fontH = FontManager.small.getFontHeight();
      float minW = fontH * 2.5F;
      this.tabH = Math.max(7.0F, headerH * 0.8F);
      this.tabY = this.getY() + headerH + gapY;
      String allLabel = getAllLabel();
      String selectedLabel = getSelectedLabel();
      this.tabAllW = Math.max(minW, FontManager.small.getWidth(allLabel) + fontH);
      this.tabSelW = Math.max(minW, FontManager.small.getWidth(selectedLabel) + fontH);
      float leftPad = headerH * 0.6F;
      this.tabAllX = this.getX() + leftPad;
      this.tabSelX = this.tabAllX + this.tabAllW + gapX;
      this.listTop = this.tabY + this.tabH + gapY;
   }

   protected float getScrollTop(float headerH) {
      return this.listTop;
   }

   protected float getScrollHeight(float headerH) {
      float bottom = this.getY() + this.getHeight() - 1.0F;
      return Math.max(1.0F, bottom - this.listTop);
   }

   private static boolean isChinese() {
      return ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
   }

   private static String getTitleLabel() {
      return isChinese() ? "物品" : "Items";
   }

   private static String getSearchLabel() {
      return isChinese() ? "搜索" : "Search";
   }

   private static String getAllLabel() {
      return isChinese() ? "全部" : "All";
   }

   private static String getSelectedLabel() {
      return isChinese() ? "已选" : "Selected";
   }

   private static String getEmptyLabel() {
      return isChinese() ? "这里还没有内容" : "It's empty here yet";
   }

   private static record ItemPlate(float id, float offset, class_1792 item, String key) {
   }
}
