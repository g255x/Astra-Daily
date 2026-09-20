package nekiplay.meteorplus.utils.algoritms;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.utils.RotationUtils;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_243;

public class ShadyRotation {
   private static float pitchDifference;
   public static float yawDifference;
   private static int ticks = -1;
   private static int tickCounter = 0;
   private static Runnable callback = null;
   public static boolean running = false;
   private static boolean client = false;

   private static double wrapAngleTo180(double angle) {
      return angle - Math.floor(angle / (double)360.0F + (double)0.5F) * (double)360.0F;
   }

   private static float wrapAngleTo180(float angle) {
      return (float)((double)angle - Math.floor((double)(angle / 360.0F) + (double)0.5F) * (double)360.0F);
   }

   public static Rotation getRotationToBlock(class_2338 block) {
      double diffX = (double)block.method_10263() - MeteorClient.mc.field_1724.method_73189().field_1352 + (double)0.5F;
      double diffY = (double)block.method_10264() - MeteorClient.mc.field_1724.method_73189().field_1351 + (double)0.5F - MeteorClient.mc.field_1724.method_23320();
      double diffZ = (double)block.method_10260() - MeteorClient.mc.field_1724.method_73189().field_1350 + (double)0.5F;
      double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float pitch = (float)(-Math.atan2(dist, diffY));
      float yaw = (float)Math.atan2(diffZ, diffX);
      pitch = (float)wrapAngleTo180(((double)(pitch * 180.0F) / Math.PI + (double)90.0F) * (double)-1.0F);
      yaw = (float)wrapAngleTo180((double)(yaw * 180.0F) / Math.PI - (double)90.0F);
      return new Rotation(pitch, yaw);
   }

   public static Rotation getRotationToEntity(class_1297 entity) {
      double diffX = entity.method_73189().field_1352 - MeteorClient.mc.field_1724.method_73189().field_1352;
      double diffY = entity.method_73189().field_1351 + entity.method_33571().field_1351 - MeteorClient.mc.field_1724.method_73189().field_1351 - MeteorClient.mc.field_1724.method_23320();
      double diffZ = entity.method_73189().field_1350 - MeteorClient.mc.field_1724.method_73189().field_1350;
      double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float pitch = (float)(-Math.atan2(dist, diffY));
      float yaw = (float)Math.atan2(diffZ, diffX);
      pitch = (float)wrapAngleTo180(((double)(pitch * 180.0F) / Math.PI + (double)90.0F) * (double)-1.0F);
      yaw = (float)wrapAngleTo180((double)(yaw * 180.0F) / Math.PI - (double)90.0F);
      return new Rotation(pitch, yaw);
   }

   public static Rotation vec3ToRotation(class_243 vec) {
      double diffX = vec.field_1352 - MeteorClient.mc.field_1724.method_73189().field_1352;
      double diffY = vec.field_1351 - MeteorClient.mc.field_1724.method_73189().field_1351 - MeteorClient.mc.field_1724.method_23320();
      double diffZ = vec.field_1350 - MeteorClient.mc.field_1724.method_73189().field_1350;
      double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float pitch = (float)(-Math.atan2(dist, diffY));
      float yaw = (float)Math.atan2(diffZ, diffX);
      pitch = (float)wrapAngleTo180(((double)(pitch * 180.0F) / Math.PI + (double)90.0F) * (double)-1.0F);
      yaw = (float)wrapAngleTo180((double)(yaw * 180.0F) / Math.PI - (double)90.0F);
      return new Rotation(pitch, yaw);
   }

   public static void smoothLook(Rotation rotation, int ticks, boolean client, Runnable callback) {
      ShadyRotation.client = client;
      if (ticks == 0) {
         look(rotation, client);
         callback.run();
      } else {
         ShadyRotation.callback = callback;
         pitchDifference = wrapAngleTo180(rotation.pitch - MeteorClient.mc.field_1724.method_36455());
         yawDifference = wrapAngleTo180(rotation.yaw - MeteorClient.mc.field_1724.method_36454());
         ShadyRotation.ticks = ticks * 20;
         tickCounter = 0;
      }
   }

   public static void smoothLook(RotationUtils.Rotation rotation, int ticks, boolean client, Runnable callback) {
      ShadyRotation.client = client;
      if (ticks == 0) {
         look(new Rotation(rotation.getPitch(), rotation.getYaw()), client);
         callback.run();
      } else {
         ShadyRotation.callback = callback;
         if (client) {
            pitchDifference = wrapAngleTo180(rotation.getPitch() - MeteorClient.mc.field_1724.method_36455());
            yawDifference = wrapAngleTo180(rotation.getYaw() - MeteorClient.mc.field_1724.method_36454());
         } else {
            pitchDifference = wrapAngleTo180(rotation.getPitch() - Rotations.serverPitch);
            yawDifference = wrapAngleTo180(rotation.getYaw() - Rotations.serverYaw);
         }

         ShadyRotation.ticks = ticks * 20;
         tickCounter = 0;
      }
   }

   public static void smartLook(Rotation rotation, int ticksPer180, boolean client, Runnable callback) {
      ShadyRotation.client = client;
      float rotationDifference = Math.max(Math.abs(rotation.pitch - MeteorClient.mc.field_1724.method_36455()), Math.abs(rotation.yaw - MeteorClient.mc.field_1724.method_36454()));
      smoothLook(rotation, (int)(rotationDifference / 180.0F * (float)ticksPer180), client, callback);
   }

   public static void look(Rotation rotation, boolean client) {
      Rotations.serverYaw = rotation.yaw;
      Rotations.serverPitch = rotation.pitch;
      if (client) {
         MeteorClient.mc.field_1724.method_36457(rotation.pitch);
         MeteorClient.mc.field_1724.method_36456(rotation.yaw);
      }

   }

   public void Init() {
      MeteorClient.EVENT_BUS.subscribe(this);
   }

   private void rotatorWorker() {
      if (MeteorClient.mc.field_1724 != null) {
         if (tickCounter < ticks) {
            running = true;
            if (client) {
               MeteorClient.mc.field_1724.method_36457(MeteorClient.mc.field_1724.method_36455() + pitchDifference / (float)ticks);
               MeteorClient.mc.field_1724.method_36456(MeteorClient.mc.field_1724.method_36454() + yawDifference / (float)ticks);
            } else {
               Rotations.setCamRotation((double)(Rotations.serverYaw + yawDifference / (float)ticks), (double)(Rotations.serverPitch + pitchDifference / (float)ticks));
            }

            ++tickCounter;
         } else {
            running = false;
         }

      }
   }

   @EventHandler
   public void onTick(TickEvent.Pre event) {
      this.rotatorWorker();
   }

   @EventHandler
   public void onTick2(TickEvent.Post event) {
      this.rotatorWorker();
   }

   public static class Rotation {
      public float pitch;
      public float yaw;

      public Rotation(float pitch, float yaw) {
         this.pitch = pitch;
         this.yaw = yaw;
      }
   }
}
