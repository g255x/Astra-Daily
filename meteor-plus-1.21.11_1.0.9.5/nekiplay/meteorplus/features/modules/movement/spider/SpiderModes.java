package nekiplay.meteorplus.features.modules.movement.spider;

public enum SpiderModes {
   Matrix,
   Vulcan,
   Elytra_clip;

   public String toString() {
      return super.toString().replace('_', ' ').replaceAll("_Lower_", "<");
   }

   // $FF: synthetic method
   private static SpiderModes[] $values() {
      return new SpiderModes[]{Matrix, Vulcan, Elytra_clip};
   }
}
