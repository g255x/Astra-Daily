package dev.Astra.asm.accessors;

import net.minecraft.class_1703;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1703.class})
public interface IScreenHandler {
   @Accessor("field_34024")
   int getRevision();

   @Accessor("field_34024")
   void setRevision(int var1);
}
