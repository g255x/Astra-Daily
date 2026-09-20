package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.impl.combat.AutoMine;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_3532;

public class HoleManager implements Wrapper {
   public boolean isHole(class_2338 pos) {
      return this.isHole(pos, true, false, false);
   }

   public boolean isHole(class_2338 pos, boolean canStand, boolean checkTrap, boolean anyBlock) {
      int blockProgress = 0;

      for(class_2350 i : class_2350.values()) {
         if (i != class_2350.field_11036 && i != class_2350.field_11033 && (anyBlock && !mc.field_1687.method_22347(pos.method_10093(i)) || Alien.HOLE.isHard(pos.method_10093(i)))) {
            ++blockProgress;
         }
      }

      return (!checkTrap || mc.field_1687.method_22347(pos) && mc.field_1687.method_22347(pos.method_10084()) && mc.field_1687.method_22347(pos.method_10086(1)) && mc.field_1687.method_22347(pos.method_10086(2)) && (mc.field_1724.method_31478() - 1 <= pos.method_10264() || mc.field_1687.method_22347(pos.method_10086(3))) && (mc.field_1724.method_31478() - 2 <= pos.method_10264() || mc.field_1687.method_22347(pos.method_10086(4)))) && blockProgress > 3 && (!canStand || BlockUtil.canCollide(new class_238(pos.method_10069(0, -1, 0))));
   }

   public class_2338 getHole(float range, boolean doubleHole, boolean any, boolean up) {
      class_2338 bestPos = null;
      double bestDistance = (double)(range + 1.0F);

      for(class_2338 pos : BlockUtil.getSphere(range, mc.field_1724.method_19538())) {
         if ((pos.method_10263() == mc.field_1724.method_31477() && pos.method_10260() == mc.field_1724.method_31479() || up || !((double)(pos.method_10264() + 1) > mc.field_1724.method_23318())) && (Alien.HOLE.isHole(pos, true, true, any) || doubleHole && this.isDoubleHole(pos)) && pos.method_10264() - mc.field_1724.method_31478() <= 1) {
            double distance = (double)class_3532.method_15355((float)mc.field_1724.method_5649((double)pos.method_10263() + (double)0.5F, (double)pos.method_10264() + (double)0.5F, (double)pos.method_10260() + (double)0.5F));
            if (bestPos == null || distance < bestDistance) {
               bestPos = pos;
               bestDistance = distance;
            }
         }
      }

      return bestPos;
   }

   public boolean isDoubleHole(class_2338 pos) {
      class_2350 unHardFacing = this.is3Block(pos);
      if (unHardFacing != null) {
         pos = pos.method_10093(unHardFacing);
         unHardFacing = this.is3Block(pos);
         return unHardFacing != null;
      } else {
         return false;
      }
   }

   public class_2350 is3Block(class_2338 pos) {
      if (!this.isHard(pos.method_10074())) {
         return null;
      } else if (mc.field_1687.method_22347(pos) && mc.field_1687.method_22347(pos.method_10084()) && mc.field_1687.method_22347(pos.method_10086(2))) {
         int progress = 0;
         class_2350 unHardFacing = null;

         for(class_2350 facing : class_2350.values()) {
            if (facing != class_2350.field_11036 && facing != class_2350.field_11033) {
               if (this.isHard(pos.method_10093(facing))) {
                  ++progress;
               } else {
                  int progress2 = 0;

                  for(class_2350 facing2 : class_2350.values()) {
                     if (facing2 != class_2350.field_11033 && facing2 != facing.method_10153() && this.isHard(pos.method_10093(facing).method_10093(facing2))) {
                        ++progress2;
                     }
                  }

                  if (progress2 == 4) {
                     ++progress;
                  } else {
                     unHardFacing = facing;
                  }
               }
            }
         }

         return progress == 3 ? unHardFacing : null;
      } else {
         return null;
      }
   }

   public boolean isHard(class_2338 pos) {
      class_2248 block = mc.field_1687.method_8320(pos).method_26204();
      return this.isHard(block);
   }

   public boolean isHard(class_2248 block) {
      return block == class_2246.field_9987 || AutoMine.hard.contains(block);
   }
}
