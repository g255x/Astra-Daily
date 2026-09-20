package dev.luminous.mod.modules.impl.client;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DeathEvent;
import dev.luminous.api.events.impl.EntitySpawnEvent;
import dev.luminous.api.events.impl.RemoveEntityEvent;
import dev.luminous.api.events.impl.TotemEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.FadeUtils;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Objects;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_4587;

public class Notification extends Module {
   public static Notification INSTANCE;
   public final EnumSetting<Mode> mode;
   public final EnumSetting<TextStyle> textStyle;
   private final ColorSetting textColor;
   private final SliderSetting stayTime;
   private final BooleanSetting font;
   private final BooleanSetting shadow;
   public final BooleanSetting visualRange;
   public final BooleanSetting showfriend;
   public final BooleanSetting popCounter;
   private final SliderSetting baseY;
   private final SliderSetting rangeY;
   private final SliderSetting rightOffset;
   private final SliderSetting xTime;
   private final EnumSetting<Easing> xEase;
   private final SliderSetting yTime;
   private final EnumSetting<Easing> yEase;
   private final SliderSetting notificationWidth;
   private final SliderSetting notificationHeight;
   private final SliderSetting outlineWidth;
   private final ColorSetting fillColor;
   private final ColorSetting outlineColor;
   private final BooleanSetting blur;
   private final SliderSetting blurRadius;
   private final EnumSetting<TextPosition> textPosition;
   private final BooleanSetting showProgress;
   private final SliderSetting Height;
   private final ColorSetting Color;
   private final EnumSetting<ProgressPosition> Position;
   private final SliderSetting maxCount;
   private final EnumSetting<Easing> ease;
   private final LinkedList<NotificationBar> notifications;

