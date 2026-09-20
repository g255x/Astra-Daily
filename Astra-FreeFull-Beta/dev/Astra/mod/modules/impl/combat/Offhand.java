package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.TotemEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.enums.Timing;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1829;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2838;
import net.minecraft.class_2846;
import net.minecraft.class_2846.class_2847;

public class Offhand extends Module {
   private final EnumSetting<OffhandItem> item;
   private final BooleanSetting safe;
   private final SliderSetting safeHealth;
   private final BooleanSetting lethalCrystal;
   private final BooleanSetting gapSwitch;
   private final BooleanSetting always;
   private final BooleanSetting gapOnTotem;
   private final BooleanSetting gapOnSword;
   private final BooleanSetting gapOnPick;
   private final BooleanSetting mainHandTotem;
   private final SliderSetting slot;
   private final BooleanSetting forceUpdate;
   private final BooleanSetting withOffhand;
   private final BooleanSetting onlyPlayer;
   private final SliderSetting playerDistance;
   private final BooleanSetting autoRestore;
   private final EnumSetting<SwapMode> swapMode;
   private final SliderSetting delay;
   private final EnumSetting<Timing> timing;
   private final Timer timer;
   private int previousMainHandSlot;
   private boolean holdingTotemDueToDanger;

   public Offhand() {
      super("Offhand", Module.Category.Combat);
      this.item = this.add(new EnumSetting("Item", Offhand.OffhandItem.Totem));
      this.safe = this.add((new BooleanSetting("Safe", true)).setParent());
      BooleanSetting var10009 = this.safe;
      Objects.requireNonNull(var10009);
      this.safeHealth = this.add((new SliderSetting("Health", 0.2, (double)0.0F, (double)36.0F, 0.1, var10009::isOpen)).setSuffix("hp"));
      BooleanSetting var10006 = this.safe;
      Objects.requireNonNull(var10006);
      this.lethalCrystal = this.add(new BooleanSetting("LethalCrystal", true, var10006::isOpen));
      this.gapSwitch = this.add((new BooleanSetting("GapSwitch", false)).setParent());
      var10006 = this.gapSwitch;
      Objects.requireNonNull(var10006);
      this.always = this.add(new BooleanSetting("Always", false, var10006::isOpen));
      var10006 = this.gapSwitch;
      Objects.requireNonNull(var10006);
      this.gapOnTotem = this.add(new BooleanSetting("Gap-Totem", true, var10006::isOpen));
      var10006 = this.gapSwitch;
      Objects.requireNonNull(var10006);
      this.gapOnSword = this.add(new BooleanSetting("Gap-Sword", true, var10006::isOpen));
      var10006 = this.gapSwitch;
      Objects.requireNonNull(var10006);
      this.gapOnPick = this.add(new BooleanSetting("Gap-Pickaxe", true, var10006::isOpen));
      this.mainHandTotem = this.add((new BooleanSetting("MainHandTotem", true)).setParent());
      var10009 = this.mainHandTotem;
      Objects.requireNonNull(var10009);
      this.slot = this.add(new SliderSetting("Slot", (double)9.0F, (double)1.0F, (double)9.0F, (double)1.0F, var10009::isOpen));
      var10006 = this.mainHandTotem;
      Objects.requireNonNull(var10006);
      this.forceUpdate = this.add(new BooleanSetting("ForceUpdate", true, var10006::isOpen));
      var10006 = this.mainHandTotem;
      Objects.requireNonNull(var10006);
      this.withOffhand = this.add(new BooleanSetting("WithOffhand", true, var10006::isOpen));
      var10006 = this.mainHandTotem;
      Objects.requireNonNull(var10006);
      this.onlyPlayer = this.add(new BooleanSetting("OnlyPlayer", true, var10006::isOpen));
      this.playerDistance = this.add((new SliderSetting("PlayerDistance", (double)14.0F, (double)1.0F, (double)30.0F, (double)0.5F, () -> this.mainHandTotem.isOpen() && this.onlyPlayer.getValue())).setSuffix("m"));
      var10006 = this.mainHandTotem;
      Objects.requireNonNull(var10006);
      this.autoRestore = this.add(new BooleanSetting("AutoRestore", true, var10006::isOpen));
      this.swapMode = this.add(new EnumSetting("SwapMode", Offhand.SwapMode.OffhandSwap));
      this.delay = this.add((new SliderSetting("Delay", (double)50.0F, (double)0.0F, (double)500.0F, (double)1.0F)).setSuffix("ms"));
      this.timing = this.add(new EnumSetting("Timing", Timing.All));
      this.timer = new Timer();
      this.previousMainHandSlot = -1;
      this.holdingTotemDueToDanger = false;
      this.setChinese("副手物品");
   }

