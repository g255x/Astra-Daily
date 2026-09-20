package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render2DEvent;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_308;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_640;

public class PlayerInfoHUD extends Module {
   public final SliderSetting cardWidth = this.add(new SliderSetting("CardWidth", (double)200.0F, (double)120.0F, (double)400.0F, (double)1.0F));
   public final SliderSetting cardHeight = this.add(new SliderSetting("CardHeight", (double)80.0F, (double)60.0F, (double)150.0F, (double)1.0F));
   public final SliderSetting firstCardX = this.add(new SliderSetting("FirstCardX", (double)10.0F, (double)0.0F, (double)1920.0F, (double)1.0F));
   public final SliderSetting firstCardY = this.add(new SliderSetting("FirstCardY", (double)10.0F, (double)0.0F, (double)1080.0F, (double)1.0F));
   public final SliderSetting cardSpacing = this.add(new SliderSetting("CardSpacing", (double)5.0F, (double)0.0F, (double)30.0F, (double)0.5F));
   public final EnumSetting<CardLayout> layout;
   public final BooleanSetting enableBackgroundImage;
   private static final class_2960 BACKGROUND_TEXTURE = class_2960.method_60655("alienclient", "textures/xingye.png");
   public final SliderSetting imageAlpha;
   public final EnumSetting<ImageMode> imageMode;
   private static final float FIXED_IMAGE_WIDTH = 200.0F;
   private static final float FIXED_IMAGE_HEIGHT = 240.0F;
   private static final float FIXED_IMAGE_POS_X = 0.0F;
   private static final float FIXED_IMAGE_POS_Y = -19.0F;
   public final BooleanSetting enableCardBackground;
   public final ColorSetting cardBgColor;
   public final ColorSetting cardBorderColor;
   public final BooleanSetting cardRoundedCorners;
   public final SliderSetting cardCornerRadius;
   public final SliderSetting cardBorderThickness;
   public final BooleanSetting enableBlur;
   public final SliderSetting blurStrength;
   public final SliderSetting blurRadius;
   public final ColorSetting blurOverlay;
   public final SliderSetting maxPlayers;
   public final SliderSetting maxDistance;
   public final BooleanSetting showSkin;
   public final BooleanSetting showName;
   public final BooleanSetting showHealth;
   public final BooleanSetting showHealthBar;
   public final BooleanSetting showHealthAnimation;
   public final BooleanSetting showItems;
   public final BooleanSetting showArmorItems;
   public final BooleanSetting showMainHand;
   public final BooleanSetting showOffHand;
   public final BooleanSetting showDistance;
   public final BooleanSetting showPing;
   public final EnumSetting<FontMode> fontMode;
   public final SliderSetting nameScale;
   public final SliderSetting infoScale;
   public final ColorSetting nameColor;
   public final ColorSetting friendColor;
   public final ColorSetting healthColor;
   public final ColorSetting healthBarBgColor;
   public final ColorSetting distanceColor;
   public final SliderSetting skinSize;
   public final SliderSetting skinPadding;
   public final SliderSetting healthBarHeight;
   public final SliderSetting healthBarPadding;
   public final SliderSetting itemSize;
   public final SliderSetting itemSpacing;
   public final SliderSetting animationSpeed;
   public final SliderSetting particleCount;
   public final SliderSetting particleSize;
   private final List<PlayerData> visiblePlayers;

