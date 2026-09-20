package dev.Astra.mod.modules.impl.player;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1713;
import net.minecraft.class_1799;

public class Replenish extends Module {
   private final EnumSetting<Mode> mode;
   private final SliderSetting delay;
   private final SliderSetting min;
   private final Timer timer;

   public Replenish() {
      super("Replenish", Module.Category.Player);
      this.mode = this.add(new EnumSetting("Mode", Replenish.Mode.QuickMove));
      this.delay = this.add((new SliderSetting("Delay", (double)2.0F, (double)0.0F, (double)5.0F, 0.01)).setSuffix("s"));
      this.min = this.add(new SliderSetting("Min", 50, 1, 100)).setSuffix("%");
      this.timer = new Timer();
      this.setChinese("物品栏补充");
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      for(int i = 0; i < 9; ++i) {
         if (this.replenish(i)) {
            this.timer.reset();
            return;
         }
      }

   }

   private boolean replenish(int slot) {
      class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
      if (stack.method_7960()) {
         return false;
      } else if (!stack.method_7946()) {
         return false;
      } else {
         int percent = (int)((double)stack.method_7947() / (double)stack.method_7914() * (double)100.0F);
         if ((double)percent > this.min.getValue()) {
            return false;
         } else if (!this.timer.passedS(this.delay.getValue())) {
            return false;
         } else {
            for(int i = 9; i < 36; ++i) {
               class_1799 item = mc.field_1724.method_31548().method_5438(i);
               if (!item.method_7960() && this.canMerge(stack, item)) {
                  if (this.mode.getValue() == Replenish.Mode.QuickMove) {
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, i, 0, class_1713.field_7794, mc.field_1724);
                  } else {
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, i, 0, class_1713.field_7790, mc.field_1724);
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot + 36, 0, class_1713.field_7790, mc.field_1724);
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, i, 0, class_1713.field_7790, mc.field_1724);
                  }

                  return true;
               }
            }

            return false;
         }
      }
   }

   private boolean canMerge(class_1799 a, class_1799 b) {
      return class_1799.method_31577(a, b);
   }

   public static enum Mode {
      QuickMove,
      ClickSlot;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{QuickMove, ClickSlot};
      }
   }
}
