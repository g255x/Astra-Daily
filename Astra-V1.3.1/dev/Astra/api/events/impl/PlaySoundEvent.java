package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_1113;

public class PlaySoundEvent extends Event {
   private static final PlaySoundEvent INSTANCE = new PlaySoundEvent();
   public class_1113 sound;

   public static PlaySoundEvent get(class_1113 sound) {
      INSTANCE.setCancelled(false);
      INSTANCE.sound = sound;
      return INSTANCE;
   }
}
