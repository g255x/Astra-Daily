package dev.Astra.mod.gui.clickgui.pages;

import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.Mod;
import dev.Astra.mod.gui.clickgui.ClickGuiFrame;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.gui.items.Component;
import dev.Astra.mod.gui.items.buttons.ModuleButton;
import dev.Astra.mod.gui.windows.WindowBase;
import dev.Astra.mod.gui.windows.WindowsScreen;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.hud.HudSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import org.lwjgl.glfw.GLFW;

public final class ClickGuiHudPage {
   private final ClickGuiScreen host;
   private Component hudComponent;
   private boolean hudPosInit;
   private float hudLocalX;
   private float hudLocalY;
   private HudModule elementDragging;
   private int elementDragDx;
   private int elementDragDy;
   private boolean selecting;
   private int selectStartX;
   private int selectStartY;
   private int selectEndX;
   private int selectEndY;
   private final ArrayList<HudModule> selectedHud = new ArrayList();
   private boolean groupDragging;
   private int groupDragStartMouseX;
   private int groupDragStartMouseY;
   private final ArrayList<GroupDragEntry> groupDragEntries = new ArrayList();
   private boolean layoutMenuOpen;
   private int layoutMenuX;
   private int layoutMenuY;
   private final Animation layoutButtonAnim = new Animation();
   private final Animation layoutMenuAnim = new Animation();
   private WindowBase editWindow;
   private boolean editWindowHover;
   private boolean editWindowPosInit;
   private float editWindowLocalX;
   private float editWindowLocalY;

   public ClickGuiHudPage(ClickGuiScreen host) {
      this.host = host;
   }

   public void init() {
      this.initHudButtons();
   }

   public void openHudWindow(WindowBase window) {
      this.editWindow = window;
      if (this.editWindow != null) {
         this.editWindow.setVisible(true);
         this.editWindowPosInit = false;
      }

   }

   public void resetHudLayout() {
      this.hudPosInit = false;
   }

