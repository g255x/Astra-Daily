package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PlaySoundEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.event.KeyEvent;
import java.util.Objects;
import net.minecraft.class_1113;
import net.minecraft.class_1536;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2189;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_6880;

public class AutoTool extends Module {
   private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true));
   private final BooleanSetting fish = this.add((new BooleanSetting("Fish", true)).setParent());
   private final BooleanSetting autoCast;
   private final SliderSetting ticksAutoCast;
   private final SliderSetting ticksCatch;
   private final SliderSetting ticksThrow;
   private final BooleanSetting splashDetection;
   private final SliderSetting splashDetectionRange;
   private boolean ticksEnabled;
   private int ticksToRightClick;
   private int ticksData;
   private int autoCastTimer;
   private boolean autoCastEnabled;
   private int autoCastCheckTimer;

   public AutoTool() {
      super("AutoTool", Module.Category.Player);
      BooleanSetting var10006 = this.fish;
      Objects.requireNonNull(var10006);
      this.autoCast = this.add(new BooleanSetting("AutoCast", true, var10006::isOpen));
      BooleanSetting var10008 = this.fish;
      Objects.requireNonNull(var10008);
      this.ticksAutoCast = this.add(new SliderSetting("TicksAutoCast", 10, 0, 60, var10008::isOpen));
      var10008 = this.fish;
      Objects.requireNonNull(var10008);
      this.ticksCatch = this.add(new SliderSetting("TicksCatch", 6, 0, 60, var10008::isOpen));
      var10008 = this.fish;
      Objects.requireNonNull(var10008);
      this.ticksThrow = this.add(new SliderSetting("TicksThrow", 14, 0, 60, var10008::isOpen));
      var10006 = this.fish;
      Objects.requireNonNull(var10006);
      this.splashDetection = this.add(new BooleanSetting("SplashDetection", false, var10006::isOpen));
      var10008 = this.fish;
      Objects.requireNonNull(var10008);
      this.splashDetectionRange = this.add(new SliderSetting("DetectionRange", 10, 0, 60, var10008::isOpen));
      this.setChinese("自动工具");
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.autoFish();
      this.autoTool();
   }

   public void autoTool() {
      if (this.mine.getValue()) {
         class_239 var2 = mc.field_1765;
         if (var2 instanceof class_3965) {
            class_3965 result = (class_3965)var2;
            class_2338 var4 = result.method_17777();
            if (!mc.field_1687.method_22347(var4)) {
               int tool = getTool(var4);
               if (tool != -1 && mc.field_1690.field_1886.method_1434()) {
                  mc.field_1724.method_31548().field_7545 = tool;
               }
            }
         }
      }

   }

   public boolean onEnable() {
      this.ticksEnabled = false;
      this.autoCastEnabled = false;
      this.autoCastCheckTimer = 0;
      return false;
   }

   @EventListener
   private void onPlaySound(PlaySoundEvent event) {
      if (!nullCheck() && this.fish.getValue()) {
         class_1113 p = event.sound;
         class_1536 b = mc.field_1724.field_7513;
         if (b != null && p.method_4775().method_12832().equals("entity.fishing_bobber.splash") && (!this.splashDetection.getValue() || MathUtil.distance(b.method_23317(), b.method_23318(), b.method_23321(), p.method_4784(), p.method_4779(), p.method_4778()) <= this.splashDetectionRange.getValue())) {
            this.ticksEnabled = true;
            this.ticksToRightClick = this.ticksCatch.getValueInt();
            this.ticksData = 0;
         }
      }

   }

   public void autoFish() {
      if (this.fish.getValue()) {
         if (this.autoCastCheckTimer <= 0) {
            this.autoCastCheckTimer = 30;
            if (this.autoCast.getValue() && !this.ticksEnabled && !this.autoCastEnabled && mc.field_1724.field_7513 == null && mc.field_1724.method_6047().method_7909() == class_1802.field_8378) {
               this.autoCastTimer = 0;
               this.autoCastEnabled = true;
            }
         } else {
            --this.autoCastCheckTimer;
         }

         if (this.autoCastEnabled) {
            ++this.autoCastTimer;
            if ((double)this.autoCastTimer > this.ticksAutoCast.getValue()) {
               this.autoCastEnabled = false;
               mc.method_1583();
            }
         }

         if (this.ticksEnabled && this.ticksToRightClick <= 0) {
            if (this.ticksData == 0) {
               mc.method_1583();
               this.ticksToRightClick = this.ticksThrow.getValueInt();
               this.ticksData = 1;
            } else if (this.ticksData == 1) {
               mc.method_1583();
               this.ticksEnabled = false;
            }
         }

         --this.ticksToRightClick;
      }

   }

   @EventListener
   private void onKey(KeyEvent event) {
      if (mc.field_1690.field_1904.method_1434()) {
         this.ticksEnabled = false;
      }

   }

   public static int getTool(class_2338 pos) {
      int index = -1;
      float CurrentFastest = 1.0F;

      for(int i = 0; i < 9; ++i) {
         class_1799 stack = mc.field_1724.method_31548().method_5438(i);
         if (stack != class_1799.field_8037) {
            float digSpeed = (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get(), stack);
            float destroySpeed = stack.method_7924(mc.field_1687.method_8320(pos));
            if (mc.field_1687.method_8320(pos).method_26204() instanceof class_2189) {
               return -1;
            }

            if (digSpeed + destroySpeed > CurrentFastest) {
               CurrentFastest = digSpeed + destroySpeed;
               index = i;
            }
         }
      }

      return index;
   }
}
