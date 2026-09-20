package dev.Astra.asm.accessors;

import net.minecraft.class_2824;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2824.class})
public interface IPlayerInteractEntityC2SPacket {
   @Accessor("entityId")
   int getEntityId();
}
