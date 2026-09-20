package dev.Astra.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.player.AutoPearl;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_1684;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1771;
import net.minecraft.class_1776;
import net.minecraft.class_1779;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1803;
import net.minecraft.class_1823;
import net.minecraft.class_1828;
import net.minecraft.class_1835;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_6880;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class Trajectories extends Module {
   static class_4587 matrixStack;
   private final ColorSetting hand = this.add((new ColorSetting("Hand", new Color(255, 255, 255, 255))).injectBoolean(true));
   private final ColorSetting pearl = this.add((new ColorSetting("Pearl", new Color(255, 255, 255, 255))).injectBoolean(true));
   private final ColorSetting arrow = this.add((new ColorSetting("Arrow", new Color(255, 255, 255, 255))).injectBoolean(true));
   private final ColorSetting xp = this.add((new ColorSetting("XP", new Color(255, 255, 255, 255))).injectBoolean(true));

   public Trajectories() {
      super("Trajectories", Module.Category.Render);
      this.setChinese("抛物线预测");
   }

   public void onRender3D(class_4587 matrixStack) {
      if (!nullCheck()) {
         Trajectories.matrixStack = matrixStack;
         if (this.pearl.booleanValue || this.arrow.booleanValue || this.xp.booleanValue) {
            RenderSystem.disableDepthTest();

            for(class_1297 en : Astra.THREAD.getEntities()) {
               if (en instanceof class_1684 && this.pearl.booleanValue) {
                  this.calcTrajectory(en, this.pearl.getValue());
               }

               if (en instanceof class_1667 && this.arrow.booleanValue) {
                  this.calcTrajectory(en, this.arrow.getValue());
               }

               if (en instanceof class_1683 && this.xp.booleanValue) {
                  this.calcTrajectory(en, this.xp.getValue());
               }
            }

            RenderSystem.enableDepthTest();
         }

         if (this.hand.booleanValue) {
            if (!mc.field_1690.method_31044().method_31034()) {
               return;
            }

            class_1799 mainHand = mc.field_1724.method_6047();
            class_1799 offHand = mc.field_1724.method_6079();
            class_1268 hand;
            if (!(mainHand.method_7909() instanceof class_1753) && !(mainHand.method_7909() instanceof class_1764) && !this.isThrowable(mainHand.method_7909()) && !AutoPearl.INSTANCE.isOn()) {
               if (!(offHand.method_7909() instanceof class_1753) && !(offHand.method_7909() instanceof class_1764) && !this.isThrowable(offHand.method_7909())) {
                  return;
               }

               hand = class_1268.field_5810;
            } else {
               hand = class_1268.field_5808;
            }

            RenderSystem.disableDepthTest();
            boolean prev_bob = (Boolean)mc.field_1690.method_42448().method_41753();
            mc.field_1690.method_42448().method_41748(false);
            double x = MathUtil.interpolate(mc.field_1724.field_6014, mc.field_1724.method_23317(), (double)mc.method_60646().method_60637(true));
            double y = MathUtil.interpolate(mc.field_1724.field_6036, mc.field_1724.method_23318(), (double)mc.method_60646().method_60637(true));
            double z = MathUtil.interpolate(mc.field_1724.field_5969, mc.field_1724.method_23321(), (double)mc.method_60646().method_60637(true));
            if (offHand.method_7909() instanceof class_1764 && class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9108.method_58273()).method_40264(class_1893.field_9108).get(), offHand) != 0 || mainHand.method_7909() instanceof class_1764 && class_1890.method_8225((class_6880)mc.field_1687.method_30349().method_30530(class_1893.field_9108.method_58273()).method_40264(class_1893.field_9108).get(), mainHand) != 0) {
               this.calcTrajectory(hand == class_1268.field_5810 ? offHand.method_7909() : mainHand.method_7909(), mc.field_1724.method_36454() - 10.0F, x, y, z);
               this.calcTrajectory(hand == class_1268.field_5810 ? offHand.method_7909() : mainHand.method_7909(), mc.field_1724.method_36454(), x, y, z);
               this.calcTrajectory(hand == class_1268.field_5810 ? offHand.method_7909() : mainHand.method_7909(), mc.field_1724.method_36454() + 10.0F, x, y, z);
            } else {
               this.calcTrajectory(hand == class_1268.field_5810 ? offHand.method_7909() : mainHand.method_7909(), mc.field_1724.method_36454(), x, y, z);
            }

            mc.field_1690.method_42448().method_41748(prev_bob);
            RenderSystem.enableDepthTest();
         }

      }
   }

   private void calcTrajectory(class_1297 e, Color color) {
      double motionX = e.method_18798().field_1352;
      double motionY = e.method_18798().field_1351;
      double motionZ = e.method_18798().field_1350;
      if (motionX != (double)0.0F || motionY != (double)0.0F || motionZ != (double)0.0F) {
         double x = e.method_23317();
         double y = e.method_23318();
         double z = e.method_23321();

         for(int i = 0; i < 300; ++i) {
            class_243 lastPos = new class_243(x, y, z);
            if (mc.field_1687.method_8320(new class_2338((int)(x += motionX), (int)(y += motionY), (int)(z += motionZ))).method_26204() == class_2246.field_10382) {
               motionX *= 0.8;
               motionY *= 0.8;
               motionZ *= 0.8;
            } else {
               motionX *= 0.99;
               motionY *= 0.99;
               motionZ *= 0.99;
            }

            double var20;
            double var21;
            motionY = e instanceof class_1667 ? (var20 = motionY - (double)0.05F) : (var21 = motionY - (double)0.03F);
            class_243 pos = new class_243(x, y, z);
            if (mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3960.field_17559, class_242.field_1348, mc.field_1724)) != null && (mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3960.field_17559, class_242.field_1348, mc.field_1724)).method_17783() == class_240.field_1331 || mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3960.field_17559, class_242.field_1348, mc.field_1724)).method_17783() == class_240.field_1332) || y <= (double)-65.0F) {
               break;
            }

            int alpha = (int)MathUtil.clamp(255.0F * ((float)(i + 1) / 10.0F), 0.0F, 255.0F);
            Render3DUtil.drawLine(lastPos, pos, ColorUtil.injectAlpha(color, alpha));
         }

      }
   }

   private void calcTrajectory(class_1792 item, float yaw, double x, double y, double z) {
      y = y + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()) - 0.1000000014901161;
      if (item == mc.field_1724.method_6047().method_7909()) {
         x -= (double)(class_3532.method_15362(yaw / 180.0F * (float)Math.PI) * 0.16F);
         z -= (double)(class_3532.method_15374(yaw / 180.0F * (float)Math.PI) * 0.16F);
      } else {
         x += (double)(class_3532.method_15362(yaw / 180.0F * (float)Math.PI) * 0.16F);
         z += (double)(class_3532.method_15374(yaw / 180.0F * (float)Math.PI) * 0.16F);
      }

      float maxDist = this.getDistance(item);
      double motionX = (double)(-class_3532.method_15374(yaw / 180.0F * (float)Math.PI) * class_3532.method_15362(mc.field_1724.method_36455() / 180.0F * (float)Math.PI) * maxDist);
      double motionY = (double)(-class_3532.method_15374((mc.field_1724.method_36455() - (float)this.getThrowPitch(item)) / 180.0F * 3.141593F) * maxDist);
      double motionZ = (double)(class_3532.method_15362(yaw / 180.0F * (float)Math.PI) * class_3532.method_15362(mc.field_1724.method_36455() / 180.0F * (float)Math.PI) * maxDist);
      float power = (float)mc.field_1724.method_6048() / 20.0F;
      power = (power * power + power * 2.0F) / 3.0F;
      if (power > 1.0F) {
         power = 1.0F;
      }

      float getDistance = class_3532.method_15355((float)(motionX * motionX + motionY * motionY + motionZ * motionZ));
      motionX /= (double)getDistance;
      motionY /= (double)getDistance;
      motionZ /= (double)getDistance;
      float pow = (item instanceof class_1753 ? power * 2.0F : (item instanceof class_1764 ? 2.2F : 1.0F)) * this.getThrowVelocity(item);
      motionX *= (double)pow;
      motionY *= (double)pow;
      motionZ *= (double)pow;
      motionX += mc.field_1724.method_18798().method_10216();
      motionY += mc.field_1724.method_18798().method_10214();
      motionZ += mc.field_1724.method_18798().method_10215();

      for(int i = 0; i < 300; ++i) {
         class_243 lastPos = new class_243(x, y, z);
         if (mc.field_1687.method_8320(new class_2338((int)(x += motionX), (int)(y += motionY), (int)(z += motionZ))).method_26204() == class_2246.field_10382) {
            motionX *= 0.8;
            motionY *= 0.8;
            motionZ *= 0.8;
         } else {
            motionX *= 0.99;
            motionY *= 0.99;
            motionZ *= 0.99;
         }

         double var35;
         double var36;
         double var37;
         motionY = item instanceof class_1753 ? (var35 = motionY - (double)0.05F) : (mc.field_1724.method_6047().method_7909() instanceof class_1764 ? (var36 = motionY - (double)0.05F) : (var37 = motionY - (double)0.03F));
         class_243 pos = new class_243(x, y, z);

         for(class_1297 ent : Astra.THREAD.getEntities()) {
            if (!(ent instanceof class_1667) && !ent.equals(mc.field_1724) && ent.method_5829().method_994(new class_238(x - 0.3, y - 0.3, z - 0.3, x + 0.3, y + 0.3, z + 0.3))) {
               Render3DUtil.drawBox(matrixStack, ent.method_5829(), this.hand.getValue());
               break;
            }
         }

         class_3965 bhr;
         if ((bhr = mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3960.field_17559, class_242.field_1348, mc.field_1724))) != null && bhr.method_17783() == class_240.field_1332) {
            Render3DUtil.drawBox(matrixStack, new class_238(bhr.method_17777()), this.hand.getValue());
            break;
         }

         if (y <= (double)-65.0F) {
            break;
         }

         if (motionX != (double)0.0F || motionY != (double)0.0F || motionZ != (double)0.0F) {
            Render3DUtil.drawLine(lastPos, pos, this.hand.getValue());
         }
      }

   }

   private boolean isThrowable(class_1792 item) {
      return item instanceof class_1776 || item instanceof class_1835 || item instanceof class_1779 || item instanceof class_1823 || item instanceof class_1771 || item instanceof class_1828 || item instanceof class_1803;
   }

   private float getDistance(class_1792 item) {
      return item instanceof class_1753 ? 1.0F : 0.4F;
   }

   private float getThrowVelocity(class_1792 item) {
      if (!(item instanceof class_1828) && !(item instanceof class_1803)) {
         if (item instanceof class_1779) {
            return 0.59F;
         } else {
            return item instanceof class_1835 ? 2.0F : 1.5F;
         }
      } else {
         return 0.5F;
      }
   }

   private int getThrowPitch(class_1792 item) {
      return !(item instanceof class_1828) && !(item instanceof class_1803) && !(item instanceof class_1779) ? 0 : 20;
   }
}
