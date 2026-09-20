package dev.Astra.mod.modules.impl.movement;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.KeyboardInputEvent;
import dev.Astra.api.events.impl.MoveEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.events.impl.TimerEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.player.Freecam;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class HoleSnap extends Module {
   public static HoleSnap INSTANCE;
   public final BooleanSetting any = this.add(new BooleanSetting("AnyHole", true));
   public final SliderSetting timer = this.add(new SliderSetting("Timer", (double)1.0F, 0.1, (double)8.0F, 0.1));
   public final BooleanSetting up = this.add(new BooleanSetting("Up", true));
   public final BooleanSetting grim = this.add(new BooleanSetting("Grim", false));
   public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
   public final SliderSetting circleSize = this.add(new SliderSetting("CircleSize", (double)1.0F, (double)0.1F, (double)2.5F));
   public final BooleanSetting fade = this.add(new BooleanSetting("Fade", true));
   public final SliderSetting segments = this.add(new SliderSetting("Segments", 180, 0, 360));
   private final SliderSetting range = this.add(new SliderSetting("Range", 5, 1, 50));
   private final SliderSetting timeoutTicks = this.add(new SliderSetting("TimeOut", 40, 0, 100));
   private final SliderSetting steps;
   private final SliderSetting priority;
   boolean resetMove;
   boolean applyTimer;
   class_243 targetPos;
   private class_2338 holePos;
   private int stuckTicks;
   private int enabledTicks;

   public HoleSnap() {
      super("HoleSnap", "HoleSnap", Module.Category.Movement);
      BooleanSetting var10009 = this.grim;
      Objects.requireNonNull(var10009);
      this.steps = this.add(new SliderSetting("Steps", 0.8, (double)0.0F, (double)1.0F, 0.01, var10009::getValue));
      BooleanSetting var10008 = this.grim;
      Objects.requireNonNull(var10008);
      this.priority = this.add(new SliderSetting("Priority", 10, 0, 100, var10008::getValue));
      this.resetMove = false;
      this.applyTimer = false;
      this.setChinese("拉坑");
      INSTANCE = this;
   }

   public static class_241 getRotationTo(class_243 posFrom, class_243 posTo) {
      class_243 vec3d = posTo.method_1020(posFrom);
      return getRotationFromVec(vec3d);
   }

   public static void drawCircle(class_4587 matrixStack, Color color, double circleSize, class_243 pos, int segments) {
      class_243 camPos = mc.method_31975().field_4344.method_19326();
      RenderSystem.disableDepthTest();
      Matrix4f matrix = matrixStack.method_23760().method_23761();
      RenderSystem.setShader(class_757::method_34540);
      float keyCodec = (float)color.getAlpha() / 255.0F;
      float r = (float)color.getRed() / 255.0F;
      float g = (float)color.getGreen() / 255.0F;
      float elementCodec = (float)color.getBlue() / 255.0F;
      class_287 bufferBuilder = class_289.method_1348().method_60827(class_5596.field_27381, class_290.field_1576);

      for(double i = (double)0.0F; i < (double)360.0F; i += (double)360.0F / (double)segments) {
         double x = Math.sin(Math.toRadians(i)) * circleSize;
         double z = Math.cos(Math.toRadians(i)) * circleSize;
         class_243 tempPos = (new class_243(pos.field_1352 + x, pos.field_1351, pos.field_1350 + z)).method_1031(-camPos.field_1352, -camPos.field_1351, -camPos.field_1350);
         bufferBuilder.method_22918(matrix, (float)tempPos.field_1352, (float)tempPos.field_1351, (float)tempPos.field_1350).method_22915(r, g, elementCodec, keyCodec);
      }

      class_286.method_43433(bufferBuilder.method_60800());
      RenderSystem.enableDepthTest();
   }

   private static class_241 getRotationFromVec(class_243 vec) {
      double d = vec.field_1352;
      double d2 = vec.field_1350;
      double xz = Math.hypot(d, d2);
      d2 = vec.field_1350;
      double d3 = vec.field_1352;
      double yaw = normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - (double)90.0F);
      double pitch = normalizeAngle(Math.toDegrees(-Math.atan2(vec.field_1351, xz)));
      return new class_241((float)yaw, (float)pitch);
   }

   private static double normalizeAngle(double angleIn) {
      double angle = angleIn % (double)360.0F;
      if (angle >= (double)180.0F) {
         angle -= (double)360.0F;
      }

      if (angle < (double)-180.0F) {
         angle += (double)360.0F;
      }

      return angle;
   }

   @EventListener(
      priority = -99
   )
   public void onTimer(TimerEvent event) {
      if (this.applyTimer) {
         event.set(this.timer.getValueFloat());
      }

   }

   public void onEnable() {
      this.applyTimer = false;
      if (nullCheck()) {
         this.disable();
      } else {
         this.resetMove = false;
         this.holePos = Astra.HOLE.getHole((float)this.range.getValue(), true, this.any.getValue(), this.up.getValue());
      }
   }

   public void onDisable() {
      this.holePos = null;
      this.stuckTicks = 0;
      this.enabledTicks = 0;
      if (!nullCheck()) {
         if (this.resetMove && !this.grim.getValue()) {
            MovementUtil.setMotionX((double)0.0F);
            MovementUtil.setMotionZ((double)0.0F);
         }

      }
   }

   @EventListener
   public void onReceivePacket(PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2708) {
         this.disable();
      }

   }

   @EventListener(
      priority = -999
   )
   public void onKeyInput(KeyboardInputEvent e) {
      if (this.grim.getValue()) {
         if (!AntiCheat.INSTANCE.movementSync()) {
            this.sendMessage("§4HoleSnap require MovementSync.");
            this.disable();
         } else if (!mc.field_1724.method_3144() && !Freecam.INSTANCE.isOn()) {
            mc.field_1724.field_3913.field_3907 = 0.0F;
            mc.field_1724.field_3913.field_3905 = 1.0F;
         }
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.holePos = Astra.HOLE.getHole((float)this.range.getValue(), true, this.any.getValue(), this.up.getValue());
      if (this.holePos == null) {
         this.disable();
      } else {
         ++this.enabledTicks;
         if ((double)this.enabledTicks > this.timeoutTicks.getValue() - (double)1.0F) {
            this.disable();
         } else {
            this.applyTimer = true;
            if (this.grim.getValue()) {
               if (mc.field_1724.method_5805() && !mc.field_1724.method_6128()) {
                  if (this.stuckTicks > 8) {
                     this.disable();
                  } else if (this.holePos == null) {
                     this.disable();
                  } else {
                     class_243 playerPos = mc.field_1724.method_19538();
                     this.targetPos = new class_243((double)this.holePos.method_10263() + (double)0.5F, mc.field_1724.method_23318(), (double)this.holePos.method_10260() + (double)0.5F);
                     class_2350 facing;
                     if (Astra.HOLE.isDoubleHole(this.holePos) && (facing = Astra.HOLE.is3Block(this.holePos)) != null) {
                        this.targetPos = this.targetPos.method_1019(new class_243((double)facing.method_10163().method_10263() * (double)0.5F, (double)facing.method_10163().method_10264() * (double)0.5F, (double)facing.method_10163().method_10260() * (double)0.5F));
                     }

                     this.applyTimer = true;
                     this.resetMove = true;
                     float rotation = getRotationTo(playerPos, this.targetPos).field_1343;
                     float yawRad = rotation / 180.0F * (float)Math.PI;
                     double dist = playerPos.method_1022(this.targetPos);
                     double cappedSpeed = Math.min(0.2873, dist);
                     double x = (double)(-((float)Math.sin((double)yawRad))) * cappedSpeed;
                     double z = (double)((float)Math.cos((double)yawRad)) * cappedSpeed;
                     if (Math.abs(x) < (double)0.25F && Math.abs(z) < (double)0.25F && playerPos.field_1351 <= (double)this.holePos.method_10264() + 0.8) {
                        this.disable();
                     } else {
                        this.stuckTicks = mc.field_1724.field_5976 ? ++this.stuckTicks : 0;
                     }
                  }
               } else {
                  this.disable();
               }
            }
         }
      }
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.grim.getValue() && this.holePos != null) {
         this.targetPos = new class_243((double)this.holePos.method_10263() + (double)0.5F, mc.field_1724.method_23318(), (double)this.holePos.method_10260() + (double)0.5F);
         class_2350 facing;
         if (Astra.HOLE.isDoubleHole(this.holePos) && (facing = Astra.HOLE.is3Block(this.holePos)) != null) {
            this.targetPos = this.targetPos.method_1019(new class_243((double)facing.method_10163().method_10263() * (double)0.5F, (double)facing.method_10163().method_10264() * (double)0.5F, (double)facing.method_10163().method_10260() * (double)0.5F));
         }

         event.setTarget(this.targetPos, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   @EventListener
   public void onMove(MoveEvent event) {
      if (!this.grim.getValue()) {
         if (mc.field_1724.method_5805() && !mc.field_1724.method_6128()) {
            if (this.stuckTicks > 8) {
               this.disable();
            } else if (this.holePos == null) {
               this.disable();
            } else {
               class_243 playerPos = mc.field_1724.method_19538();
               this.targetPos = new class_243((double)this.holePos.method_10263() + (double)0.5F, mc.field_1724.method_23318(), (double)this.holePos.method_10260() + (double)0.5F);
               class_2350 facing;
               if (Astra.HOLE.isDoubleHole(this.holePos) && (facing = Astra.HOLE.is3Block(this.holePos)) != null) {
                  this.targetPos = this.targetPos.method_1019(new class_243((double)facing.method_10163().method_10263() * (double)0.5F, (double)facing.method_10163().method_10264() * (double)0.5F, (double)facing.method_10163().method_10260() * (double)0.5F));
               }

               this.applyTimer = true;
               this.resetMove = true;
               float rotation = getRotationTo(playerPos, this.targetPos).field_1343;
               float yawRad = rotation / 180.0F * (float)Math.PI;
               double dist = playerPos.method_1022(this.targetPos);
               double cappedSpeed = Math.min(0.2873, dist);
               double x = (double)(-((float)Math.sin((double)yawRad))) * cappedSpeed;
               double z = (double)((float)Math.cos((double)yawRad)) * cappedSpeed;
               event.setX(x);
               event.setZ(z);
               if (Math.abs(x) < 0.1 && Math.abs(z) < 0.1 && playerPos.field_1351 <= (double)this.holePos.method_10264() + (double)0.5F) {
                  this.disable();
               }

               this.stuckTicks = mc.field_1724.field_5976 ? ++this.stuckTicks : 0;
            }
         } else {
            this.disable();
         }
      }
   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.targetPos != null && this.holePos != null) {
         GL11.glEnable(3042);
         Color color = this.color.getValue();
         class_243 pos = new class_243(this.targetPos.field_1352, (double)this.holePos.method_10264(), this.targetPos.method_10215());
         if (this.fade.getValue()) {
            double temp = 0.01;

            for(double i = (double)0.0F; i < this.circleSize.getValue(); i += temp) {
               drawCircle(matrixStack, ColorUtil.injectAlpha(color, (int)Math.min((double)(color.getAlpha() * 2) / (this.circleSize.getValue() / temp), (double)255.0F)), i, pos, this.segments.getValueInt());
            }
         } else {
            drawCircle(matrixStack, color, this.circleSize.getValue(), pos, this.segments.getValueInt());
         }

         GL11.glDisable(3042);
      }
   }
}
