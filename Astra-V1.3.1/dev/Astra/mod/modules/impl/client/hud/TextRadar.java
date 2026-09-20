package dev.Astra.mod.modules.impl.client.hud;

import dev.Astra.Astra;
import dev.Astra.core.impl.FontManager;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import net.minecraft.class_742;

public class TextRadar extends HudModule {
   public static TextRadar INSTANCE;
   private final DecimalFormat df = new DecimalFormat("0.0");
   private final BooleanSetting friend = this.add(new BooleanSetting("Friend", true));
   private final BooleanSetting doubleBlank = this.add(new BooleanSetting("Double", false));
   private final BooleanSetting health = this.add(new BooleanSetting("Health", true));
   private final BooleanSetting pops = this.add(new BooleanSetting("Pops", true));
   public final BooleanSetting red = this.add(new BooleanSetting("Red", false));
   private final BooleanSetting getDistance = this.add(new BooleanSetting("Distance", true));
   private final BooleanSetting effects = this.add(new BooleanSetting("Effects", true));

   public TextRadar() {
      super("TextRadar", "文字雷达", 0, 100);
      INSTANCE = this;
   }

   public void onRender2D(class_332 drawContext, float tickDelta) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         int maxW = 0;
         int lineH;
         if (HudSetting.useFont()) {
            lineH = (int)FontManager.ui.getFontHeight();
         } else {
            Objects.requireNonNull(mc.field_1772);
            lineH = 9;
         }

         ArrayList<String> linesText = new ArrayList();
         ArrayList<Integer> linesColor = new ArrayList();
         ArrayList<class_742> players = new ArrayList(mc.field_1687.method_18456());
         players.sort(Comparator.comparingDouble((player) -> (double)mc.field_1724.method_5739(player)));
         double counter = (double)20.0F;

         for(class_1657 playerEntity : players) {
            if (playerEntity != mc.field_1724) {
               StringBuilder stringBuilder = new StringBuilder();
               String blank = this.doubleBlank.getValue() ? "  " : " ";
               if (this.health.getValue()) {
                  stringBuilder.append(getHealthColor(playerEntity));
                  stringBuilder.append(this.df.format((double)(playerEntity.method_6032() + playerEntity.method_6067())));
                  stringBuilder.append(blank);
               }

               stringBuilder.append(class_124.field_1070);
               stringBuilder.append(playerEntity.method_5477().getString());
               if (this.getDistance.getValue()) {
                  stringBuilder.append(blank);
                  stringBuilder.append(class_124.field_1068);
                  stringBuilder.append(this.df.format((double)mc.field_1724.method_5739(playerEntity)));
                  stringBuilder.append("m");
               }

               if (this.effects.getValue()) {
                  if (playerEntity.method_6059(class_1294.field_5909)) {
                     stringBuilder.append(blank);
                     stringBuilder.append(class_124.field_1080);
                     stringBuilder.append("Lv.");
                     stringBuilder.append(playerEntity.method_6112(class_1294.field_5909).method_5578() + 1);
                     stringBuilder.append(blank);
                     stringBuilder.append(playerEntity.method_6112(class_1294.field_5909).method_5584() / 20 + 1);
                     stringBuilder.append("s");
                  }

                  if (playerEntity.method_6059(class_1294.field_5904)) {
                     stringBuilder.append(blank);
                     stringBuilder.append(class_124.field_1075);
                     stringBuilder.append("Lv.");
                     stringBuilder.append(playerEntity.method_6112(class_1294.field_5904).method_5578() + 1);
                     stringBuilder.append(blank);
                     stringBuilder.append(playerEntity.method_6112(class_1294.field_5904).method_5584() / 20 + 1);
                     stringBuilder.append("s");
                  }

                  if (playerEntity.method_6059(class_1294.field_5910)) {
                     stringBuilder.append(blank);
                     stringBuilder.append(class_124.field_1079);
                     stringBuilder.append("Lv.");
                     stringBuilder.append(playerEntity.method_6112(class_1294.field_5910).method_5578() + 1);
                     stringBuilder.append(blank);
                     stringBuilder.append(playerEntity.method_6112(class_1294.field_5910).method_5584() / 20 + 1);
                     stringBuilder.append("s");
                  }

                  if (playerEntity.method_6059(class_1294.field_5907)) {
                     stringBuilder.append(blank);
                     stringBuilder.append(class_124.field_1078);
                     stringBuilder.append("Lv.");
                     stringBuilder.append(playerEntity.method_6112(class_1294.field_5907).method_5578() + 1);
                     stringBuilder.append(blank);
                     stringBuilder.append(playerEntity.method_6112(class_1294.field_5907).method_5584() / 20 + 1);
                     stringBuilder.append("s");
                  }
               }

               int totemPopped;
               if (this.pops.getValue() && (totemPopped = Astra.POP.getPop(playerEntity)) > 0) {
                  stringBuilder.append(blank);
                  stringBuilder.append(getPopColor(totemPopped));
                  stringBuilder.append("-");
                  stringBuilder.append(totemPopped);
               }

               boolean isFriend;
               if (!(isFriend = Astra.FRIEND.isFriend(playerEntity)) || this.friend.getValue()) {
                  counter += (double)10.0F;
                  int color = isFriend ? this.getFriendColor(counter) : this.getHudColor(counter);
                  String s = stringBuilder.toString();
                  int w = HudSetting.useFont() ? (int)FontManager.ui.getWidth(s) : mc.field_1772.method_1727(s);
                  maxW = Math.max(maxW, w);
                  linesText.add(s);
                  linesColor.add(color);
               }
            }
         }

