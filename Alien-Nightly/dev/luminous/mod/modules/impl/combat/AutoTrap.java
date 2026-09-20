package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.PredictUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1770;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2292;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public class AutoTrap extends Module {
   public static AutoTrap INSTANCE;
   public final SliderSetting delay = this.add((new SliderSetting("Delay", 100, 0, 500)).setSuffix("ms"));
   private final EnumSetting<TargetMode> targetMod;
   private final EnumSetting<Mode> headMode;
   final ArrayList<class_2338> trapList;
   final ArrayList<class_2338> placeList;
   private final Timer timer;
   private final SliderSetting placeRange;
   private final SliderSetting blocksPer;
   public final SliderSetting predictTicks;
   private final BooleanSetting rotate;
   private final BooleanSetting autoDisable;
   private final SliderSetting range;
   private final BooleanSetting checkMine;
   private final BooleanSetting helper;
   private final BooleanSetting inventory;
   private final BooleanSetting onlyCrawling;
   private final BooleanSetting checkElytra;
   private final BooleanSetting extend;
   private final BooleanSetting antiStep;
   private final BooleanSetting onlyBreak;
   private final BooleanSetting head;
   private final BooleanSetting headExtend;
   private final BooleanSetting chestUp;
   private final BooleanSetting onlyBreaking;
   private final BooleanSetting chest;
   private final BooleanSetting onlyGround;
   private final BooleanSetting ignoreCrawling;
   private final BooleanSetting legs;
   private final BooleanSetting legAnchor;
   private final BooleanSetting down;
   private final BooleanSetting onlyHole;
   private final BooleanSetting breakCrystal;
   private final BooleanSetting usingPause;
   private final BooleanSetting selfGround;
   public class_1657 target;
   int progress;

   public AutoTrap() {
      super("AutoTrap", Module.Category.Combat);
      this.targetMod = this.add(new EnumSetting("TargetMode", AutoTrap.TargetMode.Single));
      this.headMode = this.add(new EnumSetting("BlockForHead", AutoTrap.Mode.Anchor));
      this.trapList = new ArrayList();
      this.placeList = new ArrayList();
      this.timer = new Timer();
      this.placeRange = this.add((new SliderSetting("PlaceRange", (double)4.0F, (double)1.0F, (double)6.0F)).setSuffix("m"));
      this.blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8));
      this.predictTicks = this.add(new SliderSetting("PredictTicks", (double)2.0F, (double)0.0F, (double)50.0F, (double)1.0F));
      this.rotate = this.add(new BooleanSetting("Rotate", true));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", true));
      this.range = this.add((new SliderSetting("Range", (double)5.0F, (double)1.0F, (double)8.0F)).setSuffix("m"));
      this.checkMine = this.add(new BooleanSetting("DetectMining", false));
      this.helper = this.add(new BooleanSetting("Helper", true));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true));
      this.onlyCrawling = this.add(new BooleanSetting("OnlyCrawling", false));
      this.checkElytra = this.add(new BooleanSetting("CheckElytra", false));
      this.extend = this.add(new BooleanSetting("Extend", true));
      this.antiStep = this.add(new BooleanSetting("AntiStep", false));
      BooleanSetting var10006 = this.antiStep;
      Objects.requireNonNull(var10006);
      this.onlyBreak = this.add(new BooleanSetting("OnlyBreak", false, var10006::getValue));
      this.head = this.add(new BooleanSetting("Head", true));
      this.headExtend = this.add(new BooleanSetting("HeadExtend", true));
      this.chestUp = this.add(new BooleanSetting("ChestUp", true));
      var10006 = this.chestUp;
      Objects.requireNonNull(var10006);
      this.onlyBreaking = this.add(new BooleanSetting("OnlyBreaking", false, var10006::getValue));
      this.chest = this.add(new BooleanSetting("Chest", true));
      var10006 = this.chest;
      Objects.requireNonNull(var10006);
      this.onlyGround = this.add(new BooleanSetting("OnlyGround", false, var10006::getValue));
      var10006 = this.chest;
      Objects.requireNonNull(var10006);
      this.ignoreCrawling = this.add(new BooleanSetting("IgnoreCrawling", false, var10006::getValue));
      this.legs = this.add(new BooleanSetting("Legs", false));
      this.legAnchor = this.add(new BooleanSetting("LegAnchor", true));
      this.down = this.add(new BooleanSetting("Down", false));
      this.onlyHole = this.add(new BooleanSetting("OnlyHole", false));
      this.breakCrystal = this.add(new BooleanSetting("Break", true));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true));
      this.selfGround = this.add(new BooleanSetting("SelfGround", true));
      this.progress = 0;
      this.setChinese("自动困住");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.trapList.clear();
      this.placeList.clear();
      this.progress = 0;
      this.target = null;
      if ((!this.selfGround.getValue() || mc.field_1724.method_24828()) && (!this.inventory.getValue() || EntityUtil.inInventory()) && (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) && (!this.usingPause.getValue() || !mc.field_1724.method_6115()) && this.timer.passed((long)this.delay.getValue())) {
         if (this.targetMod.getValue() == AutoTrap.TargetMode.Single) {
            this.target = CombatUtil.getClosestEnemy(this.range.getValue());
            if (this.target == null) {
               if (this.autoDisable.getValue()) {
                  this.disable();
               }

               return;
            }

            this.trapTarget(this.target);
         } else if (this.targetMod.getValue() == AutoTrap.TargetMode.Multi) {
            boolean found = false;

            for(class_1657 player : CombatUtil.getEnemies(this.range.getValue())) {
               found = true;
               this.target = player;
               this.trapTarget(this.target);
            }

            if (!found) {
               if (this.autoDisable.getValue()) {
                  this.disable();
               }

               this.target = null;
            }
         }
      }

   }

   private void trapTarget(class_1657 target) {
      if ((!this.onlyHole.getValue() || Alien.HOLE.isHole(EntityUtil.getEntityPos(target))) && (!this.onlyCrawling.getValue() || target.method_20448() || this.checkElytra.getValue() && ((class_1799)target.method_31548().field_7548.get(2)).method_7909() instanceof class_1770 && (!(mc.field_1724.method_23318() < target.method_23318() + (double)1.0F) || target.method_6128()))) {
         class_243 playerPos = this.predictTicks.getValue() > (double)0.0F ? PredictUtil.getPos(target, this.predictTicks.getValueInt()) : target.method_19538();
         this.doTrap(target, new BlockPosX(playerPos.method_10216(), playerPos.method_10214(), playerPos.method_10215()));
      }

   }

   private void doTrap(class_1657 player, class_2338 pos) {
      if (pos != null && !this.trapList.contains(pos)) {
         this.trapList.add(pos);
         int headOffset = player.method_20448() ? 1 : 2;
         int chestOffset = player.method_20448() ? 0 : 1;
         if (this.legs.getValue()) {
            for(class_2350 i : class_2350.values()) {
               if (i != class_2350.field_11033 && i != class_2350.field_11036) {
                  class_2338 offsetPos = pos.method_10093(i);
                  this.tryPlaceBlock(offsetPos, this.legAnchor.getValue(), false, false);
                  if (BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue()) && this.getHelper(offsetPos) != null) {
                     this.tryPlaceObsidian(this.getHelper(offsetPos));
                  }
               }
            }
         }

         if (this.headExtend.getValue()) {
            for(int x : new int[]{1, 0, -1}) {
               for(int z : new int[]{1, 0, -1}) {
                  class_2338 offsetPos = pos.method_10069(z, 0, x);
                  if (this.checkEntity(new class_2338(offsetPos))) {
                     this.tryPlaceBlock(offsetPos.method_10086(headOffset), this.headMode.getValue() == AutoTrap.Mode.Anchor, this.headMode.getValue() == AutoTrap.Mode.Concrete, this.headMode.getValue() == AutoTrap.Mode.Web);
                  }
               }
            }
         }

         if (this.head.getValue() && BlockUtil.clientCanPlace(pos.method_10086(headOffset), this.breakCrystal.getValue())) {
            if (BlockUtil.getPlaceSide(pos.method_10086(headOffset)) == null) {
               boolean trapChest = this.helper.getValue();
               if (this.getHelper(pos.method_10086(headOffset)) != null) {
                  this.tryPlaceObsidian(this.getHelper(pos.method_10086(headOffset)));
                  trapChest = false;
               }

               if (trapChest) {
                  for(class_2350 ix : class_2350.values()) {
                     if (ix != class_2350.field_11033 && ix != class_2350.field_11036) {
                        class_2338 offsetPos = pos.method_10093(ix).method_10086(chestOffset);
                        if (BlockUtil.isStrictDirection(pos.method_10093(ix).method_10084(), ix.method_10153()) && BlockUtil.clientCanPlace(offsetPos.method_10086(chestOffset), this.breakCrystal.getValue()) && BlockUtil.canPlace(offsetPos, this.placeRange.getValue(), this.breakCrystal.getValue())) {
                           this.tryPlaceObsidian(offsetPos);
                           trapChest = false;
                           break;
                        }
                     }
                  }

                  if (trapChest) {
                     for(class_2350 ixx : class_2350.values()) {
                        if (ixx != class_2350.field_11033 && ixx != class_2350.field_11036) {
                           class_2338 offsetPos = pos.method_10093(ixx).method_10086(chestOffset);
                           if (BlockUtil.isStrictDirection(pos.method_10093(ixx).method_10084(), ixx.method_10153()) && BlockUtil.clientCanPlace(offsetPos.method_10086(chestOffset), this.breakCrystal.getValue()) && BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue()) && this.getHelper(offsetPos) != null) {
                              this.tryPlaceObsidian(this.getHelper(offsetPos));
                              trapChest = false;
                              break;
                           }
                        }
                     }

                     if (trapChest) {
                        for(class_2350 ixxx : class_2350.values()) {
                           if (ixxx != class_2350.field_11033 && ixxx != class_2350.field_11036) {
                              class_2338 offsetPos = pos.method_10093(ixxx).method_10086(chestOffset);
                              if (BlockUtil.isStrictDirection(pos.method_10093(ixxx).method_10084(), ixxx.method_10153()) && BlockUtil.clientCanPlace(offsetPos.method_10086(chestOffset), this.breakCrystal.getValue()) && BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue()) && this.getHelper(offsetPos) != null && BlockUtil.getPlaceSide(offsetPos.method_10074()) == null && BlockUtil.clientCanPlace(offsetPos.method_10074(), this.breakCrystal.getValue()) && this.getHelper(offsetPos.method_10074()) != null) {
                                 this.tryPlaceObsidian(this.getHelper(offsetPos.method_10074()));
                                 break;
                              }
                           }
                        }
                     }
                  }
               }
            }

            this.tryPlaceBlock(pos.method_10086(headOffset), this.headMode.getValue() == AutoTrap.Mode.Anchor, this.headMode.getValue() == AutoTrap.Mode.Concrete, this.headMode.getValue() == AutoTrap.Mode.Web);
         }

         if (this.antiStep.getValue() && (Alien.BREAK.isMining(pos.method_10086(headOffset)) || !this.onlyBreak.getValue())) {
            if (BlockUtil.getPlaceSide(pos.method_10086(3)) == null && BlockUtil.clientCanPlace(pos.method_10086(3), this.breakCrystal.getValue()) && this.getHelper(pos.method_10086(3), class_2350.field_11033) != null) {
               this.tryPlaceObsidian(this.getHelper(pos.method_10086(3)));
            }

            this.tryPlaceObsidian(pos.method_10086(3));
         }

         if (this.down.getValue()) {
            class_2338 offsetPos = pos.method_10074();
            this.tryPlaceObsidian(offsetPos);
            if (BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue()) && this.getHelper(offsetPos) != null) {
               this.tryPlaceObsidian(this.getHelper(offsetPos));
            }
         }

         if (this.chestUp.getValue()) {
            for(class_2350 ixxxx : class_2350.values()) {
               if (ixxxx != class_2350.field_11033 && ixxxx != class_2350.field_11036) {
                  class_2338 offsetPos = pos.method_10093(ixxxx).method_10086(headOffset);
                  if (!this.onlyBreaking.getValue() || Alien.BREAK.isMining(pos.method_10086(headOffset))) {
                     this.tryPlaceObsidian(offsetPos);
                     if (BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())) {
                        if (this.getHelper(offsetPos) != null) {
                           this.tryPlaceObsidian(this.getHelper(offsetPos));
                        } else if (BlockUtil.getPlaceSide(offsetPos.method_10074()) == null && BlockUtil.clientCanPlace(offsetPos.method_10074(), this.breakCrystal.getValue()) && this.getHelper(offsetPos.method_10074()) != null) {
                           this.tryPlaceObsidian(this.getHelper(offsetPos.method_10074()));
                        }
                     }
                  }
               }
            }
         }

         if (this.chest.getValue() && (!this.onlyGround.getValue() || this.target.method_24828()) && (!this.ignoreCrawling.getValue() || !this.target.method_20448())) {
            for(class_2350 ixxxxx : class_2350.values()) {
               if (ixxxxx != class_2350.field_11033 && ixxxxx != class_2350.field_11036) {
                  class_2338 offsetPos = pos.method_10093(ixxxxx).method_10086(chestOffset);
                  this.tryPlaceObsidian(offsetPos);
                  if (BlockUtil.getPlaceSide(offsetPos) == null && BlockUtil.clientCanPlace(offsetPos, this.breakCrystal.getValue())) {
                     if (this.getHelper(offsetPos) != null) {
                        this.tryPlaceObsidian(this.getHelper(offsetPos));
                     } else if (BlockUtil.getPlaceSide(offsetPos.method_10074()) == null && BlockUtil.clientCanPlace(offsetPos.method_10074(), this.breakCrystal.getValue()) && this.getHelper(offsetPos.method_10074()) != null) {
                        this.tryPlaceObsidian(this.getHelper(offsetPos.method_10074()));
                     }
                  }
               }
            }
         }

         if (this.extend.getValue()) {
            for(int x : new int[]{1, 0, -1}) {
               for(int zx : new int[]{1, 0, -1}) {
                  class_2338 offsetPos = pos.method_10069(x, 0, zx);
                  if (this.checkEntity(new class_2338(offsetPos))) {
                     this.doTrap(player, offsetPos);
                  }
               }
            }
         }
      }

   }

   public String getInfo() {
      return this.target != null ? this.target.method_5477().getString() : null;
   }

   public class_2338 getHelper(class_2338 pos) {
      if (!this.helper.getValue()) {
         return null;
      } else {
         for(class_2350 i : class_2350.values()) {
            if ((!this.checkMine.getValue() || !Alien.BREAK.isMining(pos.method_10093(i))) && BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) && BlockUtil.canPlace(pos.method_10093(i), this.placeRange.getValue(), this.breakCrystal.getValue())) {
               return pos.method_10093(i);
            }
         }

         return null;
      }
   }

   public class_2338 getHelper(class_2338 pos, class_2350 ignore) {
      if (!this.helper.getValue()) {
         return null;
      } else {
         for(class_2350 i : class_2350.values()) {
            if (i != ignore && (!this.checkMine.getValue() || !Alien.BREAK.isMining(pos.method_10093(i))) && BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) && BlockUtil.canPlace(pos.method_10093(i), this.placeRange.getValue(), this.breakCrystal.getValue())) {
               return pos.method_10093(i);
            }
         }

         return null;
      }
   }

   private boolean checkEntity(class_2338 pos) {
      if (mc.field_1724.method_5829().method_994(new class_238(pos))) {
         return false;
      } else {
         for(class_1297 entity : Alien.THREAD.getPlayers()) {
            if (entity.method_5829().method_994(new class_238(pos)) && entity.method_5805()) {
               return true;
            }
         }

         return false;
      }
   }

   private void tryPlaceBlock(class_2338 pos, boolean anchor, boolean sand, boolean web) {
      if (!this.placeList.contains(pos) && !Alien.BREAK.isMining(pos) && BlockUtil.canPlace(pos, (double)6.0F, this.breakCrystal.getValue()) && (double)this.progress < this.blocksPer.getValue() && !((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(pos.method_46558())) > this.placeRange.getValue())) {
         int old = mc.field_1724.method_31548().field_7545;
         int block = sand ? this.getConcrete() : (web ? (this.getWeb() != -1 ? this.getWeb() : this.getBlock()) : (anchor && this.getAnchor() != -1 ? this.getAnchor() : this.getBlock()));
         if (block != -1) {
            this.placeList.add(pos);
            CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.usingPause.getValue());
            this.doSwap(block);
            BlockUtil.placeBlock(pos, this.rotate.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(block);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }

            this.timer.reset();
            ++this.progress;
         }
      }

   }

   private void tryPlaceObsidian(class_2338 pos) {
      if (pos != null && !this.placeList.contains(pos) && !Alien.BREAK.isMining(pos) && BlockUtil.canPlace(pos, (double)6.0F, this.breakCrystal.getValue()) && (double)this.progress < this.blocksPer.getValue() && !((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(pos.method_46558())) > this.placeRange.getValue())) {
         int old = mc.field_1724.method_31548().field_7545;
         int block = this.getBlock();
         if (block != -1) {
            BlockUtil.placedPos.add(pos);
            this.placeList.add(pos);
            CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.usingPause.getValue());
            this.doSwap(block);
            BlockUtil.placeBlock(pos, this.rotate.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(block);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }

            this.timer.reset();
            ++this.progress;
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

   private int getBlock() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
   }

   private int getConcrete() {
      return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(class_2292.class) : InventoryUtil.findClass(class_2292.class);
   }

   private int getWeb() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10343) : InventoryUtil.findBlock(class_2246.field_10343);
   }

   private int getAnchor() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_23152) : InventoryUtil.findBlock(class_2246.field_23152);
   }

   private static enum Mode {
      Obsidian,
      Anchor,
      Web,
      Concrete;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Obsidian, Anchor, Web, Concrete};
      }
   }

   public static enum TargetMode {
      Single,
      Multi;

      // $FF: synthetic method
      private static TargetMode[] $values() {
         return new TargetMode[]{Single, Multi};
      }
   }
}
