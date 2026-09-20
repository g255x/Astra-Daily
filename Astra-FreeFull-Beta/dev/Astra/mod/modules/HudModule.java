package dev.Astra.mod.modules;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ResizeEvent;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;

public abstract class HudModule extends Module {
   private static int cachedScaledWidth = -1;
   private static int cachedScaledHeight = -1;
   public final EnumSetting<Corner> corner;
   protected final SliderSetting x;
   protected final SliderSetting y;
   private int lastHudX;
   private int lastHudY;
   private int lastHudW;
   private int lastHudH;

   public HudModule(String name, String chinese, int defaultX, int defaultY) {
      this(name, "", chinese, defaultX, defaultY, HudModule.Corner.LeftTop);
   }

   public HudModule(String name, String description, String chinese, int defaultX, int defaultY) {
      this(name, description, chinese, defaultX, defaultY, HudModule.Corner.LeftTop);
   }

   protected HudModule(String name, String description, String chinese, int defaultX, int defaultY, Corner defaultCorner) {
      super(name, description, Module.Category.Client);
      this.setChinese(chinese);
      this.corner = this.add(new EnumSetting("Corner", defaultCorner));
      this.x = this.add(new SliderSetting("X", defaultX, 0, 1500));
      this.y = this.add(new SliderSetting("Y", defaultY, 0, 1000));
   }

   public static void updateWindowSize() {
      if (mc != null && mc.method_22683() != null) {
         cachedScaledWidth = mc.method_22683().method_4486();
         cachedScaledHeight = mc.method_22683().method_4502();
      }

   }

   private static int getScaledWidth() {
      if (cachedScaledWidth <= 0 && mc != null && mc.method_22683() != null) {
         cachedScaledWidth = mc.method_22683().method_4486();
      }

      return cachedScaledWidth;
   }

   private static int getScaledHeight() {
      if (cachedScaledHeight <= 0 && mc != null && mc.method_22683() != null) {
         cachedScaledHeight = mc.method_22683().method_4502();
      }

      return cachedScaledHeight;
   }

   @EventListener
   public void onResize(ResizeEvent event) {
      updateWindowSize();
   }

   public final int getHudX() {
      return this.x.getValueInt();
   }

   public final int getHudY() {
      return this.y.getValueInt();
   }

   public final void setHudX(int x) {
      this.x.setValue(clamp((double)x, this.x.getMin(), this.x.getMax()));
   }

   public final void setHudY(int y) {
      this.y.setValue(clamp((double)y, this.y.getMin(), this.y.getMax()));
   }

   public final void setHudPos(int x, int y) {
      this.setHudX(x);
      this.setHudY(y);
   }

   public final int getHudRenderX(int elementW) {
      int sw = getScaledWidth();
      if (sw <= 0) {
         return this.getHudX();
      } else {
         int w = Math.max(0, elementW);
         int margin = this.getHudX();
         int x = ((Corner)this.corner.getValue()).isRight() ? sw - w - margin : margin;
         return clampInt(x, 0, Math.max(0, sw - w));
      }
   }

   public final int getHudRenderY(int elementH) {
      int sh = getScaledHeight();
      if (sh <= 0) {
         return this.getHudY();
      } else {
         int h = Math.max(0, elementH);
         int margin = this.getHudY();
         int y = ((Corner)this.corner.getValue()).isBottom() ? sh - h - margin : margin;
         return clampInt(y, 0, Math.max(0, sh - h));
      }
   }

   public final void setHudPosFromBounds(int boundsX, int boundsY) {
      int sw = getScaledWidth();
      int sh = getScaledHeight();
      if (sw > 0 && sh > 0) {
         int w = Math.max(0, this.getHudBoundW());
         int h = Math.max(0, this.getHudBoundH());
         Corner c = this.corner.getValue();
         int nx = c.isRight() ? sw - w - boundsX : boundsX;
         int ny = c.isBottom() ? sh - h - boundsY : boundsY;
         this.setHudPos(nx, ny);
      } else {
         this.setHudPos(boundsX, boundsY);
      }
   }

   private static int clampInt(int v, int min, int max) {
      return v < min ? min : Math.min(v, max);
   }

   private static double clamp(double v, double min, double max) {
      return v < min ? min : Math.min(v, max);
   }

   protected final void setHudBounds(int x, int y, int w, int h) {
      this.lastHudX = x;
      this.lastHudY = y;
      this.lastHudW = w;
      this.lastHudH = h;
   }

   protected final void clearHudBounds() {
      this.lastHudW = this.lastHudH = 0;
   }

   public final int getHudBoundX() {
      return this.lastHudX;
   }

   public final int getHudBoundY() {
      return this.lastHudY;
   }

   public final int getHudBoundW() {
      return this.lastHudW;
   }

   public final int getHudBoundH() {
      return this.lastHudH;
   }

   public final boolean isHudHit(int mouseX, int mouseY) {
      return this.lastHudW > 0 && this.lastHudH > 0 && mouseX >= this.lastHudX && mouseX <= this.lastHudX + this.lastHudW && mouseY >= this.lastHudY && mouseY <= this.lastHudY + this.lastHudH;
   }

   public final boolean isHudOverlapping(int x1, int y1, int x2, int y2) {
      if (this.lastHudW > 0 && this.lastHudH > 0) {
         int rx1 = Math.min(x1, x2);
         int ry1 = Math.min(y1, y2);
         int rx2 = Math.max(x1, x2);
         int ry2 = Math.max(y1, y2);
         int bx1 = this.lastHudX;
         int by1 = this.lastHudY;
         int bx2 = this.lastHudX + this.lastHudW;
         int by2 = this.lastHudY + this.lastHudH;
         return rx1 < bx2 && rx2 > bx1 && ry1 < by2 && ry2 > by1;
      } else {
         return false;
      }
   }

   public static enum Corner {
      LeftTop(false, false),
      RightTop(true, false),
      LeftBottom(false, true),
      RightBottom(true, true);

      private final boolean right;
      private final boolean bottom;

      private Corner(boolean right, boolean bottom) {
         this.right = right;
         this.bottom = bottom;
      }

      public boolean isRight() {
         return this.right;
      }

      public boolean isBottom() {
         return this.bottom;
      }

      // $FF: synthetic method
      private static Corner[] $values() {
         return new Corner[]{LeftTop, RightTop, LeftBottom, RightBottom};
      }
   }
}
