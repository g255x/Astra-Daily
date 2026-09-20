package dev.Astra.asm.accessors;

import net.minecraft.class_591;
import net.minecraft.class_630;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_591.class})
public interface IPlayerEntityModel {
   @Accessor("leftPants")
   class_630 getLeftPants();

   @Accessor("rightPants")
   class_630 getRightPants();

   @Accessor("leftSleeve")
   class_630 getLeftSleeve();

   @Accessor("rightSleeve")
   class_630 getRightSleeve();

   @Accessor("jacket")
   class_630 getJacket();
}
