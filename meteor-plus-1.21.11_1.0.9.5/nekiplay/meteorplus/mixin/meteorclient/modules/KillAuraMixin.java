package nekiplay.meteorplus.mixin.meteorclient.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.KillAura;
import meteordevelopment.meteorclient.systems.modules.movement.Flight;
import meteordevelopment.meteorclient.systems.modules.movement.elytrafly.ElytraFly;
import meteordevelopment.meteorclient.utils.entity.DamageUtils;
import nekiplay.meteorplus.features.modules.combat.AntiBotPlus;
import nekiplay.meteorplus.features.modules.combat.Teams;
import nekiplay.meteorplus.features.modules.combat.criticals.CriticalsPlus;
import nekiplay.meteorplus.features.modules.movement.elytrafly.ElytraFlyPlus;
import nekiplay.meteorplus.features.modules.movement.fly.FlyPlus;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   value = {KillAura.class},
   remap = false,
   priority = 1001
)
public class KillAuraMixin extends Module {
   @Unique
   private final SettingGroup sgTimingPlus;
   @Final
   @Shadow
   private final SettingGroup sgTargeting;
   @Final
   @Shadow
   private final SettingGroup sgTiming;
   @Final
   @Shadow
   private final Setting<Set<class_1299<?>>> entities;
   @Final
   @Shadow
   private final List<class_1297> targets;
   @Shadow
   @Final
   private final Setting<Boolean> customDelay;
   @Unique
   private final Setting<Boolean> smartDelayv2;
   @Unique
   private final Setting<Integer> maxHurtTime;
   @Unique
   private final Setting<Boolean> onlyCrits;
   @Unique
   private final Setting<Boolean> onlyCritsIgnoreFlight;
   @Unique
   private final Setting<Boolean> ignoreOnlyCritsOnLevitation;
   @Unique
   private final Setting<Boolean> ignoreSmartDelayForShulkerBulletAndGhastCharge;
   @Unique
   private final Setting<Boolean> ignoreOnlyCritsForOneHitEntity;
   @Unique
   private final Setting<Boolean> customDelayOneHit;
   @Unique
   private final Setting<Integer> hitDelay;
   @Unique
   private int hitTimer;

   @Shadow
   public class_1297 getTarget() {
      return !this.targets.isEmpty() ? (class_1297)this.targets.get(0) : null;
   }

