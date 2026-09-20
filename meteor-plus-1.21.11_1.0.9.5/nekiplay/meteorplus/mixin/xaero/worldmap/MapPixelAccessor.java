package nekiplay.meteorplus.mixin.xaero.worldmap;

import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import xaero.map.region.MapPixel;

@Mixin({MapPixel.class})
public interface MapPixelAccessor {
   @Accessor("state")
   class_2680 getBlockState();
}
