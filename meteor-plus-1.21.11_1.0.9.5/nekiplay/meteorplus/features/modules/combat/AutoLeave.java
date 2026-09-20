package nekiplay.meteorplus.features.modules.combat;

import java.util.Objects;
import meteordevelopment.meteorclient.events.entity.EntityAddedEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_2661;

public class AutoLeave extends Module {
   private final SettingGroup ALSettings;
   private final Setting<Boolean> visualRangeIgnoreFriends;
   private final Setting<Boolean> AutoDisable;
   private final Setting<Boolean> Command;
   private final Setting<String> command_str;

   public AutoLeave() {
      super(Categories.Combat, "auto-leave", "Automatically logs out from the server when someone enters your render distance.");
      this.ALSettings = this.settings.getDefaultGroup();
      this.visualRangeIgnoreFriends = this.ALSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("ignore-friends")).description("Ignores friends.")).defaultValue(true)).build());
      this.AutoDisable = this.ALSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("auto-disable")).description("Disables function after player detect.")).defaultValue(true)).build());
      this.Command = this.ALSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("command")).description("Send command instead of leave.")).defaultValue(false)).build());
      SettingGroup var10001 = this.ALSettings;
      StringSetting.Builder var10002 = (StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)(new StringSetting.Builder()).name("command:")).description("Send command in chat.")).defaultValue("/spawn");
      Setting var10003 = this.Command;
      Objects.requireNonNull(var10003);
      this.command_str = var10001.add(((StringSetting.Builder)var10002.visible(var10003::get)).build());
   }

   @EventHandler
   public void onEntityAdded(EntityAddedEvent event) {
      if (this.mc.field_1724 != null) {
         if ((Boolean)this.visualRangeIgnoreFriends.get()) {
            if (event.entity.method_31747() && !Friends.get().isFriend((class_1657)event.entity) && !Objects.equals(event.entity.method_5477(), this.mc.field_1724.method_5477()) && !Objects.equals(event.entity.method_5477(), "FreeCamera")) {
               if ((Boolean)this.Command.get()) {
                  ChatUtils.sendPlayerMsg((String)this.command_str.get());
                  this.info(String.format("player §c%s§r was detected", event.entity.method_5477()), new Object[0]);
               } else {
                  assert this.mc.field_1687 != null;

                  this.mc.field_1687.method_8525(class_2561.method_30163(""));
                  this.mc.field_1724.field_3944.method_52781(new class_2661(class_2561.method_43470(String.format("[§dAuto Leaeve§r] player %s was detected", event.entity.method_5477()))));
               }

               if ((Boolean)this.AutoDisable.get()) {
                  this.toggle();
               }
            }
         } else if (event.entity.method_31747()) {
            this.mc.field_1724.field_3944.method_52781(new class_2661(class_2561.method_43470(String.format("[§dAuto Leaeve§r] player %s was detected", event.entity.method_5477()))));
            if ((Boolean)this.AutoDisable.get()) {
               this.toggle();
            }
         }

      }
   }
}
