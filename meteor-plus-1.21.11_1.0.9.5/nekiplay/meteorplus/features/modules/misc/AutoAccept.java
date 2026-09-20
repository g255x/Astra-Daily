package nekiplay.meteorplus.features.modules.misc;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.friends.Friend;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.MeteorPlusAddon;
import nekiplay.meteorplus.utils.ColorRemover;

public class AutoAccept extends Module {
   private final SettingGroup AASettings;
   private final Setting<Mode> mode;
   private final Setting<String> custom_pattern;
   private final Setting<Integer> custom_group;
   private final Setting<String> accept_command;
   private final Setting<Boolean> FriendsOnly;
   private final Setting<Integer> Delay;
   private final Setting<Boolean> Debug;
   private final ArrayList<TPPattern> patterns;

   public AutoAccept() {
      super(Categories.Misc, "Auto Accept", "Automatically accepts incoming teleport requests.");
      this.AASettings = this.settings.getDefaultGroup();
      this.mode = this.AASettings.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("Mode")).description("Accept mode.")).defaultValue(AutoAccept.Mode.Auto)).build());
      this.custom_pattern = this.AASettings.add(((StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)(new StringSetting.Builder()).name("Pattern command")).description("Custom pattern.")).defaultValue(".*Игрок (.*) просит телепортироваться к вам!.*")).visible(() -> this.mode.get() == AutoAccept.Mode.Custom)).build());
      this.custom_group = this.AASettings.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Pattern command")).description("Custom pattern.")).defaultValue(1)).visible(() -> this.mode.get() == AutoAccept.Mode.Custom)).build());
      this.accept_command = this.AASettings.add(((StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)(new StringSetting.Builder()).name("Accept command")).description("Accept command.")).defaultValue("/cmi tpaccept {username} tpa")).visible(() -> this.mode.get() == AutoAccept.Mode.Custom)).build());
      this.FriendsOnly = this.AASettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Friends only")).description("Accepts only friends requests.")).defaultValue(true)).build());
      this.Delay = this.AASettings.add(((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Delay")).defaultValue(0)).build());
      this.Debug = this.AASettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Debug")).description("Prints all incoming messages in console (raw format).")).defaultValue(false)).build());
      this.patterns = new ArrayList();
   }

   public void onActivate() {
      this.patterns.clear();
      TPPattern MST_Network = new TPPattern(".*Игрок (.*) просит телепортироваться к вам!.*", 1, "/cmi tpaccept {username} tpa");
      TPPattern HolyWorld = new TPPattern("(.*) просит телепортироваться.*", 1, "/tpaccept");
      TPPattern SimpleTpa = new TPPattern(".*\\[SimpleTpa\\] (.*) has sent you a teleport request!.*", 1, "/tpaccept");
      TPPattern EssentialsEN = new TPPattern("(.*) has requested to teleport to you\\..*", 1, "/tpaccept");
      TPPattern DonutSMP = new TPPattern("(.*) sent you a tpa request", 1, "/tpaccept");
      this.patterns.add(MST_Network);
      this.patterns.add(HolyWorld);
      this.patterns.add(SimpleTpa);
      this.patterns.add(EssentialsEN);
      this.patterns.add(DonutSMP);
   }

   public void onDeactivate() {
      this.patterns.clear();
   }

   private void BetterAccept(String username, TPPattern pattern) {
      if (this.mc.field_1724 != null && (Boolean)this.FriendsOnly.get() && this.isFriend(username)) {
         this.info("Accepting request from §c" + username, new Object[0]);
         ChatUtils.sendPlayerMsg(pattern.command.replace("{username}", username));
      } else if (!(Boolean)this.FriendsOnly.get()) {
         this.info("Accepting request from §c" + username, new Object[0]);
         ChatUtils.sendPlayerMsg(pattern.command.replace("{username}", username));
      }

   }

   private void Accept(String username, TPPattern pattern, String message) {
      if (this.mc.field_1724 != null && this.mode.get() == AutoAccept.Mode.Custom) {
         username = this.getName(pattern, message);
         if ((Boolean)this.FriendsOnly.get() && this.isFriend(username)) {
            this.info("Accepting request from §c" + username, new Object[0]);
            ChatUtils.sendPlayerMsg(((String)this.accept_command.get()).replace("{username}", username));
         } else if (!(Boolean)this.FriendsOnly.get()) {
            this.info("Accepting request from §c" + username, new Object[0]);
            ChatUtils.sendPlayerMsg(((String)this.accept_command.get()).replace("{username}", username));
         }
      } else {
         this.BetterAccept(username, pattern);
      }

   }

   @EventHandler
   public void onMessageRecieve(ReceiveMessageEvent event) {
      if (event.getMessage() != null && this.mc.field_1724 != null) {
         String message = ColorRemover.GetVerbatim(event.getMessage().getString());
         if ((Boolean)this.Debug.get()) {
            MeteorPlusAddon.LOG.info(message);
         }

         Thread th = new Thread(() -> {
            TPPattern custom = new TPPattern((String)this.custom_pattern.get(), (Integer)this.custom_group.get(), (String)this.accept_command.get());
            String nickname = this.getName(message);
            TPPattern pattern = this.getPattern(message);
            if (pattern != null && this.mode.get() != AutoAccept.Mode.Custom) {
               try {
                  Thread.sleep((long)(Integer)this.Delay.get());
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }

               this.Accept(nickname, pattern, message);
            } else {
               nickname = this.getName(custom, message);
               if (!nickname.equals("")) {
                  try {
                     Thread.sleep((long)(Integer)this.Delay.get());
                  } catch (InterruptedException e) {
                     e.printStackTrace();
                  }

                  this.Accept(nickname, custom, message);
               }
            }

         });
         th.start();
      }

   }

   private String getName(String message) {
      String nickname = "";

      for(TPPattern tpPattern : this.patterns) {
         String nn = this.getName(tpPattern, message);
         if (!nn.equals("")) {
            nickname = nn;
         }
      }

      return nickname;
   }

   private String getName(TPPattern tpPattern, String message) {
      String nickname = "";
      Pattern pattern = Pattern.compile(tpPattern.pattern);
      Matcher matcher = pattern.matcher(message);
      if (matcher.find()) {
         String player = matcher.group(tpPattern.group);
         if (!player.equals("")) {
            nickname = player;
         }
      }

      return nickname;
   }

   private TPPattern getPattern(String message) {
      for(TPPattern tpPattern : this.patterns) {
         Pattern pattern = Pattern.compile(tpPattern.pattern);
         Matcher matcher = pattern.matcher(message);
         if (matcher.find()) {
            String player = matcher.group(tpPattern.group);
            if (!player.equals("")) {
               return tpPattern;
            }
         }
      }

      return null;
   }

   private boolean isFriend(String username) {
      for(Friend f : Friends.get()) {
         if (f.name.equals(username)) {
            return true;
         }
      }

      return false;
   }

   public static enum Mode {
      Auto,
      Custom;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Auto, Custom};
      }
   }

   private static class TPPattern {
      public String pattern;
      public int group;
      public String command;

      public TPPattern(String pattern, int group, String command) {
         this.pattern = pattern;
         this.group = group;
         this.command = command;
      }
   }
}
