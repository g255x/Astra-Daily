package nekiplay.meteorplus.utils;

import java.util.ArrayList;
import java.util.List;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.utils.world.Dimension;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public class BlockHelper {
   public static boolean isVecComplete(ArrayList<class_243> vlist) {
      class_2338 ppos = MeteorClient.mc.field_1724.method_24515();

      for(class_243 b : vlist) {
         class_2338 bb = ppos.method_10069((int)b.method_10216(), (int)b.method_10214(), (int)b.method_10215());
         if (getBlock(bb) == class_2246.field_10124) {
            return false;
         }
      }

      return true;
   }

   public static List<class_2338> getSphere(class_2338 centerPos, int radius, int height) {
      ArrayList<class_2338> blocks = new ArrayList();

      for(int i = centerPos.method_10263() - radius; i < centerPos.method_10263() + radius; ++i) {
         for(int j = centerPos.method_10264() - height; j < centerPos.method_10264() + height; ++j) {
            for(int k = centerPos.method_10260() - radius; k < centerPos.method_10260() + radius; ++k) {
               class_2338 pos = new class_2338(i, j, k);
               if (distanceBetween(centerPos, pos) <= (double)radius && !blocks.contains(pos)) {
                  blocks.add(pos);
               }
            }
         }
      }

      return blocks;
   }

   public static double distanceBetween(class_2338 pos1, class_2338 pos2) {
      double d = (double)(pos1.method_10263() - pos2.method_10263());
      double e = (double)(pos1.method_10264() - pos2.method_10264());
      double f = (double)(pos1.method_10260() - pos2.method_10260());
      return (double)class_3532.method_15355((float)(d * d + e * e + f * f));
   }

   public static class_2338 getBlockPosFromDirection(class_2350 direction, class_2338 orginalPos) {
      class_2338 var10000;
      switch (direction) {
         case field_11036 -> var10000 = orginalPos.method_10084();
         case field_11033 -> var10000 = orginalPos.method_10074();
         case field_11034 -> var10000 = orginalPos.method_10078();
         case field_11039 -> var10000 = orginalPos.method_10067();
         case field_11043 -> var10000 = orginalPos.method_10095();
         case field_11035 -> var10000 = orginalPos.method_10072();
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static class_2248 getBlock(class_2338 p) {
      if (p == null) {
         return null;
      } else {
         return MeteorClient.mc.field_1687 == null ? null : MeteorClient.mc.field_1687.method_8320(p).method_26204();
      }
   }

   public static boolean isOurSurroundBlock(class_2338 bp) {
      class_2338 ppos = MeteorClient.mc.field_1724.method_24515();

      for(class_2350 direction : class_2350.values()) {
         if (direction != class_2350.field_11036 && direction != class_2350.field_11033) {
            class_2338 pos = ppos.method_10093(direction);
            if (pos.equals(bp)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean outOfRange(class_2338 cityBlock) {
      return class_3532.method_15355((float)MeteorClient.mc.field_1724.method_5649((double)cityBlock.method_10263(), (double)cityBlock.method_10264(), (double)cityBlock.method_10260())) > 4.0F;
   }

   public static class_2338 opposite(class_2338 pos, Dimension dimension) {
      int x = pos.method_10263();
      int z = pos.method_10260();
      if (dimension == Dimension.Overworld) {
         x /= 8;
         z /= 8;
      } else if (dimension == Dimension.Nether) {
         x *= 8;
         z *= 8;
      }

      return new class_2338(x, pos.method_10264(), z);
   }
}
