package dev.Astra.asm.accessors;

import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1309.class})
public interface ILivingEntity {
   @Accessor("field_6273")
   int getLastAttackedTicks();

   @Accessor("field_6228")
   int getLastJumpCooldown();

   @Accessor("field_6228")
   void setLastJumpCooldown(int var1);

   @Accessor("field_6243")
   float getLeaningPitch();

   @Accessor("field_6243")
   void setLeaningPitch(float var1);

   @Accessor("field_6264")
   void setLastLeaningPitch(float var1);
}
