package dev.Astra.asm.accessors;

import net.minecraft.class_1268;
import net.minecraft.class_2886;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2886.class})
public interface IPlayerInteractItemC2SPacket {
   @Accessor("hand")
   class_1268 getHand();
}
