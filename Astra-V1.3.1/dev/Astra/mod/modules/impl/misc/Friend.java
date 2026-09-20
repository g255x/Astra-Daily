package dev.Astra.mod.modules.impl.misc;

import dev.Astra.Astra;
import dev.Astra.mod.modules.Module;
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

   public void onEnable() {
      if (nullCheck()) {
         this.disable();
      } else {
         class_239 target = mc.field_1765;
         class_3966 entityHitResult;
         class_1297 entity;
         if (target instanceof class_3966 && (entity = (entityHitResult = (class_3966)target).method_17782()) instanceof class_1657) {
            class_1657 player = (class_1657)entity;
            Astra.FRIEND.friend(player);
         }

         this.disable();
      }
   }
}
