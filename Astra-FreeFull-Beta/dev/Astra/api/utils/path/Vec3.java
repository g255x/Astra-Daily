package dev.Astra.api.utils.path;

import net.minecraft.class_243;

public record Vec3(double x, double y, double z) {
   public Vec3 addVector(double x, double y, double z) {
      return new Vec3(this.x + x, this.y + y, this.z + z);
   }

   public Vec3 floor() {
      return new Vec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
   }

   public double squareDistanceTo(Vec3 v) {
      return Math.pow(v.x - this.x, (double)2.0F) + Math.pow(v.y - this.y, (double)2.0F) + Math.pow(v.z - this.z, (double)2.0F);
   }

   public Vec3 add(Vec3 v) {
      return this.addVector(v.x(), v.y(), v.z());
   }

   public class_243 mc() {
      return new class_243(this.x, this.y, this.z);
   }

   public String toString() {
      return "[" + this.x + ";" + this.y + ";" + this.z + "]";
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Vec3 vec3)) {
         return false;
      } else {
         double x1;
         try {
            x1 = vec3.x();
         } catch (Throwable throwable) {
            throw new MatchException(throwable.toString(), throwable);
         }

         double y1 = vec3.y();
         double z1 = vec3.z();
         if (this.x != x1) {
            return false;
         } else if (this.y != y1) {
            return false;
         } else {
            return this.z == z1;
         }
      }
   }
}
