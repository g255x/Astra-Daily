package dev.Astra.core.impl;

import dev.Astra.Astra;
import dev.Astra.api.interfaces.IChatHudHook;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.mod.commands.Command;
import dev.Astra.mod.commands.impl.AimCommand;
import dev.Astra.mod.commands.impl.BindCommand;
import dev.Astra.mod.commands.impl.BindsCommand;
import dev.Astra.mod.commands.impl.ClipCommand;
import dev.Astra.mod.commands.impl.FakePlayerCommand;
import dev.Astra.mod.commands.impl.FriendCommand;
import dev.Astra.mod.commands.impl.GamemodeCommand;
import dev.Astra.mod.commands.impl.GcCommand;
import dev.Astra.mod.commands.impl.LoadCommand;
import dev.Astra.mod.commands.impl.PeekCommand;
import dev.Astra.mod.commands.impl.PingCommand;
import dev.Astra.mod.commands.impl.PrefixCommand;
import dev.Astra.mod.commands.impl.RejoinCommand;
import dev.Astra.mod.commands.impl.ReloadCommand;
import dev.Astra.mod.commands.impl.SaveCommand;
import dev.Astra.mod.commands.impl.TCommand;
import dev.Astra.mod.commands.impl.TeleportCommand;
import dev.Astra.mod.commands.impl.ToggleCommand;
import dev.Astra.mod.commands.impl.WatermarkCommand;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import java.util.HashMap;
import net.minecraft.class_2561;
import net.minecraft.class_338;

public class CommandManager implements Wrapper {
   private final HashMap<String, Command> commands = new HashMap();

   public CommandManager() {
      this.init();
   }

   public void init() {
      this.registerCommand(new AimCommand());
      this.registerCommand(new BindCommand());
      this.registerCommand(new BindsCommand());
      this.registerCommand(new ClipCommand());
      this.registerCommand(new FakePlayerCommand());
      this.registerCommand(new FriendCommand());
      this.registerCommand(new GamemodeCommand());
      this.registerCommand(new LoadCommand());
      this.registerCommand(new PingCommand());
      this.registerCommand(new PrefixCommand());
      this.registerCommand(new RejoinCommand());
      this.registerCommand(new ReloadCommand());
      this.registerCommand(new SaveCommand());
      this.registerCommand(new TeleportCommand());
      this.registerCommand(new TCommand());
      this.registerCommand(new ToggleCommand());
      this.registerCommand(new WatermarkCommand());
      this.registerCommand(new PeekCommand());
      this.registerCommand(new GcCommand());
   }

   public static void sendMessage(String message) {
      mc.execute(() -> {
         if (!Module.nullCheck()) {
            if (ClientSetting.INSTANCE.messageStyle.getValue() == ClientSetting.Style.Earth) {
               mc.field_1705.method_1743().method_1812(class_2561.method_30163(message));
            } else if (ClientSetting.INSTANCE.messageStyle.getValue() == ClientSetting.Style.Moon) {
               class_338 var1 = mc.field_1705.method_1743();
               String var2 = ClientSetting.INSTANCE.hackName.getValue();
               var1.method_1812(class_2561.method_30163("§f[§b" + var2 + "§f] " + message));
            } else {
               IChatHudHook var10000 = (IChatHudHook)mc.field_1705.method_1743();
               String var10001 = ClientSetting.INSTANCE.hackName.getValue();
               var10000.frogClient$addMessage(class_2561.method_30163(var10001 + "§f " + message));
            }
         }
      });
   }

   public static void sendMessageId(String message, int id) {
      mc.execute(() -> {
         if (!Module.nullCheck()) {
            if (ClientSetting.INSTANCE.messageStyle.getValue() == ClientSetting.Style.Earth) {
               ((IChatHudHook)mc.field_1705.method_1743()).frogClient$addMessage(class_2561.method_30163(message), id);
            } else if (ClientSetting.INSTANCE.messageStyle.getValue() == ClientSetting.Style.Moon) {
               IChatHudHook var2 = (IChatHudHook)mc.field_1705.method_1743();
               String var3 = ClientSetting.INSTANCE.hackName.getValue();
               var2.frogClient$addMessage(class_2561.method_30163("§f[§b" + var3 + "§f] " + message), id);
            } else {
               IChatHudHook var10000 = (IChatHudHook)mc.field_1705.method_1743();
               String var10001 = ClientSetting.INSTANCE.hackName.getValue();
               var10000.frogClient$addMessage(class_2561.method_30163(var10001 + "§f " + message), id);
            }
         }
      });
   }

   public static void sendChatMessageWidthIdNoSync(String message, int id) {
      mc.execute(() -> {
         if (!Module.nullCheck()) {
            ((IChatHudHook)mc.field_1705.method_1743()).frogClient$addMessageOutSync(class_2561.method_30163("§f" + message), id);
         }
      });
   }

   private void registerCommand(Command command) {
      this.commands.put(command.getName(), command);
   }

   public Command getCommandBySyntax(String string) {
      return (Command)this.commands.get(string);
   }

   public HashMap<String, Command> getCommands() {
      return this.commands;
   }

   public void command(String[] commandIn) {
      Command command = (Command)this.commands.get(commandIn[0].substring(Astra.getPrefix().length()).toLowerCase());
      if (command == null) {
         sendMessage("§4Invalid Command!");
      } else {
         String[] parameterList = new String[commandIn.length - 1];
         System.arraycopy(commandIn, 1, parameterList, 0, commandIn.length - 1);
         if (parameterList.length == 1 && parameterList[0].equals("help")) {
            command.sendUsage();
            return;
         }

         command.runCommand(parameterList);
      }

   }
}
