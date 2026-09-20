package dev.Astra.mod.modules.impl.render;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.EntitySpawnedEvent;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.asm.accessors.IEntity;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1684;
import net.minecraft.class_1799;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2611;
import net.minecraft.class_2627;
import net.minecraft.class_2640;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public class ESP extends Module {
   public static ESP INSTANCE;
   public final EnumSetting<RenderMode> mode;
   private final BooleanSetting twoDOutline;
   private final BooleanSetting twoDHealth;
   private final ColorSetting hHealth;
   private final ColorSetting mHealth;
   private final ColorSetting lHealth;
   private final BooleanSetting twoDArmorDura;
   private final ColorSetting armorDuraColor;
   private final SliderSetting duraScale;
   public final BooleanSetting target;
   public final BooleanSetting self;
   private final ColorSetting endPortalFill;
   private final ColorSetting endPortalOutline;
   private final ColorSetting itemFill;
   private final ColorSetting itemOutline;
   private final ColorSetting playerFill;
   private final ColorSetting playerOutline;
   private final ColorSetting chestFill;
   private final ColorSetting chestOutline;
   private final ColorSetting enderChestFill;
   private final ColorSetting enderChestOutline;
   private final ColorSetting shulkerBoxFill;
   private final ColorSetting shulkerBoxOutline;
   public final BooleanSetting item;
   public final BooleanSetting customName;
   public final BooleanSetting count;
   private final ColorSetting text;
   public final BooleanSetting pearl;

   public ESP() {
      super("ESP", Module.Category.Render);
      this.mode = this.add(new EnumSetting("Mode", ESP.RenderMode.ThreeD));
      this.twoDOutline = this.add(new BooleanSetting("2DOutline", true, () -> this.mode.getValue() == ESP.RenderMode.TwoD));
      this.twoDHealth = this.add(new BooleanSetting("2DHealth", true, () -> this.mode.getValue() == ESP.RenderMode.TwoD));
      this.hHealth = this.add(new ColorSetting("HighHealth", new Color(-16711936, true), () -> this.mode.getValue() == ESP.RenderMode.TwoD));
      this.mHealth = this.add(new ColorSetting("MidHealth", new Color(-256, true), () -> this.mode.getValue() == ESP.RenderMode.TwoD));
      this.lHealth = this.add(new ColorSetting("LowHealth", new Color(-65536, true), () -> this.mode.getValue() == ESP.RenderMode.TwoD));
      this.twoDArmorDura = this.add(new BooleanSetting("2DArmorDura", true, () -> this.mode.getValue() == ESP.RenderMode.TwoD));
      this.armorDuraColor = this.add(new ColorSetting("ArmorDuraColor", new Color(-13631744, true), () -> this.mode.getValue() == ESP.RenderMode.TwoD && this.twoDArmorDura.getValue()));
      this.duraScale = this.add(new SliderSetting("DuraScale", (double)1.0F, (double)0.0F, (double)2.0F, 0.1, () -> this.mode.getValue() == ESP.RenderMode.TwoD && this.twoDArmorDura.getValue()));
      this.target = this.add((new BooleanSetting("Target", true)).setParent());
      BooleanSetting var10006 = this.target;
      Objects.requireNonNull(var10006);
      this.self = this.add(new BooleanSetting("Self", false, var10006::isOpen));
      this.endPortalFill = this.add((new ColorSetting("EndPortalFill", new Color(-1858405124, true), () -> this.mode.getValue() != ESP.RenderMode.TwoD && this.target.isOpen())).injectBoolean(false));
      this.endPortalOutline = this.add((new ColorSetting("EndPortalOutline", new Color(1694495617, true), () -> this.mode.getValue() != ESP.RenderMode.TwoD && this.target.isOpen())).injectBoolean(false));
      Color var10005 = new Color(931429313, true);
      var10006 = this.target;
      Objects.requireNonNull(var10006);
      this.itemFill = this.add((new ColorSetting("ItemFill", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(-8094783, true);
      var10006 = this.target;
      Objects.requireNonNull(var10006);
      this.itemOutline = this.add((new ColorSetting("ItemOutline", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(1694498815, true);
      var10006 = this.target;
      Objects.requireNonNull(var10006);
      this.playerFill = this.add((new ColorSetting("PlayerFill", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(1694495617, true);
      var10006 = this.target;
      Objects.requireNonNull(var10006);
      this.playerOutline = this.add((new ColorSetting("PlayerOutline", var10005, var10006::isOpen)).injectBoolean(true));
      this.chestFill = this.add((new ColorSetting("ChestFill", new Color(1862060803, true), () -> this.mode.getValue() != ESP.RenderMode.TwoD && this.target.isOpen())).injectBoolean(false));
      this.chestOutline = this.add((new ColorSetting("ChestOutline", new Color(1694495617, true), () -> this.mode.getValue() != ESP.RenderMode.TwoD && this.target.isOpen())).injectBoolean(false));
      this.enderChestFill = this.add((new ColorSetting("EnderChestFill", new Color(965346303, true), () -> this.mode.getValue() != ESP.RenderMode.TwoD && this.target.isOpen())).injectBoolean(false));
      this.enderChestOutline = this.add((new ColorSetting("EnderChestOutline", new Color(1694495617, true), () -> this.mode.getValue() != ESP.RenderMode.TwoD && this.target.isOpen())).injectBoolean(false));
      this.shulkerBoxFill = this.add((new ColorSetting("ShulkerBoxFill", new Color(-2080414740, true), () -> this.mode.getValue() != ESP.RenderMode.TwoD && this.target.isOpen())).injectBoolean(false));
      this.shulkerBoxOutline = this.add((new ColorSetting("ShulkerBoxOutline", new Color(1694495617, true), () -> this.mode.getValue() != ESP.RenderMode.TwoD && this.target.isOpen())).injectBoolean(false));
      this.item = this.add((new BooleanSetting("ItemName", false)).setParent());
      var10006 = this.item;
      Objects.requireNonNull(var10006);
      this.customName = this.add(new BooleanSetting("CustomName", false, var10006::isOpen));
      var10006 = this.item;
      Objects.requireNonNull(var10006);
      this.count = this.add(new BooleanSetting("Count", true, var10006::isOpen));
      var10005 = new Color(-1, true);
      var10006 = this.item;
      Objects.requireNonNull(var10006);
      this.text = this.add(new ColorSetting("Text", var10005, var10006::isOpen));
      this.pearl = this.add(new BooleanSetting("PearlOwner", true));
      this.setChinese("透视");
      INSTANCE = this;
   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.mode.getValue() != ESP.RenderMode.TwoD) {
         if (this.item.getValue()) {
            for(class_1297 entity : Astra.THREAD.getEntities()) {
               if (entity instanceof class_1542) {
                  class_1542 itemEntity = (class_1542)entity;
                  int itemCount = itemEntity.method_6983().method_7947();
                  String s = this.count.getValue() && itemCount > 1 ? " x" + itemCount : "";
                  String name = (this.customName.getValue() ? itemEntity.method_6983().method_7964() : itemEntity.method_6983().method_7909().method_7848()).getString();
                  Render3DUtil.drawText3D(name + s, ((IEntity)itemEntity).getDimensions().method_30757(new class_243(MathUtil.interpolate(itemEntity.field_6038, itemEntity.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(itemEntity.field_5971, itemEntity.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(itemEntity.field_5989, itemEntity.method_23321(), (double)mc.method_60646().method_60637(true)))).method_1009((double)0.0F, 0.1, (double)0.0F).method_1005().method_1031((double)0.0F, (double)0.5F, (double)0.0F), this.text.getValue());
               }
            }
         }

         if (this.target.getValue()) {
            if (this.itemFill.booleanValue || this.playerFill.booleanValue) {
               for(class_1297 entity : Astra.THREAD.getEntities()) {
                  if (!(entity instanceof class_1542) || !this.itemFill.booleanValue && !this.itemOutline.booleanValue) {
                     if ((entity != mc.field_1724 || this.self.getValue() && !mc.field_1690.method_31044().method_31034()) && entity instanceof class_1657 && (this.playerFill.booleanValue || this.playerOutline.booleanValue)) {
                        Color color = this.playerFill.getValue();
                        Render3DUtil.draw3DBox(matrixStack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)mc.method_60646().method_60637(true)))).method_1009((double)0.0F, 0.1, (double)0.0F), color, this.playerOutline.getValue(), this.playerOutline.booleanValue, this.playerFill.booleanValue);
                     }
                  } else {
                     Color color = this.itemFill.getValue();
                     Render3DUtil.draw3DBox(matrixStack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)mc.method_60646().method_60637(true)))), color, this.itemOutline.getValue(), this.itemOutline.booleanValue, this.itemFill.booleanValue);
                  }
               }
            }

            for(class_2586 blockEntity : BlockUtil.getTileEntities()) {
               if (!(blockEntity instanceof class_2595) || !this.chestFill.booleanValue && !this.chestOutline.booleanValue) {
                  if (!(blockEntity instanceof class_2611) || !this.enderChestFill.booleanValue && !this.enderChestOutline.booleanValue) {
                     if (!(blockEntity instanceof class_2627) || !this.shulkerBoxFill.booleanValue && !this.shulkerBoxOutline.booleanValue) {
                        if (blockEntity instanceof class_2640 && (this.endPortalFill.booleanValue || this.endPortalOutline.booleanValue)) {
                           class_238 box = new class_238(blockEntity.method_11016());
                           Render3DUtil.draw3DBox(matrixStack, box, this.endPortalFill.getValue(), this.endPortalOutline.getValue(), this.endPortalOutline.booleanValue, this.endPortalFill.booleanValue);
                        }
                     } else {
                        class_238 box = new class_238(blockEntity.method_11016());
                        Render3DUtil.draw3DBox(matrixStack, box, this.shulkerBoxFill.getValue(), this.shulkerBoxOutline.getValue(), this.shulkerBoxOutline.booleanValue, this.shulkerBoxFill.booleanValue);
                     }
                  } else {
                     class_238 box = new class_238(blockEntity.method_11016());
                     Render3DUtil.draw3DBox(matrixStack, box, this.enderChestFill.getValue(), this.enderChestOutline.getValue(), this.enderChestOutline.booleanValue, this.enderChestFill.booleanValue);
                  }
               } else {
                  class_238 box = new class_238(blockEntity.method_11016());
                  Render3DUtil.draw3DBox(matrixStack, box, this.chestFill.getValue(), this.chestOutline.getValue(), this.chestOutline.booleanValue, this.chestFill.booleanValue);
               }
            }
         }

      }
   }

   public void onRender2D(class_332 drawContext, float tickDelta) {
      if (this.mode.getValue() == ESP.RenderMode.TwoD) {
         if (!nullCheck()) {
            if (this.target.getValue()) {
               for(class_1297 entity : Astra.THREAD.getEntities()) {
                  if (entity instanceof class_1542) {
                     boolean outline = this.itemOutline.booleanValue;
                     boolean fill = this.itemFill.booleanValue;
                     if (outline || fill) {
                        this.draw2DForEntity(drawContext.method_51448(), entity, this.itemOutline.getValue(), this.itemFill.getValue(), outline, fill, tickDelta, false);
                     }
                  } else if ((entity != mc.field_1724 || this.self.getValue() && !mc.field_1690.method_31044().method_31034()) && entity instanceof class_1657) {
                     boolean outline = this.playerOutline.booleanValue;
                     boolean fill = this.playerFill.booleanValue;
                     if (outline || fill) {
                        this.draw2DForEntity(drawContext.method_51448(), entity, this.playerOutline.getValue(), this.playerFill.getValue(), outline, fill, tickDelta, true);
                     }
                  }
               }
            }

            if (this.item.getValue()) {
               for(class_1297 entity : Astra.THREAD.getEntities()) {
                  if (entity instanceof class_1542) {
                     this.draw2DItemText(drawContext, (class_1542)entity, tickDelta);
                  }
               }
            }

         }
      }
   }

   private void draw2DForEntity(class_4587 matrices, class_1297 entity, Color outlineColor, Color fillColor, boolean outline, boolean fill, float tickDelta, boolean isPlayer) {
      double[] box = this.getEntity2DBox(entity, tickDelta);
      if (box != null) {
         float minX = (float)box[0];
         float minY = (float)box[1];
         float maxX = (float)box[2];
         float maxY = (float)box[3];
         if (!(maxX - minX <= 0.0F) && !(maxY - minY <= 0.0F)) {
            if (fill) {
               Render2DUtil.drawRect(matrices, minX, minY, maxX - minX, maxY - minY, fillColor);
            }

            if (outline) {
               this.draw2DBox(matrices, minX, minY, maxX, maxY, outlineColor);
            }

            if (this.twoDHealth.getValue() && entity instanceof class_1309) {
               class_1309 living = (class_1309)entity;
               if (living.method_6063() > 0.0F && living.method_6032() > 0.0F) {
                  this.draw2DHealthBar(matrices, minX, minY, maxY, living);
               }
            }

            if (isPlayer && this.twoDArmorDura.getValue() && entity instanceof class_1657) {
               this.draw2DArmorDura(matrices, maxX, minY, maxY, (class_1657)entity);
            }

         }
      }
   }

   private void draw2DBox(class_4587 matrices, float posX, float posY, float endPosX, float endPosY, Color col) {
      if (this.twoDOutline.getValue()) {
         Render2DUtil.drawRect(matrices, posX - 1.0F, posY, 1.5F, endPosY - posY + 0.5F, Color.BLACK);
         Render2DUtil.drawRect(matrices, posX - 1.0F, posY - 0.5F, endPosX - posX + 1.5F, 1.5F, Color.BLACK);
         Render2DUtil.drawRect(matrices, endPosX - 1.0F, posY, 1.5F, endPosY - posY + 0.5F, Color.BLACK);
         Render2DUtil.drawRect(matrices, posX - 1.0F, endPosY - 1.0F, endPosX - posX + 1.5F, 1.5F, Color.BLACK);
      }

      Render2DUtil.drawRect(matrices, posX - 0.5F, posY, 0.5F, endPosY - posY, col);
      Render2DUtil.drawRect(matrices, posX, endPosY - 0.5F, endPosX - posX, 0.5F, col);
      Render2DUtil.drawRect(matrices, posX - 0.5F, posY, endPosX - posX + 0.5F, 0.5F, col);
      Render2DUtil.drawRect(matrices, endPosX - 0.5F, posY, 0.5F, endPosY - posY, col);
   }

   private void draw2DHealthBar(class_4587 matrices, float minX, float minY, float maxY, class_1309 living) {
      float height = maxY - minY;
      float health = living.method_6032();
      float maxHealth = living.method_6063();
      float ratio = Math.max(0.0F, Math.min(1.0F, health / maxHealth));
      Render2DUtil.drawRect(matrices, minX - 4.0F, minY, 1.0F, height, Color.BLACK);
      Render2DUtil.drawRect(matrices, minX - 4.0F, maxY - height * ratio, 1.0F, height * ratio, this.getHealthColor(health));
   }

   private Color getHealthColor(float health) {
      if (health >= 20.0F) {
         return this.hHealth.getValue();
      } else {
         return health > 10.0F ? this.mHealth.getValue() : this.lHealth.getValue();
      }
   }

   private void draw2DArmorDura(class_4587 matrices, float maxX, float minY, float maxY, class_1657 player) {
      float height = maxY - minY;
      float piece = height / 4.0F;
      ArrayList<class_1799> stacks = new ArrayList();
      stacks.add((class_1799)player.method_31548().field_7548.get(3));
      stacks.add((class_1799)player.method_31548().field_7548.get(2));
      stacks.add((class_1799)player.method_31548().field_7548.get(1));
      stacks.add((class_1799)player.method_31548().field_7548.get(0));

      for(int i = 0; i < stacks.size(); ++i) {
         class_1799 armor = (class_1799)stacks.get(i);
         if (!armor.method_7960() && armor.method_7936() > 0) {
            float ratio = (float)(armor.method_7936() - armor.method_7919()) / (float)armor.method_7936();
            ratio = Math.max(0.0F, Math.min(1.0F, ratio));
            float segmentTop = minY + piece * (float)i;
            float segmentBottom = segmentTop + piece;
            float filled = piece * ratio * this.duraScale.getValueFloat();
            filled = Math.min(piece, filled);
            Render2DUtil.drawRect(matrices, maxX + 1.5F, segmentBottom - filled, 1.5F, filled, this.armorDuraColor.getValue());
         }
      }

   }

   private double[] getEntity2DBox(class_1297 ent, float tickDelta) {
      double x = MathUtil.interpolate(ent.field_6038, ent.method_23317(), (double)tickDelta);
      double y = MathUtil.interpolate(ent.field_5971, ent.method_23318(), (double)tickDelta);
      double z = MathUtil.interpolate(ent.field_5989, ent.method_23321(), (double)tickDelta);
      class_238 axisAlignedBB2 = ent.method_5829();
      class_238 axisAlignedBB = new class_238(axisAlignedBB2.field_1323 - ent.method_23317() + x - 0.05, axisAlignedBB2.field_1322 - ent.method_23318() + y, axisAlignedBB2.field_1321 - ent.method_23321() + z - 0.05, axisAlignedBB2.field_1320 - ent.method_23317() + x + 0.05, axisAlignedBB2.field_1325 - ent.method_23318() + y + 0.15, axisAlignedBB2.field_1324 - ent.method_23321() + z + 0.05);
      class_243[] vectors = new class_243[]{new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
      double minX = Double.POSITIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY;
      double maxX = Double.NEGATIVE_INFINITY;
      double maxY = Double.NEGATIVE_INFINITY;
      boolean found = false;

      for(class_243 vector : vectors) {
         class_243 screen = TextUtil.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
         if (screen.field_1350 > (double)0.0F && screen.field_1350 < (double)1.0F) {
            found = true;
            minX = Math.min(screen.field_1352, minX);
            minY = Math.min(screen.field_1351, minY);
            maxX = Math.max(screen.field_1352, maxX);
            maxY = Math.max(screen.field_1351, maxY);
         }
      }

      if (!found) {
         return null;
      } else {
         return new double[]{minX, minY, maxX, maxY};
      }
   }

   private void draw2DItemText(class_332 drawContext, class_1542 itemEntity, float tickDelta) {
      double[] box = this.getEntity2DBox(itemEntity, tickDelta);
      if (box != null) {
         int itemCount = itemEntity.method_6983().method_7947();
         String s = this.count.getValue() && itemCount > 1 ? " x" + itemCount : "";
         String name = (this.customName.getValue() ? itemEntity.method_6983().method_7964() : itemEntity.method_6983().method_7909().method_7848()).getString();
         String text = name + s;
         int width = mc.field_1772.method_1727(text);
         float x = (float)(box[0] + (box[2] - box[0]) / (double)2.0F - (double)width / (double)2.0F);
         drawContext.method_51433(mc.field_1772, text, (int)x, (int)(box[1] - (double)10.0F), this.text.getValue().getRGB(), false);
      }
   }

   @EventListener
   public void onReceivePacket(EntitySpawnedEvent event) {
      if (!nullCheck()) {
         class_1297 entity;
         if (this.pearl.getValue() && (entity = event.getEntity()) instanceof class_1684) {
            class_1684 pearlEntity = (class_1684)entity;
            if (pearlEntity.method_24921() != null) {
               pearlEntity.method_5665(pearlEntity.method_24921().method_5477());
               pearlEntity.method_5880(true);
            } else {
               mc.field_1687.method_18456().stream().min(Comparator.comparingDouble((p) -> p.method_19538().method_1022(new class_243(pearlEntity.method_23317(), pearlEntity.method_23318(), pearlEntity.method_23321())))).ifPresent((player) -> {
                  pearlEntity.method_5665(player.method_5477());
                  pearlEntity.method_5880(true);
               });
            }
         }

      }
   }

   private static enum RenderMode {
      ThreeD,
      TwoD;

      // $FF: synthetic method
      private static RenderMode[] $values() {
         return new RenderMode[]{ThreeD, TwoD};
      }
   }
}
