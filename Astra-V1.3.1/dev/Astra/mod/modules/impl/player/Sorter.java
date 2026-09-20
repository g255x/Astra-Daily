package dev.Astra.mod.modules.impl.player;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.core.Manager;
import dev.Astra.mod.gui.windows.WindowBase;
import dev.Astra.mod.gui.windows.WindowsScreen;
import dev.Astra.mod.gui.windows.impl.ItemSelectWindow;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1713;
import net.minecraft.class_1738;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_2399;
import net.minecraft.class_2480;
import net.minecraft.class_2665;
import net.minecraft.class_9334;
import org.apache.commons.io.IOUtils;

public class Sorter extends Module {
   public static Sorter INSTANCE;
   final int[] stealCountList = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
   private final SliderSetting tasksPerTicks = this.add(new SliderSetting("TasksPerTick", 2, 1, 20));
   private final SliderSetting delay = this.add((new SliderSetting("Delay", 0.1, (double)0.0F, (double)5.0F, 0.01)).setSuffix("s"));
   private final BooleanSetting stack = this.add(new BooleanSetting("Stack", true));
   private final EnumSetting<Mode> trashMode;
   private final BooleanSetting edit;
   private final BooleanSetting sort;
   private final BooleanSetting kit;
   private final StringSetting kitName;
   private final BooleanSetting drop;
   private final BooleanSetting trash;
   private final BooleanSetting rename;
   private final BooleanSetting kitExceed;
   private final BooleanSetting exceed;
   private final SliderSetting crystal;
   private final SliderSetting exp;
   private final SliderSetting totem;
   private final SliderSetting eGapple;
   private final SliderSetting gapple;
   private final SliderSetting obsidian;
   private final SliderSetting web;
   private final SliderSetting glowstone;
   private final SliderSetting anchor;
   private final SliderSetting pearl;
   private final SliderSetting piston;
   private final SliderSetting redstone;
   private final SliderSetting ladder;
   private final SliderSetting bed;
   private final SliderSetting speed;
   private final SliderSetting turtle;
   private final SliderSetting strength;
   private final Timer timer;
   private final Map<Integer, String> kitMap;

