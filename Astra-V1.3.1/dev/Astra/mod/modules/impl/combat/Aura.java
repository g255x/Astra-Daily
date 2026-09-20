package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.JelloUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.asm.accessors.IEntity;
import dev.Astra.asm.accessors.ILivingEntity;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.impl.movement.Velocity;
import dev.Astra.mod.modules.settings.enums.SwingSide;
import dev.Astra.mod.modules.settings.enums.Timing;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1621;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2879;
import net.minecraft.class_3989;
import net.minecraft.class_4587;
import net.minecraft.class_5134;
import net.minecraft.class_2824.class_5907;

public class Aura extends Module {
   public static Aura INSTANCE;
   public static class_1297 target;
   public final EnumSetting<Page> page;
   public final SliderSetting range;
   private final SliderSetting targetRange;
   private final EnumSetting<Cooldown> cooldownMode;
   private final BooleanSetting reset;
   private final EnumSetting<SwingSide> swingMode;
   private final SliderSetting hurtTime;
   private final SliderSetting cooldown;
   private final SliderSetting wallRange;
   private final BooleanSetting whileEating;
   private final BooleanSetting weaponOnly;
   private final EnumSetting<Timing> timing;
   private final BooleanSetting Players;
   private final BooleanSetting armorLow;
   private final BooleanSetting Mobs;
   private final BooleanSetting Animals;
   private final BooleanSetting Villagers;
   private final BooleanSetting Slimes;
   private final EnumSetting<TargetMode> targetMode;
   private final EnumSetting<TargetESP> mode;
   private final SliderSetting animationTime;
   private final EnumSetting<Easing> ease;
   private final ColorSetting color;
   private final ColorSetting outlineColor;
   private final ColorSetting hitColor;
   private final ColorSetting hitOutlineColor;
   private final Animation animation;
   private final BooleanSetting rotate;
   private final BooleanSetting yawStep;
   private final BooleanSetting whenElytra;
   private final SliderSetting steps;
   private final BooleanSetting checkFov;
   private final SliderSetting fov;
   private final SliderSetting priority;
   private final Timer tick;
   public class_243 directionVec;

