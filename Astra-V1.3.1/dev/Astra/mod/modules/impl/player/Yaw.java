package dev.Astra.mod.modules.impl.player;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.LookDirectionEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;

public class Yaw extends Module {
   public static Yaw INSTANCE;
   private final BooleanSetting yawLock = this.add(new BooleanSetting("YawLock", true));
   private final BooleanSetting smart = this.add(new BooleanSetting("Smart", true));
   private final SliderSetting yaw = this.add(new SliderSetting("Yaw", (double)0.0F, (double)-180.0F, (double)180.0F, 0.1, () -> !this.smart.getValue()));
   private final BooleanSetting pitchLock = this.add(new BooleanSetting("PitchLock", true));
   private final SliderSetting pitch = this.add(new SliderSetting("Pitch", (double)0.0F, (double)-90.0F, (double)90.0F, 0.1));
   private final BooleanSetting lock = this.add(new BooleanSetting("Lock", true));

   public Yaw() {
      super("Yaw", Module.Category.Player);
      this.setChinese("视角锁定");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.yawLock.getValue()) {
         mc.field_1724.method_36456(this.smart.getValue() ? (float)Math.round((mc.field_1724.method_36454() + 1.0F) / 45.0F) * 45.0F : this.yaw.getValueFloat());
      }

      if (this.pitchLock.getValue()) {
         mc.field_1724.method_36457(this.pitch.getValueFloat());
      }

   }

   @EventListener
   public void onLookDirection(LookDirectionEvent event) {
      if (this.lock.getValue()) {
         event.cancel();
      }

   }
}
