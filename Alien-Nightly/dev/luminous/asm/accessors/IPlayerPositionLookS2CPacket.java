package dev.luminous.asm.accessors;

import net.minecraft.class_2708;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2708.class})
public interface IPlayerPositionLookS2CPacket {
   @Mutable
   @Accessor("field_12393")
   void setYaw(float var1);

   @Mutable
   @Accessor("field_12391")
   void setPitch(float var1);
}
