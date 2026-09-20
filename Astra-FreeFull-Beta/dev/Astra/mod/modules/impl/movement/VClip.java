package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import net.minecraft.class_2828;

public class VClip extends Module {
   private final EnumSetting<Mode> mode;

   public VClip() {
      super("VClip", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", VClip.Mode.Jump));
      this.setChinese("纵向穿墙");
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.disable();
      switch (((Mode)this.mode.getValue()).ordinal()) {
         case 0:
            double posX = mc.field_1724.method_23317();
            double posY = (double)Math.round(mc.field_1724.method_23318());
            double posZ = mc.field_1724.method_23321();
            boolean onGround = mc.field_1724.method_24828();
            mc.method_1562().method_52787(new class_2828.class_2829(posX, posY, posZ, onGround));
            double halfY = 0.005;
            double var11;
            mc.field_1724.method_5814(posX, var11 = posY - halfY, posZ);
            mc.method_1562().method_52787(new class_2828.class_2829(posX, var11, posZ, onGround));
            mc.field_1724.method_5814(posX, posY = var11 - halfY * (double)300.0F, posZ);
            mc.method_1562().method_52787(new class_2828.class_2829(posX, posY, posZ, onGround));
            break;
         case 1:
            mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)3.0F, mc.field_1724.method_23321());
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
            break;
         case 2:
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.4199999868869781, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.7531999805212017, mc.field_1724.method_23321(), false));
            mc.field_1724.method_5814(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)1.0F, mc.field_1724.method_23321());
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
      }

   }

   public static enum Mode {
      Glitch,
      Teleport,
      Jump;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Glitch, Teleport, Jump};
      }
   }
}
