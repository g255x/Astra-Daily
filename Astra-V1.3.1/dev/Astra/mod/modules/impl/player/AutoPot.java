package dev.Astra.mod.modules.impl.player;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2886;
import net.minecraft.class_9334;

public class AutoPot extends Module {
   public static AutoPot INSTANCE;
   private final SliderSetting delay = this.add((new SliderSetting("Delay", (double)5.0F, (double)0.0F, (double)10.0F, 0.1)).setSuffix("s"));
   private final BooleanSetting speed = this.add(new BooleanSetting("Speed", true));
   private final BooleanSetting resistance = this.add(new BooleanSetting("Resistance", true));
   private final BooleanSetting strength = this.add(new BooleanSetting("Strength", true));
   private final BooleanSetting slowFalling = this.add(new BooleanSetting("SlowFalling", false));
   private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BindSetting speedKey = this.add(new BindSetting("SpeedKey", -1));
   private final BindSetting strengthKey = this.add(new BindSetting("StrengthKey", -1));
   private final BindSetting resistanceKey = this.add(new BindSetting("ResistanceKey", -1));
   private final SliderSetting prePotTime = this.add((new SliderSetting("PrePotTime", 0.2, (double)0.0F, (double)1.0F, 0.01)).setSuffix("s"));
   private final Timer delayTimer = new Timer();
   private boolean throwing = false;
   private boolean turtlePress;
   private boolean speedPress;
   private boolean strengthPress;

   public AutoPot() {
      super("AutoPot", Module.Category.Player);
      this.setChinese("自动药水");
      INSTANCE = this;
      Astra.EVENT_BUS.subscribe(new AutoPotTick());
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
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!this.inventory.getValue() || EntityUtil.inInventory()) {
         if (this.delayTimer.passedMs(this.delay.getValue() * (double)1000.0F)) {
            if (!this.onlyGround.getValue() || (mc.field_1724.method_24828() || Astra.PLAYER.isInWeb(mc.field_1724)) && !mc.field_1687.method_22347(new BlockPosX(mc.field_1724.method_19538().method_1031((double)0.0F, (double)-1.0F, (double)0.0F)))) {
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

                  if (shouldThrow) {
                     this.throwing = this.checkThrow((class_1291)class_1294.field_5907.comp_349());
                     if (this.isThrow()) {
                        this.throwPotion((class_1291)class_1294.field_5907.comp_349());
                        return;
                     }
                  }
               }

               if (this.speed.getValue()) {
                  boolean shouldThrow = false;
                  class_1293 speedEffect = mc.field_1724.method_6112(class_1294.field_5904);
                  if (speedEffect == null) {
                     shouldThrow = true;
                  } else if ((double)speedEffect.method_5584() / (double)20.0F < this.prePotTime.getValue()) {
                     shouldThrow = true;
                  }

                  if (shouldThrow) {
                     this.throwing = this.checkThrow((class_1291)class_1294.field_5904.comp_349());
                     if (this.isThrow()) {
                        this.throwPotion((class_1291)class_1294.field_5904.comp_349());
                        return;
                     }
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

                  if (shouldThrow) {
                     this.throwing = this.checkThrow((class_1291)class_1294.field_5910.comp_349());
                     if (this.isThrow()) {
                        this.throwPotion((class_1291)class_1294.field_5910.comp_349());
                        return;
                     }
                  }
               }

               if (this.slowFalling.getValue()) {
                  boolean shouldThrow = false;
                  class_1293 slowFallingEffect = mc.field_1724.method_6112(class_1294.field_5906);
                  if (slowFallingEffect == null) {
                     shouldThrow = true;
                  } else if ((double)slowFallingEffect.method_5584() / (double)20.0F < this.prePotTime.getValue()) {
                     shouldThrow = true;
                  }

                  if (shouldThrow) {
                     this.throwing = this.checkThrow((class_1291)class_1294.field_5906.comp_349());
                     if (this.isThrow()) {
                        this.throwPotion((class_1291)class_1294.field_5906.comp_349());
                     }
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
      } else {
         newSlot = findPotion(targetEffect);
         if (newSlot != -1) {
            Astra.ROTATION.snapAt(Astra.ROTATION.rotationYaw, 90.0F);
            InventoryUtil.switchToSlot(newSlot);
            sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
            InventoryUtil.switchToSlot(oldSlot);
            Astra.ROTATION.snapBack();
            this.delayTimer.reset();
         }
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
         if (!Module.nullCheck() && !event.isPost()) {
            if (!AutoPot.this.inventory.getValue() || EntityUtil.inInventory()) {
               if (Wrapper.mc.field_1755 == null) {
                  if (AutoPot.this.resistanceKey.isPressed()) {
                     if (!AutoPot.this.turtlePress && AutoPot.this.checkThrow((class_1291)class_1294.field_5907.comp_349())) {
                        AutoPot.this.throwPotion((class_1291)class_1294.field_5907.comp_349());
                        AutoPot.this.turtlePress = true;
                        return;
                     }
                  } else {
                     AutoPot.this.turtlePress = false;
                  }

                  if (AutoPot.this.strengthKey.isPressed()) {
                     if (!AutoPot.this.strengthPress && AutoPot.this.checkThrow((class_1291)class_1294.field_5910.comp_349())) {
                        AutoPot.this.throwPotion((class_1291)class_1294.field_5910.comp_349());
                        AutoPot.this.strengthPress = true;
                        return;
                     }
                  } else {
                     AutoPot.this.strengthPress = false;
                  }

                  if (AutoPot.this.speedKey.isPressed()) {
                     if (!AutoPot.this.speedPress && AutoPot.this.checkThrow((class_1291)class_1294.field_5904.comp_349())) {
                        AutoPot.this.throwPotion((class_1291)class_1294.field_5904.comp_349());
                        AutoPot.this.speedPress = true;
                     }
                  } else {
                     AutoPot.this.speedPress = false;
                  }
               } else {
                  AutoPot.this.speedPress = false;
                  AutoPot.this.turtlePress = false;
                  AutoPot.this.strengthPress = false;
               }

            }
         }
      }
   }
}
