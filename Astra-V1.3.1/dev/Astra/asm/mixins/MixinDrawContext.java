package dev.Astra.asm.mixins;

import dev.Astra.mod.modules.impl.misc.ShulkerViewer;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_2480;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_332.class})
public class MixinDrawContext {
   @Unique
   private static final class_1799[] ITEMS = new class_1799[27];
   @Final
   @Shadow
   private class_4587 field_44657;

   @Inject(
      method = {"drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"},
      at = {@At("TAIL")}
   )
   public void onDrawItem(class_327 textRenderer, class_1799 stack, int x, int y, String countOverride, CallbackInfo ci) {
      if (ShulkerViewer.INSTANCE.isOn() && ShulkerViewer.INSTANCE.icon.getValue() && stack.method_7909() instanceof class_1747 && ((class_1747)stack.method_7909()).method_7711() instanceof class_2480) {
         ShulkerViewer.getItemsInContainerItem(stack, ITEMS);

         for(class_1799 itemStack : ITEMS) {
            if (!itemStack.method_7960()) {
               this.field_44657.method_22903();
               this.field_44657.method_22905(0.5F, 0.5F, 1.0F);
               this.method_51427(itemStack, x * 2 + 20, y * 2 + 20);
               this.field_44657.method_22909();
               return;
            }
         }
      }

   }

   @Shadow
   public void method_51427(class_1799 item, int x, int y) {
   }
}
