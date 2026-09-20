package dev.Astra.mod.modules.impl.movement;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;

public class Flatten extends Module {
   public static Flatten INSTANCE;
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
   private final BooleanSetting cover = this.add(new BooleanSetting("Cover", false));
   private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 2, 1, 8));
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 1000));
   private final Timer timer = new Timer();
   int progress = 0;

   public Flatten() {
      super("Flatten", Module.Category.Movement);
      this.setChinese("填平脚下");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.progress = 0;
      if (!this.inventory.getValue() || EntityUtil.inInventory()) {
         if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
            if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
               if (mc.field_1724.method_24828()) {
                  if (this.timer.passed((long)this.delay.getValueInt())) {
                     int oldSlot = mc.field_1724.method_31548().field_7545;
                     int block = this.getBlock();
                     if (block != -1) {
                        if (EntityUtil.isInsideBlock()) {
                           class_2338 pos1 = (new BlockPosX(mc.field_1724.method_23317() + (double)0.5F, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() + (double)0.5F)).method_10074();
                           class_2338 pos2 = (new BlockPosX(mc.field_1724.method_23317() - (double)0.5F, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() + (double)0.5F)).method_10074();
                           class_2338 pos3 = (new BlockPosX(mc.field_1724.method_23317() + (double)0.5F, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() - (double)0.5F)).method_10074();
                           class_2338 pos4 = (new BlockPosX(mc.field_1724.method_23317() - (double)0.5F, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() - (double)0.5F)).method_10074();
                           if (this.canPlace(pos1) || this.canPlace(pos2) || this.canPlace(pos3) || this.canPlace(pos4)) {
                              CombatUtil.attackCrystal(pos1, this.rotate.getValue(), this.usingPause.getValue());
                              CombatUtil.attackCrystal(pos2, this.rotate.getValue(), this.usingPause.getValue());
                              CombatUtil.attackCrystal(pos3, this.rotate.getValue(), this.usingPause.getValue());
                              CombatUtil.attackCrystal(pos4, this.rotate.getValue(), this.usingPause.getValue());
                              this.doSwap(block);
                              this.tryPlaceObsidian(pos1, this.rotate.getValue());
                              this.tryPlaceObsidian(pos2, this.rotate.getValue());
                              this.tryPlaceObsidian(pos3, this.rotate.getValue());
                              this.tryPlaceObsidian(pos4, this.rotate.getValue());
                              if (this.inventory.getValue()) {
                                 this.doSwap(block);
                                 EntityUtil.syncInventory();
                              } else {
                                 this.doSwap(oldSlot);
                              }

                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void tryPlaceObsidian(class_2338 pos, boolean rotate) {
      if (this.canPlace(pos)) {
         if (!((double)this.progress < this.blocksPer.getValue())) {
            return;
         }

         if (BlockUtil.allowAirPlace()) {
            BlockUtil.placedPos.add(pos);
            BlockUtil.airPlace(pos, rotate);
            this.timer.reset();
            ++this.progress;
            return;
         }

         class_2350 side = BlockUtil.getPlaceSide(pos);
         if (side == null) {
            return;
         }

         ++this.progress;
         BlockUtil.placedPos.add(pos);
         BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), rotate);
         this.timer.reset();
      }

   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private boolean canPlace(class_2338 pos) {
      if (this.checkMine.getValue() && Astra.BREAK.isMining(pos)) {
         return false;
      } else if (this.cover.getValue() && mc.field_1687.method_22347(pos.method_10084())) {
         return false;
      } else if (BlockUtil.getPlaceSide(pos) == null) {
         return false;
      } else if (!BlockUtil.canReplace(pos)) {
         return false;
      } else {
         return !this.hasEntity(pos);
      }
   }

   private boolean hasEntity(class_2338 pos) {
      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (entity != mc.field_1724 && entity.method_5805() && !(entity instanceof class_1542) && !(entity instanceof class_1303) && !(entity instanceof class_1683) && !(entity instanceof class_1667) && !(entity instanceof class_1511)) {
            return true;
         }
      }

      return false;
   }

   private int getBlock() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
   }
}
