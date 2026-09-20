package dev.Astra.mod.gui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.AnimateUtil;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.core.impl.CommandManager;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.gui.clickgui.pages.ClickGuiConfigPage;
import dev.Astra.mod.gui.clickgui.pages.ClickGuiHudPage;
import dev.Astra.mod.gui.clickgui.pages.ClickGuiModulePage;
import dev.Astra.mod.gui.items.Component;
import dev.Astra.mod.gui.items.Item;
import dev.Astra.mod.gui.windows.WindowBase;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;

public class ClickGuiScreen extends class_437 {
   private static ClickGuiScreen INSTANCE = new ClickGuiScreen();
   private final ArrayList<Component> components = new ArrayList();
   private boolean layoutCorrected = false;
   private int lastLayoutScreenW = -1;
   private int lastLayoutScreenH = -1;
   private final ArrayList<TopTab> topTabs = new ArrayList();
   private Page page;
   private final Animation pageSlide;
   private final ClickGuiModulePage modulePage;
   private final ClickGuiConfigPage configPage;
   private final ClickGuiHudPage hudPage;
   private final ClickGuiFrame reusableFrame;
   private ClickGuiFrame lastFrame;
   private float topTabAnimX;
   private float topTabAnimW;
   private boolean topTabAnimInit;
   private boolean confirmOpen;
   private String confirmTitle;
   private String confirmMessage;
   private Runnable confirmYesAction;
   private int lastScreenW;
   private boolean lastChineseLang;
   private boolean boundsDirty;
   private int cachedPanelX;
   private int cachedPanelY;
   private int cachedPanelW;
   private int cachedPanelH;
   private int cachedAlpha;
   private boolean cachedFocused;
   private static final int COLOR_BLACK_140 = -1946157056;
   private final Map<String, Integer> textWidthCache;
   private boolean lastTextWidthCustomFont;
   private boolean lastTextWidthChinese;
   private static final String[] TIPS_EN = new String[]{"LMB drag, RMB expand/collapse", "Scroll up/down, SHIFT+scroll left/right", "SHIFT+LMB: toggle hold/release", "SHIFT+LMB: reset this setting", "RMB on String setting: edit"};
   private static final String[] TIPS_ZH = new String[]{"左键拖动列 右键展开/折叠", "滚轮是上下移动 SHIFT+滚轮是左右移动", "SHIFT+左键 快捷键按钮 切换功能(按住/松开)触发", "SHIFT+左键 功能按钮 重置设置", "文本设置 右键编辑"};

   public ClickGuiScreen() {
      super(class_2561.method_43470("Astra"));
      this.page = ClickGuiScreen.Page.Module;
      this.pageSlide = new Animation();
      this.modulePage = new ClickGuiModulePage(this);
      this.configPage = new ClickGuiConfigPage(this);
      this.hudPage = new ClickGuiHudPage(this);
      this.reusableFrame = new ClickGuiFrame();
      this.lastScreenW = -1;
      this.lastChineseLang = false;
      this.boundsDirty = true;
      this.textWidthCache = new HashMap();
      this.lastTextWidthCustomFont = true;
      this.lastTextWidthChinese = false;
      this.setInstance();
      this.load();
   }

