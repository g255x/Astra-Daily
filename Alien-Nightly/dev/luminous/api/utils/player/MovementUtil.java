package dev.luminous.api.utils.player;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.asm.accessors.IVec3d;
import dev.luminous.mod.modules.impl.movement.HoleSnap;
import net.minecraft.class_1293;
import net.minecraft.class_1294;

public class MovementUtil implements Wrapper {
   public static boolean isMoving() {
      return (double)mc.field_1724.field_3913.field_3905 != (double)0.0F || (double)mc.field_1724.field_3913.field_3907 != (double)0.0F || HoleSnap.INSTANCE.isOn();
   }

   public static boolean isStatic() {
      return mc.field_1724.method_18798().method_10216() == (double)0.0F && mc.field_1724.method_24828() && mc.field_1724.method_18798().method_10215() == (double)0.0F;
   }

   public static boolean isJumping() {
      return mc.field_1724.field_3913.field_3904;
   }

   public static double getDistance2D() {
      double xDist = mc.field_1724.method_23317() - mc.field_1724.field_6014;
      double zDist = mc.field_1724.method_23321() - mc.field_1724.field_5969;
      return Math.sqrt(xDist * xDist + zDist * zDist);
   }

   public static double getJumpSpeed() {
      double defaultSpeed = (double)0.0F;
      if (mc.field_1724.method_6059(class_1294.field_5913)) {
         int amplifier = ((class_1293)mc.field_1724.method_6088().get(class_1294.field_5913)).method_5578();
         defaultSpeed += (double)(amplifier + 1) * 0.1;
      }

      return defaultSpeed;
   }

   public static double[] directionSpeed(double speed) {
      float forward = mc.field_1724.field_3913.field_3905;
      float side = mc.field_1724.field_3913.field_3907;
      return directionSpeed(speed, forward, side);
   }

   private static double[] directionSpeed(double speed, float forward, float side) {
      float yaw = mc.field_1724.field_5982 + (mc.field_1724.method_36454() - mc.field_1724.field_5982) * mc.method_60646().method_60637(true);
      if (forward != 0.0F) {
         if (side > 0.0F) {
            yaw += forward > 0.0F ? -45.0F : 45.0F;
         } else if (side < 0.0F) {
            yaw += forward > 0.0F ? 45.0F : -45.0F;
         }

         side = 0.0F;
         if (forward > 0.0F) {
            forward = 1.0F;
         } else if (forward < 0.0F) {
            forward = -1.0F;
         }
      }

      double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double posX = (double)forward * speed * cos + (double)side * speed * sin;
      double posZ = (double)forward * speed * sin - (double)side * speed * cos;
      return new double[]{posX, posZ};
   }

   public static double getMotionX() {
      return mc.field_1724.method_18798().field_1352;
   }

   public static void setMotionX(double x) {
      ((IVec3d)mc.field_1724.method_18798()).setX(x);
   }

   public static double getMotionY() {
      return mc.field_1724.method_18798().field_1351;
   }

   public static void setMotionY(double y) {
      ((IVec3d)mc.field_1724.method_18798()).setY(y);
   }

   public static double getMotionZ() {
      return mc.field_1724.method_18798().field_1350;
   }

   public static void setMotionZ(double z) {
      ((IVec3d)mc.field_1724.method_18798()).setZ(z);
   }

   public static double getSpeed(boolean slowness) {
      double defaultSpeed = 0.2873;
      return getSpeed(slowness, defaultSpeed);
   }

   public static double getSpeed(boolean slowness, double defaultSpeed) {
      if (mc.field_1724.method_6059(class_1294.field_5904)) {
         int amplifier = ((class_1293)mc.field_1724.method_6088().get(class_1294.field_5904)).method_5578();
         defaultSpeed *= (double)1.0F + 0.2 * (double)(amplifier + 1);
      }

      if (slowness && mc.field_1724.method_6059(class_1294.field_5909)) {
         int amplifier = ((class_1293)mc.field_1724.method_6088().get(class_1294.field_5909)).method_5578();
         defaultSpeed /= (double)1.0F + 0.2 * (double)(amplifier + 1);
      }

      if (mc.field_1724.method_5715()) {
         defaultSpeed /= (double)5.0F;
      }

      return defaultSpeed;
   }

   public static boolean isInputtingMovement() {
      return mc.field_1690.field_1894.method_1434() || mc.field_1690.field_1881.method_1434() || mc.field_1690.field_1913.method_1434() || mc.field_1690.field_1849.method_1434();
   }
}
