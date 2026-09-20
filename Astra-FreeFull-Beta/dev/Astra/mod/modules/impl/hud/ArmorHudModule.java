package dev.Astra.mod.modules.impl.hud;

import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_1799;
import net.minecraft.class_332;

public class ArmorHudModule extends HudModule {
   public static ArmorHudModule INSTANCE;
   private final BooleanSetting durability = this.add(new BooleanSetting("Durability", true));
   private final Color minColor = new Color(255, 0, 0);
   private final Color maxColor = new Color(0, 255, 0);

   public ArmorHudModule() {
      super("Armor", "护甲", 0, 70);
      INSTANCE = this;
   }

   public void onRender2D(class_332 context, float tickDelta) {
      if (mc.field_1724 == null) {
         this.clearHudBounds();
      } else {
         int slot = 0;

         for(class_1799 armor : mc.field_1724.method_31548().field_7548) {
            ++slot;
         }

         int w = Math.max(1, 20 * slot);
         int topPad = this.durability.getValue() ? 6 : 0;
         int h = 16 + topPad;
         int boundsX = this.getHudRenderX(w);
         int boundsY = this.getHudRenderY(h);
         int px = boundsX;
         int py = boundsY + topPad;
         slot = 0;

         for(class_1799 armor : mc.field_1724.method_31548().field_7548) {
            int x = px + slot * 20;
            if (!armor.method_7960()) {
               context.method_51448().method_22903();
               int damage = EntityUtil.getDamagePercent(armor);
               context.method_51427(armor, x, py);
               context.method_51431(mc.field_1772, armor, x, py);
               if (this.durability.getValue()) {
                  if (HudSetting.useFont()) {
                     FontManager.small.drawString(context.method_51448(), damage + "%", (double)(x + 1), (double)((float)py - FontManager.small.getFontHeight() / 2.0F), ColorUtil.fadeColor(this.minColor, this.maxColor, (double)((float)damage / 100.0F)).getRGB(), HudSetting.useShadow());
                  } else {
                     String s = damage + "%";
                     float fx = (float)(x + 2);
                     float fy = (float)py;
                     Objects.requireNonNull(mc.field_1772);
                     TextUtil.drawStringScale(context, s, fx, fy - 2.25F, ColorUtil.fadeColor(this.minColor, this.maxColor, (double)((float)damage / 100.0F)).getRGB(), 0.5F, HudSetting.useShadow());
                  }
               }

               context.method_51448().method_22909();
            }

            ++slot;
         }

         this.setHudBounds(boundsX, boundsY, w, h);
      }
   }
}
