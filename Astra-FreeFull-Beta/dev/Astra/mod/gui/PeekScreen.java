package dev.Astra.mod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.mod.modules.impl.misc.ShulkerViewer;
import net.minecraft.class_1277;
import net.minecraft.class_1733;
import net.minecraft.class_1747;
import net.minecraft.class_1767;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2248;
import net.minecraft.class_2480;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3872;
import net.minecraft.class_495;
import net.minecraft.class_757;
import net.minecraft.class_9334;
import net.minecraft.class_3872.class_3931;

public class PeekScreen extends class_495 {
   private static final class_2960 TEXTURE = class_2960.method_60654("textures/gui/container/shulker_box.png");
   private final class_1799[] contents;
   private final class_1799 storageBlock;
   private final int colorArgb;

   public PeekScreen(class_1799 storageBlock, class_1799[] contents) {
      super(new class_1733(0, Wrapper.mc.field_1724.method_31548(), new class_1277(contents)), Wrapper.mc.field_1724.method_31548(), storageBlock.method_7964());
      this.contents = contents;
      this.storageBlock = storageBlock;
      this.colorArgb = getShulkerColorArgb(storageBlock);
   }

   private static int getShulkerColorArgb(class_1799 shulkerItem) {
      class_1792 item = shulkerItem.method_7909();
      if (item instanceof class_1747 blockItem) {
         class_2248 block = blockItem.method_7711();
         if (block instanceof class_2480 shulkerBlock) {
            class_1767 dye = shulkerBlock.method_10528();
            if (dye == null) {
               return -1;
            }

            int rgb = dye.method_7787();
            return -16777216 | rgb;
         }
      }

      return -1;
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (button == 2 && this.field_2787 != null && !this.field_2787.method_7677().method_7960() && Wrapper.mc.field_1724.field_7512.method_34255().method_7960()) {
         class_1799 itemStack = this.field_2787.method_7677();
         if (ShulkerViewer.hasItems(itemStack) || itemStack.method_7909() == class_1802.field_8466) {
            return ShulkerViewer.openContainer(this.field_2787.method_7677(), this.contents, false);
         }

         if (itemStack.method_57824(class_9334.field_49606) != null || itemStack.method_57824(class_9334.field_49653) != null) {
            this.method_25419();
            Wrapper.mc.method_1507(new class_3872(class_3931.method_17562(itemStack)));
            return true;
         }
      }

      return false;
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      return false;
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      if (keyCode != 256 && !Wrapper.mc.field_1690.field_1822.method_1417(keyCode, scanCode)) {
         return false;
      } else {
         this.method_25419();
         return true;
      }
   }

   public boolean method_16803(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         this.method_25419();
         return true;
      } else {
         return false;
      }
   }

   protected void method_2389(class_332 context, float delta, int mouseX, int mouseY) {
      float r = (float)(this.colorArgb >> 16 & 255) / 255.0F;
      float g = (float)(this.colorArgb >> 8 & 255) / 255.0F;
      float b = (float)(this.colorArgb & 255) / 255.0F;
      float a = (float)(this.colorArgb >> 24 & 255) / 255.0F;
      RenderSystem.setShader(class_757::method_34542);
      RenderSystem.setShaderColor(r, g, b, a);
      int i = (this.field_22789 - this.field_2792) / 2;
      int j = (this.field_22790 - this.field_2779) / 2;
      context.method_25302(TEXTURE, i, j, 0, 0, this.field_2792, this.field_2779);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
