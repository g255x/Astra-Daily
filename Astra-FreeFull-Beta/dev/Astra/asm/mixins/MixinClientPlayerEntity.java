package dev.Astra.asm.mixins;

import com.mojang.authlib.GameProfile;
import dev.Astra.Astra;
import dev.Astra.api.events.Event;
import dev.Astra.api.events.impl.MoveEvent;
import dev.Astra.api.events.impl.MovedEvent;
import dev.Astra.api.events.impl.SendMovementPacketsEvent;
import dev.Astra.api.events.impl.TickEvent;
import dev.Astra.api.events.impl.TickMovementEvent;
import dev.Astra.asm.accessors.IClientPlayerEntity;
import dev.Astra.core.impl.CommandManager;
import dev.Astra.core.impl.RotationManager;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.exploit.PacketControl;
import dev.Astra.mod.modules.impl.movement.NoSlow;
import dev.Astra.mod.modules.impl.movement.Velocity;
import net.minecraft.class_1313;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_638;
import net.minecraft.class_742;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_746.class})
public abstract class MixinClientPlayerEntity extends class_742 {
   @Shadow
   public class_744 field_3913;
   @Final
   @Shadow
   protected class_310 field_3937;
   @Unique
   private float preYaw;
   @Unique
   private float prePitch;
   @Unique
   private boolean rotation = false;
   @Shadow
   private double field_3926;
   @Shadow
   private double field_3940;
   @Shadow
   private double field_3924;
   @Shadow
   private int field_3923;
   @Shadow
   private float field_3941;
   @Shadow
   private float field_3925;

   public MixinClientPlayerEntity(class_638 world, GameProfile profile) {
      super(world, profile);
   }

   @Inject(
      method = {"method_30673"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPushOutOfBlocksHook(double x, double d, CallbackInfo info) {
      if (Velocity.INSTANCE.isOn() && Velocity.INSTANCE.blockPush.getValue()) {
         info.cancel();
      }

   }

   @Redirect(
      method = {"method_6007"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_746;method_6115()Z"
),
      require = 0
   )
   private boolean tickMovementHook(class_746 player) {
      return NoSlow.INSTANCE.noSlow() ? false : player.method_6115();
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"method_60887"},
      cancellable = true
   )
   private void updateNausea(CallbackInfo ci) {
      if (ClientSetting.INSTANCE.portalGui()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_5784"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_742;method_5784(Lnet/minecraft/class_1313;Lnet/minecraft/class_243;)V"
)},
      cancellable = true
   )
   public void onMoveHook(class_1313 movementType, class_243 movement, CallbackInfo ci) {
      MoveEvent event = MoveEvent.get(movement.field_1352, movement.field_1351, movement.field_1350);
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         ci.cancel();
      } else if (event.modify) {
         ci.cancel();
         super.method_5784(movementType, new class_243(event.getX(), event.getY(), event.getZ()));
         Astra.EVENT_BUS.post(MovedEvent.INSTANCE);
      }

   }

   @Inject(
      method = {"method_5784"},
      at = {@At("TAIL")}
   )
   public void onMoveReturnHook(class_1313 movementType, class_243 movement, CallbackInfo ci) {
      Astra.EVENT_BUS.post(MovedEvent.INSTANCE);
   }

   @Shadow
   public abstract float method_5695(float var1);

   @Shadow
   public abstract float method_5705(float var1);

   @Inject(
      method = {"method_5773"},
      at = {@At("HEAD")}
   )
   private void tickHead(CallbackInfo ci) {
      try {
         Astra.EVENT_BUS.post(TickEvent.get(Event.Stage.Pre));
      } catch (Exception e) {
         e.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            CommandManager.sendMessage("§4An error has occurred (ClientPlayerEntity.tick() [HEAD]) Message: [" + e.getMessage() + "]");
         }
      }

   }

   @Inject(
      method = {"method_5773"},
      at = {@At("RETURN")}
   )
   private void tickReturn(CallbackInfo ci) {
      try {
         Astra.EVENT_BUS.post(TickEvent.get(Event.Stage.Post));
      } catch (Exception e) {
         e.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            CommandManager.sendMessage("§4An error has occurred (ClientPlayerEntity.tick() [RETURN]) Message: [" + e.getMessage() + "]");
         }
      }

   }

   @Inject(
      method = {"method_3136"},
      at = {@At("HEAD")}
   )
   private void onSendMovementPacketsHead(CallbackInfo info) {
      this.rotation();
      if (PacketControl.INSTANCE.isOn() && PacketControl.INSTANCE.positionSync.getValue() && this.field_3923 >= PacketControl.INSTANCE.positionDelay.getValueInt() - 1) {
         ((IClientPlayerEntity)this).setTicksSinceLastPositionPacketSent(50);
      }

      if (RotationManager.snapBack) {
         ((IClientPlayerEntity)this).setTicksSinceLastPositionPacketSent(50);
         ((IClientPlayerEntity)this).setLastYaw(999.0F);
         RotationManager.snapBack = false;
      } else {
         if (AntiCheat.INSTANCE.fullPackets.getValue()) {
            double d = this.method_23317() - this.field_3926;
            double e = this.method_23318() - this.field_3940;
            double f = this.method_23321() - this.field_3924;
            double g = (double)(this.method_36454() - this.field_3941);
            double h = (double)(this.method_36455() - this.field_3925);
            boolean bl3 = g != (double)0.0F || h != (double)0.0F;
            if (AntiCheat.INSTANCE.force.getValue() || !(class_3532.method_41190(d, e, f) > class_3532.method_33723(2.0E-4)) && this.field_3923 >= 19 || bl3) {
               ((IClientPlayerEntity)this).setTicksSinceLastPositionPacketSent(50);
               ((IClientPlayerEntity)this).setLastYaw(999.0F);
            }
         }

      }
   }

   @Inject(
      method = {"method_5773"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_634;method_52787(Lnet/minecraft/class_2596;)V",
   ordinal = 0
)}
   )
   private void onTickHasVehicleBeforeSendPackets(CallbackInfo info) {
      this.rotation();
   }

   @Unique
   private void rotation() {
      this.rotation = true;
      this.preYaw = this.method_36454();
      this.prePitch = this.method_36455();
      SendMovementPacketsEvent event = SendMovementPacketsEvent.get(this.method_36454(), this.method_36455());
      Astra.EVENT_BUS.post(event);
      Astra.ROTATION.rotationYaw = event.getYaw();
      Astra.ROTATION.rotationPitch = event.getPitch();
      this.method_36456(event.getYaw());
      this.method_36457(event.getPitch());
   }

   @Inject(
      method = {"method_3136"},
      at = {@At("TAIL")}
   )
   private void onSendMovementPacketsTail(CallbackInfo info) {
      if (this.rotation) {
         this.method_36456(this.preYaw);
         this.method_36457(this.prePitch);
         this.rotation = false;
      }

   }

   @Inject(
      method = {"method_5773"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_634;method_52787(Lnet/minecraft/class_2596;)V",
   ordinal = 1,
   shift = Shift.AFTER
)}
   )
   private void onTickHasVehicleAfterSendPackets(CallbackInfo info) {
      if (this.rotation) {
         this.method_36456(this.preYaw);
         this.method_36457(this.prePitch);
         this.rotation = false;
      }

   }

   @Inject(
      method = {"method_6007"},
      at = {@At("HEAD")}
   )
   private void tickMovement(CallbackInfo ci) {
      Astra.EVENT_BUS.post(TickMovementEvent.INSTANCE);
   }
}
