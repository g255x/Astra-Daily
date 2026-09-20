package nekiplay.meteorplus.utils;

import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_746;
import org.joml.Vector3d;

public class RotationUtils {
   public static class_243 getEyesPos() {
      class_746 player = MeteorClient.mc.field_1724;
      return new class_243(player.method_23317(), player.method_23318() + (double)player.method_18381(player.method_18376()), player.method_23321());
   }

   public static float getAngleDifference(float a, float b) {
      return ((a - b) % 360.0F + 540.0F) % 360.0F - 180.0F;
   }

   public static double getRotationDifference(Rotation a, Rotation b) {
      return Math.hypot((double)getAngleDifference(a.yaw, b.yaw), (double)(a.getPitch() - b.getPitch()));
   }

   public static Rotation toRotation(class_243 vec, boolean predict) {
      class_243 eyesPos = new class_243(MeteorClient.mc.field_1724.method_23317(), MeteorClient.mc.field_1724.method_5829().field_1322 + (double)MeteorClient.mc.field_1724.method_18381(MeteorClient.mc.field_1724.method_18376()), MeteorClient.mc.field_1724.method_23321());
      if (predict) {
         if (MeteorClient.mc.field_1724.method_24828()) {
            eyesPos.method_1031(MeteorClient.mc.field_1724.method_18798().field_1352, (double)0.0F, MeteorClient.mc.field_1724.method_18798().field_1350);
         } else {
            eyesPos.method_1031(MeteorClient.mc.field_1724.method_18798().field_1352, MeteorClient.mc.field_1724.method_18798().field_1351, MeteorClient.mc.field_1724.method_18798().field_1350);
         }
      }

      double diffX = vec.field_1352 - eyesPos.field_1352;
      double diffY = vec.field_1351 - eyesPos.field_1351;
      double diffZ = vec.field_1350 - eyesPos.field_1350;
      return new Rotation((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)))));
   }

   public static class_243 getCenter(class_238 bb) {
      return new class_243(bb.field_1323 + (bb.field_1320 - bb.field_1323) * (double)0.5F, bb.field_1322 + (bb.field_1325 - bb.field_1322) * (double)0.5F, bb.field_1321 + (bb.field_1324 - bb.field_1321) * (double)0.5F);
   }

   public static Rotation limitAngleChange(Rotation currentRotation, Rotation targetRotation, float turnSpeed) {
      float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
      float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
      return new Rotation(currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)));
   }

   public static Rotation getNeededRotations(class_243 vec) {
      class_243 eyesPos = getEyesPos();
      double diffX = vec.field_1352 - eyesPos.field_1352;
      double diffY = vec.field_1351 - eyesPos.field_1351;
      double diffZ = vec.field_1350 - eyesPos.field_1350;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new Rotation(yaw, pitch);
   }

   public static double getAngleToLookVec(class_243 vec) {
      Rotation needed = getNeededRotations(vec);
      class_746 player = MeteorClient.mc.field_1724;
      float currentYaw = class_3532.method_15393(player.method_36454());
      float currentPitch = class_3532.method_15393(player.method_36455());
      float diffYaw = currentYaw - needed.yaw;
      float diffPitch = currentPitch - needed.pitch;
      return Math.sqrt((double)(diffYaw * diffYaw + diffPitch * diffPitch));
   }

   public static Vector3d getVector3d(class_1309 me, class_1309 to, float range) {
      double wHalf = (double)(to.method_17681() / 2.0F);
      double yExpand = org.joml.Math.clamp((double)me.method_18381(me.method_18376()) - to.method_23318(), (double)0.0F, (double)(to.method_17682() * (MeteorClient.mc.field_1724.method_5739(to) / range)));
      double xExpand = org.joml.Math.clamp(MeteorClient.mc.field_1724.method_73189().field_1352 - to.method_73189().field_1352, -wHalf, wHalf);
      double zExpand = org.joml.Math.clamp(MeteorClient.mc.field_1724.method_73189().field_1350 - to.method_73189().field_1350, -wHalf, wHalf);
      return new Vector3d(to.method_23317() - me.method_23317() + xExpand, to.method_23318() - (double)me.method_18381(me.method_18376()) + yExpand, to.method_23321() - me.method_23321() + zExpand);
   }

   public static double getDistance(class_1309 entity, float range) {
      return getVector3d(MeteorClient.mc.field_1724, entity, range).length();
   }

   public static double getDegree(class_1309 entity, float range) {
      Vector3d vec = getVector3d(MeteorClient.mc.field_1724, entity, range);
      double yaw = class_3532.method_15338(Math.toDegrees(Math.atan2(vec.z, vec.x)) - (double)90.0F);
      double delta = class_3532.method_15338(yaw - (double)MeteorClient.mc.field_1724.method_36454());
      if (Math.abs(delta) > (double)180.0F) {
         delta -= Math.signum(delta) * (double)360.0F;
      }

      return Math.abs(delta);
   }

   public static boolean isLookingAtMe(class_1309 target) {
      double x = target.method_23317() - MeteorClient.mc.field_1724.method_23317();
      double z = target.method_23321() - MeteorClient.mc.field_1724.method_23321();
      float entityYaw = class_3532.method_15393(target.method_36454());
      double yaw = Math.toDegrees(Math.atan2(z, x)) + (double)90.0F;
      return Math.abs(class_3532.method_15338(yaw - (double)entityYaw)) <= (double)90.0F;
   }

   public static boolean isInHitBox(class_1309 me, class_1309 to) {
      double wHalf = (double)(to.method_17681() / 2.0F);
      double yExpand = org.joml.Math.clamp((double)me.method_18381(me.method_18376()) - to.method_23318(), (double)0.0F, (double)to.method_17682());
      double xExpand = org.joml.Math.clamp(MeteorClient.mc.field_1724.method_23317() - to.method_23317(), -wHalf, wHalf);
      double zExpand = org.joml.Math.clamp(MeteorClient.mc.field_1724.method_23321() - to.method_23321(), -wHalf, wHalf);
      return (new Vector3d(to.method_23317() - me.method_23317() + xExpand, to.method_23318() - (double)me.method_18381(me.method_18376()) + yExpand, to.method_23321() - me.method_23321() + zExpand)).length() == (double)0.0F;
   }

   public static final class Rotation {
      private float yaw;
      private float pitch;

      public Rotation(float yaw, float pitch) {
         this.yaw = class_3532.method_15393(yaw);
         this.pitch = class_3532.method_15393(pitch);
      }

      public Rotation(double yaw, double pitch) {
         this.yaw = class_3532.method_15393((float)yaw);
         this.pitch = class_3532.method_15393((float)pitch);
      }

      public float getYaw() {
         return this.yaw;
      }

      public float getPitch() {
         return this.pitch;
      }

      public void setYaw(float yaw) {
         this.yaw = yaw;
      }

      public void setPitch(float pitch) {
         this.pitch = pitch;
      }
   }
}
