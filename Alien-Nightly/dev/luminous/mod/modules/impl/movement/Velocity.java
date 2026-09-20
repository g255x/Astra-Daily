package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.EntityVelocityUpdateEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1536;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_2708;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2350.class_2352;
import net.minecraft.class_2846.class_2847;

public class Velocity extends Module {
   public static Velocity INSTANCE;
   private final EnumSetting<Mode> mode;
   private final SliderSetting lagPause;
   public final BooleanSetting ignorePearlLag;
   private final SliderSetting phaseTime;
   public final BooleanSetting noRotation;
   public final BooleanSetting flagInWall;
   public final BooleanSetting whenPushOutOfBlocks;
   public final BooleanSetting staticSetting;
   public final BooleanSetting cancelAll;
   private final SliderSetting horizontal;
   private final SliderSetting vertical;
   public final BooleanSetting whileLiquid;
   public final BooleanSetting whileElytra;
   public final BooleanSetting noClimb;
   public final BooleanSetting waterPush;
   public final BooleanSetting entityPush;
   public final BooleanSetting blockPush;
   public final BooleanSetting fishBob;
   public final Timer pearlTimer;
   private final Timer lagBackTimer;
   private boolean flag;
   static boolean pushOutOfBlocks = false;

   public Velocity() {
      super("Velocity", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", Velocity.Mode.Plain));
      this.lagPause = this.add(new SliderSetting("LagPause", 50, 0, 500, () -> this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.Wall)));
      this.ignorePearlLag = this.add((new BooleanSetting("IgnorePearlLag", true, () -> this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.Wall))).setParent());
      this.phaseTime = this.add(new SliderSetting("PhaseTime", 250, 0, 1000, () -> (this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.Wall)) && this.ignorePearlLag.isOpen()));
      this.noRotation = this.add(new BooleanSetting("NoRotation", false, () -> this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.Wall)));
      this.flagInWall = this.add((new BooleanSetting("FlagInWall", false, () -> this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.Wall))).setParent());
      this.whenPushOutOfBlocks = this.add(new BooleanSetting("WhilePushOut", false, () -> (this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.Wall)) && this.flagInWall.isOpen()));
      this.staticSetting = this.add(new BooleanSetting("Static", false, () -> this.mode.is(Velocity.Mode.Grim)));
      this.cancelAll = this.add(new BooleanSetting("CancelAll", false, () -> !this.mode.is(Velocity.Mode.None)));
      this.horizontal = this.add(new SliderSetting("Horizontal", (double)0.0F, (double)0.0F, (double)100.0F, (double)1.0F, () -> !this.mode.is(Velocity.Mode.None) && !this.cancelAll.getValue()));
      this.vertical = this.add(new SliderSetting("Vertical", (double)0.0F, (double)0.0F, (double)100.0F, (double)1.0F, () -> !this.mode.is(Velocity.Mode.None) && !this.cancelAll.getValue()));
      this.whileLiquid = this.add(new BooleanSetting("WhileLiquid", false));
      this.whileElytra = this.add(new BooleanSetting("FallFlying", false));
      this.noClimb = this.add(new BooleanSetting("NoClimb", false));
      this.waterPush = this.add(new BooleanSetting("NoWaterPush", false));
      this.entityPush = this.add(new BooleanSetting("NoEntityPush", true));
      this.blockPush = this.add(new BooleanSetting("NoBlockPush", true));
      this.fishBob = this.add(new BooleanSetting("NoFishBob", true));
      this.pearlTimer = new Timer();
      this.lagBackTimer = new Timer();
      this.setChinese("反击退");
      INSTANCE = this;
   }

   public String getInfo() {
      return this.mode.is(Velocity.Mode.None) ? null : ((Mode)this.mode.getValue()).name() + ", " + (this.cancelAll.getValue() ? "Cancel" : this.horizontal.getValueInt() + "%, " + this.vertical.getValueInt() + "%");
   }

   @EventListener
   public void onRotate(UpdateRotateEvent event) {
      if (this.noRotation()) {
         event.setRotation(Alien.ROTATION.rotationYaw, 89.0F);
      }

   }

   public boolean noRotation() {
      return this.isOn() && (this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.Wall)) && EntityUtil.isInsideBlock() && this.noRotation.getValue();
   }

   @EventListener
   public void onVelocity(EntityVelocityUpdateEvent event) {
      if (!nullCheck() && event.getEntity() == mc.field_1724 && !this.mode.is(Velocity.Mode.None) && (!mc.field_1724.method_52535() || this.whileLiquid.getValue()) && (!mc.field_1724.method_6128() || this.whileElytra.getValue())) {
         switch (((Mode)this.mode.getValue()).ordinal()) {
            case 0:
            default:
               break;
            case 1:
               if (!this.lagBackTimer.passedMs(this.lagPause.getValue())) {
                  return;
               }

               if (!EntityUtil.isInsideBlock() && this.getPos() == null && (!this.staticSetting.getValue() || !MovementUtil.isStatic())) {
                  return;
               }

               if (event.getX() != (double)0.0F || event.getZ() != (double)0.0F) {
                  this.flag = true;
               }
               break;
            case 2:
               if (!this.lagBackTimer.passedMs(this.lagPause.getValue())) {
                  return;
               }

               if (!EntityUtil.isInsideBlock()) {
                  return;
               }

               if (event.getX() != (double)0.0F || event.getZ() != (double)0.0F) {
                  this.flag = true;
               }
         }

         if (this.cancelAll.getValue()) {
            event.cancel();
         } else {
            double h = (double)this.horizontal.getValueInt() / (double)100.0F;
            double v = (double)this.vertical.getValueInt() / (double)100.0F;
            event.setX(event.getX() * h);
            event.setZ(event.getZ() * h);
            event.setY(event.getY() * v);
         }
      }

   }

   @EventListener
   public void onReceivePacket(PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2708 && (!this.ignorePearlLag.getValue() || this.pearlTimer.passed((long)this.phaseTime.getValueInt()))) {
         this.lagBackTimer.reset();
      }

      if (!nullCheck() && (!mc.field_1724.method_52535() || this.whileLiquid.getValue()) && this.fishBob.getValue()) {
         class_2596 var4 = event.getPacket();
         if (var4 instanceof class_2663) {
            class_2663 packet = (class_2663)var4;
            if (packet.method_11470() == 31) {
               class_1297 var5 = packet.method_11469(mc.field_1687);
               if (var5 instanceof class_1536) {
                  class_1536 fishHook = (class_1536)var5;
                  if (fishHook.method_26957() == mc.field_1724) {
                     event.setCancelled(true);
                  }
               }
            }
         }
      }

   }

   @EventListener
   public void onUpdate(TickEvent event) {
      if (!nullCheck() && !event.isPost() && (!mc.field_1724.method_52535() || this.whileLiquid.getValue())) {
         if (this.flagInWall.getValue()) {
            pushOutOfBlocks = false;
            pushOutOfBlocks(mc.field_1724.method_23317() - (double)mc.field_1724.method_17681() * 0.35, mc.field_1724.method_23321() + (double)mc.field_1724.method_17681() * 0.35);
            pushOutOfBlocks(mc.field_1724.method_23317() - (double)mc.field_1724.method_17681() * 0.35, mc.field_1724.method_23321() - (double)mc.field_1724.method_17681() * 0.35);
            pushOutOfBlocks(mc.field_1724.method_23317() + (double)mc.field_1724.method_17681() * 0.35, mc.field_1724.method_23321() - (double)mc.field_1724.method_17681() * 0.35);
            pushOutOfBlocks(mc.field_1724.method_23317() + (double)mc.field_1724.method_17681() * 0.35, mc.field_1724.method_23321() + (double)mc.field_1724.method_17681() * 0.35);
         }

         if ((!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) && this.flag) {
            if (this.lagBackTimer.passedMs(this.lagPause.getValue()) && (this.flagInWall.getValue() && (!pushOutOfBlocks || this.whenPushOutOfBlocks.getValue()) || !EntityUtil.isInsideBlock())) {
               mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), Alien.ROTATION.rotationYaw, Alien.ROTATION.rotationPitch, mc.field_1724.method_24828()));
               class_2338 pos = this.getPos();
               if (pos != null) {
                  mc.method_1562().method_52787(new class_2846(class_2847.field_12973, pos, mc.field_1724.method_5735().method_10153()));
               }
            }

            this.flag = false;
         }
      }

   }

   public class_2338 getPos() {
      return mc.field_1687.method_8320(mc.field_1724.method_24515().method_10074()).method_26204() == class_2246.field_10540 ? mc.field_1724.method_24515().method_10074() : null;
   }

   private static void pushOutOfBlocks(double x, double z) {
      class_2338 blockPos = class_2338.method_49637(x, mc.field_1724.method_23318(), z);
      if (wouldCollideAt(blockPos)) {
         double d = x - (double)blockPos.method_10263();
         double e = z - (double)blockPos.method_10260();
         class_2350 direction = null;
         double f = Double.MAX_VALUE;
         class_2350[] directions = new class_2350[]{class_2350.field_11039, class_2350.field_11034, class_2350.field_11043, class_2350.field_11035};

         for(class_2350 direction2 : directions) {
            double g = direction2.method_10166().method_10172(d, (double)0.0F, e);
            double h = direction2.method_10171() == class_2352.field_11056 ? (double)1.0F - g : g;
            if (h < f && !wouldCollideAt(blockPos.method_10093(direction2))) {
               f = h;
               direction = direction2;
            }
         }

         if (direction != null) {
            pushOutOfBlocks = true;
         }
      }

   }

   private static boolean wouldCollideAt(class_2338 pos) {
      class_238 box = mc.field_1724.method_5829();
      class_238 box2 = (new class_238((double)pos.method_10263(), box.field_1322, (double)pos.method_10260(), (double)pos.method_10263() + (double)1.0F, box.field_1325, (double)pos.method_10260() + (double)1.0F)).method_1011(1.0E-7);
      return mc.field_1724.method_37908().method_39454(mc.field_1724, box2);
   }

   public static enum Mode {
      Plain,
      Grim,
      Wall,
      None;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Plain, Grim, Wall, None};
      }
   }
}
