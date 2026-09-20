package nekiplay.meteorplus.utils;

import java.util.function.Predicate;
import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.class_1297;
import net.minecraft.class_1675;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class RaycastUtils {
   public static class_3966 raycastEntity(double range, float yaw, float pitch, double boxexpand) {
      class_1297 camera = MeteorClient.mc.method_1560();
      class_243 cameraVec = MeteorClient.mc.field_1724.method_33571();
      float yawCos = class_3532.method_15362((double)(-yaw * ((float)Math.PI / 180F) - (float)Math.PI));
      float yawSin = class_3532.method_15374((double)(-yaw * ((float)Math.PI / 180F) - (float)Math.PI));
      float pitchCos = -class_3532.method_15362((double)(-pitch * ((float)Math.PI / 180F)));
      float pitchSin = class_3532.method_15374((double)(-pitch * ((float)Math.PI / 180F)));
      class_243 rotation = new class_243((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
      class_243 vec3d3 = cameraVec.method_1031(rotation.field_1352 * range, rotation.field_1351 * range, rotation.field_1350 * range);
      class_238 box = camera.method_5829().method_18804(rotation.method_1021(range)).method_1009(boxexpand, boxexpand, boxexpand);
      return class_1675.method_18075(camera, cameraVec, vec3d3, box, new Predicate<class_1297>() {
         public boolean test(class_1297 entity) {
            return !entity.method_7325() && entity.method_30948(MeteorClient.mc.field_1724);
         }
      }, (double)0.0F);
   }

   public static class_243 getRotationVector(float pitch, float yaw) {
      float f = pitch * ((float)Math.PI / 180F);
      float g = -yaw * ((float)Math.PI / 180F);
      float h = class_3532.method_15362((double)g);
      float i = class_3532.method_15374((double)g);
      float j = class_3532.method_15362((double)f);
      float k = class_3532.method_15374((double)f);
      return new class_243((double)(i * j), (double)(-k), (double)(h * j));
   }

   public static class_239 raycast(class_243 camera, class_243 rotation, double maxDistance, float tickDelta, boolean includeFluids) {
      class_243 vec3d3 = camera.method_1031(rotation.field_1352 * maxDistance, rotation.field_1351 * maxDistance, rotation.field_1350 * maxDistance);
      return MeteorClient.mc.field_1687.method_17742(new class_3959(camera, vec3d3, class_3960.field_17559, includeFluids ? class_242.field_1347 : class_242.field_1348, MeteorClient.mc.field_1724));
   }

   public static class_3965 bucketRaycast(class_243 camera, float pitch, float yaw, class_3959.class_242 fluidHandling) {
      float h = class_3532.method_15362((double)(-yaw * ((float)Math.PI / 180F) - (float)Math.PI));
      float i = class_3532.method_15374((double)(-yaw * ((float)Math.PI / 180F) - (float)Math.PI));
      float j = -class_3532.method_15362((double)(-pitch * ((float)Math.PI / 180F)));
      float k = class_3532.method_15374((double)(-pitch * ((float)Math.PI / 180F)));
      float l = i * j;
      float n = h * j;
      double d = (double)5.0F;
      class_243 vec3d2 = camera.method_1031((double)l * (double)5.0F, (double)k * (double)5.0F, (double)n * (double)5.0F);
      return MeteorClient.mc.field_1687.method_17742(new class_3959(camera, vec3d2, class_3960.field_17559, fluidHandling, MeteorClient.mc.field_1724));
   }
}