   @EventListener
   public void totem(TotemEvent event) {
      if (event.getPlayer() == mc.field_1724) {
         if (mc.field_1724.method_6047().method_7909() == class_1802.field_8288) {
            mc.field_1724.method_31548().method_5441(0);
         } else if (mc.field_1724.method_6079().method_7909() == class_1802.field_8288) {
            mc.field_1724.method_31548().field_7544.set(0, class_1799.field_8037);
         }
      }

   }

   private boolean lethalCrystal() {
      if (!this.lethalCrystal.getValue()) {
         return false;
      } else {
         for(class_1297 entity : Astra.THREAD.getEntities()) {
            if (entity instanceof class_1511 && mc.field_1724.method_5739(entity) <= 12.0F) {
               class_243 vec3d = new class_243(entity.method_23317(), entity.method_23318(), entity.method_23321());
               if (AutoCrystal.INSTANCE.calculateDamage((class_243)vec3d, mc.field_1724, mc.field_1724) >= EntityUtil.getHealth(mc.field_1724)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean hasNearbyPlayers() {
      if (!this.onlyPlayer.getValue()) {
         return true;
      } else {
         double dist = this.playerDistance.getValue();

         for(class_1657 player : mc.field_1687.method_18456()) {
            if (player != mc.field_1724 && (double)mc.field_1724.method_5739(player) <= dist) {
               return true;
            }
         }

         return false;
      }
   }

   private void ensureTotemInSlot() {
      int targetSlot = this.slot.getValueInt() - 1;
      if (mc.field_1724.method_31548().method_5438(targetSlot).method_7909() != class_1802.field_8288) {
         int totemSlot = InventoryUtil.findItemInventorySlot(class_1802.field_8288);
         if (totemSlot != -1) {
            switch (((SwapMode)this.swapMode.getValue()).ordinal()) {
               case 0:
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, totemSlot, 0, class_1713.field_7790, mc.field_1724);
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, targetSlot + 36, 0, class_1713.field_7790, mc.field_1724);
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, totemSlot, 0, class_1713.field_7790, mc.field_1724);
                  break;
               case 1:
                  mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, totemSlot, targetSlot, class_1713.field_7791, mc.field_1724);
                  break;
               case 2:
                  int old = mc.field_1724.method_31548().field_7545;
                  InventoryUtil.switchToSlot(targetSlot);
                  mc.method_1562().method_52787(new class_2838(totemSlot));
                  InventoryUtil.switchToSlot(old);
            }

            EntityUtil.syncInventory();
         }
      }
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if ((!this.timing.is(Timing.Pre) || !event.isPost()) && (!this.timing.is(Timing.Post) || !event.isPre())) {
            if (this.timer.passed((long)this.delay.getValueInt())) {
               if (EntityUtil.inInventory()) {
                  boolean unsafe = (double)EntityUtil.getHealth(mc.field_1724) < this.safeHealth.getValue() || this.lethalCrystal();
                  boolean switchMainHandTotem = mc.field_1724.method_6079().method_7909() != class_1802.field_8288 || this.withOffhand.getValue();
                  if (this.mainHandTotem.getValue()) {
                     this.ensureTotemInSlot();
                  }

                  if (this.autoRestore.getValue() && !unsafe && this.holdingTotemDueToDanger && this.previousMainHandSlot != -1) {
                     InventoryUtil.switchToSlot(this.previousMainHandSlot);
                     this.previousMainHandSlot = -1;
                     this.holdingTotemDueToDanger = false;
                     this.timer.reset();
                  } else {
                     if (this.safe.getValue() && unsafe) {
                        if (!this.mainHandTotem.getValue() || !switchMainHandTotem || !this.hasNearbyPlayers()) {
                           this.swap(class_1802.field_8288);
                           this.timer.reset();
                           return;
                        }

                        int targetSlot = this.slot.getValueInt() - 1;
                        if (targetSlot != mc.field_1724.method_31548().field_7545 || this.forceUpdate.getValue()) {
                           if (!this.holdingTotemDueToDanger) {
                              this.previousMainHandSlot = mc.field_1724.method_31548().field_7545;
                              this.holdingTotemDueToDanger = true;
                           }

                           InventoryUtil.switchToSlot(targetSlot);
                        }
                     }

                     if (this.gapSwitch.getValue() && mc.field_1690.field_1904.method_1434()) {
                        boolean canGap = false;
                        class_1792 mainItem = mc.field_1724.method_6047().method_7909();
                        if (this.gapOnSword.getValue() && mainItem instanceof class_1829) {
                           canGap = true;
                        }

                        if (this.always.getValue() && mainItem != class_1802.field_8463 && mainItem != class_1802.field_8367) {
                           canGap = true;
                        }

                        if (this.gapOnPick.getValue() && mainItem instanceof class_1810) {
                           canGap = true;
                        }

                        if (this.gapOnTotem.getValue() && mainItem == class_1802.field_8288) {
                           canGap = true;
                        }

                        if (canGap) {
                           this.swap(class_1802.field_8463);
                           this.timer.reset();
                           return;
                        }
                     }

                     OffhandItem i = this.item.getValue();
                     if (i == Offhand.OffhandItem.Shield) {
                        this.swap(class_1802.field_8255);
                        this.timer.reset();
                     } else if (i == Offhand.OffhandItem.Chorus) {
                        this.swap(class_1802.field_8233);
                        this.timer.reset();
                     } else if (i == Offhand.OffhandItem.Crystal) {
                        this.swap(class_1802.field_8301);
                        this.timer.reset();
                     } else if (i == Offhand.OffhandItem.Totem) {
                        this.swap(class_1802.field_8288);
                        this.timer.reset();
                     } else if (i == Offhand.OffhandItem.Gapple) {
                        this.swap(class_1802.field_8463);
                        this.timer.reset();
                     }

                  }
               }
            }
         }
      }
   }

