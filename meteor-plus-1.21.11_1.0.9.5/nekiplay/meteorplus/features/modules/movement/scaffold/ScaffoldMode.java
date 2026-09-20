package nekiplay.meteorplus.features.modules.movement.scaffold;

import meteordevelopment.meteorclient.systems.modules.Modules;
import nekiplay.main.events.PlayerUseMultiplierEvent;
import net.minecraft.class_310;

public class ScaffoldMode {
   protected final class_310 mc = class_310.method_1551();
   protected final ScaffoldPlus settings = (ScaffoldPlus)Modules.get().get(ScaffoldPlus.class);
   private final ScaffoldModes type;

   public ScaffoldMode(ScaffoldModes type) {
      this.type = type;
   }

   public void onUse(PlayerUseMultiplierEvent event) {
   }
}
