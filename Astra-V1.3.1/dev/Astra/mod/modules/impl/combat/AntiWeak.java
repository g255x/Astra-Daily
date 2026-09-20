package dev.Astra.mod.modules.impl.combat;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1294;
import net.minecraft.class_1511;
import net.minecraft.class_1829;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2824.class_5907;

public class AntiWeak extends Module {
   private final EnumSetting<SwapMode> swapMode;
   private final SliderSetting delay;
   private final BooleanSetting onlyCrystal;
   private final Timer delayTimer;
   boolean ignore;
   private class_2824 lastPacket;

   public AntiWeak() {
      super("AntiWeak", Module.Category.Combat);
      this.swapMode = this.add(new EnumSetting("SwapMode", AntiWeak.SwapMode.Inventory));
      this.delay = this.add((new SliderSetting("Delay", 100, 0, 500)).setSuffix("ms"));
      this.onlyCrystal = this.add(new BooleanSetting("OnlyCrystal", true));
      this.delayTimer = new Timer();
      this.ignore = false;
      this.lastPacket = null;
      this.setChinese("反虚弱");
   }

   public String getInfo() {
      return ((SwapMode)this.swapMode.getValue()).name();
   }

   @EventListener(
      priority = 200
   )
   public void onPacketSend(PacketEvent.Send event) {
      if (!nullCheck()) {
         if (!event.isCancelled()) {
            if (!this.ignore) {
               if (mc.field_1724.method_6112(class_1294.field_5911) != null) {
                  if (!(mc.field_1724.method_6047().method_7909() instanceof class_1829)) {
                     if (this.delayTimer.passedMs(this.delay.getValue())) {
                        class_2596<?> packet2 = event.getPacket();
                        class_2824 packet;
                        if (packet2 instanceof class_2824 && Criticals.getInteractType(packet = (class_2824)packet2) == class_5907.field_29172) {
                           if (this.onlyCrystal.getValue() && !(Criticals.getEntity(packet) instanceof class_1511)) {
                              return;
                           }

                           this.lastPacket = packet;
                           this.delayTimer.reset();
                           this.ignore = true;
                           this.doAnti();
                           this.ignore = false;
                           event.cancel();
                        }

                     }
                  }
               }
            }
         }
      }
   }

   private void doAnti() {
      if (this.lastPacket != null) {
         int strong = this.swapMode.getValue() != AntiWeak.SwapMode.Inventory ? InventoryUtil.findClass(class_1829.class) : InventoryUtil.findClassInventorySlot(class_1829.class);
         if (strong != -1) {
            int old = mc.field_1724.method_31548().field_7545;
            if (this.swapMode.getValue() != AntiWeak.SwapMode.Inventory) {
               InventoryUtil.switchToSlot(strong);
            } else {
               InventoryUtil.inventorySwap(strong, mc.field_1724.method_31548().field_7545);
            }

            mc.method_1562().method_52787(this.lastPacket);
            if (this.swapMode.getValue() != AntiWeak.SwapMode.Inventory) {
               if (this.swapMode.getValue() != AntiWeak.SwapMode.Normal) {
                  InventoryUtil.switchToSlot(old);
               }
            } else {
               InventoryUtil.inventorySwap(strong, mc.field_1724.method_31548().field_7545);
               EntityUtil.syncInventory();
            }

         }
      }
   }

   public static enum SwapMode {
      Normal,
      Silent,
      Inventory;

      // $FF: synthetic method
      private static SwapMode[] $values() {
         return new SwapMode[]{Normal, Silent, Inventory};
      }
   }
}
