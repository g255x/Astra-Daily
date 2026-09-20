package dev.Astra.asm.accessors;

import net.minecraft.class_1268;
import net.minecraft.class_2879;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2879.class})
public interface IHandSwingC2SPacket {
   @Accessor("field_13102")
   class_1268 getHand();
}
