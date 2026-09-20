package dev.Astra.asm.mixins;

import net.minecraft.class_2804;
import net.minecraft.class_3560;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({class_3560.class})
public class MixinLightStorage {
   @Redirect(
      method = {"get(J)I"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/chunk/ChunkNibbleArray;get(III)I"
),
      require = 0
   )
   private static int redirectChunkNibbleGetDesc(class_2804 chunkNibbleArray, int x, int y, int z) {
      return chunkNibbleArray == null ? 0 : chunkNibbleArray.method_12139(x, y, z);
   }

   @Redirect(
      method = {"get"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/chunk/ChunkNibbleArray;get(III)I"
),
      require = 0
   )
   private static int redirectChunkNibbleGet(class_2804 chunkNibbleArray, int x, int y, int z) {
      return chunkNibbleArray == null ? 0 : chunkNibbleArray.method_12139(x, y, z);
   }
}
