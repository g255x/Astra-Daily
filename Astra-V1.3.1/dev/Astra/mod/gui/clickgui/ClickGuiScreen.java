package dev.Astra.mod.gui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.AnimateUtil;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.ColorUtil;
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
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;

public class ClickGuiScreen extends class_437 {
   private static ClickGuiScreen INSTANCE = new ClickGuiScreen();
   private final ArrayList<Component> components = new ArrayList();
   private float mouseMoveOffsetX;
   private float mouseMoveOffsetY;
   private float walkShakeOffsetX;
   private float walkShakeOffsetY;
   private float walkShakeTime;
   private boolean layoutCorrected = false;
   private int lastLayoutScreenW = -1;
   private int lastLayoutScreenH = -1;
   private final Random snowRandom = new Random();
   private final ArrayList<Snowflake> snowflakes = new ArrayList();
   private final ArrayList<TopTab> topTabs = new ArrayList();
   private Page page;
   private final Animation pageSlide;
   private final ClickGuiModulePage modulePage;
   private final ClickGuiConfigPage configPage;
   private final ClickGuiHudPage hudPage;
   private ClickGuiFrame lastFrame;
   private float topTabAnimX;
   private float topTabAnimW;
   private boolean topTabAnimInit;
   private boolean confirmOpen;
   private String confirmTitle;
   private String confirmMessage;
   private Runnable confirmYesAction;

