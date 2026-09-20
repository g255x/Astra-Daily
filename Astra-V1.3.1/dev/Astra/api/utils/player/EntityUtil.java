package dev.Astra.api.utils.player;

import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.gui.PeekScreen;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.settings.enums.SwingSide;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1743;
import net.minecraft.class_1799;
import net.minecraft.class_1829;
import net.minecraft.class_1835;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2815;
import net.minecraft.class_2879;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_408;
import net.minecraft.class_429;
import net.minecraft.class_433;
import net.minecraft.class_4667;
import net.minecraft.class_490;
import net.minecraft.class_9362;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class EntityUtil implements Wrapper {
   public static boolean inInventory() {
      return mc.field_1755 == null || mc.field_1755 instanceof class_4667 || mc.field_1755 instanceof class_429 || mc.field_1755 instanceof PeekScreen || mc.field_1755 instanceof class_408 || mc.field_1755 instanceof class_490 || mc.field_1755 instanceof ClickGuiScreen || mc.field_1755 instanceof class_433;
   }

   public static boolean isHoldingWeapon(class_1657 player) {
      return player.method_6047().method_7909() instanceof class_1829 || player.method_6047().method_7909() instanceof class_1743 || player.method_6047().method_7909() instanceof class_9362 || player.method_6047().method_7909() instanceof class_1835;
   }

   public static boolean isInsideBlock(class_1657 player) {
      return BlockUtil.canCollide(player, player.method_5829());
   }

   public static boolean isInsideBlock() {
      return isInsideBlock(mc.field_1724);
   }

   public static int getDamagePercent(class_1799 stack) {
      return stack.method_7919() == stack.method_7936() ? 100 : (int)((double)(stack.method_7936() - stack.method_7919()) / Math.max(0.1, (double)stack.method_7936()) * (double)100.0F);
   }

   public static boolean isArmorLow(class_1657 player, int durability) {
      for(class_1799 piece : player.method_5661()) {
         if (piece == null || piece.method_7960()) {
            return true;
         }

         if (getDamagePercent(piece) < durability) {
            return true;
         }
      }

      return false;
   }

   public static float getHealth(class_1297 entity) {
      if (entity.method_5709()) {
         class_1309 livingBase = (class_1309)entity;
         return livingBase.method_6032() + livingBase.method_6067();
      } else {
         return 0.0F;
      }
   }

   public static class_2338 getEntityPos(class_1297 entity) {
      return new BlockPosX(entity.method_19538());
   }

   public static class_2338 getPlayerPos(boolean fix) {
      return new BlockPosX(mc.field_1724.method_19538(), fix);
   }

   public static class_2338 getEntityPos(class_1297 entity, boolean fix) {
      return new BlockPosX(entity.method_19538(), fix);
   }

   public static boolean canSee(class_2338 pos, class_2350 side) {
      class_243 testVec = pos.method_46558().method_1031((double)side.method_10163().method_10263() * (double)0.5F, (double)side.method_10163().method_10264() * (double)0.5F, (double)side.method_10163().method_10260() * (double)0.5F);
      class_3965 result = mc.field_1687.method_17742(new class_3959(mc.field_1724.method_33571(), testVec, class_3960.field_17558, class_242.field_1348, mc.field_1724));
      return result == null || result.method_17783() == class_240.field_1333;
   }

   public static void swingHand(class_1268 hand, SwingSide side) {
      switch (side) {
         case All -> mc.field_1724.method_6104(hand);
         case Client -> mc.field_1724.method_23667(hand, false);
         case Server -> mc.method_1562().method_52787(new class_2879(hand));
      }

   }

   public static void syncInventory() {
      if (AntiCheat.INSTANCE.closeScreen.getValue()) {
         mc.method_1562().method_52787(new class_2815(mc.field_1724.field_7512.field_7763));
      }

   }
}
