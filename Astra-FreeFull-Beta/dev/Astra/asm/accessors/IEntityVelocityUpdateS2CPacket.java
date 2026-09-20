package dev.Astra.asm.accessors;

import net.minecraft.class_2743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2743.class})
public interface IEntityVelocityUpdateS2CPacket {
   @Mutable
   @Accessor("field_12563")
   void setX(int var1);

   @Mutable
   @Accessor("field_12562")
   void setY(int var1);

   @Mutable
   @Accessor("field_12561")
   void setZ(int var1);

   @Accessor("field_12563")
   int getX();

   @Accessor("field_12562")
   int getY();

   @Accessor("field_12561")
   int getZ();
}
