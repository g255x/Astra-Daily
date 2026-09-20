package nekiplay.meteorplus.features.modules.world.timer;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.world.timer.modes.NCP;
import nekiplay.meteorplus.features.modules.world.timer.modes.NCPv2;
import nekiplay.meteorplus.features.modules.world.timer.modes.Vulcan;

public class TimerPlus extends Module {
   public static int workingDelay = 27;
   public static int workingTimer = 0;
   public static int rechargeTimer = 0;
   public static int rechargeDelay = 352;
   public static double timerMultiplier = (double)2.0F;
   public static double timerMultiplierInAir = (double)1.5F;
   public static double timerMultiplierOnRecharge = (double)1.0F;
   private final SettingGroup settingsGroup;
   public final Setting<Boolean> onlyInMove;
   private final SettingGroup sgGeneral;
   private final Setting<TimerModes> mode;
   private final Setting<Boolean> rechargeOnDisable;
   private final Setting<Integer> rechargeDelaySetting;
   private final Setting<Integer> boostDelaySetting;
   private final Setting<Double> boostMultiplierInAir;
   private final Setting<Double> boostMultiplier;
   private final Setting<Double> boostMultiplierOnRecharge;
   private TimerMode currentMode;

   public TimerPlus() {
      super(Categories.World, "timer+", "Bypass timer.");
      this.settingsGroup = this.settings.getDefaultGroup();
      this.onlyInMove = this.settingsGroup.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("work-only-in-move")).description("Prevent false un charge.")).defaultValue(true)).build());
      this.sgGeneral = this.settings.getDefaultGroup();
      this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("Timer mode.")).defaultValue(TimerModes.NCP)).onModuleActivated((timerModesSetting) -> this.onTimerModeChanged((TimerModes)timerModesSetting.get()))).onChanged(this::onTimerModeChanged)).build());
      this.rechargeOnDisable = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("recharge-on-disable")).description("Recharge timer delay on disable.")).defaultValue(false)).build());
      this.rechargeDelaySetting = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("recharge-delay")).description("Recharge timer delay.")).defaultValue(352)).visible(() -> this.mode.get() == TimerModes.Custom || this.mode.get() == TimerModes.Custom_v2)).onChanged((a) -> {
         rechargeDelay = a;
         rechargeTimer = 0;
      })).build());
      this.boostDelaySetting = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("boost-delay")).description("Working timer delay.")).defaultValue(27)).visible(() -> this.mode.get() == TimerModes.Custom || this.mode.get() == TimerModes.Custom_v2)).onChanged((a) -> {
         workingDelay = a;
         workingTimer = 0;
      })).build());
      this.boostMultiplierInAir = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("multiplier-in-air")).description("Timer multiplier in air.")).defaultValue((double)2.0F).visible(() -> this.mode.get() == TimerModes.Custom_v2)).onChanged((a) -> timerMultiplierInAir = a)).build());
      this.boostMultiplier = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("multiplier")).description("Timer multiplier.")).defaultValue((double)2.0F).visible(() -> this.mode.get() == TimerModes.Custom || this.mode.get() == TimerModes.Custom_v2)).onChanged((a) -> timerMultiplier = a)).build());
      this.boostMultiplierOnRecharge = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("multiplier-on-recharge")).description("Timer multiplier on recharge.")).defaultValue((double)1.0F).visible(() -> this.mode.get() == TimerModes.Custom || this.mode.get() == TimerModes.Custom_v2)).onChanged((a) -> timerMultiplierOnRecharge = a)).build());
      this.autoSubscribe = false;
      MeteorClient.EVENT_BUS.subscribe(this);
   }

   private void onTimerModeChanged(TimerModes mode) {
      switch (mode) {
         case NCP:
            this.currentMode = new NCP();
            workingDelay = 27;
            rechargeDelay = 352;
            timerMultiplier = (double)2.0F;
            timerMultiplierOnRecharge = (double)1.0F;
            break;
         case Intave:
            this.currentMode = new NCP();
            workingDelay = 30;
            rechargeDelay = 105;
            timerMultiplier = (double)1.25F;
            timerMultiplierOnRecharge = (double)1.0F;
            break;
         case Vulcan:
            this.currentMode = new Vulcan();
            break;
         case Grim:
            this.currentMode = new NCP();
            workingDelay = 14;
            rechargeDelay = 16;
            timerMultiplier = (double)2.0F;
            timerMultiplierOnRecharge = (double)1.0F;
            break;
         case OldFag:
            this.currentMode = new NCPv2();
            workingDelay = 16;
            rechargeDelay = 155;
            timerMultiplier = (double)2.0F;
            timerMultiplierInAir = 1.7;
            timerMultiplierOnRecharge = (double)1.0F;
            break;
         case Custom:
            this.currentMode = new NCP();
            workingDelay = (Integer)this.boostDelaySetting.get();
            rechargeDelay = (Integer)this.rechargeDelaySetting.get();
            timerMultiplier = (Double)this.boostMultiplier.get();
            timerMultiplierOnRecharge = (Double)this.boostMultiplierOnRecharge.get();
            break;
         case Custom_v2:
            this.currentMode = new NCPv2();
            workingDelay = (Integer)this.boostDelaySetting.get();
            rechargeDelay = (Integer)this.rechargeDelaySetting.get();
            timerMultiplier = (Double)this.boostMultiplier.get();
            timerMultiplierInAir = (Double)this.boostMultiplierInAir.get();
            timerMultiplierOnRecharge = (Double)this.boostMultiplierOnRecharge.get();
      }

   }

   public void onActivate() {
      this.currentMode.onActivate();
   }

   public void onDeactivate() {
      if ((Boolean)this.rechargeOnDisable.get()) {
         workingTimer = 0;
         rechargeTimer = 0;
      }

      this.currentMode.onDeactivate();
   }

   @EventHandler
   private void onPreTick(TickEvent.Pre event) {
      this.currentMode.onTickEventPre(event);
   }

   @EventHandler
   private void onPostTick(TickEvent.Post event) {
      this.currentMode.onTickEventPost(event);
   }

   @EventHandler
   public void onSendPacket(PacketEvent.Send event) {
      this.currentMode.onSendPacket(event);
   }

   @EventHandler
   public void onSentPacket(PacketEvent.Sent event) {
      this.currentMode.onSentPacket(event);
   }

   public static double getPercentage() {
      return find_percent((double)0.0F, (double)rechargeDelay, (double)rechargeTimer);
   }

   public String getInfoString() {
      double percentage = find_percent((double)0.0F, (double)rechargeDelay, (double)rechargeTimer);
      return String.format("%.1f", percentage);
   }

   public static double find_percent(double start, double end, double val) {
      end -= start;
      val -= start;
      start = (double)0.0F;
      return ((double)1.0F - val / end) * (double)100.0F;
   }
}
