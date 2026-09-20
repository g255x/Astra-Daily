package nekiplay.meteorplus.features.modules.world.timer.modes;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import nekiplay.meteorplus.features.modules.world.timer.TimerMode;
import nekiplay.meteorplus.features.modules.world.timer.TimerModes;
import nekiplay.meteorplus.features.modules.world.timer.TimerPlus;
import nekiplay.meteorplus.utils.algoritms.RandomUtils;

public class Vulcan extends TimerMode {
   public Vulcan() {
      super(TimerModes.Vulcan);
   }

   public void onDeactivate() {
      this.timer.setOverride((double)1.0F);
   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (this.mc.field_1724 != null) {
         if (TimerPlus.rechargeTimer == 0) {
            if (TimerPlus.workingTimer > RandomUtils.nextInt(8, 10)) {
               int delay = RandomUtils.nextInt(510, 720);
               TimerPlus.rechargeTimer = delay;
               TimerPlus.rechargeDelay = delay;
               TimerPlus.workingTimer = 0;
               this.timer.setOverride((double)1.0F);
            } else if (this.settings.isActive()) {
               if ((Boolean)this.settings.onlyInMove.get() && PlayerUtils.isMoving()) {
                  ++TimerPlus.workingTimer;
                  this.timer.setOverride(1.35);
               } else if (!(Boolean)this.settings.onlyInMove.get()) {
                  ++TimerPlus.workingTimer;
                  this.timer.setOverride(1.35);
               } else {
                  this.timer.setOverride((double)1.0F);
               }
            }
         } else {
            --TimerPlus.rechargeTimer;
            if (this.settings.isActive()) {
               this.timer.setOverride((double)1.0F);
            } else {
               this.timer.setOverride((double)1.0F);
            }
         }

      }
   }
}
