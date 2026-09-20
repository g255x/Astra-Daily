package nekiplay.meteorplus.mixin.minecraft.hud;

import java.util.List;
import meteordevelopment.meteorclient.MeteorClient;
import nekiplay.main.events.hud.DebugDrawTextEvent;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_340;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(
   value = {class_340.class},
   priority = 1001
)
public class DebugHudMixin {
   @Shadow
   @Final
   private class_310 field_2079;

   @Inject(
      method = {"method_51745"},
      at = {@At("HEAD")},
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void modifyDrawLeftText(class_332 context, List<String> text, boolean left, CallbackInfo ci) {
      DebugDrawTextEvent debugDrawTextEvent = DebugDrawTextEvent.get(text, left);
      MeteorClient.EVENT_BUS.post(debugDrawTextEvent);
   }
}
