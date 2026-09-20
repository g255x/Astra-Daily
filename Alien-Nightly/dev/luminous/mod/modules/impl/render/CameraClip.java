package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.FadeUtils;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_4587;
import net.minecraft.class_5498;

public class CameraClip extends Module {
   public static CameraClip INSTANCE;
   public final SliderSetting distance = this.add(new SliderSetting("Distance", (double)4.0F, (double)1.0F, (double)20.0F));
   public final SliderSetting animateTime = this.add(new SliderSetting("AnimationTime", 200, 0, 1000));
   private final EnumSetting<Easing> ease;
   final FadeUtils animation;
   private final BooleanSetting noFront;
   boolean first;

   public CameraClip() {
      super("CameraClip", Module.Category.Render);
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut));
      this.animation = new FadeUtils(300L);
      this.noFront = this.add(new BooleanSetting("NoFront", false));
      this.first = false;
      this.setChinese("摄像机穿墙");
      INSTANCE = this;
   }

   public void onRender3D(class_4587 matrixStack) {
      if (mc.field_1690.method_31044() == class_5498.field_26666 && this.noFront.getValue()) {
         mc.field_1690.method_31043(class_5498.field_26664);
      }

      this.animation.setLength((long)this.animateTime.getValueInt());
      if (mc.field_1690.method_31044() == class_5498.field_26664) {
         if (!this.first) {
            this.first = true;
            this.animation.reset();
         }
      } else if (this.first) {
         this.first = false;
         this.animation.reset();
      }

   }

   public double getDistance() {
      double quad = mc.field_1690.method_31044() == class_5498.field_26664 ? (double)1.0F - this.animation.ease(this.ease.getValue()) : this.animation.ease(this.ease.getValue());
      return this.distance.getValue() * quad - (double)1.0F + (double)1.0F;
   }
}
