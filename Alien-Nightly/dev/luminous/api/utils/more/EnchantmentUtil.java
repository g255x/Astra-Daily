package dev.luminous.api.utils.more;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Set;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_5321;
import net.minecraft.class_6880;
import net.minecraft.class_9304;
import net.minecraft.class_9334;

public class EnchantmentUtil {
   public static int getLevel(class_1799 stack, class_5321<class_1887> enchantmentRegistryKey) {
      if (!stack.method_57353().method_57832(class_9334.field_49633)) {
         return 0;
      } else {
         for(Object2IntMap.Entry<class_6880<class_1887>> e : ((class_9304)stack.method_57353().method_57829(class_9334.field_49633)).method_57539()) {
            if (((class_6880)e.getKey()).method_40230().isPresent() && ((class_5321)((class_6880)e.getKey()).method_40230().get()).equals(enchantmentRegistryKey)) {
               return e.getIntValue();
            }
         }

         return 0;
      }
   }

   public static boolean isFakeEnchant2b2t(class_1799 itemStack) {
      Set<Object2IntMap.Entry<class_6880<class_1887>>> enchants = class_1890.method_57532(itemStack).method_57539();
      if (enchants.size() > 1) {
         return false;
      } else {
         for(Object2IntMap.Entry<class_6880<class_1887>> e : enchants) {
            class_6880<class_1887> enchantment = (class_6880)e.getKey();
            int lvl = e.getIntValue();
            if (lvl == 0 && enchantment.method_40230().isPresent() && enchantment.method_40230().get() == class_1893.field_9111) {
               return true;
            }
         }

         return false;
      }
   }
}
