package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.PredictUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.move.MoveUtils;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.rotation.Rotation;
import dev.luminous.api.utils.rotation.RotationUtils;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1667;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2533;
import net.minecraft.class_2680;
import net.minecraft.class_2868;

public class TrapdoorAura extends Module {
   public static TrapdoorAura INSTANCE;
   public class_1657 target;
   private final SliderSetting delay = this.add((new SliderSetting("Delay", 100, 0, 500)).setSuffix("ms"));
   private final SliderSetting range = this.add((new SliderSetting("Range", (double)5.0F, (double)0.0F, (double)8.0F)).setSuffix("m"));
   private final SliderSetting placeRange = this.add((new SliderSetting("PlaceRange", (double)4.5F, (double)0.0F, (double)6.0F)).setSuffix("m"));
   private final SliderSetting predictTicks = this.add(new SliderSetting("PredictTicks", (double)2.0F, (double)0.0F, (double)50.0F, (double)1.0F));
   private final SliderSetting closeDelay = this.add((new SliderSetting("CloseDelay", 75, 0, 500)).setSuffix("ms"));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting movementFix = this.add(new BooleanSetting("MovementFix", true));
   private final BooleanSetting silent = this.add(new BooleanSetting("Silent", false));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting ghostHand = this.add(new BooleanSetting("GhostHand", true));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
   private final BooleanSetting pauseEat = this.add(new BooleanSetting("PauseEat", true));
   private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", false));
   private final BooleanSetting ignoreNakeds = this.add(new BooleanSetting("IgnoreNakeds", false));
   private final BooleanSetting antiStandUp = this.add(new BooleanSetting("AntiStandUp", true));
   private final BooleanSetting placeOtherBlocks = this.add(new BooleanSetting("PlaceOtherBlocks", true));
   private final BooleanSetting useTrapdoors = this.add(new BooleanSetting("UseTrapdoors", true));
   private final BooleanSetting useWebs = this.add(new BooleanSetting("UseWebs", false));
   private final BooleanSetting useLadders = this.add(new BooleanSetting("UseLadders", false));
   private final BooleanSetting useVines = this.add(new BooleanSetting("UseVines", false));
   private final BooleanSetting useScaffolds = this.add(new BooleanSetting("UseScaffolds", false));
   private final Timer timer = new Timer();
   private final Timer closeTimer = new Timer();
   private boolean waitingClose = false;
   private class_2338 pendingClosePos = null;
   private class_2338 completedPos = null;
   private class_1657 currentTarget = null;
   private static final Set<Class<? extends class_2248>> TRAPDOOR_TYPES = new HashSet();

   public TrapdoorAura() {
      super("TrapdoorAura", Module.Category.Combat);
      this.setChinese("活板门光环");
      INSTANCE = this;
   }

   public boolean onEnable() {
      this.target = null;
      return false;
   }

   public void onDisable() {
      this.reset();
   }

