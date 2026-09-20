package dev.luminous.mod.modules.impl.client;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render2DEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.Animation;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.mod.gui.ClickGuiScreen;
import dev.luminous.mod.gui.items.Component;
import dev.luminous.mod.gui.items.Item;
import dev.luminous.mod.gui.items.buttons.Button;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_1109;
import net.minecraft.class_3417;

public class ClickGui extends Module {
   private static ClickGui INSTANCE;
   public final BooleanSetting autoSave = this.add(new BooleanSetting("AutoSave", true));
   public final BooleanSetting font = this.add(new BooleanSetting("Font", true));
   public final BooleanSetting shadow = this.add(new BooleanSetting("Shadow", true));
   public final BooleanSetting disableNotification = this.add(new BooleanSetting("DisableNotification", false));
   public final BooleanSetting sound = this.add((new BooleanSetting("Sound", true)).setParent());
   public final BooleanSetting guiSound = this.add(new BooleanSetting("GuiSound", true));
   public final SliderSetting soundPitch;
   public final SliderSetting height;
   public final SliderSetting textOffset;
   public final SliderSetting titleOffset;
   public final SliderSetting alpha;
   public final SliderSetting hoverAlpha;
   public final SliderSetting topAlpha;
   public final BooleanSetting fade;
   public final SliderSetting length;
   public final EnumSetting<Easing> easing;
   public final BooleanSetting blur;
   public final SliderSetting radius;
   public final BooleanSetting elements;
   public final BooleanSetting line;
   public final ColorSetting gear;
   public final BooleanSetting verticalGradient;
   public final BooleanSetting colors;
   public final ColorSetting color;
   public final ColorSetting hoverColor;
   public final ColorSetting defaultColor;
   public final ColorSetting defaultTextColor;
   public final ColorSetting enableTextColor;
   public final ColorSetting backGround;
   public final ColorSetting tint;
   public final ColorSetting endColor;
   public double alphaValue;
   private final Animation animation;
   private boolean paletteApplied;

   public ClickGui() {
      super("ClickGui", Module.Category.Client);
      BooleanSetting var10009 = this.sound;
      Objects.requireNonNull(var10009);
      this.soundPitch = this.add(new SliderSetting("SoundPitch", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, var10009::isOpen));
      this.height = this.add(new SliderSetting("Height", 3, 0, 7));
      this.textOffset = this.add(new SliderSetting("TextOffset", (double)0.0F, (double)-5.0F, (double)5.0F, (double)1.0F));
      this.titleOffset = this.add(new SliderSetting("TitleOffset", (double)-1.0F, (double)-5.0F, (double)5.0F, (double)1.0F));
      this.alpha = this.add(new SliderSetting("Alpha", 220, 0, 255));
      this.hoverAlpha = this.add(new SliderSetting("HoverAlpha", 255, 0, 255));
      this.topAlpha = this.add(new SliderSetting("TopAlpha", 180, 0, 255));
      this.fade = this.add((new BooleanSetting("Fade", true)).setParent());
      BooleanSetting var10008 = this.fade;
      Objects.requireNonNull(var10008);
      this.length = this.add(new SliderSetting("Length", 250, 0, 1000, var10008::isOpen));
      Easing var10005 = Easing.Expo;
      BooleanSetting var10006 = this.fade;
      Objects.requireNonNull(var10006);
      this.easing = this.add(new EnumSetting("Easing", var10005, var10006::isOpen));
      this.blur = this.add((new BooleanSetting("Blur", false)).setParent());
      var10008 = this.blur;
      Objects.requireNonNull(var10008);
      this.radius = this.add(new SliderSetting("Radius", (double)10.0F, (double)0.0F, (double)100.0F, var10008::isOpen));
      this.elements = this.add((new BooleanSetting("Elements", false)).setParent().injectTask(this::a));
      var10006 = this.elements;
      Objects.requireNonNull(var10006);
      this.line = this.add(new BooleanSetting("Line", true, var10006::isOpen));
      var10006 = this.elements;
      Objects.requireNonNull(var10006);
      this.gear = this.add((new ColorSetting("Gear", -1, var10006::isOpen)).injectBoolean(false));
      var10006 = this.elements;
      Objects.requireNonNull(var10006);
      this.verticalGradient = this.add(new BooleanSetting("VerticalGradient", true, var10006::isOpen));
      this.colors = this.add((new BooleanSetting("Colors", false)).setParent().injectTask(this::b));
      Color var1 = new Color(255, 0, 0, 255);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.color = this.add(new ColorSetting("Color", var1, var10006::isOpen));
      var1 = new Color(230, 242, 251, 200);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.hoverColor = this.add(new ColorSetting("HoverColor", var1, var10006::isOpen));
      var1 = new Color(255, 255, 255, 0);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.defaultColor = this.add(new ColorSetting("DefaultColor", var1, var10006::isOpen));
      var1 = new Color(255, 255, 255, 255);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.defaultTextColor = this.add(new ColorSetting("DefaultTextColor", var1, var10006::isOpen));
      var1 = new Color(255, 255, 255, 255);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.enableTextColor = this.add(new ColorSetting("EnableTextColor", var1, var10006::isOpen));
      var1 = new Color(255, 255, 255, 0);
      var10006 = this.colors;
      Objects.requireNonNull(var10006);
      this.backGround = this.add((new ColorSetting("BackGround", var1, var10006::isOpen)).injectBoolean(true));
      this.tint = this.add((new ColorSetting("Tint", new Color(12, 60, 95, 56))).injectBoolean(false));
      this.endColor = this.add(new ColorSetting("End", new Color(255, 120, 240, 72), () -> this.tint.booleanValue));
      this.animation = new Animation();
      this.paletteApplied = false;
      this.setChinese("点击界面");
      INSTANCE = this;
      Alien.EVENT_BUS.subscribe(new FadeOut());
   }

