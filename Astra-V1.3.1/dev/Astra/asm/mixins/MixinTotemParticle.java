package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.TotemParticleEvent;
import java.awt.Color;
import net.minecraft.class_4002;
import net.minecraft.class_638;
import net.minecraft.class_734;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_734.class})
public abstract class MixinTotemParticle extends MixinParticle {
   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void hookInit(class_638 world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, class_4002 spriteProvider, CallbackInfo ci) {
      TotemParticleEvent event = TotemParticleEvent.get(velocityX, velocityY, velocityZ);
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         this.field_3852 = event.velocityX;
         this.field_3869 = event.velocityY;
         this.field_3850 = event.velocityZ;
         Color color = event.color;
         if (color != null) {
            this.method_3084((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
            this.method_3083((float)color.getAlpha() / 255.0F);
         }
      }

   }
}