   public static ClickGuiScreen getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new ClickGuiScreen();
      }

      return INSTANCE;
   }

   public Page getPage() {
      return this.page;
   }

   private void setInstance() {
      INSTANCE = this;
   }

   private void load() {
      this.topTabs.clear();
      this.topTabs.add(new TopTab(ClickGuiScreen.Page.Module, "Module", "模块"));
      this.topTabs.add(new TopTab(ClickGuiScreen.Page.Config, "Config", "配置"));
      this.topTabs.add(new TopTab(ClickGuiScreen.Page.Hud, "HUD", "HUD"));
      this.modulePage.load();
      this.hudPage.init();
      this.boundsDirty = true;
      this.invalidateTextWidthCache();
   }

   private void renderHudModules(class_332 context, float delta) {
      for(Module module : Astra.MODULE.getModules()) {
         if (module instanceof HudModule && module.isOn()) {
            try {
               module.onRender2D(context, delta);
            } catch (Exception e) {
               e.printStackTrace();
               if (ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.debug.getValue()) {
                  String var10000 = module.getName();
                  CommandManager.sendMessage("§4Error in " + var10000 + " [onRender2D]: " + e.getMessage());
               }
            }
         }
      }

   }

   private void invalidateBounds() {
      this.boundsDirty = true;
   }

   private void invalidateTextWidthCache() {
      this.textWidthCache.clear();
   }

   private void updateBoundsIfNeeded(int sw, int sh) {
      if (this.boundsDirty) {
         this.boundsDirty = false;
         int minX = Integer.MAX_VALUE;
         int minY = Integer.MAX_VALUE;
         int maxX = Integer.MIN_VALUE;
         int maxY = Integer.MIN_VALUE;

         for(Component c : this.components) {
            int x = c.getX();
            int y = c.getY();
            int w = c.getWidth();
            int h = c.getHeight();
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x + w);
            maxY = Math.max(maxY, y + h);
         }

         int margin = 16;
         int panelX = Math.max(8, minX - margin);
         int panelY = Math.max(6, minY - margin);
         int panelW = Math.min(sw - panelX - 8, maxX - minX + margin * 2);
         int panelH = Math.min(sh - panelY - 6, maxY - minY + margin * 2 + 24);
         this.cachedPanelX = panelX;
         this.cachedPanelY = panelY;
         this.cachedPanelW = panelW;
         this.cachedPanelH = panelH;
      }
   }

   private void updateFocusAndAlpha(int mouseX, int mouseY) {
      boolean focused = mouseX >= this.cachedPanelX && mouseX <= this.cachedPanelX + this.cachedPanelW && mouseY >= this.cachedPanelY && mouseY <= this.cachedPanelY + this.cachedPanelH;
      this.cachedFocused = focused;
      this.cachedAlpha = focused ? 242 : 227;
   }

   private int getTextWidthCached(String text, boolean customFont, boolean chinese) {
      if (customFont != this.lastTextWidthCustomFont || chinese != this.lastTextWidthChinese) {
         this.invalidateTextWidthCache();
         this.lastTextWidthCustomFont = customFont;
         this.lastTextWidthChinese = chinese;
      }

      return (Integer)this.textWidthCache.computeIfAbsent(text, (k) -> customFont && FontManager.isCustomFontEnabled() ? (int)FontManager.ui.getWidth(k) : Wrapper.mc != null ? Wrapper.mc.field_1772.method_1727(k) : 0);
   }

   private void updateTopTabsLayoutIfNeeded(int screenWidth, boolean chinese) {
      if (screenWidth != this.lastScreenW || chinese != this.lastChineseLang) {
         this.lastScreenW = screenWidth;
         this.lastChineseLang = chinese;
         int gap = 0;
         int padX = 8;
         int y = 6;
         int h = this.getFontHeight() + 6;
         int total = 0;

         for(int i = 0; i < this.topTabs.size(); ++i) {
            TopTab tab = (TopTab)this.topTabs.get(i);
            String label = tab.getLabel(chinese);
            int w = this.getTextWidthCached(label, FontManager.isCustomFontEnabled(), chinese) + padX * 2;
            tab.w = w;
            tab.h = h;
            tab.y = y;
            total += w;
            if (i != this.topTabs.size() - 1) {
               total += gap;
            }
         }

         int x = Math.round((float)(screenWidth - total) / 2.0F);

         for(TopTab tab : this.topTabs) {
            tab.x = x;
            x += tab.w + gap;
         }

      }
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      ClickGui gui = ClickGui.getInstance();
      ClientSetting clientSetting = ClientSetting.INSTANCE;
      if (gui != null) {
         float dt = AnimateUtil.deltaTime();
         if (dt <= 0.0F) {
            dt = 0.016F;
         }

         float keyCodec = (float)gui.alphaValue;
         float scale = 0.92F + 0.08F * keyCodec;
         float slideY = (1.0F - keyCodec) * 20.0F;
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, keyCodec);
         Item.context = context;
         this.method_25420(context, mouseX, mouseY, delta);
         if (this.page == ClickGuiScreen.Page.Hud) {
            this.renderHudModules(context, delta);
         }

         if (gui.colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
            gui.updateSpectrumLut(context.method_51443());
         }

         int sw = context.method_51421();
         int sh = context.method_51443();
         if (sw != this.lastLayoutScreenW || sh != this.lastLayoutScreenH) {
            this.layoutCorrected = false;
            this.lastLayoutScreenW = sw;
            this.lastLayoutScreenH = sh;
            this.hudPage.resetHudLayout();
            this.invalidateBounds();
         }

         if (!this.layoutCorrected && Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
            int categoryWidth = gui.categoryWidth.getValueInt();
            int moduleButtonWidth = gui.moduleButtonWidth.getValueInt();
            int layoutWidth = Math.max(categoryWidth, moduleButtonWidth);
            int spacing = layoutWidth + 1;
            int count = this.components.size();
            if (count > 0) {
               int totalWidth = count * layoutWidth + (count - 1);
               int startX = Math.round(((float)sw - (float)totalWidth) / 2.0F);
               int startY = Math.round((float)sh / 6.0F);
               int offsetX = Math.round(((float)layoutWidth - (float)moduleButtonWidth) / 2.0F);
               int x = startX - spacing;

               for(Component component : this.components) {
                  x += spacing;
                  component.setX(x + offsetX);
                  component.setY(startY);
               }

               this.invalidateBounds();
            }

            this.layoutCorrected = true;
         }

         this.updateBoundsIfNeeded(sw, sh);
         this.updateFocusAndAlpha(mouseX, mouseY);
         this.renderTopTabs(context, mouseX, mouseY, dt, gui, clientSetting);
         class_4587 matrices = context.method_51448();
         matrices.method_22903();
         float centerX = (float)this.cachedPanelX + (float)this.cachedPanelW / 2.0F;
         float centerY = (float)this.cachedPanelY + (float)this.cachedPanelH / 2.0F;
         matrices.method_46416(centerX, centerY + slideY, 0.0F);
         matrices.method_22905(scale, scale, 1.0F);
         matrices.method_46416(-centerX, -centerY, 0.0F);
         matrices.method_22909();
         int categoryWidth = gui.categoryWidth.getValueInt();
         int moduleButtonWidth = gui.moduleButtonWidth.getValueInt();
         int layoutWidth = Math.max(categoryWidth, moduleButtonWidth);
         int count = this.components.size();
         int totalWidth = count > 0 ? count * layoutWidth + (count - 1) : sw;
         int pageW = Math.max(sw, totalWidth + 32);
         float pageX = (float)this.pageSlide.get(-((double)this.page.ordinal()) * (double)pageW, 260L, Easing.SineOut);
         float pageOffsetX = scale == 0.0F ? pageX : pageX / scale;

         for(Component c : this.components) {
            c.setPageOffsetX(pageOffsetX);
         }

         this.reusableFrame.set(scale, slideY, pageOffsetX, pageW, this.cachedPanelX, this.cachedPanelY, this.cachedPanelW, this.cachedPanelH, 0.0F, 0.0F, sw, sh);
         this.lastFrame = this.reusableFrame;
         matrices.method_22903();
         matrices.method_46416(0.0F, slideY, 0.0F);
         matrices.method_22905(scale, scale, 1.0F);
         this.modulePage.render(context, mouseX, mouseY, delta);
         this.configPage.render(context, mouseX, mouseY, delta, this.reusableFrame);
         this.hudPage.render(context, mouseX, mouseY, delta, this.reusableFrame);
         matrices.method_22909();
         if (gui.tips.getValue()) {
            this.renderTips(context, gui, clientSetting);
         }

         if (this.confirmOpen) {
            this.renderConfirmDialog(context, mouseX, mouseY, gui, clientSetting);
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   private void renderTips(class_332 ctx, ClickGui gui, ClientSetting cs) {
      class_4587 matrices = ctx.method_51448();
      matrices.method_22903();
      float pageX = (float)this.pageSlide.get(-((double)this.page.ordinal()) * (double)Math.max(ctx.method_51421(), 0), 260L, Easing.SineOut);
      matrices.method_46416(pageX, 0.0F, 0.0F);
      boolean customFont = FontManager.isCustomFontEnabled();
      boolean shadow = FontManager.isShadowEnabled();
      float lineHeight = customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight();
      float marginBottom = 6.0F;
      int lines = 5;
      float baseY = (float)ctx.method_51443() - marginBottom - lineHeight * (float)lines;
      int tipX = 6;
      int tipY = Math.round(baseY);
      boolean chinese = cs != null && cs.chinese.getValue();
      String[] tips = chinese ? TIPS_ZH : TIPS_EN;
      boolean spectrum = gui.colorMode.getValue() == ClickGui.ColorMode.Spectrum;

      for(int i = 0; i < tips.length; ++i) {
         double delay = spectrum ? (double)((float)tipY + (float)i * lineHeight) * (double)0.25F : (double)((float)tipY + (float)i * lineHeight) / (double)10.0F;
         int color = gui.getActiveColorRGB(delay);
         color = injectAlpha(color, this.cachedAlpha);
         TextUtil.drawString(ctx, tips[i], (double)tipX, (double)(tipY + (int)((float)i * lineHeight)), color, customFont, shadow);
      }

      matrices.method_22909();
   }

   private void renderTopTabs(class_332 ctx, int mx, int my, float dt, ClickGui gui, ClientSetting cs) {
      if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         boolean chinese = cs != null && cs.chinese.getValue();
         int sw = Wrapper.mc.method_22683().method_4486();
         this.updateTopTabsLayoutIfNeeded(sw, chinese);
         boolean customFont = FontManager.isCustomFontEnabled();
         boolean shadow = FontManager.isShadowEnabled();
         TopTab activeTab = null;

         for(TopTab tab : this.topTabs) {
            if (this.page == tab.page) {
               activeTab = tab;
               break;
            }
         }

         if (activeTab != null) {
            float targetX = (float)activeTab.x;
            float targetW = (float)activeTab.w;
            if (!this.topTabAnimInit) {
               this.topTabAnimX = targetX;
               this.topTabAnimW = targetW;
               this.topTabAnimInit = true;
            } else {
               float a = dt * 18.0F;
               if (a > 0.35F) {
                  a = 0.35F;
               }

               this.topTabAnimX += (targetX - this.topTabAnimX) * a;
               this.topTabAnimW += (targetW - this.topTabAnimW) * a;
            }

            int defaultBg = gui.defaultColor.getValue().getRGB();
            int hoverBg = gui.hoverColor.getValue().getRGB();

            for(TopTab tab : this.topTabs) {
               boolean hover = mx >= tab.x && mx <= tab.x + tab.w && my >= tab.y && my <= tab.y + tab.h;
               int bg = hover ? hoverBg : defaultBg;
               Render2DUtil.rect(ctx.method_51448(), (float)tab.x, (float)tab.y, (float)(tab.x + tab.w), (float)(tab.y + tab.h) - 0.5F, bg);
            }

            boolean hoverActive = mx >= activeTab.x && mx <= activeTab.x + activeTab.w && my >= activeTab.y && my <= activeTab.y + activeTab.h;
            int activeAlpha = hoverActive ? gui.hoverAlpha.getValueInt() : (int)(gui.alphaValue * (double)255.0F);
            if (gui.colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
               Render2DUtil.drawLutRect(ctx.method_51448(), this.topTabAnimX, (float)activeTab.y, this.topTabAnimW, (float)activeTab.h - 0.5F, gui.getSpectrumLutId(), gui.getSpectrumLutHeight(), activeAlpha);
            } else {
               int activeColor = gui.getActiveColorRGB((double)activeTab.y * (double)0.25F);
               activeColor = injectAlpha(activeColor, activeAlpha);
               Render2DUtil.rect(ctx.method_51448(), this.topTabAnimX, (float)activeTab.y, this.topTabAnimX + this.topTabAnimW, (float)(activeTab.y + activeTab.h) - 0.5F, activeColor);
            }

            int enableTextColor = gui.enableTextColor.getValue().getRGB();
            int defaultTextColor = gui.defaultTextColor.getValue().getRGB();
            int padX = 8;

            for(TopTab tab : this.topTabs) {
               boolean hover = mx >= tab.x && mx <= tab.x + tab.w && my >= tab.y && my <= tab.y + tab.h;
               boolean active = this.page == tab.page;
               int textColor = !active && !hover ? defaultTextColor : enableTextColor;
               float textY = this.getCenteredTextY((float)tab.y, (float)tab.h - 0.5F);
               TextUtil.drawString(ctx, tab.getLabel(chinese), (double)(tab.x + padX), (double)textY, textColor, customFont, shadow);
            }

         }
      }
   }

   private boolean handleTopTabClick(int mx, int my) {
      if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
         int sw = Wrapper.mc.method_22683().method_4486();
         this.updateTopTabsLayoutIfNeeded(sw, chinese);

         for(TopTab tab : this.topTabs) {
            if (mx >= tab.x && mx <= tab.x + tab.w && my >= tab.y && my <= tab.y + tab.h) {
               this.setPage(tab.page);
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void renderConfirmDialog(class_332 ctx, int mx, int my, ClickGui gui, ClientSetting cs) {
      boolean chn = cs != null && cs.chinese.getValue();
      boolean customFont = FontManager.isCustomFontEnabled();
      boolean shadow = FontManager.isShadowEnabled();
      int sw = ctx.method_51421();
      int sh = ctx.method_51443();
      Render2DUtil.rect(ctx.method_51448(), 0.0F, 0.0F, (float)sw, (float)sh, -1946157056);
      float lineH = customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight();
      float pad = 10.0F;
      float boxW = (float)Math.min(340, sw - 40);
      float btnH = lineH + 6.0F;
      float boxH = pad + lineH + 6.0F + lineH + 12.0F + btnH + pad;
      float x = ((float)sw - boxW) / 2.0F;
      float y = ((float)sh - boxH) / 2.0F;
      int bgColor = gui.defaultColor.getValue().getRGB();
      Render2DUtil.rect(ctx.method_51448(), x, y, x + boxW, y + boxH, bgColor);
      String title = this.confirmTitle != null && !this.confirmTitle.isEmpty() ? this.confirmTitle : (chn ? "确认" : "Confirm");
      String msg = this.confirmMessage == null ? "" : this.confirmMessage;
      float titleY = y + pad;
      float msgY = titleY + lineH + 6.0F;
      int titleColor = gui.enableTextColor.getValue().getRGB();
      int msgColor = gui.defaultTextColor.getValue().getRGB();
      float titleX = x + (boxW - (float)this.getTextWidthCached(title, customFont, chn)) / 2.0F;
      float msgX = x + (boxW - (float)this.getTextWidthCached(msg, customFont, chn)) / 2.0F;
      TextUtil.drawString(ctx, title, (double)titleX, (double)titleY, titleColor, customFont, shadow);
      TextUtil.drawString(ctx, msg, (double)msgX, (double)msgY, msgColor, customFont, shadow);
      float gap = 8.0F;
      float btnW = (boxW - pad * 2.0F - gap) / 2.0F;
      float btnY = y + boxH - pad - btnH;
      float yesX = x + pad;
      float noX = yesX + btnW + gap;
      boolean hYes = (float)mx >= yesX && (float)mx <= yesX + btnW && (float)my >= btnY && (float)my <= btnY + btnH;
      boolean hNo = (float)mx >= noX && (float)mx <= noX + btnW && (float)my >= btnY && (float)my <= btnY + btnH;
      int activeAlpha = hYes ? gui.hoverAlpha.getValueInt() : (int)(gui.alphaValue * (double)255.0F);
      if (gui.colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
         Render2DUtil.drawLutRect(ctx.method_51448(), yesX, btnY, btnW, btnH, gui.getSpectrumLutId(), gui.getSpectrumLutHeight(), activeAlpha);
      } else {
         int activeColor = gui.getActiveColorRGB((double)btnY * (double)0.25F);
         activeColor = injectAlpha(activeColor, activeAlpha);
         Render2DUtil.rect(ctx.method_51448(), yesX, btnY, yesX + btnW, btnY + btnH, activeColor);
      }

      int bgNo = hNo ? gui.hoverColor.getValue().getRGB() : gui.defaultColor.getValue().getRGB();
      Render2DUtil.rect(ctx.method_51448(), noX, btnY, noX + btnW, btnY + btnH, bgNo);
      String yes = chn ? "确认" : "Yes";
      String no = chn ? "取消" : "No";
      float yesTx = yesX + (btnW - (float)this.getTextWidthCached(yes, customFont, chn)) / 2.0F;
      float noTx = noX + (btnW - (float)this.getTextWidthCached(no, customFont, chn)) / 2.0F;
      float btnTy = this.getCenteredTextY(btnY, btnH);
      TextUtil.drawString(ctx, yes, (double)yesTx, (double)btnTy, titleColor, customFont, shadow);
      TextUtil.drawString(ctx, no, (double)noTx, (double)btnTy, titleColor, customFont, shadow);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (this.confirmOpen) {
         return this.handleConfirmClick((int)mouseX, (int)mouseY, button);
      } else if (button == 0 && this.handleTopTabClick((int)mouseX, (int)mouseY)) {
         return true;
      } else if (this.page == ClickGuiScreen.Page.Module) {
         this.modulePage.mouseClicked((int)mouseX, (int)mouseY, button);
         return super.method_25402(mouseX, mouseY, button);
      } else {
         ClickGuiFrame frame = this.lastFrame;
         if (frame == null) {
            return super.method_25402(mouseX, mouseY, button);
         } else if (this.page == ClickGuiScreen.Page.Config && this.configPage.mouseClicked((int)mouseX, (int)mouseY, button, frame)) {
            return true;
         } else {
            return this.page == ClickGuiScreen.Page.Hud && this.hudPage.mouseClicked((int)mouseX, (int)mouseY, button, frame) ? true : super.method_25402(mouseX, mouseY, button);
         }
      }
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      if (this.page == ClickGuiScreen.Page.Module) {
         this.modulePage.mouseReleased((int)mouseX, (int)mouseY, button);
      } else if (this.page == ClickGuiScreen.Page.Hud) {
         ClickGuiFrame frame = this.lastFrame;
         int mx = frame != null ? (int)frame.unitMouseX((int)mouseX) : (int)mouseX;
         int my = frame != null ? (int)frame.unitMouseY((int)mouseY) : (int)mouseY;
         this.hudPage.mouseReleased(mx, my, button);
      }

      this.invalidateBounds();
      return super.method_25406(mouseX, mouseY, button);
   }

   public boolean method_25401(double mx, double my, double horiz, double vert) {
      if (this.page == ClickGuiScreen.Page.Module) {
         this.modulePage.mouseScrolled(vert);
      } else if (this.page == ClickGuiScreen.Page.Config) {
         if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
            this.configPage.mouseScrolled(vert, Wrapper.mc.method_22683().method_4502());
         }
      } else if (this.page == ClickGuiScreen.Page.Hud) {
         this.hudPage.mouseScrolled(vert);
      }

      return super.method_25401(mx, my, horiz, vert);
   }

   public boolean method_25404(int keyCode, int scanCode, int mods) {
      if (this.confirmOpen) {
         if (keyCode == 256) {
            this.closeConfirm();
            return true;
         } else if (keyCode != 257 && keyCode != 335) {
            return true;
         } else {
            this.confirmYes();
            return true;
         }
      } else if (this.page == ClickGuiScreen.Page.Module) {
         this.modulePage.keyPressed(keyCode);
         return super.method_25404(keyCode, scanCode, mods);
      } else if (this.page == ClickGuiScreen.Page.Hud) {
         this.hudPage.keyPressed(keyCode);
         return super.method_25404(keyCode, scanCode, mods);
      } else {
         return this.page == ClickGuiScreen.Page.Config && this.configPage.keyPressed(keyCode) ? true : super.method_25404(keyCode, scanCode, mods);
      }
   }

   public boolean method_25400(char chr, int mods) {
      if (this.page == ClickGuiScreen.Page.Module) {
         this.modulePage.charTyped(chr, mods);
         return super.method_25400(chr, mods);
      } else if (this.page == ClickGuiScreen.Page.Hud) {
         this.hudPage.charTyped(chr, mods);
         return super.method_25400(chr, mods);
      } else {
         return this.page == ClickGuiScreen.Page.Config && this.configPage.charTyped(chr) ? true : super.method_25400(chr, mods);
      }
   }

   public void openConfirm(String title, String msg, Runnable yesAction) {
      this.confirmOpen = true;
      this.confirmTitle = title;
      this.confirmMessage = msg;
      this.confirmYesAction = yesAction;
      this.configPage.stopNameListening();
   }

   private void closeConfirm() {
      this.confirmOpen = false;
      this.confirmTitle = this.confirmMessage = null;
      this.confirmYesAction = null;
   }

   private void confirmYes() {
      if (this.confirmYesAction != null) {
         this.confirmYesAction.run();
      }

      this.closeConfirm();
   }

   private boolean handleConfirmClick(int mx, int my, int btn) {
      if (btn != 0) {
         this.closeConfirm();
         return true;
      } else {
         ClickGui gui = ClickGui.getInstance();
         if (gui == null) {
            this.closeConfirm();
            return true;
         } else {
            int sw = Item.context != null ? Item.context.method_51421() : (Wrapper.mc != null && Wrapper.mc.method_22683() != null ? Wrapper.mc.method_22683().method_4486() : 0);
            int sh = Item.context != null ? Item.context.method_51443() : (Wrapper.mc != null && Wrapper.mc.method_22683() != null ? Wrapper.mc.method_22683().method_4502() : 0);
            if (sw > 0 && sh > 0) {
               boolean customFont = FontManager.isCustomFontEnabled();
               float lineH = customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight();
               float pad = 10.0F;
               float boxW = (float)Math.min(340, sw - 40);
               float btnH = lineH + 6.0F;
               float boxH = pad + lineH + 6.0F + lineH + 12.0F + btnH + pad;
               float x = ((float)sw - boxW) / 2.0F;
               float y = ((float)sh - boxH) / 2.0F;
               float gap = 8.0F;
               float btnW = (boxW - pad * 2.0F - gap) / 2.0F;
               float btnY = y + boxH - pad - btnH;
               float yesX = x + pad;
               float noX = yesX + btnW + gap;
               boolean inYes = (float)mx >= yesX && (float)mx <= yesX + btnW && (float)my >= btnY && (float)my <= btnY + btnH;
               boolean inNo = (float)mx >= noX && (float)mx <= noX + btnW && (float)my >= btnY && (float)my <= btnY + btnH;
               if (inYes) {
                  this.confirmYes();
               } else if (inNo) {
                  this.closeConfirm();
               } else {
                  this.closeConfirm();
               }

               return true;
            } else {
               this.closeConfirm();
               return true;
            }
         }
      }
   }

   public boolean method_25421() {
      return false;
   }

   public final ArrayList<Component> getComponents() {
      return this.components;
   }

   public int getTextOffset() {
      return -ClickGui.getInstance().textOffset.getValueInt() - 6;
   }

   private void setPage(Page page) {
      this.setPage(page, true);
   }

   private void setPage(Page page, boolean resetHud) {
      if (page != null) {
         this.page = page;

         for(Component c : this.components) {
            c.drag = false;
         }

         this.configPage.stopNameListening();
         if (page == ClickGuiScreen.Page.Config) {
            this.configPage.onOpen();
         }

         if (page == ClickGuiScreen.Page.Hud && resetHud) {
            this.hudPage.resetHudLayout();
         }

         this.invalidateBounds();
      }
   }

   public void openHudWindow(WindowBase window) {
      this.hudPage.openHudWindow(window);
      this.setPage(ClickGuiScreen.Page.Hud, false);
   }

   public int getFontHeight() {
      return FontManager.isCustomFontEnabled() ? (int)FontManager.ui.getFontHeight() : 9;
   }

   public int getTextWidth(String s) {
      boolean customFont = FontManager.isCustomFontEnabled();
      boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
      return this.getTextWidthCached(s, customFont, chinese);
   }

   public float getCenteredTextY(float baseY, float boxHeight) {
      return baseY + (boxHeight - (float)this.getFontHeight()) / 2.0F + (float)ClickGui.getInstance().textOffset.getValueInt();
   }

   private static int injectAlpha(int color, int alpha) {
      return color & 16777215 | alpha << 24;
   }

   public static enum Page {
      Module,
      Config,
      Hud,
      AiAssistant;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{Module, Config, Hud, AiAssistant};
      }
   }

   private static final class TopTab {
      private final Page page;
      private final String labelEn;
      private final String labelZh;
      private int x;
      private int y;
      private int w;
      private int h;

      TopTab(Page page, String en, String zh) {
         this.page = page;
         this.labelEn = en;
         this.labelZh = zh;
      }

      String getLabel(boolean chinese) {
         return chinese && this.labelZh != null ? this.labelZh : this.labelEn;
      }
   }
}
