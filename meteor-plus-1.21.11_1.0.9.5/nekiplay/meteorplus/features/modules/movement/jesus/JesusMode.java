package nekiplay.meteorplus.features.modules.movement.jesus;

import meteordevelopment.meteorclient.events.entity.player.CanWalkOnFluidEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.CollisionShapeEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.class_310;

public class JesusMode {
   protected final class_310 mc = class_310.method_1551();
   protected final JesusPlus settings = (JesusPlus)Modules.get().get(JesusPlus.class);
   private final JesusModes type;

   public JesusMode(JesusModes type) {
      this.type = type;
   }

   public void onSendPacket(PacketEvent.Send event) {
   }

   public void onSentPacket(PacketEvent.Sent event) {
   }

   public void onPlayerMoveEvent(PlayerMoveEvent event) {
   }

   public void onCanWalkOnFluid(CanWalkOnFluidEvent event) {
   }

   public void onCollisionShape(CollisionShapeEvent event) {
   }

   public void onTickEventPre(TickEvent.Pre event) {
   }

   public void onTickEventPost(TickEvent.Post event) {
   }

   public void onActivate() {
   }

   public void onDeactivate() {
   }
}
