package dev.luminous.asm.accessors;

import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_636.class})
public interface IClientPlayerInteractionManager {
   @Accessor("field_3721")
   int getLastSelectedSlot();

   @Accessor("field_3721")
   void setLastSelectedSlot(int var1);
}
