package nekiplay.meteorplus.features.modules.movement.jesus;

import meteordevelopment.meteorclient.events.entity.player.CanWalkOnFluidEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.CollisionShapeEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.movement.jesus.modes.MatrixZoom;
import nekiplay.meteorplus.features.modules.movement.jesus.modes.MatrixZoom2;
import nekiplay.meteorplus.features.modules.movement.jesus.modes.NCP;
import nekiplay.meteorplus.features.modules.movement.jesus.modes.VulcanExploit;

public class JesusPlus extends Module {
   private final SettingGroup sgGeneral;
   public final Setting<JesusModes> jesusMode;
   public final Setting<Double> speed;
   public final Setting<Double> limit_speed;
   public final Setting<Boolean> autoSwapVulcan;
   private JesusMode currentMode;

   public JesusPlus() {
      super(Categories.Movement, "jesus+", "Bypass-jesus");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.jesusMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("The method of applying jesus.")).defaultValue(JesusModes.Matrix_Zoom)).onModuleActivated((spiderModesSetting) -> this.onJesusModeChanged((JesusModes)spiderModesSetting.get()))).onChanged(this::onJesusModeChanged)).build());
      this.speed = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("speed")).description("Jesus speed.")).defaultValue((double)1.25F).max((double)2500.0F).sliderRange((double)0.0F, (double)2500.0F).build());
      this.limit_speed = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("limit-speed")).description("Jesus speed.")).visible(() -> this.jesusMode.get() == JesusModes.NCP)).build());
      this.autoSwapVulcan = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("auto-swap")).description("Auto swap.")).defaultValue(true)).visible(() -> this.jesusMode.get() == JesusModes.Vulcan_Exploit)).build());
      this.onJesusModeChanged((JesusModes)this.jesusMode.get());
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
   public void onCanWalkOnFluid(CanWalkOnFluidEvent event) {
      this.currentMode.onCanWalkOnFluid(event);
   }

   @EventHandler
   public void onCollisionShape(CollisionShapeEvent event) {
      this.currentMode.onCollisionShape(event);
   }

   @EventHandler
   private void onPlayerMoveEvent(PlayerMoveEvent event) {
      this.currentMode.onPlayerMoveEvent(event);
   }

   private void onJesusModeChanged(JesusModes mode) {
      switch (mode) {
         case Matrix_Zoom -> this.currentMode = new MatrixZoom();
         case Matrix_Zoom_2 -> this.currentMode = new MatrixZoom2();
         case Vulcan_Exploit -> this.currentMode = new VulcanExploit();
         case NCP -> this.currentMode = new NCP();
      }

   }
}
