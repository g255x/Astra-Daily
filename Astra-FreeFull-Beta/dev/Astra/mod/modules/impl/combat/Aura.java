package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.JelloUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.asm.accessors.IEntity;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.settings.enums.SwingSide;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1429;
import net.minecraft.class_1621;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_1835;
import net.minecraft.class_243;
import net.minecraft.class_2824;
import net.minecraft.class_3989;
import net.minecraft.class_4587;

public class Aura extends Module {
   public static Aura INSTANCE;
   public static class_1297 target;
   private final EnumSetting<Page> page;
   private final SliderSetting range;
   private final SliderSetting targetRange;
   private final SliderSetting wallRange;
   private final SliderSetting attackInterval;
   private final EnumSetting<SwingSide> swingMode;
   private final BooleanSetting usingPause;
   private final BooleanSetting useSword;
   private final BooleanSetting useAxe;
   private final BooleanSetting useMace;
   private final BooleanSetting useTrident;
   private final BooleanSetting useOther;
   private final BooleanSetting otherOnlyPlayer;
   private final BooleanSetting Players;
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
   private final BooleanSetting swapEnabled;
   private final BooleanSetting inventorySwap;
   private final SliderSetting durabilityThreshold;
   private final SliderSetting fallSpeed;
   private final Timer attackTimer;
   public class_243 directionVec;