   public void mouseScrolled(double verticalAmount) {
      if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         if (this.editWindow != null && this.editWindow.isVisible()) {
            this.editWindow.mouseScrolled((int)verticalAmount);
            if (this.editWindowHover) {
               return;
            }
         }

         if (this.hudComponent != null) {
            if (this.hudPosInit) {
               if (class_3675.method_15987(Wrapper.mc.method_22683().method_4490(), 340)) {
                  if (verticalAmount < (double)0.0F) {
                     this.hudLocalX -= 15.0F;
                  } else if (verticalAmount > (double)0.0F) {
                     this.hudLocalX += 15.0F;
                  }
               } else if (verticalAmount < (double)0.0F) {
                  this.hudLocalY -= 15.0F;
               } else if (verticalAmount > (double)0.0F) {
                  this.hudLocalY += 15.0F;
               }

            }
         }
      }
   }

   public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
      if (this.editWindow != null && this.editWindow.isVisible()) {
         this.editWindow.mouseReleased((double)mouseX, (double)mouseY, releaseButton);
      }

      if (releaseButton == 0) {
         this.elementDragging = null;
         this.groupDragging = false;
         this.groupDragEntries.clear();
      }

      if (this.hudComponent != null) {
         this.hudComponent.mouseReleased(mouseX, mouseY, releaseButton);
      }

   }

   public void keyPressed(int keyCode) {
      if (this.editWindow != null && this.editWindow.isVisible()) {
         this.editWindow.keyPressed(keyCode, 0, 0);
      } else {
         if (keyCode == 256) {
            if (this.layoutMenuOpen) {
               this.closeLayoutMenu();
               return;
            }

            if (!this.selectedHud.isEmpty()) {
               this.clearSelection();
               return;
            }
         }

         if (!this.selectedHud.isEmpty()) {
            if (keyCode == 71) {
               this.applyGridLayout();
               return;
            }

            if (keyCode == 72) {
               this.applyHorizontalLayout();
               return;
            }

            if (keyCode == 86) {
               this.applyVerticalLayout();
               return;
            }

            if (keyCode == 76) {
               this.applyAlignLeft();
               return;
            }

            if (keyCode == 84) {
               this.applyAlignTop();
               return;
            }
         }

         if (this.hudComponent != null) {
            this.hudComponent.onKeyPressed(keyCode);
         }

      }
   }

   public void charTyped(char chr, int modifiers) {
      if (this.editWindow != null && this.editWindow.isVisible()) {
         this.editWindow.charTyped(chr, modifiers);
      } else {
         if (this.hudComponent != null) {
            this.hudComponent.onKeyTyped(chr, modifiers);
         }

      }
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta, ClickGuiFrame frame) {
      ClickGui gui = ClickGui.getInstance();
      if (gui != null) {
         if (this.editWindow != null && !this.editWindow.isVisible()) {
            this.editWindow = null;
            this.editWindowPosInit = false;
         }

         this.dragHudElements(mouseX, mouseY);
         this.updateSelection(mouseX, mouseY);
         float baseX = frame.baseX(ClickGuiScreen.Page.Hud);
         float screenUnitW = frame.scale == 0.0F ? (float)frame.screenW : (float)frame.screenW / frame.scale;
         float panelXf = Math.max(8.0F, (screenUnitW - (float)frame.panelW) / 2.0F);
         float defaultLocalX = panelXf + 10.0F;
         float defaultLocalY = (float)frame.panelY + 10.0F;
         if (this.hudComponent != null) {
            this.hudComponent.setMouseMoveOffset(frame.totalOffsetX, frame.totalOffsetY);
            this.hudComponent.setPageOffsetX(baseX);
            if (!this.hudPosInit) {
               this.hudLocalX = defaultLocalX;
               this.hudLocalY = defaultLocalY;
               this.hudComponent.setX((int)this.hudLocalX);
               this.hudComponent.setY((int)this.hudLocalY);
               this.hudPosInit = true;
            }

            if (this.hudComponent.drag) {
               this.hudLocalX = (float)this.hudComponent.getX() - baseX - frame.totalOffsetX;
               this.hudLocalY = (float)this.hudComponent.getY() - frame.totalOffsetY;
            } else {
               this.hudComponent.setX((int)this.hudLocalX);
               this.hudComponent.setY((int)this.hudLocalY);
            }

            this.hudComponent.drawScreen(context, mouseX, mouseY, delta);
            context.method_51448().method_22903();
            context.method_51448().method_46416(0.0F, 0.0F, 900.0F);
            this.renderSelectionOverlay(context, frame, mouseX, mouseY);
            this.renderLayoutMenu(context, frame, mouseX, mouseY);
            context.method_51448().method_22909();
            if (this.editWindow != null && this.editWindow.isVisible()) {
               int umx = (int)frame.unitMouseX(mouseX);
               int umy = (int)frame.unitMouseY(mouseY);
               context.method_51448().method_22903();
               context.method_51448().method_46416(0.0F, 0.0F, 1100.0F);
               this.syncEditWindowPosition(frame);
               this.editWindowHover = this.isWindowHover(this.editWindow, umx, umy);
               this.editWindow.render(context, umx, umy);
               this.updateEditWindowLocal(frame);
               context.method_51448().method_22909();
            }

         }
      }
   }

   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton, ClickGuiFrame frame) {
      if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         ClickGui gui = ClickGui.getInstance();
         if (gui == null) {
            return false;
         } else {
            if (this.editWindow != null && this.editWindow.isVisible()) {
               this.syncEditWindowPosition(frame);
               int umx = (int)frame.unitMouseX(mouseX);
               int umy = (int)frame.unitMouseY(mouseY);
               this.editWindow.mouseClicked((double)umx, (double)umy, mouseButton);
               if (this.isWindowHover(this.editWindow, umx, umy) || WindowsScreen.draggingWindow == this.editWindow) {
                  return true;
               }
            }

            float mx = frame.unitMouseX(mouseX);
            float my = frame.unitMouseY(mouseY);
            if (this.isLayoutMenuVisible() && this.handleLayoutMenuClick(mouseX, mouseY, mouseButton, frame)) {
               return true;
            } else if (!this.selectedHud.isEmpty() && mouseButton == 0 && this.isHoveringLayoutButton(mx, my, frame)) {
               this.openLayoutMenu(frame);
               return true;
            } else if (this.hudComponent != null && this.hudComponent.isMouseOver(mouseX, mouseY)) {
               this.hudComponent.mouseClicked(mouseX, mouseY, mouseButton);
               return true;
            } else if (mouseButton == 0) {
               if (this.tryBeginDragHudElement(mouseX, mouseY)) {
                  return true;
               } else {
                  this.beginSelection(mouseX, mouseY);
                  return true;
               }
            } else if (mouseButton == 1 && !this.selectedHud.isEmpty()) {
               this.openLayoutMenu(frame);
               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private boolean isWindowHover(WindowBase window, int mouseX, int mouseY) {
      return Render2DUtil.isHovered((double)mouseX, (double)mouseY, (double)window.getX(), (double)window.getY(), (double)window.getWidth(), (double)window.getHeight());
   }

   private void syncEditWindowPosition(ClickGuiFrame frame) {
      if (this.editWindow != null) {
         float baseX = frame.baseX(ClickGuiScreen.Page.Hud);
         if (!this.editWindowPosInit) {
            this.editWindowLocalX = this.editWindow.getX() - baseX - frame.totalOffsetX;
            this.editWindowLocalY = this.editWindow.getY() - frame.totalOffsetY;
            this.editWindowPosInit = true;
         }

         if (WindowsScreen.draggingWindow != this.editWindow) {
            float x = baseX + this.editWindowLocalX + frame.totalOffsetX;
            float y = this.editWindowLocalY + frame.totalOffsetY;
            this.editWindow.setPosition(x, y);
         }

      }
   }

   private void updateEditWindowLocal(ClickGuiFrame frame) {
      if (this.editWindow != null) {
         if (WindowsScreen.draggingWindow == this.editWindow) {
            float baseX = frame.baseX(ClickGuiScreen.Page.Hud);
            this.editWindowLocalX = this.editWindow.getX() - baseX - frame.totalOffsetX;
            this.editWindowLocalY = this.editWindow.getY() - frame.totalOffsetY;
         }

      }
   }

   private void initHudButtons() {
      ArrayList<Module> modules = new ArrayList();

      for(Module m : Astra.MODULE.getModules()) {
         if (this.isHudComponentModule(m)) {
            modules.add(m);
         }
      }

      modules.sort(Comparator.comparingInt(ClickGuiHudPage::getHudListGroup).thenComparingInt(ClickGuiHudPage::getHudListPriority).thenComparing(Mod::getName));
      this.hudComponent = new Component("HUD", Module.Category.Client, 100, 100, true);

      for(Module m : modules) {
         this.hudComponent.addButton(new ModuleButton(m));
      }

   }

   private static int getHudListGroup(Mod m) {
      String name = m.getName();
      if (!name.equals("HUDSetting") && !name.equals("ArrayList") && !name.equals("Coords") && !name.equals("Info") && !name.equals("WaterMark") && !name.equals("Armor")) {
         return name.startsWith("Items") ? 2 : 1;
      } else {
         return 0;
      }
   }

   private static int getHudListPriority(Mod m) {
      short var10000;
      switch (m.getName()) {
         case "HUDSetting" -> var10000 = -1;
         case "ArrayList" -> var10000 = 0;
         case "Coords" -> var10000 = 1;
         case "Info" -> var10000 = 2;
         case "WaterMark" -> var10000 = 3;
         case "Armor" -> var10000 = 4;
         case "TextRadar" -> var10000 = 5;
         default -> var10000 = 999;
      }

      return var10000;
   }

   private boolean isHudComponentModule(Module module) {
      return module instanceof HudModule || module instanceof HudSetting;
   }

   private boolean tryBeginDragHudElement(int mouseX, int mouseY) {
      if (this.hudComponent == null) {
         return false;
      } else {
         for(ModuleButton b : this.hudComponent.getItems()) {
            Module m = b.getModule();
            if (m instanceof HudModule) {
               HudModule hm = (HudModule)m;
               if (hm.isOn() && hm.isHudHit(mouseX, mouseY)) {
                  if (!this.selectedHud.isEmpty() && this.selectedHud.contains(hm)) {
                     this.beginGroupDrag(mouseX, mouseY);
                     return true;
                  }

                  this.groupDragging = false;
                  this.groupDragEntries.clear();
                  this.elementDragging = hm;
                  this.elementDragDx = mouseX - hm.getHudBoundX();
                  this.elementDragDy = mouseY - hm.getHudBoundY();
                  return true;
               }
            }
         }

         return false;
      }
   }

   private void dragHudElements(int mouseX, int mouseY) {
      if (this.groupDragging || this.elementDragging != null) {
         if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
            long handle = Wrapper.mc.method_22683().method_4490();
            if (GLFW.glfwGetMouseButton(handle, 0) != 1) {
               this.elementDragging = null;
               this.groupDragging = false;
               this.groupDragEntries.clear();
            } else if (!this.groupDragging) {
               int nx = mouseX - this.elementDragDx;
               int ny = mouseY - this.elementDragDy;
               this.elementDragging.setHudPosFromBounds(nx, ny);
            } else {
               int dx = mouseX - this.groupDragStartMouseX;
               int dy = mouseY - this.groupDragStartMouseY;

               for(GroupDragEntry e : this.groupDragEntries) {
                  e.module.setHudPosFromBounds(e.startX + dx, e.startY + dy);
               }

            }
         } else {
            this.elementDragging = null;
            this.groupDragging = false;
            this.groupDragEntries.clear();
         }
      }
   }

   private void beginSelection(int mouseX, int mouseY) {
      this.closeLayoutMenu();
      this.selecting = true;
      this.selectStartX = mouseX;
      this.selectStartY = mouseY;
      this.selectEndX = mouseX;
      this.selectEndY = mouseY;
      this.selectedHud.clear();
      this.groupDragging = false;
      this.groupDragEntries.clear();
      this.elementDragging = null;
      if (this.hudComponent != null) {
         this.hudComponent.drag = false;
      }

   }

   private void updateSelection(int mouseX, int mouseY) {
      if (this.selecting) {
         this.selectEndX = mouseX;
         this.selectEndY = mouseY;
         if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
            long handle = Wrapper.mc.method_22683().method_4490();
            if (GLFW.glfwGetMouseButton(handle, 0) != 1) {
               this.finishSelection();
            }

         } else {
            this.finishSelection();
         }
      }
   }

   private void beginGroupDrag(int mouseX, int mouseY) {
      this.closeLayoutMenu();
      this.groupDragging = true;
      this.groupDragStartMouseX = mouseX;
      this.groupDragStartMouseY = mouseY;
      this.groupDragEntries.clear();

      for(HudModule hm : this.selectedHud) {
         if (hm.isOn()) {
            this.groupDragEntries.add(new GroupDragEntry(hm, hm.getHudBoundX(), hm.getHudBoundY()));
         }
      }

      this.elementDragging = null;
      if (this.hudComponent != null) {
         this.hudComponent.drag = false;
      }

   }

   private void finishSelection() {
      if (this.selecting) {
         this.selecting = false;
         int x1 = this.selectStartX;
         int y1 = this.selectStartY;
         int x2 = this.selectEndX;
         int y2 = this.selectEndY;
         this.selectedHud.clear();

         for(Module m : Astra.MODULE.getModules()) {
            if (m instanceof HudModule) {
               HudModule hm = (HudModule)m;
               if (hm.isOn() && hm.isHudOverlapping(x1, y1, x2, y2)) {
                  this.selectedHud.add(hm);
               }
            }
         }

      }
   }

   private void clearSelection() {
      this.selecting = false;
      this.selectedHud.clear();
      this.closeLayoutMenu();
   }

   private void renderSelectionOverlay(class_332 context, ClickGuiFrame frame, int mouseX, int mouseY) {
      ClickGui gui = ClickGui.getInstance();
      if (gui != null) {
         boolean customFont = FontManager.isCustomFontEnabled();
         boolean shadow = FontManager.isShadowEnabled();
         boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
         if (this.selecting) {
            int x1 = this.selectStartX;
            int y1 = this.selectStartY;
            int x2 = this.selectEndX;
            int y2 = this.selectEndY;
            float rx = Math.min(frame.unitMouseX(x1), frame.unitMouseX(x2));
            float ry = Math.min(frame.unitMouseY(y1), frame.unitMouseY(y2));
            float rw = Math.abs(frame.unitMouseX(x2) - frame.unitMouseX(x1));
            float rh = Math.abs(frame.unitMouseY(y2) - frame.unitMouseY(y1));
            Color fill = ColorUtil.injectAlpha(gui.hoverColor.getValue(), 36);
            Color outline = ColorUtil.injectAlpha(gui.hoverColor.getValue(), 170);
            Render2DUtil.drawRectWithOutline(context.method_51448(), rx, ry, rw, rh, fill, outline);
         }

         double btnP = this.layoutButtonAnim.get(this.selectedHud.isEmpty() ? (double)0.0F : (double)1.0F, 160L, Easing.CubicInOut);
         float btnProgress = (float)btnP;
         if (!(btnProgress < 0.01F) || !this.selectedHud.isEmpty()) {
            Color outline = ColorUtil.injectAlpha(gui.hoverColor.getValue(), (int)Math.round((double)210.0F * (double)btnProgress));
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;

            for(HudModule hm : this.selectedHud) {
               int bw = hm.getHudBoundW();
               int bh = hm.getHudBoundH();
               if (bw > 0 && bh > 0) {
                  int bx = hm.getHudBoundX();
                  int by = hm.getHudBoundY();
                  minX = Math.min(minX, bx);
                  minY = Math.min(minY, by);
                  float ux = frame.unitMouseX(bx);
                  float uy = frame.unitMouseY(by);
                  float uw = frame.unitMouseX(bx + bw) - frame.unitMouseX(bx);
                  float uh = frame.unitMouseY(by + bh) - frame.unitMouseY(by);
                  Render2DUtil.drawRectWithOutline(context.method_51448(), ux, uy, uw, uh, new Color(0, 0, 0, 0), outline);
               }
            }

            if (minX != Integer.MAX_VALUE) {
               String label = chinese ? "布局" : "Layout";
               float tw = customFont ? FontManager.ui.getWidth(label) : TextUtil.getWidth(label);
               float th = customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight();
               float pad = 4.0F;
               float bw = tw + pad * 2.0F;
               float bh = th + 4.0F;
               float bx = frame.unitMouseX(minX);
               float by = frame.unitMouseY(minY) - bh - 2.0F;
               float maxH = frame.scale == 0.0F ? (float)frame.screenH : ((float)frame.screenH - frame.slideY) / frame.scale;
               if (by < 1.0F) {
                  by = 1.0F;
               }

               if (by + bh > maxH - 1.0F) {
                  by = maxH - bh - 1.0F;
               }

               float btnScale = 0.85F + 0.15F * btnProgress;
               float cx = bx + bw / 2.0F;
               float cy = by + bh / 2.0F;
               context.method_51448().method_22903();
               context.method_51448().method_46416(cx, cy, 0.0F);
               context.method_51448().method_22905(btnScale, btnScale, 1.0F);
               context.method_51448().method_46416(-cx, -cy, 0.0F);
               int bgA = Math.max(0, Math.min(255, (int)Math.round((double)240.0F * (double)btnProgress)));
               int outA = Math.max(0, Math.min(255, (int)Math.round((double)200.0F * (double)btnProgress)));
               Color bg = ColorUtil.injectAlpha(gui.defaultColor.getValue(), bgA);
               Color out = ColorUtil.injectAlpha(gui.hoverColor.getValue(), outA);
               Render2DUtil.drawRectWithOutline(context.method_51448(), bx, by, bw, bh, bg, out);
               int textA = Math.max(0, Math.min(255, (int)Math.round((double)gui.enableTextColor.getValue().getAlpha() * (double)btnProgress)));
               int textColor = ColorUtil.injectAlpha(gui.enableTextColor.getValue(), textA).getRGB();
               TextUtil.drawString(context, label, (double)(bx + pad), (double)(by + 2.0F), textColor, customFont, shadow);
               context.method_51448().method_22909();
            }
         }
      }
   }

   private void openLayoutMenu(ClickGuiFrame frame) {
      this.layoutMenuOpen = true;
      if (this.selectedHud.isEmpty()) {
         this.layoutMenuX = 0;
         this.layoutMenuY = 0;
      } else {
         boolean customFont = FontManager.isCustomFontEnabled();
         boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
         int minX = Integer.MAX_VALUE;
         int minY = Integer.MAX_VALUE;

         for(HudModule hm : this.selectedHud) {
            int bw = hm.getHudBoundW();
            int bh = hm.getHudBoundH();
            if (bw > 0 && bh > 0) {
               minX = Math.min(minX, hm.getHudBoundX());
               minY = Math.min(minY, hm.getHudBoundY());
            }
         }

         if (minX == Integer.MAX_VALUE) {
            this.layoutMenuX = 0;
            this.layoutMenuY = 0;
         } else {
            String label = chinese ? "布局" : "Layout";
            float tw = customFont ? FontManager.ui.getWidth(label) : TextUtil.getWidth(label);
            float th = customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight();
            float pad = 4.0F;
            float btnW = tw + pad * 2.0F;
            float btnH = th + 4.0F;
            float btnX = frame.unitMouseX(minX);
            float btnY = frame.unitMouseY(minY) - btnH - 2.0F;
            float maxH = frame.scale == 0.0F ? (float)frame.screenH : ((float)frame.screenH - frame.slideY) / frame.scale;
            if (btnY < 1.0F) {
               btnY = 1.0F;
            }

            if (btnY + btnH > maxH - 1.0F) {
               btnY = maxH - btnH - 1.0F;
            }

            float anchorX = btnX + btnW;
            this.layoutMenuX = Math.round(frame.scale == 0.0F ? anchorX : anchorX * frame.scale);
            this.layoutMenuY = Math.round(frame.scale == 0.0F ? btnY : btnY * frame.scale + frame.slideY);
         }
      }
   }

   private void closeLayoutMenu() {
      this.layoutMenuOpen = false;
   }

   private boolean isHoveringLayoutButton(float mx, float my, ClickGuiFrame frame) {
      if (this.selectedHud.isEmpty()) {
         return false;
      } else {
         boolean customFont = FontManager.isCustomFontEnabled();
         boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
         int minX = Integer.MAX_VALUE;
         int minY = Integer.MAX_VALUE;

         for(HudModule hm : this.selectedHud) {
            int bw = hm.getHudBoundW();
            int bh = hm.getHudBoundH();
            if (bw > 0 && bh > 0) {
               minX = Math.min(minX, hm.getHudBoundX());
               minY = Math.min(minY, hm.getHudBoundY());
            }
         }

         if (minX == Integer.MAX_VALUE) {
            return false;
         } else {
            String label = chinese ? "布局" : "Layout";
            float tw = customFont ? FontManager.ui.getWidth(label) : TextUtil.getWidth(label);
            float th = customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight();
            float pad = 4.0F;
            float bw = tw + pad * 2.0F;
            float bh = th + 4.0F;
            float bx = frame.unitMouseX(minX);
            float by = frame.unitMouseY(minY) - bh - 2.0F;
            float maxH = frame.scale == 0.0F ? (float)frame.screenH : ((float)frame.screenH - frame.slideY) / frame.scale;
            if (by < 1.0F) {
               by = 1.0F;
            }

            if (by + bh > maxH - 1.0F) {
               by = maxH - bh - 1.0F;
            }

            return mx >= bx && mx <= bx + bw && my >= by && my <= by + bh;
         }
      }
   }

   private String[] getLayoutMenuLabels(boolean chinese) {
      return chinese ? new String[]{"网格 (G)", "水平 (H)", "垂直 (V)"} : new String[]{"Grid (G)", "Horizontal (H)", "Vertical (V)"};
   }

   private boolean isLayoutMenuVisible() {
      if (!this.layoutMenuOpen && this.selectedHud.isEmpty()) {
         return false;
      } else {
         double p = this.layoutMenuAnim.get(this.layoutMenuOpen ? (double)1.0F : (double)0.0F, 160L, Easing.CubicInOut);
         return p > 0.01;
      }
   }

   private boolean handleLayoutMenuClick(int mouseX, int mouseY, int mouseButton, ClickGuiFrame frame) {
      double p = this.layoutMenuAnim.get(this.layoutMenuOpen ? (double)1.0F : (double)0.0F, 160L, Easing.CubicInOut);
      if (!(p <= 0.01) && !this.selectedHud.isEmpty()) {
         ClickGui gui = ClickGui.getInstance();
         if (gui == null) {
            this.closeLayoutMenu();
            return false;
         } else {
            boolean customFont = FontManager.isCustomFontEnabled();
            boolean shadow = FontManager.isShadowEnabled();
            boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
            String[] labels = this.getLayoutMenuLabels(chinese);
            float mx = frame.unitMouseX(mouseX);
            float my = frame.unitMouseY(mouseY);
            float x = frame.unitMouseX(this.layoutMenuX);
            float y = frame.unitMouseY(this.layoutMenuY);
            float textH = customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight();
            float rowH = textH + 6.0F;
            float pad = 6.0F;
            float maxW = 0.0F;

            for(String s : labels) {
               float w = customFont ? FontManager.ui.getWidth(s) : TextUtil.getWidth(s);
               if (w > maxW) {
                  maxW = w;
               }
            }

            float menuW = maxW + pad * 2.0F;
            float menuH = rowH * (float)labels.length + pad * 2.0F;
            float progress = (float)p;
            float menuScale = 0.85F + 0.15F * progress;
            float scaledW = menuW * menuScale;
            float scaledH = menuH * menuScale;
            boolean inside = mx >= x && mx <= x + scaledW && my >= y && my <= y + scaledH;
            if (!inside) {
               if (mouseButton == 0) {
                  this.closeLayoutMenu();
                  return false;
               } else {
                  this.closeLayoutMenu();
                  return true;
               }
            } else if (mouseButton != 0) {
               return true;
            } else {
               float u = (mx - x) / Math.max(1.0E-4F, menuScale);
               float v = (my - y) / Math.max(1.0E-4F, menuScale);
               int idx = (int)Math.floor((double)((v - pad) / rowH));
               if (idx >= 0 && idx < labels.length) {
                  if (idx == 0) {
                     this.applyGridLayout();
                  } else if (idx == 1) {
                     this.applyHorizontalLayout();
                  } else if (idx == 2) {
                     this.applyVerticalLayout();
                  }

                  this.closeLayoutMenu();
                  return true;
               } else {
                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   private void renderLayoutMenu(class_332 context, ClickGuiFrame frame, int mouseX, int mouseY) {
      double p = this.layoutMenuAnim.get(this.layoutMenuOpen ? (double)1.0F : (double)0.0F, 160L, Easing.CubicInOut);
      float progress = (float)p;
      if (!(progress <= 0.01F) && !this.selectedHud.isEmpty()) {
         ClickGui gui = ClickGui.getInstance();
         if (gui == null) {
            this.closeLayoutMenu();
         } else {
            boolean customFont = FontManager.isCustomFontEnabled();
            boolean shadow = FontManager.isShadowEnabled();
            boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
            String[] labels = this.getLayoutMenuLabels(chinese);
            float x = frame.unitMouseX(this.layoutMenuX);
            float y = frame.unitMouseY(this.layoutMenuY);
            float textH = customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight();
            float rowH = textH + 6.0F;
            float pad = 6.0F;
            float maxW = 0.0F;

            for(String s : labels) {
               float w = customFont ? FontManager.ui.getWidth(s) : TextUtil.getWidth(s);
               if (w > maxW) {
                  maxW = w;
               }
            }

            float menuW = maxW + pad * 2.0F;
            float menuH = rowH * (float)labels.length + pad * 2.0F;
            float maxWUnit = frame.scale == 0.0F ? (float)frame.screenW : (float)frame.screenW / frame.scale;
            float maxHUnit = frame.scale == 0.0F ? (float)frame.screenH : ((float)frame.screenH - frame.slideY) / frame.scale;
            if (x + menuW > maxWUnit - 2.0F) {
               x = maxWUnit - menuW - 2.0F;
            }

            if (y + menuH > maxHUnit - 2.0F) {
               y = maxHUnit - menuH - 2.0F;
            }

            if (x < 2.0F) {
               x = 2.0F;
            }

            if (y < 2.0F) {
               y = 2.0F;
            }

            float cx = x + menuW / 2.0F;
            float cy = y + menuH / 2.0F;
            float menuScale = 0.85F + 0.15F * progress;
            context.method_51448().method_22903();
            context.method_51448().method_46416(cx, cy, 0.0F);
            context.method_51448().method_22905(menuScale, menuScale, 1.0F);
            context.method_51448().method_46416(-cx, -cy, 0.0F);
            int bgA = Math.max(0, Math.min(255, (int)Math.round((double)245.0F * (double)progress)));
            int outA = Math.max(0, Math.min(255, (int)Math.round((double)210.0F * (double)progress)));
            Color bg = ColorUtil.injectAlpha(gui.defaultColor.getValue(), bgA);
            Color out = ColorUtil.injectAlpha(gui.hoverColor.getValue(), outA);
            Render2DUtil.drawRectWithOutline(context.method_51448(), x, y, menuW, menuH, bg, out);
            float mx = frame.unitMouseX(mouseX);
            float my = frame.unitMouseY(mouseY);
            float localMx = x + (mx - x) / Math.max(1.0E-4F, menuScale);
            float localMy = y + (my - y) / Math.max(1.0E-4F, menuScale);
            int textA = Math.max(0, Math.min(255, (int)Math.round((double)gui.enableTextColor.getValue().getAlpha() * (double)progress)));
            int textColor = ColorUtil.injectAlpha(gui.enableTextColor.getValue(), textA).getRGB();

            for(int i = 0; i < labels.length; ++i) {
               float ry = y + pad + (float)i * rowH;
               boolean hovered = localMx >= x && localMx <= x + menuW && localMy >= ry && localMy <= ry + rowH;
               if (hovered) {
                  Render2DUtil.drawRect(context.method_51448(), x + 1.0F, ry, menuW - 2.0F, rowH, ColorUtil.injectAlpha(gui.hoverColor.getValue(), Math.max(0, Math.min(255, (int)Math.round((double)160.0F * (double)progress)))));
               }

               TextUtil.drawString(context, labels[i], (double)(x + pad), (double)(ry + 2.0F), textColor, customFont, shadow);
            }

            context.method_51448().method_22909();
         }
      }
   }

   private void applyGridLayout() {
      if (!this.selectedHud.isEmpty()) {
         this.selectedHud.sort(Comparator.comparingInt(HudModule::getHudBoundY).thenComparingInt(HudModule::getHudBoundX));
         int minX = Integer.MAX_VALUE;
         int minY = Integer.MAX_VALUE;
         int maxW = 0;
         int maxH = 0;

         for(HudModule hm : this.selectedHud) {
            minX = Math.min(minX, hm.getHudBoundX());
            minY = Math.min(minY, hm.getHudBoundY());
            maxW = Math.max(maxW, Math.max(1, hm.getHudBoundW()));
            maxH = Math.max(maxH, Math.max(1, hm.getHudBoundH()));
         }

         int pad = 2;
         int cellW = maxW + pad;
         int cellH = maxH + pad;
         int n = this.selectedHud.size();
         int cols = (int)Math.ceil(Math.sqrt((double)n));
         cols = Math.max(1, cols);

         for(int i = 0; i < n; ++i) {
            int col = i % cols;
            int row = i / cols;
            int x = minX + col * cellW;
            int y = minY + row * cellH;
            ((HudModule)this.selectedHud.get(i)).setHudPosFromBounds(x, y);
         }

      }
   }

   private void applyHorizontalLayout() {
      if (!this.selectedHud.isEmpty()) {
         this.selectedHud.sort(Comparator.comparingInt(HudModule::getHudBoundX));
         int minY = Integer.MAX_VALUE;

         for(HudModule hm : this.selectedHud) {
            minY = Math.min(minY, hm.getHudBoundY());
         }

         int pad = 2;
         int x = ((HudModule)this.selectedHud.get(0)).getHudBoundX();

         for(HudModule hm : this.selectedHud) {
            hm.setHudPosFromBounds(x, minY);
            int w = Math.max(1, hm.getHudBoundW());
            x += w + pad;
         }

      }
   }

   private void applyVerticalLayout() {
      if (!this.selectedHud.isEmpty()) {
         this.selectedHud.sort(Comparator.comparingInt(HudModule::getHudBoundY));
         int minX = Integer.MAX_VALUE;

         for(HudModule hm : this.selectedHud) {
            minX = Math.min(minX, hm.getHudBoundX());
         }

         int pad = 2;
         int y = ((HudModule)this.selectedHud.get(0)).getHudBoundY();

         for(HudModule hm : this.selectedHud) {
            hm.setHudPosFromBounds(minX, y);
            int h = Math.max(1, hm.getHudBoundH());
            y += h + pad;
         }

      }
   }

   private void applyAlignLeft() {
      if (!this.selectedHud.isEmpty()) {
         int minX = Integer.MAX_VALUE;

         for(HudModule hm : this.selectedHud) {
            minX = Math.min(minX, hm.getHudBoundX());
         }

         for(HudModule hm : this.selectedHud) {
            hm.setHudPosFromBounds(minX, hm.getHudBoundY());
         }

      }
   }

   private void applyAlignTop() {
      if (!this.selectedHud.isEmpty()) {
         int minY = Integer.MAX_VALUE;

         for(HudModule hm : this.selectedHud) {
            minY = Math.min(minY, hm.getHudBoundY());
         }

         for(HudModule hm : this.selectedHud) {
            hm.setHudPosFromBounds(hm.getHudBoundX(), minY);
         }

      }
   }

   private static final class GroupDragEntry {
      private final HudModule module;
      private final int startX;
      private final int startY;

      private GroupDragEntry(HudModule module, int startX, int startY) {
         this.module = module;
         this.startX = startX;
         this.startY = startY;
      }
   }
}
