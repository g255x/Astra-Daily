package nekiplay.meteorplus.mixin.minecraft;

import meteordevelopment.meteorclient.MeteorClient;
import nekiplay.main.events.ClickWindowEvent;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_636.class})
public class ClientPlayerInteractionManagerMixin {
   @Inject(
      method = {"method_2906"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void windowClick(int syncId, int slotId, int button, class_1713 actionType, class_1657 player, CallbackInfo callbackInfo) {
      ClickWindowEvent event = ClickWindowEvent.get(syncId, slotId, button, actionType);
      MeteorClient.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         callbackInfo.cancel();
      }

   }
}