   public PlayerInfoHUD() {
      super("PlayerInfoHUD", Module.Category.Render);
      this.layout = this.add(new EnumSetting("Layout", PlayerInfoHUD.CardLayout.VERTICAL));
      this.enableBackgroundImage = this.add(new BooleanSetting("EnableBGImage", true));
      this.imageAlpha = this.add(new SliderSetting("ImageAlpha", (double)150.0F, (double)0.0F, (double)255.0F, (double)1.0F));
      this.imageMode = this.add(new EnumSetting("ImageMode", PlayerInfoHUD.ImageMode.RELAY));
      this.enableCardBackground = this.add(new BooleanSetting("EnableCardBG", true));
      this.cardBgColor = this.add(new ColorSetting("CardBG", new Color(Integer.MIN_VALUE, true)));
      this.cardBorderColor = this.add(new ColorSetting("CardBorder", new Color(-16711936, true)));
      this.cardRoundedCorners = this.add(new BooleanSetting("CardRounded", true));
      this.cardCornerRadius = this.add(new SliderSetting("CardCornerRadius", (double)8.0F, (double)0.0F, (double)20.0F, (double)0.5F));
      this.cardBorderThickness = this.add(new SliderSetting("CardBorderThickness", (double)2.0F, (double)0.0F, (double)5.0F, (double)0.5F));
      this.enableBlur = this.add(new BooleanSetting("EnableBlur", true));
      this.blurStrength = this.add(new SliderSetting("BlurStrength", (double)5.0F, (double)1.0F, (double)20.0F, (double)0.5F));
      this.blurRadius = this.add(new SliderSetting("BlurRadius", (double)8.0F, (double)0.0F, (double)20.0F, (double)0.5F));
      this.blurOverlay = this.add(new ColorSetting("BlurOverlay", new Color(1610612736, true)));
      this.maxPlayers = this.add(new SliderSetting("MaxPlayers", (double)3.0F, (double)1.0F, (double)10.0F, (double)1.0F));
      this.maxDistance = this.add(new SliderSetting("MaxDistance", (double)50.0F, (double)10.0F, (double)200.0F, (double)1.0F));
      this.showSkin = this.add(new BooleanSetting("ShowSkin", true));
      this.showName = this.add(new BooleanSetting("ShowName", true));
      this.showHealth = this.add(new BooleanSetting("ShowHealth", true));
      this.showHealthBar = this.add(new BooleanSetting("HealthBar", true));
      this.showHealthAnimation = this.add(new BooleanSetting("HealthAnimation", true));
      this.showItems = this.add(new BooleanSetting("ShowItems", true));
      this.showArmorItems = this.add(new BooleanSetting("ShowArmor", true));
      this.showMainHand = this.add(new BooleanSetting("ShowMainHand", true));
      this.showOffHand = this.add(new BooleanSetting("ShowOffHand", true));
      this.showDistance = this.add(new BooleanSetting("ShowDistance", true));
      this.showPing = this.add(new BooleanSetting("ShowPing", false));
      this.fontMode = this.add(new EnumSetting("Font", PlayerInfoHUD.FontMode.VANILLA));
      this.nameScale = this.add(new SliderSetting("NameScale", (double)1.0F, (double)0.5F, (double)2.0F, 0.1));
      this.infoScale = this.add(new SliderSetting("InfoScale", 0.8, (double)0.5F, (double)2.0F, 0.1));
      this.nameColor = this.add(new ColorSetting("NameColor", new Color(-1, true)));
      this.friendColor = this.add(new ColorSetting("FriendColor", new Color(-16711936, true)));
      this.healthColor = this.add(new ColorSetting("HealthColor", new Color(-65536, true)));
      this.healthBarBgColor = this.add(new ColorSetting("HealthBarBG", new Color(-2144128205, true)));
      this.distanceColor = this.add(new ColorSetting("DistanceColor", new Color(-256, true)));
      this.skinSize = this.add(new SliderSetting("SkinSize", (double)40.0F, (double)20.0F, (double)60.0F, (double)1.0F));
      this.skinPadding = this.add(new SliderSetting("SkinPadding", (double)5.0F, (double)0.0F, (double)20.0F, (double)1.0F));
      this.healthBarHeight = this.add(new SliderSetting("HealthBarHeight", (double)6.0F, (double)2.0F, (double)10.0F, (double)0.5F));
      this.healthBarPadding = this.add(new SliderSetting("HealthBarPadding", (double)3.0F, (double)0.0F, (double)10.0F, (double)0.5F));
      this.itemSize = this.add(new SliderSetting("ItemSize", (double)16.0F, (double)12.0F, (double)24.0F, (double)1.0F));
      this.itemSpacing = this.add(new SliderSetting("ItemSpacing", (double)2.0F, (double)0.0F, (double)10.0F, (double)0.5F));
      this.animationSpeed = this.add(new SliderSetting("AnimSpeed", (double)5.0F, (double)1.0F, (double)20.0F, (double)0.5F));
      this.particleCount = this.add(new SliderSetting("ParticleCount", (double)8.0F, (double)2.0F, (double)20.0F, (double)1.0F));
      this.particleSize = this.add(new SliderSetting("ParticleSize", (double)2.0F, (double)1.0F, (double)5.0F, (double)0.5F));
      this.visiblePlayers = new ArrayList();
      this.setChinese("玩家信息HUD");
   }

