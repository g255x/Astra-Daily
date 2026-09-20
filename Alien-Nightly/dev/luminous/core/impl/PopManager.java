package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DeathEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.TotemEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.Module;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_742;

public class PopManager implements Wrapper {
   public final HashMap<String, Integer> popContainer = new HashMap();
   private final List<class_1657> deadPlayer = new ArrayList();

   public PopManager() {
      this.init();
   }

   public void init() {
      Alien.EVENT_BUS.subscribe(this);
   }

   public int getPop(String s) {
      return (Integer)this.popContainer.getOrDefault(s, 0);
   }

   public int getPop(class_1657 player) {
      return this.getPop(player.method_5477().getString());
   }

   public void onUpdate() {
      if (!Module.nullCheck()) {
         for(class_742 player : Alien.THREAD.getPlayers()) {
            if (player != null && player.method_29504()) {
               if (!this.deadPlayer.contains(player)) {
                  Alien.EVENT_BUS.post(DeathEvent.get(player));
                  this.onDeath(player);
                  this.deadPlayer.add(player);
               }
            } else {
               this.deadPlayer.remove(player);
            }
         }
      }

   }

   @EventListener
   public void onPacketReceive(PacketEvent.Receive event) {
      if (!Module.nullCheck()) {
         class_2596 var4 = event.getPacket();
         if (var4 instanceof class_2663) {
            class_2663 packet = (class_2663)var4;
            if (packet.method_11470() == 35) {
               class_1297 var5 = packet.method_11469(mc.field_1687);
               if (var5 instanceof class_1657) {
                  class_1657 player = (class_1657)var5;
                  this.onTotemPop(player);
               }
            }
         }
      }

   }

   public void onDeath(class_1657 player) {
      this.popContainer.remove(player.method_5477().getString());
   }

   public void onTotemPop(class_1657 player) {
      int l_Count = 1;
      if (this.popContainer.containsKey(player.method_5477().getString())) {
         l_Count = (Integer)this.popContainer.get(player.method_5477().getString());
         HashMap var10000 = this.popContainer;
         String var10001 = player.method_5477().getString();
         ++l_Count;
         var10000.put(var10001, l_Count);
      } else {
         this.popContainer.put(player.method_5477().getString(), l_Count);
      }

      Alien.EVENT_BUS.post(TotemEvent.get(player));
   }
}
