package dev.Astra.asm.accessors;

import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1309.class})
public interface ILivingEntity {
   @Accessor("lastAttackedTicks")
   int getLastAttackedTicks();

   @Accessor("jumpingCooldown")
   int getLastJumpCooldown();

   @Accessor("jumpingCooldown")
   void setLastJumpCooldown(int var1);

   @Accessor("leaningPitch")
   float getLeaningPitch();

   @Accessor("leaningPitch")
   void setLeaningPitch(float var1);

   @Accessor("lastLeaningPitch")
   void setLastLeaningPitch(float var1);
}
