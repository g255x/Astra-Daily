package nekiplay.meteorplus.features.modules.movement.speed.modes.vulcan;

import java.util.Objects;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.SlotUtils;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedMode;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedModes;
import nekiplay.meteorplus.utils.CustomSpeedUtils;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1304;
import net.minecraft.class_1792;
import net.minecraft.class_1802;

public class Vulcan extends SpeedMode {
   public class_1792 chestPlate;

   public Vulcan() {
      super(SpeedModes.Vulcan);
   }

   public void onDeactivate() {
      FindItemResult chest = InvUtils.find(new class_1792[]{this.chestPlate});
      if (chest.found() && this.mc.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833 && (Boolean)this.settings.autoSwapVulcan.get()) {
         InvUtils.move().from(chest.slot()).toArmor(2);
      }

   }

   public void onActivate() {
      FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
      if (!elytra.found()) {
         this.settings.error("Elytra not found", new Object[0]);
         this.settings.toggle();
      } else if (!SlotUtils.isArmor(elytra.slot()) && (Boolean)this.settings.autoSwapVulcan.get() && this.mc.field_1724.method_6118(class_1304.field_6174).method_7909() != class_1802.field_8833) {
         this.chestPlate = this.mc.field_1724.method_6118(class_1304.field_6174).method_7909();
         InvUtils.move().from(elytra.slot()).toArmor(2);
      }

   }

   public void onPlayerMoveEvent(PlayerMoveEvent event) {
      if (this.mc.field_1724 != null && this.mc.field_1724.method_6118(class_1304.field_6174).method_7909() == class_1802.field_8833) {
         if (this.mc.field_1724.method_6059(class_1294.field_5904) && this.mc.field_1724.method_6112(class_1294.field_5904) != null) {
            if (((class_1293)Objects.requireNonNull(this.mc.field_1724.method_6112(class_1294.field_5904))).method_5578() == 1) {
               CustomSpeedUtils.applySpeed(event, (Double)this.settings.speedVulcanef2.get());
            } else if (((class_1293)Objects.requireNonNull(this.mc.field_1724.method_6112(class_1294.field_5904))).method_5578() == 0) {
               CustomSpeedUtils.applySpeed(event, (Double)this.settings.speedVulcanef1.get());
            }
         } else {
            CustomSpeedUtils.applySpeed(event, (Double)this.settings.speedVulcanef0.get());
         }
      }

   }
}
