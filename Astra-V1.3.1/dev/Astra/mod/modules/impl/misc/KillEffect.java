package dev.Astra.mod.modules.impl.misc;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.DeathEvent;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1299;
import net.minecraft.class_1538;
import net.minecraft.class_1657;
import net.minecraft.class_3417;
import net.minecraft.class_3419;

public class KillEffect extends Module {
   private final BooleanSetting lightning = this.add(new BooleanSetting("Lightning", true));
   private final BooleanSetting levelUp = this.add((new BooleanSetting("LevelUp", true)).setParent());
   private final SliderSetting lMaxPitch;
   private final SliderSetting lMinPitch;
   private final BooleanSetting trident;
   private final SliderSetting tMaxPitch;
   private final SliderSetting tMinPitch;
   private final SliderSetting factor;

   public KillEffect() {
      super("KillEffect", Module.Category.Misc);
      BooleanSetting var10009 = this.levelUp;
      Objects.requireNonNull(var10009);
      this.lMaxPitch = this.add(new SliderSetting("LMaxPitch", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, var10009::isOpen));
      var10009 = this.levelUp;
      Objects.requireNonNull(var10009);
      this.lMinPitch = this.add(new SliderSetting("LMinPitch", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, var10009::isOpen));
      this.trident = this.add((new BooleanSetting("Trident", false)).setParent());
      var10009 = this.trident;
      Objects.requireNonNull(var10009);
      this.tMaxPitch = this.add(new SliderSetting("TMaxPitch", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, var10009::isOpen));
      var10009 = this.trident;
      Objects.requireNonNull(var10009);
      this.tMinPitch = this.add(new SliderSetting("TMinPitch", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, var10009::isOpen));
      this.factor = this.add(new SliderSetting("Factor", (double)1.0F, (double)1.0F, (double)10.0F, (double)1.0F));
      this.setChinese("击杀效果");
   }

   @EventListener
   public void onPlayerDeath(DeathEvent event) {
      if (!nullCheck()) {
         class_1657 player = event.getPlayer();
         if (player != null) {
            for(int i = 0; (double)i < this.factor.getValue(); ++i) {
               this.doEffect(player);
            }

         }
      }
   }

   private void doEffect(class_1657 player) {
      double x = player.method_23317();
      double y = player.method_23318();
      double z = player.method_23321();
      if (this.lightning.getValue()) {
         class_1538 lightningEntity = new class_1538(class_1299.field_6112, mc.field_1687);
         lightningEntity.method_30634(x, y, z);
         lightningEntity.method_24203(x, y, z);
         mc.field_1687.method_53875(lightningEntity);
      }

      if (this.levelUp.getValue()) {
         mc.field_1687.method_43128(mc.field_1724, x, y, z, class_3417.field_14709, class_3419.field_15248, 100.0F, MathUtil.random(this.lMinPitch.getValueFloat(), this.lMaxPitch.getValueFloat()));
      }

      if (this.trident.getValue()) {
         mc.field_1687.method_60511(mc.field_1724, x, y, z, class_3417.field_14896, class_3419.field_15250, 999.0F, MathUtil.random(this.tMinPitch.getValueFloat(), this.tMaxPitch.getValueFloat()));
      }

   }
}
