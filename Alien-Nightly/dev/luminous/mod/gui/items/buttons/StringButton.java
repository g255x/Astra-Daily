package dev.luminous.mod.gui.items.buttons;

import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.mod.gui.ClickGuiScreen;
import dev.luminous.mod.modules.impl.client.ClickGui;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.awt.Color;
import net.minecraft.class_124;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_3728;

public class StringButton extends Button {
   private static final Timer idleTimer = new Timer();
   private static boolean idle;
   private final StringSetting setting;
   public boolean isListening;
   private String currentString = "";

   public StringButton(StringSetting setting) {
      super(setting.getName());
      this.setting = setting;
   }

   public static String removeLastChar(String str) {
      String output = "";
      if (str != null && !str.isEmpty()) {
         output = str.substring(0, str.length() - 1);
      }

      return output;
   }

   public static String getIdleSign() {
      if (idleTimer.passed(500L)) {
         idle = !idle;
         idleTimer.reset();
      }

      return idle ? "_" : "";
   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
      Color color = ClickGui.getInstance().color.getValue();
      Render2DUtil.rect(context.method_51448(), this.x, this.y, this.x + (float)this.width + 7.0F, this.y + (float)this.height - 0.5F, this.getState() ? (!this.isHovering(mouseX, mouseY) ? ColorUtil.injectAlpha(color, ClickGui.getInstance().alpha.getValueInt()).getRGB() : ColorUtil.injectAlpha(color, ClickGui.getInstance().hoverAlpha.getValueInt()).getRGB()) : (!this.isHovering(mouseX, mouseY) ? defaultColor : hoverColor));
      if (this.isListening) {
         String var10001 = this.currentString;
         this.drawString(var10001 + getIdleSign(), (double)(this.x + 2.3F), (double)(this.y - 1.7F - (float)ClickGuiScreen.getInstance().getTextOffset()), this.getState() ? enableTextColor : defaultTextColor);
      } else {
         String var6 = this.setting.getName();
         this.drawString(var6 + ": " + String.valueOf(class_124.field_1080) + this.setting.getValue(), (double)(this.x + 2.3F), (double)(this.y - 1.7F - (float)ClickGuiScreen.getInstance().getTextOffset()), this.getState() ? enableTextColor : defaultTextColor);
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
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
               this.setString(removeLastChar(this.currentString));
         }
      }

   }

   public void update() {
      this.setHidden(!this.setting.isVisible());
   }

   private void enterString() {
      if (this.currentString.isEmpty()) {
         this.setting.setValue(this.setting.getDefaultValue());
      } else {
         this.setting.setValue(this.currentString);
      }

      this.onMouseClick();
   }

   public void toggle() {
      this.setString(this.setting.getValue());
      this.isListening = !this.isListening;
   }

   public boolean getState() {
      return !this.isListening;
   }

   public void setString(String newString) {
      this.currentString = newString;
   }
}
