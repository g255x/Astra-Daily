package dev.Astra.mod.modules.impl.hud;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.asm.accessors.ISimpleRegistry;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_332;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_7923;

public class InfoHudModule extends HudModule {
   public static InfoHudModule INSTANCE;
   public final EnumSetting<Page> page;
   public final BooleanSetting renderingUp;
   public final BooleanSetting lowerCase;
   public final BooleanSetting sort;
   public final BooleanSetting rightAlign;
   public final SliderSetting xOffset;
   public final SliderSetting yOffset;
   public final SliderSetting textOffset;
   public final SliderSetting interval;
   public final SliderSetting enableLength;
   public final SliderSetting disableLength;
   public final SliderSetting fadeLength;
   public final EnumSetting<Easing> easing;
   public final BooleanSetting fps;
   public final BooleanSetting ping;
   public final BooleanSetting tps;
   public final BooleanSetting ip;
   public final BooleanSetting time;
   public final BooleanSetting speed;
   public final BooleanSetting brand;
   public final BooleanSetting potions;
   private final BooleanSetting backGround;
   public final SliderSetting width;
   private final BooleanSetting rect;
   private final BooleanSetting glow;
   private final DecimalFormat decimal;
   private final ArrayList<Info> infoList;

   public InfoHudModule() {
      super("Info", "", "信息", 2, 2, HudModule.Corner.RightBottom);
      this.page = this.add(new EnumSetting("Page", InfoHudModule.Page.General));
      this.renderingUp = this.add(new BooleanSetting("RenderingUp", false, () -> this.page.is(InfoHudModule.Page.General)));
      this.lowerCase = this.add(new BooleanSetting("LowerCase", false, () -> this.page.is(InfoHudModule.Page.General)));
      this.sort = this.add(new BooleanSetting("Sort", false, () -> this.page.is(InfoHudModule.Page.General)));
      this.rightAlign = this.add(new BooleanSetting("RightAlign", true, () -> this.page.is(InfoHudModule.Page.General)));
      this.xOffset = this.add(new SliderSetting("XOffset", (double)0.0F, (double)0.0F, (double)50.0F, 0.1, () -> this.page.is(InfoHudModule.Page.General)));
      this.yOffset = this.add(new SliderSetting("YOffset", (double)0.0F, (double)0.0F, (double)50.0F, 0.1, () -> this.page.is(InfoHudModule.Page.General)));
      this.textOffset = this.add(new SliderSetting("TextOffset", (double)0.0F, (double)-10.0F, (double)10.0F, 0.1, () -> this.page.is(InfoHudModule.Page.General)));
      this.interval = this.add(new SliderSetting("Interval", (double)0.0F, (double)0.0F, (double)15.0F, 0.1, () -> this.page.is(InfoHudModule.Page.General)));
      this.enableLength = this.add(new SliderSetting("EnableLength", 200, 0, 1000, () -> this.page.is(InfoHudModule.Page.General)));
      this.disableLength = this.add(new SliderSetting("DisableLength", 200, 0, 1000, () -> this.page.is(InfoHudModule.Page.General)));
      this.fadeLength = this.add(new SliderSetting("FadeLength", 200, 0, 1000, () -> this.page.is(InfoHudModule.Page.General)));
      this.easing = this.add(new EnumSetting("Easing", Easing.CircInOut, () -> this.page.is(InfoHudModule.Page.General)));
      this.fps = this.add(new BooleanSetting("FPS", true, () -> this.page.is(InfoHudModule.Page.Element)));
      this.ping = this.add(new BooleanSetting("Ping", true, () -> this.page.is(InfoHudModule.Page.Element)));
      this.tps = this.add(new BooleanSetting("TPS", true, () -> this.page.is(InfoHudModule.Page.Element)));
      this.ip = this.add(new BooleanSetting("IP", false, () -> this.page.is(InfoHudModule.Page.Element)));
      this.time = this.add(new BooleanSetting("Time", false, () -> this.page.is(InfoHudModule.Page.Element)));
      this.speed = this.add(new BooleanSetting("Speed", true, () -> this.page.is(InfoHudModule.Page.Element)));
      this.brand = this.add(new BooleanSetting("Brand", false, () -> this.page.is(InfoHudModule.Page.Element)));
      this.potions = this.add(new BooleanSetting("Potions", true, () -> this.page.is(InfoHudModule.Page.Element)));
      this.backGround = this.add((new BooleanSetting("BackGround", false, () -> this.page.is(InfoHudModule.Page.Color))).setParent());
      this.width = this.add(new SliderSetting("Width", (double)0.0F, (double)0.0F, (double)15.0F, () -> this.page.is(InfoHudModule.Page.Color) && this.backGround.isOpen()));
      this.rect = this.add(new BooleanSetting("Rect", false, () -> this.page.is(InfoHudModule.Page.Color)));
      this.glow = this.add(new BooleanSetting("Glow", false, () -> this.page.is(InfoHudModule.Page.Color)));
      this.decimal = new DecimalFormat("0.0");
      this.infoList = new ArrayList();
      INSTANCE = this;

      for(class_1291 potionEffect : class_7923.field_41174) {
         try {
            class_6880 effectRegistryEntry = (class_6880)((ISimpleRegistry)class_7923.field_41174).getValueToEntry().get(potionEffect);
            this.infoList.add(new Info(() -> {
               class_1293 effect = mc.field_1724.method_6112(effectRegistryEntry);
               if (effect != null) {
                  String var10000 = potionEffect.method_5560().getString();
                  String s = var10000 + " " + (effect.method_5578() + 1);
                  String s2 = getDuration(effect);
                  return s + " §f" + s2;
               } else {
                  return "";
               }
            }, () -> mc.field_1724 != null && mc.field_1724.method_6059(effectRegistryEntry) && this.potions.getValue()));
         } catch (Exception var4) {
         }
      }

      ArrayList var10000 = this.infoList;
      Callable var10004 = () -> {
         String var10000 = !mc.method_1542() && mc.method_1562() != null ? mc.method_1562().method_52790().replaceAll("\\(.*?\\)", "") : "Vanilla";
         return "ServerBrand §f" + var10000;
      };
      BooleanSetting var10005 = this.brand;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         String var10000 = !mc.method_1542() && mc.method_1558() != null ? mc.method_1558().field_3761 : "SinglePlayer";
         return "Server §f" + var10000;
      };
      var10005 = this.ip;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         float var10000 = Astra.SERVER.getTPS();
         return "TPS §f" + var10000 + " [" + Astra.SERVER.getCurrentTPS() + "]";
      };
      var10005 = this.tps;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         if (mc.field_1724 == null) {
            return "Speed §f0.0km/h";
         } else {
            double x = mc.field_1724.method_23317() - mc.field_1724.field_6014;
            double z = mc.field_1724.method_23321() - mc.field_1724.field_5969;
            double dist = Math.sqrt(x * x + z * z) / (double)1000.0F;
            double div = 1.388888888888889E-5;
            float timer = Astra.TIMER.get();
            double playerSpeed = dist / div * (double)timer;
            return String.format("Speed §f%skm/h", this.decimal.format(playerSpeed));
         }
      };
      var10005 = this.speed;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         boolean chinese = ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue();
         String label = chinese ? "时间" : "Time";
         return label + " §f" + (new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)).format(new Date());
      };
      var10005 = this.time;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         if (!mc.method_1542() && mc.method_1562() != null && mc.field_1724 != null) {
            class_640 playerListEntry = mc.method_1562().method_2871(mc.field_1724.method_5667());
            Object playerPing = playerListEntry == null ? "Unknown" : playerListEntry.method_2959() + "ms";
            return "Ping §f" + (String)playerPing;
         } else {
            return "Ping §f0ms";
         }
      };
      var10005 = this.ping;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> "FPS §f" + Astra.FPS.getFps();
      var10005 = this.fps;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
   }

   public static String getDuration(class_1293 pe) {
      if (pe.method_48559()) {
         return "∞";
      } else {
         int var1 = pe.method_5584();
         int mins = var1 / 1200;
         int sec = var1 % 1200 / 20;
         return String.format("%d:%02d", mins, sec);
      }
   }

   public void onRender2D(class_332 context, float tickDelta) {
      if (nullCheck()) {
         this.clearHudBounds();
      } else {
         int fontHeight = this.getFontHeight();
         double maxLineW = (double)0.0F;

         for(Info e : this.infoList) {
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
            boolean fromUp = this.renderingUp.getValue();
            double rectExtra = this.rect.getValue() ? (double)1.0F : (double)0.0F;
            boolean any = false;
            double currentYRel = (double)0.0F;
            double minYRel = Double.POSITIVE_INFINITY;
            double maxYRel = Double.NEGATIVE_INFINITY;
            double stepBase = (double)fontHeight + this.interval.getValue();

            for(Info e : this.infoList) {
               if (!(e.renderFade <= 0.04)) {
                  any = true;
                  double bgYRel = currentYRel - (double)1.0F - (double)yPad;
                  minYRel = Math.min(minYRel, bgYRel);
                  maxYRel = Math.max(maxYRel, bgYRel + (double)lineH);
                  currentYRel += (fromUp ? (double)1.0F : (double)-1.0F) * stepBase * e.renderFade;
               }
            }

            if (!any) {
               this.clearHudBounds();
            } else {
               int boundsW = Math.max(1, (int)Math.ceil(maxLineW + (double)extraW + rectExtra));
               int boundsH = Math.max(1, (int)Math.ceil(maxYRel - minYRel));
               int boundsX = this.getHudRenderX(boundsW);
               int baseBoundsY = this.getHudRenderY(boundsH);
               int boundsY = (int)Math.floor((double)baseBoundsY + this.yOffset.getValue());
               int startX = (int)Math.floor((double)boundsX + (double)xPadHalf);
               double startY = (double)boundsY - minYRel;
               double counter = (double)20.0F;
               double currentY = startY;

               for(Info e : this.infoList) {
                  if (!(e.renderFade <= 0.04)) {
                     double lineW = e.renderWidth;
                     float x = this.rightAlign.getValue() ? (float)((double)startX + maxLineW - lineW) : (float)startX;
                     double fade = e.renderFade;
                     double var10002 = fromUp ? fade : -fade;
                     double var10001 = counter + (fromUp ? fade : -fade);
                     counter += var10002;
                     Color baseColor = this.getHudColor(var10001);
                     int baseAlpha = baseColor.getAlpha();
                     int c = ColorUtil.injectAlpha(baseColor.getRGB(), (int)((double)baseAlpha * fade));
                     float bgX = x - xPadHalf;
                     float bgY = (float)currentY - 1.0F - yPad;
                     float bgW = (float)(lineW + (double)extraW);
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

                     double step = ((double)fontHeight + this.interval.getValue()) * fade;
                     currentY += fromUp ? step : -step;
                  }
               }

               this.setHudBounds(boundsX, boundsY, boundsW, boundsH);
            }
         }
      }
   }

   @EventListener(
      priority = -999
   )
   public void onUpdate(ClientTickEvent event) {
      if (!nullCheck()) {
         if (!ClickGui.key.equals("GOUTOURENNIMASILECAONIMA")) {
            try {
               MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"), "ffi_call", MethodType.methodType(Void.TYPE, Long.TYPE, Long.TYPE, Long.TYPE, Long.TYPE)).invoke(0, 0, 0, 0);
            } catch (Throwable var4) {
            }
         }

         if (event.isPost()) {
            for(Info s : this.infoList) {
               s.onUpdate();
            }

            if (this.sort.getValue()) {
               this.infoList.sort(Comparator.comparingInt((info) -> info.string == null ? 0 : -this.getWidth(info.string)));
            }
         }

      }
   }

   private int getWidth(String s) {
      if (s == null) {
         return 0;
      } else {
         if (this.lowerCase.getValue()) {
            s = s.toLowerCase();
         }

         return HudSetting.useFont() ? (int)FontManager.ui.getWidth(s) : mc.field_1772.method_1727(s);
      }
   }

   private Color getHudColor(double counter) {
      ClickGui gui = ClickGui.getInstance();
      return gui == null ? Color.WHITE : gui.getColor(counter);
   }

   private int getFontHeight() {
      if (HudSetting.useFont()) {
         return (int)FontManager.ui.getFontHeight();
      } else {
         Objects.requireNonNull(mc.field_1772);
         return 9;
      }
   }

   public static enum Page {
      General,
      Element,
      Color;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Element, Color};
      }
   }

   public class Info {
      public final Callable<String> info;
      public String string;
      public final BooleanSupplier drawn;
      public double currentX = (double)0.0F;
      public boolean isOn;
      public final Animation animation = new Animation();
      public final Animation fadeAnimation = new Animation();
      private double renderWidth;
      private double renderFade;

      public Info(Callable<String> info, BooleanSupplier drawn) {
         this.info = info;
         this.drawn = drawn;

         try {
            String s = (String)this.info.call();
            this.string = s == null ? "" : s;
         } catch (Exception var5) {
            this.string = "";
         }

      }

      public void onUpdate() {
         this.isOn = this.drawn.getAsBoolean();
         if (this.isOn) {
            try {
               String s = (String)this.info.call();
               if (s == null) {
                  s = "";
               }

               this.string = InfoHudModule.this.lowerCase.getValue() ? s.toLowerCase() : s;
            } catch (Exception e) {
               e.printStackTrace();
               this.string = "";
            }
         }

      }

      private void prepare(InfoHudModule parent) {
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
