package nekiplay.meteorplus.mixin.minecraft.hud;

import meteordevelopment.meteorclient.MeteorClient;
import nekiplay.main.events.hud.RenderArmorBarEvent;
import nekiplay.main.events.hud.RenderFoodBarEvent;
import nekiplay.main.events.hud.RenderHealthBarEvent;
import nekiplay.main.events.hud.RenderMountHealthBarEvent;
import net.minecraft.class_1657;
import net.minecraft.class_329;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_329.class})
public class InGameHudMixin {
   @Inject(
      method = {"method_37298"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderHealthBar(class_332 context, class_1657 player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
      RenderHealthBarEvent healthBarRenderEvent = RenderHealthBarEvent.get(context, player, x, y, lines, regeneratingHeartIndex, maxHealth, lastHealth, health, absorption, blinking);
      MeteorClient.EVENT_BUS.post(healthBarRenderEvent);
      if (healthBarRenderEvent.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_58477"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderFoodBar(class_332 context, class_1657 player, int top, int right, CallbackInfo ci) {
      RenderFoodBarEvent renderFoodBarEvent = RenderFoodBarEvent.get(context, player, top, right);
      MeteorClient.EVENT_BUS.post(renderFoodBarEvent);
      if (renderFoodBarEvent.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_58478"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onRenderArmor(class_332 context, class_1657 player, int i, int j, int k, int x, CallbackInfo ci) {
      RenderArmorBarEvent renderArmorBarEvent = RenderArmorBarEvent.get(context, player, i, j, k, x);
      MeteorClient.EVENT_BUS.post(renderArmorBarEvent);
      if (renderArmorBarEvent.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_1741"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderMountHealth(class_332 context, CallbackInfo ci) {
      RenderMountHealthBarEvent renderMountHealthBarEvent = RenderMountHealthBarEvent.get(context);
      MeteorClient.EVENT_BUS.post(renderMountHealthBarEvent);
      if (renderMountHealthBarEvent.isCancelled()) {
         ci.cancel();
      }

   }
}
