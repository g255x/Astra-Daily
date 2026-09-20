package dev.Astra.mod.modules.impl.client.hud;

import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.util.Objects;
import net.minecraft.class_332;

public class WaterMarkHudModule extends HudModule {
   public static WaterMarkHudModule INSTANCE;
   public final StringSetting title = this.add(new StringSetting("Title", "%hackname% %version%"));

   public WaterMarkHudModule() {
      super("WaterMark", "水印", 1, 1);
      INSTANCE = this;
   }

   public void onRender2D(class_332 context, float tickDelta) {
      String text = this.title.getValue().replaceAll("%version%", "V1.3.1").replaceAll("%hackname%", "Astra Client");
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
   }

   private int getHudColor(double delay) {
      ClickGui gui = ClickGui.getInstance();
      return gui == null ? -1 : gui.getColor(delay).getRGB();
   }
}
