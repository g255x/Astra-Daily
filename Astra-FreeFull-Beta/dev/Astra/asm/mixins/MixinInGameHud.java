package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.PreRender2DEvent;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.render.Crosshair;
import dev.Astra.mod.modules.impl.render.NoRender;
import java.awt.Color;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_329.class})
public abstract class MixinInGameHud {
   @Final
   @Shadow
   private class_310 field_2035;
   @Unique
   final Color minColor = new Color(196, 0, 0);
   @Unique
   final Color maxColor = new Color(0, 227, 0);

   @Inject(
      method = {"method_1746"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderPortalOverlay(class_332 context, float nauseaStrength, CallbackInfo ci) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.portal.getValue()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_1765"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderStatusEffectOverlay(CallbackInfo info) {
      if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.potionsIcon.getValue()) {
         info.cancel();
      }

   }

   @Shadow
   private int method_1733(int t) {
      return 0;
   }

   @Shadow
   private int method_1744(class_1309 livingEntity) {
      return 0;
   }

   @Shadow
   private class_1657 method_1737() {
      return null;
   }

   @Shadow
   private class_1309 method_1734() {
      return null;
   }

   @Shadow
   public abstract void method_1753(class_332 var1, class_9779 var2);

   @Inject(
      at = {@At("HEAD")},
      method = {"method_1753"}
   )
   public void renderStart(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Astra.EVENT_BUS.post(PreRender2DEvent.get(context));
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"method_1753"}
   )
   public void renderHook(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      Astra.MODULE.onRender2D(context);
   }

   @Inject(
      method = {"method_1747"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_338;method_1808(Z)V"
)},
      cancellable = true
   )
   private void onClear(CallbackInfo info) {
      if (ClientSetting.INSTANCE.isOn() && ClientSetting.INSTANCE.keepHistory.getValue()) {
         info.cancel();
      }

   }

   @ModifyArg(
      method = {"method_1759"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_332;method_52706(Lnet/minecraft/class_2960;IIII)V",
   ordinal = 1
),
      index = 1
   )
   private int selectedSlotX(int x) {
      if (ClientSetting.INSTANCE.hotbar()) {
         double hotbarX = ClientSetting.animation.get((double)x, (long)ClientSetting.INSTANCE.hotbarTime.getValueInt(), ClientSetting.INSTANCE.animEase.getValue());
         return (int)hotbarX;
      } else {
         return x;
      }
   }

   @Inject(
      method = {"method_1736"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderCrosshairBegin(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      if (Crosshair.INSTANCE.isOn()) {
         Crosshair.INSTANCE.draw(context);
         ci.cancel();
      }

   }
}
