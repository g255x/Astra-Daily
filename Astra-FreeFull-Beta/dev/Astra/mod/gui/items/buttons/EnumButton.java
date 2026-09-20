package dev.Astra.mod.gui.items.buttons;

import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.gui.items.Component;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import java.awt.Color;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3675;

public class EnumButton extends Button {
   public final EnumSetting<?> setting;
   private boolean open;

   public EnumButton(EnumSetting<?> setting) {
      super(setting.getName());
      this.setting = setting;
   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
      boolean hovered = this.isHovering(mouseX, mouseY);
      double baseDelay = this.getColorDelay();
      float w = (float)this.width + 7.0F;
      float h = (float)this.height - 0.5F;
      if (ClickGui.getInstance().colorMode.getValue() == ClickGui.ColorMode.Spectrum && this.getState()) {
         int a = hovered ? ClickGui.getInstance().hoverAlpha.getValueInt() : ClickGui.getInstance().alpha.getValueInt();
         Render2DUtil.drawLutRect(context.method_51448(), this.x, this.y, w, h, ClickGui.getInstance().getSpectrumLutId(), ClickGui.getInstance().getSpectrumLutHeight(), a);
      } else {
         Color color = ClickGui.getInstance().getColor(baseDelay);
         Render2DUtil.rect(context.method_51448(), this.x, this.y, this.x + w, this.y + (float)this.height - 0.5F, this.getState() ? (!hovered ? ColorUtil.injectAlpha(color, ClickGui.getInstance().alpha.getValueInt()).getRGB() : ColorUtil.injectAlpha(color, ClickGui.getInstance().hoverAlpha.getValueInt()).getRGB()) : (!hovered ? defaultColor : hoverColor));
      }

      float textY = this.getCenteredTextY(this.y, (float)this.height - 0.5F);
      if (this.isHovering(mouseX, mouseY) && class_3675.method_15987(mc.method_22683().method_4490(), 340)) {
         this.drawString(this.setting.getName().equalsIgnoreCase("Page") ? "Reset Page" : "Reset Default", (double)(this.x + 2.3F), (double)textY, enableTextColor);
      } else {
         String var10001 = this.setting.getName();
         this.drawString(var10001 + " " + String.valueOf(class_124.field_1080) + (this.setting.getValue().name().equalsIgnoreCase("ABC") ? "ABC" : this.setting.getValue().name()), (double)(this.x + 2.3F), (double)textY, this.getState() ? enableTextColor : defaultTextColor);
      }

      if (this.open) {
         int y = (int)this.y;

         for(Enum e : (Enum[])this.setting.getValue().getDeclaringClass().getEnumConstants()) {
            String s = e.name();
            float optionY = this.getCenteredTextY((float)y + (float)this.height + 1.0F, 11.0F);
            this.drawString(s, (double)((float)this.width / 2.0F - (float)this.getWidth(s) / 2.0F + 2.0F + this.x), (double)optionY, this.setting.getValue().name().equals(s) ? enableTextColor : defaultTextColor);
            y += 11;
         }
      }

   }

   public void update() {
      this.setHidden(!this.setting.isVisible());
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isHovering(mouseX, mouseY) && class_3675.method_15987(mc.method_22683().method_4490(), 340)) {
         if (!this.setting.getName().equalsIgnoreCase("Page")) {
            Enum defaultValue = this.setting.getDefaultValue();
            this.setting.setEnumValue(defaultValue.name());
            sound();
         } else {
            boolean resetPage = false;

            for(Component component : ClickGuiScreen.getInstance().getComponents()) {
               for(ModuleButton moduleButton : component.getItems()) {
                  if (moduleButton.getModule().getSettings().contains(this.setting)) {
                     moduleButton.resetPageSettingsToDefault(this.setting);
                     resetPage = true;
                     break;
                  }
               }

               if (resetPage) {
                  break;
               }
            }

            sound();
         }
      } else {
         super.mouseClicked(mouseX, mouseY, mouseButton);
         if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            sound();
         } else if (mouseButton == 0 && this.open) {
            int y = (int)this.y;

            for(Enum o : (Enum[])this.setting.getValue().getDeclaringClass().getEnumConstants()) {
               if ((float)mouseX > this.x && (float)mouseX < this.x + (float)this.width && mouseY >= y + this.height + 1 && mouseY < y + this.height + 11 + 1) {
                  this.setting.setEnumValue(String.valueOf(o));
                  sound();
               }

               y += 11;
            }
         }

      }
   }

   public int getHeight() {
      return super.getHeight() + (this.open ? 11 * ((Enum[])this.setting.getValue().getDeclaringClass().getEnumConstants()).length : 0);
   }

   public void toggle() {
      this.setting.increaseEnum();
   }

   public boolean getState() {
      return true;
   }
}
