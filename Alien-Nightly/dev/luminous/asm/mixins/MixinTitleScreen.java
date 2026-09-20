package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.asm.accessors.IScreen;
import dev.luminous.core.impl.FontManager;
import dev.luminous.core.impl.ShaderManager;
import java.awt.Color;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4068;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_4587;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_442.class})
public class MixinTitleScreen {
   @Inject(
      method = {"method_25394"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void vitalityBackground(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      float w = (float)context.method_51421();
      float h = (float)context.method_51443();
      class_4587 m = context.method_51448();
      long now = System.currentTimeMillis();
      float tNorm = (float)(now % 8000L) / 8000.0F;
      Color blue = new Color(28, 60, 110, 255);
      Color purple = new Color(190, 50, 160, 255);
      float mix = (float)((Math.sin((double)tNorm * Math.PI * (double)2.0F) + (double)1.0F) * (double)0.5F);
      int topR = (int)((float)blue.getRed() * (1.0F - mix) + (float)purple.getRed() * mix);
      int topG = (int)((float)blue.getGreen() * (1.0F - mix) + (float)purple.getGreen() * mix);
      int topB = (int)((float)blue.getBlue() * (1.0F - mix) + (float)purple.getBlue() * mix);
      int botR = (int)((float)purple.getRed() * (1.0F - mix) + (float)blue.getRed() * mix);
      int botG = (int)((float)purple.getGreen() * (1.0F - mix) + (float)blue.getGreen() * mix);
      int botB = (int)((float)purple.getBlue() * (1.0F - mix) + (float)blue.getBlue() * mix);
      Color g1 = new Color(topR, topG, topB, 255);
      Color g2 = new Color(botR, botG, botB, 255);
      Render2DUtil.verticalGradient(m, 0.0F, 0.0F, w, h, g1, g2);
      context.method_25296(0, 0, (int)w, (int)h, g1.getRGB(), g2.getRGB());
      float phase = (float)(now % 7000L) / 7000.0F;
      float angle = 0.523599F;
      float dx = (float)Math.tan((double)angle) * h;
      float base = -h;
      float spacing = 68.0F;
      float shift = phase * spacing * 3.1F;
      Color c1 = new Color(255, 150, 240, 26);
      Color c2 = new Color(120, 220, 255, 20);

      for(float i = base; i < w; i += spacing) {
         float x0 = i + shift;
         Render2DUtil.drawLine(m, x0, 0.0F, x0 + dx, h, c1.getRGB());
      }

      for(float i = base + spacing / 2.0F; i < w; i += spacing) {
         float x0 = i + shift * 0.85F;
         Render2DUtil.drawLine(m, x0, 0.0F, x0 + dx, h, c2.getRGB());
      }

      float pulse = (float)Math.sin((double)((float)(now % 4000L) / 4000.0F) * Math.PI * (double)2.0F) * 0.5F + 0.5F;
      float radius = Math.min(w, h) * (0.12F + 0.08F * pulse);
      Color ring = new Color(255, 255, 255, 30);
      Render2DUtil.drawCircle(m, w / 2.0F, h / 2.0F, radius, ring, 80);
      Render2DUtil.drawCircle(m, w / 2.0F, h / 2.0F, radius * 1.2F, new Color(120, 220, 255, 24), 80);
      int dots = 22;

      for(int i = 0; i < dots; ++i) {
         float a = (float)((Math.PI * 2D) * (double)i / (double)dots + (double)(phase * 2.0F) * Math.PI);
         float rx = (float)Math.cos((double)a) * radius * 1.2F;
         float ry = (float)Math.sin((double)a) * radius * 0.7F;
         float px = w / 2.0F + rx;
         float py = h / 2.0F + ry + (float)Math.sin((double)(a * 2.0F + phase * 4.0F)) * 6.0F;
         Render2DUtil.drawCircle(m, px, py, 1.5F, new Color(255, 255, 255, 38), 30);
      }

      for(int i = 0; i < 12; ++i) {
         float a = (float)((Math.PI * 2D) * (double)i / (double)12.0F + (double)(phase * 3.1F));
         float rx = (float)Math.cos((double)a) * radius * 1.6F;
         float ry = (float)Math.sin((double)a) * radius * 1.0F;
         float px = w / 2.0F + rx;
         float py = h / 2.0F + ry;
         Render2DUtil.drawCircle(m, px, py, 2.4F, new Color(120, 220, 255, 36), 36);
      }

      for(int y = 0; (float)y < h; y += 3) {
         int alpha = 18;
         int c = (new Color(0, 0, 0, alpha)).getRGB();
         Render2DUtil.drawLine(m, 0.0F, (float)y, w, (float)y, c);
      }

      if (!Alien.SHADER.fullNullCheck()) {
         ManagedShaderEffect gradient = Alien.SHADER.getShader(ShaderManager.Shader.Gradient);
         gradient.setUniformValue("alpha2", 0.36F);
         gradient.setUniformValue("rgb", 0.1F, 0.75F, 1.0F);
         gradient.setUniformValue("rgb1", 0.98F, 0.35F, 0.74F);
         gradient.setUniformValue("rgb2", 0.46F, 0.19F, 0.81F);
         gradient.setUniformValue("rgb3", 0.12F, 0.5F, 0.95F);
         gradient.setUniformValue("step", 99.0F);
         gradient.setUniformValue("radius", 1.6F);
         gradient.setUniformValue("quality", 0.8F);
         gradient.setUniformValue("divider", 220.0F);
         gradient.setUniformValue("maxSample", 6.0F);
         gradient.setUniformValue("resolution", w, h);
         float t = (float)(now % 100000L) / 1000.0F;
         gradient.setUniformValue("time", t * 220.0F);
         gradient.render(class_310.method_1551().method_60646().method_60637(true));
      }

      m.method_22903();
      float titleScale = 3.6F;
      m.method_46416(w / 2.0F, h * 0.24F, 0.0F);
      m.method_22905(titleScale, titleScale, 1.0F);
      FontManager.ui.drawCenteredString(m, "Alien", (double)0.0F, (double)0.0F, new Color(230, 255, 255, 255));
      FontManager.ui.drawCenteredString(m, "Alien", (double)0.0F, (double)2.2F, new Color(120, 220, 255, 180));
      FontManager.ui.drawCenteredString(m, "Alien", (double)0.0F, (double)-2.2F, new Color(255, 160, 240, 160));
      m.method_22909();
      class_437 idx = class_310.method_1551().field_1755;
      if (idx instanceof class_442 ts) {
         int idx = 0;

         for(class_4068 d : ((IScreen)IScreen.class.cast(ts)).getDrawables()) {
            if (d instanceof class_4185 bw) {
               if (idx < 3) {
                  int bx = bw.method_46426();
                  int by = bw.method_46427();
                  int bwid = bw.method_25368();
                  int bhei = bw.method_25364();
                  boolean hovered = mouseX >= bx && mouseX <= bx + bwid && mouseY >= by && mouseY <= by + bhei;
                  boolean pressed = hovered && GLFW.glfwGetMouseButton(class_310.method_1551().method_22683().method_4490(), 0) == 1;
                  Color accent = new Color(0, 120, 212, 255);
                  Color neon = new Color(0, 224, 255, 180);
                  Color amber = new Color(255, 210, 0, 160);
                  Render2DUtil.drawRoundedStroke(context.method_51448(), (float)bx, (float)by, (float)bwid, (float)bhei, 5.0F, hovered ? new Color(220, 224, 230, 200) : new Color(220, 224, 230, 160), 64);
                  Render2DUtil.drawLine(context.method_51448(), (float)bx + 2.0F, (float)by + 2.0F, (float)(bx + bwid) - 2.0F, (float)by + 2.0F, (new Color(255, 255, 255, 80)).getRGB());
                  Render2DUtil.drawLine(context.method_51448(), (float)bx + 2.0F, (float)(by + bhei) - 2.2F, (float)(bx + bwid) - 2.0F, (float)(by + bhei) - 2.2F, (new Color(120, 130, 140, 60)).getRGB());
                  Render2DUtil.drawLine(context.method_51448(), (float)bx + 4.0F, (float)by + 4.0F, (float)bx + 18.0F, (float)by + 10.0F, neon.getRGB());
                  Render2DUtil.drawLine(context.method_51448(), (float)(bx + bwid) - 18.0F, (float)(by + bhei) - 6.0F, (float)(bx + bwid) - 6.0F, (float)(by + bhei) - 2.0F, amber.getRGB());
                  if (pressed) {
                     Render2DUtil.drawGlow(context.method_51448(), (float)bx - 2.0F, (float)by - 2.0F, (float)bwid + 4.0F, (float)bhei + 4.0F, (new Color(0, 0, 0, 18)).getRGB());
                     Render2DUtil.verticalGradient(context.method_51448(), (float)bx + 1.5F, (float)by + 1.5F, (float)(bx + bwid) - 1.5F, (float)(by + bhei) - 1.5F, new Color(255, 255, 255, 50), new Color(0, 0, 0, 46));
                     Render2DUtil.drawRoundedStroke(context.method_51448(), (float)bx + 1.0F, (float)by + 1.0F, (float)bwid - 2.0F, (float)bhei - 2.0F, 4.2F, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 160), 96);
                     Render2DUtil.drawRoundedStroke(context.method_51448(), (float)bx + 1.6F, (float)by + 1.6F, (float)bwid - 3.2F, (float)bhei - 3.2F, 3.8F, new Color(255, 255, 255, 140), 96);
                     Render2DUtil.drawRoundedStroke(context.method_51448(), (float)bx + 2.0F, (float)(by + bhei) - 2.8F, (float)bwid - 4.0F, 1.6F, 2.0F, new Color(90, 100, 110, 100), 96);
                     by = (int)((float)by + 1.2F);
                     bx = (int)((float)bx + 0.6F);
                  } else if (hovered) {
                     Render2DUtil.drawRoundedStroke(context.method_51448(), (float)bx - 0.5F, (float)by - 0.5F, (float)bwid + 1.0F, (float)bhei + 1.0F, 5.4F, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 160), 96);
                  }

                  d.method_25394(context, mouseX, mouseY, delta);
               }
            }

            if (d instanceof class_4185) {
               ++idx;
            }

            if (!(d instanceof class_4185)) {
               d.method_25394(context, mouseX, mouseY, delta);
            } else if (idx >= 3) {
               d.method_25394(context, mouseX, mouseY, delta);
            }
         }
      }

      ci.cancel();
   }

