package dev.luminous.mod.modules.impl.client;

import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2665;
import net.minecraft.class_332;

public class ItemsCounter extends Module {
   public static ItemsCounter INSTANCE;
   private final BooleanSetting hideEmpty = this.add(new BooleanSetting("HideEmpty", true));
   private final BooleanSetting crystal = this.add(new BooleanSetting("Crystal", true));
   private final BooleanSetting xp = this.add(new BooleanSetting("XP", true));
   private final BooleanSetting pearl = this.add(new BooleanSetting("Pearl", true));
   private final BooleanSetting obsidian = this.add(new BooleanSetting("Obsidian", true));
   private final BooleanSetting egApple = this.add(new BooleanSetting("E-GApple", true));
   private final BooleanSetting gApple = this.add(new BooleanSetting("GApple", true));
   private final BooleanSetting totem = this.add(new BooleanSetting("Totem", true));
   private final BooleanSetting web = this.add(new BooleanSetting("Web", true));
   private final BooleanSetting anchor = this.add(new BooleanSetting("Anchor", true));
   private final BooleanSetting glowstone = this.add(new BooleanSetting("Glowstone", true));
   private final BooleanSetting piston = this.add(new BooleanSetting("Piston", true));
   private final BooleanSetting redstone = this.add(new BooleanSetting("RedStone", true));
   private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true));
   private final BooleanSetting firework = this.add(new BooleanSetting("Firework", true));
   private final SliderSetting xOffset = this.add(new SliderSetting("X", 100, 0, 1500));
   private final SliderSetting yOffset = this.add(new SliderSetting("Y", 100, 0, 1000));
   private final SliderSetting offset = this.add(new SliderSetting("Offset", 18, 0, 30));
   private final class_1799 crystalStack;
   private final class_1799 xpStack;
   private final class_1799 pearlStack;
   private final class_1799 obsidianStack;
   private final class_1799 eGappleStack;
   private final class_1799 gappleStack;
   private final class_1799 totemStack;
   private final class_1799 webStack;
   private final class_1799 anchorStack;
   private final class_1799 glowstoneStack;
   private final class_1799 pistonStack;
   private final class_1799 redstoneStack;
   private final class_1799 enderChestStack;
   private final class_1799 fireworkStack;
   int x;
   int y;
   class_332 drawContext;

   public ItemsCounter() {
      super("Items", Module.Category.Client);
      this.crystalStack = new class_1799(class_1802.field_8301);
      this.xpStack = new class_1799(class_1802.field_8287);
      this.pearlStack = new class_1799(class_1802.field_8634);
      this.obsidianStack = new class_1799(class_1802.field_8281);
      this.eGappleStack = new class_1799(class_1802.field_8367);
      this.gappleStack = new class_1799(class_1802.field_8463);
      this.totemStack = new class_1799(class_1802.field_8288);
      this.webStack = new class_1799(class_1802.field_8786);
      this.anchorStack = new class_1799(class_1802.field_23141);
      this.glowstoneStack = new class_1799(class_1802.field_8801);
      this.pistonStack = new class_1799(class_1802.field_8249);
      this.redstoneStack = new class_1799(class_1802.field_8793);
      this.enderChestStack = new class_1799(class_1802.field_8466);
      this.fireworkStack = new class_1799(class_1802.field_8639);
      this.setChinese("物品数量");
      INSTANCE = this;
   }

   public void onRender2D(class_332 drawContext, float tickDelta) {
      this.drawContext = drawContext;
      this.x = this.xOffset.getValueInt() - this.offset.getValueInt();
      this.y = this.yOffset.getValueInt();
      if (this.crystal.getValue()) {
         this.crystalStack.method_7939(this.getItemCount(class_1802.field_8301));
         this.drawItem(this.crystalStack);
      }

      if (this.xp.getValue()) {
         this.xpStack.method_7939(this.getItemCount(class_1802.field_8287));
         this.drawItem(this.xpStack);
      }

      if (this.pearl.getValue()) {
         this.pearlStack.method_7939(this.getItemCount(class_1802.field_8634));
         this.drawItem(this.pearlStack);
      }

      if (this.obsidian.getValue()) {
         this.obsidianStack.method_7939(this.getItemCount(class_1802.field_8281));
         this.drawItem(this.obsidianStack);
      }

      if (this.egApple.getValue()) {
         this.eGappleStack.method_7939(this.getItemCount(class_1802.field_8367));
         this.drawItem(this.eGappleStack);
      }

      if (this.gApple.getValue()) {
         this.gappleStack.method_7939(this.getItemCount(class_1802.field_8463));
         this.drawItem(this.gappleStack);
      }

      if (this.totem.getValue()) {
         this.totemStack.method_7939(this.getItemCount(class_1802.field_8288));
         this.drawItem(this.totemStack);
      }

      if (this.web.getValue()) {
         this.webStack.method_7939(this.getItemCount(class_1802.field_8786));
         this.drawItem(this.webStack);
      }

      if (this.anchor.getValue()) {
         this.anchorStack.method_7939(this.getItemCount(class_1802.field_23141));
         this.drawItem(this.anchorStack);
      }

      if (this.glowstone.getValue()) {
         this.glowstoneStack.method_7939(this.getItemCount(class_1802.field_8801));
         this.drawItem(this.glowstoneStack);
      }

      if (this.piston.getValue()) {
         int pistonCount = InventoryUtil.getItemCount(class_2665.class);
         if (pistonCount > 0 || !this.hideEmpty.getValue()) {
            this.x += this.offset.getValueInt();
            this.pistonStack.method_7939(Math.max(1, pistonCount));
            this.drawItem(this.pistonStack);
         }
      }

      if (this.redstone.getValue()) {
         this.redstoneStack.method_7939(this.getItemCount(class_1802.field_8793));
         this.drawItem(this.redstoneStack);
      }

      if (this.enderChest.getValue()) {
         this.enderChestStack.method_7939(this.getItemCount(class_1802.field_8466));
         this.drawItem(this.enderChestStack);
      }

      if (this.firework.getValue()) {
         this.fireworkStack.method_7939(this.getItemCount(class_1802.field_8639));
         this.drawItem(this.fireworkStack);
      }

   }

   private int getItemCount(class_1792 item) {
      int i = InventoryUtil.getItemCount(item);
      if (this.hideEmpty.getValue() && i == 0) {
         return 0;
      } else {
         this.x += this.offset.getValueInt();
         return Math.max(i, 1);
      }
   }

   private void drawItem(class_1799 itemStack) {
      this.drawContext.method_51427(itemStack, this.x, this.y);
      this.drawContext.method_51431(mc.field_1772, itemStack, this.x, this.y);
   }
}
