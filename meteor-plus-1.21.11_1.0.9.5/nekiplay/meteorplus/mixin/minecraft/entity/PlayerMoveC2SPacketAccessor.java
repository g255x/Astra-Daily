package nekiplay.meteorplus.mixin.minecraft.entity;

import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2828.class})
public interface PlayerMoveC2SPacketAccessor {
   @Mutable
   @Accessor("field_12886")
   void setY(double var1);

   @Accessor("field_12886")
   double getY();

   @Mutable
   @Accessor("field_29179")
   void setOnGround(boolean var1);

   @Accessor("field_29179")
   boolean getOnGround();
}
