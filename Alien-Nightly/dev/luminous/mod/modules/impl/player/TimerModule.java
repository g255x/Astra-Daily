package dev.luminous.mod.modules.impl.player;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.FadeUtils;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Objects;
import net.minecraft.class_2708;
import net.minecraft.class_332;

public class TimerModule extends Module {
   public static TimerModule INSTANCE;
   public final SliderSetting multiplier = this.add(new SliderSetting("Speed", (double)1.0F, 0.1, (double)5.0F, 0.01));
   public final BindSetting boostKey = this.add(new BindSetting("HoldKey", -1));
   public final SliderSetting boost = this.add(new SliderSetting("OnKey", (double)1.0F, 0.1, (double)10.0F, 0.01));
   final DecimalFormat df = new DecimalFormat("0.0");
   private final BooleanSetting tickShift = this.add((new BooleanSetting("TickShift", true)).setParent());
   private final SliderSetting shiftTimer;
   private final SliderSetting accumulate;
   private final SliderSetting minAccumulate;
   private final BooleanSetting smooth;
   private final EnumSetting<Easing> ease;
   private final BooleanSetting reset;
   private final BooleanSetting indicator;
   private final ColorSetting work;
   private final ColorSetting charging;
   private final SliderSetting yOffset;
   private final Timer timer;
   private final Timer timer2;
   private final FadeUtils end;
   long lastMs;
   boolean moving;

   public TimerModule() {
      super("Timer", Module.Category.Player);
      BooleanSetting var10009 = this.tickShift;
      Objects.requireNonNull(var10009);
      this.shiftTimer = this.add(new SliderSetting("ShiftTimer", (double)2.0F, (double)1.0F, (double)10.0F, 0.1, var10009::isOpen));
      var10009 = this.tickShift;
      Objects.requireNonNull(var10009);
      this.accumulate = this.add((new SliderSetting("Charge", (double)2000.0F, (double)1.0F, (double)10000.0F, (double)50.0F, var10009::isOpen)).setSuffix("ms"));
      var10009 = this.tickShift;
      Objects.requireNonNull(var10009);
      this.minAccumulate = this.add((new SliderSetting("MinCharge", (double)500.0F, (double)1.0F, (double)10000.0F, (double)50.0F, var10009::isOpen)).setSuffix("ms"));
      BooleanSetting var10006 = this.tickShift;
      Objects.requireNonNull(var10006);
      this.smooth = this.add((new BooleanSetting("Smooth", true, var10006::isOpen)).setParent());
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut, () -> this.smooth.isOpen() && this.tickShift.isOpen()));
      var10006 = this.tickShift;
      Objects.requireNonNull(var10006);
      this.reset = this.add(new BooleanSetting("Reset", true, var10006::isOpen));
      var10006 = this.tickShift;
      Objects.requireNonNull(var10006);
      this.indicator = this.add((new BooleanSetting("Indicator", true, var10006::isOpen)).setParent());
      this.work = this.add(new ColorSetting("Completed", new Color(0, 255, 0), () -> this.indicator.isOpen() && this.tickShift.isOpen()));
      this.charging = this.add(new ColorSetting("Charging", new Color(255, 0, 0), () -> this.indicator.isOpen() && this.tickShift.isOpen()));
      this.yOffset = this.add(new SliderSetting("YOffset", (double)0.0F, (double)-200.0F, (double)200.0F, (double)1.0F, () -> this.indicator.isOpen() && this.tickShift.isOpen()));
      this.timer = new Timer();
      this.timer2 = new Timer();
      this.end = new FadeUtils(500L);
      this.lastMs = 0L;
      this.moving = false;
      this.setChinese("时间加速");
      INSTANCE = this;
   }

   public void onDisable() {
      Alien.TIMER.reset();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      Alien.TIMER.tryReset();
   }

   public boolean onEnable() {
      Alien.TIMER.reset();
      return false;
   }

   public void onRender2D(class_332 drawContext, float tickDelta) {
      if (this.tickShift.getValue()) {
         this.timer.setMs(Math.min(Math.max(0L, this.timer.getMs()), (long)this.accumulate.getValueInt()));
         if (MovementUtil.isMoving() && !EntityUtil.isInsideBlock()) {
            if (!this.moving) {
               if (this.timer.passedMs(this.minAccumulate.getValue())) {
                  this.timer2.reset();
                  this.lastMs = this.timer.getMs();
               } else {
                  this.lastMs = 0L;
               }

               this.moving = true;
            }

            this.timer.reset();
            if (this.timer2.passed(this.lastMs)) {
               Alien.TIMER.reset();
            } else if (this.smooth.getValue()) {
               double timer = (double)Alien.TIMER.getDefault() + ((double)1.0F - this.end.ease(this.ease.getValue())) * (double)(this.shiftTimer.getValueFloat() - 1.0F) * ((double)this.lastMs / this.accumulate.getValue());
               Alien.TIMER.set((float)Math.max((double)Alien.TIMER.getDefault(), timer));
            } else {
               Alien.TIMER.set(this.shiftTimer.getValueFloat());
            }
         } else {
            if (this.moving) {
               Alien.TIMER.reset();
               if (this.reset.getValue()) {
                  this.timer.reset();
               } else {
                  this.timer.setMs(Math.max(this.lastMs - this.timer2.getMs(), 0L));
               }

               this.moving = false;
            }

            this.end.setLength(this.timer.getMs());
            this.end.reset();
         }

         if (this.indicator.getValue()) {
            double current = this.moving ? (double)Math.max(this.lastMs - this.timer2.getMs(), 0L) : (double)this.timer.getMs();
            boolean completed = this.moving && current > (double)0.0F || current >= (double)this.minAccumulate.getValueInt();
            double max = this.accumulate.getValue();
            String var10000 = this.df.format(current / max * (double)100.0F);
            String text = var10000 + "%";
            drawContext.method_51433(mc.field_1772, text, mc.method_22683().method_4486() / 2 - mc.field_1772.method_1727(text) / 2, mc.method_22683().method_4502() / 2 + 9 - this.yOffset.getValueInt(), completed ? this.work.getValue().getRGB() : this.charging.getValue().getRGB(), true);
         }
      }

   }

   public String getInfo() {
      if (!this.tickShift.getValue()) {
         return null;
      } else {
         double current = this.moving ? (double)Math.max(this.lastMs - this.timer2.getMs(), 0L) : (double)this.timer.getMs();
         double max = this.accumulate.getValue();
         double value = Math.min(current / max * (double)100.0F, (double)100.0F);
         String var10000 = this.df.format(value);
         return var10000 + "%";
      }
   }

   @EventListener
   public void onReceivePacket(PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2708) {
         this.lastMs = 0L;
      }

   }
}
