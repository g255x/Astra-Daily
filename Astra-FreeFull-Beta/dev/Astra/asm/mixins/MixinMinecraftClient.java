package dev.Astra.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.events.Event;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.DoAttackEvent;
import dev.Astra.api.events.impl.GameLeftEvent;
import dev.Astra.api.events.impl.OpenScreenEvent;
import dev.Astra.api.events.impl.ResizeEvent;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.core.impl.CommandManager;
import dev.Astra.core.impl.FontManager;
import dev.Astra.core.impl.ShaderManager;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.player.InteractTweaks;
import java.awt.Color;
import net.minecraft.class_1041;
import net.minecraft.class_1074;
import net.minecraft.class_1132;
import net.minecraft.class_1268;
import net.minecraft.class_155;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_310;
import net.minecraft.class_329;
import net.minecraft.class_3965;
import net.minecraft.class_4093;
import net.minecraft.class_412;
import net.minecraft.class_434;
import net.minecraft.class_435;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_4587;
import net.minecraft.class_542;
import net.minecraft.class_634;
import net.minecraft.class_636;
import net.minecraft.class_638;
import net.minecraft.class_642;
import net.minecraft.class_702;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_310.class})
public abstract class MixinMinecraftClient extends class_4093<Runnable> {
   @Shadow
   @Final
   public class_329 field_1705;
   @Shadow
   public int field_1771;
   @Shadow
   public class_746 field_1724;
   @Shadow
   public class_239 field_1765;
   @Shadow
   public class_636 field_1761;
   @Final
   @Shadow
   public class_702 field_1713;
   @Shadow
   public class_638 field_1687;
   @Shadow
   private class_1132 field_1766;
   @Shadow
   public class_437 field_1755;
   @Shadow
   @Final
   private class_1041 field_1704;
   @Unique
   private static long FrogStartTs = 0L;

   public MixinMinecraftClient(String string) {
      super(string);
   }

   @Inject(
      method = {"method_15993"},
      at = {@At("TAIL")}
   )
   private void captureResize(CallbackInfo ci) {
      Astra.EVENT_BUS.post(new ResizeEvent(this.field_1704));
   }

