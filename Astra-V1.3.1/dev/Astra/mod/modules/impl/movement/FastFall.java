package dev.Astra.mod.modules.impl.movement;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.MoveEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.TimerEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.HashMap;
import java.util.Objects;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2708;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class FastFall extends Module {
   private final EnumSetting<Mode> mode;
   private final BooleanSetting noLag;
   private final BooleanSetting useTimerSetting;
   private final SliderSetting timer;
   private final BooleanSetting anchor;
   private final SliderSetting height;
   private final Timer lagTimer;
   boolean onGround;
   private boolean useTimer;

   public FastFall() {
      super("FastFall", "Miyagi son simulator", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", FastFall.Mode.Fast));
      this.noLag = this.add(new BooleanSetting("NoLag", true, () -> this.mode.getValue() == FastFall.Mode.Fast));
      this.useTimerSetting = this.add(new BooleanSetting("UseTimer", false));
      BooleanSetting var10009 = this.useTimerSetting;
      Objects.requireNonNull(var10009);
      this.timer = this.add(new SliderSetting("Timer", (double)2.5F, (double)1.0F, (double)8.0F, 0.1, var10009::getValue));
      this.anchor = this.add(new BooleanSetting("Anchor", true));
      this.height = this.add(new SliderSetting("Height", (double)10.0F, (double)1.0F, (double)20.0F, (double)0.5F));
      this.lagTimer = new Timer();
      this.onGround = false;
      this.setChinese("快速坠落");
   }

   public void onDisable() {
      this.useTimer = false;
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   @EventListener(
      priority = -100
   )
   public void onMove(MoveEvent event) {
      if (!nullCheck()) {
         if (mc.field_1724.method_24828() && this.anchor.getValue() && this.traceDown() != 0 && (double)this.traceDown() <= this.height.getValue() && this.trace()) {
            event.setX(event.getX() * 0.05);
            event.setZ(event.getZ() * 0.05);
         }

      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if ((!(this.height.getValue() > (double)0.0F) || !((double)this.traceDown() > this.height.getValue())) && !mc.field_1724.method_5757() && !mc.field_1724.method_5869() && !mc.field_1724.method_5771() && !mc.field_1724.method_21754() && this.lagTimer.passed(1000L) && !mc.field_1724.method_6128() && !Fly.INSTANCE.isOn() && !nullCheck()) {
         if (!Astra.PLAYER.isInWeb(mc.field_1724)) {
            if (mc.field_1724.method_24828() && this.mode.getValue() == FastFall.Mode.Fast) {
               MovementUtil.setMotionY(MovementUtil.getMotionY() - (double)(this.noLag.getValue() ? 0.62F : 1.0F));
            }

            if (this.useTimerSetting.getValue()) {
               if (!mc.field_1724.method_24828()) {
                  if (this.onGround) {
                     this.useTimer = true;
                  }

                  if (MovementUtil.getMotionY() >= (double)0.0F) {
                     this.useTimer = false;
                  }

                  this.onGround = false;
               } else {
                  this.useTimer = false;
                  MovementUtil.setMotionY(-0.08);
                  this.onGround = true;
               }
            } else {
               this.useTimer = false;
            }

         }
      }
   }

   @EventListener
   public void onTimer(TimerEvent event) {
      if (!nullCheck()) {
         if (!mc.field_1724.method_24828() && this.useTimer) {
            event.set(this.timer.getValueFloat());
         }

      }
   }

   @EventListener
   public void onPacket(PacketEvent.Receive event) {
      if (!nullCheck() && event.getPacket() instanceof class_2708) {
         this.lagTimer.reset();
      }

   }

   private int traceDown() {
      int retval = 0;

      for(int tracey = (int)Math.round(mc.field_1724.method_23318()) - 1; tracey >= 0; --tracey) {
         class_3965 trace = mc.field_1687.method_17742(new class_3959(mc.field_1724.method_19538(), new class_243(mc.field_1724.method_23317(), (double)tracey, mc.field_1724.method_23321()), class_3960.field_17558, class_242.field_1348, mc.field_1724));
         if (trace != null && trace.method_17783() == class_240.field_1332) {
            return retval;
         }

         ++retval;
      }

      return retval;
   }

   private boolean trace() {
      class_238 bbox = mc.field_1724.method_5829();
      class_243 basepos = bbox.method_1005();
      double minX = bbox.field_1323;
      double minZ = bbox.field_1321;
      double maxX = bbox.field_1320;
      double maxZ = bbox.field_1324;
      HashMap<class_243, class_243> positions = new HashMap();
      positions.put(basepos, new class_243(basepos.field_1352, basepos.field_1351 - (double)1.0F, basepos.field_1350));
      positions.put(new class_243(minX, basepos.field_1351, minZ), new class_243(minX, basepos.field_1351 - (double)1.0F, minZ));
      positions.put(new class_243(maxX, basepos.field_1351, minZ), new class_243(maxX, basepos.field_1351 - (double)1.0F, minZ));
      positions.put(new class_243(minX, basepos.field_1351, maxZ), new class_243(minX, basepos.field_1351 - (double)1.0F, maxZ));
      positions.put(new class_243(maxX, basepos.field_1351, maxZ), new class_243(maxX, basepos.field_1351 - (double)1.0F, maxZ));

      for(class_243 key : positions.keySet()) {
         class_3959 context = new class_3959(key, (class_243)positions.get(key), class_3960.field_17558, class_242.field_1348, mc.field_1724);
         class_3965 result = mc.field_1687.method_17742(context);
         if (result != null && result.method_17783() == class_240.field_1332) {
            return false;
         }
      }

      class_2680 state = mc.field_1687.method_8320(new BlockPosX(mc.field_1724.method_23317(), mc.field_1724.method_23318() - (double)1.0F, mc.field_1724.method_23321()));
      return state.method_26215();
   }

   private static enum Mode {
      Fast,
      None;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Fast, None};
      }
   }
}
