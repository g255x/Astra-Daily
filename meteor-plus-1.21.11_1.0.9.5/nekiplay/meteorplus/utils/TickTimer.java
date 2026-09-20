package nekiplay.meteorplus.utils;

import meteordevelopment.meteorclient.MeteorClient;
import nekiplay.meteorplus.mixin.minecraft.ClientConnectionAccessor;
import net.minecraft.class_634;

public class TickTimer {
   private int ticksBegin = ticks();
   private double delay;

   public TickTimer(double delay) {
      this.delay = delay;
   }

   public void reset(double delay) {
      this.delay = delay;
      this.ticksBegin = ticks();
   }

   public boolean elapsed(double newDelay) {
      int currentTicks = ticks();
      if (currentTicks != -1 && this.ticksBegin != -1 && !((double)50.0F * (double)(currentTicks - this.ticksBegin) >= this.delay)) {
         return false;
      } else {
         this.reset(newDelay);
         return true;
      }
   }

   public boolean elapsed() {
      return this.elapsed(this.delay);
   }

   private static int ticks() {
      class_634 handler = MeteorClient.mc.method_1562();
      return handler == null ? -1 : ((ClientConnectionAccessor)handler.method_48296()).getTicks();
   }
}
