package dev.luminous.mod.gui.windows.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.Manager;
import dev.luminous.core.impl.CleanerManager;
import dev.luminous.core.impl.FontManager;
import dev.luminous.core.impl.TradeManager;
import dev.luminous.core.impl.XrayManager;
import dev.luminous.mod.gui.items.buttons.StringButton;
import dev.luminous.mod.gui.windows.WindowBase;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.class_1074;
import net.minecraft.class_1792;
import net.minecraft.class_2248;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3544;
import net.minecraft.class_3675;
import net.minecraft.class_757;
import net.minecraft.class_7923;
import net.minecraft.class_293.class_5596;

public class ItemSelectWindow extends WindowBase {
   private final Manager manager;
   private final ArrayList<ItemPlate> itemPlates;
   private final ArrayList<ItemPlate> allItems;
   private boolean allTab;
   private boolean listening;
   private String search;

   public ItemSelectWindow(Manager manager) {
      this((float)Wrapper.mc.method_22683().method_4486() / 2.0F - 100.0F, (float)Wrapper.mc.method_22683().method_4502() / 2.0F - 150.0F, 200.0F, 300.0F, manager);
   }

   public ItemSelectWindow(float x, float y, float width, float height, Manager manager) {
      super(x, y, width, height, "Items", (class_2960)null);
      this.itemPlates = new ArrayList();
      this.allItems = new ArrayList();
      this.allTab = true;
      this.listening = false;
      this.search = "Search";
      this.manager = manager;
      this.refreshItemPlates();
      int id1 = 0;

      for(class_2248 block : class_7923.field_41175) {
         this.allItems.add(new ItemPlate((float)id1, (float)(id1 * 20), block.method_8389(), block.method_9539()));
         ++id1;
      }

      for(class_1792 item : class_7923.field_41178) {
         this.allItems.add(new ItemPlate((float)id1, (float)(id1 * 20), item, item.method_7876()));
         ++id1;
      }

   }

