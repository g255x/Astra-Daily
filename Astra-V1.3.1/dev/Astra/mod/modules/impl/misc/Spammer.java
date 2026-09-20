package dev.Astra.mod.modules.impl.misc;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import net.minecraft.class_634;
import net.minecraft.class_640;

public class Spammer extends Module {
   private static final String CHARACTERS = "欢迎使用_Kushen_自研的Astra客户端";
   public final BooleanSetting checkSelf = this.add(new BooleanSetting("CheckSelf", false));
   final StringSetting message = this.add(new StringSetting("Message", ""));
   private final Random random = new Random();
   private final SliderSetting randoms = this.add(new SliderSetting("Random", (double)3.0F, (double)0.0F, (double)20.0F, (double)1.0F));
   private final SliderSetting delay = this.add((new SliderSetting("Delay", (double)5.0F, (double)0.0F, (double)60.0F, 0.1)).setSuffix("s"));
   private final BooleanSetting tellMode = this.add(new BooleanSetting("RandomWhisper", false));
   private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
   private final Timer timer = new Timer();

   public Spammer() {
      super("Spammer", Module.Category.Misc);
      this.setChinese("自动刷屏");
   }

   public void onLogout() {
      if (this.autoDisable.getValue()) {
         this.disable();
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passedS(this.delay.getValue())) {
         this.timer.reset();
         Object randomString = this.generateRandomString(this.randoms.getValueInt());
         if (!((String)randomString).isEmpty()) {
            randomString = " " + (String)randomString;
         }

         if (this.tellMode.getValue()) {
            Collection players = mc.method_1562().method_2880();
            ArrayList list = new ArrayList(players);
            int size = list.size();
            if (size == 0) {
               return;
            }

            class_640 playerListEntry = (class_640)list.get(this.random.nextInt(size));

            for(int i = 0; this.checkSelf.getValue() && Objects.equals(playerListEntry.method_2966().getName(), mc.field_1724.method_7334().getName()); playerListEntry = (class_640)list.get(this.random.nextInt(size))) {
               if (i > 50) {
                  return;
               }

               ++i;
            }

            class_634 var10000 = mc.method_1562();
            String var10001 = playerListEntry.method_2966().getName();
            var10000.method_45730("tell " + var10001 + " " + this.message.getValue() + (String)randomString);
         } else if (this.message.getValue().startsWith("/")) {
            class_634 var8 = mc.method_1562();
            String var10 = this.message.getValue().replaceFirst("/", "");
            var8.method_45731(var10 + (String)randomString);
         } else {
            class_634 var9 = mc.method_1562();
            String var11 = this.message.getValue();
            var9.method_45729(var11 + (String)randomString);
         }

      }
   }

   private String generateRandomString(int LENGTH) {
      StringBuilder sb = new StringBuilder(LENGTH);

      for(int i = 0; i < LENGTH; ++i) {
         int index = this.random.nextInt("欢迎使用_Kushen_自研的Astra客户端".length());
         sb.append("欢迎使用_Kushen_自研的Astra客户端".charAt(index));
      }

      return sb.toString();
   }
}
