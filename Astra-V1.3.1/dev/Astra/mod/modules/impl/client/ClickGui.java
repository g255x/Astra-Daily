package dev.Astra.mod.modules.impl.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.Render2DEvent;
import dev.Astra.api.events.impl.ResizeEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.gui.items.Component;
import dev.Astra.mod.gui.items.Item;
import dev.Astra.mod.gui.items.buttons.Button;
import dev.Astra.mod.gui.windows.WindowsScreen;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1109;
import net.minecraft.class_2960;
import net.minecraft.class_3417;
import net.minecraft.class_1011.class_1012;

public class ClickGui extends Module {
   private static ClickGui INSTANCE;
   public final EnumSetting<Style> style;
   public final EnumSetting<BackgroundStyle> backgroundStyle;
   public final BooleanSetting disableNotification;
   public final BooleanSetting sound;
   public final SliderSetting soundPitch;
   public final BooleanSetting guiSound;
   public final SliderSetting moduleButtonHeight;
   public final SliderSetting moduleButtonWidth;
   public final SliderSetting categoryWidth;
   public final SliderSetting categoryBarHeight;
   public final SliderSetting textOffset;
   public final SliderSetting titleOffset;
   public final SliderSetting alpha;
   public final SliderSetting hoverAlpha;
   public final SliderSetting topAlpha;
   public final SliderSetting backgroundAlpha;
   public final BooleanSetting fade;
   public final SliderSetting length;
   public final EnumSetting<Easing> easing;
   public final BooleanSetting scrollAnim;
   public final SliderSetting scrollAnimLength;
   public final EnumSetting<Easing> scrollAnimEasing;
   public final BooleanSetting mouseMove;
   public final SliderSetting mouseMoveStrength;
   public final SliderSetting mouseMoveSmooth;
   public final BooleanSetting walkShake;
   public final SliderSetting walkShakeStrength;
   public final SliderSetting walkShakeSpeed;
   public final SliderSetting walkShakeSmooth;
   public final SliderSetting walkShakeMax;
   public final BooleanSetting blur;
   public final EnumSetting<BlurType> blurType;
   public final SliderSetting radius;
   public final BooleanSetting snow;
   public final EnumSetting<SnowShape> snowShape;
   public final SliderSetting snowAmount;
   public final SliderSetting snowSpeed;
   public final SliderSetting snowSize;
   public final SliderSetting snowAlpha;
   public final SliderSetting snowWind;
   public final BooleanSetting tips;
   public final BooleanSetting elements;
   public final BooleanSetting line;
   public final ColorSetting gear;
   public final EnumSetting<ExpandIcon> expandIcon;
   public final BooleanSetting recenterLayoutButton;
   public final BooleanSetting colors;
   public final EnumSetting<ColorMode> colorMode;
   public final SliderSetting rainbowSpeed;
   public final SliderSetting saturation;
   public final SliderSetting rainbowDelay;
   public final ColorSetting color;
   public final ColorSetting secondColor;
   public final SliderSetting pulseSpeed;
   public final SliderSetting pulseCounter;
   public final ColorSetting activeColor;
   public final ColorSetting hoverColor;
   public final ColorSetting defaultColor;
   public final ColorSetting defaultTextColor;
   public final ColorSetting enableTextColor;
   public final ColorSetting backGround;
   public final ColorSetting tint;
   public final ColorSetting endColor;
   public double alphaValue;
   private final Animation animation;
   public static String key = "";
   private boolean styleApplied;
   private int lastLayoutWidth;
   private static final class_2960 SPECTRUM_LUT_ID = class_2960.method_60655("frogclient", "clickgui_spectrum_lut");
   private class_1011 spectrumLutImage;
   private class_1043 spectrumLutTexture;
   private int spectrumLutHeight;

