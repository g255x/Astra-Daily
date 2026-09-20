package dev.Astra.mod.commands.impl;

import dev.Astra.Astra;
import dev.Astra.core.impl.PlayerManager;
import dev.Astra.mod.commands.Command;
import dev.Astra.mod.gui.windows.WindowBase;
import dev.Astra.mod.gui.windows.WindowsScreen;
import dev.Astra.mod.gui.windows.impl.ItemSelectWindow;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2246;

public class XrayCommand extends Command {
   public XrayCommand() {
      super("xray", "[\"\"/name/reset/clear/list] | [add/remove] [name]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         PlayerManager.screenToOpen = new WindowsScreen(new WindowBase[]{new ItemSelectWindow(Astra.XRAY)});
      } else {
         switch (parameters[0]) {
            case "reset":
               Astra.XRAY.clear();
               Astra.XRAY.add(class_2246.field_10442.method_9539());
               Astra.XRAY.add(class_2246.field_29029.method_9539());
               Astra.XRAY.add(class_2246.field_10571.method_9539());
               Astra.XRAY.add(class_2246.field_23077.method_9539());
               Astra.XRAY.add(class_2246.field_10212.method_9539());
               Astra.XRAY.add(class_2246.field_29027.method_9539());
               Astra.XRAY.add(class_2246.field_10080.method_9539());
               Astra.XRAY.add(class_2246.field_10013.method_9539());
               Astra.XRAY.add(class_2246.field_29220.method_9539());
               Astra.XRAY.add(class_2246.field_29030.method_9539());
               Astra.XRAY.add(class_2246.field_10418.method_9539());
               Astra.XRAY.add(class_2246.field_29219.method_9539());
               Astra.XRAY.add(class_2246.field_22109.method_9539());
               Astra.XRAY.add(class_2246.field_10213.method_9539());
               Astra.XRAY.add(class_2246.field_10090.method_9539());
               Astra.XRAY.add(class_2246.field_29028.method_9539());
               this.sendChatMessage("§fBlocks list got reset");
               return;
            case "clear":
               Astra.XRAY.clear();
               this.sendChatMessage("§fBlocks list got clear");
               return;
            case "list":
               if (Astra.XRAY.getList().isEmpty()) {
                  this.sendChatMessage("§fBlocks list is empty");
                  return;
               }

               for(String name : Astra.XRAY.getList()) {
                  this.sendChatMessage("§a" + name);
               }

               return;
            case "add":
               if (parameters.length == 2) {
                  Astra.XRAY.add(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Astra.XRAY.inWhitelist(parameters[1]) ? " §ahas been added" : " §chas been removed"));
                  return;
               }

               this.sendUsage();
               return;
            case "remove":
               if (parameters.length == 2) {
                  Astra.XRAY.remove(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Astra.XRAY.inWhitelist(parameters[1]) ? " §ahas been added" : " §chas been removed"));
                  return;
               }

               this.sendUsage();
               return;
            default:
               if (parameters.length == 1) {
                  this.sendChatMessage("§f" + parameters[0] + (Astra.XRAY.inWhitelist(parameters[0]) ? " §ais in whitelist" : " §cisn't in whitelist"));
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
         ArrayList<String> correct = new ArrayList();

         for(String x : List.of("add", "remove", "list", "reset", "clear")) {
            if (input.equalsIgnoreCase(Astra.getPrefix() + "xray") || x.toLowerCase().startsWith(input)) {
               correct.add(x);
            }
         }

         int numCmds = correct.size();
         String[] commands = new String[numCmds];
         int i = 0;

         for(String x : correct) {
            commands[i++] = x;
         }

         return commands;
      }
   }
}
