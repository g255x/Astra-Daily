package nekiplay.meteorplus.features.modules.movement.speed.modes;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedMode;
import nekiplay.meteorplus.features.modules.movement.speed.SpeedModes;

public class NCPHop extends SpeedMode {
   public NCPHop() {
      super(SpeedModes.NCP_Hop);
   }

   public void onActivate() {
      ((Timer)Modules.get().get(Timer.class)).setOverride((double)1.0865F);
   }

   public void onDeactivate() {
      ((Timer)Modules.get().get(Timer.class)).setOverride((double)1.0F);
      this.mc.field_1724.method_31549().method_7248(0.02F);
   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (!this.mc.field_1724.method_5799() && !this.mc.field_1724.method_5771() && !this.mc.field_1724.method_6101() && !this.mc.field_1724.method_3144()) {
         Timer timer = (Timer)Modules.get().get(Timer.class);
         if (PlayerUtils.isMoving() && this.mc.field_1724.method_24828()) {
            this.mc.field_1724.method_6043();
            this.mc.field_1724.method_31549().method_7248(0.0223F);
         } else {
            timer.setOverride((double)1.0F);
         }

      }
   }
}
