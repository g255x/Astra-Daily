package dev.Astra.mod.modules.impl.misc;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.TotemEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.core.impl.CommandManager;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_2661;
import net.minecraft.class_2868;

public class AutoLog extends Module {
   public static boolean loggedOut = false;
   private final BooleanSetting logOnEnable = this.add(new BooleanSetting("LogOnEnable", false));
   private final BooleanSetting onPop = this.add(new BooleanSetting("OnPop", true));
   private final BooleanSetting lowArmor = this.add(new BooleanSetting("LowArmor", true));
   private final BooleanSetting totemLess = this.add((new BooleanSetting("TotemLess", true)).setParent());
   private final SliderSetting totems;
   private final BooleanSetting autoDisable;
   private final BooleanSetting showReason;

   public AutoLog() {
      super("AutoLog", Module.Category.Misc);
      BooleanSetting var10009 = this.totemLess;
      Objects.requireNonNull(var10009);
      this.totems = this.add(new SliderSetting("Totems", (double)2.0F, (double)0.0F, (double)20.0F, (double)1.0F, var10009::isOpen));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", true));
      this.showReason = this.add(new BooleanSetting("ShowReason", false));
      this.setChinese("自动下线");
   }

   public void onEnable() {
      if (this.logOnEnable.getValue()) {
         this.disconnect("Enabled");
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      int totem;
      if (this.totemLess.getValue() && (double)(totem = InventoryUtil.getItemCount(class_1802.field_8288)) <= this.totems.getValue()) {
         this.disconnect("You have too few totems (" + totem + ").");
      } else {
         if (this.lowArmor.getValue()) {
            for(class_1799 armor : mc.field_1724.method_31548().field_7548) {
               if (!armor.method_7960() && EntityUtil.getDamagePercent(armor) < 5) {
                  this.disconnect("Your armor has keyCodec durability of less than 5%.");
                  return;
               }
            }
         }

      }
   }

   @EventListener
   public void onPop(TotemEvent event) {
      if (this.onPop.getValue() && event.getPlayer() == mc.field_1724) {
         this.disconnect("You poped 1 totem!");
      }

   }

   public void onLogout() {
      if (this.autoDisable.getValue()) {
         this.disable();
      }

   }

   private void disconnect(String reason) {
      loggedOut = true;
      CommandManager.sendMessage("§4[AutoLog] " + reason);
      mc.method_1562().method_52787(new class_2868(114514));
      if (this.showReason.getValue()) {
         mc.field_1724.field_3944.method_52781(new class_2661(class_2561.method_43470("[AutoLog]" + reason)));
      }

   }
}