   @EventListener
   public void onRender2D(Render2DEvent event) {
      class_332 context = event.drawContext;
      class_4587 matrices = context.method_51448();
      this.updateVisiblePlayers();
      if (!this.visiblePlayers.isEmpty()) {
         this.calculateCardPositions();
         if (this.enableBackgroundImage.getValue()) {
            this.drawBackgroundImage(context, matrices);
         }

         for(PlayerData playerData : this.visiblePlayers) {
            if (playerData.cardPos != null) {
               this.enableCardScissor(context, playerData.cardPos);
               this.drawPlayerCard(context, matrices, playerData);
               context.method_44380();
            }
         }

      }
   }

   private void enableCardScissor(class_332 context, CardPosition cardPos) {
      int x = (int)cardPos.x;
      int y = (int)cardPos.y;
      int width = (int)cardPos.width;
      int height = (int)cardPos.height;
      context.method_44379(x, y, x + width, y + height);
   }

   private void drawBackgroundImage(class_332 context, class_4587 matrices) {
      try {
         int alpha = (int)this.imageAlpha.getValueFloat();
         float baseX = this.firstCardX.getValueFloat();
         float baseY = this.firstCardY.getValueFloat();
         float imageX = baseX + 0.0F;
         float imageY = baseY + -19.0F;
         context.method_51422(1.0F, 1.0F, 1.0F, (float)alpha / 255.0F);
         if (this.imageMode.getValue() == PlayerInfoHUD.ImageMode.RELAY) {
            int playerCount = this.visiblePlayers.size();
            float textureHeight = 240.0F;
            float textureStartY = 0.0F;
            float displayHeight;
            if (playerCount == 1) {
               displayHeight = textureHeight / 3.0F;
               textureStartY = 0.0F;
            } else if (playerCount == 2) {
               displayHeight = textureHeight / 3.0F;
               textureStartY = textureHeight / 3.0F;
            } else {
               displayHeight = textureHeight;
               textureStartY = 0.0F;
            }

            context.method_25290(BACKGROUND_TEXTURE, (int)imageX, (int)imageY, 0.0F, (float)((int)textureStartY), 200, (int)displayHeight, 200, (int)textureHeight);
         } else {
            context.method_25290(BACKGROUND_TEXTURE, (int)imageX, (int)imageY, 0.0F, 0.0F, 200, 240, 200, 240);
         }

         context.method_51422(1.0F, 1.0F, 1.0F, 1.0F);
      } catch (Exception var12) {
         this.drawFallbackBackground(matrices);
      }

   }

   private void drawFallbackBackground(class_4587 matrices) {
      float baseX = this.firstCardX.getValueFloat();
      float baseY = this.firstCardY.getValueFloat();
      float imageX = baseX + 0.0F;
      float imageY = baseY + -19.0F;
      Color color1 = new Color(1073741824, true);
      if (this.imageMode.getValue() == PlayerInfoHUD.ImageMode.RELAY) {
         int playerCount = this.visiblePlayers.size();
         float currentImageY = imageY;
         float displayHeight;
         if (playerCount == 1) {
            displayHeight = 80.0F;
         } else if (playerCount == 2) {
            displayHeight = 80.0F;
            currentImageY = imageY + 80.0F;
         } else {
            displayHeight = 240.0F;
         }

         Render2DUtil.drawRect(matrices, imageX, currentImageY, 200.0F, displayHeight, color1);
      } else {
         Render2DUtil.drawRect(matrices, imageX, imageY, 200.0F, 240.0F, color1);
      }

   }

