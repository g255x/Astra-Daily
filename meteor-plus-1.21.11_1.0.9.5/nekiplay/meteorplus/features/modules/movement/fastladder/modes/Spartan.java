package nekiplay.meteorplus.features.modules.movement.fastladder.modes;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.PlayerMoveC2SPacketAccessor;
import nekiplay.meteorplus.features.modules.movement.fastladder.FastLadderMode;
import nekiplay.meteorplus.features.modules.movement.fastladder.FastLadderModes;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import net.minecraft.class_634;
import net.minecraft.class_746;

public class Spartan extends FastLadderMode {
   private int tick = 0;
   private boolean modify = false;
   private boolean start = false;
   private double startY = (double)0.0F;
   private double lastY = (double)0.0F;
   private double coff = 3.26E-11;
   private boolean block = false;

   public Spartan() {
      super(FastLadderModes.Spartan);
   }

   public void onActivate() {
      this.tick = 0;
      this.start = false;
      this.modify = false;

      assert this.mc.field_1724 != null;

      this.startY = this.mc.field_1724.method_73189().field_1351;
   }

   private boolean YGround(double height, double min, double max) {
      String yString = String.valueOf(height);
      yString = yString.substring(yString.indexOf("."));
      double y = Double.parseDouble(yString);
      return y >= min && y <= max;
   }

   private double RGround(double height) {
      String yString = String.valueOf(height);
      yString = yString.substring(yString.indexOf("."));
      return Double.parseDouble(yString);
   }

   public void onSendPacket(PacketEvent.Send event) {
      this.work(event.packet);
   }

   public void onSentPacket(PacketEvent.Sent event) {
      this.work(event.packet);
   }

   private void work(class_2596<?> packet) {
      if (this.modify) {
         if (packet instanceof class_2828) {
            class_2828 move = (class_2828)packet;

            assert this.mc.field_1724 != null;

            double y = this.mc.field_1724.method_23318();
            y = move.method_12268(y);
            if (this.YGround(y, this.RGround(this.startY) - 0.1, this.RGround(this.startY) + 0.1)) {
               ((PlayerMoveC2SPacketAccessor)packet).meteor$setOnGround(true);
            }

            if (this.mc.field_1724.method_24828() && this.block) {
               this.block = false;
               this.startY = this.mc.field_1724.method_73189().field_1351;
               this.start = false;
            }
         }
      } else {
         assert this.mc.field_1724 != null;

         if (this.mc.field_1724.method_24828() && this.block) {
            this.block = false;
            this.startY = this.mc.field_1724.method_73189().field_1351;
            this.start = false;
         }
      }

   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (this.modify) {
         class_746 player = this.mc.field_1724;
         double y = player.method_73189().field_1351;
         if (this.lastY == y && this.tick > 1) {
            this.block = true;
         } else {
            this.lastY = y;
         }
      }

   }

   public void onTickEventPost(TickEvent.Post event) {
      class_746 player = this.mc.field_1724;

      assert player != null;

      class_243 pl_velocity = player.method_18798();
      class_243 pos = player.method_73189();
      class_634 h = this.mc.method_1562();
      class_2680 state = this.mc.field_1687.method_8320(player.method_24515());
      class_2680 state2 = this.mc.field_1687.method_8320(player.method_24515().method_10069(0, 1, 0));
      if (this.mc.field_1724.method_6101()) {
         this.modify = player.field_5976 && player.method_21754();
         if (this.mc.field_1724.method_24828()) {
            this.block = false;
            this.startY = this.mc.field_1724.method_73189().field_1351;
            this.start = false;
         }

         if (player.field_5976) {
            if (!this.start) {
               this.start = true;
               this.startY = this.mc.field_1724.method_73189().field_1351;
               this.lastY = this.mc.field_1724.method_23318();
            }

            if (!this.block) {
               if (this.tick == 0) {
                  this.mc.field_1724.method_18800(pl_velocity.field_1352, 0.41999998688698, pl_velocity.field_1350);
                  this.tick = 1;
               } else if (this.tick == 1) {
                  this.mc.field_1724.method_18800(pl_velocity.field_1352, 0.33319999363698 - this.coff, pl_velocity.field_1350);
                  this.tick = 2;
               } else if (this.tick == 2) {
                  this.mc.field_1724.method_18800(pl_velocity.field_1352, 0.24813599862698 - this.coff, pl_velocity.field_1350);
                  this.tick = 0;
               }
            }
         } else {
            this.modify = false;
            this.tick = 0;
         }
      }

   }
}
