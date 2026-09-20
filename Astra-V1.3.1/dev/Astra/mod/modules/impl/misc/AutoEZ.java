package dev.Astra.mod.modules.impl.misc;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.DeathEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.util.List;
import java.util.Random;
import net.minecraft.class_1657;

public class AutoEZ extends Module {
   private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   public final List<String> bot = List.of("鼠标明天到，触摸板打的", "转人工", "收徒", "不收徒", "有真人吗", "墨镜上车", "素材局", "不接单", "接单", "征婚", "4399?", "暂时不考虑打职业", "bot?", "叫你家长大人来打", "假肢上门安装", "浪费我的网费", "不收残疾人", "下课", "自己找差距", "不接代", "代+", "这样的治好了也流口水", "人机", "人机怎么调难度啊", "只收不被0封的", "Bot吗这是", "领养", "纳亲", "正视差距", "近亲繁殖?", "我玩的是新手教程?", "来调灵敏度的", "来调参数的", "小号", "不是本人别加", "下次记得晚点玩", "随便玩玩,不带妹", "打1上车");
   public final List<String> legit = List.of("good game", "good fight", "well played", "good luck", "have fun", "nice shot", "nice try", "respect", "you fought well", "thanks for the fight", "that was fun", "well fought", "nice fight", "good fight mate", "honorable fight", "fair fight", "respectable", "you are skilled", "nice moves", "impressive", "well done", "great fight", "awesome fight", "enjoyed that", "until next time", "you played well", "good duel", "nice duel", "respected opponent", "worthy opponent");
   private final EnumSetting<Type> type;
   final StringSetting message;
   final Random random;
   private final SliderSetting range;
   private final SliderSetting randoms;

   public AutoEZ() {
      super("AutoEZ", Module.Category.Misc);
      this.type = this.add(new EnumSetting("Type", AutoEZ.Type.Bot));
      this.message = this.add(new StringSetting("Message", "EZ %player%", () -> this.type.getValue() == AutoEZ.Type.Custom));
      this.random = new Random();
      this.range = this.add(new SliderSetting("Range", (double)10.0F, (double)0.0F, (double)20.0F, 0.1));
      this.randoms = this.add(new SliderSetting("Random", (double)3.0F, (double)0.0F, (double)20.0F, (double)1.0F));
      this.setChinese("自动嘲讽");
   }

   @EventListener
   public void onDeath(DeathEvent event) {
      if (mc.field_1724 != null && mc.method_1562() != null) {
         class_1657 player = event.getPlayer();
         if (player != null) {
            if (player != mc.field_1724 && !Astra.FRIEND.isFriend(player)) {
               if (!(this.range.getValue() > (double)0.0F) || !((double)mc.field_1724.method_5739(player) > this.range.getValue())) {
                  String randomSuffix = "";
                  if (this.type.getValue() != AutoEZ.Type.Legit) {
                     int len = this.randoms.getValueInt();
                     if (len > 0) {
                        String var10000 = this.generateRandomString(len);
                        randomSuffix = " " + var10000;
                     }
                  }

                  switch (((Type)this.type.getValue()).ordinal()) {
                     case 0:
                        String var8 = (String)this.bot.get(this.random.nextInt(this.bot.size()));
                        String botMsg = var8 + " " + player.method_5477().getString() + randomSuffix;
                        mc.method_1562().method_45729(botMsg);
                        break;
                     case 1:
                        String var7 = this.message.getValue().replace("%player%", player.method_5477().getString());
                        String customMsg = var7 + randomSuffix;
                        mc.method_1562().method_45729(customMsg);
                        break;
                     case 2:
                        if (!this.legit.isEmpty()) {
                           mc.method_1562().method_45729((String)this.legit.get(this.random.nextInt(this.legit.size())));
                        }
                  }

               }
            }
         }
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

   public static enum Type {
      Bot,
      Custom,
      Legit;

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{Bot, Custom, Legit};
      }
   }
}
