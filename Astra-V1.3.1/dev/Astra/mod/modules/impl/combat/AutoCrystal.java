package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.EntitySpawnedEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.Render3DEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.entity.PlayerEntityPredict;
import dev.Astra.api.utils.math.AnimateUtil;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.DamageUtils;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.math.ExplosionUtil;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.JelloUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.asm.accessors.IEntity;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.impl.movement.Velocity;
import dev.Astra.mod.modules.impl.player.PacketMine;
import dev.Astra.mod.modules.settings.enums.SwingSide;
import dev.Astra.mod.modules.settings.enums.Timing;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.text.DecimalFormat;
import java.util.ArrayList;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2868;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_1297.class_5529;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class AutoCrystal extends Module {
   public static AutoCrystal INSTANCE;
   public class_2338 crystalPos;
   public final Timer lastBreakTimer = new Timer();
   private final EnumSetting<Page> page;
   final Animation animation;
   final DecimalFormat df;
   private final Timer baseTimer;
   private final Timer placeTimer;
   private final Timer noPosTimer;
   private final Timer switchTimer;
   private final Timer calcDelay;
   private final BindSetting pause;
   private final BooleanSetting preferAnchor;
   private final BooleanSetting breakOnlyHasCrystal;
   private final BooleanSetting eatingPause;
   private final SliderSetting switchCooldown;
   private final SliderSetting targetRange;
   private final SliderSetting updateDelay;
   private final BooleanSetting rotate;
   private final BooleanSetting onPlace;
   private final BooleanSetting onBreak;
   private final BooleanSetting yawStep;
   private final BooleanSetting whenElytra;
   private final SliderSetting steps;
   private final BooleanSetting checkFov;
   private final SliderSetting fov;
   private final SliderSetting priority;
   private final SliderSetting minDamage;
   private final SliderSetting maxSelf;
   private final SliderSetting reserve;
   private final BooleanSetting balance;
   private final SliderSetting balanceOffset;
   private final BooleanSetting place;
   public final SliderSetting placeRange;
   private final SliderSetting placeDelay;
   private final EnumSetting<SwapMode> autoSwap;
   private final BooleanSetting afterBreak;
   private final BooleanSetting forcePlace;
   private final BooleanSetting breakSetting;
   public final SliderSetting breakRange;
   private final SliderSetting breakDelay;
   private final SliderSetting minAge;
   private final BooleanSetting breakRemove;
   private final BooleanSetting onAdd;
   private final BooleanSetting resetCD;
   private final EnumSetting<Timing> timing;
   private final BooleanSetting interactOnRender;
   private final SliderSetting wallRange;
   private final EnumSetting<SwingSide> swingMode;
   private final ColorSetting text;
   private final EnumSetting<TargetESP> mode;
   private final SliderSetting animationTime;
   private final EnumSetting<Easing> ease;
   private final ColorSetting color;
   private final ColorSetting outlineColor;
   private final ColorSetting hitColor;
   private final ColorSetting hitOutlineColor;
   private final BooleanSetting render;
   private final BooleanSetting sync;
   private final BooleanSetting shrink;
   private final ColorSetting box;
   private final SliderSetting lineWidth;
   private final ColorSetting fill;
   private final SliderSetting sliderSpeed;
   private final SliderSetting startFadeTime;
   private final SliderSetting fadeSpeed;
   private final SliderSetting attackVecStep;
   private final BooleanSetting thread;
   private final BooleanSetting doCrystal;
   private final SliderSetting selfPredict;
   private final SliderSetting predictTicks;
   private final SliderSetting simulation;
   private final SliderSetting maxMotionY;
   private final BooleanSetting step;
   private final BooleanSetting doubleStep;
   private final BooleanSetting jump;
   private final BooleanSetting inBlockPause;
   private final BooleanSetting terrainIgnore;
   private final BooleanSetting basePlace;
   private final SliderSetting baseMin;
   private final SliderSetting baseMax;
   private final SliderSetting overrideMax;
   private final BooleanSetting baseBalance;
   private final BooleanSetting onlyBelow;
   private final BooleanSetting inventory;
   private final BooleanSetting detectMining;
   private final SliderSetting delay;
   private final BooleanSetting ignoreMine;
   private final SliderSetting constantProgress;
   private final BooleanSetting antiSurround;
   private final SliderSetting miningProgress;
   private final SliderSetting antiSurroundMax;
   private final BooleanSetting slowPlace;
   private final SliderSetting slowDelay;
   private final SliderSetting slowMinDamage;
   private final BooleanSetting lethalOverride;
   private final SliderSetting forceMaxHealth;
   private final SliderSetting forceMin;
   private final BooleanSetting armorBreaker;
   private final SliderSetting maxDurable;
   private final SliderSetting armorBreakerDamage;
   private final BooleanSetting forceWeb;
   public final BooleanSetting airPlace;
   public final BooleanSetting replace;
   private final SliderSetting hurtTime;
   private final SliderSetting waitHurt;
   private final SliderSetting syncTimeout;
   private final Timer syncTimer;
   public class_1657 displayTarget;
   public float breakDamage;
   public float tempDamage;
   public float lastDamage;
   public class_243 directionVec;
   double currentFade;
   private class_1511 tempBreakCrystal;
   private class_1511 breakCrystal;
   private class_2338 tempPos;
   private class_2338 syncPos;
   private class_243 placeVec3d;
   private class_243 curVec3d;
   int lastSlot;
   class_2338 tempBasePos;
   class_2338 basePos;

   public AutoCrystal() {
      super("AutoCrystal", Module.Category.Combat);
      this.page = this.add(new EnumSetting("Page", AutoCrystal.Page.General));
      this.animation = new Animation();
      this.df = new DecimalFormat("0.0");
      this.baseTimer = new Timer();
      this.placeTimer = new Timer();
      this.noPosTimer = new Timer();
      this.switchTimer = new Timer();
      this.calcDelay = new Timer();
      this.pause = this.add(new BindSetting("Pause", -1, () -> this.page.is(AutoCrystal.Page.Check)));
      this.preferAnchor = this.add(new BooleanSetting("PreferAnchor", true, () -> this.page.getValue() == AutoCrystal.Page.Check));
      this.breakOnlyHasCrystal = this.add(new BooleanSetting("OnlyHold", true, () -> this.page.getValue() == AutoCrystal.Page.Check));
      this.eatingPause = this.add(new BooleanSetting("EatingPause", true, () -> this.page.getValue() == AutoCrystal.Page.Check));
      this.switchCooldown = this.add((new SliderSetting("SwitchPause", 100, 0, 1000, () -> this.page.getValue() == AutoCrystal.Page.Check)).setSuffix("ms"));
      this.targetRange = this.add((new SliderSetting("TargetRange", (double)12.0F, (double)0.0F, (double)20.0F, () -> this.page.getValue() == AutoCrystal.Page.Check)).setSuffix("m"));
      this.updateDelay = this.add((new SliderSetting("UpdateDelay", 50, 0, 1000, () -> this.page.getValue() == AutoCrystal.Page.Check)).setSuffix("ms"));
      this.rotate = this.add((new BooleanSetting("Rotate", true, () -> this.page.getValue() == AutoCrystal.Page.Rotation)).setParent());
      this.onPlace = this.add(new BooleanSetting("OnPlace", false, () -> this.rotate.isOpen() && this.page.getValue() == AutoCrystal.Page.Rotation));
      this.onBreak = this.add(new BooleanSetting("OnBreak", false, () -> this.rotate.isOpen() && this.page.getValue() == AutoCrystal.Page.Rotation));
      this.yawStep = this.add((new BooleanSetting("YawStep", false, () -> this.rotate.isOpen() && this.page.getValue() == AutoCrystal.Page.Rotation)).setParent());
      this.whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.rotate.isOpen() && this.page.getValue() == AutoCrystal.Page.Rotation && this.yawStep.isOpen()));
      this.steps = this.add(new SliderSetting("Steps", 0.05, (double)0.0F, (double)1.0F, 0.01, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == AutoCrystal.Page.Rotation));
      this.checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == AutoCrystal.Page.Rotation));
      this.fov = this.add(new SliderSetting("Fov", (double)20.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.checkFov.getValue() && this.page.getValue() == AutoCrystal.Page.Rotation));
      this.priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == AutoCrystal.Page.Rotation));
      this.minDamage = this.add((new SliderSetting("Min", (double)5.0F, (double)0.0F, (double)36.0F, () -> this.page.getValue() == AutoCrystal.Page.General)).setSuffix("dmg"));
      this.maxSelf = this.add((new SliderSetting("Max", (double)12.0F, (double)0.0F, (double)36.0F, () -> this.page.getValue() == AutoCrystal.Page.General)).setSuffix("dmg"));
      this.reserve = this.add((new SliderSetting("Reserve", (double)2.0F, (double)0.0F, (double)10.0F, () -> this.page.getValue() == AutoCrystal.Page.General)).setSuffix("hp"));
      this.balance = this.add((new BooleanSetting("Balance", true, () -> this.page.getValue() == AutoCrystal.Page.General)).setParent());
      this.balanceOffset = this.add((new SliderSetting("BalanceOffset", (double)0.0F, (double)-20.0F, (double)20.0F, 0.1, () -> this.page.getValue() == AutoCrystal.Page.General && this.balance.isOpen())).setSuffix("hp"));
      this.place = this.add((new BooleanSetting("Place", true, () -> this.page.getValue() == AutoCrystal.Page.General)).setParent());
      this.placeRange = this.add((new SliderSetting("PlaceRange", (double)5.0F, (double)0.0F, (double)6.0F, 0.01, () -> this.page.getValue() == AutoCrystal.Page.General && this.place.isOpen())).setSuffix("m"));
      this.placeDelay = this.add((new SliderSetting("PlaceDelay", 300, 0, 1000, () -> this.page.getValue() == AutoCrystal.Page.General && this.place.isOpen())).setSuffix("ms"));
      this.autoSwap = this.add(new EnumSetting("AutoSwap", AutoCrystal.SwapMode.None, () -> this.page.getValue() == AutoCrystal.Page.General && this.place.isOpen()));
      this.afterBreak = this.add(new BooleanSetting("AfterBreak", true, () -> this.page.getValue() == AutoCrystal.Page.General && this.place.isOpen()));
      this.forcePlace = this.add(new BooleanSetting("ForcePlace", false, () -> this.page.getValue() == AutoCrystal.Page.General && this.place.isOpen()));
      this.breakSetting = this.add((new BooleanSetting("Break", true, () -> this.page.getValue() == AutoCrystal.Page.General)).setParent());
      this.breakRange = this.add((new SliderSetting("BreakRange", (double)4.0F, (double)0.0F, (double)6.0F, 0.01, () -> this.page.getValue() == AutoCrystal.Page.General && this.breakSetting.isOpen())).setSuffix("m"));
      this.breakDelay = this.add((new SliderSetting("BreakDelay", 300, 0, 1000, () -> this.page.getValue() == AutoCrystal.Page.General && this.breakSetting.isOpen())).setSuffix("ms"));
      this.minAge = this.add((new SliderSetting("MinAge", 0, 0, 20, () -> this.page.getValue() == AutoCrystal.Page.General && this.breakSetting.isOpen())).setSuffix("tick"));
      this.breakRemove = this.add(new BooleanSetting("Remove", false, () -> this.page.getValue() == AutoCrystal.Page.General && this.breakSetting.isOpen()));
      this.onAdd = this.add(new BooleanSetting("OnAdd", false, () -> this.page.getValue() == AutoCrystal.Page.General && this.breakSetting.isOpen()));
      this.resetCD = this.add(new BooleanSetting("ResetAttack", true, () -> this.page.getValue() == AutoCrystal.Page.General && this.breakSetting.isOpen()));
      this.timing = this.add(new EnumSetting("Timing", Timing.All, () -> this.page.getValue() == AutoCrystal.Page.General));
      this.interactOnRender = this.add(new BooleanSetting("InteractOnRender", false, () -> this.page.getValue() == AutoCrystal.Page.General));
      this.wallRange = this.add((new SliderSetting("WallRange", (double)6.0F, (double)0.0F, (double)6.0F, () -> this.page.getValue() == AutoCrystal.Page.General)).setSuffix("m"));
      this.swingMode = this.add(new EnumSetting("Swing", SwingSide.All, () -> this.page.getValue() == AutoCrystal.Page.General));
      this.text = this.add((new ColorSetting("Text", new Color(-1), () -> this.page.getValue() == AutoCrystal.Page.Render)).injectBoolean(true));
      this.mode = this.add(new EnumSetting("TargetESP", AutoCrystal.TargetESP.Fill, () -> this.page.getValue() == AutoCrystal.Page.Render));
      this.animationTime = this.add(new SliderSetting("AnimationTime", (double)200.0F, (double)0.0F, (double)2000.0F, (double)1.0F, () -> this.page.getValue() == AutoCrystal.Page.Render));
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut, () -> this.page.getValue() == AutoCrystal.Page.Render));
      this.color = this.add(new ColorSetting("TargetColor", new Color(255, 255, 255, 50), () -> this.page.getValue() == AutoCrystal.Page.Render));
      this.outlineColor = this.add(new ColorSetting("TargetOutlineColor", new Color(255, 255, 255, 50), () -> this.page.getValue() == AutoCrystal.Page.Render));
      this.hitColor = this.add(new ColorSetting("HitColor", new Color(255, 255, 255, 150), () -> this.page.getValue() == AutoCrystal.Page.Render));
      this.hitOutlineColor = this.add(new ColorSetting("HitOutlineColor", new Color(255, 255, 255, 150), () -> this.page.getValue() == AutoCrystal.Page.Render));
      this.render = this.add(new BooleanSetting("Render", true, () -> this.page.getValue() == AutoCrystal.Page.Render));
      this.sync = this.add(new BooleanSetting("Sync", true, () -> this.page.getValue() == AutoCrystal.Page.Render && this.render.getValue()));
      this.shrink = this.add(new BooleanSetting("Shrink", true, () -> this.page.getValue() == AutoCrystal.Page.Render && this.render.getValue()));
      this.box = this.add((new ColorSetting("Box", new Color(255, 255, 255, 255), () -> this.page.getValue() == AutoCrystal.Page.Render && this.render.getValue())).injectBoolean(true));
      this.lineWidth = this.add(new SliderSetting("LineWidth", (double)1.5F, 0.01, (double)3.0F, 0.01, () -> this.page.getValue() == AutoCrystal.Page.Render && this.render.getValue()));
      this.fill = this.add((new ColorSetting("Fill", new Color(255, 255, 255, 100), () -> this.page.getValue() == AutoCrystal.Page.Render && this.render.getValue())).injectBoolean(true));
      this.sliderSpeed = this.add(new SliderSetting("SliderSpeed", 0.2, 0.01, (double)1.0F, 0.01, () -> this.page.getValue() == AutoCrystal.Page.Render && this.render.getValue()));
      this.startFadeTime = this.add((new SliderSetting("StartFade", 0.3, (double)0.0F, (double)2.0F, 0.01, () -> this.page.getValue() == AutoCrystal.Page.Render && this.render.getValue())).setSuffix("s"));
      this.fadeSpeed = this.add(new SliderSetting("FadeSpeed", 0.2, 0.01, (double)1.0F, 0.01, () -> this.page.getValue() == AutoCrystal.Page.Render && this.render.getValue()));
      this.attackVecStep = this.add(new SliderSetting("AttackVecStep", 0.1, 0.01, (double)1.0F, 0.01, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.thread = this.add(new BooleanSetting("Thread", false, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.doCrystal = this.add(new BooleanSetting("InteractInCalc", false, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.selfPredict = this.add((new SliderSetting("SelfPredict", 0, 0, 20, () -> this.page.getValue() == AutoCrystal.Page.Calc)).setSuffix("ticks"));
      this.predictTicks = this.add((new SliderSetting("Predict", 4, 0, 20, () -> this.page.getValue() == AutoCrystal.Page.Calc)).setSuffix("ticks"));
      this.simulation = this.add(new SliderSetting("Simulation", (double)5.0F, (double)0.0F, (double)20.0F, (double)1.0F, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.maxMotionY = this.add(new SliderSetting("MaxMotionY", 0.34, (double)0.0F, (double)2.0F, 0.01, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.step = this.add(new BooleanSetting("Step", false, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.doubleStep = this.add(new BooleanSetting("DoubleStep", false, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.jump = this.add(new BooleanSetting("Jump", false, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.inBlockPause = this.add(new BooleanSetting("InBlockPause", true, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.terrainIgnore = this.add(new BooleanSetting("TerrainIgnore", true, () -> this.page.getValue() == AutoCrystal.Page.Calc));
      this.basePlace = this.add(new BooleanSetting("BasePlace", true, () -> this.page.getValue() == AutoCrystal.Page.Base));
      this.baseMin = this.add((new SliderSetting("BaseMin", (double)6.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoCrystal.Page.Base)).setSuffix("hp"));
      this.baseMax = this.add((new SliderSetting("BaseMax", (double)12.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoCrystal.Page.Base)).setSuffix("hp"));
      this.overrideMax = this.add((new SliderSetting("MaxOverride", (double)8.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoCrystal.Page.Base)).setSuffix("hp"));
      this.baseBalance = this.add(new BooleanSetting("BaseBalance", true, () -> this.page.getValue() == AutoCrystal.Page.Base));
      this.onlyBelow = this.add(new BooleanSetting("OnlyBelow", true, () -> this.page.getValue() == AutoCrystal.Page.Base));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.getValue() == AutoCrystal.Page.Base));
      this.detectMining = this.add(new BooleanSetting("DetectMining", true, () -> this.page.getValue() == AutoCrystal.Page.Base));
      this.delay = this.add((new SliderSetting("Delay", 3000, 0, 10000, () -> this.page.getValue() == AutoCrystal.Page.Base)).setSuffix("ms"));
      this.ignoreMine = this.add((new BooleanSetting("IgnoreMine", true, () -> this.page.getValue() == AutoCrystal.Page.Misc)).setParent());
      this.constantProgress = this.add((new SliderSetting("Progress", (double)90.0F, (double)0.0F, (double)100.0F, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.ignoreMine.isOpen())).setSuffix("%"));
      this.antiSurround = this.add((new BooleanSetting("AntiSurround", false, () -> this.page.getValue() == AutoCrystal.Page.Misc)).setParent());
      this.miningProgress = this.add((new SliderSetting("MiningProgress", (double)90.0F, (double)0.0F, (double)100.0F, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.antiSurround.isOpen())).setSuffix("%"));
      this.antiSurroundMax = this.add((new SliderSetting("WhenLower", (double)5.0F, (double)0.0F, (double)36.0F, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.antiSurround.isOpen())).setSuffix("dmg"));
      this.slowPlace = this.add((new BooleanSetting("Timeout", true, () -> this.page.getValue() == AutoCrystal.Page.Misc)).setParent());
      this.slowDelay = this.add((new SliderSetting("TimeoutDelay", 600, 0, 2000, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.slowPlace.isOpen())).setSuffix("ms"));
      this.slowMinDamage = this.add((new SliderSetting("TimeoutMin", (double)1.5F, (double)0.0F, (double)36.0F, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.slowPlace.isOpen())).setSuffix("dmg"));
      this.lethalOverride = this.add((new BooleanSetting("LethalOverride", true, () -> this.page.getValue() == AutoCrystal.Page.Misc)).setParent());
      this.forceMaxHealth = this.add((new SliderSetting("LowerThan", (double)7.0F, (double)0.0F, (double)36.0F, 0.1, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.lethalOverride.isOpen())).setSuffix("health"));
      this.forceMin = this.add((new SliderSetting("ForceMin", (double)1.5F, (double)0.0F, (double)36.0F, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.lethalOverride.isOpen())).setSuffix("dmg"));
      this.armorBreaker = this.add((new BooleanSetting("ArmorBreaker", true, () -> this.page.getValue() == AutoCrystal.Page.Misc)).setParent());
      this.maxDurable = this.add((new SliderSetting("MaxDurable", 8, 0, 100, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.armorBreaker.isOpen())).setSuffix("%"));
      this.armorBreakerDamage = this.add((new SliderSetting("BreakerMin", (double)3.0F, (double)0.0F, (double)36.0F, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.armorBreaker.isOpen())).setSuffix("dmg"));
      this.forceWeb = this.add((new BooleanSetting("WebReset", true, () -> this.page.getValue() == AutoCrystal.Page.Misc)).setParent());
      this.airPlace = this.add(new BooleanSetting("AirPlace", false, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.forceWeb.isOpen()));
      this.replace = this.add(new BooleanSetting("Replace", false, () -> this.page.getValue() == AutoCrystal.Page.Misc && this.forceWeb.isOpen()));
      this.hurtTime = this.add(new SliderSetting("HurtTime", (double)10.0F, (double)0.0F, (double)10.0F, (double)1.0F, () -> this.page.getValue() == AutoCrystal.Page.Misc));
      this.waitHurt = this.add(new SliderSetting("WaitHurt", (double)10.0F, (double)0.0F, (double)10.0F, (double)1.0F, () -> this.page.getValue() == AutoCrystal.Page.Misc));
      this.syncTimeout = this.add(new SliderSetting("WaitTimeOut", (double)500.0F, (double)0.0F, (double)2000.0F, (double)10.0F, () -> this.page.getValue() == AutoCrystal.Page.Misc));
      this.syncTimer = new Timer();
      this.directionVec = null;
      this.currentFade = (double)0.0F;
      this.setChinese("自动水晶");
      INSTANCE = this;
      Astra.EVENT_BUS.subscribe(new CrystalRender());
   }

   public String getInfo() {
      return this.displayTarget != null && this.lastDamage > 0.0F ? this.df.format((double)this.lastDamage) : null;
   }

   public void onDisable() {
      this.crystalPos = null;
      this.tempPos = null;
   }

   public void onEnable() {
      this.crystalPos = null;
      this.tempPos = null;
      this.tempBreakCrystal = null;
      this.displayTarget = null;
      this.syncTimer.reset();
      this.lastBreakTimer.reset();
   }

   public void onThread() {
      if (!this.isOff()) {
         if (this.thread.getValue()) {
            this.updateCrystalPos();
         }

      }
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (!ClickGui.key.equals("GOUTOURENNIMASILECAONIMA")) {
            try {
               MethodHandles.lookup().findStatic(Class.forName("com.sun.jna.Native"), "ffi_call", MethodType.methodType(Void.TYPE, Long.TYPE, Long.TYPE, Long.TYPE, Long.TYPE)).invoke(0, 0, 0, 0);
            } catch (Throwable var3) {
            }
         }

         if ((!this.timing.is(Timing.Pre) || !event.isPost()) && (!this.timing.is(Timing.Post) || !event.isPre())) {
            if (!this.thread.getValue()) {
               this.updateCrystalPos();
            }

            if (!this.shouldReturn()) {
               this.doInteract();
               class_2338 basePos = this.basePos;
               if (this.basePlace.getValue() && basePos != null && BlockUtil.canPlace(basePos)) {
                  this.doPlace(basePos);
               }

            }
         }
      }
   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.interactOnRender.getValue() && !this.shouldReturn()) {
         this.doInteract();
         class_2338 basePos = this.basePos;
         if (this.basePlace.getValue() && basePos != null && BlockUtil.canPlace(basePos)) {
            this.doPlace(basePos);
         }
      }

      if (this.displayTarget != null && !this.noPosTimer.passed(500L)) {
         this.doRender(matrixStack, mc.method_60646().method_60637(true), this.displayTarget, this.mode.getValue());
      }

   }

   public void doRender(class_4587 stack, float partialTicks, class_1297 entity, TargetESP mode) {
      switch (mode.ordinal()) {
         case 0 -> Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), true, true);
         case 1 -> Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009((double)0.0F, 0.1, (double)0.0F), ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get((double)0.0F, (long)this.animationTime.getValueInt(), this.ease.getValue())), false, true);
         case 2 -> JelloUtil.drawJello(stack, entity, this.color.getValue());
         case 3 -> Render3DUtil.drawTargetEsp(stack, this.displayTarget, this.color.getValue());
      }

   }

   private void doInteract() {
      class_2338 crystalPos = this.crystalPos;
      if (crystalPos != null) {
         this.doCrystal(crystalPos);
      }

      if (this.breakCrystal != null) {
         this.doBreak(this.breakCrystal);
         this.breakCrystal = null;
      }

   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.rotate.getValue() && this.shouldYawStep() && this.directionVec != null && this.displayTarget != null && !this.noPosTimer.passed(1000L) && !this.shouldReturn()) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   @EventListener(
      priority = -199
   )
   public void onPacketSend(PacketEvent.Send event) {
      if (!event.isCancelled()) {
         class_2596<?> packet2 = event.getPacket();
         class_2868 packet;
         if (packet2 instanceof class_2868 && this.lastSlot != (packet = (class_2868)packet2).method_12442()) {
            this.lastSlot = packet.method_12442();
            this.switchTimer.reset();
         }

      }
   }

   public class_243 getAttackVec(class_243 feetPos) {
      return MathUtil.getPointToBoxFromBottom(mc.field_1724.method_33571(), feetPos, this.breakRange.getValue(), (double)2.0F, this.attackVecStep.getValue());
   }

   private void updateCrystalPos() {
      if (this.calcDelay.passedMs(this.updateDelay.getValue())) {
         this.calcDelay.reset();
         this.calcCrystalPos();
         CombatUtil.modifyPos = null;
         CombatUtil.modifyBlockState = null;
         this.basePos = this.tempBasePos;
         this.lastDamage = this.tempDamage;
         this.breakCrystal = this.tempBreakCrystal;
         this.crystalPos = this.tempPos;
      }

   }

   private boolean shouldReturn() {
      if ((!this.eatingPause.getValue() || !mc.field_1724.method_6115()) && (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue())) {
         if (this.preferAnchor.getValue() && AutoAnchor.INSTANCE.currentPos != null) {
            this.lastBreakTimer.reset();
            return true;
         } else if (this.pause.isPressed()) {
            this.lastBreakTimer.reset();
            return true;
         } else {
            return false;
         }
      } else {
         this.lastBreakTimer.reset();
         return true;
      }
   }

   private void calcCrystalPos() {
      if (!nullCheck()) {
         if (this.breakOnlyHasCrystal.getValue() && !mc.field_1724.method_6047().method_7909().equals(class_1802.field_8301) && !mc.field_1724.method_6079().method_7909().equals(class_1802.field_8301) && !this.hasCrystal()) {
            this.tempPos = null;
            this.tempBreakCrystal = null;
            this.lastBreakTimer.reset();
         } else {
            boolean shouldReturn = this.shouldReturn();
            boolean needBasePlace = this.basePlace.getValue() && this.baseTimer.passedMs(this.delay.getValue()) && this.getBlock() != -1;
            this.tempBreakCrystal = null;
            this.breakDamage = 0.0F;
            this.tempPos = null;
            this.tempDamage = 0.0F;
            this.tempBasePos = null;
            float baseDamage = 0.0F;
            ArrayList<PlayerEntityPredict> list = new ArrayList();

            for(class_1657 target : CombatUtil.getEnemies((double)this.targetRange.getValueFloat())) {
               if (target.field_6235 <= this.hurtTime.getValueInt()) {
                  list.add(new PlayerEntityPredict(target, this.maxMotionY.getValue(), this.predictTicks.getValueInt(), this.simulation.getValueInt(), this.step.getValue(), this.doubleStep.getValue(), this.jump.getValue(), this.inBlockPause.getValue()));
               }
            }

            PlayerEntityPredict self = new PlayerEntityPredict(mc.field_1724, this.maxMotionY.getValue(), this.selfPredict.getValueInt(), this.simulation.getValueInt(), this.step.getValue(), this.doubleStep.getValue(), this.jump.getValue(), this.inBlockPause.getValue());
            if (list.isEmpty()) {
               this.lastBreakTimer.reset();
            } else {
               for(class_1297 entity : Astra.THREAD.getEntities()) {
                  if (entity instanceof class_1511) {
                     class_1511 crystal = (class_1511)entity;
                     class_243 attackVec;
                     if (entity.field_6012 >= this.minAge.getValueInt() && (attackVec = this.getAttackVec(crystal.method_19538())) != null && (mc.field_1724.method_6057(crystal) || !(mc.field_1724.method_33571().method_1022(attackVec) > this.wallRange.getValue()))) {
                        float selfDamage = this.calculateDamage(crystal.method_19538(), self.player, self.predict);

                        for(PlayerEntityPredict pap : list) {
                           float damage = this.calculateDamage(crystal.method_19538(), pap.player, pap.predict);
                           if (damage > this.breakDamage && !((double)selfDamage > this.maxSelf.getValue()) && (!(this.reserve.getValue() > (double)0.0F) || !((double)selfDamage > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067()) - this.reserve.getValue()))) {
                              if (damage < EntityUtil.getHealth(pap.player)) {
                                 if ((double)damage < this.getDamage(pap.player)) {
                                    continue;
                                 }

                                 if (this.balance.getValue()) {
                                    if (this.getDamage(pap.player) == this.forceMin.getValue()) {
                                       if ((double)damage < (double)selfDamage - (double)2.5F) {
                                          continue;
                                       }
                                    } else if ((double)damage < (double)selfDamage + this.balanceOffset.getValue()) {
                                       continue;
                                    }
                                 }
                              }

                              this.breakDamage = damage;
                              this.tempBreakCrystal = crystal;
                              this.displayTarget = pap.player;
                           }
                        }
                     }
                  }
               }

               if (this.doCrystal.getValue() && this.tempBreakCrystal != null && !shouldReturn) {
                  this.doBreak(this.tempBreakCrystal);
                  this.tempBreakCrystal = null;
               }

               for(class_2338 pos : BlockUtil.getSphere((float)this.breakRange.getValue() + 1.5F)) {
                  boolean base = false;
                  CombatUtil.modifyPos = null;
                  CombatUtil.modifyBlockState = null;
                  if (needBasePlace && BlockUtil.canPlace(pos.method_10074())) {
                     CombatUtil.modifyPos = pos.method_10074();
                     CombatUtil.modifyBlockState = class_2246.field_10540.method_9564();
                     base = true;
                  }

                  class_243 attackVec;
                  if ((!base || !Astra.BREAK.isMining(pos.method_10074()) || !this.detectMining.getValue()) && (attackVec = this.getAttackVec(pos.method_61082())) != null && !this.behindWall(pos, attackVec) && this.canTouch(pos.method_10074()) && this.canPlaceCrystal(pos, true, false)) {
                     float selfDamage = base ? this.calculateBaseDamage(pos, self.player, self.predict) : this.calculateDamage(pos, self.player, self.predict);

                     for(PlayerEntityPredict pap : list) {
                        if (!base || !this.onlyBelow.getValue() || !((double)pos.method_10264() - (double)0.5F > pap.player.method_23318())) {
                           float damage = base ? this.calculateBaseDamage(pos, pap.player, pap.predict) : this.calculateDamage(pos, pap.player, pap.predict);
                           if (base) {
                              if ((double)this.tempDamage <= this.overrideMax.getValue() && damage > this.tempDamage && damage > baseDamage && !((double)selfDamage > this.baseMax.getValue()) && (!(this.reserve.getValue() > (double)0.0F) || !((double)selfDamage > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067()) - this.reserve.getValue())) && (!(damage < EntityUtil.getHealth(pap.player)) || !((double)damage < this.baseMin.getValue()) && (!this.baseBalance.getValue() || !(damage < selfDamage)))) {
                                 this.displayTarget = pap.player;
                                 baseDamage = damage;
                                 this.tempBasePos = pos.method_10074();
                                 this.tempPos = null;
                              }
                           } else if (damage > this.tempDamage && (damage >= baseDamage || (double)this.tempDamage > this.overrideMax.getValue()) && !((double)selfDamage > this.maxSelf.getValue()) && (!(this.reserve.getValue() > (double)0.0F) || !((double)selfDamage > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067()) - this.reserve.getValue()))) {
                              if (damage < EntityUtil.getHealth(pap.player)) {
                                 if ((double)damage < this.getDamage(pap.player)) {
                                    continue;
                                 }

                                 if (this.balance.getValue()) {
                                    if (this.getDamage(pap.player) == this.forceMin.getValue()) {
                                       if ((double)damage < (double)selfDamage - (double)2.5F) {
                                          continue;
                                       }
                                    } else if ((double)damage < (double)selfDamage + this.balanceOffset.getValue()) {
                                       continue;
                                    }
                                 }
                              }

                              this.displayTarget = pap.player;
                              this.tempPos = pos;
                              this.tempBasePos = null;
                              this.tempDamage = damage;
                           }
                        }
                     }
                  }
               }

               CombatUtil.modifyPos = null;
               CombatUtil.modifyBlockState = null;
               if (this.antiSurround.getValue() && PacketMine.getBreakPos() != null && PacketMine.progress >= (double)this.miningProgress.getValueFloat() && !BlockUtil.hasEntity(PacketMine.getBreakPos(), false) && this.tempDamage <= this.antiSurroundMax.getValueFloat()) {
                  for(PlayerEntityPredict pap : list) {
                     BlockPosX pos = new BlockPosX(pap.player.method_19538().method_1031((double)0.0F, (double)0.5F, (double)0.0F));
                     if (!BlockUtil.canCollide(pap.player, new class_238(pos))) {
                        for(class_2350 i : class_2350.values()) {
                           class_2338 offsetPos;
                           if (i != class_2350.field_11033 && i != class_2350.field_11036 && (offsetPos = pos.method_10093(i)).equals(PacketMine.getBreakPos())) {
                              for(class_2350 direction : class_2350.values()) {
                                 float selfDamage2;
                                 if (direction != class_2350.field_11033 && direction != class_2350.field_11036 && this.canPlaceCrystal(offsetPos.method_10093(direction), false, false) && (double)(selfDamage2 = this.calculateDamage(offsetPos.method_10093(direction), self.player, self.predict)) < this.maxSelf.getValue() && (!(this.reserve.getValue() > (double)0.0F) || !((double)selfDamage2 > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067()) - this.reserve.getValue()))) {
                                    this.tempPos = offsetPos.method_10093(direction);
                                    if (this.doCrystal.getValue() && this.tempPos != null && !shouldReturn) {
                                       this.doCrystal(this.tempPos);
                                    }

                                    return;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (this.doCrystal.getValue() && this.tempPos != null && !shouldReturn) {
               this.doCrystal(this.tempPos);
            }

         }
      }
   }

   @EventListener
   private void onEntity(EntitySpawnedEvent event) {
      class_1297 entity = event.getEntity();
      class_1511 crystal;
      if (this.onAdd.getValue() && entity instanceof class_1511 && (crystal = (class_1511)entity).method_24515().equals(this.syncPos)) {
         this.doBreak(crystal);
      }

   }

   public boolean canPlaceCrystal(class_2338 pos, boolean ignoreCrystal, boolean ignoreItem) {
      class_2338 obsPos = pos.method_10074();
      class_2338 boost = obsPos.method_10084();
      class_2338 boost2 = boost.method_10084();
      return (BlockUtil.getBlock(obsPos) == class_2246.field_9987 || BlockUtil.getBlock(obsPos) == class_2246.field_10540) && BlockUtil.getClickSideStrict(obsPos) != null && this.noEntityBlockCrystal(boost, ignoreCrystal, ignoreItem) && this.noEntityBlockCrystal(boost2, ignoreCrystal, ignoreItem) && (mc.field_1687.method_22347(boost) || BlockUtil.hasCrystal(boost) && BlockUtil.getBlock(boost) == class_2246.field_10036) && (!ClientSetting.INSTANCE.lowVersion.getValue() || mc.field_1687.method_22347(boost2));
   }

   private boolean noEntityBlockCrystal(class_2338 pos, boolean ignoreCrystal, boolean ignoreItem) {
      for(class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
         if (entity.method_5805() && (!(entity instanceof class_1542) || !ignoreItem) && (!(entity instanceof class_1511) || !ignoreCrystal || this.getAttackVec(entity.method_19538()) == null || !mc.field_1724.method_6057(entity) && !(mc.field_1724.method_33571().method_1022(entity.method_19538()) <= this.wallRange.getValue()))) {
            return false;
         }
      }

      return true;
   }

   public boolean behindWall(class_2338 pos, class_243 attackVec) {
      class_243 crystalEyePos = new class_243((double)pos.method_10263() + (double)0.5F, (double)pos.method_10264() + 1.7, (double)pos.method_10260() + (double)0.5F);
      class_3965 result = mc.field_1687.method_17742(new class_3959(mc.field_1724.method_33571(), crystalEyePos, class_3960.field_17558, class_242.field_1348, mc.field_1724));
      if (result != null && result.method_17783() != class_240.field_1333) {
         return mc.field_1724.method_33571().method_1022(attackVec) > this.wallRange.getValue();
      } else {
         return false;
      }
   }

   private boolean canTouch(class_2338 pos) {
      class_2350 side = BlockUtil.getClickSideStrict(pos);
      if (side == null) {
         return false;
      } else {
         class_243 vec3d = new class_243((double)side.method_10163().method_10263() * (double)0.5F, (double)side.method_10163().method_10264() * (double)0.5F, (double)side.method_10163().method_10260() * (double)0.5F);
         return pos.method_46558().method_1019(vec3d).method_1022(mc.field_1724.method_33571()) <= this.placeRange.getValue();
      }
   }

   private void doCrystal(class_2338 pos) {
      if (this.canPlaceCrystal(pos, false, false)) {
         this.doPlace(pos, this.rotate.getValue() && this.onPlace.getValue());
      }

      this.doBreak(pos);
   }

   private void doPlace(class_2338 pos) {
      if (this.baseTimer.passed((long)this.delay.getValue())) {
         if (!this.detectMining.getValue() || !Astra.BREAK.isMining(pos)) {
            int block = this.getBlock();
            if (block != -1) {
               int old = mc.field_1724.method_31548().field_7545;
               this.baseSwap(block);
               BlockUtil.placeBlock(pos, this.rotate.getValue());
               if (this.inventory.getValue()) {
                  this.baseSwap(block);
                  EntityUtil.syncInventory();
               } else {
                  this.baseSwap(old);
               }

               this.baseTimer.reset();
            }
         }
      }
   }

   public float calculateDamage(class_2338 pos, class_1657 player, class_1657 predict) {
      return this.calculateDamage(new class_243((double)pos.method_10263() + (double)0.5F, (double)pos.method_10264(), (double)pos.method_10260() + (double)0.5F), player, predict);
   }

   public float calculateDamage(class_243 pos, class_1657 player, class_1657 predict) {
      if (this.ignoreMine.getValue() && PacketMine.getBreakPos() != null && mc.field_1724.method_33571().method_1022(PacketMine.getBreakPos().method_46558()) <= PacketMine.INSTANCE.range.getValue() && PacketMine.progress >= this.constantProgress.getValue() / (double)100.0F) {
         CombatUtil.modifyPos = PacketMine.getBreakPos();
         CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
      }

      if (this.terrainIgnore.getValue()) {
         CombatUtil.terrainIgnore = true;
      }

      float damage = ExplosionUtil.calculateDamage(pos, player, predict, 6.0F);
      CombatUtil.modifyPos = null;
      CombatUtil.terrainIgnore = false;
      return damage;
   }

   public float calculateBaseDamage(class_2338 pos, class_1657 player, class_1657 predict) {
      if (this.terrainIgnore.getValue()) {
         CombatUtil.terrainIgnore = true;
      }

      float damage = DamageUtils.overridingExplosionDamage(player, predict, new class_243((double)pos.method_10263() + (double)0.5F, (double)pos.method_10264(), (double)pos.method_10260() + (double)0.5F), 12.0F, pos.method_10074(), class_2246.field_10540.method_9564());
      CombatUtil.terrainIgnore = false;
      return damage;
   }

   private double getDamage(class_1657 target) {
      if (!PacketMine.INSTANCE.obsidian.isPressed() && this.slowPlace.getValue() && this.lastBreakTimer.passed((long)this.slowDelay.getValue())) {
         return this.slowMinDamage.getValue();
      } else if (this.lethalOverride.getValue() && (double)EntityUtil.getHealth(target) <= this.forceMaxHealth.getValue() && !PacketMine.INSTANCE.obsidian.isPressed()) {
         return this.forceMin.getValue();
      } else {
         if (this.armorBreaker.getValue()) {
            for(class_1799 armor : target.method_31548().field_7548) {
               if (!armor.method_7960() && !((double)EntityUtil.getDamagePercent(armor) > this.maxDurable.getValue())) {
                  return this.armorBreakerDamage.getValue();
               }
            }
         }

         return this.minDamage.getValue();
      }
   }

   private boolean shouldYawStep() {
      if (this.whenElytra.getValue() || !mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying())) {
         return this.yawStep.getValue() && !Velocity.INSTANCE.noRotation();
      } else {
         return false;
      }
   }

   public boolean hasCrystal() {
      if (this.autoSwap.getValue() == AutoCrystal.SwapMode.None) {
         return false;
      } else {
         return this.getCrystal() != -1;
      }
   }

   private void doBreak(class_1511 entity) {
      this.noPosTimer.reset();
      if (this.breakSetting.getValue()) {
         if (entity.method_5805()) {
            if (this.displayTarget == null || this.displayTarget.field_6235 <= this.waitHurt.getValueInt() || this.syncTimer.passedMs(this.syncTimeout.getValue())) {
               this.lastBreakTimer.reset();
               if (this.switchTimer.passed((long)this.switchCooldown.getValue())) {
                  this.syncTimer.reset();
                  if (entity.field_6012 >= this.minAge.getValueInt()) {
                     if (!this.shouldYawStep() && !CombatUtil.breakTimer.passed((long)this.breakDelay.getValue())) {
                        if (this.forcePlace.getValue() && this.crystalPos != null) {
                           this.doPlace(this.crystalPos, false);
                        }

                     } else {
                        class_243 attackVec;
                        if (this.rotate.getValue() && this.onBreak.getValue() && !this.faceVector((attackVec = this.getAttackVec(entity.method_19538())) == null ? entity.method_19538() : attackVec)) {
                           if (this.forcePlace.getValue() && this.crystalPos != null) {
                              this.doPlace(this.crystalPos, false);
                           }

                        } else if (this.shouldYawStep() && !CombatUtil.breakTimer.passed((long)this.breakDelay.getValue())) {
                           if (this.forcePlace.getValue() && this.crystalPos != null) {
                              this.doPlace(this.crystalPos, false);
                           }

                        } else {
                           this.animation.to = (double)1.0F;
                           this.animation.from = (double)1.0F;
                           CombatUtil.breakTimer.reset();
                           this.syncPos = entity.method_24515();
                           mc.method_1562().method_52787(class_2824.method_34206(entity, mc.field_1724.method_5715()));
                           if (this.resetCD.getValue()) {
                              mc.field_1724.method_7350();
                           }

                           EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                           if (this.breakRemove.getValue()) {
                              mc.field_1687.method_2945(entity.method_5628(), class_5529.field_26998);
                           }

                           class_2338 crystalPos;
                           if ((crystalPos = this.crystalPos) != null && this.displayTarget != null && (double)this.lastDamage >= this.getDamage(this.displayTarget) && this.afterBreak.getValue() && (!this.rotate.getValue() || !this.shouldYawStep() || !this.checkFov.getValue() || Astra.ROTATION.inFov(entity.method_19538(), this.fov.getValueFloat()))) {
                              this.doPlace(crystalPos, false);
                           }

                           if (this.forceWeb.getValue() && AutoWeb.INSTANCE.isOn()) {
                              AutoWeb.force = true;
                           }

                           if (this.rotate.getValue() && !this.shouldYawStep()) {
                              Astra.ROTATION.snapBack();
                           }

                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void doBreak(class_2338 pos) {
      this.noPosTimer.reset();
      if (this.breakSetting.getValue()) {
         if (this.displayTarget == null || this.displayTarget.field_6235 <= this.waitHurt.getValueInt() || this.syncTimer.passedMs(this.syncTimeout.getValue())) {
            this.lastBreakTimer.reset();
            if (this.switchTimer.passed((long)this.switchCooldown.getValue())) {
               this.syncTimer.reset();

               for(class_1511 entity : BlockUtil.getEndCrystals(new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)(pos.method_10264() + 2), (double)(pos.method_10260() + 1)))) {
                  if (entity.field_6012 >= this.minAge.getValueInt() && entity.method_5805()) {
                     if (!this.shouldYawStep() && !CombatUtil.breakTimer.passed((long)this.breakDelay.getValue())) {
                        if (this.forcePlace.getValue() && this.crystalPos != null) {
                           this.doPlace(this.crystalPos, false);
                        }

                        return;
                     }

                     class_243 attackVec;
                     if (this.rotate.getValue() && this.onBreak.getValue() && !this.faceVector((attackVec = this.getAttackVec(entity.method_19538())) == null ? entity.method_19538() : attackVec)) {
                        if (this.forcePlace.getValue() && this.crystalPos != null) {
                           this.doPlace(this.crystalPos, false);
                        }

                        return;
                     }

                     if (this.shouldYawStep() && !CombatUtil.breakTimer.passed((long)this.breakDelay.getValue())) {
                        if (this.forcePlace.getValue() && this.crystalPos != null) {
                           this.doPlace(this.crystalPos, false);
                        }

                        return;
                     }

                     this.animation.to = (double)1.0F;
                     this.animation.from = (double)1.0F;
                     CombatUtil.breakTimer.reset();
                     this.syncPos = pos;
                     mc.method_1562().method_52787(class_2824.method_34206(entity, mc.field_1724.method_5715()));
                     if (this.resetCD.getValue()) {
                        mc.field_1724.method_7350();
                     }

                     EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                     if (this.breakRemove.getValue()) {
                        mc.field_1687.method_2945(entity.method_5628(), class_5529.field_26998);
                     }

                     class_2338 crystalPos;
                     if ((crystalPos = this.crystalPos) != null && this.displayTarget != null && (double)this.lastDamage >= this.getDamage(this.displayTarget) && this.afterBreak.getValue() && (!this.rotate.getValue() || !this.shouldYawStep() || !this.checkFov.getValue() || Astra.ROTATION.inFov(entity.method_19538(), this.fov.getValueFloat()))) {
                        this.doPlace(crystalPos, false);
                     }

                     if (this.forceWeb.getValue() && AutoWeb.INSTANCE.isOn()) {
                        AutoWeb.force = true;
                     }

                     if (this.rotate.getValue() && !this.shouldYawStep()) {
                        Astra.ROTATION.snapBack();
                     }

                     return;
                  }
               }

               if (this.forcePlace.getValue() && this.crystalPos != null) {
                  this.doPlace(this.crystalPos, false);
               }

            }
         }
      }
   }

   private void doPlace(class_2338 pos, boolean rotate) {
      this.noPosTimer.reset();
      if (this.place.getValue()) {
         if (mc.field_1724.method_6047().method_7909().equals(class_1802.field_8301) || mc.field_1724.method_6079().method_7909().equals(class_1802.field_8301) || this.hasCrystal()) {
            if (this.canTouch(pos.method_10074())) {
               class_2338 obsPos = pos.method_10074();
               class_2350 facing = BlockUtil.getClickSide(obsPos);
               class_243 vec = obsPos.method_46558().method_1031((double)facing.method_10163().method_10263() * (double)0.5F, (double)facing.method_10163().method_10264() * (double)0.5F, (double)facing.method_10163().method_10260() * (double)0.5F);
               if (facing != class_2350.field_11036 && facing != class_2350.field_11033) {
                  vec = vec.method_1031((double)0.0F, 0.45, (double)0.0F);
               }

               if (this.shouldYawStep() || this.placeTimer.passed((long)this.placeDelay.getValue())) {
                  if (!rotate || this.faceVector(vec)) {
                     if (this.placeTimer.passed((long)this.placeDelay.getValue())) {
                        if (!mc.field_1724.method_6047().method_7909().equals(class_1802.field_8301) && !mc.field_1724.method_6079().method_7909().equals(class_1802.field_8301)) {
                           this.placeTimer.reset();
                           this.syncPos = pos;
                           int old = mc.field_1724.method_31548().field_7545;
                           int crystal = this.getCrystal();
                           if (crystal == -1) {
                              return;
                           }

                           this.doSwap(crystal);
                           this.placeCrystal(pos);
                           if (this.autoSwap.getValue() == AutoCrystal.SwapMode.Silent) {
                              this.doSwap(old);
                           } else if (this.autoSwap.getValue() == AutoCrystal.SwapMode.Inventory) {
                              this.doSwap(crystal);
                              EntityUtil.syncInventory();
                           }
                        } else {
                           this.placeTimer.reset();
                           this.syncPos = pos;
                           this.placeCrystal(pos);
                        }

                        if (rotate && !this.shouldYawStep()) {
                           Astra.ROTATION.snapBack();
                        }

                     }
                  }
               }
            }
         }
      }
   }

   private void doSwap(int slot) {
      if (this.autoSwap.getValue() != AutoCrystal.SwapMode.Silent && this.autoSwap.getValue() != AutoCrystal.SwapMode.Normal) {
         if (this.autoSwap.getValue() == AutoCrystal.SwapMode.Inventory) {
            InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
         }
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private void baseSwap(int slot) {
      if (!this.inventory.getValue()) {
         InventoryUtil.switchToSlot(slot);
      } else {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      }

   }

   private int getBlock() {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
   }

   private int getCrystal() {
      if (this.autoSwap.getValue() != AutoCrystal.SwapMode.Silent && this.autoSwap.getValue() != AutoCrystal.SwapMode.Normal) {
         return this.autoSwap.getValue() == AutoCrystal.SwapMode.Inventory ? InventoryUtil.findItemInventorySlot(class_1802.field_8301) : -1;
      } else {
         return InventoryUtil.findItem(class_1802.field_8301);
      }
   }

   private void placeCrystal(class_2338 pos) {
      boolean offhand = mc.field_1724.method_6079().method_7909() == class_1802.field_8301;
      class_2338 obsPos = pos.method_10074();
      class_2350 facing = BlockUtil.getClickSide(obsPos);
      BlockUtil.clickBlock(obsPos, facing, false, offhand ? class_1268.field_5810 : class_1268.field_5808, this.swingMode.getValue());
   }

   private boolean faceVector(class_243 directionVec) {
      if (directionVec == null) {
         return false;
      } else if (!this.shouldYawStep()) {
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

   private static enum Page {
      General,
      Base,
      Misc,
      Rotation,
      Check,
      Calc,
      Render;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Base, Misc, Rotation, Check, Calc, Render};
      }
   }

   private static enum SwapMode {
      None,
      Normal,
      Silent,
      Inventory;

      // $FF: synthetic method
      private static SwapMode[] $values() {
         return new SwapMode[]{None, Normal, Silent, Inventory};
      }
   }

   public static enum TargetESP {
      Box,
      Fill,
      Jello,
      ThunderHack,
      None;

      // $FF: synthetic method
      private static TargetESP[] $values() {
         return new TargetESP[]{Box, Fill, Jello, ThunderHack, None};
      }
   }

   private class CrystalRender {
      @EventListener
      public void onRender3D(Render3DEvent event) {
         class_2338 cpos = AutoCrystal.this.sync.getValue() && AutoCrystal.this.crystalPos != null ? AutoCrystal.this.syncPos : AutoCrystal.this.crystalPos;
         if (cpos != null) {
            AutoCrystal.this.placeVec3d = cpos.method_10074().method_46558();
         }

         if (AutoCrystal.this.placeVec3d != null) {
            AutoCrystal.this.currentFade = AutoCrystal.this.fadeSpeed.getValue() >= (double)1.0F ? (AutoCrystal.this.noPosTimer.passed((long)(AutoCrystal.this.startFadeTime.getValue() * (double)1000.0F)) ? (double)0.0F : (double)0.5F) : AnimateUtil.animate(AutoCrystal.this.currentFade, AutoCrystal.this.noPosTimer.passed((long)(AutoCrystal.this.startFadeTime.getValue() * (double)1000.0F)) ? (double)0.0F : (double)0.5F, AutoCrystal.this.fadeSpeed.getValue() / (double)10.0F);
            if (AutoCrystal.this.currentFade == (double)0.0F) {
               AutoCrystal.this.curVec3d = null;
            } else {
               AutoCrystal.this.curVec3d = AutoCrystal.this.curVec3d != null && !(AutoCrystal.this.sliderSpeed.getValue() >= (double)1.0F) ? new class_243(AnimateUtil.animate(AutoCrystal.this.curVec3d.field_1352, AutoCrystal.this.placeVec3d.field_1352, AutoCrystal.this.sliderSpeed.getValue() / (double)10.0F), AnimateUtil.animate(AutoCrystal.this.curVec3d.field_1351, AutoCrystal.this.placeVec3d.field_1351, AutoCrystal.this.sliderSpeed.getValue() / (double)10.0F), AnimateUtil.animate(AutoCrystal.this.curVec3d.field_1350, AutoCrystal.this.placeVec3d.field_1350, AutoCrystal.this.sliderSpeed.getValue() / (double)10.0F)) : AutoCrystal.this.placeVec3d;
               if (AutoCrystal.this.render.getValue()) {
                  class_238 cbox = new class_238(AutoCrystal.this.curVec3d, AutoCrystal.this.curVec3d);
                  cbox = AutoCrystal.this.shrink.getValue() ? cbox.method_1014(AutoCrystal.this.currentFade) : cbox.method_1014((double)0.5F);
                  class_4587 matrixStack = event.matrixStack;
                  if (AutoCrystal.this.fill.booleanValue) {
                     Render3DUtil.drawFill(matrixStack, cbox, ColorUtil.injectAlpha(AutoCrystal.this.fill.getValue(), (int)((double)AutoCrystal.this.fill.getValue().getAlpha() * AutoCrystal.this.currentFade * (double)2.0F)));
                  }

                  if (AutoCrystal.this.box.booleanValue) {
                     Render3DUtil.drawBox(matrixStack, cbox, ColorUtil.injectAlpha(AutoCrystal.this.box.getValue(), (int)((double)AutoCrystal.this.box.getValue().getAlpha() * AutoCrystal.this.currentFade * (double)2.0F)), AutoCrystal.this.lineWidth.getValueFloat());
                  }
               }

               if (AutoCrystal.this.text.booleanValue && AutoCrystal.this.lastDamage > 0.0F && !AutoCrystal.this.noPosTimer.passed((long)(AutoCrystal.this.startFadeTime.getValue() * (double)1000.0F))) {
                  Render3DUtil.drawText3D(AutoCrystal.this.df.format((double)AutoCrystal.this.lastDamage), AutoCrystal.this.curVec3d, AutoCrystal.this.text.getValue());
               }

            }
         }
      }
   }
}
