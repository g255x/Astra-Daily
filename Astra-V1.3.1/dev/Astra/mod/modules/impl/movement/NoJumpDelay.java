package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.asm.accessors.ILivingEntity;
import dev.Astra.mod.modules.Module;

public class NoJumpDelay extends Module {
   public static NoJumpDelay INSTANCE;

   public NoJumpDelay() {
      super("NoJumpDelay", Module.Category.Movement);
      this.setChinese("无跳跃冷却");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      ((ILivingEntity)mc.field_1724).setLastJumpCooldown(0);
   }
}
