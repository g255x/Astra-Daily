package dev.Astra.core.impl;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.BlockActivateEvent;
import dev.Astra.api.events.impl.GameLeftEvent;
import dev.Astra.api.events.impl.OpenScreenEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.mod.modules.Module;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1263;
import net.minecraft.class_1657;
import net.minecraft.class_1707;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2336;
import net.minecraft.class_2338;
import net.minecraft.class_2371;
import net.minecraft.class_238;
import net.minecraft.class_437;
import net.minecraft.class_476;
import net.minecraft.class_5134;

public class PlayerManager implements Wrapper {
   public static class_437 screenToOpen;
   public final class_2371<class_1799> ENDERCHEST_ITEM;
   public final Map<class_1657, EntityAttribute> map;
   public final CopyOnWriteArrayList<class_1657> inWebPlayers;
   public boolean known;
   private int echestOpenedState;

   public PlayerManager() {
      this.ENDERCHEST_ITEM = class_2371.method_10213(27, class_1799.field_8037);
      this.map = new ConcurrentHashMap();
      this.inWebPlayers = new CopyOnWriteArrayList();
      this.known = false;
      Astra.EVENT_BUS.subscribe(this);
   }

   @EventListener
   public void onLogout(GameLeftEvent event) {
      this.inWebPlayers.clear();
      this.map.clear();
      this.ENDERCHEST_ITEM.clear();
      this.known = false;
   }

   @EventListener
   private void onBlockActivate(BlockActivateEvent event) {
      if (event.blockState.method_26204() instanceof class_2336 && this.echestOpenedState == 0) {
         this.echestOpenedState = 1;
      }

   }

   @EventListener
   private void onOpenScreenEvent(OpenScreenEvent event) {
      if (this.echestOpenedState == 1 && event.screen instanceof class_476) {
         this.echestOpenedState = 2;
      } else if (this.echestOpenedState != 0) {
         if (mc.field_1755 instanceof class_476) {
            class_1707 container = (class_1707)((class_476)mc.field_1755).method_17577();
            if (container != null) {
               class_1263 inv = container.method_7629();

               for(int i = 0; i < 27; ++i) {
                  this.ENDERCHEST_ITEM.set(i, inv.method_5438(i));
               }

               this.known = true;
               this.echestOpenedState = 0;
            }
         }
      }
   }

   public void onUpdate() {
      if (!Module.nullCheck()) {
         if (screenToOpen != null && mc.field_1755 == null) {
            mc.method_1507(screenToOpen);
            screenToOpen = null;
         }

         this.inWebPlayers.clear();

         for(class_1657 playerEntity : Astra.THREAD.getPlayers()) {
            this.map.put(playerEntity, new EntityAttribute(playerEntity.method_6096(), playerEntity.method_45325(class_5134.field_23725)));
            this.webUpdate(playerEntity);
         }

      }
   }

   public boolean isInWeb(class_1657 player) {
      return this.inWebPlayers.contains(player);
   }

   private void webUpdate(class_1657 player) {
      for(float x : new float[]{0.0F, 0.3F, -0.3F}) {
         for(float z : new float[]{0.0F, 0.3F, -0.3F}) {
            for(int y : new int[]{-1, 0, 1, 2}) {
               class_2338 pos = (new BlockPosX(player.method_23317() + (double)x, player.method_23318(), player.method_23321() + (double)z)).method_10086(y);
               if ((new class_238(pos)).method_994(player.method_5829()) && mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10343) {
                  this.inWebPlayers.add(player);
                  return;
               }
            }
         }
      }

   }

   public static record EntityAttribute(int armor, double toughness) {
   }
}
