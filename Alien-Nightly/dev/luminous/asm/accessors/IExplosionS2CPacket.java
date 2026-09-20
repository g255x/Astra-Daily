package dev.luminous.asm.accessors;

import net.minecraft.class_2664;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2664.class})
public interface IExplosionS2CPacket {
   @Mutable
   @Accessor("field_12176")
   void setVelocityX(float var1);

   @Mutable
   @Accessor("field_12183")
   void setVelocityY(float var1);

   @Mutable
   @Accessor("field_12182")
   void setVelocityZ(float var1);

   @Accessor("field_12176")
   float getX();

   @Accessor("field_12183")
   float getY();

   @Accessor("field_12182")
   float getZ();
}
