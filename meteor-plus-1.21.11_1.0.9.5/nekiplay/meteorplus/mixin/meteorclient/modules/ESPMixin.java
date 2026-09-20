package nekiplay.meteorplus.mixin.meteorclient.modules;

import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.ESP;
import nekiplay.meteorplus.features.modules.combat.AntiBotPlus;
import nekiplay.meteorplus.features.modules.combat.Teams;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ESP.class})
public class ESPMixin {
   @Inject(
      method = {"shouldSkip"},
      at = {@At("RETURN")},
      cancellable = true
   )
   protected void shouldSkip(class_1297 entity, CallbackInfoReturnable<Boolean> cir) {
      AntiBotPlus antiBotPlus = (AntiBotPlus)Modules.get().get(AntiBotPlus.class);
      Teams teams = (Teams)Modules.get().get(Teams.class);
      if (antiBotPlus != null && teams != null && !(Boolean)cir.getReturnValue() && entity instanceof class_1657) {
         boolean ignore = antiBotPlus.isBot(entity);
         if (!ignore) {
            ignore = teams.isInYourTeam(entity);
         }

         cir.setReturnValue(ignore);
      }

   }
}
