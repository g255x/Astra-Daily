package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1829;
import net.minecraft.class_2199;
import net.minecraft.class_2248;
import net.minecraft.class_2281;
import net.minecraft.class_2336;
import net.minecraft.class_2596;
import net.minecraft.class_2885;
import net.minecraft.class_418;
import net.minecraft.class_437;

public class InteractTweaks extends Module {
   public static InteractTweaks INSTANCE;
   public final BooleanSetting noEntityTrace = this.add((new BooleanSetting("NoEntityTrace", true)).setParent());
   public final BooleanSetting onlyPickaxe;
   public final BooleanSetting multiTask;
   public final BooleanSetting respawn;
   public final BooleanSetting ghostHand;
   private final BooleanSetting noAbort;
   private final BooleanSetting noReset;
   private final BooleanSetting noDelay;
   private final BooleanSetting noInteract;
   private final BooleanSetting pickaxeSwitch;
   private final BooleanSetting allowSword;
   private final BooleanSetting allowPickaxe;
   private final BooleanSetting reach;
   public final SliderSetting blockRange;
   public final SliderSetting entityRange;
   private final SliderSetting delay;
   public boolean isActive;
   boolean swapped;
   int lastSlot;

   public InteractTweaks() {
      super("InteractTweaks", Module.Category.Player);
      BooleanSetting var10006 = this.noEntityTrace;
      Objects.requireNonNull(var10006);
      this.onlyPickaxe = this.add(new BooleanSetting("OnlyPickaxe", true, var10006::isOpen));
      this.multiTask = this.add(new BooleanSetting("MultiTask", true));
      this.respawn = this.add(new BooleanSetting("Respawn", true));
      this.ghostHand = this.add(new BooleanSetting("IgnoreBedrock", false));
      this.noAbort = this.add(new BooleanSetting("NoMineAbort", false));
      this.noReset = this.add(new BooleanSetting("NoMineReset", false));
      this.noDelay = this.add(new BooleanSetting("NoMineDelay", false));
      this.noInteract = this.add(new BooleanSetting("NoInteract", false));
      this.pickaxeSwitch = this.add((new BooleanSetting("SwitchEat", false)).setParent());
      var10006 = this.pickaxeSwitch;
      Objects.requireNonNull(var10006);
      this.allowSword = this.add(new BooleanSetting("Sword", true, var10006::isOpen));
      var10006 = this.pickaxeSwitch;
      Objects.requireNonNull(var10006);
      this.allowPickaxe = this.add(new BooleanSetting("Pickaxe", true, var10006::isOpen));
      this.reach = this.add(new BooleanSetting("Reach", false));
      BooleanSetting var10009 = this.reach;
      Objects.requireNonNull(var10009);
      this.blockRange = this.add(new SliderSetting("BlockRange", (double)5.0F, (double)0.0F, (double)15.0F, 0.1, var10009::getValue));
      var10009 = this.reach;
      Objects.requireNonNull(var10009);
      this.entityRange = this.add(new SliderSetting("EntityRange", (double)5.0F, (double)0.0F, (double)15.0F, 0.1, var10009::getValue));
      this.delay = this.add(new SliderSetting("UseDelay", (double)4.0F, (double)0.0F, (double)4.0F, (double)1.0F));
      this.swapped = false;
      this.lastSlot = 0;
      this.setChinese("交互调整");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.respawn.getValue() && mc.field_1755 instanceof class_418) {
         mc.field_1724.method_7331();
         mc.method_1507((class_437)null);
      }

      if (mc.field_1752 <= 4 - this.delay.getValueInt()) {
         mc.field_1752 = 0;
      }

      if (this.pickaxeSwitch.getValue()) {
         if ((!(mc.field_1724.method_6047().method_7909() instanceof class_1810) || !this.allowPickaxe.getValue()) && (!(mc.field_1724.method_6047().method_7909() instanceof class_1829) || !this.allowSword.getValue()) && mc.field_1724.method_6047().method_7909() != class_1802.field_8367 && mc.field_1724.method_6047().method_7909() != class_1802.field_8463) {
            this.swapped = false;
            return;
         }

         int gapple = InventoryUtil.findItem(class_1802.field_8367);
         if (gapple == -1) {
            gapple = InventoryUtil.findItem(class_1802.field_8463);
         }

         if (gapple == -1) {
            if (this.swapped) {
               InventoryUtil.switchToSlot(this.lastSlot);
               this.swapped = false;
            }

            return;
         }

         if (mc.field_1690.field_1904.method_1434()) {
            if ((mc.field_1724.method_6047().method_7909() instanceof class_1810 && this.allowPickaxe.getValue() || mc.field_1724.method_6047().method_7909() instanceof class_1829 && this.allowSword.getValue()) && mc.field_1724.method_6079().method_7909() != class_1802.field_8367 && mc.field_1724.method_6047().method_7909() != class_1802.field_8463) {
               this.lastSlot = mc.field_1724.method_31548().field_7545;
               InventoryUtil.switchToSlot(gapple);
               this.swapped = true;
            }
         } else if (this.swapped) {
            InventoryUtil.switchToSlot(this.lastSlot);
            this.swapped = false;
         }
      }

   }

   @EventListener
   public void onPacket(PacketEvent.Send event) {
      if (!nullCheck() && this.noInteract.getValue()) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2885) {
            class_2885 packet = (class_2885)var3;
            class_2248 var4 = mc.field_1687.method_8320(packet.method_12543().method_17777()).method_26204();
            if (!mc.field_1724.method_5715() && (var4 instanceof class_2281 || var4 instanceof class_2336 || var4 instanceof class_2199)) {
               event.cancel();
            }
         }
      }

   }

   public void onDisable() {
      this.isActive = false;
   }

   public boolean reach() {
      return this.isOn() && this.reach.getValue();
   }

   public boolean noAbort() {
      return this.isOn() && this.noAbort.getValue() && !mc.field_1690.field_1904.method_1434();
   }

   public boolean noReset() {
      return this.isOn() && this.noReset.getValue();
   }

   public boolean noDelay() {
      return this.isOn() && this.noDelay.getValue();
   }

   public boolean multiTask() {
      return this.isOn() && this.multiTask.getValue();
   }

   public boolean noEntityTrace() {
      if (!this.isOff() && this.noEntityTrace.getValue()) {
         return !this.onlyPickaxe.getValue() ? true : mc.field_1724.method_6047().method_7909() instanceof class_1810 || mc.field_1724.method_6115() && !(mc.field_1724.method_6047().method_7909() instanceof class_1829);
      } else {
         return false;
      }
   }

   public boolean ghostHand() {
      return this.isOn() && this.ghostHand.getValue() && !mc.field_1690.field_1904.method_1434() && !mc.field_1690.field_1832.method_1434();
   }
}
