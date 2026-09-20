package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.Render3DEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.entity.PlayerEntityPredict;
import dev.Astra.api.utils.math.AnimateUtil;
import dev.Astra.api.utils.math.ExplosionUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.player.AirPlace;
import dev.Astra.mod.modules.settings.enums.SwingSide;
import dev.Astra.mod.modules.settings.enums.Timing;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2846;
import net.minecraft.class_2885;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class AutoAnchor extends Module {
   public static AutoAnchor INSTANCE;
   static class_243 placeVec3d;
   static class_243 curVec3d;
   public final EnumSetting<Page> page;
   public final SliderSetting range;
   public final SliderSetting targetRange;
   private final BooleanSetting preferCrystal;
   private final BooleanSetting thread;
   private final BooleanSetting inventorySwap;
   private final BooleanSetting breakCrystal;
   private final BooleanSetting usingPause;
   private final EnumSetting<SwingSide> swingMode;
   private final EnumSetting<Timing> timing;
   private final SliderSetting placeDelay;
   private final SliderSetting fillDelay;
   private final SliderSetting breakDelay;
   private final SliderSetting spamDelay;
   private final SliderSetting updateDelay;
   private final BooleanSetting spam;
   private final BooleanSetting mineSpam;
   private final BooleanSetting spamPlace;
   private final BooleanSetting inSpam;
   public final SliderSetting minDamage;
   public final SliderSetting breakMin;
   public final SliderSetting headDamage;
   private final BooleanSetting noSuicide;
   private final BooleanSetting smart;
   private final BooleanSetting terrainIgnore;
   private final SliderSetting minPrefer;
   private final SliderSetting maxSelfDamage;
   private final SliderSetting selfPredict;
   private final SliderSetting predictTicks;
   private final SliderSetting simulation;
   private final SliderSetting maxMotionY;
   private final BooleanSetting step;
   private final BooleanSetting doubleStep;
   private final BooleanSetting jump;
   private final BooleanSetting inBlockPause;
   private final BooleanSetting rotate;
   private final BooleanSetting yawStep;
   private final SliderSetting steps;
   private final BooleanSetting checkFov;
   private final SliderSetting fov;
   private final SliderSetting priority;
   private final BooleanSetting assist;
   private final BooleanSetting obsidian;
   private final BooleanSetting checkMine;
   private final SliderSetting assistRange;
   private final SliderSetting assistDamage;
   private final SliderSetting assistDelay;
   private final EnumSetting<Aura.TargetESP> mode;
   private final ColorSetting color;
   private final ColorSetting outlineColor;
   private final BooleanSetting render;
   private final BooleanSetting shrink;
   private final ColorSetting box;
   private final ColorSetting fill;
   private final SliderSetting sliderSpeed;
   private final SliderSetting startFadeTime;
   private final SliderSetting fadeSpeed;
   final ArrayList<class_2338> chargeList;
   private final Timer delayTimer;
   private final Timer calcTimer;
   private final Timer noPosTimer;
   private final Timer assistTimer;
   public class_243 directionVec;
   public class_1657 displayTarget;
   public class_2338 currentPos;
   public class_2338 tempPos;
   double fade;
   class_2338 assistPos;

   public AutoAnchor() {
      super("AutoAnchor", Module.Category.Combat);
      this.page = this.add(new EnumSetting("Page", AutoAnchor.Page.General));
      this.range = this.add((new SliderSetting("Range", (double)5.0F, (double)0.0F, (double)6.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.General)).setSuffix("m"));
      this.targetRange = this.add((new SliderSetting("TargetRange", (double)8.0F, 0.1, (double)12.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.General)).setSuffix("m"));
      this.preferCrystal = this.add(new BooleanSetting("PreferCrystal", false, () -> this.page.getValue() == AutoAnchor.Page.General));
      this.thread = this.add(new BooleanSetting("Thread", false, () -> this.page.getValue() == AutoAnchor.Page.General));
      this.inventorySwap = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.getValue() == AutoAnchor.Page.General));
      this.breakCrystal = this.add(new BooleanSetting("BreakCrystal", true, () -> this.page.getValue() == AutoAnchor.Page.General));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.page.getValue() == AutoAnchor.Page.General));
      this.swingMode = this.add(new EnumSetting("Swing", SwingSide.All, () -> this.page.getValue() == AutoAnchor.Page.General));
      this.timing = this.add(new EnumSetting("Timing", Timing.All, () -> this.page.getValue() == AutoAnchor.Page.General));
      this.placeDelay = this.add((new SliderSetting("PlaceDelay", (double)100.0F, (double)0.0F, (double)500.0F, (double)1.0F, () -> this.page.getValue() == AutoAnchor.Page.General)).setSuffix("ms"));
      this.fillDelay = this.add((new SliderSetting("FillDelay", (double)100.0F, (double)0.0F, (double)500.0F, (double)1.0F, () -> this.page.getValue() == AutoAnchor.Page.General)).setSuffix("ms"));
      this.breakDelay = this.add((new SliderSetting("BreakDelay", (double)100.0F, (double)0.0F, (double)500.0F, (double)1.0F, () -> this.page.getValue() == AutoAnchor.Page.General)).setSuffix("ms"));
      this.spamDelay = this.add((new SliderSetting("SpamDelay", (double)200.0F, (double)0.0F, (double)1000.0F, (double)1.0F, () -> this.page.getValue() == AutoAnchor.Page.General)).setSuffix("ms"));
      this.updateDelay = this.add((new SliderSetting("UpdateDelay", (double)200.0F, (double)0.0F, (double)1000.0F, (double)1.0F, () -> this.page.getValue() == AutoAnchor.Page.General)).setSuffix("ms"));
      this.spam = this.add((new BooleanSetting("Spam", true, () -> this.page.getValue() == AutoAnchor.Page.General)).setParent());
      this.mineSpam = this.add(new BooleanSetting("OnlyMining", true, () -> this.page.getValue() == AutoAnchor.Page.General && this.spam.isOpen()));
      this.spamPlace = this.add((new BooleanSetting("Fast", true, () -> this.page.getValue() == AutoAnchor.Page.General)).setParent());
      this.inSpam = this.add(new BooleanSetting("WhenSpamming", true, () -> this.page.getValue() == AutoAnchor.Page.General && this.spamPlace.isOpen()));
      this.minDamage = this.add((new SliderSetting("Min", (double)4.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.Interact)).setSuffix("hp"));
      this.breakMin = this.add((new SliderSetting("ExplosionMin", (double)4.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.Interact)).setSuffix("hp"));
      this.headDamage = this.add((new SliderSetting("ForceHead", (double)7.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.Interact)).setSuffix("hp"));
      this.noSuicide = this.add(new BooleanSetting("NoSuicide", true, () -> this.page.getValue() == AutoAnchor.Page.Interact));
      this.smart = this.add(new BooleanSetting("Smart", true, () -> this.page.getValue() == AutoAnchor.Page.Interact));
      this.terrainIgnore = this.add(new BooleanSetting("TerrainIgnore", true, () -> this.page.getValue() == AutoAnchor.Page.Interact));
      this.minPrefer = this.add((new SliderSetting("Prefer", (double)7.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.Interact)).setSuffix("hp"));
      this.maxSelfDamage = this.add((new SliderSetting("MaxSelf", (double)8.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.Interact)).setSuffix("hp"));
      this.selfPredict = this.add((new SliderSetting("SelfPredict", 4, 0, 10, () -> this.page.getValue() == AutoAnchor.Page.Predict)).setSuffix("ticks"));
      this.predictTicks = this.add((new SliderSetting("Predict", 4, 0, 10, () -> this.page.getValue() == AutoAnchor.Page.Predict)).setSuffix("ticks"));
      this.simulation = this.add(new SliderSetting("Simulation", (double)5.0F, (double)0.0F, (double)20.0F, (double)1.0F, () -> this.page.getValue() == AutoAnchor.Page.Predict));
      this.maxMotionY = this.add(new SliderSetting("MaxMotionY", 0.34, (double)0.0F, (double)2.0F, 0.01, () -> this.page.getValue() == AutoAnchor.Page.Predict));
      this.step = this.add(new BooleanSetting("Step", false, () -> this.page.getValue() == AutoAnchor.Page.Predict));
      this.doubleStep = this.add(new BooleanSetting("DoubleStep", false, () -> this.page.getValue() == AutoAnchor.Page.Predict));
      this.jump = this.add(new BooleanSetting("Jump", false, () -> this.page.getValue() == AutoAnchor.Page.Predict));
      this.inBlockPause = this.add(new BooleanSetting("InBlockPause", true, () -> this.page.getValue() == AutoAnchor.Page.Predict));
      this.rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.getValue() == AutoAnchor.Page.Rotate));
      this.yawStep = this.add(new BooleanSetting("YawStep", false, () -> this.page.getValue() == AutoAnchor.Page.Rotate));
      this.steps = this.add(new SliderSetting("Steps", (double)1.0F, (double)0.0F, (double)1.0F, 0.01, () -> this.page.getValue() == AutoAnchor.Page.Rotate));
      this.checkFov = this.add(new BooleanSetting("OnlyLooking", false, () -> this.page.getValue() == AutoAnchor.Page.Rotate));
      this.fov = this.add((new SliderSetting("Fov", (double)10.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.Rotate)).setSuffix("°"));
      this.priority = this.add(new SliderSetting("Priority", 30, 0, 100, () -> this.page.getValue() == AutoAnchor.Page.Rotate));
      this.assist = this.add(new BooleanSetting("Assist", true, () -> this.page.getValue() == AutoAnchor.Page.Assist));
      this.obsidian = this.add(new BooleanSetting("Obsidian", true, () -> this.page.getValue() == AutoAnchor.Page.Assist));
      this.checkMine = this.add(new BooleanSetting("DetectMining", true, () -> this.page.getValue() == AutoAnchor.Page.Assist));
      this.assistRange = this.add((new SliderSetting("AssistRange", (double)10.0F, (double)0.0F, (double)20.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.Assist)).setSuffix("m"));
      this.assistDamage = this.add((new SliderSetting("AssistDamage", (double)6.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoAnchor.Page.Assist)).setSuffix("hp"));
      this.assistDelay = this.add((new SliderSetting("AssistDelay", 0.1, (double)0.0F, (double)1.0F, 0.01, () -> this.page.getValue() == AutoAnchor.Page.Assist)).setSuffix("s"));
      this.mode = this.add(new EnumSetting("TargetESP", Aura.TargetESP.None, () -> this.page.getValue() == AutoAnchor.Page.Render));
      this.color = this.add(new ColorSetting("TargetColor", new Color(419430399, true), () -> this.page.getValue() == AutoAnchor.Page.Render));
      this.outlineColor = this.add(new ColorSetting("TargetOutlineColor", new Color(419430399, true), () -> this.page.getValue() == AutoAnchor.Page.Render));
      this.render = this.add(new BooleanSetting("Render", true, () -> this.page.getValue() == AutoAnchor.Page.Render));
      this.shrink = this.add(new BooleanSetting("Shrink", false, () -> this.page.getValue() == AutoAnchor.Page.Render && this.render.getValue()));
      this.box = this.add((new ColorSetting("Box", new Color(-1049473294, true), () -> this.page.getValue() == AutoAnchor.Page.Render && this.render.getValue())).injectBoolean(true));
      this.fill = this.add((new ColorSetting("Fill", new Color(1282823407, true), () -> this.page.getValue() == AutoAnchor.Page.Render && this.render.getValue())).injectBoolean(true));
      this.sliderSpeed = this.add(new SliderSetting("SliderSpeed", 0.92, (double)0.0F, (double)1.0F, 0.01, () -> this.page.getValue() == AutoAnchor.Page.Render && this.render.getValue()));
      this.startFadeTime = this.add((new SliderSetting("StartFade", 0.1, (double)0.0F, (double)2.0F, 0.01, () -> this.page.getValue() == AutoAnchor.Page.Render && this.render.getValue())).setSuffix("s"));
      this.fadeSpeed = this.add(new SliderSetting("FadeSpeed", (double)1.0F, 0.01, (double)1.0F, 0.01, () -> this.page.getValue() == AutoAnchor.Page.Render && this.render.getValue()));
      this.chargeList = new ArrayList();
      this.delayTimer = new Timer();
      this.calcTimer = new Timer();
      this.noPosTimer = new Timer();
      this.assistTimer = new Timer();
      this.directionVec = null;
      this.fade = (double)0.0F;
      this.setChinese("重生锚光环");
      INSTANCE = this;
      Astra.EVENT_BUS.subscribe(new AnchorRender());
   }

   public void onEnable() {
      placeVec3d = null;
      curVec3d = null;
   }

   public static boolean canSee(class_243 from, class_243 to) {
      class_3965 result = mc.field_1687.method_17742(new class_3959(from, to, class_3960.field_17558, class_242.field_1348, mc.field_1724));
      return result == null || result.method_17783() == class_240.field_1333;
   }

   public String getInfo() {
      return this.displayTarget != null && this.currentPos != null ? this.displayTarget.method_5477().getString() : null;
   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.displayTarget != null && this.currentPos != null) {
         Aura.doRender(matrixStack, mc.method_60646().method_60637(true), this.displayTarget, this.color.getValue(), this.outlineColor.getValue(), this.mode.getValue());
      }

   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.currentPos != null && this.rotate.getValue() && this.yawStep.getValue() && this.directionVec != null) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   public void onDisable() {
      this.tempPos = null;
      this.currentPos = null;
      placeVec3d = null;
      curVec3d = null;
   }

   public void onThread() {
      if (!this.isOff() && !nullCheck()) {
         if (mc.field_1687 != null && mc.field_1687.method_27983() == class_1937.field_25180) {
            this.currentPos = null;
         } else {
            if (this.thread.getValue()) {
               if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
                  this.currentPos = null;
                  return;
               }

               if (AutoCrystal.INSTANCE.isOn() && AutoCrystal.INSTANCE.crystalPos != null && this.preferCrystal.getValue()) {
                  this.currentPos = null;
                  return;
               }

               int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_23152) : InventoryUtil.findBlock(class_2246.field_23152);
               int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10171) : InventoryUtil.findBlock(class_2246.field_10171);
               int unBlock = this.inventorySwap.getValue() ? anchor : InventoryUtil.findUnBlock();
               if (anchor == -1) {
                  this.currentPos = null;
                  return;
               }

               if (glowstone == -1) {
                  this.currentPos = null;
                  return;
               }

               if (unBlock == -1) {
                  this.currentPos = null;
                  return;
               }

               if (mc.field_1724.method_5715()) {
                  this.currentPos = null;
                  return;
               }

               if (this.usingPause.getValue() && mc.field_1724.method_6115()) {
                  this.currentPos = null;
                  return;
               }

               this.calc();
            }

         }
      }
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (mc.field_1687 != null && mc.field_1687.method_27983() == class_1937.field_25180) {
            this.currentPos = null;
         } else if ((!this.timing.is(Timing.Pre) || !event.isPost()) && (!this.timing.is(Timing.Post) || !event.isPre())) {
            int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_23152) : InventoryUtil.findBlock(class_2246.field_23152);
            int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10171) : InventoryUtil.findBlock(class_2246.field_10171);
            int unBlock = this.inventorySwap.getValue() ? anchor : InventoryUtil.findUnBlock();
            int old = mc.field_1724.method_31548().field_7545;
            if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
               this.currentPos = null;
            } else if (AutoCrystal.INSTANCE.isOn() && AutoCrystal.INSTANCE.crystalPos != null && this.preferCrystal.getValue()) {
               this.currentPos = null;
            } else if (anchor == -1) {
               this.currentPos = null;
            } else if (glowstone == -1) {
               this.currentPos = null;
            } else if (unBlock == -1) {
               this.currentPos = null;
            } else if (mc.field_1724.method_5715()) {
               this.currentPos = null;
            } else if (this.usingPause.getValue() && mc.field_1724.method_6115()) {
               this.currentPos = null;
            } else if (!this.inventorySwap.getValue() || EntityUtil.inInventory()) {
               if (this.assist.getValue()) {
                  this.onAssist();
               }

               if (!this.thread.getValue()) {
                  this.calc();
               }

               class_2338 pos;
               if ((pos = this.currentPos) != null) {
                  if (this.breakCrystal.getValue()) {
                     CombatUtil.attackCrystal(new class_2338(pos), this.rotate.getValue(), false);
                  }

                  boolean shouldSpam = this.spam.getValue() && (!this.mineSpam.getValue() || Astra.BREAK.isMining(pos));
                  if (shouldSpam) {
                     if (!this.delayTimer.passed((long)this.spamDelay.getValueFloat())) {
                        return;
                     }

                     this.delayTimer.reset();
                     if (BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue())) {
                        this.placeBlock(pos, this.rotate.getValue(), anchor);
                     }

                     if (!this.chargeList.contains(pos)) {
                        this.delayTimer.reset();
                        this.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue(), glowstone);
                        this.chargeList.add(pos);
                     }

                     this.chargeList.remove(pos);
                     this.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue(), unBlock);
                     if (this.spamPlace.getValue() && this.inSpam.getValue()) {
                        if (this.yawStep.getValue() && this.checkFov.getValue()) {
                           class_2350 side = BlockUtil.getClickSide(pos);
                           class_243 directionVec = new class_243((double)pos.method_10263() + (double)0.5F + (double)side.method_10163().method_10263() * (double)0.5F, (double)pos.method_10264() + (double)0.5F + (double)side.method_10163().method_10264() * (double)0.5F, (double)pos.method_10260() + (double)0.5F + (double)side.method_10163().method_10260() * (double)0.5F);
                           if (Astra.ROTATION.inFov(directionVec, this.fov.getValueFloat())) {
                              CombatUtil.modifyPos = pos;
                              CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
                              this.placeBlock(pos, this.rotate.getValue(), anchor);
                              CombatUtil.modifyPos = null;
                           }
                        } else {
                           CombatUtil.modifyPos = pos;
                           CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
                           this.placeBlock(pos, this.rotate.getValue(), anchor);
                           CombatUtil.modifyPos = null;
                        }
                     }
                  } else if (BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue())) {
                     if (!this.delayTimer.passed((long)this.placeDelay.getValueFloat())) {
                        return;
                     }

                     this.delayTimer.reset();
                     this.placeBlock(pos, this.rotate.getValue(), anchor);
                  } else if (BlockUtil.getBlock(pos) == class_2246.field_23152) {
                     if (!this.chargeList.contains(pos)) {
                        if (!this.delayTimer.passed((long)this.fillDelay.getValueFloat())) {
                           return;
                        }

                        this.delayTimer.reset();
                        this.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue(), glowstone);
                        this.chargeList.add(pos);
                     } else {
                        if (!this.delayTimer.passed((long)this.breakDelay.getValueFloat())) {
                           return;
                        }

                        this.delayTimer.reset();
                        this.chargeList.remove(pos);
                        this.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue(), unBlock);
                        if (this.spamPlace.getValue()) {
                           if (this.yawStep.getValue() && this.checkFov.getValue()) {
                              class_2350 side = BlockUtil.getClickSide(pos);
                              class_243 directionVec = new class_243((double)pos.method_10263() + (double)0.5F + (double)side.method_10163().method_10263() * (double)0.5F, (double)pos.method_10264() + (double)0.5F + (double)side.method_10163().method_10264() * (double)0.5F, (double)pos.method_10260() + (double)0.5F + (double)side.method_10163().method_10260() * (double)0.5F);
                              if (Astra.ROTATION.inFov(directionVec, this.fov.getValueFloat())) {
                                 CombatUtil.modifyPos = pos;
                                 CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
                                 this.placeBlock(pos, this.rotate.getValue(), anchor);
                                 CombatUtil.modifyPos = null;
                              }
                           } else {
                              CombatUtil.modifyPos = pos;
                              CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
                              this.placeBlock(pos, this.rotate.getValue(), anchor);
                              CombatUtil.modifyPos = null;
                           }
                        }
                     }
                  }

                  if (!this.inventorySwap.getValue()) {
                     this.doSwap(old);
                  }
               }

            }
         }
      }
   }

   private void calc() {
      if (!nullCheck()) {
         if (mc.field_1687 != null && mc.field_1687.method_27983() == class_1937.field_25180) {
            this.currentPos = null;
         } else {
            if (this.calcTimer.passed((long)this.updateDelay.getValueFloat())) {
               this.calcTimer.reset();
               PlayerEntityPredict selfPredict = new PlayerEntityPredict(mc.field_1724, this.maxMotionY.getValue(), this.selfPredict.getValueInt(), this.simulation.getValueInt(), this.step.getValue(), this.doubleStep.getValue(), this.jump.getValue(), this.inBlockPause.getValue());
               this.tempPos = null;
               double placeDamage = this.minDamage.getValue();
               double breakDamage = this.breakMin.getValue();
               boolean anchorFound = false;
               List<class_1657> enemies = CombatUtil.getEnemies(this.targetRange.getValue());
               ArrayList<PlayerEntityPredict> list = new ArrayList();

               for(class_1657 player : enemies) {
                  list.add(new PlayerEntityPredict(player, this.maxMotionY.getValue(), this.predictTicks.getValueInt(), this.simulation.getValueInt(), this.step.getValue(), this.doubleStep.getValue(), this.jump.getValue(), this.inBlockPause.getValue()));
               }

               for(PlayerEntityPredict pap : list) {
                  class_2338 pos = EntityUtil.getEntityPos(pap.player, true).method_10086(2);
                  double selfDamage;
                  if ((BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue()) || BlockUtil.getBlock(pos) == class_2246.field_23152 && BlockUtil.getClickSideStrict(pos) != null) && !((selfDamage = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue()) && (!this.noSuicide.getValue() || !(selfDamage > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067())))) {
                     double damage = this.getAnchorDamage(pos, pap.player, pap.predict);
                     if (damage > (double)this.headDamage.getValueFloat() && (!this.smart.getValue() || !(selfDamage > damage))) {
                        this.displayTarget = pap.player;
                        this.tempPos = pos;
                        break;
                     }
                  }
               }

               if (this.tempPos == null) {
                  for(class_2338 pos : BlockUtil.getSphere(this.range.getValueFloat() + 1.0F, mc.field_1724.method_33571())) {
                     for(PlayerEntityPredict pap : list) {
                        if (BlockUtil.getBlock(pos) != class_2246.field_23152) {
                           if (!anchorFound && BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue())) {
                              CombatUtil.modifyPos = pos;
                              CombatUtil.modifyBlockState = class_2246.field_10540.method_9564();
                              boolean skip = BlockUtil.getClickSideStrict(pos) == null;
                              CombatUtil.modifyPos = null;
                              double damage;
                              double selfDamage2;
                              if (!skip && (damage = this.getAnchorDamage(pos, pap.player, pap.predict)) >= placeDamage && !((selfDamage2 = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue()) && (!this.noSuicide.getValue() || !(selfDamage2 > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067()))) && (!this.smart.getValue() || !(selfDamage2 > damage))) {
                                 this.displayTarget = pap.player;
                                 placeDamage = damage;
                                 this.tempPos = pos;
                              }
                           }
                        } else {
                           double damage2 = this.getAnchorDamage(pos, pap.player, pap.predict);
                           if (BlockUtil.getClickSideStrict(pos) != null && damage2 >= breakDamage) {
                              if (damage2 >= this.minPrefer.getValue()) {
                                 anchorFound = true;
                              }

                              double selfDamage;
                              if ((anchorFound || !(damage2 < placeDamage)) && !((selfDamage = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue()) && (!this.noSuicide.getValue() || !(selfDamage > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067()))) && (!this.smart.getValue() || !(selfDamage > damage2))) {
                                 this.displayTarget = pap.player;
                                 breakDamage = damage2;
                                 this.tempPos = pos;
                              }
                           }
                        }
                     }
                  }
               }
            }

            this.currentPos = this.tempPos;
         }
      }
   }

   public double getAnchorDamage(class_2338 anchorPos, class_1657 target, class_1657 predict) {
      if (this.terrainIgnore.getValue()) {
         CombatUtil.terrainIgnore = true;
      }

      double damage = (double)ExplosionUtil.anchorDamage(anchorPos, target, predict);
      CombatUtil.terrainIgnore = false;
      return damage;
   }

   public void placeBlock(class_2338 pos, boolean rotate, int slot) {
      if (BlockUtil.allowAirPlace()) {
         this.airPlace(pos, rotate, slot);
      } else {
         class_2350 side = BlockUtil.getPlaceSide(pos);
         if (side != null) {
            this.clickBlock(pos.method_10093(side), side.method_10153(), rotate, slot);
         }
      }
   }

   public void clickBlock(class_2338 pos, class_2350 side, boolean rotate, int slot) {
      if (pos != null) {
         class_243 directionVec = new class_243((double)pos.method_10263() + (double)0.5F + (double)side.method_10163().method_10263() * (double)0.5F, (double)pos.method_10264() + (double)0.5F + (double)side.method_10163().method_10264() * (double)0.5F, (double)pos.method_10260() + (double)0.5F + (double)side.method_10163().method_10260() * (double)0.5F);
         if (!rotate || this.faceVector(directionVec)) {
            this.doSwap(slot);
            EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
            class_3965 result = new class_3965(directionVec, side, pos, false);
            Module.sendSequencedPacket((id) -> new class_2885(class_1268.field_5808, result, id));
            if (this.inventorySwap.getValue()) {
               this.doSwap(slot);
            }

            if (rotate && !this.yawStep.getValue()) {
               Astra.ROTATION.snapBack();
            }

         }
      }
   }

   public void airPlace(class_2338 pos, boolean rotate, int slot) {
      if (pos != null) {
         class_2350 side = BlockUtil.getClickSide(pos);
         class_243 directionVec = new class_243((double)pos.method_10263() + (double)0.5F + (double)side.method_10163().method_10263() * (double)0.5F, (double)pos.method_10264() + (double)0.5F + (double)side.method_10163().method_10264() * (double)0.5F, (double)pos.method_10260() + (double)0.5F + (double)side.method_10163().method_10260() * (double)0.5F);
         if (!rotate || this.faceVector(directionVec)) {
            this.doSwap(slot);
            boolean bypass = AirPlace.INSTANCE.grimBypass.getValue();
            if (bypass) {
               mc.method_1562().method_52787(new class_2846(class_2847.field_12969, new class_2338(0, 0, 0), class_2350.field_11033));
            }

            EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
            class_3965 result = new class_3965(directionVec, side, pos, false);
            Module.sendSequencedPacket((id) -> new class_2885(bypass ? class_1268.field_5810 : class_1268.field_5808, result, id));
            if (bypass) {
               mc.method_1562().method_52787(new class_2846(class_2847.field_12969, new class_2338(0, 0, 0), class_2350.field_11033));
            }

            if (this.inventorySwap.getValue()) {
               this.doSwap(slot);
            }

            if (rotate && !this.yawStep.getValue()) {
               Astra.ROTATION.snapBack();
            }

         }
      }
   }

   private void doSwap(int slot) {
      if (this.inventorySwap.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   public boolean faceVector(class_243 directionVec) {
      if (!this.yawStep.getValue()) {
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

   public void onAssist() {
      if (mc.field_1687 == null || mc.field_1687.method_27983() != class_1937.field_25180) {
         this.assistPos = null;
         int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_23152) : InventoryUtil.findBlock(class_2246.field_23152);
         int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10171) : InventoryUtil.findBlock(class_2246.field_10171);
         int old = mc.field_1724.method_31548().field_7545;
         if (anchor != -1) {
            if (this.obsidian.getValue()) {
               anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
               if (anchor == -1) {
                  return;
               }
            }

            if (glowstone != -1) {
               if (!mc.field_1724.method_5715()) {
                  if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
                     if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                        if (this.assistTimer.passed((long)(this.assistDelay.getValueFloat() * 1000.0F))) {
                           this.assistTimer.reset();
                           ArrayList<PlayerEntityPredict> list = new ArrayList();

                           for(class_1657 player : CombatUtil.getEnemies(this.assistRange.getValue())) {
                              list.add(new PlayerEntityPredict(player, this.maxMotionY.getValue(), this.predictTicks.getValueInt(), this.simulation.getValueInt(), this.step.getValue(), this.doubleStep.getValue(), this.jump.getValue(), this.inBlockPause.getValue()));
                           }

                           double bestDamage = this.assistDamage.getValue();

                           for(PlayerEntityPredict pap : list) {
                              class_2338 pos = EntityUtil.getEntityPos(pap.player, true).method_10086(2);
                              if (mc.field_1687.method_8320(pos).method_26204() == class_2246.field_23152) {
                                 return;
                              }

                              double damage;
                              if (BlockUtil.clientCanPlace(pos, false) && (damage = this.getAnchorDamage(pos, pap.player, pap.predict)) >= bestDamage) {
                                 bestDamage = damage;
                                 this.assistPos = pos;
                              }

                              for(class_2350 i : class_2350.values()) {
                                 double damage2;
                                 if (i != class_2350.field_11036 && i != class_2350.field_11033 && BlockUtil.clientCanPlace(pos.method_10093(i), false) && (damage2 = this.getAnchorDamage(pos.method_10093(i), pap.player, pap.predict)) >= bestDamage) {
                                    bestDamage = damage2;
                                    this.assistPos = pos.method_10093(i);
                                 }
                              }
                           }

                           class_2338 placePos;
                           if (this.assistPos != null && BlockUtil.getPlaceSide(this.assistPos, this.range.getValue()) == null && (placePos = this.getHelper(this.assistPos)) != null) {
                              this.doSwap(anchor);
                              BlockUtil.placeBlock(placePos, this.rotate.getValue());
                              if (this.inventorySwap.getValue()) {
                                 this.doSwap(anchor);
                              } else {
                                 this.doSwap(old);
                              }
                           }

                        }
                     }
                  }
               }
            }
         }
      }
   }

   public class_2338 getHelper(class_2338 pos) {
      for(class_2350 i : class_2350.values()) {
         if ((!this.checkMine.getValue() || !Astra.BREAK.isMining(pos.method_10093(i))) && BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) && BlockUtil.canPlace(pos.method_10093(i))) {
            return pos.method_10093(i);
         }
      }

      return null;
   }

   public static enum Page {
      General,
      Interact,
      Predict,
      Rotate,
      Assist,
      Render;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Interact, Predict, Rotate, Assist, Render};
      }
   }

   public class AnchorRender {
      @EventListener
      public void onRender3D(Render3DEvent event) {
         class_2338 currentPos = AutoAnchor.INSTANCE.currentPos;
         if (currentPos != null) {
            AutoAnchor.this.noPosTimer.reset();
            AutoAnchor.placeVec3d = currentPos.method_46558();
         }

         if (AutoAnchor.placeVec3d != null) {
            AutoAnchor.this.fade = AutoAnchor.this.fadeSpeed.getValue() >= (double)1.0F ? (AutoAnchor.this.noPosTimer.passed((long)(AutoAnchor.this.startFadeTime.getValue() * (double)1000.0F)) ? (double)0.0F : (double)0.5F) : AnimateUtil.animate(AutoAnchor.this.fade, AutoAnchor.this.noPosTimer.passed((long)(AutoAnchor.this.startFadeTime.getValue() * (double)1000.0F)) ? (double)0.0F : (double)0.5F, AutoAnchor.this.fadeSpeed.getValue() / (double)10.0F);
            if (AutoAnchor.this.fade == (double)0.0F) {
               AutoAnchor.curVec3d = null;
            } else {
               AutoAnchor.curVec3d = AutoAnchor.curVec3d != null && !(AutoAnchor.this.sliderSpeed.getValue() >= (double)1.0F) ? new class_243(AnimateUtil.animate(AutoAnchor.curVec3d.field_1352, AutoAnchor.placeVec3d.field_1352, AutoAnchor.this.sliderSpeed.getValue() / (double)10.0F), AnimateUtil.animate(AutoAnchor.curVec3d.field_1351, AutoAnchor.placeVec3d.field_1351, AutoAnchor.this.sliderSpeed.getValue() / (double)10.0F), AnimateUtil.animate(AutoAnchor.curVec3d.field_1350, AutoAnchor.placeVec3d.field_1350, AutoAnchor.this.sliderSpeed.getValue() / (double)10.0F)) : AutoAnchor.placeVec3d;
               if (AutoAnchor.this.render.getValue()) {
                  class_238 cbox = new class_238(AutoAnchor.curVec3d, AutoAnchor.curVec3d);
                  cbox = AutoAnchor.this.shrink.getValue() ? cbox.method_1014(AutoAnchor.this.fade) : cbox.method_1014((double)0.5F);
                  if (AutoAnchor.this.fill.booleanValue) {
                     event.drawFill(cbox, ColorUtil.injectAlpha(AutoAnchor.this.fill.getValue(), (int)((double)AutoAnchor.this.fill.getValue().getAlpha() * AutoAnchor.this.fade * (double)2.0F)));
                  }

                  if (AutoAnchor.this.box.booleanValue) {
                     event.drawBox(cbox, ColorUtil.injectAlpha(AutoAnchor.this.box.getValue(), (int)((double)AutoAnchor.this.box.getValue().getAlpha() * AutoAnchor.this.fade * (double)2.0F)));
                  }
               }

            }
         }
      }
   }
}
