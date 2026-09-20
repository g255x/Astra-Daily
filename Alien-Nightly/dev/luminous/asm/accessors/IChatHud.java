package dev.luminous.asm.accessors;

import java.util.List;
import net.minecraft.class_303;
import net.minecraft.class_338;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_338.class})
public interface IChatHud {
   @Mutable
   @Accessor("field_2064")
   void setVisibleMessages(List<class_303.class_7590> var1);

   @Mutable
   @Accessor("field_2061")
   void setMessages(List<class_303.class_7590> var1);
}
