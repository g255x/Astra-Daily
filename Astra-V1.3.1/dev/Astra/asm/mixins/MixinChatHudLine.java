package dev.Astra.asm.mixins;

import dev.Astra.api.interfaces.IChatHudLineHook;
import dev.Astra.api.utils.math.FadeUtils;
import net.minecraft.class_303;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_303.class})
public class MixinChatHudLine implements IChatHudLineHook {
   @Unique
   private int id = 0;
   @Unique
   private boolean sync = false;
   @Unique
   private FadeUtils fade;

   public int frogClient$getMessageId() {
      return this.id;
   }

   public void frogClient$setMessageId(int id) {
      this.id = id;
   }

   public boolean frogClient$getSync() {
      return this.sync;
   }

   public void frogClient$setSync(boolean sync) {
      this.sync = sync;
   }

   public FadeUtils frogClient$getFade() {
      return this.fade;
   }

   public void frogClient$setFade(FadeUtils fade) {
      this.fade = fade;
   }
}
