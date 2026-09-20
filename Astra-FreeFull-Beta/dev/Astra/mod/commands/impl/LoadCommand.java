package dev.Astra.mod.commands.impl;

import dev.Astra.core.impl.ConfigManager;
import dev.Astra.mod.commands.Command;
import java.util.List;

public class LoadCommand extends Command {
   public LoadCommand() {
      super("load", "[config]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.sendUsage();
      } else {
         this.sendChatMessage("§fLoading..");
         ConfigManager.loadCfg(parameters[0]);
      }
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
