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
   public final List<String> sex = List.of("呐呐~杂鱼哥哥不会这样就被捉弄的不会说话了吧♡", "嘻嘻~杂鱼哥哥不会以为竖个大拇哥就能欺负我了吧~不会吧♡不会吧♡", "杂鱼哥哥怎么可能欺负得了别人呢~只能欺负自己哦♡~", "哥哥真是好欺负啊♡嘻嘻~", "哎♡~杂鱼说话就是无趣唉~", "呐呐~杂鱼哥哥发这个是想教育我吗~嘻嘻~怎么可能啊♡", "什么嘛~废柴哥哥会想这种事情啊~唔呃", "把你肮脏的目光拿开啦~很恶心哦♡", "咱的期待就是被你这样的笨蛋破坏了~♡");
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

                  String playerName = player.method_5477().getString();
                  int popCount = (Integer)Astra.POP.popContainer.getOrDefault(playerName, 0);
                  switch (((Type)this.type.getValue()).ordinal()) {
                     case 0:
                        String botMsg = (String)this.bot.get(this.random.nextInt(this.bot.size())) + " " + playerName + randomSuffix;
                        mc.method_1562().method_45729(botMsg);
                        break;
                     case 1:
                        String var10 = this.message.getValue().replace("%player%", playerName).replace("%pop%", String.valueOf(popCount));
                        String customMsg = var10 + randomSuffix;
                        mc.method_1562().method_45729(customMsg);
                        break;
                     case 2:
                        if (!this.legit.isEmpty()) {
                           mc.method_1562().method_45729((String)this.legit.get(this.random.nextInt(this.legit.size())));
                        }
                        break;
                     case 3:
                        if (!this.sex.isEmpty()) {
                           String sexMsg = (String)this.sex.get(this.random.nextInt(this.sex.size())) + " " + playerName + randomSuffix;
                           mc.method_1562().method_45729(sexMsg);
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
      Legit,
      AutoSex;

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{Bot, Custom, Legit, AutoSex};
      }
   }
}
