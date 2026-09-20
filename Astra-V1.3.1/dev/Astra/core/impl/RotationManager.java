package dev.Astra.core.impl;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.DoAttackEvent;
import dev.Astra.api.events.impl.FireworkShooterRotationEvent;
import dev.Astra.api.events.impl.InteractBlockEvent;
import dev.Astra.api.events.impl.InteractItemEvent;
import dev.Astra.api.events.impl.JumpEvent;
import dev.Astra.api.events.impl.KeyboardInputEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.events.impl.SendMovementPacketsEvent;
import dev.Astra.api.events.impl.TickEvent;
import dev.Astra.api.events.impl.TickMovementEvent;
import dev.Astra.api.events.impl.TravelEvent;
import dev.Astra.api.events.impl.UpdateRotateEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.path.BaritoneUtil;
import dev.Astra.asm.accessors.IClientPlayerEntity;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.movement.HoleSnap;
import dev.Astra.mod.modules.impl.player.Freecam;
import dev.Astra.mod.modules.settings.enums.SnapBack;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3532;

public class RotationManager implements Wrapper {
   public static final Timer ROTATE_TIMER = new Timer();
   public static class_243 directionVec = null;
   public static boolean snapBack = false;
   private static float renderPitch;
   private static float renderYawOffset;
   private static float prevRenderPitch;
   private static float prevRenderYawOffset;
   private static float prevRotationYawHead;
   private static float rotationYawHead;
   public float nextYaw;
   public float nextPitch;
   public float rotationYaw;
   public float rotationPitch;
   public float lastYaw;
   public float lastPitch;
   public class_243 crossHairUpdatePos;
   private int ticksExisted;
   public static float fixYaw;
   public static float fixPitch;
   private float prevYaw;
   private float prevPitch;

   public RotationManager() {
      Astra.EVENT_BUS.subscribe(this);
   }

   @EventListener
   public void onInteract(InteractItemEvent event) {
      if (AntiCheat.INSTANCE.interactRotation.getValue() && mc.field_1724 != null) {
         if (event.isPre()) {
            this.snapAt(mc.field_1724.method_36454(), mc.field_1724.method_36455());
         } else {
            this.snapBack();
         }
      }

   }

   @EventListener
   public void onInteract(InteractBlockEvent event) {
      if (AntiCheat.INSTANCE.interactRotation.getValue() && mc.field_1724 != null) {
         if (event.isPre()) {
            this.snapAt(mc.field_1724.method_36454(), mc.field_1724.method_36455());
         } else {
            this.snapBack();
         }
      }

   }

   @EventListener
   public void doAttack(DoAttackEvent event) {
      if (AntiCheat.INSTANCE.interactRotation.getValue() && mc.field_1724 != null) {
         if (event.isPre()) {
            this.snapAt(mc.field_1724.method_36454(), mc.field_1724.method_36455());
         } else {
            this.snapBack();
         }
      }

   }

