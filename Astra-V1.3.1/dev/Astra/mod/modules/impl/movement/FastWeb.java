package dev.Astra.mod.modules.impl.movement;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.TimerEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2560;
import net.minecraft.class_2846;
import net.minecraft.class_2846.class_2847;

public class FastWeb extends Module {
   public static FastWeb INSTANCE;
   public final EnumSetting<Mode> mode;
   public final BooleanSetting onlySneak;
   public final BooleanSetting grim;
   public final BooleanSetting abortPacket;
   public final SliderSetting xZSlow;
   public final SliderSetting ySlow;
   private final SliderSetting fastSpeed;
   private boolean work;

   public FastWeb() {
      super("FastWeb", "So you don't need to keep timer on keybind", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", FastWeb.Mode.Vanilla));
      this.onlySneak = this.add(new BooleanSetting("OnlySneak", true));
      this.grim = this.add((new BooleanSetting("Grim", false)).setParent());
      BooleanSetting var10006 = this.grim;
      Objects.requireNonNull(var10006);
      this.abortPacket = this.add(new BooleanSetting("AbortPacket", true, var10006::isOpen));
      this.xZSlow = this.add((new SliderSetting("XZSpeed", (double)25.0F, (double)0.0F, (double)100.0F, 0.1, () -> this.mode.getValue() == FastWeb.Mode.Custom)).setSuffix("%"));
      this.ySlow = this.add((new SliderSetting("YSpeed", (double)100.0F, (double)0.0F, (double)100.0F, 0.1, () -> this.mode.getValue() == FastWeb.Mode.Custom)).setSuffix("%"));
      this.fastSpeed = this.add(new SliderSetting("Speed", (double)3.0F, (double)0.0F, (double)8.0F, () -> this.mode.getValue() == FastWeb.Mode.Vanilla || this.mode.getValue() == FastWeb.Mode.Strict));
      this.work = false;
      this.setChinese("蜘蛛网加速");
      INSTANCE = this;
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      boolean bl = this.work = !mc.field_1724.method_24828() && (mc.field_1690.field_1832.method_1434() || !this.onlySneak.getValue()) && Astra.PLAYER.isInWeb(mc.field_1724);
      if (this.work && this.mode.is(FastWeb.Mode.Vanilla)) {
         MovementUtil.setMotionY(-this.fastSpeed.getValue());
      }

      if (this.grim.getValue() && (mc.field_1690.field_1832.method_1434() || !this.onlySneak.getValue())) {
         for(class_2338 pos : this.getIntersectingWebs()) {
            if (this.abortPacket.getValue()) {
               mc.method_1562().method_52787(new class_2846(class_2847.field_12971, pos, class_2350.field_11033));
            }

            mc.method_1562().method_52787(new class_2846(class_2847.field_12973, pos, class_2350.field_11033));
         }
      }

   }

   @EventListener(
      priority = -100
   )
   public void onTimer(TimerEvent event) {
      if (this.work && this.mode.getValue() == FastWeb.Mode.Strict) {
         event.set(this.fastSpeed.getValueFloat());
      }

   }

   public List<class_2338> getIntersectingWebs() {
      int radius = 2;
      ArrayList<class_2338> blocks = new ArrayList();

      for(int x = radius; x > -radius; --x) {
         for(int y = radius; y > -radius; --y) {
            for(int z = radius; z > -radius; --z) {
               class_2338 blockPos = class_2338.method_49637(mc.field_1724.method_23317() + (double)x, mc.field_1724.method_23318() + (double)y, mc.field_1724.method_23321() + (double)z);
               if ((!(mc.field_1724.method_19538().method_1022(blockPos.method_46558()) > (double)1.0F) || !(mc.field_1724.method_33571().method_1022(blockPos.method_46558()) > (double)1.0F)) && mc.field_1687.method_8320(blockPos).method_26204() instanceof class_2560) {
                  blocks.add(blockPos);
               }
            }
         }
      }

      return blocks;
   }

   public static enum Mode {
      Vanilla,
      Strict,
      Custom,
      Ignore;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Vanilla, Strict, Custom, Ignore};
      }
   }
}
