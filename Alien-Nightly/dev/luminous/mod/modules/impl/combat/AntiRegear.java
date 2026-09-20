package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PlaceBlockEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2480;
import net.minecraft.class_2586;
import net.minecraft.class_2627;
import net.minecraft.class_3532;

public class AntiRegear extends Module {
   public static AntiRegear INSTANCE;
   public final List<class_2338> safe = new ArrayList();
   private final SliderSetting safeRange = this.add(new SliderSetting("SafeRange", (double)2.0F, (double)0.0F, (double)8.0F, 0.1));
   private final SliderSetting range = this.add(new SliderSetting("Range", (double)5.0F, (double)0.0F, (double)8.0F, 0.1));
   private final BooleanSetting checkSelf = this.add(new BooleanSetting("CheckSelf", true));

   public AntiRegear() {
      super("AntiRegear", Module.Category.Combat);
      this.setChinese("反补给");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (SpeedMine.getBreakPos() == null || !(mc.field_1687.method_8320(SpeedMine.getBreakPos()).method_26204() instanceof class_2480)) {
         this.safe.removeIf((pos) -> !(mc.field_1687.method_8320(pos).method_26204() instanceof class_2480));
         if (this.getBlock() != null) {
            SpeedMine.INSTANCE.mine(this.getBlock().method_11016());
         }
      }

   }

   @EventListener
   public void onPlace(PlaceBlockEvent event) {
      if (event.block instanceof class_2480) {
         this.safe.add(event.blockPos);
      }

   }

   private class_2627 getBlock() {
      for(class_2586 entity : BlockUtil.getTileEntities()) {
         if (entity instanceof class_2627 shulker) {
            if (!((double)class_3532.method_15355((float)mc.field_1724.method_5707(shulker.method_11016().method_46558())) <= this.safeRange.getValue()) && (!this.checkSelf.getValue() || !this.safe.contains(shulker.method_11016()) && (!shulker.method_11016().equals(AutoRegear.INSTANCE.placePos) || AutoRegear.INSTANCE.timeoutTimer.passed(100L))) && (double)class_3532.method_15355((float)mc.field_1724.method_5707(shulker.method_11016().method_46558())) <= this.range.getValue()) {
               return shulker;
            }
         }
      }

      return null;
   }
}
