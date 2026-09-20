package dev.Astra.mod.gui.items.buttons;

import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.gui.items.Component;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_3675;

public class BooleanButton extends Button {
   private final BooleanSetting setting;
   private final Animation toggleAnimation = new Animation();

   public BooleanButton(BooleanSetting setting) {
      super(setting.getName());
      this.setting = setting;
      double initial = setting.getValue() ? (double)1.0F : (double)0.0F;
      this.toggleAnimation.from = initial;
      this.toggleAnimation.to = initial;
   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
      boolean hovered = this.isHovering(mouseX, mouseY);
      double toggleProgress = this.toggleAnimation.get(this.getState() ? (double)1.0F : (double)0.0F, 160L, Easing.CubicInOut);
      double baseDelay = this.getColorDelay();
      int accentA = hovered ? ClickGui.getInstance().hoverAlpha.getValueInt() : ClickGui.getInstance().alpha.getValueInt();
      Color unpressedFill = new Color(hovered ? hoverColor : defaultColor, true);
      float w = (float)this.width + 7.0F;
      float h = (float)this.height - 0.5F;
      if (ClickGui.getInstance().colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
         if (toggleProgress >= 0.999) {
            Render2DUtil.drawLutRect(context.method_51448(), this.x, this.y, w, h, ClickGui.getInstance().getSpectrumLutId(), ClickGui.getInstance().getSpectrumLutHeight(), accentA);
         } else {
            Render2DUtil.rect(context.method_51448(), this.x, this.y, this.x + w, this.y + h, unpressedFill.getRGB());
            int overlayA = (int)Math.round((double)accentA * toggleProgress);
            if (overlayA > 0) {
               Render2DUtil.drawLutRect(context.method_51448(), this.x, this.y, w, h, ClickGui.getInstance().getSpectrumLutId(), ClickGui.getInstance().getSpectrumLutHeight(), overlayA);
            }
         }
      } else {
         Color accent = ClickGui.getInstance().getColor(baseDelay);
         Color pressedFill = new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), accentA);
         Color baseFill = ColorUtil.fadeColor(unpressedFill, pressedFill, toggleProgress);
         Render2DUtil.rect(context.method_51448(), this.x, this.y, this.x + w, this.y + (float)this.height - 0.5F, baseFill.getRGB());
      }

      float textY = this.getCenteredTextY(this.y, (float)this.height - 0.5F);
      if (hovered && class_3675.method_15987(mc.method_22683().method_4490(), 340)) {
         this.drawString("Reset Default", (double)(this.x + 2.3F), (double)textY, enableTextColor);
      } else {
         this.drawString(this.getName(), (double)(this.x + 2.3F), (double)textY, this.getState() ? enableTextColor : defaultTextColor);
      }

      if (this.setting.hasParent()) {
         this.drawString(this.setting.isOpen() ? "-" : "+", (double)(this.x + (float)this.width - 1.0F), (double)textY, ClickGui.getInstance().gear.getValue());
      }

   }

   public void update() {
      this.setHidden(!this.setting.isVisible());
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && class_3675.method_15987(mc.method_22683().method_4490(), 340)) {
         if (this.setting.hasParent()) {
            boolean resetChildren = false;

            for(Component component : ClickGuiScreen.getInstance().getComponents()) {
               for(ModuleButton moduleButton : component.getItems()) {
                  if (moduleButton.getModule().getSettings().contains(this.setting)) {
                     moduleButton.resetChildSettingsToDefault(this.setting);
                     resetChildren = true;
                     break;
                  }
               }

               if (resetChildren) {
                  break;
               }
            }
         }

         this.setting.setValue(this.setting.getDefaultValue());
         sound();
      } else {
         super.mouseClicked(mouseX, mouseY, mouseButton);
         if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            sound();
            this.setting.setOpen(!this.setting.isOpen());
         }

      }
   }

   public void toggle() {
      this.setting.setValue(!this.setting.getValue());
   }

   public boolean getState() {
      return this.setting.getValue();
   }
}
