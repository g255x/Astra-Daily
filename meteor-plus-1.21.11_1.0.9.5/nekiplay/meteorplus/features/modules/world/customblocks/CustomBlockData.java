package nekiplay.meteorplus.features.modules.world.customblocks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;

public class CustomBlockData {
   public int block_id;
   public List<PosData> positions = new ArrayList();
   public String world;
   public String dimension;

   public CustomBlockData(class_2338 pos, int block_id) {
      this.positions.add(new PosData(pos));
      this.block_id = block_id;
   }
}