   public void render(class_332 context, int mouseX, int mouseY) {
      super.render(context, mouseX, mouseY);
      boolean hover1 = Render2DUtil.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 90.0F), (double)(this.getY() + 3.0F), (double)70.0F, (double)10.0F);
      Render2DUtil.drawRect(context.method_51448(), this.getX() + this.getWidth() - 90.0F, this.getY() + 3.0F, 70.0F, 10.0F, hover1 ? new Color(-981236861, true) : new Color(-984131753, true));
      FontManager.small.drawString(context.method_51448(), this.search, (double)(this.getX() + this.getWidth() - 86.0F), (double)(this.getY() + 7.0F), (new Color(14013909)).getRGB());
      RenderSystem.setShader(class_757::method_34540);
      int tabColor1 = this.allTab ? (new Color(14013909)).getRGB() : Color.GRAY.getRGB();
      int tabColor2 = this.allTab ? Color.GRAY.getRGB() : (new Color(12434877)).getRGB();
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_29345, class_290.field_1576);
      bufferBuilder.method_22912(this.getX() + 1.5F, this.getY() + 29.0F, 0.0F).method_39415(Color.DARK_GRAY.getRGB());
      bufferBuilder.method_22912(this.getX() + 8.0F, this.getY() + 29.0F, 0.0F).method_39415(tabColor1);
      bufferBuilder.method_22912(this.getX() + 8.0F, this.getY() + 19.0F, 0.0F).method_39415(tabColor1);
      bufferBuilder.method_22912(this.getX() + 48.0F, this.getY() + 19.0F, 0.0F).method_39415(tabColor1);
      bufferBuilder.method_22912(this.getX() + 54.0F, this.getY() + 29.0F, 0.0F).method_39415(tabColor1);
      bufferBuilder.method_22912(this.getX() + 52.0F, this.getY() + 25.0F, 0.0F).method_39415(tabColor2);
      bufferBuilder.method_22912(this.getX() + 52.0F, this.getY() + 19.0F, 0.0F).method_39415(tabColor2);
      bufferBuilder.method_22912(this.getX() + 92.0F, this.getY() + 19.0F, 0.0F).method_39415(tabColor2);
      bufferBuilder.method_22912(this.getX() + 100.0F, this.getY() + 29.0F, 0.0F).method_39415(Color.GRAY.getRGB());
      bufferBuilder.method_22912(this.getX() + this.getWidth() - 1.0F, this.getY() + 29.0F, 0.0F).method_39415(Color.DARK_GRAY.getRGB());
      class_286.method_43433(bufferBuilder.method_60800());
      FontManager.small.drawString(context.method_51448(), "All", (double)(this.getX() + 25.0F), (double)(this.getY() + 25.0F), tabColor1);
      FontManager.small.drawString(context.method_51448(), "Selected", (double)(this.getX() + 60.0F), (double)(this.getY() + 25.0F), tabColor2);
      if (!this.allTab && this.itemPlates.isEmpty()) {
         FontManager.ui.drawCenteredString(context.method_51448(), "It's empty here yet", (double)(this.getX() + this.getWidth() / 2.0F), (double)(this.getY() + this.getHeight() / 2.0F), (new Color(12434877)).getRGB());
      }

      context.method_44379((int)this.getX(), (int)(this.getY() + 30.0F), (int)(this.getX() + this.getWidth()), (int)(this.getY() + this.getHeight() - 1.0F));

      for(ItemPlate itemPlate : this.allTab ? this.allItems : this.itemPlates) {
         if (!(itemPlate.offset + this.getY() + 25.0F + this.getScrollOffset() > this.getY() + this.getHeight()) && !(itemPlate.offset + this.getScrollOffset() + this.getY() + 10.0F < this.getY())) {
            context.method_51448().method_22903();
            context.method_51448().method_46416(this.getX() + 6.0F, itemPlate.offset + this.getY() + 32.0F + this.getScrollOffset(), 0.0F);
            context.method_51427(itemPlate.item().method_7854(), 0, 0);
            context.method_51448().method_22909();
            FontManager.ui.drawString(context.method_51448(), class_1074.method_4662(itemPlate.key(), new Object[0]), (double)(this.getX() + 26.0F), (double)(itemPlate.offset + this.getY() + 38.0F + this.getScrollOffset()), (new Color(12434877)).getRGB());
            boolean hover2 = Render2DUtil.isHovered((double)mouseX, (double)mouseY, (double)(this.getX() + this.getWidth() - 20.0F), (double)(itemPlate.offset + this.getY() + 35.0F + this.getScrollOffset()), (double)11.0F, (double)11.0F);
            Render2DUtil.drawRect(context.method_51448(), this.getX() + this.getWidth() - 20.0F, itemPlate.offset + this.getY() + 35.0F + this.getScrollOffset(), 11.0F, 11.0F, hover2 ? new Color(-981828998, true) : new Color(-984131753, true));
            boolean selected = this.itemPlates.stream().anyMatch((sI) -> Objects.equals(sI.key, itemPlate.key));
            if (this.allTab && !selected) {
               FontManager.ui.drawString(context.method_51448(), "+", (double)(this.getX() + this.getWidth() - 17.0F), (double)(itemPlate.offset + this.getY() + 37.0F + this.getScrollOffset()), -1);
            } else {
               FontManager.ui.drawString(context.method_51448(), "-", (double)(this.getX() + this.getWidth()) - (double)16.5F, (double)(itemPlate.offset + this.getY()) + (double)37.5F + (double)this.getScrollOffset(), -1);
            }
         }
      }

      this.setMaxElementsHeight((float)((this.allTab ? this.allItems : this.itemPlates).size() * 20));
      context.method_44380();
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      super.mouseClicked(mouseX, mouseY, button);
      if (Render2DUtil.isHovered(mouseX, mouseY, (double)(this.getX() + 8.0F), (double)(this.getY() + 19.0F), (double)52.0F, (double)19.0F)) {
         this.allTab = true;
         this.resetScroll();
      }

      if (Render2DUtil.isHovered(mouseX, mouseY, (double)(this.getX() + 54.0F), (double)(this.getY() + 19.0F), (double)70.0F, (double)19.0F)) {
         this.allTab = false;
         this.resetScroll();
      }

      if (Render2DUtil.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 90.0F), (double)(this.getY() + 3.0F), (double)70.0F, (double)10.0F)) {
         this.listening = true;
         this.search = "";
      }

      for(ItemPlate itemPlate : Lists.newArrayList(this.allTab ? this.allItems : this.itemPlates)) {
         if (!((float)((int)(itemPlate.offset + this.getY() + 50.0F)) + this.getScrollOffset() > this.getY() + this.getHeight())) {
            String name = itemPlate.key().replace("item.minecraft.", "").replace("block.minecraft.", "");
            if (Render2DUtil.isHovered(mouseX, mouseY, (double)(this.getX() + this.getWidth() - 20.0F), (double)(itemPlate.offset + this.getY() + 35.0F + this.getScrollOffset()), (double)10.0F, (double)10.0F)) {
               boolean selected = this.itemPlates.stream().anyMatch((sI) -> Objects.equals(sI.key(), itemPlate.key));
               if (this.allTab && !selected) {
                  Manager var19 = this.manager;
                  if (var19 instanceof TradeManager) {
                     TradeManager m = (TradeManager)var19;
                     if (!m.inWhitelist(name)) {
                        m.add(name);
                        this.refreshItemPlates();
                     }
                  } else {
                     var19 = this.manager;
                     if (var19 instanceof CleanerManager) {
                        CleanerManager mx = (CleanerManager)var19;
                        if (!mx.inList(name)) {
                           mx.add(name);
                           this.refreshItemPlates();
                        }
                     } else {
                        var19 = this.manager;
                        if (var19 instanceof XrayManager) {
                           XrayManager mxx = (XrayManager)var19;
                           if (!mxx.inWhitelist(name)) {
                              mxx.add(name);
                              this.refreshItemPlates();
                           }
                        }
                     }
                  }
               } else {
                  Manager var13 = this.manager;
                  if (var13 instanceof TradeManager) {
                     TradeManager mxx = (TradeManager)var13;
                     mxx.remove(name);
                  } else {
                     var13 = this.manager;
                     if (var13 instanceof CleanerManager) {
                        CleanerManager mxx = (CleanerManager)var13;
                        mxx.remove(name);
                     } else {
                        var13 = this.manager;
                        if (var13 instanceof XrayManager) {
                           XrayManager mxx = (XrayManager)var13;
                           mxx.remove(name);
                        }
                     }
                  }

                  this.refreshItemPlates();
               }
            }
         }
      }

   }

   public void keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode != 70 || !class_3675.method_15987(Wrapper.mc.method_22683().method_4490(), 341) && !class_3675.method_15987(Wrapper.mc.method_22683().method_4490(), 345)) {
         if (this.listening) {
            switch (keyCode) {
               case 32:
                  this.search = this.search + " ";
                  break;
               case 256:
                  this.listening = false;
                  this.search = "Search";
                  this.refreshAllItems();
                  break;
               case 259:
                  this.search = StringButton.removeLastChar(this.search);
                  this.refreshAllItems();
                  if (Objects.equals(this.search, "")) {
                     this.listening = false;
                     this.search = "Search";
                  }
            }
         }
      } else {
         this.listening = !this.listening;
      }

   }

   public void charTyped(char key, int keyCode) {
      if (class_3544.method_57175(key) && this.listening) {
         this.search = this.search + key;
         this.refreshAllItems();
      }

   }

   private void refreshItemPlates() {
      this.itemPlates.clear();
      int id = 0;

      for(class_1792 item : class_7923.field_41178) {
         Manager var7 = this.manager;
         if (var7 instanceof TradeManager m) {
            if (m.inWhitelist(item.method_7876())) {
               this.itemPlates.add(new ItemPlate((float)id, (float)(id * 20), item.method_8389(), item.method_7876()));
               ++id;
            }
         } else {
            var7 = this.manager;
            if (var7 instanceof CleanerManager mx) {
               if (mx.inList(item.method_7876())) {
                  this.itemPlates.add(new ItemPlate((float)id, (float)(id * 20), item.method_8389(), item.method_7876()));
                  ++id;
               }
            } else {
               var7 = this.manager;
               if (var7 instanceof XrayManager mxx) {
                  if (mxx.inWhitelist(item.method_7876())) {
                     this.itemPlates.add(new ItemPlate((float)id, (float)(id * 20), item.method_8389(), item.method_7876()));
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
      int id1 = 0;

      for(class_1792 item : class_7923.field_41178) {
         if (this.search.equals("Search") || this.search.isEmpty() || item.method_7876().contains(this.search) || item.method_7848().getString().toLowerCase().contains(this.search.toLowerCase())) {
            this.allItems.add(new ItemPlate((float)id1, (float)(id1 * 20), item, item.method_7876()));
            ++id1;
         }
      }

   }

   private static record ItemPlate(float id, float offset, class_1792 item, String key) {
   }
}
