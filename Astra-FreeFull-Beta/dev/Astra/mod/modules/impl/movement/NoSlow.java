package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.InteractItemEvent;
import dev.Astra.api.events.impl.KeyboardInputEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import java.util.Objects;
import net.minecraft.class_2868;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import net.minecraft.class_408;

public class NoSlow extends Module {
   public static NoSlow INSTANCE;
   private final EnumSetting<Mode> mode;
   private final BooleanSetting soulSand;
   private final BooleanSetting sneak;
   private final BooleanSetting climb;
   private final BooleanSetting gui;
   private final BooleanSetting allowSneak;
   boolean using;
   int delay;

   public NoSlow() {
      super("NoSlow", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", NoSlow.Mode.GrimV3));
      this.soulSand = this.add(new BooleanSetting("SoulSand", false));
      this.sneak = this.add(new BooleanSetting("Sneak", false));
      this.climb = this.add(new BooleanSetting("Climb", false));
      this.gui = this.add(new BooleanSetting("Gui", true));
      BooleanSetting var10006 = this.gui;
      Objects.requireNonNull(var10006);
      this.allowSneak = this.add(new BooleanSetting("AllowSneak", true, var10006::getValue));
      this.using = false;
      this.delay = 0;
      this.setChinese("无减速");
      INSTANCE = this;
   }

   private static float getMovementMultiplier(boolean positive, boolean negative) {
      if (positive == negative) {
         return 0.0F;
      } else {
         return positive ? 1.0F : -1.0F;
      }
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   private boolean shouldNoSlowGrimV3() {
      if (mc.field_1724 == null) {
         return false;
      } else {
         return !mc.field_1724.method_3144() && mc.field_1724.method_6048() > 1 && mc.field_1724.method_6048() % 2 != 0;
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.using = mc.field_1724.method_6115();
      --this.delay;
      if (this.using) {
         this.delay = 2;
      }

      if (this.using && !mc.field_1724.method_3144() && !mc.field_1724.method_6128() && this.mode.is(NoSlow.Mode.NCP)) {
         mc.method_1562().method_52787(new class_2868(mc.field_1724.method_31548().field_7545));
      }

      if (this.gui.getValue() && !(mc.field_1755 instanceof class_408)) {
         for(class_304 k : new class_304[]{mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849}) {
            k.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(k.method_1428()).method_1444()));
         }

         mc.field_1690.field_1903.method_23481(ElytraFly.INSTANCE.isOn() && ElytraFly.INSTANCE.mode.is(ElytraFly.Mode.Bounce) && ElytraFly.INSTANCE.autoJump.getValue() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1903.method_1428()).method_1444()));
         mc.field_1690.field_1894.method_23481(AutoWalk.INSTANCE.forward() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1894.method_1428()).method_1444()));
         mc.field_1690.field_1867.method_23481(Sprint.INSTANCE.isOn() && !Sprint.INSTANCE.inWater() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1867.method_1428()).method_1444()));
         if (this.allowSneak.getValue()) {
            mc.field_1690.field_1832.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1832.method_1428()).method_1444()));
         }
      }

   }

   @EventListener(
      priority = 100
   )
   public void keyboard(KeyboardInputEvent event) {
      if (this.sneak.getValue()) {
         event.cancel();
      }

      if (this.gui.getValue() && !(mc.field_1755 instanceof class_408)) {
         for(class_304 k : new class_304[]{mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849}) {
            k.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(k.method_1428()).method_1444()));
         }

         mc.field_1690.field_1903.method_23481(ElytraFly.INSTANCE.isOn() && ElytraFly.INSTANCE.mode.is(ElytraFly.Mode.Bounce) && ElytraFly.INSTANCE.autoJump.getValue() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1903.method_1428()).method_1444()));
         mc.field_1690.field_1894.method_23481(AutoWalk.INSTANCE.forward() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1894.method_1428()).method_1444()));
         mc.field_1690.field_1867.method_23481(Sprint.INSTANCE.isOn() && !Sprint.INSTANCE.inWater() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1867.method_1428()).method_1444()));
         if (this.allowSneak.getValue()) {
            mc.field_1690.field_1832.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1832.method_1428()).method_1444()));
         }

         mc.field_1724.field_3913.field_3910 = mc.field_1690.field_1894.method_1434();
         mc.field_1724.field_3913.field_3909 = mc.field_1690.field_1881.method_1434();
         mc.field_1724.field_3913.field_3908 = mc.field_1690.field_1913.method_1434();
         mc.field_1724.field_3913.field_3906 = mc.field_1690.field_1849.method_1434();
         mc.field_1724.field_3913.field_3905 = getMovementMultiplier(mc.field_1724.field_3913.field_3910, mc.field_1724.field_3913.field_3909);
         mc.field_1724.field_3913.field_3907 = getMovementMultiplier(mc.field_1724.field_3913.field_3908, mc.field_1724.field_3913.field_3906);
         mc.field_1724.field_3913.field_3904 = mc.field_1690.field_1903.method_1434();
         mc.field_1724.field_3913.field_3903 = mc.field_1690.field_1832.method_1434();
      }

   }

   @EventListener
   public void onUse(InteractItemEvent event) {
      if (event.isPre() && this.delay > 0) {
         mc.field_1752 = 0;
         event.cancel();
      }

   }

   public boolean noSlow() {
      if (!this.isOn()) {
         return false;
      } else {
         Mode currentMode = this.mode.getValue();
         if (currentMode == NoSlow.Mode.None) {
            return false;
         } else {
            return currentMode == NoSlow.Mode.GrimV3 ? this.shouldNoSlowGrimV3() : true;
         }
      }
   }

   public boolean soulSand() {
      return this.isOn() && this.soulSand.getValue();
   }

   public boolean climb() {
      return this.isOn() && this.climb.getValue();
   }

   public static enum Mode {
      Vanilla,
      NCP,
      GrimV3,
      None;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Vanilla, NCP, GrimV3, None};
      }
   }
}
