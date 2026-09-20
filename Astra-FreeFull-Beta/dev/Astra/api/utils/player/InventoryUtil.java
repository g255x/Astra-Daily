package dev.Astra.api.utils.player;

import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1713;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2868;
import net.minecraft.class_9334;

public class InventoryUtil implements Wrapper {
   private static int lastSlot = -1;
   private static int lastSelect = -1;

   public static void inventorySwap(int slot, int selectedSlot) {
      if (slot == lastSlot) {
         switchToSlot(lastSelect);
         lastSlot = -1;
         lastSelect = -1;
      } else if (slot - 36 != selectedSlot) {
         if (EntityUtil.inInventory()) {
            if (AntiCheat.INSTANCE.invSwapBypass.getValue()) {
               if (slot - 36 >= 0) {
                  lastSlot = slot;
                  lastSelect = selectedSlot;
                  switchToSlot(slot - 36);
               }
            } else {
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, selectedSlot, class_1713.field_7791, mc.field_1724);
               mc.field_1724.method_31548().method_7381();
            }

         }
      }
   }

   public static void switchToSlot(int slot) {
      mc.field_1724.method_31548().field_7545 = slot;
      mc.method_1562().method_52787(new class_2868(slot));
   }

   public static int findItem(class_1792 input) {
      for(int i = 0; i < 9; ++i) {
         if (mc.field_1724.method_31548().method_5438(i).method_7909() == input) {
            return i;
         }
      }

      return -1;
   }

   public static int getFood() {
      for(int i = 0; i < 9; ++i) {
         if (mc.field_1724.method_31548().method_5438(i).method_57826(class_9334.field_50075)) {
            return i;
         }
      }

      return -1;
   }

   public static int getPotionCount(class_1291 targetEffect) {
      int count = 0;

      for(int i = 35; i >= 0; --i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (stack.method_7909() == class_1802.field_8436) {
            class_1844 potion = (class_1844)stack.method_57824(class_9334.field_49651);
            if (potion != null) {
               for(class_1293 effect : potion.method_57397()) {
                  if (effect.method_5579().comp_349() == targetEffect) {
                     count += stack.method_7947();
                     break;
                  }
               }
            }
         }
      }

      return count;
   }

   public static int getItemCount(Class<?> clazz) {
      int count = 0;

      for(class_1799 stack : getInventoryAndHotbarSlots().values()) {
         if (stack.method_7909() instanceof class_1747 && clazz.isInstance(((class_1747)stack.method_7909()).method_7711())) {
            count += stack.method_7947();
         }
      }

      return count;
   }

   public static int getItemCount(class_1792 item) {
      int count = 0;

      for(class_1799 stack : getInventoryAndHotbarSlots().values()) {
         if (stack.method_7909() == item) {
            count += stack.method_7947();
         }
      }

      if (mc.field_1724.method_6079().method_7909() == item) {
         count += mc.field_1724.method_6079().method_7947();
      }

      return count;
   }

   public static int findClass(Class<?> clazz) {
      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960()) {
            class_1792 item = stack.method_7909();
            if (clazz.isInstance(item)) {
               return i;
            }

            if (item instanceof class_1747 && clazz.isInstance(((class_1747)item).method_7711())) {
               return i;
            }
         }
      }

      return -1;
   }

   public static int findClassInventorySlot(Class<?> clazz) {
      boolean fromZero = AntiCheat.INSTANCE.priorHotbar.getValue();
      int i = fromZero ? 0 : 35;

      while(true) {
         if (fromZero) {
            if (i >= 36) {
               break;
            }
         } else if (i < 0) {
            break;
         }

         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960()) {
            class_1792 item = stack.method_7909();
            if (clazz.isInstance(item) || item instanceof class_1747 && clazz.isInstance(((class_1747)item).method_7711())) {
               return i < 9 ? i + 36 : i;
            }
         }

         i += fromZero ? 1 : -1;
      }

      return -1;
   }

   public static int findBlock(class_2248 blockIn) {
      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960() && stack.method_7909() instanceof class_1747 && ((class_1747)stack.method_7909()).method_7711() == blockIn) {
            return i;
         }
      }

      return -1;
   }

   public static int findUnBlock() {
      for(int i = 0; i < 9; ++i) {
         if (!(mc.field_1724.method_31548().method_5438(i).method_7909() instanceof class_1747)) {
            return i;
         }
      }

      return -1;
   }

   public static int findBlock() {
      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (stack.method_7909() instanceof class_1747) {
            class_2248 block = ((class_1747)stack.method_7909()).method_7711();
            if (!BlockUtil.isClickable(block) && block != class_2246.field_10343) {
               return i;
            }
         }
      }

      return -1;
   }

   public static int findBlockInventorySlot(class_2248 block) {
      return findItemInventorySlot(block.method_8389());
   }

   public static int findItemInventorySlot(class_1792 item) {
      boolean fromZero = AntiCheat.INSTANCE.priorHotbar.getValue();
      int i = fromZero ? 0 : 35;

      while(true) {
         if (fromZero) {
            if (i >= 36) {
               break;
            }
         } else if (i < 0) {
            break;
         }

         if (mc.field_1724.method_31548().method_5438(i).method_7909() == item) {
            return i < 9 ? i + 36 : i;
         }

         i += fromZero ? 1 : -1;
      }

      return -1;
   }

   public static int findItemInventorySlotFromZero(class_1792 item) {
      for(int i = 0; i < 36; ++i) {
         if (mc.field_1724.method_31548().method_5438(i).method_7909() == item) {
            return i < 9 ? i + 36 : i;
         }
      }

      return -1;
   }

   public static Map<Integer, class_1799> getInventoryAndHotbarSlots() {
      Map<Integer, class_1799> map = new HashMap();

      for(int i = 0; i < 36; ++i) {
         map.put(i, mc.field_1724.method_31548().method_5438(i));
      }

      return map;
   }
}
