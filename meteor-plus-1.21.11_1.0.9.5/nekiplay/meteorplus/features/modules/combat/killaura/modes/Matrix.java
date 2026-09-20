package nekiplay.meteorplus.features.modules.combat.killaura.modes;

import java.util.ArrayList;
import java.util.Set;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.combat.KillAura.ShieldMode;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import nekiplay.meteorplus.features.modules.combat.criticals.CriticalsPlus;
import nekiplay.meteorplus.features.modules.combat.killaura.KillAuraPlusMode;
import nekiplay.meteorplus.features.modules.combat.killaura.KillAuraPlusModes;
import nekiplay.meteorplus.utils.GameSensitivityUtils;
import nekiplay.meteorplus.utils.RaycastUtils;
import nekiplay.meteorplus.utils.math.StopWatch;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1493;
import net.minecraft.class_1560;
import net.minecraft.class_1590;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import net.minecraft.class_5611;
import net.minecraft.class_6025;
import net.minecraft.class_239.class_240;

public class Matrix extends KillAuraPlusMode {
   private final ArrayList<class_1297> targets = new ArrayList();
   private final StopWatch stopWatch = new StopWatch();
   private class_5611 rotateVector = new class_5611(0.0F, 0.0F);
   private class_1309 target;
   private class_1297 selected;
   float lastYaw;
   float lastPitch;
   int ticks = 0;
   boolean isRotated;

   public Matrix() {
      super(KillAuraPlusModes.Matrix);
   }

   public void onTickPre(TickEvent.Pre event) {
      if (this.target == null || !this.entityCheck(this.target)) {
         TargetUtils.getList(this.targets, this::entityCheck, (SortPriority)this.settings.priority.get(), 15);
         if (!this.targets.isEmpty()) {
            Object var3 = this.targets.get(0);
            if (var3 instanceof class_1309) {
               class_1309 livingEntity = (class_1309)var3;
               this.target = livingEntity;
            }
         }
      }

      if (this.target != null && this.target.method_5805()) {
         this.isRotated = false;
         class_3966 result = RaycastUtils.raycastEntity((Double)this.settings.range.get(), this.rotateVector.comp_5231(), this.rotateVector.comp_5232(), (double)0.0F);
         if (result != null) {
            ChatUtils.info(result.method_17783().name(), new Object[0]);
         }

         if ((!(Boolean)this.settings.onlyCrits.get() || CriticalsPlus.allowCrit() || !CriticalsPlus.needCrit(this.target)) && this.delayCheck() && result != null && result.method_17783() == class_240.field_1331) {
            this.attack(this.target);
            this.ticks = 2;
         }

         if (this.settings.rotationType.get() == Matrix.Type.Fast) {
            if (this.ticks > 0) {
               this.updateRotation(true, 180.0F, 90.0F);
               Rotations.rotate((double)this.rotateVector.comp_5231(), (double)this.rotateVector.comp_5232());
               --this.ticks;
            } else {
               this.reset();
            }
         } else if (!this.isRotated) {
            this.updateRotation(false, 80.0F, 35.0F);
            Rotations.rotate((double)this.rotateVector.comp_5231(), (double)this.rotateVector.comp_5232());
         }
      } else {
         this.reset();
      }

   }

   private boolean delayCheck() {
      return this.mc.field_1724.method_7261(0.5F) >= 1.0F;
   }

   private void attack(class_1297 target) {
      this.mc.field_1761.method_2918(this.mc.field_1724, target);
      this.mc.field_1724.method_6104(class_1268.field_5808);
   }

