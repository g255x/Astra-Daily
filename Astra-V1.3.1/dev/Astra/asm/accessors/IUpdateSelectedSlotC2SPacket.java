package dev.Astra.asm.accessors;

import net.minecraft.class_2868;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2868.class})
public interface IUpdateSelectedSlotC2SPacket {
   @Accessor("selectedSlot")
   int getSelectedSlot();
}
