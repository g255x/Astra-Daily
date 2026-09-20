package nekiplay.meteorplus.features.modules.combat;

import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2561;
import net.minecraft.class_746;

public class Teams extends Module {
   private final SettingGroup sgGeneral;
   private final Setting<Boolean> scoreBoardTeam;
   private final Setting<Boolean> colorTeam;
   private final Setting<Boolean> gommeSkyWars;

   public Teams() {
      super(Categories.Combat, "teams", "Check if [entity] is in your own team using scoreboard, name color or team prefix.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.scoreBoardTeam = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Scoreboard-Team")).defaultValue(true)).build());
      this.colorTeam = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("color")).defaultValue(true)).build());
      this.gommeSkyWars = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("GommeHD-SkyWars")).defaultValue(false)).build());
   }

   public boolean isInYourTeam(class_1297 entity) {
      return entity instanceof class_1309 ? this.isInYourTeam((class_1309)entity) : false;
   }

   public boolean isInYourTeam(class_1309 entity) {
      class_746 player = this.mc.field_1724;
      if (player == null) {
         return false;
      } else if (!this.isActive()) {
         return false;
      } else if ((Boolean)this.scoreBoardTeam.get() && player.method_5781() != null && entity.method_5781() != null && player.method_5645(entity.method_5781())) {
         return true;
      } else {
         class_2561 displayName = player.method_5476();
         if ((Boolean)this.gommeSkyWars.get() && displayName != null && entity.method_5476() != null) {
            String targetName = entity.method_5476().getString().replaceAll("§r", "");
            String clientName = displayName.getString().replaceAll("§r", "");
            if (targetName.startsWith("T") && clientName.startsWith("T") && Character.isDigit(targetName.charAt(1)) && Character.isDigit(clientName.charAt(1))) {
               return targetName.charAt(1) == clientName.charAt(1);
            }
         }

         if ((Boolean)this.colorTeam.get() && displayName != null && entity.method_5476() != null) {
            String targetName = entity.method_5476().getString().replaceAll("§r", "");
            String clientName = displayName.getString().replaceAll("§r", "");
            return targetName.startsWith("§" + clientName.charAt(1));
         } else {
            return false;
         }
      }
   }
}
