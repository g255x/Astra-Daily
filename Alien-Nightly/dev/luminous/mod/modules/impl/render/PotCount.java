package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render2DEvent;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Objects;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public class PotCount extends Module {
   public final SliderSetting posX = this.add(new SliderSetting("PosX", (double)10.0F, (double)0.0F, (double)1920.0F, (double)1.0F));
   public final SliderSetting posY = this.add(new SliderSetting("PosY", (double)10.0F, (double)0.0F, (double)1080.0F, (double)1.0F));
   public final SliderSetting width = this.add(new SliderSetting("Width", (double)120.0F, (double)80.0F, (double)200.0F, (double)1.0F));
   public final SliderSetting height = this.add(new SliderSetting("Height", (double)80.0F, (double)60.0F, (double)150.0F, (double)1.0F));
   public final ColorSetting bgColor = this.add(new ColorSetting("Background", new Color(Integer.MIN_VALUE, true)));
   public final ColorSetting borderColor = this.add(new ColorSetting("Border", new Color(-16711936, true)));
   public final BooleanSetting roundedCorners = this.add(new BooleanSetting("Rounded", true));
   public final SliderSetting cornerRadius = this.add(new SliderSetting("CornerRadius", (double)8.0F, (double)0.0F, (double)20.0F, (double)0.5F));
   public final SliderSetting borderThickness = this.add(new SliderSetting("BorderThickness", (double)2.0F, (double)0.0F, (double)5.0F, (double)0.5F));
   public final BooleanSetting enableBlur = this.add(new BooleanSetting("EnableBlur", true));
   public final SliderSetting blurStrength = this.add(new SliderSetting("BlurStrength", (double)5.0F, (double)1.0F, (double)20.0F, (double)0.5F));
   public final SliderSetting blurRadius = this.add(new SliderSetting("BlurRadius", (double)8.0F, (double)0.0F, (double)20.0F, (double)0.5F));
   public final ColorSetting blurOverlay = this.add(new ColorSetting("BlurOverlay", new Color(1610612736, true)));
   public final EnumSetting<FontMode> fontMode;
   public final SliderSetting textScale;
   public final ColorSetting textColor;
   public final ColorSetting potCountColor;
   public final ColorSetting timeColor;
   public final BooleanSetting showSplashPotCount;
   public final BooleanSetting showResistanceTime;
   private final DecimalFormat timeFormat;

   public PotCount() {
      super("PotCount", Module.Category.Render);
      this.fontMode = this.add(new EnumSetting("Font", PotCount.FontMode.VANILLA));
      this.textScale = this.add(new SliderSetting("TextScale", (double)1.0F, (double)0.5F, (double)2.0F, 0.1));
      this.textColor = this.add(new ColorSetting("TextColor", new Color(-1, true)));
      this.potCountColor = this.add(new ColorSetting("PotCountColor", new Color(-16711936, true)));
      this.timeColor = this.add(new ColorSetting("TimeColor", new Color(-22016, true)));
      this.showSplashPotCount = this.add(new BooleanSetting("ShowSplashPotion", true));
      this.showResistanceTime = this.add(new BooleanSetting("ShowResistanceTime", true));
      this.timeFormat = new DecimalFormat("0.0");
      this.setChinese("药水计数HUD");
   }

   @EventListener
   public void onRender2D(Render2DEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_332 context = event.drawContext;
         class_4587 matrices = context.method_51448();
         float x = this.posX.getValueFloat();
         float y = this.posY.getValueFloat();
         float w = this.width.getValueFloat();
         float h = this.height.getValueFloat();
         if (this.enableBlur.getValue()) {
            this.drawBlurBackground(matrices, x, y, w, h);
         }

         this.drawRoundedBackground(matrices, x, y, w, h);
         this.drawPotionInfo(context, matrices, x, y, w, h);
      }
   }

   private void drawBlurBackground(class_4587 matrices, float x, float y, float w, float h) {
      float radius = this.cornerRadius.getValueFloat();
      float blurStrengthValue = this.blurStrength.getValueFloat();

      for(int i = 0; (float)i < blurStrengthValue; ++i) {
         float offset = (float)i * 0.5F;
         float alpha = (float)((double)this.blurOverlay.getValue().getAlpha() / (double)255.0F * ((double)1.0F - (double)((float)i / blurStrengthValue)));
         Color blurColor = new Color(this.blurOverlay.getValue().getRed(), this.blurOverlay.getValue().getGreen(), this.blurOverlay.getValue().getBlue(), (int)(alpha * 255.0F));
         Render2DUtil.drawRoundedRect(matrices, x - offset, y - offset, w + offset * 2.0F, h + offset * 2.0F, radius + offset, blurColor);
      }

   }

   private void drawRoundedBackground(class_4587 matrices, float x, float y, float w, float h) {
      float radius = this.cornerRadius.getValueFloat();
      Render2DUtil.drawRoundedRect(matrices, x, y, w, h, radius, this.bgColor.getValue());
      if (this.borderThickness.getValueFloat() > 0.0F) {
         Render2DUtil.drawRoundedStroke(matrices, x, y, w, h, radius, this.borderColor.getValue(), 32);
      }

   }

   private void drawPotionInfo(class_332 context, class_4587 matrices, float x, float y, float w, float h) {
      float padding = 10.0F;
      float contentX = x + padding;
      float contentY = y + padding;
      float contentWidth = w - padding * 2.0F;
      int splashPotCount = this.countSplashPotions();
      float resistanceTimeLeft = this.getResistanceTimeLeft();
      float currentY = contentY;
      if (this.showSplashPotCount.getValue()) {
         String potText = "POT: " + splashPotCount;
         this.drawTextWithScale(context, matrices, potText, contentX, contentY, contentWidth, this.potCountColor.getValue());
         float textHeight = this.getTextHeight();
         currentY = contentY + textHeight + 5.0F;
      }

      if (this.showResistanceTime.getValue()) {
         Object timeText = resistanceTimeLeft > 0.0F ? "Time: " + this.timeFormat.format((double)resistanceTimeLeft) + "s" : "Time: --";
         this.drawTextWithScale(context, matrices, (String)timeText, contentX, currentY, contentWidth, this.timeColor.getValue());
      }

   }

   private void drawTextWithScale(class_332 context, class_4587 matrices, String text, float x, float y, float maxWidth, Color color) {
      matrices.method_22903();
      matrices.method_46416(x, y, 0.0F);
      matrices.method_22905(this.textScale.getValueFloat(), this.textScale.getValueFloat(), 1.0F);
      if (this.fontMode.getValue() == PotCount.FontMode.VANILLA) {
         float scaledTextWidth = (float)mc.field_1772.method_1727(text) * this.textScale.getValueFloat();
         float centeredX = (maxWidth - scaledTextWidth) / (2.0F * this.textScale.getValueFloat());
         context.method_51433(mc.field_1772, text, (int)centeredX, 0, color.getRGB(), true);
      } else {
         float scaledTextWidth = FontManager.ui.getWidth(text) * this.textScale.getValueFloat();
         float centeredX = (maxWidth - scaledTextWidth) / (2.0F * this.textScale.getValueFloat());
         FontManager.ui.drawString(matrices, text, (double)centeredX, (double)0.0F, color.getRGB());
      }

      matrices.method_22909();
   }

   private float getTextHeight() {
      float f;
      if (this.fontMode.getValue() == PotCount.FontMode.VANILLA) {
         Objects.requireNonNull(mc.field_1772);
         f = 9.0F;
      } else {
         f = FontManager.ui.getFontHeight();
      }

      return f * this.textScale.getValueFloat();
   }

   private int countSplashPotions() {
      if (mc.field_1724 == null) {
         return 0;
      } else {
         int count = 0;
         class_1799 mainHand = mc.field_1724.method_6047();
         if (mainHand.method_7909() == class_1802.field_8436) {
            ++count;
         }

         if (mc.field_1724.method_6079().method_7909() == class_1802.field_8436) {
            ++count;
         }

         for(int i = 0; i < mc.field_1724.method_31548().method_5439(); ++i) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7909() == class_1802.field_8436) {
               count += stack.method_7947();
            }
         }

         return count;
      }
   }

   private float getResistanceTimeLeft() {
      if (mc.field_1724 == null) {
         return 0.0F;
      } else {
         class_1293 resistanceEffect = mc.field_1724.method_6112(class_1294.field_5907);
         if (resistanceEffect != null && resistanceEffect.method_5578() >= 3) {
            int durationTicks = resistanceEffect.method_5584();
            float durationSeconds = (float)durationTicks / 20.0F;
            return durationSeconds;
         } else {
            return 0.0F;
         }
      }
   }

   public boolean onEnable() {
      super.onEnable();
      return false;
   }

   public void onDisable() {
      super.onDisable();
   }

   public static enum FontMode {
      VANILLA("Vanilla"),
      CUSTOM("Custom");

      private final String name;

      private FontMode(String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      // $FF: synthetic method
      private static FontMode[] $values() {
         return new FontMode[]{VANILLA, CUSTOM};
      }
   }
}
