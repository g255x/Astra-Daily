package dev.Astra.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.player.AutoPearl;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1667;
import net.minecraft.class_1676;
import net.minecraft.class_1680;
import net.minecraft.class_1681;
import net.minecraft.class_1683;
import net.minecraft.class_1684;
import net.minecraft.class_1685;
import net.minecraft.class_1686;
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
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_6880;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class Trajectories extends Module {
   private static class_4587 matrixStack;
   private final SliderSetting maxEntities = this.add(new SliderSetting("MaxEntities", (double)5.0F, (double)1.0F, (double)20.0F, (double)1.0F));
   private final SliderSetting maxSteps = this.add(new SliderSetting("MaxSteps", (double)200.0F, (double)50.0F, (double)500.0F, (double)10.0F));
   private final ColorSetting pearl = this.add((new ColorSetting("Pearl", new Color(255, 255, 255, 255))).injectBoolean(true));
   private final ColorSetting arrow = this.add((new ColorSetting("Arrow", new Color(255, 255, 255, 255))).injectBoolean(true));
   private final ColorSetting xp = this.add((new ColorSetting("Xp", new Color(255, 255, 255, 255))).injectBoolean(true));
   private final ColorSetting snowball = this.add((new ColorSetting("Snowball", new Color(255, 255, 255, 255))).injectBoolean(false));
   private final ColorSetting egg = this.add((new ColorSetting("Egg", new Color(255, 255, 255, 255))).injectBoolean(false));
   private final ColorSetting potion = this.add((new ColorSetting("Potion", new Color(255, 255, 255, 255))).injectBoolean(false));
   private final ColorSetting trident = this.add((new ColorSetting("Trident", new Color(255, 255, 255, 255))).injectBoolean(false));
   private final SliderSetting pearlSpeed = this.add(new SliderSetting("PearlSpeed", (double)1.5F, (double)0.0F, (double)10.0F, 0.01));
   private final SliderSetting arrowSpeed = this.add(new SliderSetting("ArrowSpeed", (double)3.0F, (double)0.0F, (double)10.0F, 0.01));
   private final SliderSetting xpSpeed = this.add(new SliderSetting("XpSpeed", 0.7, (double)0.0F, (double)10.0F, 0.01));
   private final SliderSetting snowballSpeed = this.add(new SliderSetting("SnowballSpeed", (double)1.5F, (double)0.0F, (double)10.0F, 0.01));
   private final SliderSetting eggSpeed = this.add(new SliderSetting("EggSpeed", (double)1.5F, (double)0.0F, (double)10.0F, 0.01));
   private final SliderSetting potionSpeed = this.add(new SliderSetting("PotionSpeed", (double)0.5F, (double)0.0F, (double)10.0F, 0.01));
   private final SliderSetting tridentSpeed = this.add(new SliderSetting("TridentSpeed", (double)2.5F, (double)0.0F, (double)10.0F, 0.01));
   private final SliderSetting airResistance = this.add(new SliderSetting("AirResistance", 0.99, (double)0.5F, 0.99, 0.01));
   private final SliderSetting waterResistance = this.add(new SliderSetting("WaterResistance", 0.8, (double)0.5F, 0.99, 0.01));
   private final ColorSetting hitFill = this.add(new ColorSetting("HitFill", new Color(255, 0, 0, 50)));
   private final ColorSetting hitOutline = this.add(new ColorSetting("HitOutline", new Color(255, 0, 0, 255)));

   public Trajectories() {
      super("Trajectories", Module.Category.Render);
      this.setChinese("投掷物轨迹预测");
   }

   public void onRender3D(class_4587 stack) {
      if (!nullCheck()) {
         matrixStack = stack;
         RenderSystem.disableDepthTest();
         List<class_1297> entitiesToRender = new ArrayList();

         for(class_1297 e : Astra.THREAD.getEntities()) {
            if (this.getEntityColor(e) != null) {
               entitiesToRender.add(e);
            }
         }

         entitiesToRender.sort(Comparator.comparingDouble((ex) -> ex.method_5858(mc.field_1724)));
         int max = this.maxEntities.getValueInt();

         for(int i = 0; i < Math.min(entitiesToRender.size(), max); ++i) {
            this.calcTrajectory((class_1297)entitiesToRender.get(i), this.getEntityColor((class_1297)entitiesToRender.get(i)));
         }

         if (mc.field_1690.method_31044().method_31034()) {
            class_1799 main = mc.field_1724.method_6047();
            class_1799 off = mc.field_1724.method_6079();
            class_1792 item = null;
            class_1268 activeHand = null;
            if (!this.isThrowable(main.method_7909()) && !(main.method_7909() instanceof class_1753) && !(main.method_7909() instanceof class_1764) && !AutoPearl.INSTANCE.isOn()) {
               if (this.isThrowable(off.method_7909()) || off.method_7909() instanceof class_1753 || off.method_7909() instanceof class_1764) {
                  item = off.method_7909();
                  activeHand = class_1268.field_5810;
               }
            } else {
               item = main.method_7909();
               activeHand = class_1268.field_5808;
            }

            if (item != null) {
               ColorItemPair pair = this.getColorForItem(item);
               if (pair != null && pair.enabled) {
                  boolean oldBob = (Boolean)mc.field_1690.method_42448().method_41753();
                  mc.field_1690.method_42448().method_41748(false);
                  double x = mc.field_1724.method_23317();
                  double y = mc.field_1724.method_23320();
                  double z = mc.field_1724.method_23321();
                  boolean multishot = activeHand == class_1268.field_5808 && this.hasMultishot(main) || activeHand == class_1268.field_5810 && this.hasMultishot(off);
                  if (multishot && item instanceof class_1764) {
                     this.calcPreview(item, mc.field_1724.method_36454(), x, y, z, pair.color);
                     this.calcPreview(item, mc.field_1724.method_36454(), x, y, z, pair.color);
                     this.calcPreview(item, mc.field_1724.method_36454(), x, y, z, pair.color);
                  } else {
                     this.calcPreview(item, mc.field_1724.method_36454(), x, y, z, pair.color);
                  }

                  mc.field_1690.method_42448().method_41748(oldBob);
               }
            }
         }

         RenderSystem.enableDepthTest();
      }
   }

   private ColorItemPair getColorForItem(class_1792 item) {
      if (item instanceof class_1776) {
         return new ColorItemPair(this.pearl.getValue(), this.pearl.booleanValue);
      } else if (!(item instanceof class_1753) && !(item instanceof class_1764)) {
         if (item instanceof class_1835) {
            return new ColorItemPair(this.trident.getValue(), this.trident.booleanValue);
         } else if (item instanceof class_1779) {
            return new ColorItemPair(this.xp.getValue(), this.xp.booleanValue);
         } else if (item instanceof class_1823) {
            return new ColorItemPair(this.snowball.getValue(), this.snowball.booleanValue);
         } else if (item instanceof class_1771) {
            return new ColorItemPair(this.egg.getValue(), this.egg.booleanValue);
         } else {
            return !(item instanceof class_1828) && !(item instanceof class_1803) ? null : new ColorItemPair(this.potion.getValue(), this.potion.booleanValue);
         }
      } else {
         return new ColorItemPair(this.arrow.getValue(), this.arrow.booleanValue);
      }
   }

   private Color getEntityColor(class_1297 e) {
      if (e instanceof class_1684 && this.pearl.booleanValue) {
         return this.pearl.getValue();
      } else if (e instanceof class_1667 && this.arrow.booleanValue) {
         return this.arrow.getValue();
      } else if (e instanceof class_1683 && this.xp.booleanValue) {
         return this.xp.getValue();
      } else if (e instanceof class_1680 && this.snowball.booleanValue) {
         return this.snowball.getValue();
      } else if (e instanceof class_1681 && this.egg.booleanValue) {
         return this.egg.getValue();
      } else if (e instanceof class_1686 && this.potion.booleanValue) {
         return this.potion.getValue();
      } else {
         return e instanceof class_1685 && this.trident.booleanValue ? this.trident.getValue() : null;
      }
   }

   private double getSpeedValue(class_1297 e) {
      if (e instanceof class_1684) {
         return this.pearlSpeed.getValue();
      } else if (e instanceof class_1667) {
         return this.arrowSpeed.getValue();
      } else if (e instanceof class_1683) {
         return this.xpSpeed.getValue();
      } else if (e instanceof class_1680) {
         return this.snowballSpeed.getValue();
      } else if (e instanceof class_1681) {
         return this.eggSpeed.getValue();
      } else if (e instanceof class_1686) {
         return this.potionSpeed.getValue();
      } else {
         return e instanceof class_1685 ? this.tridentSpeed.getValue() : (double)0.0F;
      }
   }

   private double getPreviewSpeedValue(class_1792 item) {
      if (item instanceof class_1776) {
         return this.pearlSpeed.getValue();
      } else if (!(item instanceof class_1753) && !(item instanceof class_1764)) {
         if (item instanceof class_1835) {
            return this.tridentSpeed.getValue();
         } else if (item instanceof class_1779) {
            return this.xpSpeed.getValue();
         } else if (item instanceof class_1823) {
            return this.snowballSpeed.getValue();
         } else if (item instanceof class_1771) {
            return this.eggSpeed.getValue();
         } else {
            return !(item instanceof class_1828) && !(item instanceof class_1803) ? (double)1.5F : this.potionSpeed.getValue();
         }
      } else {
         return this.arrowSpeed.getValue();
      }
   }

   private void calcTrajectory(class_1297 e, Color color) {
      class_243 vel = e.method_18798();
      double motionX = vel.field_1352;
      double motionY = vel.field_1351;
      double motionZ = vel.field_1350;
      if (motionX != (double)0.0F || motionY != (double)0.0F || motionZ != (double)0.0F) {
         double speed = this.getSpeedValue(e);
         if (speed > (double)0.0F) {
            double len = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
            if (len > (double)0.0F) {
               motionX = motionX / len * speed;
               motionY = motionY / len * speed;
               motionZ = motionZ / len * speed;
            }
         }

         double x = e.method_23317();
         double y = e.method_23318();
         double z = e.method_23321();
         double gravity = !(e instanceof class_1667) && !(e instanceof class_1685) ? 0.03 : 0.05;
         int maxStep = this.maxSteps.getValueInt();
         double airRes = this.airResistance.getValue();
         double waterRes = this.waterResistance.getValue();

         for(int step = 0; step < maxStep; ++step) {
            class_243 last = new class_243(x, y, z);
            x += motionX;
            y += motionY;
            z += motionZ;
            if (mc.field_1687.method_8320(new class_2338((int)x, (int)y, (int)z)).method_26204() == class_2246.field_10382) {
               motionX *= waterRes;
               motionY *= waterRes;
               motionZ *= waterRes;
            } else {
               motionX *= airRes;
               motionY *= airRes;
               motionZ *= airRes;
            }

            motionY -= gravity;
            class_243 now = new class_243(x, y, z);
            class_243 hitPoint = null;
            class_1297 hitEntity = null;

            for(class_1297 other : mc.field_1687.method_18112()) {
               if (other != e && !(other instanceof class_1676)) {
                  Optional<class_243> hit = other.method_5829().method_992(last, now);
                  if (hit.isPresent()) {
                     hitPoint = (class_243)hit.get();
                     hitEntity = other;
                     break;
                  }
               }
            }

            if (hitPoint != null) {
               Render3DUtil.drawLine(last, hitPoint, color);
               if (hitEntity != null) {
                  class_238 box = hitEntity.method_5829();
                  Render3DUtil.draw3DBox(matrixStack, box, this.hitFill.getValue(), this.hitOutline.getValue(), true, true);
               }
               break;
            }

            class_3965 bhr = mc.field_1687.method_17742(new class_3959(last, now, class_3960.field_17559, class_242.field_1348, mc.field_1724));
            if (bhr != null && bhr.method_17783() == class_240.field_1332) {
               Render3DUtil.drawLine(last, bhr.method_17784(), color);
               class_2338 pos = bhr.method_17777();
               class_265 shape = mc.field_1687.method_8320(pos).method_26218(mc.field_1687, pos);
               if (shape != null && !shape.method_1110()) {
                  class_238 box = shape.method_1107().method_996(pos);
                  Render3DUtil.draw3DBox(matrixStack, box, this.hitFill.getValue(), this.hitOutline.getValue(), true, true);
               }
               break;
            }

            if (y <= (double)-65.0F) {
               break;
            }

            Render3DUtil.drawLine(last, now, color);
         }

      }
   }

   private void calcPreview(class_1792 item, float yaw, double x, double y, double z, Color color) {
      float maxDist = this.getDistance(item);
      float pitch = mc.field_1724.method_36455();
      double motionX = (double)(-class_3532.method_15374(yaw * ((float)Math.PI / 180F)) * class_3532.method_15362(pitch * ((float)Math.PI / 180F)) * maxDist);
      double motionY = (double)(-class_3532.method_15374(pitch * ((float)Math.PI / 180F)) * maxDist);
      double motionZ = (double)(class_3532.method_15362(yaw * ((float)Math.PI / 180F)) * class_3532.method_15362(pitch * ((float)Math.PI / 180F)) * maxDist);
      float power = (float)mc.field_1724.method_6048() / 20.0F;
      power = (power * power + power * 2.0F) / 3.0F;
      if (power > 1.0F) {
         power = 1.0F;
      }

      float len = class_3532.method_15355((float)(motionX * motionX + motionY * motionY + motionZ * motionZ));
      motionX /= (double)len;
      motionY /= (double)len;
      motionZ /= (double)len;
      float pow = (item instanceof class_1753 ? power * 2.0F : (item instanceof class_1764 ? 2.2F : 1.0F)) * this.getThrowVelocity(item);
      motionX *= (double)pow;
      motionY *= (double)pow;
      motionZ *= (double)pow;
      double speed = this.getPreviewSpeedValue(item);
      if (speed > (double)0.0F) {
         double currentLen = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
         if (currentLen > (double)0.0F) {
            motionX = motionX / currentLen * speed;
            motionY = motionY / currentLen * speed;
            motionZ = motionZ / currentLen * speed;
         }
      }

      double gravity = !(item instanceof class_1753) && !(item instanceof class_1764) ? 0.03 : 0.05;
      int maxStep = 300;
      class_243 lastPos = new class_243(x, y, z);
      double airRes = this.airResistance.getValue();
      double waterRes = this.waterResistance.getValue();

      for(int step = 0; step < maxStep; ++step) {
         double newX = x + motionX;
         double newY = y + motionY;
         double newZ = z + motionZ;
         class_243 newPos = new class_243(newX, newY, newZ);
         if (mc.field_1687.method_8320(new class_2338((int)newX, (int)newY, (int)newZ)).method_26204() == class_2246.field_10382) {
            motionX *= waterRes;
            motionY *= waterRes;
            motionZ *= waterRes;
         } else {
            motionX *= airRes;
            motionY *= airRes;
            motionZ *= airRes;
         }

         motionY -= gravity;
         class_243 hitPoint = null;
         class_1297 hitEntity = null;

         for(class_1297 other : mc.field_1687.method_18112()) {
            if (other != mc.field_1724 && !(other instanceof class_1676)) {
               Optional<class_243> hit = other.method_5829().method_992(lastPos, newPos);
               if (hit.isPresent()) {
                  hitPoint = (class_243)hit.get();
                  hitEntity = other;
                  break;
               }
            }
         }

         class_3965 bhr = null;
         if (hitPoint == null) {
            bhr = mc.field_1687.method_17742(new class_3959(lastPos, newPos, class_3960.field_17559, class_242.field_1348, mc.field_1724));
            if (bhr != null && bhr.method_17783() == class_240.field_1332) {
               hitPoint = bhr.method_17784();
            }
         }

         if (hitPoint != null) {
            Render3DUtil.drawLine(lastPos, hitPoint, color);
            if (hitEntity != null) {
               class_238 box = hitEntity.method_5829();
               Render3DUtil.draw3DBox(matrixStack, box, this.hitFill.getValue(), this.hitOutline.getValue(), true, true);
            } else if (bhr != null) {
               class_2338 pos = bhr.method_17777();
               class_265 shape = mc.field_1687.method_8320(pos).method_26218(mc.field_1687, pos);
               if (shape != null && !shape.method_1110()) {
                  class_238 box = shape.method_1107().method_996(pos);
                  Render3DUtil.draw3DBox(matrixStack, box, this.hitFill.getValue(), this.hitOutline.getValue(), true, true);
               }
            }
            break;
         }

         Render3DUtil.drawLine(lastPos, newPos, color);
         x = newX;
         y = newY;
         z = newZ;
         lastPos = newPos;
         if (newY <= (double)-65.0F) {
            break;
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
      return 0;
   }

   private boolean hasMultishot(class_1799 stack) {
      if (!(stack.method_7909() instanceof class_1764)) {
         return false;
      } else {
         class_6880.class_6883<class_1887> multishot = (class_6880.class_6883)mc.field_1687.method_30349().method_30530(class_1893.field_9108.method_58273()).method_40264(class_1893.field_9108).get();
         return class_1890.method_8225(multishot, stack) != 0;
      }
   }

   private static class ColorItemPair {
      Color color;
      boolean enabled;

      ColorItemPair(Color color, boolean enabled) {
         this.color = color;
         this.enabled = enabled;
      }
   }
}
