package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventHandler;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1294;

public class AntiSlowFall extends Module {
   public static AntiSlowFall INSTANCE;
   private final BooleanSetting bypassSlowFalling = this.add(new BooleanSetting("BypassSlowFalling", true));
   final SliderSetting y;
   final SliderSetting jumpDelay;
   Timer jumpTimer;

   public AntiSlowFall() {
      super("AntiSlowFall", Module.Category.Movement);
      BooleanSetting var10008 = this.bypassSlowFalling;
      Objects.requireNonNull(var10008);
      this.y = this.add(new SliderSetting("motion-y", 0.01, (double)0.0F, (double)1.0F, var10008::getValue));
      var10008 = this.bypassSlowFalling;
      Objects.requireNonNull(var10008);
      this.jumpDelay = this.add(new SliderSetting("jump-delay", 0, 0, 500, var10008::getValue));
      this.jumpTimer = new Timer();
      this.setChinese("反缓解");
      INSTANCE = this;
   }

   @EventHandler
   public void onUpdate() {
      if (!nullCheck()) {
         if (this.bypassSlowFalling.getValue()) {
            if (this.jumpTimer.passedMs(this.jumpDelay.getValue())) {
               if (mc.field_1724.method_6059(class_1294.field_5906)) {
                  if (!mc.field_1724.method_7325()) {
                     if (EntityUtil.isInsideBlock()) {
                        if (mc.field_1724.method_24828() && mc.field_1724.field_6017 <= 0.0F) {
                           MovementUtil.setMotionY(this.y.getValue());
                           this.jumpTimer.reset();
                        }

                     }
                  }
               }
            }
         }
      }
   }
}
