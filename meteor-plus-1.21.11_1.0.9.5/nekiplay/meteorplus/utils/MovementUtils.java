package nekiplay.meteorplus.utils;

import meteordevelopment.meteorclient.MeteorClient;

public class MovementUtils {
   public static double getSpeed() {
      return Math.sqrt(MeteorClient.mc.field_1724.method_18798().field_1352 * MeteorClient.mc.field_1724.method_18798().field_1352 + MeteorClient.mc.field_1724.method_18798().field_1350 * MeteorClient.mc.field_1724.method_18798().field_1350);
   }

   public static void strafe(float speed) {
      strafe((double)speed);
   }

   public static void strafe(double speed) {
      double yaw = direction();
      double sin = -Math.sin(yaw) * speed;
      double cos = Math.cos(yaw) * speed;
      MeteorClient.mc.field_1724.method_18798().method_1031(cos, (double)0.0F, sin);
   }

   public static double direction() {
      float yaw = MeteorClient.mc.field_1724.method_36454();
      if (MeteorClient.mc.field_1724.field_3913.method_3128().field_1343 < 0.0F) {
         yaw += 180.0F;
      }

      float forward = 1.0F;
      if (MeteorClient.mc.field_1724.field_3913.method_3128().field_1343 < 0.0F) {
         forward = (float)((double)forward - (double)0.5F);
      } else if (MeteorClient.mc.field_1724.field_3913.method_3128().field_1343 > 0.0F) {
         forward = (float)((double)forward + (double)0.5F);
      }

      if (MeteorClient.mc.field_1724.field_3913.method_3128().field_1342 > 0.0F) {
         yaw -= 90.0F * forward;
      }

      if (MeteorClient.mc.field_1724.field_3913.method_3128().field_1342 < 0.0F) {
         yaw += 90.0F * forward;
      }

      return Math.toRadians((double)yaw);
   }
}
