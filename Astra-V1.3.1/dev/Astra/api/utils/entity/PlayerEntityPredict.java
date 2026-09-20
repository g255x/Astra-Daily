package dev.Astra.api.utils.entity;

import net.minecraft.class_1657;

public class PlayerEntityPredict {
   public final class_1657 player;
   public final class_1657 predict;

   public PlayerEntityPredict(class_1657 player, double maxMotionY, int ticks, int simulation, boolean step, boolean doubleStep, boolean jump, boolean inBlockPause) {
      this.player = player;
      this.predict = (class_1657)(ticks > 0 ? new CopyPlayerEntity(player, true, maxMotionY, ticks, simulation, step, doubleStep, jump, inBlockPause) : player);
   }
}
