package dev.luminous.api.utils.render;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.class_1058;
import net.minecraft.class_1087;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import net.minecraft.class_5819;
import net.minecraft.class_777;
import net.minecraft.class_806;
import net.minecraft.class_809;
import org.jetbrains.annotations.Nullable;

public class SimpleItemModel implements class_1087 {
   private class_1087 flattenedItem;
   private final List<class_777> nullQuadList = new ObjectArrayList();

   public void setItem(class_1087 model) {
      this.flattenedItem = model;
   }

   private boolean isCorrectDirectionForType(class_2350 direction) {
      return direction == class_2350.field_11035;
   }

   public List<class_777> method_4707(@Nullable class_2680 state, @Nullable class_2350 face, class_5819 random) {
      if (face != null) {
         return (List<class_777>)(this.isCorrectDirectionForType(face) ? this.flattenedItem.method_4707(state, face, random) : ImmutableList.of());
      } else {
         this.nullQuadList.clear();

         for(class_777 quad : this.flattenedItem.method_4707(state, (class_2350)null, random)) {
            if (this.isCorrectDirectionForType(quad.method_3358())) {
               this.nullQuadList.add(quad);
            }
         }

         return this.nullQuadList;
      }
   }

   public boolean method_4708() {
      return this.flattenedItem.method_4708();
   }

   public boolean method_4712() {
      return this.flattenedItem.method_4712();
   }

   public boolean method_24304() {
      return this.flattenedItem.method_24304();
   }

   public boolean method_4713() {
      return this.flattenedItem.method_4713();
   }

   public class_1058 method_4711() {
      return this.flattenedItem.method_4711();
   }

   public class_809 method_4709() {
      return this.flattenedItem.method_4709();
   }

   public class_806 method_4710() {
      return this.flattenedItem.method_4710();
   }
}
