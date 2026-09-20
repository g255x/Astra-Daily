package dev.Astra.asm.mixins;

import dev.Astra.api.interfaces.IShaderEffectHook;
import dev.Astra.asm.accessors.IPostProcessShader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.class_276;
import net.minecraft.class_279;
import net.minecraft.class_283;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_279.class})
public class MixinShaderEffect implements IShaderEffectHook {
   @Unique
   private final List<String> fakedBufferNames = new ArrayList();
   @Shadow
   @Final
   private Map<String, class_276> field_1495;
   @Shadow
   @Final
   private List<class_283> field_1497;

   public void frogClient$addHook(String name, class_276 buffer) {
      class_276 previousFramebuffer = (class_276)this.field_1495.get(name);
      if (previousFramebuffer != buffer) {
         if (previousFramebuffer != null) {
            for(class_283 pass : this.field_1497) {
               if (pass.field_1536 == previousFramebuffer) {
                  ((IPostProcessShader)pass).setInput(buffer);
               }

               if (pass.field_1538 == previousFramebuffer) {
                  ((IPostProcessShader)pass).setOutput(buffer);
               }
            }

            this.field_1495.remove(name);
            this.fakedBufferNames.remove(name);
         }

         this.field_1495.put(name, buffer);
         this.fakedBufferNames.add(name);
      }
   }

   @Inject(
      method = {"close"},
      at = {@At("HEAD")}
   )
   void deleteFakeBuffersHook(CallbackInfo ci) {
      for(String fakedBufferName : this.fakedBufferNames) {
         this.field_1495.remove(fakedBufferName);
      }

   }
}
