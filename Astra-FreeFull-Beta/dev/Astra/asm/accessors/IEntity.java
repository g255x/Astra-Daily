package dev.Astra.asm.accessors;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_4048;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1297.class})
public interface IEntity {
   @Mutable
   @Accessor("field_22467")
   void setPos(class_243 var1);

   @Accessor("field_18065")
   class_4048 getDimensions();
}
