package nekiplay.meteorplus.features.modules.render;

import java.util.ArrayList;
import java.util.Iterator;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1538;
import net.minecraft.class_2398;

public class KillEffect extends Module {
   public final ArrayList<class_1297> entityList = new ArrayList();
   private final SettingGroup sgGeneral;
   public final Setting<Effects> mode;

   public KillEffect() {
      super(Categories.Render, "kill-effect", "Render kill effect");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.mode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("effect")).description("The method of applying fly.")).defaultValue(KillEffect.Effects.Lighting_Bolt)).build());
   }

   @EventHandler
   public void onTickEvent(TickEvent.Post event) {
      Iterator var2 = this.mc.field_1687.method_18112().iterator();

      while(true) {
         class_1297 entity;
         label39:
         while(true) {
            if (!var2.hasNext()) {
               return;
            }

            entity = (class_1297)var2.next();
            if (entity instanceof class_1309 livingEntity) {
               if ((livingEntity.field_6213 > 0 || livingEntity.method_6032() <= 0.0F) && !this.entityList.contains(entity)) {
                  switch (((Effects)this.mode.get()).ordinal()) {
                     case 0:
                        class_1538 lightning = new class_1538(class_1299.field_6112, this.mc.field_1687);
                        lightning.method_29495(livingEntity.method_73189());
                        this.mc.field_1687.method_53875(lightning);
                        break label39;
                     case 1:
                        for(int i = 0; (float)i < entity.method_17682() * 10.0F; ++i) {
                           for(int j = 0; (float)j < entity.method_17681() * 10.0F; ++j) {
                              for(int k = 0; (float)k < entity.method_17681() * 10.0F; ++k) {
                                 this.mc.field_1687.method_8406(class_2398.field_18304, entity.method_23317() + (double)j * 0.1, entity.method_23318() + (double)i * 0.1, entity.method_23321() + (double)k * 0.1, (double)0.0F, (double)0.0F, (double)0.0F);
                              }
                           }
                        }
                     default:
                        break label39;
                  }
               }
            }
         }

         this.entityList.add(entity);
      }
   }

   public static enum Effects {
      Lighting_Bolt,
      Falling_Lava;

      public String toString() {
         return super.toString().replaceAll("_", " ");
      }

      // $FF: synthetic method
      private static Effects[] $values() {
         return new Effects[]{Lighting_Bolt, Falling_Lava};
      }
   }
}
