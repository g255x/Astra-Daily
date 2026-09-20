package nekiplay.meteorplus.features.modules.misc;

import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_5250;
import net.minecraft.class_5251;

public class ChatPrefix extends Module {
   private final SettingGroup sgGeneral;
   private final Setting<String> prefix;
   private final Setting<SettingColor> prefixColor;

   public ChatPrefix() {
      super(Categories.Misc, "meteor+-chat-prefix", "prefix for enabling and disabling Meteor+ modules.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.prefix = this.sgGeneral.add(((StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)(new StringSetting.Builder()).name("prefix")).description("Which prefix to be displayed for Meteor+ modules.")).defaultValue("Meteor+")).onChanged((reload) -> this.setPrefixes())).build());
      this.prefixColor = this.sgGeneral.add(((ColorSetting.Builder)((ColorSetting.Builder)(new ColorSetting.Builder()).name("color")).description("Which color to use for the prefix.")).defaultValue(new SettingColor(0, 220, 4, 255)).build());
   }

   public void onActivate() {
      this.setPrefixes();
   }

   public void onDeactivate() {
      ChatUtils.unregisterCustomPrefix("nekiplay.meteorplus.features.modules");
   }

   public void setPrefixes() {
      if (this.isActive()) {
         ChatUtils.registerCustomPrefix("nekiplay.meteorplus.features.modules", this::getPrefix);
      }

   }

   public class_2561 getPrefix() {
      class_5250 value = class_2561.method_43470((String)this.prefix.get());
      class_5250 prefix = class_2561.method_43470("");
      value.method_10862(value.method_10866().method_27703(class_5251.method_27717(((SettingColor)this.prefixColor.get()).getPacked())));
      prefix.method_10862(prefix.method_10866().method_27706(class_124.field_1080)).method_10852(class_2561.method_43470("[")).method_10852(value).method_10852(class_2561.method_43470("] "));
      return prefix;
   }
}
