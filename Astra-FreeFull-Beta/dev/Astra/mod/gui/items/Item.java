package dev.Astra.mod.gui.items;

import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.Mod;
import dev.Astra.mod.modules.impl.client.ClickGui;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_1109;
import net.minecraft.class_332;
import net.minecraft.class_3417;

public class Item extends Mod {
   public static class_332 context;
   protected float x;
   protected float y;
   protected int width;
   protected int height;
   private boolean hidden;
   private final Animation visibleHeightAnimation = new Animation();

   public Item(String name) {
      super(name);
   }

   public void setLocation(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public static void sound() {
      if (ClickGui.getInstance().sound.getValue()) {
         mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, ClickGui.getInstance().soundPitch.getValueFloat()));
      }

   }

   public void drawScreen(class_332 context, int mouseX, int mouseY, float partialTicks) {
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
   }

   public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
   }

   public void update() {
   }

   public void onKeyTyped(char typedChar, int keyCode) {
   }

   public void onKeyPressed(int key) {
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public void setHidden(boolean hidden) {
      this.hidden = hidden;
   }

   public double getVisibleHeight() {
      double target = this.hidden ? (double)0.0F : (double)this.getHeight();
      return this.visibleHeightAnimation.get(target, 200L, Easing.CubicInOut);
   }

   protected double getColorDelay() {
      return (double)this.y / (double)10.0F;
   }

   protected void drawString(String text, double x, double y, Color color) {
      this.drawString(text, x, y, color.hashCode());
   }

   protected void drawString(String text, double x, double y, int color) {
      boolean shadow = FontManager.isShadowEnabled();
      if (FontManager.isCustomFontEnabled()) {
         FontManager.ui.drawString(context.method_51448(), text, (double)((int)x), (double)((int)y), color, shadow);
      } else {
         context.method_51433(mc.field_1772, text, (int)x, (int)y, color, shadow);
      }

   }

   protected int getFontHeight() {
      if (FontManager.isCustomFontEnabled()) {
         return (int)FontManager.ui.getFontHeight();
      } else {
         Objects.requireNonNull(mc.field_1772);
         return 9;
      }
   }

   protected float getCenteredTextY(float baseY, float boxHeight) {
      return baseY + (boxHeight - (float)this.getFontHeight()) / 2.0F + (float)ClickGui.getInstance().textOffset.getValueInt();
   }

   protected int getWidth(String s) {
      return FontManager.isCustomFontEnabled() ? (int)FontManager.ui.getWidth(s) : mc.field_1772.method_1727(s);
   }
}
