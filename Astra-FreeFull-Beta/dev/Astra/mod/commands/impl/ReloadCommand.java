package dev.Astra.mod.commands.impl;

import dev.Astra.Astra;
import dev.Astra.core.impl.ConfigManager;
import dev.Astra.mod.commands.Command;
import java.util.List;

public class ReloadCommand extends Command {
   public ReloadCommand() {
      super("reload", "");
   }

   public void runCommand(String[] parameters) {
      this.sendChatMessage("§fReloading..");
      Astra.CONFIG = new ConfigManager();
      Astra.CONFIG.load();
      Astra.FRIEND.read();
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
