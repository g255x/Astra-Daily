package dev.Astra.mod.modules.impl.movement;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.MoveEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.TickEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import io.netty.util.internal.ConcurrentSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2793;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_434;
import net.minecraft.class_2848.class_2849;

public class PacketFly extends Module {
   public static PacketFly INSTANCE;
   public final EnumSetting<Mode> mode;
   public final SliderSetting factor;
   public final EnumSetting<Phase> phase;
   public final EnumSetting<Type> type;
   public final BooleanSetting antiKick;
   public final BooleanSetting noRotation;
   public final BooleanSetting noMovePacket;
   public final BooleanSetting bbOffset;
   public final SliderSetting invalidY;
   public final SliderSetting invalids;
   public final SliderSetting sendTeleport;
   public final SliderSetting concealY;
   public final SliderSetting conceal;
   public final SliderSetting ySpeed;
   public final SliderSetting xzSpeed;
   public final BooleanSetting elytra;
   public final BooleanSetting xzJitter;
   public final BooleanSetting yJitter;
   public final BooleanSetting zeroSpeed;
   public final BooleanSetting zeroY;
   public final BooleanSetting zeroTeleport;
   public final SliderSetting zoomer;
   public final Map<Integer, TimeVec> posLooks;
   public final Set<class_2596<?>> playerPackets;
   public final AtomicInteger teleportID;
   public class_243 vecDelServer;
   public int packetCounter;
   public boolean zoomies;
   public float lastFactor;
   public int zoomTimer;

