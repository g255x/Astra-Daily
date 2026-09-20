package nekiplay.meteorplus.mixin.meteorclient.gui;

import java.util.Calendar;
import meteordevelopment.meteorclient.utils.player.TitleScreenCredits;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_5250;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({TitleScreenCredits.class})
public class TitleScreenCreditsMixin {
   @ModifyArgs(
      method = {"add"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_5250;method_10852(Lnet/minecraft/class_2561;)Lnet/minecraft/class_5250;"
)
   )
   private static void modifyAddText(Args args) {
      class_2561 text = (class_2561)args.get(0);
      if (text != null) {
         Calendar calendar = Calendar.getInstance();
         int day = calendar.get(5);
         int month = calendar.get(2) + 1;
         if (day <= 7 && month == 4) {
            class_5250 newText = class_2561.method_43470(text.getString().replaceAll("Meteor", "Motor")).method_10862(text.method_10866());
            if (text instanceof class_5250) {
               class_5250 mutableText = (class_5250)text;
               newText = newText.method_27694((style) -> {
                  class_2583 original = mutableText.method_10866();
                  if (original.method_10969() != null) {
                     style = style.method_10949(original.method_10969());
                  }

                  if (original.method_10970() != null) {
                     style = style.method_10958(original.method_10970());
                  }

                  if (original.method_10955() != null) {
                     style = style.method_10975(original.method_10955());
                  }

                  return style.method_27703(original.method_10973()).method_10982(original.method_10984()).method_10978(original.method_10966()).method_36140(original.method_10986()).method_36141(original.method_10987());
               });
            }

            args.set(0, newText);
         }
      }

   }
}
