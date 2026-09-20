package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.EntityVelocityUpdateEvent;
import dev.Astra.api.events.impl.GameLeftEvent;
import dev.Astra.api.events.impl.InventoryS2CPacketEvent;
import dev.Astra.api.events.impl.S2CCloseScreenEvent;
import dev.Astra.api.events.impl.SendMessageEvent;
import dev.Astra.api.events.impl.ServerChangePositionEvent;
import dev.Astra.mod.modules.impl.exploit.AntiPacket;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_2535;
import net.minecraft.class_2600;
import net.minecraft.class_2602;
import net.minecraft.class_2645;
import net.minecraft.class_2649;
import net.minecraft.class_2678;
import net.minecraft.class_2708;
import net.minecraft.class_2709;
import net.minecraft.class_2793;
import net.minecraft.class_2828;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_8588;
import net.minecraft.class_8673;
import net.minecraft.class_8675;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_634.class})
public abstract class MixinClientPlayNetworkHandler extends class_8673 {
   @Shadow
   private class_638 field_3699;
   @Unique
   private boolean frog$worldNotNull;
   @Unique
   private boolean ignore;

   protected MixinClientPlayNetworkHandler(class_310 client, class_2535 connection, class_8675 connectionState) {
      super(client, connection, connectionState);
   }