   public PacketFly() {
      super("PacketFly", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", PacketFly.Mode.Factor));
      this.factor = this.add(new SliderSetting("Factor", (double)1.0F, (double)0.0F, (double)10.0F));
      this.phase = this.add(new EnumSetting("Phase", PacketFly.Phase.Full));
      this.type = this.add(new EnumSetting("Type", PacketFly.Type.Up));
      this.antiKick = this.add(new BooleanSetting("AntiKick", true));
      this.noRotation = this.add(new BooleanSetting("NoRotation", false));
      this.noMovePacket = this.add(new BooleanSetting("NoMovePacket", false));
      this.bbOffset = this.add(new BooleanSetting("BB-Offset", false));
      this.invalidY = this.add(new SliderSetting("Invalid-Offset", 1337, 0, 1337));
      this.invalids = this.add(new SliderSetting("Invalids", 1, 0, 10));
      this.sendTeleport = this.add(new SliderSetting("Teleport", 1, 0, 10));
      this.concealY = this.add(new SliderSetting("C-Y", (double)0.0F, (double)-256.0F, (double)256.0F));
      this.conceal = this.add(new SliderSetting("C-Multiplier", (double)1.0F, (double)0.0F, (double)2.0F));
      this.ySpeed = this.add(new SliderSetting("Y-Multiplier", (double)1.0F, (double)0.0F, (double)2.0F));
      this.xzSpeed = this.add(new SliderSetting("X/Z-Multiplier", (double)1.0F, (double)0.0F, (double)2.0F));
      this.elytra = this.add(new BooleanSetting("Elytra", false));
      this.xzJitter = this.add(new BooleanSetting("Jitter-XZ", false));
      this.yJitter = this.add(new BooleanSetting("Jitter-Y", false));
      this.zeroSpeed = this.add(new BooleanSetting("Zero-Speed", false));
      this.zeroY = this.add(new BooleanSetting("Zero-Y", false));
      this.zeroTeleport = this.add(new BooleanSetting("Zero-Teleport", true));
      this.zoomer = this.add(new SliderSetting("Zoomies", 3, 0, 10));
      this.posLooks = new ConcurrentHashMap();
      this.playerPackets = new ConcurrentSet();
      this.teleportID = new AtomicInteger();
      this.zoomTimer = 0;
      this.setChinese("发包飞行");
      INSTANCE = this;
   }

   public void onLogin() {
      this.disable();
      this.clearValues();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.posLooks.entrySet().removeIf((entry) -> System.currentTimeMillis() - ((TimeVec)entry.getValue()).getTime() > TimeUnit.SECONDS.toMillis(30L));
   }

   @EventListener
   public void invoke(TickEvent event) {
      if (event.isPre() && this.mode.getValue() != PacketFly.Mode.Compatibility) {
         double ySpeed = (double)0.0F;
         MovementUtil.setMotionX((double)0.0F);
         MovementUtil.setMotionY((double)0.0F);
         MovementUtil.setMotionZ((double)0.0F);
         if (this.mode.getValue() != PacketFly.Mode.Setback && this.teleportID.get() == 0) {
            if (this.checkPackets(6)) {
               this.sendPackets((double)0.0F, (double)0.0F, (double)0.0F, true);
            }

            return;
         }

         boolean isPhasing = this.isPlayerCollisionBoundingBoxEmpty();
         if (!mc.field_1724.field_3913.field_3904 || !isPhasing && MovementUtil.isMoving()) {
            if (!mc.field_1724.field_3913.field_3903) {
               if (!isPhasing) {
                  if (this.checkPackets(4)) {
                     double var10000 = this.antiKick.getValue() ? -0.04 : (double)0.0F;
                  } else {
                     double var15 = (double)0.0F;
                  }
               } else {
                  ySpeed = (double)0.0F;
               }
            } else {
               ySpeed = this.yJitter.getValue() && this.zoomies ? -0.061 : -0.062;
            }
         } else {
            ySpeed = this.antiKick.getValue() && !isPhasing ? (this.checkPackets(this.mode.getValue() == PacketFly.Mode.Setback ? 10 : 20) ? -0.032 : 0.062) : (this.yJitter.getValue() && this.zoomies ? 0.061 : 0.062);
         }

         if (this.phase.getValue() == PacketFly.Phase.Full && isPhasing && MovementUtil.isMoving() && ySpeed != (double)0.0F) {
            ySpeed /= (double)2.5F;
         }

         double high = this.xzJitter.getValue() && this.zoomies ? (double)0.25F : 0.26;
         double low = this.xzJitter.getValue() && this.zoomies ? 0.03 : 0.031;
         double[] dirSpeed = MovementUtil.directionSpeed(this.phase.getValue() == PacketFly.Phase.Full && isPhasing ? low : high);
         if (this.mode.getValue() == PacketFly.Mode.Increment) {
            if ((double)this.lastFactor >= this.factor.getValue()) {
               this.lastFactor = 1.0F;
            } else {
               ++this.lastFactor;
               if ((double)this.lastFactor > this.factor.getValue()) {
                  this.lastFactor = this.factor.getValueFloat();
               }
            }
         } else {
            this.lastFactor = this.factor.getValueFloat();
         }

         int i = 1;

         while(true) {
            float f = (float)i;
            float f2 = this.mode.getValue() != PacketFly.Mode.Factor && this.mode.getValue() != PacketFly.Mode.Slow && this.mode.getValue() != PacketFly.Mode.Increment ? 1.0F : this.lastFactor;
            if (!(f <= f2)) {
               ++this.zoomTimer;
               if ((double)this.zoomTimer > this.zoomer.getValue()) {
                  this.zoomies = !this.zoomies;
                  this.zoomTimer = 0;
               }
               break;
            }

            double conceal = mc.field_1724.method_23318() < this.concealY.getValue() && MovementUtil.isMoving() ? this.conceal.getValue() : (double)1.0F;
            MovementUtil.setMotionX(dirSpeed[0] * (double)i * conceal * this.xzSpeed.getValue());
            MovementUtil.setMotionY(ySpeed * (double)i * this.ySpeed.getValue());
            MovementUtil.setMotionZ(dirSpeed[1] * (double)i * conceal * this.xzSpeed.getValue());
            this.sendPackets(MovementUtil.getMotionX(), MovementUtil.getMotionY(), MovementUtil.getMotionZ(), this.mode.getValue() != PacketFly.Mode.Setback);
            ++i;
         }
      }

   }

   @EventListener
   public void invoke(PacketEvent.Receive event) {
      if (!nullCheck()) {
         if (this.mode.getValue() != PacketFly.Mode.Compatibility) {
            class_2596<?> packet = event.getPacket();
            if (packet instanceof class_2708) {
               class_2708 packet2 = (class_2708)packet;
               TimeVec vec;
               if (mc.field_1724.method_5805() && this.mode.getValue() != PacketFly.Mode.Setback && this.mode.getValue() != PacketFly.Mode.Slow && !(mc.field_1755 instanceof class_434) && (vec = (TimeVec)this.posLooks.remove(packet2.method_11737())) != null && vec.field_1352 == packet2.method_11734() && vec.field_1351 == packet2.method_11735() && vec.field_1350 == packet2.method_11738()) {
                  event.setCancelled(true);
                  return;
               }

               this.teleportID.set(packet2.method_11737());
            }

         }
      }
   }

   @EventListener
   public void invoke(MoveEvent event) {
      if (this.phase.getValue() == PacketFly.Phase.Semi || this.isPlayerCollisionBoundingBoxEmpty()) {
         mc.field_1724.field_5960 = true;
      }

      if (this.mode.getValue() != PacketFly.Mode.Compatibility && (this.mode.getValue() == PacketFly.Mode.Setback || this.teleportID.get() != 0)) {
         if (this.zeroSpeed.getValue()) {
            event.setX((double)0.0F);
            event.setY((double)0.0F);
            event.setZ((double)0.0F);
         } else {
            event.setX(MovementUtil.getMotionX());
            event.setY(MovementUtil.getMotionY());
            event.setZ(MovementUtil.getMotionZ());
         }

         if (this.zeroY.getValue()) {
            event.setY((double)0.0F);
         }
      }

   }

   @EventListener
   public void onPacket(PacketEvent.Send event) {
      class_2596<?> packet = event.getPacket();
      if (packet instanceof class_2828 packet2) {
         if (this.mode.getValue() != PacketFly.Mode.Compatibility && !this.playerPackets.remove(event.getPacket())) {
            if (packet2 instanceof class_2828.class_2831 && !this.noRotation.getValue()) {
               return;
            }

            if (!this.noMovePacket.getValue()) {
               return;
            }

            event.setCancelled(true);
         }
      }

   }

   public void onEnable() {
      this.clearValues();
      if (mc.field_1724 == null) {
         this.disable();
      }

   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).toString();
   }

