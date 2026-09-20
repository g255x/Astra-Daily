package dev.luminous.core.impl;

import com.google.common.collect.Lists;
import dev.luminous.api.events.eventbus.EventBus;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DisconnectEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.TickCounterEvent;
import dev.luminous.api.utils.more.EvictingQueue;
import dev.luminous.core.Manager;
import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Queue;
import net.minecraft.class_2761;

public class TickManager extends Manager {
   private final Deque<Float> ticks = new EvictingQueue<Float>(20);
   private long time;
   private float clientTick = 1.0F;

   public TickManager() {
      EventBus.INSTANCE.subscribe(this);
   }

   @EventListener
   public void onDisconnect(DisconnectEvent event) {
      this.ticks.clear();
   }

   @EventListener
   public void onPacketInbound(PacketEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (event.getPacket() instanceof class_2761) {
            float last = 20000.0F / (float)(System.currentTimeMillis() - this.time);
            this.ticks.addFirst(last);
            this.time = System.currentTimeMillis();
         }

      }
   }

   public void setClientTick(float ticks) {
      this.clientTick = ticks;
   }

   @EventListener
   public void onTickCounter(TickCounterEvent event) {
      if (this.clientTick != 1.0F) {
         event.cancel();
         event.setTicks(this.clientTick);
      }

   }

   public Queue<Float> getTicks() {
      return this.ticks;
   }

   public float getTpsAverage() {
      float avg = 0.0F;

      try {
         ArrayList<Float> ticksCopy = Lists.newArrayList(this.ticks);
         if (!ticksCopy.isEmpty()) {
            for(float t : ticksCopy) {
               avg += t;
            }

            avg /= Math.max((float)ticksCopy.size(), 1.0F);
         }
      } catch (NullPointerException var5) {
      }

      return Math.min(100.0F, avg);
   }

   public float getTpsCurrent() {
      try {
         if (!this.ticks.isEmpty()) {
            return Math.min(100.0F, (Float)this.ticks.getFirst());
         }
      } catch (NoSuchElementException var2) {
      }

      return 20.0F;
   }

   public float getTpsMin() {
      float min = 20.0F;

      try {
         for(float t : this.ticks) {
            if (t < min) {
               min = t;
            }
         }
      } catch (NullPointerException var4) {
      }

      return min;
   }

   public boolean isTicksFilled() {
      return this.ticks.size() >= 20;
   }

   public float getTickSync(TickSync tps) {
      float var10000;
      switch (tps) {
         case AVERAGE -> var10000 = this.getTpsAverage();
         case CURRENT -> var10000 = this.getTpsCurrent();
         case MINIMAL -> var10000 = this.getTpsMin();
         case NONE -> var10000 = 20.0F;
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }
}
