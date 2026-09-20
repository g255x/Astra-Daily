package nekiplay.main.events.hud;

import nekiplay.main.events.Cancellable;
import net.minecraft.class_1657;
import net.minecraft.class_332;

public class RenderFoodBarEvent extends Cancellable {
   private static final RenderFoodBarEvent INSTANCE = new RenderFoodBarEvent();
   private class_332 context;
   private class_1657 player;
   private int top;
   private int right;

   public static RenderFoodBarEvent get(class_332 context, class_1657 player, int top, int right) {
      INSTANCE.context = context;
      INSTANCE.player = player;
      INSTANCE.top = top;
      INSTANCE.right = right;
      return INSTANCE;
   }
}
