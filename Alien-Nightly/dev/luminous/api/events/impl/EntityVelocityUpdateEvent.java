package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_1297;

public class EntityVelocityUpdateEvent extends Event {
   private static final EntityVelocityUpdateEvent INSTANCE = new EntityVelocityUpdateEvent();
   private class_1297 entity;
   private double x;
   private double y;
   private double z;
   private boolean explosion;

   private EntityVelocityUpdateEvent() {
   }

   public static EntityVelocityUpdateEvent get(class_1297 entity, double x, double y, double z, boolean explosion) {
      INSTANCE.entity = entity;
      INSTANCE.x = x;
      INSTANCE.y = y;
      INSTANCE.z = z;
      INSTANCE.explosion = explosion;
      INSTANCE.setCancelled(false);
      return INSTANCE;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public void setX(double x) {
      this.x = x;
   }

   public void setY(double y) {
      this.y = y;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public class_1297 getEntity() {
      return this.entity;
   }

   public void setEntity(class_1297 entity) {
      this.entity = entity;
   }

   public boolean isExplosion() {
      return this.explosion;
   }
}
