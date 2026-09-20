package nekiplay.meteorplus.mixin.minecraft;

import net.minecraft.class_1867;
import net.minecraft.class_7710;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1867.class})
public interface ShapelessRecipeAccessor {
   @Accessor("field_40257")
   class_7710 getCategory();
}
