package nekiplay.meteorplus.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2172;
import net.minecraft.class_2561;

public class ItemRawIdCommand extends Command {
   public ItemRawIdCommand() {
      super("rawitemid", "Get raw item id", new String[0]);
   }

   public void build(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         class_1799 itemStack = mc.field_1724.method_6047();
         if (itemStack != null) {
            int raw_id = class_1792.method_7880(itemStack.method_7909());
            ChatUtils.sendMsg(class_2561.method_30163("Raw Item ID: " + raw_id));
         }

         return 1;
      });
   }
}
