package nekiplay.meteorplus.features.modules.movement.fastladder;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.movement.fastladder.modes.Spartan;

public class FastLadderPlus extends Module {
   private final SettingGroup sgGeneral;
   public final Setting<FastLadderModes> spiderMode;
   private FastLadderMode currentMode;

   public FastLadderPlus() {
      super(Categories.Movement, "fast-climb+", "Bypass fast-climb");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.spiderMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("The method of applying spider.")).defaultValue(FastLadderModes.Spartan)).onModuleActivated((spiderModesSetting) -> this.onSpiderModeChanged((FastLadderModes)spiderModesSetting.get()))).onChanged(this::onSpiderModeChanged)).build());
      this.onSpiderModeChanged((FastLadderModes)this.spiderMode.get());
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

   private void onSpiderModeChanged(FastLadderModes mode) {
      switch (mode) {
         case Spartan:
            this.currentMode = new Spartan();
         default:
      }
   }
}
