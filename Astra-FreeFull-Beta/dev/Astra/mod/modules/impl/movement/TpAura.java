package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.path.PathUtils;
import dev.Astra.api.utils.path.TPUtils;
import dev.Astra.api.utils.path.Vec3;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2828;

public class TpAura extends Module {
   public static TpAura INSTANCE;
   private final EnumSetting<TpMode> tpMode;
   private final BooleanSetting keyMode;
   private final BindSetting key;
   private final SliderSetting range;
   private final SliderSetting delay;
   private final SliderSetting moveDistance;
   private final SliderSetting up;
   private final EnumSetting<TPUtils.TeleportType> tpType;
   private final BooleanSetting autoDisable;
   private final SliderSetting stayTime;
   private final Timer timer;
   private final Timer stayTimer;
   private class_243 originalPos;
   private boolean isAtTarget;

   public TpAura() {
      super("TargetTP", Module.Category.Movement);
      this.tpMode = this.add(new EnumSetting("Mode", TpAura.TpMode.Normal));
      this.keyMode = this.add(new BooleanSetting("KeyMode", false));
      BooleanSetting var10006 = this.keyMode;
      Objects.requireNonNull(var10006);
      this.key = this.add(new BindSetting("TeleportKey", -1, var10006::getValue));
      this.range = this.add(new SliderSetting("Range", (double)114.0F, (double)1.0F, (double)1024.0F, (double)1.0F));
      this.delay = this.add(new SliderSetting("Delay", (double)0.0F, (double)0.0F, (double)1000.0F, (double)1.0F));
      this.moveDistance = this.add(new SliderSetting("MoveDistance", (double)10.0F, (double)1.0F, (double)50.0F, (double)1.0F));
      this.up = this.add(new SliderSetting("UP", (double)0.0F, (double)0.0F, (double)50.0F, (double)1.0F));
      this.tpType = this.add(new EnumSetting("TPType", TPUtils.TeleportType.New));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", true));
      this.stayTime = this.add((new SliderSetting("StayTime", (double)500.0F, (double)50.0F, (double)3000.0F, (double)50.0F)).setSuffix("ms"));
      this.timer = new Timer();
      this.stayTimer = new Timer();
      this.originalPos = null;
      this.isAtTarget = false;
      this.setChinese("目标传送");
      INSTANCE = this;
   }

   public String getInfo() {
      class_1657 target = CombatUtil.getClosestEnemy(this.range.getValue());
      return target == null ? null : target.method_5477().getString();
   }

   public void onDisable() {
      this.originalPos = null;
      this.isAtTarget = false;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!nullCheck() && (!this.keyMode.getValue() || this.key.isPressed())) {
         if (this.isAtTarget) {
            if (this.stayTimer.passed((long)this.stayTime.getValue())) {
               this.teleportBack();
               this.isAtTarget = false;
               this.originalPos = null;
               if (this.autoDisable.getValue()) {
                  this.disable();
               }
            }
         } else {
            class_1657 target = CombatUtil.getClosestEnemy(this.range.getValue());
            if (target != null && this.timer.passed((long)this.delay.getValue())) {
               this.timer.reset();
               class_243 targetPos = target.method_19538().method_1031((double)0.0F, this.up.getValue(), (double)0.0F);
               if (this.tpMode.getValue() == TpAura.TpMode.FakeTP) {
                  this.originalPos = mc.field_1724.method_19538();
                  this.teleportTo(targetPos);
                  this.isAtTarget = true;
                  this.stayTimer.reset();
               } else {
                  this.teleportTo(targetPos);
                  if (this.autoDisable.getValue()) {
                     this.disable();
                  }
               }
            }
         }
      }

   }

   private void teleportTo(class_243 targetPos) {
      switch ((TPUtils.TeleportType)this.tpType.getValue()) {
         case New -> this.teleportNew(targetPos);
         case Legacy -> this.teleportLegacy(targetPos);
      }

   }

   private void teleportBack() {
      if (this.originalPos != null) {
         switch ((TPUtils.TeleportType)this.tpType.getValue()) {
            case New -> this.teleportNew(this.originalPos);
            case Legacy -> this.teleportLegacy(this.originalPos);
         }
      }

   }

   private void teleportNew(class_243 targetPos) {
      class_243 playerPos = mc.field_1724.method_19538();
      double distance = playerPos.method_1022(targetPos);
      int packetsRequired = (int)Math.ceil(distance / this.moveDistance.getValue()) - 1;

      for(int i = 0; i < packetsRequired; ++i) {
         mc.method_1562().method_52787(new class_2828.class_5911(true));
      }

      mc.method_1562().method_52787(new class_2828.class_2829(targetPos.field_1352, targetPos.field_1351, targetPos.field_1350, true));
      mc.field_1724.method_33574(targetPos);
   }

   private void teleportLegacy(class_243 targetPos) {
      ArrayList<Vec3> path = PathUtils.computePath(targetPos);
      if (!path.isEmpty()) {
         path.removeFirst();

         for(Vec3 vec3 : path) {
            mc.method_1562().method_52787(new class_2828.class_2829(vec3.x(), vec3.y(), vec3.z(), false));
         }

         mc.field_1724.method_5814(targetPos.field_1352, targetPos.field_1351, targetPos.field_1350);
      }

   }

   public static enum TpMode {
      Normal,
      FakeTP;

      // $FF: synthetic method
      private static TpMode[] $values() {
         return new TpMode[]{Normal, FakeTP};
      }
   }
}
