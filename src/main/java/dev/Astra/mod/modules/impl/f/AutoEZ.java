package dev.Astra.mod.modules.impl.f;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.DeathEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import j2cc.Nativeify;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
import java.util.Random;

public class AutoEZ extends Module {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public final List<String> sex = List.of(
            "呐呐~杂鱼哥哥不会这样就被捉弄的不会说话了吧♡",
            "嘻嘻~杂鱼哥哥不会以为竖个大拇哥就能欺负我了吧~不会吧♡不会吧♡",
            "杂鱼哥哥怎么可能欺负得了别人呢~只能欺负自己哦♡~",
            "哥哥真是好欺负啊♡嘻嘻~",
            "哎♡~杂鱼说话就是无趣唉~",
            "呐呐~杂鱼哥哥发这个是想教育我吗~嘻嘻~怎么可能啊♡",
            "什么嘛~废柴哥哥会想这种事情啊~唔呃",
            "把你肮脏的目光拿开啦~很恶心哦♡",
            "咱的期待就是被你这样的笨蛋破坏了~♡",
            "唔~哥哥这是害羞了吗♡脸都红了呢~",
            "杂鱼就是杂鱼呢♡再练几年吧~",
            "咦~哥哥还没有被虐够吗♡还想来一次？",
            "呐~被打败的杂鱼哥哥在想什么呢♡",
            "哎嘿~这次又是哥哥输了呢♡真可怜~",
            "杂鱼就是好欺负~手感真好呢♡",
            "呼呼~哥哥是不是该叫姐姐了♡",
            "啊啦~这么弱的哥哥怎么好意思来打我呢♡",
            "真是的~杂鱼就是杂鱼~永远赢不了咱♡",
            "唔噗噗~哥哥的战术好可爱哦♡像小孩子一样~",
            "呐呐~要不要姐姐教你怎么玩呀♡杂鱼哥哥~",
            "呜呜~哥哥怎么又输了呀♡咱都心疼你了呢~",
            "杂鱼哥哥的手速是不是和脑子一样慢呀♡",
            "哎嘿~哥哥这波操作咱给零分哦♡不及格呢~",
            "呐~哥哥是不是只会按左键呀♡好可怜哦~",
            "嘻嘻~哥哥的走位像在跳广场舞呢♡真有趣~",
            "唔~哥哥是不是把显示器关着打的呀♡",
            "杂鱼哥哥~你的队友是不是在骂你了呀♡",
            "啊啦~哥哥这么菜，家里人知道吗♡",
            "呐呐~要不要咱给你介绍个师傅呀♡杂鱼哥哥~",
            "呼呼~哥哥的装备真好呢♡可惜技术配不上哦~",
            "咦~哥哥怎么不说话啦♡是不是被打自闭了呀~",
            "嘻嘻~哥哥这个段位是不是代打上来的呀♡",
            "杂鱼哥哥~你的鼠标垫是不是该换了呢♡",
            "唔噗噗~哥哥是不是第一次玩这个游戏呀♡",
            "呐~哥哥这么弱，怎么还敢来PVP呀♡",
            "哎嘿~咱都不忍心打你了呢♡太可怜啦~",
            "杂鱼哥哥~要不咱让你一只手吧♡",
            "啊啦~哥哥是不是网络卡了呀♡怎么站着不动呢~",
            "呼呼~哥哥的技术和你的名字一样可爱呢♡",
            "呐呐~哥哥再练一百年也打不过咱哦♡"
    );
    //XD deepseek还会写这些xswlhhh(ShYin
    public final List<String> bot = List.of(
            "鼠标明天到，触摸板打的",
            "转人工",
            "收徒",
            "不收徒",
            "有真人吗",
            "墨镜上车",
            "素材局",
            "不接单",
            "接单",
            "征婚",
            "4399?",
            "暂时不考虑打职业",
            "bot?",
            "叫你家长大人来打",
            "假肢上门安装",
            "浪费我的网费",
            "不收残疾人",
            "下课",
            "自己找差距",
            "不接代",
            "代+",
            "这样的治好了也流口水",
            "人机",
            "人机怎么调难度啊",
            "只收不被0封的",
            "Bot吗这是",
            "领养",
            "纳亲",
            "正视差距",
            "近亲繁殖?",
            "我玩的是新手教程?",
            "来调灵敏度的",
            "来调参数的",
            "小号",
            "不是本人别加",
            "下次记得晚点玩",
            "打1上车",
            "别打了,回家写作业吧",
            "这就破防了?",
            "给你机会你也不中用",
            "再来一局吧,给你留点面子",
            "这就是你的实力吗",
            "是不是鼠标没充电",
            "让你一只手都打得过",
            "手残党建议卸载",
            "练练再来吧,打你太无聊",
            "这就是所谓的‘高手’?",
            "打你都掉分了,唉",
            "别浪费我时间",
            "看你打游戏的样子好可怜"
    );

