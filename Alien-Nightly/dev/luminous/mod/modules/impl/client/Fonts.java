package dev.luminous.mod.modules.impl.client;

import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;

public class Fonts extends Module {
   public static Fonts INSTANCE;
   public final StringSetting font = this.add(new StringSetting("Font", "default"));
   public final StringSetting alternate = this.add(new StringSetting("Alternate", "msyh"));
   public final EnumSetting<Style> style;
   public final SliderSetting size;
   public final SliderSetting shift;
   public final SliderSetting translate;

   public Fonts() {
      super("Fonts", Module.Category.Client);
      this.style = this.add(new EnumSetting("Style", Fonts.Style.BOLD));
      this.size = this.add(new SliderSetting("Size", (double)8.0F, (double)1.0F, (double)15.0F, (double)1.0F));
      this.shift = this.add(new SliderSetting("Shift", (double)0.0F, (double)-10.0F, (double)10.0F, (double)1.0F));
      this.translate = this.add(new SliderSetting("Translate", (double)0.0F, (double)-10.0F, (double)10.0F, (double)1.0F));
      this.setChinese("字体");
      INSTANCE = this;
   }

   public void enable() {
      this.refresh();
   }

   public void refresh() {
      try {
         if (this.font.getValue().equals("default")) {
            if (this.alternate.getValue().equals("null")) {
               FontManager.ui = FontManager.assets((float)this.size.getValueInt(), this.font.getValue(), ((Style)this.style.getValue()).get());
               FontManager.small = FontManager.assets(6.0F, this.font.getValue(), ((Style)this.style.getValue()).get());
            } else {
               FontManager.ui = FontManager.assets((float)this.size.getValueInt(), this.font.getValue(), ((Style)this.style.getValue()).get(), this.alternate.getValue());
               FontManager.small = FontManager.assets(6.0F, this.font.getValue(), ((Style)this.style.getValue()).get(), this.alternate.getValue());
            }
         } else if (this.alternate.getValue().equals("null")) {
            FontManager.ui = FontManager.create(this.size.getValueInt(), this.font.getValue(), ((Style)this.style.getValue()).get());
            FontManager.small = FontManager.create(6, this.font.getValue(), ((Style)this.style.getValue()).get());
         } else {
            FontManager.ui = FontManager.create(this.size.getValueInt(), this.font.getValue(), ((Style)this.style.getValue()).get(), this.alternate.getValue());
            FontManager.small = FontManager.create(6, this.font.getValue(), ((Style)this.style.getValue()).get(), this.alternate.getValue());
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static enum Style {
      PLAIN {
         public int get() {
            return 0;
         }
      },
      BOLD {
         public int get() {
            return 1;
         }
      },
      ITALIC {
         public int get() {
            return 2;
         }
      };

      public abstract int get();

      // $FF: synthetic method
      private static Style[] $values() {
         return new Style[]{PLAIN, BOLD, ITALIC};
      }
   }
}