   public void snapBack() {
      if (AntiCheat.INSTANCE.snapBackEnum.is(SnapBack.Force)) {
         mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), this.rotationYaw, this.rotationPitch, mc.field_1724.method_24828()));
      } else if (AntiCheat.INSTANCE.snapBackEnum.is(SnapBack.Tick)) {
         snapBack = true;
      }

   }

   public void lookAt(class_243 directionVec) {
      this.rotationTo(directionVec);
      this.snapAt(directionVec);
   }

   public void lookAt(class_2338 pos, class_2350 side) {
      class_243 hitVec = pos.method_46558().method_1019(new class_243((double)side.method_10163().method_10263() * (double)0.5F, (double)side.method_10163().method_10264() * (double)0.5F, (double)side.method_10163().method_10260() * (double)0.5F));
      this.lookAt(hitVec);
   }

   public void snapAt(float yaw, float pitch) {
      this.setRenderRotation(yaw, pitch, true);
      if (AntiCheat.INSTANCE.grimRotation.getValue()) {
         mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), yaw, pitch, mc.field_1724.method_24828()));
      } else {
         mc.method_1562().method_52787(new class_2828.class_2831(yaw, pitch, mc.field_1724.method_24828()));
      }

   }

   public void snapAt(class_243 directionVec) {
      float[] angle = getRotation(directionVec);
      this.snapAt(angle[0], angle[1]);
   }

   public void rotationTo(class_243 vec3d) {
      ROTATE_TIMER.reset();
      directionVec = vec3d;
   }

   public boolean inFov(class_243 directionVec, float fov) {
      float[] angle = getRotation(this.crossHairUpdatePos != null ? this.crossHairUpdatePos : new class_243(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()), mc.field_1724.method_23321()), directionVec);
      return this.inFov(angle[0], angle[1], fov);
   }

   public boolean inFov(float yaw, float pitch, float fov) {
      float yawDifferent = class_3532.method_15356(yaw, this.rotationYaw);
      float pitchDifferent;
      return yawDifferent * yawDifferent + (pitchDifferent = Math.abs(pitch - this.rotationPitch)) * pitchDifferent <= fov * fov;
   }

   @EventListener
   public void onTickMovement(TickMovementEvent event) {
      if (mc.field_1724 != null) {
         this.crossHairUpdatePos = new class_243(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)mc.field_1724.method_18381(mc.field_1724.method_18376()), mc.field_1724.method_23321());
      }
   }

   @EventListener
   public void update(SendMovementPacketsEvent event) {
      if (AntiCheat.INSTANCE.movementSync() && !BaritoneUtil.isActive()) {
         event.setYaw(this.nextYaw);
         event.setPitch(this.nextPitch);
      } else {
         UpdateRotateEvent updateRotateEvent = UpdateRotateEvent.get(event.getYaw(), event.getPitch());
         Astra.EVENT_BUS.post(updateRotateEvent);
         event.setYaw(updateRotateEvent.getYaw());
         event.setPitch(updateRotateEvent.getPitch());
      }

   }

   @EventListener(
      priority = 999
   )
   public void update(TickMovementEvent event) {
      if (mc.field_1724 != null) {
         if (AntiCheat.INSTANCE.movementSync() && !BaritoneUtil.isActive()) {
            UpdateRotateEvent updateRotateEvent = UpdateRotateEvent.get(mc.field_1724.method_36454(), mc.field_1724.method_36455());
            Astra.EVENT_BUS.post(updateRotateEvent);
            this.nextYaw = updateRotateEvent.getYaw();
            this.nextPitch = updateRotateEvent.getPitch();
            fixYaw = this.nextYaw;
            fixPitch = this.nextPitch;
         }

      }
   }

   @EventListener(
      priority = -200
   )
   public void onLastRotation(UpdateRotateEvent event) {
      RotationEvent rotationEvent = RotationEvent.get();
      Astra.EVENT_BUS.post(rotationEvent);
      if (rotationEvent.getRotation()) {
         float[] newAngle = this.injectStep(new float[]{rotationEvent.getYaw(), rotationEvent.getPitch()}, rotationEvent.getSpeed());
         event.setYaw(newAngle[0]);
         event.setPitch(newAngle[1]);
      } else if (rotationEvent.getTarget() != null) {
         float[] newAngle = this.injectStep(rotationEvent.getTarget(), rotationEvent.getSpeed());
         event.setYaw(newAngle[0]);
         event.setPitch(newAngle[1]);
      } else if (!event.isModified() && AntiCheat.INSTANCE.look.getValue() && directionVec != null && !ROTATE_TIMER.passed((long)(AntiCheat.INSTANCE.rotateTime.getValue() * (double)1000.0F))) {
         float[] newAngle = this.injectStep(directionVec, AntiCheat.INSTANCE.steps.getValueFloat());
         event.setYaw(newAngle[0]);
         event.setPitch(newAngle[1]);
      }

   }

   @EventListener
   public void travel(TravelEvent e) {
      if (AntiCheat.INSTANCE.movementSync()) {
         if (!BaritoneUtil.isActive()) {
            if (!mc.field_1724.method_3144()) {
               if (e.isPre()) {
                  this.prevYaw = mc.field_1724.method_36454();
                  this.prevPitch = mc.field_1724.method_36455();
                  mc.field_1724.method_36456(fixYaw);
                  mc.field_1724.method_36457(fixPitch);
               } else {
                  mc.field_1724.method_36456(this.prevYaw);
                  mc.field_1724.method_36457(this.prevPitch);
               }

            }
         }
      }
   }

   @EventListener
   public void onJump(JumpEvent e) {
      if (AntiCheat.INSTANCE.movementSync()) {
         if (!BaritoneUtil.isActive()) {
            if (!mc.field_1724.method_3144()) {
               if (e.isPre()) {
                  this.prevYaw = mc.field_1724.method_36454();
                  this.prevPitch = mc.field_1724.method_36455();
                  mc.field_1724.method_36456(fixYaw);
                  mc.field_1724.method_36457(fixPitch);
               } else {
                  mc.field_1724.method_36456(this.prevYaw);
                  mc.field_1724.method_36457(this.prevPitch);
               }

            }
         }
      }
   }

   @EventListener
   public void onFirework(FireworkShooterRotationEvent event) {
      if (AntiCheat.INSTANCE.movementSync()) {
         if (!BaritoneUtil.isActive()) {
            if (event.shooter == mc.field_1724) {
               event.yaw = fixYaw;
               event.pitch = fixPitch;
               event.cancel();
            }

         }
      }
   }

   @EventListener(
      priority = -999
   )
   public void onKeyInput(KeyboardInputEvent e) {
      if (AntiCheat.INSTANCE.movementSync()) {
         if (!BaritoneUtil.isActive()) {
            if (!HoleSnap.INSTANCE.isOn()) {
               if (!mc.field_1724.method_3144() && !Freecam.INSTANCE.isOn()) {
                  float mF = mc.field_1724.field_3913.field_3905;
                  float mS = mc.field_1724.field_3913.field_3907;
                  float delta = (mc.field_1724.method_36454() - fixYaw) * ((float)Math.PI / 180F);
                  float cos = class_3532.method_15362(delta);
                  float sin = class_3532.method_15374(delta);
                  mc.field_1724.field_3913.field_3907 = (float)Math.round(mS * cos - mF * sin);
                  mc.field_1724.field_3913.field_3905 = (float)Math.round(mF * cos + mS * sin);
               }
            }
         }
      }
   }

   public float[] injectStep(class_243 vec, float steps) {
      float currentYaw = AntiCheat.INSTANCE.serverSide.getValue() ? this.getLastYaw() : this.rotationYaw;
      float currentPitch = AntiCheat.INSTANCE.serverSide.getValue() ? this.getLastPitch() : this.rotationPitch;
      float yawDelta = class_3532.method_15393((float)class_3532.method_15338(Math.toDegrees(Math.atan2(vec.field_1350 - mc.field_1724.method_23321(), vec.field_1352 - mc.field_1724.method_23317())) - (double)90.0F) - currentYaw);
      float pitchDelta = (float)(-Math.toDegrees(Math.atan2(vec.field_1351 - (mc.field_1724.method_19538().field_1351 + (double)mc.field_1724.method_18381(mc.field_1724.method_18376())), Math.sqrt(Math.pow(vec.field_1352 - mc.field_1724.method_23317(), (double)2.0F) + Math.pow(vec.field_1350 - mc.field_1724.method_23321(), (double)2.0F))))) - currentPitch;
      if (AntiCheat.INSTANCE.random.getValue()) {
         float angleToRad = (float)Math.toRadians((double)(27 * (mc.field_1724.field_6012 % 30)));
         yawDelta = (float)((double)yawDelta + Math.sin((double)angleToRad) * (double)3.0F) + MathUtil.random(-1.0F, 1.0F);
         pitchDelta += MathUtil.random(-0.6F, 0.6F);
      }

      if (yawDelta > 180.0F) {
         yawDelta -= 180.0F;
      }

      float yawStepVal = 180.0F * steps;
      float clampedYawDelta = class_3532.method_15363(class_3532.method_15379(yawDelta), -yawStepVal, yawStepVal);
      float clampedPitchDelta = class_3532.method_15363(pitchDelta, -45.0F, 45.0F);
      float newYaw = currentYaw + (yawDelta > 0.0F ? clampedYawDelta : -clampedYawDelta);
      float newPitch = class_3532.method_15363(currentPitch + clampedPitchDelta, -90.0F, 90.0F);
      return new float[]{newYaw, newPitch};
   }

   public float[] injectStep(float[] angle, float steps) {
      float currentYaw = AntiCheat.INSTANCE.serverSide.getValue() ? this.getLastYaw() : this.rotationYaw;
      float currentPitch = AntiCheat.INSTANCE.serverSide.getValue() ? this.getLastPitch() : this.rotationPitch;
      float yawDelta = class_3532.method_15393(angle[0] - currentYaw);
      float pitchDelta = angle[1] - currentPitch;
      if (AntiCheat.INSTANCE.random.getValue()) {
         float angleToRad = (float)Math.toRadians((double)(27 * (mc.field_1724.field_6012 % 30)));
         yawDelta = (float)((double)yawDelta + Math.sin((double)angleToRad) * (double)3.0F) + MathUtil.random(-1.0F, 1.0F);
         pitchDelta += MathUtil.random(-0.6F, 0.6F);
      }

      if (yawDelta > 180.0F) {
         yawDelta -= 180.0F;
      }

      float yawStepVal = 180.0F * steps;
      float pitchStepVal = 90.0F * steps;
      float clampedYawDelta = class_3532.method_15363(class_3532.method_15379(yawDelta), -yawStepVal, yawStepVal);
      float clampedPitchDelta = class_3532.method_15363(pitchDelta, -pitchStepVal, pitchStepVal);
      float newYaw = currentYaw + (yawDelta > 0.0F ? clampedYawDelta : -clampedYawDelta);
      float newPitch = class_3532.method_15363(currentPitch + clampedPitchDelta, -90.0F, 90.0F);
      return new float[]{newYaw, newPitch};
   }

   @EventListener(
      priority = -999
   )
   public void onPacketSend(PacketEvent.Sent event) {
      if (mc.field_1724 != null) {
         class_2596<?> packet2 = event.getPacket();
         class_2828 packet;
         if (packet2 instanceof class_2828 && (packet = (class_2828)packet2).method_36172()) {
            this.setLastYaw(packet.method_12271(this.getLastYaw()));
            this.setLastPitch(packet.method_12270(this.getLastPitch()));
            this.setRenderRotation(this.getLastYaw(), this.getLastPitch(), ClientSetting.INSTANCE.sync.getValue());
         }

      }
   }

   @EventListener
   public void onUpdateWalkingPost(TickEvent event) {
      if (event.isPost()) {
         this.setRenderRotation(this.getLastYaw(), this.getLastPitch(), false);
      }

   }

   public void setRenderRotation(float yaw, float pitch, boolean force) {
      if (mc.field_1724 != null) {
         if (mc.field_1724.field_6012 != this.ticksExisted || force) {
            this.ticksExisted = mc.field_1724.field_6012;
            prevRenderPitch = renderPitch;
            prevRenderYawOffset = renderYawOffset;
            renderYawOffset = this.getRenderYawOffset(yaw, prevRenderYawOffset);
            prevRotationYawHead = rotationYawHead;
            rotationYawHead = yaw;
            renderPitch = pitch;
         }
      }
   }

   private float getRenderYawOffset(float yaw, float offsetIn) {
      float result = offsetIn;
      double xDif = mc.field_1724.method_23317() - mc.field_1724.field_6014;
      double zDif;
      if (xDif * xDif + (zDif = mc.field_1724.method_23321() - mc.field_1724.field_5969) * zDif > (double)0.0025000002F) {
         float offset = (float)class_3532.method_15349(zDif, xDif) * (180F / (float)Math.PI) - 90.0F;
         float wrap = class_3532.method_15379(class_3532.method_15393(yaw) - offset);
         result = 95.0F < wrap && wrap < 265.0F ? offset - 180.0F : offset;
      }

      if (mc.field_1724.field_6251 > 0.0F) {
         result = yaw;
      }

      float offset;
      if ((offset = class_3532.method_15393(yaw - (offsetIn + class_3532.method_15393(result - offsetIn) * 0.3F))) < -75.0F) {
         offset = -75.0F;
      } else if (offset >= 75.0F) {
         offset = 75.0F;
      }

      result = yaw - offset;
      if (offset * offset > 2500.0F) {
         result += offset * 0.2F;
      }

      return result;
   }

   public static float[] getRotation(class_243 eyesPos, class_243 vec) {
      double diffX = vec.field_1352 - eyesPos.field_1352;
      double diffY = vec.field_1351 - eyesPos.field_1351;
      double diffZ = vec.field_1350 - eyesPos.field_1350;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{class_3532.method_15393(yaw), class_3532.method_15393(pitch)};
   }

   public static float[] getRotation(class_243 vec) {
      class_243 eyesPos = mc.field_1724.method_33571();
      return getRotation(eyesPos, vec);
   }

   public static float getRenderPitch() {
      return renderPitch;
   }

   public static float getRotationYawHead() {
      return rotationYawHead;
   }

   public static float getRenderYawOffset() {
      return renderYawOffset;
   }

   public static float getPrevRenderPitch() {
      return prevRenderPitch;
   }

   public static float getPrevRotationYawHead() {
      return prevRotationYawHead;
   }

   public static float getPrevRenderYawOffset() {
      return prevRenderYawOffset;
   }

   public float getLastYaw() {
      return this.lastYaw;
   }

   public void setLastYaw(float lastYaw) {
      this.lastYaw = lastYaw;
      if (AntiCheat.INSTANCE.forceSync.getValue() && Astra.SERVER.playerNull.passedS(0.15)) {
         ((IClientPlayerEntity)mc.field_1724).setLastYaw(lastYaw);
      }

   }

   public float getLastPitch() {
      return this.lastPitch;
   }

   public void setLastPitch(float lastPitch) {
      this.lastPitch = lastPitch;
      if (AntiCheat.INSTANCE.forceSync.getValue() && Astra.SERVER.playerNull.passedS(0.15)) {
         ((IClientPlayerEntity)mc.field_1724).setLastPitch(lastPitch);
      }

   }
}
