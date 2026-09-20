package nekiplay.meteorplus.features.modules.combat;

import java.util.Objects;
import java.util.Set;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EntityTypeListSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.combat.criticals.CriticalsPlus;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1657;
import net.minecraft.class_1934;
import net.minecraft.class_6025;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Unique;

public class TriggerBot extends Module {
   private final SettingGroup sgGeneral;
   private final SettingGroup sgTiming;
   private final Setting<Set<class_1299<?>>> entities;
   private final Setting<Boolean> babies;
   private final Setting<Boolean> smartDelay;
   private final Setting<Integer> hitDelay;
   private final Setting<Boolean> randomDelayEnabled;
   private final Setting<Integer> randomDelayMax;
   private final Setting<Boolean> onlyCrits;
   @Unique
   private final Setting<Boolean> ignoreOnlyCritsOnLevitation;
   private int hitDelayTimer;

   public TriggerBot() {
      super(Categories.Combat, "Trigger-bot", "Attacks specified entities around you.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.sgTiming = this.settings.createGroup("Timing");
      this.entities = this.sgGeneral.add(((EntityTypeListSetting.Builder)((EntityTypeListSetting.Builder)(new EntityTypeListSetting.Builder()).name("entities")).description("Entities to attack.")).onlyAttackable().build());
      this.babies = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("babies")).description("Whether or not to attack baby variants of the entity.")).defaultValue(true)).build());
      this.smartDelay = this.sgTiming.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("smart-delay")).description("Uses the vanilla cooldown to attack entities.")).defaultValue(true)).build());
      this.hitDelay = this.sgTiming.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("hit-delay")).description("How fast you hit the entity in ticks.")).defaultValue(0)).min(0).sliderMax(60).visible(() -> !(Boolean)this.smartDelay.get())).build());
      this.randomDelayEnabled = this.sgTiming.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("random-delay-enabled")).description("Adds a random delay between hits to attempt to bypass anti-cheats.")).defaultValue(false)).visible(() -> !(Boolean)this.smartDelay.get())).build());
      this.randomDelayMax = this.sgTiming.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("random-delay-max")).description("The maximum value for random delay.")).defaultValue(4)).min(0).sliderMax(20).visible(() -> (Boolean)this.randomDelayEnabled.get() && !(Boolean)this.smartDelay.get())).build());
      this.onlyCrits = this.sgTiming.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("only-crits")).description("Attack enemy only if this attack crit after jump.")).defaultValue(true)).build());
      this.ignoreOnlyCritsOnLevitation = this.sgTiming.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("ignore-only-crits-on-levetation")).defaultValue(true)).visible(() -> (Boolean)this.onlyCrits.get())).build());
   }

   public void onDeactivate() {
      this.hitDelayTimer = 0;
   }

   private boolean entityCheck(class_1297 entity) {
      if (!entity.equals(this.mc.field_1724) && !entity.equals(this.mc.method_1560())) {
         if ((!(entity instanceof class_1309) || !((class_1309)entity).method_29504()) && entity.method_5805()) {
            if (!((Set)this.entities.get()).contains(entity.method_5864())) {
               return false;
            } else {
               if (entity instanceof class_6025) {
                  class_6025 tameable = (class_6025)entity;
                  if (tameable.method_35057().method_5667() != null && tameable.method_35057().method_5667().equals(this.mc.field_1724.method_5667())) {
                     return false;
                  }
               }

               if (entity instanceof class_1657) {
                  class_1657 player = (class_1657)entity;
                  if (player.method_68878()) {
                     return false;
                  }

                  if (!Friends.get().shouldAttack(player)) {
                     return false;
                  }

                  AntiBotPlus antiBotPlus = (AntiBotPlus)Modules.get().get(AntiBotPlus.class);
                  Teams teams = (Teams)Modules.get().get(Teams.class);
                  if (antiBotPlus != null && antiBotPlus.isBot((class_1309)player)) {
                     return false;
                  }

                  if (teams != null && teams.isInYourTeam((class_1309)player)) {
                     return false;
                  }
               }

               return !(entity instanceof class_1429) || (Boolean)this.babies.get() || !((class_1429)entity).method_6109();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean delayCheck() {
      if ((Boolean)this.onlyCrits.get() && !CriticalsPlus.allowCrit() && CriticalsPlus.needCrit(this.mc.field_1692)) {
         if ((Boolean)this.ignoreOnlyCritsOnLevitation.get() && !((class_746)Objects.requireNonNull(this.mc.field_1724)).method_6059(class_1294.field_5902)) {
            return false;
         }

         if (!(Boolean)this.ignoreOnlyCritsOnLevitation.get()) {
            return false;
         }
      }

      if ((Boolean)this.smartDelay.get()) {
         return this.mc.field_1724.method_7261(0.5F) >= 1.0F;
      } else if (this.hitDelayTimer > 0) {
         --this.hitDelayTimer;
         return false;
      } else {
         this.hitDelayTimer = (Integer)this.hitDelay.get();
         if ((Boolean)this.randomDelayEnabled.get()) {
            this.hitDelayTimer = (int)((long)this.hitDelayTimer + Math.round(Math.random() * (double)(Integer)this.randomDelayMax.get()));
         }

         return true;
      }
   }

   @EventHandler
   private void onTick(Render3DEvent event) {
      if (this.mc.field_1724.method_5805() && PlayerUtils.getGameMode() != class_1934.field_9219) {
         if (this.mc.field_1692 != null) {
            if (this.delayCheck() && this.entityCheck(this.mc.field_1692)) {
               this.hitEntity(this.mc.field_1692);
            }

         }
      }
   }

   private void hitEntity(class_1297 target) {
      this.mc.field_1761.method_2918(this.mc.field_1724, target);
      this.mc.field_1724.method_6104(class_1268.field_5808);
   }
}
