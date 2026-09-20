package dev.Astra.mod.modules.impl.render;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.Render3DEvent;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;

public class Zoom extends Module {
   public static Zoom INSTANCE;
   public static boolean on = false;
   public final EnumSetting<Easing> ease;
   final SliderSetting fov;
   final Animation animation;
   private final SliderSetting animTime;
   public double currentFov;
   private final ZoomAnim zoomAnim;

   public Zoom() {
      super("Zoom", Module.Category.Render);
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut));
      this.fov = this.add(new SliderSetting("ZoomFov", (double)60.0F, (double)0.0F, (double)130.0F, (double)1.0F));
      this.animation = new Animation();
      this.animTime = this.add(new SliderSetting("AnimTime", 300, 0, 1000));
      this.setChinese("放大");
      INSTANCE = this;
      this.zoomAnim = new ZoomAnim();
      Astra.EVENT_BUS.subscribe(this.zoomAnim);
   }

   public void onDisable() {
      Astra.EVENT_BUS.unsubscribe(this.zoomAnim);
   }

   public void onEnable() {
      if (nullCheck()) {
         this.disable();
      }

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