   private void calculateCardPositions() {
      float startX = this.firstCardX.getValueFloat();
      float startY = this.firstCardY.getValueFloat();
      float cardWidthValue = this.cardWidth.getValueFloat();
      float cardHeightValue = this.cardHeight.getValueFloat();
      float spacing = this.cardSpacing.getValueFloat();
      CardLayout layoutMode = this.layout.getValue();

      for(int i = 0; i < this.visiblePlayers.size(); ++i) {
         PlayerData playerData = (PlayerData)this.visiblePlayers.get(i);
         float cardY;
         float cardX;
         if (layoutMode == PlayerInfoHUD.CardLayout.VERTICAL) {
            cardX = startX;
            cardY = startY + (float)i * (cardHeightValue + spacing);
         } else {
            cardX = startX + (float)i * (cardWidthValue + spacing);
            cardY = startY;
         }

         playerData.cardPos = new CardPosition(cardX, cardY, cardWidthValue, cardHeightValue);
      }

   }

   private void drawPlayerCard(class_332 context, class_4587 matrices, PlayerData playerData) {
      if (playerData.cardPos != null) {
         float x = playerData.cardPos.x;
         float y = playerData.cardPos.y;
         float w = playerData.cardPos.width;
         float h = playerData.cardPos.height;
         if (this.enableCardBackground.getValue()) {
            if (this.enableBlur.getValue()) {
               this.drawCardBlurBackground(matrices, x, y, w, h);
            }

            if (!this.enableBackgroundImage.getValue()) {
               this.drawCardRoundedBackground(matrices, x, y, w, h);
            }
         }

         this.drawPlayerInCard(context, matrices, x, y, w, h, playerData);
         this.drawCardParticlesOnTop(matrices, x, y, w, h, playerData);
      }
   }

   private void drawCardBlurBackground(class_4587 matrices, float x, float y, float w, float h) {
      float radius = this.cardCornerRadius.getValueFloat();
      float blurStrengthValue = this.blurStrength.getValueFloat();

      for(int i = 0; (float)i < blurStrengthValue; ++i) {
         float offset = (float)i * 0.5F;
         float alpha = (float)((double)this.blurOverlay.getValue().getAlpha() / (double)255.0F * ((double)1.0F - (double)((float)i / blurStrengthValue)));
         Color blurColor = new Color(this.blurOverlay.getValue().getRed(), this.blurOverlay.getValue().getGreen(), this.blurOverlay.getValue().getBlue(), (int)(alpha * 255.0F));
         Render2DUtil.drawRoundedRect(matrices, x - offset, y - offset, w + offset * 2.0F, h + offset * 2.0F, radius + offset, blurColor);
      }

   }

   private void drawCardRoundedBackground(class_4587 matrices, float x, float y, float w, float h) {
      float radius = this.cardCornerRadius.getValueFloat();
      Render2DUtil.drawRoundedRect(matrices, x, y, w, h, radius, this.cardBgColor.getValue());
      if (this.cardBorderThickness.getValueFloat() > 0.0F) {
         Render2DUtil.drawRoundedStroke(matrices, x, y, w, h, radius, this.cardBorderColor.getValue(), 32);
      }

   }

   private void drawPlayerInCard(class_332 context, class_4587 matrices, float x, float y, float w, float h, PlayerData playerData) {
      class_1657 player = playerData.player;
      float padding = this.skinPadding.getValueFloat();
      float skinX = x + padding;
      float skinY = y + (h - this.skinSize.getValueFloat()) / 2.0F;
      if (this.showSkin.getValue()) {
         this.drawPlayerSkin(context, matrices, skinX, skinY, this.skinSize.getValueFloat(), player);
      }

      if (this.cardBorderThickness.getValueFloat() > 0.0F) {
         float radius = Math.min(this.cardCornerRadius.getValueFloat(), this.skinSize.getValueFloat() / 2.0F);
         Render2DUtil.drawRoundedStroke(matrices, skinX, skinY, this.skinSize.getValueFloat(), this.skinSize.getValueFloat(), radius, this.cardBorderColor.getValue(), 32);
      }

      float contentX = skinX + this.skinSize.getValueFloat() + padding;
      float contentWidth = w - (this.skinSize.getValueFloat() + padding * 2.0F);
      float contentStartY = y + padding;
      this.drawPlayerInfo(context, matrices, contentX, contentStartY, contentWidth, player, playerData);
   }

