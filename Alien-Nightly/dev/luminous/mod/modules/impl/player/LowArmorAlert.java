package dev.luminous.mod.modules.impl.player;

import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1738;
import net.minecraft.class_1799;
import net.minecraft.class_332;

public class LowArmorAlert extends Module {
   public static LowArmorAlert INSTANCE;
   private final SliderSetting threshold = this.add(new SliderSetting("Threshold", (double)35.0F, (double)0.0F, (double)100.0F, (double)1.0F));
   private final SliderSetting yOffset = this.add(new SliderSetting("YOffset", (double)-60.0F, (double)0.0F, (double)200.0F, (double)1.0F));
   private long lastFlashTime;

   public LowArmorAlert() {
      super("LowArmorAlert", Module.Category.Player);
      this.setChinese("盔甲低耐久警报");
      INSTANCE = this;
   }

   public void onRender2D(class_332 drawContext, float tickDelta) {
      if (!nullCheck()) {
         double thresholdValue = this.threshold.getValue();
         int screenWidth = mc.method_22683().method_4486();
         int screenHeight = mc.method_22683().method_4502();
         int yOffset = (int)this.yOffset.getValue();
         boolean hasLowDurability = false;
         class_1799 helmet = (class_1799)mc.field_1724.method_31548().field_7548.get(3);
         if (!helmet.method_7960() && helmet.method_7909() instanceof class_1738 && (double)this.getDurabilityPercent(helmet) < thresholdValue) {
            hasLowDurability = true;
         }

         class_1799 chestplate;
         if (!(chestplate = (class_1799)mc.field_1724.method_31548().field_7548.get(2)).method_7960() && chestplate.method_7909() instanceof class_1738 && (double)this.getDurabilityPercent(chestplate) < thresholdValue) {
            hasLowDurability = true;
         }

         class_1799 leggings;
         if (!(leggings = (class_1799)mc.field_1724.method_31548().field_7548.get(1)).method_7960() && leggings.method_7909() instanceof class_1738 && (double)this.getDurabilityPercent(leggings) < thresholdValue) {
            hasLowDurability = true;
         }

         class_1799 boots;
         if (!(boots = (class_1799)mc.field_1724.method_31548().field_7548.get(0)).method_7960() && boots.method_7909() instanceof class_1738 && (double)this.getDurabilityPercent(boots) < thresholdValue) {
            hasLowDurability = true;
         }

         if (hasLowDurability) {
            this.drawWarning(drawContext, "Your armor durability is low!", screenWidth, screenHeight, yOffset);
         }

      }
   }

   private float getDurabilityPercent(class_1799 stack) {
      if (stack.method_7960()) {
         return 0.0F;
      } else {
         float maxDamage = (float)stack.method_7936();
         float currentDamage = (float)stack.method_7919();
         float durability = maxDamage - currentDamage;
         return durability / maxDamage * 100.0F;
      }
   }

   private void drawWarning(class_332 drawContext, String text, int screenWidth, int screenHeight, int yOffset) {
      int textWidth = mc.field_1772.method_1727(text);
      Objects.requireNonNull(mc.field_1772);
      int textHeight = 9;
      int x = (screenWidth - textWidth) / 2;
      int y = screenHeight / 2 - 100 + yOffset;
      long currentTime = System.currentTimeMillis();
      boolean shouldFlash = currentTime - this.lastFlashTime < 250L;
      if (currentTime - this.lastFlashTime >= 500L) {
         this.lastFlashTime = currentTime;
      }

      if (shouldFlash) {
         int padding = 2;
         int bgColor = -2136342528;
         int left = x - padding;
         int top = y - padding;
         int right = x + textWidth + padding;
         int bottom = y + textHeight + padding;
         drawContext.method_25294(left + 1, top, right - 1, top + 1, bgColor);
         drawContext.method_25294(left + 1, bottom - 1, right - 1, bottom, bgColor);
         drawContext.method_25294(left, top + 1, left + 1, bottom - 1, bgColor);
         drawContext.method_25294(right - 1, top + 1, right, bottom - 1, bgColor);
         drawContext.method_25294(left + 1, top + 1, right - 1, bottom - 1, bgColor);
      }

      drawContext.method_51433(mc.field_1772, text, x, y, -65536, false);
   }
}
