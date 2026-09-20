package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.PredictUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2533;
import net.minecraft.class_2868;

public class TrapdoorAura extends Module {
   public static TrapdoorAura INSTANCE;
   public class_243 directionVec = null;
   private final EnumSetting<Page> page;
   private final SliderSetting delay;
   private final SliderSetting range;
   private final SliderSetting placeRange;
   private final SliderSetting predictTicks;
   private final SliderSetting closeDelay;
   private final BooleanSetting autoDisable;
   private final BooleanSetting ignoreNakeds;
   private final BooleanSetting antiStandUp;
   private final BooleanSetting ghostHand;
   private final BooleanSetting rotate;
   private final BooleanSetting yawStep;
   private final BooleanSetting whenElytra;
   private final SliderSetting steps;
   private final BooleanSetting checkFov;
   private final SliderSetting fov;
   private final SliderSetting priority;
   private final BooleanSetting silent;
   private final BooleanSetting inventory;
   private final BooleanSetting usingPause;
   private final BooleanSetting pauseEat;
   private final Timer timer;
   private final Timer closeTimer;
   private boolean waitingClose;
   private class_2338 pendingClosePos;
   private class_2338 completedPos;
   private class_1657 currentTarget;

   public TrapdoorAura() {
      super("TrapdoorAura", Module.Category.Combat);
      this.page = this.add(new EnumSetting("Page", TrapdoorAura.Page.General));
      this.delay = this.add((new SliderSetting("Delay", 100, 0, 500, () -> this.page.is(TrapdoorAura.Page.General))).setSuffix("ms"));
      this.range = this.add((new SliderSetting("Range", (double)5.0F, (double)0.0F, (double)8.0F, () -> this.page.is(TrapdoorAura.Page.General))).setSuffix("m"));
      this.placeRange = this.add((new SliderSetting("PlaceRange", (double)4.5F, (double)0.0F, (double)6.0F, () -> this.page.is(TrapdoorAura.Page.General))).setSuffix("m"));
      this.predictTicks = this.add(new SliderSetting("PredictTicks", (double)2.0F, (double)0.0F, (double)50.0F, (double)1.0F, () -> this.page.is(TrapdoorAura.Page.General)));
      this.closeDelay = this.add((new SliderSetting("CloseDelay", 75, 0, 500, () -> this.page.is(TrapdoorAura.Page.General))).setSuffix("ms"));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", false, () -> this.page.is(TrapdoorAura.Page.General)));
      this.ignoreNakeds = this.add(new BooleanSetting("IgnoreNakeds", false, () -> this.page.is(TrapdoorAura.Page.General)));
      this.antiStandUp = this.add(new BooleanSetting("AntiStandUp", true, () -> this.page.is(TrapdoorAura.Page.General)));
      this.ghostHand = this.add(new BooleanSetting("GhostHand", true, () -> this.page.is(TrapdoorAura.Page.General)));
      this.rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.is(TrapdoorAura.Page.Rotate)));
      this.yawStep = this.add((new BooleanSetting("YawStep", false, () -> this.rotate.isOpen() && this.page.is(TrapdoorAura.Page.Rotate))).setParent());
      this.whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.is(TrapdoorAura.Page.Rotate)));
      this.steps = this.add(new SliderSetting("Steps", 0.05, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(TrapdoorAura.Page.Rotate) && this.yawStep.isOpen()));
      this.checkFov = this.add((new BooleanSetting("OnlyLooking", true, () -> this.page.is(TrapdoorAura.Page.Rotate) && this.yawStep.isOpen())).setParent());
      this.fov = this.add(new SliderSetting("Fov", (double)20.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.checkFov.isOpen() && this.page.is(TrapdoorAura.Page.Rotate) && this.yawStep.isOpen()));
      this.priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.page.is(TrapdoorAura.Page.Rotate) && this.yawStep.isOpen()));
      this.silent = this.add(new BooleanSetting("Silent", false, () -> this.page.is(TrapdoorAura.Page.Swap)));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(TrapdoorAura.Page.Swap)));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.page.is(TrapdoorAura.Page.Misc)));
      this.pauseEat = this.add(new BooleanSetting("PauseEat", true, () -> this.page.is(TrapdoorAura.Page.Misc)));
      this.timer = new Timer();
      this.closeTimer = new Timer();
      this.waitingClose = false;
      this.pendingClosePos = null;
      this.completedPos = null;
      this.currentTarget = null;
      this.setChinese("活板门光环");
      INSTANCE = this;
   }

   public String getInfo() {
      return this.currentTarget != null ? this.currentTarget.method_5477().getString() : null;
   }

   public void onEnable() {
      this.reset();
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
      this.directionVec = null;
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.directionVec != null && this.rotate.getValue() && this.shouldYawStep()) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (!event.isPost()) {
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
                     int predict = (int)this.predictTicks.getValue();
                     class_243 targetPos = this.predictTicks.getValue() > (double)0.0F ? PredictUtil.getPos(target, predict) : target.method_19538();
                     BlockPosX trapdoorPos = new BlockPosX(targetPos.method_10216(), targetPos.method_10214() + (double)1.0F, targetPos.method_10215());
                     if (trapdoorPos.method_10264() < 320 && trapdoorPos.method_10264() >= -64) {
                        if (this.isTrapdoor(trapdoorPos)) {
                           this.handleExistingTrapdoor(trapdoorPos);
                        } else {
                           if (this.completedPos != null && this.completedPos.equals(trapdoorPos)) {
                              this.completedPos = null;
                           }

                           this.placeBlockOnTarget(trapdoorPos);
                        }
                     }
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

   private boolean shouldYawStep() {
      return !this.whenElytra.getValue() && mc.field_1724.method_6128() ? false : this.yawStep.getValue();
   }

   private void handleExistingTrapdoor(class_2338 trapdoorPos) {
      boolean isOpen = (Boolean)mc.field_1687.method_8320(trapdoorPos).method_11654(class_2533.field_11631);
      if (isOpen) {
         if (this.toggleTrapdoor(trapdoorPos, false)) {
            this.completedPos = trapdoorPos;
            this.timer.reset();
         }
      } else {
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

   private void placeBlockOnTarget(class_2338 trapdoorPos) {
      if (this.canPlaceBlock(trapdoorPos)) {
         int slot = this.findTrapdoorSlot();
         if (slot != -1) {
            int oldSlot = mc.field_1724.method_31548().field_7545;
            this.selectSlot(slot, oldSlot);
            BlockUtil.placeBlock(trapdoorPos, this.rotate.getValue());
            this.restoreSlot(slot, oldSlot);
            if (this.antiStandUp.getValue()) {
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

         if (this.placeRange.getValue() > (double)0.0F && mc.field_1724.method_19538().method_1022(pos.method_46558()) > this.placeRange.getValue()) {
            return false;
         } else if (BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) == null && !BlockUtil.allowAirPlace()) {
            return false;
         } else {
            return true;
         }
      }
   }

   private int findTrapdoorSlot() {
      return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(class_2533.class) : InventoryUtil.findClass(class_2533.class);
   }

   private boolean toggleTrapdoor(class_2338 trapdoorPos, boolean desiredOpen) {
      if (!this.isTrapdoor(trapdoorPos)) {
         return false;
      } else {
         boolean isOpen = (Boolean)mc.field_1687.method_8320(trapdoorPos).method_11654(class_2533.field_11631);
         if (isOpen == desiredOpen) {
            return false;
         } else if (this.placeRange.getValue() > (double)0.0F && mc.field_1724.method_19538().method_1022(trapdoorPos.method_46558()) > this.placeRange.getValue()) {
            return false;
         } else {
            class_2350 side = BlockUtil.getClickSide(trapdoorPos);
            if (side == null) {
               return false;
            } else {
               BlockUtil.clickBlock(trapdoorPos, side, this.rotate.getValue());
               return true;
            }
         }
      }
   }

   private boolean faceVector(class_243 vec) {
      if (!this.shouldYawStep()) {
         Astra.ROTATION.lookAt(vec);
         return true;
      } else {
         this.directionVec = vec;
         if (Astra.ROTATION.inFov(vec, this.fov.getValueFloat())) {
            return true;
         } else {
            return !this.checkFov.getValue();
         }
      }
   }

   private boolean isTrapdoor(class_2338 pos) {
      return mc.field_1687.method_8320(pos).method_26204() instanceof class_2533;
   }

   private boolean hasEntityIgnoringPlayers(class_2338 pos) {
      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (entity != null && entity.method_5709() && !(entity instanceof class_1657)) {
            return true;
         }
      }

      return false;
   }

   private boolean isNaked(class_1657 player) {
      for(int i = 0; i < 4; ++i) {
         if (!((class_1799)player.method_31548().field_7548.get(i)).method_7960()) {
            return false;
         }
      }

      return true;
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

   public static enum Page {
      General,
      Rotate,
      Swap,
      Misc;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Rotate, Swap, Misc};
      }
   }
}
