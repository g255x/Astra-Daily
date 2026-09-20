package nekiplay.meteorplus.features.modules.movement.spider;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.movement.spider.modes.Eclip;
import nekiplay.meteorplus.features.modules.movement.spider.modes.Matrix;
import nekiplay.meteorplus.features.modules.movement.spider.modes.Vulcan;

public class SpiderPlus extends Module {
   private final SettingGroup sgGeneral;
   public final Setting<SpiderModes> spiderMode;
   public final Setting<Integer> Blocks;
   private SpiderMode currentMode;

   public SpiderPlus() {
      super(Categories.Movement, "spider+", "Bypass spider");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.spiderMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("The method of applying spider.")).defaultValue(SpiderModes.Matrix)).onModuleActivated((spiderModesSetting) -> this.onSpiderModeChanged((SpiderModes)spiderModesSetting.get()))).onChanged(this::onSpiderModeChanged)).build());
      this.Blocks = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("blocks")).defaultValue(3)).description("Don't touch if you don't know what it does.")).visible(() -> this.spiderMode.get() == SpiderModes.Elytra_clip)).range(0, 10).build());
      this.onSpiderModeChanged((SpiderModes)this.spiderMode.get());
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

   private void onSpiderModeChanged(SpiderModes mode) {
      switch (mode) {
         case Matrix -> this.currentMode = new Matrix();
         case Vulcan -> this.currentMode = new Vulcan();
         case Elytra_clip -> this.currentMode = new Eclip();
      }

   }
}
