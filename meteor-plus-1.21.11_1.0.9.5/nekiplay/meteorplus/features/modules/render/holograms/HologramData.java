package nekiplay.meteorplus.features.modules.render.holograms;

import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.world.Dimension;
import net.minecraft.class_2338;

public class HologramData {
   public double x;
   public double y;
   public double z;
   public String text;
   public Color color;
   public int item_id = 0;
   public int item_scale = 2;

   public HologramData() {
   }

   public HologramData(double x, double y, double z, String text, String dimension, Color color, double max_render_distance) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.color = color;
      this.text = text;
   }

   public HologramData(class_2338 pos, String text, Dimension dimension, Color color, double max_render_distance) {
      this.x = (double)pos.method_10263();
      this.y = (double)pos.method_10264();
      this.z = (double)pos.method_10260();
      this.color = color;
      this.text = text;
   }
}
