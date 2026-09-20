package dev.Astra.asm.accessors;

import net.minecraft.class_2815;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2815.class})
public interface ICloseHandledScreenC2SPacket {
   @Accessor("syncId")
   int getSyncId();
}
