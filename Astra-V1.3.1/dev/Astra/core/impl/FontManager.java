package dev.Astra.core.impl;

import dev.Astra.mod.gui.fonts.FontRenderer;
import dev.Astra.mod.modules.impl.client.Fonts;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import net.minecraft.class_4587;

public class FontManager {
   public static FontRenderer ui;
   public static FontRenderer small;
   public static FontRenderer icon;

   public static boolean isCustomFontEnabled() {
      return Fonts.INSTANCE != null && Fonts.INSTANCE.isOn();
   }

   public static boolean isShadowEnabled() {
      return Fonts.INSTANCE == null || Fonts.INSTANCE.shadow.getValue();
   }

   public static void init() {
      try {
         ui = assets(8.0F, "default", 0);
         small = assets(6.0F, "default", 0);
         icon = assetsWithoutOffset(8.0F, "icon", 0);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public static FontRenderer assets(float size, String font, int style, String alternate) throws IOException, FontFormatException {
      ClassLoader classLoader = FontManager.class.getClassLoader();
      InputStream primary = classLoader.getResourceAsStream("assets/frogclient/font/" + font + ".ttf");
      InputStream fallback = classLoader.getResourceAsStream("assets/minecraft/font/font.ttf");
      InputStream stream = primary != null ? primary : fallback;
      return new FontRenderer(Font.createFont(0, (InputStream)Objects.requireNonNull(stream)).deriveFont(style, size), getFont(alternate, style, (int)size), size) {
         public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float elementCodec, float keyCodec, boolean shadow) {
            float dx = 0.0F;
            float dy = 0.0F;
            if (FontManager.isCustomFontEnabled()) {
               dx = (float)Fonts.INSTANCE.translate.getValueInt();
               dy = (float)Fonts.INSTANCE.shift.getValueInt();
            }

            super.drawString(stack, s, x + dx, y + dy, r, g, elementCodec, keyCodec, shadow);
         }
      };
   }

   public static FontRenderer assetsWithoutOffset(float size, String name, int style) throws IOException, FontFormatException {
      ClassLoader classLoader = FontManager.class.getClassLoader();
      InputStream primary = classLoader.getResourceAsStream("assets/frog/icon/" + name + ".ttf");
      InputStream fallback = classLoader.getResourceAsStream("assets/minecraft/font/font.ttf");
      InputStream stream = primary != null ? primary : fallback;
      return new FontRenderer(Font.createFont(0, (InputStream)Objects.requireNonNull(stream)).deriveFont(style, size), size);
   }

   public static FontRenderer assets(float size, String name, int style) throws IOException, FontFormatException {
      ClassLoader classLoader = FontManager.class.getClassLoader();
      InputStream primary = classLoader.getResourceAsStream("assets/frogclient/font/" + name + ".ttf");
      InputStream fallback = classLoader.getResourceAsStream("assets/minecraft/font/font.ttf");
      InputStream stream = primary != null ? primary : fallback;
      return new FontRenderer(Font.createFont(0, (InputStream)Objects.requireNonNull(stream)).deriveFont(style, size), size) {
         public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float elementCodec, float keyCodec, boolean shadow) {
            float dx = 0.0F;
            float dy = 0.0F;
            if (FontManager.isCustomFontEnabled()) {
               dx = (float)Fonts.INSTANCE.translate.getValueInt();
               dy = (float)Fonts.INSTANCE.shift.getValueInt();
            }

            super.drawString(stack, s, x + dx, y + dy, r, g, elementCodec, keyCodec, shadow);
         }
      };
   }

   public static FontRenderer create(int size, String font, int style, String alternate) {
      return new FontRenderer(getFont(font, style, size), getFont(alternate, style, size), (float)size) {
         public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float elementCodec, float keyCodec, boolean shadow) {
            float dx = 0.0F;
            float dy = 0.0F;
            if (FontManager.isCustomFontEnabled()) {
               dx = (float)Fonts.INSTANCE.translate.getValueInt();
               dy = (float)Fonts.INSTANCE.shift.getValueInt();
            }

            super.drawString(stack, s, x + dx, y + dy, r, g, elementCodec, keyCodec, shadow);
         }
      };
   }

   public static FontRenderer create(int size, String font, int style) {
      return new FontRenderer(getFont(font, style, size), (float)size) {
         public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float elementCodec, float keyCodec, boolean shadow) {
            float dx = 0.0F;
            float dy = 0.0F;
            if (FontManager.isCustomFontEnabled()) {
               dx = (float)Fonts.INSTANCE.translate.getValueInt();
               dy = (float)Fonts.INSTANCE.shift.getValueInt();
            }

            super.drawString(stack, s, x + dx, y + dy, r, g, elementCodec, keyCodec, shadow);
         }
      };
   }

   private static Font getFont(String font, int style, int size) {
      File fontDir = new File("C:\\Windows\\Fonts");

      try {
         for(File file : fontDir.listFiles()) {
            if (file.getName().replace(".ttf", "").replace(".ttc", "").replace(".otf", "").equalsIgnoreCase(font)) {
               try {
                  return Font.createFont(0, file).deriveFont(style, (float)size);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }

         for(File file : fontDir.listFiles()) {
            if (file.getName().startsWith(font)) {
               try {
                  return Font.createFont(0, file).deriveFont(style, (float)size);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      } catch (Exception var11) {
      }

      return new Font((String)null, style, size);
   }
}
