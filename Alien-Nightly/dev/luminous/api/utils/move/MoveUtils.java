package dev.luminous.api.utils.move;

import net.minecraft.class_310;
import net.minecraft.class_3532;

public class MoveUtils {
   private static final class_310 mc = class_310.method_1551();

   private static float direction(float forward, float strafe) {
      float direction = mc.field_1724.method_36454();
      boolean isMovingForward = forward > 0.0F;
      boolean isMovingBack = forward < 0.0F;
      boolean isMovingRight = strafe > 0.0F;
      boolean isMovingLeft = strafe < 0.0F;
      boolean isMovingSideways = isMovingRight || isMovingLeft;
      boolean isMovingStraight = isMovingForward || isMovingBack;
      if (forward != 0.0F || strafe != 0.0F) {
         if (isMovingBack && !isMovingSideways) {
            return direction + 180.0F;
         }

         if (isMovingForward && isMovingLeft) {
            return direction + 45.0F;
         }

         if (isMovingForward && isMovingRight) {
            return direction - 45.0F;
         }

         if (!isMovingStraight && isMovingLeft) {
            return direction + 90.0F;
         }

         if (!isMovingStraight && isMovingRight) {
            return direction - 90.0F;
         }

         if (isMovingBack && isMovingLeft) {
            return direction + 135.0F;
         }

         if (isMovingBack) {
            return direction - 135.0F;
         }
      }

      return direction;
   }

   public static void fixMovement(float yaw) {
      if (mc.field_1724 != null) {
         float forward = mc.field_1724.field_3913.field_3905;
         float strafe = mc.field_1724.field_3913.field_3907;
         int angleUnit = 45;
         float angleTolerance = 22.5F;
         float directionFactor = Math.max(Math.abs(forward), Math.abs(strafe));
         double angleDifference = (double)class_3532.method_15393(direction(forward, strafe) - yaw);
         double angleDistance = Math.abs(angleDifference);
         forward = 0.0F;
         strafe = 0.0F;
         if (angleDistance <= (double)((float)angleUnit + angleTolerance)) {
            ++forward;
         } else if (angleDistance >= (double)(180.0F - (float)angleUnit - angleTolerance)) {
            --forward;
         }

         if (angleDifference >= (double)((float)angleUnit - angleTolerance) && angleDifference <= (double)(180.0F - (float)angleUnit + angleTolerance)) {
            --strafe;
         } else if (angleDifference <= (double)((float)(-angleUnit) + angleTolerance) && angleDifference >= (double)(-180.0F + (float)angleUnit - angleTolerance)) {
            ++strafe;
         }

         forward *= directionFactor;
         strafe *= directionFactor;
         mc.field_1724.field_3913.field_3905 = forward;
         mc.field_1724.field_3913.field_3907 = strafe;
      }

   }

   public static boolean isMoving() {
      if (mc.field_1724 == null) {
         return false;
      } else {
         return mc.field_1724.field_3913.field_3905 != 0.0F || mc.field_1724.field_3913.field_3907 != 0.0F || mc.field_1690.field_1894.method_1434() || mc.field_1690.field_1881.method_1434() || mc.field_1690.field_1913.method_1434() || mc.field_1690.field_1849.method_1434() || mc.field_1690.field_1903.method_1434();
      }
   }
}
