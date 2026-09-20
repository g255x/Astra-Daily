package dev.luminous.mod.commands.impl;

import dev.luminous.Alien;
import dev.luminous.core.impl.PlayerManager;
import dev.luminous.mod.commands.Command;
import dev.luminous.mod.gui.windows.WindowBase;
import dev.luminous.mod.gui.windows.WindowsScreen;
import dev.luminous.mod.gui.windows.impl.ItemSelectWindow;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2246;

public class XrayCommand extends Command {
   public XrayCommand() {
      super("xray", "[\"\"/name/reset/clear/list] | [add/remove] [name]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         PlayerManager.screenToOpen = new WindowsScreen(new WindowBase[]{new ItemSelectWindow(Alien.XRAY)});
      } else {
         switch (parameters[0]) {
            case "reset":
               Alien.XRAY.clear();
               Alien.XRAY.add(class_2246.field_10442.method_9539());
               Alien.XRAY.add(class_2246.field_29029.method_9539());
               Alien.XRAY.add(class_2246.field_10571.method_9539());
               Alien.XRAY.add(class_2246.field_23077.method_9539());
               Alien.XRAY.add(class_2246.field_10212.method_9539());
               Alien.XRAY.add(class_2246.field_29027.method_9539());
               Alien.XRAY.add(class_2246.field_10080.method_9539());
               Alien.XRAY.add(class_2246.field_10013.method_9539());
               Alien.XRAY.add(class_2246.field_29220.method_9539());
               Alien.XRAY.add(class_2246.field_29030.method_9539());
               Alien.XRAY.add(class_2246.field_10418.method_9539());
               Alien.XRAY.add(class_2246.field_29219.method_9539());
               Alien.XRAY.add(class_2246.field_22109.method_9539());
               Alien.XRAY.add(class_2246.field_10213.method_9539());
               Alien.XRAY.add(class_2246.field_10090.method_9539());
               Alien.XRAY.add(class_2246.field_29028.method_9539());
               this.sendChatMessage("§fBlocks list got reset");
               return;
            case "clear":
               Alien.XRAY.clear();
               this.sendChatMessage("§fBlocks list got clear");
               return;
            case "list":
               if (Alien.XRAY.getList().isEmpty()) {
                  this.sendChatMessage("§fBlocks list is empty");
                  return;
               }

               for(String name : Alien.XRAY.getList()) {
                  this.sendChatMessage("§a" + name);
               }

               return;
            case "add":
               if (parameters.length == 2) {
                  Alien.XRAY.add(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Alien.XRAY.inWhitelist(parameters[1]) ? " §ahas been added" : " §chas been removed"));
                  return;
               }

               this.sendUsage();
               return;
            case "remove":
               if (parameters.length == 2) {
                  Alien.XRAY.remove(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Alien.XRAY.inWhitelist(parameters[1]) ? " §ahas been added" : " §chas been removed"));
                  return;
               }

               this.sendUsage();
               return;
            default:
               if (parameters.length == 1) {
                  this.sendChatMessage("§f" + parameters[0] + (Alien.XRAY.inWhitelist(parameters[0]) ? " §ais in whitelist" : " §cisn't in whitelist"));
               } else {
                  this.sendUsage();
               }
         }
      }

   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      if (count != 1) {
         return null;
      } else {
         String input = ((String)seperated.getLast()).toLowerCase();
         List<String> correct = new ArrayList();

         for(String x : List.of("add", "remove", "list", "reset", "clear")) {
            if (input.equalsIgnoreCase(Alien.getPrefix() + "xray") || x.toLowerCase().startsWith(input)) {
               correct.add(x);
            }
         }

         int numCmds = correct.size();
         String[] commands = new String[numCmds];
         int i = 0;

         for(String xx : correct) {
            commands[i++] = xx;
         }

         return commands;
      }
   }
}
