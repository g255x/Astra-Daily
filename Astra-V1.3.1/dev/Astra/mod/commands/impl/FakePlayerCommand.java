package dev.Astra.mod.commands.impl;

import dev.Astra.Astra;
import dev.Astra.mod.commands.Command;
import dev.Astra.mod.modules.impl.misc.FakePlayer;
import java.util.ArrayList;
import java.util.List;

public class FakePlayerCommand extends Command {
   public FakePlayerCommand() {
      super("fakeplayer", "[record/play]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         FakePlayer.INSTANCE.toggle();
      } else {
         String string = parameters[0];
         int n = 0;
         switch (string) {
            case "record" -> FakePlayer.INSTANCE.record.setValue(!FakePlayer.INSTANCE.record.getValue());
            case "play" -> FakePlayer.INSTANCE.play.setValue(!FakePlayer.INSTANCE.play.getValue());
            default -> this.sendUsage();
         }

      }
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      if (count != 1) {
         return null;
      } else {
         String input = ((String)seperated.getLast()).toLowerCase();
         ArrayList<String> correct = new ArrayList();

         for(String x : List.of("record", "play")) {
            if (input.equalsIgnoreCase(Astra.getPrefix() + "fakeplayer") || x.toLowerCase().startsWith(input)) {
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
