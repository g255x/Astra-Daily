package dev.Astra.api.events.impl;

import net.minecraft.class_2338;

public class BlockBreakingProgressEvent {
   private static final BlockBreakingProgressEvent INSTANCE = new BlockBreakingProgressEvent();
   private class_2338 pos;
   private int breakerId;
   private int progress;

   private BlockBreakingProgressEvent() {
   }

   public static BlockBreakingProgressEvent get(class_2338 pos, int breakerId, int progress) {
      INSTANCE.pos = pos;
      INSTANCE.breakerId = breakerId;
      INSTANCE.progress = progress;
      return INSTANCE;
   }

   public class_2338 getPosition() {
      return this.pos;
   }

   public int getBreakerId() {
      return this.breakerId;
   }

   public int getProgress() {
      return this.progress;
   }
}
