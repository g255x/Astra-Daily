package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1733;
import net.minecraft.class_1735;
import net.minecraft.class_2338;
import net.minecraft.class_2480;
import net.minecraft.class_495;

public class DropAntiRegear extends Module {
   private final SliderSetting delay = this.add((new SliderSetting("Delay", (double)5.0F, (double)0.0F, (double)10.0F, 0.1)).setSuffix("s"));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
   private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true));
   private final BooleanSetting detectShulkers = this.add(new BooleanSetting("DetectShulkers", true));
   private final SliderSetting range = this.add(new SliderSetting("Range", (double)4.0F, (double)0.0F, (double)6.0F, 0.1));
   private final SliderSetting disableTime = this.add(new SliderSetting("DisableTime", 500, 0, 1000));
   private final SliderSetting detectRange = this.add(new SliderSetting("DetectRange", (double)5.0F, (double)1.0F, (double)8.0F, 0.1));
   private class_2338 detectedShulkerPos = null;
   private final Timer timer = new Timer();
   private final Timer timeoutTimer = new Timer();
   private final Timer delayTimer = new Timer();
   private class_2338 placePos = null;
   private class_2338 openPos = null;
   private class_2338 safePos = null;
   private boolean opend = false;

   public DropAntiRegear() {
      super("DropAntiRegear", Module.Category.Combat);
      this.setChinese("丢弃反补给");
   }

   public boolean onEnable() {
      this.opend = false;
      this.placePos = null;
      this.openPos = null;
      this.safePos = null;
      this.timeoutTimer.reset();
      return false;
   }

   public void onDisable() {
      this.opend = false;
      if (this.mine.getValue() && this.placePos != null) {
         SpeedMine.INSTANCE.mine(this.placePos);
      }

      this.placePos = null;
      this.openPos = null;
      this.safePos = null;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (this.delayTimer.passedMs(this.delay.getValue() * (double)1000.0F)) {
            if (this.detectShulkers.getValue() && !this.opend) {
               if (this.detectedShulkerPos == null || !(mc.field_1687.method_8320(this.detectedShulkerPos).method_26204() instanceof class_2480)) {
                  this.detectedShulkerPos = this.findNearbyShulker();
               }

               if (this.detectedShulkerPos != null && !this.detectedShulkerPos.equals(this.openPos) && !this.detectedShulkerPos.equals(this.placePos)) {
                  this.openPos = this.detectedShulkerPos;
                  BlockUtil.clickBlock(this.detectedShulkerPos, BlockUtil.getClickSide(this.detectedShulkerPos), this.rotate.getValue());
               }
            }

            if (this.safePos != null && mc.field_1724.method_5707(this.safePos.method_46558()) > (double)100.0F) {
               this.safePos = null;
            }

            if (!(mc.field_1755 instanceof class_495)) {
               if (this.opend) {
                  this.opend = false;
                  if (this.autoDisable.getValue()) {
                     this.timeoutToDisable();
                  }

                  if (this.openPos != null) {
                     if (mc.field_1687.method_8320(this.openPos).method_26204() instanceof class_2480) {
                        BlockUtil.clickBlock(this.openPos, BlockUtil.getClickSide(this.openPos), this.rotate.getValue());
                     } else {
                        this.openPos = null;
                     }
                  }
               } else if (this.safePos != null && mc.field_1687.method_8320(this.safePos).method_26204() instanceof class_2480) {
                  this.openPos = this.safePos;
                  BlockUtil.clickBlock(this.safePos, BlockUtil.getClickSide(this.safePos), this.rotate.getValue());
               }
            } else {
               this.opend = true;
               class_1703 screenHandler = mc.field_1724.field_7512;
               if (screenHandler instanceof class_1733) {
                  class_1733 shulker = (class_1733)screenHandler;
                  boolean dropped = false;

                  for(class_1735 slot : shulker.field_7761) {
                     if (slot.field_7874 < 27 && !slot.method_7677().method_7960()) {
                        mc.field_1761.method_2906(shulker.field_7763, slot.field_7874, 1, class_1713.field_7795, mc.field_1724);
                        dropped = true;
                     }
                  }

                  if (!dropped) {
                     mc.field_1724.method_7346();
                  }
               }

               if (this.autoDisable.getValue()) {
                  this.timeoutToDisable();
               }

               this.delayTimer.reset();
            }
         }

      }
   }

   private void timeoutToDisable() {
      if (this.timeoutTimer.passed((long)this.disableTime.getValueInt())) {
         this.disable();
      }

   }

   private class_2338 findNearbyShulker() {
      for(class_2338 pos : BlockUtil.getSphere((float)this.detectRange.getValue())) {
         if (mc.field_1687.method_8320(pos).method_26204() instanceof class_2480 && !pos.equals(this.placePos) && !pos.equals(this.safePos)) {
            return pos;
         }
      }

      return null;
   }
}
