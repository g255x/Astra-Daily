package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_3616;

public class LavaFiller extends Module {
   public final SliderSetting placeDelay = this.add((new SliderSetting("PlaceDelay", 50, 0, 500)).setSuffix("ms"));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final SliderSetting range = this.add(new SliderSetting("Range", (double)5.0F, (double)0.0F, (double)8.0F, 0.1));
   private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8));
   private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", false));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true));
   private final Timer timer = new Timer();
   int progress = 0;

   public LavaFiller() {
      super("LavaFiller", Module.Category.Misc);
      this.setChinese("自动填岩浆");
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passed((long)this.placeDelay.getValue()) && (!this.inventory.getValue() || EntityUtil.inInventory())) {
         this.progress = 0;
         if (this.getBlock() != -1 && (!this.usingPause.getValue() || !mc.field_1724.method_6115())) {
            for(class_2338 pos : BlockUtil.getSphere(this.range.getValueFloat())) {
               if (mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10164 && mc.field_1687.method_8320(pos).method_26227().method_15772() instanceof class_3616.class_3618) {
                  this.tryPlaceBlock(pos);
               }
            }
         }
      }

   }

   private void tryPlaceBlock(class_2338 pos) {
      if (pos != null && (!this.detectMining.getValue() || !Alien.BREAK.isMining(pos)) && (double)this.progress < this.blocksPer.getValue()) {
         int block = this.getBlock();
         if (block != -1 && BlockUtil.canPlace(pos, this.range.getValue(), false)) {
            int old = mc.field_1724.method_31548().field_7545;
            this.doSwap(block);
            BlockUtil.placeBlock(pos, this.rotate.getValue(), this.packetPlace.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(block);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }

            ++this.progress;
            this.timer.reset();
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
}
