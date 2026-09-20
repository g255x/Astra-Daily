package nekiplay.meteorplus.features.modules.world.autoobsidianmine;

import meteordevelopment.meteorclient.events.world.CollisionShapeEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.class_2828;
import net.minecraft.class_310;

public class AutoObsidianFarmMode {
   protected final class_310 mc = class_310.method_1551();
   protected final AutoObsidianFarm settings = (AutoObsidianFarm)Modules.get().get(AutoObsidianFarm.class);
   private final AutoObsidianFarmModes type;

   public AutoObsidianFarmMode(AutoObsidianFarmModes type) {
      this.type = type;
   }

   public void onActivate() {
   }

   public void onDeactivate() {
   }

   public void onTickEventPre(TickEvent.Pre event) {
   }

   public void onTickEventPost(TickEvent.Post event) {
   }

   public void onCollisionShape(CollisionShapeEvent event) {
   }

   public void onMovePacket(class_2828 playerMove) {
   }

   public String getInfoString() {
      return "";
   }
}
