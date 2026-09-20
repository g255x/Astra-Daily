package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.TimeoutException;
import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2548;
import net.minecraft.class_2596;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_2535.class})
public class MixinClientConnection {
   @Inject(
      at = {@At("HEAD")},
      method = {"method_10759"},
      cancellable = true
   )
   private static <T extends class_2547> void onHandlePacket(class_2596<T> packet, class_2547 listener, CallbackInfo ci) {
      PacketEvent.Receive event = new PacketEvent.Receive(packet);
      Alien.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_10743(Lnet/minecraft/class_2596;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendPacketPre(class_2596<?> packet, CallbackInfo info) {
      PacketEvent.Send event = new PacketEvent.Send(packet);
      Alien.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         info.cancel();
      }

   }

   @Inject(
      method = {"method_10743(Lnet/minecraft/class_2596;)V"},
      at = {@At("RETURN")}
   )
   private void onSendPacketPost(class_2596<?> packet, CallbackInfo info) {
      PacketEvent.Sent event = new PacketEvent.Sent(packet);
      Alien.EVENT_BUS.post(event);
   }

   @Inject(
      method = {"exceptionCaught"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void exceptionCaught(ChannelHandlerContext context, Throwable throwable, CallbackInfo ci) {
      if (!(throwable instanceof TimeoutException) && !(throwable instanceof class_2548) && ClientSetting.INSTANCE.caughtException.getValue()) {
         if (ClientSetting.INSTANCE.log.getValue()) {
            CommandManager.sendMessage("§4Caught exception §7" + throwable.getMessage());
         }

         ci.cancel();
      }

   }
}