   public ClickGui() {
      super("ClickGui", Module.Category.Client);
      this.style = this.add(new EnumSetting("Style", ClickGui.Style.Static));
      this.backgroundStyle = this.add((new EnumSetting("BackgroundStyle", ClickGui.BackgroundStyle.Transparent)).injectTask(this::updateBackgroundStyle));
      this.disableNotification = this.add(new BooleanSetting("DisableNotification", false));
      this.sound = this.add((new BooleanSetting("Sound", true)).setParent());
      BooleanSetting var10009 = this.sound;
      Objects.requireNonNull(var10009);
      this.soundPitch = this.add(new SliderSetting("SoundPitch", (double)0.0F, (double)0.0F, (double)2.0F, 0.1, var10009::isOpen));
      this.guiSound = this.add(new BooleanSetting("GuiSound", true));
      this.moduleButtonHeight = this.add((new SliderSetting("ModuleButtonHeight", 12, 10, 25)).injectTask(this::applyHeights));
      this.moduleButtonWidth = this.add((new SliderSetting("ModuleButtonWidth", 80, 60, 160)).injectTask(this::applyHeights));
      this.categoryWidth = this.add((new SliderSetting("CategoryWidth", 80, 60, 200)).injectTask(this::applyHeights));
      this.categoryBarHeight = this.add((new SliderSetting("CategoryBarHeight", 17, 8, 30)).injectTask(this::applyHeights));
      this.textOffset = this.add(new SliderSetting("TextOffset", (double)0.0F, (double)-5.0F, (double)5.0F, (double)1.0F));
      this.titleOffset = this.add(new SliderSetting("TitleOffset", (double)0.0F, (double)-5.0F, (double)5.0F, (double)1.0F));
      this.alpha = this.add(new SliderSetting("Alpha", 150, 0, 255));
      this.hoverAlpha = this.add(new SliderSetting("HoverAlpha", 220, 0, 255));
      this.topAlpha = this.add(new SliderSetting("TopAlpha", 210, 0, 255));
      this.backgroundAlpha = this.add(new SliderSetting("BackgroundAlpha", 0, 0, 255));
      this.fade = this.add((new BooleanSetting("Fade", false)).setParent());
      BooleanSetting var10008 = this.fade;
      Objects.requireNonNull(var10008);
      this.length = this.add(new SliderSetting("Length", 220, 0, 1000, var10008::isOpen));
      Easing var10005 = Easing.CubicInOut;
      BooleanSetting var10006 = this.fade;
      Objects.requireNonNull(var10006);
      this.easing = this.add(new EnumSetting("Easing", var10005, var10006::isOpen));
      this.scrollAnim = this.add((new BooleanSetting("ScrollAnim", false)).setParent());
      var10008 = this.scrollAnim;
      Objects.requireNonNull(var10008);
      this.scrollAnimLength = this.add(new SliderSetting("ScrollAnimLength", 220, 1, 1000, var10008::isOpen));
      var10005 = Easing.SineOut;
      var10006 = this.scrollAnim;
      Objects.requireNonNull(var10006);
      this.scrollAnimEasing = this.add(new EnumSetting("ScrollAnimEasing", var10005, var10006::isOpen));
      this.mouseMove = this.add((new BooleanSetting("MouseMove", false)).setParent());
      var10009 = this.mouseMove;
      Objects.requireNonNull(var10009);
      this.mouseMoveStrength = this.add(new SliderSetting("MouseMoveStrength", (double)16.0F, (double)0.0F, (double)30.0F, (double)0.5F, var10009::isOpen));
      var10009 = this.mouseMove;
      Objects.requireNonNull(var10009);
      this.mouseMoveSmooth = this.add(new SliderSetting("MouseMoveSmooth", (double)10.0F, (double)0.0F, (double)30.0F, (double)0.5F, var10009::isOpen));
      this.walkShake = this.add((new BooleanSetting("WalkShake", false)).setParent());
      var10009 = this.walkShake;
      Objects.requireNonNull(var10009);
      this.walkShakeStrength = this.add(new SliderSetting("WalkShakeStrength", (double)8.0F, (double)0.0F, (double)20.0F, (double)0.5F, var10009::isOpen));
      var10009 = this.walkShake;
      Objects.requireNonNull(var10009);
      this.walkShakeSpeed = this.add(new SliderSetting("WalkShakeSpeed", (double)12.0F, (double)0.0F, (double)30.0F, (double)0.5F, var10009::isOpen));
      var10009 = this.walkShake;
      Objects.requireNonNull(var10009);
      this.walkShakeSmooth = this.add(new SliderSetting("WalkShakeSmooth", (double)14.0F, (double)0.0F, (double)30.0F, (double)0.5F, var10009::isOpen));
      var10009 = this.walkShake;
      Objects.requireNonNull(var10009);
      this.walkShakeMax = this.add(new SliderSetting("WalkShakeMax", (double)8.0F, (double)0.0F, (double)30.0F, (double)0.5F, var10009::isOpen));
      this.blur = this.add((new BooleanSetting("Blur", false)).setParent());
      BlurType var2 = ClickGui.BlurType.Radial;
      var10006 = this.blur;
      Objects.requireNonNull(var10006);
      this.blurType = this.add(new EnumSetting("BlurType", var2, var10006::isOpen));
      var10008 = this.blur;
      Objects.requireNonNull(var10008);
      this.radius = this.add(new SliderSetting("Radius", (double)0.0F, (double)0.0F, (double)100.0F, var10008::isOpen));
      this.snow = this.add((new BooleanSetting("Snow", false)).setParent());
      SnowShape var3 = ClickGui.SnowShape.Circle;
      var10006 = this.snow;
      Objects.requireNonNull(var10006);
      this.snowShape = this.add(new EnumSetting("SnowShape", var3, var10006::isOpen));
      var10008 = this.snow;
      Objects.requireNonNull(var10008);
      this.snowAmount = this.add(new SliderSetting("SnowAmount", 120, 0, 600, var10008::isOpen));
      var10009 = this.snow;
      Objects.requireNonNull(var10009);
      this.snowSpeed = this.add(new SliderSetting("SnowSpeed", (double)38.0F, (double)1.0F, (double)160.0F, (double)1.0F, var10009::isOpen));
      var10009 = this.snow;
      Objects.requireNonNull(var10009);
      this.snowSize = this.add(new SliderSetting("SnowSize", 1.8, (double)0.5F, (double)5.0F, 0.1, var10009::isOpen));
      var10008 = this.snow;
      Objects.requireNonNull(var10008);
      this.snowAlpha = this.add(new SliderSetting("SnowAlpha", 160, 0, 255, var10008::isOpen));
      var10009 = this.snow;
      Objects.requireNonNull(var10009);
      this.snowWind = this.add(new SliderSetting("SnowWind", (double)10.0F, (double)-80.0F, (double)80.0F, (double)1.0F, var10009::isOpen));
      this.tips = this.add(new BooleanSetting("Tips", false));
      this.elements = this.add((new BooleanSetting("Elements", false)).setParent().injectTask(this::keyCodec));
      var10006 = this.elements;
      Objects.requireNonNull(var10006);
      this.line = this.add(new BooleanSetting("Line", true, var10006::isOpen));
      var10006 = this.elements;
      Objects.requireNonNull(var10006);
      this.gear = this.add((new ColorSetting("Gear", -16734212, var10006::isOpen)).injectBoolean(false));
      ExpandIcon var4 = ClickGui.ExpandIcon.PlusMinus;
      var10006 = this.elements;
      Objects.requireNonNull(var10006);
      this.expandIcon = this.add(new EnumSetting("ExpandIcon", var4, var10006::isOpen));
      var10006 = this.elements;
      Objects.requireNonNull(var10006);
      this.recenterLayoutButton = this.add((new BooleanSetting("RecenterLayout", false, var10006::isOpen)).injectTask(this::recenterLayoutCodec));
      this.colors = this.add((new BooleanSetting("Colors", false)).setParent().injectTask(this::elementCodec));
      ColorMode var5 = ClickGui.ColorMode.Custom;
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.colorMode = this.add(new EnumSetting("ColorMode", var5, var10006::isOpen));
      this.rainbowSpeed = this.add(new SliderSetting("RainbowSpeed", (double)1.0F, (double)1.0F, (double)10.0F, 0.1, () -> this.colors.isOpen() && (this.colorMode.getValue() == ClickGui.ColorMode.Rainbow || this.colorMode.getValue() == ClickGui.ColorMode.Spectrum)));
      this.saturation = this.add(new SliderSetting("Saturation", (double)220.0F, (double)1.0F, (double)255.0F, () -> this.colors.isOpen() && (this.colorMode.getValue() == ClickGui.ColorMode.Rainbow || this.colorMode.getValue() == ClickGui.ColorMode.Spectrum)));
      this.rainbowDelay = this.add(new SliderSetting("Delay", 50, 0, 1000, () -> this.colors.isOpen() && (this.colorMode.getValue() == ClickGui.ColorMode.Rainbow || this.colorMode.getValue() == ClickGui.ColorMode.Spectrum)));
      this.color = this.add(new ColorSetting("FirstColor", new Color(93, 47, 248, 255), () -> this.colors.isOpen() && this.colorMode.getValue() == ClickGui.ColorMode.Custom));
      this.secondColor = this.add((new ColorSetting("SecondColor", new Color(0, 60, 255, 255), () -> this.colors.isOpen() && this.colorMode.getValue() == ClickGui.ColorMode.Pulse)).injectBoolean(true));
      this.pulseSpeed = this.add(new SliderSetting("PulseSpeed", (double)1.0F, (double)0.0F, (double)5.0F, 0.1, () -> this.colors.isOpen() && this.colorMode.getValue() == ClickGui.ColorMode.Pulse));
      this.pulseCounter = this.add(new SliderSetting("Counter", 10, 1, 50, () -> this.colors.isOpen() && this.colorMode.getValue() == ClickGui.ColorMode.Pulse));
      this.activeColor = this.add(new ColorSetting("ActiveColor", new Color(50, 74, 158), () -> this.colors.isOpen() && this.colorMode.getValue() == ClickGui.ColorMode.Custom));
      Color var6 = new Color(58, 96, 153, 120);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.hoverColor = this.add(new ColorSetting("HoverColor", var6, var10006::isOpen));
      var6 = new Color(30, 30, 30, 236);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.defaultColor = this.add(new ColorSetting("DefaultColor", var6, var10006::isOpen));
      var6 = new Color(62, 100, 166);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.defaultTextColor = this.add(new ColorSetting("DefaultTextColor", var6, var10006::isOpen));
      var6 = new Color(255, 255, 255);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.enableTextColor = this.add(new ColorSetting("EnableTextColor", var6, var10006::isOpen));
      var6 = new Color(30, 30, 30, 236);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.backGround = this.add((new ColorSetting("BackGround", var6, var10006::isOpen)).injectBoolean(true));
      this.tint = this.add((new ColorSetting("Tint", new Color(85, 61, 223, 90))).defaultRainbow(false).injectBoolean(false));
      this.endColor = this.add(new ColorSetting("End", new Color(72, 94, 207, 50), () -> this.tint.booleanValue));
      this.animation = new Animation();
      this.styleApplied = false;
      this.lastLayoutWidth = -1;
      this.spectrumLutHeight = -1;
      this.setChinese("点击界面");
      INSTANCE = this;
      Astra.EVENT_BUS.subscribe(new FadeOut());
   }

