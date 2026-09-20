package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.move.MoveUtils;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.rotation.Rotation;
import dev.luminous.api.utils.rotation.RotationUtils;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_2269;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;

public class AutoButton extends Module {
   public static AutoButton INSTANCE;
   private final SliderSetting delay = this.add((new SliderSetting("Delay", 100, 0, 500)).setSuffix("ms"));
   private final SliderSetting range = this.add((new SliderSetting("Range", (double)5.0F, (double)0.0F, (double)8.0F)).setSuffix("m"));
   private final SliderSetting placeRange = this.add((new SliderSetting("PlaceRange", (double)4.5F, (double)0.0F, (double)6.0F)).setSuffix("m"));
   private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting ghostHand = this.add(new BooleanSetting("GhostHand", true));
   private final BooleanSetting movementFix = this.add(new BooleanSetting("MovementFix", true));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
   private final BooleanSetting pauseEat = this.add(new BooleanSetting("PauseEat", true));
   private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", false));
   private final Timer timer = new Timer();

   public AutoButton() {
      super("AutoButton", Module.Category.Combat);
      this.setChinese("自动按钮");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passed((long)this.delay.getValue())) {
         if (!this.inventorySwap.getValue() || EntityUtil.inInventory()) {
            class_1657 target = CombatUtil.getClosestEnemy(this.range.getValue());
            if (target != null) {
               class_2338 pos = EntityUtil.getEntityPos(target, true);
               if (pos.method_10264() < 320) {
                  if (!this.shouldSkipPlace(pos)) {
                     int buttonSlot = this.getButtonSlot();
                     if (buttonSlot != -1) {
                        int oldSlot = mc.field_1724.method_31548().field_7545;
                        this.doSwap(buttonSlot);
                        if (this.movementFix.getValue()) {
                           class_2350 side = BlockUtil.allowAirPlace() ? BlockUtil.getClickSide(pos) : BlockUtil.getPlaceSide(pos, this.placeRange.getValue());
                           if (side != null) {
                              class_243 hitVec = pos.method_46558().method_1031((double)side.method_10163().method_10263() * (double)0.5F, (double)side.method_10163().method_10264() * (double)0.5F, (double)side.method_10163().method_10260() * (double)0.5F);
                              Rotation rotation = RotationUtils.calculate(hitVec);
                              MoveUtils.fixMovement(rotation.getYaw());
                           }
                        }

                        BlockUtil.placeBlock(pos, true);
                        if (this.inventorySwap.getValue()) {
                           this.doSwap(buttonSlot);
                           EntityUtil.syncInventory();
                        } else {
                           this.doSwap(oldSlot);
                        }

                        this.timer.reset();
                        if (!this.shouldPause() && this.timer.passed((long)this.delay.getValue())) {
                           target = CombatUtil.getClosestEnemy(this.range.getValue());
                           if (target == null && this.autoDisable.getValue()) {
                              this.disable();
                           }
                        }

                     }
                  }
               }
            }
         }
      }
   }

   private boolean shouldSkipPlace(class_2338 pos) {
      if (!BlockUtil.canReplace(pos)) {
         return true;
      } else {
         if (this.ghostHand.getValue()) {
            if (this.hasEntityIgnoringPlayers(pos)) {
               return true;
            }
         } else if (BlockUtil.hasEntity(pos, false)) {
            return true;
         }

         if (this.placeRange.getValue() > (double)0.0F && mc.field_1724.method_33571().method_1022(pos.method_46558()) > this.placeRange.getValue()) {
            return true;
         } else {
            return BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) == null && !BlockUtil.allowAirPlace();
         }
      }
   }

   private boolean hasEntityIgnoringPlayers(class_2338 pos) {
      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (entity != null && entity.method_5805() && !(entity instanceof class_1542) && !(entity instanceof class_1303) && !(entity instanceof class_1683) && !(entity instanceof class_1667) && !(entity instanceof class_1657)) {
            return true;
         }
      }

      return false;
   }

   private void doSwap(int slot) {
      if (this.inventorySwap.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private int getButtonSlot() {
      return this.inventorySwap.getValue() ? InventoryUtil.findClassInventorySlot(class_2269.class) : InventoryUtil.findClass(class_2269.class);
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
}
