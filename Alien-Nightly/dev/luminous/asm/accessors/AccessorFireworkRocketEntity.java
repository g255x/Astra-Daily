package dev.luminous.asm.accessors;

import net.minecraft.class_1309;
import net.minecraft.class_1671;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1671.class})
public interface AccessorFireworkRocketEntity {
   @Accessor("field_7616")
   class_1309 hookGetShooter();

   @Invoker("method_7476")
   boolean hookWasShotByEntity();

   @Invoker("method_16830")
   void hookExplodeAndRemove();
}