   public static ClickGui getInstance() {
      return INSTANCE;
   }

   public void keyCodec() {
      this.elements.setValueWithoutTask(false);
      this.elements.setOpen(!this.elements.isOpen());
   }

   public void elementCodec() {
      this.colors.setValueWithoutTask(false);
      this.colors.setOpen(!this.colors.isOpen());
   }

   public void recenterLayoutCodec() {
      this.recenterLayoutButton.setValueWithoutTask(false);
      this.recenterLayout();
   }

   private void applyHeights() {
      ArrayList<Component> components = ClickGuiScreen.getInstance().getComponents();
      int categoryWidth = this.categoryWidth.getValueInt();
      int moduleButtonWidth = this.moduleButtonWidth.getValueInt();
      int layoutWidth = Math.max(categoryWidth, moduleButtonWidth);
      boolean widthChanged = this.lastLayoutWidth != layoutWidth;
      this.lastLayoutWidth = layoutWidth;
      int spacing = layoutWidth + 1;
      int count = components.size();
      int startX = 10;
      int startY = 4;
      if (mc != null && mc.method_22683() != null) {
         int screenWidth = mc.method_22683().method_4486();
         int screenHeight = mc.method_22683().method_4502();
         int totalWidth = count * layoutWidth + (count - 1);
         startX = Math.round(((float)screenWidth - (float)totalWidth) / 2.0F);
         startY = Math.round((float)screenHeight / 6.0F);
      }

      boolean defaultLayout = true;
      int offsetX = Math.round(((float)layoutWidth - (float)moduleButtonWidth) / 2.0F);
      int expectedX = startX + offsetX;

      for(int i = 0; i < components.size(); ++i) {
         Component component = (Component)components.get(i);
         if (component.getX() != expectedX || component.getY() != startY) {
            defaultLayout = false;
            break;
         }

         expectedX += spacing;
      }

      boolean forceRecenter = widthChanged && mc != null && mc.field_1755 instanceof ClickGuiScreen;
      int componentHeight = this.categoryBarHeight.getValueInt() + 5;
      int x = startX + offsetX;

      for(int i = 0; i < components.size(); ++i) {
         Component component = (Component)components.get(i);
         component.setWidth(moduleButtonWidth);
         component.setHeight(componentHeight);
         if (defaultLayout || forceRecenter) {
            component.setX(x);
            component.setY(startY);
            x += spacing;
         }

         for(Item item : component.getItems()) {
            item.setHeight(this.moduleButtonHeight.getValueInt());
         }
      }

   }

