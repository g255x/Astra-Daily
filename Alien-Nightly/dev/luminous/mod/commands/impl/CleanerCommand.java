package dev.luminous.mod.commands.impl;

import dev.luminous.Alien;
import dev.luminous.core.impl.PlayerManager;
import dev.luminous.mod.commands.Command;
import dev.luminous.mod.gui.windows.WindowBase;
import dev.luminous.mod.gui.windows.WindowsScreen;
import dev.luminous.mod.gui.windows.impl.ItemSelectWindow;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1802;

public class CleanerCommand extends Command {
   public CleanerCommand() {
      super("cleaner", "[\"\"/name/reset/clear/list] | [add/remove] [name]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         PlayerManager.screenToOpen = new WindowsScreen(new WindowBase[]{new ItemSelectWindow(Alien.CLEANER)});
      } else {
         switch (parameters[0]) {
            case "reset":
               Alien.CLEANER.clear();
               Alien.CLEANER.add(class_1802.field_22022.method_7876());
               Alien.CLEANER.add(class_1802.field_22024.method_7876());
               Alien.CLEANER.add(class_1802.field_22027.method_7876());
               Alien.CLEANER.add(class_1802.field_22028.method_7876());
               Alien.CLEANER.add(class_1802.field_22029.method_7876());
               Alien.CLEANER.add(class_1802.field_22030.method_7876());
               Alien.CLEANER.add(class_1802.field_8281.method_7876());
               Alien.CLEANER.add(class_1802.field_8466.method_7876());
               Alien.CLEANER.add(class_1802.field_8634.method_7876());
               Alien.CLEANER.add(class_1802.field_8367.method_7876());
               Alien.CLEANER.add(class_1802.field_8287.method_7876());
               Alien.CLEANER.add(class_1802.field_8786.method_7876());
               Alien.CLEANER.add(class_1802.field_8574.method_7876());
               Alien.CLEANER.add(class_1802.field_8436.method_7876());
               Alien.CLEANER.add(class_1802.field_8288.method_7876());
               Alien.CLEANER.add(class_1802.field_8301.method_7876());
               Alien.CLEANER.add(class_1802.field_8833.method_7876());
               Alien.CLEANER.add(class_1802.field_8884.method_7876());
               Alien.CLEANER.add(class_1802.field_8249.method_7876());
               Alien.CLEANER.add(class_1802.field_8105.method_7876());
               Alien.CLEANER.add(class_1802.field_8793.method_7876());
               Alien.CLEANER.add(class_1802.field_8801.method_7876());
               Alien.CLEANER.add(class_1802.field_23141.method_7876());
               Alien.CLEANER.add(class_1802.field_8782.method_7876());
               this.sendChatMessage("§fItems list got reset");
               return;
            case "clear":
               Alien.CLEANER.getList().clear();
               this.sendChatMessage("§fItems list got clear");
               return;
            case "list":
               if (Alien.CLEANER.getList().isEmpty()) {
                  this.sendChatMessage("§fItems list is empty");
                  return;
               }

               for(String name : Alien.CLEANER.getList()) {
                  this.sendChatMessage("§a" + name);
               }

               return;
            case "add":
               if (parameters.length == 2) {
                  Alien.CLEANER.add(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Alien.CLEANER.inList(parameters[1]) ? " §ahas been added" : " §chas been removed"));
                  return;
               }

               this.sendUsage();
               return;
            case "remove":
               if (parameters.length == 2) {
                  Alien.CLEANER.remove(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Alien.CLEANER.inList(parameters[1]) ? " §ahas been added" : " §chas been removed"));
                  return;
               }

               this.sendUsage();
               return;
            default:
               if (parameters.length == 1) {
                  this.sendChatMessage("§f" + parameters[0] + (Alien.CLEANER.inList(parameters[0]) ? " §ais in whitelist" : " §cisn't in whitelist"));
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
            if (input.equalsIgnoreCase(Alien.getPrefix() + "cleaner") || x.toLowerCase().startsWith(input)) {
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
