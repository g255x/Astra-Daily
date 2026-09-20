package nekiplay.main.events.hud;

import nekiplay.main.events.Cancellable;
import net.minecraft.class_1657;
import net.minecraft.class_332;

public class RenderArmorBarEvent extends Cancellable {
   private static final RenderArmorBarEvent INSTANCE = new RenderArmorBarEvent();
   private class_332 context;
   private class_1657 player;
   private int i;
   private int j;
   private int k;
   private int x;

   public static RenderArmorBarEvent get(class_332 context, class_1657 player, int i, int j, int k, int x) {
      INSTANCE.context = context;
      INSTANCE.player = player;
      INSTANCE.i = i;
      INSTANCE.j = j;
      INSTANCE.k = k;
      INSTANCE.x = x;
      return INSTANCE;
   }
}
