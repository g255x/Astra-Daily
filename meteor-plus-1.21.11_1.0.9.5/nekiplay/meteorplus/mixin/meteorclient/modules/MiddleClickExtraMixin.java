package nekiplay.meteorplus.mixin.meteorclient.modules;

import meteordevelopment.meteorclient.events.meteor.MouseClickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.player.MiddleClickExtra;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {MiddleClickExtra.class},
   remap = false
)
public class MiddleClickExtraMixin extends Module {
   @Final
   @Shadow
   private final SettingGroup sgGeneral;
   @Unique
   private final Setting<Boolean> noInventory;

   public MiddleClickExtraMixin() {
      super(Categories.Player, "middle-click-extra", "Perform various actions when you middle click.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.noInventory = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Anti-inventory")).description("Not work in inventory.")).defaultValue(true)).build());
   }

   @Inject(
      method = {"onMouseClick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onMouseClick(MouseClickEvent event, CallbackInfo ci) {
      if (event.action == KeyAction.Press && event.button() == 2 && this.mc.field_1755 == null) {
         if (event.action != KeyAction.Press || event.button() != 2) {
            return;
         }

         if ((Boolean)this.noInventory.get() && this.mc.field_1755 != null) {
            ci.cancel();
         }
      }

   }
}
