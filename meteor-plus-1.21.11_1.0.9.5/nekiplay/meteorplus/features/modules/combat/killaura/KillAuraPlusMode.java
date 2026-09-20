package nekiplay.meteorplus.features.modules.combat.killaura;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.class_310;

public class KillAuraPlusMode {
   protected final class_310 mc = class_310.method_1551();
   protected final KillAuraPlus settings = (KillAuraPlus)Modules.get().get(KillAuraPlus.class);
   private final KillAuraPlusModes type;

   public KillAuraPlusMode(KillAuraPlusModes type) {
      this.type = type;
   }

   public void onActivate() {
   }

   public void onDeactivate() {
   }

   public void onTickPre(TickEvent.Pre event) {
   }

   public void onTickPost(TickEvent.Post event) {
   }

   public void onSendPacket(PacketEvent.Send event) {
   }

   public String getInfoString() {
      return "";
   }
}
