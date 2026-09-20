package dev.Astra.api.events.impl;

import dev.Astra.api.events.Event;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public class FireworkShooterRotationEvent extends Event {
   private static final FireworkShooterRotationEvent instance = new FireworkShooterRotationEvent();
   public class_1309 shooter;
   public float pitch;
   public float yaw;

   private FireworkShooterRotationEvent() {
   }

   public static FireworkShooterRotationEvent get(class_1309 shooter, float yaw, float pitch) {
      instance.shooter = shooter;
      instance.yaw = yaw;
      instance.pitch = pitch;
      instance.setCancelled(false);
      return instance;
   }

   public final class_243 getRotationVector() {
      float f = this.pitch * ((float)Math.PI / 180F);
      float g = -this.yaw * ((float)Math.PI / 180F);
      float h = class_3532.method_15362(g);
      float i = class_3532.method_15374(g);
      float j = class_3532.method_15362(f);
      float k = class_3532.method_15374(f);
      return new class_243((double)(i * j), (double)(-k), (double)(h * j));
   }
}