   @Inject(
      method = {"method_25426()V"},
      at = {@At("TAIL")}
   )
   private void vitalityLayout(CallbackInfo ci) {
      class_442 self = (class_442)this;
      int w = self.field_22789;
      int h = self.field_22790;
      int btnW = Math.min(300, (int)((double)w * 0.42));
      int btnH = 24;
      int startY = (int)((double)h * (double)0.5F);
      int spacing = 8 + btnH;
      int xLeft = (int)((double)w * 0.26) - btnW / 2;
      int xRight = (int)((double)w * 0.74) - btnW / 2;
      int xCenter = w / 2 - btnW / 2;
      int idx = 0;

      for(class_4068 d : ((IScreen)IScreen.class.cast(self)).getDrawables()) {
         if (d instanceof class_4185 bw) {
            int col = idx % 2;
            int row = idx / 2;
            if (idx == 0) {
               bw.method_25358(btnW);
               bw.method_48229(xLeft, startY);
            } else if (idx == 1) {
               bw.method_25358(btnW);
               bw.method_48229(xRight, startY);
            } else if (idx == 2) {
               bw.method_25358(btnW);
               bw.method_48229(xCenter, startY + spacing);
            } else {
               int x = col == 0 ? xLeft : xRight;
               bw.method_48229(x, startY + spacing * row);
            }

            ++idx;
         }
      }

   }
}
