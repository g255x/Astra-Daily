package nekiplay.meteorplus.features.modules.world.timer;

public enum TimerModes {
   NCP,
   Intave,
   Vulcan,
   Grim,
   OldFag,
   Custom,
   Custom_v2;

   public String toString() {
      String name = this.name();
      return name.replace('_', ' ').replaceAll("rem", "");
   }

   // $FF: synthetic method
   private static TimerModes[] $values() {
      return new TimerModes[]{NCP, Intave, Vulcan, Grim, OldFag, Custom, Custom_v2};
   }
}
