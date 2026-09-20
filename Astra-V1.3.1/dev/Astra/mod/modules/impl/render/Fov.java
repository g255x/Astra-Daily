package dev.Astra.mod.modules.impl.render;

import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.SliderSetting;

public class Fov extends Module {
   public static Fov INSTANCE;
   public final SliderSetting fov = this.add(new SliderSetting("Fov", (double)90.0F, (double)30.0F, (double)170.0F, (double)1.0F));
   public final SliderSetting itemFov = this.add(new SliderSetting("ItemFov", (double)70.0F, (double)30.0F, (double)170.0F, (double)1.0F));

   public Fov() {
      super("Fov", Module.Category.Render);
      this.setChinese("自定义视角");
      INSTANCE = this;
   }
}
