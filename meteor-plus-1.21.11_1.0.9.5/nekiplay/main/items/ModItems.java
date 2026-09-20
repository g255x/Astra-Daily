package nekiplay.main.items;

import java.util.function.Function;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.minecraft.class_7924;

public class ModItems {
   public static class_1792 METEOR_PLUS_LOGO_ITEM = null;
   public static class_1792 METEOR_PLUS_LOGO_MODS_ITEM = null;
   public static class_1792 METEOR_PLUS_STAR_ITEM = null;
   public static class_1792 METEOR_PLUS_DIAMOND_ITEM = null;
   public static class_1792 METEOR_PLUS_MONEY_ITEM = null;

   public static class_1792 register(String path, Function<class_1792.class_1793, class_1792> factory, class_1792.class_1793 settings) {
      class_5321<class_1792> registryKey = class_5321.method_29179(class_7924.field_41197, class_2960.method_60655("meteorplus", path));
      return class_1802.method_51348(registryKey, factory, settings);
   }

   public static void initialize() {
      METEOR_PLUS_LOGO_ITEM = register("logo", class_1792::new, new class_1792.class_1793());
      METEOR_PLUS_LOGO_MODS_ITEM = register("logo_mods", class_1792::new, new class_1792.class_1793());
      METEOR_PLUS_STAR_ITEM = register("star", class_1792::new, new class_1792.class_1793());
      METEOR_PLUS_DIAMOND_ITEM = register("diamond", class_1792::new, new class_1792.class_1793());
      METEOR_PLUS_MONEY_ITEM = register("money", class_1792::new, new class_1792.class_1793());
   }
}
