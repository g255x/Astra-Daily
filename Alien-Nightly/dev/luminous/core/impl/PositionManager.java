package dev.luminous.core.impl;

import dev.luminous.core.Manager;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2828;
import net.minecraft.class_3532;

public class PositionManager extends Manager {
   private double x;
   private double y;
   private double z;
   private class_2338 blockPos;
   private boolean sneaking;
   private boolean sprinting;
   private boolean onGround;

   public void setPosition(class_243 vec3d) {
      this.setPosition(vec3d.method_10216(), vec3d.method_10214(), vec3d.method_10215());
   }

   public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
      this.setPositionClient(x, y, z);
      mc.method_1562().method_52787(new class_2828.class_2830(x, y, z, yaw, pitch, this.isOnGround()));
   }

   public void setPosition(double x, double y, double z) {
      this.setPositionClient(x, y, z);
      mc.method_1562().method_52787(new class_2828.class_2829(x, y, z, this.isOnGround()));
   }

   public void setPositionClient(double x, double y, double z) {
      if (mc.field_1724.method_3144()) {
         mc.field_1724.method_5854().method_5814(x, y, z);
      } else {
         mc.field_1724.method_5814(x, y, z);
      }
   }

   public void setPositionXZ(double x, double z) {
      this.setPosition(x, this.y, z);
   }

   public void setPositionY(double y) {
      this.setPosition(this.x, y, this.z);
   }

   public class_243 getPos() {
      return new class_243(this.getX(), this.getY(), this.getZ());
   }

   public class_243 getEyePos() {
      return this.getPos().method_1031((double)0.0F, (double)mc.field_1724.method_5751(), (double)0.0F);
   }

   public final class_243 getCameraPosVec(float tickDelta) {
      double d = class_3532.method_16436((double)tickDelta, mc.field_1724.field_6014, this.getX());
      double e = class_3532.method_16436((double)tickDelta, mc.field_1724.field_6036, this.getY()) + (double)mc.field_1724.method_5751();
      double f = class_3532.method_16436((double)tickDelta, mc.field_1724.field_5969, this.getZ());
      return new class_243(d, e, f);
   }

   public double squaredDistanceTo(class_1297 entity) {
      float f = (float)(this.getX() - entity.method_23317());
      float g = (float)(this.getY() - entity.method_23318());
      float h = (float)(this.getZ() - entity.method_23321());
      return class_3532.method_41190((double)f, (double)g, (double)h);
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

   public class_2338 getBlockPos() {
      return this.blockPos;
   }

   public boolean isSneaking() {
      return this.sneaking;
   }

   public boolean isSprinting() {
      return this.sprinting;
   }

   public boolean isOnGround() {
      return this.onGround;
   }
}
