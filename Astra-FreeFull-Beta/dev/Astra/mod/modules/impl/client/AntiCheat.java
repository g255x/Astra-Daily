package dev.Astra.mod.modules.impl.client;

import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.enums.Placement;
import dev.Astra.mod.modules.settings.enums.SnapBack;
import dev.Astra.mod.modules.settings.enums.SwingSide;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;

public class AntiCheat extends Module {
   public static AntiCheat INSTANCE;
   public final EnumSetting<Page> page;
   public final BooleanSetting attackCDFix;
   public final BooleanSetting multiPlace;
   public final BooleanSetting packetPlace;
   public final BooleanSetting attackRotate;
   public final BooleanSetting invSwapBypass;
   public final BooleanSetting priorHotbar;
   public final BooleanSetting movementSync;
   public final BooleanSetting noBadSlot;
   public final SliderSetting ieRange;
   public final SliderSetting boxSize;
   public final SliderSetting attackDelay;
   public final EnumSetting<Placement> placement;
   public final BooleanSetting upDirectionLimit;
   public final EnumSetting<SwingSide> interactSwing;
   public final EnumSetting<SwingSide> attackSwing;
   public final BooleanSetting grimRotation;
   public final EnumSetting<SnapBack> snapBackEnum;
   public final BooleanSetting look;
   public final SliderSetting rotateTime;
   public final BooleanSetting random;
   public final SliderSetting steps;
   public final BooleanSetting serverSide;
   public final BooleanSetting fullPackets;
   public final BooleanSetting force;
   public final BooleanSetting forceSync;
   public final BooleanSetting interactRotation;
   public final BooleanSetting detectDouble;
   public final SliderSetting doubleMineTimeout;
   public final SliderSetting minTimeout;
   public final SliderSetting breakTimeout;
   public final BooleanSetting ignoreArmorStand;
   public final BooleanSetting closeScreen;
   public final EnumSetting<Motion> motion;
   public final SliderSetting predictTicks;
   public final SliderSetting simulation;
   public final SliderSetting maxMotionY;
   public final BooleanSetting step;
   public final BooleanSetting doubleStep;
   public final BooleanSetting jump;
   public final BooleanSetting inBlockPause;

