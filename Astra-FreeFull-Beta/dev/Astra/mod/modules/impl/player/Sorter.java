package dev.Astra.mod.modules.impl.player;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.class_1713;
import net.minecraft.class_1738;
import net.minecraft.class_1743;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1787;
import net.minecraft.class_1792;
import net.minecraft.class_1794;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1819;
import net.minecraft.class_1820;
import net.minecraft.class_1821;
import net.minecraft.class_1829;
import net.minecraft.class_1831;
import net.minecraft.class_1835;
import net.minecraft.class_2960;
import net.minecraft.class_7923;
import net.minecraft.class_9362;
import net.minecraft.class_1738.class_8051;

public class Sorter extends Module {
   public static Sorter INSTANCE;
   private final EnumSetting<Page> page;
   private final SliderSetting tasksPerTicks;
   private final SliderSetting delay;
   private final BooleanSetting drop;
   private final BooleanSetting stack;
   private final BooleanSetting sort;
   private final BooleanSetting descending;
   private final BooleanSetting trashUselessMinerals;
   private final BooleanSetting trashRawOres;
   private final BooleanSetting trashNuggets;
   private final BooleanSetting trashStones;
   private final BooleanSetting trashPlants;
   private final BooleanSetting trashMobDrops;
   private final BooleanSetting trashUselessOreBlocks;
   private final BooleanSetting trashAnimalDrops;
   private final BooleanSetting trashMisc;
   private final BooleanSetting trashEquipment;
   private final Timer timer;

