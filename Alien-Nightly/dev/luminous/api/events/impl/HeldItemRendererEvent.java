package dev.luminous.api.events.impl;

import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_4587;

public class HeldItemRendererEvent {
   private static final HeldItemRendererEvent INSTANCE = new HeldItemRendererEvent();
   private class_1268 hand;
   private class_1799 item;
   private float ep;
   private class_4587 stack;

   private HeldItemRendererEvent() {
   }

   public static HeldItemRendererEvent get(class_1268 hand, class_1799 item, float equipProgress, class_4587 stack) {
      INSTANCE.hand = hand;
      INSTANCE.item = item;
      INSTANCE.ep = equipProgress;
      INSTANCE.stack = stack;
      return INSTANCE;
   }

   public class_1268 getHand() {
      return this.hand;
   }

   public class_1799 getItem() {
      return this.item;
   }

   public float getEp() {
      return this.ep;
   }

   public class_4587 getStack() {
      return this.stack;
   }
}
