package dev.Astra.asm.accessors;

import net.minecraft.class_6374;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_6374.class})
public interface ICommonPongC2SPacket {
   @Accessor("parameter")
   int getParameter();
}
