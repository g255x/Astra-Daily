package dev.Astra.core.impl;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.BlockBreakingProgressEvent;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.ServerConnectBeginEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.FadeUtils;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.player.PacketMine;
import dev.Astra.mod.modules.impl.render.BreakESP;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2620;
import net.minecraft.class_3532;

public class BreakManager implements Wrapper {
   public final ConcurrentHashMap<Integer, BreakData> breakMap = new ConcurrentHashMap();
   public final ConcurrentHashMap<Integer, BreakData> doubleMap = new ConcurrentHashMap();

   public BreakManager() {
      Astra.EVENT_BUS.subscribe(this);
   }

   @EventListener
   public void onServerConnectBegin(ServerConnectBeginEvent event) {
      this.breakMap.clear();
      this.doubleMap.clear();
   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!Module.nullCheck()) {
         if (AntiCheat.INSTANCE.detectDouble.getValue()) {
            for(int i : Astra.BREAK.doubleMap.keySet()) {
               BreakData breakData = (BreakData)Astra.BREAK.doubleMap.get(i);
               if (breakData == null || breakData.getEntity() == null || mc.field_1687.method_22347(breakData.pos) || breakData.timer.passedMs(Math.max(AntiCheat.INSTANCE.minTimeout.getValue() * (double)1000.0F, breakData.breakTime * AntiCheat.INSTANCE.doubleMineTimeout.getValue()))) {
                  Astra.BREAK.doubleMap.remove(i);
               }
            }
         }

         for(BreakData breakData : this.breakMap.values()) {
            breakData.breakTime = Math.max(BreakESP.getBreakTime(breakData.pos, false), (double)50.0F);
            if (PacketMine.unbreakable(breakData.pos)) {
               breakData.fade.setLength(0L);
               breakData.complete = false;
               breakData.failed = true;
            } else if (mc.field_1687.method_22347(breakData.pos)) {
               breakData.fade.setLength(0L);
               breakData.complete = true;
               breakData.failed = false;
            } else if (!breakData.complete && breakData.timer.passedMs(breakData.breakTime * AntiCheat.INSTANCE.breakTimeout.getValue())) {
               breakData.fade.setLength(0L);
               breakData.failed = true;
            } else {
               breakData.fade.setLength((long)breakData.breakTime);
            }
         }

      }
   }

   @EventListener
   public void onPacket(PacketEvent.Receive event) {
      if (!Module.nullCheck()) {
         class_2596<?> packet = event.getPacket();
         if (packet instanceof class_2620) {
            class_2620 packet2 = (class_2620)packet;
            if (packet2.method_11277() == null) {
               return;
            }

            BreakData breakData = new BreakData(packet2.method_11277(), packet2.method_11280(), false);
            if (breakData.getEntity() == null) {
               return;
            }

            if (class_3532.method_15355((float)breakData.getEntity().method_33571().method_1025(packet2.method_11277().method_46558())) > 8.0F) {
               return;
            }

            if (AntiCheat.INSTANCE.detectDouble.getValue() && packet2.method_11278() != 255) {
               if (packet2.method_11278() != 0) {
                  BreakData doublePos = (BreakData)this.doubleMap.get(packet2.method_11280());
                  if (doublePos != null) {
                     doublePos.pos = packet2.method_11277();
                     doublePos.timer.reset();
                  } else if (!PacketMine.unbreakable(packet2.method_11277())) {
                     this.doubleMap.put(packet2.method_11280(), new BreakData(packet2.method_11277(), packet2.method_11280(), true));
                  }

                  return;
               }

               BreakData doublePos = (BreakData)this.doubleMap.get(packet2.method_11280());
               if (doublePos != null && doublePos.pos.equals(packet2.method_11277()) && !doublePos.timer.passedS((double)150.0F)) {
                  return;
               }
            }

            BreakData current;
            if ((current = (BreakData)this.breakMap.get(packet2.method_11280())) != null && !current.failed && current.pos.equals(packet2.method_11277())) {
               return;
            }

            this.breakMap.put(packet2.method_11280(), breakData);
            Astra.EVENT_BUS.post(BlockBreakingProgressEvent.get(packet2.method_11277(), packet2.method_11280(), packet2.method_11278()));
            if (AntiCheat.INSTANCE.detectDouble.getValue() && !this.doubleMap.containsKey(packet2.method_11280()) && !PacketMine.unbreakable(packet2.method_11277())) {
               this.doubleMap.put(packet2.method_11280(), new BreakData(packet2.method_11277(), packet2.method_11280(), true));
            }
         }

      }
   }

   public boolean isMining(class_2338 pos) {
      return this.isMining(pos, true);
   }

   public boolean isMining(class_2338 pos, boolean self) {
      if (self && PacketMine.getBreakPos() != null && PacketMine.getBreakPos().equals(pos)) {
         return true;
      } else {
         for(BreakData breakData : this.breakMap.values()) {
            if (breakData.getEntity() != null && !(breakData.getEntity().method_33571().method_1022(pos.method_46558()) > (double)7.0F) && !breakData.failed && breakData.pos.equals(pos)) {
               return true;
            }
         }

         return false;
      }
   }

   public static class BreakData {
      public class_2338 pos;
      private final int entityId;
      public final FadeUtils fade;
      public final Timer timer;
      public double breakTime;
      public boolean failed = false;
      public boolean complete = false;

      public BreakData(class_2338 pos, int entityId, boolean extraBreak) {
         this.pos = pos;
         this.entityId = entityId;
         this.breakTime = Math.max(BreakESP.getBreakTime(pos, extraBreak), (double)50.0F);
         this.fade = new FadeUtils((long)this.breakTime);
         this.timer = new Timer();
      }

      public class_1297 getEntity() {
         if (Wrapper.mc.field_1687 == null) {
            return null;
         } else {
            class_1297 entity = Wrapper.mc.field_1687.method_8469(this.entityId);
            return entity instanceof class_1657 ? entity : null;
         }
      }
   }
}
