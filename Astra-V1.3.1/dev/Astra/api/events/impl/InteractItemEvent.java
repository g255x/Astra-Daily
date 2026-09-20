package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_1268;

public class InteractItemEvent extends Event {
   private static final InteractItemEvent INSTANCE = new InteractItemEvent();
   public class_1268 hand;

   private InteractItemEvent() {
   }

   public static InteractItemEvent getPre(class_1268 hand) {
      INSTANCE.hand = hand;
      INSTANCE.stage = Event.Stage.Pre;
      INSTANCE.setCancelled(false);
      return INSTANCE;
   }

   public static InteractItemEvent getPost(class_1268 hand) {
      INSTANCE.hand = hand;
      INSTANCE.stage = Event.Stage.Post;
      return INSTANCE;
   }
}
