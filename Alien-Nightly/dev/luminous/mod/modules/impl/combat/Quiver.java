package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1753;
import net.minecraft.class_1802;

public class Quiver extends Module {
   private final BooleanSetting instant = this.add(new BooleanSetting("InstantRotate", true));
   private final SliderSetting time = this.add(new SliderSetting("Time", (double)0.11F, (double)0.0F, (double)1.0F, 0.01));
   private final BooleanSetting onlyPress = this.add(new BooleanSetting("OnlyPress", false));
   private final BindSetting key = this.add(new BindSetting("ActiveKey", -1));
   boolean bow = false;
   boolean pressed = false;
   boolean switching = false;
   int startSlot;

   public Quiver() {
      super("Quiver", Module.Category.Combat);
      this.setChinese("头顶射箭");
   }

   public boolean onEnable() {
      this.bow = false;
      return false;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.key.isPressed()) {
         if (!this.pressed && !this.switching) {
            int bow = InventoryUtil.findItem(class_1802.field_8102);
            if (bow != -1) {
               this.startSlot = mc.field_1724.method_31548().field_7545;
               InventoryUtil.switchToSlot(bow);
               mc.field_1690.field_1904.method_23481(true);
               this.switching = true;
               this.pressed = true;
            }
         }
      } else {
         this.pressed = false;
      }

      if (this.switching && (!mc.field_1690.field_1904.method_1434() || mc.field_1724.method_6115() && mc.field_1724.method_6030().method_7909() != class_1802.field_8102)) {
         InventoryUtil.switchToSlot(this.startSlot);
         this.switching = false;
      }

      this.bow = mc.field_1724.method_6115() && (mc.field_1724.method_6058() == class_1268.field_5808 ? mc.field_1724.method_6047() : mc.field_1724.method_6079()).method_7909() instanceof class_1753;
      if (this.bow && (!this.onlyPress.getValue() || this.switching) && (double)class_1753.method_7722(mc.field_1724.method_6048()) >= this.time.getValue()) {
         if (this.instant.getValue()) {
            Alien.ROTATION.snapAt(Alien.ROTATION.rotationYaw, -90.0F);
         }

         mc.field_1690.field_1904.method_23481(false);
         mc.field_1761.method_2897(mc.field_1724);
         if (this.instant.getValue()) {
            Alien.ROTATION.snapBack();
         }
      }

   }

   @EventListener
   public void onRotate(UpdateRotateEvent event) {
      if (this.bow && !this.instant.getValue()) {
         event.setPitch(-90.0F);
      }

   }
}
