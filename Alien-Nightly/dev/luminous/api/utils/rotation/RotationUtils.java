package dev.luminous.api.utils.rotation;

import dev.luminous.Alien;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class RotationUtils {
   private static final class_310 mc = class_310.method_1551();

   public static Rotation calculate(class_243 from, class_243 to) {
      class_243 diff = to.method_1020(from);
      double distance = Math.hypot(diff.field_1352, diff.field_1350);
      float yaw = (float)(Math.atan2(diff.field_1350, diff.field_1352) * (double)(180F / (float)Math.PI)) - 90.0F;
      float pitch = (float)(-(Math.atan2(diff.field_1351, distance) * (double)(180F / (float)Math.PI)));
      return new Rotation(yaw, pitch);
   }

   public static Rotation calculate(class_1297 entity) {
      if (mc.field_1724 == null) {
         return new Rotation(0.0F, 0.0F);
      } else {
         double eyeHeight = (double)mc.field_1724.method_18381(mc.field_1724.method_18376());
         double clamped = Math.max((double)0.0F, Math.min(mc.field_1724.method_23318() - entity.method_23318() + eyeHeight, (entity.method_5829().field_1325 - entity.method_5829().field_1322) * 0.9));
         return calculate(entity.method_19538().method_1031((double)0.0F, clamped, (double)0.0F));
      }
   }

   public static Rotation calculate(class_1297 entity, boolean adaptive, double range) {
      Rotation normalRotations = calculate(entity);
      class_239 result = rayCast(normalRotations, range);
      if (adaptive && (result == null || result.method_17783() != class_240.field_1331)) {
         class_238 bb = entity.method_5829();
         double minX = bb.field_1323;
         double maxX = bb.field_1320;
         double minY = bb.field_1322;
         double maxY = bb.field_1325;
         double minZ = bb.field_1321;
         double maxZ = bb.field_1324;
         class_243 basePos = entity.method_19538();

         for(double yPercent = (double)1.0F; yPercent >= (double)0.0F; yPercent -= (double)0.25F + Math.random() * 0.1) {
            for(double xPercent = (double)1.0F; xPercent >= (double)-0.5F; xPercent -= (double)0.5F) {
               for(double zPercent = (double)1.0F; zPercent >= (double)-0.5F; zPercent -= (double)0.5F) {
                  double offsetX = (maxX - minX) * xPercent;
                  double offsetY = (maxY - minY) * yPercent;
                  double offsetZ = (maxZ - minZ) * zPercent;
                  class_243 targetPoint = basePos.method_1031(offsetX, offsetY, offsetZ);
                  Rotation adaptiveRotations = calculate(targetPoint);
                  class_239 rayCastResult = rayCast(adaptiveRotations, range);
                  if (rayCastResult != null && rayCastResult.method_17783() == class_240.field_1331) {
                     return adaptiveRotations;
                  }
               }
            }
         }

         return normalRotations;
      } else {
         return normalRotations;
      }
   }

   private static class_239 rayCast(Rotation rotation, double range) {
      class_243 eyePos = mc.field_1724.method_33571();
      class_243 direction = getRotationVector(rotation.getPitch(), rotation.getYaw());
      class_243 endPos = eyePos.method_1019(direction.method_1021(range));
      return mc.field_1687.method_17742(new class_3959(eyePos, endPos, class_3960.field_17559, class_242.field_1348, mc.field_1724));
   }

   private static class_243 getRotationVector(float pitch, float yaw) {
      float f = pitch * ((float)Math.PI / 180F);
      float g = -yaw * ((float)Math.PI / 180F);
      float h = class_3532.method_15362(g);
      return new class_243((double)(h * class_3532.method_15362(f)), (double)class_3532.method_15374(f), (double)(class_3532.method_15374(g) * class_3532.method_15362(f)));
   }

   public static Rotation calculate(class_2338 to) {
      return calculate(mc.field_1724.method_33571(), (new class_243((double)to.method_10263(), (double)to.method_10264(), (double)to.method_10260())).method_1031((double)0.5F, (double)0.5F, (double)0.5F));
   }

   public static Rotation calculate(class_243 to) {
      return calculate(mc.field_1724.method_33571(), to);
   }

   public static Rotation calculate(class_243 position, class_2350 enumFacing) {
      double x = position.field_1352 + (double)0.5F;
      double y = position.field_1351 + (double)0.5F;
      double z = position.field_1350 + (double)0.5F;
      x += (double)enumFacing.method_10163().method_10263() * (double)0.5F;
      y += (double)enumFacing.method_10163().method_10264() * (double)0.5F;
      z += (double)enumFacing.method_10163().method_10260() * (double)0.5F;
      return calculate(new class_243(x, y, z));
   }

   public static Rotation calculate(class_2338 position, class_2350 enumFacing) {
      double x = (double)position.method_10263() + (double)0.5F;
      double y = (double)position.method_10264() + (double)0.5F;
      double z = (double)position.method_10260() + (double)0.5F;
      x += (double)enumFacing.method_10163().method_10263() * (double)0.5F;
      y += (double)enumFacing.method_10163().method_10264() * (double)0.5F;
      z += (double)enumFacing.method_10163().method_10260() * (double)0.5F;
      return calculate(new class_243(x, y, z));
   }

   public static Rotation applySensitivityPatch(Rotation rotation) {
      Rotation previousRotation = RotationManager.lastRotations;
      if (previousRotation == null) {
         previousRotation = new Rotation(mc.field_1724.field_5982, mc.field_1724.field_6004);
      }

      return applySensitivityPatch(rotation, previousRotation);
   }

   public static Rotation applySensitivityPatch(Rotation rotation, Rotation previousRotation) {
      float mouseSensitivity = (float)((Double)mc.field_1690.method_42495().method_41753() * (double)0.6F + (double)0.2F);
      double multiplier = (double)(mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0F) * 0.15;
      float yaw = previousRotation.getYaw() + (float)((double)Math.round((double)(rotation.getYaw() - previousRotation.getYaw()) / multiplier) * multiplier);
      float pitch = previousRotation.getPitch() + (float)((double)Math.round((double)(rotation.getPitch() - previousRotation.getPitch()) / multiplier) * multiplier);
      return new Rotation(yaw, class_3532.method_15363(pitch, -90.0F, 90.0F));
   }

   public static Rotation relateToPlayerRotation(Rotation rotation) {
      Rotation previousRotation = new Rotation(mc.field_1724.field_5982, mc.field_1724.field_6004);
      float yaw = previousRotation.getYaw() + class_3532.method_15393(rotation.getYaw() - previousRotation.getYaw());
      float pitch = class_3532.method_15363(rotation.getPitch(), -90.0F, 90.0F);
      return new Rotation(yaw, pitch);
   }

   public static Rotation resetRotation(Rotation rotation) {
      if (rotation == null) {
         return null;
      } else {
         float yaw = rotation.getYaw() + class_3532.method_15393(mc.field_1724.method_36454() - rotation.getYaw());
         float pitch = mc.field_1724.method_36455();
         return new Rotation(yaw, pitch);
      }
   }

   public static Rotation move(Rotation targetRotation, double speed) {
      return move(RotationManager.lastRotations, targetRotation, speed);
   }

   public static Rotation move(Rotation lastRotation, Rotation targetRotation, double speed) {
      if (speed != (double)0.0F) {
         double deltaYaw = (double)class_3532.method_15393(targetRotation.getYaw() - lastRotation.getYaw());
         double deltaPitch = (double)(targetRotation.getPitch() - lastRotation.getPitch());
         double distance = Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
         double distributionYaw = Math.abs(deltaYaw / distance);
         double distributionPitch = Math.abs(deltaPitch / distance);
         double maxYaw = speed * distributionYaw;
         double maxPitch = speed * distributionPitch;
         float moveYaw = (float)Math.max(Math.min(deltaYaw, maxYaw), -maxYaw);
         float movePitch = (float)Math.max(Math.min(deltaPitch, maxPitch), -maxPitch);
         return new Rotation(moveYaw, movePitch);
      } else {
         return new Rotation(0.0F, 0.0F);
      }
   }

   public static Rotation smooth(Rotation targetRotation, double speed) {
      return smooth(RotationManager.lastRotations, targetRotation, speed);
   }

   public static Rotation smooth(Rotation lastRotation, Rotation targetRotation, double speed) {
      float yaw = targetRotation.getYaw();
      float pitch = targetRotation.getPitch();
      float lastYaw = lastRotation.getYaw();
      float lastPitch = lastRotation.getPitch();
      if (speed != (double)0.0F) {
         Rotation move = move(targetRotation, speed);
         yaw = lastYaw + move.getYaw();
         pitch = lastPitch + move.getPitch();

         for(int i = 1; i <= (int)((float)Alien.FPS.getFps() / 20.0F + (float)Math.random() * 10.0F); ++i) {
            if ((double)(Math.abs(move.getYaw()) + Math.abs(move.getPitch())) > 1.0E-4) {
               yaw = (float)((double)yaw + (Math.random() - (double)0.5F) / (double)1000.0F);
               pitch = (float)((double)pitch - Math.random() / (double)200.0F);
            }

            Rotation rotations = new Rotation(yaw, pitch);
            Rotation fixedRotations = applySensitivityPatch(rotations, lastRotation);
            yaw = shortestYaw(lastYaw, fixedRotations.getYaw());
            pitch = Math.max(-90.0F, Math.min(90.0F, fixedRotations.getPitch()));
         }
      }

      return new Rotation(yaw, pitch);
   }

   public static double getDistance(class_1297 entity) {
      class_243 eyes = mc.field_1724.method_33571();
      class_238 aabb = entity.method_5829();
      double x = class_3532.method_15350(eyes.field_1352, aabb.field_1323, aabb.field_1320);
      double y = class_3532.method_15350(eyes.field_1351, aabb.field_1322, aabb.field_1325);
      double z = class_3532.method_15350(eyes.field_1350, aabb.field_1321, aabb.field_1324);
      return Math.sqrt(eyes.method_1028(x, y, z));
   }

   public static double getDistance(class_1657 player, class_1297 entity) {
      class_243 eyes = player.method_33571();
      class_238 aabb = entity.method_5829();
      double x = class_3532.method_15350(eyes.field_1352, aabb.field_1323, aabb.field_1320);
      double y = class_3532.method_15350(eyes.field_1351, aabb.field_1322, aabb.field_1325);
      double z = class_3532.method_15350(eyes.field_1350, aabb.field_1321, aabb.field_1324);
      return Math.sqrt(eyes.method_1028(x, y, z));
   }

   public static float shortestYaw(float from, float to) {
      return from + class_3532.method_15393(to - from);
   }

   public static float getActualYaw() {
      return RotationManager.getRotation() != null ? RotationManager.getRotation().getYaw() : mc.field_1724.method_36454();
   }

   public static float getActualPitch() {
      return RotationManager.getRotation() != null ? RotationManager.getRotation().getPitch() : mc.field_1724.method_36455();
   }
}
