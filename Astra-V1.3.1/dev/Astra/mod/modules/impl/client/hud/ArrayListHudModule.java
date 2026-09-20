package dev.Astra.mod.modules.impl.client.hud;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.InitEvent;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import net.minecraft.class_332;

public class ArrayListHudModule extends HudModule {
   public static ArrayListHudModule INSTANCE;
   private final BooleanSetting lowerCase = this.add(new BooleanSetting("LowerCase", false));
   private final BooleanSetting listSort = this.add(new BooleanSetting("ListSort", true));
   private final BooleanSetting rightAlign = this.add(new BooleanSetting("RightAlign", true));
   private final SliderSetting xOffset = this.add(new SliderSetting("XOffset", (double)0.0F, (double)0.0F, (double)50.0F, 0.1));
   private final SliderSetting textOffset = this.add(new SliderSetting("TextOffset", (double)0.0F, (double)-10.0F, (double)10.0F, 0.1));
   private final SliderSetting interval = this.add(new SliderSetting("Interval", (double)0.0F, (double)0.0F, (double)15.0F, 0.1));
   private final SliderSetting enableLength = this.add(new SliderSetting("EnableLength", 200, 0, 1000));
   private final SliderSetting disableLength = this.add(new SliderSetting("DisableLength", 200, 0, 1000));
   private final SliderSetting fadeLength = this.add(new SliderSetting("FadeLength", 200, 0, 1000));
   private final EnumSetting<Easing> easing;
   private final BooleanSetting blur;
   private final SliderSetting radius;
   private final BooleanSetting backGround;
   private final SliderSetting width;
   private final BooleanSetting rect;
   private final BooleanSetting glow;
   private final ArrayList<Entry> entries;

   public ArrayListHudModule() {
      super("ArrayList", "", "模块列表", 2, 2, HudModule.Corner.RightTop);
      this.easing = this.add(new EnumSetting("Easing", Easing.CircInOut));
      this.blur = this.add((new BooleanSetting("Blur", false)).setParent());
      this.radius = this.add(new SliderSetting("Radius", (double)10.0F, (double)0.0F, (double)100.0F, () -> this.blur.isOpen()));
      this.backGround = this.add((new BooleanSetting("BackGround", false)).setParent());
      this.width = this.add(new SliderSetting("Width", (double)0.0F, (double)0.0F, (double)15.0F, () -> this.backGround.isOpen()));
      this.rect = this.add(new BooleanSetting("Rect", false));
      this.glow = this.add(new BooleanSetting("Glow", false));
      this.entries = new ArrayList();
      INSTANCE = this;
      Astra.EVENT_BUS.subscribe(new InitHandler());
   }

   public void onRender2D(class_332 context, float tickDelta) {
      if (Module.nullCheck()) {
         this.clearHudBounds();
      } else {
         int fontHeight = this.getFontHeight();
         double maxLineW = (double)0.0F;

         for(Entry e : this.entries) {
            e.prepare(this);
            if (!(e.renderFade <= 0.01) || !(e.renderWidth <= 0.01)) {
               maxLineW = Math.max(maxLineW, e.renderWidth);
            }
         }

         if (maxLineW <= (double)0.5F) {
            this.clearHudBounds();
         } else {
            float extraW = this.width.getValueFloat();
            float xPadHalf = extraW / 2.0F;
            float lineH = (float)fontHeight + this.interval.getValueFloat();
            float yPad = this.interval.getValueFloat() / 2.0F;
            double usedH = (double)0.0F;

            for(Entry e : this.entries) {
               if (!(e.renderFade <= 0.04)) {
                  usedH += ((double)fontHeight + this.interval.getValue()) * e.renderFade;
               }
            }

            int boundsW = Math.max(1, (int)Math.ceil(maxLineW + (double)extraW + (double)xPadHalf));
            int boundsH = Math.max(1, (int)Math.ceil(Math.max((double)1.0F, usedH + (double)fontHeight)));
            int boundsX = this.getHudRenderX(boundsW);
            int boundsY = this.getHudRenderY(boundsH);
            int startX = (int)Math.floor((double)boundsX + (double)xPadHalf);
            double counter = (double)20.0F;
            double currentY = (double)boundsY;

            for(Entry e : this.entries) {
               if (!(e.renderFade <= 0.04)) {
                  double lineW = e.renderWidth;
                  float x = this.rightAlign.getValue() ? (float)((double)startX + maxLineW - lineW) : (float)startX;
                  double fade = e.renderFade;
                  Color baseColor = this.getHudColor(counter += fade);
                  int baseAlpha = baseColor.getAlpha();
                  int c = ColorUtil.injectAlpha(baseColor.getRGB(), (int)((double)baseAlpha * fade));
                  float bgX = x - xPadHalf;
                  float bgY = (float)currentY - 1.0F - yPad;
                  float bgW = (float)(lineW + (double)extraW);
                  if (this.blur.getValue()) {
                     Astra.BLUR.applyBlur((float)(this.radius.getValue() * fade), bgX, bgY, bgW, lineH);
                  }

                  if (this.backGround.getValue()) {
                     Render2DUtil.drawRect(context.method_51448(), bgX, bgY, bgW, lineH, ColorUtil.injectAlpha(baseColor.getRGB(), (int)((double)100.0F * fade)));
                  }

                  if (this.glow.getValue()) {
                     Render2DUtil.drawGlow(context.method_51448(), bgX, bgY, bgW, lineH, ColorUtil.injectAlpha(baseColor.getRGB(), (int)((double)baseAlpha * fade)));
                  }

                  TextUtil.drawString(context, e.string, (double)x, currentY + (double)this.textOffset.getValueFloat(), c, HudSetting.useFont(), HudSetting.useShadow());
                  if (this.rect.getValue()) {
                     Render2DUtil.drawRect(context.method_51448(), bgX + bgW, bgY, 1.0F, lineH, ColorUtil.injectAlpha(baseColor.getRGB(), (int)((double)baseAlpha * fade)));
                  }

                  currentY += ((double)fontHeight + this.interval.getValue()) * fade;
               }
            }

            this.setHudBounds(boundsX, boundsY, boundsW, boundsH);
         }
      }
   }

