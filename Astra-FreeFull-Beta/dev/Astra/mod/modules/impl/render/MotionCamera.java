package dev.Astra.mod.modules.impl.render;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.utils.math.AnimateUtil;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;

public class MotionCamera extends Module {
   public static MotionCamera INSTANCE;
   public final BooleanSetting noFirstPerson = this.add(new BooleanSetting("NoFirstPerson", true));
   public final SliderSetting firstPersonSpeed = this.add(new SliderSetting("FirstPersonSpeed", 0.6, (double)0.0F, (double)1.0F, 0.01));
   public final SliderSetting speed = this.add(new SliderSetting("Speed", 0.3, (double)0.0F, (double)1.0F, 0.01));
   private double fakeX;
   private double fakeY;
   private double fakeZ;
   private double prevFakeX;
   private double prevFakeY;
   private double prevFakeZ;

   public MotionCamera() {
      super("MotionCamera", Module.Category.Render);
      INSTANCE = this;
      this.setChinese("运动相机");
   }

   public boolean on() {
      return this.isOn() && (!this.noFirstPerson.getValue() || !mc.field_1690.method_31044().method_31034());
   }

   public void onEnable() {
      if (!nullCheck()) {
         this.fakeX = mc.field_1724.method_23317();
         this.fakeY = mc.field_1724.method_23318() + (double)mc.field_1724.method_18381(mc.field_1724.method_18376());
         this.fakeZ = mc.field_1724.method_23321();
         this.prevFakeX = this.fakeX;
         this.prevFakeY = this.fakeY;
         this.prevFakeZ = this.fakeZ;
      }
   }

   @EventListener
   public void onUpdate(ClientTickEvent event) {
      if (!event.isPre() && !nullCheck()) {
         this.prevFakeX = this.fakeX;
         this.prevFakeY = this.fakeY;
         this.prevFakeZ = this.fakeZ;
         double speed = mc.field_1690.method_31044().method_31034() ? this.firstPersonSpeed.getValue() : this.speed.getValue();
         this.fakeX = AnimateUtil.animate(this.fakeX, mc.field_1724.method_23317(), speed);
         this.fakeY = AnimateUtil.animate(this.fakeY, mc.field_1724.method_23318() + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()), speed);
         this.fakeZ = AnimateUtil.animate(this.fakeZ, mc.field_1724.method_23321(), speed);
      }
   }

   public double getFakeX() {
      return MathUtil.interpolate(this.prevFakeX, this.fakeX, (double)mc.method_60646().method_60637(true));
   }

   public double getFakeY() {
      return MathUtil.interpolate(this.prevFakeY, this.fakeY, (double)mc.method_60646().method_60637(true));
   }

   public double getFakeZ() {
      return MathUtil.interpolate(this.prevFakeZ, this.fakeZ, (double)mc.method_60646().method_60637(true));
   }
}
