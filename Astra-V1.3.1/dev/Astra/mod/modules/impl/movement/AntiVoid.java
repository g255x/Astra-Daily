package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_2246;

public class AntiVoid extends Module {
   private final SliderSetting voidHeight = this.add(new SliderSetting("VoidHeight", (double)-64.0F, (double)-64.0F, (double)319.0F, (double)1.0F));
   private final SliderSetting height = this.add(new SliderSetting("Height", (double)100.0F, (double)-40.0F, (double)256.0F, (double)1.0F));

   public AntiVoid() {
      super("AntiVoid", "Allows you to fly over void blocks", Module.Category.Movement);
      this.setChinese("反虚空");
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      boolean isVoid = true;

      for(int i = (int)mc.field_1724.method_23318(); i > this.voidHeight.getValueInt() - 1; --i) {
         if (!mc.field_1687.method_8320(new BlockPosX(mc.field_1724.method_23317(), (double)i, mc.field_1724.method_23321())).method_26215() && mc.field_1687.method_8320(new BlockPosX(mc.field_1724.method_23317(), (double)i, mc.field_1724.method_23321())).method_26204() != class_2246.field_10243) {
            isVoid = false;
            break;
         }
      }

      if (mc.field_1724.method_23318() < this.height.getValue() + this.voidHeight.getValue() && isVoid) {
         MovementUtil.setMotionY((double)0.0F);
      }

   }
}
