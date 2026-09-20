package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1533;
import net.minecraft.class_1542;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2824;
import net.minecraft.class_3736;

public class AntiPhase extends Module {
   private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 4, 0, 8));
   private final BooleanSetting ladder = this.add((new BooleanSetting("Ladder", true)).setParent());
   private final BooleanSetting onlyHard;
   private final BooleanSetting itemFrame;
   private final BooleanSetting fill;
   private final BooleanSetting scaffolding;
   private final BooleanSetting rotate;
   private final BooleanSetting eatingPause;
   private final BooleanSetting collideSkip;
   private final BooleanSetting crawlingSkip;
   private final BooleanSetting onlyGround;
   private final SliderSetting targetRange;
   private final SliderSetting delay;
   private final BooleanSetting inventory;
   private final Timer timer;

   public AntiPhase() {
      super("AntiPhase", Module.Category.Combat);
      BooleanSetting var10006 = this.ladder;
      Objects.requireNonNull(var10006);
      this.onlyHard = this.add(new BooleanSetting("OnlyHard", true, var10006::isOpen));
      this.itemFrame = this.add((new BooleanSetting("ItemFrame", true)).setParent());
      var10006 = this.itemFrame;
      Objects.requireNonNull(var10006);
      this.fill = this.add(new BooleanSetting("Fill", false, var10006::isOpen));
      this.scaffolding = this.add(new BooleanSetting("Scaffolding", true));
      this.rotate = this.add(new BooleanSetting("Rotate", true));
      this.eatingPause = this.add(new BooleanSetting("EatingPause", true));
      this.collideSkip = this.add(new BooleanSetting("CollideSkip", true));
      this.crawlingSkip = this.add(new BooleanSetting("CrawlingSkip", true));
      this.onlyGround = this.add(new BooleanSetting("InAirSkip", false));
      this.targetRange = this.add(new SliderSetting("TargetRange", (double)5.0F, (double)0.0F, (double)7.0F, 0.1));
      this.delay = this.add(new SliderSetting("Delay", (double)100.0F, (double)0.0F, (double)2000.0F, (double)1.0F));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true));
      this.timer = new Timer();
      this.setChinese("反穿墙");
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if ((!this.eatingPause.getValue() || !mc.field_1724.method_6115()) && (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue())) {
         for(class_1297 target : CombatUtil.getEnemies(this.targetRange.getValue())) {
            if ((!this.crawlingSkip.getValue() || !target.method_20448()) && (!this.onlyGround.getValue() || target.method_24828()) && (!this.collideSkip.getValue() || !BlockUtil.canCollide(target, target.method_5829())) && this.timer.passed((long)this.delay.getValueInt())) {
               if (this.scaffolding.getValue() && BlockUtil.canReplace(target.method_24515())) {
                  int block = this.getScaffolding();
                  if (block != -1) {
                     class_2338 bp = target.method_24515();
                     class_2350 downSide = null;
                     class_2350 placeSide = BlockUtil.getPlaceSide(bp, (double)6.0F);
                     if (placeSide != null || (downSide = this.getSideIgnore(bp.method_10074())) != null && BlockUtil.getBlock(bp.method_10074()) instanceof class_3736 && !mc.field_1724.method_5715() || mc.field_1724.method_5715() && (downSide = this.getSideOnly(bp.method_10074())) != null && BlockUtil.getBlock(bp.method_10074()) instanceof class_3736) {
                        class_243 targetPos = placeSide != null ? bp.method_10093(placeSide).method_46558().method_1031((double)placeSide.method_10153().method_10163().method_10263() * (double)0.5F, (double)placeSide.method_10153().method_10163().method_10264() * (double)0.5F, (double)placeSide.method_10153().method_10163().method_10260() * (double)0.5F) : bp.method_10074().method_46558().method_1031((double)downSide.method_10163().method_10263() * (double)0.5F, (double)downSide.method_10163().method_10264() * (double)0.5F, (double)downSide.method_10163().method_10260() * (double)0.5F);
                        double distance = mc.field_1724.method_33571().method_1022(targetPos);
                        if (distance <= this.placeRange.getValue()) {
                           int old = mc.field_1724.method_31548().field_7545;
                           this.doSwap(block);
                           if (BlockUtil.getBlock(bp.method_10074()) instanceof class_3736 && downSide != null) {
                              BlockUtil.clickBlock(bp.method_10074(), downSide, this.rotate.getValue());
                           } else {
                              BlockUtil.placeBlock(bp, this.rotate.getValue());
                           }

                           this.timer.reset();
                           if (this.inventory.getValue()) {
                              this.doSwap(block);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(old);
                           }
                        }
                     }
                  }
               }

               if (this.itemFrame.getValue() && mc.field_1687.method_22347(target.method_24515())) {
                  class_1533 itemFrameEntity = this.hasItemFrame(new class_238(target.method_24515()));
                  if (itemFrameEntity == null) {
                     int block = this.getItemFrame();
                     if (block != -1) {
                        class_2338 bp = target.method_24515().method_10074();
                        double distance = mc.field_1724.method_33571().method_1022(bp.method_61082().method_1031((double)0.0F, (double)1.0F, (double)0.0F));
                        if (distance <= this.placeRange.getValue() && BlockUtil.isStrictDirection(bp, class_2350.field_11036) && !BlockUtil.canReplace(bp) && BlockUtil.canClick(bp)) {
                           int oldx = mc.field_1724.method_31548().field_7545;
                           this.doSwap(block);
                           BlockUtil.clickBlock(bp, class_2350.field_11036, this.rotate.getValue());
                           this.timer.reset();
                           if (this.inventory.getValue()) {
                              this.doSwap(block);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(oldx);
                           }
                        }
                     }
                  }

                  if (this.fill.getValue() && itemFrameEntity != null && itemFrameEntity.method_6940().method_7960()) {
                     class_243 hitVec = MathUtil.getClosestPointToBox(mc.field_1724.method_33571(), itemFrameEntity.method_5829());
                     if (mc.field_1724.method_33571().method_1022(hitVec) <= AntiCheat.INSTANCE.ieRange.getValue()) {
                        int block = this.getObsidian();
                        if (block != -1) {
                           int oldx = mc.field_1724.method_31548().field_7545;
                           this.doSwap(block);
                           if (this.rotate.getValue()) {
                              Alien.ROTATION.snapAt(hitVec);
                           }

                           mc.field_1724.field_3944.method_52787(class_2824.method_34207(itemFrameEntity, mc.field_1724.method_5715(), class_1268.field_5808));
                           this.timer.reset();
                           if (this.inventory.getValue()) {
                              this.doSwap(block);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(oldx);
                           }

                           if (this.rotate.getValue()) {
                              Alien.ROTATION.snapBack();
                           }
                        }
                     }
                  }
               }

               if (this.ladder.getValue()) {
                  int block = this.getLadder();
                  if (block != -1 && BlockUtil.canReplace(target.method_24515())) {
                     class_2350 facing = this.targetFacing(target.method_19538());
                     if (facing != null) {
                        class_2338 bp = target.method_24515().method_10093(facing);
                        double distance = mc.field_1724.method_33571().method_1022(bp.method_46558().method_1031((double)facing.method_10153().method_10163().method_10263() * (double)0.5F, (double)facing.method_10153().method_10163().method_10264() * (double)0.5F, (double)facing.method_10153().method_10163().method_10260() * (double)0.5F));
                        if (distance <= this.placeRange.getValue()) {
                           BlockUtil.placedPos.add(target.method_24515());
                           int oldxx = mc.field_1724.method_31548().field_7545;
                           this.doSwap(block);
                           BlockUtil.clickBlock(bp, facing.method_10153(), this.rotate.getValue());
                           this.timer.reset();
                           if (this.inventory.getValue()) {
                              this.doSwap(block);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(oldxx);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   private class_2350 getSideOnly(class_2338 pos) {
      return BlockUtil.isStrictDirection(pos, class_2350.field_11036) ? class_2350.field_11036 : null;
   }

   private class_2350 getSideIgnore(class_2338 pos) {
      for(class_2350 i : class_2350.values()) {
         if (i != class_2350.field_11036 && BlockUtil.isStrictDirection(pos, i)) {
            return i;
         }
      }

      return null;
   }

   private class_1533 hasItemFrame(class_238 box) {
      for(class_1297 entity : BlockUtil.getEntities(box)) {
         if (entity instanceof class_1533 itemFrameEntity) {
            if (entity.method_58149() == class_2350.field_11036) {
               return itemFrameEntity;
            }
         }
      }

      return null;
   }

   private static class_238 getBox(class_2350 facing, class_2338 bp) {
      class_238 box = null;
      double wide = (double)0.1875F;
      double x = (double)facing.method_10148() * (double)0.5F + (double)bp.method_10263() + (double)0.5F;
      double y = (double)bp.method_10264();
      double z = (double)facing.method_10165() * (double)0.5F + (double)bp.method_10260() + (double)0.5F;
      switch (facing) {
         case field_11039 -> box = new class_238(x, y, z, x + wide, y + (double)1.0F, z + (double)1.0F);
         case field_11034 -> box = new class_238(x, y, z, x - wide, y + (double)1.0F, z + (double)1.0F);
         case field_11043 -> box = new class_238(x, y, z, x + (double)1.0F, y + (double)1.0F, z + wide);
         case field_11035 -> box = new class_238(x, y, z, x + (double)1.0F, y + (double)1.0F, z - wide);
      }

      return box;
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private int getFlintAndSteel() {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8884) : InventoryUtil.findItem(class_1802.field_8884);
   }

   private int getObsidian() {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8281) : InventoryUtil.findItem(class_1802.field_8281);
   }

   private int getItemFrame() {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8143) : InventoryUtil.findItem(class_1802.field_8143);
   }

   private int getLadder() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_9983) : InventoryUtil.findBlock(class_2246.field_9983);
   }

   private int getScaffolding() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_16492) : InventoryUtil.findBlock(class_2246.field_16492);
   }

   private class_2350 targetFacing(class_243 vec3d) {
      class_2338 blockPos = new BlockPosX(vec3d);
      class_243 centerPos = blockPos.method_61082();
      float factorValue = 0.4F;
      double minDistance = Double.MAX_VALUE;
      class_2350 facing = null;

      for(class_2350 direction : class_2350.values()) {
         if (direction != class_2350.field_11036 && direction != class_2350.field_11033) {
            class_2338 bp = blockPos.method_10093(direction);
            if (BlockUtil.isStrictDirection(bp, direction.method_10153())) {
               if (this.onlyHard.getValue()) {
                  if (!Alien.HOLE.isHard(bp)) {
                     continue;
                  }
               } else if (BlockUtil.canReplace(bp) || !BlockUtil.canClick(bp)) {
                  continue;
               }

               class_238 box = getBox(direction, blockPos);
               if (box != null && !hasEntity(box)) {
                  class_243 tempPos = centerPos.method_1031((double)((float)direction.method_10148() * factorValue), (double)0.0F, (double)((float)direction.method_10165() * factorValue));
                  double distance = tempPos.method_1022(vec3d);
                  if (distance < minDistance) {
                     minDistance = distance;
                     facing = direction;
                  }
               }
            }
         }
      }

      return facing;
   }

   public static boolean hasEntity(class_238 box) {
      for(class_1297 entity : BlockUtil.getEntities(box)) {
         if (entity.method_5805() && !(entity instanceof class_1542) && !(entity instanceof class_1303) && !(entity instanceof class_1683) && !(entity instanceof class_1667) && !(entity instanceof class_1533)) {
            return true;
         }
      }

      return false;
   }
}
