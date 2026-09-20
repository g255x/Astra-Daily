package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2665;
import net.minecraft.class_2680;
import net.minecraft.class_3532;
import net.minecraft.class_476;
import net.minecraft.class_9334;

public class AutoEnderChest extends Module {
   public static AutoEnderChest INSTANCE;
   public final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   public final Timer timeoutTimer = new Timer();
   final int[] stealCountList = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
   private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
   private final SliderSetting disableTime = this.add(new SliderSetting("DisableTime", 500, 0, 1000));
   private final BooleanSetting place = this.add(new BooleanSetting("Place", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting preferOpen = this.add(new BooleanSetting("PreferOpen", true));
   private final BooleanSetting open = this.add(new BooleanSetting("Open", true));
   private final SliderSetting range = this.add(new SliderSetting("MaxRange", (double)4.0F, (double)0.0F, (double)6.0F, 0.1));
   private final SliderSetting minRange = this.add(new SliderSetting("MinRange", (double)1.0F, (double)0.0F, (double)3.0F, 0.1));
   private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true));
   private final BooleanSetting take = this.add(new BooleanSetting("Take", true));
   private final BooleanSetting smart;
   private final BooleanSetting forceMove;
   private final SliderSetting crystal;
   private final SliderSetting exp;
   private final SliderSetting totem;
   private final SliderSetting gapple;
   private final SliderSetting obsidian;
   private final SliderSetting web;
   private final SliderSetting glowstone;
   private final SliderSetting anchor;
   private final SliderSetting pearl;
   private final SliderSetting piston;
   private final SliderSetting redstone;
   private final SliderSetting bed;
   private final SliderSetting speed;
   private final SliderSetting resistance;
   private final SliderSetting strength;
   private final BindSetting placeKey;
   private final Timer timer;
   private final List<class_2338> openList;
   public class_2338 placePos;
   private class_2338 openPos;
   private boolean opend;
   private boolean on;

