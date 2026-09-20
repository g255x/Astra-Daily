package nekiplay.meteorplus.features.modules.movement.fly;

public enum FlyModes {
   Vulcan_Clip,
   Matrix_Exploit_2,
   Matrix_Exploit;

   public String toString() {
      String name = this.name();
      return name.replace('_', ' ');
   }

   // $FF: synthetic method
   private static FlyModes[] $values() {
      return new FlyModes[]{Vulcan_Clip, Matrix_Exploit_2, Matrix_Exploit};
   }
}
