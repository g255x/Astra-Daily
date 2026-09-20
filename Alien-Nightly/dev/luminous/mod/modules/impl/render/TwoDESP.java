package dev.luminous.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4d;

public class TwoDESP extends Module {
   private final EnumSetting page;
   public final ColorSetting armorDuraColor;
   public final ColorSetting hHealth;
   public final ColorSetting mHealth;
   public final ColorSetting lHealth;
   private final BooleanSetting outline;
   private final BooleanSetting renderHealth;
   private final BooleanSetting renderArmor;
   private final SliderSetting durascale;
   private final BooleanSetting drawItem;
   private final BooleanSetting drawItemC;
   public final ColorSetting countColor;
   public final ColorSetting textcolor;
   private final BooleanSetting font;
   private final BooleanSetting players;
   private final BooleanSetting friends;
   private final BooleanSetting crystals;
   private final BooleanSetting creatures;
   private final BooleanSetting monsters;
   private final BooleanSetting ambients;
   private final BooleanSetting others;
   private final ColorSetting playersC;
   private final ColorSetting friendsC;
   private final ColorSetting crystalsC;
   private final ColorSetting creaturesC;
   private final ColorSetting monstersC;
   private final ColorSetting ambientsC;
   private final ColorSetting othersC;

   public TwoDESP() {
      super("2DESP", Module.Category.Render);
      this.page = this.add(new EnumSetting("Settings", TwoDESP.TwoDESPMode.Target));
      this.armorDuraColor = this.add(new ColorSetting("Armor Dura Color", new Color(3145472), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.hHealth = this.add(new ColorSetting("High Health Color", new Color(0, 255, 0, 255), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.mHealth = this.add(new ColorSetting("Mid Health Color", new Color(255, 255, 0, 255), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.lHealth = this.add(new ColorSetting("Low Health Color", new Color(255, 0, 0, 255), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.outline = this.add(new BooleanSetting("Outline", true, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Setting));
      this.renderHealth = this.add(new BooleanSetting("renderHealth", true, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Setting));
      this.renderArmor = this.add(new BooleanSetting("Armor Dura", true, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Setting));
      this.durascale = this.add(new SliderSetting("DuraScale", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, () -> this.renderArmor.getValue()));
      this.drawItem = this.add(new BooleanSetting("draw Item Name", true, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Setting));
      this.drawItemC = this.add(new BooleanSetting("draw Item Count", true, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Setting && this.drawItem.getValue()));
      this.countColor = this.add(new ColorSetting("Item Count Color", new Color(255, 255, 0, 255), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color && this.drawItemC.getValue()));
      this.textcolor = this.add(new ColorSetting("Item Name Color", new Color(255, 255, 255, 255), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color && this.drawItem.getValue()));
      this.font = this.add(new BooleanSetting("CustomFont", true, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Setting));
      this.players = this.add(new BooleanSetting("Players", true, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Target));
      this.friends = this.add(new BooleanSetting("Friends", true, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Target));
      this.crystals = this.add(new BooleanSetting("Crystals", true, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Target));
      this.creatures = this.add(new BooleanSetting("Creatures", false, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Target));
      this.monsters = this.add(new BooleanSetting("Monsters", false, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Target));
      this.ambients = this.add(new BooleanSetting("Ambients", false, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Target));
      this.others = this.add(new BooleanSetting("Others", false, () -> this.page.getValue() == TwoDESP.TwoDESPMode.Target));
      this.playersC = this.add(new ColorSetting("PlayersBox", new Color(16749056), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.friendsC = this.add(new ColorSetting("FriendsBox", new Color(3211008), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.crystalsC = this.add(new ColorSetting("CrystalsBox", new Color(48127), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.creaturesC = this.add(new ColorSetting("CreaturesBox", new Color(10527910), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.monstersC = this.add(new ColorSetting("MonstersBox", new Color(16711680), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.ambientsC = this.add(new ColorSetting("AmbientsBox", new Color(8061183), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
      this.othersC = this.add(new ColorSetting("OthersBox", new Color(16711778), () -> this.page.getValue() == TwoDESP.TwoDESPMode.Color));
   }

   public static float getRotations(class_241 vec) {
      if (mc.field_1724 == null) {
         return 0.0F;
      } else {
         double x = (double)vec.field_1343 - mc.field_1724.method_19538().field_1352;
         double z = (double)vec.field_1342 - mc.field_1724.method_19538().field_1350;
         return (float)(-(Math.atan2(x, z) * (180D / Math.PI)));
      }
   }

   public void onRender2D(class_332 context, float tickDelta) {
      Matrix4f matrix = context.method_51448().method_23760().method_23761();
      Render2DUtil.setupRender();
      RenderSystem.setShader(class_757::method_34540);
      class_287 bufferBuilder = null;
      boolean hasVertices = false;

      for(class_1297 ent : mc.field_1687.method_18112()) {
         if (this.shouldRender(ent)) {
            if (bufferBuilder == null) {
               bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
            }

            if (this.drawBox(bufferBuilder, ent, matrix, context)) {
               hasVertices = true;
            }
         }
      }

      if (hasVertices && bufferBuilder != null) {
         class_286.method_43433(bufferBuilder.method_60800());
      }

      Render2DUtil.endRender();

      for(class_1297 ent : mc.field_1687.method_18112()) {
         if (this.shouldRender(ent)) {
            this.drawText(ent, context);
         }
      }

   }

   public boolean shouldRender(class_1297 entity) {
      if (entity == null) {
         return false;
      } else if (mc.field_1724 == null) {
         return false;
      } else if (entity instanceof class_1657) {
         if (entity == mc.field_1724 && mc.field_1690.method_31044().method_31034()) {
            return false;
         } else {
            return Alien.FRIEND.isFriend((class_1657)entity) ? this.friends.getValue() : this.players.getValue();
         }
      } else if (entity instanceof class_1511) {
         return this.crystals.getValue();
      } else {
         boolean var10000;
         switch (entity.method_5864().method_5891()) {
            case field_6294:
            case field_6300:
               var10000 = this.creatures.getValue();
               break;
            case field_6302:
               var10000 = this.monsters.getValue();
               break;
            case field_6303:
            case field_24460:
               var10000 = this.ambients.getValue();
               break;
            default:
               var10000 = this.others.getValue();
         }

         return var10000;
      }
   }

   public Color getEntityColor(class_1297 entity) {
      if (entity == null) {
         return new Color(-1);
      } else if (entity instanceof class_1657) {
         return Alien.FRIEND.isFriend((class_1657)entity) ? this.friendsC.getValue() : this.playersC.getValue();
      } else if (entity instanceof class_1511) {
         return this.crystalsC.getValue();
      } else {
         Color var10000;
         switch (entity.method_5864().method_5891()) {
            case field_6294:
            case field_6300:
               var10000 = this.creaturesC.getValue();
               break;
            case field_6302:
               var10000 = this.monstersC.getValue();
               break;
            case field_6303:
            case field_24460:
               var10000 = this.ambientsC.getValue();
               break;
            default:
               var10000 = this.othersC.getValue();
         }

         return var10000;
      }
   }

   public boolean drawBox(class_287 bufferBuilder, @NotNull class_1297 ent, Matrix4f matrix, class_332 context) {
      double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)mc.method_60646().method_60637(true);
      double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)mc.method_60646().method_60637(true);
      double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)mc.method_60646().method_60637(true);
      class_238 axisAlignedBB2 = ent.method_5829();
      class_238 axisAlignedBB = new class_238(axisAlignedBB2.field_1323 - ent.method_23317() + x - 0.05, axisAlignedBB2.field_1322 - ent.method_23318() + y, axisAlignedBB2.field_1321 - ent.method_23321() + z - 0.05, axisAlignedBB2.field_1320 - ent.method_23317() + x + 0.05, axisAlignedBB2.field_1325 - ent.method_23318() + y + 0.15, axisAlignedBB2.field_1324 - ent.method_23321() + z + 0.05);
      class_243[] vectors = new class_243[]{new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
      Color col = this.getEntityColor(ent);
      Vector4d position = null;

      for(class_243 vector : vectors) {
         vector = TextUtil.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
         if (vector.field_1350 > (double)0.0F && vector.field_1350 < (double)1.0F) {
            if (position == null) {
               position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, (double)0.0F);
            }

            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
            position.w = Math.max(vector.field_1351, position.w);
         }
      }

      if (position == null) {
         return false;
      } else {
         double posX = position.x;
         double posY = position.y;
         double endPosX = position.z;
         double endPosY = position.w;
         if (this.outline.getValue()) {
            this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - (double)1.0F), (float)posY, 1.5F, (float)(endPosY - posY + (double)0.5F), Color.BLACK);
            this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - (double)1.0F), (float)(posY - (double)0.5F), (float)(endPosX - posX + (double)1.5F), 1.5F, Color.BLACK);
            this.drawRectToBuffer(bufferBuilder, matrix, (float)(endPosX - (double)1.0F), (float)posY, 1.5F, (float)(endPosY - posY + (double)0.5F), Color.BLACK);
            this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - (double)1.0F), (float)(endPosY - (double)1.0F), (float)(endPosX - posX + (double)1.5F), 1.5F, Color.BLACK);
            this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - (double)0.5F), (float)posY, 0.5F, (float)(endPosY - posY), col);
            this.drawRectToBuffer(bufferBuilder, matrix, (float)posX, (float)(endPosY - (double)0.5F), (float)(endPosX - posX), 0.5F, col);
            this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - (double)0.5F), (float)posY, (float)(endPosX - posX + (double)0.5F), 0.5F, col);
            this.drawRectToBuffer(bufferBuilder, matrix, (float)(endPosX - (double)0.5F), (float)posY, 0.5F, (float)(endPosY - posY), col);
         }

         class_1309 lent;
         if (ent instanceof class_1309 && (lent = (class_1309)ent).method_6032() != 0.0F && this.renderHealth.getValue()) {
            this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - (double)4.0F), (float)posY, 1.0F, (float)(endPosY - posY), Color.BLACK);
            Color color = this.getcolor(lent.method_6032());
            float healthHeight = (float)(endPosY - (endPosY + (posY - endPosY) * (double)lent.method_6032() / (double)lent.method_6063()));
            this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - (double)4.0F), (float)(endPosY - (double)healthHeight), 1.0F, healthHeight, color);
         }

         if (ent instanceof class_1657) {
            class_1657 player = (class_1657)ent;
            if (this.renderArmor.getValue()) {
               double height = (endPosY - posY) / (double)4.0F;
               ArrayList<class_1799> stacks = new ArrayList();
               stacks.add((class_1799)player.method_31548().field_7548.get(3));
               stacks.add((class_1799)player.method_31548().field_7548.get(2));
               stacks.add((class_1799)player.method_31548().field_7548.get(1));
               stacks.add((class_1799)player.method_31548().field_7548.get(0));
               int i = -1;

               for(class_1799 armor : stacks) {
                  ++i;
                  if (!armor.method_7960()) {
                     float durability = (float)(armor.method_7936() - armor.method_7919());
                     int percent = (int)(durability / (float)armor.method_7936() * 100.0F);
                     double finalH = height * (double)(percent / 100);
                     this.drawRectToBuffer(bufferBuilder, matrix, (float)(endPosX + (double)1.5F), (float)((double)((float)posY) + height * (double)i + 1.2 * (double)(i + 1)), 1.5F, (float)finalH, this.armorDuraColor.getValue());
                  }
               }
            }
         }

         return true;
      }
   }

   private void drawRectToBuffer(class_287 bufferBuilder, Matrix4f matrix, float x, float y, float width, float height, Color color) {
      float a = (float)color.getAlpha() / 255.0F;
      float r = (float)color.getRed() / 255.0F;
      float g = (float)color.getGreen() / 255.0F;
      float b = (float)color.getBlue() / 255.0F;
      bufferBuilder.method_22918(matrix, x, y, 0.0F).method_22915(r, g, b, a);
      bufferBuilder.method_22918(matrix, x, y + height, 0.0F).method_22915(r, g, b, a);
      bufferBuilder.method_22918(matrix, x + width, y + height, 0.0F).method_22915(r, g, b, a);
      bufferBuilder.method_22918(matrix, x + width, y, 0.0F).method_22915(r, g, b, a);
   }

   public void drawText(class_1297 ent, class_332 context) {
      double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)mc.method_60646().method_60637(true);
      double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)mc.method_60646().method_60637(true);
      double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)mc.method_60646().method_60637(true);
      class_238 axisAlignedBB2 = ent.method_5829();
      class_238 axisAlignedBB = new class_238(axisAlignedBB2.field_1323 - ent.method_23317() + x - 0.05, axisAlignedBB2.field_1322 - ent.method_23318() + y, axisAlignedBB2.field_1321 - ent.method_23321() + z - 0.05, axisAlignedBB2.field_1320 - ent.method_23317() + x + 0.05, axisAlignedBB2.field_1325 - ent.method_23318() + y + 0.15, axisAlignedBB2.field_1324 - ent.method_23321() + z + 0.05);
      class_243[] vectors = new class_243[]{new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
      this.getEntityColor(ent);
      Vector4d position = null;

      for(class_243 vector : vectors) {
         vector = TextUtil.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
         if (vector.field_1350 > (double)0.0F && vector.field_1350 < (double)1.0F) {
            if (position == null) {
               position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, (double)0.0F);
            }

            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
            position.w = Math.max(vector.field_1351, position.w);
         }
      }

      if (position != null) {
         double posX = position.x;
         double posY = position.y;
         double endPosX = position.z;
         double endPosY = position.w;
         if (ent instanceof class_1542) {
            class_1542 entity = (class_1542)ent;
            if (this.drawItem.getValue()) {
               float diff = (float)((endPosX - posX) / (double)2.0F);
               float textWidth = FontManager.ui.getWidth(entity.method_5476().getString());
               float tagX = (float)(posX + (double)diff - (double)(textWidth / 2.0F));
               int count = entity.method_6983().method_7947();
               context.method_51433(mc.field_1772, entity.method_5476().getString(), (int)tagX, (int)(posY - (double)10.0F), this.textcolor.getValue().getRGB(), false);
               if (this.drawItemC.getValue()) {
                  context.method_51433(mc.field_1772, "x" + count, (int)(tagX + (float)mc.field_1772.method_1727(entity.method_5476().getString() + " ")), (int)posY - 10, this.countColor.getValue().getRGB(), false);
               }
            }
         }

         if (ent instanceof class_1657) {
            class_1657 player = (class_1657)ent;
            if (this.renderArmor.getValue()) {
               double height = (endPosY - posY) / (double)4.0F;
               ArrayList<class_1799> stacks = new ArrayList();
               stacks.add((class_1799)player.method_31548().field_7548.get(3));
               stacks.add((class_1799)player.method_31548().field_7548.get(2));
               stacks.add((class_1799)player.method_31548().field_7548.get(1));
               stacks.add((class_1799)player.method_31548().field_7548.get(0));
               int i = -1;

               for(class_1799 armor : stacks) {
                  ++i;
                  if (!armor.method_7960()) {
                     float durability = (float)(armor.method_7936() - armor.method_7919());
                     int percent = (int)(durability / (float)armor.method_7936() * 100.0F);
                     double finalH = height * ((double)percent / (double)100.0F);
                     context.method_51427(armor, (int)(endPosX + (double)4.0F), (int)(posY + height * (double)i + 1.2 * (double)(i + 1) + finalH / (double)2.0F));
                  }
               }
            }
         }
      }

   }

   public Color getcolor(float health) {
      if (health >= 20.0F) {
         return this.hHealth.getValue();
      } else {
         return 20.0F > health && health > 10.0F ? this.mHealth.getValue() : this.lHealth.getValue();
      }
   }

   public static enum TwoDESPMode {
      Setting,
      Target,
      Color;

      // $FF: synthetic method
      private static TwoDESPMode[] $values() {
         return new TwoDESPMode[]{Setting, Target, Color};
      }
   }
}