    public final List<String> legit = List.of(
            "good game",
            "good fight",
            "well played",
            "good luck",
            "have fun",
            "nice shot",
            "nice try",
            "respect",
            "you fought well",
            "thanks for the fight",
            "that was fun",
            "well fought",
            "nice fight",
            "good fight mate",
            "honorable fight",
            "fair fight",
            "respectable",
            "you are skilled",
            "nice moves",
            "impressive",
            "well done",
            "great fight",
            "awesome fight",
            "enjoyed that",
            "until next time",
            "you played well",
            "good duel",
            "nice duel",
            "respected opponent",
            "worthy opponent",
            "gg wp",
            "gg ez",
            "that was a close one",
            "solid fight",
            "you almost had me",
            "better luck next time",
            "well deserved",
            "i respect the effort",
            "great sportsmanship",
            "looking forward to the rematch",
            "well executed",
            "nice combo",
            "good timing",
            "that was intense",
            "amazing performance"
    );

    private final EnumSetting<Type> type = this.add(new EnumSetting<>("Type", Type.Bot));
    final StringSetting message = this.add(new StringSetting("Message", "EZ %player%", () -> this.type.getValue() == Type.Custom));
    final Random random = new Random();
    private final SliderSetting range = this.add(new SliderSetting("Range", 10.0, 0.0, 20.0, 0.1));
    private final SliderSetting randoms = this.add(new SliderSetting("Random", 3.0, 0.0, 20.0, 1.0));

    public AutoEZ() {
        super("AutoEZ", Module.Category.Misc);
        this.setChinese("自动嘲讽");
    }

    // ⚠️ @EventListener，不能加 @Nativeify
    @EventListener
    public void onDeath(DeathEvent event) {
        // 关键空值保护
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        PlayerEntity player = event.getPlayer();
        if (player == null) return;

        // 不嘲讽自己或好友
        if (player == mc.player || Astra.FRIEND.isFriend(player)) return;

        // 范围限制
        if (range.getValue() > 0.0 && mc.player.distanceTo(player) > range.getValue()) return;

        // 生成随机后缀（仅 Bot / Custom / AutoSex 模式）
        String randomSuffix = "";
        if (type.getValue() != Type.Legit) {
            int len = randoms.getValueInt();
            if (len > 0) {
                randomSuffix = " " + generateRandomString(len);
            }
        }

        // 发送消息
        String playerName = player.getName().getString();
        int popCount = Astra.POP.popContainer.getOrDefault(playerName, 0);
        switch (type.getValue()) {
            case Bot:
                String botMsg = bot.get(random.nextInt(bot.size())) + " " + playerName + randomSuffix;
                mc.getNetworkHandler().sendChatMessage(botMsg);
                break;
            case Custom:
                String customMsg = message.getValue().replace("%player%", playerName).replace("%pop%", String.valueOf(popCount)) + randomSuffix;
                mc.getNetworkHandler().sendChatMessage(customMsg);
                break;
            case Legit:
                if (!legit.isEmpty()) {
                    mc.getNetworkHandler().sendChatMessage(legit.get(random.nextInt(legit.size())));
                }
                break;
            case AutoSex:
                if (!sex.isEmpty()) {
                    String sexMsg = sex.get(random.nextInt(sex.size())) + " " + playerName + randomSuffix;
                    mc.getNetworkHandler().sendChatMessage(sexMsg);
                }
                break;
        }
    }

    // ==================== 核心算法：随机字符串生成 ====================
    // ✅ 加 @Nativeify：循环 + StringBuilder + 字符索引
    //    逆向者看不到"如何生成随机后缀"
    @Nativeify
    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static enum Type {
        Bot,
        Custom,
        Legit,
        AutoSex;
    }
}