package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_1268;

public class InteractBlockEvent extends Event {
   private static final InteractBlockEvent INSTANCE = new InteractBlockEvent();
   public class_1268 hand;

   private InteractBlockEvent() {
   }

   public static InteractBlockEvent getPre(class_1268 hand) {
      INSTANCE.hand = hand;
      INSTANCE.stage = Event.Stage.Pre;
      INSTANCE.setCancelled(false);
      return INSTANCE;
   }

   public static InteractBlockEvent getPost(class_1268 hand) {
      INSTANCE.hand = hand;
      INSTANCE.stage = Event.Stage.Post;
      return INSTANCE;
   }
}
