package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.EntityVelocityUpdateEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.TickEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.text.DecimalFormat;
import net.minecraft.class_1297;
import net.minecraft.class_1536;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_2708;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2846.class_2847;

public class Velocity extends Module {
   public static Velocity INSTANCE;
   private final EnumSetting<Mode> mode;
   private final SliderSetting lagPause;
   private final BooleanSetting ignorePearlLag;
   private final SliderSetting phaseTime;
   private final BooleanSetting cancelAll;
   private final SliderSetting horizontal;
   private final SliderSetting vertical;
   private final BooleanSetting wallsGroundOnly;
   private final BooleanSetting wallsTrapped;
   public final BooleanSetting whileLiquid;
   public final BooleanSetting whileElytra;
   public final BooleanSetting entityPush;
   public final BooleanSetting blockPush;
   public final BooleanSetting fishBob;
   public final BooleanSetting waterPush;
   public final BooleanSetting noClimb;
   private boolean shouldSkipNextZeroVelocity;
   private boolean cancelVelocity;
   private final Timer lagBackTimer;
   public final Timer pearlTimer;

   public Velocity() {
      super("Velocity", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", Velocity.Mode.GrimV3));
      this.lagPause = this.add((new SliderSetting("LagPause", 100, 0, 500, () -> this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.GrimV3) || this.mode.is(Velocity.Mode.Wall))).setSuffix("ms"));
      this.ignorePearlLag = this.add(new BooleanSetting("IgnorePearlLag", true, () -> this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.GrimV3) || this.mode.is(Velocity.Mode.Wall)));
      this.phaseTime = this.add((new SliderSetting("PhaseTime", 200, 0, 1000, () -> (this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.GrimV3) || this.mode.is(Velocity.Mode.Wall)) && this.ignorePearlLag.getValue())).setSuffix("ms"));
      this.cancelAll = this.add(new BooleanSetting("CancelAll", true, () -> this.mode.is(Velocity.Mode.Plain) || this.mode.is(Velocity.Mode.Wall)));
      this.horizontal = this.add((new SliderSetting("Horizontal", (double)0.0F, (double)0.0F, (double)100.0F, (double)1.0F, () -> !this.cancelAll.getValue() && (this.mode.is(Velocity.Mode.Plain) || this.mode.is(Velocity.Mode.Wall)))).setSuffix("%"));
      this.vertical = this.add((new SliderSetting("Vertical", (double)0.0F, (double)0.0F, (double)100.0F, (double)1.0F, () -> !this.cancelAll.getValue() && (this.mode.is(Velocity.Mode.Plain) || this.mode.is(Velocity.Mode.Wall)))).setSuffix("%"));
      this.wallsGroundOnly = this.add(new BooleanSetting("GroundOnly", false, () -> this.mode.is(Velocity.Mode.Wall)));
      this.wallsTrapped = this.add(new BooleanSetting("Trapped", false, () -> this.mode.is(Velocity.Mode.Wall)));
      this.whileLiquid = this.add(new BooleanSetting("WhileLiquid", true));
      this.whileElytra = this.add(new BooleanSetting("FallFlying", true));
      this.entityPush = this.add(new BooleanSetting("NoEntityPush", true));
      this.blockPush = this.add(new BooleanSetting("NoBlockPush", true));
      this.fishBob = this.add(new BooleanSetting("NoFishBob", true));
      this.waterPush = this.add(new BooleanSetting("NoWaterPush", false));
      this.noClimb = this.add(new BooleanSetting("NoClimb", false));
      this.lagBackTimer = new Timer();
      this.pearlTimer = new Timer();
      this.setChinese("反击退");
      INSTANCE = this;
   }

   public String getInfo() {
      if (this.mode.getValue() == Velocity.Mode.None) {
         return null;
      } else if (this.mode.is(Velocity.Mode.Grim)) {
         return "Grim";
      } else if (this.mode.is(Velocity.Mode.GrimV3)) {
         return "GrimV3";
      } else if (this.mode.is(Velocity.Mode.Wall)) {
         return "Wall";
      } else if (this.cancelAll.getValue()) {
         return "Cancel";
      } else {
         DecimalFormat df = new DecimalFormat("0.0");
         return String.format("H:%s%%, V:%s%%", df.format(this.horizontal.getValue()), df.format(this.vertical.getValue()));
      }
   }

   public void onEnable() {
      this.shouldSkipNextZeroVelocity = false;
      this.cancelVelocity = false;
   }

   public void onDisable() {
      if (this.cancelVelocity && this.mode.getValue() == Velocity.Mode.Grim) {
         this.sendCompensationPackets();
         this.cancelVelocity = false;
      }

      this.shouldSkipNextZeroVelocity = false;
   }

   private boolean isInsideBlock() {
      class_238 bb = mc.field_1724.method_5829();
      class_2338 min = new class_2338((int)Math.floor(bb.field_1323), (int)Math.floor(bb.field_1322), (int)Math.floor(bb.field_1321));
      class_2338 max = new class_2338((int)Math.floor(bb.field_1320), (int)Math.floor(bb.field_1325), (int)Math.floor(bb.field_1324));

      for(int x = min.method_10263(); x <= max.method_10263(); ++x) {
         for(int y = min.method_10264(); y <= max.method_10264(); ++y) {
            for(int z = min.method_10260(); z <= max.method_10260(); ++z) {
               if (!mc.field_1687.method_8320(new class_2338(x, y, z)).method_45474()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean isWallsTrapped() {
      class_2338 headPos = mc.field_1724.method_24515().method_10086(mc.field_1724.method_20448() ? 1 : 2);
      return !mc.field_1687.method_8320(headPos).method_45474();
   }

   private void sendCompensationPackets() {
      float yaw = mc.field_1724.method_36454();
      float pitch = mc.field_1724.method_36455();
      mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), yaw, pitch, mc.field_1724.method_24828()));
      class_2338 pos = mc.field_1724.method_24515().method_10074();
      if (!mc.field_1687.method_8320(pos).method_45474()) {
         mc.method_1562().method_52787(new class_2846(class_2847.field_12973, pos, mc.field_1724.method_5735().method_10153()));
      }

   }

   private void applyScale(EntityVelocityUpdateEvent event) {
      double h = this.horizontal.getValue() / (double)100.0F;
      double v = this.vertical.getValue() / (double)100.0F;
      event.setX(event.getX() * h);
      event.setZ(event.getZ() * h);
      event.setY(event.getY() * v);
   }

   @EventListener
   public void onVelocity(EntityVelocityUpdateEvent event) {
      if (!nullCheck()) {
         if (event.getEntity() == mc.field_1724) {
            if (this.mode.getValue() != Velocity.Mode.None) {
               if (!mc.field_1724.method_52535() || this.whileLiquid.getValue()) {
                  if (!mc.field_1724.method_6128() || this.whileElytra.getValue()) {
                     if (this.cancelAll.getValue() && this.shouldSkipNextZeroVelocity && event.getX() == (double)0.0F && event.getZ() == (double)0.0F && event.getY() == (double)0.0F) {
                        event.cancel();
                        this.shouldSkipNextZeroVelocity = false;
                     } else if (!this.mode.is(Velocity.Mode.Grim) && !this.mode.is(Velocity.Mode.GrimV3) && !this.mode.is(Velocity.Mode.Wall) || this.lagBackTimer.passedMs((double)((long)this.lagPause.getValue()))) {
                        if (this.mode.is(Velocity.Mode.Wall)) {
                           if (!this.isInsideBlock() && (!this.wallsTrapped.getValue() || !this.isWallsTrapped())) {
                              return;
                           }

                           if (this.wallsGroundOnly.getValue() && !mc.field_1724.method_24828()) {
                              return;
                           }
                        } else if ((this.mode.is(Velocity.Mode.Grim) || this.mode.is(Velocity.Mode.GrimV3)) && !this.isInsideBlock()) {
                           return;
                        }

                        if (this.mode.is(Velocity.Mode.GrimV3)) {
                           event.cancel();
                        } else if (this.mode.is(Velocity.Mode.Grim)) {
                           event.cancel();
                           this.cancelVelocity = true;
                        } else {
                           this.applyScale(event);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void onReceivePacket(PacketEvent.Receive event) {
      if (!nullCheck()) {
         if (!mc.field_1724.method_52535() || this.whileLiquid.getValue()) {
            if (event.getPacket() instanceof class_2708) {
               if (!this.ignorePearlLag.getValue() || this.pearlTimer.passed((long)this.phaseTime.getValueInt())) {
                  this.lagBackTimer.reset();
               }

               if ((this.mode.is(Velocity.Mode.Plain) || this.mode.is(Velocity.Mode.Wall)) && this.cancelAll.getValue()) {
                  this.shouldSkipNextZeroVelocity = true;
               }
            }

            if (this.fishBob.getValue()) {
               class_2596 var3 = event.getPacket();
               if (var3 instanceof class_2663) {
                  class_2663 packet = (class_2663)var3;
                  if (packet.method_11470() == 31) {
                     class_1297 entity = packet.method_11469(mc.field_1687);
                     if (entity instanceof class_1536) {
                        class_1536 hook = (class_1536)entity;
                        if (hook.method_26957() == mc.field_1724) {
                           event.setCancelled(true);
                        }
                     }
                  }
               }
            }

         }
      }
   }

   @EventListener
   public void onUpdate(TickEvent event) {
      if (!nullCheck()) {
         if (!event.isPost()) {
            if (!mc.field_1724.method_52535() || this.whileLiquid.getValue()) {
               if (Blink.INSTANCE == null || !Blink.INSTANCE.isOn() || Blink.INSTANCE.pauseModule == null || !Blink.INSTANCE.pauseModule.getValue()) {
                  if (this.cancelVelocity && this.mode.getValue() == Velocity.Mode.Grim && this.lagBackTimer.passedMs((double)((long)this.lagPause.getValue()))) {
                     this.sendCompensationPackets();
                     this.cancelVelocity = false;
                  }

               }
            }
         }
      }
   }

   @EventListener
   public void onPushEntity(Object event) {
      try {
         class_1297 pushed = (class_1297)event.getClass().getMethod("getEntity").invoke(event);
         if (this.entityPush.getValue() && pushed == mc.field_1724) {
            event.getClass().getMethod("setCancelled", Boolean.TYPE).invoke(event, true);
         }
      } catch (Exception var3) {
      }

   }

   @EventListener
   public void onPushOutOfBlocks(Object event) {
      if (this.blockPush.getValue()) {
         try {
            event.getClass().getMethod("setCancelled", Boolean.TYPE).invoke(event, true);
         } catch (Exception var3) {
         }
      }

   }

   private class_2338 getPos() {
      return mc.field_1687.method_8320(mc.field_1724.method_24515().method_10074()).method_26204() == class_2246.field_10540 ? mc.field_1724.method_24515().method_10074() : null;
   }

   public static enum Mode {
      Plain,
      Grim,
      Wall,
      GrimV3,
      None;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Plain, Grim, Wall, GrimV3, None};
      }
   }
}