   @Inject(
      method = {"method_52798"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2600;method_11074(Lnet/minecraft/class_2596;Lnet/minecraft/class_2547;Lnet/minecraft/class_1255;)V",
   shift = Shift.AFTER
)}
   )
   private void onEnterReconfiguration(class_8588 packet, CallbackInfo info) {
      Astra.EVENT_BUS.post(GameLeftEvent.INSTANCE);
   }

   @Inject(
      method = {"method_11120"},
      at = {@At("HEAD")}
   )
   private void onGameJoinHead(class_2678 packet, CallbackInfo info) {
      this.frog$worldNotNull = this.field_3699 != null;
   }

   @Inject(
      method = {"method_11120"},
      at = {@At("TAIL")}
   )
   private void onGameJoinTail(class_2678 packet, CallbackInfo info) {
      if (this.frog$worldNotNull) {
         Astra.EVENT_BUS.post(GameLeftEvent.INSTANCE);
      }

   }

   @Shadow
   public abstract void method_45729(String var1);

   @Inject(
      method = {"method_11153"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2600;method_11074(Lnet/minecraft/class_2596;Lnet/minecraft/class_2547;Lnet/minecraft/class_1255;)V",
   shift = Shift.AFTER
)},
      cancellable = true
   )
   public void onInventoryS2CPacket(class_2649 packet, CallbackInfo ci) {
      InventoryS2CPacketEvent event = InventoryS2CPacketEvent.get(packet);
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_45729"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendChatMessage(String message, CallbackInfo ci) {
      if (!this.ignore) {
         if (message.startsWith(Astra.getPrefix())) {
            Astra.COMMAND.command(message.split(" "));
            ci.cancel();
         } else {
            SendMessageEvent event = SendMessageEvent.get(message);
            Astra.EVENT_BUS.post(event);
            if (event.isCancelled()) {
               ci.cancel();
            } else if (!event.message.equals(event.defaultMessage)) {
               this.ignore = true;
               this.method_45729(event.message);
               this.ignore = false;
               ci.cancel();
            }
         }

      }
   }

   @Inject(
      method = {"method_11102"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2600;method_11074(Lnet/minecraft/class_2596;Lnet/minecraft/class_2547;Lnet/minecraft/class_1255;)V",
   shift = Shift.AFTER
)},
      cancellable = true
   )
   public void onCloseScreen(class_2645 packet, CallbackInfo ci) {
      S2CCloseScreenEvent event = S2CCloseScreenEvent.get();
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }

   @Redirect(
      method = {"method_11132"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1297;method_5750(DDD)V"
),
      require = 0
   )
   private void velocityHook(class_1297 instance, double x, double y, double z) {
      EntityVelocityUpdateEvent event = EntityVelocityUpdateEvent.get(instance, x, y, z, false);
      Astra.EVENT_BUS.post(event);
      if (!event.isCancelled()) {
         instance.method_5750(event.getX(), event.getY(), event.getZ());
      }

   }

   @Redirect(
      method = {"method_11124"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_243;method_1031(DDD)Lnet/minecraft/class_243;"
),
      require = 0
   )
   private class_243 velocityHook2(class_243 instance, double x, double y, double z) {
      EntityVelocityUpdateEvent event = EntityVelocityUpdateEvent.get(this.field_45588.field_1724, x, y, z, true);
      Astra.EVENT_BUS.post(event);
      return !event.isCancelled() ? instance.method_1031(event.getX(), event.getY(), event.getZ()) : instance;
   }

   @Inject(
      method = {"method_11157"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2600;method_11074(Lnet/minecraft/class_2596;Lnet/minecraft/class_2547;Lnet/minecraft/class_1255;)V",
   shift = Shift.AFTER
)},
      cancellable = true
   )
   public void onPlayerPositionLook(class_2708 packet, CallbackInfo ci) {
      boolean noRotate = AntiPacket.INSTANCE.isOn() && AntiPacket.INSTANCE.s2CRotate.getValue() && Astra.SERVER.playerNull.passedS((double)0.25F);
      if (noRotate) {
         ci.cancel();
         class_2600.method_11074(packet, (class_2602)class_634.class.cast(this), this.field_45588);
         class_746 playerEntity = this.field_45588.field_1724;
         class_243 vec3d = playerEntity.method_18798();
         boolean bl2 = packet.method_11733().contains(class_2709.field_12400);
         boolean bl22 = packet.method_11733().contains(class_2709.field_12398);
         boolean bl3 = packet.method_11733().contains(class_2709.field_12403);
         double e;
         double d;
         if (bl2) {
            d = vec3d.method_10216();
            e = playerEntity.method_23317() + packet.method_11734();
            playerEntity.field_6038 += packet.method_11734();
            playerEntity.field_6014 += packet.method_11734();
         } else {
            d = (double)0.0F;
            playerEntity.field_6038 = e = packet.method_11734();
            playerEntity.field_6014 = e;
         }

         double g;
         double f;
         if (bl22) {
            f = vec3d.method_10214();
            g = playerEntity.method_23318() + packet.method_11735();
            playerEntity.field_5971 += packet.method_11735();
            playerEntity.field_6036 += packet.method_11735();
         } else {
            f = (double)0.0F;
            playerEntity.field_5971 = g = packet.method_11735();
            playerEntity.field_6036 = g;
         }

         double i;
         double h;
         if (bl3) {
            h = vec3d.method_10215();
            i = playerEntity.method_23321() + packet.method_11738();
            playerEntity.field_5989 += packet.method_11738();
            playerEntity.field_5969 += packet.method_11738();
         } else {
            h = (double)0.0F;
            playerEntity.field_5989 = i = packet.method_11738();
            playerEntity.field_5969 = i;
         }

         playerEntity.method_5814(e, g, i);
         playerEntity.method_18800(d, f, h);
         if (AntiPacket.INSTANCE.applyYaw.getValue()) {
            float yaw = packet.method_11736();
            float pitch = packet.method_11739();
            if (packet.method_11733().contains(class_2709.field_12397)) {
               pitch += Astra.ROTATION.getLastPitch();
            }

            if (packet.method_11733().contains(class_2709.field_12401)) {
               yaw += Astra.ROTATION.getLastYaw();
            }

            this.field_45589.method_10743(new class_2793(packet.method_11737()));
            this.field_45589.method_10743(new class_2828.class_2830(playerEntity.method_23317(), playerEntity.method_23318(), playerEntity.method_23321(), yaw, pitch, false));
         } else {
            this.field_45589.method_10743(new class_2793(packet.method_11737()));
            this.field_45589.method_10743(new class_2828.class_2830(playerEntity.method_23317(), playerEntity.method_23318(), playerEntity.method_23321(), Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch(), false));
         }

         Astra.EVENT_BUS.post(ServerChangePositionEvent.INSTANCE);
      }

   }
}
