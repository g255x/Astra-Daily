package nekiplay.meteorplus.features.modules.combat.killaura;

import java.util.Objects;
import java.util.Set;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EntityTypeListSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.combat.KillAura;
import meteordevelopment.meteorclient.systems.modules.combat.KillAura.AttackItems;
import meteordevelopment.meteorclient.systems.modules.combat.KillAura.ShieldMode;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.combat.killaura.modes.Matrix;
import net.minecraft.class_1299;
import org.spongepowered.asm.mixin.Unique;

public class KillAuraPlus extends Module {
   public final SettingGroup sgGeneral;
   public final Setting<KillAuraPlusModes> mode;
   private final SettingGroup sgTargeting;
   private final SettingGroup sgRotation;
   public final Setting<KillAura.AttackItems> weapon;
   public final Setting<Boolean> autoSwitch;
   public final Setting<KillAura.ShieldMode> shieldMode;
   public final Setting<Set<class_1299<?>>> entities;
   public final Setting<SortPriority> priority;
   public final Setting<Double> range;
   public final Setting<Double> wallsRange;
   public final Setting<Boolean> ignorePassive;
   @Unique
   public final Setting<Boolean> onlyCrits;
   public final Setting<Boolean> ignoreTamed;
   public final Setting<Matrix.Type> rotationType;
   public final Setting<Boolean> speedUpRotationWhenAttacking;
   private KillAuraPlusMode currentMode;

   public KillAuraPlus() {
      super(Categories.Combat, "kill-aura+", "Better killaura.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("KillAura mode.")).defaultValue(KillAuraPlusModes.Matrix)).onModuleActivated((modesSetting) -> this.onModeChanged((KillAuraPlusModes)modesSetting.get()))).onChanged(this::onModeChanged)).build());
      this.sgTargeting = this.settings.createGroup("Targeting");
      this.sgRotation = this.settings.createGroup("Rotation");
      this.weapon = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("weapon")).description("Only attacks an entity when a specified weapon is in your hand.")).defaultValue(AttackItems.All)).build());
      this.autoSwitch = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("auto-switch")).description("Switches to an acceptable weapon when attacking the target.")).defaultValue(false)).build());
      SettingGroup var10001 = this.sgGeneral;
      EnumSetting.Builder var10002 = (EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("shield-mode")).description("Will try and use an axe to break target shields.")).defaultValue(ShieldMode.Break);
      Setting var10003 = this.autoSwitch;
      Objects.requireNonNull(var10003);
      this.shieldMode = var10001.add(((EnumSetting.Builder)var10002.visible(var10003::get)).build());
      this.entities = this.sgTargeting.add(((EntityTypeListSetting.Builder)((EntityTypeListSetting.Builder)(new EntityTypeListSetting.Builder()).name("entities")).description("Entities to attack.")).onlyAttackable().defaultValue(new class_1299[]{class_1299.field_6097}).build());
      this.priority = this.sgTargeting.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("priority")).description("How to filter targets within range.")).defaultValue(SortPriority.ClosestAngle)).build());
      this.range = this.sgTargeting.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("range")).description("The maximum range the entity can be to attack it.")).defaultValue((double)4.5F).min((double)0.0F).sliderMax((double)6.0F).build());
      this.wallsRange = this.sgTargeting.add(((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("walls-range")).description("The maximum range the entity can be attacked through walls.")).defaultValue((double)3.5F).min((double)0.0F).sliderMax((double)6.0F).build());
      this.ignorePassive = this.sgTargeting.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("ignore-passive")).description("Will only attack sometimes passive mobs if they are targeting you.")).defaultValue(true)).build());
      this.onlyCrits = this.sgTargeting.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("only-crits")).description("Attack enemy only if this attack crit after jump.")).defaultValue(true)).build());
      this.ignoreTamed = this.sgTargeting.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("ignore-tamed")).description("Will avoid attacking mobs you tamed.")).defaultValue(false)).build());
      this.rotationType = this.sgRotation.add(((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("rotation-type")).defaultValue(Matrix.Type.Smooth)).build());
      this.speedUpRotationWhenAttacking = this.sgRotation.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Speed-up-the-rotation-when-attacking")).defaultValue(false)).build());
   }

   private void onModeChanged(KillAuraPlusModes mode) {
      switch (mode) {
         case Matrix:
            this.currentMode = new Matrix();
         default:
      }
   }

   @EventHandler
   private void onTickPre(TickEvent.Pre event) {
      this.currentMode.onTickPre(event);
   }

   @EventHandler
   private void onTickPost(TickEvent.Post event) {
      this.currentMode.onTickPost(event);
   }

   @EventHandler
   private void onSendPacket(PacketEvent.Send event) {
      this.currentMode.onSendPacket(event);
   }

   public void onDeactivate() {
      this.currentMode.onDeactivate();
   }

   public void onActivate() {
      this.currentMode.onActivate();
   }

   public String getInfoString() {
      return this.currentMode.getInfoString();
   }
}
