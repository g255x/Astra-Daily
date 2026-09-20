package dev.Astra.asm.accessors;

import net.minecraft.class_310;
import net.minecraft.class_320;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_310.class})
public interface IMinecraftClient {
   @Mutable
   @Accessor("session")
   void setSession(class_320 var1);

   @Invoker("doItemUse")
   void invokeDoItemUse();
}
