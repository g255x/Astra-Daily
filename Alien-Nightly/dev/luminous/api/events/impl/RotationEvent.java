package dev.luminous.api.events.impl;

import net.minecraft.class_243;

public class RotationEvent {
   private static final RotationEvent instance = new RotationEvent();
   public float priority;
   private class_243 target;
   private float yaw;
   private float pitch;
   private boolean rotation;
   private float speed;

   private RotationEvent() {
   }

   public static RotationEvent get() {
      instance.priority = 0.0F;
      instance.target = null;
      instance.yaw = 0.0F;
      instance.pitch = 0.0F;
      instance.rotation = false;
      instance.speed = 0.0F;
      return instance;
   }

   public class_243 getTarget() {
      return this.target;
   }

   public float getSpeed() {
      return this.speed;
   }

   public boolean getRotation() {
      return this.rotation;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setTarget(class_243 target, float speed, float priority) {
      if (priority >= this.priority) {
         this.rotation = false;
         this.priority = priority;
         this.target = target;
         this.speed = speed;
      }

   }

   public void setRotation(float yaw, float pitch, float speed, float priority) {
      if (priority >= this.priority) {
         this.rotation = true;
         this.priority = priority;
         this.yaw = yaw;
         this.pitch = pitch;
         this.speed = speed;
      }

   }
}
