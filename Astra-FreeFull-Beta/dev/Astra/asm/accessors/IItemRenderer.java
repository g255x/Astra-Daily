package dev.Astra.asm.accessors;

import net.minecraft.class_1087;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_756;
import net.minecraft.class_918;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_918.class})
public interface IItemRenderer {
   @Accessor("field_27770")
   class_756 hookGetBuiltinModelItemRenderer();

   @Invoker("method_23182")
   void hookRenderBakedItemModel(class_1087 var1, class_1799 var2, int var3, int var4, class_4587 var5, class_4588 var6);
}
