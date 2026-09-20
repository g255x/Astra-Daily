package dev.luminous.mod.modules.impl.client;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.InitEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.Animation;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.asm.accessors.ISimpleRegistry;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1937;
import net.minecraft.class_332;
import net.minecraft.class_408;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_7923;

public class HUD extends Module {
   public static HUD INSTANCE;
   public final EnumSetting<Page> page;
   public final BooleanSetting renderingUp;
   public final BooleanSetting font;
   public final BooleanSetting shadow;
   public final BooleanSetting lowerCase;
   public final BooleanSetting sort;
   public final SliderSetting xOffset;
   public final SliderSetting yOffset;
   public final SliderSetting textOffset;
   public final SliderSetting interval;
   public final SliderSetting enableLength;
   public final SliderSetting disableLength;
   public final SliderSetting fadeLength;
   public final EnumSetting<Easing> easing;
   public final BooleanSetting arrayList;
   public final BooleanSetting listSort;
   public final BooleanSetting armor;
   public final SliderSetting armorOffset;
   public final BooleanSetting durability;
   public final BooleanSetting waterMark;
   public final ColorSetting pulse;
   public final StringSetting waterMarkString;
   public final SliderSetting waterMarkOffset;
   public final BooleanSetting fps;
   public final BooleanSetting ping;
   public final BooleanSetting tps;
   public final BooleanSetting ip;
   public final BooleanSetting time;
   public final BooleanSetting speed;
   public final BooleanSetting brand;
   public final BooleanSetting potions;
   public final BooleanSetting coords;
   public final BooleanSetting colorSync;
   public final ColorSetting color;
   private final EnumSetting<ColorMode> colorMode;
   private final SliderSetting rainbowSpeed;
   private final SliderSetting saturation;
   private final SliderSetting rainbowDelay;
   private final ColorSetting endColor;
   private final SliderSetting pulseSpeed;
   private final SliderSetting pulseCounter;
   public final BooleanSetting blur;
   public final SliderSetting radius;
   private final BooleanSetting backGround;
   public final SliderSetting width;
   private final ColorSetting bgColor;
   private final ColorSetting rect;
   private final ColorSetting glow;
   private final DecimalFormat decimal;
   private final ArrayList<Info> infoList;
   private final ArrayList<Info> moduleList;

