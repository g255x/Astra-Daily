package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2886;

public class OffFirework extends Module {
   public static OffFirework INSTANCE;
   public final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));

   public OffFirework() {
      super("OffFirework", Module.Category.Player);
      this.setChinese("放烟花");
      INSTANCE = this;
   }

   public boolean onEnable() {
      if (nullCheck()) {
         this.disable();
      }

      this.off();
      this.disable();
      return false;
   }

   public void off() {
      ElytraFly.INSTANCE.fireworkTimer.reset();
      if (mc.field_1724.method_6047().method_7909() == class_1802.field_8639) {
         sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455()));
      } else {
         int firework;
         if (this.inventory.getValue() && (firework = InventoryUtil.findItemInventorySlot(class_1802.field_8639)) != -1) {
            InventoryUtil.inventorySwap(firework, mc.field_1724.method_31548().field_7545);
            sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455()));
            InventoryUtil.inventorySwap(firework, mc.field_1724.method_31548().field_7545);
            EntityUtil.syncInventory();
         } else if ((firework = InventoryUtil.findItem(class_1802.field_8639)) != -1) {
            int old = mc.field_1724.method_31548().field_7545;
            InventoryUtil.switchToSlot(firework);
            sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, mc.field_1724.method_36454(), mc.field_1724.method_36455()));
            InventoryUtil.switchToSlot(old);
         }
      }

   }
}
