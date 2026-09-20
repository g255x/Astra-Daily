package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.rotation.RotationUtils;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import io.netty.buffer.Unpooled;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_2246;
import net.minecraft.class_2540;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2828;

public class CriticalsPlus extends Module {
   public static CriticalsPlus INSTANCE;
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
   public final EnumSetting<Mode> mode;
   boolean canGrimCrit;

   public CriticalsPlus() {
      super("Criticals+", Module.Category.Combat);
      this.mode = this.add(new EnumSetting("Mode", CriticalsPlus.Mode.OldNCP));
      this.canGrimCrit = false;
      this.setChinese("刀爆+");
      INSTANCE = this;
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   @EventListener
   public void onPacketSend(PacketEvent.Send event) {
      class_2596 var4 = event.getPacket();
      if (var4 instanceof class_2824 packet) {
         class_1297 entity;
         if (getInteractType(packet) == CriticalsPlus.InteractType.ATTACK && !((entity = getEntity(packet)) instanceof class_1511) && (!this.onlyGround.getValue() || mc.field_1724.method_24828() || mc.field_1724.method_31549().field_7479) && !mc.field_1724.method_5771() && !mc.field_1724.method_5869() && entity != null) {
            mc.field_1724.method_7277(entity);
            this.doCrit();
         }
      }

   }

   public void doCrit() {
      if (this.mode.getValue() == CriticalsPlus.Mode.Strict && mc.field_1687.method_8320(mc.field_1724.method_24515()).method_26204() != class_2246.field_10343) {
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.062600301692775, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.07260029960661, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
      } else if (this.mode.getValue() == CriticalsPlus.Mode.NCP) {
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)0.0625F, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
      } else if (this.mode.getValue() == CriticalsPlus.Mode.OldNCP) {
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.058293536E-5, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 9.16580235E-6, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0371854E-7, mc.field_1724.method_23321(), false));
      } else if (this.mode.getValue() == CriticalsPlus.Mode.NewNCP) {
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.71875E-7, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
      } else if (this.mode.is(CriticalsPlus.Mode.Hypixel2K22)) {
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.0045, mc.field_1724.method_23321(), true));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.52121E-4, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.3, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.025, mc.field_1724.method_23321(), false));
      } else if (this.mode.is(CriticalsPlus.Mode.Packet)) {
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 5.0E-4, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-4, mc.field_1724.method_23321(), false));
      } else if (this.mode.is(CriticalsPlus.Mode.BBTT)) {
         if (MovementUtil.isMoving() || !MovementUtil.isStatic()) {
            return;
         }

         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)0.0625F, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.045, mc.field_1724.method_23321(), false));
      } else if (this.mode.getValue() == CriticalsPlus.Mode.LowPacket) {
         mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.71875E-7, mc.field_1724.method_23321(), false));
         mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
      } else if (this.mode.is(CriticalsPlus.Mode.Grim)) {
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)0.0625F, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.04535, mc.field_1724.method_23321(), false));
      } else if (this.mode.is(CriticalsPlus.Mode.GrimV2)) {
         mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() - (double)1.0E-4F, mc.field_1724.method_23321(), RotationUtils.getActualYaw(), RotationUtils.getActualPitch(), false));
      } else if (this.mode.is(CriticalsPlus.Mode.GrimCC)) {
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)0.0625F, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.0625013579, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.3579E-6, mc.field_1724.method_23321(), false));
      } else if (this.mode.getValue() == CriticalsPlus.Mode.New2b2t) {
         mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.71875E-7, mc.field_1724.method_23321(), false));
         mc.field_1724.field_3944.method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
      }

   }

   public static class_1297 getEntity(class_2824 packet) {
      class_2540 packetBuf = new class_2540(Unpooled.buffer());
      packet.method_55976(packetBuf);
      return mc.field_1687 == null ? null : mc.field_1687.method_8469(packetBuf.method_10816());
   }

   public static InteractType getInteractType(class_2824 packet) {
      class_2540 packetBuf = new class_2540(Unpooled.buffer());
      packet.method_55976(packetBuf);
      packetBuf.method_10816();
      return (InteractType)packetBuf.method_10818(InteractType.class);
   }

   public static enum Mode {
      NewNCP,
      Strict,
      NCP,
      OldNCP,
      Hypixel2K22,
      Packet,
      BBTT,
      LowPacket,
      GrimCC,
      GrimV2,
      Grim,
      New2b2t,
      Grimv3;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{NewNCP, Strict, NCP, OldNCP, Hypixel2K22, Packet, BBTT, LowPacket, GrimCC, GrimV2, Grim, New2b2t, Grimv3};
      }
   }

   public static enum InteractType {
      INTERACT,
      ATTACK,
      INTERACT_AT;

      // $FF: synthetic method
      private static InteractType[] $values() {
         return new InteractType[]{INTERACT, ATTACK, INTERACT_AT};
      }
   }
}
