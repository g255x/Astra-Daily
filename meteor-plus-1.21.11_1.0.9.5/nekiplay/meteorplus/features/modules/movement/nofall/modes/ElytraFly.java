package nekiplay.meteorplus.features.modules.movement.nofall.modes;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallMode;
import nekiplay.meteorplus.features.modules.movement.nofall.NoFallModes;
import net.minecraft.class_1304;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_2848.class_2849;

public class ElytraFly extends NoFallMode {
   public ElytraFly() {
      super(NoFallModes.Elytra_Fly);
   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (this.mc.field_1724.field_6017 > (double)2.0F) {
         FindItemResult elytra = InvUtils.find(new class_1792[]{class_1802.field_8833});
         if (elytra.found()) {
            int slot = elytra.slot();
            if (this.mc.field_1724.method_6118(class_1304.field_6174).method_7909() != class_1802.field_8833) {
               InvUtils.move().from(slot).toArmor(2);
            }
         }

         if (this.mc.field_1724.field_6017 > 2.7) {
            this.mc.field_1724.field_3944.method_52787(new class_2848(this.mc.field_1724, class_2849.field_12982));
            this.mc.field_1724.field_3944.method_52787(new class_2828.class_5911(true, this.mc.field_1724.field_5976));
            class_243 vel = this.mc.field_1724.method_18798();
            this.mc.field_1724.method_18800(vel.field_1352, (double)0.0F, vel.field_1350);
            this.mc.field_1724.field_6017 = (double)0.0F;
            this.mc.field_1724.method_24830(true);
         }
      }

   }
}
