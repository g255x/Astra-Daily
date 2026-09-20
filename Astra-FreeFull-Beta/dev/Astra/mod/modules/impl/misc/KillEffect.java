package dev.Astra.mod.modules.impl.misc;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.DeathEvent;
import dev.Astra.api.events.impl.Render3DEvent;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1299;
import net.minecraft.class_1538;
import net.minecraft.class_1657;
import net.minecraft.class_2378;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_7923;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;

public class KillEffect extends Module {
   private static final class_2960 KILL1_ID = class_2960.method_60655("alien", "killeffect.kill1");
   private static final class_2960 KILL2_ID = class_2960.method_60655("alien", "killeffect.kill2");
   private static final class_2960 KILL3_ID = class_2960.method_60655("alien", "killeffect.kill3");
   private static final class_2960 KILL4_ID = class_2960.method_60655("alien", "killeffect.kill4");
   private static final class_2960 KILL5_ID = class_2960.method_60655("alien", "killeffect.kill5");
   public static final class_3414 KILL1_SOUND;
   public static final class_3414 KILL2_SOUND;
   public static final class_3414 KILL3_SOUND;
   public static final class_3414 KILL4_SOUND;
   public static final class_3414 KILL5_SOUND;
   private static final class_3414[] STREAK_SOUNDS;
   private final EnumSetting<KillEffectMode> mode;
   private final BooleanSetting lightning;
   private final BooleanSetting levelUp;
   private final SliderSetting lMaxPitch;
   private final SliderSetting lMinPitch;
   private final BooleanSetting trident;
   private final SliderSetting tMaxPitch;
   private final SliderSetting tMinPitch;
   private final SliderSetting factor;
   private final SliderSetting duration;
   private final SliderSetting scale;
   private final BooleanSetting flash;
   private final SliderSetting lightPillarDuration;
   private final SliderSetting lightPillarWidth;
   private final SliderSetting lightPillarHeight;
   private final SliderSetting lightPillarAlpha;
   private final BooleanSetting streakSound;
   private final SliderSetting streakResetTime;
   private int killStreak = 0;
   private long lastKillTime = 0L;
   private final List<DeathEffect> deathEffects = new ArrayList();
   private final List<LightPillarEffect> lightPillarEffects = new ArrayList();

   public static class_3414 getStreakSound(int streak) {
      int index = Math.min(streak - 1, STREAK_SOUNDS.length - 1);
      return index < 0 ? STREAK_SOUNDS[0] : STREAK_SOUNDS[index];
   }

   public static void registerSounds() {
      class_2378.method_10230(class_7923.field_41172, KILL1_ID, KILL1_SOUND);
      class_2378.method_10230(class_7923.field_41172, KILL2_ID, KILL2_SOUND);
      class_2378.method_10230(class_7923.field_41172, KILL3_ID, KILL3_SOUND);
      class_2378.method_10230(class_7923.field_41172, KILL4_ID, KILL4_SOUND);
      class_2378.method_10230(class_7923.field_41172, KILL5_ID, KILL5_SOUND);
   }

