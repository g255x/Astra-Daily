package dev.Astra.mod.commands.impl;

import dev.Astra.Astra;
import dev.Astra.mod.commands.Command;
import dev.Astra.mod.modules.Module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BindsCommand extends Command {
   public BindsCommand() {
      super("binds", "");
   }

   public void runCommand(String[] parameters) {
      ArrayList<String> list = new ArrayList();

      for(Module x : Astra.MODULE.getModules()) {
         if (x.getBindSetting().getValue() != -1) {
            String var10001 = x.getDisplayName();
            list.add("§f" + var10001 + " §7- §r" + x.getBindSetting().getKeyString());
         }
      }

      Iterator<String> temp = list.iterator();
      int i = 0;
      StringBuilder string = new StringBuilder();

      while(temp.hasNext()) {
         if (i == 0) {
            string = new StringBuilder((String)temp.next());
         } else {
            string.append("§7, ").append((String)temp.next());
         }

         ++i;
         if (i >= 3 || !temp.hasNext()) {
            this.sendChatMessage(string.toString());
            i = 0;
         }
      }

   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
