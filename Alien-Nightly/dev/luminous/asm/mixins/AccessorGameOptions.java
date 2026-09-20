package dev.luminous.asm.mixins;

import java.util.Set;
import net.minecraft.class_1664;
import net.minecraft.class_315;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_315.class})
public interface AccessorGameOptions {
   @Accessor("field_1892")
   @Mutable
   Set<class_1664> getPlayerModelParts();
}
