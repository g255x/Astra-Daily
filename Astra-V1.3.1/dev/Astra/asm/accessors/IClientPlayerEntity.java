package dev.Astra.asm.accessors;

import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_746.class})
public interface IClientPlayerEntity {
   @Accessor("ticksSinceLastPositionPacketSent")
   void setTicksSinceLastPositionPacketSent(int var1);

   @Accessor("lastYaw")
   float getLastYaw();

   @Accessor("lastYaw")
   void setLastYaw(float var1);

   @Accessor("lastPitch")
   float getLastPitch();

   @Accessor("lastPitch")
   void setLastPitch(float var1);
}
