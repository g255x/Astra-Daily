package nekiplay.main.events.hud;

import nekiplay.main.events.Cancellable;
import net.minecraft.class_332;

public class RenderMountHealthBarEvent extends Cancellable {
   private static final RenderMountHealthBarEvent INSTANCE = new RenderMountHealthBarEvent();
   private class_332 context;

   public static RenderMountHealthBarEvent get(class_332 context) {
      INSTANCE.context = context;
      return INSTANCE;
   }
}
