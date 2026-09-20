package nekiplay.main.events.hud;

import nekiplay.main.events.Cancellable;
import net.minecraft.class_1657;
import net.minecraft.class_332;

public class RenderHealthBarEvent extends Cancellable {
   private static final RenderHealthBarEvent INSTANCE = new RenderHealthBarEvent();
   private class_332 context;
   private class_1657 player;
   private int x;
   private int y;
   private int lines;
   private int regeneratingHeartIndex;
   private float maxHealth;
   private int lastHealth;
   private int health;
   private int absorption;
   private boolean blinking;

   public static RenderHealthBarEvent get(class_332 context, class_1657 player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
      INSTANCE.context = context;
      INSTANCE.player = player;
      INSTANCE.x = x;
      INSTANCE.y = y;
      INSTANCE.lines = lines;
      INSTANCE.regeneratingHeartIndex = regeneratingHeartIndex;
      INSTANCE.maxHealth = maxHealth;
      INSTANCE.lastHealth = lastHealth;
      INSTANCE.health = health;
      INSTANCE.absorption = absorption;
      INSTANCE.blinking = blinking;
      return INSTANCE;
   }
}
