package dev.luminous.mod.modules.impl.player;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClickBlockEvent;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.FadeUtils;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.IPlayerMoveC2SPacket;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.combat.AutoAnchor;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.impl.combat.CevBreaker;
import dev.luminous.mod.modules.impl.combat.Criticals;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import dev.luminous.mod.modules.settings.enums.Timing;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1531;
import net.minecraft.class_1542;
import net.minecraft.class_1713;
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

public class SpeedMine extends Module {
   public static SpeedMine INSTANCE;
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
   public final BooleanSetting noGhostHand;
   public final BooleanSetting noCollide;
   private final EnumSetting<Timing> timing;
   private final BooleanSetting grimDisabler;
   private final BooleanSetting instant;
   private final BooleanSetting wait;
   private final BooleanSetting mineAir;
   private final BooleanSetting hotBar;
   private final BooleanSetting doubleBreak;
   public final BooleanSetting autoSwitch;
   private final SliderSetting start;
   private final SliderSetting timeOut;
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
   boolean swapped;
   int mainSlot;

   public SpeedMine() {
      super("SpeedMine", Module.Category.Player);
      this.page = this.add(new EnumSetting("Page", SpeedMine.Page.General));
      this.stopDelay = this.add(new SliderSetting("StopDelay", (double)50.0F, (double)0.0F, (double)500.0F, (double)1.0F, () -> this.page.is(SpeedMine.Page.General)));
      this.startDelay = this.add(new SliderSetting("StartDelay", (double)200.0F, (double)0.0F, (double)500.0F, (double)1.0F, () -> this.page.is(SpeedMine.Page.General)));
      this.damage = this.add(new SliderSetting("Damage", (double)0.7F, (double)0.0F, (double)2.0F, 0.01, () -> this.page.is(SpeedMine.Page.General)));
      this.maxBreak = this.add(new SliderSetting("MaxBreak", (double)3.0F, (double)0.0F, (double)20.0F, (double)1.0F, () -> this.page.is(SpeedMine.Page.General)));
      this.noGhostHand = this.add(new BooleanSetting("1.21", false, () -> this.page.is(SpeedMine.Page.General)));
      this.noCollide = this.add(new BooleanSetting("NoCollide", true, () -> this.page.is(SpeedMine.Page.General)));
      this.timing = this.add(new EnumSetting("Timing", Timing.All, () -> this.page.getValue() == SpeedMine.Page.General));
      this.grimDisabler = this.add(new BooleanSetting("GrimDisabler", false, () -> this.page.is(SpeedMine.Page.General)));
      this.instant = this.add(new BooleanSetting("Instant", false, () -> this.page.is(SpeedMine.Page.General)));
      this.wait = this.add(new BooleanSetting("Wait", true, () -> !this.instant.getValue() && this.page.is(SpeedMine.Page.General)));
      this.mineAir = this.add(new BooleanSetting("MineAir", true, () -> this.wait.getValue() && !this.instant.getValue() && this.page.is(SpeedMine.Page.General)));
      this.hotBar = this.add(new BooleanSetting("HotbarSwap", false, () -> this.page.is(SpeedMine.Page.General)));
      this.doubleBreak = this.add(new BooleanSetting("DoubleBreak", true, () -> this.page.is(SpeedMine.Page.General))).setParent();
      this.autoSwitch = this.add(new BooleanSetting("AutoSwitch", true, () -> this.page.is(SpeedMine.Page.General) && this.doubleBreak.isOpen()));
      this.start = this.add(new SliderSetting("Start", (double)0.9F, (double)0.0F, (double)2.0F, 0.01, () -> this.page.is(SpeedMine.Page.General) && this.doubleBreak.isOpen()));
      this.timeOut = this.add(new SliderSetting("TimeOut", (double)1.2F, (double)0.0F, (double)2.0F, 0.01, () -> this.page.is(SpeedMine.Page.General) && this.doubleBreak.isOpen()));
      this.setAir = this.add(new BooleanSetting("SetAir", false, () -> this.page.is(SpeedMine.Page.General)));
      this.swing = this.add(new BooleanSetting("Swing", true, () -> this.page.is(SpeedMine.Page.General)));
      this.endSwing = this.add(new BooleanSetting("EndSwing", false, () -> this.page.is(SpeedMine.Page.General)));
      this.range = this.add(new SliderSetting("Range", (double)6.0F, (double)3.0F, (double)10.0F, 0.1, () -> this.page.is(SpeedMine.Page.General)));
      this.swingMode = this.add(new EnumSetting("SwingSide", SwingSide.All, () -> this.page.is(SpeedMine.Page.General)));
      this.unbreakableCancel = this.add(new BooleanSetting("UnbreakableCancel", true, () -> this.page.is(SpeedMine.Page.Check)));
      this.switchReset = this.add(new BooleanSetting("SwitchReset", false, () -> this.page.is(SpeedMine.Page.Check)));
      this.preferWeb = this.add(new BooleanSetting("PreferWeb", true, () -> this.page.is(SpeedMine.Page.Check)));
      this.preferHead = this.add(new BooleanSetting("PreferHead", true, () -> this.page.is(SpeedMine.Page.Check)));
      this.farCancel = this.add(new BooleanSetting("FarCancel", false, () -> this.page.is(SpeedMine.Page.Check)));
      this.onlyGround = this.add(new BooleanSetting("OnlyGround", true, () -> this.page.is(SpeedMine.Page.Check)));
      this.checkWeb = this.add(new BooleanSetting("CheckWeb", true, () -> this.page.is(SpeedMine.Page.Check)));
      this.checkGround = this.add(new BooleanSetting("CheckGround", true, () -> this.page.is(SpeedMine.Page.Check)));
      this.smart = this.add(new BooleanSetting("Smart", true, () -> this.page.is(SpeedMine.Page.Check) && this.checkGround.getValue()));
      this.usingPause = this.add((new BooleanSetting("UsingPause", false, () -> this.page.is(SpeedMine.Page.Check))).setParent());
      this.allowOffhand = this.add(new BooleanSetting("AllowOffhand", true, () -> this.page.is(SpeedMine.Page.Check) && this.usingPause.isOpen()));
      this.bypassGround = this.add(new BooleanSetting("BypassGround", true, () -> this.page.is(SpeedMine.Page.Check)));
      this.bypassTime = this.add(new SliderSetting("BypassTime", 400, 0, 2000, () -> this.bypassGround.getValue() && this.page.is(SpeedMine.Page.Check)));
      this.pause = this.add(new BindSetting("Pause", -1, () -> this.page.is(SpeedMine.Page.Check)));
      this.rotate = this.add(new BooleanSetting("StartRotate", true, () -> this.page.is(SpeedMine.Page.Rotation)));
      this.endRotate = this.add(new BooleanSetting("EndRotate", false, () -> this.page.is(SpeedMine.Page.Rotation)));
      this.syncTime = this.add(new SliderSetting("Sync", 300, 0, 1000, () -> this.page.is(SpeedMine.Page.Rotation)));
      this.yawStep = this.add((new BooleanSetting("YawStep", false, () -> this.page.is(SpeedMine.Page.Rotation))).setParent());
      this.whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.page.is(SpeedMine.Page.Rotation) && this.yawStep.isOpen()));
      this.steps = this.add(new SliderSetting("Steps", 0.05, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(SpeedMine.Page.Rotation) && this.yawStep.isOpen()));
      this.checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.page.is(SpeedMine.Page.Rotation) && this.yawStep.isOpen()));
      this.fov = this.add(new SliderSetting("Fov", (double)20.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.page.is(SpeedMine.Page.Rotation) && this.yawStep.isOpen()));
      this.priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.page.is(SpeedMine.Page.Rotation) && this.yawStep.isOpen()));
      this.crystal = this.add((new BooleanSetting("Crystal", false, () -> this.page.is(SpeedMine.Page.Place))).setParent());
      this.onlyHeadBomber = this.add(new BooleanSetting("OnlyCev", true, () -> this.page.is(SpeedMine.Page.Place) && this.crystal.isOpen()));
      this.waitPlace = this.add(new BooleanSetting("WaitPlace", true, () -> this.page.is(SpeedMine.Page.Place) && this.crystal.isOpen()));
      this.spamPlace = this.add(new BooleanSetting("SpamPlace", false, () -> this.page.is(SpeedMine.Page.Place) && this.crystal.isOpen()));
      this.afterBreak = this.add(new BooleanSetting("AfterBreak", true, () -> this.page.is(SpeedMine.Page.Place) && this.crystal.isOpen()));
      this.checkDamage = this.add(new BooleanSetting("DetectProgress", true, () -> this.page.is(SpeedMine.Page.Place) && this.crystal.isOpen()));
      this.crystalDamage = this.add(new SliderSetting("Progress", (double)0.9F, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(SpeedMine.Page.Place) && this.crystal.isOpen() && this.checkDamage.getValue()));
      this.obsidian = this.add(new BindSetting("Obsidian", -1, () -> this.page.is(SpeedMine.Page.Place)));
      this.enderChest = this.add(new BindSetting("EnderChest", -1, () -> this.page.is(SpeedMine.Page.Place)));
      this.placeRotate = this.add(new BooleanSetting("PlaceRotate", true, () -> this.page.is(SpeedMine.Page.Place)));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(SpeedMine.Page.Place)));
      this.placeDelay = this.add(new SliderSetting("PlaceDelay", 100, 0, 1000, () -> this.page.is(SpeedMine.Page.Place)));
      this.checkDouble = this.add(new BooleanSetting("CheckDouble", false, () -> this.page.is(SpeedMine.Page.Render)));
      this.animation = this.add(new EnumSetting("Animation", SpeedMine.Animation.Up, () -> this.page.is(SpeedMine.Page.Render)));
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut, () -> this.page.is(SpeedMine.Page.Render)));
      this.fadeEase = this.add(new EnumSetting("FadeEase", Easing.CubicInOut, () -> this.page.is(SpeedMine.Page.Render)));
      this.expandLine = this.add(new SliderSetting("ExpandLine", (double)0.0F, (double)0.0F, (double)1.0F, () -> this.page.is(SpeedMine.Page.Render)));
      this.startColor = this.add(new ColorSetting("StartFill", new Color(255, 255, 255, 100), () -> this.page.is(SpeedMine.Page.Render)));
      this.startOutlineColor = this.add(new ColorSetting("StartOutline", new Color(255, 255, 255, 100), () -> this.page.is(SpeedMine.Page.Render)));
      this.endColor = this.add(new ColorSetting("EndFill", new Color(255, 255, 255, 100), () -> this.page.is(SpeedMine.Page.Render)));
      this.endOutlineColor = this.add(new ColorSetting("EndOutline", new Color(255, 255, 255, 100), () -> this.page.is(SpeedMine.Page.Render)));
      this.doubleColor = this.add(new ColorSetting("DoubleFill", new Color(88, 94, 255, 100), () -> this.doubleBreak.getValue() && this.page.is(SpeedMine.Page.Render)));
      this.doubleOutlineColor = this.add(new ColorSetting("DoubleOutline", new Color(88, 94, 255, 100), () -> this.doubleBreak.getValue() && this.page.is(SpeedMine.Page.Render)));
      this.text = this.add(new BooleanSetting("Text", true, () -> this.page.is(SpeedMine.Page.Render)));
      this.box = this.add(new BooleanSetting("Box", true, () -> this.page.is(SpeedMine.Page.Render)));
      this.outline = this.add(new BooleanSetting("Outline", true, () -> this.page.is(SpeedMine.Page.Render)));
      this.mineTimer = new Timer();
      this.sync = new Timer();
      this.secondTimer = new Timer();
      this.delayTimer = new Timer();
      this.placeTimer = new Timer();
      this.startTime = new Timer();
      this.lastSlot = -1;
      this.directionVec = null;
      this.startPacket = false;
      this.breakNumber = 0;
      this.sendGroundPacket = false;
      this.swapped = false;
      this.mainSlot = 0;
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
      this.startPacket = false;
      ghost = false;
      complete = false;
      this.breakPos = null;
      secondPos = null;
   }

   public void onDisable() {
      this.startPacket = false;
      ghost = false;
      complete = false;
      this.breakPos = null;
   }

   private void autoSwitch() {
      if (this.autoSwitch.getValue() && this.doubleBreak.getValue()) {
         int index = -1;
         if (secondPos != null) {
            float CurrentFastest = 1.0F;

            for(int i = 0; i < 9; ++i) {
               class_1799 stack = mc.field_1724.method_31548().method_5438(i);
               if (stack != class_1799.field_8037) {
                  float digSpeed = (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get(), stack);
                  float destroySpeed = stack.method_7924(mc.field_1687.method_8320(secondPos));
                  if (digSpeed + destroySpeed > CurrentFastest) {
                     CurrentFastest = digSpeed + destroySpeed;
                     index = i;
                  }
               }
            }
         }

         if (index != -1 && !mc.field_1690.field_1904.method_1434() && !mc.field_1690.field_1886.method_1434() && !mc.field_1724.method_6115() && this.secondTimer.passedMs(this.getBreakTime(secondPos, index, this.start.getValue()))) {
            if (index != mc.field_1724.method_31548().field_7545) {
               this.mainSlot = mc.field_1724.method_31548().field_7545;
               InventoryUtil.switchToSlot(index);
               this.swapped = true;
            }
         } else if (this.swapped) {
            InventoryUtil.switchToSlot(this.mainSlot);
            this.swapped = false;
         }
      }

   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (this.breakPos != null && mc.field_1687.method_22347(this.breakPos)) {
            complete = true;
         }

         if (secondPos != null) {
            int secondSlot = this.getTool(secondPos);
            if (secondSlot == -1) {
               secondSlot = mc.field_1724.method_31548().field_7545;
            }

            this.secondFinalTime = this.getBreakTime(secondPos, secondSlot, (double)1.0F);
            if (!this.isAir(secondPos) && !unbreakable(secondPos)) {
               double time = this.getBreakTime(secondPos, mc.field_1724.method_31548().field_7545, (double)1.0F);
               if (this.secondTimer.passedMs(time * this.timeOut.getValue())) {
                  secondPos = null;
               }
            } else {
               secondPos = null;
            }
         }

         if (this.switchBack != null && event.isPre()) {
            this.switchBack.run();
            this.switchBack = null;
         }

         if ((!this.timing.is(Timing.Pre) || !event.isPost()) && (!this.timing.is(Timing.Post) || !event.isPre())) {
            if (mc.field_1724.method_29504()) {
               secondPos = null;
            }

            this.autoSwitch();
            if (mc.field_1724.method_7337()) {
               this.startPacket = false;
               ghost = false;
               complete = false;
               this.breakNumber = 0;
               this.breakPos = null;
               progress = (double)0.0F;
            } else if (this.breakPos == null) {
               this.breakNumber = 0;
               this.startPacket = false;
               ghost = false;
               complete = false;
               progress = (double)0.0F;
            } else {
               int slot = this.getTool(this.breakPos);
               if (slot == -1) {
                  slot = mc.field_1724.method_31548().field_7545;
               }

               this.breakFinalTime = this.getBreakTime(this.breakPos, slot);
               progress = (double)this.mineTimer.getMs() / this.breakFinalTime;
               if (this.isAir(this.breakPos)) {
                  this.breakNumber = 0;
               }

               if ((!((double)this.breakNumber > this.maxBreak.getValue() - (double)1.0F) || !(this.maxBreak.getValue() > (double)0.0F) || complete) && (this.wait.getValue() || !this.isAir(this.breakPos) || this.instant.getValue())) {
                  if (unbreakable(this.breakPos)) {
                     if (this.unbreakableCancel.getValue()) {
                        this.breakPos = null;
                        this.startPacket = false;
                        ghost = false;
                        complete = false;
                     }

                     this.breakNumber = 0;
                  } else if ((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(this.breakPos.method_46558())) > this.range.getValue()) {
                     if (this.farCancel.getValue()) {
                        this.startPacket = false;
                        ghost = false;
                        complete = false;
                        this.breakNumber = 0;
                        this.breakPos = null;
                     }
                  } else if ((!this.usingPause.getValue() || !mc.field_1724.method_6115() || this.allowOffhand.getValue() && mc.field_1724.method_6058() != class_1268.field_5808) && !this.pause.isPressed() && (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) && (!this.breakPos.equals(AutoAnchor.INSTANCE.currentPos) || !(BlockUtil.getBlock(getBreakPos()) instanceof class_4969)) && (this.hotBar.getValue() || EntityUtil.inInventory())) {
                     if (!this.isAir(this.breakPos)) {
                        if (this.canPlaceCrystal(this.breakPos.method_10084()) && this.shouldCrystal()) {
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
                     } else {
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
                           } else if (this.obsidian.isPressed()) {
                              int obsidian = this.findBlock(class_2246.field_10540);
                              if (obsidian != -1) {
                                 boolean hasCrystal = false;
                                 if (this.shouldCrystal()) {
                                    for(class_1297 entity : BlockUtil.getEntities(new class_238(this.breakPos.method_10084()))) {
                                       if (entity instanceof class_1511) {
                                          hasCrystal = true;
                                          break;
                                       }
                                    }
                                 }

                                 if (!hasCrystal || this.spamPlace.getValue()) {
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

                              int old = mc.field_1724.method_31548().field_7545;
                              boolean shouldSwitch;
                              if (this.hotBar.getValue()) {
                                 shouldSwitch = slot != old;
                              } else {
                                 if (slot < 9) {
                                    slot += 36;
                                 }

                                 shouldSwitch = old + 36 != slot;
                              }

                              if (shouldSwitch) {
                                 if (this.hotBar.getValue()) {
                                    InventoryUtil.switchToSlot(slot);
                                 } else {
                                    mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, old, class_1713.field_7791, mc.field_1724);
                                 }
                              }

                              this.switchBack = () -> {
                                 if (this.endRotate.getValue() && !this.faceVector(this.breakPos.method_46558().method_43206(BlockUtil.getClickSide(this.breakPos), (double)0.5F))) {
                                    if (shouldSwitch) {
                                       if (this.hotBar.getValue()) {
                                          InventoryUtil.switchToSlot(old);
                                       } else {
                                          mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, old, class_1713.field_7791, mc.field_1724);
                                          EntityUtil.syncInventory();
                                       }
                                    }
                                 } else {
                                    sendSequencedPacket((id) -> new class_2846(class_2847.field_12973, this.breakPos, BlockUtil.getClickSide(this.breakPos), id));
                                    if (this.endSwing.getValue()) {
                                       EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                                    }

                                    if (shouldSwitch) {
                                       if (this.hotBar.getValue()) {
                                          InventoryUtil.switchToSlot(old);
                                       } else {
                                          mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, slot, old, class_1713.field_7791, mc.field_1724);
                                          EntityUtil.syncInventory();
                                       }
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
                                       Alien.ROTATION.snapBack();
                                    }

                                    ghost = true;
                                 }

                              };
                              if (!this.noGhostHand.getValue()) {
                                 this.switchBack.run();
                                 this.switchBack = null;
                              }
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
                           if (this.swing.getValue()) {
                              EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                           }

                           if (this.doubleBreak.getValue()) {
                              if (secondPos == null || this.isAir(secondPos)) {
                                 double breakTime = this.getBreakTime(this.breakPos, slot, (double)1.0F);
                                 this.secondAnim.reset();
                                 this.secondAnim.setLength((long)breakTime);
                                 this.secondTimer.reset();
                                 secondPos = this.breakPos;
                              }

                              this.doDoubleBreak(side);
                           }

                           sendSequencedPacket((id) -> new class_2846(class_2847.field_12968, this.breakPos, side, id));
                           if (this.rotate.getValue() && !this.shouldYawStep()) {
                              Alien.ROTATION.snapBack();
                           }

                           this.startTime.reset();
                        }
                     }
                  }
               } else {
                  if (this.breakPos.equals(secondPos)) {
                     secondPos = null;
                  }

                  this.startPacket = false;
                  ghost = false;
                  complete = false;
                  this.breakNumber = 0;
                  this.breakPos = null;
               }
            }
         }
      }

   }

   private void breakBlock(class_2338 breakPos) {
      mc.field_1687.method_8320(breakPos).method_26204().method_9576(mc.field_1687, breakPos, mc.field_1687.method_8320(breakPos), mc.field_1724);
   }

   void doDoubleBreak(class_2350 side) {
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

   @EventListener
   public void onAttackBlock(ClickBlockEvent event) {
      if (!nullCheck() && !mc.field_1724.method_7337()) {
         event.cancel();
         class_2338 pos = event.getPos();
         if (!pos.equals(this.breakPos) && !unbreakable(pos) && (this.breakPos == null || !this.preferWeb.getValue() || BlockUtil.getBlock(this.breakPos) != class_2246.field_10343) && (this.breakPos == null || !this.preferHead.getValue() || !mc.field_1724.method_20448() || !EntityUtil.getPlayerPos(true).method_10084().equals(this.breakPos)) && BlockUtil.getClickSideStrict(pos) != null && !((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(pos.method_46558())) > this.range.getValue())) {
            this.breakPos = pos;
            this.breakNumber = 0;
            this.startPacket = false;
            ghost = false;
            complete = false;
            this.mineTimer.reset();
            this.animationTime.reset();
            if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
               class_2350 side = BlockUtil.getClickSide(this.breakPos);
               if (this.rotate.getValue()) {
                  class_2382 vec3i = side.method_10163();
                  if (!this.faceVector(this.breakPos.method_46558().method_1019(new class_243((double)vec3i.method_10263() * (double)0.5F, (double)vec3i.method_10264() * (double)0.5F, (double)vec3i.method_10260() * (double)0.5F)))) {
                     return;
                  }
               }

               if (this.startTime.passed((long)this.startDelay.getValueInt())) {
                  if (this.swing.getValue()) {
                     EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                  }

                  if (this.doubleBreak.getValue()) {
                     if (secondPos == null || this.isAir(secondPos)) {
                        int slot = this.getTool(this.breakPos);
                        if (slot == -1) {
                           slot = mc.field_1724.method_31548().field_7545;
                        }

                        this.secondFinalTime = this.getBreakTime(this.breakPos, slot, (double)1.0F);
                        this.secondAnim.reset();
                        this.secondAnim.setLength((long)this.secondFinalTime);
                        this.secondTimer.reset();
                        secondPos = this.breakPos;
                     }

                     this.doDoubleBreak(side);
                  }

                  int slot = this.getTool(this.breakPos);
                  if (slot == -1) {
                     slot = mc.field_1724.method_31548().field_7545;
                  }

                  this.breakFinalTime = this.getBreakTime(this.breakPos, slot);
                  sendSequencedPacket((id) -> new class_2846(class_2847.field_12968, this.breakPos, side, id));
                  if (this.rotate.getValue() && !this.shouldYawStep()) {
                     Alien.ROTATION.snapBack();
                  }

                  this.startTime.reset();
               }
            }
         }
      }

   }

   public void mine(class_2338 pos) {
      if (!nullCheck()) {
         if (mc.field_1724.method_7337()) {
            mc.field_1761.method_2910(pos, BlockUtil.getClickSide(pos));
         } else if (this.isOff()) {
            mc.field_1761.method_2910(pos, BlockUtil.getClickSide(pos));
         } else if (!pos.equals(this.breakPos) && !unbreakable(pos) && (this.breakPos == null || !this.preferWeb.getValue() || BlockUtil.getBlock(this.breakPos) != class_2246.field_10343) && (this.breakPos == null || !this.preferHead.getValue() || !mc.field_1724.method_20448() || !EntityUtil.getPlayerPos(true).method_10084().equals(this.breakPos)) && BlockUtil.getClickSideStrict(pos) != null && !((double)class_3532.method_15355((float)mc.field_1724.method_33571().method_1025(pos.method_46558())) > this.range.getValue())) {
            this.breakPos = pos;
            this.breakNumber = 0;
            this.startPacket = false;
            ghost = false;
            complete = false;
            this.mineTimer.reset();
            this.animationTime.reset();
         }
      }

   }

   boolean faceVector(class_243 directionVec) {
      if (!this.shouldYawStep()) {
         Alien.ROTATION.lookAt(directionVec);
         return true;
      } else {
         this.sync.reset();
         this.directionVec = directionVec;
         return Alien.ROTATION.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
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
            this.animationTime.setLength((long)this.breakFinalTime);
            double easex = this.animationTime.ease(this.ease.getValue());
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
               Render3DUtil.drawFill(matrixStack, this.getFillBox(this.breakPos, easex), this.getColor(this.animationTime.ease(this.fadeEase.getValue())));
            }

            if (this.outline.getValue()) {
               Render3DUtil.drawBox(matrixStack, this.getOutlineBox(this.breakPos, easex), this.getOutlineColor(this.animationTime.ease(this.fadeEase.getValue())));
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
                  this.sendGroundPacket = true;
                  ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(true);
               }
            } else {
               this.sendGroundPacket = false;
            }
         } else {
            class_2596 var6 = event.getPacket();
            if (var6 instanceof class_2868) {
               class_2868 packet = (class_2868)var6;
               if (packet.method_12442() != this.lastSlot) {
                  this.lastSlot = packet.method_12442();
                  if (this.switchReset.getValue()) {
                     this.startPacket = false;
                     ghost = false;
                     complete = false;
                     this.mineTimer.reset();
                     this.animationTime.reset();
                  }
               }
            } else {
               var6 = event.getPacket();
               if (var6 instanceof class_2846) {
                  class_2846 packetx = (class_2846)var6;
                  if (packetx.method_12363() == class_2847.field_12968) {
                     if (this.breakPos == null || !packetx.method_12362().equals(this.breakPos)) {
                        return;
                     }

                     if (this.grimDisabler.getValue()) {
                        mc.method_1562().method_52787(new class_2846(class_2847.field_12973, packetx.method_12362(), packetx.method_12360()));
                     }

                     this.startPacket = true;
                  } else if (packetx.method_12363() == class_2847.field_12973) {
                     if (this.breakPos == null || !packetx.method_12362().equals(this.breakPos)) {
                        return;
                     }

                     if (!this.instant.getValue()) {
                        this.startPacket = false;
                        ghost = false;
                        complete = false;
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

   void doSwap(int slot, int inv) {
      if (!this.inventory.getValue()) {
         InventoryUtil.switchToSlot(slot);
      } else {
         InventoryUtil.inventorySwap(inv, mc.field_1724.method_31548().field_7545);
      }

   }

   int findCrystal() {
      return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8301) : InventoryUtil.findItem(class_1802.field_8301);
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
      if (digSpeed > 1.0F) {
         int efficiencyModifier = class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get(), itemStack);
         if (efficiencyModifier > 0 && !itemStack.method_7960()) {
            digSpeed += (float)(StrictMath.pow((double)efficiencyModifier, (double)2.0F) + (double)1.0F);
         }
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

      boolean inWeb = this.checkWeb.getValue() && Alien.PLAYER.isInWeb(mc.field_1724) && mc.field_1687.method_8320(this.breakPos).method_26204() == class_2246.field_10343;
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
      if (this.hotBar.getValue()) {
         int index = -1;
         float CurrentFastest = 1.0F;

         for(int i = 0; i < 9; ++i) {
            class_1799 stack = mc.field_1724.method_31548().method_5438(i);
            if (stack != class_1799.field_8037) {
               float digSpeed = (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get(), stack);
               float destroySpeed = stack.method_7924(mc.field_1687.method_8320(pos));
               if (digSpeed + destroySpeed > CurrentFastest) {
                  CurrentFastest = digSpeed + destroySpeed;
                  index = i;
               }
            }
         }

         return index;
      } else {
         AtomicInteger slot = new AtomicInteger();
         slot.set(-1);
         float CurrentFastest = 1.0F;

         for(Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!(((class_1799)entry.getValue()).method_7909() instanceof class_1739)) {
               float digSpeed = (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get(), (class_1799)entry.getValue());
               float destroySpeed = ((class_1799)entry.getValue()).method_7924(mc.field_1687.method_8320(pos));
               if (digSpeed + destroySpeed > CurrentFastest) {
                  CurrentFastest = digSpeed + destroySpeed;
                  slot.set((Integer)entry.getKey());
               }
            }
         }

         return slot.get();
      }
   }

   private boolean shouldYawStep() {
      return this.whenElytra.getValue() || !mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() && !Velocity.INSTANCE.noRotation() : false;
   }

   boolean isAir(class_2338 breakPos) {
      return mc.field_1687.method_22347(breakPos) || BlockUtil.getBlock(breakPos) == class_2246.field_10036 && BlockUtil.hasCrystal(breakPos);
   }

   public static boolean unbreakable(class_2338 blockPos) {
      class_2248 block = mc.field_1687.method_8320(blockPos).method_26204();
      return !(block instanceof class_2189) && (block.method_36555() == -1.0F || block.method_36555() == 100.0F);
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
}