   public static ClickGui getInstance() {
      return INSTANCE;
   }

   public void a() {
      this.elements.setValueWithoutTask(false);
      this.elements.setOpen(!this.elements.isOpen());
   }

   public void b() {
      this.colors.setValueWithoutTask(false);
      this.colors.setOpen(!this.colors.isOpen());
   }

   public boolean onEnable() {
      if (nullCheck()) {
         this.disable();
      } else {
         this.updateColor();
         if (this.guiSound.getValue() && mc.method_1483() != null) {
            mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, this.soundPitch.getValueFloat()));
         }

         for(Component component : ClickGuiScreen.getInstance().getComponents()) {
            component.setHeight(18);

            for(Item item : component.getItems()) {
               item.setHeight(10 + this.height.getValueInt());
            }
         }

         mc.method_1507(ClickGuiScreen.getInstance());
      }

      return false;
   }

   public void onDisable() {
      if (mc.field_1755 instanceof ClickGuiScreen) {
         mc.field_1755.method_25419();
      }

      if (this.guiSound.getValue() && mc.method_1483() != null) {
         mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, this.soundPitch.getValueFloat()));
      }

      if (this.autoSave.getValue()) {
         Alien.save();
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.updateColor();
      if (!(mc.field_1755 instanceof ClickGuiScreen)) {
         this.disable();
      }

   }

   public void updateColor() {
      Button.hoverColor = this.hoverColor.getValue().getRGB();
      Button.defaultTextColor = this.defaultTextColor.getValue().getRGB();
      Button.defaultColor = this.defaultColor.getValue().getRGB();
      Button.enableTextColor = this.enableTextColor.getValue().getRGB();
   }

   private void applyPaletteOnce() {
      if (!this.paletteApplied) {
         this.color.setValue(new Color(0, 120, 212));
         this.hoverColor.setValue(new Color(230, 242, 251, 200));
         this.defaultColor.setValue(new Color(255, 255, 255, 236));
         this.defaultTextColor.setValue(new Color(30, 30, 30));
         this.enableTextColor.setValue(new Color(24, 24, 24));
         this.backGround.setValue(new Color(255, 255, 255, 236));
         this.tint.setValue(new Color(0, 120, 212, 36));
         this.endColor.setValue(new Color(0, 120, 212, 18));
         this.paletteApplied = true;
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
