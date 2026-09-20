package dev.luminous.api.utils.entity;

import dev.luminous.api.utils.Wrapper;
import net.minecraft.class_1268;
import net.minecraft.class_1657;

public class PauseUtil implements Wrapper {
   public static boolean checkPause(boolean sameHand, class_1657 player, class_1268 useHand) {
      return sameHand ? player.method_6115() && player.method_6058() == useHand : player.method_6115();
   }

   public static boolean checkPause(boolean sameHand, class_1268 useHand) {
      return checkPause(sameHand, mc.field_1724, useHand);
   }

   public static boolean checkPause(boolean sameHand, class_1657 player) {
      return checkPause(sameHand, player, class_1268.field_5808);
   }

   public static boolean checkPause(boolean sameHand) {
      return checkPause(sameHand, (class_1657)mc.field_1724);
   }
}
