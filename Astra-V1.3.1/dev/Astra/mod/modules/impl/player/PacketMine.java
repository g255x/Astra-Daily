package dev.Astra.mod.modules.impl.player;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClickBlockEvent;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.math.FadeUtils;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.asm.accessors.IPlayerMoveC2SPacket;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.combat.AutoAnchor;
import dev.Astra.mod.modules.impl.combat.AutoCrystal;
import dev.Astra.mod.modules.impl.combat.CevBreaker;
import dev.Astra.mod.modules.impl.combat.Criticals;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.impl.movement.Velocity;
import dev.Astra.mod.modules.settings.enums.SwingSide;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1531;
import net.minecraft.class_1542;
import net.minecraft.class_1739;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2189;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2868;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4969;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import net.minecraft.class_2846.class_2847;

public class PacketMine extends Module {
   public static PacketMine INSTANCE;
   public static class_2338 secondPos;
   public static double progress = (double)0.0F;
   private final FadeUtils animationTime = new FadeUtils(1000L);
   private final FadeUtils secondAnim = new FadeUtils(1000L);
   private final DecimalFormat df = new DecimalFormat("0.0");
   private final EnumSetting<Page> page;
   private final SliderSetting stopDelay;
   private final SliderSetting startDelay;
   private final SliderSetting damage;
   private final SliderSetting maxBreak;
   public final BooleanSetting noCollide;
   private final BooleanSetting fastBypass;
   private final SliderSetting packetDelay;
   private final BooleanSetting instant;
   private final BooleanSetting wait;
   private final BooleanSetting mineAir;
   private final BooleanSetting hotBar;
   private final BooleanSetting doubleBreak;
   public final EnumSetting<ToolSwitchMode> toolSwitchMode;
   private final SliderSetting switchDamage;
   private final BooleanSetting setAir;
   private final BooleanSetting swing;
   private final BooleanSetting endSwing;
   public final SliderSetting range;
   private final EnumSetting<SwingSide> swingMode;
   private final BooleanSetting unbreakableCancel;
   private final BooleanSetting switchReset;
   private final BooleanSetting preferWeb;
   private final BooleanSetting preferHead;
   private final BooleanSetting farCancel;
   private final BooleanSetting onlyGround;
   private final BooleanSetting checkWeb;
   private final BooleanSetting checkGround;
   private final BooleanSetting smart;
   private final BooleanSetting usingPause;
   private final BooleanSetting allowOffhand;
   private final BooleanSetting bypassGround;
   private final SliderSetting bypassTime;
   private final BindSetting pause;
   private final BooleanSetting rotate;
   private final BooleanSetting endRotate;
   private final SliderSetting syncTime;
   private final BooleanSetting yawStep;
   private final BooleanSetting whenElytra;
   private final SliderSetting steps;
   private final BooleanSetting checkFov;
   private final SliderSetting fov;
   private final SliderSetting priority;
   public final BooleanSetting crystal;
   private final BooleanSetting onlyHeadBomber;
   private final BooleanSetting waitPlace;
   private final BooleanSetting spamPlace;
   private final BooleanSetting afterBreak;
   private final BooleanSetting checkDamage;
   private final SliderSetting crystalDamage;
   public final BindSetting obsidian;
   private final BindSetting enderChest;
   private final BooleanSetting placeRotate;
   private final BooleanSetting inventory;
   private final SliderSetting placeDelay;
   private final BooleanSetting checkDouble;
   private final EnumSetting<Animation> animation;
   private final EnumSetting<Easing> ease;
   private final EnumSetting<Easing> fadeEase;
   private final SliderSetting expandLine;
   private final ColorSetting startColor;
   private final ColorSetting startOutlineColor;
   private final ColorSetting endColor;
   private final ColorSetting endOutlineColor;
   private final ColorSetting doubleColor;
   private final ColorSetting doubleOutlineColor;
   private final BooleanSetting text;
   private final BooleanSetting box;
   private final BooleanSetting outline;
   private final Timer mineTimer;
   private final Timer sync;
   private final Timer secondTimer;
   private final Timer delayTimer;
   private final Timer placeTimer;
   private final Timer startTime;
   private final Timer switchBackTimer;
   private final Timer packetTimer;
   private final Timer secondSwitchBackTimer;
   public static boolean ghost = false;
   public static boolean complete = false;
   int lastSlot;
   class_243 directionVec;
   Runnable switchBack;
   class_2338 breakPos;
   boolean startPacket;
   int breakNumber;
   double breakFinalTime;
   double secondFinalTime;
   boolean sendGroundPacket;
   boolean toolSwitched;
   int originalSlot;
   int bestToolSlot;
   boolean secondToolSwitched;
   int secondOriginalSlot;
   int secondBestToolSlot;
   boolean usingPauseDelayed;
   boolean secondUsingPauseDelayed;
   List<class_2338> fastBypassPositions;
   private boolean isRotating;
   private class_243 lastRotationTarget;
   private final Timer rotationResetTimer;

   public void mine(class_2338 pos) {
      if (!nullCheck()) {
         if (mc.field_1724.method_7337()) {
            mc.field_1761.method_2910(pos, BlockUtil.getClickSide(pos));
         } else if (this.isOff()) {
            mc.field_1761.method_2910(pos, BlockUtil.getClickSide(pos));
         } else if (!pos.equals(this.breakPos)) {
            if (!unbreakable(pos)) {
               if (this.breakPos == null || !this.preferWeb.getValue() || BlockUtil.getBlock(this.breakPos) != class_2246.field_10343) {
                  if (this.breakPos == null || !this.preferHead.getValue() || !mc.field_1724.method_20448() || !EntityUtil.getPlayerPos(true).method_10084().equals(this.breakPos)) {
                     if (BlockUtil.getClickSideStrict(pos) != null) {
                        if (!((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(pos.method_46558())) > this.range.getValue())) {
                           this.switchBackToOriginal(false);
                           this.switchBackToOriginal(true);
                           this.breakPos = pos;
                           this.breakNumber = 0;
                           this.startPacket = false;
                           ghost = false;
                           complete = false;
                           this.mineTimer.reset();
                           this.animationTime.reset();
                           this.toolSwitched = false;
                           this.secondToolSwitched = false;
                           this.originalSlot = mc.field_1724.method_31548().field_7545;
                           this.secondOriginalSlot = -1;
                           if (this.doubleBreak.getValue()) {
                              this.initializeDoubleBreak();
                           }

                        }
                     }
                  }
               }
            }
         }
      }
   }

