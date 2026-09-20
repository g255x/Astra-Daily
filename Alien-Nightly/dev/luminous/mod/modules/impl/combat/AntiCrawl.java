package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import net.minecraft.class_2246;
import net.minecraft.class_2338;

public class AntiCrawl extends Module {
   public static AntiCrawl INSTANCE;
   final double[] xzOffset = new double[]{(double)0.0F, 0.3, -0.3};
   private final EnumSetting<While> whileSetting;
   private final BooleanSetting web;
   public boolean work;

   public AntiCrawl() {
      super("AntiCrawl", Module.Category.Combat);
      this.whileSetting = this.add(new EnumSetting("While", AntiCrawl.While.Crawling));
      this.web = this.add(new BooleanSetting("Web", true));
      this.work = false;
      this.setChinese("反趴下");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.work = false;
      if (!mc.field_1724.method_6128() && (this.whileSetting.is(AntiCrawl.While.Always) && BlockUtil.getBlock(mc.field_1724.method_24515()) != class_2246.field_9987 || mc.field_1724.method_20448() || this.whileSetting.is(AntiCrawl.While.Mining) && Alien.BREAK.isMining(mc.field_1724.method_24515()))) {
         for(double offset : this.xzOffset) {
            for(double offset2 : this.xzOffset) {
               class_2338 pos = new BlockPosX(mc.field_1724.method_23317() + offset, mc.field_1724.method_23318() + 1.2, mc.field_1724.method_23321() + offset2);
               if (this.canBreak(pos)) {
                  SpeedMine.INSTANCE.mine(pos);
                  this.work = true;
                  return;
               }

               if (this.web.getValue()) {
                  class_2338 web = new BlockPosX(mc.field_1724.method_23317() + offset, mc.field_1724.method_23318(), mc.field_1724.method_23321() + offset2);
                  if (mc.field_1687.method_8320(web).method_26204() == class_2246.field_10343 && this.canBreak(web)) {
                     SpeedMine.INSTANCE.mine(web);
                     this.work = true;
                     return;
                  }
               }
            }
         }
      }

   }

   private boolean canBreak(class_2338 pos) {
      return (BlockUtil.getClickSideStrict(pos) != null || pos.equals(SpeedMine.getBreakPos())) && !SpeedMine.unbreakable(pos) && !mc.field_1687.method_22347(pos);
   }

   private static enum While {
      Crawling,
      Mining,
      Always;

      // $FF: synthetic method
      private static While[] $values() {
         return new While[]{Crawling, Mining, Always};
      }
   }
}
