package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.PreRender2DEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_2761;

public class Ambience extends Module {
   public static Ambience INSTANCE;
   public final ColorSetting filter = this.add((new ColorSetting("Filter", new Color(255, 255, 255, 20))).injectBoolean(false));
   public final ColorSetting worldColor = this.add((new ColorSetting("WorldColor", new Color(-1, true))).injectBoolean(true));
   public final BooleanSetting customTime = this.add((new BooleanSetting("CustomTime", false)).setParent());
   public final SliderSetting time;
   public final ColorSetting fog;
   public final ColorSetting sky;
   public final ColorSetting cloud;
   public final ColorSetting dimensionColor;
   public final BooleanSetting fogDistance;
   public final SliderSetting fogStart;
   public final SliderSetting fogEnd;
   public final BooleanSetting fullBright;
   public final BooleanSetting forceOverworld;
   public final BooleanSetting customLuminance;
   public final SliderSetting luminance;
   long oldTime;

   public Ambience() {
      super("Ambience", "Custom ambience", Module.Category.Render);
      BooleanSetting var10008 = this.customTime;
      Objects.requireNonNull(var10008);
      this.time = this.add(new SliderSetting("Time", 0, 0, 24000, var10008::isOpen));
      this.fog = this.add((new ColorSetting("FogColor", new Color(13401557))).injectBoolean(false));
      this.sky = this.add((new ColorSetting("SkyColor", new Color(0))).injectBoolean(false));
      this.cloud = this.add((new ColorSetting("CloudColor", new Color(0))).injectBoolean(false));
      this.dimensionColor = this.add((new ColorSetting("DimensionColor", new Color(0))).injectBoolean(false));
      this.fogDistance = this.add((new BooleanSetting("FogDistance", false)).setParent());
      var10008 = this.fogDistance;
      Objects.requireNonNull(var10008);
      this.fogStart = this.add(new SliderSetting("FogStart", 50, 0, 1000, var10008::isOpen));
      var10008 = this.fogDistance;
      Objects.requireNonNull(var10008);
      this.fogEnd = this.add(new SliderSetting("FogEnd", 100, 0, 1000, var10008::isOpen));
      this.fullBright = this.add(new BooleanSetting("FullBright", false));
      this.forceOverworld = this.add(new BooleanSetting("ForceOverworld", false));
      this.customLuminance = this.add((new BooleanSetting("CustomLuminance", false)).setParent().injectTask(() -> {
         if (!nullCheck()) {
            mc.field_1769.method_3279();
         }

      }));
      var10008 = this.customLuminance;
      Objects.requireNonNull(var10008);
      this.luminance = this.add((new SliderSetting("Luminance", 15, 0, 15, var10008::isOpen)).injectTask(() -> {
         if (!nullCheck() && this.customLuminance.getValue()) {
            mc.field_1769.method_3279();
         }

      }));
      this.setChinese("自定义环境");
      INSTANCE = this;
   }

   @EventListener
   public void onRender2D(PreRender2DEvent event) {
      if (this.filter.booleanValue) {
         event.drawContext.method_25294(0, 0, mc.method_22683().method_4486(), mc.method_22683().method_4502(), this.filter.getValue().getRGB());
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.customTime.getValue()) {
         mc.field_1687.method_8435((long)this.time.getValue());
      }

   }

   public boolean onEnable() {
      if (!nullCheck()) {
         this.oldTime = mc.field_1687.method_8532();
         if (this.customTime.getValue()) {
            mc.field_1687.method_8435((long)this.time.getValue());
         }
      }

      return false;
   }

   public void onDisable() {
      if (!nullCheck()) {
         mc.field_1687.method_8435(this.oldTime);
      }

   }

   @EventListener
   public void onReceivePacket(PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_2761 && this.customTime.getValue()) {
         this.oldTime = ((class_2761)event.getPacket()).method_11871();
         event.cancel();
      }

   }
}
