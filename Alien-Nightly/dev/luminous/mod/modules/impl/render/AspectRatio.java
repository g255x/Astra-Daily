package dev.luminous.mod.modules.impl.render;

import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.SliderSetting;

public class AspectRatio extends Module {
   public static AspectRatio INSTANCE;
   public final SliderSetting ratio = this.add(new SliderSetting("Ratio", 1.78, (double)0.0F, (double)5.0F, 0.01));

   public AspectRatio() {
      super("AspectRatio", Module.Category.Render);
      this.setChinese("分辨率");
      INSTANCE = this;
   }
}