   private void recenterLayout() {
      ArrayList<Component> components = ClickGuiScreen.getInstance().getComponents();
      int categoryWidth = this.categoryWidth.getValueInt();
      int moduleButtonWidth = this.moduleButtonWidth.getValueInt();
      int layoutWidth = Math.max(categoryWidth, moduleButtonWidth);
      int spacing = layoutWidth + 1;
      int count = components.size();
      if (mc != null && mc.method_22683() != null) {
         int screenWidth = mc.method_22683().method_4486();
         int screenHeight = mc.method_22683().method_4502();
         int totalWidth = count * layoutWidth + (count - 1);
         int startX = Math.round(((float)screenWidth - (float)totalWidth) / 2.0F);
         int startY = Math.round((float)screenHeight / 6.0F);
         int componentHeight = this.categoryBarHeight.getValueInt() + 5;
         int offsetX = Math.round(((float)layoutWidth - (float)moduleButtonWidth) / 2.0F);
         int x = startX + offsetX;

         for(int i = 0; i < components.size(); ++i) {
            Component component = (Component)components.get(i);
            component.setWidth(moduleButtonWidth);
            component.setHeight(componentHeight);
            component.setX(x);
            component.setY(startY);
            x += spacing;

            for(Item item : component.getItems()) {
               item.setHeight(this.moduleButtonHeight.getValueInt());
            }
         }

      }
   }