   public AntiCheat() {
      super("AntiCheat", Module.Category.Client);
      this.page = this.add(new EnumSetting("Page", AntiCheat.Page.General));
      this.attackCDFix = this.add(new BooleanSetting("TrueAttackCD", false, () -> this.page.is(AntiCheat.Page.General)));
      this.multiPlace = this.add(new BooleanSetting("MultiPlace", true, () -> this.page.is(AntiCheat.Page.General)));
      this.packetPlace = this.add(new BooleanSetting("PacketPlace", true, () -> this.page.is(AntiCheat.Page.General)));
      this.attackRotate = this.add(new BooleanSetting("AttackRotation", false, () -> this.page.is(AntiCheat.Page.General)));
      this.invSwapBypass = this.add(new BooleanSetting("PickSwap", false, () -> this.page.is(AntiCheat.Page.General)));
      this.priorHotbar = this.add(new BooleanSetting("PriorHotbar", false, () -> this.page.is(AntiCheat.Page.General)));
      this.movementSync = this.add(new BooleanSetting("MovementSync", true, () -> this.page.is(AntiCheat.Page.General)));
      this.noBadSlot = this.add(new BooleanSetting("NoBadSlot", false, () -> this.page.is(AntiCheat.Page.General)));
      this.ieRange = this.add((new SliderSetting("InteractEntityRange", (double)3.0F, (double)0.0F, (double)8.0F, 0.1, () -> this.page.is(AntiCheat.Page.General))).setSuffix("m"));
      this.boxSize = this.add(new SliderSetting("HitBoxSize", 0.6, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(AntiCheat.Page.General)));
      this.attackDelay = this.add((new SliderSetting("BreakDelay", 0.2, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(AntiCheat.Page.General))).setSuffix("s"));
      this.placement = this.add(new EnumSetting("Placement", Placement.Vanilla, () -> this.page.is(AntiCheat.Page.General)));
      this.upDirectionLimit = this.add(new BooleanSetting("UPDirectionLimit", true, () -> this.page.is(AntiCheat.Page.General) && this.placement.is(Placement.NCP)));
      this.interactSwing = this.add(new EnumSetting("InteractSwing", SwingSide.All, () -> this.page.is(AntiCheat.Page.General)));
      this.attackSwing = this.add(new EnumSetting("AttackSwing", SwingSide.All, () -> this.page.is(AntiCheat.Page.General)));
      this.grimRotation = this.add(new BooleanSetting("GrimRotation", false, () -> this.page.is(AntiCheat.Page.Rotation)));
      this.snapBackEnum = this.add(new EnumSetting("SnapBack", SnapBack.None, () -> this.page.is(AntiCheat.Page.Rotation)));
      this.look = this.add(new BooleanSetting("Look", true, () -> this.page.is(AntiCheat.Page.Rotation)));
      this.rotateTime = this.add((new SliderSetting("LookTime", (double)0.5F, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(AntiCheat.Page.Rotation))).setSuffix("s"));
      this.random = this.add(new BooleanSetting("Random", true, () -> this.page.is(AntiCheat.Page.Rotation)));
      this.steps = this.add(new SliderSetting("Steps", 0.6, (double)0.0F, (double)1.0F, 0.01, () -> this.page.is(AntiCheat.Page.Rotation)));
      this.serverSide = this.add(new BooleanSetting("ServerSide", false, () -> this.page.is(AntiCheat.Page.Rotation)));
      this.fullPackets = this.add((new BooleanSetting("FullPackets", false, () -> this.page.is(AntiCheat.Page.Rotation))).setParent());
      this.force = this.add(new BooleanSetting("AlwaysSend", false, () -> this.page.is(AntiCheat.Page.Rotation) && this.fullPackets.isOpen()));
      this.forceSync = this.add(new BooleanSetting("ForceSync", true, () -> this.page.is(AntiCheat.Page.Rotation)));
      this.interactRotation = this.add(new BooleanSetting("InteractRotation", false, () -> this.page.is(AntiCheat.Page.Rotation)));
      this.detectDouble = this.add(new BooleanSetting("DetectDouble", true, () -> this.page.is(AntiCheat.Page.Misc)));
      this.doubleMineTimeout = this.add((new SliderSetting("DoubleTimeout", (double)2.0F, (double)0.0F, (double)3.0F, 0.1, () -> this.page.is(AntiCheat.Page.Misc))).setSuffix("s"));
      this.minTimeout = this.add((new SliderSetting("MinTimeout", (double)2.0F, (double)0.0F, (double)10.0F, 0.1, () -> this.page.is(AntiCheat.Page.Misc))).setSuffix("s"));
      this.breakTimeout = this.add((new SliderSetting("BreakFailed", (double)1.5F, (double)0.0F, (double)3.0F, 0.1, () -> this.page.is(AntiCheat.Page.Misc))).setSuffix("s"));
      this.ignoreArmorStand = this.add(new BooleanSetting("IgnoreArmorStand", false, () -> this.page.is(AntiCheat.Page.Misc)));
      this.closeScreen = this.add(new BooleanSetting("CloseScreen", false, () -> this.page.is(AntiCheat.Page.Misc)));
      this.motion = this.add(new EnumSetting("Motion", AntiCheat.Motion.Position, () -> this.page.getValue() == AntiCheat.Page.Predict));
      this.predictTicks = this.add((new SliderSetting("Predict", 4, 0, 10, () -> this.page.getValue() == AntiCheat.Page.Predict)).setSuffix("ticks"));
      this.simulation = this.add(new SliderSetting("Simulation", (double)5.0F, (double)0.0F, (double)20.0F, (double)1.0F, () -> this.page.getValue() == AntiCheat.Page.Predict));
      this.maxMotionY = this.add(new SliderSetting("MaxMotionY", 0.34, (double)0.0F, (double)2.0F, 0.01, () -> this.page.getValue() == AntiCheat.Page.Predict));
      this.step = this.add(new BooleanSetting("Step", false, () -> this.page.getValue() == AntiCheat.Page.Predict));
      this.doubleStep = this.add(new BooleanSetting("DoubleStep", false, () -> this.page.getValue() == AntiCheat.Page.Predict));
      this.jump = this.add(new BooleanSetting("Jump", false, () -> this.page.getValue() == AntiCheat.Page.Predict));
      this.inBlockPause = this.add(new BooleanSetting("InBlockPause", true, () -> this.page.getValue() == AntiCheat.Page.Predict));
      this.setChinese("反作弊选项");
      INSTANCE = this;
   }

   public boolean movementSync() {
      return this.movementSync.getValue();
   }

   public static double getOffset() {
      return INSTANCE != null ? INSTANCE.boxSize.getValue() / (double)2.0F : 0.3;
   }

   public void enable() {
      this.state = true;
   }

   public void disable() {
      this.state = true;
   }

   public boolean isOn() {
      return true;
   }

   public static enum Page {
      General,
      Rotation,
      Misc,
      Predict;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Rotation, Misc, Predict};
      }
   }

   public static enum Motion {
      Velocity,
      Position;

      // $FF: synthetic method
      private static Motion[] $values() {
         return new Motion[]{Velocity, Position};
      }
   }
}
