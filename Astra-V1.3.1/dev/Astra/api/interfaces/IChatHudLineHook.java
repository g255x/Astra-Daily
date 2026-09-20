package dev.Astra.api.interfaces;

import dev.Astra.api.utils.math.FadeUtils;

public interface IChatHudLineHook {
   int frogClient$getMessageId();

   void frogClient$setMessageId(int var1);

   boolean frogClient$getSync();

   void frogClient$setSync(boolean var1);

   FadeUtils frogClient$getFade();

   void frogClient$setFade(FadeUtils var1);
}
