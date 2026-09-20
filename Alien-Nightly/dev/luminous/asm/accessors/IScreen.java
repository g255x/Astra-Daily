package dev.luminous.asm.accessors;

import java.util.List;
import net.minecraft.class_4068;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_437.class})
public interface IScreen {
   @Accessor("field_33816")
   List<class_4068> getDrawables();
}
