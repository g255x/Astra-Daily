package dev.Astra.mod.modules.impl.combat;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.player.PacketMine;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_1810;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;

public class Breaker extends Module {
   public static Breaker INSTANCE;
   public final SliderSetting targetRange = this.add((new SliderSetting("TargetRange", (double)6.0F, (double)0.0F, (double)8.0F, 0.1)).setSuffix("m"));
   public final SliderSetting range = this.add((new SliderSetting("Range", (double)4.5F, (double)0.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final BooleanSetting burrow = this.add(new BooleanSetting("Burrow", true));
   private final BooleanSetting head = this.add(new BooleanSetting("Head", false));
   private final BooleanSetting face = this.add(new BooleanSetting("Face", false));
   private final BooleanSetting down = this.add(new BooleanSetting("Down", false));
   private final BooleanSetting surround = this.add(new BooleanSetting("Surround", true));
   private final BooleanSetting cevPause = this.add(new BooleanSetting("CevPause", false));
   public final BooleanSetting avoidSelf = this.add(new BooleanSetting("AvoidSelf", true));
   private final BooleanSetting forceDouble = this.add(new BooleanSetting("ForceDouble", false));
   public static final List<class_2248> hard;

   public Breaker() {
      super("Breaker", Module.Category.Combat);
      this.setChinese("自动挖掘");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!CevBreaker.INSTANCE.isOn() || !this.cevPause.getValue()) {
         if (!AntiCrawl.INSTANCE.work) {
            if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
               class_1657 player = CombatUtil.getClosestEnemy(this.targetRange.getValue());
               if (player != null) {
                  this.doBreak(player);
               }
            }
         }
      }
   }

   private void doBreak(class_1657 player) {
      class_2338 pos = EntityUtil.getEntityPos(player, true);
      boolean avoid = this.avoidSelf.getValue();
      if (!this.forceDouble.getValue() || PacketMine.getBreakPos() == null || PacketMine.getBreakPos().equals(PacketMine.secondPos) || PacketMine.secondPos == null || mc.field_1687.method_22347(PacketMine.secondPos)) {
         double[] dArray = new double[]{-0.8, 0.3, 1.1};
         double[] xzOffset = new double[]{0.3, -0.3};

         for(class_1657 playerEntity : CombatUtil.getEnemies(this.targetRange.getValue())) {
            for(double y : dArray) {
               for(double x : xzOffset) {
                  for(double z : xzOffset) {
                     BlockPosX offsetPos = new BlockPosX(playerEntity.method_23317() + x, playerEntity.method_23318() + y, playerEntity.method_23321() + z);
                     if (this.canBreak(offsetPos, Math.abs(y - 0.3) < 0.01) && (offsetPos.equals(PacketMine.getBreakPos()) || offsetPos.equals(PacketMine.secondPos))) {
                        return;
                     }
                  }
               }
            }
         }

         ArrayList<Float> yList = new ArrayList();
         if (this.down.getValue()) {
            yList.add(-0.8F);
         }

         if (this.head.getValue()) {
            yList.add(2.3F);
         }

         if (this.burrow.getValue()) {
            yList.add(0.3F);
         }

         if (this.face.getValue()) {
            yList.add(1.1F);
         }

         Iterator candidates = yList.iterator();

         while(candidates.hasNext()) {
            double y = (double)(Float)candidates.next();
            boolean isBurrow = Math.abs(y - 0.3) < 0.01;

            for(double offset : xzOffset) {
               BlockPosX offsetPos = new BlockPosX(player.method_23317() + offset, player.method_23318() + y, player.method_23321() + offset);
               if (this.canBreak(offsetPos, isBurrow) && (!avoid || !this.isSelfBlock(offsetPos))) {
                  PacketMine.INSTANCE.mine(offsetPos);
                  return;
               }
            }
         }

         candidates = yList.iterator();

         while(candidates.hasNext()) {
            double y = (double)(Float)candidates.next();
            boolean isBurrow = Math.abs(y - 0.3) < 0.01;

            for(double offset : xzOffset) {
               for(double offset2 : xzOffset) {
                  BlockPosX offsetPos = new BlockPosX(player.method_23317() + offset2, player.method_23318() + y, player.method_23321() + offset);
                  if (this.canBreak(offsetPos, isBurrow) && (!avoid || !this.isSelfBlock(offsetPos))) {
                     PacketMine.INSTANCE.mine(offsetPos);
                     return;
                  }
               }
            }
         }

         if (this.surround.getValue()) {
            for(class_2350 direction : class_2350.values()) {
               if (direction != class_2350.field_11036 && direction != class_2350.field_11033) {
                  class_2338 surroundPos = pos.method_10093(direction);
                  if (!(Math.sqrt(mc.field_1724.method_33571().method_1025(surroundPos.method_46558())) > this.range.getValue()) && (mc.field_1687.method_22347(surroundPos) || surroundPos.equals(PacketMine.getBreakPos()) || surroundPos.equals(PacketMine.secondPos)) && this.canPlaceCrystal(surroundPos, false)) {
                     return;
                  }
               }
            }

            ArrayList<class_2338> candidates = new ArrayList();

            for(class_2350 direction : class_2350.values()) {
               if (direction != class_2350.field_11036 && direction != class_2350.field_11033) {
                  class_2338 checkPos = pos.method_10093(direction);
                  if (!(Math.sqrt(mc.field_1724.method_33571().method_1025(checkPos.method_46558())) > this.range.getValue()) && this.canBreak(checkPos, false) && this.canPlaceCrystal(checkPos, true) && !this.isSurroundPos(checkPos) && (!avoid || !this.isSelfBlock(checkPos))) {
                     candidates.add(checkPos);
                  }
               }
            }

            if (!candidates.isEmpty()) {
               PacketMine.INSTANCE.mine((class_2338)candidates.stream().min(Comparator.comparingDouble((e) -> e.method_19770(mc.field_1724.method_33571()))).get());
            } else {
               candidates.clear();

               for(class_2350 direction : class_2350.values()) {
                  if (direction != class_2350.field_11036 && direction != class_2350.field_11033) {
                     class_2338 checkPos = pos.method_10093(direction);
                     if (!(Math.sqrt(mc.field_1724.method_33571().method_1025(checkPos.method_46558())) > this.range.getValue()) && this.canBreak(checkPos, false) && this.canPlaceCrystal(checkPos, false) && (!avoid || !this.isSelfBlock(checkPos))) {
                        candidates.add(checkPos);
                     }
                  }
               }

               if (!candidates.isEmpty()) {
                  PacketMine.INSTANCE.mine((class_2338)candidates.stream().min(Comparator.comparingDouble((e) -> e.method_19770(mc.field_1724.method_33571()))).get());
               }
            }
         }

      }
   }

   private boolean isSelfBlock(class_2338 pos) {
      class_2338 selfPos = EntityUtil.getPlayerPos(true);
      if (pos.equals(selfPos)) {
         return true;
      } else if (pos.equals(selfPos.method_10074())) {
         return true;
      } else {
         for(class_2350 dir : class_2350.values()) {
            if (dir != class_2350.field_11036 && dir != class_2350.field_11033 && pos.equals(selfPos.method_10093(dir))) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean isSurroundPos(class_2338 pos) {
      for(class_2350 i : class_2350.values()) {
         if (i != class_2350.field_11036 && i != class_2350.field_11033) {
            class_2338 self = EntityUtil.getPlayerPos(true);
            if (self.method_10093(i).equals(pos)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean canPlaceCrystal(class_2338 pos, boolean block) {
      class_2338 obsPos = pos.method_10074();
      class_2338 boost = obsPos.method_10084();
      return (BlockUtil.getBlock(obsPos) == class_2246.field_9987 || BlockUtil.getBlock(obsPos) == class_2246.field_10540 || !block) && BlockUtil.noEntityBlockCrystal(boost, true, true) && BlockUtil.noEntityBlockCrystal(boost.method_10084(), true, true);
   }

   private boolean canBreak(class_2338 pos, boolean isBurrow) {
      if (mc.field_1724.method_33571().method_1022(pos.method_46558()) > PacketMine.INSTANCE.range.getValue()) {
         return false;
      } else if (!pos.equals(PacketMine.getBreakPos()) && !pos.equals(PacketMine.secondPos) && BlockUtil.getClickSideStrict(pos) == null) {
         return false;
      } else {
         class_2248 block = BlockUtil.getBlock(pos);
         if (block != null && block != class_2246.field_9987 && block != class_2246.field_10499 && !mc.field_1687.method_22347(pos) && !mc.field_1687.method_8320(pos).method_51176()) {
            if (isBurrow && !hard.contains(block)) {
               return false;
            } else {
               return !hard.contains(block) || mc.field_1724.method_6047().method_7909() instanceof class_1810 || PacketMine.INSTANCE.autoSwitch.getValue();
            }
         } else {
            return false;
         }
      }
   }

   static {
      hard = Arrays.asList(class_2246.field_10540, class_2246.field_10443, class_2246.field_22108, class_2246.field_22423, class_2246.field_23152, class_2246.field_22109, class_2246.field_10535);
   }
}
