package dev.Astra.asm.accessors;

import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_636.class})
public interface IClientPlayerInteractionManager {
   @Accessor("lastSelectedSlot")
   int getLastSelectedSlot();

   @Accessor("lastSelectedSlot")
   void setLastSelectedSlot(int var1);
}
