package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.AnimateUtil;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4587;
import org.lwjgl.opengl.GL11;

public class Scaffold extends Module {
   private static class_243 lastVec3d;
   public final SliderSetting rotateTime = this.add(new SliderSetting("KeepRotate", (double)1000.0F, (double)0.0F, (double)3000.0F, (double)10.0F));
   private final BooleanSetting tower = this.add(new BooleanSetting("Tower", true));
   private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", false));
   private final BooleanSetting safeWalk = this.add(new BooleanSetting("SafeWalk", false));
   private final BooleanSetting rotate = this.add((new BooleanSetting("Rotate", true)).setParent());
   private final BooleanSetting yawStep = this.add((new BooleanSetting("YawStep", false, () -> this.rotate.isOpen() && this.rotate.isOpen())).setParent());
   private final BooleanSetting whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.rotate.isOpen()));
   private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, (double)0.0F, (double)1.0F, 0.01, () -> this.rotate.isOpen() && this.yawStep.isOpen()));
   private final BooleanSetting checkFov = this.add((new BooleanSetting("OnlyLooking", true, () -> this.rotate.isOpen() && this.yawStep.isOpen())).setParent());
   private final SliderSetting fov = this.add(new SliderSetting("Fov", (double)20.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.checkFov.isOpen() && this.rotate.isOpen() && this.yawStep.isOpen()));
   private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.rotate.isOpen() && this.yawStep.isOpen()));
   private final BooleanSetting render = this.add((new BooleanSetting("Render", true)).setParent());
   public final ColorSetting color;
   public final ColorSetting outlineColor;
   public final SliderSetting sliderSpeed;
   private final BooleanSetting esp;
   private final BooleanSetting fill;
   private final BooleanSetting outline;
   private final Timer timer;
   private final Timer towerTimer;
   private class_243 vec;
   private class_2338 pos;

   public Scaffold() {
      super("Scaffold", Module.Category.Movement);
      Color var10005 = new Color(255, 255, 255, 100);
      BooleanSetting var10006 = this.render;
      Objects.requireNonNull(var10006);
      this.color = this.add(new ColorSetting("Color", var10005, var10006::isOpen));
      var10005 = new Color(255, 255, 255, 100);
      var10006 = this.render;
      Objects.requireNonNull(var10006);
      this.outlineColor = this.add(new ColorSetting("OutlineColor", var10005, var10006::isOpen));
      BooleanSetting var10009 = this.render;
      Objects.requireNonNull(var10009);
      this.sliderSpeed = this.add(new SliderSetting("SliderSpeed", 0.2, 0.01, (double)1.0F, 0.01, var10009::isOpen));
      var10006 = this.render;
      Objects.requireNonNull(var10006);
      this.esp = this.add(new BooleanSetting("ESP", true, var10006::isOpen));
      var10006 = this.render;
      Objects.requireNonNull(var10006);
      this.fill = this.add(new BooleanSetting("Fill", true, var10006::isOpen));
      var10006 = this.render;
      Objects.requireNonNull(var10006);
      this.outline = this.add(new BooleanSetting("Box", true, var10006::isOpen));
      this.timer = new Timer();
      this.towerTimer = new Timer();
      this.setChinese("自动搭路");
   }

   @EventListener(
      priority = -100
   )
   public void onMove(MoveEvent event) {
      if (this.safeWalk.getValue()) {
         SafeWalk.INSTANCE.onMove(event);
      }

   }

   @EventListener
   public void onRotation(RotationEvent event) {
      if (this.rotate.getValue() && !this.timer.passed((long)this.rotateTime.getValueInt()) && this.vec != null) {
         event.setTarget(this.vec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   public boolean onEnable() {
      lastVec3d = null;
      this.pos = null;
      return false;
   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.render.getValue()) {
         if (this.esp.getValue()) {
            GL11.glEnable(3042);
            double temp = 0.01;

            for(double i = (double)0.0F; i < 0.8; i += temp) {
               HoleSnap.drawCircle(matrixStack, ColorUtil.injectAlpha(this.color.getValue(), (int)Math.min((double)(this.color.getValue().getAlpha() * 2) / (0.8 / temp), (double)255.0F)), i, new class_243(MathUtil.interpolate(mc.field_1724.field_6038, mc.field_1724.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(mc.field_1724.field_5971, mc.field_1724.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(mc.field_1724.field_5989, mc.field_1724.method_23321(), (double)mc.method_60646().method_60637(true))), 5);
            }

            GL11.glDisable(3042);
         }

         if (this.pos != null) {
            class_243 cur = this.pos.method_46558();
            if (lastVec3d == null) {
               lastVec3d = cur;
            } else {
               lastVec3d = new class_243(AnimateUtil.animate(lastVec3d.method_10216(), cur.field_1352, this.sliderSpeed.getValue()), AnimateUtil.animate(lastVec3d.method_10214(), cur.field_1351, this.sliderSpeed.getValue()), AnimateUtil.animate(lastVec3d.method_10215(), cur.field_1350, this.sliderSpeed.getValue()));
            }

            Render3DUtil.draw3DBox(matrixStack, new class_238(lastVec3d.method_1031((double)0.5F, (double)0.5F, (double)0.5F), lastVec3d.method_1031((double)-0.5F, (double)-0.5F, (double)-0.5F)), ColorUtil.injectAlpha(this.color.getValue(), this.color.getValue().getAlpha()), ColorUtil.injectAlpha(this.outlineColor.getValue(), this.outlineColor.getValue().getAlpha()), this.outline.getValue(), this.fill.getValue());
         }
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      int block = InventoryUtil.findBlock();
      if (block != -1) {
         class_2338 placePos = mc.field_1724.method_24515().method_10074();
         if (BlockUtil.clientCanPlace(placePos, false)) {
            int old = mc.field_1724.method_31548().field_7545;
            if (BlockUtil.getPlaceSide(placePos) == null) {
               double distance = (double)1000.0F;
               class_2338 bestPos = null;

               for(class_2350 i : class_2350.values()) {
                  if (i != class_2350.field_11036 && BlockUtil.canPlace(placePos.method_10093(i)) && (bestPos == null || mc.field_1724.method_5707(placePos.method_10093(i).method_46558()) < distance)) {
                     bestPos = placePos.method_10093(i);
                     distance = mc.field_1724.method_5707(placePos.method_10093(i).method_46558());
                  }
               }

               if (bestPos == null) {
                  return;
               }

               placePos = bestPos;
            }

            if (this.rotate.getValue()) {
               class_2350 side = BlockUtil.getPlaceSide(placePos);
               this.vec = placePos.method_10093(side).method_46558().method_1031((double)side.method_10153().method_10163().method_10263() * (double)0.5F, (double)side.method_10153().method_10163().method_10264() * (double)0.5F, (double)side.method_10153().method_10163().method_10260() * (double)0.5F);
               this.timer.reset();
               if (!this.faceVector(this.vec)) {
                  return;
               }
            }

            InventoryUtil.switchToSlot(block);
            BlockUtil.placeBlock(placePos, false, this.packetPlace.getValue());
            InventoryUtil.switchToSlot(old);
            if (this.rotate.getValue()) {
               Alien.ROTATION.snapBack();
            }

            this.pos = placePos;
            if (this.tower.getValue() && mc.field_1690.field_1903.method_1434() && !MovementUtil.isMoving()) {
               MovementUtil.setMotionY(0.42);
               MovementUtil.setMotionX((double)0.0F);
               MovementUtil.setMotionZ((double)0.0F);
               if (this.towerTimer.passed(1500L)) {
                  MovementUtil.setMotionY(-0.28);
                  this.towerTimer.reset();
               }
            } else {
               this.towerTimer.reset();
            }
         }
      }

   }

   private boolean shouldYawStep() {
      return this.whenElytra.getValue() || !mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() && !Velocity.INSTANCE.noRotation() : false;
   }

   private boolean faceVector(class_243 directionVec) {
      if (!this.shouldYawStep()) {
         Alien.ROTATION.lookAt(directionVec);
         return true;
      } else {
         return Alien.ROTATION.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
      }
   }
}
