package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2318;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2665;
import net.minecraft.class_3532;

public class PistonCrystal extends Module {
   public static PistonCrystal INSTANCE;
   private final SliderSetting placeRange = this.add((new SliderSetting("PlaceRange", (double)4.0F, (double)1.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final SliderSetting range = this.add((new SliderSetting("Range", (double)6.0F, (double)1.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final SliderSetting breakRange = this.add((new SliderSetting("BreakRange", (double)3.0F, (double)1.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final BooleanSetting pistonPacket = this.add(new BooleanSetting("PistonPacket", true));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false));
   private final BooleanSetting usingBreak = this.add(new BooleanSetting("UsingBreak", true));
   private final BooleanSetting switchPos = this.add(new BooleanSetting("Switch", false));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("SelfGround", false));
   private final BooleanSetting onlyStatic = this.add(new BooleanSetting("MovingPause", false));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final SliderSetting updateDelay = this.add((new SliderSetting("PlaceDelay", (double)150.0F, (double)0.0F, (double)500.0F)).setSuffix("ms"));
   private final SliderSetting posUpdateDelay = this.add((new SliderSetting("PosUpdateDelay", (double)200.0F, (double)0.0F, (double)1000.0F)).setSuffix("ms"));
   private final SliderSetting stageSetting = this.add(new SliderSetting("Stage", (double)2.0F, (double)1.0F, (double)10.0F));
   private final SliderSetting pistonStage = this.add(new SliderSetting("PistonStage", 1, 1, 10));
   private final SliderSetting pistonMaxStage = this.add(new SliderSetting("PistonMaxStage", 1, 1, 10));
   private final SliderSetting powerStage = this.add(new SliderSetting("PowerStage", 2, 1, 10));
   private final SliderSetting powerMaxStage = this.add(new SliderSetting("PowerMaxStage", 2, 1, 10));
   private final SliderSetting crystalStage = this.add(new SliderSetting("CrystalStage", 2, 1, 10));
   private final SliderSetting crystalMaxStage = this.add(new SliderSetting("CrystalMaxStage", 2, 1, 10));
   private final SliderSetting minDamage = this.add((new SliderSetting("MinDamage", (double)6.0F, (double)0.0F, (double)20.0F, (double)0.5F)).setSuffix("hp"));
   private final Timer timer = new Timer();
   private final Timer crystalTimer = new Timer();
   public class_2338 bestPos = null;
   public class_2338 bestOPos = null;
   public class_2350 bestFacing = null;
   public double getDistance = (double)100.0F;
   public boolean getPos = false;
   public int stage = 1;
   private class_1657 target = null;
   private boolean isPiston = false;

   public PistonCrystal() {
      super("PistonCrystal", Module.Category.Combat);
      this.setChinese("活塞水晶");
      INSTANCE = this;
   }

   public void onTick() {
      if (this.pistonStage.getValue() > this.stageSetting.getValue()) {
         this.pistonStage.setValue(this.stageSetting.getValue());
      }

      if (this.powerStage.getValue() > this.stageSetting.getValue()) {
         this.powerStage.setValue(this.stageSetting.getValue());
      }

      if (this.crystalStage.getValue() > this.stageSetting.getValue()) {
         this.crystalStage.setValue(this.stageSetting.getValue());
      }

      if (this.pistonMaxStage.getValue() > this.stageSetting.getValue()) {
         this.pistonMaxStage.setValue(this.stageSetting.getValue());
      }

      if (this.powerMaxStage.getValue() > this.stageSetting.getValue()) {
         this.powerMaxStage.setValue(this.stageSetting.getValue());
      }

      if (this.crystalMaxStage.getValue() > this.stageSetting.getValue()) {
         this.crystalMaxStage.setValue(this.stageSetting.getValue());
      }

      if (this.crystalMaxStage.getValue() < this.crystalStage.getValue()) {
         this.crystalStage.setValue(this.crystalMaxStage.getValue());
      }

      if (this.powerMaxStage.getValue() < this.powerStage.getValue()) {
         this.powerStage.setValue(this.powerMaxStage.getValue());
      }

      if (this.pistonMaxStage.getValue() < this.pistonStage.getValue()) {
         this.pistonStage.setValue(this.pistonMaxStage.getValue());
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.onTick();
      this.target = CombatUtil.getClosestEnemy(this.range.getValue());
      if (this.target != null) {
         if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
            if (!this.check(this.onlyStatic.getValue(), !mc.field_1724.method_24828(), this.onlyGround.getValue())) {
               class_2338 pos = EntityUtil.getEntityPos(this.target, true);
               double breakRangeSq = this.breakRange.getValue() * this.breakRange.getValue();
               if (!mc.field_1724.method_6115() || this.usingBreak.getValue()) {
                  if (this.checkCrystal(pos.method_10086(0))) {
                     class_2338 crystalPos = pos.method_10086(0);
                     if (mc.field_1724.method_5649((double)crystalPos.method_10263() + (double)0.5F, (double)crystalPos.method_10264() + (double)0.5F, (double)crystalPos.method_10260() + (double)0.5F) <= breakRangeSq) {
                        CombatUtil.attackCrystal(crystalPos, this.rotate.getValue(), true);
                     }
                  }

                  if (this.checkCrystal(pos.method_10086(1))) {
                     class_2338 crystalPos = pos.method_10086(1);
                     if (mc.field_1724.method_5649((double)crystalPos.method_10263() + (double)0.5F, (double)crystalPos.method_10264() + (double)0.5F, (double)crystalPos.method_10260() + (double)0.5F) <= breakRangeSq) {
                        CombatUtil.attackCrystal(crystalPos, this.rotate.getValue(), true);
                     }
                  }

                  if (this.checkCrystal(pos.method_10086(2))) {
                     class_2338 crystalPos = pos.method_10086(2);
                     if (mc.field_1724.method_5649((double)crystalPos.method_10263() + (double)0.5F, (double)crystalPos.method_10264() + (double)0.5F, (double)crystalPos.method_10260() + (double)0.5F) <= breakRangeSq) {
                        CombatUtil.attackCrystal(crystalPos, this.rotate.getValue(), true);
                     }
                  }
               }

               if (this.bestPos != null && mc.field_1687.method_8320(this.bestPos).method_26204() instanceof class_2665) {
                  this.isPiston = true;
               } else if (this.isPiston) {
                  this.isPiston = false;
                  this.crystalTimer.reset();
                  this.bestPos = null;
               }

               if (this.crystalTimer.passedMs((double)this.posUpdateDelay.getValueInt())) {
                  this.stage = 0;
                  this.getDistance = (double)100.0F;
                  this.getPos = false;
                  this.getBestPos(pos.method_10086(2));
                  this.getBestPos(pos.method_10084());
               }

               if (this.timer.passedMs((double)this.updateDelay.getValueInt())) {
                  if (this.getPos && this.bestPos != null) {
                     this.timer.reset();
                     this.doPistonAura(this.bestPos, this.bestFacing, this.bestOPos);
                  }

               }
            }
         }
      }
   }

   public boolean check(boolean onlyStatic, boolean onGround, boolean onlyGround) {
      if (MovementUtil.isMoving() && onlyStatic) {
         return true;
      } else if (onGround && onlyGround) {
         return true;
      } else if (this.findBlock(class_2246.field_10002) == -1) {
         return true;
      } else if (this.findClass(class_2665.class) == -1) {
         return true;
      } else {
         return this.findItem(class_1802.field_8301) == -1;
      }
   }

   private boolean checkCrystal(class_2338 pos) {
      float threshold = (float)this.minDamage.getValue();

      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (entity instanceof class_1511 && AutoCrystal.INSTANCE.calculateDamage(entity.method_19538(), this.target, this.target) > threshold) {
            return true;
         }
      }

      return false;
   }

   private boolean checkCrystal2(class_2338 pos) {
      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (entity instanceof class_1511 && EntityUtil.getEntityPos(entity).equals(pos)) {
            return true;
         }
      }

      return false;
   }

   public String getInfo() {
      return this.target != null ? this.target.method_5477().getString() : null;
   }

   private void getBestPos(class_2338 pos) {
      for(class_2350 i : class_2350.values()) {
         if (i != class_2350.field_11033 && i != class_2350.field_11036) {
            this.getPos(pos, i);
         }
      }

   }

   private void getPos(class_2338 pos, class_2350 i) {
      this.getPos(pos.method_10079(i, 3), i, pos);
      this.getPos(pos.method_10079(i, 3).method_10084(), i, pos);
      int offsetX = pos.method_10093(i).method_10263() - pos.method_10263();
      int offsetZ = pos.method_10093(i).method_10260() - pos.method_10260();
      this.getPos(pos.method_10079(i, 3).method_10069(offsetZ, 0, offsetX), i, pos);
      this.getPos(pos.method_10079(i, 3).method_10069(-offsetZ, 0, -offsetX), i, pos);
      this.getPos(pos.method_10079(i, 3).method_10069(offsetZ, 1, offsetX), i, pos);
      this.getPos(pos.method_10079(i, 3).method_10069(-offsetZ, 1, -offsetX), i, pos);
      this.getPos(pos.method_10079(i, 2), i, pos);
      this.getPos(pos.method_10079(i, 2).method_10084(), i, pos);
      this.getPos(pos.method_10079(i, 2).method_10069(offsetZ, 0, offsetX), i, pos);
      this.getPos(pos.method_10079(i, 2).method_10069(-offsetZ, 0, -offsetX), i, pos);
      this.getPos(pos.method_10079(i, 2).method_10069(offsetZ, 1, offsetX), i, pos);
      this.getPos(pos.method_10079(i, 2).method_10069(-offsetZ, 1, -offsetX), i, pos);
   }

   private void getPos(class_2338 pos, class_2350 facing, class_2338 oPos) {
      if (!this.switchPos.getValue() || this.bestPos == null || !this.bestPos.equals(pos) || !mc.field_1687.method_22347(this.bestPos)) {
         if (BlockUtil.canPlace(pos, this.placeRange.getValue()) || this.getBlock(pos) instanceof class_2665) {
            if (this.findClass(class_2665.class) != -1) {
               if (!ClientSetting.INSTANCE.lowVersion.getValue() || this.getBlock(pos) instanceof class_2665 || !(mc.field_1724.method_23318() - (double)pos.method_10264() <= (double)-2.0F) && !(mc.field_1724.method_23318() - (double)pos.method_10264() >= (double)3.0F) || !(BlockUtil.distanceToXZ((double)pos.method_10263() + (double)0.5F, (double)pos.method_10260() + (double)0.5F) < 2.6)) {
                  if (mc.field_1687.method_22347(pos.method_10079(facing, -1)) && (this.getBlock(pos.method_10093(facing.method_10153())) != class_2246.field_10008 || this.checkCrystal2(pos.method_10093(facing.method_10153())))) {
                     if (BlockUtil.canPlace(pos, this.placeRange.getValue()) || this.isPiston(pos, facing)) {
                        if ((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(pos.method_46558())) < this.getDistance || this.bestPos == null) {
                           this.bestPos = pos;
                           this.bestOPos = oPos;
                           this.bestFacing = facing;
                           this.getDistance = (double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(pos.method_46558()));
                           this.getPos = true;
                           this.crystalTimer.reset();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void doPistonAura(class_2338 pos, class_2350 facing, class_2338 oPos) {
      if ((double)this.stage >= this.stageSetting.getValue()) {
         this.stage = 0;
      }

      ++this.stage;
      if (mc.field_1687.method_22347(pos)) {
         if (!BlockUtil.canPlace(pos)) {
            return;
         }

         if ((double)this.stage >= this.pistonStage.getValue() && (double)this.stage <= this.pistonMaxStage.getValue()) {
            class_2350 side = BlockUtil.getPlaceSide(pos);
            if (side == null) {
               return;
            }

            int old = mc.field_1724.method_31548().field_7545;
            AutoPush.pistonFacing(facing);
            int piston = this.findClass(class_2665.class);
            this.doSwap(piston);
            BlockUtil.placeBlock(pos, false, this.pistonPacket.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(piston);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }

            class_2338 neighbour = pos.method_10093(side);
            class_2350 opposite = side.method_10153();
            if (this.rotate.getValue()) {
               Astra.ROTATION.lookAt(neighbour, opposite);
            }
         }
      }

      if ((double)this.stage >= this.powerStage.getValue() && (double)this.stage <= this.powerMaxStage.getValue()) {
         this.doRedStone(pos, facing, oPos.method_10093(facing));
      }

      if ((double)this.stage >= this.crystalStage.getValue() && (double)this.stage <= this.crystalMaxStage.getValue()) {
         this.placeCrystal(oPos, facing);
      }

   }

   private void placeCrystal(class_2338 pos, class_2350 facing) {
      if (BlockUtil.canPlaceCrystal(pos.method_10093(facing))) {
         int crystal = this.findItem(class_1802.field_8301);
         if (crystal != -1) {
            int old = mc.field_1724.method_31548().field_7545;
            this.doSwap(crystal);
            BlockUtil.placeCrystal(pos.method_10093(facing), true);
            if (this.inventory.getValue()) {
               this.doSwap(crystal);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }

         }
      }
   }

   private boolean isPiston(class_2338 pos, class_2350 facing) {
      if (!(mc.field_1687.method_8320(pos).method_26204() instanceof class_2665)) {
         return false;
      } else if (((class_2350)mc.field_1687.method_8320(pos).method_11654(class_2318.field_10927)).method_10153() != facing) {
         return false;
      } else {
         return mc.field_1687.method_22347(pos.method_10079(facing, -1)) || this.getBlock(pos.method_10093(facing.method_10153())) == class_2246.field_10008;
      }
   }

   private void doRedStone(class_2338 pos, class_2350 facing, class_2338 crystalPos) {
      if (mc.field_1687.method_22347(pos.method_10079(facing, -1)) || this.getBlock(pos.method_10093(facing.method_10153())) == class_2246.field_10008) {
         for(class_2350 i : class_2350.values()) {
            if (this.getBlock(pos.method_10093(i)) == class_2246.field_10002) {
               return;
            }
         }

         int power = this.findBlock(class_2246.field_10002);
         if (power != -1) {
            int old = mc.field_1724.method_31548().field_7545;
            class_2350 bestNeighboring = BlockUtil.getBestNeighboring(pos, facing);
            if (bestNeighboring != null && bestNeighboring != facing.method_10153() && BlockUtil.canPlace(pos.method_10093(bestNeighboring), this.placeRange.getValue()) && !pos.method_10093(bestNeighboring).equals(crystalPos)) {
               this.doSwap(power);
               BlockUtil.placeBlock(pos.method_10093(bestNeighboring), this.rotate.getValue());
               if (this.inventory.getValue()) {
                  this.doSwap(power);
                  EntityUtil.syncInventory();
               } else {
                  this.doSwap(old);
               }

            } else {
               for(class_2350 i : class_2350.values()) {
                  if (BlockUtil.canPlace(pos.method_10093(i), this.placeRange.getValue()) && !pos.method_10093(i).equals(crystalPos) && i != facing.method_10153()) {
                     this.doSwap(power);
                     BlockUtil.placeBlock(pos.method_10093(i), this.rotate.getValue());
                     if (this.inventory.getValue()) {
                        this.doSwap(power);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(old);
                     }

                     return;
                  }
               }

            }
         }
      }
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   public int findItem(class_1792 itemIn) {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(itemIn) : InventoryUtil.findItem(itemIn);
   }

   public int findBlock(class_2248 blockIn) {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(blockIn) : InventoryUtil.findBlock(blockIn);
   }

   public int findClass(Class<?> clazz) {
      return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(clazz) : InventoryUtil.findClass(clazz);
   }

   private class_2248 getBlock(class_2338 pos) {
      return mc.field_1687.method_8320(pos).method_26204();
   }
}