   public Sorter() {
      super("Sorter", Module.Category.Player);
      this.page = this.add(new EnumSetting("Page", Sorter.Page.General));
      this.tasksPerTicks = this.add(new SliderSetting("TasksPerTick", 2, 1, 20, () -> this.page.is(Sorter.Page.General)));
      this.delay = this.add((new SliderSetting("Delay", 0.1, (double)0.0F, (double)5.0F, 0.01, () -> this.page.is(Sorter.Page.General))).setSuffix("s"));
      this.drop = this.add(new BooleanSetting("Drop", true, () -> this.page.is(Sorter.Page.General)));
      this.stack = this.add(new BooleanSetting("Stack", true, () -> this.page.is(Sorter.Page.General)));
      this.sort = this.add((new BooleanSetting("Sort", true, () -> this.page.is(Sorter.Page.General))).setParent());
      this.descending = this.add(new BooleanSetting("Descending", false, () -> this.page.is(Sorter.Page.General) && this.sort.isOpen()));
      this.trashUselessMinerals = this.add(new BooleanSetting("UselessMinerals", false, () -> this.page.is(Sorter.Page.Trash)));
      this.trashRawOres = this.add(new BooleanSetting("RawOres", false, () -> this.page.is(Sorter.Page.Trash)));
      this.trashNuggets = this.add(new BooleanSetting("Nuggets", false, () -> this.page.is(Sorter.Page.Trash)));
      this.trashStones = this.add(new BooleanSetting("Stones", false, () -> this.page.is(Sorter.Page.Trash)));
      this.trashPlants = this.add(new BooleanSetting("Plants", false, () -> this.page.is(Sorter.Page.Trash)));
      this.trashMobDrops = this.add(new BooleanSetting("MobDrops", false, () -> this.page.is(Sorter.Page.Trash)));
      this.trashUselessOreBlocks = this.add(new BooleanSetting("UselessOreBlocks", false, () -> this.page.is(Sorter.Page.Trash)));
      this.trashAnimalDrops = this.add(new BooleanSetting("AnimalDrops", false, () -> this.page.is(Sorter.Page.Trash)));
      this.trashMisc = this.add(new BooleanSetting("Misc", false, () -> this.page.is(Sorter.Page.Trash)));
      this.trashEquipment = this.add(new BooleanSetting("Equipment", false, () -> this.page.is(Sorter.Page.Trash)));
      this.timer = new Timer();
      this.setChinese("背包整理");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passedS(this.delay.getValue())) {
         if (EntityUtil.inInventory()) {
            int iterations = (int)Math.round(this.tasksPerTicks.getValue());

            for(int i = 0; i < iterations; ++i) {
               boolean actionDone = false;
               if (this.drop.getValue() && this.tryDropTrash()) {
                  actionDone = true;
               } else if (this.stack.getValue() && this.tryStackItems()) {
                  actionDone = true;
               } else if (this.sort.getValue() && this.trySortItems()) {
                  actionDone = true;
               } else if (this.trashEquipment.getValue() && this.tryOptimizeEquipment()) {
                  actionDone = true;
               }

               if (actionDone) {
                  this.timer.reset();
                  return;
               }
            }

         }
      }
   }

   private boolean tryDropTrash() {
      Set<class_1792> trashItems = this.collectTrashItems();

      for(int slot = 35; slot >= 0; --slot) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
         if (!stack.method_7960() && trashItems.contains(stack.method_7909())) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot < 9 ? slot + 36 : slot, 1, class_1713.field_7795, mc.field_1724);
            return true;
         }
      }

      return false;
   }

   private Set<class_1792> collectTrashItems() {
      Set<class_1792> trash = new HashSet();
      if (this.trashUselessMinerals.getValue()) {
         trash.addAll(Set.of(class_1802.field_8713, class_1802.field_8725, class_1802.field_8759, class_1802.field_27022, class_1802.field_8155, class_1802.field_29212, class_1802.field_29023, class_1802.field_29021, class_1802.field_29211, class_1802.field_8702));
      }

      if (this.trashRawOres.getValue()) {
         trash.addAll(Set.of(class_1802.field_33401, class_1802.field_33400, class_1802.field_33402));
      }

      if (this.trashNuggets.getValue()) {
         trash.addAll(Set.of(class_1802.field_8675, class_1802.field_8397));
      }

      if (this.trashStones.getValue()) {
         trash.addAll(Set.of(class_1802.field_20391, class_1802.field_20412, class_1802.field_20394, class_1802.field_20401, class_1802.field_20407, class_1802.field_20397, class_1802.field_20403, class_1802.field_20411, class_1802.field_20395, class_1802.field_20392, class_1802.field_20396, class_1802.field_8525, class_1802.field_20389, class_1802.field_8225, class_1802.field_8401, class_1802.field_8541, class_1802.field_8596, class_1802.field_8148, class_1802.field_8110, class_1802.field_8145, class_1802.field_27021, class_1802.field_27020, class_1802.field_28043, class_1802.field_19060, class_1802.field_8696, class_1802.field_29025, class_1802.field_28654, class_1802.field_8595, class_1802.field_8053, class_1802.field_8194, class_1802.field_8310, class_1802.field_8392, class_1802.field_8312, class_1802.field_8608, class_1802.field_8488, class_1802.field_8659, class_1802.field_8860, class_1802.field_8559, class_1802.field_8742, class_1802.field_8526, class_1802.field_8206, class_1802.field_8228, class_1802.field_8319, class_1802.field_8569, class_1802.field_8650, class_1802.field_8395, class_1802.field_8316, class_1802.field_8291, class_1802.field_8524, class_1802.field_8481, class_1802.field_8337, class_1802.field_8576, class_1802.field_8890, class_1802.field_8811, class_1802.field_8342, class_1802.field_8663, class_1802.field_8804, class_1802.field_28866, class_1802.field_28871, class_1802.field_28859, class_1802.field_28860, class_1802.field_28861, class_1802.field_28872, class_1802.field_28868, class_1802.field_28863, class_1802.field_28875, class_1802.field_28870, class_1802.field_28865, class_1802.field_28874, class_1802.field_28869, class_1802.field_28864, class_1802.field_20384, class_1802.field_20388, class_1802.field_8552, class_1802.field_20408, class_1802.field_20387, class_1802.field_8822, class_1802.field_18888, class_1802.field_8443, class_1802.field_8223, class_1802.field_18886, class_1802.field_8456, class_1802.field_8359, class_1802.field_20402, class_1802.field_20386, class_1802.field_8084, class_1802.field_8746, class_1802.field_8412, class_1802.field_8499, class_1802.field_8217, class_1802.field_8430, class_1802.field_8328, class_1802.field_23843, class_1802.field_23848, class_1802.field_23837, class_1802.field_23836, class_1802.field_22000, class_1802.field_23069, class_1802.field_29024, class_1802.field_20398, class_1802.field_20410, class_1802.field_8067, class_1802.field_21999, class_1802.field_8354, class_1802.field_8505, class_1802.field_8444, class_1802.field_8765, class_1802.field_8522, class_1802.field_8302, class_1802.field_8419, class_1802.field_23844, class_1802.field_23845, class_1802.field_23846, class_1802.field_23849, class_1802.field_23832, class_1802.field_23833, class_1802.field_20399, class_1802.field_20400, class_1802.field_20393, class_1802.field_8490, class_1802.field_8202, class_1802.field_8644, class_1802.field_8282, class_1802.field_8152, class_1802.field_8819, class_1802.field_20404, class_1802.field_20405, class_1802.field_20406, class_1802.field_8440, class_1802.field_8386, class_1802.field_8057, class_1802.field_8588, class_1802.field_8673, class_1802.field_8459, class_1802.field_8854));
      }

      if (this.trashPlants.getValue()) {
         trash.addAll(Set.of(class_1802.field_8317, class_1802.field_46249, class_1802.field_46250, class_1802.field_8309, class_1802.field_8790, class_1802.field_42711, class_1802.field_17535, class_1802.field_17536, class_1802.field_17537, class_1802.field_17538, class_1802.field_17539, class_1802.field_17540, class_1802.field_37508, class_1802.field_42688, class_1802.field_17503, class_1802.field_17504, class_1802.field_17505, class_1802.field_17506, class_1802.field_17507, class_1802.field_17508, class_1802.field_37511, class_1802.field_42694, class_1802.field_28648, class_1802.field_28649, class_1802.field_8491, class_1802.field_8880, class_1802.field_17499, class_1802.field_17500, class_1802.field_17501, class_1802.field_17502, class_1802.field_17509, class_1802.field_17510, class_1802.field_17511, class_1802.field_17512, class_1802.field_17513, class_1802.field_17514, class_1802.field_17515, class_1802.field_17525, class_1802.field_17526, class_1802.field_17527, class_1802.field_17529, class_1802.field_43192, class_1802.field_8861, class_1802.field_8179, class_1802.field_8567, class_1802.field_8186, class_1802.field_16998, class_1802.field_28659, class_1802.field_17531, class_1802.field_8648, class_1802.field_17532, class_1802.field_8551, class_1802.field_17523, class_1802.field_17524, class_1802.field_28656, class_1802.field_28657, class_1802.field_28658, class_1802.field_28652, class_1802.field_28409, class_1802.field_8689, class_1802.field_8471, class_1802.field_8561, class_1802.field_8256, class_1802.field_21987, class_1802.field_21988, class_1802.field_21989, class_1802.field_21990, class_1802.field_21991, class_1802.field_8710, class_1802.field_8358));
      }

      if (this.trashMobDrops.getValue()) {
         trash.addAll(Set.of(class_1802.field_8511, class_1802.field_8606, class_1802.field_8680, class_1802.field_8054, class_1802.field_8276, class_1802.field_8777, class_1802.field_8070, class_1802.field_8135));
      }

      if (this.trashUselessOreBlocks.getValue()) {
         trash.addAll(Set.of(class_1802.field_8476, class_1802.field_29212, class_1802.field_8604, class_1802.field_29023, class_1802.field_8809, class_1802.field_29021, class_1802.field_27018, class_1802.field_29211, class_1802.field_8599, class_1802.field_29020, class_1802.field_8775, class_1802.field_29019, class_1802.field_23140, class_1802.field_8702));
      }

      if (this.trashAnimalDrops.getValue()) {
         trash.addAll(Set.of(class_1802.field_8046, class_1802.field_8389, class_1802.field_8748, class_1802.field_8726, class_1802.field_8504, class_1802.field_8429, class_1802.field_8209, class_1802.field_8846, class_1802.field_8323, class_1802.field_8745, class_1802.field_8153, class_1802.field_8245, class_1802.field_8073));
      }

      if (this.trashMisc.getValue()) {
         trash.addAll(Set.of(class_1802.field_8831, class_1802.field_8460, class_1802.field_8382, class_1802.field_28655, class_1802.field_20390, class_1802.field_8260, class_1802.field_8156, class_1802.field_8043, class_1802.field_8783, class_1802.field_8717, class_1802.field_8385, class_1802.field_8672, class_1802.field_8853, class_1802.field_8304, class_1802.field_8133, class_1802.field_8821, class_1802.field_8715, class_1802.field_8455, class_1802.field_8467, class_1802.field_8798, class_1802.field_8353, class_1802.field_8181, class_1802.field_8582, class_1802.field_8487, class_1802.field_8336, class_1802.field_8764, class_1802.field_8205, class_1802.field_8418, class_1802.field_8222, class_1802.field_8818, class_1802.field_8558, class_1802.field_8593, class_1802.field_8690, class_1802.field_8164, class_1802.field_8437, class_1802.field_8198, class_1802.field_8757, class_1802.field_8516, class_1802.field_8280, class_1802.field_8141, class_1802.field_8483, class_1802.field_8736, class_1802.field_8393, class_1802.field_8761, class_1802.field_8243, class_1802.field_8119, class_1802.field_8869, class_1802.field_8196, class_1802.field_8095, class_1802.field_8703, class_1802.field_8340, class_1802.field_8581, class_1802.field_8770, class_1802.field_8500, class_1802.field_8507, class_1802.field_8871, class_1802.field_8363, class_1802.field_8240, class_1802.field_8685, class_1802.field_8085, class_1802.field_8838, class_1802.field_8739, class_1802.field_8126, class_1802.field_8747, class_1802.field_8332, class_1802.field_8501, class_1802.field_8734, class_1802.field_8656, class_1802.field_8636, class_1802.field_8879, class_1802.field_8410, class_1802.field_8157, class_1802.field_8850, class_1802.field_8683, class_1802.field_8384, class_1802.field_8078, class_1802.field_8142, class_1802.field_8253, class_1802.field_8580, class_1802.field_8875, class_1802.field_8654, class_1802.field_8290, class_1802.field_8098, class_1802.field_8115, class_1802.field_8294, class_1802.field_8664, class_1802.field_8482, class_1802.field_8611, class_1802.field_8539, class_1802.field_8824, class_1802.field_8671, class_1802.field_8379, class_1802.field_8049, class_1802.field_8778, class_1802.field_8329, class_1802.field_8617, class_1802.field_8855, class_1802.field_8629, class_1802.field_8405, class_1802.field_8128, class_1802.field_8124, class_1802.field_8295, class_1802.field_8586, class_1802.field_8572, class_1802.field_8398, class_1802.field_8791, class_1802.field_8470, class_1802.field_8681, class_1802.field_8712, class_1802.field_41304, class_1802.field_8892, class_1802.field_8074, class_1802.field_8694, class_1802.field_8144, class_1802.field_8075, class_1802.field_8425, class_1802.field_8623, class_1802.field_8502, class_1802.field_8534, class_1802.field_8344, class_1802.field_8834, class_1802.field_8065, class_1802.field_8355, class_1802.field_8731, class_1802.field_8806, class_1802.field_35358, class_1802.field_44705, class_1802.field_38973, class_1802.field_23984, class_1802.field_51628, class_1802.field_51629, class_1802.field_51630, class_1802.field_8691, class_1802.field_8165, class_1802.field_8438, class_1802.field_8199, class_1802.field_8758, class_1802.field_8517, class_1802.field_37528, class_1802.field_42705, class_1802.field_8376, class_1802.field_8495, class_1802.field_8774, class_1802.field_8321, class_1802.field_8190, class_1802.field_8844, class_1802.field_37529, class_1802.field_42702, class_1802.field_8792, class_1802.field_8701, class_1802.field_8457, class_1802.field_8823, class_1802.field_8646, class_1802.field_8454, class_1802.field_37520, class_1802.field_42698, class_1802.field_8874, class_1802.field_8653, class_1802.field_8289, class_1802.field_8097, class_1802.field_8114, class_1802.field_8293, class_1802.field_37532, class_1802.field_42703, class_1802.field_21981, class_1802.field_21982, class_1802.field_21983, class_1802.field_21984, class_1802.field_8182, class_1802.field_22008, class_1802.field_22031, class_1802.field_22032, class_1802.field_21985, class_1802.field_21986, class_1802.field_22006, class_1802.field_22007, class_1802.field_21995, class_1802.field_21996, class_1802.field_21997, class_1802.field_21998, class_1802.field_22010, class_1802.field_22009, class_1802.field_22002, class_1802.field_22003, class_1802.field_22004, class_1802.field_22005, class_1802.field_21993, class_1802.field_21994));
      }

      return trash;
   }

   private boolean tryStackItems() {
      for(int slot1 = 35; slot1 >= 9; --slot1) {
         class_1799 stack1 = mc.field_1724.method_31548().method_5438(slot1);
         if (!stack1.method_7960() && stack1.method_7946() && stack1.method_7947() != stack1.method_7914()) {
            for(int slot2 = 0; slot2 < 36; ++slot2) {
               if (slot1 != slot2) {
                  class_1799 stack2 = mc.field_1724.method_31548().method_5438(slot2);
                  if (!stack2.method_7960() && canMerge(stack1, stack2) && stack2.method_7947() != stack2.method_7914()) {
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot1 < 9 ? slot1 + 36 : slot1, 0, class_1713.field_7790, mc.field_1724);
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot2 < 9 ? slot2 + 36 : slot2, 0, class_1713.field_7790, mc.field_1724);
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot1 < 9 ? slot1 + 36 : slot1, 0, class_1713.field_7790, mc.field_1724);
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean trySortItems() {
      boolean reverse = this.descending.getValue();

      for(int slot = 9; slot < 36; ++slot) {
         class_1799 currentStack = mc.field_1724.method_31548().method_5438(slot);
         int currentId = currentStack.method_7960() ? 0 : class_1792.method_7880(currentStack.method_7909());
         int targetId = currentId;
         int targetSlot = slot;

         for(int other = slot + 1; other < 36; ++other) {
            class_1799 otherStack = mc.field_1724.method_31548().method_5438(other);
            int otherId = otherStack.method_7960() ? 0 : class_1792.method_7880(otherStack.method_7909());
            boolean shouldSwap = reverse ? otherId > targetId : otherId < targetId;
            if (shouldSwap) {
               targetId = otherId;
               targetSlot = other;
            }
         }

         if (targetSlot != slot) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot < 9 ? slot + 36 : slot, 0, class_1713.field_7790, mc.field_1724);
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, targetSlot < 9 ? targetSlot + 36 : targetSlot, 0, class_1713.field_7790, mc.field_1724);
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot < 9 ? slot + 36 : slot, 0, class_1713.field_7790, mc.field_1724);
            return true;
         }
      }

      return false;
   }

   private boolean tryOptimizeEquipment() {
      if (this.optimizeArmor()) {
         return true;
      } else {
         return this.optimizeTools() ? true : this.optimizeWeapons();
      }
   }

   private int getArmorMaterialLevel(class_1799 stack) {
      class_1792 var3 = stack.method_7909();
      if (var3 instanceof class_1738 armor) {
         class_2960 id = class_7923.field_41178.method_10221(armor);
         String path = id.method_12832();
         if (path.contains("netherite")) {
            return 4;
         } else if (path.contains("diamond")) {
            return 3;
         } else if (path.contains("iron")) {
            return 2;
         } else {
            return path.contains("gold") ? 1 : 0;
         }
      } else {
         return 0;
      }
   }

   private int getToolMaterialLevel(class_1799 stack) {
      class_1792 var3 = stack.method_7909();
      if (var3 instanceof class_1831 tool) {
         class_2960 id = class_7923.field_41178.method_10221(tool);
         String path = id.method_12832();
         if (path.contains("netherite")) {
            return 4;
         } else if (path.contains("diamond")) {
            return 3;
         } else if (path.contains("iron")) {
            return 2;
         } else if (!path.contains("gold") && !path.contains("stone")) {
            return path.contains("wood") ? 0 : 0;
         } else {
            return 1;
         }
      } else {
         return 0;
      }
   }

   private boolean optimizeArmor() {
      Map<class_1738.class_8051, List<class_1799>> armorMap = new EnumMap(class_1738.class_8051.class);

      for(class_1738.class_8051 type : class_8051.values()) {
         armorMap.put(type, new ArrayList());
      }

      for(int slot = 0; slot < 40; ++slot) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
         if (!stack.method_7960()) {
            class_1792 var17 = stack.method_7909();
            if (var17 instanceof class_1738) {
               class_1738 armor = (class_1738)var17;
               ((List)armorMap.get(armor.method_48398())).add(stack);
            }
         }
      }

      Set<class_1799> keep = new HashSet();

      for(class_1738.class_8051 type : class_8051.values()) {
         List<class_1799> list = (List)armorMap.get(type);
         if (!list.isEmpty()) {
            Stream var10000 = list.stream().filter((s) -> this.getArmorMaterialLevel(s) >= 3);
            Objects.requireNonNull(keep);
            var10000.forEach(keep::add);
            class_1799 best = (class_1799)list.stream().max(Comparator.comparingInt(this::getArmorMaterialLevel)).orElse((Object)null);
            if (best != null) {
               keep.add(best);
            }
         }
      }

      for(int slot = 0; slot < 36; ++slot) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
         if (!stack.method_7960() && stack.method_7909() instanceof class_1738 && !keep.contains(stack)) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot < 9 ? slot + 36 : slot, 1, class_1713.field_7795, mc.field_1724);
            return true;
         }
      }

      return false;
   }

   private boolean optimizeTools() {
      Class<?>[] toolClasses = new Class[]{class_1810.class, class_1743.class, class_1821.class, class_1794.class, class_1820.class, class_1787.class};
      return this.optimizeGeneric(toolClasses);
   }

   private boolean optimizeWeapons() {
      Class<?>[] weaponClasses = new Class[]{class_1829.class, class_1753.class, class_1764.class, class_1835.class, class_1819.class, class_9362.class};
      return this.optimizeGeneric(weaponClasses);
   }

   private boolean optimizeGeneric(Class<?>[] classes) {
      Map<Class<?>, List<class_1799>> map = new HashMap();

      for(Class<?> clazz : classes) {
         map.put(clazz, new ArrayList());
      }

      for(int slot = 0; slot < 36; ++slot) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
         if (!stack.method_7960()) {
            class_1792 item = stack.method_7909();

            for(Class<?> clazz : classes) {
               if (clazz.isInstance(item)) {
                  ((List)map.get(clazz)).add(stack);
                  break;
               }
            }
         }
      }

      Set<class_1799> keep = new HashSet();

      for(Class<?> clazz : classes) {
         List<class_1799> list = (List)map.get(clazz);
         if (!list.isEmpty()) {
            Stream var10000 = list.stream().filter((s) -> this.getToolMaterialLevel(s) >= 3);
            Objects.requireNonNull(keep);
            var10000.forEach(keep::add);
            class_1799 best = (class_1799)list.stream().max(Comparator.comparingInt(this::getToolMaterialLevel)).orElse((Object)null);
            if (best != null) {
               keep.add(best);
            }
         }
      }

      for(int slot = 0; slot < 36; ++slot) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
         if (!stack.method_7960()) {
            class_1792 item = stack.method_7909();

            for(Class<?> clazz : classes) {
               if (clazz.isInstance(item) && !keep.contains(stack)) {
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot < 9 ? slot + 36 : slot, 1, class_1713.field_7795, mc.field_1724);
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static boolean canMerge(class_1799 a, class_1799 b) {
      return class_1799.method_31577(a, b);
   }

   public static enum Page {
      General,
      Trash;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Trash};
      }
   }
}
