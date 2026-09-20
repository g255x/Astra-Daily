package nekiplay.meteorplus.features.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.class_2172;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_239.class_240;

public class BlockRawIdCommand extends Command {
   public BlockRawIdCommand() {
      super("rawblockid", "Get raw block id under mouse", new String[0]);
   }

   public void build(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes((context) -> {
         if (mc.field_1765 != null && mc.field_1765.method_17783() == class_240.field_1332) {
            class_2338 pos = new class_2338((int)mc.field_1765.method_17784().field_1352, (int)mc.field_1765.method_17784().field_1351, (int)mc.field_1765.method_17784().field_1350);
            class_2680 state = mc.field_1687.method_8320(pos);
            int raw_id = class_2248.method_9507(state);
            this.info(String.valueOf(raw_id), new Object[0]);
         }

         return 1;
      });
   }
}
