package dev.Astra.mod.modules.impl.player;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import net.minecraft.class_2886;
import net.minecraft.class_9334;
import net.minecraft.class_2846.class_2847;

public class PacketEat extends Module {
   public static PacketEat INSTANCE;
   private final BooleanSetting deSync = this.add(new BooleanSetting("DeSync", false));
   private final BooleanSetting noRelease = this.add(new BooleanSetting("NoRelease", true));

   public PacketEat() {
      super("PacketEat", Module.Category.Player);
      this.setChinese("发包进食");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.deSync.getValue() && mc.field_1724.method_6115() && mc.field_1724.method_6030().method_7909().method_57347().method_57832(class_9334.field_50075)) {
         Module.sendSequencedPacket((id) -> new class_2886(mc.field_1724.method_6058(), id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
      }

   }

   @EventListener
   public void onPacket(PacketEvent.Send event) {
      class_2846 packet;
      class_2596<?> packet2;
      if (this.noRelease.getValue() && (packet2 = event.getPacket()) instanceof class_2846 && (packet = (class_2846)packet2).method_12363() == class_2847.field_12974 && mc.field_1724.method_6030().method_7909().method_57347().method_57832(class_9334.field_50075)) {
         event.cancel();
      }

   }
}
