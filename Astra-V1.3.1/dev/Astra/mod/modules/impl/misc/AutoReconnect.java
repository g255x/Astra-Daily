package dev.Astra.mod.modules.impl.misc;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.ServerConnectBeginEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import java.util.HashMap;
import net.minecraft.class_1268;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2886;
import net.minecraft.class_476;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_7439;

public class AutoReconnect extends Module {
   public static AutoReconnect INSTANCE;
   public final BooleanSetting rejoin = this.add(new BooleanSetting("Rejoin", true));
   public final SliderSetting delay = this.add((new SliderSetting("Delay", (double)5.0F, (double)0.0F, (double)20.0F, 0.1)).setSuffix("s"));
   public final BooleanSetting autoLogin = this.add(new BooleanSetting("AutoAuth", true));
   public final SliderSetting afterLoginTime = this.add((new SliderSetting("AfterLoginTime", (double)3.0F, (double)0.0F, (double)10.0F, 0.1)).setSuffix("s"));
   public final BooleanSetting autoQueue = this.add(new BooleanSetting("AutoQueue", true));
   public final SliderSetting joinQueueDelay = this.add((new SliderSetting("JoinQueueDelay", (double)3.0F, (double)0.0F, (double)10.0F, 0.1)).setSuffix("s"));
   final StringSetting password = this.add(new StringSetting("password", "123456"));
   public final BooleanSetting autoAnswer = this.add(new BooleanSetting("AutoAnswer", true));
   public static boolean inQueueServer;
   private boolean queueingByMessage;
   private final Timer queueTimer = new Timer();
   private final Timer queueMenuTimer = new Timer();
   private final Timer timer = new Timer();
   public Pair<class_639, class_642> lastServerConnection;
   private boolean login = false;
   final String[] abc = new String[]{"A", "B", "C"};
   public static final HashMap<String, String> asks = new HashMap<String, String>() {
      {
         this.put("红石火把", "15");
         this.put("猪被闪电", "僵尸猪人");
         this.put("小箱子能", "27");
         this.put("开服年份", "2020");
         this.put("定位末地遗迹", "0");
         this.put("爬行者被闪电", "高压爬行者");
         this.put("大箱子能", "54");
         this.put("羊驼会主动", "不会");
         this.put("无限水", "3");
         this.put("挖掘速度最快", "金镐");
         this.put("凋灵死后", "下界之星");
         this.put("苦力怕的官方", "爬行者");
         this.put("南瓜的生长", "不需要");
         this.put("定位末地", "0");
      }
   };

   public AutoReconnect() {
      super("AutoReconnect", Module.Category.Misc);
      this.setChinese("自动重连");
      INSTANCE = this;
      Astra.EVENT_BUS.subscribe(new StaticListener());
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.login && this.timer.passedS(this.afterLoginTime.getValue())) {
         mc.method_1562().method_45730("login " + this.password.getValue());
         this.login = false;
      }

      if (this.autoQueue.getValue() && !this.queueingByMessage && InventoryUtil.findItem(class_1802.field_8251) != -1 && this.queueTimer.passedS(this.joinQueueDelay.getValue())) {
         InventoryUtil.switchToSlot(InventoryUtil.findItem(class_1802.field_8251));
         sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
         this.queueTimer.reset();
      }

      if (nullCheck()) {
         inQueueServer = false;
         this.queueingByMessage = false;
      } else {
         if (this.autoQueue.getValue() && !this.queueingByMessage && mc.field_1755 instanceof class_476 && this.queueMenuTimer.passedS(this.joinQueueDelay.getValue())) {
            class_1707 container = (class_1707)((class_476)mc.field_1755).method_17577();
            if (container != null && mc.field_1724.field_7512.method_34255().method_7960()) {
               int size = container.method_7629().method_5439();
               int targetSlot = size == 9 ? 4 : (size == 5 ? 2 : -1);
               if (targetSlot != -1) {
                  class_1799 stack = container.method_7629().method_5438(targetSlot);
                  if (stack != null && stack.method_7909() == class_1802.field_8251) {
                     mc.field_1761.method_2906(container.field_7763, targetSlot, 0, class_1713.field_7790, mc.field_1724);
                     this.queueMenuTimer.reset();
                  }
               }
            }
         }

         inQueueServer = InventoryUtil.findItem(class_1802.field_8251) != -1;
      }
   }

   public void onLogin() {
      if (this.autoLogin.getValue()) {
         this.login = true;
         this.timer.reset();
      }

   }

   public boolean rejoin() {
      return this.isOn() && this.rejoin.getValue() && !AutoLog.loggedOut;
   }

   public void onLogout() {
      inQueueServer = false;
      this.queueingByMessage = false;
   }

   public void onDisable() {
      inQueueServer = false;
      this.queueingByMessage = false;
   }

   @EventListener
   public void onPacketReceive(PacketEvent.Receive e) {
      if (!nullCheck()) {
         class_2596<?> packet = e.getPacket();
         String content = null;
         if (packet instanceof class_7439) {
            class_7439 packet2 = (class_7439)packet;
            content = packet2.comp_763().getString();
            if (content.toLowerCase().contains("position in queue")) {
               this.queueingByMessage = true;
            }
         }

         if (this.autoAnswer.getValue()) {
            if (inQueueServer) {
               if (content != null) {
                  for(String key : asks.keySet()) {
                     if (content.contains(key)) {
                        for(String s : this.abc) {
                           if (content.contains(s + "." + (String)asks.get(key))) {
                              mc.method_1562().method_45729(s.toLowerCase());
                              return;
                           }
                        }
                     }
                  }
               }

            }
         }
      }
   }

   private class StaticListener {
      @EventListener
      private void onGameJoined(ServerConnectBeginEvent event) {
         AutoReconnect.this.lastServerConnection = new ObjectObjectImmutablePair(event.address, event.info);
      }
   }
}
