package nekiplay.meteorplus.features.modules.movement.speed.modes;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedMode;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedModes;

public class AACHop438 extends SpeedMode {
   public AACHop438() {
      super(SpeedModes.AAC_Hop_4dot3dot8);
   }

   public void onDeactivate() {
      ((Timer)Modules.get().get(Timer.class)).setOverride((double)1.0F);
   }

   public void onTickEventPre(TickEvent.Pre event) {
      Timer timer = (Timer)Modules.get().get(Timer.class);
      timer.setOverride((double)1.0F);
      if (PlayerUtils.isMoving() && !this.mc.field_1724.method_5799() && !this.mc.field_1724.method_5771() && !this.mc.field_1724.method_6101() && !this.mc.field_1724.method_3144()) {
         if (this.mc.field_1724.method_24828()) {
            this.mc.field_1724.method_6043();
         } else if (this.mc.field_1724.field_6017 <= 0.1) {
            timer.setOverride((double)1.5F);
         } else if (this.mc.field_1724.field_6017 < 1.3) {
            timer.setOverride(0.7);
         } else {
            timer.setOverride((double)1.0F);
         }

      }
   }
}