         if (linesText.isEmpty()) {
            this.clearHudBounds();
         } else {
            int totalH = Math.max(1, linesText.size() * lineH);
            int startX = this.getHudRenderX(Math.max(1, maxW));
            int startY = this.getHudRenderY(totalH);
            int currentY = startY;

            for(int i = 0; i < linesText.size(); ++i) {
               String s = (String)linesText.get(i);
               int color = (Integer)linesColor.get(i);
               if (HudSetting.useFont()) {
                  FontManager.ui.drawString(drawContext.method_51448(), s, (double)startX, (double)currentY, color, HudSetting.useShadow());
               } else {
                  drawContext.method_51433(mc.field_1772, s, startX, currentY, color, HudSetting.useShadow());
               }

               currentY += lineH;
            }

            this.setHudBounds(startX, startY, Math.max(1, maxW), Math.max(1, totalH));
         }
      } else {
         this.clearHudBounds();
      }
   }

   public static class_124 getHealthColor(class_1657 player) {
      double health = (double)(player.method_6032() + player.method_6067());
      if (health > (double)18.0F) {
         return class_124.field_1060;
      } else if (health > (double)16.0F) {
         return class_124.field_1077;
      } else if (health > (double)12.0F) {
         return class_124.field_1054;
      } else if (health > (double)8.0F) {
         return class_124.field_1065;
      } else {
         return health > (double)4.0F ? class_124.field_1061 : class_124.field_1079;
      }
   }

   private int getHudColor(double delay) {
      ClickGui gui = ClickGui.getInstance();
      return gui == null ? -1 : gui.getColor(delay).getRGB();
   }

   private int getFriendColor(double delay) {
      ClickGui gui = ClickGui.getInstance();
      return gui == null ? -1 : gui.getActiveColor(delay).getRGB();
   }

   public static class_124 getPopColor(int totems) {
      if (INSTANCE.red.getValue()) {
         return class_124.field_1061;
      } else if (totems > 10) {
         return class_124.field_1079;
      } else if (totems > 8) {
         return class_124.field_1061;
      } else if (totems > 6) {
         return class_124.field_1065;
      } else if (totems > 4) {
         return class_124.field_1054;
      } else {
         return totems > 2 ? class_124.field_1077 : class_124.field_1060;
      }
   }
}
