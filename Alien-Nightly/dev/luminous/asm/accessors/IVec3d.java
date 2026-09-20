package dev.luminous.asm.accessors;

import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_243.class})
public interface IVec3d {
   @Mutable
   @Accessor("field_1352")
   void setX(double var1);

   @Mutable
   @Accessor("field_1351")
   void setY(double var1);

   @Mutable
   @Accessor("field_1350")
   void setZ(double var1);
}
