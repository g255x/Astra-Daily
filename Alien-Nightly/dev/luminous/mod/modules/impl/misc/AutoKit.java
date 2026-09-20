package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DeathEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.StringSetting;

public class AutoKit extends Module {
   public static AutoKit INSTANCE;
   final StringSetting command = this.add(new StringSetting("Command", "kit 1"));
   boolean kit = false;
   final Timer timer = new Timer();

   public AutoKit() {
      super("AutoKit", Module.Category.Misc);
      this.setChinese("自动配装命令");
      INSTANCE = this;
   }

   public void onLogin() {
      this.kit = true;
      this.timer.reset();
   }

   @EventListener
   public void onDeath(DeathEvent event) {
      if (event.getPlayer() == mc.field_1724) {
         this.kit = true;
         this.timer.reset();
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.kit && this.timer.passedS((double)2.0F)) {
         this.kit = false;
         mc.field_1724.field_3944.method_45731(this.command.getValue());
      }

   }
}
