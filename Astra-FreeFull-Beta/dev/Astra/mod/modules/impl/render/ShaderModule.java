package dev.Astra.mod.modules.impl.render;

import dev.Astra.Astra;
import dev.Astra.asm.accessors.IGameRenderer;
import dev.Astra.core.impl.ShaderManager;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_3532;
import net.minecraft.class_4587;

public class ShaderModule extends Module {
   public static ShaderModule INSTANCE;
   private final EnumSetting<Page> page;
   public final EnumSetting<ShaderManager.Shader> mode;
   public final SliderSetting speed;
   public final ColorSetting fill;
   public final SliderSetting maxSample;
   public final SliderSetting divider;
   public final SliderSetting radius;
   public final SliderSetting smoothness;
   public final SliderSetting alpha;
   public final SliderSetting maxRange;
   public final SliderSetting pulseSpeed;
   public final ColorSetting pulse;
   public final SliderSetting step;
   public final SliderSetting octaves;
   public final ColorSetting smoke1;
   public final ColorSetting smoke2;
   public final ColorSetting smoke3;
   public final ColorSetting smoke4;
   private final BooleanSetting hands;
   private final BooleanSetting self;
   private final BooleanSetting players;
   private final BooleanSetting friends;
   private final BooleanSetting crystals;
   private final BooleanSetting creatures;
   private final BooleanSetting monsters;
   private final BooleanSetting ambients;
   private final BooleanSetting items;
   private final BooleanSetting others;

   public ShaderModule() {
      super("Shader", Module.Category.Render);
      this.page = this.add(new EnumSetting("Page", ShaderModule.Page.Target));
      this.mode = this.add(new EnumSetting("Mode", ShaderManager.Shader.Solid, () -> this.page.getValue() == ShaderModule.Page.Shader));
      this.speed = this.add(new SliderSetting("Speed", (double)0.0F, (double)0.0F, (double)20.0F, 0.1, () -> this.page.getValue() == ShaderModule.Page.Shader));
      this.fill = this.add(new ColorSetting("Color", new Color(-1685490725, true), () -> this.page.getValue() == ShaderModule.Page.Shader));
      this.maxSample = this.add(new SliderSetting("MaxSample", (double)0.0F, (double)0.0F, (double)20.0F, () -> this.page.getValue() == ShaderModule.Page.Shader));
      this.divider = this.add(new SliderSetting("Divider", (double)0.0F, (double)0.0F, (double)300.0F, () -> this.page.getValue() == ShaderModule.Page.Shader));
      this.radius = this.add(new SliderSetting("Radius", 2.6, (double)0.0F, (double)6.0F, () -> this.page.getValue() == ShaderModule.Page.Shader));
      this.smoothness = this.add(new SliderSetting("Smoothness", 0.1, (double)0.0F, (double)1.0F, 0.01, () -> this.page.getValue() == ShaderModule.Page.Shader));
      this.alpha = this.add(new SliderSetting("GlowAlpha", 255, 0, 255, () -> this.page.getValue() == ShaderModule.Page.Shader));
      this.maxRange = this.add((new SliderSetting("MaxRange", 32, 16, 512, () -> this.page.getValue() == ShaderModule.Page.Target)).setSuffix("m"));
      this.pulseSpeed = this.add(new SliderSetting("PulseSize", (double)0.0F, (double)0.0F, (double)200.0F, 0.1, () -> this.page.getValue() == ShaderModule.Page.Color));
      this.pulse = this.add(new ColorSetting("Pulse", new Color(-10262857, true), () -> this.page.getValue() == ShaderModule.Page.Color));
      this.step = this.add(new SliderSetting("Step", (double)0.0F, (double)0.0F, (double)4.0F, 0.01, () -> this.page.getValue() == ShaderModule.Page.Color));
      this.octaves = this.add(new SliderSetting("Octaves", 5, 5, 30, () -> this.page.getValue() == ShaderModule.Page.Color));
      this.smoke1 = this.add(new ColorSetting("Color1", new Color(-50331649, true), () -> this.page.getValue() == ShaderModule.Page.Color));
      this.smoke2 = this.add(new ColorSetting("Color2", new Color(-1, true), () -> this.page.getValue() == ShaderModule.Page.Color));
      this.smoke3 = this.add(new ColorSetting("Color3", new Color(-197380, true), () -> this.page.getValue() == ShaderModule.Page.Color));
      this.smoke4 = this.add(new ColorSetting("Color4", new Color(-772, true), () -> this.page.getValue() == ShaderModule.Page.Color));
      this.hands = this.add(new BooleanSetting("Hands", true, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.self = this.add(new BooleanSetting("Self", false, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.players = this.add(new BooleanSetting("Players", true, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.friends = this.add(new BooleanSetting("Friends", true, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.crystals = this.add(new BooleanSetting("Crystals", true, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.creatures = this.add(new BooleanSetting("Creatures", false, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.monsters = this.add(new BooleanSetting("Monsters", true, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.ambients = this.add(new BooleanSetting("Ambients", false, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.items = this.add(new BooleanSetting("Items", true, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.others = this.add(new BooleanSetting("Others", true, () -> this.page.getValue() == ShaderModule.Page.Target));
      this.setChinese("着色器");
      INSTANCE = this;
   }

   public String getInfo() {
      return ((ShaderManager.Shader)this.mode.getValue()).name();
   }

   public boolean shouldRender(class_1297 entity) {
      if (entity == null) {
         return false;
      } else if (mc.field_1724 == null) {
         return false;
      } else if ((double)class_3532.method_15355((float)mc.field_1724.method_5707(entity.method_19538())) > this.maxRange.getValue()) {
         return false;
      } else if (entity instanceof class_1657) {
         class_1657 playerEntity = (class_1657)entity;
         if (entity == mc.field_1724) {
            return this.self.getValue();
         } else {
            return Astra.FRIEND.isFriend(playerEntity) ? this.friends.getValue() : this.players.getValue();
         }
      } else if (entity instanceof class_1511) {
         return this.crystals.getValue();
      } else if (entity instanceof class_1542) {
         return this.items.getValue();
      } else {
         boolean var10000;
         switch (entity.method_5864().method_5891()) {
            case field_6294:
            case field_6300:
               var10000 = this.creatures.getValue();
               break;
            case field_6302:
               var10000 = this.monsters.getValue();
               break;
            case field_6303:
            case field_24460:
               var10000 = this.ambients.getValue();
               break;
            default:
               var10000 = this.others.getValue();
         }

         return var10000;
      }
   }

   public void onRender3D(class_4587 matrixStack) {
      if (this.hands.getValue()) {
         Astra.SHADER.renderShader(() -> ((IGameRenderer)mc.field_1773).IRenderHand(mc.field_1773.method_19418(), mc.method_60646().method_60637(true), matrixStack.method_23760().method_23761()), this.mode.getValue());
      }

   }

   public void onToggle() {
      Astra.SHADER.reloadShaders();
   }

   public void onLogin() {
      Astra.SHADER.reloadShaders();
   }

   private static enum Page {
      Shader,
      Target,
      Color;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{Shader, Target, Color};
      }
   }
}
