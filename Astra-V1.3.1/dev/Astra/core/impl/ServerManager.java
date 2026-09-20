package dev.Astra.core.impl;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.GameLeftEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.client.Fonts;
import dev.Astra.mod.modules.impl.combat.Criticals;
import dev.Astra.mod.modules.impl.misc.AutoLog;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import net.minecraft.class_2596;
import net.minecraft.class_2761;
import net.minecraft.class_2824;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_7438;
import net.minecraft.class_7439;
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
      Astra.EVENT_BUS.subscribe(this);
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
            class_2868 packet2;
            if (packet instanceof class_2868 && this.lastSlot != (packet2 = (class_2868)packet).method_12442()) {
               this.lastSlot = packet2.method_12442();
               mc.field_1724.method_7350();
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
      class_2596<?> packet = event.getPacket();
      if (packet instanceof class_2868 packet2) {
         int packetSlot = packet2.method_12442();
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

   @EventListener
   private void PacketReceive(PacketEvent.Receive event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         String s = null;
         class_2596<?> packet = event.getPacket();
         if (packet instanceof class_7439) {
            class_7439 packet2 = (class_7439)packet;
            if (packet2.comp_763() != null) {
               s = packet2.comp_763().getString();
            }
         } else {
            packet = event.getPacket();
            if (packet instanceof class_7438) {
               class_7438 packet3 = (class_7438)packet;
               s = packet3.comp_1103() != null ? packet3.comp_1103().getString() : packet3.comp_1102().comp_1090();
            }
         }

         if (s != null) {
         }

      }
   }

   public void onUpdate() {
      if (mc.field_1724 == null) {
         this.playerNull.reset();
      }

      if (this.worldNull && mc.field_1687 != null) {
         Fonts.INSTANCE.refresh();
         AutoLog.loggedOut = false;
         Astra.MODULE.onLogin();
         this.worldNull = false;
      } else if (!this.worldNull && mc.field_1687 == null) {
         Astra.save();
         Astra.MODULE.onLogout();
         this.worldNull = true;
      }

   }
}
