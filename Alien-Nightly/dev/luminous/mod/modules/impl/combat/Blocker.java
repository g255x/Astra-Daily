package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;

public class Blocker extends Module {
   public static Blocker INSTANCE;
   private final EnumSetting<Page> page;
   final List<class_2338> placePos;
   final List<class_2338> blockerPos;
   final List<class_2338> list;
   private final Timer timer;
   private final SliderSetting delay;
   private final SliderSetting blocksPer;
   private final BooleanSetting rotate;
   private final BooleanSetting breakCrystal;
   private final BooleanSetting inventorySwap;
   private final BooleanSetting bevelCev;
   private final BooleanSetting burrow;
   private final BooleanSetting face;
   private final BooleanSetting faceUp;
   private final BooleanSetting feet;
   private final BooleanSetting extend;
   private final BooleanSetting onlySurround;
   private final BooleanSetting inAirPause;
   private final BooleanSetting detectMining;
   private final BooleanSetting eatingPause;
   private int placeProgress;
   private class_2338 playerBP;

   public Blocker() {
      super("Blocker", Module.Category.Combat);
      this.page = this.add(new EnumSetting("Page", Blocker.Page.General));
      this.placePos = new ArrayList();
      this.blockerPos = new ArrayList();
      this.list = new ArrayList();
      this.timer = new Timer();
      this.delay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, () -> this.page.getValue() == Blocker.Page.General));
      this.blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, () -> this.page.getValue() == Blocker.Page.General));
      this.rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.getValue() == Blocker.Page.General));
      this.breakCrystal = this.add(new BooleanSetting("Break", true, () -> this.page.getValue() == Blocker.Page.General));
      this.inventorySwap = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.getValue() == Blocker.Page.General));
      this.bevelCev = this.add(new BooleanSetting("BevelCev", true, () -> this.page.getValue() == Blocker.Page.Target));
      this.burrow = this.add(new BooleanSetting("Burrow", true, () -> this.page.getValue() == Blocker.Page.Target));
      this.face = this.add((new BooleanSetting("Face", true, () -> this.page.getValue() == Blocker.Page.Target)).setParent());
      this.faceUp = this.add(new BooleanSetting("FaceUp", false, () -> this.page.getValue() == Blocker.Page.Target && this.face.isOpen()));
      this.feet = this.add((new BooleanSetting("Feet", true, () -> this.page.getValue() == Blocker.Page.Target)).setParent());
      this.extend = this.add(new BooleanSetting("Extend", false, () -> this.page.getValue() == Blocker.Page.Target && this.feet.isOpen()));
      this.onlySurround = this.add(new BooleanSetting("OnlySurround", true, () -> this.page.getValue() == Blocker.Page.Target && this.feet.isOpen()));
      this.inAirPause = this.add(new BooleanSetting("InAirPause", false, () -> this.page.getValue() == Blocker.Page.Check));
      this.detectMining = this.add(new BooleanSetting("DetectMining", true, () -> this.page.getValue() == Blocker.Page.Check));
      this.eatingPause = this.add(new BooleanSetting("EatingPause", true, () -> this.page.getValue() == Blocker.Page.Check));
      this.placeProgress = 0;
      this.setChinese("水晶阻挡");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.list.clear();
      if ((!this.inventorySwap.getValue() || EntityUtil.inInventory()) && (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) && this.timer.passedMs(this.delay.getValue()) && (!this.eatingPause.getValue() || !mc.field_1724.method_6115())) {
         this.placeProgress = 0;
         if (this.playerBP != null && !this.playerBP.equals(EntityUtil.getPlayerPos(true))) {
            this.placePos.clear();
            this.blockerPos.clear();
         }

         this.playerBP = EntityUtil.getPlayerPos(true);
         double[] offset = new double[]{AntiCheat.getOffset(), -AntiCheat.getOffset(), (double)0.0F};
         if (this.bevelCev.getValue()) {
            for(class_2350 i : class_2350.values()) {
               if (i != class_2350.field_11033 && !this.isBedrock(this.playerBP.method_10093(i).method_10084())) {
                  class_2338 blockerPos = this.playerBP.method_10093(i).method_10086(2);
                  if (this.crystalHere(blockerPos) && !this.placePos.contains(blockerPos)) {
                     this.placePos.add(blockerPos);
                  }
               }
            }
         }

         if (this.face.getValue() && (!this.onlySurround.getValue() || Surround.INSTANCE.isOn() || SelfTrap.INSTANCE.isOn())) {
            for(double x : offset) {
               for(double z : offset) {
                  for(class_2350 ix : class_2350.values()) {
                     for(int d = 0; d < 3; ++d) {
                        class_2338 aroundPos = (new BlockPosX(mc.field_1724.method_23317() + x, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() + z)).method_10079(ix, 1).method_10084();
                        class_2338 blockerPos = (new BlockPosX(mc.field_1724.method_23317() + x, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() + z)).method_10079(ix, d).method_10084();
                        if (this.crystalHere(blockerPos) && !this.placePos.contains(blockerPos) && !Alien.HOLE.isHard(aroundPos)) {
                           this.placePos.add(blockerPos);
                        }
                     }
                  }
               }
            }

            if (this.faceUp.getValue()) {
               for(class_2350 ix : class_2350.values()) {
                  if (ix != class_2350.field_11033 && !this.isBedrock(this.playerBP.method_10093(ix).method_10084())) {
                     class_2338 blockerPos = this.playerBP.method_10093(ix).method_10086(2);
                     if (this.crystalHere(blockerPos) && !this.placePos.contains(blockerPos)) {
                        this.placePos.add(blockerPos);
                     }
                  }
               }
            }
         }

         if (this.getObsidian() != -1) {
            this.placePos.removeIf((pos) -> !BlockUtil.clientCanPlace(pos, true));
            if (this.burrow.getValue()) {
               for(double x : offset) {
                  for(double z : offset) {
                     class_2338 surroundPos = new BlockPosX(mc.field_1724.method_23317() + x, mc.field_1724.method_23318(), mc.field_1724.method_23321() + z);
                     if (!this.isBedrock(surroundPos) && Alien.BREAK.isMining(surroundPos)) {
                        for(class_2350 direction : class_2350.values()) {
                           if (direction != class_2350.field_11033 && direction != class_2350.field_11036) {
                              class_2338 defensePos = surroundPos.method_10093(direction);
                              if (!this.detectMining.getValue() || !Alien.BREAK.isMining(defensePos)) {
                                 if (this.breakCrystal.getValue()) {
                                    CombatUtil.attackCrystal(defensePos, this.rotate.getValue(), false);
                                 }

                                 if (BlockUtil.canPlace(defensePos, (double)6.0F, this.breakCrystal.getValue())) {
                                    this.blockerPos.add(defensePos);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (this.feet.getValue() && (!this.onlySurround.getValue() || Surround.INSTANCE.isOn() || SelfTrap.INSTANCE.isOn())) {
               for(double x : offset) {
                  for(double zx : offset) {
                     for(class_2350 ixx : class_2350.values()) {
                        class_2338 surroundPos = (new BlockPosX(mc.field_1724.method_23317() + x, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() + zx)).method_10093(ixx);
                        if (!this.isBedrock(surroundPos) && Alien.BREAK.isMining(surroundPos)) {
                           for(class_2350 directionx : class_2350.values()) {
                              class_2338 defensePos = surroundPos.method_10093(directionx);
                              if (!this.detectMining.getValue() || !Alien.BREAK.isMining(defensePos)) {
                                 if (this.breakCrystal.getValue()) {
                                    CombatUtil.attackCrystal(defensePos, this.rotate.getValue(), false);
                                 }

                                 if (BlockUtil.canPlace(defensePos, (double)6.0F, this.breakCrystal.getValue())) {
                                    this.blockerPos.add(defensePos);
                                 } else if (BlockUtil.canReplace(defensePos) && !BlockUtil.hasEntity(defensePos, true) && this.getHelper(defensePos) != null) {
                                    this.blockerPos.add(this.getHelper(defensePos));
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (this.feet.getValue() && this.extend.getValue() && (!this.onlySurround.getValue() || Surround.INSTANCE.isOn() || SelfTrap.INSTANCE.isOn())) {
               for(double x : offset) {
                  for(double zx : offset) {
                     for(class_2350 ixxx : class_2350.values()) {
                        if (ixxx != class_2350.field_11036 && ixxx != class_2350.field_11033) {
                           class_2338 surroundPos = (new BlockPosX(mc.field_1724.method_23317() + x, mc.field_1724.method_23318() + (double)0.5F, mc.field_1724.method_23321() + zx)).method_10093(ixxx);
                           if (!this.isBedrock(surroundPos)) {
                              for(class_2350 directionxx : class_2350.values()) {
                                 if (directionxx != class_2350.field_11036 && directionxx != class_2350.field_11033) {
                                    class_2338 blockPos = surroundPos.method_10093(directionxx);
                                    if (AutoCrystal.INSTANCE.canPlaceCrystal(blockPos, true, true) && (!this.detectMining.getValue() || !Alien.BREAK.isMining(blockPos))) {
                                       if (this.breakCrystal.getValue()) {
                                          CombatUtil.attackCrystal(blockPos, this.rotate.getValue(), false);
                                       }

                                       if (BlockUtil.canPlace(blockPos, (double)6.0F, this.breakCrystal.getValue())) {
                                          this.blockerPos.add(blockPos);
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

            this.blockerPos.removeIf((pos) -> !BlockUtil.clientCanPlace(pos, true));
            if ((!this.inAirPause.getValue() || mc.field_1724.method_24828()) && !this.blockerPos.isEmpty()) {
               int oldSlot = mc.field_1724.method_31548().field_7545;
               int block = this.getObsidian();
               if (block != -1) {
                  this.doSwap(block);

                  for(class_2338 defensePos : this.blockerPos) {
                     if (BlockUtil.canPlace(defensePos, (double)6.0F, this.breakCrystal.getValue())) {
                        this.doPlace(defensePos);
                     }
                  }

                  if (this.inventorySwap.getValue()) {
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

   public class_2338 getHelper(class_2338 pos) {
      for(class_2350 i : class_2350.values()) {
         if (i != class_2350.field_11036 && (!this.detectMining.getValue() || !Alien.BREAK.isMining(pos.method_10093(i))) && BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) && BlockUtil.canPlace(pos.method_10093(i), (double)6.0F)) {
            return pos.method_10093(i);
         }
      }

      return null;
   }

   private boolean crystalHere(class_2338 pos) {
      return BlockUtil.getEndCrystals(new class_238(pos)).stream().anyMatch((entity) -> entity.method_24515().equals(pos));
   }

   private boolean isBedrock(class_2338 pos) {
      return mc.field_1687.method_8320(pos).method_26204() == class_2246.field_9987;
   }

   private void doPlace(class_2338 pos) {
      if (!this.list.contains(pos)) {
         this.list.add(pos);
         if ((double)this.placeProgress < this.blocksPer.getValue()) {
            BlockUtil.placeBlock(pos, this.rotate.getValue());
            this.timer.reset();
            ++this.placeProgress;
         }
      }

   }

   private void tryPlaceObsidian(class_2338 pos) {
      if (!this.list.contains(pos)) {
         this.list.add(pos);
         if ((double)this.placeProgress < this.blocksPer.getValue() && (!this.detectMining.getValue() || !Alien.BREAK.isMining(pos))) {
            int oldSlot = mc.field_1724.method_31548().field_7545;
            int block;
            if ((block = this.getObsidian()) != -1) {
               this.doSwap(block);
               BlockUtil.placeBlock(pos, this.rotate.getValue());
               this.timer.reset();
               if (this.inventorySwap.getValue()) {
                  this.doSwap(block);
                  EntityUtil.syncInventory();
               } else {
                  this.doSwap(oldSlot);
               }

               ++this.placeProgress;
            }
         }
      }

   }

   private void doSwap(int slot) {
      if (this.inventorySwap.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private int getObsidian() {
      return this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
   }

   public static enum Page {
      General,
      Target,
      Check;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Target, Check};
      }
   }
}
