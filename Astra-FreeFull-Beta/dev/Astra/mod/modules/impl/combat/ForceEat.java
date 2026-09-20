package dev.Astra.mod.modules.impl.combat;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import net.minecraft.class_2886;

public class ForceEat extends Module {
   public static ForceEat INSTANCE;
   private final EnumSetting<Mode> mode;
   private final BooleanSetting autoSwitch;
   private final BooleanSetting noInteract;
   private boolean active;
   private int eatProgress;
   private int delay;
   private int prevSlot;

   public ForceEat() {
      super("ForceEat", Module.Category.Combat);
      this.mode = this.add(new EnumSetting("Mode", ForceEat.Mode.Both));
      this.autoSwitch = this.add(new BooleanSetting("AutoSwitch", true));
      this.noInteract = this.add(new BooleanSetting("NoInteract", true));
      this.active = false;
      this.eatProgress = 0;
      this.delay = 0;
      this.prevSlot = -1;
      this.setChinese("主手苹果");
      INSTANCE = this;
   }

   public void onEnable() {
      if (!nullCheck()) {
         this.prevSlot = mc.field_1724.method_31548().field_7545;
         int appleSlot = this.findAppleSlot();
         if (appleSlot == -1) {
            this.disable();
         } else {
            if (this.autoSwitch.getValue()) {
               InventoryUtil.switchToSlot(appleSlot);
            }

            this.active = false;
            this.eatProgress = 0;
            this.delay = 1;
         }
      }
   }

   public void onDisable() {
      mc.field_1690.field_1904.method_23481(false);
      if (!nullCheck()) {
         if (this.prevSlot != -1 && this.prevSlot < 9 && this.autoSwitch.getValue()) {
            InventoryUtil.switchToSlot(this.prevSlot);
         }

         this.active = false;
         this.eatProgress = 0;
         this.delay = 0;
         this.prevSlot = -1;
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.isOn() && !nullCheck()) {
         if (this.delay > 0) {
            --this.delay;
         } else {
            if (this.autoSwitch.getValue() && !this.isAppleInHand()) {
               int slot = this.findAppleSlot();
               if (slot == -1) {
                  this.disable();
                  return;
               }

               InventoryUtil.switchToSlot(slot);
               this.active = false;
               this.eatProgress = 0;
               mc.field_1690.field_1904.method_23481(false);
            }

            if (!this.active) {
               mc.field_1690.field_1904.method_23481(true);
               mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
               this.active = true;
               this.eatProgress = 0;
            } else {
               mc.field_1690.field_1904.method_23481(true);
               ++this.eatProgress;
               if (this.eatProgress >= 32) {
                  if (!this.isAppleInHand()) {
                     if (this.autoSwitch.getValue()) {
                        int nextSlot = this.findAppleSlot();
                        if (nextSlot != -1) {
                           InventoryUtil.switchToSlot(nextSlot);
                           this.eatProgress = 0;
                           mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
                        } else {
                           this.disable();
                        }
                     } else {
                        this.disable();
                     }
                  } else {
                     this.eatProgress = 0;
                     mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
                  }
               }

            }
         }
      }
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (this.isOn() && !nullCheck() && this.active) {
         mc.field_1690.field_1904.method_23481(true);
      }
   }

   @EventListener
   public void onPacketSend(PacketEvent.Send event) {
      if (this.isOn() && !nullCheck() && this.active) {
         class_2596<?> packet = event.getPacket();
         if (this.noInteract.getValue()) {
            if (packet instanceof class_2886 && ((class_2886)packet).method_12551() != class_1268.field_5808) {
               event.cancel();
            }

            if (packet instanceof class_2846) {
               event.cancel();
            }
         }

      }
   }

   private int findAppleSlot() {
      int var10000;
      switch (((Mode)this.mode.getValue()).ordinal()) {
         case 0:
            var10000 = InventoryUtil.findItem(class_1802.field_8463);
            break;
         case 1:
            var10000 = InventoryUtil.findItem(class_1802.field_8367);
            break;
         case 2:
            int slot = InventoryUtil.findItem(class_1802.field_8367);
            if (slot == -1) {
               slot = InventoryUtil.findItem(class_1802.field_8463);
            }

            var10000 = slot;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private boolean isAppleInHand() {
      class_1799 stack = mc.field_1724.method_6047();
      if (stack.method_7960()) {
         return false;
      } else {
         boolean var10000;
         switch (((Mode)this.mode.getValue()).ordinal()) {
            case 0 -> var10000 = stack.method_7909() == class_1802.field_8463;
            case 1 -> var10000 = stack.method_7909() == class_1802.field_8367;
            case 2 -> var10000 = stack.method_7909() == class_1802.field_8463 || stack.method_7909() == class_1802.field_8367;
            default -> throw new MatchException((String)null, (Throwable)null);
         }

         return var10000;
      }
   }

   public static enum Mode {
      GoldenApple("Gapple"),
      EnchantedGoldenApple("EnchantedGapple"),
      Both("Both");

      private final String name;

      private Mode(String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{GoldenApple, EnchantedGoldenApple, Both};
      }
   }
}
