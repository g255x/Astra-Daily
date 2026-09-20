package nekiplay.meteorplus.features.modules.movement.nofall.modes;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.PlayerMoveC2SPacketAccessor;
import meteordevelopment.meteorclient.mixininterface.IPlayerMoveC2SPacket;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallMode;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallModes;
import nekiplay.meteorplus.utils.ElytraUtils;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2828;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class Eclip extends NoFallMode {
   private int ticks = 0;
   private int slot = -1;
   private int blocks = 0;
   private boolean cliped = false;
   private boolean groundcheck = false;
   private int timer = 0;
   private int teleports = 0;

   public Eclip() {
      super(NoFallModes.Elytra_Clip);
   }

   public void onTickEventPre(TickEvent.Pre event) {
      FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
      if (!elytra.found()) {
         ChatUtils.error("Elytra not found", new Object[0]);
         this.settings.toggle();
      } else {
         if (this.mc.field_1724.method_24828() && this.groundcheck) {
            this.groundcheck = false;
            this.cliped = false;
            ChatUtils.infoPrefix("No Fall Plus", "Grounded in " + this.teleports + " teleports", new Object[0]);
            this.mc.field_1724.field_6017 = (double)0.0F;
            this.teleports = 0;
         } else if (this.mc.field_1724.field_6017 > (double)3.0F) {
            class_3965 result = this.mc.field_1687.method_17742(new class_3959(this.mc.field_1724.method_73189(), this.mc.field_1724.method_73189().method_1023((double)0.0F, (double)10.0F, (double)0.0F), class_3960.field_17559, class_242.field_1348, this.mc.field_1724));
            if (result != null && result.method_17783() == class_240.field_1332) {
               this.blocks = result.method_17777().method_10069(0, 1, 0).method_10264();
               this.cliped = true;
            } else if (result == null || result.method_17783() == class_240.field_1333) {
               this.blocks = (int)this.mc.field_1724.method_73189().field_1351 - 10;
               this.cliped = true;
            }
         }

         if (this.cliped) {
            this.clip();
         }
      }

   }

   public void onSendPacket(PacketEvent.Send event) {
      if (this.groundcheck) {
         if (event.packet instanceof class_2828 && ((IPlayerMoveC2SPacket)event.packet).meteor$getTag() != 1337) {
            ((PlayerMoveC2SPacketAccessor)event.packet).meteor$setOnGround(true);
         }
      }
   }

   private void clip() {
      if (this.blocks != 0) {
         class_746 player = this.mc.field_1724;

         assert player != null;

         switch (this.ticks) {
            case 0:
               FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
               this.slot = elytra.slot();
               InvUtils.move().from(this.slot).toArmor(2);
               ++this.ticks;
            case 1:
               this.groundcheck = true;
               this.mc.field_1724.field_3944.method_52787(new class_2828.class_5911(false, this.mc.field_1724.field_5976));
               ++this.ticks;
            case 2:
               this.mc.field_1724.field_3944.method_52787(new class_2828.class_5911(false, this.mc.field_1724.field_5976));
               ++this.ticks;
            case 3:
               ElytraUtils.startFly();
               ++this.ticks;
            case 4:
               player.method_5814(player.method_23317(), (double)this.blocks, player.method_23321());
               this.mc.field_1724.field_3944.method_52787(new class_2828.class_2829(player.method_23317(), (double)this.blocks, player.method_23321(), true, this.mc.field_1724.field_5976));
               ++this.teleports;
               ++this.ticks;
            case 5:
               this.ticks = 0;
               InvUtils.move().fromArmor(2).to(this.slot);
         }
      }

   }
}
