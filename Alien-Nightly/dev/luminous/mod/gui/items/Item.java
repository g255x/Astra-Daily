package dev.luminous.mod.gui.items;

import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.Mod;
import dev.luminous.mod.modules.impl.client.ClickGui;
import java.awt.Color;
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

   protected void drawString(String text, double x, double y, Color color) {
      this.drawString(text, x, y, color.hashCode());
   }

   protected void drawString(String text, double x, double y, int color) {
      if (ClickGui.getInstance().font.getValue()) {
         FontManager.ui.drawString(context.method_51448(), text, (double)((int)x), (double)((int)y), color, ClickGui.getInstance().shadow.getValue());
      } else {
         context.method_51433(mc.field_1772, text, (int)x, (int)y, color, ClickGui.getInstance().shadow.getValue());
      }

   }

   protected int getFontHeight() {
      return ClickGui.getInstance().font.getValue() ? (int)FontManager.ui.getFontHeight() : 9;
   }

   protected int getWidth(String s) {
      return ClickGui.getInstance().font.getValue() ? (int)FontManager.ui.getWidth(s) : mc.field_1772.method_1727(s);
   }
}
