package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.path.BaritoneUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.EnumSetting;

public class AutoWalk extends Module {
   public static AutoWalk INSTANCE;
   private final EnumSetting<Mode> mode;
   boolean start;

   public AutoWalk() {
      super("AutoWalk", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", AutoWalk.Mode.Forward));
      this.start = false;
      this.setChinese("自动前进");
      INSTANCE = this;
   }

   public void onEnable() {
      this.start = false;
   }

   public void onLogout() {
      this.disable();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.mode.is(AutoWalk.Mode.Forward)) {
         mc.field_1690.field_1894.method_23481(true);
      } else if (this.mode.is(AutoWalk.Mode.Path)) {
         if (!this.start) {
            BaritoneUtil.forward();
            this.start = true;
         } else if (!BaritoneUtil.isActive()) {
            this.disable();
         }
      }

   }

   public void onDisable() {
      BaritoneUtil.cancelEverything();
   }

   public boolean forward() {
      return this.isOn() && this.mode.is(AutoWalk.Mode.Forward);
   }

   public static enum Mode {
      Forward,
      Path;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Forward, Path};
      }
   }
}