   private void drawPlayerInfo(class_332 context, class_4587 matrices, float x, float y, float maxWidth, class_1657 player, PlayerData playerData) {
      float currentY = y;
      if (this.showName.getValue()) {
         String name = player.method_5477().getString();
         Color nameColorValue = Alien.FRIEND.isFriend(player) ? this.friendColor.getValue() : this.nameColor.getValue();
         matrices.method_22903();
         matrices.method_46416(x, y, 0.0F);
         matrices.method_22905(this.nameScale.getValueFloat(), this.nameScale.getValueFloat(), 1.0F);
         if (this.fontMode.getValue() == PlayerInfoHUD.FontMode.VANILLA) {
            context.method_51433(mc.field_1772, name, 0, 0, nameColorValue.getRGB(), true);
         } else {
            FontManager.ui.drawString(matrices, name, (double)0.0F, (double)0.0F, nameColorValue.getRGB());
         }

         matrices.method_22909();
         float f;
         if (this.fontMode.getValue() == PlayerInfoHUD.FontMode.VANILLA) {
            Objects.requireNonNull(mc.field_1772);
            f = 9.0F;
         } else {
            f = FontManager.ui.getFontHeight();
         }

         float textHeight = f * this.nameScale.getValueFloat();
         currentY = y + textHeight + 2.0F;
      }

      if (this.showHealth.getValue() && this.showHealthBar.getValue()) {
         this.drawHealthBar(matrices, x, currentY, maxWidth, player);
         float maxHealth = player.method_6063();
         float currentHealth = Math.min(player.method_6032() + player.method_6067(), maxHealth);
         float healthPercent = Math.min(currentHealth / maxHealth, 1.0F);
         float filledWidth = maxWidth * healthPercent;
         playerData.currentHealthBarX = x + filledWidth;
         playerData.currentHealthBarY = currentY + this.healthBarHeight.getValueFloat() / 2.0F;
         if (currentHealth < playerData.lastHealth) {
            this.createParticlesAtCurrentHealthBar(playerData);
         }

         currentY += this.healthBarHeight.getValueFloat() + this.healthBarPadding.getValueFloat();
         playerData.lastHealth = currentHealth;
      }

      if (this.showItems.getValue()) {
         this.drawPlayerItems(context, matrices, x, currentY, maxWidth, player);
         currentY += this.itemSize.getValueFloat() + 2.0F;
      }

      this.drawBottomInfo(context, matrices, x, currentY, maxWidth, player);
   }

   private void drawBottomInfo(class_332 context, class_4587 matrices, float x, float y, float maxWidth, class_1657 player) {
      matrices.method_22903();
      matrices.method_46416(x, y, 0.0F);
      matrices.method_22905(this.infoScale.getValueFloat(), this.infoScale.getValueFloat(), 1.0F);
      float infoY = 0.0F;
      float currentX = 0.0F;
      if (this.showDistance.getValue()) {
         float distance = mc.field_1724.method_5739(player);
         String distanceText = String.format("%.1fm", distance);
         float textWidth;
         if (this.fontMode.getValue() == PlayerInfoHUD.FontMode.VANILLA) {
            textWidth = (float)mc.field_1772.method_1727(distanceText);
            context.method_51433(mc.field_1772, distanceText, (int)currentX, (int)infoY, this.distanceColor.getValue().getRGB(), true);
         } else {
            textWidth = FontManager.ui.getWidth(distanceText);
            FontManager.ui.drawString(matrices, distanceText, (double)currentX, (double)infoY, this.distanceColor.getValue().getRGB());
         }

         float var10000 = currentX + textWidth + 8.0F;
      }

      int ping;
      if (this.showPing.getValue() && (ping = this.getPlayerPing(player)) >= 0) {
         String pingText = ping + "ms";
         Color pingColor = this.getPingColor(ping);
         if (this.fontMode.getValue() == PlayerInfoHUD.FontMode.VANILLA) {
            float textWidth = (float)mc.field_1772.method_1727(pingText);
            float pingX = maxWidth / this.infoScale.getValueFloat() - textWidth;
            context.method_51433(mc.field_1772, pingText, (int)pingX, (int)infoY, pingColor.getRGB(), true);
         } else {
            float textWidth = FontManager.ui.getWidth(pingText);
            float pingX = maxWidth / this.infoScale.getValueFloat() - textWidth;
            FontManager.ui.drawString(matrices, pingText, (double)pingX, (double)infoY, pingColor.getRGB());
         }
      }

      matrices.method_22909();
   }