   public KillEffect() {
      super("KillEffect", Module.Category.Misc);
      this.mode = this.add(new EnumSetting("Mode", KillEffect.KillEffectMode.DEFAULT));
      this.lightning = this.add(new BooleanSetting("Lightning", true, () -> this.mode.getValue() == KillEffect.KillEffectMode.DEFAULT));
      this.levelUp = this.add((new BooleanSetting("LevelUp", true, () -> this.mode.getValue() == KillEffect.KillEffectMode.DEFAULT)).setParent());
      this.lMaxPitch = this.add(new SliderSetting("LMaxPitch", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, () -> this.mode.getValue() == KillEffect.KillEffectMode.DEFAULT && this.levelUp.isOpen()));
      this.lMinPitch = this.add(new SliderSetting("LMinPitch", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, () -> this.mode.getValue() == KillEffect.KillEffectMode.DEFAULT && this.levelUp.isOpen()));
      this.trident = this.add((new BooleanSetting("Trident", false, () -> this.mode.getValue() == KillEffect.KillEffectMode.DEFAULT)).setParent());
      this.tMaxPitch = this.add(new SliderSetting("TMaxPitch", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, () -> this.mode.getValue() == KillEffect.KillEffectMode.DEFAULT && this.trident.isOpen()));
      this.tMinPitch = this.add(new SliderSetting("TMinPitch", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, () -> this.mode.getValue() == KillEffect.KillEffectMode.DEFAULT && this.trident.isOpen()));
      this.factor = this.add(new SliderSetting("Factor", (double)1.0F, (double)1.0F, (double)10.0F, (double)1.0F, () -> this.mode.getValue() == KillEffect.KillEffectMode.DEFAULT));
      this.duration = this.add(new SliderSetting("Duration", (double)3.0F, (double)1.0F, (double)10.0F, 0.1, () -> this.mode.getValue() == KillEffect.KillEffectMode.HereHasALowiqDie));
      this.scale = this.add(new SliderSetting("Scale", (double)1.0F, 0.1, (double)3.0F, 0.1, () -> this.mode.getValue() == KillEffect.KillEffectMode.HereHasALowiqDie));
      this.flash = this.add(new BooleanSetting("Flash", true, () -> this.mode.getValue() == KillEffect.KillEffectMode.HereHasALowiqDie));
      this.lightPillarDuration = this.add(new SliderSetting("PillarDuration", 0.4, (double)0.5F, (double)5.0F, 0.1, () -> this.mode.getValue() == KillEffect.KillEffectMode.LightPillar));
      this.lightPillarWidth = this.add(new SliderSetting("PillarWidth", 0.4, 0.3, (double)1.5F, 0.05, () -> this.mode.getValue() == KillEffect.KillEffectMode.LightPillar));
      this.lightPillarHeight = this.add(new SliderSetting("PillarHeight", (double)3.0F, (double)1.0F, (double)5.0F, 0.1, () -> this.mode.getValue() == KillEffect.KillEffectMode.LightPillar));
      this.lightPillarAlpha = this.add(new SliderSetting("PillarAlpha", (double)180.0F, (double)50.0F, (double)255.0F, (double)5.0F, () -> this.mode.getValue() == KillEffect.KillEffectMode.LightPillar));
      this.streakSound = this.add(new BooleanSetting("StreakSound", true));
      this.streakResetTime = this.add(new SliderSetting("StreakReset", (double)5.0F, (double)1.0F, (double)30.0F, (double)1.0F, () -> this.streakSound.getValue()));
      this.setChinese("击杀效果");
   }

   public String getInfo() {
      return this.streakSound.getValue() && this.killStreak > 0 ? "Streak: " + this.killStreak : ((KillEffectMode)this.mode.getValue()).name();
   }

