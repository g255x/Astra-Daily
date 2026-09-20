package dev.Astra.mod.modules.impl.player;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.events.impl.TickEvent;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.settings.enums.SwingSide;
import dev.Astra.mod.modules.settings.enums.Timing;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2886;

public class AutoPearl extends Module {
   public static AutoPearl INSTANCE;
   public static boolean throwing = false;
   public final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final EnumSetting<Timing> timing;
   public final EnumSetting<SwingSide> interactSwing;
   private final BooleanSetting rotation;
   private final BooleanSetting yawStep;
   private final BooleanSetting whenElytra;
   private final SliderSetting steps;
   private final SliderSetting fov;
   private final SliderSetting priority;
   private final BooleanSetting sync;

   public AutoPearl() {
      super("AutoPearl", Module.Category.Player);
      this.timing = this.add(new EnumSetting("Timing", Timing.All));
      this.interactSwing = this.add(new EnumSetting("Swing", SwingSide.All));
      this.rotation = this.add(new BooleanSetting("Rotation", true));
      this.yawStep = this.add((new BooleanSetting("YawStep", false)).setParent());
      BooleanSetting var10006 = this.yawStep;
      Objects.requireNonNull(var10006);
      this.whenElytra = this.add(new BooleanSetting("FallFlying", true, var10006::isOpen));
      BooleanSetting var10009 = this.yawStep;
      Objects.requireNonNull(var10009);
      this.steps = this.add(new SliderSetting("Steps", 0.05, (double)0.0F, (double)1.0F, 0.01, var10009::isOpen));
      var10009 = this.yawStep;
      Objects.requireNonNull(var10009);
      this.fov = this.add(new SliderSetting("Fov", (double)20.0F, (double)0.0F, (double)360.0F, 0.1, var10009::isOpen));
      BooleanSetting var10008 = this.yawStep;
      Objects.requireNonNull(var10008);
      this.priority = this.add(new SliderSetting("Priority", 100, 0, 100, var10008::isOpen));
      var10006 = this.yawStep;
      Objects.requireNonNull(var10006);
      this.sync = this.add(new BooleanSetting("Sync", true, var10006::isOpen));
      this.setChinese("扔珍珠");
      INSTANCE = this;
   }

   public void onEnable() {
      if (nullCheck()) {
         this.disable();
      } else {
         if (INSTANCE.inventory.getValue()) {
            if (InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8634) == -1) {
               this.disable();
               return;
            }
         } else if (InventoryUtil.findItem(class_1802.field_8634) == -1) {
            this.disable();
            return;
         }

         if (!this.shouldYawStep()) {
            if (!this.inventory.getValue() || EntityUtil.inInventory()) {
               if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                  this.throwPearl(mc.field_1724.method_36454(), mc.field_1724.method_36455());
                  this.disable();
               }
            }
         }
      }
   }

   @EventListener
   public void onUpdate(TickEvent event) {
      if (!nullCheck()) {
         if ((!this.timing.is(Timing.Pre) || !event.isPost()) && (!this.timing.is(Timing.Post) || !event.isPre())) {
            if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
               if (!this.shouldYawStep()) {
                  this.throwPearl(mc.field_1724.method_36454(), mc.field_1724.method_36455());
                  this.disable();
               } else if (Astra.ROTATION.inFov(Astra.ROTATION.rotationYaw, Astra.ROTATION.rotationPitch, this.fov.getValueFloat(), mc.field_1724.method_36454(), mc.field_1724.method_36455())) {
                  if (this.sync.getValue()) {
                     this.throwPearl(mc.field_1724.method_36454(), mc.field_1724.method_36455());
                  } else {
                     throwing = true;
                     if (mc.field_1724.method_6047().method_7909() == class_1802.field_8634) {
                        sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
                        EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
                     } else {
                        int pearl;
                        if (this.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8634)) != -1) {
                           InventoryUtil.inventorySwap(pearl, mc.field_1724.method_31548().field_7545);
                           sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
                           EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
                           InventoryUtil.inventorySwap(pearl, mc.field_1724.method_31548().field_7545);
                           EntityUtil.syncInventory();
                        } else {
                           pearl = InventoryUtil.findItem(class_1802.field_8634);
                           if (pearl != -1) {
                              int old = mc.field_1724.method_31548().field_7545;
                              InventoryUtil.switchToSlot(pearl);
                              sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
                              EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
                              InventoryUtil.switchToSlot(old);
                           }
                        }
                     }

                     throwing = false;
                  }

                  this.disable();
               }

            }
         }
      }
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.shouldYawStep()) {
         event.setRotation(mc.field_1724.method_36454(), mc.field_1724.method_36455(), this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   private boolean shouldYawStep() {
      return this.whenElytra.getValue() || !mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() : false;
   }

   public void throwPearl(float yaw, float pitch) {
      throwing = true;
      if (mc.field_1724.method_6047().method_7909() == class_1802.field_8634) {
         if (this.rotation.getValue()) {
            Astra.ROTATION.snapAt(yaw, pitch);
         }

         sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, yaw, pitch));
         EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
         if (this.rotation.getValue()) {
            Astra.ROTATION.snapBack();
         }
      } else {
         int pearl;
         if (this.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8634)) != -1) {
            InventoryUtil.inventorySwap(pearl, mc.field_1724.method_31548().field_7545);
            if (this.rotation.getValue()) {
               Astra.ROTATION.snapAt(yaw, pitch);
            }

            sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, yaw, pitch));
            EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
            InventoryUtil.inventorySwap(pearl, mc.field_1724.method_31548().field_7545);
            EntityUtil.syncInventory();
            if (this.rotation.getValue()) {
               Astra.ROTATION.snapBack();
            }
         } else {
            pearl = InventoryUtil.findItem(class_1802.field_8634);
            if (pearl != -1) {
               int old = mc.field_1724.method_31548().field_7545;
               InventoryUtil.switchToSlot(pearl);
               if (this.rotation.getValue()) {
                  Astra.ROTATION.snapAt(yaw, pitch);
               }

               sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, yaw, pitch));
               EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
               InventoryUtil.switchToSlot(old);
               if (this.rotation.getValue()) {
                  Astra.ROTATION.snapBack();
               }
            }
         }
      }

      throwing = false;
   }
}