   public Sorter() {
      super("Sorter", Module.Category.Player);
      this.trashMode = this.add(new EnumSetting("TrashMode", Sorter.Mode.Whitelist));
      this.edit = this.add((new BooleanSetting("EditTrash", false)).injectTask(this::openGui));
      this.sort = this.add(new BooleanSetting("Sort", true));
      this.kit = this.add((new BooleanSetting("Kit", false)).injectTask(this::onEnable));
      this.kitName = this.add(new StringSetting("KitName", "kit1"));
      this.drop = this.add((new BooleanSetting("Drop", true)).setParent());
      BooleanSetting var10006 = this.drop;
      Objects.requireNonNull(var10006);
      this.trash = this.add(new BooleanSetting("Trash", true, var10006::isOpen));
      var10006 = this.drop;
      Objects.requireNonNull(var10006);
      this.rename = this.add(new BooleanSetting("Rename", true, var10006::isOpen));
      var10006 = this.drop;
      Objects.requireNonNull(var10006);
      this.kitExceed = this.add(new BooleanSetting("KitExceed", true, var10006::isOpen));
      var10006 = this.drop;
      Objects.requireNonNull(var10006);
      this.exceed = this.add(new BooleanSetting("Exceed", true, var10006::isOpen));
      this.crystal = this.add(new SliderSetting("Crystal", 4, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.exp = this.add(new SliderSetting("Exp", 4, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.totem = this.add(new SliderSetting("Totem", 6, 0, 36, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.eGapple = this.add(new SliderSetting("EGapple", 2, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.gapple = this.add(new SliderSetting("Gapple", 2, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.obsidian = this.add(new SliderSetting("Obsidian", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.web = this.add(new SliderSetting("Web", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.glowstone = this.add(new SliderSetting("Glowstone", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.anchor = this.add(new SliderSetting("Anchor", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.pearl = this.add(new SliderSetting("Pearl", 1, 0, 8, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.piston = this.add(new SliderSetting("Piston", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.redstone = this.add(new SliderSetting("RedStone", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.ladder = this.add(new SliderSetting("Ladder", 2, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.bed = this.add(new SliderSetting("Bed", 4, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.speed = this.add(new SliderSetting("Speed", 1, 0, 8, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.turtle = this.add(new SliderSetting("Resistance", 1, 0, 8, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.strength = this.add(new SliderSetting("Strength", 1, 0, 8, () -> this.drop.isOpen() && this.exceed.isOpen()));
      this.timer = new Timer();
      this.kitMap = new ConcurrentHashMap();
      this.setChinese("背包整理");
      INSTANCE = this;
   }

   public void onEnable() {
      if (!nullCheck()) {
         Astra.THREAD.execute(() -> {
            this.kitMap.clear();

            try {
               File file = Manager.getFile(this.kitName.getValue() + ".kit");
               if (!file.exists()) {
                  return;
               }

               for(String s : IOUtils.readLines(new FileInputStream(file), StandardCharsets.UTF_8)) {
                  String[] split = s.split(":");
                  if (split.length != 2) {
                     return;
                  }

                  this.kitMap.put(Integer.valueOf(split[0]), split[1]);
               }
            } catch (Exception e) {
               e.printStackTrace();
            }

         });
      }
   }

   public static int getItemCount(class_1792 item) {
      int count = 0;

      for(Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
         if (((class_1799)entry.getValue()).method_7909() == item) {
            ++count;
         }
      }

      if (mc.field_1724.method_6079().method_7909() == item) {
         ++count;
      }

      return count;
   }

   public static int getItemCount(Class<?> clazz) {
      int count = 0;

      for(Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
         if (((class_1799)entry.getValue()).method_7909() instanceof class_1747 && clazz.isInstance(((class_1747)((class_1799)entry.getValue()).method_7909()).method_7711())) {
            ++count;
         }
      }

      return count;
   }

   private void openGui() {
      this.edit.setValueWithoutTask(false);
      if (!nullCheck()) {
         mc.method_1507(new WindowsScreen(new WindowBase[]{new ItemSelectWindow(Astra.CLEANER)}));
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passedS(this.delay.getValue())) {
         if (EntityUtil.inInventory()) {
            if (this.exceed.getValue()) {
               this.updateItem();
            }

            for(int i = 0; (double)i < this.tasksPerTicks.getValue(); ++i) {
               this.tweak();
            }

         }
      }
   }

   private void tweak() {
      if (this.drop.getValue()) {
         for(int slot1 = 35; slot1 >= 0; --slot1) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(slot1);
            if (!stack.method_7960() && this.shouldDrop(stack)) {
               this.timer.reset();
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot1 < 9 ? slot1 + 36 : slot1, 1, class_1713.field_7795, mc.field_1724);
               return;
            }
         }
      }

      if (this.stack.getValue()) {
         for(int slot1 = 35; slot1 >= 9; --slot1) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(slot1);
            if (!stack.method_7960() && stack.method_7946() && stack.method_7947() != stack.method_7914()) {
               for(int slot2 = 0; slot2 < 36; ++slot2) {
                  class_1799 stack2;
                  if (slot1 != slot2 && (stack2 = mc.field_1724.method_31548().method_5438(slot2)).method_7947() != stack2.method_7914() && canMerge(stack, stack2)) {
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1, 0, class_1713.field_7790, mc.field_1724);
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot2 < 9 ? slot2 + 36 : slot2, 0, class_1713.field_7790, mc.field_1724);
                     mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1, 0, class_1713.field_7790, mc.field_1724);
                     this.timer.reset();
                     return;
                  }
               }
            }
         }
      }

      if (this.drop.getValue()) {
         for(int slot1 = 35; slot1 >= 0; --slot1) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(slot1);
            if (!stack.method_7960() && this.exceed.getValue() && this.exceed(stack, false)) {
               this.timer.reset();
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot1 < 9 ? slot1 + 36 : slot1, 1, class_1713.field_7795, mc.field_1724);
               return;
            }
         }
      }

      if (this.sort.getValue()) {
         if (this.kit.getValue()) {
            for(int slot1 = 0; slot1 < 36; ++slot1) {
               if (this.kitMap.containsKey(slot1)) {
                  String target = (String)this.kitMap.get(slot1);
                  String name = mc.field_1724.method_31548().method_5438(slot1).method_7909().method_7876();
                  if (!name.equals(target)) {
                     for(int slot2 = 0; slot2 < 36; ++slot2) {
                        String slot2Target = (String)this.kitMap.get(slot2);
                        class_1799 stack2 = mc.field_1724.method_31548().method_5438(slot2);
                        String itemID;
                        if (!stack2.method_7960() && !(itemID = stack2.method_7909().method_7876()).equals(slot2Target) && itemID.equals(target)) {
                           mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1 < 9 ? slot1 + 36 : slot1, 0, class_1713.field_7790, mc.field_1724);
                           mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot2 < 9 ? slot2 + 36 : slot2, 0, class_1713.field_7790, mc.field_1724);
                           mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1 < 9 ? slot1 + 36 : slot1, 0, class_1713.field_7790, mc.field_1724);
                           this.timer.reset();
                           return;
                        }
                     }
                  }
               }
            }
         } else {
            for(int slot1 = 9; slot1 < 36; ++slot1) {
               int id = class_1792.method_7880(mc.field_1724.method_31548().method_5438(slot1).method_7909());
               if (mc.field_1724.method_31548().method_5438(slot1).method_7960()) {
                  id = 114514;
               }

               int minId;
               if ((minId = this.getMinId(slot1, id)) < id) {
                  for(int slot2 = 35; slot2 > slot1; --slot2) {
                     class_1799 stack3 = mc.field_1724.method_31548().method_5438(slot2);
                     if (!stack3.method_7960() && class_1792.method_7880(stack3.method_7909()) == minId) {
                        mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1, 0, class_1713.field_7790, mc.field_1724);
                        mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot2, 0, class_1713.field_7790, mc.field_1724);
                        mc.field_1761.method_2906(mc.field_1724.field_7498.field_7763, slot1, 0, class_1713.field_7790, mc.field_1724);
                        this.timer.reset();
                        return;
                     }
                  }
               }
            }
         }
      }

      if (this.drop.getValue() && this.kitExceed.getValue()) {
         for(int slot1 = 35; slot1 >= 0; --slot1) {
            if (this.kitMap.containsKey(slot1)) {
               class_1799 stack4 = mc.field_1724.method_31548().method_5438(slot1);
               class_1747 blockItem;
               class_1792 item;
               if ((!this.exceed.getValue() || this.exceed(stack4, true)) && !stack4.method_7960() && !(stack4.method_7909() instanceof class_1738) && (!((item = stack4.method_7909()) instanceof class_1747) || !((blockItem = (class_1747)item).method_7711() instanceof class_2480)) && !stack4.method_7909().method_7876().equals(this.kitMap.get(slot1))) {
                  this.timer.reset();
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot1 < 9 ? slot1 + 36 : slot1, 1, class_1713.field_7795, mc.field_1724);
                  return;
               }
            }
         }
      }

   }

   private boolean shouldDrop(class_1799 stack) {
      class_1792 item = stack.method_7909();
      boolean inList;
      class_1747 blockItem;
      if (!this.trash.getValue() || item instanceof class_1747 && (blockItem = (class_1747)item).method_7711() instanceof class_2480 || ((inList = Astra.CLEANER.inList(item.method_7876())) || !this.trashMode.is(Sorter.Mode.Whitelist)) && (!inList || !this.trashMode.is(Sorter.Mode.Blacklist))) {
         if (!this.rename.getValue()) {
            return false;
         } else {
            return stack.method_7946() && !stack.method_7964().getString().equals(item.method_7848().getString());
         }
      } else {
         return true;
      }
   }

   private boolean exceed(class_1799 i, boolean dropOther) {
      if (i.method_7909().equals(class_1802.field_8301)) {
         if ((double)this.stealCountList[0] > this.crystal.getValue()) {
            int var21 = this.stealCountList[0]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909().equals(class_1802.field_8287)) {
         if ((double)this.stealCountList[1] > this.exp.getValue()) {
            int var20 = this.stealCountList[1]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909().equals(class_1802.field_8288)) {
         if ((double)this.stealCountList[2] > this.totem.getValue()) {
            int var19 = this.stealCountList[2]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909().equals(class_1802.field_8367)) {
         if ((double)this.stealCountList[3] > this.eGapple.getValue()) {
            int var18 = this.stealCountList[3]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909().equals(class_2246.field_10540.method_8389())) {
         if ((double)this.stealCountList[4] > this.obsidian.getValue()) {
            int var17 = this.stealCountList[4]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909().equals(class_2246.field_10343.method_8389())) {
         if ((double)this.stealCountList[5] > this.web.getValue()) {
            int var16 = this.stealCountList[5]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909().equals(class_2246.field_10171.method_8389())) {
         if ((double)this.stealCountList[6] > this.glowstone.getValue()) {
            int var15 = this.stealCountList[6]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909().equals(class_2246.field_23152.method_8389())) {
         if ((double)this.stealCountList[7] > this.anchor.getValue()) {
            int var14 = this.stealCountList[7]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909().equals(class_1802.field_8634)) {
         if ((double)this.stealCountList[8] > this.pearl.getValue()) {
            int var13 = this.stealCountList[8]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2665) {
         if ((double)this.stealCountList[9] > this.piston.getValue()) {
            int var12 = this.stealCountList[9]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909().equals(class_2246.field_10002.method_8389())) {
         if ((double)this.stealCountList[10] > this.redstone.getValue()) {
            int var11 = this.stealCountList[10]--;
            return true;
         } else {
            return false;
         }
      } else if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2244) {
         if ((double)this.stealCountList[11] > this.bed.getValue()) {
            int var10 = this.stealCountList[11]--;
            return true;
         } else {
            return false;
         }
      } else {
         if (class_1792.method_7880(i.method_7909()) == class_1792.method_7880(class_1802.field_8436)) {
            class_1844 potionContentsComponent = (class_1844)i.method_57825(class_9334.field_49651, class_1844.field_49274);

            for(class_1293 effect : potionContentsComponent.method_57397()) {
               if (effect.method_5579().comp_349() == class_1294.field_5904.comp_349()) {
                  if ((double)this.stealCountList[12] > this.speed.getValue()) {
                     int var7 = this.stealCountList[12]--;
                     return true;
                  }

                  return false;
               }

               if (effect.method_5579().comp_349() == class_1294.field_5907.comp_349()) {
                  if ((double)this.stealCountList[13] > this.turtle.getValue()) {
                     int var6 = this.stealCountList[13]--;
                     return true;
                  }

                  return false;
               }

               if (effect.method_5579().comp_349() == class_1294.field_5910.comp_349()) {
                  if ((double)this.stealCountList[16] > this.strength.getValue()) {
                     int var10002 = this.stealCountList[16]--;
                     return true;
                  }

                  return false;
               }
            }
         }

         if (i.method_7909().equals(class_1802.field_8463) && (double)this.stealCountList[14] > this.gapple.getValue()) {
            int var9 = this.stealCountList[14]--;
            return true;
         } else if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2399 && (double)this.stealCountList[15] > this.ladder.getValue()) {
            int var8 = this.stealCountList[15]--;
            return true;
         } else {
            return dropOther;
         }
      }
   }

   private void updateItem() {
      this.stealCountList[0] = getItemCount(class_1802.field_8301);
      this.stealCountList[1] = getItemCount(class_1802.field_8287);
      this.stealCountList[2] = getItemCount(class_1802.field_8288);
      this.stealCountList[3] = getItemCount(class_1802.field_8367);
      this.stealCountList[4] = getItemCount(class_1802.field_8281);
      this.stealCountList[5] = getItemCount(class_1802.field_8786);
      this.stealCountList[6] = getItemCount(class_1802.field_8801);
      this.stealCountList[7] = getItemCount(class_1802.field_23141);
      this.stealCountList[8] = getItemCount(class_1802.field_8634);
      this.stealCountList[9] = getItemCount(class_1802.field_8249) - getItemCount(class_1802.field_8105);
      this.stealCountList[10] = getItemCount(class_1802.field_8793);
      this.stealCountList[11] = getItemCount(class_2244.class);
      this.stealCountList[12] = InventoryUtil.getPotionCount((class_1291)class_1294.field_5904.comp_349());
      this.stealCountList[13] = InventoryUtil.getPotionCount((class_1291)class_1294.field_5907.comp_349());
      this.stealCountList[14] = getItemCount(class_1802.field_8463);
      this.stealCountList[15] = getItemCount(class_2399.class);
      this.stealCountList[16] = InventoryUtil.getPotionCount((class_1291)class_1294.field_5910.comp_349());
   }

   private int getMinId(int slot, int currentId) {
      int id = currentId;

      for(int slot1 = slot + 1; slot1 < 36; ++slot1) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(slot1);
         int itemID;
         if (!stack.method_7960() && (itemID = class_1792.method_7880(stack.method_7909())) < id) {
            id = itemID;
         }
      }

      return id;
   }

   public static boolean canMerge(class_1799 source, class_1799 stack) {
      return class_1799.method_31577(source, stack);
   }

   private static enum Mode {
      Whitelist,
      Blacklist;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Whitelist, Blacklist};
      }
   }
}
