package nekiplay.meteorplus.features.modules.combat.killaura;

public enum KillAuraPlusModes {
   None,
   Matrix;

   public String toString() {
      return super.toString().replaceAll("Plus", "+").replaceAll("_", " ");
   }

   // $FF: synthetic method
   private static KillAuraPlusModes[] $values() {
      return new KillAuraPlusModes[]{None, Matrix};
   }
}
