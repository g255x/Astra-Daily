package nekiplay.meteorplus.features.modules.combat.velocity;

public enum VelocityModes {
   Grim_Cancel,
   Grim_Cancel_v2,
   Grim_Skip;

   public String toString() {
      return super.toString().replace('_', ' ');
   }

   // $FF: synthetic method
   private static VelocityModes[] $values() {
      return new VelocityModes[]{Grim_Cancel, Grim_Cancel_v2, Grim_Skip};
   }
}
