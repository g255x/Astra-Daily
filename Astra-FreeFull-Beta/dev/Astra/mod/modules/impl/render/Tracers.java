package dev.Astra.mod.modules.impl.render;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.Render3DEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1429;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import org.lwjgl.opengl.GL32C;

public class Tracers extends Module {
   public static Tracers INSTANCE;
   public final EnumSetting<Point> point;
   public final SliderSetting range;
   public final SliderSetting width;
   public final ColorSetting color;
   public final BooleanSetting fade;
   public final BooleanSetting box;
   public final BooleanSetting crystal;
   public final BooleanSetting players;
   public final BooleanSetting mobs;
   public final BooleanSetting animals;

   public Tracers() {
      super("Tracers", Module.Category.Render);
      this.point = this.add(new EnumSetting("Point", Tracers.Point.Feet));
      this.range = this.add(new SliderSetting("Range", (double)200.0F, (double)0.0F, (double)500.0F, 0.1));
      this.width = this.add(new SliderSetting("Width", (double)1.5F, (double)0.5F, (double)5.0F, 0.1, () -> this.fade.getValue()));
      this.color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 255)));
      this.fade = this.add(new BooleanSetting("Fade", true));
      this.box = this.add(new BooleanSetting("Box", false));
      this.crystal = this.add(new BooleanSetting("Crystal", true));
      this.players = this.add(new BooleanSetting("Players", true));
      this.mobs = this.add(new BooleanSetting("Mobs", true));
      this.animals = this.add(new BooleanSetting("Animals", true));
      this.setChinese("追踪线");
      INSTANCE = this;
   }

   @EventListener
   public void onRender3D(Render3DEvent event) {
      if (!nullCheck()) {
         class_4184 camera = Wrapper.mc.field_1773.method_19418();
         class_243 crosshairPos = this.getCrosshairPos(camera);
         GL32C.glLineWidth(this.width.getValueFloat());

         for(class_1297 entity : Wrapper.mc.field_1687.method_18112()) {
            if (this.shouldRender(entity)) {
               double dist = camera.method_19326().method_1022(entity.method_19538());
               if (!(dist > this.range.getValue())) {
                  class_243 target = this.getTargetPos(entity);
                  Color lineColor = this.color.getValue();
                  if (this.fade.getValue()) {
                     double minDist = (double)0.0F;
                     double maxDist = this.range.getValue();
                     double t = MathUtil.clamp((dist - minDist) / (maxDist - minDist), (double)0.0F, (double)1.0F);
                     float alpha = (float)(((double)1.0F - t) * (double)lineColor.getAlpha());
                     lineColor = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)alpha);
                  }

                  Render3DUtil.drawLine(crosshairPos, target, lineColor);
                  if (this.box.getValue()) {
                     class_243 eyes = entity.method_30950(Wrapper.mc.method_60646().method_60637(true));
                     float eyeH = entity.method_18381(entity.method_18376());
                     class_243 top = eyes.method_1031((double)0.0F, (double)eyeH, (double)0.0F);
                     Render3DUtil.drawLine(eyes, top, lineColor);
                  }
               }
            }
         }

         GL32C.glLineWidth(1.0F);
      }
   }

   private boolean shouldRender(class_1297 entity) {
      if (entity == Wrapper.mc.field_1724) {
         return false;
      } else if (entity instanceof class_1511) {
         return this.crystal.getValue();
      } else if (entity instanceof class_1657) {
         return this.players.getValue();
      } else if (entity instanceof class_1308) {
         return this.mobs.getValue();
      } else {
         return entity instanceof class_1429 ? this.animals.getValue() : false;
      }
   }

   private class_243 getTargetPos(class_1297 entity) {
      class_243 pos = entity.method_30950(Wrapper.mc.method_60646().method_60637(true));
      switch (((Point)this.point.getValue()).ordinal()) {
         case 0:
            return pos;
         case 1:
            float eyeH = entity.method_18381(entity.method_18376());
            return pos.method_1031((double)0.0F, (double)eyeH, (double)0.0F);
         case 2:
            float eyeH = entity.method_18381(entity.method_18376());
            class_243 eyes = pos.method_1031((double)0.0F, (double)eyeH, (double)0.0F);
            return pos.method_1019(eyes).method_1021((double)0.5F);
         default:
            return pos.method_1031((double)0.0F, (double)entity.method_18381(entity.method_18376()), (double)0.0F);
      }
   }

   private class_243 getCrosshairPos(class_4184 camera) {
      class_243 camPos = camera.method_19326();
      float pitchRad = (float)Math.toRadians((double)camera.method_19329());
      float yawRad = (float)Math.toRadians((double)camera.method_19330());
      double x = -Math.sin((double)yawRad) * Math.cos((double)pitchRad);
      double y = -Math.sin((double)pitchRad);
      double z = Math.cos((double)yawRad) * Math.cos((double)pitchRad);
      return camPos.method_1031(x, y, z);
   }

   public static enum Point {
      Feet,
      Hand,
      Torso;

      // $FF: synthetic method
      private static Point[] $values() {
         return new Point[]{Feet, Hand, Torso};
      }
   }
}
