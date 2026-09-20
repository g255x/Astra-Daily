package dev.luminous.api.utils.combat;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2824;
import net.minecraft.class_742;

public class CombatUtil implements Wrapper {
   public static final Timer breakTimer = new Timer();
   public static boolean terrainIgnore = false;
   public static class_2338 modifyPos;
   public static class_2680 modifyBlockState;

   public static List<class_1657> getEnemies(double range) {
      List<class_1657> list = new ArrayList();

      for(class_742 player : Alien.THREAD.getPlayers()) {
         if (isValid(player, range)) {
            list.add(player);
         }
      }

      return list;
   }

   public static void attackCrystal(class_2338 pos, boolean rotate, boolean eatingPause) {
      attackCrystal(new class_238(pos), rotate, eatingPause);
   }

   public static void attackCrystal(class_238 box, boolean rotate, boolean eatingPause) {
      for(class_1511 entity : BlockUtil.getEndCrystals(box)) {
         attackWithDelay(entity, rotate, eatingPause);
      }

   }

   public static void attackWithDelay(class_1297 entity, boolean rotate, boolean usingPause) {
      if (breakTimer.passed((long)(AntiCheat.INSTANCE.attackDelay.getValue() * (double)1000.0F)) && (!usingPause || !mc.field_1724.method_6115())) {
         attack(entity, rotate);
      }

   }

   public static void attack(class_1297 entity, boolean rotate) {
      if (entity != null) {
         class_243 attackVec = MathUtil.getClosestPointToBox(mc.field_1724.method_33571(), entity.method_5829());
         if (mc.field_1724.method_33571().method_1022(attackVec) > AntiCheat.INSTANCE.ieRange.getValue()) {
            return;
         }

         breakTimer.reset();
         if (rotate && AntiCheat.INSTANCE.attackRotate.getValue()) {
            Alien.ROTATION.lookAt(attackVec);
         }

         mc.method_1562().method_52787(class_2824.method_34206(entity, mc.field_1724.method_5715()));
         mc.field_1724.method_7350();
         EntityUtil.swingHand(class_1268.field_5808, AntiCheat.INSTANCE.attackSwing.getValue());
         if (rotate && AntiCheat.INSTANCE.attackRotate.getValue()) {
            Alien.ROTATION.snapBack();
         }
      }

   }

   public static boolean isntValid(class_1297 entity, double range) {
      return !isValid(entity, range);
   }

   public static boolean isValid(class_1297 entity, double range) {
      boolean var10000;
      label27: {
         if (entity != null && entity.method_5805() && !entity.equals(mc.field_1724)) {
            label23: {
               if (entity instanceof class_1657) {
                  class_1657 player = (class_1657)entity;
                  if (Alien.FRIEND.isFriend(player)) {
                     break label23;
                  }
               }

               if (!(mc.field_1724.method_19538().method_1022(entity.method_19538()) > range)) {
                  var10000 = false;
                  break label27;
               }
            }
         }

         var10000 = true;
      }

      boolean invalid = var10000;
      return !invalid;
   }

   public static boolean isValid(class_1297 entity) {
      boolean var10000;
      label37: {
         if (entity != null && entity.method_5805() && !entity.equals(mc.field_1724)) {
            label34: {
               if (entity instanceof class_1657) {
                  class_1657 player = (class_1657)entity;
                  if (Alien.FRIEND.isFriend(player)) {
                     break label34;
                  }
               }

               var10000 = false;
               break label37;
            }
         }

         var10000 = true;
      }

      boolean invalid = var10000;
      return !invalid;
   }

   public static class_1657 getClosestEnemy(double distance) {
      class_1657 closest = null;

      for(class_1657 player : getEnemies(distance)) {
         if (closest == null) {
            closest = player;
         } else if (mc.field_1724.method_5707(player.method_19538()) < mc.field_1724.method_5858(closest)) {
            closest = player;
         }
      }

      return closest;
   }

   static {
      modifyBlockState = class_2246.field_10124.method_9564();
   }
}
