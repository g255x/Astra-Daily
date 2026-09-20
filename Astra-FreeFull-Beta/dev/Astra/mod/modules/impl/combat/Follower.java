package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.JelloUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4587;

public class Follower extends Module {
   public static Follower INSTANCE;
   private class_1297 target;
   private class_243 directionVec = null;
   private final EnumSetting<Mode> mode;
   private final SliderSetting range;
   private final BooleanSetting autoDisable;
   private final SliderSetting steps;
   private final SliderSetting priority;
   private final EnumSetting<TargetESP> renderMode;
   private final SliderSetting animationTime;
   private final EnumSetting<Easing> ease;
   private final ColorSetting color;
   private final ColorSetting outlineColor;
   private final ColorSetting hitColor;
   private final ColorSetting hitOutlineColor;
   private final Animation animation;
   private final BooleanSetting render;

   public Follower() {
      super("Follower", Module.Category.Combat);
      this.mode = this.add(new EnumSetting("Mode", Follower.Mode.LowestDistance));
      this.range = this.add((new SliderSetting("Range", (double)60.0F, (double)5.0F, (double)200.0F, 0.1)).setSuffix("m"));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", true));
      this.steps = this.add(new SliderSetting("Steps", (double)1.0F, (double)0.0F, (double)1.0F, 0.1));
      this.priority = this.add(new SliderSetting("Priority", 0, 0, 100));
      this.renderMode = this.add(new EnumSetting("RenderMode", Follower.TargetESP.Fill));
      this.animationTime = this.add((new SliderSetting("AnimationTime", (double)200.0F, (double)0.0F, (double)2000.0F, (double)1.0F)).setSuffix("ms"));
      this.ease = this.add(new EnumSetting("Ease", Easing.Linear));
      this.color = this.add(new ColorSetting("Color", new Color(1291648252, true)));
      this.outlineColor = this.add(new ColorSetting("OutlineColor", new Color(-1275068417, true)));
      this.hitColor = this.add(new ColorSetting("HitColor", new Color(-1761607681, true)));
      this.hitOutlineColor = this.add(new ColorSetting("HitOutlineColor", new Color(-1442840577, true)));
      this.animation = new Animation();
      this.render = this.add(new BooleanSetting("Render", true));
      this.setChinese("自动追人");
      INSTANCE = this;
   }

   public String getInfo() {
      return this.target == null ? null : this.target.method_5477().getString();
   }

   public void onDisable() {
      this.target = null;
      this.directionVec = null;
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!event.isPost()) {
         if (!nullCheck()) {
            if (this.autoDisable.getValue() && !ElytraFly.INSTANCE.isOn()) {
               this.disable();
               return;
            }

            this.target = this.findTarget();
            this.directionVec = this.target != null ? this.getHitVec(this.target) : null;
         }

      }
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.directionVec != null) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.target != null && this.render.getValue() && this.renderMode.getValue() != Follower.TargetESP.None) {
         float partialTicks = mc.method_60646().method_60637(true);
         this.doRender(matrixStack, partialTicks, this.target);
      }

   }

   private void doRender(class_4587 stack, float partialTicks, class_1297 entity) {
      class_238 bb = entity.method_5829();
      class_243 interpolated = new class_243(MathUtil.interpolate(entity.field_6014, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_6036, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5969, entity.method_23321(), (double)partialTicks));
      class_238 renderBox = bb.method_997(interpolated.method_1020(entity.method_19538()));
      renderBox = renderBox.method_1009((double)0.0F, 0.1, (double)0.0F);
      switch (((TargetESP)this.renderMode.getValue()).ordinal()) {
         case 0:
            Render3DUtil.draw3DBox(stack, renderBox, ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), false, true);
            break;
         case 1:
            Render3DUtil.draw3DBox(stack, renderBox, ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), true, true);
            break;
         case 2:
            JelloUtil.drawJello(stack, entity, this.color.getValue());
            break;
         case 3:
            Render3DUtil.drawTargetEsp(stack, this.target, this.color.getValue());
         case 4:
      }

   }

   private class_1297 findTarget() {
      class_1297 bestTarget = null;

      for(class_1297 entity : Astra.THREAD.getEntities()) {
         if (this.isValidTarget(entity)) {
            if (bestTarget == null) {
               bestTarget = entity;
            } else {
               double distance = mc.field_1724.method_33571().method_1022(entity.method_19538());
               float health = EntityUtil.getHealth(entity);
               switch (((Mode)this.mode.getValue()).ordinal()) {
                  case 0:
                     double bestDistance = mc.field_1724.method_33571().method_1022(bestTarget.method_19538());
                     if (distance < bestDistance) {
                        bestTarget = entity;
                     }
                     break;
                  case 1:
                     float bestHealth = EntityUtil.getHealth(bestTarget);
                     if (health < bestHealth) {
                        bestTarget = entity;
                     } else if (health == bestHealth) {
                        double currentTargetDistance = mc.field_1724.method_33571().method_1022(bestTarget.method_19538());
                        if (distance < currentTargetDistance) {
                           bestTarget = entity;
                        }
                     }
               }
            }
         }
      }

      return bestTarget;
   }

   private boolean isValidTarget(class_1297 entity) {
      if (!(entity instanceof class_1657 player)) {
         return false;
      } else if (player == mc.field_1724) {
         return false;
      } else if (Astra.FRIEND.isFriend(player)) {
         return false;
      } else if (!entity.method_5805()) {
         return false;
      } else {
         double distance = mc.field_1724.method_33571().method_1022(entity.method_19538());
         return distance <= this.range.getValue();
      }
   }

   private class_243 getHitVec(class_1297 entity) {
      class_243 eyePos = mc.field_1724.method_33571();
      class_238 box = entity.method_5829();
      double closestX = Math.max(box.field_1323, Math.min(eyePos.field_1352, box.field_1320));
      double closestZ = Math.max(box.field_1321, Math.min(eyePos.field_1350, box.field_1324));
      double heightOffset = entity instanceof class_1657 ? (double)entity.method_17682() * (double)0.5F : (double)mc.field_1724.method_18381(mc.field_1724.method_18376()) * (double)0.5F;
      double targetY = box.field_1322 + heightOffset;
      return new class_243(closestX, targetY, closestZ);
   }

   public static enum Mode {
      LowestDistance,
      LowestHealth;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{LowestDistance, LowestHealth};
      }
   }

   public static enum TargetESP {
      Fill,
      Box,
      Jello,
      ThunderHack,
      None;

      // $FF: synthetic method
      private static TargetESP[] $values() {
         return new TargetESP[]{Fill, Box, Jello, ThunderHack, None};
      }
   }
}
