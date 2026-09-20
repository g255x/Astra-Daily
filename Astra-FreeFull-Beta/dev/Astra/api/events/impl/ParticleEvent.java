package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_703;

public class ParticleEvent extends Event {
   private static final ParticleEvent instance = new ParticleEvent();
   public class_703 particle;

   public static ParticleEvent get(class_703 particle) {
      instance.particle = particle;
      instance.setCancelled(false);
      return instance;
   }
}
