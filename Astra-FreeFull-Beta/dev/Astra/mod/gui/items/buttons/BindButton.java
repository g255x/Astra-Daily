package dev.Astra.mod.gui.items.buttons;

import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import java.awt.Color;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3675;

public class BindButton extends Button {
   private final BindSetting setting;
   public boolean isListening;

   public BindButton(BindSetting setting) {
      super(setting.getName());
      this.setting = setting;
   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
      boolean hovered = this.isHovering(mouseX, mouseY);
      double baseDelay = this.getColorDelay();
      float w = (float)this.width + 7.0F;
      float h = (float)this.height - 0.5F;
      if (!this.getState() && ClickGui.getInstance().colorMode.getValue() == ClickGui.ColorMode.Spectrum) {
         int a = !hovered ? ClickGui.getInstance().alpha.getValueInt() : ClickGui.getInstance().hoverAlpha.getValueInt();
         Render2DUtil.drawLutRect(context.method_51448(), this.x, this.y, w, h, ClickGui.getInstance().getSpectrumLutId(), ClickGui.getInstance().getSpectrumLutHeight(), a);
      } else {
         Color color = ClickGui.getInstance().getColor(baseDelay);
         Render2DUtil.rect(context.method_51448(), this.x, this.y, this.x + w, this.y + (float)this.height - 0.5F, this.getState() ? (!hovered ? defaultColor : hoverColor) : (!hovered ? ColorUtil.injectAlpha(color, ClickGui.getInstance().alpha.getValueInt()).getRGB() : ColorUtil.injectAlpha(color, ClickGui.getInstance().hoverAlpha.getValueInt()).getRGB()));
      }

      float textY = this.getCenteredTextY(this.y, (float)this.height - 0.5F);
      if (this.isListening) {
         this.drawString("Press keyCodec Key...", (double)(this.x + 2.3F), (double)textY, enableTextColor);
      } else {
         String str = this.setting.getKeyString();
         if (!this.isListening && this.isHovering(mouseX, mouseY) && class_3675.method_15987(mc.method_22683().method_4490(), 340)) {
            if (this.setting.getName().equals("Key")) {
               if (this.setting.isHoldEnable()) {
                  this.drawString("§7Toggle/§fHold", (double)(this.x + 2.3F), (double)textY, enableTextColor);
               } else {
                  this.drawString("§fToggle§7/Hold", (double)(this.x + 2.3F), (double)textY, enableTextColor);
               }
            } else {
               this.drawString("Reset Default", (double)(this.x + 2.3F), (double)textY, enableTextColor);
            }
         } else {
            String var10001 = this.setting.getName();
            this.drawString(var10001 + " " + String.valueOf(class_124.field_1080) + str, (double)(this.x + 2.3F), (double)textY, this.getState() ? enableTextColor : defaultTextColor);
         }
      }

   }

   public void update() {
      this.setHidden(!this.setting.isVisible());
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
         if (class_3675.method_15987(mc.method_22683().method_4490(), 340)) {
            if (this.setting.getName().equals("Key")) {
               this.setting.setHoldEnable(!this.setting.isHoldEnable());
               sound();
            } else {
               this.setting.setValue(this.setting.getDefaultValue());
               this.setting.setHoldEnable(false);
               this.setting.setPressed(false);
               this.setting.holding = false;
               sound();
            }
         } else {
            this.onMouseClick();
         }
      } else if (this.isListening) {
         this.setting.setValue(-mouseButton - 2);
         this.onMouseClick();
      }

   }

   public void onKeyPressed(int key) {
      if (this.isListening) {
         this.setting.setValue(key);
         if (this.setting.getKeyString().equalsIgnoreCase("DELETE")) {
            this.setting.setValue(-1);
         }

         this.onMouseClick();
      }

   }

   public void toggle() {
      this.isListening = !this.isListening;
   }

   public boolean getState() {
      return !this.isListening;
   }
}