   @Redirect(
      method = {"method_1523"},
      at = @At(
   value = "INVOKE",
   target = "Lcom/mojang/blaze3d/systems/RenderSystem;limitDisplayFPS(I)V"
),
      require = 0
   )
   public void fpsHook(int fps) {
      if (!ClientSetting.INSTANCE.fuckFPSLimit.getValue()) {
         RenderSystem.limitDisplayFPS(fps);
      }

   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   void postWindowInit(class_542 args, CallbackInfo ci) {
      FontManager.init();
      FrogStartTs = System.currentTimeMillis();
   }

   @Inject(
      method = {"method_1507"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSetScreen(class_437 screen, CallbackInfo info) {
      if (Astra.EVENT_BUS != null) {
         OpenScreenEvent event = OpenScreenEvent.get(screen);
         Astra.EVENT_BUS.post(event);
         if (event.isCancelled()) {
            info.cancel();
         }
      }

   }

   @Inject(
      method = {"method_1523"},
      at = {@At("TAIL")}
   )
   private void frogMagentaOverlay(CallbackInfo ci) {
      class_437 screen = this.field_1755;
      if (screen != null) {
         boolean isMainMenu = screen instanceof class_442;
         boolean isLoading = screen instanceof class_435 || screen instanceof class_434 || screen instanceof class_412;
         if (isMainMenu || isLoading) {
            if (!Astra.SHADER.fullNullCheck()) {
               float w = (float)this.field_1704.method_4486();
               float h = (float)this.field_1704.method_4502();
               float t = (float)(System.currentTimeMillis() % 100000L) / 1000.0F;

               try {
                  ManagedShaderEffect gradient = Astra.SHADER.getShader(ShaderManager.Shader.Gradient);
                  gradient.setUniformValue("alpha2", 0.85F);
                  gradient.setUniformValue("rgb", 0.78F, 0.05F, 0.59F);
                  gradient.setUniformValue("rgb1", 0.56F, 0.06F, 0.68F);
                  gradient.setUniformValue("rgb2", 0.93F, 0.12F, 0.63F);
                  gradient.setUniformValue("rgb3", 0.64F, 0.0F, 0.64F);
                  gradient.setUniformValue("step", 180.0F);
                  gradient.setUniformValue("radius", 2.0F);
                  gradient.setUniformValue("quality", 1.0F);
                  gradient.setUniformValue("divider", 150.0F);
                  gradient.setUniformValue("maxSample", 10.0F);
                  gradient.setUniformValue("resolution", w, h);
                  gradient.setUniformValue("time", t * 300.0F);
                  gradient.render(((class_310)this).method_60646().method_60637(true));
               } catch (Exception var27) {
               }

               class_4587 m = new class_4587();
               w = (float)this.field_1704.method_4486();
               h = (float)this.field_1704.method_4502();
               long now = System.currentTimeMillis();
               long elapsed = now - FrogStartTs;
               if (elapsed < 2400L) {
                  float p = Math.max(0.0F, Math.min(1.0F, (float)elapsed / 2400.0F));
                  int aTop = (int)((double)180.0F * Math.sin((double)p * Math.PI));
                  Color c1 = new Color(28, 60, 110, aTop);
                  Color c2 = new Color(190, 50, 160, aTop);
                  Render2DUtil.verticalGradient(m, 0.0F, 0.0F, w, h, c1, c2);
                  float r = Math.min(w, h) * (0.12F + 0.18F * p);
                  Render2DUtil.drawCircle(m, w / 2.0F, h / 2.0F, r, new Color(255, 255, 255, (int)(70.0F * p)), 80);
                  Render2DUtil.drawCircle(m, w / 2.0F, h / 2.0F, r * 1.2F, new Color(120, 220, 255, (int)(50.0F * p)), 80);
               } else {
                  float phase = (float)(now % 6000L) / 6000.0F;
                  float angle = 0.523599F;
                  float dx = (float)Math.tan((double)angle) * h;
                  float base = -h;
                  float spacing = 28.0F;
                  float shift = phase * spacing * 5.2F;
                  int cA1 = (new Color(230, 60, 170, 64)).getRGB();
                  int cA2 = (new Color(160, 40, 130, 48)).getRGB();
                  int nodeC = (new Color(255, 255, 255, 42)).getRGB();

                  for(float i = base; i < w; i += spacing) {
                     float x0 = i + shift;
                     Render2DUtil.drawLine(m, x0, 0.0F, x0 + dx, h, cA1);
                     float nx = x0 + dx * 0.25F;
                     float ny = h * 0.25F;
                     Render2DUtil.drawCircle(m, nx, ny, 2.5F, new Color(nodeC, true), 32);
                  }

                  for(float var31 = base + spacing / 2.0F; var31 < w; var31 += spacing) {
                     float x0 = var31 + shift * 0.85F;
                     Render2DUtil.drawLine(m, x0, 0.0F, x0 + dx, h, cA2);
                     float nx = x0 + dx * 0.65F;
                     float ny = h * 0.6F;
                     Render2DUtil.drawCircle(m, nx, ny, 2.0F, new Color(nodeC, true), 28);
                  }

               }
            }
         }
      }
   }

   @Inject(
      method = {"method_1536"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_239;method_17783()Lnet/minecraft/class_239$class_240;",
   shift = Shift.BEFORE
)}
   )
   public void onAttack(CallbackInfoReturnable<Boolean> cir) {
      Astra.EVENT_BUS.post(DoAttackEvent.getPre());
   }

   @Inject(
      method = {"method_1536"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_746;method_6104(Lnet/minecraft/class_1268;)V",
   shift = Shift.AFTER
)}
   )
   public void onAttackPost(CallbackInfoReturnable<Boolean> cir) {
      if (Astra.EVENT_BUS != null) {
         Astra.EVENT_BUS.post(DoAttackEvent.getPost());
      }

   }

   @Inject(
      method = {"method_1536"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void doAttackHook(CallbackInfoReturnable<Boolean> cir) {
      if (Astra.EVENT_BUS != null) {
         DoAttackEvent event = new DoAttackEvent();
         Astra.EVENT_BUS.post(event);
         if (event.isCancelled()) {
            cir.setReturnValue(false);
         }
      }

   }

   @Inject(
      method = {"method_56134(Lnet/minecraft/class_437;)V"},
      at = {@At("HEAD")}
   )
   private void onDisconnect(class_437 screen, CallbackInfo info) {
      if (Astra.EVENT_BUS != null) {
         Astra.EVENT_BUS.post(GameLeftEvent.INSTANCE);
      }

      if (Astra.MODULE != null) {
         Astra.MODULE.onLogout();
      }

   }

   @Inject(
      method = {"method_56134(Lnet/minecraft/class_437;)V"},
      at = {@At("HEAD")}
   )
   private void clearTitleMixin(class_437 screen, CallbackInfo info) {
      if (ClientSetting.INSTANCE.titleFix.getValue()) {
         this.field_1705.method_34003();
         this.field_1705.method_1742();
      }

   }

   @Inject(
      method = {"method_1590"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
      if (this.field_1771 <= 0 && this.field_1724.method_6115() && InteractTweaks.INSTANCE.multiTask()) {
         if (breaking && this.field_1765 != null && this.field_1765.method_17783() == class_240.field_1332) {
            class_3965 blockHitResult = (class_3965)this.field_1765;
            class_2338 blockPos = blockHitResult.method_17777();
            class_2350 direction;
            if (!this.field_1687.method_8320(blockPos).method_26215() && this.field_1761.method_2902(blockPos, direction = blockHitResult.method_17780())) {
               this.field_1713.method_3054(blockPos, direction);
               this.field_1724.method_6104(class_1268.field_5808);
            }
         } else {
            this.field_1761.method_2925();
         }

         ci.cancel();
      }

   }

   @Inject(
      at = {@At("HEAD")},
      method = {"method_1574()V"}
   )
   public void tickHead(CallbackInfo info) {
      try {
         if (Astra.EVENT_BUS != null) {
            Astra.EVENT_BUS.post(ClientTickEvent.get(Event.Stage.Pre));
         }
      } catch (Exception e) {
         e.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            CommandManager.sendMessage("§4An error has occurred (MinecraftClient.tick() [HEAD]) Message: [" + e.getMessage() + "]");
         }
      }

   }

   @Inject(
      at = {@At("TAIL")},
      method = {"method_1574()V"}
   )
   public void tickTail(CallbackInfo info) {
      try {
         if (Astra.EVENT_BUS != null) {
            Astra.EVENT_BUS.post(ClientTickEvent.get(Event.Stage.Post));
         }
      } catch (Exception e) {
         e.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            CommandManager.sendMessage("§4An error has occurred (MinecraftClient.tick() [TAIL]) Message: [" + e.getMessage() + "]");
         }
      }

   }

   @Overwrite
   private String method_24287() {
      if (ClientSetting.INSTANCE == null) {
         return "Astra Client: Loading..";
      } else if (ClientSetting.INSTANCE.titleOverride.getValue()) {
         return ClientSetting.INSTANCE.windowTitle.getValue();
      } else {
         StringBuilder stringBuilder = new StringBuilder("Astra Client");
         stringBuilder.append(" ");
         stringBuilder.append(class_155.method_16673().method_48019());
         class_634 clientPlayNetworkHandler = this.method_1562();
         if (clientPlayNetworkHandler != null && clientPlayNetworkHandler.method_48296().method_10758()) {
            stringBuilder.append(" - ");
            class_642 serverInfo = this.method_1558();
            if (this.field_1766 != null && !this.field_1766.method_3860()) {
               stringBuilder.append(class_1074.method_4662("title.singleplayer", new Object[0]));
            } else if (serverInfo != null && serverInfo.method_52811()) {
               stringBuilder.append(class_1074.method_4662("title.multiplayer.realms", new Object[0]));
            } else if (this.field_1766 != null || serverInfo != null && serverInfo.method_2994()) {
               stringBuilder.append(class_1074.method_4662("title.multiplayer.lan", new Object[0]));
            } else {
               stringBuilder.append(class_1074.method_4662("title.multiplayer.other", new Object[0]));
            }
         }

         return stringBuilder.toString();
      }
   }

   @Shadow
   public class_634 method_1562() {
      return null;
   }

   @Shadow
   public class_642 method_1558() {
      return null;
   }
}
