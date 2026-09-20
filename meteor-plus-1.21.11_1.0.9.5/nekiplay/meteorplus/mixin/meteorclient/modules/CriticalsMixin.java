package nekiplay.meteorplus.mixin.meteorclient.modules;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixininterface.IPlayerInteractEntityC2SPacket;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.combat.Criticals;
import nekiplay.meteorplus.features.modules.combat.criticals.CriticalsPlus;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_2596;
import net.minecraft.class_2824.class_5907;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {Criticals.class},
   remap = false,
   priority = 1001
)
public class CriticalsMixin extends Module {
   @Unique
   private final SettingGroup sgMeteorPlus;
   @Unique
   private final Setting<Boolean> noWorkIfItsNotNeed;

   public CriticalsMixin(Category category, String name, String description) {
      super(category, name, description);
      this.sgMeteorPlus = this.settings.createGroup("Meteor+");
      this.noWorkIfItsNotNeed = this.sgMeteorPlus.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Use-crit-only-if-necessary")).description("Hits with a crit if the enemy's health is less than the normal damage of your weapon.")).defaultValue(true)).build());
   }

   @Inject(
      method = {"onSendPacket"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendPacket(PacketEvent.Send event, CallbackInfo ci) {
      class_2596 var4 = event.packet;
      if (var4 instanceof IPlayerInteractEntityC2SPacket packet) {
         if (packet.meteor$getType() == class_5907.field_29172) {
            if (CriticalsPlus.skipCrit()) {
               ci.cancel();
               return;
            }

            class_1297 entity = packet.meteor$getEntity();
            if (entity != null) {
               if (entity.method_5864() != class_1299.field_6100 && entity.method_5864() != class_1299.field_6066) {
                  if (entity instanceof class_1309) {
                     class_1309 livingEntity = (class_1309)entity;
                     if (!CriticalsPlus.needCrit(livingEntity) && (Boolean)this.noWorkIfItsNotNeed.get()) {
                        ci.cancel();
                     }
                  }
               } else {
                  ci.cancel();
               }
            }
         }
      }

   }
}
