package dev.Astra.mod.modules.impl.player;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.asm.accessors.IPlayerMoveC2SPacket;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.BowBomb;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2828;

public class NoFall extends Module {
   private final EnumSetting<NoFallMode> mode;
   private final SliderSetting getDistance;

   public NoFall() {
      super("NoFall", Module.Category.Player);
      this.mode = this.add(new EnumSetting("Mode", NoFall.NoFallMode.Packet));
      this.getDistance = this.add(new SliderSetting("Distance", (double)3.0F, (double)0.0F, (double)8.0F, 0.1));
      this.setChinese("没有摔落伤害");
   }

   public String getInfo() {
      return ((NoFallMode)this.mode.getValue()).name();
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
            class_2596<?> packet = event.getPacket();
            if (packet instanceof class_2828) {
               class_2828 packet2 = (class_2828)packet;
               if (mc.field_1724.field_6017 >= (float)this.getDistance.getValue() && !BowBomb.send) {
                  ((IPlayerMoveC2SPacket)packet2).setOnGround(true);
               }
            }

         }
      }
   }

   public static enum NoFallMode {
      Packet;

      // $FF: synthetic method
      private static NoFallMode[] $values() {
         return new NoFallMode[]{Packet};
      }
   }
}
