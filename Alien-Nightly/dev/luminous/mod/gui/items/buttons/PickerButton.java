package dev.luminous.mod.gui.items.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.gui.ClickGuiScreen;
import dev.luminous.mod.modules.impl.client.ClickGui;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Objects;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.lwjgl.glfw.GLFW;

public class PickerButton extends Button {
   static class_4587 matrixStack;
   private final ColorSetting setting;
   boolean pickingColor;
   boolean pickingHue;
   boolean pickingAlpha;
   boolean open;
   float[] hsb = new float[]{1.0F, 1.0F, 1.0F, 1.0F};

   public PickerButton(ColorSetting setting) {
      super(setting.getName());
      this.setting = setting;
   }

   public static boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
      return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
   }

   public static Color getColor(Color color, float alpha) {
      float red = (float)color.getRed() / 255.0F;
      float green = (float)color.getGreen() / 255.0F;
      float blue = (float)color.getBlue() / 255.0F;
      return new Color(red, green, blue, alpha);
   }

   public static void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)pickerX, (float)pickerY, 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)pickerX, (float)(pickerY + pickerHeight), 0.0F).method_22915(1.0F, 1.0F, 1.0F, 1.0F);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)(pickerX + pickerWidth), (float)(pickerY + pickerHeight), 0.0F).method_22915(red, green, blue, 1.0F);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)(pickerX + pickerWidth), (float)pickerY, 0.0F).method_22915(red, green, blue, 1.0F);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)pickerX, (float)pickerY, 0.0F).method_22915(0.0F, 0.0F, 0.0F, 0.0F);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)pickerX, (float)(pickerY + pickerHeight), 0.0F).method_22915(0.0F, 0.0F, 0.0F, 1.0F);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)(pickerX + pickerWidth), (float)(pickerY + pickerHeight), 0.0F).method_22915(0.0F, 0.0F, 0.0F, 1.0F);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)(pickerX + pickerWidth), (float)pickerY, 0.0F).method_22915(0.0F, 0.0F, 0.0F, 0.0F);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)right, (float)top, 0.0F).method_22915((float)(endColor >> 24 & 255) / 255.0F, (float)(endColor >> 16 & 255) / 255.0F, (float)(endColor >> 8 & 255) / 255.0F, (float)(endColor >> 24 & 255) / 255.0F);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)left, (float)top, 0.0F).method_22915((float)(startColor >> 16 & 255) / 255.0F, (float)(startColor >> 8 & 255) / 255.0F, (float)(startColor & 255) / 255.0F, (float)(startColor >> 24 & 255) / 255.0F);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)left, (float)bottom, 0.0F).method_22915((float)(startColor >> 16 & 255) / 255.0F, (float)(startColor >> 8 & 255) / 255.0F, (float)(startColor & 255) / 255.0F, (float)(startColor >> 24 & 255) / 255.0F);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)right, (float)bottom, 0.0F).method_22915((float)(endColor >> 24 & 255) / 255.0F, (float)(endColor >> 16 & 255) / 255.0F, (float)(endColor >> 8 & 255) / 255.0F, (float)(endColor >> 24 & 255) / 255.0F);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
      float startA = (float)(startColor >> 24 & 255) / 255.0F;
      float startR = (float)(startColor >> 16 & 255) / 255.0F;
      float startG = (float)(startColor >> 8 & 255) / 255.0F;
      float startB = (float)(startColor & 255) / 255.0F;
      float endA = (float)(endColor >> 24 & 255) / 255.0F;
      float endR = (float)(endColor >> 16 & 255) / 255.0F;
      float endG = (float)(endColor >> 8 & 255) / 255.0F;
      float endB = (float)(endColor & 255) / 255.0F;
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)minX, (float)minY, 0.0F).method_22915(startR, startG, startB, startA);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)minX, (float)maxY, 0.0F).method_22915(startR, startG, startB, startA);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)maxX, (float)maxY, 0.0F).method_22915(endR, endG, endB, endA);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)maxX, (float)minY, 0.0F).method_22915(endR, endG, endB, endA);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static int gradientColor(int color, int percentage) {
      int r = ((color & 16711680) >> 16) * (100 + percentage) / 100;
      int g = ((color & '\uff00') >> 8) * (100 + percentage) / 100;
      int b = (color & 255) * (100 + percentage) / 100;
      return (new Color(r, g, b)).hashCode();
   }

   public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor, boolean hovered) {
      if (hovered) {
         startColor = gradientColor(startColor, -20);
         endColor = gradientColor(endColor, -20);
      }

      float c = (float)(startColor >> 24 & 255) / 255.0F;
      float c1 = (float)(startColor >> 16 & 255) / 255.0F;
      float c2 = (float)(startColor >> 8 & 255) / 255.0F;
      float c3 = (float)(startColor & 255) / 255.0F;
      float c4 = (float)(endColor >> 24 & 255) / 255.0F;
      float c5 = (float)(endColor >> 16 & 255) / 255.0F;
      float c6 = (float)(endColor >> 8 & 255) / 255.0F;
      float c7 = (float)(endColor & 255) / 255.0F;
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), right, top, 0.0F).method_22915(c1, c2, c3, c);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), left, top, 0.0F).method_22915(c1, c2, c3, c);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), left, bottom, 0.0F).method_22915(c5, c6, c7, c4);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), right, bottom, 0.0F).method_22915(c5, c6, c7, c4);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public static String readClipboard() {
      try {
         return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
      } catch (UnsupportedFlavorException | IOException var1) {
         return null;
      }
   }

   public static void drawOutlineRect(double left, double top, double right, double bottom, Color color, float lineWidth) {
      if (left < right) {
         double i = left;
         left = right;
         right = i;
      }

      if (top < bottom) {
         double j = top;
         top = bottom;
         bottom = j;
      }

      float f3 = (float)(color.getRGB() >> 24 & 255) / 255.0F;
      float f = (float)(color.getRGB() >> 16 & 255) / 255.0F;
      float f1 = (float)(color.getRGB() >> 8 & 255) / 255.0F;
      float f2 = (float)(color.getRGB() & 255) / 255.0F;
      RenderSystem.enableBlend();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_29344, class_290.field_1576);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)left, (float)bottom, 0.0F).method_22915(f, f1, f2, f3);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)right, (float)bottom, 0.0F).method_22915(f, f1, f2, f3);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)right, (float)top, 0.0F).method_22915(f, f1, f2, f3);
      bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), (float)left, (float)top, 0.0F).method_22915(f, f1, f2, f3);
      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.disableBlend();
   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
      matrixStack = context.method_51448();
      Color color = ClickGui.getInstance().color.getValue();
      Render2DUtil.rect(context.method_51448(), this.x, this.y, this.x + (float)this.width + 7.0F, this.y + (float)this.height - 0.5F, this.getState() ? (!this.isHovering(mouseX, mouseY) ? ColorUtil.injectAlpha(color, ClickGui.getInstance().alpha.getValueInt()).getRGB() : ColorUtil.injectAlpha(color, ClickGui.getInstance().hoverAlpha.getValueInt()).getRGB()) : (!this.isHovering(mouseX, mouseY) ? defaultColor : hoverColor));
      Render2DUtil.rect(matrixStack, this.x - 1.5F + (float)this.width + 0.6F - 0.5F, this.y + 4.0F, this.x + (float)this.width + 7.0F - 2.5F, this.y + (float)this.height - 5.0F, ColorUtil.injectAlpha(this.setting.getValue(), 255).getRGB());
      this.drawString(this.getName(), (double)(this.x + 2.3F), (double)(this.y - 1.7F - (float)ClickGuiScreen.getInstance().getTextOffset()), enableTextColor);
      if (this.open) {
         this.drawPicker(this.setting, (int)this.x, (int)this.y + this.height, (int)this.x, (int)this.y + 103, (int)this.x, (int)this.y + 95, mouseX, mouseY);
         this.drawString("copy", (double)(this.x + 2.3F), (double)(this.y + 96.0F + (float)this.height - (float)ClickGuiScreen.getInstance().getTextOffset()), this.isInsideCopy(mouseX, mouseY) ? enableTextColor : defaultTextColor);
         this.drawString("paste", (double)(this.x + (float)this.width - 2.3F - (float)this.getWidth("paste") + 11.7F - 4.6F), (double)(this.y + 96.0F + (float)this.height - (float)ClickGuiScreen.getInstance().getTextOffset()), this.isInsidePaste(mouseX, mouseY) ? enableTextColor : defaultTextColor);
         this.drawString("sync", (double)(this.x + 2.3F), (double)(this.y + 96.0F + (float)this.getFontHeight() + (float)this.height - (float)ClickGuiScreen.getInstance().getTextOffset()), this.setting.sync ? ColorUtil.injectAlpha(color, 255).getRGB() : (this.isInsideRainbow(mouseX, mouseY) ? enableTextColor : defaultTextColor));
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      if (mouseButton == 0) {
         int pickerWidth = (int)((float)this.width + 7.4F);
         int pickerHeight = 78;
         int hueSliderWidth = pickerWidth + 3;
         int hueSliderHeight = 7;
         int alphaSliderHeight = 7;
         if (mouseOver((int)this.x, (int)this.y + 15, (int)this.x + pickerWidth, (int)this.y + 15 + pickerHeight, mouseX, mouseY)) {
            this.pickingColor = true;
         }

         if (mouseOver((int)this.x, (int)this.y + 103, (int)this.x + hueSliderWidth, (int)this.y + 103 + hueSliderHeight, mouseX, mouseY)) {
            this.pickingHue = true;
         }

         if (mouseOver((int)this.x, (int)this.y + 95, (int)this.x + pickerWidth, (int)this.y + 95 + alphaSliderHeight, mouseX, mouseY)) {
            this.pickingAlpha = true;
         }
      }

      if (this.isHovering(mouseX, mouseY)) {
         if (mouseButton == 1) {
            sound();
            this.open = !this.open;
         } else if (mouseButton == 0 && this.setting.injectBoolean) {
            sound();
            this.setting.booleanValue = !this.setting.booleanValue;
         }
      }

      if (mouseButton == 0 && this.isInsideRainbow(mouseX, mouseY) && this.open) {
         this.setting.sync = !this.setting.sync;
      }

      if (mouseButton == 0 && this.isInsideCopy(mouseX, mouseY) && this.open) {
         sound();
         String hex = String.format("#%02x%02x%02x%02x", this.setting.getValue().getAlpha(), this.setting.getValue().getRed(), this.setting.getValue().getGreen(), this.setting.getValue().getBlue());
         StringSelection selection = new StringSelection(hex);
         Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         clipboard.setContents(selection, selection);
         CommandManager.sendMessage("Copied the color to your clipboard.");
      }

      if (mouseButton == 0 && this.isInsidePaste(mouseX, mouseY) && this.open) {
         try {
            if (readClipboard() != null) {
               if (((String)Objects.requireNonNull(readClipboard())).startsWith("#")) {
                  String hex = (String)Objects.requireNonNull(readClipboard());
                  int a = Integer.valueOf(hex.substring(1, 3), 16);
                  int r = Integer.valueOf(hex.substring(3, 5), 16);
                  int g = Integer.valueOf(hex.substring(5, 7), 16);
                  int b = Integer.valueOf(hex.substring(7, 9), 16);
                  this.setting.setValue(new Color(r, g, b, a));
               } else {
                  String[] color = readClipboard().split(",");
                  this.setting.setValue(new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2])));
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
            CommandManager.sendMessage("§4Bad color format! Use Hex (#FFFFFFFF)");
            this.setting.setValue(-1);
         }
      }

   }

   public boolean getState() {
      return this.setting.booleanValue;
   }

   public void update() {
      this.setHidden(!this.setting.isVisible());
   }

   public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
      this.pickingAlpha = false;
      this.pickingHue = false;
      this.pickingColor = false;
   }

   public boolean isInsideCopy(int mouseX, int mouseY) {
      return mouseOver((int)((float)((int)this.x) + 2.3F), (int)(this.y + 96.0F + (float)this.height - (float)ClickGuiScreen.getInstance().getTextOffset()), (int)((float)((int)this.x) + 2.3F) + this.getWidth("copy"), (int)(this.y + 95.0F + (float)this.height - (float)ClickGuiScreen.getInstance().getTextOffset()) + this.getFontHeight(), mouseX, mouseY);
   }

   public boolean isInsideRainbow(int mouseX, int mouseY) {
      return mouseOver((int)((float)((int)this.x) + 2.3F), (int)(this.y + 96.0F + (float)this.height + (float)this.getFontHeight() - (float)ClickGuiScreen.getInstance().getTextOffset()), (int)((float)((int)this.x) + 2.3F) + this.getWidth("sync"), (int)(this.y + 95.0F + (float)this.height + (float)this.getFontHeight() - (float)ClickGuiScreen.getInstance().getTextOffset()) + this.getFontHeight(), mouseX, mouseY);
   }

   public boolean isInsidePaste(int mouseX, int mouseY) {
      return mouseOver((int)(this.x + (float)this.width - 2.3F - (float)this.getWidth("paste") + 11.7F - 4.6F), (int)(this.y + 96.0F + (float)this.height - (float)ClickGuiScreen.getInstance().getTextOffset()), (int)(this.x + (float)this.width - 2.3F - (float)this.getWidth("paste") + 11.7F - 4.6F) + this.getWidth("paste"), (int)(this.y + 95.0F + (float)this.height - (float)ClickGuiScreen.getInstance().getTextOffset()) + this.getFontHeight(), mouseX, mouseY);
   }

   public int getHeight() {
      return this.open ? super.getHeight() + 119 : super.getHeight();
   }

   public void drawPicker(ColorSetting setting, int pickerX, int pickerY, int hueSliderX, int hueSliderY, int alphaSliderX, int alphaSliderY, int mouseX, int mouseY) {
      int pickerWidth = (int)((float)this.width + 7.4F);
      int pickerHeight = 78;
      int hueSliderHeight = 7;
      int alphaSliderHeight = 7;
      if (this.pickingColor && (GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 0) != 1 || !mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY))) {
         this.pickingColor = false;
      }

      if (!this.pickingColor) {
         this.hsb = Color.RGBtoHSB(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), (float[])null);
      }

      float[] color = new float[]{this.hsb[0], this.hsb[1], this.hsb[2], (float)setting.getValue().getAlpha() / 255.0F};
      if (this.pickingHue && (GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 0) != 1 || !mouseOver(hueSliderX, hueSliderY, hueSliderX + pickerWidth, hueSliderY + hueSliderHeight, mouseX, mouseY))) {
         this.pickingHue = false;
      }

      if (this.pickingAlpha && (GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 0) != 1 || !mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + pickerWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY))) {
         this.pickingAlpha = false;
      }

      if (this.pickingHue) {
         float restrictedX = (float)Math.min(Math.max(hueSliderX, mouseX), hueSliderX + pickerWidth);
         color[0] = (restrictedX - (float)hueSliderX) / (float)pickerWidth;
      }

      if (this.pickingAlpha) {
         float restrictedX = (float)Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + pickerWidth);
         color[3] = 1.0F - (restrictedX - (float)alphaSliderX) / (float)pickerWidth;
      }

      if (this.pickingColor) {
         float restrictedX = (float)Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
         float restrictedY = (float)Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
         color[1] = (restrictedX - (float)pickerX) / (float)pickerWidth;
         color[2] = 1.0F - (restrictedY - (float)pickerY) / (float)pickerHeight;
      }

      int selectedColor = Color.HSBtoRGB(color[0], 1.0F, 1.0F);
      float selectedRed = (float)(selectedColor >> 16 & 255) / 255.0F;
      float selectedGreen = (float)(selectedColor >> 8 & 255) / 255.0F;
      float selectedBlue = (float)(selectedColor & 255) / 255.0F;
      drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, color[3]);
      this.drawHueSlider(hueSliderX, hueSliderY, pickerWidth, hueSliderHeight, color[0]);
      int cursorX = (int)((float)pickerX + color[1] * (float)pickerWidth);
      int cursorY = (int)((float)(pickerY + pickerHeight) - color[2] * (float)pickerHeight);
      setting.setValue(getColor(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]));
      Render2DUtil.arrow(matrixStack, (float)cursorX, (float)cursorY, setting.getValue());
      this.drawAlphaSlider(alphaSliderX, alphaSliderY, pickerWidth - 1, alphaSliderHeight, selectedRed, selectedGreen, selectedBlue, color[3]);
   }

   public void drawHueSlider(int x, int y, int width, int height, float hue) {
      int step = 0;
      if (height > width) {
         Render2DUtil.rect(matrixStack, (float)x, (float)y, (float)(x + width), (float)(y + 4), -65536);
         y += 4;

         for(int colorIndex = 0; colorIndex < 6; ++colorIndex) {
            int previousStep = Color.HSBtoRGB((float)step / 6.0F, 1.0F, 1.0F);
            int nextStep = Color.HSBtoRGB((float)(step + 1) / 6.0F, 1.0F, 1.0F);
            drawGradientRect((float)x, (float)y + (float)step * ((float)height / 6.0F), (float)(x + width), (float)y + (float)(step + 1) * ((float)height / 6.0F), previousStep, nextStep, false);
            ++step;
         }

         int sliderMinY = (int)((float)y + (float)height * hue) - 4;
         Render2DUtil.rect(matrixStack, (float)x, (float)(sliderMinY - 1), (float)(x + width), (float)(sliderMinY + 1), -1);
         drawOutlineRect((double)x, (double)(sliderMinY - 1), (double)(x + width), (double)(sliderMinY + 1), Color.BLACK, 1.0F);
      } else {
         for(int colorIndex = 0; colorIndex < 6; ++colorIndex) {
            int previousStep = Color.HSBtoRGB((float)step / 6.0F, 1.0F, 1.0F);
            int nextStep = Color.HSBtoRGB((float)(step + 1) / 6.0F, 1.0F, 1.0F);
            gradient(x + step * (width / 6), y, x + (step + 1) * (width / 6) + 3, y + height, previousStep, nextStep, true);
            ++step;
         }

         int sliderMinX = (int)((float)x + (float)width * hue);
         Render2DUtil.rect(matrixStack, (float)(sliderMinX - 1), (float)y - 1.2F, (float)(sliderMinX + 1), (float)(y + height) + 1.2F, -1);
         drawOutlineRect((double)sliderMinX - 1.2, (double)y - 1.2, (double)sliderMinX + 1.2, (double)(y + height) + 1.2, Color.BLACK, 0.1F);
      }

   }

   public void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue, float alpha) {
      boolean left = true;
      int checkerBoardSquareSize = height / 2;

      for(int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
         if (!left) {
            Render2DUtil.rect(matrixStack, (float)(x + squareIndex), (float)y, (float)(x + squareIndex + checkerBoardSquareSize), (float)(y + height), -1);
            Render2DUtil.rect(matrixStack, (float)(x + squareIndex), (float)(y + checkerBoardSquareSize), (float)(x + squareIndex + checkerBoardSquareSize), (float)(y + height), -7303024);
            if (squareIndex < width - checkerBoardSquareSize) {
               int minX = x + squareIndex + checkerBoardSquareSize;
               int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
               Render2DUtil.rect(matrixStack, (float)minX, (float)y, (float)maxX, (float)(y + height), -7303024);
               Render2DUtil.rect(matrixStack, (float)minX, (float)(y + checkerBoardSquareSize), (float)maxX, (float)(y + height), -1);
            }
         }

         left = !left;
      }

      drawLeftGradientRect(x, y, x + width, y + height, (new Color(red, green, blue, 1.0F)).getRGB(), 0);
      int sliderMinX = (int)((float)(x + width) - (float)width * alpha);
      Render2DUtil.rect(matrixStack, (float)(sliderMinX - 1), (float)y - 1.2F, (float)(sliderMinX + 1), (float)(y + height) + 1.2F, -1);
      drawOutlineRect((double)sliderMinX - 1.2, (double)y - 1.2, (double)sliderMinX + 1.2, (double)(y + height) + 1.2, Color.BLACK, 0.1F);
   }
}
