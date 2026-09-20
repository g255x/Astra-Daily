package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.asm.accessors.IPlayerMoveC2SPacket;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.BowBomb;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2828;

public class NoFall extends Module {
   private final EnumSetting<NoFallMode> mode;
   private final SliderSetting distance;

   public NoFall() {
      super("NoFall", "Prevents fall damage.", Module.Category.Player);
      this.mode = this.add(new EnumSetting("Mode", NoFall.NoFallMode.Packet));
      this.distance = this.add(new SliderSetting("Distance", (double)3.0F, (double)0.0F, (double)8.0F, 0.1));
      this.setChinese("没有摔落伤害");
   }

   public String getInfo() {
      return ((NoFallMode)this.mode.getValue()).name();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!nullCheck() && this.mode.is(NoFall.NoFallMode.Grim) && this.checkFalling()) {
         mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-9, mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), false));
         mc.field_1724.method_38785();
      }

   }

   private boolean checkFalling() {
      return mc.field_1724.field_6017 > (float)mc.field_1724.method_5850() && !mc.field_1724.method_24828() && !mc.field_1724.method_6128();
   }

   @EventListener
   public void onPacketSend(PacketEvent.Send event) {
      if (!nullCheck()) {
         for(class_1799 is : mc.field_1724.method_5661()) {
            if (is.method_7909() == class_1802.field_8833) {
               return;
            }
         }

         if (this.mode.is(NoFall.NoFallMode.Packet)) {
            class_2596 var5 = event.getPacket();
            if (var5 instanceof class_2828) {
               class_2828 packet = (class_2828)var5;
               if (mc.field_1724.field_6017 >= (float)this.distance.getValue() && !BowBomb.send) {
                  ((IPlayerMoveC2SPacket)packet).setOnGround(true);
               }
            }
         }
      }

   }

   public static enum NoFallMode {
      Packet,
      Grim;

      // $FF: synthetic method
      private static NoFallMode[] $values() {
         return new NoFallMode[]{Packet, Grim};
      }
   }
}
