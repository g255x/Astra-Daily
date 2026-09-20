package dev.Astra.mod.commands.impl;

import dev.Astra.mod.commands.Command;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import java.util.List;

public class PrefixCommand extends Command {
   public PrefixCommand() {
      super("prefix", "[prefix]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.sendUsage();
      } else if (parameters[0].startsWith("/")) {
         this.sendChatMessage("§fPlease specify keyCodec valid §bprefix.");
      } else {
         ClientSetting.INSTANCE.prefix.setValue(parameters[0]);
         this.sendChatMessage("§bPrefix §fset to §e" + parameters[0]);
      }
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
