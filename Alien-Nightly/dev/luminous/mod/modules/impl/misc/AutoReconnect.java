package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.ServerConnectBeginEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import java.util.HashMap;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2886;
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
   private final Timer queueTimer = new Timer();
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
      Alien.EVENT_BUS.subscribe(new StaticListener());
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.login && this.timer.passedS(this.afterLoginTime.getValue())) {
         mc.method_1562().method_45730("login " + this.password.getValue());
         this.login = false;
      }

      if (this.autoQueue.getValue() && InventoryUtil.findItem(class_1802.field_8251) != -1 && this.queueTimer.passedS(this.joinQueueDelay.getValue())) {
         InventoryUtil.switchToSlot(InventoryUtil.findItem(class_1802.field_8251));
         sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
         this.queueTimer.reset();
      }

      if (nullCheck()) {
         inQueueServer = false;
      } else {
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
   }

   public void onDisable() {
      inQueueServer = false;
   }

   @EventListener
   public void onPacketReceive(PacketEvent.Receive e) {
      if (!nullCheck() && this.autoAnswer.getValue() && inQueueServer) {
         class_2596 var3 = e.getPacket();
         if (var3 instanceof class_7439) {
            class_7439 packet = (class_7439)var3;

            for(String key : asks.keySet()) {
               if (packet.comp_763().getString().contains(key)) {
                  for(String s : this.abc) {
                     if (packet.comp_763().getString().contains(s + "." + (String)asks.get(key))) {
                        mc.method_1562().method_45729(s.toLowerCase());
                        return;
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
