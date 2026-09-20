package dev.Astra.mod.commands.impl;

import dev.Astra.Astra;
import dev.Astra.core.impl.PlayerManager;
import dev.Astra.mod.commands.Command;
import dev.Astra.mod.gui.windows.WindowBase;
import dev.Astra.mod.gui.windows.WindowsScreen;
import dev.Astra.mod.gui.windows.impl.ItemSelectWindow;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1802;

public class TradeCommand extends Command {
   public TradeCommand() {
      super("trade", "[\"\"/name/reset/clear/list] | [add/remove] [name]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         PlayerManager.screenToOpen = new WindowsScreen(new WindowBase[]{new ItemSelectWindow(Astra.TRADE)});
      } else {
         switch (parameters[0]) {
            case "reset":
               Astra.TRADE.clear();
               Astra.TRADE.add(class_1802.field_8598.method_7876());
               Astra.TRADE.add(class_1802.field_8603.method_7876());
               this.sendChatMessage("§fItems list got reset");
               return;
            case "clear":
               Astra.TRADE.clear();
               this.sendChatMessage("§fItems list got clear");
               return;
            case "list":
               if (Astra.TRADE.getList().isEmpty()) {
                  this.sendChatMessage("§fItems list is empty");
                  return;
               }

               for(String name : Astra.TRADE.getList()) {
                  this.sendChatMessage("§a" + name);
               }

               return;
            case "add":
               if (parameters.length == 2) {
                  Astra.TRADE.add(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Astra.TRADE.inWhitelist(parameters[1]) ? " §ahas been added" : " §chas been removed"));
                  return;
               }

               this.sendUsage();
               return;
            case "remove":
               if (parameters.length == 2) {
                  Astra.TRADE.remove(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Astra.TRADE.inWhitelist(parameters[1]) ? " §ahas been added" : " §chas been removed"));
                  return;
               }

               this.sendUsage();
               return;
            default:
               if (parameters.length == 1) {
                  this.sendChatMessage("§f" + parameters[0] + (Astra.TRADE.inWhitelist(parameters[0]) ? " §ais in whitelist" : " §cisn't in whitelist"));
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
            if (input.equalsIgnoreCase(Astra.getPrefix() + "trade") || x.toLowerCase().startsWith(input)) {
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
