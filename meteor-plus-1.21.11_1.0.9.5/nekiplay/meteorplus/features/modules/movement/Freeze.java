package nekiplay.meteorplus.features.modules.movement;

import java.util.Objects;
import meteordevelopment.meteorclient.events.entity.EntityRemovedEvent;
import meteordevelopment.meteorclient.events.entity.player.InteractBlockEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2828;

public class Freeze extends Module {
   private final SettingGroup FSettings;
   private final Setting<Boolean> FreezeLook;
   private final Setting<Boolean> Packet;
   private final Setting<Boolean> FreezeLookSilent;
   private final Setting<Boolean> FreezeLookPlace;
   private float yaw;
   private float pitch;
   private class_243 position;
   private boolean rotate;

   public Freeze() {
      super(Categories.Movement, "Freeze", "Freezes your position for server.");
      this.FSettings = this.settings.getDefaultGroup();
      this.FreezeLook = this.FSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Freeze look")).description("Freezes your pitch and yaw.")).defaultValue(false)).build());
      this.Packet = this.FSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Packet mode")).description("Enable packet mode, better.")).defaultValue(true)).build());
      this.FreezeLookSilent = this.FSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Freeze look silent")).description("Freezes your pitch and yaw silent.")).defaultValue(true)).visible(() -> (Boolean)this.Packet.get() && (Boolean)this.FreezeLook.get())).build());
      SettingGroup var10001 = this.FSettings;
      BoolSetting.Builder var10002 = (BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Freeze look place support")).description("Unfreez you yaw and pitch on place")).defaultValue(false);
      Setting var10003 = this.FreezeLookSilent;
      Objects.requireNonNull(var10003);
      this.FreezeLookPlace = var10001.add(((BoolSetting.Builder)var10002.visible(var10003::get)).build());
      this.yaw = 0.0F;
      this.pitch = 0.0F;
      this.position = class_243.field_1353;
      this.rotate = false;
   }

   public void onActivate() {
      if (this.mc.field_1724 != null) {
         this.yaw = this.mc.field_1724.method_36454();
         this.pitch = this.mc.field_1724.method_36455();
         this.position = this.mc.field_1724.method_73189();
      }

   }

   private void setFreezeLook(PacketEvent.Send event, class_2828 playerMove) {
      if (playerMove.method_36172() && (Boolean)this.FreezeLook.get() && (Boolean)this.FreezeLookSilent.get() && !this.rotate) {
         event.setCancelled(true);
      } else if (this.mc.field_1724 != null && playerMove.method_36172() && (Boolean)this.FreezeLook.get() && !(Boolean)this.FreezeLookSilent.get()) {
         event.setCancelled(true);
         this.mc.field_1724.method_36456(this.yaw);
         this.mc.field_1724.method_36457(this.pitch);
      }

      if (this.mc.field_1724 != null && playerMove.method_36171()) {
         this.mc.field_1724.method_18800((double)0.0F, (double)0.0F, (double)0.0F);
         this.mc.field_1724.method_23327(this.position.field_1352, this.position.field_1351, this.position.field_1350);
         event.setCancelled(true);
      }

   }

   @EventHandler
   private void InteractBlockEvent(InteractBlockEvent event) {
      if (this.mc.field_1724 != null && this.mc.method_1562() != null && (Boolean)this.FreezeLookPlace.get()) {
         class_2828.class_2831 r = new class_2828.class_2831(this.mc.field_1724.method_36454(), this.mc.field_1724.method_36455(), this.mc.field_1724.method_24828(), this.mc.field_1724.field_5976);
         this.rotate = true;
         this.mc.method_1562().method_52787(r);
         this.rotate = false;
      }

   }

   @EventHandler
   private void onMovePacket(PacketEvent.Send event) {
      class_2596 var3 = event.packet;
      if (var3 instanceof class_2828 playerMove) {
         if ((Boolean)this.Packet.get()) {
            this.setFreezeLook(event, playerMove);
         }
      }

   }

   @EventHandler
   private void connectToServerEvent(GameLeftEvent event) {
      this.toggle();
   }

   @EventHandler
   private void onTick(TickEvent.Pre event) {
      if (this.mc.field_1724 != null) {
         this.mc.field_1724.method_18800((double)0.0F, (double)0.0F, (double)0.0F);
         this.mc.field_1724.method_23327(this.position.field_1352, this.position.field_1351, this.position.field_1350);
      }

   }

   @EventHandler
   private void onPlayerMove(PlayerMoveEvent event) {
      event.movement = new class_243((double)0.0F, (double)0.0F, (double)0.0F);
   }

   @EventHandler
   private void remove(EntityRemovedEvent event) {
      if (event.entity == this.mc.field_1724 && this.isActive()) {
         this.toggle();
      }

   }
}
