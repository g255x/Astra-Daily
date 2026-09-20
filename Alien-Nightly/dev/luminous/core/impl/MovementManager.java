package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventBus;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketSneakingEvent;
import dev.luminous.core.Manager;
import net.minecraft.class_2848;
import net.minecraft.class_2848.class_2849;

public class MovementManager extends Manager {
   private boolean packetSneaking;

   public MovementManager() {
      EventBus.INSTANCE.subscribe(this);
   }

   public void setMotionY(double y) {
      mc.field_1724.method_18800(mc.field_1724.method_18798().method_10216(), y, mc.field_1724.method_18798().method_10215());
   }

   public void setMotionXZ(double x, double z) {
      mc.field_1724.method_18800(x, mc.field_1724.method_18798().field_1351, z);
   }

   public void setMotionX(double x) {
      mc.field_1724.method_18800(x, mc.field_1724.method_18798().field_1351, mc.field_1724.method_18798().field_1350);
   }

   public void setMotionZ(double z) {
      mc.field_1724.method_18800(mc.field_1724.method_18798().field_1352, mc.field_1724.method_18798().field_1351, z);
   }

   public void setPacketSneaking(boolean packetSneaking) {
      this.packetSneaking = packetSneaking;
      if (packetSneaking) {
         Alien.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12979));
      } else {
         Alien.NETWORK.sendPacket(new class_2848(mc.field_1724, class_2849.field_12984));
      }

   }

   @EventListener
   public void onPacketSneak(PacketSneakingEvent event) {
      event.setCanceled(this.packetSneaking);
   }
}
