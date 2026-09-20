package dev.Astra.asm.accessors;

import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2846.class})
public interface IPlayerActionC2SPacket {
   @Accessor("pos")
   class_2338 getPos();

   @Accessor("direction")
   class_2350 getDirection();

   @Accessor("action")
   class_2846.class_2847 getAction();
}
