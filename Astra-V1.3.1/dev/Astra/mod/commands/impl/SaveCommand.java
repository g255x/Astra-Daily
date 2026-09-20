package dev.Astra.mod.commands.impl;

import dev.Astra.Astra;
import dev.Astra.core.impl.ConfigManager;
import dev.Astra.mod.commands.Command;
import java.util.List;

public class SaveCommand extends Command {
   public SaveCommand() {
      super("save", "");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 1) {
         this.sendChatMessage("§fSaving config named " + parameters[0]);
         ConfigManager.saveCfg(parameters[0]);
      } else {
         this.sendChatMessage("§fSaving..");
      }

      Astra.save();
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