   @EventListener(
      priority = -999
   )
   public void onUpdate(ClientTickEvent event) {
      if (!Module.nullCheck()) {
         if (event.isPost()) {
            for(Entry e : this.entries) {
               e.onUpdate(this);
            }

            if (this.listSort.getValue()) {
               this.entries.sort(Comparator.comparingInt((ex) -> ex.string == null ? 0 : -this.getWidth(ex.string)));
            }
         }

      }
   }

   private int getWidth(String s) {
      if (s == null) {
         return 0;
      } else {
         return HudSetting.useFont() ? (int)FontManager.ui.getWidth(s) : mc.field_1772.method_1727(s);
      }
   }

   private int getFontHeight() {
      if (HudSetting.useFont()) {
         return (int)FontManager.ui.getFontHeight();
      } else {
         Objects.requireNonNull(mc.field_1772);
         return 9;
      }
   }

   private Color getHudColor(double counter) {
      ClickGui gui = ClickGui.getInstance();
      return gui == null ? Color.WHITE : gui.getColor(counter);
   }

   private class InitHandler {
      @EventListener
      public void onInit(InitEvent event) {
         for(Module module : Astra.MODULE.getModules()) {
            ArrayListHudModule.this.entries.add(new Entry(module));
         }

         Astra.EVENT_BUS.unsubscribe(this);
      }
   }

   private static final class Entry {
      private final Module module;
      private String string = "";
      private boolean isOn;
      private double currentX = (double)0.0F;
      private final Animation animation = new Animation();
      private final Animation fadeAnimation = new Animation();
      private double renderWidth;
      private double renderFade;

      private Entry(Module module) {
         this.module = module;
      }

      private void onUpdate(ArrayListHudModule parent) {
         this.isOn = this.module.isOn() && this.module.drawn.getValue();
         if (this.isOn) {
            String s = this.module.getArrayName();
            if (s == null) {
               s = "";
            }

            this.string = parent.lowerCase.getValue() ? s.toLowerCase() : s;
         }

      }

      private void prepare(ArrayListHudModule parent) {
         if (this.currentX <= (double)0.0F && !this.isOn) {
            this.renderWidth = (double)0.0F;
            this.renderFade = (double)0.0F;
         } else {
            String text = this.string == null ? "" : this.string;
            double target = (double)(parent.getWidth(text) + 1);
            this.currentX = this.animation.get(this.isOn ? target : (double)0.0F, this.isOn ? (long)parent.enableLength.getValueInt() : (long)parent.disableLength.getValueInt(), parent.easing.getValue());
            this.renderFade = this.fadeAnimation.get(this.isOn ? (double)1.0F : (double)0.0F, (long)parent.fadeLength.getValueInt(), parent.easing.getValue());
            this.renderWidth = this.currentX + (double)parent.xOffset.getValueFloat();
         }
      }
   }
}