   public Aura() {
      super("Aura", Module.Category.Combat);
      this.page = this.add(new EnumSetting("Page", Aura.Page.General));
      this.range = this.add(new SliderSetting("Range", (double)6.0F, (double)0.1F, (double)7.0F, () -> this.page.getValue() == Aura.Page.General));
      this.targetRange = this.add(new SliderSetting("TargetRange", (double)8.0F, (double)0.1F, (double)14.0F, () -> this.page.getValue() == Aura.Page.General));
      this.cooldownMode = this.add(new EnumSetting("CooldownMode", Aura.Cooldown.Delay, () -> this.page.getValue() == Aura.Page.General));
      this.reset = this.add(new BooleanSetting("Reset", true, () -> this.page.getValue() == Aura.Page.General && this.cooldownMode.is(Aura.Cooldown.Delay)));
      this.swingMode = this.add(new EnumSetting("Swing", SwingSide.All, () -> this.page.getValue() == Aura.Page.General));
      this.hurtTime = this.add(new SliderSetting("HurtTime", (double)10.0F, (double)0.0F, (double)10.0F, (double)1.0F, () -> this.page.getValue() == Aura.Page.General));
      this.cooldown = this.add(new SliderSetting("Cooldown", (double)1.1F, (double)0.0F, (double)1.2F, 0.01, () -> this.page.getValue() == Aura.Page.General));
      this.wallRange = this.add(new SliderSetting("WallRange", (double)6.0F, (double)0.1F, (double)7.0F, () -> this.page.getValue() == Aura.Page.General));
      this.whileEating = this.add(new BooleanSetting("WhileUsing", true, () -> this.page.getValue() == Aura.Page.General));
      this.weaponOnly = this.add(new BooleanSetting("WeaponOnly", true, () -> this.page.getValue() == Aura.Page.General));
      this.timing = this.add(new EnumSetting("Timing", Timing.All, () -> this.page.getValue() == Aura.Page.General));
      this.Players = this.add((new BooleanSetting("Players", true, () -> this.page.getValue() == Aura.Page.Target)).setParent());
      this.armorLow = this.add(new BooleanSetting("ArmorLow", true, () -> this.page.getValue() == Aura.Page.Target && this.Players.isOpen()));
      this.Mobs = this.add(new BooleanSetting("Mobs", true, () -> this.page.getValue() == Aura.Page.Target));
      this.Animals = this.add(new BooleanSetting("Animals", true, () -> this.page.getValue() == Aura.Page.Target));
      this.Villagers = this.add(new BooleanSetting("Villagers", true, () -> this.page.getValue() == Aura.Page.Target));
      this.Slimes = this.add(new BooleanSetting("Slimes", true, () -> this.page.getValue() == Aura.Page.Target));
      this.targetMode = this.add(new EnumSetting("Filter", Aura.TargetMode.DISTANCE, () -> this.page.getValue() == Aura.Page.Target));
      this.mode = this.add(new EnumSetting("TargetESP", Aura.TargetESP.Fill, () -> this.page.getValue() == Aura.Page.Render));
      this.animationTime = this.add(new SliderSetting("AnimationTime", (double)200.0F, (double)0.0F, (double)2000.0F, (double)1.0F, () -> this.page.getValue() == Aura.Page.Render));
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut, () -> this.page.getValue() == Aura.Page.Render));
      this.color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 50), () -> this.page.getValue() == Aura.Page.Render));
      this.outlineColor = this.add(new ColorSetting("OutlineColor", new Color(255, 255, 255, 50), () -> this.page.getValue() == Aura.Page.Render));
      this.hitColor = this.add(new ColorSetting("HitColor", new Color(255, 255, 255, 150), () -> this.page.getValue() == Aura.Page.Render));
      this.hitOutlineColor = this.add(new ColorSetting("HitOutlineColor", new Color(255, 255, 255, 150), () -> this.page.getValue() == Aura.Page.Render));
      this.animation = new Animation();
      this.rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.getValue() == Aura.Page.Rotate));
      this.yawStep = this.add((new BooleanSetting("YawStep", false, () -> this.rotate.isOpen() && this.page.getValue() == Aura.Page.Rotate)).setParent());
      this.whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == Aura.Page.Rotate));
      this.steps = this.add(new SliderSetting("Steps", 0.05, (double)0.0F, (double)1.0F, 0.01, () -> this.page.getValue() == Aura.Page.Rotate && this.yawStep.isOpen()));
      this.checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.page.getValue() == Aura.Page.Rotate && this.yawStep.isOpen()));
      this.fov = this.add(new SliderSetting("Fov", (double)20.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.checkFov.getValue() && this.page.getValue() == Aura.Page.Rotate && this.yawStep.isOpen()));
      this.priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.page.getValue() == Aura.Page.Rotate && this.yawStep.isOpen()));
      this.tick = new Timer();
      this.directionVec = null;
      this.setChinese("杀戮光环");
      INSTANCE = this;
   }

   public static void doRender(class_4587 stack, float partialTicks, class_1297 entity, Color color, Color outlineColor, TargetESP mode) {
      switch (mode.ordinal()) {
         case 0 -> Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), color, outlineColor, false, true);
         case 1 -> Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), color, outlineColor, true, true);
         case 2 -> JelloUtil.drawJello(stack, entity, color);
         case 3 -> Render3DUtil.drawTargetEsp(stack, target, color);
      }

   }

   public static float getAttackCooldownProgressPerTick() {
      return (float)((double)1.0F / mc.field_1724.method_45325(class_5134.field_23723) * (double)20.0F);
   }

   public void onRender3D(class_4587 matrixStack) {
      if (target != null) {
         this.doRender(matrixStack, mc.method_60646().method_60637(true), target, this.mode.getValue());
      }

   }

   public void doRender(class_4587 stack, float partialTicks, class_1297 entity, TargetESP mode) {
      switch (mode.ordinal()) {
         case 0 -> Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), false, true);
         case 1 -> Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), true, true);
         case 2 -> JelloUtil.drawJello(stack, entity, this.color.getValue());
         case 3 -> Render3DUtil.drawTargetEsp(stack, target, this.color.getValue());
      }

   }

   public String getInfo() {
      return target == null ? null : target.method_5477().getString();
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if ((!this.timing.is(Timing.Pre) || !event.isPost()) && (!this.timing.is(Timing.Post) || !event.isPre())) {
            if (this.weaponOnly.getValue() && !EntityUtil.isHoldingWeapon(mc.field_1724)) {
               target = null;
            } else {
               target = this.getTarget((double)this.range.getValueFloat());
               if (target == null) {
                  target = this.getTarget((double)this.targetRange.getValueFloat());
               } else {
                  this.doAura();
               }
            }
         }
      }
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (target != null && this.rotate.getValue() && this.shouldYawStep()) {
         this.directionVec = this.getAttackVec(target);
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   @EventListener
   public void onPacket(PacketEvent.Send event) {
      class_2596<?> packet;
      if (this.reset.getValue() && ((packet = event.getPacket()) instanceof class_2879 || packet instanceof class_2824 && Criticals.getInteractType((class_2824)packet) == class_5907.field_29172)) {
         this.tick.reset();
      }

   }

   private boolean check() {
      if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
         return false;
      } else {
         int at = (int)(this.tick.getMs() / 50L);
         if (this.cooldownMode.getValue() == Aura.Cooldown.Vanilla) {
            at = ((ILivingEntity)mc.field_1724).getLastAttackedTicks();
         }

         if (!((double)Math.max((float)((int)((float)at * Astra.SERVER.getTPSFactor())) / getAttackCooldownProgressPerTick(), 0.0F) >= this.cooldown.getValue())) {
            return false;
         } else {
            class_1297 entity = target;
            if (entity instanceof class_1309) {
               class_1309 entity2 = (class_1309)entity;
               if ((double)entity2.field_6235 > this.hurtTime.getValue()) {
                  return false;
               }
            }

            return this.whileEating.getValue() || !mc.field_1724.method_6115();
         }
      }
   }

   private void doAura() {
      if (this.check()) {
         if (!this.rotate.getValue() || this.faceVector(this.getAttackVec(target))) {
            this.animation.to = (double)1.0F;
            this.animation.from = (double)1.0F;
            mc.method_1562().method_52787(class_2824.method_34206(target, mc.field_1724.method_5715()));
            mc.field_1724.method_7350();
            EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
            this.tick.reset();
            if (this.rotate.getValue() && !this.shouldYawStep()) {
               Astra.ROTATION.snapBack();
            }

         }
      }
   }

   private class_243 getAttackVec(class_1297 entity) {
      return MathUtil.getClosestPointToBox(mc.field_1724.method_33571(), entity.method_5829());
   }

   private boolean shouldYawStep() {
      if (this.whenElytra.getValue() || !mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying())) {
         return this.yawStep.getValue() && !Velocity.INSTANCE.noRotation();
      } else {
         return false;
      }
   }

   public boolean faceVector(class_243 directionVec) {
      if (!this.shouldYawStep()) {
         Astra.ROTATION.lookAt(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         if (Astra.ROTATION.inFov(directionVec, this.fov.getValueFloat())) {
            return true;
         } else {
            return !this.checkFov.getValue();
         }
      }
   }

   public class_1297 getTarget(double range) {
      class_1297 target = null;
      double getDistance = range;
      double maxHealth = (double)36.0F;

      for(class_1297 entity : Astra.THREAD.getEntities()) {
         if (this.isEnemy(entity)) {
            class_243 hitVec = this.getAttackVec(entity);
            if (!(mc.field_1724.method_33571().method_1022(hitVec) > range) && (mc.field_1724.method_6057(entity) || !(mc.field_1724.method_33571().method_1022(hitVec) > this.wallRange.getValue())) && CombatUtil.isValid(entity)) {
               if (target == null) {
                  target = entity;
                  getDistance = mc.field_1724.method_33571().method_1022(hitVec);
                  maxHealth = (double)EntityUtil.getHealth(entity);
               } else {
                  if (this.armorLow.getValue() && entity instanceof class_1657 && EntityUtil.isArmorLow((class_1657)entity, 10)) {
                     target = entity;
                     break;
                  }

                  if (this.targetMode.getValue() == Aura.TargetMode.HEALTH && (double)EntityUtil.getHealth(entity) < maxHealth) {
                     target = entity;
                     maxHealth = (double)EntityUtil.getHealth(entity);
                  } else if (this.targetMode.getValue() == Aura.TargetMode.DISTANCE && mc.field_1724.method_33571().method_1022(hitVec) < getDistance) {
                     target = entity;
                     getDistance = mc.field_1724.method_33571().method_1022(hitVec);
                  }
               }
            }
         }
      }

      return target;
   }

   private boolean isEnemy(class_1297 entity) {
      if (entity instanceof class_1621) {
         return this.Slimes.getValue();
      } else if (entity instanceof class_1657) {
         return this.Players.getValue();
      } else if (!(entity instanceof class_1646) && !(entity instanceof class_3989)) {
         if (entity instanceof class_1429) {
            return this.Animals.getValue();
         } else {
            return entity instanceof class_1308 ? this.Mobs.getValue() : false;
         }
      } else {
         return this.Villagers.getValue();
      }
   }

   public static enum Page {
      General,
      Rotate,
      Target,
      Render;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Rotate, Target, Render};
      }
   }

   public static enum Cooldown {
      Vanilla,
      Delay;

      // $FF: synthetic method
      private static Cooldown[] $values() {
         return new Cooldown[]{Vanilla, Delay};
      }
   }

   private static enum TargetMode {
      DISTANCE,
      HEALTH;

      // $FF: synthetic method
      private static TargetMode[] $values() {
         return new TargetMode[]{DISTANCE, HEALTH};
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

   public static enum Mode {
      Mace,
      Axe,
      Sword;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Mace, Axe, Sword};
      }
   }
}
