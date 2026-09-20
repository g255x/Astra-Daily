package dev.Astra.mod.gui.clickgui.pages;

import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.mod.Mod;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.gui.items.Component;
import dev.Astra.mod.gui.items.buttons.ModuleButton;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.impl.client.hud.HudSetting;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.class_332;
import net.minecraft.class_3675;

public final class ClickGuiModulePage {
   private final ClickGuiScreen host;

   public ClickGuiModulePage(ClickGuiScreen host) {
      this.host = host;
   }

   public void load() {
      ArrayList<Component> components = this.host.getComponents();
      components.clear();
      int categoryWidth = ClickGui.getInstance() != null ? ClickGui.getInstance().categoryWidth.getValueInt() : 101;
      int moduleButtonWidth = ClickGui.getInstance() != null ? ClickGui.getInstance().moduleButtonWidth.getValueInt() : 93;
      int layoutWidth = Math.max(categoryWidth, moduleButtonWidth);
      int spacing = layoutWidth + 1;
      int count = Module.Category.values().length;
      int startX = 10;
      int startY = 4;
      if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         int screenWidth = Wrapper.mc.method_22683().method_4486();
         int screenHeight = Wrapper.mc.method_22683().method_4502();
         int totalWidth = count * layoutWidth + (count - 1);
         startX = Math.round(((float)screenWidth - (float)totalWidth) / 2.0F);
         startY = Math.round((float)screenHeight / 6.0F);
      }

      int offsetX = Math.round(((float)layoutWidth - (float)moduleButtonWidth) / 2.0F);
      int x = startX - spacing;

      for(final Module.Category category : Module.Category.values()) {
         x += spacing;
         components.add(new Component(category.toString(), category, x + offsetX, startY, true) {
            public void setupItems() {
               for(Module module : Astra.MODULE.getModules()) {
                  if (module.getCategory().equals(category) && !ClickGuiModulePage.this.isHudComponentModule(module)) {
                     this.addButton(new ModuleButton(module));
                  }
               }

            }
         });
      }

      components.forEach((c) -> c.getItems().sort(Comparator.comparing(Mod::getName)));
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
      this.host.getComponents().forEach((c) -> c.drawScreen(context, mouseX, mouseY, delta));
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      this.host.getComponents().forEach((c) -> c.mouseClicked(mouseX, mouseY, mouseButton));
   }

   public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
      this.host.getComponents().forEach((c) -> c.mouseReleased(mouseX, mouseY, releaseButton));
   }

   public void keyPressed(int keyCode) {
      this.host.getComponents().forEach((c) -> c.onKeyPressed(keyCode));
   }

   public void charTyped(char chr, int modifiers) {
      this.host.getComponents().forEach((c) -> c.onKeyTyped(chr, modifiers));
   }

   public void mouseScrolled(double verticalAmount) {
      if (Wrapper.mc != null && Wrapper.mc.method_22683() != null) {
         if (class_3675.method_15987(Wrapper.mc.method_22683().method_4490(), 340)) {
            if (verticalAmount < (double)0.0F) {
               this.host.getComponents().forEach((component) -> component.setX(component.getTargetX() - 15));
            } else if (verticalAmount > (double)0.0F) {
               this.host.getComponents().forEach((component) -> component.setX(component.getTargetX() + 15));
            }
         } else if (verticalAmount < (double)0.0F) {
            this.host.getComponents().forEach((component) -> component.setY(component.getTargetY() - 15));
         } else if (verticalAmount > (double)0.0F) {
            this.host.getComponents().forEach((component) -> component.setY(component.getTargetY() + 15));
         }

      }
   }

   private boolean isHudComponentModule(Module module) {
      return module instanceof HudModule || module instanceof HudSetting;
   }
}
