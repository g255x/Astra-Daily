package dev.Astra.mod.modules.impl.misc;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.core.impl.CommandManager;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.combat.AntiRegear;
import dev.Astra.mod.modules.impl.player.PacketMine;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1733;
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
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2665;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_3532;
import net.minecraft.class_495;
import net.minecraft.class_9334;

public class AutoRegear extends Module {
   public static AutoRegear INSTANCE;
   public final EnumSetting<Page> page;
   private final BooleanSetting autoDisable;
   private final SliderSetting disableTime;
   private final BindSetting placeKey;
   private final BooleanSetting place;
   private final BooleanSetting open;
   private final BooleanSetting mine;
   private final BooleanSetting inventory;
   private final SliderSetting placeRange;
   private final SliderSetting mineRange;
   private final BooleanSetting detectMining;
   private final BooleanSetting rotate;
   private final BooleanSetting take;
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
   public final Timer timeoutTimer;
   private final int[] stealCountList;
   private final List<class_2338> openList;
   public class_2338 placePos;
   private class_2338 openPos;
   private boolean opend;
   private boolean on;
   private class_243 directionVec;

   public AutoRegear() {
      super("AutoRegear", Module.Category.Misc);
      this.page = this.add(new EnumSetting("Page", AutoRegear.Page.General));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", true, () -> this.page.is(AutoRegear.Page.General)));
      this.disableTime = this.add((new SliderSetting("DisableTime", (double)800.0F, (double)0.0F, (double)1000.0F, () -> this.page.is(AutoRegear.Page.General))).setSuffix("ms"));
      this.placeKey = this.add(new BindSetting("PlaceKey", -1, () -> this.page.is(AutoRegear.Page.General)));
      this.place = this.add(new BooleanSetting("Place", true, () -> this.page.is(AutoRegear.Page.General)));
      this.open = this.add(new BooleanSetting("Open", true, () -> this.page.is(AutoRegear.Page.General)));
      this.mine = this.add(new BooleanSetting("Mine", true, () -> this.page.is(AutoRegear.Page.General)));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(AutoRegear.Page.General)));
      this.placeRange = this.add((new SliderSetting("PlaceRange", (double)4.0F, (double)0.0F, (double)6.0F, 0.1, () -> this.page.is(AutoRegear.Page.General))).setSuffix("m"));
      this.mineRange = this.add((new SliderSetting("MineRange", (double)2.0F, (double)0.0F, (double)8.0F, 0.1, () -> this.page.is(AutoRegear.Page.General) && this.mine.getValue())).setSuffix("m"));
      this.detectMining = this.add(new BooleanSetting("DetectMining", true, () -> this.page.is(AutoRegear.Page.General)));
      this.rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.is(AutoRegear.Page.General)));
      this.take = this.add(new BooleanSetting("Take", true, () -> this.page.is(AutoRegear.Page.Take)));
      this.smart = this.add((new BooleanSetting("Smart", true, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue())).setParent());
      this.forceMove = this.add(new BooleanSetting("ForceQuickMove", true, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.crystal = this.add(new SliderSetting("Crystal", 64, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.exp = this.add(new SliderSetting("Exp", 128, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.totem = this.add(new SliderSetting("Totem", 8, 0, 36, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.gapple = this.add(new SliderSetting("Gapple", 64, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.obsidian = this.add(new SliderSetting("Obsidian", 128, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.web = this.add(new SliderSetting("Web", 0, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.glowstone = this.add(new SliderSetting("Glowstone", 64, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.anchor = this.add(new SliderSetting("Anchor", 64, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.pearl = this.add(new SliderSetting("Pearl", 32, 0, 64, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.piston = this.add(new SliderSetting("Piston", 64, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.redstone = this.add(new SliderSetting("RedStone", 64, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.bed = this.add(new SliderSetting("Bed", 0, 0, 512, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.speed = this.add(new SliderSetting("Speed", 0, 0, 8, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.resistance = this.add(new SliderSetting("Resistance", 8, 0, 8, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.strength = this.add(new SliderSetting("Strength", 2, 0, 8, () -> this.page.is(AutoRegear.Page.Take) && this.take.getValue() && this.smart.isOpen()));
      this.timeoutTimer = new Timer();
      this.stealCountList = new int[15];
      this.openList = new ArrayList();
      this.placePos = null;
      this.opend = false;
      this.on = false;
      this.directionVec = null;
      this.setChinese("自动补给");
      INSTANCE = this;
   }

   private int findShulker() {
      if (this.inventory.getValue()) {
         for(int i = 0; i < 36; ++i) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
               class_1792 item = stack.method_7909();
               if (item instanceof class_1747 && ((class_1747)item).method_7711() instanceof class_2480) {
                  return i;
               }
            }
         }
      } else {
         for(int i = 0; i < 9; ++i) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (!stack.method_7960()) {
               class_1792 item = stack.method_7909();
               if (item instanceof class_1747 && ((class_1747)item).method_7711() instanceof class_2480) {
                  return i;
               }
            }
         }
      }

      return -1;
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

   private boolean faceVector(class_243 vec) {
      Astra.ROTATION.lookAt(vec);
      return true;
   }

   private boolean canOpenShulker(class_2338 pos) {
      class_2680 state = mc.field_1687.method_8320(pos);
      if (!(state.method_26204() instanceof class_2480)) {
         return false;
      } else if (state.method_28498(class_2741.field_12525)) {
         class_2350 facing = (class_2350)state.method_11654(class_2741.field_12525);
         class_2338 openDirPos = pos.method_10093(facing);
         class_2680 openState = mc.field_1687.method_8320(openDirPos);
         return openState.method_26215() || openState.method_26204() == class_2246.field_10382;
      } else {
         return mc.field_1687.method_22347(pos.method_10084()) || mc.field_1687.method_8320(pos.method_10084()).method_26204() == class_2246.field_10382;
      }
   }

   private boolean tryOpenNearbyShulker() {
      if (!this.open.getValue()) {
         return false;
      } else if (this.placePos != null && mc.field_1724.method_5707(this.placePos.method_46558()) <= this.placeRange.getValue() * this.placeRange.getValue() && mc.field_1687.method_8320(this.placePos).method_26204() instanceof class_2480 && !this.openList.contains(this.placePos) && this.canOpenShulker(this.placePos)) {
         this.openPos = this.placePos;
         BlockUtil.clickBlock(this.placePos, BlockUtil.getClickSide(this.placePos), this.rotate.getValue());
         return true;
      } else {
         for(class_2338 pos : BlockUtil.getSphere((float)this.placeRange.getValue())) {
            if (!this.openList.contains(pos)) {
               class_2680 state = mc.field_1687.method_8320(pos);
               if (state.method_26204() instanceof class_2480 && this.canOpenShulker(pos)) {
                  this.openPos = pos;
                  BlockUtil.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue());
                  return true;
               }
            }
         }

         return false;
      }
   }

   private void tryMineOpenedShulkers() {
      if (this.mine.getValue()) {
         for(class_2338 pos : this.openList) {
            if (!(mc.field_1687.method_8320(pos).method_26204() instanceof class_2480)) {
               this.openList.remove(pos);
            } else {
               double dist = mc.field_1724.method_5707(pos.method_46558());
               if (dist <= this.mineRange.getValue() * this.mineRange.getValue() && (!this.detectMining.getValue() || !Astra.BREAK.isMining(pos))) {
                  PacketMine.INSTANCE.mine(pos);
               }
            }
         }

      }
   }

   private void doPlace() {
      class_2338 bestPos = null;
      class_2350 bestDir = null;
      double bestDistance = Double.MAX_VALUE;

      for(class_2338 pos : BlockUtil.getSphere((float)this.placeRange.getValue())) {
         double dist = (double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558()));
         if ((!this.detectMining.getValue() || !Astra.BREAK.isMining(pos)) && BlockUtil.canPlace(pos, this.placeRange.getValue(), true)) {
            class_238 box = new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)(pos.method_10264() + 1), (double)(pos.method_10260() + 1));
            if (mc.field_1687.method_8333((class_1297)null, box, (e) -> !(e instanceof class_1542)).isEmpty()) {
               class_2350 validDir = null;

               for(class_2350 dir : class_2350.values()) {
                  class_2338 solidPos = pos.method_10093(dir);
                  class_2338 airPos = pos.method_10093(dir.method_10153());
                  class_2680 solidState = mc.field_1687.method_8320(solidPos);
                  if (solidState.method_51367() && !(solidState.method_26204() instanceof class_2480)) {
                     class_2680 airState = mc.field_1687.method_8320(airPos);
                     if (airState.method_26215() || airState.method_26204() == class_2246.field_10382) {
                        validDir = dir;
                        break;
                     }
                  }
               }

               if (validDir != null && dist < bestDistance) {
                  bestDistance = dist;
                  bestPos = pos;
                  bestDir = validDir;
               }
            }
         }
      }

      if (bestPos == null) {
         CommandManager.sendMessageId("§4No suitable placement position found.", this.hashCode() - 1);
      } else {
         int shulkerSlot = this.findShulker();
         if (shulkerSlot == -1) {
            CommandManager.sendMessageId("§4No shulker box found.", this.hashCode() - 1);
         } else {
            int oldSlot = mc.field_1724.method_31548().field_7545;
            if (this.inventory.getValue()) {
               if (shulkerSlot < 9) {
                  InventoryUtil.switchToSlot(shulkerSlot);
               } else {
                  InventoryUtil.inventorySwap(shulkerSlot, oldSlot);
               }
            } else {
               InventoryUtil.switchToSlot(shulkerSlot);
            }

            class_1799 mainHand = mc.field_1724.method_31548().method_7391();
            if (mainHand.method_7909() instanceof class_1747 && ((class_1747)mainHand.method_7909()).method_7711() instanceof class_2480) {
               class_2338 targetBlock = bestPos.method_10093(bestDir);
               class_2350 clickFace = bestDir.method_10153();
               boolean packet = AntiCheat.INSTANCE.packetPlace.getValue();
               if (this.rotate.getValue()) {
                  class_243 targetVec = new class_243((double)targetBlock.method_10263() + (double)0.5F + (double)clickFace.method_10148() * (double)0.5F, (double)targetBlock.method_10264() + (double)0.5F + (double)clickFace.method_10164() * (double)0.5F, (double)targetBlock.method_10260() + (double)0.5F + (double)clickFace.method_10165() * (double)0.5F);
                  if (!this.faceVector(targetVec)) {
                     if (this.inventory.getValue() && shulkerSlot >= 9) {
                        InventoryUtil.inventorySwap(shulkerSlot, oldSlot);
                        EntityUtil.syncInventory();
                     } else {
                        InventoryUtil.switchToSlot(oldSlot);
                     }

                     return;
                  }
               }

               BlockUtil.clickBlock(targetBlock, clickFace, this.rotate.getValue(), class_1268.field_5808, packet);
               if (AntiRegear.INSTANCE != null) {
                  AntiRegear.INSTANCE.addIgnored(bestPos);
               }

               if (this.inventory.getValue() && shulkerSlot >= 9) {
                  InventoryUtil.inventorySwap(shulkerSlot, oldSlot);
                  EntityUtil.syncInventory();
               } else {
                  InventoryUtil.switchToSlot(oldSlot);
               }

               if (this.rotate.getValue()) {
                  Astra.ROTATION.snapBack();
               }

               this.placePos = bestPos;
            } else {
               if (this.inventory.getValue() && shulkerSlot >= 9) {
                  InventoryUtil.inventorySwap(shulkerSlot, oldSlot);
                  EntityUtil.syncInventory();
               } else {
                  InventoryUtil.switchToSlot(oldSlot);
               }

               CommandManager.sendMessageId("§cFailed to hold shulker box, aborting.", this.hashCode() - 1);
            }
         }
      }
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (this.smart.getValue()) {
            this.update();
         }

         if (this.placeKey.isPressed() && mc.field_1755 == null) {
            if (!this.on) {
               this.opend = false;
               this.openPos = null;
               this.timeoutTimer.reset();
               this.placePos = null;
               if ((!this.open.getValue() || !this.tryOpenNearbyShulker()) && this.place.getValue()) {
                  this.doPlace();
               }
            }

            this.on = true;
         } else {
            this.on = false;
         }

         this.openList.removeIf((pos) -> !(mc.field_1687.method_8320(pos).method_26204() instanceof class_2480));
         if (!(mc.field_1755 instanceof class_495)) {
            if (this.opend) {
               this.opend = false;
               if (this.openPos != null && !this.openList.contains(this.openPos)) {
                  this.openList.add(this.openPos);
               }

               this.openPos = null;
               if (this.autoDisable.getValue()) {
                  this.timeoutTimer.reset();
               }
            }

            if (this.open.getValue()) {
               this.tryOpenNearbyShulker();
            }

            if (this.mine.getValue()) {
               this.tryMineOpenedShulkers();
            }

            if (this.autoDisable.getValue() && this.timeoutTimer.passed((long)this.disableTime.getValueInt())) {
               this.disable();
            }
         } else {
            this.opend = true;
            if (this.openPos != null && !this.openList.contains(this.openPos)) {
               this.openList.add(this.openPos);
            }

            if (this.autoDisable.getValue()) {
               this.timeoutTimer.reset();
            }

            if (this.take.getValue()) {
               boolean took = false;
               class_1703 screenHandler = mc.field_1724.field_7512;
               if (screenHandler instanceof class_1733) {
                  class_1733 shulker = (class_1733)screenHandler;

                  for(class_1735 slot : shulker.field_7761) {
                     if (slot.field_7874 < 27 && !slot.method_7677().method_7960()) {
                        Type type = this.needSteal(slot.method_7677());
                        if (this.smart.getValue() && type != AutoRegear.Type.QuickMove && (type != AutoRegear.Type.Stack || !this.forceMove.getValue())) {
                           if (type == AutoRegear.Type.Stack) {
                              for(int slot1 = 0; slot1 < 36; ++slot1) {
                                 class_1799 stack = mc.field_1724.method_31548().method_5438(slot1);
                                 if (!stack.method_7960() && stack.method_7946() && stack.method_7909() == slot.method_7677().method_7909() && stack.method_7947() < stack.method_7914()) {
                                    int i = (slot1 < 9 ? slot1 + 36 : slot1) + 18;
                                    mc.field_1761.method_2906(shulker.field_7763, slot.field_7874, 0, class_1713.field_7790, mc.field_1724);
                                    mc.field_1761.method_2906(shulker.field_7763, i, 0, class_1713.field_7790, mc.field_1724);
                                    mc.field_1761.method_2906(shulker.field_7763, slot.field_7874, 0, class_1713.field_7790, mc.field_1724);
                                    took = true;
                                    break;
                                 }
                              }
                           }
                        } else {
                           mc.field_1761.method_2906(shulker.field_7763, slot.field_7874, 0, class_1713.field_7794, mc.field_1724);
                           took = true;
                        }
                     }
                  }
               }

            }
         }
      }
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.directionVec != null && this.rotate.getValue()) {
         event.setTarget(this.directionVec, 1.0F, 100.0F);
      }

   }

   private Type needSteal(class_1799 i) {
      if (i.method_7909().equals(class_1802.field_8301) && this.stealCountList[0] > 0) {
         int[] var18 = this.stealCountList;
         var18[0] -= i.method_7947();
         return this.stealCountList[0] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909().equals(class_1802.field_8287) && this.stealCountList[1] > 0) {
         int[] var17 = this.stealCountList;
         var17[1] -= i.method_7947();
         return this.stealCountList[1] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909().equals(class_1802.field_8288) && this.stealCountList[2] > 0) {
         int[] var16 = this.stealCountList;
         var16[2] -= i.method_7947();
         return this.stealCountList[2] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909().equals(class_1802.field_8367) && this.stealCountList[3] > 0) {
         int[] var15 = this.stealCountList;
         var15[3] -= i.method_7947();
         return this.stealCountList[3] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_10540.method_8389()) && this.stealCountList[4] > 0) {
         int[] var14 = this.stealCountList;
         var14[4] -= i.method_7947();
         return this.stealCountList[4] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_10343.method_8389()) && this.stealCountList[5] > 0) {
         int[] var13 = this.stealCountList;
         var13[5] -= i.method_7947();
         return this.stealCountList[5] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_10171.method_8389()) && this.stealCountList[6] > 0) {
         int[] var12 = this.stealCountList;
         var12[6] -= i.method_7947();
         return this.stealCountList[6] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_23152.method_8389()) && this.stealCountList[7] > 0) {
         int[] var11 = this.stealCountList;
         var11[7] -= i.method_7947();
         return this.stealCountList[7] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909().equals(class_1802.field_8634) && this.stealCountList[8] > 0) {
         int[] var10 = this.stealCountList;
         var10[8] -= i.method_7947();
         return this.stealCountList[8] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2665 && this.stealCountList[9] > 0) {
         int[] var9 = this.stealCountList;
         var9[9] -= i.method_7947();
         return this.stealCountList[9] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909().equals(class_2246.field_10002.method_8389()) && this.stealCountList[10] > 0) {
         int[] var8 = this.stealCountList;
         var8[10] -= i.method_7947();
         return this.stealCountList[10] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2244 && this.stealCountList[11] > 0) {
         int[] var7 = this.stealCountList;
         var7[11] -= i.method_7947();
         return this.stealCountList[11] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
      } else {
         if (i.method_7909() == class_1802.field_8436) {
            class_1844 potion = (class_1844)i.method_57825(class_9334.field_49651, class_1844.field_49274);

            for(class_1293 effect : potion.method_57397()) {
               if (effect.method_5579().comp_349() == class_1294.field_5904.comp_349()) {
                  if (this.stealCountList[12] > 0) {
                     int[] var10000 = this.stealCountList;
                     var10000[12] -= i.method_7947();
                     return this.stealCountList[12] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
                  }
               } else if (effect.method_5579().comp_349() == class_1294.field_5907.comp_349()) {
                  if (this.stealCountList[13] > 0) {
                     int[] var5 = this.stealCountList;
                     var5[13] -= i.method_7947();
                     return this.stealCountList[13] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
                  }
               } else if (effect.method_5579().comp_349() == class_1294.field_5910.comp_349() && this.stealCountList[14] > 0) {
                  int[] var6 = this.stealCountList;
                  var6[14] -= i.method_7947();
                  return this.stealCountList[14] < 0 ? AutoRegear.Type.Stack : AutoRegear.Type.QuickMove;
               }
            }
         }

         return AutoRegear.Type.None;
      }
   }

   public void onEnable() {
      if (!nullCheck()) {
         this.opend = false;
         this.openPos = null;
         this.timeoutTimer.reset();
         this.placePos = null;
         this.openList.clear();
         if (this.open.getValue()) {
            boolean opened = this.tryOpenNearbyShulker();
            if (opened) {
               return;
            }
         }

         if (this.place.getValue()) {
            this.doPlace();
         }

      }
   }

   public void onDisable() {
      this.opend = false;
      this.directionVec = null;
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

   public static enum Page {
      General,
      Take;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Take};
      }
   }
}
