package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.combat.SelfTrap;
import dev.Astra.mod.modules.impl.player.InteractTweaks;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1937.class})
public abstract class MixinWorld {
   @Inject(
      method = {"method_8320"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void blockStateHook(class_2338 pos, CallbackInfoReturnable<class_2680> cir) {
      if (Wrapper.mc.field_1687 != null && Wrapper.mc.field_1687.method_24794(pos)) {
         if (SelfTrap.airList.contains(pos)) {
            cir.setReturnValue(class_2246.field_10124.method_9564());
            return;
         }

         if (!ClientSetting.INSTANCE.mioCompatible.getValue()) {
            boolean terrainIgnore = CombatUtil.terrainIgnore;
            class_2338 modifyPos = CombatUtil.modifyPos;
            class_2680 modifyBlockState = CombatUtil.modifyBlockState;
            if (!terrainIgnore && modifyPos == null) {
               if (InteractTweaks.INSTANCE.isActive && Wrapper.mc.field_1687.method_8497(pos.method_10263() >> 4, pos.method_10260() >> 4).method_8320(pos).method_26204() == class_2246.field_9987) {
                  cir.setReturnValue(class_2246.field_10124.method_9564());
               }
            } else {
               class_2818 worldChunk2 = Wrapper.mc.field_1687.method_8497(pos.method_10263() >> 4, pos.method_10260() >> 4);
               class_2680 tempState2 = worldChunk2.method_8320(pos);
               if (modifyPos != null && modifyBlockState != null && pos.equals(modifyPos)) {
                  cir.setReturnValue(modifyBlockState);
                  return;
               }

               if (terrainIgnore) {
                  if (Astra.HOLE.isHard(tempState2.method_26204())) {
                     return;
                  }

                  cir.setReturnValue(class_2246.field_10124.method_9564());
               }
            }
         }
      }

   }
}
