package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;

public class AutoFrameDupe extends Module {
   SliderSetting range = this.add(new SliderSetting("Range", (double)1.0F, (double)5.0F, (double)7.0F));
   SliderSetting turns = this.add(new SliderSetting("Turns", 1, 5, 10));
   SliderSetting ticks = this.add(new SliderSetting("Ticks", 1, 5, 10));
   BooleanSetting switchxd = this.add(new BooleanSetting("Switch", true));
   private int timeoutTicks = 0;

   public AutoFrameDupe() {
      super("AutoFrameDupe", "Automatically dupes using frames", Module.Category.Misc);
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (mc.field_1687 != null && mc.field_1724 != null && mc.field_1761 != null) {
         for(class_1297 entity : mc.field_1687.method_18112()) {
            if (entity instanceof class_1533) {
               class_1533 frame = (class_1533)entity;
               if ((double)mc.field_1724.method_5739(frame) <= this.range.getValue()) {
                  if ((double)this.timeoutTicks >= this.ticks.getValue()) {
                     class_1799 displayedItem = frame.method_6940();
                     boolean hasItem = !displayedItem.method_7960();
                     boolean isHolding = !mc.field_1724.method_6047().method_7960();
                     if (this.switchxd.getValue() && (!isHolding || !this.isShulkerBox(mc.field_1724.method_6047()))) {
                        int shulkerSlot = this.findShulkers();
                        if (shulkerSlot != -1) {
                           mc.field_1724.method_31548().field_7545 = shulkerSlot;
                           isHolding = true;
                        }
                     }

                     if (!hasItem && isHolding) {
                        mc.field_1761.method_2905(mc.field_1724, frame, class_1268.field_5808);
                     }

                     if (hasItem) {
                        for(int i = 0; (double)i < this.turns.getValue(); ++i) {
                           mc.field_1761.method_2905(mc.field_1724, frame, class_1268.field_5808);
                        }

                        mc.field_1761.method_2918(mc.field_1724, frame);
                     }

                     this.timeoutTicks = 0;
                  } else {
                     ++this.timeoutTicks;
                  }
               }
            }
         }

      }
   }

   private boolean isShulkerBox(class_1799 stack) {
      class_1792 item = stack.method_7909();
      return item == class_1802.field_8545 || item == class_1802.field_8722 || item == class_1802.field_8380 || item == class_1802.field_8050 || item == class_1802.field_8829 || item == class_1802.field_8271 || item == class_1802.field_8548 || item == class_1802.field_8520 || item == class_1802.field_8627 || item == class_1802.field_8451 || item == class_1802.field_8213 || item == class_1802.field_8816 || item == class_1802.field_8350 || item == class_1802.field_8584 || item == class_1802.field_8461 || item == class_1802.field_8676 || item == class_1802.field_8268;
   }

   private int findShulkers() {
      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (this.isShulkerBox(stack)) {
            return i;
         }
      }

      return -1;
   }
}
