package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.exploit.AntiPacket;
import net.minecraft.class_2505;
import net.minecraft.class_2540;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({class_2540.class})
public abstract class MixinPacketByteBuf {
   @ModifyArg(
      method = {"method_56345(Lio/netty/buffer/ByteBuf;)Lnet/minecraft/class_2487;"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2540;method_56340(Lio/netty/buffer/ByteBuf;Lnet/minecraft/class_2505;)Lnet/minecraft/class_2520;"
)
   )
   private static class_2505 xlPackets(class_2505 sizeTracker) {
      return AntiPacket.INSTANCE.isOn() && AntiPacket.INSTANCE.decode.getValue() ? class_2505.method_53898() : sizeTracker;
   }
}
