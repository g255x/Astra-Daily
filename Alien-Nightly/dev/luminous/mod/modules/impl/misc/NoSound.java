package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PlaySoundEvent;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import java.util.ArrayList;
import net.minecraft.class_3414;
import net.minecraft.class_3417;

public class NoSound extends Module {
   static final ArrayList<class_3414> armor = new ArrayList();
   public static NoSound INSTANCE;
   private final BooleanSetting equip = this.add(new BooleanSetting("ArmorEquip", true));
   private final BooleanSetting explode = this.add(new BooleanSetting("Explode", true));
   private final BooleanSetting attack = this.add(new BooleanSetting("Attack", true));
   private final BooleanSetting teleport = this.add(new BooleanSetting("Teleport", true));
   private final BooleanSetting throwConfig = this.add(new BooleanSetting("Throw", true));
   private final BooleanSetting potion = this.add(new BooleanSetting("Potion", true));
   private final BooleanSetting elytra = this.add(new BooleanSetting("Elytra", true));

   public NoSound() {
      super("NoSound", Module.Category.Misc);
      this.setChinese("去除声音");
      INSTANCE = this;
   }

   @EventListener
   public void onPlaySound(PlaySoundEvent event) {
      if (this.equip.getValue()) {
         for(class_3414 se : armor) {
            if (event.sound.method_4775() == se.method_14833()) {
               event.cancel();
               return;
            }
         }
      }

      if (!this.explode.getValue() || event.sound.method_4775() != ((class_3414)class_3417.field_15152.comp_349()).method_14833() && event.sound.method_4775() != class_3417.field_14803.method_14833()) {
         if (this.attack.getValue() && (event.sound.method_4775() == class_3417.field_14625.method_14833() || event.sound.method_4775() == class_3417.field_14999.method_14833() || event.sound.method_4775() == class_3417.field_14840.method_14833())) {
            event.cancel();
         }

         if (this.teleport.getValue() && event.sound.method_4775() == class_3417.field_46945.method_14833()) {
            event.cancel();
         }

         if (this.potion.getValue() && event.sound.method_4775() == class_3417.field_14839.method_14833()) {
            event.cancel();
         }

         if (this.elytra.getValue() && event.sound.method_4775() == class_3417.field_14572.method_14833()) {
            event.cancel();
         }

         if (this.throwConfig.getValue() && (event.sound.method_4775() == class_3417.field_14757.method_14833() || event.sound.method_4775() == class_3417.field_15012.method_14833() || event.sound.method_4775() == class_3417.field_14637.method_14833() || event.sound.method_4775() == class_3417.field_14873.method_14833() || event.sound.method_4775() == class_3417.field_14910.method_14833())) {
            event.cancel();
         }
      } else {
         event.cancel();
      }

   }

   static {
      armor.add((class_3414)class_3417.field_21866.comp_349());
      armor.add((class_3414)class_3417.field_14684.comp_349());
      armor.add((class_3414)class_3417.field_15191.comp_349());
      armor.add((class_3414)class_3417.field_14966.comp_349());
      armor.add((class_3414)class_3417.field_15103.comp_349());
      armor.add((class_3414)class_3417.field_14761.comp_349());
      armor.add((class_3414)class_3417.field_14862.comp_349());
      armor.add((class_3414)class_3417.field_14581.comp_349());
      armor.add((class_3414)class_3417.field_14883.comp_349());
   }
}
