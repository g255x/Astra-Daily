package dev.Astra.asm.accessors;

import net.minecraft.class_630;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_630.class_628.class})
public interface IModelPartCuboid {
   @Accessor("field_3649")
   class_630.class_593[] getSides();
}
