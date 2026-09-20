package dev.Astra.mod.gui.fonts;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.modules.impl.client.ClickGui;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.awt.Color;
import java.awt.Font;
import java.io.Closeable;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_293.class_5596;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class FontRenderer implements Closeable {
   private static final Char2IntArrayMap colorCodes = new Char2IntArrayMap() {
      {
         this.put('0', 0);
         this.put('1', 170);
         this.put('2', 43520);
         this.put('3', 43690);
         this.put('4', 11141120);
         this.put('5', 11141290);
         this.put('6', 16755200);
         this.put('7', 11184810);
         this.put('8', 5592405);
         this.put('9', 5592575);
         this.put('A', 5635925);
         this.put('B', 5636095);
         this.put('C', 16733525);
         this.put('D', 16733695);
         this.put('E', 16777045);
         this.put('F', 16777215);
      }
   };
   private static final ExecutorService ASYNC_WORKER = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
   private final Object2ObjectMap<class_2960, ObjectList<DrawEntry>> GLYPH_PAGE_CACHE;
   private final float originalSize;
   private final ObjectList<GlyphMap> maps;
   private final Char2ObjectArrayMap<Glyph> allGlyphs;
   private final int charsPerPage;
   private final int padding;
   private final String prebakeGlyphs;
   private int scaleMul;
   private Font font;
   private Font secondFont;
   private int previousGameScale;
   private Future<Void> prebakeGlyphsFuture;
   private boolean initialized;

   public FontRenderer(Font font, float sizePx, int charactersPerPage, int paddingBetweenCharacters, @Nullable String prebakeCharacters) {
      this.GLYPH_PAGE_CACHE = new Object2ObjectOpenHashMap();
      this.maps = new ObjectArrayList();
      this.allGlyphs = new Char2ObjectArrayMap();
      this.scaleMul = 0;
      this.previousGameScale = -1;
      this.originalSize = sizePx;
      this.charsPerPage = charactersPerPage;
      this.padding = paddingBetweenCharacters;
      this.prebakeGlyphs = prebakeCharacters;
      this.init(font, sizePx);
   }

   public FontRenderer(Font font, Font secondFont, float sizePx, int charactersPerPage, int paddingBetweenCharacters, @Nullable String prebakeCharacters) {
      this(font, sizePx, charactersPerPage, paddingBetweenCharacters, prebakeCharacters);
      this.secondFont = secondFont.deriveFont(sizePx * (float)this.scaleMul);
   }

   public FontRenderer(Font font, float sizePx) {
      this(font, sizePx, 256, 5, (String)null);
   }

   public FontRenderer(Font font, Font secondFont, float sizePx) {
      this(font, secondFont, sizePx, 256, 5, (String)null);
   }

   private static int floorNearestMulN(int x, int n) {
      return n * (int)Math.floor((double)x / (double)n);
   }

   public static String stripControlCodes(String text) {
      char[] chars = text.toCharArray();
      StringBuilder f = new StringBuilder();

      for(int i = 0; i < chars.length; ++i) {
         char c = chars[i];
         if (c == 167) {
            ++i;
         } else {
            f.append(c);
         }
      }

      return f.toString();
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   public static @NotNull class_2960 randomIdentifier() {
      return class_2960.method_60655("astraclient", "temp/" + randomString());
   }

   private static String randomString() {
      return (String)IntStream.range(0, 32).mapToObj((operand) -> String.valueOf((char)(new Random()).nextInt(97, 123))).collect(Collectors.joining());
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   public static int @NotNull [] RGBIntToRGB(int in) {
      int red = in >> 16 & 255;
      int green = in >> 8 & 255;
      int blue = in & 255;
      return new int[]{red, green, blue};
   }

   public static double roundToDecimal(double n, int point) {
      if (point == 0) {
         return Math.floor(n);
      } else {
         double factor = Math.pow((double)10.0F, (double)point);
         return (double)Math.round(n * factor) / factor;
      }
   }

   private void sizeCheck() {
      int gs = (int)Wrapper.mc.method_22683().method_4495();
      if (gs != this.previousGameScale) {
         this.close();
         this.init(this.font, this.originalSize);
         if (this.secondFont != null) {
            this.secondFont = this.secondFont.deriveFont(this.originalSize * (float)this.scaleMul);
         }
      }

   }

   private void init(Font font, float sizePx) {
      if (this.initialized) {
         throw new IllegalStateException("Double call to init()");
      } else {
         this.initialized = true;
         this.scaleMul = this.previousGameScale = (int)Wrapper.mc.method_22683().method_4495();
         this.font = font.deriveFont(sizePx * (float)this.scaleMul);
         if (this.prebakeGlyphs != null && !this.prebakeGlyphs.isEmpty()) {
            this.prebakeGlyphsFuture = this.prebake();
         }

      }
   }

   private Future<Void> prebake() {
      return ASYNC_WORKER.submit(() -> {
         for(char c : this.prebakeGlyphs.toCharArray()) {
            if (Thread.interrupted()) {
               break;
            }

            this.locateGlyph1(c);
         }

         return null;
      });
   }

   private GlyphMap generateMap(char from, char to) {
      GlyphMap gm = this.secondFont != null ? new GlyphMap(from, to, this.font, this.secondFont, randomIdentifier(), this.padding) : new GlyphMap(from, to, this.font, randomIdentifier(), this.padding);
      this.maps.add(gm);
      return gm;
   }

   private Glyph locateGlyph0(char glyph) {
      ObjectListIterator var2 = this.maps.iterator();

      while(var2.hasNext()) {
         GlyphMap map = (GlyphMap)var2.next();
         if (map.contains(glyph)) {
            return map.getGlyph(glyph);
         }
      }

      int base = floorNearestMulN(glyph, this.charsPerPage);
      GlyphMap glyphMap = this.generateMap((char)base, (char)(base + this.charsPerPage));
      return glyphMap.getGlyph(glyph);
   }

   private @Nullable Glyph locateGlyph1(char glyph) {
      return (Glyph)this.allGlyphs.computeIfAbsent(glyph, this::locateGlyph0);
   }

   public void drawString(class_4587 stack, String s, double x, double y, int color) {
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      float a = (float)(color >> 24 & 255) / 255.0F;
      this.drawString(stack, s, (float)x, (float)y, r, g, b, a, false);
   }

   public void drawString(class_4587 stack, String s, double x, double y, Color color) {
      this.drawString(stack, s, (float)x, (float)y, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F, false);
   }

   public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float b, float a) {
      this.drawString(stack, s, x, y, r, g, b, a, false);
   }

   public void drawString(class_4587 stack, String s, double x, double y, int color, boolean shadow) {
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      float a = (float)(color >> 24 & 255) / 255.0F;
      this.drawString(stack, s, (float)x, (float)y, r, g, b, a, shadow);
   }

   public void drawString(class_4587 stack, String s, double x, double y, Color color, boolean shadow) {
      this.drawString(stack, s, (float)x, (float)y, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F, shadow);
   }

   public void drawStringWithShadow(class_4587 stack, String s, double x, double y, int color) {
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      float a = (float)(color >> 24 & 255) / 255.0F;
      this.drawString(stack, s, (float)x, (float)y, r, g, b, a, true);
   }

   public void drawStringWithShadow(class_4587 stack, String s, double x, double y, Color color) {
      this.drawString(stack, s, (float)x, (float)y, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F, true);
   }

   public void drawStringWithShadow(class_4587 stack, String s, float x, float y, float r, float g, float b, float a) {
      this.drawString(stack, s, x, y, r, g, b, a, true);
   }

   public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
      if (this.prebakeGlyphsFuture != null && !this.prebakeGlyphsFuture.isDone()) {
         try {
            this.prebakeGlyphsFuture.get();
         } catch (ExecutionException | InterruptedException var40) {
         }
      }

      this.sizeCheck();
      float baseR = r;
      float baseG = g;
      float baseB = b;
      float r2 = r;
      float g2 = g;
      float b2 = b;
      boolean useRainbow = this.useClickGuiRainbowByY();
      if (useRainbow) {
         Color active = ClickGui.getInstance().getActiveColor((double)y / (double)10.0F);
         baseR = (float)active.getRed() / 255.0F;
         baseG = (float)active.getGreen() / 255.0F;
         baseB = (float)active.getBlue() / 255.0F;
         r2 = baseR;
         g2 = baseG;
         b2 = baseB;
      }

      stack.method_22903();
      stack.method_22904(roundToDecimal((double)x, 1), roundToDecimal((double)y, 1), (double)0.0F);
      stack.method_22905(1.0F / (float)this.scaleMul, 1.0F / (float)this.scaleMul, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.disableCull();
      GL11.glTexParameteri(3553, 10241, 9729);
      RenderSystem.setShader(class_757::method_34543);
      Matrix4f mat = stack.method_23760().method_23761();
      char[] chars = s.toCharArray();
      float xOffset = 0.0F;
      float yOffset = 0.0F;
      boolean inSel = false;
      int lineStart = 0;
      class_2960 currentTex = null;
      class_287 currentBuilder = null;

      for(int i = 0; i < chars.length; ++i) {
         char c = chars[i];
         if (inSel) {
            inSel = false;
            char c1 = Character.toUpperCase(c);
            if (colorCodes.containsKey(c1)) {
               int[] col = RGBIntToRGB(colorCodes.get(c1));
               r2 = (float)col[0] / 255.0F;
               g2 = (float)col[1] / 255.0F;
               b2 = (float)col[2] / 255.0F;
            } else if (c1 == 'R') {
               r2 = baseR;
               g2 = baseG;
               b2 = baseB;
            }
         } else if (c == 167) {
            inSel = true;
         } else if (c == '\n') {
            yOffset += this.getStringHeight(s.substring(lineStart, i)) * (float)this.scaleMul;
            xOffset = 0.0F;
            lineStart = i + 1;
            if (useRainbow) {
               Color active = ClickGui.getInstance().getActiveColor((double)(y + yOffset) / (double)10.0F);
               baseR = (float)active.getRed() / 255.0F;
               baseG = (float)active.getGreen() / 255.0F;
               baseB = (float)active.getBlue() / 255.0F;
               r2 = baseR;
               g2 = baseG;
               b2 = baseB;
            }
         } else {
            Glyph glyph = this.locateGlyph1(c);
            if (glyph != null && glyph.value() != ' ') {
               class_2960 tex = glyph.owner().bindToTexture;
               if (currentTex != tex) {
                  if (currentBuilder != null) {
                     Render3DUtil.endBuilding(currentBuilder);
                  }

                  currentTex = tex;
                  RenderSystem.setShaderTexture(0, tex);
                  currentBuilder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
               }

               GlyphMap owner = glyph.owner();
               float w = (float)glyph.width();
               float h = (float)glyph.height();
               float u1 = (float)glyph.u() / (float)owner.width;
               float v1 = (float)glyph.v() / (float)owner.height;
               float u2 = ((float)glyph.u() + w) / (float)owner.width;
               float v2 = ((float)glyph.v() + h) / (float)owner.height;
               if (shadow) {
                  float sx = xOffset + 1.0F;
                  float sy = yOffset + 1.0F;
                  currentBuilder.method_22918(mat, sx, sy + h, 0.0F).method_22913(u1, v2).method_22915(0.0F, 0.0F, 0.0F, a);
                  currentBuilder.method_22918(mat, sx + w, sy + h, 0.0F).method_22913(u2, v2).method_22915(0.0F, 0.0F, 0.0F, a);
                  currentBuilder.method_22918(mat, sx + w, sy, 0.0F).method_22913(u2, v1).method_22915(0.0F, 0.0F, 0.0F, a);
                  currentBuilder.method_22918(mat, sx, sy, 0.0F).method_22913(u1, v1).method_22915(0.0F, 0.0F, 0.0F, a);
               }

               currentBuilder.method_22918(mat, xOffset, yOffset + h, 0.0F).method_22913(u1, v2).method_22915(r2, g2, b2, a);
               currentBuilder.method_22918(mat, xOffset + w, yOffset + h, 0.0F).method_22913(u2, v2).method_22915(r2, g2, b2, a);
               currentBuilder.method_22918(mat, xOffset + w, yOffset, 0.0F).method_22913(u2, v1).method_22915(r2, g2, b2, a);
               currentBuilder.method_22918(mat, xOffset, yOffset, 0.0F).method_22913(u1, v1).method_22915(r2, g2, b2, a);
               xOffset += (float)glyph.width();
            } else if (glyph != null) {
               xOffset += (float)glyph.width();
            }
         }
      }

      if (currentBuilder != null) {
         Render3DUtil.endBuilding(currentBuilder);
      }

      stack.method_22909();
   }

   public void drawCenteredString(class_4587 stack, String s, double x, double y, int color) {
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      float a = (float)(color >> 24 & 255) / 255.0F;
      this.drawStringWithShadow(stack, s, (float)(x - (double)(this.getWidth(s) / 2.0F)), (float)y, r, g, b, a);
   }

   public void drawCenteredString(class_4587 stack, String s, double x, double y, Color color) {
      this.drawStringWithShadow(stack, s, (float)(x - (double)(this.getWidth(s) / 2.0F)), (float)y, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
   }

   public void drawCenteredString(class_4587 stack, String s, float x, float y, float r, float g, float b, float a) {
      this.drawStringWithShadow(stack, s, x - this.getWidth(s) / 2.0F, y, r, g, b, a);
   }

   public float getWidth(String text) {
      char[] c = stripControlCodes(text).toCharArray();
      float currentLine = 0.0F;
      float maxPreviousLines = 0.0F;

      for(char c1 : c) {
         if (c1 == '\n') {
            maxPreviousLines = Math.max(currentLine, maxPreviousLines);
            currentLine = 0.0F;
         } else {
            Glyph glyph = this.locateGlyph1(c1);
            currentLine += glyph == null ? 0.0F : (float)glyph.width() / (float)this.scaleMul;
         }
      }

      return Math.max(currentLine, maxPreviousLines);
   }

   public float getStringHeight(String text) {
      char[] c = stripControlCodes(text).toCharArray();
      if (c.length == 0) {
         c = new char[]{' '};
      }

      float currentLine = 0.0F;
      float previous = 0.0F;

      for(char c1 : c) {
         if (c1 == '\n') {
            if (currentLine == 0.0F) {
               currentLine = this.locateGlyph1(' ') == null ? 0.0F : (float)((Glyph)Objects.requireNonNull(this.locateGlyph1(' '))).height() / (float)this.scaleMul;
            }

            previous += currentLine;
            currentLine = 0.0F;
         } else {
            Glyph glyph = this.locateGlyph1(c1);
            currentLine = Math.max(glyph == null ? 0.0F : (float)glyph.height() / (float)this.scaleMul, currentLine);
         }
      }

      return currentLine + previous;
   }

   public void close() {
      try {
         if (this.prebakeGlyphsFuture != null && !this.prebakeGlyphsFuture.isDone() && !this.prebakeGlyphsFuture.isCancelled()) {
            this.prebakeGlyphsFuture.cancel(true);
            this.prebakeGlyphsFuture.get();
            this.prebakeGlyphsFuture = null;
         }

         ObjectListIterator var1 = this.maps.iterator();

         while(var1.hasNext()) {
            GlyphMap map = (GlyphMap)var1.next();
            map.destroy();
         }

         this.maps.clear();
         this.allGlyphs.clear();
         this.initialized = false;
      } catch (Exception var3) {
      }

   }

   public float getFontHeight(String str) {
      return this.getStringHeight(str);
   }

   public float getFontHeight() {
      return this.getStringHeight("A");
   }

   public void drawGradientString(class_4587 stack, String s, float x, float y) {
      this.drawString(stack, s, x, y, 255.0F, 255.0F, 255.0F, 255.0F);
   }

   public void drawGradientCenteredString(class_4587 matrices, String s, float x, float y) {
      this.drawGradientString(matrices, s, x - this.getWidth(s) / 2.0F, y);
   }

   private boolean useClickGuiRainbowByY() {
      if (Wrapper.mc != null && Wrapper.mc.field_1755 instanceof ClickGuiScreen) {
         ClickGui clickGui = ClickGui.getInstance();
         if (clickGui == null) {
            return false;
         } else {
            return clickGui.style.getValue() != ClickGui.Style.RainbowDelay && clickGui.colors.getValue() && clickGui.colorMode.getValue() == ClickGui.ColorMode.Rainbow;
         }
      } else {
         return false;
      }
   }

   private static record DrawEntry(float atX, float atY, float r, float g, float elementCodec, Glyph toDraw) {
   }
}
