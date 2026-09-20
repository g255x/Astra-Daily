package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.EntityVelocityUpdateEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.Criticals;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.class_2596;
import net.minecraft.class_2793;
import net.minecraft.class_2799;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import net.minecraft.class_2833;
import net.minecraft.class_2838;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.minecraft.class_3965;
import net.minecraft.class_6374;

public class PacketLogger extends Module {
   public static PacketLogger INSTANCE;
   private final BooleanSetting moveFull = this.add(new BooleanSetting("MoveFull", true));
   private final BooleanSetting movePos = this.add(new BooleanSetting("MovePosition", true));
   private final BooleanSetting moveLook = this.add(new BooleanSetting("MoveLook", true));
   private final BooleanSetting moveGround = this.add(new BooleanSetting("MoveGround", true));
   private final BooleanSetting vehicleMove = this.add(new BooleanSetting("VehicleMove", true));
   private final BooleanSetting playerAction = this.add(new BooleanSetting("PlayerAction", true));
   private final BooleanSetting updateSlot = this.add(new BooleanSetting("UpdateSlot", true));
   private final BooleanSetting handSwing = this.add(new BooleanSetting("HandSwing", true));
   private final BooleanSetting pong = this.add(new BooleanSetting("Pong", true));
   private final BooleanSetting interactEntity = this.add(new BooleanSetting("InteractEntity", true));
   private final BooleanSetting interactBlock = this.add(new BooleanSetting("InteractBlock", true));
   private final BooleanSetting interactItem = this.add(new BooleanSetting("InteractItem", true));
   private final BooleanSetting closeScreen = this.add(new BooleanSetting("CloseScreen", true));
   private final BooleanSetting command = this.add(new BooleanSetting("ClientCommand", true));
   private final BooleanSetting status = this.add(new BooleanSetting("ClientStatus", true));
   private final BooleanSetting clickSlot = this.add(new BooleanSetting("ClickSlot", true));
   private final BooleanSetting pickInventory = this.add(new BooleanSetting("PickInventory", true));
   private final BooleanSetting teleportConfirm = this.add(new BooleanSetting("TeleportConfirm", true));
   private final BooleanSetting s2cVelocity = this.add(new BooleanSetting("S2cVelocity", true));

   public PacketLogger() {
      super("PacketLogger", Module.Category.Misc);
      this.setChinese("数据包记录");
      INSTANCE = this;
   }

   private void logPacket(String msg, Object... args) {
      String s = String.format(msg, args);
      CommandManager.sendMessage(s);
   }

   @EventListener(
      priority = 999999
   )
   public void velocity(EntityVelocityUpdateEvent event) {
      if (this.s2cVelocity.getValue() && event.getEntity() == mc.field_1724) {
         this.logPacket("S2C Velocity, x: %s, y: %s, z: %s, isExplosion: %s", event.getX(), event.getY(), event.getZ(), event.isExplosion());
      }

   }

   @EventListener(
      priority = -999999
   )
   public void onPacketSend(PacketEvent.Send event) {
      class_2596 builder = event.getPacket();
      if (builder instanceof class_2828.class_2830 packet) {
         if (this.moveFull.getValue()) {
            StringBuilder builder = new StringBuilder();
            builder.append("PlayerMove Full - ");
            if (packet.method_36171()) {
               builder.append("x: ").append(packet.method_12269((double)0.0F)).append(", y: ").append(packet.method_12268((double)0.0F)).append(", z: ").append(packet.method_12274((double)0.0F)).append(" ");
            }

            if (packet.method_36172()) {
               builder.append("yaw: ").append(packet.method_12271(0.0F)).append(", pitch: ").append(packet.method_12270(0.0F)).append(" ");
            }

            builder.append(" onground: ").append(packet.method_12273());
            this.logPacket(builder.toString());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2828.class_2829 packet) {
         if (this.movePos.getValue()) {
            StringBuilder builderx = new StringBuilder();
            builderx.append("PlayerMove PosGround - ");
            if (packet.method_36171()) {
               builderx.append("x: ").append(packet.method_12269((double)0.0F)).append(", y: ").append(packet.method_12268((double)0.0F)).append(", z: ").append(packet.method_12274((double)0.0F)).append(" ");
            }

            builderx.append(" onground: ").append(packet.method_12273());
            this.logPacket(builderx.toString());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2828.class_2831 packet) {
         if (this.moveLook.getValue()) {
            StringBuilder builderx = new StringBuilder();
            builderx.append("PlayerMove LookGround - ");
            if (packet.method_36172()) {
               builderx.append("yaw: ").append(packet.method_12271(0.0F)).append(", pitch: ").append(packet.method_12270(0.0F)).append(" ");
            }

            builderx.append(" onground: ").append(packet.method_12273());
            this.logPacket(builderx.toString());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2828.class_5911 packet) {
         if (this.moveGround.getValue()) {
            String s = "PlayerMove Ground - onground: " + packet.method_12273();
            this.logPacket(s);
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2833 packet) {
         if (this.vehicleMove.getValue()) {
            this.logPacket("VehicleMove - x: %s, y: %s, z: %s, yaw: %s, pitch: %s", packet.method_12279(), packet.method_12280(), packet.method_12276(), packet.method_12281(), packet.method_12277());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2846 packet) {
         if (this.playerAction.getValue() && packet.method_12360() != null) {
            this.logPacket("PlayerAction - action: %s, direction: %s, pos: %s", packet.method_12363().name(), packet.method_12360().name(), packet.method_12362().method_23854());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2868 packet) {
         if (this.updateSlot.getValue()) {
            this.logPacket("UpdateSlot - slot: %d", packet.method_12442());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2879 packet) {
         if (this.handSwing.getValue()) {
            this.logPacket("HandSwing - hand: %s", packet.method_12512().name());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_6374 packet) {
         if (this.pong.getValue()) {
            this.logPacket("Pong - %d", packet.method_36960());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2824 packet) {
         if (this.interactEntity.getValue()) {
            this.logPacket("InteractEntity - Entity: %s, id: %s", Criticals.getEntity(packet).method_5477().getString(), Criticals.getEntity(packet).method_5628());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2885 packet) {
         if (this.interactBlock.getValue()) {
            class_3965 blockHitResult = packet.method_12543();
            this.logPacket("InteractBlock - pos: %s, dir: %s, hand: %s", blockHitResult.method_17777().method_23854(), blockHitResult.method_17780().name(), packet.method_12546().name());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2886 packet) {
         if (this.interactItem.getValue()) {
            this.logPacket("InteractItem - hand: %s", packet.method_12551().name());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2815 packet) {
         if (this.closeScreen.getValue()) {
            this.logPacket("CloseScreen - id: %s", packet.method_36168());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2848 packet) {
         if (this.command.getValue()) {
            this.logPacket("ClientCommand - mode: %s", packet.method_12365().name());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2799 packet) {
         if (this.status.getValue()) {
            this.logPacket("ClientStatus - mode: %s", packet.method_12119().name());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2813 packet) {
         if (this.clickSlot.getValue()) {
            this.logPacket("ClickSlot - type: %s, slot: %s, button: %s, id: %s", packet.method_12195().name(), packet.method_12192(), packet.method_12193(), packet.method_12194());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2838 packet) {
         if (this.pickInventory.getValue()) {
            this.logPacket("PickInventory - slot: %s", packet.method_12293());
         }
      }

      builder = event.getPacket();
      if (builder instanceof class_2793 packet) {
         if (this.teleportConfirm.getValue()) {
            this.logPacket("TeleportConfirm - id: %s", packet.method_12086());
         }
      }

   }
}
