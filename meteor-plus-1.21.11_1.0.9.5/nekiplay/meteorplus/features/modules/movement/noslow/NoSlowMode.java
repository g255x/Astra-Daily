package nekiplay.meteorplus.features.modules.movement.noslow;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import nekiplay.main.events.PlayerUseMultiplierEvent;
import net.minecraft.class_310;

public class NoSlowMode {
   protected final class_310 mc = class_310.method_1551();
   protected final NoSlowPlus settings = (NoSlowPlus)Modules.get().get(NoSlowPlus.class);
   private final NoSlowModes type;

   public NoSlowMode(NoSlowModes type) {
      this.type = type;
   }

   public void onUse(PlayerUseMultiplierEvent event) {
   }

   public void onTickEventPre(TickEvent.Pre event) {
   }

   public void onActivate() {
   }
}
