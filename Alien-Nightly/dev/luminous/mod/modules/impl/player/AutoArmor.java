package dev.luminous.mod.modules.impl.player;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1268;
import net.minecraft.class_1304;
import net.minecraft.class_1713;
import net.minecraft.class_1738;
import net.minecraft.class_1770;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2838;
import net.minecraft.class_2886;
import net.minecraft.class_6880;
import net.minecraft.class_9304;

public class AutoArmor extends Module {
   public static AutoArmor INSTANCE;
   private final EnumSetting<EnchantPriority> head;
   private final EnumSetting<EnchantPriority> body;
   private final EnumSetting<EnchantPriority> tights;
   private final EnumSetting<EnchantPriority> feet;
   private final BooleanSetting ignoreCurse;
   private final BooleanSetting noMove;
   private final SliderSetting delay;
   private final BooleanSetting autoElytra;
   private final EnumSetting<HotbarSwapMode> hotbarSwap;
   private final EnumSetting<InventorySwapMode> inventorySwap;
   private int tickDelay;

   public AutoArmor() {
      super("AutoArmor", Module.Category.Player);
      this.head = this.add(new EnumSetting("Head", AutoArmor.EnchantPriority.Protection));
      this.body = this.add(new EnumSetting("Body", AutoArmor.EnchantPriority.Protection));
      this.tights = this.add(new EnumSetting("Tights", AutoArmor.EnchantPriority.Protection));
      this.feet = this.add(new EnumSetting("Feet", AutoArmor.EnchantPriority.Protection));
      this.ignoreCurse = this.add(new BooleanSetting("IgnoreCurse", true));
      this.noMove = this.add(new BooleanSetting("NoMove", false));
      this.delay = this.add(new SliderSetting("Delay", (double)3.0F, (double)0.0F, (double)10.0F, (double)1.0F));
      this.autoElytra = this.add(new BooleanSetting("AutoElytra", true));
      this.hotbarSwap = this.add(new EnumSetting("HotbarSwap", AutoArmor.HotbarSwapMode.Swap));
      this.inventorySwap = this.add(new EnumSetting("InventorySwap", AutoArmor.InventorySwapMode.ClickSlot));
      this.tickDelay = 0;
      this.setChinese("自动穿甲");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (EntityUtil.inInventory() && mc.field_1724.field_7498 == mc.field_1724.field_7512 && (!MovementUtil.isMoving() || !this.noMove.getValue())) {
         if (this.tickDelay > 0) {
            --this.tickDelay;
         } else {
            this.tickDelay = this.delay.getValueInt();
            Map<class_1304, int[]> armorMap = new HashMap(4);
            armorMap.put(class_1304.field_6166, new int[]{36, this.getProtection(mc.field_1724.method_31548().method_5438(36)), -1, -1});
            armorMap.put(class_1304.field_6172, new int[]{37, this.getProtection(mc.field_1724.method_31548().method_5438(37)), -1, -1});
            armorMap.put(class_1304.field_6174, new int[]{38, this.getProtection(mc.field_1724.method_31548().method_5438(38)), -1, -1});
            armorMap.put(class_1304.field_6169, new int[]{39, this.getProtection(mc.field_1724.method_31548().method_5438(39)), -1, -1});

            for(int s = 0; s < 36; ++s) {
               if ((mc.field_1724.method_31548().method_5438(s).method_7909() instanceof class_1738 || mc.field_1724.method_31548().method_5438(s).method_7909() == class_1802.field_8833) && (mc.field_1724.method_31548().method_5438(s).method_7909() != class_1802.field_8833 || (!ElytraFly.INSTANCE.isOff() || !this.autoElytra.getValue()) && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.packet.getValue()))) {
                  int protection = this.getProtection(mc.field_1724.method_31548().method_5438(s));
                  class_1304 slot = mc.field_1724.method_31548().method_5438(s).method_7909() instanceof class_1770 ? class_1304.field_6174 : ((class_1738)mc.field_1724.method_31548().method_5438(s).method_7909()).method_7685();

                  for(Map.Entry<class_1304, int[]> e : armorMap.entrySet()) {
                     if (this.autoElytra.getValue() && ElytraFly.INSTANCE.isOn() && e.getKey() == class_1304.field_6174) {
                        if ((mc.field_1724.method_31548().method_5438(38).method_7960() || !(mc.field_1724.method_31548().method_5438(38).method_7909() instanceof class_1770) || !class_1770.method_7804(mc.field_1724.method_31548().method_5438(38))) && (((int[])e.getValue())[2] == -1 || mc.field_1724.method_31548().method_5438(((int[])e.getValue())[2]).method_7960() || !(mc.field_1724.method_31548().method_5438(((int[])e.getValue())[2]).method_7909() instanceof class_1770) || !class_1770.method_7804(mc.field_1724.method_31548().method_5438(((int[])e.getValue())[2]))) && !mc.field_1724.method_31548().method_5438(s).method_7960() && mc.field_1724.method_31548().method_5438(s).method_7909() instanceof class_1770 && class_1770.method_7804(mc.field_1724.method_31548().method_5438(s))) {
                           ((int[])e.getValue())[2] = s;
                        }
                     } else if (protection > 0 && e.getKey() == slot && protection > ((int[])e.getValue())[1] && protection > ((int[])e.getValue())[3]) {
                        ((int[])e.getValue())[2] = s;
                        ((int[])e.getValue())[3] = protection;
                     }
                  }
               }
            }

            for(Map.Entry<class_1304, int[]> equipmentSlotEntry : armorMap.entrySet()) {
               if (((int[])equipmentSlotEntry.getValue())[2] != -1) {
                  if (((int[])equipmentSlotEntry.getValue())[2] < 9) {
                     switch (((HotbarSwapMode)this.hotbarSwap.getValue()).ordinal()) {
                        case 0:
                           int armorSlot = 44 - ((int[])equipmentSlotEntry.getValue())[0];
                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, armorSlot, ((int[])equipmentSlotEntry.getValue())[2], class_1713.field_7791, mc.field_1724);
                           EntityUtil.syncInventory();
                           break;
                        case 1:
                           int old = mc.field_1724.method_31548().field_7545;
                           InventoryUtil.switchToSlot(((int[])equipmentSlotEntry.getValue())[2]);
                           sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                           InventoryUtil.switchToSlot(old);
                     }
                  } else if (mc.field_1724.field_7498 == mc.field_1724.field_7512) {
                     int armorSlot = 44 - ((int[])equipmentSlotEntry.getValue())[0];
                     int newArmorSlot = ((int[])equipmentSlotEntry.getValue())[2];
                     switch (((InventorySwapMode)this.inventorySwap.getValue()).ordinal()) {
                        case 0:
                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, newArmorSlot, 0, class_1713.field_7790, mc.field_1724);
                           mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, armorSlot, 0, class_1713.field_7790, mc.field_1724);
                           if (((int[])equipmentSlotEntry.getValue())[1] != -1) {
                              mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, newArmorSlot, 0, class_1713.field_7790, mc.field_1724);
                           }
                           break;
                        case 1:
                           mc.method_1562().method_52787(new class_2838(newArmorSlot));
                           sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                           mc.method_1562().method_52787(new class_2838(newArmorSlot));
                     }

                     EntityUtil.syncInventory();
                  }

