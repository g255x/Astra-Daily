package nekiplay.meteorplus.features.modules.movement.elytrafly;

import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.class_243;
import net.minecraft.class_310;

public class ElytraFlyMode {
   protected final class_310 mc = class_310.method_1551();
   protected final ElytraFlyPlus elytraFly = (ElytraFlyPlus)Modules.get().get(ElytraFlyPlus.class);
   private final ElytraFlyModes type;
   protected boolean lastJumpPressed;
   protected boolean incrementJumpTimer;
   protected boolean lastForwardPressed;
   protected int jumpTimer;
   protected double velX;
   protected double velY;
   protected double velZ;
   protected double ticksLeft;
   protected class_243 forward;
   protected class_243 right;
   protected double acceleration;

   public ElytraFlyMode(ElytraFlyModes type) {
      this.type = type;
   }

   public void onTick() {
   }

   public void onPreTick() {
   }

   public void onPacketSend(PacketEvent.Send event) {
   }

   public void onPacketReceive(PacketEvent.Receive event) {
   }

   public void onPlayerMove(PlayerMoveEvent event) {
   }

   public void onActivate() {
      this.lastJumpPressed = false;
      this.jumpTimer = 0;
      this.ticksLeft = (double)0.0F;
      this.acceleration = (double)0.0F;
   }

   public void onDeactivate() {
   }

   public String getHudString() {
      return this.type.name();
   }
}
