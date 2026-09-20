package nekiplay.meteorplus.features.modules.movement.noslow.modes;

import nekiplay.main.events.PlayerUseMultiplierEvent;
import nekiplay.meteorplus.features.modules.movement.noslow.NoSlowMode;
import nekiplay.meteorplus.features.modules.movement.noslow.NoSlowModes;
import net.minecraft.class_1268;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_634;

public class GrimNew extends NoSlowMode {
   public GrimNew() {
      super(NoSlowModes.Grim_New);
   }

   public void onUse(PlayerUseMultiplierEvent event) {
      if (this.mc.field_1724.method_5715()) {
         event.setForward(((Double)this.settings.sneakForward.get()).floatValue());
         event.setSideways(((Double)this.settings.sneakSideways.get()).floatValue());
      } else if (this.mc.field_1724.method_6115()) {
         event.setForward(((Double)this.settings.usingForward.get()).floatValue());
         event.setSideways(((Double)this.settings.usingSideways.get()).floatValue());
      } else {
         event.setForward(((Double)this.settings.otherForward.get()).floatValue());
         event.setSideways(((Double)this.settings.otherSideways.get()).floatValue());
      }

      class_1268 hand = this.mc.field_1724.method_6058();
      class_634 network = this.mc.method_1562();

      assert network != null;

      if (hand == class_1268.field_5808) {
         network.method_52787(new class_2886(class_1268.field_5810, 0, 0.0F, 0.0F));
      } else if (hand == class_1268.field_5810) {
         network.method_52787(new class_2868(this.mc.field_1724.method_31548().method_67532() % 8 + 1));
         network.method_52787(new class_2868(this.mc.field_1724.method_31548().method_67532()));
      }

   }
}
