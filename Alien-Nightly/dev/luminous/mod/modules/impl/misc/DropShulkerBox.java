package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;

public class DropShulkerBox extends Module {
   private final Timer timer = new Timer();
   private final SliderSetting delay = this.add((new SliderSetting("Delay", (double)100.0F, (double)0.0F, (double)1000.0F, (double)10.0F)).setSuffix("ms"));
   private final BooleanSetting checkInventory = this.add(new BooleanSetting("CheckInventory", true));
   private final BooleanSetting checkHotbar = this.add(new BooleanSetting("CheckHotbar", true));
   private final BooleanSetting checkOffhand = this.add(new BooleanSetting("CheckOffhand", true));
   private final BooleanSetting dropFullShulkers = this.add(new BooleanSetting("DropFullShulkers", false));
   private final BooleanSetting onlyWhenEmpty = this.add(new BooleanSetting("OnlyWhenEmpty", false));

   public DropShulkerBox() {
      super("DropShulkerBox", Module.Category.Misc);
      this.setChinese("丢弃潜影盒");
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (this.timer.passedMs((double)this.delay.getValueInt())) {
            if (this.checkInventory.getValue()) {
               for(int i = 9; i < 36; ++i) {
                  class_1799 stack2 = mc.field_1724.method_31548().method_5438(i);
                  if (this.shouldDropShulker(stack2)) {
                     this.dropItem(i);
                     this.timer.reset();
                     return;
                  }
               }
            }

            if (this.checkHotbar.getValue()) {
               for(int i = 0; i < 9; ++i) {
                  class_1799 stack2 = mc.field_1724.method_31548().method_5438(i);
                  if (this.shouldDropShulker(stack2)) {
                     this.dropItem(i < 9 ? i + 36 : i);
                     this.timer.reset();
                     return;
                  }
               }
            }

            if (this.checkOffhand.getValue() && this.shouldDropShulker(mc.field_1724.method_6079())) {
               this.dropItem(45);
               this.timer.reset();
            }
         }
      }
   }

   private boolean shouldDropShulker(class_1799 stack) {
      return !stack.method_7960() && stack.method_7909() == class_1802.field_8545 ? false : false;
   }

   private void dropItem(int slot) {
      int syncId = mc.field_1724.field_7512.field_7763;
      mc.field_1761.method_2906(syncId, slot, 1, class_1713.field_7795, mc.field_1724);
   }
}
