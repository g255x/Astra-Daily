package dev.Astra.asm.mixins;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.EntitySpawnEvent;
import dev.Astra.api.events.impl.EntitySpawnedEvent;
import dev.Astra.api.events.impl.RemoveEntityEvent;
import dev.Astra.api.events.impl.TickEntityEvent;
import dev.Astra.mod.modules.impl.render.Ambience;
import dev.Astra.mod.modules.impl.render.NoRender;
import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_2874;
import net.minecraft.class_3695;
import net.minecraft.class_5269;
import net.minecraft.class_5294;
import net.minecraft.class_5321;
import net.minecraft.class_5455;
import net.minecraft.class_638;
import net.minecraft.class_6880;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_638.class})
public abstract class MixinClientWorld extends class_1937 {
   @Unique
   private final class_5294 overworld = new class_5294.class_5297();

   protected MixinClientWorld(class_5269 properties, class_5321<class_1937> registryRef, class_5455 registryManager, class_6880<class_2874> dimensionEntry, Supplier<class_3695> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
      super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
   }

   @Inject(
      method = {"method_18646"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onTickEntity(class_1297 entity, CallbackInfo ci) {
      TickEntityEvent event = TickEntityEvent.get(entity);
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_53875"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onAddEntity(class_1297 entity, CallbackInfo ci) {
      EntitySpawnEvent event = EntitySpawnEvent.get(entity);
      Astra.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"method_2945"},
      at = {@At("HEAD")}
   )
   private void hookRemoveEntity(int entityId, class_1297.class_5529 removalReason, CallbackInfo ci) {
      class_1297 entity = this.method_8469(entityId);
      if (entity != null) {
         RemoveEntityEvent removeEntityEvent = RemoveEntityEvent.get(entity, removalReason);
         Astra.EVENT_BUS.post(removeEntityEvent);
      }
   }

   @Inject(
      method = {"method_53875"},
      at = {@At("TAIL")}
   )
   public void onAddEntityTail(class_1297 entity, CallbackInfo ci) {
      EntitySpawnedEvent event = EntitySpawnedEvent.get(entity);
      Astra.EVENT_BUS.post(event);
   }

   @Inject(
      method = {"method_23777"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetSkyColor(class_243 cameraPos, float tickDelta, CallbackInfoReturnable<class_243> info) {
      if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.sky.booleanValue) {
         Color sky = Ambience.INSTANCE.sky.getValue();
         info.setReturnValue(new class_243((double)sky.getRed() / (double)255.0F, (double)sky.getGreen() / (double)255.0F, (double)sky.getBlue() / (double)255.0F));
      }

   }

   @Inject(
      method = {"method_23785"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hookGetCloudsColor(float tickDelta, CallbackInfoReturnable<class_243> cir) {
      if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.cloud.booleanValue) {
         Color sky = Ambience.INSTANCE.cloud.getValue();
         cir.setReturnValue(new class_243((double)sky.getRed() / (double)255.0F, (double)sky.getGreen() / (double)255.0F, (double)sky.getBlue() / (double)255.0F));
      }

   }

   @Inject(
      method = {"method_28103"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetSkyProperties(CallbackInfoReturnable<class_5294> info) {
      if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.forceOverworld.getValue()) {
         info.setReturnValue(this.overworld);
      }

   }

   public float method_8430(float delta) {
      return NoRender.INSTANCE.isOn() && NoRender.INSTANCE.weather.getValue() ? 0.0F : super.method_8430(delta);
   }

   public float method_8478(float delta) {
      return NoRender.INSTANCE.isOn() && NoRender.INSTANCE.weather.getValue() ? 0.0F : super.method_8478(delta);
   }
}
