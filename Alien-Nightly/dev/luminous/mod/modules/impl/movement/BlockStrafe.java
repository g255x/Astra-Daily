package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.AutoAnchor;
import dev.luminous.mod.modules.settings.impl.SliderSetting;

public class BlockStrafe extends Module {
   public static BlockStrafe INSTANCE;
   private final SliderSetting speed = this.add((new SliderSetting("Speed", (double)10.0F, (double)0.0F, (double)20.0F, (double)1.0F)).setSuffix("%"));
   private final SliderSetting aSpeed = this.add((new SliderSetting("AnchorSpeed", (double)3.0F, (double)0.0F, (double)20.0F, (double)1.0F)).setSuffix("%"));

   public BlockStrafe() {
      super("BlockStrafe", Module.Category.Movement);
      this.setChinese("方块灵活移动");
      INSTANCE = this;
   }

   @EventListener
   public void onMove(MoveEvent event) {
      if (EntityUtil.isInsideBlock()) {
         double speed = AutoAnchor.INSTANCE.currentPos == null ? this.speed.getValue() : this.aSpeed.getValue();
         double moveSpeed = 0.002873 * speed;
         double n = (double)mc.field_1724.field_3913.field_3905;
         double n2 = (double)mc.field_1724.field_3913.field_3907;
         double n3 = (double)mc.field_1724.method_36454();
         if (n == (double)0.0F && n2 == (double)0.0F) {
            event.setX((double)0.0F);
            event.setZ((double)0.0F);
         } else {
            if (n != (double)0.0F && n2 != (double)0.0F) {
               n *= Math.sin((Math.PI / 4D));
               n2 *= Math.cos((Math.PI / 4D));
            }

            event.setX(n * moveSpeed * -Math.sin(Math.toRadians(n3)) + n2 * moveSpeed * Math.cos(Math.toRadians(n3)));
            event.setZ(n * moveSpeed * Math.cos(Math.toRadians(n3)) - n2 * moveSpeed * -Math.sin(Math.toRadians(n3)));
         }
      }

   }
}
