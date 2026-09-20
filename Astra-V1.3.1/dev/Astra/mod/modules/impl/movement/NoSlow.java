package dev.Astra.mod.modules.impl.movement;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.InteractItemEvent;
import dev.Astra.api.events.impl.KeyboardInputEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.class_1268;
import net.minecraft.class_1713;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2886;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import net.minecraft.class_408;
import net.minecraft.class_9334;
import net.minecraft.class_2846.class_2847;
import net.minecraft.class_2848.class_2849;

public class NoSlow extends Module {
   public static NoSlow INSTANCE;
   final Queue<class_2813> storedClicks = new LinkedList();
   final AtomicBoolean pause = new AtomicBoolean();
   private final EnumSetting<Mode> mode;
   private final BooleanSetting soulSand;
   private final BooleanSetting sneak;
   private final BooleanSetting climb;
   private final BooleanSetting gui;
   private final BooleanSetting allowSneak;
   private final EnumSetting<Bypass> clickBypass;
   boolean using;
   int delay;

   public NoSlow() {
      super("NoSlow", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", NoSlow.Mode.Vanilla));
      this.soulSand = this.add(new BooleanSetting("SoulSand", true));
      this.sneak = this.add(new BooleanSetting("Sneak", false));
      this.climb = this.add(new BooleanSetting("Climb", false));
      this.gui = this.add(new BooleanSetting("Gui", true));
      BooleanSetting var10006 = this.gui;
      Objects.requireNonNull(var10006);
      this.allowSneak = this.add(new BooleanSetting("AllowSneak", false, var10006::getValue));
      this.clickBypass = this.add(new EnumSetting("GuiMoveBypass", NoSlow.Bypass.None));
      this.using = false;
      this.delay = 0;
      this.setChinese("无减速");
      INSTANCE = this;
   }

   private static float getMovementMultiplier(boolean positive, boolean negative) {
      if (positive == negative) {
         return 0.0F;
      } else {
         return positive ? 1.0F : -1.0F;
      }
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.using = mc.field_1724.method_6115();
      --this.delay;
      if (this.using) {
         this.delay = 2;
      }

      if (this.using && !mc.field_1724.method_3144() && !mc.field_1724.method_6128()) {
         switch (((Mode)this.mode.getValue()).ordinal()) {
            case 1:
               mc.method_1562().method_52787(new class_2868(mc.field_1724.method_31548().field_7545));
               break;
            case 2:
               if (mc.field_1724.method_6058() == class_1268.field_5810) {
                  sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
               } else {
                  sendSequencedPacket((id) -> new class_2886(class_1268.field_5810, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
               }
               break;
            case 3:
               mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 1, 0, class_1713.field_7790, mc.field_1724);
               if (mc.field_1724.method_6058() == class_1268.field_5810) {
                  sendSequencedPacket((id) -> new class_2886(class_1268.field_5808, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
               } else {
                  sendSequencedPacket((id) -> new class_2886(class_1268.field_5810, id, Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch()));
               }
         }
      }

      if (this.gui.getValue() && !(mc.field_1755 instanceof class_408)) {
         for(class_304 k : new class_304[]{mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849}) {
            k.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(k.method_1428()).method_1444()));
         }

         mc.field_1690.field_1903.method_23481(ElytraFly.INSTANCE.isOn() && ElytraFly.INSTANCE.mode.is(ElytraFly.Mode.Bounce) && ElytraFly.INSTANCE.autoJump.getValue() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1903.method_1428()).method_1444()));
         mc.field_1690.field_1894.method_23481(AutoWalk.INSTANCE.forward() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1894.method_1428()).method_1444()));
         mc.field_1690.field_1867.method_23481(Sprint.INSTANCE.isOn() && !Sprint.INSTANCE.inWater() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1867.method_1428()).method_1444()));
         if (this.allowSneak.getValue()) {
            mc.field_1690.field_1832.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1832.method_1428()).method_1444()));
         }
      }

   }

   @EventListener(
      priority = 100
   )
   public void keyboard(KeyboardInputEvent event) {
      if (this.sneak.getValue()) {
         event.cancel();
      }

      if (this.gui.getValue() && !(mc.field_1755 instanceof class_408)) {
         for(class_304 k : new class_304[]{mc.field_1690.field_1881, mc.field_1690.field_1913, mc.field_1690.field_1849}) {
            k.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(k.method_1428()).method_1444()));
         }

         mc.field_1690.field_1903.method_23481(ElytraFly.INSTANCE.isOn() && ElytraFly.INSTANCE.mode.is(ElytraFly.Mode.Bounce) && ElytraFly.INSTANCE.autoJump.getValue() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1903.method_1428()).method_1444()));
         mc.field_1690.field_1894.method_23481(AutoWalk.INSTANCE.forward() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1894.method_1428()).method_1444()));
         mc.field_1690.field_1867.method_23481(Sprint.INSTANCE.isOn() && !Sprint.INSTANCE.inWater() || class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1867.method_1428()).method_1444()));
         if (this.allowSneak.getValue()) {
            mc.field_1690.field_1832.method_23481(class_3675.method_15987(mc.method_22683().method_4490(), class_3675.method_15981(mc.field_1690.field_1832.method_1428()).method_1444()));
         }

         mc.field_1724.field_3913.field_3910 = mc.field_1690.field_1894.method_1434();
         mc.field_1724.field_3913.field_3909 = mc.field_1690.field_1881.method_1434();
         mc.field_1724.field_3913.field_3908 = mc.field_1690.field_1913.method_1434();
         mc.field_1724.field_3913.field_3906 = mc.field_1690.field_1849.method_1434();
         mc.field_1724.field_3913.field_3905 = getMovementMultiplier(mc.field_1724.field_3913.field_3910, mc.field_1724.field_3913.field_3909);
         mc.field_1724.field_3913.field_3907 = getMovementMultiplier(mc.field_1724.field_3913.field_3908, mc.field_1724.field_3913.field_3906);
         mc.field_1724.field_3913.field_3904 = mc.field_1690.field_1903.method_1434();
         mc.field_1724.field_3913.field_3903 = mc.field_1690.field_1832.method_1434();
      }

   }

   @EventListener
   public void onUse(InteractItemEvent event) {
      if (event.isPre()) {
         if (this.delay > 0) {
            mc.field_1752 = 0;
            event.cancel();
         } else if (this.mode.is(NoSlow.Mode.GrimPacket) && mc.field_1724 != null && mc.field_1724.method_5998(event.hand).method_7909().method_57347().method_57832(class_9334.field_50075)) {
            mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, 1, 0, class_1713.field_7790, mc.field_1724);
         }
      }

   }

   @EventListener
   public void onPacketSend(PacketEvent.Send e) {
      if (!nullCheck()) {
         class_2886 packet;
         class_2596<?> packet2;
         if (this.mode.is(NoSlow.Mode.Drop) && (packet2 = e.getPacket()) instanceof class_2886 && (packet = (class_2886)packet2).method_12551() == class_1268.field_5808 && mc.field_1724.method_6047().method_7909().method_57347().method_57832(class_9334.field_50075)) {
            mc.method_1562().method_52787(new class_2846(class_2847.field_12975, class_2338.field_10980, class_2350.field_11033));
         } else if (MovementUtil.isMoving() && !this.pause.get()) {
            packet2 = e.getPacket();
            if (packet2 instanceof class_2813) {
               class_2813 click = (class_2813)packet2;
               switch (((Bypass)this.clickBypass.getValue()).ordinal()) {
                  case 1:
                     if (mc.field_1724.method_24828() && !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989((double)0.0F, 0.0656, (double)0.0F)).iterator().hasNext()) {
                        if (mc.field_1724.method_5624()) {
                           mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12985));
                        }

                        mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.0656, mc.field_1724.method_23321(), false));
                     }
                     break;
                  case 2:
                     if (mc.field_1724.method_24828() && !mc.field_1687.method_20812(mc.field_1724, mc.field_1724.method_5829().method_989((double)0.0F, 2.71875E-7, (double)0.0F)).iterator().hasNext()) {
                        if (mc.field_1724.method_5624()) {
                           mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12985));
                        }

                        mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.71875E-7, mc.field_1724.method_23321(), false));
                     }
                     break;
                  case 3:
                     if (click.method_12195() != class_1713.field_7790 && click.method_12195() != class_1713.field_7793) {
                        mc.method_1562().method_52787(new class_2815(0));
                     }
                     break;
                  case 4:
                     this.storedClicks.add(click);
                     e.cancel();
               }
            }

            if (e.getPacket() instanceof class_2815 && this.clickBypass.is(NoSlow.Bypass.Delay)) {
               this.pause.set(true);

               while(!this.storedClicks.isEmpty()) {
                  mc.method_1562().method_52787((class_2596)this.storedClicks.poll());
               }

               this.pause.set(false);
            }

         }
      }
   }

   @EventListener
   public void onPacketSendPost(PacketEvent.Sent e) {
      if (e.getPacket() instanceof class_2813 && mc.field_1724.method_5624() && this.clickBypass.is(NoSlow.Bypass.NCP)) {
         mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12981));
      }

   }

   public boolean noSlow() {
      return this.isOn() && this.mode.getValue() != NoSlow.Mode.None && (this.mode.getValue() != NoSlow.Mode.Drop && this.mode.getValue() != NoSlow.Mode.GrimPacket || this.using);
   }

   public boolean soulSand() {
      return this.isOn() && this.soulSand.getValue();
   }

   public boolean climb() {
      return this.isOn() && this.climb.getValue();
   }

   public static enum Mode {
      Vanilla,
      NCP,
      Grim,
      GrimPacket,
      Drop,
      None;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Vanilla, NCP, Grim, GrimPacket, Drop, None};
      }
   }

   private static enum Bypass {
      None,
      NCP,
      NCP2,
      Grim,
      Delay;

      // $FF: synthetic method
      private static Bypass[] $values() {
         return new Bypass[]{None, NCP, NCP2, Grim, Delay};
      }
   }
}
