package dev.Astra.core.impl;

import java.util.ArrayDeque;
import java.util.Deque;

public class FPSManager {
   private final Deque<Long> records = new ArrayDeque();

   public void record() {
      this.records.addLast(System.currentTimeMillis());
   }

   public int getFps() {
      long now = System.currentTimeMillis();

      while(!this.records.isEmpty() && (Long)this.records.peekFirst() + 1000L < now) {
         this.records.pollFirst();
      }

      return this.records.size();
   }
}
