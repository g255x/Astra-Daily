package dev.luminous.mod.commands.impl;

import dev.luminous.mod.commands.Command;
import java.text.DecimalFormat;
import java.util.List;

public class ClipCommand extends Command {
   public ClipCommand() {
      super("clip", "[x] [y] [z]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length != 3) {
         this.sendUsage();
      } else if (this.isNumeric(parameters[0])) {
         double x = mc.field_1724.method_23317() + Double.parseDouble(parameters[0]);
         if (this.isNumeric(parameters[1])) {
            double y = mc.field_1724.method_23318() + Double.parseDouble(parameters[1]);
            if (this.isNumeric(parameters[2])) {
               double z = mc.field_1724.method_23321() + Double.parseDouble(parameters[2]);
               mc.field_1724.method_5814(x, y, z);
               DecimalFormat df = new DecimalFormat("0.0");
               String var10001 = df.format(x);
               this.sendChatMessage("§fTeleported to §e" + var10001 + ", " + df.format(y) + ", " + df.format(z));
            } else {
               this.sendUsage();
            }
         } else {
            this.sendUsage();
         }
      } else {
         this.sendUsage();
      }

   }

   private boolean isNumeric(String str) {
      return str.matches("-?\\d+(\\.\\d+)?");
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return new String[]{"0 "};
   }
}
