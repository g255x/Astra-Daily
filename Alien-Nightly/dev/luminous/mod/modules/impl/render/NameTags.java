package dev.luminous.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render3DEvent;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.TextRadar;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1087;
import net.minecraft.class_124;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_243;
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
import net.minecraft.class_327.class_6415;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class NameTags extends Module {
   public static NameTags INSTANCE;
   final ColorSetting colorConfig = this.add(new ColorSetting("Color", new Color(255, 255, 255)));
   final ColorSetting friendConfig = this.add((new ColorSetting("Friend", new Color(155, 155, 255))).injectBoolean(true));
   final ColorSetting invisibleConfig = this.add((new ColorSetting("Invisible", new Color(200, 200, 200))).injectBoolean(true));
   final ColorSetting died = this.add((new ColorSetting("Died", new Color(180, 0, 0))).injectBoolean(true));
   final ColorSetting sneakingConfig = this.add((new ColorSetting("Sneaking", new Color(200, 200, 0))).injectBoolean(true));
   final ColorSetting rectConfig = this.add((new ColorSetting("Rectangle", new Color(0, 0, 0, 100))).injectBoolean(true));
   final BooleanSetting armorConfig = this.add((new BooleanSetting("Armor", true)).setParent());
   final BooleanSetting drawItemConfig;
   final SliderSetting offsetConfig;
   final BooleanSetting enchantmentsConfig;
   final BooleanSetting durabilityConfig;
   final BooleanSetting forceBarConfig;
   final BooleanSetting itemNameConfig;
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
   final DecimalFormat df;

   public NameTags() {
      super("NameTags", "Renders info on player NameTags", Module.Category.Render);
      BooleanSetting var10006 = this.armorConfig;
      Objects.requireNonNull(var10006);
      this.drawItemConfig = this.add(new BooleanSetting("DrawItem", true, var10006::isOpen));
      BooleanSetting var10009 = this.armorConfig;
      Objects.requireNonNull(var10009);
      this.offsetConfig = this.add(new SliderSetting("Offset", (double)-20.0F, (double)-30.0F, (double)10.0F, 0.01, var10009::isOpen));
      this.enchantmentsConfig = this.add(new BooleanSetting("Enchantments", true));
      this.durabilityConfig = this.add((new BooleanSetting("Durability", true)).setParent());
      var10006 = this.durabilityConfig;
      Objects.requireNonNull(var10006);
      this.forceBarConfig = this.add(new BooleanSetting("ForceBar", true, var10006::isOpen));
      this.itemNameConfig = this.add(new BooleanSetting("ItemName", false));
      this.entityIdConfig = this.add(new BooleanSetting("EntityId", false));
      this.gamemodeConfig = this.add(new BooleanSetting("Gamemode", false));
      this.pingConfig = this.add(new BooleanSetting("Ping", true));
      this.healthConfig = this.add(new BooleanSetting("Health", true));
      this.totemsConfig = this.add(new BooleanSetting("Totems", false));
      this.scaleConfig = this.add(new SliderSetting("Scale", (double)1.0F, (double)0.0F, (double)3.0F, 0.1));
      this.factorConfig = this.add((new BooleanSetting("Factor", true)).setParent());
      var10009 = this.factorConfig;
      Objects.requireNonNull(var10009);
      this.scalingConfig = this.add(new SliderSetting("Scaling", (double)1.0F, (double)0.0F, (double)3.0F, 0.1, var10009::isOpen));
      var10009 = this.factorConfig;
      Objects.requireNonNull(var10009);
      this.distanceConfig = this.add(new SliderSetting("Distance", (double)6.0F, (double)0.0F, (double)20.0F, 0.1, var10009::isOpen));
      this.heightConfig = this.add(new SliderSetting("Height", (double)0.0F, (double)-3.0F, (double)3.0F, 0.01));
      this.df = new DecimalFormat("0.0");
      this.setChinese("名字标签");
      INSTANCE = this;
   }

   @EventListener
   public void onRender3D(Render3DEvent event) {
      if (mc.field_1773 != null && mc.method_1560() != null) {
         class_4184 camera = mc.field_1773.method_19418();
         RenderSystem.enableBlend();
         GL11.glDepthFunc(519);
         class_4587 matrixStack = new class_4587();

         for(class_1657 player : Alien.THREAD.getPlayers()) {
            if ((this.died.booleanValue || player.method_5805()) && (player != mc.field_1724 || !mc.field_1690.method_31044().method_31034()) && (this.invisibleConfig.booleanValue || !player.method_5767())) {
               String info = this.getNametagInfo(player);
               class_243 renderPosition = MathUtil.getRenderPosition(player, event.tickDelta);
               double x = renderPosition.method_10216();
               double y = renderPosition.method_10214();
               double z = renderPosition.method_10215();
               int width = mc.field_1772.method_1727(info);
               float hwidth = (float)width / 2.0F;
               this.renderInfo(info, hwidth, player, x, y, z, camera, matrixStack);
            }
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
      if (this.rectConfig.booleanValue) {
         Render2DUtil.drawRect(matrices, -width - 2.0F, -1.0F, width * 2.0F + 3.0F, 10.0F, this.rectConfig.getValue());
      }

      this.drawWithShadow(matrices, info, -width, 0.0F, this.getNametagColor(entity));
      if (this.armorConfig.getValue()) {
         this.renderItems(matrices, entity);
      }

      matrices.method_22909();
   }

   private void drawWithShadow(class_4587 matrices, String info, float x, float y, int color) {
      class_4597.class_4598 immediate = mc.method_22940().method_23000();
      mc.field_1772.method_1724(info, x, y, color, true, matrices.method_23760().method_23761(), immediate, class_6415.field_33994, 0, 15728880);
      immediate.method_22993();
      mc.field_1772.method_27521(info, x, y, color, false, matrices.method_23760().method_23761(), immediate, class_6415.field_33994, 0, 15728880);
      immediate.method_22993();
   }

   private void renderItems(class_4587 matrixStack, class_1657 player) {
      List<class_1799> displayItems = new CopyOnWriteArrayList();
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

      for(class_1799 stack : displayItems) {
         x -= 8.0F;
         if (stack.method_58657().method_57541() > n11) {
            n11 = stack.method_58657().method_57541();
         }
      }

      float y = this.offsetConfig.getValueFloat();

      for(class_1799 stackx : displayItems) {
         GL11.glDepthFunc(519);
         if (this.drawItemConfig.getValue()) {
            this.renderItemStack(matrixStack, stackx, x, y + 1.0F);
         }

         this.renderItemOverlay(matrixStack, stackx, x, y + 2.5F);
         matrixStack.method_22905(0.5F, 0.5F, 0.5F);
         if (this.durabilityConfig.getValue()) {
            this.renderDurability(matrixStack, stackx, x + 2.0F, y + 11.5F);
         }

         if (this.enchantmentsConfig.getValue()) {
            this.renderEnchants(matrixStack, stackx, x + 2.0F, y + 7.0F);
         }

         matrixStack.method_22905(2.0F, 2.0F, 2.0F);
         x += 16.0F;
         GL11.glDepthFunc(515);
      }

      class_1799 heldItem = player.method_6047();
      if (!heldItem.method_7960()) {
         matrixStack.method_22905(0.5F, 0.5F, 0.5F);
         if (this.itemNameConfig.getValue()) {
            this.renderItemName(matrixStack, heldItem, y - 4.5F + this.enchantOffset(n11));
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
      if (this.enchantmentsConfig.getValue() && n > 2) {
         float value = -2.0F;
         return value - (float)(n - 3) * 4.5F;
      } else {
         return 0.0F;
      }
   }

   private void renderItemName(class_4587 matrixStack, class_1799 itemStack, float y) {
      String itemName = itemStack.method_7964().getString();
      float width = (float)mc.field_1772.method_1727(itemName) / 4.0F;
      this.drawWithShadow(matrixStack, itemName, (0.0F - width) * 2.0F, y * 2.0F, -1);
   }

   private String getNametagInfo(class_1657 player) {
      StringBuilder info = new StringBuilder();
      if (this.gamemodeConfig.getValue()) {
         if (player.method_7337()) {
            info.append(class_124.field_1065);
            info.append("[C] ");
         } else if (player.method_7325()) {
            info.append(class_124.field_1080);
            info.append("[I] ");
         } else {
            info.append(class_124.field_1067);
            info.append("[S] ");
         }
      }

      if (this.pingConfig.getValue()) {
         info.append(this.getEntityPing(player));
         info.append("ms ");
         info.append(class_124.field_1070);
      }

      info.append(player.method_5477().getString());
      info.append(" ");
      if (this.entityIdConfig.getValue()) {
         info.append("ID: ");
         info.append(player.method_5628());
         info.append(" ");
      }

      if (this.healthConfig.getValue()) {
         double health = (double)(player.method_6032() + player.method_6067());
         class_124 hcolor;
         if (health > (double)18.0F) {
            hcolor = class_124.field_1060;
         } else if (health > (double)16.0F) {
            hcolor = class_124.field_1077;
         } else if (health > (double)12.0F) {
            hcolor = class_124.field_1054;
         } else if (health > (double)8.0F) {
            hcolor = class_124.field_1065;
         } else if (health > (double)4.0F) {
            hcolor = class_124.field_1061;
         } else {
            hcolor = class_124.field_1079;
         }

         String phealth = this.df.format(health);
         info.append(hcolor);
         info.append(phealth);
         info.append(" ");
      }

      if (this.totemsConfig.getValue()) {
         int totems = Alien.POP.getPop(player);
         if (totems > 0) {
            class_124 c = TextRadar.getPopColor(totems);
            info.append(c);
            info.append(-totems);
            info.append(" ");
         }
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
            class_124 color;
            if (ping >= 200) {
               color = class_124.field_1061;
            } else if (ping >= 100) {
               color = class_124.field_1054;
            } else {
               color = class_124.field_1060;
            }

            String var10000 = color.toString();
            return var10000 + ping;
         }
      }
   }

   private int getNametagColor(class_1657 player) {
      if (this.friendConfig.booleanValue && player.method_5476() != null && Alien.FRIEND.isFriend(player)) {
         return this.friendConfig.getValue().getRGB();
      } else if (this.invisibleConfig.booleanValue && player.method_5767()) {
         return this.invisibleConfig.getValue().getRGB();
      } else if (this.sneakingConfig.booleanValue && player.method_5715()) {
         return this.sneakingConfig.getValue().getRGB();
      } else {
         return !player.method_5805() ? this.died.getValue().getRGB() : this.colorConfig.getValue().getRGB();
      }
   }
}