   public Notification() {
      super("Notification", Module.Category.Client);
      this.mode = this.add(new EnumSetting("Mode", Notification.Mode.Screen));
      this.textStyle = this.add(new EnumSetting("TextStyle", Notification.TextStyle.Mio));
      this.textColor = this.add(new ColorSetting("TextColor", new Color(255, 255, 255, 255)));
      this.stayTime = this.add((new SliderSetting("StayTime", 1200, 0, 5000)).setSuffix("ms"));
      this.font = this.add(new BooleanSetting("Font", true));
      this.shadow = this.add(new BooleanSetting("Shadow", true));
      this.visualRange = this.add((new BooleanSetting("VisualRange", false)).setParent());
      BooleanSetting var10006 = this.visualRange;
      Objects.requireNonNull(var10006);
      this.showfriend = this.add(new BooleanSetting("ShowFriends", false, var10006::isOpen));
      this.popCounter = this.add(new BooleanSetting("PopCounter", true));
      this.baseY = this.add(new SliderSetting("BaseY", 300, 0, 1000, () -> this.mode.is(Notification.Mode.Screen)));
      this.rangeY = this.add(new SliderSetting("RangeY", 120, 20, 600, () -> this.mode.is(Notification.Mode.Screen)));
      this.rightOffset = this.add(new SliderSetting("RightOffset", 0, -100, 100, () -> this.mode.is(Notification.Mode.Screen)));
      this.xTime = this.add((new SliderSetting("XTime", 500, 0, 5000, () -> this.mode.is(Notification.Mode.Screen))).setSuffix("ms"));
      this.xEase = this.add(new EnumSetting("XEase", Easing.Linear, () -> this.mode.is(Notification.Mode.Screen)));
      this.yTime = this.add((new SliderSetting("YTime", 1000, 0, 5000, () -> this.mode.is(Notification.Mode.Screen))).setSuffix("ms"));
      this.yEase = this.add(new EnumSetting("YEase", Easing.Linear, () -> this.mode.is(Notification.Mode.Screen)));
      this.notificationWidth = this.add(new SliderSetting("Width", 200, 100, 400, () -> this.mode.is(Notification.Mode.Screen)));
      this.notificationHeight = this.add(new SliderSetting("Height", 20, 5, 40, () -> this.mode.is(Notification.Mode.Screen)));
      this.outlineWidth = this.add(new SliderSetting("OutlineWidth", (double)0.5F, (double)0.0F, (double)5.0F, (double)0.1F, () -> this.mode.is(Notification.Mode.Screen)));
      this.fillColor = this.add(new ColorSetting("FillColor", new Color(30, 30, 30, 180), () -> this.mode.is(Notification.Mode.Screen)));
      this.outlineColor = this.add(new ColorSetting("OutlineColor", new Color(255, 255, 255, 255), () -> this.mode.is(Notification.Mode.Screen)));
      this.blur = this.add((new BooleanSetting("Blur", false, () -> this.mode.is(Notification.Mode.Screen))).setParent());
      this.blurRadius = this.add(new SliderSetting("BlurRadius", (double)10.0F, (double)0.0F, (double)100.0F, (double)1.0F, () -> this.mode.is(Notification.Mode.Screen) && this.blur.isOpen()));
      this.textPosition = this.add(new EnumSetting("TextPosition", Notification.TextPosition.Center, () -> this.mode.is(Notification.Mode.Screen)));
      this.showProgress = this.add((new BooleanSetting("ShowProgress", true, () -> this.mode.is(Notification.Mode.Screen))).setParent());
      this.Height = this.add(new SliderSetting("PHeight", 1, 1, 8, () -> this.mode.is(Notification.Mode.Screen) && this.showProgress.isOpen()));
      this.Color = this.add(new ColorSetting("PColor", new Color(255, 255, 255, 255), () -> this.mode.is(Notification.Mode.Screen) && this.showProgress.isOpen()));
      this.Position = this.add(new EnumSetting("PPosition", Notification.ProgressPosition.Bottom, () -> this.mode.is(Notification.Mode.Screen) && this.showProgress.isOpen()));
      this.maxCount = this.add(new SliderSetting("MaxCount", 5, 1, 10, () -> this.mode.is(Notification.Mode.TopCenter)));
      this.ease = this.add(new EnumSetting("Ease", Easing.Linear, () -> this.mode.is(Notification.Mode.TopCenter)));
      this.notifications = new LinkedList();
      this.setChinese("通知");
      INSTANCE = this;
   }

   public void push(String text) {
      String formattedText = this.formatText(text);
      this.notifications.addFirst(new NotificationBar(formattedText));
   }

   public void push(String text, Color color) {
      String formattedText = this.formatText(text);
      this.notifications.addFirst(new NotificationBar(formattedText, color));
   }

   private String formatText(String text) {
      String lowerText = text.toLowerCase();
      if (!lowerText.contains("error") && !lowerText.contains("failed") && !lowerText.contains("disabled")) {
         return !lowerText.contains("enabled") && !lowerText.contains("success") && !lowerText.contains("complete") ? text : "§2[+] §f" + text;
      } else {
         return "§4[-] §f" + text;
      }
   }

   public void onRender2D(class_332 ctx, float tickDelta) {
      if (this.mode.is(Notification.Mode.Screen)) {
         this.renderScreenMode(ctx);
      } else if (this.mode.is(Notification.Mode.TopCenter)) {
         this.renderTopCenterMode(ctx);
      }

   }