   public ClickGuiScreen() {
      super(class_2561.method_43470("Astra"));
      this.page = ClickGuiScreen.Page.Module;
      this.pageSlide = new Animation();
      this.modulePage = new ClickGuiModulePage(this);
      this.configPage = new ClickGuiConfigPage(this);
      this.hudPage = new ClickGuiHudPage(this);
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
                  CommandManager.sendMessage("§4An error has occurred (" + var10000 + " [onRender2D]) Message: [" + e.getMessage() + "]");
               }
            }
         }
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      float keyCodec = (float)ClickGui.getInstance().alphaValue;
      float scale = 0.92F + 0.08F * keyCodec;
      float slideY = (1.0F - keyCodec) * 20.0F;
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, keyCodec);
      Item.context = context;
      this.method_25420(context, mouseX, mouseY, delta);
      if (this.page == ClickGuiScreen.Page.Hud) {
         this.renderHudModules(context, delta);
      }

      ClickGui clickGui = ClickGui.getInstance();
      if (clickGui != null && clickGui.colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
         clickGui.updateSpectrumLut(context.method_51443());
      }

      if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         int sw = Wrapper.mc.method_22683().method_4486();
         int sh = Wrapper.mc.method_22683().method_4502();
         if (sw != this.lastLayoutScreenW || sh != this.lastLayoutScreenH) {
            this.layoutCorrected = false;
            this.lastLayoutScreenW = sw;
            this.lastLayoutScreenH = sh;
            this.hudPage.resetHudLayout();
         }
      }

      if (!this.layoutCorrected && Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         int categoryWidth = ClickGui.getInstance() != null ? ClickGui.getInstance().categoryWidth.getValueInt() : 101;
         int moduleButtonWidth = ClickGui.getInstance() != null ? ClickGui.getInstance().moduleButtonWidth.getValueInt() : 93;
         int layoutWidth = Math.max(categoryWidth, moduleButtonWidth);
         int spacing = layoutWidth + 1;
         int count = this.components.size();
         if (count > 0) {
            int screenWidth = Wrapper.mc.method_22683().method_4486();
            int screenHeight = Wrapper.mc.method_22683().method_4502();
            int totalWidth = count * layoutWidth + (count - 1);
            int startX = Math.round(((float)screenWidth - (float)totalWidth) / 2.0F);
            int startY = Math.round((float)screenHeight / 6.0F);
            int offsetX = Math.round(((float)layoutWidth - (float)moduleButtonWidth) / 2.0F);
            int x = startX - spacing;

            for(Component component : this.components) {
               x += spacing;
               component.setX(x + offsetX);
               component.setY(startY);
            }
         }

         this.layoutCorrected = true;
      }

      boolean dragging = false;

      for(Component c : this.components) {
         if (c.drag) {
            dragging = true;
            break;
         }
      }

      float targetOffsetX = 0.0F;
      float targetOffsetY = 0.0F;
      if (ClickGui.getInstance().mouseMove.getValue() && !dragging) {
         float strength = ClickGui.getInstance().mouseMoveStrength.getValueFloat() * (float)ClickGui.getInstance().alphaValue;
         float cx = (float)context.method_51421() / 2.0F;
         float cy = (float)context.method_51443() / 2.0F;
         float nx = cx <= 0.0F ? 0.0F : ((float)mouseX - cx) / cx;
         float ny = cy <= 0.0F ? 0.0F : ((float)mouseY - cy) / cy;
         nx = Math.max(-1.0F, Math.min(1.0F, nx));
         ny = Math.max(-1.0F, Math.min(1.0F, ny));
         targetOffsetX = nx * strength;
         targetOffsetY = ny * strength;
      }

      float smooth = ClickGui.getInstance().mouseMoveSmooth.getValueFloat();
      if (smooth <= 0.0F) {
         this.mouseMoveOffsetX = targetOffsetX;
         this.mouseMoveOffsetY = targetOffsetY;
      } else {
         float a = AnimateUtil.deltaTime() * smooth;
         if (a < 0.0F) {
            a = 0.0F;
         }

         if (a > 0.35F) {
            a = 0.35F;
         }

         this.mouseMoveOffsetX += (targetOffsetX - this.mouseMoveOffsetX) * a;
         this.mouseMoveOffsetY += (targetOffsetY - this.mouseMoveOffsetY) * a;
      }

      float targetWalkX = 0.0F;
      float targetWalkY = 0.0F;
      float maxWalk = 0.0F;
      if (ClickGui.getInstance().walkShake.getValue() && !dragging && Wrapper.mc.field_1724 != null) {
         class_243 v = Wrapper.mc.field_1724.method_18798();
         float horizontalSpeed = (float)Math.sqrt(v.field_1352 * v.field_1352 + v.field_1350 * v.field_1350);
         float moving = horizontalSpeed > 0.003F ? Math.min(1.0F, horizontalSpeed * 18.0F) : 0.0F;
         float dt = AnimateUtil.deltaTime();
         if (moving > 0.0F) {
            float speed = ClickGui.getInstance().walkShakeSpeed.getValueFloat();
            this.walkShakeTime += dt * speed * (0.4F + 0.6F * moving);
         }

         float strength = ClickGui.getInstance().walkShakeStrength.getValueFloat() * moving * (float)ClickGui.getInstance().alphaValue;
         maxWalk = ClickGui.getInstance().walkShakeMax.getValueFloat() * (float)ClickGui.getInstance().alphaValue;
         targetWalkX = (float)Math.sin((double)this.walkShakeTime) * strength;
         targetWalkY = (float)Math.cos((double)(this.walkShakeTime * 2.0F)) * strength * 0.35F;
         targetWalkX = Math.max(-maxWalk, Math.min(maxWalk, targetWalkX));
         targetWalkY = Math.max(-maxWalk, Math.min(maxWalk, targetWalkY));
      }

      float walkSmooth = ClickGui.getInstance().walkShakeSmooth.getValueFloat();
      if (walkSmooth <= 0.0F) {
         this.walkShakeOffsetX = targetWalkX;
         this.walkShakeOffsetY = targetWalkY;
      } else {
         float a = AnimateUtil.deltaTime() * walkSmooth;
         if (a < 0.0F) {
            a = 0.0F;
         }

         if (a > 0.35F) {
            a = 0.35F;
         }

         this.walkShakeOffsetX += (targetWalkX - this.walkShakeOffsetX) * a;
         this.walkShakeOffsetY += (targetWalkY - this.walkShakeOffsetY) * a;
      }

      if (maxWalk > 0.0F) {
         this.walkShakeOffsetX = Math.max(-maxWalk, Math.min(maxWalk, this.walkShakeOffsetX));
         this.walkShakeOffsetY = Math.max(-maxWalk, Math.min(maxWalk, this.walkShakeOffsetY));
      }

      float totalOffsetX = this.mouseMoveOffsetX + this.walkShakeOffsetX;
      float totalOffsetY = this.mouseMoveOffsetY + this.walkShakeOffsetY;
      this.components.forEach((cxx) -> cxx.setMouseMoveOffset(totalOffsetX, totalOffsetY));
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;

      for(Component c : this.components) {
         minX = Math.min(minX, c.getX());
         minY = Math.min(minY, c.getY());
         maxX = Math.max(maxX, c.getX() + c.getWidth());
         maxY = Math.max(maxY, c.getY() + c.getHeight());
      }

      int margin = 16;
      int panelX = Math.max(8, minX - margin);
      int panelY = Math.max(6, minY - margin);
      int panelW = Math.min(context.method_51421() - panelX - 8, maxX - minX + margin * 2);
      int panelH = Math.min(context.method_51443() - panelY - 6, maxY - minY + margin * 2 + 24);
      boolean focused = mouseX >= panelX && mouseX <= panelX + panelW && mouseY >= panelY && mouseY <= panelY + panelH;
      int alpha = focused ? (int)Math.round((double)242.25F) : (int)Math.round(226.95000000000002);
      if (ClickGui.getInstance().blur.getValue() && this.page != ClickGuiScreen.Page.Hud) {
         float blurRadius = 1.0F + (ClickGui.getInstance().radius.getValueFloat() - 1.0F) * (float)ClickGui.getInstance().alphaValue;
         Astra.BLUR.applyBlur(blurRadius, 0.0F, 0.0F, (float)context.method_51421(), (float)context.method_51443(), (float)((ClickGui.BlurType)ClickGui.getInstance().blurType.getValue()).ordinal());
      }

      this.renderSnow(context);
      this.renderTopTabs(context, mouseX, mouseY);
      context.method_51448().method_22903();
      context.method_51448().method_46416((float)panelX + (float)panelW / 2.0F, (float)panelY + (float)panelH / 2.0F + slideY, 0.0F);
      context.method_51448().method_22905(scale, scale, 1.0F);
      context.method_51448().method_46416(-((float)panelX + (float)panelW / 2.0F), -((float)panelY + (float)panelH / 2.0F), 0.0F);
      int strokeA = Math.max(0, Math.min(255, (int)Math.round((double)alpha * 0.22)));
      context.method_51448().method_22909();
      int screenW = context.method_51421();
      int categoryWidth = ClickGui.getInstance() != null ? ClickGui.getInstance().categoryWidth.getValueInt() : 101;
      int moduleButtonWidth = ClickGui.getInstance() != null ? ClickGui.getInstance().moduleButtonWidth.getValueInt() : 93;
      int layoutWidth = Math.max(categoryWidth, moduleButtonWidth);
      int count = this.components.size();
      int totalWidth = count > 0 ? count * layoutWidth + (count - 1) : screenW;
      int pageW = Math.max(screenW, totalWidth + 32);
      float pageX = (float)this.pageSlide.get(-((double)this.page.ordinal() * (double)pageW), 260L, Easing.SineOut);
      float pageOffsetX = scale == 0.0F ? pageX : pageX / scale;
      this.components.forEach((cxx) -> cxx.setPageOffsetX(pageOffsetX));
      ClickGuiFrame frame = new ClickGuiFrame(scale, slideY, pageOffsetX, pageW, panelX, panelY, panelW, panelH, totalOffsetX, totalOffsetY, context.method_51421(), context.method_51443());
      this.lastFrame = frame;
      context.method_51448().method_22903();
      context.method_51448().method_46416(0.0F, slideY, 0.0F);
      context.method_51448().method_22905(scale, scale, 1.0F);
      this.modulePage.render(context, mouseX, mouseY, delta);
      this.configPage.render(context, mouseX, mouseY, delta, frame);
      this.hudPage.render(context, mouseX, mouseY, delta, frame);
      context.method_51448().method_22909();
      ClickGui gui = ClickGui.getInstance();
      if (gui != null && gui.tips.getValue()) {
         context.method_51448().method_22903();
         context.method_51448().method_46416(pageX, 0.0F, 0.0F);
         boolean customFont = FontManager.isCustomFontEnabled();
         boolean shadow = FontManager.isShadowEnabled();
         float lineHeight = customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight();
         float marginBottom = 6.0F;
         int lines = 5;
         float baseY = (float)context.method_51443() - marginBottom - lineHeight * (float)lines;
         int tipX = 6;
         int tipY = Math.round(baseY);
         boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
         String tip1 = chinese ? "左键拖动列 右键展开/折叠" : "LMB drag, RMB expand/collapse";
         String tip2 = chinese ? "滚轮是上下移动 SHIFT+滚轮是左右移动" : "Scroll up/down, SHIFT+scroll left/right";
         String tip3 = chinese ? "SHIFT+左键 快捷键按钮 切换功能(按住/松开)触发" : "SHIFT+LMB: toggle hold/release";
         String tip4 = chinese ? "SHIFT+左键 功能按钮 重置设置" : "SHIFT+LMB: reset this setting";
         String tip5 = chinese ? "文本设置 右键编辑" : "RMB on String setting: edit";
         boolean spectrumTips = gui.colorMode.getValue() == ClickGui.ColorMode.Spectrum;
         double delay1 = spectrumTips ? (double)tipY * (double)0.25F : (double)tipY / (double)10.0F;
         Color c1 = gui.getActiveColor(delay1);
         int color1 = ColorUtil.injectAlpha(c1, alpha).getRGB();
         TextUtil.drawString(context, tip1, (double)tipX, (double)tipY, color1, customFont, shadow);
         int tipY2 = (int)((float)tipY + lineHeight);
         double delay2 = spectrumTips ? (double)tipY2 * (double)0.25F : (double)tipY2 / (double)10.0F;
         Color c2 = gui.getActiveColor(delay2);
         int color2 = ColorUtil.injectAlpha(c2, alpha).getRGB();
         TextUtil.drawString(context, tip2, (double)tipX, (double)tipY2, color2, customFont, shadow);
         int tipY3 = (int)((float)tipY + lineHeight * 2.0F);
         double delay3 = spectrumTips ? (double)tipY3 * (double)0.25F : (double)tipY3 / (double)10.0F;
         Color c3 = gui.getActiveColor(delay3);
         int color3 = ColorUtil.injectAlpha(c3, alpha).getRGB();
         TextUtil.drawString(context, tip3, (double)tipX, (double)tipY3, color3, customFont, shadow);
         int tipY4 = (int)((float)tipY + lineHeight * 3.0F);
         double delay4 = spectrumTips ? (double)tipY4 * (double)0.25F : (double)tipY4 / (double)10.0F;
         Color c4 = gui.getActiveColor(delay4);
         int color4 = ColorUtil.injectAlpha(c4, alpha).getRGB();
         TextUtil.drawString(context, tip4, (double)tipX, (double)tipY4, color4, customFont, shadow);
         int tipY5 = (int)((float)tipY + lineHeight * 4.0F);
         double delay5 = spectrumTips ? (double)tipY5 * (double)0.25F : (double)tipY5 / (double)10.0F;
         Color c5 = gui.getActiveColor(delay5);
         int color5 = ColorUtil.injectAlpha(c5, alpha).getRGB();
         TextUtil.drawString(context, tip5, (double)tipX, (double)tipY5, color5, customFont, shadow);
         context.method_51448().method_22909();
      }

      if (this.confirmOpen) {
         this.renderConfirmDialog(context, mouseX, mouseY);
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean method_25402(double mouseX, double mouseY, int clickedButton) {
      if (this.confirmOpen) {
         return this.handleConfirmClick((int)mouseX, (int)mouseY, clickedButton);
      } else if (clickedButton == 0 && this.handleTopTabClick((int)mouseX, (int)mouseY)) {
         return true;
      } else if (this.page == ClickGuiScreen.Page.Module) {
         this.modulePage.mouseClicked((int)mouseX, (int)mouseY, clickedButton);
         return super.method_25402(mouseX, mouseY, clickedButton);
      } else {
         ClickGuiFrame frame = this.lastFrame;
         if (frame == null) {
            return super.method_25402(mouseX, mouseY, clickedButton);
         } else if (this.page == ClickGuiScreen.Page.Config && this.configPage.mouseClicked((int)mouseX, (int)mouseY, clickedButton, frame)) {
            return true;
         } else {
            return this.page == ClickGuiScreen.Page.Hud && this.hudPage.mouseClicked((int)mouseX, (int)mouseY, clickedButton, frame) ? true : super.method_25402(mouseX, mouseY, clickedButton);
         }
      }
   }

   public boolean method_25406(double mouseX, double mouseY, int releaseButton) {
      if (this.page == ClickGuiScreen.Page.Module) {
         this.modulePage.mouseReleased((int)mouseX, (int)mouseY, releaseButton);
      } else if (this.page == ClickGuiScreen.Page.Hud) {
         ClickGuiFrame frame = this.lastFrame;
         int mX = frame != null ? (int)frame.unitMouseX((int)mouseX) : (int)mouseX;
         int mY = frame != null ? (int)frame.unitMouseY((int)mouseY) : (int)mouseY;
         this.hudPage.mouseReleased(mX, mY, releaseButton);
      }

      return super.method_25406(mouseX, mouseY, releaseButton);
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.page == ClickGuiScreen.Page.Module) {
         this.modulePage.mouseScrolled(verticalAmount);
      } else if (this.page == ClickGuiScreen.Page.Config) {
         if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
            this.configPage.mouseScrolled(verticalAmount, Wrapper.mc.method_22683().method_4502());
         }
      } else if (this.page == ClickGuiScreen.Page.Hud) {
         this.hudPage.mouseScrolled(verticalAmount);
      }

      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
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
         return super.method_25404(keyCode, scanCode, modifiers);
      } else if (this.page == ClickGuiScreen.Page.Hud) {
         this.hudPage.keyPressed(keyCode);
         return super.method_25404(keyCode, scanCode, modifiers);
      } else {
         return this.page == ClickGuiScreen.Page.Config && this.configPage.keyPressed(keyCode) ? true : super.method_25404(keyCode, scanCode, modifiers);
      }
   }

   public boolean method_25400(char chr, int modifiers) {
      if (this.page == ClickGuiScreen.Page.Module) {
         this.modulePage.charTyped(chr, modifiers);
         return super.method_25400(chr, modifiers);
      } else if (this.page == ClickGuiScreen.Page.Hud) {
         this.hudPage.charTyped(chr, modifiers);
         return super.method_25400(chr, modifiers);
      } else {
         return this.page == ClickGuiScreen.Page.Config && this.configPage.charTyped(chr) ? true : super.method_25400(chr, modifiers);
      }
   }

   public void openConfirm(String title, String message, Runnable yesAction) {
      this.confirmOpen = true;
      this.confirmTitle = title;
      this.confirmMessage = message;
      this.confirmYesAction = yesAction;
      this.configPage.stopNameListening();
   }

   private void closeConfirm() {
      this.confirmOpen = false;
      this.confirmTitle = null;
      this.confirmMessage = null;
      this.confirmYesAction = null;
   }

   private void confirmYes() {
      if (this.confirmYesAction != null) {
         try {
            this.confirmYesAction.run();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      this.closeConfirm();
   }

   private boolean handleConfirmClick(int mouseX, int mouseY, int clickedButton) {
      if (clickedButton != 0) {
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
               float lineH = customFont ? (float)((int)FontManager.ui.getFontHeight()) : TextUtil.getHeight();
               float pad = 10.0F;
               float boxW = Math.min(340.0F, (float)sw - 40.0F);
               float btnH = lineH + 6.0F;
               float boxH = pad + lineH + 6.0F + lineH + 12.0F + btnH + pad;
               float x = ((float)sw - boxW) / 2.0F;
               float y = ((float)sh - boxH) / 2.0F;
               float gap = 8.0F;
               float btnW = (boxW - pad * 2.0F - gap) / 2.0F;
               float btnY = y + boxH - pad - btnH;
               float yesX = x + pad;
               float noX = yesX + btnW + gap;
               boolean inYes = (float)mouseX >= yesX && (float)mouseX <= yesX + btnW && (float)mouseY >= btnY && (float)mouseY <= btnY + btnH;
               boolean inNo = (float)mouseX >= noX && (float)mouseX <= noX + btnW && (float)mouseY >= btnY && (float)mouseY <= btnY + btnH;
               if (inYes) {
                  this.confirmYes();
                  return true;
               } else if (inNo) {
                  this.closeConfirm();
                  return true;
               } else {
                  this.closeConfirm();
                  return true;
               }
            } else {
               this.closeConfirm();
               return true;
            }
         }
      }
   }

   private void renderConfirmDialog(class_332 context, int mouseX, int mouseY) {
      ClickGui gui = ClickGui.getInstance();
      if (gui != null) {
         boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
         boolean customFont = FontManager.isCustomFontEnabled();
         boolean shadow = FontManager.isShadowEnabled();
         int sw = context.method_51421();
         int sh = context.method_51443();
         Render2DUtil.rect(context.method_51448(), 0.0F, 0.0F, (float)sw, (float)sh, (new Color(0, 0, 0, 140)).getRGB());
         float lineH = customFont ? (float)((int)FontManager.ui.getFontHeight()) : TextUtil.getHeight();
         float pad = 10.0F;
         float boxW = Math.min(340.0F, (float)sw - 40.0F);
         float btnH = lineH + 6.0F;
         float boxH = pad + lineH + 6.0F + lineH + 12.0F + btnH + pad;
         float x = ((float)sw - boxW) / 2.0F;
         float y = ((float)sh - boxH) / 2.0F;
         Render2DUtil.rect(context.method_51448(), x, y, x + boxW, y + boxH, gui.defaultColor.getValue().getRGB());
         String title = this.confirmTitle != null && !this.confirmTitle.isEmpty() ? this.confirmTitle : (chinese ? "确认" : "Confirm");
         String msg = this.confirmMessage == null ? "" : this.confirmMessage;
         float titleY = y + pad;
         float msgY = titleY + lineH + 6.0F;
         float titleX = x + (boxW - (float)this.getTextWidth(title)) / 2.0F;
         float msgX = x + (boxW - (float)this.getTextWidth(msg)) / 2.0F;
         TextUtil.drawString(context, title, (double)titleX, (double)titleY, gui.enableTextColor.getValue().getRGB(), customFont, shadow);
         TextUtil.drawString(context, msg, (double)msgX, (double)msgY, gui.defaultTextColor.getValue().getRGB(), customFont, shadow);
         float gap = 8.0F;
         float btnW = (boxW - pad * 2.0F - gap) / 2.0F;
         float btnY = y + boxH - pad - btnH;
         float yesX = x + pad;
         float noX = yesX + btnW + gap;
         boolean hYes = (float)mouseX >= yesX && (float)mouseX <= yesX + btnW && (float)mouseY >= btnY && (float)mouseY <= btnY + btnH;
         boolean hNo = (float)mouseX >= noX && (float)mouseX <= noX + btnW && (float)mouseY >= btnY && (float)mouseY <= btnY + btnH;
         int activeAlpha = hYes ? gui.hoverAlpha.getValueInt() : gui.alpha.getValueInt();
         if (gui.colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
            Render2DUtil.drawLutRect(context.method_51448(), yesX, btnY, btnW, btnH, gui.getSpectrumLutId(), gui.getSpectrumLutHeight(), activeAlpha);
         } else {
            Color ac = gui.getActiveColor((double)btnY * (double)0.25F);
            Render2DUtil.rect(context.method_51448(), yesX, btnY, yesX + btnW, btnY + btnH, ColorUtil.injectAlpha(ac, activeAlpha).getRGB());
         }

         int bgNo = hNo ? gui.hoverColor.getValue().getRGB() : gui.defaultColor.getValue().getRGB();
         Render2DUtil.rect(context.method_51448(), noX, btnY, noX + btnW, btnY + btnH, bgNo);
         String yes = chinese ? "确认" : "Yes";
         String no = chinese ? "取消" : "No";
         float yesTx = yesX + (btnW - (float)this.getTextWidth(yes)) / 2.0F;
         float noTx = noX + (btnW - (float)this.getTextWidth(no)) / 2.0F;
         float btnTy = this.getCenteredTextY(btnY, btnH);
         TextUtil.drawString(context, yes, (double)yesTx, (double)btnTy, gui.enableTextColor.getValue().getRGB(), customFont, shadow);
         TextUtil.drawString(context, no, (double)noTx, (double)btnTy, gui.enableTextColor.getValue().getRGB(), customFont, shadow);
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
      if (FontManager.isCustomFontEnabled()) {
         return (int)FontManager.ui.getWidth(s);
      } else {
         return Wrapper.mc != null ? Wrapper.mc.field_1772.method_1727(s) : 0;
      }
   }

   public float getCenteredTextY(float baseY, float boxHeight) {
      return baseY + (boxHeight - (float)this.getFontHeight()) / 2.0F + (float)ClickGui.getInstance().textOffset.getValueInt();
   }

   private void updateTopTabsLayout(int screenWidth, boolean chinese) {
      int gap = 0;
      int padX = 8;
      int y = 6;
      int h = this.getFontHeight() + 6;
      int total = 0;

      for(int i = 0; i < this.topTabs.size(); ++i) {
         TopTab tab = (TopTab)this.topTabs.get(i);
         String label = tab.getLabel(chinese);
         int w = this.getTextWidth(label) + padX * 2;
         tab.w = w;
         tab.h = h;
         tab.y = y;
         total += w;
         if (i != this.topTabs.size() - 1) {
            total += gap;
         }
      }

      int x = Math.round(((float)screenWidth - (float)total) / 2.0F);

      for(int i = 0; i < this.topTabs.size(); ++i) {
         TopTab tab = (TopTab)this.topTabs.get(i);
         tab.x = x;
         x += tab.w + gap;
      }

   }

   private void renderTopTabs(class_332 context, int mouseX, int mouseY) {
      if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         ClickGui gui = ClickGui.getInstance();
         if (gui != null) {
            boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
            this.updateTopTabsLayout(Wrapper.mc.method_22683().method_4486(), chinese);
            int padX = 8;
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

               float targetX = (float)activeTab.x;
               float targetW = (float)activeTab.w;
               if (!this.topTabAnimInit) {
                  this.topTabAnimX = targetX;
                  this.topTabAnimW = targetW;
                  this.topTabAnimInit = true;
               } else {
                  this.topTabAnimX += (targetX - this.topTabAnimX) * a;
                  this.topTabAnimW += (targetW - this.topTabAnimW) * a;
               }

               for(TopTab tab : this.topTabs) {
                  boolean hovered = mouseX >= tab.x && mouseX <= tab.x + tab.w && mouseY >= tab.y && mouseY <= tab.y + tab.h;
                  int base = gui.defaultColor.getValue().getRGB();
                  int hov = gui.hoverColor.getValue().getRGB();
                  Render2DUtil.rect(context.method_51448(), (float)tab.x, (float)tab.y, (float)(tab.x + tab.w), (float)tab.y + (float)tab.h - 0.5F, hovered ? hov : base);
               }

               boolean hoveredActive = mouseX >= activeTab.x && mouseX <= activeTab.x + activeTab.w && mouseY >= activeTab.y && mouseY <= activeTab.y + activeTab.h;
               int activeAlpha = hoveredActive ? gui.hoverAlpha.getValueInt() : gui.alpha.getValueInt();
               if (gui.colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
                  Render2DUtil.drawLutRect(context.method_51448(), this.topTabAnimX, (float)activeTab.y, this.topTabAnimW, (float)activeTab.h - 0.5F, gui.getSpectrumLutId(), gui.getSpectrumLutHeight(), activeAlpha);
               } else {
                  Color c = gui.getActiveColor((double)activeTab.y * (double)0.25F);
                  Render2DUtil.rect(context.method_51448(), this.topTabAnimX, (float)activeTab.y, this.topTabAnimX + this.topTabAnimW, (float)activeTab.y + (float)activeTab.h - 0.5F, ColorUtil.injectAlpha(c, activeAlpha).getRGB());
               }

               for(TopTab tab : this.topTabs) {
                  boolean hovered = mouseX >= tab.x && mouseX <= tab.x + tab.w && mouseY >= tab.y && mouseY <= tab.y + tab.h;
                  boolean active = this.page == tab.page;
                  int textColor = !active && !hovered ? gui.defaultTextColor.getValue().getRGB() : gui.enableTextColor.getValue().getRGB();
                  float textY = this.getCenteredTextY((float)tab.y, (float)tab.h - 0.5F);
                  TextUtil.drawString(context, tab.getLabel(chinese), (double)(tab.x + padX), (double)textY, textColor, customFont, shadow);
               }

            }
         }
      }
   }

   private boolean handleTopTabClick(int mouseX, int mouseY) {
      if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
         this.updateTopTabsLayout(Wrapper.mc.method_22683().method_4486(), chinese);

         for(TopTab tab : this.topTabs) {
            if (mouseX >= tab.x && mouseX <= tab.x + tab.w && mouseY >= tab.y && mouseY <= tab.y + tab.h) {
               this.setPage(tab.page);
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void renderSnow(class_332 context) {
      ClickGui gui = ClickGui.getInstance();
      if (gui != null && gui.snow.getValue()) {
         float fade = (float)gui.alphaValue;
         if (!(fade <= 0.01F)) {
            int w = context.method_51421();
            int h = context.method_51443();
            int target = Math.max(0, gui.snowAmount.getValueInt());

            while(this.snowflakes.size() < target) {
               this.snowflakes.add(this.spawnSnowflake(w, h, true));
            }

            while(this.snowflakes.size() > target) {
               this.snowflakes.remove(this.snowflakes.size() - 1);
            }

            float dt = AnimateUtil.deltaTime();
            if (dt <= 0.0F) {
               dt = 0.016F;
            }

            float baseSpeed = gui.snowSpeed.getValueFloat();
            float baseSize = gui.snowSize.getValueFloat();
            float wind = gui.snowWind.getValueFloat();
            int a = (int)Math.round((double)gui.snowAlpha.getValueInt() * (double)fade);
            a = Math.max(0, Math.min(255, a));
            Color c = new Color(255, 255, 255, a);

            for(int i = 0; i < this.snowflakes.size(); ++i) {
               Snowflake f = (Snowflake)this.snowflakes.get(i);
               f.y += baseSpeed * f.speedMul * dt;
               f.x += wind * dt + (float)Math.sin((double)(f.y * 0.02F + f.phase)) * f.drift * dt;
               float size = Math.max(0.3F, baseSize * f.sizeMul);
               if (!(f.y > (float)h + size + 2.0F) && !(f.x < -10.0F) && !(f.x > (float)w + 10.0F)) {
                  if (gui.snowShape.getValue() == ClickGui.SnowShape.Circle) {
                     Render2DUtil.drawCircle(context.method_51448(), f.x, f.y, size, c, 16);
                  } else {
                     this.drawSnowflake(context.method_51448(), f.x, f.y, size, c.getRGB(), f.phase + f.y * 0.01F);
                  }
               } else {
                  this.snowflakes.set(i, this.spawnSnowflake(w, h, false));
               }
            }

         }
      } else {
         if (!this.snowflakes.isEmpty()) {
            this.snowflakes.clear();
         }

      }
   }

   private void drawSnowflake(class_4587 matrices, float x, float y, float r, int color, float rotation) {
      float branchLen = r * 0.45F;
      float branchOffset = 0.65F;

      for(int i = 0; i < 3; ++i) {
         double a = (double)rotation + (double)i * (Math.PI / 3D);
         float dx = (float)Math.cos(a) * r;
         float dy = (float)Math.sin(a) * r;
         Render2DUtil.drawLine(matrices, x - dx, y - dy, x + dx, y + dy, color);
         float fx1 = x + dx * branchOffset;
         float fy1 = y + dy * branchOffset;
         Render2DUtil.drawLine(matrices, fx1, fy1, fx1 + (float)Math.cos(a + (Math.PI / 6D)) * branchLen, fy1 + (float)Math.sin(a + (Math.PI / 6D)) * branchLen, color);
         Render2DUtil.drawLine(matrices, fx1, fy1, fx1 + (float)Math.cos(a - (Math.PI / 6D)) * branchLen, fy1 + (float)Math.sin(a - (Math.PI / 6D)) * branchLen, color);
         double a2 = a + Math.PI;
         float fx2 = x - dx * branchOffset;
         float fy2 = y - dy * branchOffset;
         Render2DUtil.drawLine(matrices, fx2, fy2, fx2 + (float)Math.cos(a2 + (Math.PI / 6D)) * branchLen, fy2 + (float)Math.sin(a2 + (Math.PI / 6D)) * branchLen, color);
         Render2DUtil.drawLine(matrices, fx2, fy2, fx2 + (float)Math.cos(a2 - (Math.PI / 6D)) * branchLen, fy2 + (float)Math.sin(a2 - (Math.PI / 6D)) * branchLen, color);
      }

   }

   private Snowflake spawnSnowflake(int w, int h, boolean randomY) {
      float x = (float)this.snowRandom.nextInt(Math.max(1, w));
      float y = randomY ? (float)this.snowRandom.nextInt(Math.max(1, h)) : -5.0F - this.snowRandom.nextFloat() * 20.0F;
      float phase = this.snowRandom.nextFloat() * ((float)Math.PI * 2F);
      float drift = 10.0F + this.snowRandom.nextFloat() * 40.0F;
      float speedMul = 0.55F + this.snowRandom.nextFloat() * 1.05F;
      float sizeMul = 0.6F + this.snowRandom.nextFloat() * 1.2F;
      return new Snowflake(x, y, phase, drift, speedMul, sizeMul);
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

      private TopTab(Page page, String labelEn, String labelZh) {
         this.page = page;
         this.labelEn = labelEn;
         this.labelZh = labelZh;
      }

      private String getLabel(boolean chinese) {
         if (chinese) {
            return this.labelZh == null ? this.labelEn : this.labelZh;
         } else {
            return this.labelEn;
         }
      }
   }

   private static final class Snowflake {
      private float x;
      private float y;
      private final float phase;
      private final float drift;
      private final float speedMul;
      private final float sizeMul;

      private Snowflake(float x, float y, float phase, float drift, float speedMul, float sizeMul) {
         this.x = x;
         this.y = y;
         this.phase = phase;
         this.drift = drift;
         this.speedMul = speedMul;
         this.sizeMul = sizeMul;
      }
   }
}
