package nekiplay.meteorplus.features.modules.movement.nofall;

public enum NoFallModes {
   Matrix_New,
   Vulcan,
   Vulcan_2dot7dot7,
   Verus,
   Elytra_Clip,
   Elytra_Fly,
   No_Ground,
   No_Ground_Elytra;

   public String toString() {
      return super.toString().replace('_', ' ').replaceAll("dot", ".");
   }

   // $FF: synthetic method
   private static NoFallModes[] $values() {
      return new NoFallModes[]{Matrix_New, Vulcan, Vulcan_2dot7dot7, Verus, Elytra_Clip, Elytra_Fly, No_Ground, No_Ground_Elytra};
   }
}
