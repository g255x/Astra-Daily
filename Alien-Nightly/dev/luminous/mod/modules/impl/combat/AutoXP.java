package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2886;
import net.minecraft.class_9304;

public class AutoXP extends Module {
   public static AutoXP INSTANCE;
   public final BooleanSetting rotation = this.add((new BooleanSetting("Rotation", true)).setParent());
   private final BooleanSetting instant;
   public final BooleanSetting onlyBroken;
   private final SliderSetting delay;
   private final BooleanSetting inventory;
   public final EnumSetting<SwingSide> interactSwing;
   private final BooleanSetting usingPause;
   private final BooleanSetting onlyGround;
   public final BooleanSetting autoDisable;
   private final Timer delayTimer;
   boolean lookDown;
   int exp;
   private boolean throwing;

   public AutoXP() {
      super("AutoXP", Module.Category.Combat);
      BooleanSetting var10006 = this.rotation;
      Objects.requireNonNull(var10006);
      this.instant = this.add(new BooleanSetting("Instant", false, var10006::isOpen));
      this.onlyBroken = this.add(new BooleanSetting("OnlyBroken", true));
      this.delay = this.add(new SliderSetting("Delay", 3, 0, 5));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true));
      this.interactSwing = this.add(new EnumSetting("InteractSwing", SwingSide.All));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true));
      this.onlyGround = this.add(new BooleanSetting("OnlyGround", true));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", true));
      this.delayTimer = new Timer();
      this.lookDown = false;
      this.exp = 0;
      this.throwing = false;
      this.setChinese("自动经验瓶");
      INSTANCE = this;
   }

   public void onDisable() {
      this.throwing = false;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.throwing = this.checkThrow();
      if (!this.inventory.getValue() || EntityUtil.inInventory()) {
         if (this.lookDown && this.isThrow() && this.delayTimer.passed((long)this.delay.getValueInt() * 20L) && (!this.onlyGround.getValue() || mc.field_1724.method_24828())) {
            this.exp = InventoryUtil.getItemCount(class_1802.field_8287) - 1;
            if (this.rotation.getValue() && this.instant.getValue()) {
               Alien.ROTATION.snapAt(Alien.ROTATION.rotationYaw, 88.0F);
            }

            this.throwExp();
            if (this.rotation.getValue() && this.instant.getValue()) {
               Alien.ROTATION.snapBack();
            }
         }

         if (this.autoDisable.getValue() && !this.isThrow()) {
            this.disable();
         }
      }

   }

   public boolean onEnable() {
      this.lookDown = !this.rotation.getValue() || this.instant.getValue();
      if (nullCheck()) {
         this.disable();
      } else {
         this.exp = InventoryUtil.getItemCount(class_1802.field_8287);
      }

      return false;
   }

   public String getInfo() {
      return String.valueOf(this.exp);
   }

   public void throwExp() {
      int oldSlot = mc.field_1724.method_31548().field_7545;
      int newSlot;
      if (this.inventory.getValue() && (newSlot = InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8287)) != -1) {
         InventoryUtil.inventorySwap(newSlot, mc.field_1724.method_31548().field_7545);
         sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
         EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
         InventoryUtil.inventorySwap(newSlot, mc.field_1724.method_31548().field_7545);
         EntityUtil.syncInventory();
         this.delayTimer.reset();
      } else if ((newSlot = InventoryUtil.findItem(class_1802.field_8287)) != -1) {
         InventoryUtil.switchToSlot(newSlot);
         sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
         EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
         InventoryUtil.switchToSlot(oldSlot);
         this.delayTimer.reset();
      }

   }

   @EventListener(
      priority = -200
   )
   public void RotateEvent(UpdateRotateEvent event) {
      if (this.rotation.getValue() && !this.instant.getValue() && this.isThrow()) {
         event.setPitch(88.0F);
         this.lookDown = true;
      }

   }

   public boolean isThrow() {
      return this.throwing;
   }

   public boolean checkThrow() {
      if (this.isOff()) {
         return false;
      } else if (mc.field_1755 != null) {
         return false;
      } else if (this.usingPause.getValue() && mc.field_1724.method_6115()) {
         return false;
      } else if (InventoryUtil.findItem(class_1802.field_8287) != -1 || this.inventory.getValue() && InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8287) != -1) {
         if (this.onlyBroken.getValue()) {
            for(class_1799 armor : mc.field_1724.method_31548().field_7548) {
               if (!armor.method_7960() && EntityUtil.getDamagePercent(armor) < 100) {
                  class_9304 enchants = class_1890.method_57532(armor);
                  return enchants.method_57534().contains(mc.field_1687.method_30349().method_46762(class_1893.field_9101.method_58273()).method_46746(class_1893.field_9101).get());
               }
            }

            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }
}
