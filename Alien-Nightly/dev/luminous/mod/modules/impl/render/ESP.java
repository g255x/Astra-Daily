package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.EntitySpawnedEvent;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.IEntity;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import java.util.Comparator;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1684;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2611;
import net.minecraft.class_2627;
import net.minecraft.class_2640;
import net.minecraft.class_4587;

public class ESP extends Module {
   public static ESP INSTANCE;
   public final BooleanSetting box = this.add((new BooleanSetting("BoxESP", true)).setParent());
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
      Color var10005 = new Color(255, 243, 129, 100);
      BooleanSetting var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.endPortalFill = this.add((new ColorSetting("EndPortalFill", var10005, var10006::isOpen)).injectBoolean(false));
      var10005 = new Color(255, 243, 129, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.endPortalOutline = this.add((new ColorSetting("EndPortalOutline", var10005, var10006::isOpen)).injectBoolean(false));
      var10005 = new Color(255, 255, 255, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.itemFill = this.add((new ColorSetting("ItemFill", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(255, 255, 255, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.itemOutline = this.add((new ColorSetting("ItemOutline", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(255, 255, 255, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.playerFill = this.add((new ColorSetting("PlayerFill", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(255, 255, 255, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.playerOutline = this.add((new ColorSetting("PlayerOutline", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(255, 198, 123, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.chestFill = this.add((new ColorSetting("ChestFill", var10005, var10006::isOpen)).injectBoolean(false));
      var10005 = new Color(255, 198, 123, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.chestOutline = this.add((new ColorSetting("ChestOutline", var10005, var10006::isOpen)).injectBoolean(false));
      var10005 = new Color(255, 100, 255, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.enderChestFill = this.add((new ColorSetting("EnderChestFill", var10005, var10006::isOpen)).injectBoolean(false));
      var10005 = new Color(255, 100, 255, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.enderChestOutline = this.add((new ColorSetting("EnderChestOutline", var10005, var10006::isOpen)).injectBoolean(false));
      var10005 = new Color(15, 255, 255, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.shulkerBoxFill = this.add((new ColorSetting("ShulkerBoxFill", var10005, var10006::isOpen)).injectBoolean(false));
      var10005 = new Color(15, 255, 255, 100);
      var10006 = this.box;
      Objects.requireNonNull(var10006);
      this.shulkerBoxOutline = this.add((new ColorSetting("ShulkerBoxOutline", var10005, var10006::isOpen)).injectBoolean(false));
      this.item = this.add((new BooleanSetting("ItemName", false)).setParent());
      var10006 = this.item;
      Objects.requireNonNull(var10006);
      this.customName = this.add(new BooleanSetting("CustomName", false, var10006::isOpen));
      var10006 = this.item;
      Objects.requireNonNull(var10006);
      this.count = this.add(new BooleanSetting("Count", true, var10006::isOpen));
      var10005 = new Color(255, 255, 255, 255);
      var10006 = this.item;
      Objects.requireNonNull(var10006);
      this.text = this.add(new ColorSetting("Text", var10005, var10006::isOpen));
      this.pearl = this.add(new BooleanSetting("PearlOwner", true));
      this.setChinese("透视");
      INSTANCE = this;
   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.item.getValue()) {
         for(class_1297 entity : Alien.THREAD.getEntities()) {
            if (entity instanceof class_1542) {
               class_1542 itemEntity = (class_1542)entity;
               int itemCount = itemEntity.method_6983().method_7947();
               String s = this.count.getValue() && itemCount > 1 ? " x" + itemCount : "";
               String name = (this.customName.getValue() ? itemEntity.method_6983().method_7964() : itemEntity.method_6983().method_7909().method_7848()).getString();
               Render3DUtil.drawText3D(name + s, ((IEntity)itemEntity).getDimensions().method_30757(new class_243(MathUtil.interpolate(itemEntity.field_6038, itemEntity.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(itemEntity.field_5971, itemEntity.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(itemEntity.field_5989, itemEntity.method_23321(), (double)mc.method_60646().method_60637(true)))).method_1009((double)0.0F, 0.1, (double)0.0F).method_1005().method_1031((double)0.0F, (double)0.5F, (double)0.0F), this.text.getValue());
            }
         }
      }

      if (this.box.getValue()) {
         if (this.itemFill.booleanValue || this.playerFill.booleanValue) {
            for(class_1297 entityx : Alien.THREAD.getEntities()) {
               if (!(entityx instanceof class_1542) || !this.itemFill.booleanValue && !this.itemOutline.booleanValue) {
                  if (entityx instanceof class_1657 && (this.playerFill.booleanValue || this.playerOutline.booleanValue)) {
                     Color color = this.playerFill.getValue();
                     Render3DUtil.draw3DBox(matrixStack, ((IEntity)entityx).getDimensions().method_30757(new class_243(MathUtil.interpolate(entityx.field_6038, entityx.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entityx.field_5971, entityx.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entityx.field_5989, entityx.method_23321(), (double)mc.method_60646().method_60637(true)))).method_1009((double)0.0F, 0.1, (double)0.0F), color, this.playerOutline.getValue(), this.playerOutline.booleanValue, this.playerFill.booleanValue);
                  }
               } else {
                  Color color = this.itemFill.getValue();
                  Render3DUtil.draw3DBox(matrixStack, ((IEntity)entityx).getDimensions().method_30757(new class_243(MathUtil.interpolate(entityx.field_6038, entityx.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entityx.field_5971, entityx.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entityx.field_5989, entityx.method_23321(), (double)mc.method_60646().method_60637(true)))), color, this.itemOutline.getValue(), this.itemOutline.booleanValue, this.itemFill.booleanValue);
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

   @EventListener
   public void onReceivePacket(EntitySpawnedEvent event) {
      if (!nullCheck() && this.pearl.getValue()) {
         class_1297 var3 = event.getEntity();
         if (var3 instanceof class_1684) {
            class_1684 pearlEntity = (class_1684)var3;
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
}
