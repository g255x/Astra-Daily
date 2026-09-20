package dev.Astra.asm.accessors;

import net.minecraft.class_591;
import net.minecraft.class_630;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_591.class})
public interface IPlayerEntityModel {
   @Accessor("field_3482")
   class_630 getLeftPants();

   @Accessor("field_3479")
   class_630 getRightPants();

   @Accessor("field_3484")
   class_630 getLeftSleeve();

   @Accessor("field_3486")
   class_630 getRightSleeve();

   @Accessor("field_3483")
   class_630 getJacket();
}
