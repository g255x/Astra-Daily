package nekiplay.meteorplus.features.modules.movement.noslow;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.main.events.PlayerUseMultiplierEvent;
import nekiplay.meteorplus.features.modules.movement.noslow.modes.Grim;
import nekiplay.meteorplus.features.modules.movement.noslow.modes.GrimNew;
import nekiplay.meteorplus.features.modules.movement.noslow.modes.Matrix;
import nekiplay.meteorplus.features.modules.movement.noslow.modes.NCPStrict;
import nekiplay.meteorplus.features.modules.movement.noslow.modes.Vanila;

public class NoSlowPlus extends Module {
   public SettingGroup defaultGroup;
   private NoSlowMode currentMode;
   public final Setting<NoSlowModes> mode;
   public SettingGroup usingItemGroup;
   public SettingGroup sneakGroup;
   public SettingGroup otherGroup;
   public final Setting<Double> usingForward;
   public final Setting<Double> usingSideways;
   public final Setting<Double> sneakForward;
   public final Setting<Double> sneakSideways;
   public final Setting<Double> otherForward;
   public final Setting<Double> otherSideways;

   public NoSlowPlus() {
      super(Categories.Movement, "no-slow+", "Remove or increase slowness.");
      this.defaultGroup = this.settings.getDefaultGroup();
      this.mode = this.defaultGroup.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("The method of applying no slow.")).defaultValue(NoSlowModes.Vanila)).onModuleActivated((spiderModesSetting) -> this.onModeChanged((NoSlowModes)spiderModesSetting.get()))).onChanged(this::onModeChanged)).build());
      this.usingItemGroup = this.settings.createGroup("Using item");
      this.sneakGroup = this.settings.createGroup("Sneak");
      this.otherGroup = this.settings.createGroup("Other");
      this.usingForward = this.usingItemGroup.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("forward-multiplier")).defaultValue((double)1.0F).min(0.2).sliderRange(0.2, (double)1.0F).visible(() -> this.mode.get() != NoSlowModes.Matrix)).build());
      this.usingSideways = this.usingItemGroup.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("sideways-multiplier")).defaultValue((double)1.0F).min(0.2).sliderRange(0.2, (double)1.0F).visible(() -> this.mode.get() != NoSlowModes.Matrix)).build());
      this.sneakForward = this.sneakGroup.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("forward-multiplier")).defaultValue((double)1.0F).min(0.2).sliderRange(0.2, (double)1.0F).visible(() -> this.mode.get() != NoSlowModes.Matrix)).build());
      this.sneakSideways = this.sneakGroup.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("sideways-multiplier")).defaultValue((double)1.0F).min(0.2).sliderRange(0.2, (double)1.0F).visible(() -> this.mode.get() != NoSlowModes.Matrix)).build());
      this.otherForward = this.otherGroup.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("forward-multiplier")).defaultValue((double)1.0F).min(0.2).sliderRange(0.2, (double)1.0F).visible(() -> this.mode.get() != NoSlowModes.Matrix)).build());
      this.otherSideways = this.otherGroup.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("sideways-multiplier")).defaultValue((double)1.0F).min(0.2).sliderRange(0.2, (double)1.0F).visible(() -> this.mode.get() != NoSlowModes.Matrix)).build());
   }

   private void onModeChanged(NoSlowModes mode) {
      switch (mode) {
         case Vanila -> this.currentMode = new Vanila();
         case NCP_Strict -> this.currentMode = new NCPStrict();
         case Grim_1dot8 -> this.currentMode = new Grim();
         case Grim_New -> this.currentMode = new GrimNew();
         case Matrix -> this.currentMode = new Matrix();
      }

   }

   @EventHandler
   private void onUse(PlayerUseMultiplierEvent event) {
      this.currentMode.onUse(event);
   }

   @EventHandler
   private void onTickEventPre(TickEvent.Pre event) {
      this.currentMode.onTickEventPre(event);
   }

   public void onActivate() {
      this.currentMode.onActivate();
   }
}
