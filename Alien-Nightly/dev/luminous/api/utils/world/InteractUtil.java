package dev.luminous.api.utils.world;

import dev.luminous.api.utils.Wrapper;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1675;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3966;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class InteractUtil implements Wrapper {
   public static class_239 getRtxTarget(float yaw, float pitch, double x, double y, double z) {
      class_239 result = rayTrace((double)5.0F, yaw, pitch, x, y, z);
      class_243 vec3d = new class_243(x, y, z);
      double distancePow2 = (double)25.0F;
      if (result != null) {
         distancePow2 = result.method_17784().method_1025(vec3d);
      }

      class_243 vec3d2 = getRotationVector(pitch, yaw);
      class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * (double)5.0F, vec3d2.field_1351 * (double)5.0F, vec3d2.field_1350 * (double)5.0F);
      class_238 box = (new class_238(x - 0.3, y, z - 0.3, x + 0.3, y + 1.8, z + 0.3)).method_18804(vec3d2.method_1021((double)5.0F)).method_1009((double)1.0F, (double)1.0F, (double)1.0F);
      class_3966 entityHitResult = class_1675.method_18075(mc.field_1724, vec3d, vec3d3, box, (entity) -> !entity.method_7325() && entity.method_5863(), distancePow2);
      if (entityHitResult != null) {
         class_1297 entity2 = entityHitResult.method_17782();
         class_243 vec3d4 = entityHitResult.method_17784();
         double g = vec3d.method_1025(vec3d4);
         if ((g < distancePow2 || result == null) && entity2 instanceof class_1309) {
            return entityHitResult;
         }
      }

      return result;
   }

   public static class_239 rayTrace(double dst, float yaw, float pitch, double x, double y, double z) {
      class_243 vec3d = new class_243(x, y, z);
      class_243 vec3d2 = getRotationVector(pitch, yaw);
      class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * dst, vec3d2.field_1351 * dst, vec3d2.field_1350 * dst);
      return mc.field_1687.method_17742(new class_3959(vec3d, vec3d3, class_3960.field_17559, class_242.field_1348, mc.field_1724));
   }

   private static class_243 getRotationVector(float yaw, float pitch) {
      return new class_243((double)(class_3532.method_15374(-pitch * ((float)Math.PI / 180F)) * class_3532.method_15362(yaw * ((float)Math.PI / 180F))), (double)(-class_3532.method_15374(yaw * ((float)Math.PI / 180F))), (double)(class_3532.method_15362(-pitch * ((float)Math.PI / 180F)) * class_3532.method_15362(yaw * ((float)Math.PI / 180F))));
   }
}