   @EventListener
   public void onAddEntity(EntitySpawnEvent event) {
      if (this.visualRange.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null) {
         String playerName = event.getEntity().method_5476().getString();
         boolean isFriend = Alien.FRIEND.isFriend(playerName);
         if ((!isFriend || this.showfriend.getValue()) && event.getEntity() != mc.field_1724) {
            String message = (isFriend ? playerName : playerName) + "§f entered your visual range.";
            if (isFriend) {
               this.push(message, ColorsModule.INSTANCE.FriendColor.getValue());
            } else {
               this.push(message);
            }

            mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0F, 1.9F);
         }
      }

   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent event) {
      if (this.visualRange.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null) {
         String playerName = event.getEntity().method_5476().getString();
         boolean isFriend = Alien.FRIEND.isFriend(playerName);
         if ((!isFriend || this.showfriend.getValue()) && event.getEntity() != mc.field_1724) {
            String message = (isFriend ? playerName : playerName) + "§f left your visual range.";
            if (isFriend) {
               this.push(message, ColorsModule.INSTANCE.FriendColor.getValue());
            } else {
               this.push(message);
            }

            mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0F, 1.9F);
         }
      }

   }

   @EventListener
   public void onPlayerDeath(DeathEvent event) {
      class_1657 player = event.getPlayer();
      if (this.popCounter.getValue()) {
         if (Alien.POP.popContainer.containsKey(player.method_5477().getString())) {
            int l_Count = (Integer)Alien.POP.popContainer.get(player.method_5477().getString());
            if (l_Count == 1) {
               if (player.equals(mc.field_1724)) {
                  this.push("§fYou§r died after popping §f" + l_Count + "§r totem.");
               } else {
                  String var10001 = player.method_5477().getString();
                  this.push("§f" + var10001 + "§r died after popping §f" + l_Count + "§r totem.");
               }
            } else if (player.equals(mc.field_1724)) {
               this.push("§fYou§r died after popping §f" + l_Count + "§r totems.");
            } else {
               String var4 = player.method_5477().getString();
               this.push("§f" + var4 + "§r died after popping §f" + l_Count + "§r totems.");
            }
         } else if (player.equals(mc.field_1724)) {
            this.push("§fYou§r died.");
         } else {
            this.push("§f" + player.method_5477().getString() + "§r died.");
         }
      }

   }

   @EventListener
   public void onTotem(TotemEvent event) {
      if (this.popCounter.getValue()) {
         class_1657 player = event.getPlayer();
         int l_Count = 1;
         if (Alien.POP.popContainer.containsKey(player.method_5477().getString())) {
            l_Count = (Integer)Alien.POP.popContainer.get(player.method_5477().getString());
         }

         if (l_Count == 1) {
            if (player.equals(mc.field_1724)) {
               this.push("§fYou§r popped §f" + l_Count + "§r totem.");
            } else {
               String var10001 = player.method_5477().getString();
               this.push("§f" + var10001 + " §rpopped §f" + l_Count + "§r totems.");
            }
         } else if (player.equals(mc.field_1724)) {
            this.push("§fYou§r popped §f" + l_Count + "§r totem.");
         } else {
            String var4 = player.method_5477().getString();
            this.push("§f" + var4 + " §rhas popped §f" + l_Count + "§r totems.");
         }
      }

   }

   private void renderScreenMode(class_332 ctx) {
      int spacing = 4;
      int clipTop = this.baseY.getValueInt() - this.rangeY.getValueInt();
      int clipBottom = this.baseY.getValueInt() + 200;
      ctx.method_44379(0, clipTop, mc.method_22683().method_4486(), clipBottom);
      this.updateTargetPositions();

      for(NotificationBar notification : this.notifications) {
         notification.update();
         notification.drawScreen(ctx);
      }

      ctx.method_44380();
      this.notifications.removeIf((n) -> n.stage == Notification.Stage.END && n.slideFade.isEnd());
   }

   private void renderTopCenterMode(class_332 ctx) {
      int max = this.maxCount.getValueInt();

      while(this.notifications.size() > max) {
         this.notifications.removeLast();
      }

      int spacing = 2;
      int startY = 50;
      int screenWidth = mc.method_22683().method_4486();

      for(int i = 0; i < this.notifications.size(); ++i) {
         NotificationBar notification = (NotificationBar)this.notifications.get(i);
         if (notification.textWidth == null && mc != null) {
            notification.textWidth = (int)(this.font.getValue() ? FontManager.ui.getWidth(notification.text) : TextUtil.getWidth(notification.text)) + 20;
         }

         int actualWidth = notification.textWidth != null ? notification.textWidth : 200;
         notification.targetY = startY + i * ((int)(this.font.getValue() ? FontManager.ui.getFontHeight() : TextUtil.getHeight()) + spacing);
         notification.targetX = (screenWidth - actualWidth) / 2;
      }

      for(NotificationBar notification : this.notifications) {
         notification.update();
         notification.drawTopCenter(ctx);
      }

      this.notifications.removeIf((n) -> n.stage == Notification.Stage.END && n.fadeProgress <= 0.0F);
   }

   private void updateTargetPositions() {
      int spacing = 4;
      int screenWidth = mc.method_22683().method_4486();

      for(int i = this.notifications.size() - 1; i >= 0; --i) {
         NotificationBar notification = (NotificationBar)this.notifications.get(i);
         if (i == this.notifications.size() - 1) {
            notification.targetY = this.baseY.getValueInt();
            notification.targetX = screenWidth - notification.width + this.rightOffset.getValueInt();
         } else {
            NotificationBar below = (NotificationBar)this.notifications.get(i + 1);
            notification.targetY = below.currentY - notification.height - spacing;
            notification.targetX = screenWidth - notification.width + this.rightOffset.getValueInt();
         }
      }

   }

   public static enum Mode {
      Screen,
      TopCenter;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Screen, TopCenter};
      }
   }

   public static enum TextStyle {
      Mio,
      HookCross;

      // $FF: synthetic method
      private static TextStyle[] $values() {
         return new TextStyle[]{Mio, HookCross};
      }
   }

   private class NotificationBar {
      final FadeUtils slideFade;
      final FadeUtils stayFade;
      final FadeUtils fadeOutFade;
      final String text;
      final Color customColor;
      int currentX;
      int targetX;
      int currentY;
      int targetY;
      final int width;
      final int height;
      Integer textWidth;
      Stage stage;
      float fadeProgress;
      private float progressWidth;
      private float lastTargetWidth;

      NotificationBar(String text) {
         this(text, (Color)null);
      }

      NotificationBar(String text, Color color) {
         this.textWidth = null;
         this.stage = Notification.Stage.START;
         this.fadeProgress = 0.0F;
         this.progressWidth = 0.0F;
         this.lastTargetWidth = 0.0F;
         this.text = text;
         this.customColor = color;
         if (Notification.this.mode.is(Notification.Mode.Screen)) {
            this.width = Notification.this.notificationWidth.getValueInt();
            this.height = Notification.this.notificationHeight.getValueInt();
         } else {
            this.width = 200;
            this.height = 30;
         }

         if (Notification.this.mode.is(Notification.Mode.Screen)) {
            this.slideFade = new FadeUtils((long)Notification.this.xTime.getValueInt());
            this.fadeOutFade = new FadeUtils(500L);
         } else {
            this.slideFade = new FadeUtils(800L);
            this.fadeOutFade = new FadeUtils(1000L);
         }

         this.stayFade = new FadeUtils((long)Notification.this.stayTime.getValueInt());
         this.currentX = this.targetX = 0;
         this.currentY = this.targetY = -this.height;
         this.fadeProgress = 0.0F;
         this.textWidth = null;
         this.slideFade.reset();
      }

      void update() {
         Easing easing = Notification.this.mode.is(Notification.Mode.Screen) ? (Easing)Notification.this.xEase.getValue() : (Easing)Notification.this.ease.getValue();
         if (Notification.this.mode.is(Notification.Mode.Screen)) {
            this.updateScreenMode(easing);
         } else {
            this.updateTopCenterMode(easing);
         }

      }

      private void updateScreenMode(Easing easing) {
         this.currentY += (int)((double)(this.targetY - this.currentY) * this.slideFade.ease(easing));
         switch (this.stage.ordinal()) {
            case 0:
               this.currentX += (int)((double)(this.targetX - this.currentX) * this.slideFade.ease(easing));
               if (this.slideFade.isEnd()) {
                  this.stage = Notification.Stage.STAY;
                  this.stayFade.reset();
               }
               break;
            case 1:
               if (this.stayFade.isEnd()) {
                  this.stage = Notification.Stage.END;
                  this.slideFade.reset();
               }
               break;
            case 2:
               int offscreen = Wrapper.mc.method_22683().method_4486() + this.width;
               this.currentX += (int)((double)(offscreen - this.currentX) * this.slideFade.ease(easing));
         }

      }

      private void updateTopCenterMode(Easing easing) {
         switch (this.stage.ordinal()) {
            case 0:
               this.currentY += (int)((double)(this.targetY - this.currentY) * this.slideFade.ease(easing));
               this.fadeProgress = Math.min(1.0F, this.fadeProgress + 0.05F);
               if (this.slideFade.isEnd()) {
                  this.stage = Notification.Stage.STAY;
                  this.stayFade.reset();
                  this.fadeProgress = 1.0F;
               }
               break;
            case 1:
               this.currentY += (int)((float)(this.targetY - this.currentY) * 0.2F);
               if (this.stayFade.isEnd()) {
                  this.stage = Notification.Stage.END;
                  this.fadeOutFade.reset();
               }
               break;
            case 2:
               this.fadeProgress = Math.max(0.0F, 1.0F - (float)this.fadeOutFade.ease(easing));
         }

      }

      void drawScreen(class_332 ctx) {
         class_4587 matrices = ctx.method_51448();
         float borderWidth = Notification.this.outlineWidth.getValueFloat();
         if (borderWidth > 0.0F) {
            this.drawOutline(matrices, borderWidth);
         }

         ctx.method_44379(this.currentX, this.currentY, this.currentX + this.width, this.currentY + this.height);
         if (Notification.this.blur.getValue()) {
            Alien.BLUR.applyBlur(Notification.this.blurRadius.getValueFloat(), (float)this.currentX, (float)this.currentY, (float)this.width, (float)this.height);
         }

         Render2DUtil.drawRect(matrices, (float)this.currentX, (float)this.currentY, (float)this.width, (float)this.height, Notification.this.fillColor.getValue());
         int textX = this.getTextPosition();
         int textY = (int)((float)this.currentY + ((float)this.height - TextUtil.getHeight()) / 2.0F);
         int drawColor = this.customColor != null ? this.customColor.getRGB() : Notification.this.textColor.getValue().getRGB();
         TextUtil.drawString(ctx, this.text, (double)textX, (double)textY, drawColor, Notification.this.font.getValue(), Notification.this.shadow.getValue());
         if (this.stage == Notification.Stage.STAY && Notification.this.showProgress.getValue()) {
            this.drawProgressBar(ctx);
         }

         ctx.method_44380();
      }

      void drawTopCenter(class_332 ctx) {
         Color baseColor = this.customColor != null ? this.customColor : Notification.this.textColor.getValue();
         Color color = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), (int)(255.0F * this.fadeProgress));
         TextUtil.drawString(ctx, this.text, (double)this.targetX, (double)this.targetY, color.getRGB(), Notification.this.font.getValue(), Notification.this.shadow.getValue());
      }

      private void drawOutline(class_4587 matrices, float borderWidth) {
         Color color = Notification.this.outlineColor.getValue();
         float x = (float)this.currentX;
         float y = (float)this.currentY;
         float w = (float)this.width;
         float h = (float)this.height;
         Render2DUtil.drawRect(matrices, x - borderWidth, y - borderWidth, w + borderWidth * 2.0F, borderWidth, color);
         Render2DUtil.drawRect(matrices, x - borderWidth, y + h, w + borderWidth * 2.0F, borderWidth, color);
         Render2DUtil.drawRect(matrices, x - borderWidth, y, borderWidth, h, color);
         Render2DUtil.drawRect(matrices, x + w, y, borderWidth, h, color);
      }

      private int getTextPosition() {
         int textWidth = (int)(Notification.this.font.getValue() ? FontManager.ui.getWidth(this.text) : TextUtil.getWidth(this.text));
         switch (((TextPosition)Notification.this.textPosition.getValue()).ordinal()) {
            case 0:
               return this.currentX + 5;
            case 1:
            default:
               return this.currentX + (this.width - textWidth) / 2;
            case 2:
               return this.currentX + this.width - textWidth - 5;
         }
      }

      private void drawProgressBar(class_332 ctx) {
         float remain = (float)((double)1.0F - this.stayFade.getFadeOne());
         remain = Math.max(0.0F, Math.min(1.0F, remain));
         int barHeight = Notification.this.Height.getValueInt();
         Color barColor = Notification.this.Color.getValue();
         int barY = Notification.this.Position.getValue() == Notification.ProgressPosition.Top ? this.currentY + 2 : this.currentY + this.height - barHeight - 2;
         int barX = this.currentX + 4;
         int maxBarW = this.width - 8;
         float targetWidth = (float)maxBarW * remain;
         float currentWidth = this.smoothProgressWidth(targetWidth, (float)maxBarW);
         int barW = (int)currentWidth;
         float alpha = Math.min(1.0F, (float)(this.stayFade.getFadeOne() * (double)2.0F));
         Color bgColor = new Color(barColor.getRed(), barColor.getGreen(), barColor.getBlue(), (int)((float)barColor.getAlpha() * 0.15F * alpha));
         Render2DUtil.drawRect(ctx, (float)barX, (float)barY, (float)maxBarW, (float)barHeight, bgColor);
         if (barW > 0) {
            Color mainColor = new Color(barColor.getRed(), barColor.getGreen(), barColor.getBlue(), (int)((float)barColor.getAlpha() * alpha));
            Render2DUtil.drawRect(ctx, (float)barX, (float)barY, (float)barW, (float)barHeight, mainColor);
            if (remain > 0.1F) {
               int glossWidth = Math.min(20, (int)((float)maxBarW * 0.15F));
               int glossX = barX + Math.min(barW - glossWidth, (int)((float)barW * 0.7F));
               Color glossColor = new Color(255, 255, 255, (int)(30.0F * alpha * (1.0F - remain)));
               Render2DUtil.drawRect(ctx, (float)glossX, (float)barY, (float)glossWidth, (float)(barHeight / 2), glossColor);
            }
         }

      }

      private float smoothProgressWidth(float targetWidth, float maxWidth) {
         if (Math.abs(targetWidth - this.lastTargetWidth) > maxWidth * 0.3F) {
            this.progressWidth = targetWidth;
            this.lastTargetWidth = targetWidth;
            return this.progressWidth;
         } else {
            float smoothing = 0.15F;
            this.progressWidth += (targetWidth - this.progressWidth) * smoothing;
            this.lastTargetWidth = targetWidth;
            return Math.max(0.0F, Math.min(maxWidth, this.progressWidth));
         }
      }
   }

   public static enum Stage {
      START,
      STAY,
      END;

      // $FF: synthetic method
      private static Stage[] $values() {
         return new Stage[]{START, STAY, END};
      }
   }

   public static enum ProgressPosition {
      Top,
      Bottom;

      // $FF: synthetic method
      private static ProgressPosition[] $values() {
         return new ProgressPosition[]{Top, Bottom};
      }
   }

   public static enum TextPosition {
      Left,
      Center,
      Right;

      // $FF: synthetic method
      private static TextPosition[] $values() {
         return new TextPosition[]{Left, Center, Right};
      }
   }
}
