package dev.Astra.mod.modules.impl.movement;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.TickEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.path.BaritoneUtil;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.combat.SelfTrap;
import dev.Astra.mod.modules.impl.combat.Surround;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_2828;
import net.minecraft.class_5134;

public class Step extends Module {
   private final EnumSetting<Mode> mode;
   private final SliderSetting height;
   private final BooleanSetting useTimer;
   private final BooleanSetting fast;
   private final BooleanSetting onlyMoving;
   private final BooleanSetting surroundPause;
   private final BooleanSetting inWebPause;
   private final BooleanSetting inBlockPause;
   private final BooleanSetting sneakingPause;
   private final BooleanSetting pathingPause;
   boolean timer;
   int packets;

   public Step() {
      super("Step", "Steps up blocks.", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", Step.Mode.Vanilla));
      this.height = this.add(new SliderSetting("Height", (double)1.0F, (double)0.0F, (double)5.0F, (double)0.5F));
      this.useTimer = this.add(new BooleanSetting("Timer", true, () -> this.mode.getValue() == Step.Mode.OldNCP || this.mode.getValue() == Step.Mode.NCP));
      this.fast = this.add(new BooleanSetting("Fast", true, () -> this.mode.getValue() == Step.Mode.NCP && this.useTimer.getValue()));
      this.onlyMoving = this.add(new BooleanSetting("OnlyMoving", true));
      this.surroundPause = this.add(new BooleanSetting("SurroundPause", true));
      this.inWebPause = this.add(new BooleanSetting("InWebPause", true));
      this.inBlockPause = this.add(new BooleanSetting("InBlockPause", true));
      this.sneakingPause = this.add(new BooleanSetting("SneakingPause", true));
      this.pathingPause = this.add(new BooleanSetting("PathingPause", true));
      this.packets = 0;
      this.setChinese("步行辅助");
   }

   public static void setStepHeight(float v) {
      mc.field_1724.method_5996(class_5134.field_47761).method_6192((double)v);
   }

   public void onDisable() {
      if (!nullCheck()) {
         setStepHeight(0.6F);
         Astra.TIMER.reset();
      }
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if ((!this.pathingPause.getValue() || !BaritoneUtil.isActive()) && (!this.sneakingPause.getValue() || !mc.field_1724.method_5715()) && (!this.inBlockPause.getValue() || !EntityUtil.isInsideBlock()) && !mc.field_1724.method_5771() && !mc.field_1724.method_5799() && (!this.inWebPause.getValue() || !Astra.PLAYER.isInWeb(mc.field_1724)) && mc.field_1724.method_24828() && (!this.onlyMoving.getValue() || MovementUtil.isMoving()) && (!this.surroundPause.getValue() || !Surround.INSTANCE.isOn() && !SelfTrap.INSTANCE.isOn())) {
         setStepHeight(this.height.getValueFloat());
      } else {
         setStepHeight(0.6F);
      }
   }

   @EventListener
   public void onStep(TickEvent event) {
      if (event.isPost()) {
         --this.packets;
      } else {
         if (this.timer && this.packets <= 0) {
            Astra.TIMER.reset();
            this.timer = false;
         }

         boolean strict = this.mode.getValue() == Step.Mode.NCP;
         if (((Mode)this.mode.getValue()).equals(Step.Mode.OldNCP) || strict) {
            double stepHeight = mc.field_1724.method_23318() - mc.field_1724.field_6036;
            if (stepHeight <= (double)0.75F || stepHeight > this.height.getValue()) {
               return;
            }

            double[] offsets = this.getOffset(stepHeight);
            if (offsets != null && offsets.length > 1) {
               if (this.useTimer.getValue()) {
                  Astra.TIMER.set((float)this.getTimer(stepHeight));
                  this.timer = true;
                  this.packets = 2;
               }

               for(double offset : offsets) {
                  mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.field_6014, mc.field_1724.field_6036 + offset, mc.field_1724.field_5969, false));
               }
            }
         }

      }
   }

   public double getTimer(double height) {
      if (height > 0.6 && height <= (double)1.0F) {
         return !this.fast.getValue() && this.mode.getValue() == Step.Mode.NCP ? 0.3333333333333333 : (double)0.5F;
      } else {
         double[] offsets = this.getOffset(height);
         return offsets == null ? (double)1.0F : (double)1.0F / (double)offsets.length;
      }
   }

   public double[] getOffset(double height) {
      boolean strict = this.mode.getValue() == Step.Mode.NCP;
      if (height == (double)0.75F) {
         return strict ? new double[]{0.42, 0.753, (double)0.75F} : new double[]{0.42, 0.753};
      } else if (height == (double)0.8125F) {
         return strict ? new double[]{0.39, 0.7, (double)0.8125F} : new double[]{0.39, 0.7};
      } else if (height == (double)0.875F) {
         return strict ? new double[]{0.39, 0.7, (double)0.875F} : new double[]{0.39, 0.7};
      } else if (height == (double)1.0F) {
         return strict ? new double[]{0.42, 0.753, (double)1.0F} : new double[]{0.42, 0.753};
      } else if (height == (double)1.5F) {
         return new double[]{0.42, (double)0.75F, (double)1.0F, 1.16, 1.23, 1.2};
      } else if (height == (double)2.0F) {
         return new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
      } else {
         return height == (double)2.5F ? new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907} : null;
      }
   }

   public static enum Mode {
      Vanilla,
      OldNCP,
      NCP;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Vanilla, OldNCP, NCP};
      }
   }
}
