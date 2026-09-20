package nekiplay.meteorplus.features.modules.movement.spider.modes;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.misc.Names;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import nekiplay.meteorplus.features.modules.movement.spider.SpiderMode;
import nekiplay.meteorplus.features.modules.movement.spider.SpiderModes;
import nekiplay.meteorplus.utils.ElytraUtils;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2828;
import net.minecraft.class_746;

public class Eclip extends SpiderMode {
   private int ticks = 0;
   private int slot = -1;
   private double blocks = (double)0.0F;

   public Eclip() {
      super(SpiderModes.Elytra_clip);
   }

   public void onActivate() {
      FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
      if (!elytra.found()) {
         this.settings.error(Names.get(class_1802.field_8833) + " not found", new Object[0]);
         this.settings.toggle();
      }

   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (this.work() && this.mc.field_1724.field_5976) {
         this.blocks = (double)(Integer)this.settings.Blocks.get();
         this.clip();
      } else {
         this.ticks = 0;
      }

   }

   private boolean work() {
      class_746 player = this.mc.field_1724;

      assert player != null;

      FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
      return elytra.found();
   }

   private void clip() {
      if (this.blocks != (double)0.0F) {
         class_746 player = this.mc.field_1724;

         assert player != null;

         switch (this.ticks) {
            case 0:
               FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
               this.slot = elytra.slot();
               InvUtils.move().from(this.slot).toArmor(2);
               ++this.ticks;
            case 1:
               this.mc.field_1724.field_3944.method_52787(new class_2828.class_5911(false, this.mc.field_1724.field_5976));
               ++this.ticks;
            case 2:
               this.mc.field_1724.field_3944.method_52787(new class_2828.class_5911(false, this.mc.field_1724.field_5976));
               ++this.ticks;
            case 3:
               ElytraUtils.startFly();
               ++this.ticks;
            case 4:
               player.method_5814(player.method_23317(), player.method_23318() + this.blocks, player.method_23321());
               this.mc.field_1724.field_3944.method_52787(new class_2828.class_2829(player.method_23317(), player.method_23318() + this.blocks, player.method_23321(), false, this.mc.field_1724.field_5976));
               ++this.ticks;
            case 5:
               ElytraUtils.startFly();
               ++this.ticks;
            case 6:
               this.ticks = 0;
               this.blocks = (double)0.0F;
               InvUtils.move().fromArmor(2).to(this.slot);
         }
      }

   }
}
