package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.ServerConnectBeginEvent;
import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_437;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_9112;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_412.class})
public abstract class MixinConnectScreen {
   @Inject(
      method = {"method_36877(Lnet/minecraft/class_437;Lnet/minecraft/class_310;Lnet/minecraft/class_639;Lnet/minecraft/class_642;ZLnet/minecraft/class_9112;)V"},
      at = {@At("HEAD")}
   )
   private static void tryConnectEvent(class_437 screen, class_310 client, class_639 address, class_642 info, boolean quickPlay, class_9112 cookieStorage, CallbackInfo ci) {
      Astra.EVENT_BUS.post(ServerConnectBeginEvent.get(address, info));
   }

   @Inject(
      method = {"method_2130(Lnet/minecraft/class_310;Lnet/minecraft/class_639;Lnet/minecraft/class_642;Lnet/minecraft/class_9112;)V"},
      at = {@At("HEAD")}
   )
   private void tryConnectEvent(class_310 client, class_639 address, class_642 info, class_9112 cookieStorage, CallbackInfo ci) {
      Astra.EVENT_BUS.post(ServerConnectBeginEvent.get(address, info));
   }
}