   public AutoEnderChest() {
      super("AutoEnderChest", Module.Category.Combat);
      BooleanSetting var10006 = this.take;
      Objects.requireNonNull(var10006);
      this.smart = this.add((new BooleanSetting("Smart", true, var10006::getValue)).setParent());
      this.forceMove = this.add(new BooleanSetting("ForceQuickMove", true, () -> this.take.getValue() && this.smart.isOpen()));
      this.crystal = this.add(new SliderSetting("Crystal", 256, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.exp = this.add(new SliderSetting("Exp", 256, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.totem = this.add(new SliderSetting("Totem", 6, 0, 36, () -> this.take.getValue() && this.smart.isOpen()));
      this.gapple = this.add(new SliderSetting("Gapple", 128, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.obsidian = this.add(new SliderSetting("Obsidian", 64, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.web = this.add(new SliderSetting("Web", 64, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.glowstone = this.add(new SliderSetting("Glowstone", 128, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.anchor = this.add(new SliderSetting("Anchor", 128, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.pearl = this.add(new SliderSetting("Pearl", 16, 0, 64, () -> this.take.getValue() && this.smart.isOpen()));
      this.piston = this.add(new SliderSetting("Piston", 64, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.redstone = this.add(new SliderSetting("RedStone", 64, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.bed = this.add(new SliderSetting("Bed", 256, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
      this.speed = this.add(new SliderSetting("Speed", 1, 0, 8, () -> this.take.getValue() && this.smart.isOpen()));
      this.resistance = this.add(new SliderSetting("Resistance", 1, 0, 8, () -> this.take.getValue() && this.smart.isOpen()));
      this.strength = this.add(new SliderSetting("Strength", 1, 0, 8, () -> this.take.getValue() && this.smart.isOpen()));
      this.placeKey = this.add(new BindSetting("PlaceKey", -1));
      this.timer = new Timer();
      this.openList = new ArrayList();
      this.placePos = null;
      this.opend = false;
      this.on = false;
      this.setChinese("自动末影箱补给");
      INSTANCE = this;
   }

   public int findEnderChest() {
      if (this.inventory.getValue()) {
         for(int i = 0; i < 36; ++i) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960() && stack.method_7909() == class_1802.field_8466) {
               return i < 9 ? i + 36 : i;
            }
         }

         return -1;
      } else {
         return InventoryUtil.findItem(class_1802.field_8466);
      }
   }

   public boolean onEnable() {
      this.opend = false;
      this.openPos = null;
      this.timeoutTimer.reset();
      this.placePos = null;
      if (!nullCheck() && this.place.getValue()) {
         this.doPlace();
      }

      return false;
   }

   private void doPlace() {
      int oldSlot = mc.field_1724.method_31548().field_7545;
      double distance = (double)100.0F;
      class_2338 bestPos = null;

      for(class_2338 pos : BlockUtil.getSphere((float)this.range.getValue())) {
         if (mc.field_1687.method_22347(pos.method_10084())) {
            if (this.preferOpen.getValue() && mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10443) {
               return;
            }

            class_2338 belowPos = pos.method_10093(class_2350.field_11033);
            class_2680 belowState = mc.field_1687.method_8320(belowPos);
            if (!((double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558())) < this.minRange.getValue()) && BlockUtil.clientCanPlace(pos, false) && BlockUtil.isStrictDirection(belowPos, class_2350.field_11036) && BlockUtil.canClick(belowPos) && !belowState.method_26215() && belowState.method_26212(mc.field_1687, belowPos) && (bestPos == null || (double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558())) < distance)) {
               distance = (double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558()));
               bestPos = pos;
            }
         }
      }

      if (bestPos != null) {
         if (this.findEnderChest() == -1) {
            this.sendMessage("§4No ender chest found.");
            return;
         }

         if (this.inventory.getValue()) {
            int slot = this.findEnderChest();
            InventoryUtil.inventorySwap(slot, oldSlot);
            this.placeBlock(bestPos);
            this.placePos = bestPos;
            InventoryUtil.inventorySwap(slot, oldSlot);
         } else {
            InventoryUtil.switchToSlot(this.findEnderChest());
            this.placeBlock(bestPos);
            this.placePos = bestPos;
            InventoryUtil.switchToSlot(oldSlot);
         }

         this.timer.reset();
      } else {
         this.sendMessage("§4No place position found.");
      }

   }

   private void update() {
      this.stealCountList[0] = (int)(this.crystal.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8301));
      this.stealCountList[1] = (int)(this.exp.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8287));
      this.stealCountList[2] = (int)(this.totem.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8288));
      this.stealCountList[3] = (int)(this.gapple.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8367));
      this.stealCountList[4] = (int)(this.obsidian.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10540.method_8389()));
      this.stealCountList[5] = (int)(this.web.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10343.method_8389()));
      this.stealCountList[6] = (int)(this.glowstone.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10171.method_8389()));
      this.stealCountList[7] = (int)(this.anchor.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_23152.method_8389()));
      this.stealCountList[8] = (int)(this.pearl.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8634));
      this.stealCountList[9] = (int)(this.piston.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10560.method_8389()) - (double)InventoryUtil.getItemCount(class_2246.field_10615.method_8389()));
      this.stealCountList[10] = (int)(this.redstone.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10002.method_8389()));
      this.stealCountList[11] = (int)(this.bed.getValue() - (double)InventoryUtil.getItemCount(class_2244.class));
      this.stealCountList[12] = (int)(this.speed.getValue() - (double)InventoryUtil.getPotionCount((class_1291)class_1294.field_5904.comp_349()));
      this.stealCountList[13] = (int)(this.resistance.getValue() - (double)InventoryUtil.getPotionCount((class_1291)class_1294.field_5907.comp_349()));
      this.stealCountList[14] = (int)(this.strength.getValue() - (double)InventoryUtil.getPotionCount((class_1291)class_1294.field_5910.comp_349()));
   }

   public void onDisable() {
      this.opend = false;
      if (this.mine.getValue() && this.placePos != null) {
         SpeedMine.INSTANCE.mine(this.placePos);
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.smart.getValue()) {
         this.update();
      }

      if (this.placeKey.isPressed() && mc.field_1755 == null) {
         if (!this.on) {
            this.opend = false;
            this.openPos = null;
            this.timeoutTimer.reset();
            this.placePos = null;
            this.doPlace();
         }

         this.on = true;
      } else {
         this.on = false;
      }

      this.openList.removeIf((pos) -> mc.field_1687.method_8320(pos).method_26204() != class_2246.field_10443);
      if (!(mc.field_1755 instanceof class_476)) {
         if (this.opend) {
            this.opend = false;
            if (this.autoDisable.getValue()) {
               this.timeoutToDisable();
            }

            if (this.mine.getValue() && this.openPos != null) {
               if (mc.field_1687.method_8320(this.openPos).method_26204() == class_2246.field_10443) {
                  SpeedMine.INSTANCE.mine(this.openPos);
               } else {
                  this.openPos = null;
               }
            }
         } else if (this.open.getValue()) {
            if (this.placePos == null || !((double)class_3532.method_15355((float)mc.field_1724.method_5707(this.placePos.method_46558())) <= this.range.getValue()) || !mc.field_1687.method_22347(this.placePos.method_10084()) || this.timer.passed(500L) && mc.field_1687.method_8320(this.placePos).method_26204() != class_2246.field_10443) {
               boolean found = false;

               for(class_2338 pos2 : BlockUtil.getSphere((float)this.range.getValue())) {
                  if (!this.openList.contains(pos2) && (mc.field_1687.method_22347(pos2.method_10084()) || BlockUtil.canReplace(pos2.method_10084())) && mc.field_1687.method_8320(pos2).method_26204() == class_2246.field_10443) {
                     this.openPos = pos2;
                     BlockUtil.clickBlock(pos2, BlockUtil.getClickSide(pos2), this.rotate.getValue());
                     found = true;
                     break;
                  }
               }

               if (!found && this.autoDisable.getValue()) {
                  this.timeoutToDisable();
               }
            } else if (mc.field_1687.method_8320(this.placePos).method_26204() == class_2246.field_10443) {
               this.openPos = this.placePos;
               BlockUtil.clickBlock(this.placePos, BlockUtil.getClickSide(this.placePos), this.rotate.getValue());
            }
         } else if (!this.take.getValue() && this.autoDisable.getValue()) {
            this.timeoutToDisable();
         }
      } else {
         this.opend = true;
         if (this.openPos != null) {
            this.openList.add(this.openPos);
         }

         if (!this.take.getValue()) {
            if (this.autoDisable.getValue()) {
               this.timeoutToDisable();
            }
         } else {
            boolean take = false;
            class_1703 screenHandler = mc.field_1724.field_7512;
            if (screenHandler instanceof class_1707) {
               class_1707 container = (class_1707)screenHandler;

               for(class_1735 slot : container.field_7761) {
                  if (slot.field_7874 < 27 && !slot.method_7677().method_7960()) {
                     Type type = this.needSteal(slot.method_7677());
                     if (this.smart.getValue() && type != AutoEnderChest.Type.QuickMove && (type != AutoEnderChest.Type.Stack || !this.forceMove.getValue())) {
                        if (type == AutoEnderChest.Type.Stack) {
                           for(int slot1 = 0; slot1 < 36; ++slot1) {
                              class_1799 stack = mc.field_1724.method_31548().method_5438(slot1);
                              if (!stack.method_7960() && stack.method_7946() && stack.method_7909() == slot.method_7677().method_7909() && stack.method_7947() < stack.method_7914()) {
                                 int i = (slot1 < 9 ? slot1 + 36 : slot1) + 18;
                                 mc.field_1761.method_2906(container.field_7763, slot.field_7874, 0, class_1713.field_7790, mc.field_1724);
                                 mc.field_1761.method_2906(container.field_7763, i, 0, class_1713.field_7790, mc.field_1724);
                                 mc.field_1761.method_2906(container.field_7763, slot.field_7874, 0, class_1713.field_7790, mc.field_1724);
                                 take = true;
                                 break;
                              }
                           }
                        }
                     } else {
                        mc.field_1761.method_2906(container.field_7763, slot.field_7874, 0, class_1713.field_7794, mc.field_1724);
                        take = true;
                     }
                  }
               }
            }

            if (this.autoDisable.getValue() && !take) {
               this.timeoutToDisable();
            }
         }
      }

   }

   private void timeoutToDisable() {
      if (this.timeoutTimer.passed((long)this.disableTime.getValueInt())) {
         this.disable();
      }

   }

   private Type needSteal(class_1799 i) {
      if (i.method_7909().equals(class_1802.field_8301) && this.stealCountList[0] > 0) {
         this.stealCountList[0] -= i.method_7947();
         return this.stealCountList[0] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909().equals(class_1802.field_8287) && this.stealCountList[1] > 0) {
         this.stealCountList[1] -= i.method_7947();
         return this.stealCountList[1] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909().equals(class_1802.field_8288) && this.stealCountList[2] > 0) {
         this.stealCountList[2] -= i.method_7947();
         return this.stealCountList[2] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909().equals(class_1802.field_8367) && this.stealCountList[3] > 0) {
         this.stealCountList[3] -= i.method_7947();
         return this.stealCountList[3] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_10540.method_8389()) && this.stealCountList[4] > 0) {
         this.stealCountList[4] -= i.method_7947();
         return this.stealCountList[4] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_10343.method_8389()) && this.stealCountList[5] > 0) {
         this.stealCountList[5] -= i.method_7947();
         return this.stealCountList[5] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_10171.method_8389()) && this.stealCountList[6] > 0) {
         this.stealCountList[6] -= i.method_7947();
         return this.stealCountList[6] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_23152.method_8389()) && this.stealCountList[7] > 0) {
         this.stealCountList[7] -= i.method_7947();
         return this.stealCountList[7] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909().equals(class_1802.field_8634) && this.stealCountList[8] > 0) {
         this.stealCountList[8] -= i.method_7947();
         return this.stealCountList[8] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2665 && this.stealCountList[9] > 0) {
         this.stealCountList[9] -= i.method_7947();
         return this.stealCountList[9] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_10002.method_8389()) && this.stealCountList[10] > 0) {
         this.stealCountList[10] -= i.method_7947();
         return this.stealCountList[10] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2244 && this.stealCountList[11] > 0) {
         this.stealCountList[11] -= i.method_7947();
         return this.stealCountList[11] < 0 ? AutoEnderChest.Type.Stack : AutoEnderChest.Type.QuickMove;
      } else {
         if (class_1792.method_7880(i.method_7909()) == class_1792.method_7880(class_1802.field_8436)) {
            class_1844 potionContentsComponent = (class_1844)i.method_57825(class_9334.field_49651, class_1844.field_49274);

            for(class_1293 effect : potionContentsComponent.method_57397()) {
               if (effect.method_5579().comp_349() == class_1294.field_5904.comp_349()) {
                  if (this.stealCountList[12] > 0) {
                     this.stealCountList[12] -= i.method_7947();
                     if (this.stealCountList[12] < 0) {
                        return AutoEnderChest.Type.Stack;
                     }

                     return AutoEnderChest.Type.QuickMove;
                  }
               } else if (effect.method_5579().comp_349() == class_1294.field_5907.comp_349()) {
                  if (this.stealCountList[13] > 0) {
                     this.stealCountList[13] -= i.method_7947();
                     if (this.stealCountList[13] < 0) {
                        return AutoEnderChest.Type.Stack;
                     }

                     return AutoEnderChest.Type.QuickMove;
                  }
               } else if (effect.method_5579().comp_349() == class_1294.field_5910.comp_349() && this.stealCountList[14] > 0) {
                  this.stealCountList[14] -= i.method_7947();
                  if (this.stealCountList[14] < 0) {
                     return AutoEnderChest.Type.Stack;
                  }

                  return AutoEnderChest.Type.QuickMove;
               }
            }
         }

         return AutoEnderChest.Type.None;
      }
   }

   private void placeBlock(class_2338 pos) {
      AntiRegear.INSTANCE.safe.add(pos);
      BlockUtil.clickBlock(pos.method_10093(class_2350.field_11033), class_2350.field_11036, this.rotate.getValue());
   }

   private static enum Type {
      None,
      Stack,
      QuickMove;

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{None, Stack, QuickMove};
      }
   }
}
