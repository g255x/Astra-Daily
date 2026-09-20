package dev.luminous.mod.commands.impl;

import dev.luminous.mod.commands.Command;
import java.util.List;

public class GcCommand extends Command {
   public GcCommand() {
      super("gc", "");
   }

   public void runCommand(String[] parameters) {
      System.gc();
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
