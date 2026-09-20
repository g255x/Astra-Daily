package nekiplay.meteorplus.mixin.minecraft.entity;

import net.minecraft.class_1606;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1606.class})
public interface ShulkerEntityAccessor {
   @Invoker("method_7115")
   int getPeekAmount();

   @Invoker("method_7124")
   boolean isClosed();
}
