package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.core.impl.BreakManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_1657;
import net.minecraft.class_1739;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_4587;
import net.minecraft.class_6880;

public class BreakESP extends Module {
   public static BreakESP INSTANCE;
   private final BooleanSetting progress = this.add(new BooleanSetting("Progress", true));
   private final SliderSetting damage = this.add(new SliderSetting("Damage", (double)1.0F, (double)0.0F, (double)2.0F, 0.01));
   private final ColorSetting box = this.add((new ColorSetting("Box", new Color(198, 176, 12, 255))).injectBoolean(true));
   private final ColorSetting fill = this.add((new ColorSetting("Fill", new Color(198, 176, 12, 78))).injectBoolean(true));
   private final ColorSetting boxFriend = this.add((new ColorSetting("FriendBox", new Color(30, 45, 169, 255))).injectBoolean(true));
   private final ColorSetting fillFriend = this.add((new ColorSetting("FriendFill", new Color(30, 45, 169, 78))).injectBoolean(true));
   private final EnumSetting<Easing> ease;
   private final BooleanSetting second;
   private final ColorSetting secondBox;
   private final ColorSetting secondFill;
   final DecimalFormat df;
   final Color startColor;
   final Color endColor;
   final Color doubleColor;

   public BreakESP() {
      super("BreakESP", Module.Category.Render);
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut));
      this.second = this.add(new BooleanSetting("Second", true));
      this.secondBox = this.add((new ColorSetting("SecondBox", new Color(255, 255, 255, 255))).injectBoolean(true));
      this.secondFill = this.add((new ColorSetting("SecondFill", new Color(255, 255, 255, 100))).injectBoolean(true));
      this.df = new DecimalFormat("0.0");
      this.startColor = new Color(255, 6, 6);
      this.endColor = new Color(0, 255, 12);
      this.doubleColor = new Color(255, 179, 96);
      this.setChinese("挖掘显示");
      INSTANCE = this;
   }

   private Color getFillColor(class_1657 player) {
      return Alien.FRIEND.isFriend(player) ? this.fillFriend.getValue() : this.fill.getValue();
   }

   private Color getBoxColor(class_1657 player) {
      return Alien.FRIEND.isFriend(player) ? this.boxFriend.getValue() : this.box.getValue();
   }

   public void onRender3D(class_4587 matrixStack) {
      for(BreakManager.BreakData breakData : Alien.BREAK.breakMap.values()) {
         if (breakData != null && breakData.getEntity() != null) {
            class_1657 player = (class_1657)breakData.getEntity();
            double size = (double)0.5F * ((double)1.0F - breakData.fade.ease(this.ease.getValue()));
            class_238 cbox = (new class_238(breakData.pos)).method_1002(size, size, size).method_1002(-size, -size, -size);
            if (this.fill.booleanValue) {
               Render3DUtil.drawFill(matrixStack, cbox, this.getFillColor(player));
            }

            if (this.box.booleanValue) {
               Render3DUtil.drawBox(matrixStack, cbox, this.getBoxColor(player));
            }

            Render3DUtil.drawText3D(player.method_5477().getString(), breakData.pos.method_46558().method_1031((double)0.0F, this.progress.getValue() ? 0.15 : (double)0.0F, (double)0.0F), -1);
            if (this.progress.getValue()) {
               Render3DUtil.drawText3D(class_2561.method_30163(breakData.failed ? "§4Failed" : (breakData.complete ? "Broke" : this.df.format(Math.min((double)1.0F, (double)breakData.timer.getMs() / breakData.breakTime) * (double)100.0F))), breakData.pos.method_46558().method_1031((double)0.0F, -0.15, (double)0.0F), (double)0.0F, (double)0.0F, (double)1.0F, breakData.complete ? (mc.field_1687.method_22347(breakData.pos) ? this.endColor : this.startColor) : ColorUtil.fadeColor(this.startColor, this.endColor, (double)breakData.timer.getMs() / breakData.breakTime));
            }
         }
      }

      if (this.second.getValue()) {
         for(int i : Alien.BREAK.doubleMap.keySet()) {
            BreakManager.BreakData breakDatax = (BreakManager.BreakData)Alien.BREAK.doubleMap.get(i);
            if (breakDatax != null && breakDatax.getEntity() != null && !mc.field_1687.method_22347(breakDatax.pos)) {
               BreakManager.BreakData singleBreakData = (BreakManager.BreakData)Alien.BREAK.breakMap.get(i);
               if (singleBreakData == null || !singleBreakData.pos.equals(breakDatax.pos)) {
                  double sizex = (double)0.5F * ((double)1.0F - breakDatax.fade.ease(this.ease.getValue()));
                  class_238 cboxx = (new class_238(breakDatax.pos)).method_1002(sizex, sizex, sizex).method_1002(-sizex, -sizex, -sizex);
                  if (this.secondFill.booleanValue) {
                     Render3DUtil.drawFill(matrixStack, cboxx, this.secondFill.getValue());
                  }

                  if (this.secondBox.booleanValue) {
                     Render3DUtil.drawBox(matrixStack, cboxx, this.secondBox.getValue());
                  }

                  Render3DUtil.drawText3D(breakDatax.getEntity().method_5477().getString(), breakDatax.pos.method_46558().method_1031((double)0.0F, 0.15, (double)0.0F), -1);
                  Render3DUtil.drawText3D("Double", breakDatax.pos.method_46558().method_1031((double)0.0F, -0.15, (double)0.0F), this.doubleColor.getRGB());
               }
            } else {
               Alien.BREAK.doubleMap.remove(i);
            }
         }
      }

   }

   public static double getBreakTime(class_2338 pos, boolean extraBreak) {
      int slot = getTool(pos);
      if (slot == -1) {
         slot = mc.field_1724.method_31548().field_7545;
      }

      return getBreakTime(pos, slot, extraBreak ? (double)1.0F : INSTANCE.damage.getValue());
   }

   static int getTool(class_2338 pos) {
      AtomicInteger slot = new AtomicInteger();
      slot.set(-1);
      float CurrentFastest = 1.0F;

      for(Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
         if (!(((class_1799)entry.getValue()).method_7909() instanceof class_1739)) {
            float digSpeed = (float)class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get(), (class_1799)entry.getValue());
            float destroySpeed = ((class_1799)entry.getValue()).method_7924(mc.field_1687.method_8320(pos));
            if (digSpeed + destroySpeed > CurrentFastest) {
               CurrentFastest = digSpeed + destroySpeed;
               slot.set((Integer)entry.getKey());
            }
         }
      }

      return slot.get();
   }

   static double getBreakTime(class_2338 pos, int slot, double damage) {
      return (double)(1.0F / getBlockStrength(pos, mc.field_1724.method_31548().method_5438(slot)) / 20.0F * 1000.0F) * damage;
   }

   static float getBlockStrength(class_2338 position, class_1799 itemStack) {
      class_2680 state = mc.field_1687.method_8320(position);
      float hardness = state.method_26214(mc.field_1687, position);
      if (hardness < 0.0F) {
         return 0.0F;
      } else {
         float i = state.method_29291() && !itemStack.method_7951(state) ? 100.0F : 30.0F;
         return getDigSpeed(state, itemStack) / hardness / i;
      }
   }

   static float getDigSpeed(class_2680 state, class_1799 itemStack) {
      float digSpeed = getDestroySpeed(state, itemStack);
      if (digSpeed > 1.0F) {
         int efficiencyModifier = class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get(), itemStack);
         if (efficiencyModifier > 0 && !itemStack.method_7960()) {
            digSpeed += (float)(StrictMath.pow((double)efficiencyModifier, (double)2.0F) + (double)1.0F);
         }
      }

      return digSpeed < 0.0F ? 0.0F : digSpeed;
   }

   static float getDestroySpeed(class_2680 state, class_1799 itemStack) {
      float destroySpeed = 1.0F;
      if (itemStack != null && !itemStack.method_7960()) {
         destroySpeed *= itemStack.method_7924(state);
      }

      return destroySpeed;
   }
}
