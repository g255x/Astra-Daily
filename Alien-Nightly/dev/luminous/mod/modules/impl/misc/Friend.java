package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.mod.modules.Module;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_239;
import net.minecraft.class_3966;

public class Friend extends Module {
   public static Friend INSTANCE;

   public Friend() {
      super("Friend", Module.Category.Misc);
      this.setChinese("好友");
      INSTANCE = this;
   }

   public boolean onEnable() {
      if (nullCheck()) {
         this.disable();
      } else {
         class_239 var3 = mc.field_1765;
         if (var3 instanceof class_3966) {
            class_3966 entityHitResult = (class_3966)var3;
            class_1297 var4 = entityHitResult.method_17782();
            if (var4 instanceof class_1657) {
               class_1657 player = (class_1657)var4;
               Alien.FRIEND.friend(player);
            }
         }

         this.disable();
      }

      return false;
   }
}