   public void clearValues() {
      this.lastFactor = 1.0F;
      this.packetCounter = 0;
      this.teleportID.set(0);
      this.playerPackets.clear();
      this.posLooks.clear();
      this.vecDelServer = null;
   }

   public boolean isPlayerCollisionBoundingBoxEmpty() {
      double o = this.bbOffset.getValue() ? (double)-0.0625F : (double)0.0F;
      return BlockUtil.canCollide(mc.field_1724, mc.field_1724.method_5829().method_1009(o, o, o));
   }

   public boolean checkPackets(int amount) {
      if (++this.packetCounter >= amount) {
         this.packetCounter = 0;
         return true;
      } else {
         return false;
      }
   }

   public void sendPackets(double x, double y, double z, boolean confirm) {
      class_243 offset = new class_243(x, y, z);
      class_243 vec;
      this.vecDelServer = vec = mc.field_1724.method_19538().method_1019(offset);
      class_243 oOB = ((Type)this.type.getValue()).createOutOfBounds(vec, this.invalidY.getValueInt());
      this.sendCPacket(new class_2828.class_2829(vec.field_1352, vec.field_1351, vec.field_1350, mc.field_1724.method_24828()));
      if (!mc.method_1542()) {
         for(int i = 0; (double)i < this.invalids.getValue(); ++i) {
            this.sendCPacket(new class_2828.class_2829(oOB.field_1352, oOB.field_1351, oOB.field_1350, mc.field_1724.method_24828()));
            oOB = ((Type)this.type.getValue()).createOutOfBounds(oOB, this.invalidY.getValueInt());
         }
      }

      if (confirm && (this.zeroTeleport.getValue() || this.teleportID.get() != 0)) {
         for(int i = 0; (double)i < this.sendTeleport.getValue(); ++i) {
            this.sendConfirmTeleport(vec);
         }
      }

      if (this.elytra.getValue()) {
         mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
      }

   }

   public void sendConfirmTeleport(class_243 vec) {
      int id = this.teleportID.incrementAndGet();
      mc.method_1562().method_52787(new class_2793(id));
      this.posLooks.put(id, new TimeVec(vec));
   }

   public void sendCPacket(class_2596<?> packet) {
      this.playerPackets.add(packet);
      mc.method_1562().method_52787(packet);
   }

   public static enum Mode {
      Setback,
      Fast,
      Factor,
      Slow,
      Increment,
      Compatibility;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Setback, Fast, Factor, Slow, Increment, Compatibility};
      }
   }

   public static enum Phase {
      Off,
      Semi,
      Full;

      // $FF: synthetic method
      private static Phase[] $values() {
         return new Phase[]{Off, Semi, Full};
      }
   }

   public static enum Type {
      Down {
         public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
            return vec3d.method_1031((double)0.0F, (double)(-invalid), (double)0.0F);
         }
      },
      Up {
         public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
            return vec3d.method_1031((double)0.0F, (double)invalid, (double)0.0F);
         }
      },
      Preserve {
         final Random random = new Random();

         private int randomInt() {
            int result = this.random.nextInt(29000000);
            return this.random.nextBoolean() ? result : -result;
         }

         public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
            return vec3d.method_1031((double)this.randomInt(), (double)0.0F, (double)this.randomInt());
         }
      },
      Switch {
         final Random random = new Random();

         public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
            boolean down = this.random.nextBoolean();
            return down ? vec3d.method_1031((double)0.0F, (double)(-invalid), (double)0.0F) : vec3d.method_1031((double)0.0F, (double)invalid, (double)0.0F);
         }
      },
      X {
         public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
            return vec3d.method_1031((double)invalid, (double)0.0F, (double)0.0F);
         }
      },
      Z {
         public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
            return vec3d.method_1031((double)0.0F, (double)0.0F, (double)invalid);
         }
      },
      XZ {
         public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
            return vec3d.method_1031((double)invalid, (double)0.0F, (double)invalid);
         }
      };

      public abstract class_243 createOutOfBounds(class_243 var1, int var2);

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{Down, Up, Preserve, Switch, X, Z, XZ};
      }
   }

   public static class TimeVec extends class_243 {
      final long time;

      public TimeVec(class_243 vec3d) {
         this(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, System.currentTimeMillis());
      }

      public TimeVec(double xIn, double yIn, double zIn, long time) {
         super(xIn, yIn, zIn);
         this.time = time;
      }

      public long getTime() {
         return this.time;
      }
   }
}
