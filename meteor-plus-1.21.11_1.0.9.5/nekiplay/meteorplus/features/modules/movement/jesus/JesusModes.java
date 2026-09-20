package nekiplay.meteorplus.features.modules.movement.jesus;

public enum JesusModes {
   NCP,
   Matrix_Zoom,
   Matrix_Zoom_2,
   Vulcan_Exploit;

   public String toString() {
      return super.toString().replace('_', ' ');
   }

   // $FF: synthetic method
   private static JesusModes[] $values() {
      return new JesusModes[]{NCP, Matrix_Zoom, Matrix_Zoom_2, Vulcan_Exploit};
   }
}
