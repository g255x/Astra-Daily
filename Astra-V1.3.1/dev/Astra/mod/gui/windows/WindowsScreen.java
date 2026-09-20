package dev.Astra.mod.gui.windows;

import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.modules.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class WindowsScreen extends class_437 {
   public static WindowBase lastClickedWindow;
   public static WindowBase draggingWindow;
   private List<WindowBase> windows = new ArrayList();

   public WindowsScreen(WindowBase... windows) {
      super(class_2561.method_30163("CustomWindows"));
      this.windows.clear();
      lastClickedWindow = null;
      this.windows = Arrays.stream(windows).toList();
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      if (Module.nullCheck()) {
         this.method_25420(context, mouseX, mouseY, delta);
      }

      this.windows.stream().filter(WindowBase::isVisible).forEach((w) -> {
         if (w != lastClickedWindow) {
            w.render(context, mouseX, mouseY);
         }

      });
      if (lastClickedWindow != null && lastClickedWindow.isVisible()) {
         lastClickedWindow.render(context, mouseX, mouseY);
      }

   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      this.windows.forEach((w) -> w.mouseReleased(mouseX, mouseY, button));
      return super.method_25406(mouseX, mouseY, button);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      this.windows.stream().filter(WindowBase::isVisible).forEach((w) -> w.mouseClicked(mouseX, mouseY, button));
      int i = Wrapper.mc.method_22683().method_4486() / 2;
      float offset = (float)this.windows.size() * 20.0F / -2.0F - 23.0F;
      if (Render2DUtil.isHovered(mouseX, mouseY, (double)((float)i + offset + 1.0F), (double)(Wrapper.mc.method_22683().method_4502() - 23), (double)15.0F, (double)15.0F)) {
         Wrapper.mc.method_1507(ClickGuiScreen.getInstance());
      }

      offset += 23.0F;

      for(WindowBase w2 : this.windows) {
         if (Render2DUtil.isHovered(mouseX, mouseY, (double)((float)i + offset), (double)(Wrapper.mc.method_22683().method_4502() - 24), (double)17.0F, (double)17.0F)) {
            w2.setVisible(!w2.isVisible());
         }

         offset += 20.0F;
      }

      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      this.windows.stream().filter(WindowBase::isVisible).forEach((w) -> w.keyPressed(keyCode, scanCode, modifiers));
      return super.method_25404(keyCode, scanCode, modifiers);
   }

   public boolean method_25400(char key, int keyCode) {
      this.windows.stream().filter(WindowBase::isVisible).forEach((w) -> w.charTyped(key, keyCode));
      return super.method_25400(key, keyCode);
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      this.windows.stream().filter(WindowBase::isVisible).forEach((w) -> w.mouseScrolled((int)(verticalAmount * (double)5.0F)));
      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }
}
