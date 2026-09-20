package nekiplay.main.events.hud;

import nekiplay.main.events.Cancellable;
import net.minecraft.class_332;

public class RenderExperienceBarEvent extends Cancellable {
   private static final RenderExperienceBarEvent INSTANCE = new RenderExperienceBarEvent();
   private class_332 context;
   private int x;

   public static RenderExperienceBarEvent get(class_332 context, int x) {
      INSTANCE.context = context;
      INSTANCE.x = x;
      return INSTANCE;
   }
}
