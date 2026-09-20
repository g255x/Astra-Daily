package nekiplay.meteorplus.features.modules.integrations;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import nekiplay.MixinPlugin;
import nekiplay.meteorplus.MeteorPlusAddon;

public class MapIntegration extends Module {
   private final SettingGroup baritoneIntegration;
   public final Setting<Boolean> baritoneGoto;
   public final Setting<Boolean> baritoneElytra;
   private final SettingGroup fullMap;
   public final Setting<Boolean> showBlock;

   public MapIntegration() {
      super(MeteorPlusAddon.CATEGORYMODS, "Mini-map", "Improvements for mods on mini-maps.");
      this.baritoneIntegration = this.settings.createGroup("Baritone");
      this.baritoneGoto = this.baritoneIntegration.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Baritone support")).description("Moving in baritone at the selected location.")).defaultValue(true)).build());
      this.baritoneElytra = this.baritoneIntegration.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Baritone elytra")).description("Elytra to location.")).defaultValue(true)).visible(() -> {
         boolean allow = false;

         for(IBaritone baritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (!baritone.getCommandManager().getRegistry().stream().filter((a) -> ((String)a.getNames().get(0)).equalsIgnoreCase("elytra")).findAny().isEmpty()) {
               allow = true;
               break;
            }
         }

         if (allow) {
            allow = (Boolean)this.baritoneGoto.get();
         }

         return allow;
      })).build());
      this.fullMap = this.settings.createGroup("Full map");
      this.showBlock = this.fullMap.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Show block")).description("Shows the name of the block in the clicked position.")).visible(() -> MixinPlugin.isXaeroWorldMapresent)).defaultValue(true)).build());
   }
}
