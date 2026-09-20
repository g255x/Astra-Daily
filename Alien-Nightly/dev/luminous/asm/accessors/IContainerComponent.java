package dev.luminous.asm.accessors;

import net.minecraft.class_1799;
import net.minecraft.class_2371;
import net.minecraft.class_9288;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_9288.class})
public interface IContainerComponent {
   @Accessor("field_49338")
   class_2371<class_1799> getStacks();
}