   public Aura() {
      super("Aura", Module.Category.Combat);
      this.page = this.add(new EnumSetting("Page", Aura.Page.General));
      this.range = this.add((new SliderSetting("Range", (double)6.0F, 0.1, (double)7.0F, () -> this.page.getValue() == Aura.Page.General)).setSuffix("m"));
      this.targetRange = this.add((new SliderSetting("TargetRange", (double)8.0F, 0.1, (double)14.0F, () -> this.page.getValue() == Aura.Page.General)).setSuffix("m"));
      this.wallRange = this.add((new SliderSetting("WallRange", (double)6.0F, 0.1, (double)7.0F, () -> this.page.getValue() == Aura.Page.General)).setSuffix("m"));
      this.attackInterval = this.add((new SliderSetting("Delay", (double)200.0F, (double)0.0F, (double)1000.0F, (double)1.0F, () -> this.page.getValue() == Aura.Page.General)).setSuffix("ms"));
      this.swingMode = this.add(new EnumSetting("Swing", SwingSide.All, () -> this.page.getValue() == Aura.Page.General));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.page.getValue() == Aura.Page.General));
      this.useSword = this.add(new BooleanSetting("Sword", true, () -> this.page.getValue() == Aura.Page.Weapons));
      this.useAxe = this.add(new BooleanSetting("Axe", true, () -> this.page.getValue() == Aura.Page.Weapons));
      this.useMace = this.add(new BooleanSetting("Mace", true, () -> this.page.getValue() == Aura.Page.Weapons));
      this.useTrident = this.add(new BooleanSetting("Trident", true, () -> this.page.getValue() == Aura.Page.Weapons));
      this.useOther = this.add(new BooleanSetting("Other", true, () -> this.page.getValue() == Aura.Page.Weapons));
      this.otherOnlyPlayer = this.add(new BooleanSetting("OnlyPlayer", false, () -> this.page.getValue() == Aura.Page.Weapons && this.useOther.getValue()));
      this.Players = this.add(new BooleanSetting("Players", true, () -> this.page.getValue() == Aura.Page.Target));
      this.Mobs = this.add(new BooleanSetting("Mobs", true, () -> this.page.getValue() == Aura.Page.Target));
      this.Animals = this.add(new BooleanSetting("Animals", true, () -> this.page.getValue() == Aura.Page.Target));
      this.Villagers = this.add(new BooleanSetting("Villagers", true, () -> this.page.getValue() == Aura.Page.Target));
      this.Slimes = this.add(new BooleanSetting("Slimes", true, () -> this.page.getValue() == Aura.Page.Target));
      this.targetMode = this.add(new EnumSetting("Filter", Aura.TargetMode.DISTANCE, () -> this.page.getValue() == Aura.Page.Target));
      this.mode = this.add(new EnumSetting("TargetESP", Aura.TargetESP.Fill, () -> this.page.getValue() == Aura.Page.Render));
      this.animationTime = this.add((new SliderSetting("AnimationTime", (double)200.0F, (double)0.0F, (double)2000.0F, (double)1.0F, () -> this.page.getValue() == Aura.Page.Render)).setSuffix("ms"));
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
      this.fov = this.add((new SliderSetting("Fov", (double)20.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.checkFov.getValue() && this.page.getValue() == Aura.Page.Rotate && this.yawStep.isOpen())).setSuffix("°"));
      this.priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.page.getValue() == Aura.Page.Rotate && this.yawStep.isOpen()));
      this.swapEnabled = this.add(new BooleanSetting("Swap", false, () -> this.page.getValue() == Aura.Page.Swap));
      this.inventorySwap = this.add(new BooleanSetting("Inventory", false, () -> this.page.getValue() == Aura.Page.Swap));
      this.durabilityThreshold = this.add((new SliderSetting("Durability", (double)10.0F, (double)0.0F, (double)100.0F, (double)1.0F, () -> this.page.getValue() == Aura.Page.Swap)).setSuffix("%"));
      this.fallSpeed = this.add((new SliderSetting("FallSpeed", (double)40.0F, (double)0.0F, (double)60.0F, 0.1, () -> this.page.getValue() == Aura.Page.Swap)).setSuffix("km/h"));
      this.attackTimer = new Timer();
      this.directionVec = null;
      this.setChinese("杀戮光环");
      INSTANCE = this;
   }

   public static void doRender(class_4587 stack, float partialTicks, class_1297 entity, Color color, Color outlineColor, TargetESP mode) {
      switch (mode.ordinal()) {
         case 0:
            Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), color, outlineColor, false, true);
            break;
         case 1:
            Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), color, outlineColor, true, true);
            break;
         case 2:
            JelloUtil.drawJello(stack, entity, color);
            break;
         case 3:
            Render3DUtil.drawTargetEsp(stack, target, color);
         case 4:
      }

   }

   public void onRender3D(class_4587 matrixStack) {
      if (target != null && this.mode.getValue() != Aura.TargetESP.None) {
         this.doRender(matrixStack, mc.method_60646().method_60637(true), target, this.mode.getValue());
      }

   }

   public void doRender(class_4587 stack, float partialTicks, class_1297 entity, TargetESP mode) {
      float progress = (float)this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue());
      Color currentColor = ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), (double)progress);
      Color currentOutline = ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), (double)progress);
      switch (mode.ordinal()) {
         case 0:
            Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), currentColor, currentOutline, false, true);
            break;
         case 1:
            Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), currentColor, currentOutline, true, true);
            break;
         case 2:
            JelloUtil.drawJello(stack, entity, this.color.getValue());
            break;
         case 3:
            Render3DUtil.drawTargetEsp(stack, target, this.color.getValue());
         case 4:
      }

   }

   public String getInfo() {
      return target == null ? null : target.method_5477().getString();
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (!this.isHoldingAllowedWeapon()) {
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

   @EventListener
   public void onRotate(RotationEvent event) {
      if (target != null && this.rotate.getValue() && this.shouldYawStep()) {
         this.directionVec = this.getAttackVec(target);
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   private boolean isHoldingAllowedWeapon() {
      class_1799 mainHand = mc.field_1724.method_6047();
      class_1792 item = mainHand.method_7909();
      if (item instanceof class_1829) {
         return this.useSword.getValue();
      } else if (item instanceof class_1743) {
         return this.useAxe.getValue();
      } else if (item instanceof class_1835) {
         return this.useTrident.getValue();
      } else {
         return item.getClass().getSimpleName().equals("MaceItem") ? this.useMace.getValue() : this.useOther.getValue();
      }
   }

   private boolean check() {
      if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
         return false;
      } else if ((double)this.attackTimer.getMs() < this.attackInterval.getValue()) {
         return false;
      } else {
         return !this.usingPause.getValue() || !mc.field_1724.method_6115();
      }
   }

   private void doAura() {
      if (this.check()) {
         class_243 hitVec = this.getAttackVec(target);
         if (!this.rotate.getValue() || this.faceVector(hitVec)) {
            this.animation.to = (double)1.0F;
            this.animation.from = (double)1.0F;
            if (this.shouldUseMaceSpoof()) {
               this.doMaceSpoofAttack();
            } else {
               this.normalAttack();
            }

            this.attackTimer.reset();
            if (this.rotate.getValue() && !this.shouldYawStep()) {
               Astra.ROTATION.snapBack();
            }

         }
      }
   }

   private void normalAttack() {
      mc.method_1562().method_52787(class_2824.method_34206(target, mc.field_1724.method_5715()));
      mc.field_1724.method_7350();
      EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
   }

   private int getMaceSlot() {
      return this.inventorySwap.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_49814) : InventoryUtil.findItem(class_1802.field_49814);
   }

   private void doSwap(int slot) {
      if (this.inventorySwap.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private boolean shouldUseMaceSpoof() {
      if (!this.swapEnabled.getValue()) {
         return false;
      } else if (!this.useMace.getValue()) {
         return false;
      } else {
         double fallSpeedKmh = Math.abs(mc.field_1724.method_18798().field_1351) * 3.6;
         if (fallSpeedKmh < this.fallSpeed.getValue()) {
            return false;
         } else {
            int slot = this.getMaceSlot();
            if (slot == -1) {
               return false;
            } else {
               class_1799 stack = mc.field_1724.method_31548().method_5438(slot);
               if (!stack.method_7909().equals(class_1802.field_49814)) {
                  return false;
               } else {
                  int maxDurability = stack.method_7936();
                  if (maxDurability == 0) {
                     return true;
                  } else {
                     int damage = stack.method_7919();
                     float durabilityPercent = (float)(maxDurability - damage) / (float)maxDurability * 100.0F;
                     return (double)durabilityPercent >= this.durabilityThreshold.getValue();
                  }
               }
            }
         }
      }
   }

   private void doMaceSpoofAttack() {
      int oldSlot = mc.field_1724.method_31548().field_7545;
      int maceSlot = this.getMaceSlot();
      if (maceSlot != -1 && maceSlot != oldSlot) {
         this.doSwap(maceSlot);
         mc.method_1562().method_52787(class_2824.method_34206(target, mc.field_1724.method_5715()));
         mc.field_1724.method_7350();
         EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
         if (this.inventorySwap.getValue()) {
            this.doSwap(maceSlot);
            EntityUtil.syncInventory();
         } else {
            this.doSwap(oldSlot);
         }

      } else {
         this.normalAttack();
      }
   }

   private class_243 getAttackVec(class_1297 entity) {
      return MathUtil.getClosestPointToBox(mc.field_1724.method_33571(), entity.method_5829());
   }

   private boolean shouldYawStep() {
      return this.whenElytra.getValue() || !mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() : false;
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
            double distance = mc.field_1724.method_33571().method_1022(hitVec);
            if (!(distance > range) && (mc.field_1724.method_6057(entity) || !(distance > this.wallRange.getValue())) && CombatUtil.isValid(entity)) {
               if (target == null) {
                  target = entity;
                  getDistance = distance;
                  maxHealth = (double)EntityUtil.getHealth(entity);
               } else if (this.targetMode.getValue() == Aura.TargetMode.HEALTH) {
                  double health = (double)EntityUtil.getHealth(entity);
                  if (health < maxHealth) {
                     target = entity;
                     maxHealth = health;
                  }
               } else if (this.targetMode.getValue() == Aura.TargetMode.DISTANCE && distance < getDistance) {
                  target = entity;
                  getDistance = distance;
               }
            }
         }
      }

      return target;
   }

   private boolean isEnemy(class_1297 entity) {
      class_1799 mainHand = mc.field_1724.method_6047();
      class_1792 item = mainHand.method_7909();
      boolean isOther = !(item instanceof class_1829) && !(item instanceof class_1743) && !(item instanceof class_1835) && !item.getClass().getSimpleName().equals("MaceItem");
      if (isOther && this.otherOnlyPlayer.getValue()) {
         return entity instanceof class_1657 && this.Players.getValue();
      } else if (entity instanceof class_1621) {
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
      Weapons,
      Target,
      Render,
      Rotate,
      Swap;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Weapons, Target, Render, Rotate, Swap};
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
}
