package dev.Astra.core.impl;

import com.google.common.collect.Lists;
import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.render.JelloUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.combat.AutoAnchor;
import dev.Astra.mod.modules.impl.combat.AutoCrystal;
import dev.Astra.mod.modules.impl.render.HoleESP;
import dev.Astra.mod.modules.impl.render.PlaceRender;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.class_1297;
import net.minecraft.class_742;

public class ThreadManager implements Wrapper {
   public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
   public static ClientService clientService;
   public volatile Iterable<class_1297> threadSafeEntityList = Collections.emptyList();
   public volatile List<class_742> threadSafePlayersList = Collections.emptyList();
   public volatile boolean tickRunning = false;

   public ThreadManager() {
      this.init();
   }

   public void init() {
      Astra.EVENT_BUS.subscribe(this);
      clientService = new ClientService();
      clientService.setName("AstraClientService");
      clientService.setDaemon(true);
      clientService.start();
   }

   public Iterable<class_1297> getEntities() {
      return this.threadSafeEntityList;
   }

   public List<class_742> getPlayers() {
      return this.threadSafePlayersList;
   }

   public void execute(Runnable runnable) {
      EXECUTOR.execute(runnable);
   }

   @EventListener(
      priority = 200
   )
   public void onEvent(ClientTickEvent event) {
      Astra.POP.onUpdate();
      Astra.SERVER.onUpdate();
      if (event.isPre()) {
         JelloUtil.updateJello();
         this.tickRunning = true;
         BlockUtil.placedPos.forEach((pos) -> PlaceRender.INSTANCE.create(pos));
         BlockUtil.placedPos.clear();
         Astra.PLAYER.onUpdate();
         if (!Module.nullCheck()) {
            Astra.EVENT_BUS.post(UpdateEvent.INSTANCE);
         }
      } else {
         this.tickRunning = false;
         if (mc.field_1687 == null || mc.field_1724 == null) {
            return;
         }

         this.threadSafeEntityList = Lists.newArrayList(mc.field_1687.method_18112());
         this.threadSafePlayersList = Lists.newArrayList(mc.field_1687.method_18456());
      }

      if (!clientService.isAlive() || clientService.isInterrupted()) {
         clientService = new ClientService();
         clientService.setName("AstraService");
         clientService.setDaemon(true);
         clientService.start();
      }

   }

   public class ClientService extends Thread {
      public void run() {
         long lastErrorTime = 0L;

         while(true) {
            try {
               if (ThreadManager.this.tickRunning) {
                  Thread.sleep(5L);
               } else {
                  AutoCrystal.INSTANCE.onThread();
                  HoleESP.INSTANCE.onThread();
                  AutoAnchor.INSTANCE.onThread();
                  Thread.sleep(10L);
               }
            } catch (InterruptedException var7) {
               Thread.currentThread().interrupt();
               return;
            } catch (Exception e) {
               e.printStackTrace();
               if (ClientSetting.INSTANCE.debug.getValue()) {
                  CommandManager.sendMessage("§4An error has occurred [Thread] Message: [" + e.getMessage() + "]");
               }

               try {
                  long now = System.currentTimeMillis();
                  if (now - lastErrorTime < 50L) {
                     Thread.sleep(50L - (now - lastErrorTime));
                  } else {
                     Thread.sleep(50L);
                  }

                  lastErrorTime = System.currentTimeMillis();
               } catch (InterruptedException var6) {
                  Thread.currentThread().interrupt();
                  return;
               }
            }
         }
      }
   }
}
