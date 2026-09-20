package nekiplay.meteorplus.features.modules.combat.velocity;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.combat.velocity.modes.GrimCancel;
import nekiplay.meteorplus.features.modules.combat.velocity.modes.GrimCancel_v2;
import nekiplay.meteorplus.features.modules.combat.velocity.modes.GrimSkip;

public class VelocityPlus extends Module {
   private final SettingGroup settingsGroup;
   private final SettingGroup sgGeneral;
   private final Setting<VelocityModes> mode;
   private VelocityMode currentMode;

   public VelocityPlus() {
      super(Categories.Movement, "velocity+", "Bypass velocity.");
      this.settingsGroup = this.settings.getDefaultGroup();
      this.sgGeneral = this.settings.getDefaultGroup();
      this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("Velocity mode.")).defaultValue(VelocityModes.Grim_Cancel)).onModuleActivated((timerModesSetting) -> this.onTimerModeChanged((VelocityModes)timerModesSetting.get()))).onChanged(this::onTimerModeChanged)).build());
   }

   private void onTimerModeChanged(VelocityModes mode) {
      switch (mode) {
         case Grim_Cancel -> this.currentMode = new GrimCancel();
         case Grim_Cancel_v2 -> this.currentMode = new GrimCancel_v2();
         case Grim_Skip -> this.currentMode = new GrimSkip();
      }

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
   private void onRecivePacket(PacketEvent.Receive event) {
      this.currentMode.onReceivePacket(event);
   }
}
