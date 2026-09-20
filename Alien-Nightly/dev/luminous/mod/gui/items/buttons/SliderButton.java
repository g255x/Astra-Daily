package dev.luminous.mod.gui.items.buttons;

import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.mod.gui.ClickGuiScreen;
import dev.luminous.mod.gui.items.Component;
import dev.luminous.mod.modules.impl.client.ClickGui;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_3728;
import org.lwjgl.glfw.GLFW;

public class SliderButton extends Button {
   private final double min;
   private final double max;
   private final double difference;
   public final SliderSetting setting;
   public boolean isListening;
   private String currentString = "";
   private boolean drag = false;

   public SliderButton(SliderSetting setting) {
      super(setting.getName());
      this.setting = setting;
      this.min = setting.getMin();
      this.max = setting.getMax();
      this.difference = this.max - this.min;
   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
      this.dragSetting(mouseX, mouseY);
      Render2DUtil.rect(context.method_51448(), this.x, this.y, this.x + (float)this.width + 7.0F, this.y + (float)this.height - 0.5F, !this.isHovering(mouseX, mouseY) ? defaultColor : hoverColor);
      Color color = ClickGui.getInstance().color.getValue();
      Render2DUtil.rect(context.method_51448(), this.x, this.y, this.setting.getValue() <= this.min ? this.x : (float)((double)this.x + (double)((float)this.width + 7.0F) * this.partialMultiplier()), this.y + (float)this.height - 0.5F, !this.isHovering(mouseX, mouseY) ? ColorUtil.injectAlpha(color, ClickGui.getInstance().alpha.getValueInt()).getRGB() : ColorUtil.injectAlpha(color, ClickGui.getInstance().hoverAlpha.getValueInt()).getRGB());
      if (this.isListening) {
         String var10001 = this.currentString;
         this.drawString(var10001 + StringButton.getIdleSign(), (double)(this.x + 2.3F), (double)(this.y - 1.7F - (float)ClickGuiScreen.getInstance().getTextOffset()), this.getState() ? enableTextColor : defaultTextColor);
      } else {
         this.drawString(this.getName() + " " + String.valueOf(class_124.field_1080) + this.setting.getValueFloat() + this.setting.getSuffix(), (double)(this.x + 2.3F), (double)(this.y - 1.7F - (float)ClickGuiScreen.getInstance().getTextOffset()), enableTextColor);
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (this.isHovering(mouseX, mouseY)) {
         sound();
         if (mouseButton == 0) {
            if (this.isListening) {
               this.toggle();
            } else {
               this.setSettingFromX(mouseX);
               this.drag = true;
            }
         } else if (mouseButton == 1) {
            this.toggle();
         }
      }

   }

   public boolean isHovering(int mouseX, int mouseY) {
      for(Component component : ClickGuiScreen.getInstance().getComponents()) {
         if (component.drag) {
            return false;
         }
      }

      return (float)mouseX >= this.getX() && (float)mouseX <= this.getX() + (float)this.getWidth() + 8.0F && (float)mouseY >= this.getY() && (float)mouseY <= this.getY() + (float)this.height - 1.0F;
   }

   public void update() {
      this.setHidden(!this.setting.isVisible());
   }

   public void onKeyTyped(char typedChar, int keyCode) {
      if (this.isListening) {
         this.setString(this.currentString + typedChar);
      }

   }

   public void onKeyPressed(int key) {
      if (this.isListening) {
         switch (key) {
            case 86:
               if (class_3675.method_15987(mc.method_22683().method_4490(), 341)) {
                  String var10001 = this.currentString;
                  this.setString(var10001 + class_3728.method_27556(mc));
               }
               break;
            case 256:
               this.isListening = false;
               break;
            case 257:
            case 335:
               this.enterString();
               break;
            case 259:
               this.setString(StringButton.removeLastChar(this.currentString));
         }
      }

   }

   private void enterString() {
      if (!this.currentString.isEmpty() && this.isNumeric(this.currentString)) {
         this.setting.setValue(Double.parseDouble(this.currentString));
      } else {
         this.setting.setValue(this.setting.getDefaultValue());
      }

      this.onMouseClick();
   }

   public void setString(String newString) {
      this.currentString = newString;
   }

   private void dragSetting(int mouseX, int mouseY) {
      if (this.drag && this.isHovering(mouseX, mouseY) && GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 0) == 1) {
         this.setSettingFromX(mouseX);
      } else {
         this.drag = false;
      }

   }

   public void toggle() {
      float var10001 = this.setting.getValueFloat();
      this.setString("" + var10001);
      this.isListening = !this.isListening;
   }

   public boolean getState() {
      return !this.isListening;
   }

   private void setSettingFromX(int mouseX) {
      double percent = (double)((float)mouseX - this.x) / ((double)this.width + 7.4);
      double result = Math.min(this.setting.getMin() + this.difference * percent, this.max);
      this.setting.setValue(result);
   }

   private double part() {
      return this.setting.getValue() - this.min;
   }

   private double partialMultiplier() {
      return Math.min(this.part() / this.difference, (double)1.0F);
   }

   private boolean isNumeric(String str) {
      return str.matches("-?\\d+(\\.\\d+)?");
   }
}
