package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_1747;
import net.minecraft.class_2246;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_4587;

public class AirPlace extends Module {
   public static AirPlace INSTANCE;
   public final BooleanSetting module = this.add(new BooleanSetting("Module", true));
   public final BooleanSetting grimBypass = this.add(new BooleanSetting("GrimBypass", false));
   public final BooleanSetting crossHair = this.add((new BooleanSetting("Crosshair", true)).setParent());
   private final SliderSetting range;
   private final ColorSetting fill;
   private final ColorSetting box;
   private class_3965 hit;
   private int cooldown;

   public AirPlace() {
      super("AirPlace", Module.Category.Player);
      BooleanSetting var10008 = this.crossHair;
      Objects.requireNonNull(var10008);
      this.range = this.add(new SliderSetting("Range", (double)5.0F, (double)0.0F, (double)6.0F, var10008::isOpen));
      Color var10005 = new Color(255, 0, 0, 50);
      BooleanSetting var10006 = this.crossHair;
      Objects.requireNonNull(var10006);
      this.fill = this.add((new ColorSetting("Fill", var10005, var10006::isOpen)).injectBoolean(true));
      var10005 = new Color(255, 0, 0, 100);
      var10006 = this.crossHair;
      Objects.requireNonNull(var10006);
      this.box = this.add((new ColorSetting("Box", var10005, var10006::isOpen)).injectBoolean(true));
      this.setChinese("空气放置");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.crossHair.getValue()) {
         if (this.cooldown > 0) {
            --this.cooldown;
         }

         class_239 var3 = mc.method_1560().method_5745(this.range.getValue(), 0.0F, false);
         if (var3 instanceof class_3965) {
            class_3965 bhr = (class_3965)var3;
            this.hit = bhr;
         } else {
            this.hit = null;
         }

         if (this.hit == null || !mc.field_1687.method_8320(this.hit.method_17777()).method_26204().equals(class_2246.field_10124) || !(mc.field_1724.method_6047().method_7909() instanceof class_1747)) {
            return;
         }

         boolean main = mc.field_1724.method_6047().method_7909() instanceof class_1747;
         if (mc.field_1690.field_1904.method_1434() && main && this.cooldown <= 0) {
            BlockUtil.airPlace(this.hit.method_17777(), false);
            this.cooldown = 2;
         }
      }

   }

   public void onRender3D(class_4587 stack) {
      if (this.crossHair.getValue()) {
         if (this.hit == null || !mc.field_1687.method_8320(this.hit.method_17777()).method_26204().equals(class_2246.field_10124) || !(mc.field_1724.method_6047().method_7909() instanceof class_1747)) {
            return;
         }

         Render3DUtil.draw3DBox(stack, new class_238(this.hit.method_17777()), this.fill.getValue(), this.box.getValue(), this.box.booleanValue, this.fill.booleanValue);
      }

   }
}
