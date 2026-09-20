package nekiplay.meteorplus.mixinclasses;

public enum SpoofMode {
   Sensor,
   Fake;

   // $FF: synthetic method
   private static SpoofMode[] $values() {
      return new SpoofMode[]{Sensor, Fake};
   }
}
