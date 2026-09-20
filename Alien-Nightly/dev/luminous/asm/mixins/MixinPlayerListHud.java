package dev.luminous.asm.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.impl.misc.ExtraTab;
import java.util.Comparator;
import java.util.List;
import net.minecraft.class_355;
import net.minecraft.class_640;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_355.class})
public abstract class MixinPlayerListHud {
   @Final
   @Shadow
   private static Comparator<class_640> field_2156;

   @Shadow
   protected abstract List<class_640> method_48213();

   @Inject(
      method = {"method_48213"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void collectPlayerEntriesHook(CallbackInfoReturnable<List<class_640>> cir) {
      if (ExtraTab.INSTANCE.isOn()) {
         cir.setReturnValue(Wrapper.mc.field_1724.field_3944.method_45732().stream().sorted(field_2156).limit((long)ExtraTab.INSTANCE.size.getValueInt()).toList());
      }

   }

   @Inject(
      method = {"method_1919"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/lang/Math;min(II)I",
   shift = Shift.BEFORE
)}
   )
   private void hookRender(CallbackInfo ci, @Local(ordinal = 5) LocalIntRef o, @Local(ordinal = 6) LocalIntRef p) {
      int totalPlayers = this.method_48213().size();
      int newP = 1;

      int newO;
      for(newO = (totalPlayers + newP - 1) / newP; newO > ExtraTab.INSTANCE.columns.getValueInt(); newO = (totalPlayers + newP - 1) / newP) {
         ++newP;
      }

      o.set(newO);
      p.set(newP);
   }
}
