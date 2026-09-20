package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.PlaceBlockEvent;
import dev.Astra.mod.modules.Module;
import net.minecraft.class_1747;
import net.minecraft.class_1750;
import net.minecraft.class_2680;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1747.class})
public class MixinBlockItem {
   @Inject(
      method = {"method_7708(Lnet/minecraft/class_1750;Lnet/minecraft/class_2680;)Z"},
      at = {@At("RETURN")}
   )
   private void onPlace(@NotNull class_1750 context, class_2680 state, CallbackInfoReturnable<Boolean> info) {
      if (!Module.nullCheck()) {
         if (context.method_8045().field_9236) {
            Astra.EVENT_BUS.post(PlaceBlockEvent.get(context.method_8037(), state.method_26204()));
         }

      }
   }
}
