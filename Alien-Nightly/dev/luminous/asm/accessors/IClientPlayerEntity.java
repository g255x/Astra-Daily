package dev.luminous.asm.accessors;

import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_746.class})
public interface IClientPlayerEntity {
   @Accessor("field_3923")
   void setTicksSinceLastPositionPacketSent(int var1);

   @Accessor("field_3941")
   float getLastYaw();

   @Accessor("field_3941")
   void setLastYaw(float var1);

   @Accessor("field_3925")
   float getLastPitch();

   @Accessor("field_3925")
   void setLastPitch(float var1);
}
