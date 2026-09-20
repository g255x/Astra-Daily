package dev.Astra.mod.modules.impl.client;

import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.gui.fonts.FontRenderer;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_332;

public class NotificationsHud extends Module {
   public static NotificationsHud INSTANCE;
   private final SliderSetting scale = this.add(new SliderSetting("Scale", (double)1.0F, (double)0.5F, (double)2.0F, 0.1));
   private final SliderSetting backgroundAlpha = this.add(new SliderSetting("BackgroundAlpha", (double)200.0F, (double)0.0F, (double)255.0F, (double)1.0F));
   private final SliderSetting displayTime = this.add(new SliderSetting("DisplayTime", (double)2000.0F, (double)500.0F, (double)5000.0F, (double)100.0F));
   private final SliderSetting notifyX = this.add(new SliderSetting("X", (double)10.0F, (double)0.0F, (double)1000.0F, (double)1.0F));
   private final SliderSetting notifyY = this.add(new SliderSetting("Y", (double)330.0F, (double)0.0F, (double)1000.0F, (double)1.0F));

   public NotificationsHud() {
      super("NotificationsHud", Module.Category.Client);
      this.setChinese("通知HUD");
      INSTANCE = this;
   }

   public void onRender2D(class_332 drawContext, float tickDelta) {
      if (mc.method_22683() != null) {
         NotificationManager.INSTANCE.update();
         if (!NotificationManager.INSTANCE.isEmpty()) {
            float s = this.scale.getValueFloat();
            float boxHeight = FontManager.ui.getFontHeight() * s + 8.0F * s;
            float spacing = boxHeight + 4.0F * s;
            int bgAlpha = this.backgroundAlpha.getValueInt();
            int screenW = mc.method_22683().method_4486();
            int screenH = mc.method_22683().method_4502();
            float anchorX = (float)(screenW - this.notifyX.getValueInt());
            float anchorY = (float)(screenH - this.notifyY.getValueInt());
            Iterator<NotificationInfo> iterator = NotificationManager.INSTANCE.getNotifications().iterator();
            int index = 0;

            while(iterator.hasNext()) {
               NotificationInfo n = (NotificationInfo)iterator.next();
               n.update();
               if (!n.isExpired()) {
                  float baseWidth = 100.0F * s;
                  float fontPad = 4.0F * s;
                  FontRenderer font = FontManager.ui;
                  String var10003 = n.getTitle();
                  float boxWidth = Math.max(baseWidth, fontPad + font.getWidth(var10003 + " " + n.getSubTitle()) * s);
                  float renderX = anchorX - boxWidth;
                  float targetY = anchorY - (float)(index + 1) * spacing;
                  if (Math.abs(targetY - n.getCurrentY()) > 0.5F) {
                     n.setTargetY(targetY);
                  }

                  float y = n.getCurrentY();
                  long time = System.currentTimeMillis() - n.getCreateTime();
                  long exitTime = time - (long)n.getDisplayDuration();
                  boolean skipIntro = n.shouldSkipIntroAnimation();
                  Color modeColor = this.getModeColor(n.getMode(), 255);
                  if (!skipIntro && time <= 300L) {
                     float p = easeOutCubic((float)time / 300.0F);
                     float w = boxWidth * p;
                     Render2DUtil.drawRect(drawContext.method_51448(), renderX + boxWidth - w, y, w, boxHeight, modeColor);
                  } else if (!skipIntro && time <= 500L) {
                     float p = easeOutCubic((float)(time - 300L) / 200.0F);
                     int a = (int)((float)bgAlpha * p);
                     Render2DUtil.drawRect(drawContext.method_51448(), renderX, y, boxWidth, boxHeight, new Color(0, 0, 0, a));
                     float sliderWidth = 3.0F * s + (boxWidth - 3.0F * s) * (1.0F - p);
                     Render2DUtil.drawRect(drawContext.method_51448(), renderX, y, sliderWidth, boxHeight, modeColor);
                     this.renderText(drawContext, n, renderX, y, boxHeight, s, (int)(255.0F * p));
                  } else if (exitTime < 0L) {
                     Render2DUtil.drawRect(drawContext.method_51448(), renderX, y, boxWidth, boxHeight, new Color(0, 0, 0, bgAlpha));
                     Render2DUtil.drawRect(drawContext.method_51448(), renderX, y, 3.0F * s, boxHeight, modeColor);
                     this.renderText(drawContext, n, renderX, y, boxHeight, s, 255);
                  } else if (exitTime <= 200L) {
                     float p = easeOutCubicDec((float)exitTime / 200.0F);
                     int a = (int)((float)bgAlpha * p);
                     Render2DUtil.drawRect(drawContext.method_51448(), renderX, y, boxWidth, boxHeight, new Color(0, 0, 0, a));
                     float sliderWidth = 3.0F * s + (boxWidth - 3.0F * s) * (1.0F - p);
                     Render2DUtil.drawRect(drawContext.method_51448(), renderX, y, sliderWidth, boxHeight, modeColor);
                     this.renderText(drawContext, n, renderX, y, boxHeight, s, (int)(255.0F * p));
                  } else if (exitTime <= 500L) {
                     float p = easeOutCubicDec((float)(exitTime - 200L) / 300.0F);
                     float w = boxWidth * p;
                     Render2DUtil.drawRect(drawContext.method_51448(), renderX + boxWidth - w, y, w, boxHeight, modeColor);
                  }

                  ++index;
               }
            }

         }
      }
   }

   private void renderText(class_332 drawContext, NotificationInfo n, float x, float y, float boxHeight, float s, int alpha) {
      float textY = y + boxHeight * 0.5F - FontManager.ui.getFontHeight() * s * 0.5F;
      Color titleColor = n.isModule() ? new Color(n.getMode() == NotificationMode.Success ? 118 : 255, n.getMode() == NotificationMode.Success ? 185 : 75, n.getMode() == NotificationMode.Success ? 0 : 75, alpha) : this.getModeColor(n.getMode(), alpha);
      FontManager.ui.drawString(drawContext.method_51448(), n.getTitle(), (double)(x + 4.0F * s), (double)textY, titleColor.getRGB());
      FontManager.ui.drawString(drawContext.method_51448(), " " + n.getSubTitle(), (double)(x + 4.0F * s + FontManager.ui.getWidth(n.getTitle()) * s), (double)textY, (new Color(255, 255, 255, alpha)).getRGB());
   }

   private Color getModeColor(NotificationMode mode, int alpha) {
      Color var10000;
      switch (mode) {
         case Success -> var10000 = new Color(118, 185, 0, alpha);
         case Error -> var10000 = new Color(255, 75, 75, alpha);
         case Info -> var10000 = new Color(85, 170, 255, alpha);
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private static float easeOutCubic(float t) {
      return 1.0F - (1.0F - t) * (1.0F - t) * (1.0F - t);
   }

   private static float easeOutCubicDec(float t) {
      return 1.0F - easeOutCubic(t);
   }

   public static void addModuleNotification(String moduleName, boolean enabled) {
      if (INSTANCE != null) {
         NotificationManager.INSTANCE.postModuleNotification(moduleName, enabled, INSTANCE.displayTime.getValueInt());
      }
   }
}
