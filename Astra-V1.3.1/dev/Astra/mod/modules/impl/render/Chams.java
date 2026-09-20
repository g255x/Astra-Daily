package dev.Astra.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.Render3DEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_1308;
import net.minecraft.class_1429;
import net.minecraft.class_1511;
import net.minecraft.class_1621;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_3989;

public class Chams extends Module {
   public static Chams INSTANCE;
   public final BooleanSetting crystal = this.add((new BooleanSetting("Crystal", true)).setParent());
   public final BooleanSetting custom;
   public final BooleanSetting depth;
   public final BooleanSetting chamsTexture;
   public final ColorSetting fill;
   public final ColorSetting line;
   public final ColorSetting core;
   public final ColorSetting outerFrame;
   public final ColorSetting innerFrame;
   public final BooleanSetting glint;
   public final BooleanSetting texture;
   public final BooleanSetting spinSync;
   public final SliderSetting scale;
   public final SliderSetting spinValue;
   public final SliderSetting bounceHeight;
   public final SliderSetting floatValue;
   public final SliderSetting floatOffset;
   public final BooleanSetting throughWall;
   private final BooleanSetting Crystals;
   private final BooleanSetting Players;
   private final BooleanSetting Mobs;
   private final BooleanSetting Animals;
   private final BooleanSetting Villagers;
   private final BooleanSetting Slimes;
   public final ColorSetting hand;
   public int age;

   public Chams() {
      super("Chams", Module.Category.Render);
      BooleanSetting var10006 = this.crystal;
      Objects.requireNonNull(var10006);
      this.custom = this.add((new BooleanSetting("Custom", false, var10006::isOpen)).setParent());
      this.depth = this.add(new BooleanSetting("Depth", false, () -> this.crystal.isOpen() && this.custom.isOpen()));
      this.chamsTexture = this.add(new BooleanSetting("ChamsTexture", true, () -> this.crystal.isOpen() && this.custom.isOpen()));
      this.fill = this.add((new ColorSetting("Fill", new Color(255, 255, 255, 100), () -> this.crystal.isOpen() && this.custom.isOpen())).injectBoolean(false));
      this.line = this.add((new ColorSetting("Line", new Color(255, 255, 255, 100), () -> this.crystal.isOpen() && this.custom.isOpen())).injectBoolean(false));
      Color var10005 = new Color(255, 255, 255, 255);
      var10006 = this.crystal;
      Objects.requireNonNull(var10006);
      this.core = this.add((new ColorSetting("Core", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(255, 255, 255, 255);
      var10006 = this.crystal;
      Objects.requireNonNull(var10006);
      this.outerFrame = this.add((new ColorSetting("OuterFrame", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(255, 255, 255, 255);
      var10006 = this.crystal;
      Objects.requireNonNull(var10006);
      this.innerFrame = this.add((new ColorSetting("InnerFrame", var10005, var10006::isOpen)).injectBoolean(true));
      var10006 = this.crystal;
      Objects.requireNonNull(var10006);
      this.glint = this.add(new BooleanSetting("Glint", true, var10006::isOpen));
      var10006 = this.crystal;
      Objects.requireNonNull(var10006);
      this.texture = this.add(new BooleanSetting("Texture", true, var10006::isOpen));
      var10006 = this.crystal;
      Objects.requireNonNull(var10006);
      this.spinSync = this.add(new BooleanSetting("SpinSync", false, var10006::isOpen));
      BooleanSetting var10009 = this.crystal;
      Objects.requireNonNull(var10009);
      this.scale = this.add(new SliderSetting("Scale", (double)1.0F, (double)0.0F, (double)3.0F, 0.01, var10009::isOpen));
      var10009 = this.crystal;
      Objects.requireNonNull(var10009);
      this.spinValue = this.add(new SliderSetting("SpinSpeed", (double)1.0F, (double)0.0F, (double)3.0F, 0.01, var10009::isOpen));
      var10009 = this.crystal;
      Objects.requireNonNull(var10009);
      this.bounceHeight = this.add(new SliderSetting("BounceHeight", (double)1.0F, (double)0.0F, (double)3.0F, 0.01, var10009::isOpen));
      var10009 = this.crystal;
      Objects.requireNonNull(var10009);
      this.floatValue = this.add(new SliderSetting("BounceSpeed", (double)1.0F, (double)0.0F, (double)3.0F, 0.01, var10009::isOpen));
      var10009 = this.crystal;
      Objects.requireNonNull(var10009);
      this.floatOffset = this.add(new SliderSetting("YOffset", (double)0.0F, (double)-1.0F, (double)1.0F, 0.01, var10009::isOpen));
      this.throughWall = this.add((new BooleanSetting("ThroughWall", false)).setParent());
      var10006 = this.throughWall;
      Objects.requireNonNull(var10006);
      this.Crystals = this.add(new BooleanSetting("Crystals", true, var10006::isOpen));
      var10006 = this.throughWall;
      Objects.requireNonNull(var10006);
      this.Players = this.add(new BooleanSetting("Players", true, var10006::isOpen));
      var10006 = this.throughWall;
      Objects.requireNonNull(var10006);
      this.Mobs = this.add(new BooleanSetting("Mobs", true, var10006::isOpen));
      var10006 = this.throughWall;
      Objects.requireNonNull(var10006);
      this.Animals = this.add(new BooleanSetting("Animals", true, var10006::isOpen));
      var10006 = this.throughWall;
      Objects.requireNonNull(var10006);
      this.Villagers = this.add(new BooleanSetting("Villagers", true, var10006::isOpen));
      var10006 = this.throughWall;
      Objects.requireNonNull(var10006);
      this.Slimes = this.add(new BooleanSetting("Slimes", true, var10006::isOpen));
      this.hand = this.add((new ColorSetting("Hand", -1)).injectBoolean(true));
      this.setChinese("模型上色");
      INSTANCE = this;
   }

   public boolean customCrystal() {
      return this.isOn() && this.crystal.getValue();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      ++this.age;
   }

   @EventListener(
      priority = Integer.MIN_VALUE
   )
   public void onRender3D(Render3DEvent event) {
      if (this.hand.booleanValue) {
         Color color = this.hand.getValue();
         RenderSystem.setShaderColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      }

   }

   public boolean chams(class_1297 entity) {
      if (entity instanceof class_1511) {
         return this.Crystals.getValue();
      } else if (entity instanceof class_1621) {
         return this.Slimes.getValue();
      } else if (entity instanceof class_1657) {
         return this.Players.getValue();
      } else if (!(entity instanceof class_1646) && !(entity instanceof class_3989)) {
         if (entity instanceof class_1429) {
            return this.Animals.getValue();
         } else {
            return entity instanceof class_1308 ? this.Mobs.getValue() : false;
         }
      } else {
         return this.Villagers.getValue();
      }
   }
}
