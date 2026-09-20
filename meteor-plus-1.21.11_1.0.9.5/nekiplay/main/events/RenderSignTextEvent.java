package nekiplay.main.events;

import net.minecraft.class_2338;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_8242;

public class RenderSignTextEvent {
   private static final RenderSignTextEvent INSTANCE = new RenderSignTextEvent();
   public class_2338 pos;
   public class_8242 signText;
   public class_4587 matrices;
   public class_4597 vertexConsumers;
   public int light;
   public int lineHeight;
   public int lineWidth;
   public boolean front;

   public static RenderSignTextEvent get(class_2338 pos, class_8242 signText, class_4587 matrices, class_4597 vertexConsumers, int light, int lineHeight, int lineWidth, boolean front) {
      INSTANCE.pos = pos;
      INSTANCE.signText = signText;
      INSTANCE.matrices = matrices;
      INSTANCE.vertexConsumers = vertexConsumers;
      INSTANCE.light = light;
      INSTANCE.lineHeight = lineHeight;
      INSTANCE.lineWidth = lineWidth;
      INSTANCE.front = front;
      return INSTANCE;
   }
}
