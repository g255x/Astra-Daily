package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.events.impl.SendMovementPacketsEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.utils.math.Animation;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.JelloUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.asm.accessors.IEntity;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4587;

public class Follower extends Module {
   public static Follower INSTANCE;
   public class_1297 target;
   private final EnumSetting<Mode> mode;
   private final SliderSetting range;
   private final BooleanSetting onlyElytraFly;
   private final EnumSetting<TargetESP> renderMode;
   private final SliderSetting animationTime;
   private final EnumSetting<Easing> ease;
   private final ColorSetting color;
   private final ColorSetting outlineColor;
   private final ColorSetting hitColor;
   private final ColorSetting hitOutlineColor;
   private final Animation animation;
   private final BooleanSetting render;
   private final SliderSetting rotationSpeed;
   private final SliderSetting priority;

   public Follower() {
      super("Follower", Module.Category.Combat);
      this.mode = this.add(new EnumSetting("Mode", Follower.Mode.LowestDistance));
      this.range = this.add(new SliderSetting("Range", (double)50.0F, (double)5.0F, (double)100.0F));
      this.onlyElytraFly = this.add(new BooleanSetting("OnlyElytraFly", true));
      this.renderMode = this.add(new EnumSetting("RenderMode", Follower.TargetESP.Fill));
      this.animationTime = this.add(new SliderSetting("AnimationTime", (double)200.0F, (double)0.0F, (double)2000.0F, (double)1.0F));
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut));
      this.color = this.add(new ColorSetting("Color", new Color(160, 0, 225, 50)));
      this.outlineColor = this.add(new ColorSetting("OutlineColor", new Color(255, 255, 255, 100)));
      this.hitColor = this.add(new ColorSetting("HitColor", new Color(255, 255, 255, 150)));
      this.hitOutlineColor = this.add(new ColorSetting("HitOutlineColor", new Color(255, 255, 255, 150)));
      this.animation = new Animation();
      this.render = this.add(new BooleanSetting("Render", true));
      this.rotationSpeed = this.add(new SliderSetting("RotationSpeed", (double)180.0F, (double)10.0F, (double)360.0F));
      this.priority = this.add(new SliderSetting("Priority", (double)5.0F, (double)0.0F, (double)10.0F));
      this.setChinese("自动追踪");
      INSTANCE = this;
   }

   public String getInfo() {
      return this.target == null ? null : this.target.method_5477().getString();
   }

   public void onDisable() {
      this.target = null;
   }

   @EventListener
   public void onTick(TickEvent event) {
      if (!nullCheck()) {
         if (this.onlyElytraFly.getValue() && !ElytraFly.INSTANCE.isOn()) {
            this.target = null;
            return;
         }

         this.target = this.findTarget();
      }

   }

   @EventListener
   public void onSendMovementPackets(SendMovementPacketsEvent event) {
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (!nullCheck() && this.target != null && (!this.onlyElytraFly.getValue() || ElytraFly.INSTANCE.isOn())) {
         class_243 hitVec = this.getHitVec(this.target);
         event.setTarget(hitVec, this.rotationSpeed.getValueFloat(), this.priority.getValueFloat());
      }

   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.target != null && this.render.getValue()) {
         this.doRender(matrixStack, mc.method_60646().method_60637(true), this.target);
      }

   }

   public void doRender(class_4587 stack, float partialTicks, class_1297 entity) {
      switch (((TargetESP)this.renderMode.getValue()).ordinal()) {
         case 0 -> Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), false, true);
         case 1 -> Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), true, true);
         case 2 -> JelloUtil.drawJello(stack, entity, this.color.getValue());
         case 3 -> Render3DUtil.drawTargetEsp(stack, this.target, this.color.getValue());
      }

   }

   private class_1297 findTarget() {
      class_1297 bestTarget = null;

      for(class_1297 entity : Alien.THREAD.getEntities()) {
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
      if (entity instanceof class_1657 player) {
         if (entity == mc.field_1724) {
            return false;
         } else if (Alien.FRIEND.isFriend(player)) {
            return false;
         } else if (!entity.method_5805()) {
            return false;
         } else {
            double distance = mc.field_1724.method_33571().method_1022(entity.method_19538());
            return !(distance > this.range.getValue());
         }
      } else {
         return false;
      }
   }

   private class_243 getHitVec(class_1297 entity) {
      class_243 eyePos = mc.field_1724.method_33571();
      class_238 box = entity.method_5829();
      double targetCenterX = (box.field_1323 + box.field_1320) / (double)2.0F;
      double targetCenterY = (box.field_1322 + box.field_1325) / (double)2.0F;
      double targetCenterZ = (box.field_1321 + box.field_1324) / (double)2.0F;
      double playerEyeHeight = (double)mc.field_1724.method_18381(mc.field_1724.method_18376());
      double heightOffset = playerEyeHeight * (double)0.5F;
      if (entity instanceof class_1657) {
         heightOffset = (double)entity.method_17682() * (double)0.5F;
      }

      double targetY = box.field_1322 + heightOffset;
      double closestX = Math.max(box.field_1323, Math.min(eyePos.field_1352, box.field_1320));
      double closestZ = Math.max(box.field_1321, Math.min(eyePos.field_1350, box.field_1324));
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
      ThunderHack;

      // $FF: synthetic method
      private static TargetESP[] $values() {
         return new TargetESP[]{Fill, Box, Jello, ThunderHack};
      }
   }
}
