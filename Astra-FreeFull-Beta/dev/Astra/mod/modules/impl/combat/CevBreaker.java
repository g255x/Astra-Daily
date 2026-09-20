package dev.Astra.mod.modules.impl.combat;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.player.PacketMine;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1531;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;

public class CevBreaker extends Module {
   public static CevBreaker INSTANCE;
   private final SliderSetting targetRange = this.add((new SliderSetting("TargetRange", (double)6.0F, (double)0.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final SliderSetting mineRange = this.add((new SliderSetting("MineRange", (double)4.5F, (double)0.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final SliderSetting breakRange = this.add((new SliderSetting("BreakRange", (double)3.0F, (double)0.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final SliderSetting obsidianDelay = this.add((new SliderSetting("ObsidianDelay", (double)550.0F, (double)0.0F, (double)600.0F)).setSuffix("ms"));
   private final SliderSetting crystalDelay = this.add((new SliderSetting("CrystalDelay", (double)500.0F, (double)0.0F, (double)600.0F)).setSuffix("ms"));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting ground = this.add(new BooleanSetting("Ground", false));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting top = this.add(new BooleanSetting("Top", true));
   private final BooleanSetting bevel = this.add(new BooleanSetting("Bevel", true));
   private final BooleanSetting crystal = this.add(new BooleanSetting("Crystal", true));
   private final BooleanSetting checkDamage = this.add(new BooleanSetting("DetectProgress", true));
   private final SliderSetting crystalDamage = this.add(new SliderSetting("Progress", 0.8, (double)0.0F, (double)1.0F, 0.01, () -> this.crystal.isOpen() && this.checkDamage.getValue()));
   private final BooleanSetting placeRotate = this.add(new BooleanSetting("PlaceRotate", true));
   private final SliderSetting minDamage = this.add((new SliderSetting("MinDamage", (double)4.0F, (double)0.0F, (double)20.0F, 0.1, () -> this.crystal.isOpen())).setSuffix("hp"));
   private final SliderSetting breakDelay = this.add((new SliderSetting("BreakDelay", (double)150.0F, (double)0.0F, (double)1000.0F, () -> this.crystal.isOpen())).setSuffix("ms"));
   private final Timer obsidianPlaceTimer = new Timer();
   private final Timer crystalPlaceTimer = new Timer();
   private final Timer breakTimer = new Timer();
   private class_1657 target;
   private class_2338 placedCrystalPos = null;

   public CevBreaker() {
      super("CevBreaker", Module.Category.Combat);
      this.setChinese("自动炸头");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!this.inventory.getValue() || EntityUtil.inInventory()) {
         if (!this.ground.getValue() || mc.field_1724.method_24828()) {
            this.target = CombatUtil.getClosestEnemy(this.targetRange.getValue());
            if (this.target == null) {
               this.placedCrystalPos = null;
            } else {
               if (this.crystal.getValue() && this.placedCrystalPos != null && this.breakTimer.passedMs((double)this.breakDelay.getValueInt())) {
                  if (this.isCrystalAt(this.placedCrystalPos)) {
                     if (mc.field_1724.method_5649((double)this.placedCrystalPos.method_10263() + (double)0.5F, (double)this.placedCrystalPos.method_10264() + (double)0.5F, (double)this.placedCrystalPos.method_10260() + (double)0.5F) <= this.breakRange.getValue() * this.breakRange.getValue()) {
                        float damage = AutoCrystal.INSTANCE.calculateDamage(this.placedCrystalPos, this.target, this.target);
                        if (damage >= this.minDamage.getValueFloat()) {
                           CombatUtil.attackCrystal(this.placedCrystalPos, this.placeRotate.getValue(), true);
                           this.breakTimer.reset();
                           this.placedCrystalPos = null;
                        }
                     }
                  } else {
                     this.placedCrystalPos = null;
                  }
               }

               class_2338 targetPos = EntityUtil.getEntityPos(this.target);
               class_2338 breakPos = PacketMine.getBreakPos();
               class_2338 obsidianPos = null;

               for(class_2350 facing : class_2350.values()) {
                  if (facing != class_2350.field_11033 && (facing == class_2350.field_11036 || this.bevel.getValue()) && (facing != class_2350.field_11036 || this.top.getValue())) {
                     class_2338 pos = targetPos.method_10086(1).method_10093(facing);
                     if (pos.method_10084().method_46558().method_1022(mc.field_1724.method_19538()) <= this.mineRange.getValue()) {
                        obsidianPos = pos;
                        break;
                     }
                  }
               }

               if (obsidianPos != null) {
                  class_2248 block = this.getBlock(obsidianPos);
                  boolean isAirAbove = mc.field_1687.method_22347(obsidianPos.method_10084());
                  if (block == class_2246.field_10540) {
                     if (isAirAbove && (breakPos == null || !breakPos.equals(obsidianPos))) {
                        PacketMine.INSTANCE.mine(obsidianPos);
                     }
                  } else if (block != class_2246.field_9987) {
                     if (mc.field_1687.method_22347(obsidianPos)) {
                        if (BlockUtil.canPlace(obsidianPos) && this.obsidianPlaceTimer.passedMs((double)this.obsidianDelay.getValueInt())) {
                           this.placeObsidian(obsidianPos);
                           this.obsidianPlaceTimer.reset();
                        }
                     } else if (isAirAbove && (breakPos == null || !breakPos.equals(obsidianPos))) {
                        PacketMine.INSTANCE.mine(obsidianPos);
                     }
                  }

                  if (this.crystal.getValue() && block == class_2246.field_10540 && breakPos != null && breakPos.equals(obsidianPos) && isAirAbove) {
                     double currentProgress = (double)PacketMine.INSTANCE.mineTimer.getMs() / PacketMine.INSTANCE.breakFinalTime;
                     if (this.canPlaceCrystal(obsidianPos.method_10084())) {
                        boolean shouldPlace = !this.checkDamage.getValue() || currentProgress >= this.crystalDamage.getValue();
                        if (shouldPlace && this.crystalPlaceTimer.passedMs((double)this.crystalDelay.getValueInt())) {
                           this.placeCrystal(obsidianPos);
                           this.crystalPlaceTimer.reset();
                        }
                     }
                  }

               }
            }
         }
      }
   }

   private boolean canPlaceCrystal(class_2338 crystalPos) {
      class_2338 obsPos = crystalPos.method_10074();
      class_2248 block = this.getBlock(obsPos);
      if (block != class_2246.field_9987 && block != class_2246.field_10540) {
         return false;
      } else if (BlockUtil.getClickSideStrict(obsPos) == null) {
         return false;
      } else if (this.noEntity(crystalPos) && this.noEntity(crystalPos.method_10084())) {
         return !ClientSetting.INSTANCE.lowVersion.getValue() || mc.field_1687.method_22347(crystalPos.method_10084());
      } else {
         return false;
      }
   }

   private boolean noEntity(class_2338 pos) {
      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (!(entity instanceof class_1542) && (!(entity instanceof class_1531) || !AntiCheat.INSTANCE.ignoreArmorStand.getValue())) {
            return false;
         }
      }

      return true;
   }

   private boolean isCrystalAt(class_2338 pos) {
      for(class_1297 entity : mc.field_1687.method_18112()) {
         if (entity instanceof class_1511 && entity.method_24515().equals(pos)) {
            return true;
         }
      }

      return false;
   }

   private void placeCrystal(class_2338 obsidianPos) {
      int crystalSlot = this.findCrystal();
      if (crystalSlot != -1) {
         class_2338 crystalPos = obsidianPos.method_10084();
         if (this.canPlaceCrystal(crystalPos)) {
            int oldSlot = mc.field_1724.method_31548().field_7545;
            this.doSwap(crystalSlot, crystalSlot);
            BlockUtil.placeCrystal(crystalPos, this.placeRotate.getValue());
            this.doSwap(oldSlot, crystalSlot);
            this.placedCrystalPos = crystalPos;
            this.breakTimer.reset();
         }
      }
   }

   private void placeObsidian(class_2338 pos) {
      int obsidianSlot = this.findObsidian();
      if (obsidianSlot != -1) {
         int oldSlot = mc.field_1724.method_31548().field_7545;
         this.doSwap(obsidianSlot, obsidianSlot);
         if (BlockUtil.canPlace(pos)) {
            if (BlockUtil.allowAirPlace()) {
               BlockUtil.airPlace(pos, this.rotate.getValue());
            } else {
               class_2350 side = BlockUtil.getPlaceSide(pos);
               if (side != null) {
                  BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), this.rotate.getValue());
               }
            }
         }

         this.doSwap(oldSlot, obsidianSlot);
      }
   }

   private int findObsidian() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
   }

   private int findCrystal() {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8301) : InventoryUtil.findItem(class_1802.field_8301);
   }

   private void doSwap(int slot, int inv) {
      if (!this.inventory.getValue()) {
         InventoryUtil.switchToSlot(slot);
      } else {
         InventoryUtil.inventorySwap(inv, mc.field_1724.method_31548().field_7545);
      }

   }

   private class_2248 getBlock(class_2338 pos) {
      return mc.field_1687.method_8320(pos).method_26204();
   }

   public String getInfo() {
      return this.target != null ? this.target.method_5477().getString() : null;
   }
}
