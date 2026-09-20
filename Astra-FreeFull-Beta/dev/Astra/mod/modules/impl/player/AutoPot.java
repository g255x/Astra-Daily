package dev.Astra.mod.modules.impl.player;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.combat.Aura;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2246;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2886;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class AutoPot extends Module {
   public static AutoPot INSTANCE;
   private final SliderSetting delay = this.add((new SliderSetting("Delay", 0.2, (double)0.0F, (double)10.0F, 0.1)).setSuffix("s"));
   private final SliderSetting prePotTime = this.add((new SliderSetting("PrePotTime", (double)0.5F, (double)0.0F, (double)1.0F, 0.01)).setSuffix("s"));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", (double)12.0F, (double)0.0F, (double)20.0F, 0.1));
   private final SliderSetting health = this.add(new SliderSetting("Health", (double)16.0F, (double)0.0F, (double)36.0F, 0.1));
   private final BooleanSetting speed = this.add(new BooleanSetting("Speed", true));
   private final BooleanSetting resistance = this.add(new BooleanSetting("Resistance", true));
   private final BooleanSetting strength = this.add((new BooleanSetting("Strength", true)).setParent());
   private final BooleanSetting onlyAuraStrength = this.add(new BooleanSetting("OnlyAura", true, () -> this.strength.isOpen()));
   private final BindSetting speedKey = this.add(new BindSetting("SpeedKey", -1));
   private final BindSetting resistanceKey = this.add(new BindSetting("ResistanceKey", -1));
   private final BindSetting strengthKey = this.add(new BindSetting("StrengthKey", -1));
   private final Timer delayTimer = new Timer();
   private boolean throwing = false;
   private boolean speedPressed = false;
   private boolean resistancePressed = false;
   private boolean strengthPressed = false;
   private final AutoPotTick autoPotTick;

   public AutoPot() {
      super("AutoPot", Module.Category.Player);
      this.setChinese("自动药水");
      INSTANCE = this;
      this.autoPotTick = new AutoPotTick();
      Astra.EVENT_BUS.subscribe(this.autoPotTick);
   }

   public static int findPotionInventorySlot(class_1291 targetEffect) {
      for(int i = 35; i >= 0; --i) {
         class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
         if (class_1792.method_7880(itemStack.method_7909()) == class_1792.method_7880(class_1802.field_8436)) {
            class_1844 potionContentsComponent = (class_1844)itemStack.method_57825(class_9334.field_49651, class_1844.field_49274);

            for(class_1293 effect : potionContentsComponent.method_57397()) {
               if (effect.method_5579().comp_349() == targetEffect) {
                  return i < 9 ? i + 36 : i;
               }
            }
         }
      }

      return -1;
   }

   public static int findPotion(class_1291 targetEffect) {
      for(int i = 0; i < 9; ++i) {
         class_1799 itemStack = mc.field_1724.method_31548().method_5438(i);
         if (class_1792.method_7880(itemStack.method_7909()) == class_1792.method_7880(class_1802.field_8436)) {
            class_1844 potionContentsComponent = (class_1844)itemStack.method_57825(class_9334.field_49651, class_1844.field_49274);

            for(class_1293 effect : potionContentsComponent.method_57397()) {
               if (effect.method_5579().comp_349() == targetEffect) {
                  return i;
               }
            }
         }
      }

      return -1;
   }

   public void onDisable() {
      this.throwing = false;
      this.speedPressed = false;
      this.resistancePressed = false;
      this.strengthPressed = false;
      Astra.EVENT_BUS.unsubscribe(this.autoPotTick);
   }

   private boolean isNearGround() {
      class_243 eyePos = mc.field_1724.method_33571();
      class_243 bottomCenter = mc.field_1724.method_5829().method_1005();
      bottomCenter = new class_243(bottomCenter.field_1352, mc.field_1724.method_5829().field_1322, bottomCenter.field_1350);
      class_243 end = bottomCenter.method_1031((double)0.0F, -0.1, (double)0.0F);
      class_3965 hit = mc.field_1687.method_17742(new class_3959(eyePos, end, class_3960.field_17558, class_242.field_1348, mc.field_1724));
      if (hit != null && hit.method_17783() == class_240.field_1332) {
         class_2680 blockState = mc.field_1687.method_8320(hit.method_17777());
         return blockState.method_26212(mc.field_1687, hit.method_17777()) && blockState.method_26204() != class_2246.field_10343;
      } else {
         return false;
      }
   }

   private boolean hasNearbyPlayer() {
      double rangeSq = this.targetRange.getValue() * this.targetRange.getValue();

      for(class_1657 player : mc.field_1687.method_18456()) {
         if (player != mc.field_1724 && mc.field_1724.method_5858(player) <= rangeSq) {
            return true;
         }
      }

      return false;
   }

   private boolean hasNearbyPlayerWithTotem() {
      double rangeSq = this.targetRange.getValue() * this.targetRange.getValue();

      for(class_1657 player : mc.field_1687.method_18456()) {
         if (player != mc.field_1724 && !(mc.field_1724.method_5858(player) > rangeSq) && player.method_6079().method_7909() == class_1802.field_8288) {
            return true;
         }
      }

      return false;
   }

   private void forceThrowPotion(class_1291 targetEffect) {
      if (this.delayTimer.passedMs(this.delay.getValue() * (double)1000.0F)) {
         if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
            if (EntityUtil.inInventory()) {
               if (findPotion(targetEffect) != -1 || this.inventory.getValue() && findPotionInventorySlot(targetEffect) != -1) {
                  this.throwPotion(targetEffect);
               }
            }
         }
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!this.inventory.getValue() || EntityUtil.inInventory()) {
         if (this.delayTimer.passedMs(this.delay.getValue() * (double)1000.0F)) {
            if (!this.onlyGround.getValue() || this.isNearGround()) {
               if (this.speed.getValue()) {
                  boolean shouldThrow = false;
                  class_1293 speedEffect = mc.field_1724.method_6112(class_1294.field_5904);
                  if (speedEffect == null) {
                     shouldThrow = true;
                  } else if ((double)speedEffect.method_5584() / (double)20.0F < this.prePotTime.getValue()) {
                     shouldThrow = true;
                  }

                  float currentHealth = mc.field_1724.method_6032() + mc.field_1724.method_6067();
                  if (shouldThrow && (!this.hasNearbyPlayer() || (double)currentHealth <= this.health.getValue())) {
                     shouldThrow = false;
                  }

                  if (shouldThrow && this.checkThrow((class_1291)class_1294.field_5904.comp_349())) {
                     this.throwPotion((class_1291)class_1294.field_5904.comp_349());
                     return;
                  }
               }

               if (this.resistance.getValue()) {
                  boolean shouldThrow = false;
                  class_1293 resistanceEffect = mc.field_1724.method_6112(class_1294.field_5907);
                  if (resistanceEffect == null) {
                     shouldThrow = true;
                  } else if (resistanceEffect.method_5578() < 2) {
                     shouldThrow = true;
                  } else if ((double)resistanceEffect.method_5584() / (double)20.0F < this.prePotTime.getValue()) {
                     shouldThrow = true;
                  }

                  if (shouldThrow && !this.hasNearbyPlayerWithTotem()) {
                     shouldThrow = false;
                  }

                  if (shouldThrow && this.checkThrow((class_1291)class_1294.field_5907.comp_349())) {
                     this.throwPotion((class_1291)class_1294.field_5907.comp_349());
                     return;
                  }
               }

               if (this.strength.getValue()) {
                  boolean shouldThrow = false;
                  class_1293 strengthEffect = mc.field_1724.method_6112(class_1294.field_5910);
                  if (strengthEffect == null) {
                     shouldThrow = true;
                  } else if ((double)strengthEffect.method_5584() / (double)20.0F < this.prePotTime.getValue()) {
                     shouldThrow = true;
                  }

                  boolean auraOk = !this.onlyAuraStrength.getValue() || Aura.target != null && Aura.target instanceof class_1657;
                  float currentHealth = mc.field_1724.method_6032() + mc.field_1724.method_6067();
                  if (shouldThrow && (!auraOk || (double)currentHealth <= this.health.getValue())) {
                     shouldThrow = false;
                  }

                  if (shouldThrow && this.checkThrow((class_1291)class_1294.field_5910.comp_349())) {
                     this.throwPotion((class_1291)class_1294.field_5910.comp_349());
                     return;
                  }
               }

            }
         }
      }
   }

   public void throwPotion(class_1291 targetEffect) {
      int oldSlot = mc.field_1724.method_31548().field_7545;
      int newSlot;
      if (this.inventory.getValue() && (newSlot = findPotionInventorySlot(targetEffect)) != -1) {
         Astra.ROTATION.snapAt(Astra.ROTATION.rotationYaw, 90.0F);
         InventoryUtil.inventorySwap(newSlot, mc.field_1724.method_31548().field_7545);
         sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
         InventoryUtil.inventorySwap(newSlot, mc.field_1724.method_31548().field_7545);
         EntityUtil.syncInventory();
         Astra.ROTATION.snapBack();
         this.delayTimer.reset();
      } else if ((newSlot = findPotion(targetEffect)) != -1) {
         Astra.ROTATION.snapAt(Astra.ROTATION.rotationYaw, 90.0F);
         InventoryUtil.switchToSlot(newSlot);
         sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
         InventoryUtil.switchToSlot(oldSlot);
         Astra.ROTATION.snapBack();
         this.delayTimer.reset();
      }

   }

   public boolean isThrow() {
      return this.throwing;
   }

   public boolean checkThrow(class_1291 targetEffect) {
      if (!EntityUtil.inInventory()) {
         return false;
      } else if (this.usingPause.getValue() && mc.field_1724.method_6115()) {
         return false;
      } else {
         return findPotion(targetEffect) != -1 || this.inventory.getValue() && findPotionInventorySlot(targetEffect) != -1;
      }
   }

   public class AutoPotTick {
      @EventListener
      public void onTick(ClientTickEvent event) {
         if (!event.isPost() && !Module.nullCheck()) {
            if (Wrapper.mc.field_1755 == null) {
               if (AutoPot.this.speedKey.isPressed()) {
                  if (!AutoPot.this.speedPressed) {
                     AutoPot.this.speedPressed = true;
                     AutoPot.this.forceThrowPotion((class_1291)class_1294.field_5904.comp_349());
                  }
               } else {
                  AutoPot.this.speedPressed = false;
               }

               if (AutoPot.this.resistanceKey.isPressed()) {
                  if (!AutoPot.this.resistancePressed) {
                     AutoPot.this.resistancePressed = true;
                     AutoPot.this.forceThrowPotion((class_1291)class_1294.field_5907.comp_349());
                  }
               } else {
                  AutoPot.this.resistancePressed = false;
               }

               if (AutoPot.this.strengthKey.isPressed()) {
                  if (!AutoPot.this.strengthPressed) {
                     AutoPot.this.strengthPressed = true;
                     AutoPot.this.forceThrowPotion((class_1291)class_1294.field_5910.comp_349());
                  }
               } else {
                  AutoPot.this.strengthPressed = false;
               }

            }
         }
      }
   }
}
