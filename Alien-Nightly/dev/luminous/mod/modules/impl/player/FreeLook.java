package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.LookDirectionEvent;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.mod.modules.Module;
import net.minecraft.class_4587;

public class FreeLook extends Module {
   public static FreeLook INSTANCE;
   private float fakeYaw;
   private float fakePitch;
   private float prevFakeYaw;
   private float prevFakePitch;

   public FreeLook() {
      super("FreeLook", Module.Category.Player);
      this.setChinese("自由视角");
      INSTANCE = this;
   }

   public boolean onEnable() {
      if (nullCheck()) {
         this.disable();
      } else {
         this.fakePitch = mc.field_1724.method_36455();
         this.fakeYaw = mc.field_1724.method_36454();
         this.prevFakePitch = this.fakePitch;
         this.prevFakeYaw = this.fakeYaw;
      }

      return false;
   }

   @EventListener
   public void onLookDirection(LookDirectionEvent event) {
      this.fakeYaw += (float)event.getCursorDeltaX() * 0.15F;
      this.fakePitch += (float)event.getCursorDeltaY() * 0.15F;
      event.cancel();
   }

   public void onRender3D(class_4587 matrixStack) {
      this.prevFakeYaw = this.fakeYaw;
      this.prevFakePitch = this.fakePitch;
   }

   public float getFakeYaw() {
      return MathUtil.interpolate(this.prevFakeYaw, this.fakeYaw, mc.method_60646().method_60637(true));
   }

   public float getFakePitch() {
      return MathUtil.interpolate(this.prevFakePitch, this.fakePitch, mc.method_60646().method_60637(true));
   }
}
