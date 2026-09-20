package nekiplay.main.events.hud;

import nekiplay.main.events.Cancellable;
import net.minecraft.class_332;
import net.minecraft.class_9779;

public class RenderExperienceLevelEvent extends Cancellable {
   private static final RenderExperienceLevelEvent INSTANCE = new RenderExperienceLevelEvent();
   private class_332 context;
   private class_9779 tickCounter;

   public static RenderExperienceLevelEvent get(class_332 context, class_9779 tickCounter) {
      INSTANCE.context = context;
      INSTANCE.tickCounter = tickCounter;
      return INSTANCE;
   }
}
