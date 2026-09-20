package dev.Astra.api.utils.path;

import com.google.common.collect.Lists;
import dev.Astra.api.utils.Wrapper;
import java.util.List;
import net.minecraft.class_243;
import net.minecraft.class_2828;

public class TPUtils implements Wrapper {
   public static void teleportWithBack(class_243 newPos, TeleportType type, Runnable runnable) {
      switch (type.ordinal()) {
         case 0 -> legacyTeleportWithBack(newPos, runnable);
         case 1 -> newTeleportWithBack(newPos, runnable);
      }

   }

   public static void legacyTeleportWithBack(class_243 newPos, Runnable runnable) {
      List<Vec3> tpPath = PathUtils.computePath(newPos);
      tpPath.removeFirst();
      tpPath.forEach((vec3) -> mc.field_1724.field_3944.method_52787(new class_2828.class_2829(vec3.x(), vec3.y(), vec3.z(), false)));
      runnable.run();
      tpPath = Lists.reverse(tpPath);
      tpPath.removeFirst();
      tpPath.forEach((vec3) -> mc.field_1724.field_3944.method_52787(new class_2828.class_2829(vec3.x(), vec3.y(), vec3.z(), false)));
      mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
   }

   public static void newTeleportWithBack(class_243 newPos, Runnable runnable) {
      int packetsRequired = (int)Math.ceil(mc.field_1724.method_19538().method_1022(newPos) / (double)10.0F) - 1;

      for(int i = 0; i < packetsRequired; ++i) {
         mc.field_1724.field_3944.method_52787(new class_2828.class_5911(true));
      }

      mc.field_1724.field_3944.method_52787(new class_2828.class_2829(newPos.field_1352, newPos.field_1351, newPos.field_1350, true));
      runnable.run();

      for(int var4 = 0; var4 < packetsRequired; ++var4) {
         mc.field_1724.field_3944.method_52787(new class_2828.class_5911(true));
      }

      mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
   }

   public static void newTeleport(class_243 newPos) {
      int packetsRequired = (int)Math.ceil(mc.field_1724.method_19538().method_1022(newPos) / (double)10.0F) - 1;

      for(int i = 0; i < packetsRequired; ++i) {
         mc.field_1724.field_3944.method_52787(new class_2828.class_5911(true));
      }

      mc.field_1724.field_3944.method_52787(new class_2828.class_2829(newPos.field_1352, newPos.field_1351, newPos.field_1350, true));
   }

   public static enum TeleportType {
      Legacy,
      New;

      // $FF: synthetic method
      private static TeleportType[] $values() {
         return new TeleportType[]{Legacy, New};
      }
   }
}
