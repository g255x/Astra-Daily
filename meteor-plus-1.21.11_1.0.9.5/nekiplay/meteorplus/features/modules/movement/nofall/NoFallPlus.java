package nekiplay.meteorplus.features.modules.movement.nofall;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.movement.nofall.modes.Eclip;
import nekiplay.meteorplus.features.modules.movement.nofall.modes.ElytraFly;
import nekiplay.meteorplus.features.modules.movement.nofall.modes.MatrixNew;
import nekiplay.meteorplus.features.modules.movement.nofall.modes.No_Ground;
import nekiplay.meteorplus.features.modules.movement.nofall.modes.No_Ground_Elytra;
import nekiplay.meteorplus.features.modules.movement.nofall.modes.Verus;
import nekiplay.meteorplus.features.modules.movement.nofall.modes.Vulcan;
import nekiplay.meteorplus.features.modules.movement.nofall.modes.Vulcan277;

public class NoFallPlus extends Module {
   private final SettingGroup sgGeneral;
   private NoFallMode currentMode;
   public final Setting<NoFallModes> mode;

   public NoFallPlus() {
      super(Categories.Movement, "no-fall+", "Bypass fall damage or reduce fall damage");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("The method of applying nofall.")).defaultValue(NoFallModes.Elytra_Clip)).onModuleActivated((spiderModesSetting) -> this.onModeChanged((NoFallModes)spiderModesSetting.get()))).onChanged(this::onModeChanged)).build());
      this.onModeChanged((NoFallModes)this.mode.get());
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

   private void onModeChanged(NoFallModes mode) {
      switch (mode) {
         case No_Ground_Elytra -> this.currentMode = new No_Ground_Elytra();
         case No_Ground -> this.currentMode = new No_Ground();
         case Elytra_Fly -> this.currentMode = new ElytraFly();
         case Elytra_Clip -> this.currentMode = new Eclip();
         case Matrix_New -> this.currentMode = new MatrixNew();
         case Verus -> this.currentMode = new Verus();
         case Vulcan -> this.currentMode = new Vulcan();
         case Vulcan_2dot7dot7 -> this.currentMode = new Vulcan277();
      }

   }
}
