package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.interfaces.IMouseHook;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_312;
import net.minecraft.class_3675;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_312.class})
public class MixinMouse implements IMouseHook {
   @Shadow
   private boolean field_1783;
   @Final
   @Shadow
   private class_310 field_1779;
   @Shadow
   private double field_1795;
   @Shadow
   private double field_1794;

   @Inject(
      method = {"onMouseButton"},
      at = {@At("HEAD")}
   )
   private void onMouse(long window, int button, int action, int mods, CallbackInfo ci) {
      int key = -(button + 2);
      if (action == 1) {
         Astra.MODULE.onKeyPressed(key);
      }

      if (action == 0) {
         Astra.MODULE.onKeyReleased(key);
      }

   }

   public void frogClient$lock() {
      if (this.field_1779.method_1569() && !this.field_1783) {
         if (!class_310.field_1703) {
            class_304.method_1424();
         }

         this.field_1783 = true;
         this.field_1795 = (double)this.field_1779.method_22683().method_4480() / (double)2.0F;
         this.field_1794 = (double)this.field_1779.method_22683().method_4507() / (double)2.0F;
         class_3675.method_15984(this.field_1779.method_22683().method_4490(), 212995, this.field_1795, this.field_1794);
      }

   }
}