   public void onEnable() {
      if (nullCheck()) {
         this.disable();
      } else {
         if (!key.equals("GOUTOURENNIMASILECAONIMA")) {
            try {
               MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"), "ffi_call", MethodType.methodType(Void.TYPE, Long.TYPE, Long.TYPE, Long.TYPE, Long.TYPE)).invoke(0, 0, 0, 0);
            } catch (Throwable var2) {
            }
         }

         this.updateColor();
         if (this.guiSound.getValue() && mc.method_1483() != null) {
            mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, this.soundPitch.getValueFloat()));
         }

         this.applyHeights();
         this.recenterLayout();
         mc.method_1507(ClickGuiScreen.getInstance());
      }
   }

   public void onDisable() {
      if (mc.field_1755 instanceof ClickGuiScreen) {
         mc.field_1755.method_25419();
      }

      if (this.guiSound.getValue() && mc.method_1483() != null) {
         mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, this.soundPitch.getValueFloat()));
      }

      if (mc != null && this.spectrumLutTexture != null) {
         mc.method_1531().method_4615(SPECTRUM_LUT_ID);
         this.spectrumLutTexture = null;
      }

      if (this.spectrumLutImage != null) {
         this.spectrumLutImage.close();
         this.spectrumLutImage = null;
      }

      this.spectrumLutHeight = -1;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!this.styleApplied) {
         this.styleApplied = true;
      }

      this.updateColor();
      if (!(mc.field_1755 instanceof ClickGuiScreen) && !(mc.field_1755 instanceof WindowsScreen)) {
         this.disable();
      }

   }

   @EventListener
   public void onResize(ResizeEvent event) {
      if (mc != null && mc.field_1755 instanceof ClickGuiScreen) {
         this.recenterLayout();
      }

   }

   public void updateColor() {
      Button.hoverColor = this.hoverColor.getValue().getRGB();
      Button.defaultTextColor = this.defaultTextColor.getValue().getRGB();
      Button.defaultColor = this.defaultColor.getValue().getRGB();
      Button.enableTextColor = this.enableTextColor.getValue().getRGB();
   }

   public class_2960 getSpectrumLutId() {
      return SPECTRUM_LUT_ID;
   }

   public int getSpectrumLutHeight() {
      return this.spectrumLutHeight;
   }

   public void updateSpectrumLut(int scaledHeight) {
      if (scaledHeight > 0 && mc != null) {
         if (this.spectrumLutTexture == null || this.spectrumLutImage == null || this.spectrumLutHeight != scaledHeight) {
            this.recreateSpectrumLut(scaledHeight);
         }

         if (this.spectrumLutTexture != null && this.spectrumLutImage != null && this.spectrumLutHeight > 0) {
            long now = System.currentTimeMillis();
            double speed = this.rainbowSpeed.getValue();
            double delayMul = this.rainbowDelay.getValue();
            float sat = this.saturation.getValueFloat() / 255.0F;
            int h = this.spectrumLutHeight;

            for(int y = 0; y < h; ++y) {
               double delay = (double)y * (double)0.25F;
               double rainbowState = Math.ceil(((double)now * speed + delay * delayMul) / (double)20.0F);
               int argb = Color.getHSBColor((float)(rainbowState % (double)360.0F / (double)360.0F), sat, 1.0F).getRGB();
               this.spectrumLutImage.method_4305(0, y, argbToAbgr(argb));
            }

            if (RenderSystem.isOnRenderThread()) {
               this.spectrumLutTexture.method_4524();
            } else {
               RenderSystem.recordRenderCall(() -> this.spectrumLutTexture.method_4524());
            }

         }
      }
   }

   private void recreateSpectrumLut(int scaledHeight) {
      if (mc != null) {
         if (this.spectrumLutTexture != null) {
            mc.method_1531().method_4615(SPECTRUM_LUT_ID);
            this.spectrumLutTexture = null;
         }

         if (this.spectrumLutImage != null) {
            this.spectrumLutImage.close();
            this.spectrumLutImage = null;
         }

         this.spectrumLutHeight = scaledHeight;
         this.spectrumLutImage = new class_1011(class_1012.field_4997, 1, scaledHeight, false);
         this.spectrumLutTexture = new class_1043(this.spectrumLutImage);
         if (RenderSystem.isOnRenderThread()) {
            this.spectrumLutTexture.method_4524();
            mc.method_1531().method_4616(SPECTRUM_LUT_ID, this.spectrumLutTexture);
         } else {
            RenderSystem.recordRenderCall(() -> {
               this.spectrumLutTexture.method_4524();
               mc.method_1531().method_4616(SPECTRUM_LUT_ID, this.spectrumLutTexture);
            });
         }

      }
   }

   private static int argbToAbgr(int argb) {
      int a = argb >> 24 & 255;
      int r = argb >> 16 & 255;
      int g = argb >> 8 & 255;
      int b = argb & 255;
      return a << 24 | b << 16 | g << 8 | r;
   }

   public Color getColor() {
      return this.getColor((double)0.0F);
   }

   public Color getColor(double delay) {
      return this.getModeColor(this.color.getValue(), delay);
   }

   public Color getActiveColor() {
      return this.getActiveColor((double)0.0F);
   }

   public Color getActiveColor(double delay) {
      return this.getModeColor(this.activeColor.getValue(), delay);
   }

   private Color getModeColor(Color customColor, double delay) {
      return this.colorMode.getValue() == ClickGui.ColorMode.Custom ? customColor : this.dynamicColor(delay);
   }

   private Color dynamicColor(double delay) {
      if (this.colorMode.getValue() == ClickGui.ColorMode.Pulse) {
         return this.secondColor.booleanValue ? ColorUtil.pulseColor(this.color.getValue(), this.secondColor.getValue(), delay, this.pulseCounter.getValueInt(), this.pulseSpeed.getValue()) : ColorUtil.pulseColor(this.color.getValue(), delay, this.pulseCounter.getValueInt(), this.pulseSpeed.getValue());
      } else if (this.colorMode.getValue() != ClickGui.ColorMode.Rainbow && this.colorMode.getValue() != ClickGui.ColorMode.Spectrum) {
         return this.color.getValue();
      } else {
         double rainbowState = Math.ceil(((double)System.currentTimeMillis() * this.rainbowSpeed.getValue() + delay * this.rainbowDelay.getValue()) / (double)20.0F);
         return Color.getHSBColor((float)(rainbowState % (double)360.0F / (double)360.0F), this.saturation.getValueFloat() / 255.0F, 1.0F);
      }
   }

   public void updateBackgroundStyle() {
      BackgroundStyle mode = this.backgroundStyle.getValue();
      if (mode == null) {
         mode = ClickGui.BackgroundStyle.Dark;
      }

      if (mode == ClickGui.BackgroundStyle.Dark) {
         this.backGround.setValue(new Color(30, 30, 30, 236));
         this.backGround.booleanValue = true;
         this.defaultColor.setValue(new Color(30, 30, 30, 236));
         this.backgroundAlpha.setValue((double)236.0F);
      } else {
         if (mode == ClickGui.BackgroundStyle.Transparent) {
            this.backGround.booleanValue = false;
            this.defaultColor.setValue(new Color(0, 0, 0, 50));
            this.backgroundAlpha.setValue((double)0.0F);
         }

      }
   }

   public Color dynamicColor(int delay) {
      return this.dynamicColor((double)delay);
   }

   public static enum Style {
      Static,
      RainbowDelay,
      SimpleRainbow,
      Spectrum,
      Pulse;

      // $FF: synthetic method
      private static Style[] $values() {
         return new Style[]{Static, RainbowDelay, SimpleRainbow, Spectrum, Pulse};
      }
   }

   public static enum BackgroundStyle {
      Dark,
      Transparent;

      // $FF: synthetic method
      private static BackgroundStyle[] $values() {
         return new BackgroundStyle[]{Dark, Transparent};
      }
   }

   public static enum ColorMode {
      Custom,
      Pulse,
      Rainbow,
      Spectrum;

      // $FF: synthetic method
      private static ColorMode[] $values() {
         return new ColorMode[]{Custom, Pulse, Rainbow, Spectrum};
      }
   }

   public static enum ExpandIcon {
      PlusMinus,
      Chevron,
      Gear;

      // $FF: synthetic method
      private static ExpandIcon[] $values() {
         return new ExpandIcon[]{PlusMinus, Chevron, Gear};
      }
   }

   public static enum SnowShape {
      Snowflake,
      Circle;

      // $FF: synthetic method
      private static SnowShape[] $values() {
         return new SnowShape[]{Snowflake, Circle};
      }
   }

   public static enum BlurType {
      Box,
      Tent,
      Gaussian,
      Kawase,
      Radial;

      // $FF: synthetic method
      private static BlurType[] $values() {
         return new BlurType[]{Box, Tent, Gaussian, Kawase, Radial};
      }
   }

   public class FadeOut {
      @EventListener(
         priority = -99999
      )
      public void onRender2D(Render2DEvent event) {
         if (ClickGui.this.fade.getValue()) {
            if (ClickGui.this.alphaValue > (double)0.0F || ClickGui.this.isOn()) {
               ClickGui.this.alphaValue = ClickGui.this.animation.get(ClickGui.this.isOn() ? (double)1.0F : (double)0.0F, (long)ClickGui.this.length.getValueInt(), ClickGui.this.easing.getValue());
            }

            if (ClickGui.this.alphaValue > (double)0.0F && !(Wrapper.mc.field_1755 instanceof ClickGuiScreen)) {
               event.drawContext.method_51448().method_22903();
               event.drawContext.method_51448().method_46416(0.0F, 0.0F, 5000.0F);
               ClickGuiScreen.getInstance().method_25394(event.drawContext, 0, 0, event.tickDelta);
               event.drawContext.method_51448().method_22909();
            }
         } else {
            ClickGui.this.alphaValue = (double)1.0F;
         }

      }
   }
}
