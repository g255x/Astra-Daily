package nekiplay.meteorplus.features.modules.world.autoobsidianmine;

public enum AutoObsidianFarmModes {
   Portals_Vanila,
   Portal_Homes,
   Cauldrons;

   public String toString() {
      String name = this.name();
      return name.replace('_', ' ');
   }

   // $FF: synthetic method
   private static AutoObsidianFarmModes[] $values() {
      return new AutoObsidianFarmModes[]{Portals_Vanila, Portal_Homes, Cauldrons};
   }
}
