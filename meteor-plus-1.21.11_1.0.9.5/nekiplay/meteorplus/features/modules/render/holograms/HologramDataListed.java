package nekiplay.meteorplus.features.modules.render.holograms;

import java.util.ArrayList;
import java.util.Objects;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.world.Dimension;
import net.minecraft.class_2338;

public class HologramDataListed {
   public double x;
   public double y;
   public double z;
   public String text;
   public String dimension;
   public Color color;
   public double max_render_distance = (double)16.0F;
   public double scale = (double)1.0F;
   public boolean distanceScaling = false;
   public int item_id = 0;
   public int item_scale = 2;
   public ArrayList<HologramData> other_holograms = new ArrayList();

   public HologramDataListed() {
   }

   public HologramDataListed(double x, double y, double z, String text, String dimension, Color color, double max_render_distance) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.color = color;
      this.text = text;
      this.dimension = dimension;
      this.max_render_distance = max_render_distance;
   }

   public HologramDataListed(class_2338 pos, String text, Dimension dimension, Color color, double max_render_distance) {
      this.x = (double)pos.method_10263();
      this.y = (double)pos.method_10264();
      this.z = (double)pos.method_10260();
      this.color = color;
      this.text = text;
      this.dimension = dimension.name();
      this.max_render_distance = max_render_distance;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         HologramDataListed otherPos = (HologramDataListed)obj;
         return this.x == otherPos.x && this.y == otherPos.y && this.z == otherPos.z ? true : super.equals(obj);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.x, this.y, this.z});
   }
}
