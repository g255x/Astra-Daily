package nekiplay.meteorplus.features.modules.movement.noslow.modes;

import nekiplay.main.events.PlayerUseMultiplierEvent;
import nekiplay.meteorplus.features.modules.movement.noslow.NoSlowMode;
import nekiplay.meteorplus.features.modules.movement.noslow.NoSlowModes;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import net.minecraft.class_634;
import net.minecraft.class_2846.class_2847;

public class NCPStrict extends NoSlowMode {
   public NCPStrict() {
      super(NoSlowModes.NCP_Strict);
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

      if (this.mc.field_1724.method_6115()) {
         class_634 network = this.mc.method_1562();
         network.method_52787(new class_2846(class_2847.field_12971, this.mc.field_1724.method_24515(), class_2350.field_11033));
      }

   }
}