   private boolean entityCheck(class_1297 entity) {
      if (!entity.equals(this.mc.field_1724) && !entity.equals(this.mc.method_1560())) {
         if (entity instanceof class_1309) {
            class_1309 livingEntity = (class_1309)entity;
            if (livingEntity.method_29504()) {
               return false;
            }
         }

         if (entity.method_5805()) {
            class_238 hitbox = entity.method_5829();
            if (!PlayerUtils.isWithin(class_3532.method_15350(this.mc.field_1724.method_23317(), hitbox.field_1323, hitbox.field_1320), class_3532.method_15350(this.mc.field_1724.method_23318(), hitbox.field_1322, hitbox.field_1325), class_3532.method_15350(this.mc.field_1724.method_23321(), hitbox.field_1321, hitbox.field_1324), (Double)this.settings.range.get())) {
               return false;
            } else if (!((Set)this.settings.entities.get()).contains(entity.method_5864())) {
               return false;
            } else if (!PlayerUtils.canSeeEntity(entity) && !PlayerUtils.isWithin(entity, (Double)this.settings.wallsRange.get())) {
               return false;
            } else {
               if ((Boolean)this.settings.ignoreTamed.get() && entity instanceof class_6025) {
                  class_6025 tameable = (class_6025)entity;
                  if (tameable.method_35057().method_5667() != null && tameable.method_35057().method_5667().equals(this.mc.field_1724.method_5667())) {
                     return false;
                  }
               }

               if ((Boolean)this.settings.ignorePassive.get()) {
                  if (entity instanceof class_1560) {
                     class_1560 enderman = (class_1560)entity;
                     if (!enderman.method_7028()) {
                        return false;
                     }
                  }

                  if (entity instanceof class_1590) {
                     class_1590 piglin = (class_1590)entity;
                     if (!piglin.method_6510()) {
                        return false;
                     }
                  }

                  if (entity instanceof class_1493) {
                     class_1493 wolf = (class_1493)entity;
                     if (!wolf.method_6510()) {
                        return false;
                     }
                  }
               }

               if (entity instanceof class_1657) {
                  class_1657 player = (class_1657)entity;
                  if (player.method_68878()) {
                     return false;
                  }

                  if (!Friends.get().shouldAttack(player)) {
                     return false;
                  }

                  if (this.settings.shieldMode.get() == ShieldMode.Ignore && player.method_6039()) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void updateRotation(boolean attack, float rotationYawSpeed, float rotationPitchSpeed) {
      class_243 vec = this.target.method_73189().method_1031((double)0.0F, class_3532.method_15350((double)this.mc.field_1724.method_18381(this.mc.field_1724.method_18376()) - this.target.method_23318(), (double)0.0F, (double)this.target.method_17682() * ((double)this.mc.field_1724.method_5739(this.target) / (Double)this.settings.range.get())), (double)0.0F).method_1020(this.mc.field_1724.method_33571());
      this.isRotated = true;
      float yawToTarget = (float)class_3532.method_15338(Math.toDegrees(Math.atan2(vec.field_1350, vec.field_1352)) - (double)90.0F);
      float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(vec.field_1351, class_3532.method_39241(vec.field_1352, vec.field_1350))));
      float yawDelta = class_3532.method_15393(yawToTarget - this.rotateVector.comp_5231());
      float pitchDelta = class_3532.method_15393(pitchToTarget - this.rotateVector.comp_5232());
      int roundedYaw = (int)yawDelta;
      switch (((Type)this.settings.rotationType.get()).ordinal()) {
         case 0:
            float clampedYaw = Math.min(Math.max(Math.abs(yawDelta), 1.0F), rotationYawSpeed);
            float clampedPitch = Math.min(Math.max(Math.abs(pitchDelta), 1.0F), rotationPitchSpeed);
            if (attack && this.selected != this.target && (Boolean)this.settings.speedUpRotationWhenAttacking.get()) {
               clampedPitch = Math.max(Math.abs(pitchDelta), 1.0F);
            } else {
               clampedPitch /= 3.0F;
            }

            if (Math.abs(clampedYaw - this.lastYaw) <= 3.0F) {
               clampedYaw = this.lastYaw + 3.1F;
            }

            float yaw = this.rotateVector.comp_5231() + (yawDelta > 0.0F ? clampedYaw : -clampedYaw);
            float pitch = class_3532.method_15363(this.rotateVector.comp_5232() + (pitchDelta > 0.0F ? clampedPitch : -clampedPitch), -89.0F, 89.0F);
            float gcd = GameSensitivityUtils.getGCDValue();
            yaw -= (yaw - this.rotateVector.comp_5231()) % gcd;
            pitch -= (pitch - this.rotateVector.comp_5232()) % gcd;
            this.rotateVector = new class_5611(yaw, pitch);
            this.lastYaw = clampedYaw;
            this.lastPitch = clampedPitch;
            break;
         case 1:
            float yaw = this.rotateVector.comp_5231() + (float)roundedYaw;
            float pitch = class_3532.method_15363(this.rotateVector.comp_5232() + pitchDelta, -90.0F, 90.0F);
            float gcd = GameSensitivityUtils.getGCDValue();
            yaw -= (yaw - this.rotateVector.comp_5231()) % gcd;
            pitch -= (pitch - this.rotateVector.comp_5232()) % gcd;
            this.rotateVector = new class_5611(yaw, pitch);
      }

   }

   private void reset() {
      this.rotateVector = new class_5611(this.mc.field_1724.method_36454(), this.mc.field_1724.method_36455());
   }

   public static enum Type {
      Smooth,
      Fast;

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{Smooth, Fast};
      }
   }
}
