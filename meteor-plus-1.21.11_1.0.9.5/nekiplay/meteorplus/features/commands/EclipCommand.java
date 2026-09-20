package nekiplay.meteorplus.features.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.misc.Names;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.utils.ElytraUtils;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2828;
import net.minecraft.class_746;

public class EclipCommand extends Command {
   private int ticks = 0;
   private int slot = -1;
   private double blocks = (double)0.0F;

   public EclipCommand() {
      super("eclip", "Elyta clip need elytra bypass most anticheats", new String[0]);
   }

   public void build(LiteralArgumentBuilder<class_2172> builder) {
      builder.then(argument("blocks", DoubleArgumentType.doubleArg()).executes((context) -> {
         class_746 player = mc.field_1724;

         assert player != null;

         double blocks2 = (Double)context.getArgument("blocks", Double.class);
         if (this.work()) {
            this.blocks = blocks2;
            MeteorClient.EVENT_BUS.subscribe(this);
         } else {
            this.ticks = 0;
         }

         return 1;
      }));
      builder.then(literal("up").executes((c) -> {
         if (this.work()) {
            this.blocks = this.findBlock(true, 15);
            MeteorClient.EVENT_BUS.subscribe(this);
         } else {
            this.ticks = 0;
         }

         return 1;
      }));
      builder.then(literal("down").executes((c) -> {
         if (this.work()) {
            this.blocks = this.findBlock(false, 15);
            MeteorClient.EVENT_BUS.subscribe(this);
         } else {
            this.ticks = 0;
         }

         return 1;
      }));
   }

   private boolean work() {
      class_746 player = mc.field_1724;

      assert player != null;

      FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
      if (elytra.found()) {
         this.ticks = 0;
         return true;
      } else {
         this.error(Names.get(class_1802.field_8833) + " not found", new Object[0]);
         return false;
      }
   }

   private class_2248 getBlock(class_2338 pos) {
      return mc.field_1687.method_8320(pos).method_26204();
   }

   private double findBlock(boolean up, int maximum) {
      if (up) {
         class_2338 pos = mc.field_1724.method_24515();

         for(int i = maximum; i >= 0; --i) {
            if (this.getBlock(pos.method_10069(0, i, 0)) == class_2246.field_10124 && this.getBlock(pos.method_10069(0, i + 1, 0)) == class_2246.field_10124 && this.getBlock(pos.method_10069(0, i - 1, 0)) != class_2246.field_10124) {
               return (double)i;
            }
         }
      } else {
         class_2338 pos = mc.field_1724.method_24515();

         for(int i = -maximum; i <= 0; ++i) {
            if (this.getBlock(pos.method_10069(0, i, 0)) == class_2246.field_10124 && this.getBlock(pos.method_10069(0, i + 1, 0)) == class_2246.field_10124 && this.getBlock(pos.method_10069(0, i - 1, 0)) != class_2246.field_10124) {
               return (double)i;
            }
         }
      }

      return (double)0.0F;
   }

   @EventHandler
   private void onTick(TickEvent.Pre event) {
      this.clip(this.blocks);
   }

   private void clip(double blocks) {
      if (blocks != (double)0.0F) {
         class_746 player = mc.field_1724;

         assert player != null;

         switch (this.ticks) {
            case 0:
               FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
               this.slot = elytra.slot();
               InvUtils.move().from(this.slot).toArmor(2);
               ++this.ticks;
            case 1:
               mc.field_1724.field_3944.method_52787(new class_2828.class_5911(false, mc.field_1724.field_5976));
               ++this.ticks;
            case 2:
               mc.field_1724.field_3944.method_52787(new class_2828.class_5911(false, mc.field_1724.field_5976));
               ++this.ticks;
            case 3:
               ElytraUtils.startFly();
               ++this.ticks;
            case 4:
               player.method_5814(player.method_23317(), player.method_23318() + blocks, player.method_23321());
               mc.field_1724.field_3944.method_52787(new class_2828.class_2829(player.method_23317(), player.method_23318() + blocks, player.method_23321(), false, mc.field_1724.field_5976));
               ++this.ticks;
            case 5:
               ElytraUtils.startFly();
               ++this.ticks;
            case 6:
               this.ticks = 0;
               InvUtils.move().fromArmor(2).to(this.slot);
               MeteorClient.EVENT_BUS.unsubscribe(this);
         }
      } else {
         MeteorClient.EVENT_BUS.unsubscribe(this);
      }

   }
}
