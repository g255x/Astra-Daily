package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.KeyboardInputEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;

public class Fly extends Module {
   public static Fly INSTANCE;
   private final SliderSetting speedConfig = this.add(new SliderSetting("Speed", (double)2.5F, 0.1, (double)10.0F));
   private final SliderSetting vspeedConfig = this.add(new SliderSetting("VerticalSpeed", (double)1.0F, 0.1, (double)5.0F));
   private final BooleanSetting antiKickConfig = this.add((new BooleanSetting("AntiKick", true)).setParent());
   private final BooleanSetting up;
   private final BooleanSetting allowSneak;
   private final Timer antiKickTimer;
   private final Timer antiKick2Timer;

   public Fly() {
      super("Fly", Module.Category.Movement);
      BooleanSetting var10006 = this.antiKickConfig;
      Objects.requireNonNull(var10006);
      this.up = this.add(new BooleanSetting("Up", true, var10006::isOpen));
      this.allowSneak = this.add(new BooleanSetting("AllowSneak", false));
      this.antiKickTimer = new Timer();
      this.antiKick2Timer = new Timer();
      this.setChinese("飞行");
      INSTANCE = this;
   }

   public void onLogin() {
      this.antiKickTimer.reset();
      this.antiKick2Timer.reset();
   }

   public void onEnable() {
      if (!nullCheck()) {
         this.antiKickTimer.reset();
         this.antiKick2Timer.reset();
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.antiKickTimer.passed(3900L) && this.antiKickConfig.getValue() && !mc.field_1724.method_24828()) {
         MovementUtil.setMotionY(-0.04);
         this.antiKickTimer.reset();
      } else if (this.antiKick2Timer.passed(4000L) && this.antiKickConfig.getValue() && !mc.field_1724.method_24828() && this.up.getValue()) {
         MovementUtil.setMotionY(0.04);
         this.antiKick2Timer.reset();
      } else {
         MovementUtil.setMotionY((double)0.0F);
         if (mc.field_1690.field_1903.method_1434()) {
            MovementUtil.setMotionY(this.vspeedConfig.getValue());
         } else if (mc.field_1690.field_1832.method_1434()) {
            MovementUtil.setMotionY(-this.vspeedConfig.getValue());
         }
      }

      double[] move = MovementUtil.directionSpeed((double)this.speedConfig.getValueFloat());
      MovementUtil.setMotionX(move[0]);
      MovementUtil.setMotionZ(move[1]);
   }

   @EventListener(
      priority = -100
   )
   public void keyboard(KeyboardInputEvent event) {
      if (!this.allowSneak.getValue()) {
         mc.field_1724.field_3913.field_3903 = false;
      }

   }
}
