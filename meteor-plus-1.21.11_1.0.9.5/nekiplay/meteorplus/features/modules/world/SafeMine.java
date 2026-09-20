package nekiplay.meteorplus.features.modules.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import meteordevelopment.meteorclient.events.entity.player.CanWalkOnFluidEvent;
import meteordevelopment.meteorclient.events.entity.player.InteractBlockEvent;
import meteordevelopment.meteorclient.events.entity.player.StartBreakingBlockEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.CollisionShapeEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1268;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_3612;
import net.minecraft.class_239.class_240;

public class SafeMine extends Module {
   private final SettingGroup ALSettings;
   private final SettingGroup FSettings;
   public final Setting<Boolean> solidLava;
   public final Setting<Boolean> solidLavaFreeze;
   public final Setting<Boolean> antiMine;
   public final Setting<Boolean> replaceLava;
   private final Setting<Integer> delay;
   ArrayList<class_2338> lava;
   private Integer tick;
   private boolean freeze;
   private final Setting<Boolean> FreezeLook;
   private final Setting<Boolean> Packet;
   private final Setting<Boolean> FreezeLookSilent;
   private final Setting<Boolean> FreezeLookPlace;
   private float yaw;
   private float pitch;
   private class_243 position;
   private boolean rotate;

   public SafeMine() {
      super(Categories.World, "Safe Mine", "Save you from lava.");
      this.ALSettings = this.settings.getDefaultGroup();
      this.FSettings = this.settings.createGroup("Freeze Settings");
      this.solidLava = this.ALSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Solid lava")).description("Solid lava.")).defaultValue(true)).build());
      SettingGroup var10001 = this.ALSettings;
      BoolSetting.Builder var10002 = (BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Solid lava freeze")).description("Solid lava.")).defaultValue(true);
      Setting var10003 = this.solidLava;
      Objects.requireNonNull(var10003);
      this.solidLavaFreeze = var10001.add(((BoolSetting.Builder)var10002.visible(var10003::get)).build());
      this.antiMine = this.ALSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Anti lava mine")).description("Block mine block is nearbly lava.")).defaultValue(true)).build());
      this.replaceLava = this.ALSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Replace lava")).description("Place blocks in lava in offhand.")).defaultValue(true)).build());
      var10001 = this.ALSettings;
      IntSetting.Builder var4 = ((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Repalce delay")).description("Delay for replace lava.")).defaultValue(0)).min(0);
      var10003 = this.replaceLava;
      Objects.requireNonNull(var10003);
      this.delay = var10001.add(((IntSetting.Builder)var4.visible(var10003::get)).sliderRange(0, 20).build());
      this.lava = new ArrayList();
      this.tick = 0;
      this.freeze = false;
      this.FreezeLook = this.FSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Freeze look")).description("Freezes your pitch and yaw.")).defaultValue(false)).build());
      this.Packet = this.FSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Packet mode")).description("Enable packet mode, better.")).defaultValue(true)).build());
      var10001 = this.FSettings;
      BoolSetting.Builder var5 = (BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Freeze look silent")).description("Freezes your pitch and yaw silent.")).defaultValue(true);
      var10003 = this.Packet;
      Objects.requireNonNull(var10003);
      this.FreezeLookSilent = var10001.add(((BoolSetting.Builder)var5.visible(var10003::get)).build());
      var10001 = this.FSettings;
      var5 = (BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Freeze look place support")).description("Unfreez you yaw and pitch on place")).defaultValue(false);
      var10003 = this.FreezeLookSilent;
      Objects.requireNonNull(var10003);
      this.FreezeLookPlace = var10001.add(((BoolSetting.Builder)var5.visible(var10003::get)).build());
      this.yaw = 0.0F;
      this.pitch = 0.0F;
      this.position = class_243.field_1353;
      this.rotate = false;
   }

   @EventHandler
   private void onTickEvent(TickEvent.Post event) {
      if (this.mc.field_1765 != null && this.mc.field_1765.method_17783() == class_240.field_1332 && (Boolean)this.replaceLava.get()) {
         synchronized(this.lava) {
            Iterator<class_2338> iterator = this.lava.iterator();
            if (iterator.hasNext()) {
               if (this.tick == 0) {
                  if (this.mc.field_1724 != null) {
                     class_2338 block = (class_2338)iterator.next();
                     BlockUtils.place(block, class_1268.field_5810, this.mc.field_1724.method_31548().method_67532(), false, 0, false, false, false);
                     iterator.remove();
                     this.tick = (Integer)this.delay.get();
                  }
               } else {
                  Integer var7 = this.tick;
                  this.tick = this.tick - 1;
               }
            }
         }
      }

   }

   @EventHandler
   private void onCanContactLava(TickEvent.Post event) {
      if (this.mc.field_1724 != null && this.mc.field_1687 != null) {
         class_243 underpos = this.mc.field_1724.method_73189().method_1031((double)0.0F, (double)-1.0F, (double)0.0F);
         class_2338 under = new class_2338((int)underpos.field_1352, (int)underpos.field_1351, (int)underpos.field_1350);
         if (this.mc.field_1687.method_8320(under).method_27852(class_2246.field_10164)) {
            if ((Boolean)this.solidLavaFreeze.get() && this.mc.field_1724.method_24828() && !this.freeze) {
               this.freeze = true;
               this.yaw = this.mc.field_1724.method_36454();
               this.pitch = this.mc.field_1724.method_36455();
               this.position = this.mc.field_1724.method_73189();
            }
         } else {
            this.freeze = false;
         }
      }

   }

   @EventHandler
   private void onCanWalkOnFluid(CanWalkOnFluidEvent event) {
      if ((event.fluidState.method_15772() == class_3612.field_15908 || event.fluidState.method_15772() == class_3612.field_15907) && (Boolean)this.solidLava.get()) {
         event.walkOnFluid = true;
         if ((Boolean)this.solidLavaFreeze.get()) {
            this.freeze = true;
         }
      }

   }

   @EventHandler
   private void onFluidCollisionShape(CollisionShapeEvent event) {
      if (!event.state.method_26227().method_15769() && this.mc.field_1724 != null && event.state != null && event.state.method_27852(class_2246.field_10164) && !this.mc.field_1724.method_5771() && (Boolean)this.solidLava.get()) {
         event.shape = class_259.method_1077();
      }

   }

   @EventHandler(
      priority = 100
   )
   private void onStartBreakingBlock(StartBreakingBlockEvent event) {
      ArrayList<class_2338> lavaBlocks = this.isExposedLava(event.blockPos);
      if (lavaBlocks.size() > 0 && (Boolean)this.antiMine.get()) {
         this.mc.field_1690.field_1886.method_23481(false);
         event.setCancelled(true);
         synchronized(this.lava) {
            this.lava = this.isExposedLava(event.blockPos);
         }
      }

   }

   private ArrayList<class_2338> isExposedLava(class_2338 pos) {
      ArrayList<class_2338> blocks = new ArrayList();
      if (this.mc.field_1687 != null) {
         if (this.mc.field_1687.method_8320(pos).method_27852(class_2246.field_10164)) {
            blocks.add(pos);
         }

         if (this.mc.field_1687.method_8320(pos.method_10069(1, 0, 0)).method_27852(class_2246.field_10164)) {
            blocks.add(pos.method_10069(1, 0, 0));
         }

         if (this.mc.field_1687.method_8320(pos.method_10069(-1, 0, 0)).method_27852(class_2246.field_10164)) {
            blocks.add(pos.method_10069(-1, 0, 0));
         }

         if (this.mc.field_1687.method_8320(pos.method_10069(0, 1, 0)).method_27852(class_2246.field_10164)) {
            blocks.add(pos.method_10069(0, 1, 0));
         }

         if (this.mc.field_1687.method_8320(pos.method_10069(0, -1, 0)).method_27852(class_2246.field_10164)) {
            blocks.add(pos.method_10069(0, -1, 0));
         }

         if (this.mc.field_1687.method_8320(pos.method_10069(0, 0, 1)).method_27852(class_2246.field_10164)) {
            blocks.add(pos.method_10069(0, 0, 1));
         }

         if (this.mc.field_1687.method_8320(pos.method_10069(0, 0, -1)).method_27852(class_2246.field_10164)) {
            blocks.add(pos.method_10069(0, 0, -1));
         }
      }

      return blocks;
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
      if (this.mc.field_1724 != null && this.mc.method_1562() != null && (Boolean)this.FreezeLookPlace.get() && this.freeze) {
         class_2828.class_2831 r = new class_2828.class_2831(this.mc.field_1724.method_36454(), this.mc.field_1724.method_36455(), this.mc.field_1724.method_24828(), this.mc.field_1724.field_5976);
         this.rotate = true;
         this.mc.method_1562().method_52787(r);
         this.rotate = false;
      }

   }

   @EventHandler
   private void onMovePacket(PacketEvent.Send event) {
      if (this.freeze) {
         class_2596 var3 = event.packet;
         if (var3 instanceof class_2828) {
            class_2828 playerMove = (class_2828)var3;
            if ((Boolean)this.Packet.get()) {
               this.setFreezeLook(event, playerMove);
            }
         }
      }

   }

   @EventHandler
   private void onMovePacket2(PacketEvent.Send event) {
      if (this.freeze) {
         class_2596 var3 = event.packet;
         if (var3 instanceof class_2828) {
            class_2828 playerMove = (class_2828)var3;
            if ((Boolean)this.Packet.get()) {
               this.setFreezeLook(event, playerMove);
            }
         }
      }

   }

   @EventHandler
   private void onTick(TickEvent.Pre event) {
      if (this.freeze && this.mc.field_1724 != null) {
         this.mc.field_1724.method_18800((double)0.0F, (double)0.0F, (double)0.0F);
         this.mc.field_1724.method_23327(this.position.field_1352, this.position.field_1351, this.position.field_1350);
      }

   }
}
