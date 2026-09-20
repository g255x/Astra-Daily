package dev.Astra.mod.modules.impl.combat;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.Render2DEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2886;

public class ForceEat extends Module {
   public static ForceEat INSTANCE;
   private final EnumSetting<Mode> mode;
   private final BooleanSetting autoSwitch;
   private final BooleanSetting noInteract;
   private final BooleanSetting noMove;
   private boolean isEating;
   private int eatTicks;
   private int prevSlot;
   private int switchBackTimer;
   private int startDelayTimer;

   public ForceEat() {
      super("ForceEat", Module.Category.Combat);
      this.mode = this.add(new EnumSetting("Mode", ForceEat.Mode.Both));
      this.autoSwitch = this.add(new BooleanSetting("AutoSwitch", true));
      this.noInteract = this.add(new BooleanSetting("NoInteract", true));
      this.noMove = this.add(new BooleanSetting("NoMove", false));
      this.isEating = false;
      this.eatTicks = 0;
      this.prevSlot = -1;
      this.switchBackTimer = 0;
      this.startDelayTimer = 0;
      this.setChinese("主手吃苹果");
      INSTANCE = this;
   }

   public void onEnable() {
      if (!nullCheck()) {
         this.isEating = false;
         this.eatTicks = 0;
         this.prevSlot = -1;
         this.switchBackTimer = 0;
         this.startDelayTimer = 0;
      }
   }

   public void onDisable() {
      if (!nullCheck()) {
         if (this.isEating) {
            mc.field_1690.field_1904.method_23481(false);
            if (this.prevSlot != -1 && this.prevSlot < 9 && this.autoSwitch.getValue()) {
               InventoryUtil.switchToSlot(this.prevSlot);
            }
         }

         this.isEating = false;
         this.eatTicks = 0;
         this.switchBackTimer = 0;
         this.startDelayTimer = 0;
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!nullCheck()) {
         if (this.switchBackTimer > 0) {
            --this.switchBackTimer;
            if (this.switchBackTimer == 0 && this.prevSlot != -1 && this.prevSlot < 9 && this.autoSwitch.getValue()) {
               InventoryUtil.switchToSlot(this.prevSlot);
               this.prevSlot = -1;
            }
         }

         if (this.startDelayTimer > 0) {
            --this.startDelayTimer;
            if (this.startDelayTimer == 0) {
               this.startForceEating();
               mc.field_1690.field_1904.method_23481(true);
               mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808);
            }
         }

         if (this.isEating) {
            this.continueEating();
         } else {
            if (!mc.field_1724.method_6115()) {
               int appleSlot = this.findAppleSlot();
               if (appleSlot != -1 && this.startDelayTimer == 0) {
                  this.prevSlot = mc.field_1724.method_31548().field_7545;
                  if (this.autoSwitch.getValue()) {
                     InventoryUtil.switchToSlot(appleSlot);
                  }

                  this.startDelayTimer = 1;
               }
            } else {
               class_1799 stack = mc.field_1724.method_6047();
               boolean var10000;
               switch (((Mode)this.mode.getValue()).ordinal()) {
                  case 0 -> var10000 = stack.method_7909() == class_1802.field_8463;
                  case 1 -> var10000 = stack.method_7909() == class_1802.field_8367;
                  case 2 -> var10000 = stack.method_7909() == class_1802.field_8463 || stack.method_7909() == class_1802.field_8367;
                  default -> throw new MatchException((String)null, (Throwable)null);
               }

               boolean isApple = var10000;
               if (isApple && !this.isEating) {
                  this.startForceEating();
                  mc.field_1690.field_1904.method_23481(true);
               }
            }

         }
      }
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck() && this.isEating) {
         mc.field_1690.field_1904.method_23481(true);
      }
   }

   @EventListener
   public void onPacketSend(PacketEvent.Send event) {
      if (!nullCheck() && this.isEating) {
         class_2596<?> packet = event.getPacket();
         if (this.noInteract.getValue()) {
            if (packet instanceof class_2886) {
               class_2886 interactPacket = (class_2886)packet;
               if (interactPacket.method_12551() != class_1268.field_5808) {
                  event.cancel();
               }
            }

            if (packet instanceof class_2846) {
               event.cancel();
            }
         }

         if (this.noMove.getValue() && packet instanceof class_2828) {
            class_2828 movePacket = (class_2828)packet;
            if ((movePacket.method_36171() || movePacket.method_36172()) && (Math.abs(movePacket.method_12269((double)0.0F) - mc.field_1724.method_23317()) > (double)0.5F || Math.abs(movePacket.method_12268((double)0.0F) - mc.field_1724.method_23318()) > (double)0.5F || Math.abs(movePacket.method_12274((double)0.0F) - mc.field_1724.method_23321()) > (double)0.5F)) {
               event.cancel();
            }
         }

      }
   }

   @EventListener
   public void onRender2D(Render2DEvent event) {
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

   private void startForceEating() {
      this.isEating = true;
      this.eatTicks = 0;
      this.switchBackTimer = 0;
   }

   private void continueEating() {
      ++this.eatTicks;
      if (this.eatTicks > 32) {
         this.stopEating();
      } else {
         if (!this.isAppleInHand()) {
            int appleSlot = this.findAppleSlot();
            if (appleSlot != -1) {
               if (this.autoSwitch.getValue()) {
                  InventoryUtil.switchToSlot(appleSlot);
               }
            } else {
               this.stopEating();
            }
         }

      }
   }

   private void stopEating() {
      this.isEating = false;
      this.eatTicks = 0;
      mc.field_1690.field_1904.method_23481(false);
      if (this.prevSlot != -1 && this.prevSlot < 9 && this.autoSwitch.getValue()) {
         this.switchBackTimer = 1;
      } else {
         this.prevSlot = -1;
      }

   }

   public static enum Mode {
      GoldenApple("GoldenApple"),
      EnchantedGoldenApple("EnchantedGoldenApple"),
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