   @EventListener
   public void onPlayerDeath(DeathEvent event) {
      class_1657 player;
      if (!nullCheck() && (player = event.getPlayer()) != null) {
         if (this.streakSound.getValue()) {
            long currentTime = System.currentTimeMillis();
            if ((double)(currentTime - this.lastKillTime) > this.streakResetTime.getValue() * (double)1000.0F) {
               this.killStreak = 0;
            }

            ++this.killStreak;
            this.lastKillTime = currentTime;
         }

         if (this.mode.getValue() == KillEffect.KillEffectMode.DEFAULT) {
            if (this.streakSound.getValue() && this.killStreak >= 1) {
               mc.field_1687.method_43128(mc.field_1724, player.method_23317(), player.method_23318(), player.method_23321(), getStreakSound(this.killStreak), class_3419.field_15248, 100.0F, 1.0F);
            }

            for(int i = 0; (double)i < this.factor.getValue(); ++i) {
               this.doEffect(player);
            }

            return;
         }

         if (this.mode.getValue() == KillEffect.KillEffectMode.HereHasALowiqDie) {
            boolean exists = false;

            for(DeathEffect effect : this.deathEffects) {
               if (Math.abs(effect.x - player.method_23317()) < (double)0.5F && Math.abs(effect.y - player.method_23318()) < (double)0.5F && Math.abs(effect.z - player.method_23321()) < (double)0.5F) {
                  exists = true;
                  break;
               }
            }

            if (!exists) {
               this.deathEffects.add(new DeathEffect(player.method_23317(), player.method_23318() + (double)1.0F, player.method_23321(), System.currentTimeMillis(), this.killStreak));
            }
         }

         if (this.mode.getValue() == KillEffect.KillEffectMode.LightPillar) {
            this.lightPillarEffects.add(new LightPillarEffect(player.method_23317(), player.method_23318(), player.method_23321(), System.currentTimeMillis(), this.killStreak));
         }
      }

   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (mc.field_1687 != null) {
         if (this.mode.getValue() == KillEffect.KillEffectMode.HereHasALowiqDie) {
            this.deathEffects.removeIf((effect) -> (double)(System.currentTimeMillis() - effect.timestamp) > this.duration.getValue() * (double)1000.0F);
         }

         if (this.mode.getValue() == KillEffect.KillEffectMode.LightPillar) {
            this.lightPillarEffects.removeIf((effect) -> (double)(System.currentTimeMillis() - effect.timestamp) > this.lightPillarDuration.getValue() * (double)1000.0F);
         }
      }

   }

   @EventListener
   public void onRender3D(Render3DEvent event) {
      if (mc.field_1687 != null) {
         if (this.mode.getValue() == KillEffect.KillEffectMode.HereHasALowiqDie) {
            for(DeathEffect effect : this.deathEffects) {
               if (this.streakSound.getValue() && !effect.soundPlayed && effect.streak >= 1) {
                  mc.field_1687.method_43128(mc.field_1724, effect.x, effect.y - (double)1.0F, effect.z, getStreakSound(effect.streak), class_3419.field_15248, 100.0F, 1.0F);
                  effect.soundPlayed = true;
               }

               if (!this.flash.getValue()) {
                  this.drawEffect(event.matrixStack, effect.x, effect.y, effect.z, 1.0F);
               } else if (System.currentTimeMillis() / 200L % 2L == 0L) {
                  this.drawEffect(event.matrixStack, effect.x, effect.y, effect.z, 1.0F);
               }
            }
         }

         if (this.mode.getValue() == KillEffect.KillEffectMode.LightPillar) {
            for(LightPillarEffect effect : this.lightPillarEffects) {
               if (this.streakSound.getValue() && !effect.soundPlayed && effect.streak >= 1) {
                  mc.field_1687.method_43128(mc.field_1724, effect.x, effect.y, effect.z, getStreakSound(effect.streak), class_3419.field_15248, 100.0F, 1.0F);
                  effect.soundPlayed = true;
               }

               this.drawLightPillar(event.matrixStack, effect);
            }
         }
      }

   }

   private void doEffect(class_1657 player) {
      double x = player.method_23317();
      double y = player.method_23318();
      double z = player.method_23321();
      if (this.lightning.getValue()) {
         class_1538 lightningEntity = new class_1538(class_1299.field_6112, mc.field_1687);
         lightningEntity.method_5814(x, y, z);
         lightningEntity.method_24203(x, y, z);
         mc.field_1687.method_8649(lightningEntity);
      }

      if (this.levelUp.getValue()) {
         mc.field_1687.method_43128(mc.field_1724, x, y, z, class_3417.field_14709, class_3419.field_15248, 100.0F, MathUtil.random(this.lMinPitch.getValueFloat(), this.lMaxPitch.getValueFloat()));
      }

      if (this.trident.getValue()) {
         mc.field_1687.method_60511(mc.field_1724, x, y, z, class_3417.field_15001, class_3419.field_15248, 999.0F, MathUtil.random(this.tMinPitch.getValueFloat(), this.tMaxPitch.getValueFloat()));
      }

   }

