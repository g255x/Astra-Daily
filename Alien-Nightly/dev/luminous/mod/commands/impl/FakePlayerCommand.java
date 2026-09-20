package dev.luminous.mod.commands.impl;

import dev.luminous.Alien;
import dev.luminous.mod.commands.Command;
import dev.luminous.mod.modules.impl.misc.FakePlayer;
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
         String var2 = parameters[0];
         byte var3 = 0;
         //$FF: var3->value
         //0->record
         //1->play
         switch (var2.typeSwitch<invokedynamic>(var2, var3)) {
            case -1:
            default:
               this.sendUsage();
               break;
            case 0:
               FakePlayer.INSTANCE.record.setValue(!FakePlayer.INSTANCE.record.getValue());
               break;
            case 1:
               FakePlayer.INSTANCE.play.setValue(!FakePlayer.INSTANCE.play.getValue());
         }
      }

   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      if (count != 1) {
         return null;
      } else {
         String input = ((String)seperated.getLast()).toLowerCase();
         List<String> correct = new ArrayList();

         for(String x : List.of("record", "play")) {
            if (input.equalsIgnoreCase(Alien.getPrefix() + "fakeplayer") || x.toLowerCase().startsWith(input)) {
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
