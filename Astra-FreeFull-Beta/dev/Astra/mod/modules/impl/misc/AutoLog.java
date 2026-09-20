package dev.Astra.mod.modules.impl.misc;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.TotemEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_2661;
import net.minecraft.class_2708;
import net.minecraft.class_2868;

public class AutoLog extends Module {
   public static boolean loggedOut = false;
   private final Random random = new Random();
   private final Timer lagTimer = new Timer();
   private final List<String> exitMessages = List.of("猫踩到关机键了", "该吃饭了，先下了", "外卖到了，88", "网费到期了，告辞", "突然有事，下次再打", "电脑蓝屏了，G", "停电了，不是打不过", "键盘进水了，寄", "显示器冒烟了", "去收衣服，先撤", "快递到了，拿一下", "水烧开了，关火去", "门铃响了，88", "家里来人了，不玩了", "肚子疼，厕所在召唤", "来电话了，接一下", "电脑被抢了", "朋友拉我打别的", "突然困了，睡觉去", "该遛狗了，溜了", "猫把网线咬断了", "楼下有情况，跑路了", "点的奶茶到了", "作业还没写，溜了", "该洗澡了，先下", "手机没电了，88", "突然想出去走走", "该做饭了，告辞", "电视剧更新了，去看", "忘了关火，先去厨房");
   private final BooleanSetting logOnEnable = this.add(new BooleanSetting("LogOnEnable", false));
   private final BooleanSetting onPop = this.add(new BooleanSetting("OnPop", true));
   private final BooleanSetting lowArmor = this.add((new BooleanSetting("LowArmor", true)).setParent());
   private final SliderSetting durabilityThreshold;
   private final BooleanSetting totemLess;
   private final SliderSetting totems;
   private final BooleanSetting serverNotResponding;
   private final SliderSetting serverTimeout;
   private final BooleanSetting autoDisable;
   private final BooleanSetting showReason;
   private final BooleanSetting sendExitMessage;
   private final EnumSetting<ExitMode> exitMode;
   private final StringSetting fixedMessage;

   public AutoLog() {
      super("AutoLog", Module.Category.Misc);
      BooleanSetting var10009 = this.lowArmor;
      Objects.requireNonNull(var10009);
      this.durabilityThreshold = this.add((new SliderSetting("DurabilityThreshold", (double)5.0F, (double)0.0F, (double)100.0F, (double)1.0F, var10009::isOpen)).setSuffix("%"));
      this.totemLess = this.add((new BooleanSetting("TotemLess", true)).setParent());
      var10009 = this.totemLess;
      Objects.requireNonNull(var10009);
      this.totems = this.add(new SliderSetting("Totems", (double)2.0F, (double)0.0F, (double)10.0F, (double)1.0F, var10009::isOpen));
      this.serverNotResponding = this.add((new BooleanSetting("ServerNotResponding", true)).setParent());
      var10009 = this.serverNotResponding;
      Objects.requireNonNull(var10009);
      this.serverTimeout = this.add((new SliderSetting("ServerTimeout", 0.2, (double)0.0F, (double)5.0F, 0.1, var10009::isOpen)).setSuffix("s"));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", true));
      this.showReason = this.add(new BooleanSetting("ShowReason", false));
      this.sendExitMessage = this.add((new BooleanSetting("SendExitMessage", true)).setParent());
      ExitMode var10005 = AutoLog.ExitMode.Random;
      BooleanSetting var10006 = this.sendExitMessage;
      Objects.requireNonNull(var10006);
      this.exitMode = this.add(new EnumSetting("ExitMode", var10005, var10006::isOpen));
      this.fixedMessage = this.add(new StringSetting("FixedMessage", "客户端崩了", () -> this.exitMode.getValue() == AutoLog.ExitMode.Fixed));
      this.setChinese("自动下线");
   }

   public void onEnable() {
      if (this.logOnEnable.getValue()) {
         this.disconnect("Enabled");
      }

      this.lagTimer.reset();
   }

   @EventListener
   public void onPacketEvent(PacketEvent.Receive event) {
      this.lagTimer.reset();
      if (event.getPacket() instanceof class_2708) {
         this.lagTimer.reset();
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (mc.field_1724 != null && mc.method_1562() != null) {
         if (this.serverNotResponding.getValue()) {
            double timeout = this.serverTimeout.getValue();
            if (this.lagTimer.passedS(timeout)) {
               Object[] var10002 = new Object[]{(double)this.lagTimer.getMs() / (double)1000.0F};
               this.disconnect("Server not responding (" + String.format("%.1f", var10002) + "s)");
               return;
            }
         }

         if (this.totemLess.getValue()) {
            int totem = InventoryUtil.getItemCount(class_1802.field_8288);
            if ((double)totem <= this.totems.getValue()) {
               this.disconnect("You have too few totems (" + totem + ").");
               return;
            }
         }

         if (this.lowArmor.getValue()) {
            double threshold = this.durabilityThreshold.getValue();

            for(class_1799 armor : mc.field_1724.method_31548().field_7548) {
               if (!armor.method_7960()) {
                  double durability = (double)EntityUtil.getDamagePercent(armor);
                  if (!(durability >= threshold)) {
                     String var10001 = String.format("%.1f", durability);
                     this.disconnect("Your armor durability is " + var10001 + "% (below " + threshold + "%).");
                     return;
                  }
               }
            }
         }

      }
   }

   @EventListener
   public void onPop(TotemEvent event) {
      if (this.onPop.getValue() && event.getPlayer() == mc.field_1724) {
         this.disconnect("You popped 1 totem!");
      }

   }

   public void onLogout() {
      if (this.autoDisable.getValue()) {
         this.disable();
      }

   }

   private void disconnect(String reason) {
      loggedOut = true;
      if (this.sendExitMessage.getValue()) {
         String exitMsg = this.getExitMessage();
         if (exitMsg != null && !exitMsg.isEmpty()) {
            mc.method_1562().method_45729(exitMsg);
         }
      }

      mc.method_1562().method_52787(new class_2868(114514));
      if (this.showReason.getValue()) {
         mc.field_1724.field_3944.method_52781(new class_2661(class_2561.method_43470("[AutoLog] " + reason)));
      }

   }

   private String getExitMessage() {
      String msg = null;
      if (this.exitMode.getValue() == AutoLog.ExitMode.Random) {
         if (!this.exitMessages.isEmpty()) {
            msg = (String)this.exitMessages.get(this.random.nextInt(this.exitMessages.size()));
         }
      } else if (this.exitMode.getValue() == AutoLog.ExitMode.Fixed) {
         msg = this.fixedMessage.getValue();
      }

      return msg != null && !msg.isEmpty() ? "§7[§cAutoLog§7] §f" + msg : null;
   }

   public static enum ExitMode {
      Random,
      Fixed;

      // $FF: synthetic method
      private static ExitMode[] $values() {
         return new ExitMode[]{Random, Fixed};
      }
   }
}
