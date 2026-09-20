package dev.luminous.asm.accessors;

import net.minecraft.class_1799;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_759.class})
public interface IHeldItemRenderer {
   @Accessor("field_4043")
   float getEquippedProgressMainHand();

   @Accessor("field_4043")
   void setEquippedProgressMainHand(float var1);

   @Accessor("field_4052")
   float getEquippedProgressOffHand();

   @Accessor("field_4052")
   void setEquippedProgressOffHand(float var1);

   @Accessor("field_4047")
   void setItemStackMainHand(class_1799 var1);

   @Accessor("field_4048")
   void setItemStackOffHand(class_1799 var1);
}