   private void drawCardParticlesOnTop(class_4587 matrices, float cardX, float cardY, float cardW, float cardH, PlayerData playerData) {
      if (this.showHealthAnimation.getValue()) {
         List<HealthParticle> particles = playerData.particles;

         for(int i = particles.size() - 1; i >= 0; --i) {
            HealthParticle particle = (HealthParticle)particles.get(i);
            particle.update(playerData.currentHealthBarX, playerData.currentHealthBarY);
            if (!particle.isAlive()) {
               particles.remove(i);
            } else if (particle.x >= cardX && particle.x <= cardX + cardW && particle.y >= cardY && particle.y <= cardY + cardH) {
               float size = particle.getSize();
               Color particleColor = new Color(particle.color.getRed(), particle.color.getGreen(), particle.color.getBlue(), (int)((float)particle.color.getAlpha() * particle.life));
               Render2DUtil.drawRect(matrices, particle.x, particle.y, size, size, particleColor);
            }
         }

      }
   }

   private void createParticlesAtCurrentHealthBar(PlayerData playerData) {
      if (this.showHealthAnimation.getValue()) {
         class_1657 player = playerData.player;
         int particleCountValue = (int)this.particleCount.getValueFloat();

         for(int i = 0; i < particleCountValue; ++i) {
            float particleX = playerData.currentHealthBarX + (float)(Math.random() * (double)10.0F) - 5.0F;
            float particleY = playerData.currentHealthBarY + (float)(Math.random() * (double)10.0F) - 5.0F;
            float currentHealth = player.method_6032() + player.method_6067();
            float maxHealth = player.method_6063();
            float healthPercent = Math.min(currentHealth / maxHealth, 1.0F);
            Color particleColor = this.getHealthBarColor(healthPercent);
            playerData.particles.add(new HealthParticle(this, this, particleX, particleY, particleColor));
         }

      }
   }

   private void updateVisiblePlayers() {
      if (mc.field_1687 != null && mc.field_1724 != null) {
         List allPlayers = (List)mc.field_1687.method_18456().stream().filter((player) -> player != mc.field_1724 && !player.method_5767() && player.method_5805()).filter((player) -> !Alien.FRIEND.isFriend((class_1657)player)).filter((player) -> mc.field_1724.method_5739(player) <= this.maxDistance.getValueFloat()).sorted(Comparator.comparingDouble((p) -> (double)mc.field_1724.method_5739(p))).limit((long)((int)this.maxPlayers.getValueFloat())).collect(Collectors.toList());
         this.visiblePlayers.removeIf((playerData) -> !allPlayers.contains(playerData.player));

         for(Object player2 : allPlayers) {
            boolean exists = this.visiblePlayers.stream().anyMatch((pd) -> pd.player == player2);
            if (!exists) {
               PlayerData newData = new PlayerData();
               newData.player = (class_1657)player2;
               newData.distance = mc.field_1724.method_5739((class_1297)player2);
               newData.lastHealth = ((class_1657)player2).method_6063() + ((class_1657)player2).method_6067();
               this.visiblePlayers.add(newData);
            }
         }

         this.visiblePlayers.sort(Comparator.comparingDouble((pd) -> (double)pd.distance));
      } else {
         this.visiblePlayers.clear();
      }
   }