   public HUD() {
      super("HUD", Module.Category.Client);
      this.page = this.add(new EnumSetting("Page", HUD.Page.General));
      this.renderingUp = this.add(new BooleanSetting("RenderingUp", false, () -> this.page.is(HUD.Page.General)));
      this.font = this.add(new BooleanSetting("Font", true, () -> this.page.is(HUD.Page.General)));
      this.shadow = this.add(new BooleanSetting("Shadow", true, () -> this.page.is(HUD.Page.General)));
      this.lowerCase = this.add(new BooleanSetting("LowerCase", false, () -> this.page.is(HUD.Page.General)));
      this.sort = this.add(new BooleanSetting("Sort", false, () -> this.page.is(HUD.Page.General)));
      this.xOffset = this.add(new SliderSetting("XOffset", (double)0.0F, (double)0.0F, (double)50.0F, 0.1, () -> this.page.is(HUD.Page.General)));
      this.yOffset = this.add(new SliderSetting("YOffset", (double)0.0F, (double)0.0F, (double)50.0F, 0.1, () -> this.page.is(HUD.Page.General)));
      this.textOffset = this.add(new SliderSetting("TextOffset", (double)0.0F, (double)-10.0F, (double)10.0F, 0.1, () -> this.page.is(HUD.Page.General)));
      this.interval = this.add(new SliderSetting("Interval", (double)0.0F, (double)0.0F, (double)15.0F, 0.1, () -> this.page.is(HUD.Page.General)));
      this.enableLength = this.add(new SliderSetting("EnableLength", 200, 0, 1000, () -> this.page.is(HUD.Page.General)));
      this.disableLength = this.add(new SliderSetting("DisableLength", 200, 0, 1000, () -> this.page.is(HUD.Page.General)));
      this.fadeLength = this.add(new SliderSetting("FadeLength", 200, 0, 1000, () -> this.page.is(HUD.Page.General)));
      this.easing = this.add(new EnumSetting("Easing", Easing.CircInOut, () -> this.page.is(HUD.Page.General)));
      this.arrayList = this.add((new BooleanSetting("ArrayList", true, () -> this.page.is(HUD.Page.Element))).setParent());
      this.listSort = this.add(new BooleanSetting("ListSort", true, () -> this.page.is(HUD.Page.Element) && this.arrayList.isOpen()));
      this.armor = this.add((new BooleanSetting("Armor", true, () -> this.page.is(HUD.Page.Element))).setParent());
      this.armorOffset = this.add(new SliderSetting("ArmorOffset", (double)1.0F, (double)0.0F, (double)100.0F, (double)-1.0F, () -> this.page.is(HUD.Page.Element) && this.armor.isOpen()));
      this.durability = this.add(new BooleanSetting("Durability", true, () -> this.page.is(HUD.Page.Element) && this.armor.isOpen()));
      this.waterMark = this.add((new BooleanSetting("WaterMark", true, () -> this.page.is(HUD.Page.Element))).setParent());
      this.pulse = this.add((new ColorSetting("Pulse", new Color(79, 0, 0), () -> this.page.is(HUD.Page.Element) && this.waterMark.isOpen())).injectBoolean(true));
      this.waterMarkString = this.add(new StringSetting("Title", "%hackname% %version%", () -> this.page.is(HUD.Page.Element) && this.waterMark.isOpen()));
      this.waterMarkOffset = this.add(new SliderSetting("Offset", (double)1.0F, (double)0.0F, (double)100.0F, (double)-1.0F, () -> this.page.is(HUD.Page.Element) && this.waterMark.isOpen()));
      this.fps = this.add(new BooleanSetting("FPS", true, () -> this.page.is(HUD.Page.Element)));
      this.ping = this.add(new BooleanSetting("Ping", true, () -> this.page.is(HUD.Page.Element)));
      this.tps = this.add(new BooleanSetting("TPS", true, () -> this.page.is(HUD.Page.Element)));
      this.ip = this.add(new BooleanSetting("IP", false, () -> this.page.is(HUD.Page.Element)));
      this.time = this.add(new BooleanSetting("Time", false, () -> this.page.is(HUD.Page.Element)));
      this.speed = this.add(new BooleanSetting("Speed", true, () -> this.page.is(HUD.Page.Element)));
      this.brand = this.add(new BooleanSetting("Brand", false, () -> this.page.is(HUD.Page.Element)));
      this.potions = this.add(new BooleanSetting("Potions", true, () -> this.page.is(HUD.Page.Element)));
      this.coords = this.add((new BooleanSetting("Coords", true, () -> this.page.is(HUD.Page.Element))).setParent());
      this.colorSync = this.add(new BooleanSetting("ColorSync", true, () -> this.page.is(HUD.Page.Element) && this.coords.isOpen()));
      this.color = this.add(new ColorSetting("Color", new Color(208, 0, 0), () -> this.page.is(HUD.Page.Color)));
      this.colorMode = this.add(new EnumSetting("ColorMode", HUD.ColorMode.Pulse, () -> this.page.is(HUD.Page.Color)));
      this.rainbowSpeed = this.add(new SliderSetting("RainbowSpeed", (double)4.0F, (double)1.0F, (double)10.0F, 0.1, () -> this.page.is(HUD.Page.Color) && this.colorMode.getValue() == HUD.ColorMode.Rainbow));
      this.saturation = this.add(new SliderSetting("Saturation", (double)130.0F, (double)1.0F, (double)255.0F, () -> this.page.is(HUD.Page.Color) && this.colorMode.getValue() == HUD.ColorMode.Rainbow));
      this.rainbowDelay = this.add(new SliderSetting("Delay", 350, 0, 1000, () -> this.page.is(HUD.Page.Color) && this.colorMode.getValue() == HUD.ColorMode.Rainbow));
      this.endColor = this.add((new ColorSetting("SecondColor", new Color(255, 0, 0, 255), () -> this.page.is(HUD.Page.Color) && this.colorMode.getValue() == HUD.ColorMode.Pulse)).injectBoolean(true));
      this.pulseSpeed = this.add(new SliderSetting("PulseSpeed", (double)1.0F, (double)0.0F, (double)5.0F, 0.1, () -> this.page.is(HUD.Page.Color)));
      this.pulseCounter = this.add(new SliderSetting("Counter", 10, 1, 50, () -> this.page.is(HUD.Page.Color)));
      this.blur = this.add((new BooleanSetting("Blur", false, () -> this.page.is(HUD.Page.Color))).setParent());
      this.radius = this.add(new SliderSetting("Radius", (double)10.0F, (double)0.0F, (double)100.0F, () -> this.page.is(HUD.Page.Color) && this.blur.isOpen()));
      this.backGround = this.add((new BooleanSetting("BackGround", false, () -> this.page.is(HUD.Page.Color))).setParent());
      this.width = this.add(new SliderSetting("Width", (double)0.0F, (double)0.0F, (double)15.0F, () -> this.page.is(HUD.Page.Color) && this.backGround.isOpen()));
      this.bgColor = this.add(new ColorSetting("BGColor", new Color(0, 0, 0, 100), () -> this.page.is(HUD.Page.Color) && this.backGround.isOpen()));
      this.rect = this.add((new ColorSetting("Rect", new Color(208, 0, 0), () -> this.page.is(HUD.Page.Color))).injectBoolean(false));
      this.glow = this.add((new ColorSetting("Glow", new Color(208, 0, 100), () -> this.page.is(HUD.Page.Color))).injectBoolean(false));
      this.decimal = new DecimalFormat("0.0");
      this.infoList = new ArrayList();
      this.moduleList = new ArrayList();
      this.setChinese("界面");
      INSTANCE = this;
      Alien.EVENT_BUS.subscribe(new InitHandler());

      for(class_1291 potionEffect : class_7923.field_41174) {
         try {
            class_6880<class_1291> effectRegistryEntry = (class_6880)((ISimpleRegistry)class_7923.field_41174).getValueToEntry().get(potionEffect);
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
            }, () -> mc.field_1724.method_6059(effectRegistryEntry) && this.potions.getValue()));
         } catch (Exception var4) {
         }
      }

      ArrayList var10000 = this.infoList;
      Callable var10004 = () -> {
         String var10000 = mc.method_1542() ? "Vanilla" : mc.method_1562().method_52790().replaceAll("\\(.*?\\)", "");
         return "ServerBrand §f" + var10000;
      };
      BooleanSetting var10005 = this.brand;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         String var10000 = mc.method_1542() ? "SinglePlayer" : mc.method_1558().field_3761;
         return "Server §f" + var10000;
      };
      var10005 = this.ip;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         float var10000 = Alien.SERVER.getTPS();
         return "TPS §f" + var10000 + " [" + Alien.SERVER.getCurrentTPS() + "]";
      };
      var10005 = this.tps;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         double x = mc.field_1724.method_23317() - mc.field_1724.field_6014;
         double z = mc.field_1724.method_23321() - mc.field_1724.field_5969;
         double dist = Math.sqrt(x * x + z * z) / (double)1000.0F;
         double div = 1.388888888888889E-5;
         float timer = Alien.TIMER.get();
         double playerSpeed = dist / div * (double)timer;
         return String.format("Speed §f%skm/h", this.decimal.format(playerSpeed));
      };
      var10005 = this.speed;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         SimpleDateFormat var10000 = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
         Date var10001 = new Date();
         return "Time §f" + var10000.format(var10001);
      };
      var10005 = this.time;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> {
         class_640 playerListEntry = mc.method_1562().method_2871(mc.field_1724.method_5667());
         String playerPing;
         if (playerListEntry == null) {
            playerPing = "Unknown";
         } else {
            playerPing = playerListEntry.method_2959() + "ms";
         }

         return "Ping §f" + playerPing;
      };
      var10005 = this.ping;
      Objects.requireNonNull(var10005);
      var10000.add(new Info(var10004, var10005::getValue));
      var10000 = this.infoList;
      var10004 = () -> "FPS §f" + Alien.FPS.getFps();
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

   public void onRender2D(class_332 drawContext, float tickDelta) {
      if (this.waterMark.getValue()) {
         if (this.pulse.booleanValue) {
            TextUtil.drawStringPulse(drawContext, this.waterMarkString.getValue().replaceAll("%version%", "4.0").replaceAll("%hackname%", "Alien"), (double)this.waterMarkOffset.getValueInt(), (double)this.waterMarkOffset.getValueInt(), this.color.getValue(), this.pulse.getValue(), this.pulseSpeed.getValue(), this.pulseCounter.getValueInt(), this.font.getValue(), this.shadow.getValue());
         } else {
            TextUtil.drawString(drawContext, this.waterMarkString.getValue().replaceAll("%version%", "4.0").replaceAll("%hackname%", "Alien"), (double)this.waterMarkOffset.getValueInt(), (double)this.waterMarkOffset.getValueInt(), this.color.getValue().getRGB(), this.font.getValue(), this.shadow.getValue());
         }
      }

      int fontHeight = this.getHeight();
      if (this.coords.getValue()) {
         String coordsString = getCoords();
         this.drawCoord(drawContext, coordsString, mc.method_22683().method_4502() - fontHeight - (mc.field_1755 instanceof class_408 ? 15 : 0));
      }

      HUD.Info.onRender(drawContext, this.infoList, this.renderingUp.getValue());
      if (this.arrayList.getValue()) {
         HUD.Info.onRender(drawContext, this.moduleList, !this.renderingUp.getValue());
      }

   }

   @EventListener(
      priority = -999
   )
   public void onUpdate(ClientTickEvent event) {
      if (!nullCheck() && event.isPost()) {
         HUD.Info.onUpdate(this.infoList, this.sort.getValue());
         if (this.arrayList.getValue()) {
            HUD.Info.onUpdate(this.moduleList, this.listSort.getValue());
         }
      }

   }

   private static String getCoords() {
      boolean inNether = mc.field_1687.method_27983().equals(class_1937.field_25180);
      int posX = mc.field_1724.method_31477();
      int posY = mc.field_1724.method_31478();
      int posZ = mc.field_1724.method_31479();
      float factor = !inNether ? 0.125F : 8.0F;
      int anotherWorldX = (int)(mc.field_1724.method_23317() * (double)factor);
      int anotherWorldZ = (int)(mc.field_1724.method_23321() * (double)factor);
      String var10000 = inNether ? posX + ", " + posY + ", " + posZ + " §7[§f" + anotherWorldX + ", " + anotherWorldZ + "§7]§f" : posX + ", " + posY + ", " + posZ + "§7 [§f" + anotherWorldX + ", " + anotherWorldZ + "§7]";
      return "XYZ §f" + var10000;
   }

   private int getWidth(String s) {
      if (this.lowerCase.getValue()) {
         s = s.toLowerCase();
      }

      return this.font.getValue() ? (int)FontManager.ui.getWidth(s) : mc.field_1772.method_1727(s);
   }

   private int getHeight() {
      return this.font.getValue() ? (int)FontManager.ui.getFontHeight() : 9;
   }

   private void drawCoord(class_332 drawContext, String s, int y) {
      if (this.colorSync.getValue()) {
         if (this.lowerCase.getValue()) {
            s = s.toLowerCase();
         }

         TextUtil.drawString(drawContext, s, (double)2.0F, (double)y, this.getColor((double)20.0F), this.font.getValue(), this.shadow.getValue());
      } else if (this.pulse.booleanValue) {
         TextUtil.drawStringPulse(drawContext, s, (double)2.0F, (double)y, this.color.getValue(), this.pulse.getValue(), this.pulseSpeed.getValue(), this.pulseCounter.getValueInt(), this.font.getValue(), this.shadow.getValue());
      } else {
         TextUtil.drawString(drawContext, s, (double)2.0F, (double)y, this.color.getValue().getRGB(), this.font.getValue(), this.shadow.getValue());
      }

   }

   public int getColor(double counter) {
      return this.colorMode.getValue() != HUD.ColorMode.Custom ? this.rainbow(counter).getRGB() : this.color.getValue().getRGB();
   }

   private Color rainbow(double delay) {
      if (this.colorMode.getValue() == HUD.ColorMode.Pulse) {
         return this.endColor.booleanValue ? ColorUtil.pulseColor(this.color.getValue(), this.endColor.getValue(), delay, this.pulseCounter.getValueInt(), this.pulseSpeed.getValue()) : ColorUtil.pulseColor(this.color.getValue(), delay, this.pulseCounter.getValueInt(), this.pulseSpeed.getValue());
      } else if (this.colorMode.getValue() == HUD.ColorMode.Rainbow) {
         double rainbowState = Math.ceil(((double)System.currentTimeMillis() * this.rainbowSpeed.getValue() + delay * this.rainbowDelay.getValue()) / (double)20.0F);
         return Color.getHSBColor((float)(rainbowState % (double)360.0F / (double)360.0F), this.saturation.getValueFloat() / 255.0F, 1.0F);
      } else {
         return this.color.getValue();
      }
   }

   private int getFontHeight() {
      return this.font.getValue() ? (int)FontManager.ui.getFontHeight() : 9;
   }

   private static enum ColorMode {
      Custom,
      Pulse,
      Rainbow;

      // $FF: synthetic method
      private static ColorMode[] $values() {
         return new ColorMode[]{Custom, Pulse, Rainbow};
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
      static double fontHeight;
      static double currentY;
      static int windowWidth;
      static boolean fromUp;
      static double counter;

      public Info(Callable<String> info, BooleanSupplier drawn) {
         this.info = info;
         this.drawn = drawn;

         try {
            this.string = (String)this.info.call();
         } catch (Exception var5) {
         }

      }

      public static void onRender(class_332 context, List<Info> list, boolean fromUp) {
         counter = (double)20.0F;
         HUD.Info.fromUp = fromUp;
         fontHeight = (double)HUD.INSTANCE.getFontHeight();
         currentY = fromUp ? (double)1.0F + HUD.INSTANCE.yOffset.getValue() : (double)Wrapper.mc.method_22683().method_4502() - fontHeight - (double)1.0F - (Wrapper.mc.field_1755 instanceof class_408 && HUD.INSTANCE.yOffset.getValue() < (double)12.0F ? (double)12.0F - HUD.INSTANCE.yOffset.getValue() + HUD.INSTANCE.interval.getValue() / (double)2.0F : (double)0.0F) - HUD.INSTANCE.yOffset.getValue();
         windowWidth = Wrapper.mc.method_22683().method_4486();

         for(Info s : list) {
            s.draw(context);
         }

      }

      public static void onUpdate(List<Info> list, boolean sort) {
         for(Info s : list) {
            s.onUpdate();
         }

         if (sort) {
            list.sort(Comparator.comparingInt((info) -> info.string == null ? 0 : -HUD.INSTANCE.getWidth(info.string)));
         }

      }

      public void onUpdate() {
         this.isOn = this.drawn.getAsBoolean();
         if (this.isOn) {
            try {
               this.string = HUD.this.lowerCase.getValue() ? ((String)this.info.call()).toLowerCase() : (String)this.info.call();
            } catch (Exception var2) {
               var2.printStackTrace();
            }
         }

      }

      public void draw(class_332 context) {
         if (this.currentX > (double)0.0F || this.isOn) {
            this.currentX = this.animation.get(this.isOn ? (double)(HUD.this.getWidth(this.string) + 1) : (double)0.0F, this.isOn ? (long)HUD.this.enableLength.getValueInt() : (long)HUD.this.disableLength.getValueInt(), HUD.this.easing.getValue());
            double width = this.currentX + (double)HUD.this.xOffset.getValueFloat();
            double fade = this.fadeAnimation.get(this.isOn ? (double)1.0F : (double)0.0F, (long)HUD.this.fadeLength.getValueInt(), HUD.this.easing.getValue());
            if (fade > 0.04) {
               counter += fromUp ? fade : -fade;
               int c = ColorUtil.injectAlpha(HUD.this.getColor(counter), (int)((double)HUD.this.color.getValue().getAlpha() * fade));
               if (HUD.this.blur.getValue()) {
                  Alien.BLUR.applyBlur((float)(HUD.this.radius.getValue() * fade), (float)((double)windowWidth - width - (double)(HUD.this.width.getValueFloat() / 2.0F)), (float)currentY - 1.0F - HUD.this.interval.getValueFloat() / 2.0F, (float)width + HUD.this.width.getValueFloat() - HUD.this.xOffset.getValueFloat(), (float)fontHeight + HUD.this.interval.getValueFloat());
               }

               if (HUD.this.backGround.getValue()) {
                  Render2DUtil.drawRect(context.method_51448(), (float)((double)windowWidth - width - (double)(HUD.this.width.getValueFloat() / 2.0F)), (float)currentY - 1.0F - HUD.this.interval.getValueFloat() / 2.0F, (float)width + HUD.this.width.getValueFloat() - HUD.this.xOffset.getValueFloat(), (float)fontHeight + HUD.this.interval.getValueFloat(), ColorUtil.injectAlpha(HUD.this.bgColor.sync ? c : HUD.this.bgColor.getValue().getRGB(), (int)((double)HUD.this.bgColor.getValue().getAlpha() * fade)));
               }

               if (HUD.this.glow.booleanValue) {
                  Render2DUtil.drawGlow(context.method_51448(), (float)((double)windowWidth - width - (double)(HUD.this.width.getValueFloat() / 2.0F)), (float)currentY - 1.0F - HUD.this.interval.getValueFloat() / 2.0F, (float)width + HUD.this.width.getValueFloat() - HUD.this.xOffset.getValueFloat(), (float)fontHeight + HUD.this.interval.getValueFloat(), ColorUtil.injectAlpha(HUD.this.glow.sync ? c : HUD.this.glow.getValue().getRGB(), (int)((double)HUD.this.glow.getValue().getAlpha() * fade)));
               }

               TextUtil.drawString(context, this.string, (double)windowWidth - width, currentY + (double)HUD.this.textOffset.getValueFloat(), c, HUD.this.font.getValue(), HUD.this.shadow.getValue());
               if (HUD.this.rect.booleanValue) {
                  Render2DUtil.drawRect(context.method_51448(), (float)windowWidth + HUD.this.width.getValueFloat() / 2.0F - HUD.this.xOffset.getValueFloat(), (float)currentY - 1.0F - HUD.this.interval.getValueFloat() / 2.0F, 1.0F, (float)fontHeight + HUD.this.interval.getValueFloat(), HUD.this.rect.sync ? c : ColorUtil.injectAlpha(HUD.this.rect.getValue(), (int)((double)HUD.this.rect.getValue().getAlpha() * fade)).getRGB());
               }

               currentY += fromUp ? (fontHeight + HUD.this.interval.getValue()) * fade : -(fontHeight + HUD.this.interval.getValue()) * fade;
            }
         }

      }
   }

   public class InitHandler {
      @EventListener
      public void onInit(InitEvent event) {
         for(Module module : Alien.MODULE.getModules()) {
            ArrayList var10000 = HUD.this.moduleList;
            HUD var10003 = HUD.this;
            Objects.requireNonNull(var10003);
            Objects.requireNonNull(module);
            var10000.add(var10003.new Info(module::getArrayName, () -> module.isOn() && module.drawn.getValue()));
         }

         Alien.EVENT_BUS.unsubscribe(this);
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
}