   public PacketMine() {
      super("PacketMine", Module.Category.Player);
      this.page = this.add(new EnumSetting("Page", PacketMine.Page.General));
      this.stopDelay = this.add(new SliderSetting("StopDelay", (double)50.0F, (double)0.0F, (double)500.0F, (double)1.0F, () -> this.page.is(PacketMine.Page.General)));
      this.startDelay = this.add(new SliderSetting("StartDelay", (double)200.0F, (double)0.0F, (double)500.0F, (double)1.0F, () -> this.page.is(PacketMine.Page.General)));
      this.damage = this.add(new SliderSetting("Damage", (double)0.7F, (double)0.0F, (double)2.0F, 0.01, () -> this.page.is(PacketMine.Page.General)));
      this.maxBreak = this.add(new SliderSetting("MaxBreak", (double)3.0F, (double)0.0F, (double)20.0F, (double)1.0F, () -> this.page.is(PacketMine.Page.General)));
      this.noCollide = this.add(new BooleanSetting("NoCollide", true, () -> this.page.is(PacketMine.Page.General)));
      this.fastBypass = this.add(new BooleanSetting("FastBypass", false, () -> this.page.is(PacketMine.Page.General)));
      this.packetDelay = this.add(new SliderSetting("PacketDelay", (double)10.0F, (double)0.0F, (double)100.0F, (double)1.0F, () -> this.page.is(PacketMine.Page.General) && this.fastBypass.getValue()));
      this.instant = this.add(new BooleanSetting("Instant", false, () -> this.page.is(PacketMine.Page.General)));
      this.wait = this.add(new BooleanSetting("Wait", true, () -> !this.instant.getValue() && this.page.is(PacketMine.Page.General)));
      this.mineAir = this.add(new BooleanSetting("MineAir", true, () -> this.wait.getValue() && !this.instant.getValue() && this.page.is(PacketMine.Page.General)));
      this.hotBar = this.add(new BooleanSetting("HotbarSwap", false, () -> this.page.is(PacketMine.Page.General)));
      this.doubleBreak = this.add(new BooleanSetting("DoubleBreak", true, () -> this.page.is(PacketMine.Page.General)));
      this.toolSwitchMode = this.add(new EnumSetting("ToolSwitch", PacketMine.ToolSwitchMode.None, () -> this.page.is(PacketMine.Page.General)));
      this.switchDamage = this.add(new SliderSetting("SwitchDamage", (double)0.5F, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(PacketMine.Page.General) && !this.toolSwitchMode.is(PacketMine.ToolSwitchMode.None)));
      this.setAir = this.add(new BooleanSetting("SetAir", false, () -> this.page.is(PacketMine.Page.General)));
      this.swing = this.add(new BooleanSetting("Swing", true, () -> this.page.is(PacketMine.Page.General)));
      this.endSwing = this.add(new BooleanSetting("EndSwing", false, () -> this.page.is(PacketMine.Page.General)));
      this.range = this.add(new SliderSetting("Range", (double)6.0F, (double)3.0F, (double)10.0F, 0.1, () -> this.page.is(PacketMine.Page.General)));
      this.swingMode = this.add(new EnumSetting("SwingSide", SwingSide.All, () -> this.page.is(PacketMine.Page.General)));
      this.unbreakableCancel = this.add(new BooleanSetting("UnbreakableCancel", true, () -> this.page.is(PacketMine.Page.Check)));
      this.switchReset = this.add(new BooleanSetting("SwitchReset", false, () -> this.page.is(PacketMine.Page.Check)));
      this.preferWeb = this.add(new BooleanSetting("PreferWeb", true, () -> this.page.is(PacketMine.Page.Check)));
      this.preferHead = this.add(new BooleanSetting("PreferHead", true, () -> this.page.is(PacketMine.Page.Check)));
      this.farCancel = this.add(new BooleanSetting("FarCancel", false, () -> this.page.is(PacketMine.Page.Check)));
      this.onlyGround = this.add(new BooleanSetting("OnlyGround", true, () -> this.page.is(PacketMine.Page.Check)));
      this.checkWeb = this.add(new BooleanSetting("CheckWeb", true, () -> this.page.is(PacketMine.Page.Check)));
      this.checkGround = this.add(new BooleanSetting("CheckGround", true, () -> this.page.is(PacketMine.Page.Check)));
      this.smart = this.add(new BooleanSetting("Smart", true, () -> this.page.is(PacketMine.Page.Check) && this.checkGround.getValue()));
      this.usingPause = this.add((new BooleanSetting("UsingPause", false, () -> this.page.is(PacketMine.Page.Check))).setParent());
      this.allowOffhand = this.add(new BooleanSetting("AllowOffhand", true, () -> this.page.is(PacketMine.Page.Check) && this.usingPause.isOpen()));
      this.bypassGround = this.add(new BooleanSetting("BypassGround", true, () -> this.page.is(PacketMine.Page.Check)));
      this.bypassTime = this.add(new SliderSetting("BypassTime", 400, 0, 2000, () -> this.bypassGround.getValue() && this.page.is(PacketMine.Page.Check)));
      this.pause = this.add(new BindSetting("Pause", -1, () -> this.page.is(PacketMine.Page.Check)));
      this.rotate = this.add(new BooleanSetting("StartRotate", true, () -> this.page.is(PacketMine.Page.Rotation)));
      this.endRotate = this.add(new BooleanSetting("EndRotate", false, () -> this.page.is(PacketMine.Page.Rotation)));
      this.syncTime = this.add(new SliderSetting("Sync", 300, 0, 1000, () -> this.page.is(PacketMine.Page.Rotation)));
      this.yawStep = this.add((new BooleanSetting("YawStep", false, () -> this.page.is(PacketMine.Page.Rotation))).setParent());
      this.whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.page.is(PacketMine.Page.Rotation) && this.yawStep.isOpen()));
      this.steps = this.add(new SliderSetting("Steps", 0.05, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(PacketMine.Page.Rotation) && this.yawStep.isOpen()));
      this.checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.page.is(PacketMine.Page.Rotation) && this.yawStep.isOpen()));
      this.fov = this.add(new SliderSetting("Fov", (double)20.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.page.is(PacketMine.Page.Rotation) && this.yawStep.isOpen()));
      this.priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.page.is(PacketMine.Page.Rotation) && this.yawStep.isOpen()));
      this.crystal = this.add((new BooleanSetting("Crystal", false, () -> this.page.is(PacketMine.Page.Place))).setParent());
      this.onlyHeadBomber = this.add(new BooleanSetting("OnlyCev", true, () -> this.page.is(PacketMine.Page.Place) && this.crystal.isOpen()));
      this.waitPlace = this.add(new BooleanSetting("WaitPlace", true, () -> this.page.is(PacketMine.Page.Place) && this.crystal.isOpen()));
      this.spamPlace = this.add(new BooleanSetting("SpamPlace", false, () -> this.page.is(PacketMine.Page.Place) && this.crystal.isOpen()));
      this.afterBreak = this.add(new BooleanSetting("AfterBreak", true, () -> this.page.is(PacketMine.Page.Place) && this.crystal.isOpen()));
      this.checkDamage = this.add(new BooleanSetting("DetectProgress", true, () -> this.page.is(PacketMine.Page.Place) && this.crystal.isOpen()));
      this.crystalDamage = this.add(new SliderSetting("Progress", (double)0.9F, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(PacketMine.Page.Place) && this.crystal.isOpen() && this.checkDamage.getValue()));
      this.obsidian = this.add(new BindSetting("Obsidian", -1, () -> this.page.is(PacketMine.Page.Place)));
      this.enderChest = this.add(new BindSetting("EnderChest", -1, () -> this.page.is(PacketMine.Page.Place)));
      this.placeRotate = this.add(new BooleanSetting("PlaceRotate", true, () -> this.page.is(PacketMine.Page.Place)));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(PacketMine.Page.Place)));
      this.placeDelay = this.add(new SliderSetting("PlaceDelay", 100, 0, 1000, () -> this.page.is(PacketMine.Page.Place)));
      this.checkDouble = this.add(new BooleanSetting("CheckDouble", false, () -> this.page.is(PacketMine.Page.Render)));
      this.animation = this.add(new EnumSetting("Animation", PacketMine.Animation.Up, () -> this.page.is(PacketMine.Page.Render)));
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut, () -> this.page.is(PacketMine.Page.Render)));
      this.fadeEase = this.add(new EnumSetting("FadeEase", Easing.CubicInOut, () -> this.page.is(PacketMine.Page.Render)));
      this.expandLine = this.add(new SliderSetting("ExpandLine", (double)0.0F, (double)0.0F, (double)1.0F, () -> this.page.is(PacketMine.Page.Render)));
      this.startColor = this.add(new ColorSetting("StartFill", new Color(255, 255, 255, 100), () -> this.page.is(PacketMine.Page.Render)));
      this.startOutlineColor = this.add(new ColorSetting("StartOutline", new Color(255, 255, 255, 100), () -> this.page.is(PacketMine.Page.Render)));
      this.endColor = this.add(new ColorSetting("EndFill", new Color(255, 255, 255, 100), () -> this.page.is(PacketMine.Page.Render)));
      this.endOutlineColor = this.add(new ColorSetting("EndOutline", new Color(255, 255, 255, 100), () -> this.page.is(PacketMine.Page.Render)));
      this.doubleColor = this.add(new ColorSetting("DoubleFill", new Color(88, 94, 255, 100), () -> this.doubleBreak.getValue() && this.page.is(PacketMine.Page.Render)));
      this.doubleOutlineColor = this.add(new ColorSetting("DoubleOutline", new Color(88, 94, 255, 100), () -> this.doubleBreak.getValue() && this.page.is(PacketMine.Page.Render)));
      this.text = this.add(new BooleanSetting("Text", true, () -> this.page.is(PacketMine.Page.Render)));
      this.box = this.add(new BooleanSetting("Box", true, () -> this.page.is(PacketMine.Page.Render)));
      this.outline = this.add(new BooleanSetting("Outline", true, () -> this.page.is(PacketMine.Page.Render)));
      this.mineTimer = new Timer();
      this.sync = new Timer();
      this.secondTimer = new Timer();
      this.delayTimer = new Timer();
      this.placeTimer = new Timer();
      this.startTime = new Timer();
      this.switchBackTimer = new Timer();
      this.packetTimer = new Timer();
      this.secondSwitchBackTimer = new Timer();
      this.lastSlot = -1;
      this.directionVec = null;
      this.startPacket = false;
      this.breakNumber = 0;
      this.sendGroundPacket = false;
      this.toolSwitched = false;
      this.originalSlot = 0;
      this.bestToolSlot = 0;
      this.secondToolSwitched = false;
      this.secondOriginalSlot = 0;
      this.secondBestToolSlot = 0;
      this.usingPauseDelayed = false;
      this.secondUsingPauseDelayed = false;
      this.fastBypassPositions = new ArrayList();
      this.isRotating = false;
      this.lastRotationTarget = null;
      this.rotationResetTimer = new Timer();
      this.setChinese("发包挖掘");
      INSTANCE = this;
   }

   public static class_2338 getBreakPos() {
      return INSTANCE.isOn() ? INSTANCE.breakPos : null;
   }

   public String getInfo() {
      return progress >= (double)1.0F ? "Done" : this.df.format(progress * (double)100.0F) + "%";
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.rotate.getValue() && this.shouldYawStep() && this.directionVec != null && !this.sync.passedMs(this.syncTime.getValue())) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   public void onLogin() {
      this.resetMining();
   }

   public void onDisable() {
      this.resetMining();
      this.switchBackToOriginal(false);
      this.switchBackToOriginal(true);
   }

   private void resetMining() {
      this.startPacket = false;
      ghost = false;
      complete = false;
      this.breakPos = null;
      secondPos = null;
      this.toolSwitched = false;
      this.secondToolSwitched = false;
      this.usingPauseDelayed = false;
      this.secondUsingPauseDelayed = false;
      this.fastBypassPositions.clear();
      this.isRotating = false;
      this.lastRotationTarget = null;
      this.switchBackToOriginal(false);
      this.switchBackToOriginal(true);
      this.originalSlot = -1;
      this.secondOriginalSlot = -1;
   }

   private void switchBackToOriginal(boolean isSecond) {
      if (!this.toolSwitchMode.is(PacketMine.ToolSwitchMode.None)) {
         boolean wasSwitched = isSecond ? this.secondToolSwitched : this.toolSwitched;
         if (wasSwitched) {
            int original = isSecond ? this.secondOriginalSlot : this.originalSlot;
            if (original >= 0 && original <= 8) {
               switch (((ToolSwitchMode)this.toolSwitchMode.getValue()).ordinal()) {
                  case 1:
                     int switchedSlot = isSecond ? this.secondBestToolSlot : this.bestToolSlot;
                     int currentSlot = mc.field_1724.method_31548().field_7545;
                     if (currentSlot == switchedSlot) {
                        InventoryUtil.switchToSlot(original);
                     }
                     break;
                  case 2:
                     mc.method_1562().method_52787(new class_2868(original));
               }

               if (isSecond) {
                  this.secondToolSwitched = false;
               } else {
                  this.toolSwitched = false;
               }

            } else {
               if (isSecond) {
                  this.secondToolSwitched = false;
               } else {
                  this.toolSwitched = false;
               }

            }
         }
      }
   }

   private void handleToolSwitching(boolean isSecond) {
      if (!this.toolSwitchMode.is(PacketMine.ToolSwitchMode.None)) {
         class_2338 targetPos = isSecond ? secondPos : this.breakPos;
         if (targetPos == null) {
            this.switchBackToOriginal(isSecond);
         } else if (!this.isAir(targetPos) && !unbreakable(targetPos)) {
            if ((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(targetPos.method_46558())) > this.range.getValue() + (double)1.0F) {
               this.switchBackToOriginal(isSecond);
            } else if (this.startPacket) {
               boolean wasSwitched = isSecond ? this.secondToolSwitched : this.toolSwitched;
               if (!wasSwitched) {
                  boolean isPausing = this.usingPause.getValue() && mc.field_1724.method_6115() && (!this.allowOffhand.getValue() || mc.field_1724.method_6058() == class_1268.field_5808);
                  if (isPausing) {
                     if (isSecond) {
                        this.secondUsingPauseDelayed = true;
                     } else {
                        this.usingPauseDelayed = true;
                     }

                  } else {
                     this.performToolSwitch(isSecond);
                  }
               }
            }
         } else {
            this.switchBackToOriginal(isSecond);
         }
      }
   }

   private void performToolSwitch(boolean isSecond) {
      class_2338 targetPos = isSecond ? secondPos : this.breakPos;
      int bestSlot = this.getTool(targetPos);
      if (bestSlot != -1 && bestSlot != mc.field_1724.method_31548().field_7545) {
         double finalTime = isSecond ? this.secondFinalTime : this.breakFinalTime;
         if (!(finalTime <= (double)0.0F)) {
            Timer progressTimer = isSecond ? this.secondTimer : this.mineTimer;
            double currentProgress = Math.min((double)progressTimer.getMs() / finalTime, (double)1.0F);
            double threshold = Math.min(this.switchDamage.getValue(), (double)1.0F);
            if (currentProgress >= threshold) {
               if (isSecond) {
                  this.secondOriginalSlot = mc.field_1724.method_31548().field_7545;
                  this.secondBestToolSlot = bestSlot;
               } else {
                  this.originalSlot = mc.field_1724.method_31548().field_7545;
                  this.bestToolSlot = bestSlot;
               }

               switch (((ToolSwitchMode)this.toolSwitchMode.getValue()).ordinal()) {
                  case 1 -> InventoryUtil.switchToSlot(bestSlot);
                  case 2 -> mc.method_1562().method_52787(new class_2868(bestSlot));
               }

               if (isSecond) {
                  this.secondToolSwitched = true;
               } else {
                  this.toolSwitched = true;
               }
            }

         }
      }
   }

   private void initializeDoubleBreak() {
      if (secondPos == null || this.isAir(secondPos)) {
         int slot = this.getTool(this.breakPos);
         if (slot == -1) {
            slot = mc.field_1724.method_31548().field_7545;
         }

         this.secondFinalTime = this.getBreakTime(this.breakPos, slot);
         this.secondAnim.reset();
         this.secondAnim.setLength((long)this.secondFinalTime);
         this.secondTimer.reset();
         secondPos = this.breakPos;
      }

   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (this.breakPos != null) {
            int slot = this.getTool(this.breakPos);
            if (slot == -1) {
               slot = mc.field_1724.method_31548().field_7545;
            }

            this.breakFinalTime = this.getBreakTime(this.breakPos, slot);
         }

         if (secondPos != null) {
            int secondSlot = this.getTool(secondPos);
            if (secondSlot == -1) {
               secondSlot = mc.field_1724.method_31548().field_7545;
            }

            this.secondFinalTime = this.getBreakTime(secondPos, secondSlot);
         }

         this.handleToolSwitching(false);
         this.handleToolSwitching(true);
         if (this.usingPauseDelayed && (!mc.field_1724.method_6115() || this.allowOffhand.getValue() && mc.field_1724.method_6058() != class_1268.field_5808)) {
            this.usingPauseDelayed = false;
            this.performToolSwitch(false);
         }

         if (this.secondUsingPauseDelayed && (!mc.field_1724.method_6115() || this.allowOffhand.getValue() && mc.field_1724.method_6058() != class_1268.field_5808)) {
            this.secondUsingPauseDelayed = false;
            this.performToolSwitch(true);
         }

         if (this.breakPos != null && mc.field_1687.method_22347(this.breakPos)) {
            complete = true;
            this.switchBackToOriginal(false);
         }

         if (secondPos != null) {
            if (!this.isAir(secondPos) && !unbreakable(secondPos)) {
               double time = this.getBreakTime(secondPos, mc.field_1724.method_31548().field_7545);
               if (this.secondTimer.passedMs(time * 1.2)) {
                  this.switchBackToOriginal(true);
                  secondPos = null;
               }
            } else {
               this.switchBackToOriginal(true);
               secondPos = null;
            }
         }

         if (this.switchBack != null && event.isPre()) {
            this.switchBack.run();
            this.switchBack = null;
         }

         if (mc.field_1724.method_29504()) {
            this.switchBackToOriginal(false);
            this.switchBackToOriginal(true);
            secondPos = null;
         }

         if (mc.field_1724.method_7337()) {
            this.resetMining();
         } else if (this.breakPos == null) {
            this.breakNumber = 0;
            this.resetMining();
         } else {
            progress = (double)this.mineTimer.getMs() / this.breakFinalTime;
            if (this.isAir(this.breakPos)) {
               this.breakNumber = 0;
            }

            if ((!((double)this.breakNumber > this.maxBreak.getValue() - (double)1.0F) || !(this.maxBreak.getValue() > (double)0.0F) || complete) && (this.wait.getValue() || !this.isAir(this.breakPos) || this.instant.getValue())) {
               if (unbreakable(this.breakPos)) {
                  if (this.unbreakableCancel.getValue()) {
                     this.switchBackToOriginal(false);
                     this.resetMining();
                  }

                  this.breakNumber = 0;
               } else if ((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(this.breakPos.method_46558())) > this.range.getValue()) {
                  if (this.farCancel.getValue()) {
                     this.switchBackToOriginal(false);
                     this.resetMining();
                  }

               } else if (!this.usingPause.getValue() || !mc.field_1724.method_6115() || this.allowOffhand.getValue() && mc.field_1724.method_6058() != class_1268.field_5808) {
                  if (this.pause.isPressed()) {
                     this.switchBackToOriginal(false);
                  } else if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                     if (!this.breakPos.equals(AutoAnchor.INSTANCE.currentPos) || !(BlockUtil.getBlock(getBreakPos()) instanceof class_4969)) {
                        if (this.hotBar.getValue() || EntityUtil.inInventory()) {
                           if (this.isAir(this.breakPos)) {
                              if (this.shouldCrystal()) {
                                 for(class_2350 facing : class_2350.values()) {
                                    CombatUtil.attackCrystal(this.breakPos.method_10093(facing), this.placeRotate.getValue(), true);
                                 }
                              }

                              if (this.placeTimer.passedMs(this.placeDelay.getValue()) && BlockUtil.canPlace(this.breakPos) && mc.field_1755 == null) {
                                 if (this.enderChest.isPressed()) {
                                    int eChest = this.findBlock(class_2246.field_10443);
                                    if (eChest != -1) {
                                       int oldSlot = mc.field_1724.method_31548().field_7545;
                                       this.doSwap(eChest, eChest);
                                       BlockUtil.placeBlock(this.breakPos, this.placeRotate.getValue(), true);
                                       this.doSwap(oldSlot, eChest);
                                       this.placeTimer.reset();
                                    }
                                 } else {
                                    int obsidian;
                                    if (this.obsidian.isPressed() && (obsidian = this.findBlock(class_2246.field_10540)) != -1) {
                                       int hasCrystal = 0;
                                       if (this.shouldCrystal()) {
                                          for(class_1297 entity : BlockUtil.getEntities(new class_238(this.breakPos.method_10084()))) {
                                             if (entity instanceof class_1511) {
                                                hasCrystal = 1;
                                                break;
                                             }
                                          }
                                       }

                                       if (hasCrystal == 0 || this.spamPlace.getValue()) {
                                          int oldSlot = mc.field_1724.method_31548().field_7545;
                                          this.doSwap(obsidian, obsidian);
                                          BlockUtil.placeBlock(this.breakPos, this.placeRotate.getValue(), true);
                                          this.doSwap(oldSlot, obsidian);
                                          this.placeTimer.reset();
                                       }
                                    }
                                 }
                              }

                              this.breakNumber = 0;
                           } else if (this.canPlaceCrystal(this.breakPos.method_10084()) && this.shouldCrystal()) {
                              if (this.placeTimer.passedMs(this.placeDelay.getValue())) {
                                 if (this.checkDamage.getValue()) {
                                    if ((double)this.mineTimer.getMs() / this.breakFinalTime >= this.crystalDamage.getValue() && !this.placeCrystal()) {
                                       return;
                                    }
                                 } else if (!this.placeCrystal()) {
                                    return;
                                 }
                              } else if (this.startPacket) {
                                 return;
                              }
                           }

                           if (this.waitPlace.getValue()) {
                              for(class_2350 i : class_2350.values()) {
                                 if (this.breakPos.method_10093(i).equals(AutoCrystal.INSTANCE.crystalPos)) {
                                    if (AutoCrystal.INSTANCE.canPlaceCrystal(this.breakPos, false, false)) {
                                       return;
                                    }
                                    break;
                                 }
                              }
                           }

                           if (this.delayTimer.passed((long)this.stopDelay.getValue())) {
                              if (this.startPacket) {
                                 if (this.isAir(this.breakPos)) {
                                    return;
                                 }

                                 if (this.onlyGround.getValue() && !mc.field_1724.method_24828()) {
                                    return;
                                 }

                                 if (this.mineTimer.passed((long)this.breakFinalTime)) {
                                    if (this.endRotate.getValue() && this.shouldYawStep() && !this.faceVector(this.breakPos.method_46558().method_43206(BlockUtil.getClickSide(this.breakPos), (double)0.5F))) {
                                       return;
                                    }

                                    this.switchBack = () -> {
                                       if (!this.endRotate.getValue() || this.faceVector(this.breakPos.method_46558().method_43206(BlockUtil.getClickSide(this.breakPos), (double)0.5F))) {
                                          if (this.bypassGround.getValue() && !mc.field_1724.method_6128() && this.breakPos != null && !this.isAir(this.breakPos) && this.bypassTime.getValue() > (double)0.0F && class_3532.method_15355((float)this.breakPos.method_46558().method_1025(mc.field_1724.method_33571())) <= this.range.getValueFloat() + 2.0F) {
                                             double breakTime = this.breakFinalTime - this.bypassTime.getValue();
                                             if (breakTime <= (double)0.0F || this.mineTimer.passed((long)breakTime)) {
                                                mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455(), true));
                                             }
                                          }

                                          sendSequencedPacket((id) -> new class_2846(class_2847.field_12973, this.breakPos, BlockUtil.getClickSide(this.breakPos), id));
                                          if (this.endSwing.getValue()) {
                                             EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                                          }

                                          ++this.breakNumber;
                                          this.delayTimer.reset();
                                          this.startTime.reset();
                                          if (this.afterBreak.getValue() && this.shouldCrystal()) {
                                             for(class_2350 facing : class_2350.values()) {
                                                CombatUtil.attackCrystal(this.breakPos.method_10093(facing), this.placeRotate.getValue(), true);
                                             }
                                          }

                                          if (this.setAir.getValue()) {
                                             mc.field_1687.method_8501(this.breakPos, class_2246.field_10124.method_9564());
                                          }

                                          if (this.endRotate.getValue() && !this.shouldYawStep()) {
                                             this.scheduleRotationReset();
                                          }

                                          ghost = true;
                                          this.switchBackToOriginal(false);
                                       }
                                    };
                                    this.switchBack.run();
                                    this.switchBack = null;
                                 }
                              } else {
                                 if (!this.startTime.passed((long)this.startDelay.getValueInt())) {
                                    return;
                                 }

                                 if (!this.mineAir.getValue() && this.isAir(this.breakPos)) {
                                    return;
                                 }

                                 class_2350 side = BlockUtil.getClickSide(this.breakPos);
                                 if (this.rotate.getValue()) {
                                    class_2382 vec3i = side.method_10163();
                                    if (!this.faceVector(this.breakPos.method_46558().method_1019(new class_243((double)vec3i.method_10263() * (double)0.5F, (double)vec3i.method_10264() * (double)0.5F, (double)vec3i.method_10260() * (double)0.5F)))) {
                                       return;
                                    }
                                 }

                                 this.mineTimer.reset();
                                 this.animationTime.reset();
                                 this.animationTime.setLength((long)this.breakFinalTime);
                                 if (this.swing.getValue()) {
                                    EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                                 }

                                 this.startMining();
                              }

                           }
                        }
                     }
                  }
               } else {
                  this.switchBackToOriginal(false);
               }
            } else {
               if (this.breakPos.equals(secondPos)) {
                  secondPos = null;
               }

               this.switchBackToOriginal(false);
               this.resetMining();
            }
         }
      }
   }

   private void scheduleRotationReset() {
      this.isRotating = true;
      this.rotationResetTimer.reset();
      (new Thread(() -> {
         try {
            Thread.sleep(50L);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }

         if (this.isRotating && this.rotationResetTimer.passedMs((double)50.0F)) {
            Astra.ROTATION.snapBack();
            this.isRotating = false;
         }

      })).start();
   }

   private void startMining() {
      this.toolSwitched = false;
      this.secondToolSwitched = false;
      this.switchBackTimer.reset();
      this.secondSwitchBackTimer.reset();
      class_2350 side = BlockUtil.getClickSide(this.breakPos);
      sendSequencedPacket((id) -> new class_2846(class_2847.field_12968, this.breakPos, side, id));
      if (this.fastBypass.getValue()) {
         this.sendFastBypassPackets();
      }

      this.startPacket = true;
      if (this.rotate.getValue() && !this.shouldYawStep()) {
         this.scheduleRotationReset();
      }

      this.startTime.reset();
   }

   private void sendFastBypassPackets() {
      class_2338 highPos = new class_2338(this.breakPos.method_10263(), 400, this.breakPos.method_10260());

      for(int i = 0; i < 3; ++i) {
         int heightOffset = i * 10;
         class_2350[] directions = class_2350.values();
         class_2350 randomDir = directions[(int)(Math.random() * (double)directions.length)];
         sendSequencedPacket((id) -> new class_2846(class_2847.field_12968, highPos.method_10086(heightOffset), randomDir, id));
      }

   }

   private void doDoubleBreak() {
      class_2350 side = BlockUtil.getClickSide(this.breakPos);
      this.secondTimer.reset();
      sendSequencedPacket((id) -> new class_2846(class_2847.field_12968, this.breakPos, side, id));
      sendSequencedPacket((id) -> new class_2846(class_2847.field_12973, this.breakPos, side, id));
   }

   boolean placeCrystal() {
      int crystal = this.findCrystal();
      if (crystal != -1) {
         int oldSlot = mc.field_1724.method_31548().field_7545;
         this.doSwap(crystal, crystal);
         BlockUtil.placeCrystal(this.breakPos.method_10084(), this.placeRotate.getValue());
         this.doSwap(oldSlot, crystal);
         this.placeTimer.reset();
         return !this.waitPlace.getValue();
      } else {
         return true;
      }
   }

   int findCrystal() {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8301) : InventoryUtil.findItem(class_1802.field_8301);
   }

   void doSwap(int slot, int inv) {
      if (!this.inventory.getValue()) {
         InventoryUtil.switchToSlot(slot);
      } else {
         InventoryUtil.inventorySwap(inv, mc.field_1724.method_31548().field_7545);
      }

   }

   @EventListener
   public void onAttackBlock(ClickBlockEvent event) {
      if (!nullCheck()) {
         if (!mc.field_1724.method_7337()) {
            event.cancel();
            class_2338 pos = event.getPos();
            if (!unbreakable(pos)) {
               if (!this.isAir(pos)) {
                  if (!pos.equals(this.breakPos)) {
                     if (this.breakPos == null || !this.preferWeb.getValue() || BlockUtil.getBlock(this.breakPos) != class_2246.field_10343) {
                        if (this.breakPos == null || !this.preferHead.getValue() || !mc.field_1724.method_20448() || !EntityUtil.getPlayerPos(true).method_10084().equals(this.breakPos)) {
                           if (BlockUtil.getClickSideStrict(pos) != null) {
                              if (!((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(pos.method_46558())) > this.range.getValue())) {
                                 this.switchBackToOriginal(false);
                                 this.switchBackToOriginal(true);
                                 this.breakPos = pos;
                                 this.breakNumber = 0;
                                 this.startPacket = false;
                                 ghost = false;
                                 complete = false;
                                 this.mineTimer.reset();
                                 this.animationTime.reset();
                                 this.toolSwitched = false;
                                 this.secondToolSwitched = false;
                                 this.originalSlot = mc.field_1724.method_31548().field_7545;
                                 this.secondOriginalSlot = -1;
                                 if (this.doubleBreak.getValue()) {
                                    this.initializeDoubleBreak();
                                 }

                                 if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                                    if (this.startTime.passed((long)this.startDelay.getValueInt())) {
                                       class_2350 side = BlockUtil.getClickSide(this.breakPos);
                                       if (this.rotate.getValue()) {
                                          class_2382 vec3i = side.method_10163();
                                          if (!this.faceVector(this.breakPos.method_46558().method_1019(new class_243((double)vec3i.method_10263() * (double)0.5F, (double)vec3i.method_10264() * (double)0.5F, (double)vec3i.method_10260() * (double)0.5F)))) {
                                             return;
                                          }
                                       }

                                       if (this.swing.getValue()) {
                                          EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                                       }

                                       this.startMining();
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
      }
   }

   boolean faceVector(class_243 directionVec) {
      if (!this.shouldYawStep()) {
         Astra.ROTATION.lookAt(directionVec);
         this.isRotating = true;
         this.lastRotationTarget = directionVec;
         return true;
      } else {
         this.sync.reset();
         this.directionVec = directionVec;
         if (Astra.ROTATION.inFov(directionVec, this.fov.getValueFloat())) {
            return true;
         } else {
            return !this.checkFov.getValue();
         }
      }
   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.breakPos != null && mc.field_1687.method_22347(this.breakPos)) {
         complete = true;
      }

      if (!mc.field_1724.method_7337()) {
         if (secondPos != null) {
            if (this.isAir(secondPos)) {
               secondPos = null;
               return;
            }

            if (!this.checkDouble.getValue() || !secondPos.equals(this.breakPos)) {
               this.secondAnim.setLength((long)this.secondFinalTime);
               double ease = this.secondAnim.ease(this.ease.getValue());
               if (this.box.getValue()) {
                  Render3DUtil.drawFill(matrixStack, this.getFillBox(secondPos, ease), this.doubleColor.getValue());
               }

               if (this.outline.getValue()) {
                  Render3DUtil.drawBox(matrixStack, this.getOutlineBox(secondPos, ease), this.doubleOutlineColor.getValue());
               }
            }
         }

         if (this.breakPos != null) {
            progress = (double)this.mineTimer.getMs() / this.breakFinalTime;
            double ease;
            if (this.startPacket) {
               this.animationTime.setLength((long)this.breakFinalTime);
               ease = this.animationTime.ease(this.ease.getValue());
            } else {
               ease = (double)0.0F;
            }

            if (unbreakable(this.breakPos)) {
               if (this.box.getValue()) {
                  Render3DUtil.drawFill(matrixStack, new class_238(this.breakPos), this.startColor.getValue());
               }

               if (this.outline.getValue()) {
                  Render3DUtil.drawBox(matrixStack, new class_238(this.breakPos), this.startOutlineColor.getValue());
               }

               return;
            }

            if (this.box.getValue()) {
               double fadeProgress = this.startPacket ? (double)this.mineTimer.getMs() / this.breakFinalTime : (double)0.0F;
               fadeProgress = Math.min(fadeProgress, (double)1.0F);
               Render3DUtil.drawFill(matrixStack, this.getFillBox(this.breakPos, ease), this.getColor(fadeProgress));
            }

            if (this.outline.getValue()) {
               double fadeProgress = this.startPacket ? (double)this.mineTimer.getMs() / this.breakFinalTime : (double)0.0F;
               fadeProgress = Math.min(fadeProgress, (double)1.0F);
               Render3DUtil.drawBox(matrixStack, this.getOutlineBox(this.breakPos, ease), this.getOutlineColor(fadeProgress));
            }

            if (this.text.getValue()) {
               if (this.isAir(this.breakPos)) {
                  Render3DUtil.drawText3D("Waiting", this.breakPos.method_46558(), -1);
               } else if ((double)((int)this.mineTimer.getMs()) < this.breakFinalTime) {
                  Render3DUtil.drawText3D(this.df.format(progress * (double)100.0F) + "%", this.breakPos.method_46558(), -1);
               } else {
                  Render3DUtil.drawText3D("100.0%", this.breakPos.method_46558(), -1);
               }
            }
         } else {
            progress = (double)0.0F;
         }
      } else {
         progress = (double)0.0F;
      }

   }

   private class_238 getFillBox(class_2338 pos, double ease) {
      switch (((Animation)this.animation.getValue()).ordinal()) {
         case 0:
            ease = ((double)1.0F - ease) / (double)2.0F;
            return (new class_238(pos)).method_1002(ease, ease, ease).method_1002(-ease, -ease, -ease);
         case 1:
            ease = ((double)1.0F - ease) / (double)2.0F;
            return (new class_238(pos)).method_1002(ease, (double)0.0F, ease).method_1002(-ease, (double)0.0F, -ease);
         case 2:
            return new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)pos.method_10264() + ease, (double)(pos.method_10260() + 1));
         case 3:
            return new class_238((double)pos.method_10263(), (double)(pos.method_10264() + 1) - ease, (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)(pos.method_10264() + 1), (double)(pos.method_10260() + 1));
         case 4:
            return (new class_238(pos)).method_1002(ease, ease, ease).method_1002(-ease, -ease, -ease);
         default:
            return new class_238(pos);
      }
   }

   private class_238 getOutlineBox(class_2338 pos, double ease) {
      ease = Math.min(ease + this.expandLine.getValue(), (double)1.0F);
      switch (((Animation)this.animation.getValue()).ordinal()) {
         case 0:
            ease = ((double)1.0F - ease) / (double)2.0F;
            return (new class_238(pos)).method_1002(ease, ease, ease).method_1002(-ease, -ease, -ease);
         case 1:
            ease = ((double)1.0F - ease) / (double)2.0F;
            return (new class_238(pos)).method_1002(ease, (double)0.0F, ease).method_1002(-ease, (double)0.0F, -ease);
         case 2:
            return new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)pos.method_10264() + ease, (double)(pos.method_10260() + 1));
         case 3:
            return new class_238((double)pos.method_10263(), (double)(pos.method_10264() + 1) - ease, (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)(pos.method_10264() + 1), (double)(pos.method_10260() + 1));
         case 4:
            return (new class_238(pos)).method_1002(ease, ease, ease).method_1002(-ease, -ease, -ease);
         default:
            return new class_238(pos);
      }
   }

   @EventListener(
      priority = -200
   )
   public void onPacketSend(PacketEvent.Send event) {
      if (!nullCheck() && !mc.field_1724.method_7337()) {
         if (event.getPacket() instanceof class_2828) {
            if (this.bypassGround.getValue() && !mc.field_1724.method_6128() && this.breakPos != null && !this.isAir(this.breakPos) && this.bypassTime.getValue() > (double)0.0F && class_3532.method_15355((float)this.breakPos.method_46558().method_1025(mc.field_1724.method_33571())) <= this.range.getValueFloat() + 2.0F) {
               double breakTime = this.breakFinalTime - this.bypassTime.getValue();
               if (breakTime <= (double)0.0F || this.mineTimer.passed((long)breakTime)) {
                  ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(true);
               }
            }
         } else {
            class_2596<?> packet = event.getPacket();
            if (packet instanceof class_2868) {
               class_2868 packet2 = (class_2868)packet;
               if (packet2.method_12442() != this.lastSlot) {
                  this.lastSlot = packet2.method_12442();
                  if (this.switchReset.getValue()) {
                     this.startPacket = false;
                     ghost = false;
                     complete = false;
                     this.mineTimer.reset();
                     this.animationTime.reset();
                  }
               }
            } else {
               packet = event.getPacket();
               if (packet instanceof class_2846) {
                  class_2846 packet3 = (class_2846)packet;
                  if (packet3.method_12363() == class_2847.field_12968) {
                     if (this.breakPos == null || !packet3.method_12362().equals(this.breakPos)) {
                        return;
                     }

                     this.startPacket = true;
                  } else if (packet3.method_12363() == class_2847.field_12973) {
                     if (this.breakPos == null || !packet3.method_12362().equals(this.breakPos)) {
                        return;
                     }

                     if (!this.instant.getValue()) {
                        this.startPacket = false;
                        ghost = false;
                        complete = false;
                        this.switchBackToOriginal(false);
                     }
                  }
               }
            }
         }

      }
   }

   boolean canPlaceCrystal(class_2338 pos) {
      class_2338 obsPos = pos.method_10074();
      class_2338 boost = obsPos.method_10084();
      return (BlockUtil.getBlock(obsPos) == class_2246.field_9987 || BlockUtil.getBlock(obsPos) == class_2246.field_10540) && BlockUtil.getClickSideStrict(obsPos) != null && this.noEntity(boost) && this.noEntity(boost.method_10084()) && (!ClientSetting.INSTANCE.lowVersion.getValue() || mc.field_1687.method_22347(boost.method_10084()));
   }

   boolean noEntity(class_2338 pos) {
      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (!(entity instanceof class_1542) && (!(entity instanceof class_1531) || !AntiCheat.INSTANCE.ignoreArmorStand.getValue())) {
            return false;
         }
      }

      return true;
   }

   int findBlock(class_2248 block) {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(block) : InventoryUtil.findBlock(block);
   }

   boolean shouldCrystal() {
      return this.crystal.getValue() && (!this.onlyHeadBomber.getValue() || this.obsidian.isPressed()) || CevBreaker.INSTANCE.isOn();
   }

   public static double getBreakTime(class_2338 pos) {
      int slot = INSTANCE.getTool(pos);
      if (slot == -1) {
         slot = mc.field_1724.method_31548().field_7545;
      }

      return INSTANCE.getBreakTime(pos, slot);
   }

   double getBreakTime(class_2338 pos, int slot) {
      return this.getBreakTime(pos, slot, this.damage.getValue());
   }

   double getBreakTime(class_2338 pos, int slot, double damage) {
      return (double)(1.0F / this.getBlockStrength(pos, mc.field_1724.method_31548().method_5438(slot)) / 20.0F * 1000.0F) * damage;
   }

   float getBlockStrength(class_2338 position, class_1799 itemStack) {
      class_2680 state = mc.field_1687.method_8320(position);
      float hardness = state.method_26214(mc.field_1687, position);
      if (hardness < 0.0F) {
         return 0.0F;
      } else {
         float i = state.method_29291() && !itemStack.method_7951(state) ? 100.0F : 30.0F;
         return this.getDigSpeed(state, itemStack) / hardness / i;
      }
   }

   float getDigSpeed(class_2680 state, class_1799 itemStack) {
      float digSpeed = this.getDestroySpeed(state, itemStack);
      int efficiencyModifier;
      if (digSpeed > 1.0F && (efficiencyModifier = class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get(), itemStack)) > 0 && !itemStack.method_7960()) {
         digSpeed += (float)(StrictMath.pow((double)efficiencyModifier, (double)2.0F) + (double)1.0F);
      }

      if (mc.field_1724.method_6059(class_1294.field_5917)) {
         digSpeed *= 1.0F + (float)(mc.field_1724.method_6112(class_1294.field_5917).method_5578() + 1) * 0.2F;
      }

      if (mc.field_1724.method_6059(class_1294.field_5901)) {
         float var10001;
         switch (mc.field_1724.method_6112(class_1294.field_5901).method_5578()) {
            case 0 -> var10001 = 0.3F;
            case 1 -> var10001 = 0.09F;
            case 2 -> var10001 = 0.0027F;
            default -> var10001 = 8.1E-4F;
         }

         digSpeed *= var10001;
      }

      if (mc.field_1724.method_5869()) {
         digSpeed *= (float)mc.field_1724.method_5996(class_5134.field_51576).method_6194();
      }

      boolean inWeb = this.checkWeb.getValue() && Astra.PLAYER.isInWeb(mc.field_1724) && mc.field_1687.method_8320(this.breakPos).method_26204() == class_2246.field_10343;
      if ((!mc.field_1724.method_24828() || inWeb) && INSTANCE.checkGround.getValue() && (!this.smart.getValue() || Criticals.INSTANCE.mode.is(Criticals.Mode.Ground) && Criticals.INSTANCE.isOn() || mc.field_1724.method_6128() || inWeb)) {
         digSpeed /= 5.0F;
      }

      return digSpeed < 0.0F ? 0.0F : digSpeed;
   }

   float getDestroySpeed(class_2680 state, class_1799 itemStack) {
      float destroySpeed = 1.0F;
      if (itemStack != null && !itemStack.method_7960()) {
         destroySpeed *= itemStack.method_7924(state);
      }

      return destroySpeed;
   }

   Color getColor(double quad) {
      int sR = this.startColor.getValue().getRed();
      int sG = this.startColor.getValue().getGreen();
      int sB = this.startColor.getValue().getBlue();
      int sA = this.startColor.getValue().getAlpha();
      int eR = this.endColor.getValue().getRed();
      int eG = this.endColor.getValue().getGreen();
      int eB = this.endColor.getValue().getBlue();
      int eA = this.endColor.getValue().getAlpha();
      return new Color((int)((double)sR + (double)(eR - sR) * quad), (int)((double)sG + (double)(eG - sG) * quad), (int)((double)sB + (double)(eB - sB) * quad), (int)((double)sA + (double)(eA - sA) * quad));
   }

   Color getOutlineColor(double quad) {
      int sR = this.startOutlineColor.getValue().getRed();
      int sG = this.startOutlineColor.getValue().getGreen();
      int sB = this.startOutlineColor.getValue().getBlue();
      int sA = this.startOutlineColor.getValue().getAlpha();
      int eR = this.endOutlineColor.getValue().getRed();
      int eG = this.endOutlineColor.getValue().getGreen();
      int eB = this.endOutlineColor.getValue().getBlue();
      int eA = this.endOutlineColor.getValue().getAlpha();
      return new Color((int)((double)sR + (double)(eR - sR) * quad), (int)((double)sG + (double)(eG - sG) * quad), (int)((double)sB + (double)(eB - sB) * quad), (int)((double)sA + (double)(eA - sA) * quad));
   }

   int getTool(class_2338 pos) {
      if (this.toolSwitchMode.is(PacketMine.ToolSwitchMode.None)) {
         return -1;
      } else if (this.hotBar.getValue()) {
         int index = -1;
         float CurrentFastest = 1.0F;

         for(int i = 0; i < 9; ++i) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            float destroySpeed;
            float digSpeed;
            if (stack != class_1799.field_8037 && (digSpeed = (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get(), stack)) + (destroySpeed = stack.method_7924(mc.field_1687.method_8320(pos))) > CurrentFastest) {
               CurrentFastest = digSpeed + destroySpeed;
               index = i;
            }
         }

         return index;
      } else {
         AtomicInteger slot = new AtomicInteger();
         slot.set(-1);
         float CurrentFastest = 1.0F;

         for(Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            float destroySpeed;
            float digSpeed;
            if (!(((class_1799)entry.getValue()).method_7909() instanceof class_1739) && (digSpeed = (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9131.method_58273()).method_40264(class_1893.field_9131).get(), (class_1799)entry.getValue())) + (destroySpeed = ((class_1799)entry.getValue()).method_7924(mc.field_1687.method_8320(pos))) > CurrentFastest) {
               CurrentFastest = digSpeed + destroySpeed;
               slot.set((Integer)entry.getKey());
            }
         }

         return slot.get();
      }
   }

   private boolean shouldYawStep() {
      if (this.whenElytra.getValue() || !mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying())) {
         return this.yawStep.getValue() && !Velocity.INSTANCE.noRotation();
      } else {
         return false;
      }
   }

   boolean isAir(class_2338 breakPos) {
      return mc.field_1687.method_22347(breakPos) || BlockUtil.getBlock(breakPos) == class_2246.field_10036 && BlockUtil.hasCrystal(breakPos);
   }

   public static boolean unbreakable(class_2338 blockPos) {
      class_2248 block = mc.field_1687.method_8320(blockPos).method_26204();
      return !(block instanceof class_2189) && (block.method_36555() == -1.0F || block.method_36555() == 100.0F);
   }

   public static enum Page {
      General,
      Check,
      Place,
      Rotation,
      Render;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Check, Place, Rotation, Render};
      }
   }

   public static enum ToolSwitchMode {
      None,
      Delay,
      Silent;

      // $FF: synthetic method
      private static ToolSwitchMode[] $values() {
         return new ToolSwitchMode[]{None, Delay, Silent};
      }
   }

   private static enum Animation {
      Center,
      Grow,
      Up,
      Down,
      Oscillation,
      None;

      // $FF: synthetic method
      private static Animation[] $values() {
         return new Animation[]{Center, Grow, Up, Down, Oscillation, None};
      }
   }
}
