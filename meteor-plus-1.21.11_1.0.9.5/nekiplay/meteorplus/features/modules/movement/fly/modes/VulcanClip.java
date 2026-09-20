package nekiplay.meteorplus.features.modules.movement.fly.modes;

import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import nekiplay.meteorplus.features.modules.movement.fly.FlyMode;
import nekiplay.meteorplus.features.modules.movement.fly.FlyModes;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import net.minecraft.class_2828;

public class VulcanClip extends FlyMode {
   private boolean waitFlag = false;
   private boolean canGlide = false;
   private int ticks = 0;
   private Timer timer;

   public VulcanClip() {
      super(FlyModes.Vulcan_Clip);
   }

   public void onDeactivate() {
      this.timer.setOverride((double)1.0F);
   }

   public void onActivate() {
      this.timer = (Timer)Modules.get().get(Timer.class);
      if (this.mc.field_1724.method_24828() && (Boolean)this.settings.canClip.get()) {
         this.clip(0.0F, -0.1F);
         this.waitFlag = true;
         this.canGlide = false;
         this.ticks = 0;
         this.timer.setOverride((double)0.1F);
      } else {
         this.waitFlag = false;
         this.canGlide = true;
      }

   }

   public void onPlayerMoveSendPre(SendMovementPacketsEvent.Pre event) {
      if (this.canGlide) {
         this.timer.setOverride((double)1.0F);
         class_243 velocity = this.mc.field_1724.method_18798();
         velocity.method_1031((double)0.0F, -(this.ticks % 2 == 0 ? 0.17 : 0.1), (double)0.0F);
         if (this.ticks == 0) {
            velocity.method_1031((double)0.0F, -0.07, (double)0.0F);
         }

         this.mc.field_1724.method_18799(velocity);
         ++this.ticks;
      }

   }

   public void onRecivePacket(PacketEvent.Receive event) {
      super.onRecivePacket(event);
      if (event.packet instanceof class_2708 && this.waitFlag) {
         class_2708 packet = (class_2708)event.packet;
         class_243 playerPos = this.mc.field_1724.method_73189();
         this.waitFlag = false;
         this.mc.field_1724.method_5814(packet.comp_3228().comp_3148().field_1352, packet.comp_3228().comp_3148().field_1351, packet.comp_3228().comp_3148().field_1350);
         this.mc.field_1724.field_3944.method_52787(new class_2828.class_2829(playerPos.field_1352, playerPos.field_1351, playerPos.field_1350, false, this.mc.field_1724.field_5976));
         event.cancel();
         this.mc.field_1724.method_6043();
         this.clip(0.127318F, 0.0F);
         this.clip(3.425559F, 3.7F);
         this.clip(3.14285F, 3.54F);
         this.clip(2.88522F, 3.4F);
         this.canGlide = true;
      }

   }

   private void clip(float dist, float y) {
      float tickDelta = this.mc.method_61966().method_60637(true);
      double yaw = Math.toRadians((double)this.mc.field_1724.method_5705(tickDelta));
      double x = -Math.sin(yaw) * (double)dist;
      double z = Math.cos(yaw) * (double)dist;
      this.mc.field_1724.method_5814(this.mc.field_1724.method_73189().field_1352 + x, this.mc.field_1724.method_73189().field_1351 + (double)y, this.mc.field_1724.method_73189().field_1350 + z);
      this.mc.field_1724.field_3944.method_52787(new class_2828.class_2829(this.mc.field_1724.method_23317(), this.mc.field_1724.method_23318(), this.mc.field_1724.method_23321(), false, this.mc.field_1724.field_5976));
   }
}
