package nekiplay.meteorplus.features.commands;

import baritone.api.BaritoneAPI;
import baritone.api.command.manager.ICommandManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.List;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.utils.misc.Pool;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2397;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2680;

public class GotoPlusCommand extends Command {
   private final Pool<Cross> crossPool = new Pool(() -> new Cross());
   private final List<Cross> crosses = new ArrayList();

   public GotoPlusCommand() {
      super("gotoplus", "Baritone goto to selected type", new String[0]);
   }

   public void build(LiteralArgumentBuilder<class_2172> builder) {
      builder.then(literal("nolight").executes((c) -> {
         MeteorExecutor.execute(() -> {
            for(Cross cross : this.crosses) {
               this.crossPool.free(cross);
            }

            this.crosses.clear();
            BlockIterator.register(64, mc.field_1687.method_31605() / 2, (blockPos, blockState) -> {
               switch (BlockUtils.isValidMobSpawn(blockPos, mc.field_1687.method_8320(blockPos), 0)) {
                  case Potential:
                  case Always:
                     this.crosses.add(((Cross)this.crossPool.get()).set(blockPos));
                  case Never:
                  default:
               }
            });
            BlockIterator.after(() -> {
               class_2338 near = null;
               double dst = Double.MAX_VALUE;

               for(Cross cross : this.crosses) {
                  class_2680 ground = mc.field_1687.method_8320(new class_2338(cross.x, cross.y - 1, cross.z));
                  class_2248 ground_block = ground.method_26204();
                  if (!(ground_block instanceof class_2397) && ground_block != class_2246.field_10528) {
                     double dist = mc.field_1724.method_5707(new class_243((double)cross.x, (double)cross.y, (double)cross.z));
                     if (dist < dst) {
                        dst = dist;
                        near = new class_2338(cross.x, cross.y, cross.z);
                     }
                  }
               }

               for(Cross cross : this.crosses) {
                  this.crossPool.free(cross);
               }

               this.crosses.clear();
               if (near != null) {
                  ICommandManager var10000 = BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager();
                  int var10001 = near.method_10263();
                  var10000.execute("goto " + var10001 + " " + near.method_10264() + " " + near.method_10260());
               } else {
                  ChatUtils.sendMsg(class_2561.method_30163("Not founded near light"));
               }

            });
         });
         return 1;
      }));
   }

   private class Cross {
      private int x;
      private int y;
      private int z;

      public Cross set(class_2338 blockPos) {
         this.x = blockPos.method_10263();
         this.y = blockPos.method_10264();
         this.z = blockPos.method_10260();
         return this;
      }
   }

   public static enum Spawn {
      Never,
      Potential,
      Always;

      // $FF: synthetic method
      private static Spawn[] $values() {
         return new Spawn[]{Never, Potential, Always};
      }
   }
}
