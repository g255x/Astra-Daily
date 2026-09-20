package dev.Astra.asm.mixins;

import dev.Astra.api.utils.Wrapper;
import dev.Astra.mod.modules.impl.misc.AutoReconnect;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.class_2561;
import net.minecraft.class_412;
import net.minecraft.class_4185;
import net.minecraft.class_419;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_8667;
import net.minecraft.class_9112;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_419.class})
public abstract class MixinDisconnectedScreen extends class_437 {
   @Shadow
   @Final
   private class_8667 field_44552;
   @Unique
   private class_4185 reconnectBtn;
   @Unique
   private double time;

   protected MixinDisconnectedScreen(class_2561 title) {
      super(title);
      this.time = AutoReconnect.INSTANCE.delay.getValue() * (double)20.0F;
   }

   @Inject(
      method = {"method_25426"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_8667;method_48222()V",
   shift = Shift.BEFORE
)}
   )
   private void addButtons(CallbackInfo ci) {
      if (AutoReconnect.INSTANCE.lastServerConnection != null) {
         this.reconnectBtn = (new class_4185.class_7840(class_2561.method_43470(this.getText()), (button) -> this.tryConnecting())).method_46431();
         this.field_44552.method_52736(this.reconnectBtn);
      }

   }

   public void method_25393() {
      if (AutoReconnect.INSTANCE.rejoin() && AutoReconnect.INSTANCE.lastServerConnection != null) {
         if (this.time <= (double)0.0F) {
            this.tryConnecting();
         } else {
            --this.time;
            if (this.reconnectBtn != null) {
               this.reconnectBtn.method_25355(class_2561.method_43470(this.getText()));
            }
         }

      }
   }

   @Unique
   private String getText() {
      Object reconnectText = "Reconnect";
      if (AutoReconnect.INSTANCE.rejoin()) {
         reconnectText = (String)reconnectText + " " + String.format("(%.1f)", this.time / (double)20.0F);
      }

      return (String)reconnectText;
   }

   @Unique
   private void tryConnecting() {
      Pair<class_639, class_642> lastServer = AutoReconnect.INSTANCE.lastServerConnection;
      class_412.method_36877(new class_442(), Wrapper.mc, (class_639)lastServer.left(), (class_642)lastServer.right(), false, (class_9112)null);
   }
}
