package dev.Astra.mod.modules.impl.render;

import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.math.FadeUtils;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.render.ColorUtil;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.HashMap;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_4587;

public class PlaceRender extends Module {
   public static final HashMap<class_2338, PlacePos> renderMap = new HashMap();
   public static PlaceRender INSTANCE;
   public final SliderSetting fadeTime = this.add((new SliderSetting("FadeTime", 400, 0, 3000)).setSuffix("ms"));
   public final SliderSetting timeout = this.add((new SliderSetting("TimeOut", 400, 0, 3000)).setSuffix("ms"));
   private final ColorSetting box = this.add((new ColorSetting("Box", new Color(-1738255906, true))).injectBoolean(true));
   private final ColorSetting fill = this.add((new ColorSetting("Fill", new Color(1046761950, true))).injectBoolean(true));
   private final ColorSetting tryPlaceBox = this.add((new ColorSetting("TryPlaceBox", new Color(16777215, true))).injectBoolean(false));
   private final ColorSetting tryPlaceFill = this.add((new ColorSetting("TryPlaceFill", new Color(1627389951, true))).injectBoolean(false));
   private final BooleanSetting noFail = this.add(new BooleanSetting("NoFail", true));
   private final EnumSetting<Easing> ease;
   private final EnumSetting<Mode> mode;

   public PlaceRender() {
      super("PlaceRender", Module.Category.Render);
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut));
      this.mode = this.add(new EnumSetting("Mode", PlaceRender.Mode.Fade));
      this.setChinese("放置显示");
      this.enable();
      INSTANCE = this;
   }

   public void onRender3D(class_4587 matrixStack) {
      renderMap.values().removeIf((v) -> v.draw(matrixStack));
   }

   public void create(class_2338 pos) {
      renderMap.put(pos, new PlacePos(pos));
   }

   private static enum Mode {
      Fade,
      Shrink,
      All;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Fade, Shrink, All};
      }
   }

   public class PlacePos {
      public final FadeUtils fade;
      public final class_2338 pos;
      public final Timer timer;
      public boolean isAir;

      public PlacePos(class_2338 placePos) {
         this.fade = new FadeUtils((long)PlaceRender.this.fadeTime.getValue());
         this.pos = placePos;
         this.timer = new Timer();
         this.isAir = true;
      }

      public boolean draw(class_4587 matrixStack) {
         if (this.isAir) {
            if (!PlaceRender.this.noFail.getValue() && Wrapper.mc.field_1687.method_22347(this.pos)) {
               if (!this.timer.passedMs(PlaceRender.this.timeout.getValue())) {
                  this.fade.reset();
                  class_238 aBox = new class_238(this.pos);
                  if (PlaceRender.this.tryPlaceFill.booleanValue) {
                     Render3DUtil.drawFill(matrixStack, aBox, PlaceRender.this.tryPlaceFill.getValue());
                  }

                  if (PlaceRender.this.tryPlaceBox.booleanValue) {
                     Render3DUtil.drawBox(matrixStack, aBox, PlaceRender.this.tryPlaceBox.getValue());
                  }
               }

               return false;
            }

            this.isAir = false;
         }

         double quads;
         if ((quads = this.fade.ease(PlaceRender.this.ease.getValue())) == (double)1.0F) {
            return true;
         } else {
            double alpha = PlaceRender.this.mode.getValue() != PlaceRender.Mode.Fade && PlaceRender.this.mode.getValue() != PlaceRender.Mode.All ? (double)1.0F : (double)1.0F - quads;
            double size = PlaceRender.this.mode.getValue() != PlaceRender.Mode.Shrink && PlaceRender.this.mode.getValue() != PlaceRender.Mode.All ? (double)0.0F : quads;
            class_238 aBox = (new class_238(this.pos)).method_1009(-size * (double)0.5F, -size * (double)0.5F, -size * (double)0.5F);
            if (PlaceRender.this.fill.booleanValue) {
               Render3DUtil.drawFill(matrixStack, aBox, ColorUtil.injectAlpha(PlaceRender.this.fill.getValue(), (int)((double)PlaceRender.this.fill.getValue().getAlpha() * alpha)));
            }

            if (PlaceRender.this.box.booleanValue) {
               Render3DUtil.drawBox(matrixStack, aBox, ColorUtil.injectAlpha(PlaceRender.this.box.getValue(), (int)((double)PlaceRender.this.box.getValue().getAlpha() * alpha)));
            }

            return false;
         }
      }
   }
}
