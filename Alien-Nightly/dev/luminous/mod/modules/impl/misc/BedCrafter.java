package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1713;
import net.minecraft.class_1714;
import net.minecraft.class_1748;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_3532;
import net.minecraft.class_516;
import net.minecraft.class_8786;

public class BedCrafter extends Module {
   public static BedCrafter INSTANCE;
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", false));
   private final SliderSetting range = this.add(new SliderSetting("Range", 5, 0, 8));
   private final BooleanSetting disable = this.add(new BooleanSetting("Disable", true));

   public BedCrafter() {
      super("BedCrafter", Module.Category.Misc);
      this.setChinese("自动制作床");
      INSTANCE = this;
   }

   public static int getEmptySlots() {
      int emptySlots = 0;

      for(int i = 0; i < 36; ++i) {
         class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
         if (itemStack.method_7960()) {
            ++emptySlots;
         }
      }

      return emptySlots;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (getEmptySlots() == 0) {
         if (mc.field_1724.field_7512 instanceof class_1714) {
            mc.field_1724.method_7346();
         }

         if (this.disable.getValue()) {
            this.disable();
         }
      } else if (mc.field_1724.field_7512 instanceof class_1714) {
         boolean craft = false;

         for(class_516 recipeResult : mc.field_1724.method_3130().method_1393()) {
            for(class_8786<?> recipe : recipeResult.method_2648(true)) {
               if (recipe.comp_1933().method_8110(mc.field_1687.method_30349()).method_7909() instanceof class_1748) {
                  for(int i = 0; i < getEmptySlots(); ++i) {
                     craft = true;
                     mc.field_1761.method_2912(mc.field_1724.field_7512.field_7763, recipe, false);
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 0, 1, class_1713.field_7794, mc.field_1724);
                  }
                  break;
               }
            }
         }

         if (!craft) {
            if (mc.field_1724.field_7512 instanceof class_1714) {
               mc.field_1724.method_7346();
            }

            if (this.disable.getValue()) {
               this.disable();
            }
         }
      } else {
         this.doPlace();
      }

   }

   private void doPlace() {
      class_2338 bestPos = null;
      double distance = (double)100.0F;
      boolean place = true;

      for(class_2338 pos : BlockUtil.getSphere(this.range.getValueFloat())) {
         if (mc.field_1687.method_8320(pos).method_26204() == class_2246.field_9980 && BlockUtil.getClickSideStrict(pos) != null) {
            place = false;
            bestPos = pos;
            break;
         }

         if (BlockUtil.canPlace(pos) && (bestPos == null || (double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558())) < distance)) {
            bestPos = pos;
            distance = (double)class_3532.method_15355((float)mc.field_1724.method_5707(pos.method_46558()));
         }
      }

      if (bestPos != null) {
         if (!place) {
            BlockUtil.clickBlock(bestPos, BlockUtil.getClickSide(bestPos), this.rotate.getValue());
         } else {
            if (InventoryUtil.findItem(class_1802.field_8465) == -1) {
               return;
            }

            int old = mc.field_1724.method_31548().field_7545;
            InventoryUtil.switchToSlot(InventoryUtil.findItem(class_1802.field_8465));
            BlockUtil.placeBlock(bestPos, this.rotate.getValue());
            InventoryUtil.switchToSlot(old);
         }
      }

   }
}
