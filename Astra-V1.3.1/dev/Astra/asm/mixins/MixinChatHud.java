package dev.Astra.asm.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import dev.Astra.api.interfaces.IChatHudHook;
import dev.Astra.api.interfaces.IChatHudLineHook;
import dev.Astra.api.utils.math.FadeUtils;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.asm.accessors.IChatHud;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_2561;
import net.minecraft.class_303;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_338;
import net.minecraft.class_5481;
import net.minecraft.class_7469;
import net.minecraft.class_7591;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_338.class})
public abstract class MixinChatHud implements IChatHudHook {
   @Unique
   private int nextMessageId = 0;
   @Unique
   private boolean nextSync;
   @Unique
   private int chatLineIndex;
   @Final
   @Shadow
   private List<class_303.class_7590> field_2064;
   @Shadow
   @Final
   private List<class_303> field_2061;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   public void onInit(class_310 client, CallbackInfo ci) {
      ((IChatHud)this).setMessages(new CopyOnWriteArrayList());
      ((IChatHud)this).setVisibleMessages(new CopyOnWriteArrayList());
   }

   public void frogClient$addMessage(class_2561 message, int id) {
      this.nextMessageId = id;
      this.nextSync = true;
      this.method_1812(message);
      this.nextSync = false;
      this.nextMessageId = 0;
   }

   public void frogClient$addMessage(class_2561 message) {
      this.nextSync = true;
      this.method_1812(message);
      this.nextSync = false;
   }

   public void frogClient$addMessageOutSync(class_2561 message, int id) {
      this.nextMessageId = id;
      this.method_1812(message);
      this.nextMessageId = 0;
   }

   @Inject(
      method = {"addVisibleMessage"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/util/List;add(ILjava/lang/Object;)V",
   ordinal = 0,
   shift = Shift.AFTER
)}
   )
   private void onAddMessageAfterNewChatHudLineVisible(class_303 message, CallbackInfo ci) {
      IChatHudLineHook line = (IChatHudLineHook)this.field_2064.getFirst();
      if (line != null) {
         line.frogClient$setMessageId(this.nextMessageId);
         line.frogClient$setSync(this.nextSync);
         line.frogClient$setFade(new FadeUtils((long)ClientSetting.INSTANCE.animationTime.getValueInt()));
      }

   }

   @Inject(
      method = {"addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/util/List;add(ILjava/lang/Object;)V",
   ordinal = 0,
   shift = Shift.AFTER
)}
   )
   private void onAddMessageAfterNewChatHudLine(class_303 message, CallbackInfo ci) {
      IChatHudLineHook line = (IChatHudLineHook)this.field_2061.getFirst();
      if (line != null) {
         line.frogClient$setMessageId(this.nextMessageId);
         line.frogClient$setSync(this.nextSync);
         line.frogClient$setFade(new FadeUtils((long)ClientSetting.INSTANCE.animationTime.getValueInt()));
      }

   }

   @Inject(
      at = {@At("HEAD")},
      method = {"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"}
   )
   private void onAddMessage(class_2561 message, class_7469 signatureData, class_7591 indicator, CallbackInfo ci) {
      if (this.nextMessageId != 0) {
         this.field_2064.removeIf((msg) -> ((IChatHudLineHook)msg).frogClient$getMessageId() == this.nextMessageId);
         this.field_2061.removeIf((msg) -> ((IChatHudLineHook)msg).frogClient$getMessageId() == this.nextMessageId);
      }

   }

   @Redirect(
      method = {"addVisibleMessage"},
      at = @At(
   value = "INVOKE",
   target = "Ljava/util/List;size()I",
   ordinal = 2,
   remap = false
),
      require = 0
   )
   public int chatLinesSize(List<class_303.class_7590> list) {
      return ClientSetting.INSTANCE.isOn() && ClientSetting.INSTANCE.infiniteChat.getValue() ? -2147483647 : list.size();
   }

   @Redirect(
      method = {"render"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"
),
      require = 0
   )
   private int drawStringWithShadow(class_332 drawContext, class_327 textRenderer, class_5481 text, int x, int y, int color) {
      IChatHudLineHook line = (IChatHudLineHook)this.field_2064.get(this.chatLineIndex);
      if (line != null) {
         FadeUtils fadeUtils = line.frogClient$getFade();
         double ease = fadeUtils == null ? (double)0.0F : fadeUtils.ease(ClientSetting.INSTANCE.ease.getValue());
         double fade = (double)1.0F - ease;
         x += (int)(fade * ClientSetting.INSTANCE.animateOffset.getValue());
         double c = Math.max((double)10.0F, (double)(color >> 24 & 255) * ease);
         return line.frogClient$getSync() ? drawContext.method_35720(textRenderer, text, x, y, ColorUtil.injectAlpha(ClientSetting.INSTANCE.color.getValue(), ClientSetting.INSTANCE.fade.getValue() ? (int)c : color >> 24 & 255).getRGB()) : drawContext.method_35720(textRenderer, text, x, y, ColorUtil.injectAlpha(color, ClientSetting.INSTANCE.fade.getValue() ? (int)c : color >> 24 & 255));
      } else {
         return drawContext.method_35720(textRenderer, text, x, y, color);
      }
   }

   @Inject(
      method = {"render"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;addedTime()I"
)}
   )
   public void getChatLineIndex(CallbackInfo ci, @Local(ordinal = 13) int chatLineIndex) {
      this.chatLineIndex = chatLineIndex;
   }

   @ModifyVariable(
      method = {"render"},
      at = @At("STORE")
   )
   private class_7591 removeMessageIndicator(class_7591 messageIndicator) {
      return ClientSetting.INSTANCE.hideIndicator.getValue() ? null : messageIndicator;
   }

   @Shadow
   public abstract void method_1812(class_2561 var1);

   @Shadow
   public int method_1811() {
      return 0;
   }
}
