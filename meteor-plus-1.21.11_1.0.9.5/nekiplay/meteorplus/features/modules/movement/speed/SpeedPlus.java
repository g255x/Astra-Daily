package nekiplay.meteorplus.features.modules.movement.speed;

import meteordevelopment.meteorclient.events.entity.player.JumpVelocityMultiplierEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.movement.speed.modes.AACHop438;
import nekiplay.meteorplus.features.modules.movement.speed.modes.NCPHop;
import nekiplay.meteorplus.features.modules.movement.speed.modes.matrix.Matrix;
import nekiplay.meteorplus.features.modules.movement.speed.modes.matrix.Matrix6_7_0;
import nekiplay.meteorplus.features.modules.movement.speed.modes.matrix.MatrixExploit;
import nekiplay.meteorplus.features.modules.movement.speed.modes.matrix.MatrixExploit2;
import nekiplay.meteorplus.features.modules.movement.speed.modes.vulcan.Vulcan;
import nekiplay.meteorplus.features.modules.movement.speed.modes.vulcan.Vulcan_2_8_6;

public class SpeedPlus extends Module {
   private final SettingGroup sgGeneral;
   public final Setting<SpeedModes> speedMode;
   public final Setting<Double> speedMatrix;
   public final Setting<Double> speedVulcanef2;
   public final Setting<Double> speedVulcanef1;
   public final Setting<Double> speedVulcanef0;
   public final Setting<Boolean> autoSwapVulcan;
   private SpeedMode currentMode;

   public SpeedPlus() {
      super(Categories.Movement, "speed+", "Bypass speed");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.speedMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("The method of applying speed.")).defaultValue(SpeedModes.Matrix_Exploit)).onModuleActivated((spiderModesSetting) -> this.onSpeedModeChanged((SpeedModes)spiderModesSetting.get()))).onChanged(this::onSpeedModeChanged)).build());
      this.speedMatrix = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("Speed")).description("Speed.")).defaultValue((double)4.0F).visible(() -> this.speedMode.get() == SpeedModes.Matrix_Exploit || this.speedMode.get() == SpeedModes.Matrix_Exploit_2)).build());
      this.speedVulcanef2 = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("Speed-effect-2")).description("Speed 2 effect.")).defaultValue((double)45.0F).max((double)75.0F).sliderRange((double)0.0F, (double)75.0F).visible(() -> this.speedMode.get() == SpeedModes.Vulcan)).build());
      this.speedVulcanef1 = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("Speed-effect-1")).description("Speed 1 effect.")).defaultValue((double)45.0F).max((double)75.0F).sliderRange((double)0.0F, (double)75.0F).visible(() -> this.speedMode.get() == SpeedModes.Vulcan)).build());
      this.speedVulcanef0 = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("Speed-effect-0")).description("Speed 0 effect.")).defaultValue((double)35.0F).max((double)75.0F).sliderRange((double)0.0F, (double)75.0F).visible(() -> this.speedMode.get() == SpeedModes.Vulcan)).build());
      this.autoSwapVulcan = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("auto-swap")).description("Auto swap.")).defaultValue(true)).visible(() -> this.speedMode.get() == SpeedModes.Vulcan)).build());
      this.onSpeedModeChanged((SpeedModes)this.speedMode.get());
   }

   public void onActivate() {
      this.currentMode.onActivate();
   }

   public void onDeactivate() {
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

   @EventHandler
   private void onPlayerMoveEvent(PlayerMoveEvent event) {
      this.currentMode.onPlayerMoveEvent(event);
   }

   @EventHandler
   public void onJump(JumpVelocityMultiplierEvent event) {
      this.currentMode.onJump(event);
   }

   private void onSpeedModeChanged(SpeedModes mode) {
      switch (mode) {
         case Matrix_Exploit_2 -> this.currentMode = new MatrixExploit2();
         case Matrix_Exploit -> this.currentMode = new MatrixExploit();
         case Matrix_6dot7dot0 -> this.currentMode = new Matrix6_7_0();
         case Matrix -> this.currentMode = new Matrix();
         case AAC_Hop_4dot3dot8 -> this.currentMode = new AACHop438();
         case Vulcan -> this.currentMode = new Vulcan();
         case Vulcan_2dot8dot6 -> this.currentMode = new Vulcan_2_8_6();
         case NCP_Hop -> this.currentMode = new NCPHop();
      }

   }
}
