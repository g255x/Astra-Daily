package dev.Astra.api.utils.path;

import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.world.BlockPosX;
import java.util.ArrayList;
import net.minecraft.class_1309;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2261;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2551;

public class PathUtils implements Wrapper {
   public static final double range = (double)9.0F;

   private static boolean canPassThrough(class_2338 pos) {
      class_2248 block = mc.field_1687.method_8320(new class_2338(pos.method_10263(), pos.method_10264(), pos.method_10260())).method_26204();
      return block == class_2246.field_10124 || block instanceof class_2261 || block == class_2246.field_10597 || block == class_2246.field_9983 || block == class_2246.field_10382 || block == class_2246.field_27097 || block instanceof class_2551;
   }

   public static ArrayList<Vec3> computePath(class_1309 fromEntity, class_1309 toEntity) {
      return computePath(new Vec3(fromEntity.method_23317(), fromEntity.method_23318(), fromEntity.method_23321()), new Vec3(toEntity.method_23317(), toEntity.method_23318(), toEntity.method_23321()));
   }

   public static ArrayList<Vec3> computePath(class_243 vec3d) {
      return computePath(new Vec3(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321()), new Vec3(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350));
   }

   public static ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
      if (!canPassThrough(new BlockPosX(topFrom.mc()))) {
         topFrom = topFrom.addVector((double)0.0F, (double)1.0F, (double)0.0F);
      }

      AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
      pathfinder.compute();
      int i = 0;
      Vec3 lastLoc = null;
      Vec3 lastDashLoc = null;
      ArrayList<Vec3> path = new ArrayList();
      ArrayList<Vec3> pathFinderPath = pathfinder.getPath();

      for(Vec3 pathElm : pathFinderPath) {
         if (i != 0 && i != pathFinderPath.size() - 1) {
            boolean canContinue = true;
            if (pathElm.squareDistanceTo(lastDashLoc) > (double)9.0F) {
               canContinue = false;
            } else {
               double smallX = Math.min(lastDashLoc.x(), pathElm.x());
               double smallY = Math.min(lastDashLoc.y(), pathElm.y());
               double smallZ = Math.min(lastDashLoc.z(), pathElm.z());
               double bigX = Math.max(lastDashLoc.x(), pathElm.x());
               double bigY = Math.max(lastDashLoc.y(), pathElm.y());
               double bigZ = Math.max(lastDashLoc.z(), pathElm.z());

               label65:
               for(int x = (int)smallX; (double)x <= bigX; ++x) {
                  for(int y = (int)smallY; (double)y <= bigY; ++y) {
                     for(int z = (int)smallZ; (double)z <= bigZ; ++z) {
                        if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
                           canContinue = false;
                           break label65;
                        }
                     }
                  }
               }
            }

            if (!canContinue) {
               if (!path.contains(lastLoc.addVector((double)0.5F, (double)0.0F, (double)0.5F))) {
                  path.add(lastLoc.addVector((double)0.5F, (double)0.0F, (double)0.5F));
               }

               lastDashLoc = lastLoc;
            }
         } else {
            if (lastLoc != null && !path.contains(lastLoc.addVector((double)0.5F, (double)0.0F, (double)0.5F))) {
               path.add(lastLoc.addVector((double)0.5F, (double)0.0F, (double)0.5F));
            }

            if (!path.contains(pathElm.addVector((double)0.5F, (double)0.0F, (double)0.5F))) {
               path.add(pathElm.addVector((double)0.5F, (double)0.0F, (double)0.5F));
            }

            lastDashLoc = pathElm;
         }

         lastLoc = pathElm;
         ++i;
      }

      return path;
   }
}
