package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.KeyboardInputEvent;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.class_238;

public class SafeWalk extends Module {
   public static SafeWalk INSTANCE;
   private final BooleanSetting legit = this.add(new BooleanSetting("Legit", true));
   private final BooleanSetting onlyInsideBlock = this.add(new BooleanSetting("OnlyInsideBlock", false));

   public SafeWalk() {
      super("SafeWalk", Module.Category.Movement);
      this.setChinese("边缘行走");
      INSTANCE = this;
   }

   @EventListener(
      priority = -200
   )
   public void keyboard(KeyboardInputEvent event) {
      if (mc.field_1724.method_24828() && this.legit.getValue() && this.shouldSafeWalk()) {
         if (this.isOffsetBBEmpty(0.3, (double)-1.0F, (double)0.0F)) {
            mc.field_1724.field_3913.field_3903 = true;
         } else if (this.isOffsetBBEmpty((double)0.0F, (double)-1.0F, 0.3)) {
            mc.field_1724.field_3913.field_3903 = true;
         } else if (this.isOffsetBBEmpty(0.3, (double)-1.0F, 0.3)) {
            mc.field_1724.field_3913.field_3903 = true;
         } else if (this.isOffsetBBEmpty(-0.3, (double)-1.0F, (double)0.0F)) {
            mc.field_1724.field_3913.field_3903 = true;
         } else if (this.isOffsetBBEmpty((double)0.0F, (double)-1.0F, -0.3)) {
            mc.field_1724.field_3913.field_3903 = true;
         } else if (this.isOffsetBBEmpty(-0.3, (double)-1.0F, -0.3)) {
            mc.field_1724.field_3913.field_3903 = true;
         }
      }

   }

   @EventListener(
      priority = -100
   )
   public void onMove(MoveEvent event) {
      if (mc.field_1724.method_24828() && !this.legit.getValue() && this.shouldSafeWalk()) {
         double x = event.getX();
         double y = event.getY();
         double z = event.getZ();
         double increment = 0.05;

         while(x != (double)0.0F && this.isOffsetBBEmpty(x, (double)-1.0F, (double)0.0F)) {
            if (x < increment && x >= -increment) {
               x = (double)0.0F;
            } else if (x > (double)0.0F) {
               x -= increment;
            } else {
               x += increment;
            }
         }

         while(z != (double)0.0F && this.isOffsetBBEmpty((double)0.0F, (double)-1.0F, z)) {
            if (z < increment && z >= -increment) {
               z = (double)0.0F;
            } else if (z > (double)0.0F) {
               z -= increment;
            } else {
               z += increment;
            }
         }

         while(x != (double)0.0F && z != (double)0.0F && this.isOffsetBBEmpty(x, (double)-1.0F, z)) {
            x = x < increment && x >= -increment ? (double)0.0F : (x > (double)0.0F ? x - increment : x + increment);
            if (z < increment && z >= -increment) {
               z = (double)0.0F;
            } else if (z > (double)0.0F) {
               z -= increment;
            } else {
               z += increment;
            }
         }

         event.setX(x);
         event.setY(y);
         event.setZ(z);
      }

   }

   public boolean shouldSafeWalk() {
      return !this.onlyInsideBlock.getValue() || EntityUtil.isInsideBlock();
   }

   public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
      class_238 playerBox = mc.field_1724.method_5829();
      class_238 box = new class_238(playerBox.field_1323, playerBox.field_1322, playerBox.field_1324, playerBox.field_1320, playerBox.field_1322 + (double)0.5F, playerBox.field_1324);
      return !BlockUtil.canCollide(mc.field_1724, box.method_989(offsetX, offsetY, offsetZ));
   }
}
