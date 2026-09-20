package nekiplay.meteorplus.features.modules.player;

import java.util.List;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.SlotUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1733;
import net.minecraft.class_1792;
import net.minecraft.class_1799;

public class AutoDropPlus extends Module {
   private final SettingGroup defaultGroup;
   private final Setting<List<class_1792>> items;
   private final Setting<Integer> delay;
   private final Setting<Boolean> workInstant;
   private final Setting<Boolean> removeContainersItems;
   private final Setting<Boolean> autoDropExcludeHotbar;
   private final Setting<Boolean> removeItems;
   private int tick;

   public AutoDropPlus() {
      super(Categories.Player, "auto-drop", "Auto drop items in inventory.");
      this.defaultGroup = this.settings.getDefaultGroup();
      this.items = this.defaultGroup.add(((ItemListSetting.Builder)((ItemListSetting.Builder)(new ItemListSetting.Builder()).name("drop-items")).description("Items to dropping.")).build());
      Setting<Boolean>[] workInstantRef = new Setting[1];
      this.delay = this.defaultGroup.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("delay")).description("drop delay.")).defaultValue(5)).min(0).onChanged((value) -> {
         if (value != 0 && workInstantRef[0] != null && (Boolean)workInstantRef[0].get()) {
            workInstantRef[0].set(false);
         }

      })).build());
      workInstantRef[0] = this.defaultGroup.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("instant-work")).description("Drop or remove items instant.")).defaultValue(false)).visible(() -> (Integer)this.delay.get() == 0)).build());
      this.workInstant = workInstantRef[0];
      this.removeContainersItems = this.defaultGroup.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("work-in-containers")).description("Work in chests?.")).defaultValue(true)).build());
      this.autoDropExcludeHotbar = this.defaultGroup.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("work-in-hotbar")).description("Work in hotbar?.")).defaultValue(true)).build());
      this.removeItems = this.defaultGroup.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("remover")).description("Remove items?.")).defaultValue(false)).build());
      this.tick = 0;
   }

   @EventHandler
   public void onTickPost(TickEvent.Pre event) {
      int sync = this.mc.field_1724.field_7512.field_7763;

      for(int i = (Boolean)this.autoDropExcludeHotbar.get() ? 0 : 9; i < this.mc.field_1724.method_31548().method_5439(); ++i) {
         class_1799 itemStack = this.mc.field_1724.method_31548().method_5438(i);
         if (((List)this.items.get()).contains(itemStack.method_7909().method_8389())) {
            if (this.tick != 0) {
               --this.tick;
            } else {
               if ((Boolean)this.removeItems.get() && sync != -1) {
                  this.mc.field_1761.method_2906(sync, invIndexToSlotId(i), 300, class_1713.field_7791, this.mc.field_1724);
               } else if (!(Boolean)this.removeItems.get()) {
                  InvUtils.drop().slot(i);
               }

               if (!(Boolean)this.workInstant.get()) {
                  this.tick = (Integer)this.delay.get();
               }
            }

            if (!(Boolean)this.workInstant.get()) {
               break;
            }
         }
      }

      if ((Boolean)this.removeContainersItems.get()) {
         for(int i = 0; i < SlotUtils.indexToId(9); ++i) {
            class_1703 handler = this.mc.field_1724.field_7512;
            if (handler.method_7611(i).method_7681()) {
               class_1792 item = handler.method_7611(i).method_7677().method_7909();
               if (((List)this.items.get()).contains(item.method_8389())) {
                  if (this.tick == 0) {
                     if ((Boolean)this.removeItems.get()) {
                        this.mc.field_1761.method_2906(handler.field_7763, getIndexToSlotId(handler, i), 300, class_1713.field_7791, this.mc.field_1724);
                     } else {
                        InvUtils.drop().slotId(i);
                     }

                     if (!(Boolean)this.workInstant.get()) {
                        this.tick = (Integer)this.delay.get();
                     }
                  } else {
                     --this.tick;
                  }

                  if (!(Boolean)this.workInstant.get()) {
                     break;
                  }
               }
            }
         }
      }

   }

   public static int invIndexToSlotId(int invIndex) {
      return invIndex < 9 && invIndex != -1 ? 44 - (8 - invIndex) : invIndex;
   }

   public static int getIndexToSlotId(class_1703 handler, int invIndex) {
      if (handler instanceof class_1707 genericContainerScreenHandler) {
         int count = genericContainerScreenHandler.field_7761.size();
         return invIndex < 0 && invIndex != -1 ? count - (-1 - invIndex) : invIndex;
      } else if (handler instanceof class_1733 genericContainerScreenHandler) {
         int count = genericContainerScreenHandler.field_7761.size();
         return invIndex < 0 && invIndex != -1 ? count - (-1 - invIndex) : invIndex;
      } else {
         return invIndex < 9 && invIndex != -1 ? 44 - (8 - invIndex) : invIndex;
      }
   }
}
