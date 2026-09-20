package dev.Astra.asm.accessors;

import net.minecraft.class_1268;
import net.minecraft.class_2885;
import net.minecraft.class_3965;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2885.class})
public interface IPlayerInteractBlockC2SPacket {
   @Accessor("blockHitResult")
   class_3965 getBlockHitResult();

   @Accessor("hand")
   class_1268 getHand();
}
