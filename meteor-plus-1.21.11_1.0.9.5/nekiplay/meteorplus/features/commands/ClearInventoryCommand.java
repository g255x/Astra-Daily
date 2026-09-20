package nekiplay.meteorplus.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import nekiplay.meteorplus.features.modules.player.AutoDropPlus;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_2172;

public class ClearInventoryCommand extends Command {
   public ClearInventoryCommand() {
      super("clearinv", "Clear inventory", new String[0]);
   }

   public void build(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         for(int i = 0; i < mc.field_1724.method_31548().method_5439(); ++i) {
            class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
            if (itemStack != null) {
               mc.field_1761.method_2906(0, AutoDropPlus.invIndexToSlotId(i), 300, class_1713.field_7791, mc.field_1724);
            }
         }

         return 1;
      });
   }
}
