package dev.luminous.api.utils.rotation;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.GameLeftEvent;
import dev.luminous.api.events.impl.KeyboardInputEvent;
import dev.luminous.api.events.impl.SendMovementPacketsEvent;
import dev.luminous.api.utils.move.MoveUtils;
import java.util.function.Function;
import net.minecraft.class_310;
import net.minecraft.class_3532;

public class RotationManager {
   private static final class_310 mc = class_310.method_1551();
   private static final Rotation offset = new Rotation(0.0F, 0.0F);
   public static Rotation rotations;
   public static Rotation lastRotations = new Rotation(0.0F, 0.0F);
   public static Rotation targetRotations;
   public static Rotation animationRotation;
   public static Rotation lastAnimationRotation;
   private static boolean active;
   private static boolean smoothed;
   private static double rotationSpeed;
   private static Function<Rotation, Boolean> raycast;
   private static float randomAngle;
   private static boolean subscribed;

   public static void init() {
      if (!subscribed) {
         Alien.EVENT_BUS.subscribe(new RotationManager());
         subscribed = true;
      }

   }

   public static boolean isActive() {
      return active;
   }

   public static void setActive(boolean active) {
      RotationManager.active = active;
   }

   public static boolean isSmoothed() {
      return smoothed;
   }

   public static void setRotations(Rotation rotations, double rotationSpeed) {
      setRotations(rotations, rotationSpeed, (Function)null);
   }

   public static void setRotations(Rotation rotations, double rotationSpeed, Function<Rotation, Boolean> raycast) {
      targetRotations = rotations;
      RotationManager.rotationSpeed = rotationSpeed;
      RotationManager.raycast = raycast;
      active = true;
      smoothed = false;
      smooth();
   }

   public static void smooth() {
      if (mc.field_1724 != null) {
         if (!smoothed) {
            float targetYaw = targetRotations.getYaw();
            float targetPitch = targetRotations.getPitch();
            if (raycast != null && (Math.abs(targetYaw - rotations.getYaw()) > 5.0F || Math.abs(targetPitch - rotations.getPitch()) > 5.0F)) {
               Rotation trueTargetRotations = new Rotation(targetRotations.getYaw(), targetRotations.getPitch());
               double speed = Math.random() * Math.random() * Math.random() * (double)20.0F;
               randomAngle += (float)(((double)20.0F + (double)((float)(Math.random() - (double)0.5F)) * Math.random() * Math.random() * Math.random() * (double)360.0F) * (double)(mc.field_1724.field_6012 / 10 % 2 == 0 ? -1 : 1));
               offset.setYaw((float)((double)offset.getYaw() + (double)(-class_3532.method_15362((float)Math.toRadians((double)randomAngle))) * speed));
               offset.setPitch((float)((double)offset.getPitch() + (double)class_3532.method_15374((float)Math.toRadians((double)randomAngle)) * speed));
               targetYaw += offset.getYaw();
               targetPitch += offset.getPitch();
               if (!(Boolean)raycast.apply(new Rotation(targetYaw, targetPitch))) {
                  randomAngle = (float)Math.toDegrees(Math.atan2((double)(trueTargetRotations.getYaw() - targetYaw), (double)(targetPitch - trueTargetRotations.getPitch()))) - 180.0F;
                  targetYaw -= offset.getYaw();
                  targetPitch -= offset.getPitch();
                  offset.setYaw((float)((double)offset.getYaw() + (double)(-class_3532.method_15362((float)Math.toRadians((double)randomAngle))) * speed));
                  offset.setPitch((float)((double)offset.getPitch() + (double)class_3532.method_15374((float)Math.toRadians((double)randomAngle)) * speed));
                  targetYaw += offset.getYaw();
                  targetPitch += offset.getPitch();
               }

               if (!(Boolean)raycast.apply(new Rotation(targetYaw, targetPitch))) {
                  offset.setYaw(0.0F);
                  offset.setPitch(0.0F);
                  targetYaw = (float)((double)targetRotations.getYaw() + Math.random() * (double)2.0F);
                  targetPitch = (float)((double)targetRotations.getPitch() + Math.random() * (double)2.0F);
               }
            }

            rotations = RotationUtils.smooth(new Rotation(targetYaw, targetPitch), rotationSpeed + Math.random());
         }

         smoothed = true;
      }

   }

   public static Rotation getRotation() {
      if (mc.field_1724 == null) {
         return new Rotation(0.0F, 0.0F);
      } else {
         return active && rotations != null ? rotations : new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
      }
   }

   @EventListener
   public void onLogout(GameLeftEvent e) {
      rotations = null;
      lastRotations = null;
      targetRotations = null;
      animationRotation = null;
      lastAnimationRotation = null;
      active = false;
      smoothed = false;
   }

   @EventListener(
      priority = 4
   )
   public void onClientTick(ClientTickEvent e) {
      if (e.isPre() && mc.field_1724 != null) {
         if (!active || rotations == null || lastRotations == null || targetRotations == null) {
            rotations = lastRotations = targetRotations = new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
         }

         if (active) {
            smooth();
         }
      }

   }

   @EventListener(
      priority = 4
   )
   public void onSendMovementPackets(SendMovementPacketsEvent e) {
      if (mc.field_1724 != null) {
         if (active && rotations != null) {
            float yaw = rotations.getYaw();
            float pitch = rotations.getPitch();
            if (!Float.isNaN(yaw) && !Float.isNaN(pitch)) {
               e.setYaw(yaw);
               e.setPitch(pitch);
            }

            if (Math.abs((rotations.getYaw() - mc.field_1724.method_36454()) % 360.0F) < 1.0F && Math.abs(rotations.getPitch() - mc.field_1724.method_36455()) < 1.0F) {
               active = false;
               this.correctDisabledRotations();
            }

            lastRotations = rotations;
         } else {
            lastRotations = new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
         }

         lastAnimationRotation = animationRotation;
         animationRotation = new Rotation(e.getYaw(), e.getPitch());
         targetRotations = new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
         smoothed = false;
      }

   }

   @EventListener(
      priority = 100
   )
   public void onKeyboardInput(KeyboardInputEvent event) {
      if (mc.field_1724 != null && active && rotations != null) {
         MoveUtils.fixMovement(rotations.getYaw());
      }

   }

   private void correctDisabledRotations() {
      Rotation rotations = new Rotation(mc.field_1724.method_36454(), mc.field_1724.method_36455());
      Rotation fixedRotations = RotationUtils.resetRotation(RotationUtils.applySensitivityPatch(rotations, lastRotations));
      mc.field_1724.method_36456(fixedRotations.getYaw());
      mc.field_1724.method_36457(fixedRotations.getPitch());
   }
}
