package dev.luminous.mod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.Mod;
import dev.luminous.mod.gui.items.Component;
import dev.luminous.mod.gui.items.Item;
import dev.luminous.mod.gui.items.buttons.ModuleButton;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClickGui;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_437;

public class ClickGuiScreen extends class_437 {
   private static ClickGuiScreen INSTANCE = new ClickGuiScreen();
   private final ArrayList<Component> components = new ArrayList();

   public ClickGuiScreen() {
      super(class_2561.method_43470("Alien"));
      this.setInstance();
      this.load();
   }

   public static ClickGuiScreen getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new ClickGuiScreen();
      }

      return INSTANCE;
   }

   private void setInstance() {
      INSTANCE = this;
   }

   private void load() {
      int x = -84;

      for(final Module.Category category : Module.Category.values()) {
         String var10004 = category.toString();
         x += 94;
         this.components.add(new Component(var10004, category, x, 4, true) {
            public void setupItems() {
               for(Module module : Alien.MODULE.getModules()) {
                  if (module.getCategory().equals(category)) {
                     this.addButton(new ModuleButton(module));
                  }
               }

            }
         });
      }

      this.components.forEach((components) -> components.getItems().sort(Comparator.comparing(Mod::getName)));
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      float a = (float)ClickGui.getInstance().alphaValue;
      float scale = 0.92F + 0.08F * a;
      float slideY = (1.0F - a) * 20.0F;
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, a);
      Item.context = context;
      this.method_25420(context, mouseX, mouseY, delta);
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;

      for(Component c : this.components) {
         minX = Math.min(minX, c.getX());
         minY = Math.min(minY, c.getY());
         maxX = Math.max(maxX, c.getX() + c.getWidth());
         maxY = Math.max(maxY, c.getY() + c.getHeight());
      }

      int margin = 16;
      int panelX = Math.max(8, minX - margin);
      int panelY = Math.max(6, minY - margin);
      int panelW = Math.min(context.method_51421() - panelX - 8, maxX - minX + margin * 2);
      int panelH = Math.min(context.method_51443() - panelY - 6, maxY - minY + margin * 2 + 24);
      boolean focused = mouseX >= panelX && mouseX <= panelX + panelW && mouseY >= panelY && mouseY <= panelY + panelH;
      int alpha = focused ? (int)Math.round((double)242.25F) : (int)Math.round(226.95000000000002);
      float r = 4.0F;
      context.method_51448().method_22903();
      context.method_51448().method_46416((float)panelX + (float)panelW / 2.0F, (float)panelY + (float)panelH / 2.0F + slideY, 0.0F);
      context.method_51448().method_22905(scale, scale, 1.0F);
      context.method_51448().method_46416(-((float)panelX + (float)panelW / 2.0F), -((float)panelY + (float)panelH / 2.0F), 0.0F);
      int strokeA = Math.max(0, Math.min(255, (int)Math.round((double)alpha * 0.22)));
      context.method_51448().method_22909();
      context.method_51448().method_22903();
      context.method_51448().method_46416(0.0F, slideY, 0.0F);
      context.method_51448().method_22905(scale, scale, 1.0F);
      this.components.forEach((component) -> component.drawScreen(context, mouseX, mouseY, delta));
      context.method_51448().method_22909();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean method_25402(double mouseX, double mouseY, int clickedButton) {
      this.components.forEach((components) -> components.mouseClicked((int)mouseX, (int)mouseY, clickedButton));
      return super.method_25402(mouseX, mouseY, clickedButton);
   }

   public boolean method_25406(double mouseX, double mouseY, int releaseButton) {
      this.components.forEach((components) -> components.mouseReleased((int)mouseX, (int)mouseY, releaseButton));
      return super.method_25406(mouseX, mouseY, releaseButton);
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (class_3675.method_15987(Wrapper.mc.method_22683().method_4490(), 340)) {
         if (verticalAmount < (double)0.0F) {
            this.components.forEach((component) -> component.setX(component.getX() - 15));
         } else if (verticalAmount > (double)0.0F) {
            this.components.forEach((component) -> component.setX(component.getX() + 15));
         }
      } else if (verticalAmount < (double)0.0F) {
         this.components.forEach((component) -> component.setY(component.getY() - 15));
      } else if (verticalAmount > (double)0.0F) {
         this.components.forEach((component) -> component.setY(component.getY() + 15));
      }

      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      this.components.forEach((component) -> component.onKeyPressed(keyCode));
      return super.method_25404(keyCode, scanCode, modifiers);
   }

   public boolean method_25400(char chr, int modifiers) {
      this.components.forEach((component) -> component.onKeyTyped(chr, modifiers));
      return super.method_25400(chr, modifiers);
   }

   public boolean method_25421() {
      return false;
   }

   public final ArrayList<Component> getComponents() {
      return this.components;
   }

   public int getTextOffset() {
      return -ClickGui.getInstance().textOffset.getValueInt() - 6;
   }
}