                  return;
               }
            }
         }
      }

   }

   private int getProtection(class_1799 is) {
      if (!(is.method_7909() instanceof class_1738) && is.method_7909() != class_1802.field_8833) {
         return !is.method_7960() ? 0 : -1;
      } else {
         int prot = 0;
         class_1792 var5 = is.method_7909();
         class_1304 var10000;
         if (var5 instanceof class_1738) {
            class_1738 ai = (class_1738)var5;
            var10000 = ai.method_7685();
         } else {
            var10000 = class_1304.field_48824;
         }

         class_1304 slot = var10000;
         if (is.method_7909() instanceof class_1770) {
            if (!class_1770.method_7804(is)) {
               return 0;
            }

            prot = 1;
         }

         int blastMultiplier = 1;
         int protectionMultiplier = 1;
         switch (slot) {
            case field_6169:
               if (this.head.is(AutoArmor.EnchantPriority.Protection)) {
                  protectionMultiplier *= 2;
               } else {
                  blastMultiplier *= 2;
               }
               break;
            case field_48824:
               if (this.body.is(AutoArmor.EnchantPriority.Protection)) {
                  protectionMultiplier *= 2;
               } else {
                  blastMultiplier *= 2;
               }
               break;
            case field_6172:
               if (this.tights.is(AutoArmor.EnchantPriority.Protection)) {
                  protectionMultiplier *= 2;
               } else {
                  blastMultiplier *= 2;
               }
               break;
            case field_6166:
               if (this.feet.is(AutoArmor.EnchantPriority.Protection)) {
                  protectionMultiplier *= 2;
               } else {
                  blastMultiplier *= 2;
               }
         }

         if (is.method_7942()) {
            class_9304 enchants = class_1890.method_57532(is);
            if (enchants.method_57534().contains(mc.field_1687.method_30349().method_46762(class_1893.field_9111.method_58273()).method_46746(class_1893.field_9111).get())) {
               prot += enchants.method_57536((class_6880)mc.field_1687.method_30349().method_46762(class_1893.field_9111.method_58273()).method_46746(class_1893.field_9111).get()) * protectionMultiplier;
            }

            if (enchants.method_57534().contains(mc.field_1687.method_30349().method_46762(class_1893.field_9107.method_58273()).method_46746(class_1893.field_9107).get())) {
               prot += enchants.method_57536((class_6880)mc.field_1687.method_30349().method_46762(class_1893.field_9107.method_58273()).method_46746(class_1893.field_9107).get()) * blastMultiplier;
            }

            if (enchants.method_57534().contains(mc.field_1687.method_30349().method_46762(class_1893.field_9107.method_58273()).method_46746(class_1893.field_9113).get()) && this.ignoreCurse.getValue()) {
               prot = -999;
            }
         }

         return (is.method_7909() instanceof class_1738 ? ((class_1738)is.method_7909()).method_7687() : 0) + prot;
      }
   }

   private static enum EnchantPriority {
      Blast,
      Protection;

      // $FF: synthetic method
      private static EnchantPriority[] $values() {
         return new EnchantPriority[]{Blast, Protection};
      }
   }

   public static enum HotbarSwapMode {
      Swap,
      Switch;

      // $FF: synthetic method
      private static HotbarSwapMode[] $values() {
         return new HotbarSwapMode[]{Swap, Switch};
      }
   }

   public static enum InventorySwapMode {
      ClickSlot,
      Pick;

      // $FF: synthetic method
      private static InventorySwapMode[] $values() {
         return new InventorySwapMode[]{ClickSlot, Pick};
      }
   }
}
