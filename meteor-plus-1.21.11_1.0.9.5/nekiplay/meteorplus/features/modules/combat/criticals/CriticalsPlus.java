package nekiplay.meteorplus.features.modules.combat.criticals;

import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.Criticals;
import meteordevelopment.meteorclient.utils.entity.DamageUtils;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_310;

public class CriticalsPlus extends Module {
   private static class_310 mc = class_310.method_1551();

   public CriticalsPlus() {
      super(Categories.Combat, "Criticals+", "Better criticals module");
   }

   public static boolean canCrit() {
      return !mc.field_1724.method_24828() && mc.field_1724.field_6017 > (double)0.0F;
   }

   public static boolean skipCrit() {
      return !mc.field_1724.method_24828() || mc.field_1724.method_5869() || mc.field_1724.method_5771() || mc.field_1724.method_6101();
   }

   public static boolean allowCrit() {
      if (canCrit()) {
         return true;
      } else if (((Criticals)Modules.get().get(Criticals.class)).isActive()) {
         return !skipCrit();
      } else {
         return false;
      }
   }

   public static boolean needCrit(class_1297 entity) {
      if (entity instanceof class_1309 livingEntity) {
         return livingEntity.method_6032() >= DamageUtils.getAttackDamage(mc.field_1724, livingEntity);
      } else {
         return false;
      }
   }
}
