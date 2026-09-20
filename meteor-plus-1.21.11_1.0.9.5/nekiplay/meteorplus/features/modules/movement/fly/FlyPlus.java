package nekiplay.meteorplus.features.modules.movement.fly;

import meteordevelopment.meteorclient.events.entity.player.CanWalkOnFluidEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.CollisionShapeEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.movement.fly.modes.MatrixExploit;
import nekiplay.meteorplus.features.modules.movement.fly.modes.MatrixExploit2;
import nekiplay.meteorplus.features.modules.movement.fly.modes.VulcanClip;

public class FlyPlus extends Module {
   private final SettingGroup sgGeneral;
   public final Setting<FlyModes> flyMode;
   public final Setting<Double> speed_1;
   public final Setting<Double> speed2;
   public final Setting<Boolean> canClip;
   public final Setting<Boolean> showInfo;
   private FlyMode currentMode;

   public FlyPlus() {
      super(Categories.Movement, "flight+", "Bypass fly");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.flyMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("The method of applying fly.")).defaultValue(FlyModes.Matrix_Exploit)).onModuleActivated((spiderModesSetting) -> this.onFlyModeChanged((FlyModes)spiderModesSetting.get()))).onChanged(this::onFlyModeChanged)).build());
      this.speed_1 = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("speed-№1")).description("Fly speed.")).defaultValue((double)1.25F).max((double)2500.0F).sliderRange((double)0.0F, (double)2500.0F).visible(() -> this.flyMode.get() == FlyModes.Matrix_Exploit)).build());
      this.speed2 = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("speed-№2")).description("Fly speed.")).defaultValue(0.3).max((double)5.0F).sliderRange((double)0.0F, (double)5.0F).visible(() -> this.flyMode.get() == FlyModes.Matrix_Exploit_2)).build());
      this.canClip = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("can-clip")).description("Max fly ticks.")).visible(() -> this.flyMode.get() == FlyModes.Vulcan_Clip)).build());
      this.showInfo = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("show-info")).description("Displays information about whether this mode is running on the server.")).visible(() -> this.flyMode.get() == FlyModes.Vulcan_Clip)).build());
      this.onFlyModeChanged((FlyModes)this.flyMode.get());
   }

   public WWidget getWidget(GuiTheme theme) {
      WWidget widget = super.getWidget(theme);
      return (WWidget)(this.flyMode.get() == FlyModes.Vulcan_Clip ? theme.label("This mode work only on 1.89 servers") : widget);
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
   public void onRecivePacket(PacketEvent.Receive event) {
      this.currentMode.onRecivePacket(event);
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

   @EventHandler
   private void onPlayerMoveSendPre(SendMovementPacketsEvent.Pre event) {
      this.currentMode.onPlayerMoveSendPre(event);
   }

   private void onFlyModeChanged(FlyModes mode) {
      switch (mode) {
         case Matrix_Exploit_2:
            this.currentMode = new MatrixExploit2();
            break;
         case Matrix_Exploit:
            this.currentMode = new MatrixExploit();
            break;
         case Vulcan_Clip:
            if ((Boolean)this.showInfo.get()) {
               this.info("Vulcan fly work on 1.8.9 servers", new Object[0]);
            }

            this.currentMode = new VulcanClip();
      }

   }
}
