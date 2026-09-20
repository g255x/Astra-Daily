package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.mod.modules.impl.misc.ShulkerViewer;
import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_3936;
import net.minecraft.class_437;
import net.minecraft.class_465;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_465.class})
public abstract class MixinHandledScreen<T extends class_1703> extends class_437 implements class_3936<T> {
   @Unique
   private static final class_1799[] ITEMS = new class_1799[27];
   @Shadow
   protected @Nullable class_1735 field_2787;
   @Shadow
   protected int field_2776;
   @Shadow
   protected int field_2800;

   protected MixinHandledScreen(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"mouseClicked"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
      class_1799 itemStack;
      if (button == 2 && this.field_2787 != null && !this.field_2787.method_7677().method_7960() && Wrapper.mc.field_1724.field_7512.method_34255().method_7960() && ShulkerViewer.INSTANCE.isOn() && (ShulkerViewer.hasItems(itemStack = this.field_2787.method_7677()) || itemStack.method_7909() == class_1802.field_8466 && Astra.PLAYER.known)) {
         cir.setReturnValue(ShulkerViewer.openContainer(this.field_2787.method_7677(), ITEMS, false));
      }

   }

   @Inject(
      method = {"render"},
      at = {@At("RETURN")}
   )
   private void onRender(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (ShulkerViewer.INSTANCE.isOn() && ShulkerViewer.INSTANCE.toolTips.getValue() && this.field_2787 != null && !this.field_2787.method_7677().method_7960() && this.field_22787.field_1724.field_7498.method_34255().method_7960() && (ShulkerViewer.hasItems(this.field_2787.method_7677()) || this.field_2787.method_7677().method_7909() == class_1802.field_8466 && Astra.PLAYER.known)) {
         ShulkerViewer.renderShulkerToolTip(context, mouseX, mouseY, this.field_2787.method_7677());
      }

   }

   @Shadow
   public abstract void method_25420(class_332 var1, int var2, int var3, float var4);
}
