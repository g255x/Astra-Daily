package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.impl.render.PlaceRender;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;
import net.minecraft.class_2846.class_2847;

public class Nuker extends Module {
   private final SliderSetting range = this.add(new SliderSetting("Range", (double)4.0F, (double)0.0F, (double)8.0F, 0.1));
   private final BooleanSetting down = this.add(new BooleanSetting("Down", false));
   private final BooleanSetting sand = this.add(new BooleanSetting("Sand", false));
   private final SliderSetting breaks;

   public Nuker() {
      super("Nuker", Module.Category.Misc);
      BooleanSetting var10008 = this.sand;
      Objects.requireNonNull(var10008);
      this.breaks = this.add(new SliderSetting("Breaks", 10, 0, 20, var10008::getValue));
      this.setChinese("范围挖掘");
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (SpeedMine.getBreakPos() == null || mc.field_1687.method_22347(SpeedMine.getBreakPos())) {
         if (this.sand.getValue()) {
            if (!mc.field_1724.method_24828()) {
               return;
            }

            int b = 0;

            for(class_2338 sand : BlockUtil.getSphere(this.range.getValueFloat(), mc.field_1724.method_33571())) {
               if (class_2246.field_10102 == mc.field_1687.method_8320(sand).method_26204() || class_2246.field_10534 == mc.field_1687.method_8320(sand).method_26204()) {
                  class_2350 side = BlockUtil.getClickSideStrict(sand);
                  if (side != null) {
                     PlaceRender.INSTANCE.create(sand);
                     Alien.ROTATION.snapAt(sand.method_46558());
                     sendSequencedPacket((id) -> new class_2846(class_2847.field_12973, sand, side, id));
                     sendSequencedPacket((id) -> new class_2846(class_2847.field_12968, sand, side, id));
                     sendSequencedPacket((id) -> new class_2846(class_2847.field_12973, sand, side, id));
                     Alien.ROTATION.snapBack();
                     ++b;
                     if ((double)b >= this.breaks.getValue()) {
                        return;
                     }
                  }
               }
            }
         } else {
            class_2338 pos = this.getBlock();
            if (pos != null) {
               SpeedMine.INSTANCE.mine(pos);
            }
         }
      }

   }

   private class_2338 getBlock() {
      class_2338 down = null;

      for(class_2338 pos : BlockUtil.getSphere(this.range.getValueFloat(), mc.field_1724.method_33571())) {
         if (!mc.field_1687.method_22347(pos) && !SpeedMine.unbreakable(pos) && BlockUtil.getClickSideStrict(pos) != null) {
            if (!((double)pos.method_10264() < mc.field_1724.method_23318())) {
               return pos;
            }

            if (down == null && this.down.getValue()) {
               down = pos;
            }
         }
      }

      return down;
   }
}