   private void drawPlayerItems(class_332 context, class_4587 matrices, float x, float y, float maxWidth, class_1657 player) {
      float itemSizeValue = this.itemSize.getValueFloat();
      float itemSpacingValue = this.itemSpacing.getValueFloat();
      ArrayList<class_1799> itemsToShow = new ArrayList();
      class_1799 offHand;
      if (this.showOffHand.getValue() && !(offHand = player.method_6079()).method_7960()) {
         itemsToShow.add(offHand);
      }

      if (this.showArmorItems.getValue()) {
         for(int i = 3; i >= 0; --i) {
            class_1799 armorItem = (class_1799)player.method_31548().field_7548.get(i);
            if (!armorItem.method_7960()) {
               itemsToShow.add(armorItem);
            }
         }
      }

      class_1799 mainHand;
      if (this.showMainHand.getValue() && !(mainHand = player.method_6047()).method_7960()) {
         itemsToShow.add(mainHand);
      }

      if (!itemsToShow.isEmpty()) {
         float totalWidth = (float)itemsToShow.size() * itemSizeValue + (float)(itemsToShow.size() - 1) * itemSpacingValue;
         float startX = x + (maxWidth - totalWidth) / 2.0F;
         float itemY = y;

         for(int i = 0; i < itemsToShow.size(); ++i) {
            float itemX = startX + (float)i * (itemSizeValue + itemSpacingValue);
            this.drawItem(context, matrices, (class_1799)itemsToShow.get(i), itemX, itemY, itemSizeValue);
         }

      }
   }

   private void drawItem(class_332 context, class_4587 matrices, class_1799 itemStack, float x, float y, float size) {
      if (!itemStack.method_7960()) {
         matrices.method_22903();
         matrices.method_46416(x, y, 0.0F);
         matrices.method_22905(size / 16.0F, size / 16.0F, 1.0F);
         class_308.method_24210();
         context.method_51427(itemStack, 0, 0);
         context.method_51431(mc.field_1772, itemStack, 0, 0);
         matrices.method_22909();
         if (this.cardBorderThickness.getValueFloat() > 0.0F) {
            float thickness = Math.max(1.0F, this.cardBorderThickness.getValueFloat());
            Color borderColorValue = this.cardBorderColor.getValue();
            Render2DUtil.drawRect(matrices, x - thickness, y - thickness, size + thickness * 2.0F, thickness, borderColorValue);
            Render2DUtil.drawRect(matrices, x - thickness, y + size, size + thickness * 2.0F, thickness, borderColorValue);
            Render2DUtil.drawRect(matrices, x - thickness, y, thickness, size, borderColorValue);
            Render2DUtil.drawRect(matrices, x + size, y, thickness, size, borderColorValue);
         }

      }
   }

   private boolean drawPlayerSkin(class_332 context, class_4587 matrices, float x, float y, float size, class_1657 player) {
      try {
         float radius = Math.min(this.cardCornerRadius.getValueFloat(), size / 2.0F);
         Render2DUtil.drawRoundedRect(matrices, x, y, size, size, radius, new Color(-13421773));
         float innerSize = size - 4.0F;
         float innerX = x + 2.0F;
         float innerY = y + 2.0F;
         float innerRadius = Math.max(0.0F, radius - 2.0F);
         Render2DUtil.drawRoundedRect(matrices, innerX, innerY, innerSize, innerSize, innerRadius, new Color(-11184811));
         return true;
      } catch (Exception var12) {
         return false;
      }
   }

   private void drawHealthBar(class_4587 matrices, float x, float y, float maxWidth, class_1657 player) {
      float maxHealth = player.method_6063();
      float currentHealth = Math.min(player.method_6032() + player.method_6067(), maxHealth);
      float healthPercent = Math.min(currentHealth / maxHealth, 1.0F);
      float barHeight = this.healthBarHeight.getValueFloat();
      float barRadius = Math.min(barHeight / 2.0F, this.cardCornerRadius.getValueFloat());
      Render2DUtil.drawRoundedRect(matrices, x, y, maxWidth, barHeight, barRadius, this.healthBarBgColor.getValue());
      float filledWidth = maxWidth * healthPercent;
      if (filledWidth > 0.0F) {
         Color healthColorValue = this.getHealthBarColor(healthPercent);
         if (filledWidth >= maxWidth - 0.5F) {
            Render2DUtil.drawRoundedRect(matrices, x, y, maxWidth, barHeight, barRadius, healthColorValue);
         } else if (filledWidth > barRadius * 2.0F) {
            Render2DUtil.drawRect(matrices, x + barRadius, y, filledWidth - barRadius * 2.0F, barHeight, healthColorValue);
            Render2DUtil.drawRoundedRect(matrices, x, y, barRadius * 2.0F, barHeight, barRadius, healthColorValue);
         } else {
            Render2DUtil.drawRoundedRect(matrices, x, y, filledWidth, barHeight, barRadius, healthColorValue);
         }
      }

   }