   private void swap(class_1792 item) {
      int itemSlot = item == class_1802.field_8463 ? this.getGAppleSlot() : this.findItemInventorySlot(item);
      if (itemSlot != -1) {
         switch (((SwapMode)this.swapMode.getValue()).ordinal()) {
            case 0:
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, itemSlot, 0, class_1713.field_7790, mc.field_1724);
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 45, 0, class_1713.field_7790, mc.field_1724);
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, itemSlot, 0, class_1713.field_7790, mc.field_1724);
               EntityUtil.syncInventory();
               break;
            case 1:
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, itemSlot, 40, class_1713.field_7791, mc.field_1724);
               EntityUtil.syncInventory();
               break;
            case 2:
               mc.method_1562().method_52787(new class_2838(itemSlot));
               mc.method_1562().method_52787(new class_2846(class_2847.field_12969, new class_2338(0, 0, 0), class_2350.field_11033, 0));
               mc.method_1562().method_52787(new class_2838(itemSlot));
         }

      }
   }

   private int getGAppleSlot() {
      return this.findItemInventorySlot(class_1802.field_8367) != -1 ? this.findItemInventorySlot(class_1802.field_8367) : this.findItemInventorySlot(class_1802.field_8463);
   }

   public String getInfo() {
      return ((OffhandItem)this.item.getValue()).name();
   }

   public int findItemInventorySlot(class_1792 item) {
      if (mc.field_1724.method_6079().method_7909() == class_1802.field_8463 && item == class_1802.field_8463) {
         return -1;
      } else if (mc.field_1724.method_6079().method_7909() != class_1802.field_8367 || item != class_1802.field_8463 && item != class_1802.field_8367) {
         if (item == mc.field_1724.method_6079().method_7909()) {
            return -1;
         } else {
            switch (((SwapMode)this.swapMode.getValue()).ordinal()) {
               case 0:
               case 1:
                  for(int i = 44; i >= 0; --i) {
                     if (mc.field_1724.method_31548().method_5438(i).method_7909() == item) {
                        return i < 9 ? i + 36 : i;
                     }
                  }
                  break;
               case 2:
                  for(int i = 9; i < mc.field_1724.method_31548().method_5439() + 1; ++i) {
                     if (mc.field_1724.method_31548().method_5438(i).method_7909() == item) {
                        return i;
                     }
                  }

                  for(int i = 0; i < 9; ++i) {
                     if (mc.field_1724.method_31548().method_5438(i).method_7909() == item) {
                        return i;
                     }
                  }
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public static enum OffhandItem {
      None,
      Totem,
      Crystal,
      Gapple,
      Shield,
      Chorus;

      // $FF: synthetic method
      private static OffhandItem[] $values() {
         return new OffhandItem[]{None, Totem, Crystal, Gapple, Shield, Chorus};
      }
   }

   public static enum SwapMode {
      ClickSlot,
      OffhandSwap,
      Pick;

      // $FF: synthetic method
      private static SwapMode[] $values() {
         return new SwapMode[]{ClickSlot, OffhandSwap, Pick};
      }
   }
}
