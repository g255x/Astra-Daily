package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.TotemEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.class_1657;

public class PopEz extends Module {
   public static PopEz INSTANCE;
   private final SliderSetting randomChars = this.add(new SliderSetting("RandomChars", (double)3.0F, (double)0.0F, (double)20.0F, (double)1.0F));
   public final BooleanSetting slowSend = this.add(new BooleanSetting("SlowSend", false));
   Random random = new Random();
   Timer timer = new Timer();
   private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   private Map<Integer, Integer> popQueue = new HashMap();

   public PopEz() {
      super("PopEz", Module.Category.Misc);
      this.setChinese("POP嘲讽");
      INSTANCE = this;
   }

   @EventListener
   public void onTotem(TotemEvent event) {
      class_1657 player = event.getPlayer();
      if (player != null && !player.equals(mc.field_1724) && !Alien.FRIEND.isFriend(player)) {
         int l_Count = 1;
         if (Alien.POP.popContainer.containsKey(player.method_5477().getString())) {
            l_Count = (Integer)Alien.POP.popContainer.get(player.method_5477().getString());
         }

         if (this.slowSend.getValue()) {
            this.popQueue.put(player.method_5628(), l_Count);
         } else {
            String message = l_Count == 1 ? player.method_5477().getString() + " has popped " + l_Count + " totem." : player.method_5477().getString() + " has popped " + l_Count + " totems.";
            this.sendMessage(message, player.method_5628());
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

         for(class_1657 p : mc.field_1687.method_18456()) {
            if (p.method_5628() == playerId) {
               player = p;
               break;
            }
         }

         if (player != null) {
            String message = popCount == 1 ? player.method_5477().getString() + " has popped " + popCount + " totem." : player.method_5477().getString() + " has popped " + popCount + " totems.";
            this.sendMessage(message, playerId);
            this.popQueue.remove(playerId);
         } else {
            this.popQueue.remove(playerId);
         }
      }

   }

   public void sendMessage(String message, int id) {
      if (!nullCheck() && mc.field_1724 != null && mc.field_1724.field_3944 != null) {
         String randomString = this.generateRandomString(this.randomChars.getValueInt());
         if (!randomString.isEmpty()) {
            message = message + " " + randomString;
         }

         mc.field_1724.field_3944.method_45729(message);
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
