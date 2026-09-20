package dev.Astra.mod.modules.impl.client.hud;

import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import java.util.Objects;
import net.minecraft.class_1937;
import net.minecraft.class_332;

public class CoordsHudModule extends HudModule {
   public static CoordsHudModule INSTANCE;
   private final BooleanSetting lowerCase = this.add(new BooleanSetting("LowerCase", false));

   public CoordsHudModule() {
      super("Coords", "", "坐标", 2, 2, HudModule.Corner.LeftBottom);
      INSTANCE = this;
   }

   public void onRender2D(class_332 context, float tickDelta) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         String text = this.getCoordsString();
         if (this.lowerCase.getValue()) {
            text = text.toLowerCase();
         }

         int w = HudSetting.useFont() ? (int)Math.ceil((double)FontManager.ui.getWidth(text)) : mc.field_1772.method_1727(text);
         int h;
         if (HudSetting.useFont()) {
            h = (int)Math.ceil((double)FontManager.ui.getFontHeight());
         } else {
            Objects.requireNonNull(mc.field_1772);
            h = 9;
         }

         int x = this.getHudRenderX(w);
         int y = this.getHudRenderY(h);
         int color = this.getHudColor((double)0.0F);
         TextUtil.drawString(context, text, (double)x, (double)y, color, HudSetting.useFont(), HudSetting.useShadow());
         this.setHudBounds(x, y, Math.max(1, w), Math.max(1, h));
      } else {
         this.clearHudBounds();
      }
   }

   private int getHudColor(double delay) {
      ClickGui gui = ClickGui.getInstance();
      return gui == null ? -1 : gui.getColor(delay).getRGB();
   }

   private String getCoordsString() {
      boolean inNether = mc.field_1687.method_27983().equals(class_1937.field_25180);
      int posX = mc.field_1724.method_31477();
      int posY = mc.field_1724.method_31478();
      int posZ = mc.field_1724.method_31479();
      float factor = !inNether ? 0.125F : 8.0F;
      int anotherWorldX = (int)(mc.field_1724.method_23317() * (double)factor);
      int anotherWorldZ = (int)(mc.field_1724.method_23321() * (double)factor);
      String var10000 = inNether ? posX + ", " + posY + ", " + posZ + " §7[§f" + anotherWorldX + ", " + anotherWorldZ + "§7]§f" : posX + ", " + posY + ", " + posZ + "§7 [§f" + anotherWorldX + ", " + anotherWorldZ + "§7]";
      return "XYZ §f" + var10000;
   }
}
