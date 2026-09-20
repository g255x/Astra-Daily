package dev.Astra.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.TotemEvent;
import dev.Astra.api.utils.entity.CopyPlayerEntity;
import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.api.utils.render.ModelPlayer;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1657;
import net.minecraft.class_4587;

public class PopChams extends Module {
   public static PopChams INSTANCE;
   public final EnumSetting<Easing> ease;
   private final SliderSetting fadeTime;
   private final SliderSetting yOffset;
   private final SliderSetting scale;
   private final SliderSetting yaw;
   private final BooleanSetting alpha;
   private final BooleanSetting forceSneak;
   private final BooleanSetting noSelf;
   private final BooleanSetting noLimb;
   private final ColorSetting fill;
   private final ColorSetting line;
   final CopyOnWriteArrayList<Person> popList;

   public PopChams() {
      super("PopChams", Module.Category.Render);
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut));
      this.fadeTime = this.add(new SliderSetting("FadeTime", 300, 0, 1000));
      this.yOffset = this.add(new SliderSetting("YOffset", (double)0.0F, (double)-10.0F, (double)10.0F, 0.01));
      this.scale = this.add(new SliderSetting("Scale", (double)1.0F, (double)0.0F, (double)2.0F, 0.01));
      this.yaw = this.add(new SliderSetting("Yaw", (double)0.0F, (double)0.0F, (double)720.0F, 0.01));
      this.alpha = this.add(new BooleanSetting("Alpha", true));
      this.forceSneak = this.add(new BooleanSetting("ForceSneak", false));
      this.noSelf = this.add(new BooleanSetting("NoSelf", true));
      this.noLimb = this.add(new BooleanSetting("NoLimb", true));
      this.fill = this.add((new ColorSetting("Fill", new Color(255, 255, 255, 100))).injectBoolean(true));
      this.line = this.add((new ColorSetting("Line", new Color(255, 255, 255, 100))).injectBoolean(true));
      this.popList = new CopyOnWriteArrayList();
      this.setChinese("爆图腾上色");
      INSTANCE = this;
   }

   public void onRender3D(class_4587 matrixStack) {
      RenderSystem.depthMask(false);
      this.popList.removeIf((person) -> person.render(matrixStack));
      RenderSystem.depthMask(true);
   }

   @EventListener
   private void onTotemPop(TotemEvent event) {
      if (!this.noSelf.getValue() || !event.getPlayer().equals(mc.field_1724)) {
         this.popList.add(new Person(new CopyPlayerEntity(event.getPlayer())));
      }
   }

   private class Person {
      public final ModelPlayer modelPlayer;
      final Animation animation;

      public Person(class_1657 player) {
         this.modelPlayer = new ModelPlayer(player);
         this.animation = new Animation();
      }

      public boolean render(class_4587 matrixStack) {
         double animation = this.animation.get((double)1.0F, (long)PopChams.this.fadeTime.getValueInt(), PopChams.this.ease.getValue());
         if (animation >= (double)1.0F) {
            return true;
         } else {
            this.modelPlayer.render(matrixStack, PopChams.this.fill, PopChams.this.line, PopChams.this.alpha.getValue() ? (double)1.0F - animation : (double)1.0F, PopChams.this.yOffset.getValue() * animation, (double)1.0F + (PopChams.this.scale.getValue() - (double)1.0F) * animation, PopChams.this.yaw.getValue() * animation, PopChams.this.noLimb.getValue(), PopChams.this.forceSneak.getValue());
            return false;
         }
      }
   }
}
