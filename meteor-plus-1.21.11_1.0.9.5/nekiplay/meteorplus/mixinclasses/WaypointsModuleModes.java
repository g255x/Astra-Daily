package nekiplay.meteorplus.mixinclasses;

import java.util.Comparator;
import java.util.Map;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.waypoints.Waypoint;

public class WaypointsModuleModes {
   public static enum SortMode {
      None,
      Distance,
      Name;

      // $FF: synthetic method
      private static SortMode[] $values() {
         return new SortMode[]{None, Distance, Name};
      }
   }

   public static class DistanceComparator implements Comparator<String> {
      Map<String, Waypoint> base;

      public DistanceComparator(Map<String, Waypoint> base) {
         this.base = base;
      }

      public int compare(String a, String b) {
         long distance1 = 0L;
         long distance2 = 0L;
         if (MeteorClient.mc.field_1724 != null) {
            Waypoint awp = (Waypoint)this.base.get(a);
            Waypoint bwp = (Waypoint)this.base.get(b);
            if (awp != null && bwp != null && awp.getPos() != null && bwp.getPos() != null) {
               distance1 = Math.round(MeteorClient.mc.field_1724.method_73189().method_1022(awp.getPos().method_46558()));
               distance2 = Math.round(MeteorClient.mc.field_1724.method_73189().method_1022(bwp.getPos().method_46558()));
            }
         }

         return distance1 >= distance2 ? 1 : -1;
      }
   }

   public static class NameComparator implements Comparator<String> {
      Map<String, Waypoint> base;

      public NameComparator(Map<String, Waypoint> base) {
         this.base = base;
      }

      public int compare(String a, String b) {
         if (this.base.containsKey(a) && this.base.containsKey(b)) {
            Waypoint awp = (Waypoint)this.base.get(a);
            Waypoint bwp = (Waypoint)this.base.get(b);
            if (awp != null && bwp != null) {
               return ((String)awp.name.get()).length() >= ((String)bwp.name.get()).length() ? 1 : -1;
            } else {
               return 0;
            }
         } else {
            return 0;
         }
      }
   }
}