   private void drawEffect(class_4587 matrixStack, double x, double y, double z, float alpha) {
      matrixStack.method_22903();
      class_243 cameraPos = mc.field_1773.method_19418().method_19326();
      double cameraX = cameraPos.field_1352;
      double cameraY = cameraPos.field_1351;
      double cameraZ = cameraPos.field_1350;
      matrixStack.method_22904(x - cameraX, y - cameraY, z - cameraZ);
      matrixStack.method_22907(mc.field_1773.method_19418().method_23767());
      double scaleValue = 0.0245 * this.scale.getValue();
      matrixStack.method_22905((float)scaleValue, (float)scaleValue, (float)scaleValue);
      class_2960 yuanquanTexture = class_2960.method_60655("alien", "killeffect/yuanquan.png");
      RenderSystem.setShaderTexture(0, yuanquanTexture);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      this.drawTexture(matrixStack, -32.0F, -32.0F, 64.0F, 64.0F);
      class_2960 jiantouTexture = class_2960.method_60655("alien", "killeffect/jiantou.png");
      RenderSystem.setShaderTexture(0, jiantouTexture);
      this.drawTexture(matrixStack, -5.0F, -5.0F, 64.0F, 64.0F);
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      matrixStack.method_22909();
   }

   private void drawTexture(class_4587 matrices, float x, float y, float width, float height) {
      RenderSystem.setShader(class_757::method_34542);
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1585);
      Matrix4f matrix = matrices.method_23760().method_23761();
      bufferBuilder.method_22918(matrix, x, y + height, 0.0F).method_22913(0.0F, 0.0F);
      bufferBuilder.method_22918(matrix, x + width, y + height, 0.0F).method_22913(1.0F, 0.0F);
      bufferBuilder.method_22918(matrix, x + width, y, 0.0F).method_22913(1.0F, 1.0F);
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_22913(0.0F, 1.0F);
      class_286.method_43433(bufferBuilder.method_60800());
   }

   private void drawLightPillar(class_4587 matrixStack, LightPillarEffect effect) {
      long currentTime = System.currentTimeMillis();
      long elapsed = currentTime - effect.timestamp;
      double durationMs = this.lightPillarDuration.getValue() * (double)1000.0F;
      double progress = (double)elapsed / durationMs;
      if (progress > (double)1.0F) {
         progress = (double)1.0F;
      }

      double easedProgress = (double)1.0F - Math.pow((double)1.0F - progress, (double)3.0F);
      double shrinkFactor = (double)1.0F - easedProgress;
      double currentWidth = this.lightPillarWidth.getValue() * shrinkFactor;
      if (!(currentWidth < 0.01)) {
         double height = this.lightPillarHeight.getValue();
         int baseAlpha = (int)this.lightPillarAlpha.getValue();
         float fadeAlpha = (float)((double)1.0F - progress);
         class_243 cameraPos = mc.field_1773.method_19418().method_19326();
         double x = effect.x - cameraPos.field_1352;
         double y = effect.y - cameraPos.field_1351;
         double z = effect.z - cameraPos.field_1350;
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.setShader(class_757::method_34540);
         class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
         Matrix4f matrix = matrixStack.method_23760().method_23761();
         double halfWidth = currentWidth / (double)2.0F;
         double minX = x - halfWidth;
         double maxX = x + halfWidth;
         double minZ = z - halfWidth;
         double maxZ = z + halfWidth;
         int bottomAlpha = (int)((float)baseAlpha * fadeAlpha);
         int topAlpha = (int)((double)((float)baseAlpha * fadeAlpha) * 0.3);
         bufferBuilder.method_22918(matrix, (float)minX, (float)y, (float)minZ).method_1336(255, 255, 255, bottomAlpha);
         bufferBuilder.method_22918(matrix, (float)maxX, (float)y, (float)minZ).method_1336(255, 255, 255, bottomAlpha);
         bufferBuilder.method_22918(matrix, (float)maxX, (float)(y + height), (float)minZ).method_1336(30, 30, 30, topAlpha);
         bufferBuilder.method_22918(matrix, (float)minX, (float)(y + height), (float)minZ).method_1336(30, 30, 30, topAlpha);
         bufferBuilder.method_22918(matrix, (float)maxX, (float)y, (float)minZ).method_1336(255, 255, 255, bottomAlpha);
         bufferBuilder.method_22918(matrix, (float)maxX, (float)y, (float)maxZ).method_1336(255, 255, 255, bottomAlpha);
         bufferBuilder.method_22918(matrix, (float)maxX, (float)(y + height), (float)maxZ).method_1336(30, 30, 30, topAlpha);
         bufferBuilder.method_22918(matrix, (float)maxX, (float)(y + height), (float)minZ).method_1336(30, 30, 30, topAlpha);
         bufferBuilder.method_22918(matrix, (float)maxX, (float)y, (float)maxZ).method_1336(255, 255, 255, bottomAlpha);
         bufferBuilder.method_22918(matrix, (float)minX, (float)y, (float)maxZ).method_1336(255, 255, 255, bottomAlpha);
         bufferBuilder.method_22918(matrix, (float)minX, (float)(y + height), (float)maxZ).method_1336(30, 30, 30, topAlpha);
         bufferBuilder.method_22918(matrix, (float)maxX, (float)(y + height), (float)maxZ).method_1336(30, 30, 30, topAlpha);
         bufferBuilder.method_22918(matrix, (float)minX, (float)y, (float)maxZ).method_1336(255, 255, 255, bottomAlpha);
         bufferBuilder.method_22918(matrix, (float)minX, (float)y, (float)minZ).method_1336(255, 255, 255, bottomAlpha);
         bufferBuilder.method_22918(matrix, (float)minX, (float)(y + height), (float)minZ).method_1336(30, 30, 30, topAlpha);
         bufferBuilder.method_22918(matrix, (float)minX, (float)(y + height), (float)maxZ).method_1336(30, 30, 30, topAlpha);
         class_286.method_43433(bufferBuilder.method_60800());
         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
      }

   }

   static {
      KILL1_SOUND = class_3414.method_47908(KILL1_ID);
      KILL2_SOUND = class_3414.method_47908(KILL2_ID);
      KILL3_SOUND = class_3414.method_47908(KILL3_ID);
      KILL4_SOUND = class_3414.method_47908(KILL4_ID);
      KILL5_SOUND = class_3414.method_47908(KILL5_ID);
      STREAK_SOUNDS = new class_3414[]{KILL1_SOUND, KILL2_SOUND, KILL3_SOUND, KILL4_SOUND, KILL5_SOUND};
   }

   public static enum KillEffectMode {
      DEFAULT,
      HereHasALowiqDie,
      LightPillar;

      // $FF: synthetic method
      private static KillEffectMode[] $values() {
         return new KillEffectMode[]{DEFAULT, HereHasALowiqDie, LightPillar};
      }
   }

   private static class DeathEffect {
      public final double x;
      public final double y;
      public final double z;
      public final long timestamp;
      public final int streak;
      public boolean soundPlayed = false;

      public DeathEffect(double x, double y, double z, long timestamp, int streak) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.timestamp = timestamp;
         this.streak = streak;
      }
   }

   private static class LightPillarEffect {
      public final double x;
      public final double y;
      public final double z;
      public final long timestamp;
      public final int streak;
      public boolean soundPlayed = false;

      public LightPillarEffect(double x, double y, double z, long timestamp, int streak) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.timestamp = timestamp;
         this.streak = streak;
      }
   }
}
