package dev.Astra.mod.modules.impl.misc;

import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.SliderSetting;

public class ExtraTab extends Module {
   public static ExtraTab INSTANCE;
   public final SliderSetting size = this.add(new SliderSetting("Size", (double)200.0F, (double)80.0F, (double)1000.0F, (double)1.0F));
   public final SliderSetting columns = this.add(new SliderSetting("Columns", (double)20.0F, (double)1.0F, (double)100.0F, (double)1.0F));

   public ExtraTab() {
      super("ExtraTab", Module.Category.Misc);
      this.setChinese("玩家列表");
      INSTANCE = this;
   }
}
