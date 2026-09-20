package nekiplay.meteorplus.features.modules.world.customblocks;

import java.util.Objects;
import net.minecraft.class_2338;

public class PosData {
   public double x;
   public double y;
   public double z;

   public PosData(class_2338 pos) {
      this.x = (double)pos.method_10263();
      this.y = (double)pos.method_10264();
      this.z = (double)pos.method_10260();
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         PosData otherPos = (PosData)obj;
         return this.x == otherPos.x && this.y == otherPos.y && this.z == otherPos.z ? true : super.equals(obj);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.x, this.y, this.z});
   }
}
