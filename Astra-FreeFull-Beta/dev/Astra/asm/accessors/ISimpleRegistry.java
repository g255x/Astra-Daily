package dev.Astra.asm.accessors;

import java.util.Map;
import net.minecraft.class_2370;
import net.minecraft.class_6880;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2370.class})
public interface ISimpleRegistry<T> {
   @Accessor("field_36461")
   Map<T, class_6880.class_6883<T>> getValueToEntry();
}
