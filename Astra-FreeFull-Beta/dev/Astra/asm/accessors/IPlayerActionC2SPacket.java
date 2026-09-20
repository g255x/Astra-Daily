package dev.Astra.asm.accessors;

import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2846.class})
public interface IPlayerActionC2SPacket {
   @Accessor("field_12967")
   class_2338 getPos();

   @Accessor("field_12965")
   class_2350 getDirection();

   @Accessor("field_12966")
   class_2846.class_2847 getAction();
}
