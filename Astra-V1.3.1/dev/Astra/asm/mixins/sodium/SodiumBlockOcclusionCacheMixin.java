package dev.Astra.asm.mixins.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.Astra.mod.modules.impl.render.Xray;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
   value = {BlockOcclusionCache.class},
   remap = false
)
public abstract class SodiumBlockOcclusionCacheMixin {
   @ModifyReturnValue(
      method = {"shouldDrawSide"},
      at = {@At("RETURN")}
   )
   private boolean shouldDrawSide(boolean original, class_2680 state, class_1922 view, class_2338 pos, class_2350 facing) {
      return Xray.INSTANCE.isOn() ? Xray.INSTANCE.modifyDrawSide(state, view, pos, facing, original) : original;
   }
}
