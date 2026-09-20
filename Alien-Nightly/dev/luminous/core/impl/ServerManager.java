package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.GameLeftEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.client.Fonts;
import dev.luminous.mod.modules.impl.combat.Criticals;
import dev.luminous.mod.modules.impl.misc.AutoLog;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import net.minecraft.class_2596;
import net.minecraft.class_2761;
import net.minecraft.class_2824;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2824.class_5907;

public class ServerManager implements Wrapper {
   public final Timer playerNull = new Timer();
   public int currentSlot = -1;
   private final ArrayDeque<Float> tpsResult = new ArrayDeque(20);
   boolean worldNull = true;
   private long time;
   private long tickTime;
   private float tps;
   int lastSlot;

   public ServerManager() {
      Alien.EVENT_BUS.subscribe(this);
   }

   public static float round2(double value) {
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.floatValue();
   }

   @EventListener(
      priority = -200
   )
   public void onPacket(PacketEvent.Send event) {
      if (AntiCheat.INSTANCE.attackCDFix.getValue()) {
         if (event.isCancelled()) {
            return;
         }

         class_2596<?> packet = event.getPacket();
         if (!(packet instanceof class_2879) && (!(packet instanceof class_2824) || Criticals.getInteractType((class_2824)packet) != class_5907.field_29172)) {
            if (packet instanceof class_2868) {
               class_2868 packet2 = (class_2868)packet;
               if (this.lastSlot != packet2.method_12442()) {
                  this.lastSlot = packet2.method_12442();
                  mc.field_1724.method_7350();
               }
            }
         } else {
            mc.field_1724.method_7350();
         }
      }

   }

   @EventListener
   public void onLeft(GameLeftEvent event) {
      this.currentSlot = -1;
   }

   @EventListener
   public void onPacketSend(PacketEvent.Send event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2868 packet) {
         int packetSlot = packet.method_12442();
         if (AntiCheat.INSTANCE.noBadSlot.getValue() && packetSlot == this.currentSlot) {
            event.cancel();
            return;
         }

         this.currentSlot = packetSlot;
      }

   }

   public float getTPS() {
      return round2((double)this.tps);
   }

   public float getCurrentTPS() {
      return round2((double)(20.0F * ((float)this.tickTime / 1000.0F)));
   }

   public float getTPSFactor() {
      return this.getTPS() / 20.0F;
   }

   @EventListener(
      priority = 999
   )
   public void onPacketReceive(PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2761) {
         if (this.time != 0L) {
            this.tickTime = System.currentTimeMillis() - this.time;
            if (this.tpsResult.size() > 20) {
               this.tpsResult.poll();
            }

            this.tpsResult.add(20.0F * (1000.0F / (float)this.tickTime));
            float average = 0.0F;

            for(Float value : this.tpsResult) {
               average += MathUtil.clamp(value, 0.0F, 20.0F);
            }

            this.tps = average / (float)this.tpsResult.size();
         }

         this.time = System.currentTimeMillis();
      }

   }

   public void onUpdate() {
      if (mc.field_1724 == null) {
         this.playerNull.reset();
      }

      if (this.worldNull && mc.field_1687 != null) {
         Fonts.INSTANCE.refresh();
         AutoLog.loggedOut = false;
         Alien.MODULE.onLogin();
         this.worldNull = false;
      } else if (!this.worldNull && mc.field_1687 == null) {
         Alien.save();
         Alien.MODULE.onLogout();
         this.worldNull = true;
      }

   }
}
