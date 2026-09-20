package dev.Astra.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.Render3DEvent;
import dev.Astra.api.utils.combat.PotionTracker;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.hud.TextRadar;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.class_1087;
import net.minecraft.class_124;
import net.minecraft.class_1291;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1686;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_1847;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2673;
import net.minecraft.class_308;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_7833;
import net.minecraft.class_811;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_327.class_6415;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class NameTags extends Module {
   public static NameTags INSTANCE;
   private final SliderSetting maxPlayers = this.add(new SliderSetting("MaxPlayers", (double)12.0F, (double)1.0F, (double)50.0F, (double)1.0F));
   final BooleanSetting armorConfig = this.add((new BooleanSetting("Armor", true)).setParent());
   final BooleanSetting drawItemConfig;
   final SliderSetting offsetConfig;
   final BooleanSetting enchantmentsConfig;
   final BooleanSetting durabilityConfig;
   final BooleanSetting forceBarConfig;
   final BooleanSetting entityIdConfig;
   final BooleanSetting gamemodeConfig;
   final BooleanSetting pingConfig;
   final BooleanSetting healthConfig;
   final BooleanSetting totemsConfig;
   final SliderSetting scaleConfig;
   final BooleanSetting factorConfig;
   final SliderSetting scalingConfig;
   final SliderSetting distanceConfig;
   final SliderSetting heightConfig;
   final ColorSetting colorConfig;
   final ColorSetting friendConfig;
   final ColorSetting invisibleConfig;
   final ColorSetting died;
   final ColorSetting sneakingConfig;
   final ColorSetting rectConfig;
   public final BooleanSetting potionGroup;
   public final SliderSetting effectRadius;
   public final BooleanSetting showPotionLevel;
   final DecimalFormat df;

   public NameTags() {
      super("NameTags", Module.Category.Render);
      BooleanSetting var10006 = this.armorConfig;
      Objects.requireNonNull(var10006);
      this.drawItemConfig = this.add(new BooleanSetting("DrawItem", true, var10006::isOpen));
      BooleanSetting var10009 = this.armorConfig;
      Objects.requireNonNull(var10009);
      this.offsetConfig = this.add(new SliderSetting("Offset", (double)-20.0F, (double)-30.0F, (double)10.0F, 0.01, var10009::isOpen));
      this.enchantmentsConfig = this.add(new BooleanSetting("Enchantments", false));
      this.durabilityConfig = this.add((new BooleanSetting("Durability", false)).setParent());
      var10006 = this.durabilityConfig;
      Objects.requireNonNull(var10006);
      this.forceBarConfig = this.add(new BooleanSetting("ForceBar", true, var10006::isOpen));
      this.entityIdConfig = this.add(new BooleanSetting("EntityId", false));
      this.gamemodeConfig = this.add(new BooleanSetting("Gamemode", false));
      this.pingConfig = this.add(new BooleanSetting("Ping", true));
      this.healthConfig = this.add(new BooleanSetting("Health", true));
      this.totemsConfig = this.add(new BooleanSetting("Totems", true));
      this.scaleConfig = this.add(new SliderSetting("Scale", (double)1.0F, (double)0.0F, (double)3.0F, 0.1));
      this.factorConfig = this.add((new BooleanSetting("Factor", true)).setParent());
      var10009 = this.factorConfig;
      Objects.requireNonNull(var10009);
      this.scalingConfig = this.add(new SliderSetting("Scaling", (double)1.0F, (double)0.0F, (double)3.0F, 0.1, var10009::isOpen));
      var10009 = this.factorConfig;
      Objects.requireNonNull(var10009);
      this.distanceConfig = this.add((new SliderSetting("Distance", (double)6.0F, (double)0.0F, (double)20.0F, 0.1, var10009::isOpen)).setSuffix("m"));
      this.heightConfig = this.add((new SliderSetting("Height", (double)0.0F, (double)-3.0F, (double)3.0F, 0.01)).setSuffix("m"));
      this.colorConfig = this.add(new ColorSetting("Color", new Color(-197380, true)));
      this.friendConfig = this.add((new ColorSetting("Friend", new Color(-16712452, true))).injectBoolean(true));
      this.invisibleConfig = this.add((new ColorSetting("Invisible", new Color(-3618616, true))).injectBoolean(true));
      this.died = this.add((new ColorSetting("Died", new Color(-4980736, true))).injectBoolean(true));
      this.sneakingConfig = this.add((new ColorSetting("Sneaking", new Color(-3618816, true))).injectBoolean(true));
      this.rectConfig = this.add((new ColorSetting("Rectangle", new Color(1677721600, true))).injectBoolean(true));
      this.potionGroup = this.add((new BooleanSetting("PotionSettings", true)).setParent());
      var10009 = this.potionGroup;
      Objects.requireNonNull(var10009);
      this.effectRadius = this.add((new SliderSetting("EffectRadius", (double)1.5F, (double)0.5F, (double)4.0F, 0.1, var10009::isOpen)).setSuffix("m"));
      var10006 = this.potionGroup;
      Objects.requireNonNull(var10006);
      this.showPotionLevel = this.add(new BooleanSetting("ShowPotionLevel", true, var10006::isOpen));
      this.df = new DecimalFormat("0.0");
      this.setChinese("名字标签");
      INSTANCE = this;
   }

   public void onDisable() {
      PotionTracker.clear();
   }

   private String toRoman(int number) {
      switch (number) {
         case 1 -> {
            return "I";
         }
         case 2 -> {
            return "II";
         }
         case 3 -> {
            return "III";
         }
         case 4 -> {
            return "IV";
         }
         default -> {
            return "";
         }
      }
   }

   private String getEffectDisplayName(class_6880<class_1291> effect) {
      if (effect.method_55838(class_1294.field_5910)) {
         return "Strength";
      } else {
         return effect.method_55838(class_1294.field_5907) ? "Resistance" : "Unknown";
      }
   }

   private int getEffectColor(class_6880<class_1291> effect) {
      if (effect.method_55838(class_1294.field_5910)) {
         return -14592;
      } else {
         return effect.method_55838(class_1294.field_5907) ? -7256336 : -1;
      }
   }

   @EventListener
   public void onPacketReceive(PacketEvent.Receive event) {
      if (this.potionGroup.getValue()) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2673) {
            class_2673 packet = (class_2673)var3;
            if (packet.method_11532() == 2002) {
               class_243 splashPos = class_243.method_24953(packet.method_11531());
               class_1686 potion = null;
               double minDist = Double.MAX_VALUE;

               for(class_1297 e : mc.field_1687.method_18112()) {
                  if (e instanceof class_1686) {
                     class_1686 pe = (class_1686)e;
                     double d = e.method_19538().method_1025(splashPos);
                     if (d < (double)16.0F && d < minDist) {
                        minDist = d;
                        potion = pe;
                     }
                  }
               }

               if (potion != null) {
                  class_1799 stack = potion.method_7495();
                  class_1844 contents = (class_1844)stack.method_57824(class_9334.field_49651);
                  if (contents != null) {
                     class_6880<class_1291> effectToTrack = null;
                     int baseDuration = 0;
                     int level = 0;
                     if (contents.method_57401(class_1847.field_8978)) {
                        effectToTrack = class_1294.field_5910;
                        baseDuration = 3600;
                        level = 1;
                     } else if (contents.method_57401(class_1847.field_8965)) {
                        effectToTrack = class_1294.field_5910;
                        baseDuration = 9600;
                        level = 1;
                     } else if (contents.method_57401(class_1847.field_8993)) {
                        effectToTrack = class_1294.field_5910;
                        baseDuration = 1800;
                        level = 2;
                     } else if (contents.method_57401(class_1847.field_8990)) {
                        effectToTrack = class_1294.field_5907;
                        baseDuration = 400;
                        level = 3;
                     } else if (contents.method_57401(class_1847.field_8988)) {
                        effectToTrack = class_1294.field_5907;
                        baseDuration = 800;
                        level = 3;
                     } else if (contents.method_57401(class_1847.field_8977)) {
                        effectToTrack = class_1294.field_5907;
                        baseDuration = 400;
                        level = 4;
                     }

                     if (effectToTrack != null && baseDuration > 0) {
                        class_6880<class_1291> trackedEffect = effectToTrack;
                        float radius = this.effectRadius.getValueFloat();

                        for(class_1657 player : mc.field_1687.method_18456()) {
                           class_243 closestPoint = MathUtil.getClosestPointToBox(splashPos, player.method_5829());
                           double dist = splashPos.method_1022(closestPoint);
                           if (!(dist > (double)radius)) {
                              double impact = (double)1.0F - dist / (double)radius;
                              if (impact < (double)0.0F) {
                                 impact = (double)0.0F;
                              }

                              int duration = (int)(impact * (double)baseDuration);
                              if (duration > 20) {
                                 long durationMs = (long)duration * 50L;
                                 PotionTracker.addEffect(player.method_5628(), trackedEffect, level, durationMs);
                              }
                           }
                        }

                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void onRender3D(Render3DEvent event) {
      if (mc.field_1773 != null && mc.method_1560() != null) {
         PotionTracker.tick();
         class_4184 camera = mc.field_1773.method_19418();
         RenderSystem.enableBlend();
         GL11.glDepthFunc(519);
         class_4587 matrixStack = new class_4587();
         class_243 cameraPos = camera.method_19326();
         int max = (int)this.maxPlayers.getValue();

         for(class_1657 playerEntity : (List)Astra.THREAD.getPlayers().stream().filter((player) -> {
            if (player == mc.field_1724 && mc.field_1690.method_31044().method_31034()) {
               return false;
            } else if (!this.died.booleanValue && !player.method_5805()) {
               return false;
            } else {
               return this.invisibleConfig.booleanValue || !player.method_5767();
            }
         }).sorted(Comparator.comparingDouble((player) -> {
            class_243 pos = player.method_19538();
            return cameraPos.method_1028(pos.field_1352, pos.field_1351 + (double)player.method_17682() / (double)2.0F, pos.field_1350);
         })).limit((long)max).collect(Collectors.toList())) {
            String info = this.getNametagInfo(playerEntity);
            class_243 renderPosition = MathUtil.getRenderPosition(playerEntity, event.tickDelta);
            double x = renderPosition.method_10216();
            double y = renderPosition.method_10214();
            double z = renderPosition.method_10215();
            int width = mc.field_1772.method_1727(info);
            float hwidth = (float)width / 2.0F;
            this.renderInfo(info, hwidth, playerEntity, x, y, z, camera, matrixStack);
         }

         GL11.glDepthFunc(515);
         RenderSystem.disableBlend();
      }
   }

   private void renderInfo(String info, float width, class_1657 entity, double x, double y, double z, class_4184 camera, class_4587 matrices) {
      class_243 pos = camera.method_19326();
      double eyeY = y + (double)entity.method_17682() + (double)(entity.method_5715() ? 0.4F : 0.43F) + (double)this.heightConfig.getValueFloat();
      float scale = (float)((double)(-0.025F * this.scaleConfig.getValueFloat()) + (this.factorConfig.getValue() && pos.method_1028(x, eyeY, z) > (double)(this.distanceConfig.getValueFloat() * this.distanceConfig.getValueFloat()) ? (Math.sqrt(pos.method_1028(x, eyeY, z)) - (double)this.distanceConfig.getValueFloat()) * (double)-0.0025F * (double)this.scalingConfig.getValueFloat() : (double)0.0F));
      matrices.method_22903();
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
      matrices.method_22904(x - pos.method_10216(), eyeY - pos.method_10214() + (double)((scale / -0.025F - 1.0F) / 4.0F), z - pos.method_10215());
      matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrices.method_22905(scale, scale, -1.0F);
      List<EffectSegment> effectSegments = new ArrayList();
      if (this.potionGroup.getValue()) {
         long now = System.currentTimeMillis();
         int strengthLevel = PotionTracker.getStrengthLevel(entity);
         long strengthSeconds = PotionTracker.getStrengthRemainingSeconds(entity);
         if (strengthLevel > 0 && strengthSeconds > 0L) {
            StringBuilder sb = new StringBuilder();
            sb.append("Strength ");
            if (this.showPotionLevel.getValue()) {
               sb.append(this.toRoman(strengthLevel)).append(" ");
            }

            sb.append(strengthSeconds).append("s");
            effectSegments.add(new EffectSegment(sb.toString().trim(), -14592));
         }

         int resistanceLevel = PotionTracker.getResistanceLevel(entity);
         long resistanceSeconds = PotionTracker.getResistanceRemainingSeconds(entity);
         if (resistanceLevel > 0 && resistanceSeconds > 0L) {
            StringBuilder sb = new StringBuilder();
            sb.append("Resistance ");
            if (this.showPotionLevel.getValue()) {
               sb.append(this.toRoman(resistanceLevel)).append(" ");
            }

            sb.append(resistanceSeconds).append("s");
            effectSegments.add(new EffectSegment(sb.toString().trim(), -7256336));
         }
      }

      float nameWidth = (float)mc.field_1772.method_1727(info);
      if (this.rectConfig.booleanValue) {
         float rectX = -nameWidth / 2.0F - 2.0F;
         float rectY = -2.0F;
         float var10003 = nameWidth + 4.0F;
         Objects.requireNonNull(mc.field_1772);
         Render2DUtil.drawRect(matrices, rectX, rectY, var10003, (float)(9 + 4), this.rectConfig.getValue());
      }

      this.drawWithShadow(matrices, info, -nameWidth / 2.0F, 0.0F, this.getNametagColor(entity));
      if (this.armorConfig.getValue()) {
         this.renderItems(matrices, entity, effectSegments);
      } else if (!effectSegments.isEmpty()) {
         Objects.requireNonNull(mc.field_1772);
         float yOffset = (float)(9 + 2);
         float totalWidth = 0.0F;

         for(EffectSegment seg : effectSegments) {
            totalWidth += (float)(mc.field_1772.method_1727(seg.text) + 4);
         }

         if (!effectSegments.isEmpty()) {
            totalWidth -= 4.0F;
         }

         float startX = -totalWidth / 2.0F;

         for(EffectSegment seg : effectSegments) {
            this.drawWithShadow(matrices, seg.text, startX, yOffset, seg.color);
            startX += (float)(mc.field_1772.method_1727(seg.text) + 4);
         }
      }

      matrices.method_22909();
   }

   private void drawWithShadow(class_4587 matrices, String info, float x, float y, int color) {
      class_4597.class_4598 immediate = mc.method_22940().method_23000();
      mc.field_1772.method_27521(info, x, y, color, false, matrices.method_23760().method_23761(), immediate, class_6415.field_33994, 0, 15728880);
      immediate.method_22993();
   }

   private void renderItems(class_4587 matrixStack, class_1657 player, List<EffectSegment> effectSegments) {
      List<class_1799> displayItems = new ArrayList();
      if (!player.method_6079().method_7960()) {
         displayItems.add(player.method_6079());
      }

      player.method_31548().field_7548.forEach((armorStack) -> {
         if (!armorStack.method_7960()) {
            displayItems.add(armorStack);
         }

      });
      if (!player.method_6047().method_7960()) {
         displayItems.add(player.method_6047());
      }

      Collections.reverse(displayItems);
      float x = 0.0F;
      int n11 = 0;

      for(class_1799 itemStack : displayItems) {
         x -= 8.0F;
         if (itemStack.method_58657().method_57541() > n11) {
            n11 = itemStack.method_58657().method_57541();
         }
      }

      float y = this.offsetConfig.getValueFloat();

      for(class_1799 stack : displayItems) {
         GL11.glDepthFunc(519);
         if (this.drawItemConfig.getValue()) {
            this.renderItemStack(matrixStack, stack, x, y + 1.0F);
         }

         this.renderItemOverlay(matrixStack, stack, x, y + 2.5F);
         matrixStack.method_22905(0.5F, 0.5F, 0.5F);
         if (this.durabilityConfig.getValue()) {
            this.renderDurability(matrixStack, stack, x + 2.0F, y + 11.5F);
         }

         if (this.enchantmentsConfig.getValue()) {
            this.renderEnchants(matrixStack, stack, x + 2.0F, y + 7.0F);
         }

         matrixStack.method_22905(2.0F, 2.0F, 2.0F);
         x += 16.0F;
         GL11.glDepthFunc(515);
      }

      if (effectSegments != null && !effectSegments.isEmpty()) {
         float nameY = y - 4.5F + this.enchantOffset(n11);
         matrixStack.method_22905(0.5F, 0.5F, 0.5F);
         float totalWidth = 0.0F;

         for(EffectSegment seg : effectSegments) {
            totalWidth += (float)(mc.field_1772.method_1727(seg.text) + 4);
         }

         if (!effectSegments.isEmpty()) {
            totalWidth -= 4.0F;
         }

         float drawX = -totalWidth / 2.0F;

         for(EffectSegment seg : effectSegments) {
            float segWidth = (float)mc.field_1772.method_1727(seg.text);
            this.drawWithShadow(matrixStack, seg.text, drawX, nameY * 2.0F, seg.color);
            drawX += segWidth + 4.0F;
         }

         matrixStack.method_22905(2.0F, 2.0F, 2.0F);
      }

   }

   private void renderItemStack(class_4587 matrixStack, class_1799 stack, float x, float y) {
      matrixStack.method_22903();
      matrixStack.method_46416(x, y, 0.0F);
      matrixStack.method_46416(8.0F, 8.0F, 0.0F);
      matrixStack.method_22905(16.0F, 16.0F, 1.0E-8F);
      matrixStack.method_34425((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
      class_308.method_24210();
      class_1087 model = mc.method_1480().method_4019(stack, mc.field_1687, (class_1309)null, 0);
      class_4597.class_4598 i = mc.method_22940().method_23000();
      mc.method_1480().method_23179(stack, class_811.field_4317, false, matrixStack, i, 16711680, class_4608.field_21444, model);
      i.method_22993();
      class_308.method_24211();
      matrixStack.method_22909();
   }

   private void renderItemOverlay(class_4587 matrixStack, class_1799 stack, float x, float y) {
      matrixStack.method_22903();
      if (stack.method_7947() != 1) {
         String string = String.valueOf(stack.method_7947());
         this.drawWithShadow(matrixStack, string, x + 17.0F - (float)mc.field_1772.method_1727(string), y + 9.0F, -1);
      }

      if (stack.method_31578() || stack.method_7963() && this.forceBarConfig.getValue()) {
         int i = stack.method_31579();
         int j = stack.method_31580();
         float k = x + 2.0F;
         float l = y + 13.0F;
         Render2DUtil.drawRect(matrixStack, k, l, 13.0F, 2.0F, -16777216);
         Render2DUtil.drawRect(matrixStack, k, l, (float)i, 1.0F, j | -16777216);
      }

      matrixStack.method_22909();
   }

   private void renderDurability(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
      if (itemStack.method_7963()) {
         int n = itemStack.method_7936();
         int n2 = itemStack.method_7919();
         int durability = (int)((float)(n - n2) / (float)n * 100.0F);
         this.drawWithShadow(matrixStack, durability + "%", x * 2.0F, y * 2.0F, ColorUtil.hslToColor((float)(n - n2) / (float)n * 120.0F, 100.0F, 50.0F, 1.0F).getRGB());
      }
   }

   private void renderEnchants(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
      if (itemStack.method_7909() == class_1802.field_8367) {
         this.drawWithShadow(matrixStack, "God", x * 2.0F, y * 2.0F, -3977663);
      } else if (itemStack.method_7942()) {
         class_9304 enchants = class_1890.method_57532(itemStack);
         float n2 = 0.0F;

         for(class_6880<class_1887> enchantment : enchants.method_57534()) {
            int lvl = enchants.method_57536(enchantment);
            StringBuilder enchantString = new StringBuilder();
            String translatedName = ((class_1887)enchantment.comp_349()).toString().replace("Enchantment ", "");
            if (translatedName.contains("Vanish")) {
               enchantString.append("§cVan");
            } else if (translatedName.contains("Bind")) {
               enchantString.append("§cBind");
            } else {
               int maxLen = lvl > 1 ? 2 : 3;
               if (translatedName.length() > maxLen) {
                  translatedName = translatedName.substring(0, maxLen);
               }

               enchantString.append(translatedName);
               enchantString.append(lvl);
            }

            this.drawWithShadow(matrixStack, enchantString.toString(), x * 2.0F, (y + n2) * 2.0F, -1);
            n2 -= 4.5F;
         }

      }
   }

   private float enchantOffset(int n) {
      return this.enchantmentsConfig.getValue() && n > 2 ? -2.0F - (float)(n - 3) * 4.5F : 0.0F;
   }

   private String getNametagInfo(class_1657 player) {
      StringBuilder info = new StringBuilder();
      if (this.gamemodeConfig.getValue()) {
         if (player.method_7337()) {
            info.append(class_124.field_1065).append("[C] ");
         } else if (player.method_7325()) {
            info.append(class_124.field_1080).append("[I] ");
         } else {
            info.append(class_124.field_1067).append("[S] ");
         }
      }

      if (this.pingConfig.getValue()) {
         info.append(this.getEntityPing(player)).append("ms ").append(class_124.field_1070);
      }

      info.append(player.method_5477().getString()).append(" ");
      if (this.entityIdConfig.getValue()) {
         info.append("ID: ").append(player.method_5628()).append(" ");
      }

      if (this.healthConfig.getValue()) {
         double health = (double)(player.method_6032() + player.method_6067());
         class_124 hcolor = health > (double)18.0F ? class_124.field_1060 : (health > (double)16.0F ? class_124.field_1077 : (health > (double)12.0F ? class_124.field_1054 : (health > (double)8.0F ? class_124.field_1065 : (health > (double)4.0F ? class_124.field_1061 : class_124.field_1079))));
         info.append(hcolor).append(this.df.format(health)).append(" ");
      }

      int totems;
      if (this.totemsConfig.getValue() && (totems = Astra.POP.getPop(player)) > 0) {
         info.append(TextRadar.getPopColor(totems)).append(-totems).append(" ");
      }

      return info.toString().trim();
   }

   private String getEntityPing(class_1657 entity) {
      if (mc.method_1562() == null) {
         return "§7-1";
      } else {
         class_640 playerListEntry = mc.method_1562().method_2871(entity.method_5667());
         if (playerListEntry == null) {
            return "§7-1";
         } else {
            int ping = playerListEntry.method_2959();
            class_124 color = ping >= 200 ? class_124.field_1061 : (ping >= 100 ? class_124.field_1054 : class_124.field_1060);
            String var10000 = color.toString();
            return var10000 + ping;
         }
      }
   }

   private int getNametagColor(class_1657 player) {
      if (this.friendConfig.booleanValue && player.method_5476() != null && Astra.FRIEND.isFriend(player)) {
         return this.friendConfig.getValue().getRGB();
      } else if (this.invisibleConfig.booleanValue && player.method_5767()) {
         return this.invisibleConfig.getValue().getRGB();
      } else if (this.sneakingConfig.booleanValue && player.method_5715()) {
         return this.sneakingConfig.getValue().getRGB();
      } else {
         return !player.method_5805() ? this.died.getValue().getRGB() : this.colorConfig.getValue().getRGB();
      }
   }

   private static class EffectSegment {
      String text;
      int color;

      EffectSegment(String text, int color) {
         this.text = text;
         this.color = color;
      }
   }
}
