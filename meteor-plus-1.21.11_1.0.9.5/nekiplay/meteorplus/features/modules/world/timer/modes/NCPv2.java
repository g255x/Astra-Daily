package nekiplay.meteorplus.features.modules.world.timer.modes;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import nekiplay.meteorplus.features.modules.world.timer.TimerMode;
import nekiplay.meteorplus.features.modules.world.timer.TimerModes;
import nekiplay.meteorplus.features.modules.world.timer.TimerPlus;

public class NCPv2 extends TimerMode {
   public NCPv2() {
      super(TimerModes.Custom_v2);
   }

   public void onDeactivate() {
      this.timer.setOverride((double)1.0F);
   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (this.mc.field_1724 != null) {
         if (TimerPlus.rechargeTimer == 0) {
            if (TimerPlus.workingTimer > TimerPlus.workingDelay) {
               TimerPlus.rechargeTimer = TimerPlus.rechargeDelay;
               TimerPlus.workingTimer = 0;
               this.timer.setOverride((double)1.0F);
            } else if (this.settings.isActive()) {
               if ((Boolean)this.settings.onlyInMove.get() && PlayerUtils.isMoving()) {
                  ++TimerPlus.workingTimer;
                  if (this.mc.field_1724.method_24828()) {
                     this.timer.setOverride(TimerPlus.timerMultiplier);
                  } else {
                     this.timer.setOverride(TimerPlus.timerMultiplierInAir);
                  }
               } else if (!(Boolean)this.settings.onlyInMove.get()) {
                  ++TimerPlus.workingTimer;
                  if (this.mc.field_1724.method_24828()) {
                     this.timer.setOverride(TimerPlus.timerMultiplier);
                  } else {
                     this.timer.setOverride(TimerPlus.timerMultiplierInAir);
                  }
               } else {
                  this.timer.setOverride(TimerPlus.timerMultiplierOnRecharge);
               }
            }
         } else {
            --TimerPlus.rechargeTimer;
            if (this.settings.isActive()) {
               this.timer.setOverride(TimerPlus.timerMultiplierOnRecharge);
            } else {
               this.timer.setOverride((double)1.0F);
            }
         }

      }
   }
}
