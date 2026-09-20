package dev.Astra.mod.commands.impl;

import dev.Astra.mod.commands.Command;
import dev.Astra.mod.modules.impl.misc.ShulkerViewer;
import java.util.List;
import net.minecraft.class_1799;

public class PeekCommand extends Command {
   private static final class_1799[] ITEMS = new class_1799[27];

   public PeekCommand() {
      super("peek", "");
   }

   public void runCommand(String[] parameters) {
      ShulkerViewer.openContainer(mc.field_1724.method_6047(), ITEMS, true);
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }
}
