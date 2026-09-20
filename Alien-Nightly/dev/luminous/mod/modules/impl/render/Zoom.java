package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render3DEvent;
import dev.luminous.api.utils.math.Animation;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;

public class Zoom extends Module {
   public static Zoom INSTANCE;
   public static boolean on = false;
   public final EnumSetting<Easing> ease;
   final SliderSetting fov;
   final Animation animation;
   private final SliderSetting animTime;
   public double currentFov;

   public Zoom() {
      super("Zoom", Module.Category.Render);
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut));
      this.fov = this.add(new SliderSetting("ZoomFov", (double)60.0F, (double)0.0F, (double)130.0F, (double)1.0F));
      this.animation = new Animation();
      this.animTime = this.add(new SliderSetting("AnimTime", 300, 0, 1000));
      this.setChinese("放大");
      INSTANCE = this;
      Alien.EVENT_BUS.subscribe(new ZoomAnim());
   }

   public boolean onEnable() {
      if (nullCheck()) {
         this.disable();
      }

      return false;
   }

   public class ZoomAnim {
      @EventListener
      public void onRender3D(Render3DEvent event) {
         if (Zoom.this.isOn()) {
            Zoom.this.currentFov = Zoom.this.animation.get(Zoom.this.fov.getValue(), (long)Zoom.this.animTime.getValueInt(), Zoom.this.ease.getValue());
            Zoom.on = true;
         } else if (Zoom.on) {
            Zoom.this.currentFov = Zoom.this.animation.get((double)0.0F, (long)Zoom.this.animTime.getValueInt(), Zoom.this.ease.getValue());
            if ((int)Zoom.this.currentFov == 0) {
               Zoom.on = false;
            }
         }

      }
   }
}
