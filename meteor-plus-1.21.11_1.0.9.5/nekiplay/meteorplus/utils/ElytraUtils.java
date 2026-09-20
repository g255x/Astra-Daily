package nekiplay.meteorplus.utils;

import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.class_2815;
import net.minecraft.class_2848;
import net.minecraft.class_2848.class_2849;

public class ElytraUtils {
   public static void startFly() {
      if (MeteorClient.mc.field_1724 != null && MeteorClient.mc.field_1724.field_3944 != null) {
         MeteorClient.mc.field_1724.field_3944.method_52787(new class_2848(MeteorClient.mc.field_1724, class_2849.field_12982));
      }

   }

   public static void fakeInventoryOpen(boolean open) {
      if (MeteorClient.mc.field_1724 != null && MeteorClient.mc.field_1724.field_3944 != null) {
         if (open) {
            MeteorClient.mc.field_1724.field_3944.method_52787(new class_2848(MeteorClient.mc.field_1724, class_2849.field_12988));
         } else {
            MeteorClient.mc.field_1724.field_3944.method_52787(new class_2815(0));
         }
      }

   }
}