   private void reset() {
      this.waitingClose = false;
      this.pendingClosePos = null;
      this.completedPos = null;
      this.currentTarget = null;
      this.timer.reset();
      this.closeTimer.reset();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.waitingClose) {
         if (this.pendingClosePos != null && this.closeTimer.passed((long)this.closeDelay.getValue())) {
            if (this.toggleTrapdoor(this.pendingClosePos, false)) {
               this.completedPos = this.pendingClosePos;
               this.timer.reset();
            }

            this.pendingClosePos = null;
            this.waitingClose = false;
         }
      } else if (!this.shouldPause() && this.timer.passed((long)this.delay.getValue())) {
         class_1657 target = CombatUtil.getClosestEnemy(this.range.getValue());
         if (target == null) {
            if (this.autoDisable.getValue()) {
               this.disable();
            }

            this.currentTarget = null;
         } else {
            this.currentTarget = target;
            if (!this.ignoreNakeds.getValue() || !this.isNaked(target)) {
               class_243 targetPos = this.predictTicks.getValue() > (double)0.0F ? PredictUtil.getPos(target, this.predictTicks.getValueInt()) : target.method_19538();
               class_2338 trapdoorPos = new BlockPosX(targetPos.method_10216(), targetPos.method_10214() + (double)1.0F, targetPos.method_10215());
               if (trapdoorPos.method_10264() < 320 && trapdoorPos.method_10264() >= -64) {
                  if (this.isTrapdoor(trapdoorPos)) {
                     this.handleExistingTrapdoor(trapdoorPos);
                  } else {
                     if (this.completedPos != null && this.completedPos.equals(trapdoorPos)) {
                        this.completedPos = null;
                     }

                     this.placeBlockOnTarget(trapdoorPos, targetPos);
                  }
               }
            }
         }
      }

   }

   private boolean shouldPause() {
      if (Blink.INSTANCE != null && Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
         return true;
      } else if (this.usingPause.getValue() && mc.field_1724.method_6115()) {
         return true;
      } else {
         return this.pauseEat.getValue() && mc.field_1724.method_6115();
      }
   }

   private void handleExistingTrapdoor(class_2338 trapdoorPos) {
      class_2680 state = mc.field_1687.method_8320(trapdoorPos);
      if (state.method_26204() instanceof class_2533) {
         boolean isOpen = Boolean.TRUE.equals(state.method_11654(class_2533.field_11631));
         if (isOpen) {
            if (this.toggleTrapdoor(trapdoorPos, false)) {
               this.completedPos = trapdoorPos;
               this.timer.reset();
            }

            return;
         }

         if (this.completedPos != null && this.completedPos.equals(trapdoorPos)) {
            return;
         }

         if (this.toggleTrapdoor(trapdoorPos, true)) {
            this.waitingClose = true;
            this.pendingClosePos = trapdoorPos;
            this.closeTimer.reset();
            this.timer.reset();
         }
      }

   }

   private void placeBlockOnTarget(class_2338 trapdoorPos, class_243 targetPos) {
      if (this.canPlaceBlock(trapdoorPos)) {
         int slot = this.findSuitableSlot(trapdoorPos);
         if (slot != -1) {
            int oldSlot = mc.field_1724.method_31548().field_7545;
            this.selectSlot(slot, oldSlot);
            this.placeBlock(trapdoorPos, slot);
            this.restoreSlot(slot, oldSlot);
            class_2248 block = mc.field_1687.method_8320(trapdoorPos).method_26204();
            if (block instanceof class_2533 && this.antiStandUp.getValue()) {
               this.waitingClose = true;
               this.pendingClosePos = trapdoorPos;
               this.closeTimer.reset();
            }

            this.timer.reset();
         }
      }

   }

   private boolean canPlaceBlock(class_2338 pos) {
      if (!BlockUtil.canReplace(pos)) {
         return false;
      } else {
         if (this.ghostHand.getValue()) {
            if (this.hasEntityIgnoringPlayers(pos)) {
               return false;
            }
         } else if (BlockUtil.hasEntity(pos, false)) {
            return false;
         }

         if (this.placeRange.getValue() > (double)0.0F && mc.method_1560().method_19538().method_1022(pos.method_46558()) > this.placeRange.getValue()) {
            return false;
         } else if (BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) == null && !BlockUtil.allowAirPlace()) {
            return false;
         } else {
            class_2338 breakPos = SpeedMine.getBreakPos();
            return SpeedMine.INSTANCE == null || !SpeedMine.INSTANCE.isOn() || breakPos == null || !pos.equals(breakPos);
         }
      }
   }

   private int findSuitableSlot(class_2338 targetPos) {
      if (this.useTrapdoors.getValue()) {
         int trapdoorSlot = this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(class_2533.class) : InventoryUtil.findClass(class_2533.class);
         if (trapdoorSlot != -1) {
            return trapdoorSlot;
         }
      }

      if (this.placeOtherBlocks.getValue()) {
         if (this.useWebs.getValue()) {
            int slot = this.findBlockSlot(class_2246.field_10343);
            if (slot != -1) {
               return slot;
            }
         }

         if (this.useLadders.getValue()) {
            int slot = this.findBlockSlot(class_2246.field_9983);
            if (slot != -1) {
               return slot;
            }
         }

         if (this.useVines.getValue()) {
            int slot = this.findBlockSlot(class_2246.field_10597);
            if (slot != -1) {
               return slot;
            }
         }

         if (this.useScaffolds.getValue()) {
            int slot = this.findBlockSlot(class_2246.field_16492);
            if (slot != -1) {
               return slot;
            }
         }
      }

      return -1;
   }

   private int findBlockSlot(class_2248 block) {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(block) : InventoryUtil.findBlock(block);
   }

   private void placeBlock(class_2338 pos, int slot) {
      if (this.isTrapdoorItem(slot)) {
         BlockUtil.placeBlock(pos, this.rotate.getValue());
      } else {
         BlockUtil.placeBlock(pos, this.rotate.getValue());
      }

   }

   private boolean isTrapdoorItem(int slot) {
      if (slot >= 0 && slot < mc.field_1724.method_31548().field_7547.size()) {
         class_1799 stack = (class_1799)mc.field_1724.method_31548().field_7547.get(slot);
         if (!stack.method_7960() && stack.method_7909() instanceof class_1747) {
            class_2248 block = ((class_1747)stack.method_7909()).method_7711();
            return block instanceof class_2533;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean toggleTrapdoor(class_2338 trapdoorPos, boolean desiredOpen) {
      class_2680 state = mc.field_1687.method_8320(trapdoorPos);
      if (!(state.method_26204() instanceof class_2533)) {
         return false;
      } else {
         boolean isOpen = Boolean.TRUE.equals(state.method_11654(class_2533.field_11631));
         if (isOpen == desiredOpen) {
            return false;
         } else if (this.placeRange.getValue() > (double)0.0F && mc.method_1560().method_19538().method_1022(trapdoorPos.method_46558()) > this.placeRange.getValue()) {
            return false;
         } else {
            class_2350 clickSide = BlockUtil.getClickSide(trapdoorPos);
            if (clickSide == null) {
               return false;
            } else {
               if (this.movementFix.getValue() && this.rotate.getValue()) {
                  class_243 hitVec = trapdoorPos.method_46558().method_1031((double)clickSide.method_10163().method_10263() * (double)0.5F, (double)clickSide.method_10163().method_10264() * (double)0.5F, (double)clickSide.method_10163().method_10260() * (double)0.5F);
                  Rotation rotation = RotationUtils.calculate(hitVec);
                  MoveUtils.fixMovement(rotation.getYaw());
               }

               BlockUtil.clickBlock(trapdoorPos, clickSide, this.rotate.getValue());
               return true;
            }
         }
      }
   }

   private boolean isTrapdoor(class_2338 pos) {
      return mc.field_1687.method_8320(pos).method_26204() instanceof class_2533;
   }

   private boolean hasEntityIgnoringPlayers(class_2338 pos) {
      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (entity != null && entity.method_5805() && !(entity instanceof class_1542) && !(entity instanceof class_1667) && !(entity instanceof class_1657)) {
            return true;
         }
      }

      return false;
   }

   private boolean isNaked(class_1657 player) {
      return player.method_31548().method_5438(0).method_7960() && player.method_31548().method_5438(1).method_7960() && player.method_31548().method_5438(2).method_7960() && player.method_31548().method_5438(3).method_7960();
   }

   private void selectSlot(int slot, int selectedSlot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, selectedSlot);
      } else if (this.silent.getValue()) {
         mc.method_1562().method_52787(new class_2868(slot));
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private void restoreSlot(int slot, int selectedSlot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, selectedSlot);
         EntityUtil.syncInventory();
      } else if (this.silent.getValue()) {
         mc.method_1562().method_52787(new class_2868(selectedSlot));
      } else {
         InventoryUtil.switchToSlot(selectedSlot);
      }

   }

   public class_1657 getCurrentTarget() {
      return this.currentTarget;
   }

   public String getStatus() {
      if (this.currentTarget == null) {
         return "等待目标";
      } else {
         return this.waitingClose ? "关闭等待中" : "激活中";
      }
   }

   static {
      TRAPDOOR_TYPES.add(class_2533.class);
   }
}
