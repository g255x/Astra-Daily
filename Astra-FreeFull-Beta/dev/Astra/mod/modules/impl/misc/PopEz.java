package dev.Astra.mod.modules.impl.misc;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.TotemEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.class_1297;
import net.minecraft.class_1657;

public class PopEz extends Module {
   public static PopEz INSTANCE;
   private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   private final SliderSetting randomChars = this.add(new SliderSetting("RandomChars", (double)3.0F, (double)0.0F, (double)20.0F, (double)1.0F));
   public final BooleanSetting slowSend = this.add(new BooleanSetting("SlowSend", false));
   final StringSetting customMessage = this.add(new StringSetting("CustomMessage", ""));
   Random random = new Random();
   Timer timer = new Timer();
   private final Map<Integer, Integer> popQueue = new HashMap();

   public PopEz() {
      super("PopEz", Module.Category.Misc);
      this.setChinese("POP嘲讽");
      INSTANCE = this;
   }

   @EventListener
   public void onTotem(TotemEvent event) {
      class_1657 player = event.getPlayer();
      if (player != null && player != mc.field_1724 && !Astra.FRIEND.isFriend(player)) {
         int count = 1;
         if (Astra.POP.popContainer.containsKey(player.method_5477().getString())) {
            count = (Integer)Astra.POP.popContainer.get(player.method_5477().getString());
         }

         if (this.slowSend.getValue()) {
            this.popQueue.put(player.method_5628(), count);
         } else {
            this.sendPopMessage(player, count);
         }
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.slowSend.getValue() && !this.popQueue.isEmpty() && this.timer.passedS(3.2)) {
         this.timer.reset();
         Map.Entry<Integer, Integer> entry = (Map.Entry)this.popQueue.entrySet().iterator().next();
         int playerId = (Integer)entry.getKey();
         int popCount = (Integer)entry.getValue();
         class_1657 player = null;

         for(class_1297 entity : mc.field_1687.method_18112()) {
            if (entity instanceof class_1657) {
               class_1657 p = (class_1657)entity;
               if (p.method_5628() == playerId) {
                  player = p;
                  break;
               }
            }
         }

         if (player != null) {
            this.sendPopMessage(player, popCount);
         }

         this.popQueue.remove(playerId);
      }

   }

   private void sendPopMessage(class_1657 player, int count) {
      String rawMessage = this.customMessage.getValue();
      String message;
      if (rawMessage.isEmpty()) {
         if (count == 1) {
            String var10000 = player.method_5477().getString();
            message = var10000 + " has popped " + count + " totem.";
         } else {
            String var5 = player.method_5477().getString();
            message = var5 + " has popped " + count + " totems.";
         }
      } else {
         message = rawMessage.replace("%player%", player.method_5477().getString()).replace("%pop%", String.valueOf(count));
      }

      this.sendMessage(message);
   }

   public void sendMessage(String message) {
      if (mc.field_1724 != null && mc.method_1562() != null) {
         String randomString = this.generateRandomString(this.randomChars.getValueInt());
         if (!randomString.isEmpty()) {
            message = message + " " + randomString;
         }

         mc.method_1562().method_45729(message);
      }
   }

   private String generateRandomString(int length) {
      StringBuilder sb = new StringBuilder(length);

      for(int i = 0; i < length; ++i) {
         int index = this.random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length());
         sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(index));
      }

      return sb.toString();
   }
}
