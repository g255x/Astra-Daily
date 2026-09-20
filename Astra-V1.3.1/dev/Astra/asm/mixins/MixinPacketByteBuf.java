package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.exploit.AntiPacket;
import net.minecraft.class_2505;
import net.minecraft.class_2540;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({class_2540.class})
public abstract class MixinPacketByteBuf {
   @ModifyArg(
      method = {"readNbt(Lio/netty/buffer/ByteBuf;)Lnet/minecraft/nbt/NbtCompound;"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/network/PacketByteBuf;readNbt(Lio/netty/buffer/ByteBuf;Lnet/minecraft/nbt/NbtSizeTracker;)Lnet/minecraft/nbt/NbtElement;"
)
   )
   private static class_2505 xlPackets(class_2505 sizeTracker) {
      return AntiPacket.INSTANCE.isOn() && AntiPacket.INSTANCE.decode.getValue() ? class_2505.method_53898() : sizeTracker;
   }
}
