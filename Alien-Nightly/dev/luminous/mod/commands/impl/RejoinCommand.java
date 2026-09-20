package dev.luminous.mod.commands.impl;

import dev.luminous.mod.commands.Command;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_2661;
import net.minecraft.class_412;
import net.minecraft.class_442;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_9112;

public class RejoinCommand extends Command {
   public RejoinCommand() {
      super("rejoin", "");
   }

   public void runCommand(String[] parameters) {
      mc.method_18859(() -> {
         if (mc.field_1687 != null && mc.method_1558() != null) {
            class_642 lastestServerEntry = mc.method_1558();
            (new class_2661(class_2561.method_30163("Self kick"))).method_11467(mc.method_1562());
            class_412.method_36877(new class_442(), mc, class_639.method_2950(lastestServerEntry.field_3761), lastestServerEntry, false, (class_9112)null);
         }

      });
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