   private Color getHealthBarColor(float percent) {
      if (percent > 0.7F) {
         return new Color(-16711936);
      } else if (percent > 0.4F) {
         return new Color(-256);
      } else {
         return percent > 0.2F ? new Color(-39424) : new Color(-65536);
      }
   }

   private Color getPingColor(int ping) {
      if (ping < 100) {
         return new Color(-16711936);
      } else if (ping < 200) {
         return new Color(-256);
      } else {
         return ping < 300 ? new Color(-39424) : new Color(-65536);
      }
   }

   private int getPlayerPing(class_1657 player) {
      if (mc.method_1562() == null) {
         return -1;
      } else {
         class_640 entry = mc.method_1562().method_2871(player.method_5667());
         return entry != null ? entry.method_2959() : -1;
      }
   }

   public boolean onEnable() {
      super.onEnable();
      this.visiblePlayers.clear();
      return false;
   }

   public void onDisable() {
      super.onDisable();
      this.visiblePlayers.clear();
   }

   public static enum CardLayout {
      VERTICAL("Vertical"),
      HORIZONTAL("Horizontal");

      private final String name;

      private CardLayout(String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      // $FF: synthetic method
      private static CardLayout[] $values() {
         return new CardLayout[]{VERTICAL, HORIZONTAL};
      }
   }

   public static enum ImageMode {
      NORMAL("Normal"),
      RELAY("Relay");

      private final String name;

      private ImageMode(String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      // $FF: synthetic method
      private static ImageMode[] $values() {
         return new ImageMode[]{NORMAL, RELAY};
      }
   }

   public static enum FontMode {
      VANILLA("Vanilla"),
      CUSTOM("Custom");

      private final String name;

      private FontMode(String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      // $FF: synthetic method
      private static FontMode[] $values() {
         return new FontMode[]{VANILLA, CUSTOM};
      }
   }

   private static class PlayerData {
      class_1657 player;
      float distance;
      float lastHealth;
      List<HealthParticle> particles = new ArrayList();
      float currentHealthBarX = 0.0F;
      float currentHealthBarY = 0.0F;
      CardPosition cardPos;
   }

   private static class CardPosition {
      float x;
      float y;
      float width;
      float height;

      CardPosition(float x, float y, float width, float height) {
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
      }
   }

   private class HealthParticle {
      float x;
      float y;
      float velocityX;
      float velocityY;
      float life;
      Color color;
      PlayerInfoHUD parent;

      HealthParticle(PlayerInfoHUD playerInfoHUD, PlayerInfoHUD parent, float x, float y, Color color) {
         this.parent = parent;
         this.x = x;
         this.y = y;
         this.color = color;
         this.velocityX = (float)((Math.random() - (double)0.5F) * (double)4.0F);
         this.velocityY = (float)((Math.random() - (double)0.5F) * (double)4.0F - (double)2.0F);
         this.life = 1.0F;
      }

      void update(float healthX, float healthY) {
         float dx = healthX - this.x;
         float dy = healthY - this.y;
         float distance = (float)Math.sqrt((double)(dx * dx + dy * dy));
         if (distance > 0.0F) {
            float force = 0.2F;
            this.velocityX += dx / distance * force;
            this.velocityY += dy / distance * force;
         }

         this.x += this.velocityX;
         this.y += this.velocityY;
         this.velocityY += 0.1F;
         this.velocityX *= 0.95F;
         this.velocityY *= 0.95F;
         this.life -= 0.02F * this.parent.animationSpeed.getValueFloat();
      }

      boolean isAlive() {
         return this.life > 0.0F;
      }

      float getSize() {
         return this.parent.particleSize.getValueFloat();
      }
   }
}
