package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.MoveEvent;
import dev.Astra.api.utils.path.BaritoneUtil;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import java.util.Objects;
import net.minecraft.class_1293;
import net.minecraft.class_1294;

public class Strafe extends Module {
   public static Strafe INSTANCE;
   private final BooleanSetting airStop = this.add(new BooleanSetting("AirStop", true));
   private final BooleanSetting slowCheck = this.add(new BooleanSetting("SlowCheck", true));

   public Strafe() {
      super("Strafe", Module.Category.Movement);
      this.setChinese("灵活移动");
      INSTANCE = this;
   }

   @EventListener
   public void onStrafe(MoveEvent event) {
      if (!BaritoneUtil.isActive()) {
         if (!mc.field_1724.method_5715() && !Fly.INSTANCE.isOn() && !HoleSnap.INSTANCE.isOn() && !Speed.INSTANCE.isOn() && !mc.field_1724.method_6128() && !EntityUtil.isInsideBlock() && !mc.field_1724.method_5771() && !mc.field_1724.method_5799() && !mc.field_1724.method_31549().field_7479) {
            if (!MovementUtil.isMoving()) {
               if (this.airStop.getValue()) {
                  MovementUtil.setMotionX((double)0.0F);
                  MovementUtil.setMotionZ((double)0.0F);
               }

            } else {
               double[] dir = MovementUtil.directionSpeed(this.getBaseMoveSpeed());
               event.setX(dir[0]);
               event.setZ(dir[1]);
            }
         }
      }
   }

   public double getBaseMoveSpeed() {
      double n = 0.2873;
      if (mc.field_1724.method_6059(class_1294.field_5904) && (!this.slowCheck.getValue() || !mc.field_1724.method_6059(class_1294.field_5909))) {
         n *= (double)1.0F + 0.2 * (double)(((class_1293)Objects.requireNonNull(mc.field_1724.method_6112(class_1294.field_5904))).method_5578() + 1);
      }

      return n;
   }
}
