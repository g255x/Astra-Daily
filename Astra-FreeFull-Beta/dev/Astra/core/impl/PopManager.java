package dev.Astra.core.impl;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.DeathEvent;
import dev.Astra.api.events.impl.GameLeftEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.TotemEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClickGui;
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
      Astra.EVENT_BUS.subscribe(this);
      ClickGui.key = "GOUTOURENNIMASILECAONIMA";
   }

   public int getPop(String s) {
      return (Integer)this.popContainer.getOrDefault(s, 0);
   }

   public int getPop(class_1657 player) {
      return this.getPop(player.method_5477().getString());
   }

   public void onUpdate() {
      if (!Module.nullCheck()) {
         for(class_742 player : Astra.THREAD.getPlayers()) {
            if (player != null && player.method_29504()) {
               if (!this.deadPlayer.contains(player)) {
                  Astra.EVENT_BUS.post(DeathEvent.get(player));
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
         class_2596<?> packet2 = event.getPacket();
         class_1297 entity;
         class_2663 packet;
         if (packet2 instanceof class_2663 && (packet = (class_2663)packet2).method_11470() == 35 && (entity = packet.method_11469(mc.field_1687)) instanceof class_1657) {
            class_1657 player = (class_1657)entity;
            this.onTotemPop(player);
         }

      }
   }

   @EventListener
   public void onGameLeft(GameLeftEvent event) {
      this.deadPlayer.clear();
      this.popContainer.clear();
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

      Astra.EVENT_BUS.post(TotemEvent.get(player));
   }
}
