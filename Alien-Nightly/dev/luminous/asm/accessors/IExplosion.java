package dev.luminous.asm.accessors;

import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1927;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1927.class})
public interface IExplosion {
   @Mutable
   @Accessor("field_9195")
   void setX(double var1);

   @Mutable
   @Accessor("field_9192")
   void setY(double var1);

   @Mutable
   @Accessor("field_9189")
   void setZ(double var1);

   @Mutable
   @Accessor("field_9190")
   void setPower(float var1);

   @Mutable
   @Accessor("field_9185")
   void setEntity(class_1297 var1);

   @Mutable
   @Accessor("field_9187")
   class_1937 getWorld();

   @Mutable
   @Accessor("field_9187")
   void setWorld(class_1937 var1);

   @Mutable
   @Accessor("field_9193")
   class_1282 getDamageSource();
}
