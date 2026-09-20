package nekiplay.meteorplus.utils;

import meteordevelopment.meteorclient.utils.world.Dimension;
import net.minecraft.class_2246;
import net.minecraft.class_2248;

public class GenerationBlock {
   public int min_height;
   public int max_height;
   public Dimension dimension;
   public class_2248 block;

   public GenerationBlock(class_2248 block, Dimension dimension, int min_height, int max_height) {
      this.block = block;
      this.min_height = min_height;
      this.max_height = max_height;
      this.dimension = dimension;
   }

   public static GenerationBlock getGenerationBlock(class_2248 block, boolean newGeneration) {
      if (block == class_2246.field_22109) {
         return new GenerationBlock(block, Dimension.Nether, 8, 22);
      } else if (block != class_2246.field_10442 && block != class_2246.field_29029) {
         if (block != class_2246.field_10571 && block != class_2246.field_29026) {
            if (block != class_2246.field_10212 && block != class_2246.field_29027) {
               if (block != class_2246.field_10090 && block != class_2246.field_29028) {
                  if (block != class_2246.field_10080 && block != class_2246.field_29030) {
                     if (block != class_2246.field_10418 && block != class_2246.field_29219) {
                        return null;
                     } else {
                        return newGeneration ? new GenerationBlock(block, Dimension.Overworld, 0, 80) : new GenerationBlock(block, Dimension.Overworld, 1, 114);
                     }
                  } else {
                     return newGeneration ? new GenerationBlock(block, Dimension.Overworld, -64, 8) : new GenerationBlock(block, Dimension.Overworld, 1, 15);
                  }
               } else {
                  return newGeneration ? new GenerationBlock(block, Dimension.Overworld, -64, 64) : new GenerationBlock(block, Dimension.Overworld, 1, 31);
               }
            } else {
               return newGeneration ? new GenerationBlock(block, Dimension.Overworld, -16, 48) : new GenerationBlock(block, Dimension.Overworld, 1, 63);
            }
         } else {
            return newGeneration ? new GenerationBlock(block, Dimension.Overworld, -16, 112) : new GenerationBlock(block, Dimension.Overworld, 1, 32);
         }
      } else {
         return newGeneration ? new GenerationBlock(block, Dimension.Overworld, -64, 15) : new GenerationBlock(block, Dimension.Overworld, 1, 15);
      }
   }
}
