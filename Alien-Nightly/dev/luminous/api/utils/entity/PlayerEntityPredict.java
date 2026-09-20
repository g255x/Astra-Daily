package dev.luminous.api.utils.entity;

import net.minecraft.class_1657;

public class PlayerEntityPredict {
   public final class_1657 player;
   public final class_1657 predict;

   public PlayerEntityPredict(class_1657 player, double maxMotionY, int ticks, int simulation, boolean step, boolean doubleStep, boolean jump, boolean inBlockPause) {
      this.player = player;
      if (ticks > 0) {
         this.predict = new CopyPlayerEntity(player, true, maxMotionY, ticks, simulation, step, doubleStep, jump, inBlockPause);
      } else {
         this.predict = player;
      }

   }
}