   public KillAuraMixin(Category category, String name, String description) {
      super(category, name, description);
      this.sgTimingPlus = this.settings.createGroup("Meteor+ Timing");
      this.sgTargeting = this.settings.getGroup("Targeting");
      this.sgTiming = this.settings.getGroup("Timing");
      this.entities = this.sgTargeting.get("entities");
      this.targets = new ArrayList();
      this.customDelay = this.sgTiming.get("custom-delay");
      this.smartDelayv2 = this.sgTimingPlus.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("smart-delay-v2")).description("Calculate sword damage to enemy.")).defaultValue(true)).visible(() -> !(Boolean)this.customDelay.get())).build());
      SettingGroup var10001 = this.sgTimingPlus;
      IntSetting.Builder var10002 = ((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("max-enemy-hurt-time")).defaultValue(0)).min(0).sliderMax(20);
      Setting var10003 = this.smartDelayv2;
      Objects.requireNonNull(var10003);
      this.maxHurtTime = var10001.add(((IntSetting.Builder)var10002.visible(var10003::get)).build());
      this.onlyCrits = this.sgTimingPlus.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("only-crits")).description("Attack enemy only if this attack crit after jump.")).defaultValue(false)).build());
      var10001 = this.sgTimingPlus;
      BoolSetting.Builder var7 = (BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("ignore-only-crits-on-flight")).defaultValue(true);
      var10003 = this.onlyCrits;
      Objects.requireNonNull(var10003);
      this.onlyCritsIgnoreFlight = var10001.add(((BoolSetting.Builder)var7.visible(var10003::get)).build());
      var10001 = this.sgTimingPlus;
      var7 = (BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("ignore-only-crits-on-levetation")).defaultValue(true);
      var10003 = this.onlyCrits;
      Objects.requireNonNull(var10003);
      this.ignoreOnlyCritsOnLevitation = var10001.add(((BoolSetting.Builder)var7.visible(var10003::get)).build());
      this.ignoreSmartDelayForShulkerBulletAndGhastCharge = this.sgTimingPlus.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("ignore-delay-for-one-hit-entities")).description("Ignore attack delay for shulker bullet and fireball.")).defaultValue(true)).visible(() -> ((Set)this.entities.get()).contains(class_1299.field_6100) || ((Set)this.entities.get()).contains(class_1299.field_6066))).build());
      this.ignoreOnlyCritsForOneHitEntity = this.sgTimingPlus.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("ignore-only-crits-for-one-hit-entities")).description("Ignore only crits delay for shulker bullet and fireball.")).defaultValue(true)).visible(() -> (Boolean)this.ignoreSmartDelayForShulkerBulletAndGhastCharge.get() && (Boolean)this.onlyCrits.get())).build());
      this.customDelayOneHit = this.sgTimingPlus.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("custom-delay-for-one-hit-entities")).defaultValue(true)).visible(() -> ((Set)this.entities.get()).contains(class_1299.field_6100) || ((Set)this.entities.get()).contains(class_1299.field_6066))).build());
      var10001 = this.sgTimingPlus;
      IntSetting.Builder var9 = ((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("delay-for-one-hit-entities")).description("How fast you hit the entity in ticks.")).defaultValue(2)).min(0).sliderMax(60);
      var10003 = this.customDelayOneHit;
      Objects.requireNonNull(var10003);
      this.hitDelay = var10001.add(((IntSetting.Builder)var9.visible(var10003::get)).build());
   }

   @Inject(
      method = {"delayCheck"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void delayCheck(CallbackInfoReturnable<Boolean> cir) {
      Modules modules = Modules.get();
      if ((Boolean)this.onlyCrits.get() && !CriticalsPlus.allowCrit() && CriticalsPlus.needCrit(this.getTarget())) {
         if ((Boolean)this.ignoreOnlyCritsOnLevitation.get() && !((class_746)Objects.requireNonNull(this.mc.field_1724)).method_6059(class_1294.field_5902)) {
            cir.setReturnValue(false);
            return;
         }

         if ((Boolean)this.onlyCritsIgnoreFlight.get() && !modules.isActive(Flight.class) && !modules.isActive(FlyPlus.class) && !modules.isActive(ElytraFly.class) && !modules.isActive(ElytraFlyPlus.class)) {
            cir.setReturnValue(false);
            return;
         }

         if (!(Boolean)this.ignoreOnlyCritsOnLevitation.get() && !(Boolean)this.onlyCritsIgnoreFlight.get()) {
            cir.setReturnValue(false);
            return;
         }
      }

      float delay = (Boolean)this.customDelayOneHit.get() ? (float)(Integer)this.hitDelay.get() : 0.5F;
      if (this.oneHitEntity()) {
         if ((Boolean)this.customDelayOneHit.get()) {
            if ((float)this.hitTimer < delay) {
               ++this.hitTimer;
               cir.setReturnValue(false);
               return;
            }

            if ((Boolean)this.ignoreOnlyCritsForOneHitEntity.get()) {
               cir.setReturnValue(true);
               return;
            }

            if (this.oneHitEntity() && (Boolean)this.ignoreSmartDelayForShulkerBulletAndGhastCharge.get() && !(Boolean)this.onlyCrits.get()) {
               cir.setReturnValue(true);
               return;
            }
         } else {
            if ((Boolean)this.ignoreOnlyCritsForOneHitEntity.get()) {
               cir.setReturnValue(true);
               return;
            }

            if (this.oneHitEntity() && (Boolean)this.ignoreSmartDelayForShulkerBulletAndGhastCharge.get() && !(Boolean)this.onlyCrits.get()) {
               cir.setReturnValue(true);
               return;
            }
         }
      }

      if ((Boolean)this.smartDelayv2.get()) {
         class_1297 var5 = this.getTarget();
         if (var5 instanceof class_1309) {
            class_1309 livingEntity = (class_1309)var5;
            if ((double)DamageUtils.getAttackDamage(this.mc.field_1724, livingEntity) >= (double)livingEntity.method_6032() + (double)1.5F && (double)this.mc.field_1724.method_7261(0.5F) >= (double)0.25F && livingEntity.field_6235 <= (Integer)this.maxHurtTime.get()) {
               cir.setReturnValue(true);
            }
         }
      }

   }

   @Unique
   private boolean oneHitEntity() {
      return this.getTarget() != null && (Boolean)this.ignoreSmartDelayForShulkerBulletAndGhastCharge.get() && (this.getTarget().method_5864() == class_1299.field_6066 || this.getTarget().method_5864() == class_1299.field_6100);
   }

   @Inject(
      method = {"attack"},
      at = {@At("RETURN")}
   )
   private void onAttackEntity(class_1297 target, CallbackInfo ci) {
      this.hitTimer = 0;
   }

   @Inject(
      method = {"entityCheck"},
      at = {@At("RETURN")},
      cancellable = true
   )
   protected void entityCheck(class_1297 entity, CallbackInfoReturnable<Boolean> cir) {
      AntiBotPlus antiBotPlus = (AntiBotPlus)Modules.get().get(AntiBotPlus.class);
      Teams teams = (Teams)Modules.get().get(Teams.class);
      if (antiBotPlus != null && teams != null && entity instanceof class_1657 && cir.getReturnValueZ()) {
         boolean ignore = !antiBotPlus.isBot(entity);
         if (ignore) {
            ignore = !teams.isInYourTeam(entity);
         }

         cir.setReturnValue(ignore);
      }

   }
}
