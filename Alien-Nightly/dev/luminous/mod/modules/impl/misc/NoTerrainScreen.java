package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.mod.modules.Module;
import net.minecraft.class_434;
import net.minecraft.class_435;

public class NoTerrainScreen extends Module {
   public NoTerrainScreen() {
      super("NoTerrainScreen", Module.Category.Misc);
      this.setChinese("没有加载界面");
   }

   @EventListener
   public void onEvent(ClientTickEvent event) {
      if (!nullCheck() && (mc.field_1755 instanceof class_434 || mc.field_1755 instanceof class_435)) {
         mc.field_1755 = null;
      }

   }
}
