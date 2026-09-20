package nekiplay.meteorplus.features.modules.movement.speed;

import meteordevelopment.meteorclient.events.entity.player.JumpVelocityMultiplierEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.class_1294;
import net.minecraft.class_310;

public class SpeedMode {
   protected final class_310 mc = class_310.method_1551();
   protected final SpeedPlus settings = (SpeedPlus)Modules.get().get(SpeedPlus.class);
   private final SpeedModes type;

   public SpeedMode(SpeedModes type) {
      this.type = type;
   }

   public void onReceivePacket(PacketEvent.Receive event) {
   }

   public void onSendPacket(PacketEvent.Send event) {
   }

   public void onSentPacket(PacketEvent.Sent event) {
   }

   public void onPlayerMoveEvent(PlayerMoveEvent event) {
   }

   public void onTickEventPre(TickEvent.Pre event) {
   }

   public void onTickEventPost(TickEvent.Post event) {
   }

   public void onJump(JumpVelocityMultiplierEvent event) {
   }

   public void onActivate() {
   }

   public void onDeactivate() {
   }

   protected double getDefaultSpeed() {
      double defaultSpeed = 0.2873;
      if (this.mc.field_1724.method_6059(class_1294.field_5904)) {
         int amplifier = this.mc.field_1724.method_6112(class_1294.field_5904).method_5578();
         defaultSpeed *= (double)1.0F + 0.2 * (double)(amplifier + 1);
      }

      if (this.mc.field_1724.method_6059(class_1294.field_5909)) {
         int amplifier = this.mc.field_1724.method_6112(class_1294.field_5909).method_5578();
         defaultSpeed /= (double)1.0F + 0.2 * (double)(amplifier + 1);
      }

      return defaultSpeed;
   }
}
