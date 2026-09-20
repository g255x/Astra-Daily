package dev.Astra.mod.modules.impl.misc;

import com.mojang.authlib.GameProfile;
import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.math.DamageUtils;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.asm.accessors.ILivingEntity;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.combat.AutoAnchor;
import dev.Astra.mod.modules.impl.combat.AutoCrystal;
import dev.Astra.mod.modules.impl.combat.Criticals;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import net.minecraft.class_2664;
import net.minecraft.class_2824;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_745;
import net.minecraft.class_1297.class_5529;
import net.minecraft.class_2824.class_5907;

public class FakePlayer extends Module {
   public static FakePlayer INSTANCE;
   public static FakePlayerEntity fakePlayer;
   final StringSetting name = this.add(new StringSetting("Name", "IDhammaI"));
   private final BooleanSetting damage = this.add(new BooleanSetting("Damage", true));
   private final BooleanSetting autoTotem = this.add(new BooleanSetting("AutoTotem", true));
   public final BooleanSetting record = this.add(new BooleanSetting("Record", false));
   public final BooleanSetting play = this.add(new BooleanSetting("Play", false));
   final List<PlayerState> positions = new ArrayList();
   int movementTick;
   boolean lastRecordValue = false;

   public FakePlayer() {
      super("FakePlayer", Module.Category.Misc);
      this.setChinese("假人");
      INSTANCE = this;
   }

   public String getInfo() {
      return this.name.getValue();
   }

   public void onEnable() {
      if (nullCheck()) {
         this.disable();
      } else {
         fakePlayer = new FakePlayerEntity(mc.field_1724, this.name.getValue());
         mc.field_1687.method_53875(fakePlayer);
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (fakePlayer != null && fakePlayer.field_17892 == mc.field_1687) {
         if (this.autoTotem.getValue()) {
            if (fakePlayer.method_6079().method_7909() != class_1802.field_8288) {
               fakePlayer.method_6122(class_1268.field_5810, new class_1799(class_1802.field_8288));
            }

            if (fakePlayer.method_6047().method_7909() != class_1802.field_8288) {
               fakePlayer.method_6122(class_1268.field_5808, new class_1799(class_1802.field_8288));
            }
         }

         if (this.record.getValue() != this.lastRecordValue && this.record.getValue()) {
            this.positions.clear();
         }

         this.lastRecordValue = this.record.getValue();
         if (this.record.getValue()) {
            this.positions.add(new PlayerState(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), mc.field_1724.method_36454(), mc.field_1724.method_36455()));
         }

         if (this.play.getValue() && !this.positions.isEmpty()) {
            ++this.movementTick;
            if (this.movementTick >= this.positions.size()) {
               this.movementTick = 0;
            }

            PlayerState p = (PlayerState)this.positions.get(this.movementTick);
            fakePlayer.method_36456(p.yaw);
            fakePlayer.method_36457(p.pitch);
            fakePlayer.method_5847(p.yaw);
            fakePlayer.method_43391(p.x, p.y, p.z);
            fakePlayer.method_5759(p.x, p.y, p.z, p.yaw, p.pitch, 3);
         }

      } else {
         this.disable();
      }
   }

   public void onDisable() {
      if (fakePlayer != null) {
         fakePlayer.method_5768();
         fakePlayer.method_31745(class_5529.field_26998);
         fakePlayer.method_36209();
         fakePlayer = null;
      }
   }

   @EventListener
   public void onAttack(PacketEvent.Send event) {
      class_2596<?> packet2 = event.getPacket();
      class_2824 packet;
      if (packet2 instanceof class_2824 && Criticals.getInteractType(packet = (class_2824)packet2) == class_5907.field_29172 && Criticals.getEntity(packet) == fakePlayer) {
         mc.field_1687.method_43128(mc.field_1724, fakePlayer.method_23317(), fakePlayer.method_23318(), fakePlayer.method_23321(), class_3417.field_15115, class_3419.field_15248, 1.0F, 1.0F);
         float damage = DamageUtils.getAttackDamage(mc.field_1724, fakePlayer);
         if ((mc.field_1724.field_6017 > 0.0F || Criticals.INSTANCE.isOn() && !Criticals.INSTANCE.mode.is(Criticals.Mode.Ground) && (mc.field_1724.method_24828() || !Criticals.INSTANCE.onlyGround.getValue())) && (!mc.field_1724.method_24828() || Criticals.INSTANCE.isOn() && !Criticals.INSTANCE.mode.is(Criticals.Mode.Ground)) && !mc.field_1724.method_6101() && !mc.field_1724.method_5799() && !mc.field_1724.method_6059(class_1294.field_5919) && !mc.field_1724.method_5765()) {
            mc.field_1687.method_43128(mc.field_1724, fakePlayer.method_23317(), fakePlayer.method_23318(), fakePlayer.method_23321(), class_3417.field_15016, class_3419.field_15248, 1.0F, 1.0F);
            mc.field_1724.method_7277(fakePlayer);
         }

         if (fakePlayer.field_6235 <= 0) {
            fakePlayer.method_48922(mc.field_1687.method_48963().method_48830());
            if (fakePlayer.method_6067() >= damage) {
               fakePlayer.method_6073(fakePlayer.method_6067() - damage);
            } else {
               float damage2 = damage - fakePlayer.method_6067();
               fakePlayer.method_6073(0.0F);
               fakePlayer.method_6033(fakePlayer.method_6032() - damage2);
            }

            if (fakePlayer.method_29504()) {
               Astra.POP.onTotemPop(fakePlayer);
               if (fakePlayer.method_6095(mc.field_1687.method_48963().method_48830())) {
                  fakePlayer.method_6033(10.0F);
                  (new class_2663(fakePlayer, (byte)35)).method_11471(mc.method_1562());
               }
            }
         }
      }

   }

   @EventListener
   public void onPacketReceive(PacketEvent.Receive event) {
      class_2596<?> packet;
      if (this.damage.getValue() && fakePlayer != null && fakePlayer.field_6235 <= 0 && (packet = event.getPacket()) instanceof class_2664) {
         class_2664 explosion = (class_2664)packet;
         class_243 vec3d = new class_243(explosion.method_11475(), explosion.method_11477(), explosion.method_11478());
         if (Math.sqrt(vec3d.method_1025(fakePlayer.method_19538())) > (double)10.0F) {
            return;
         }

         float damage = BlockUtil.getBlock(new BlockPosX(explosion.method_11475(), explosion.method_11477(), explosion.method_11478())) == class_2246.field_23152 ? (float)AutoAnchor.INSTANCE.getAnchorDamage(new BlockPosX(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()), fakePlayer, fakePlayer) : AutoCrystal.INSTANCE.calculateDamage((class_243)(new class_243(explosion.method_11475(), explosion.method_11477(), explosion.method_11478())), fakePlayer, fakePlayer);
         fakePlayer.method_48922(mc.field_1687.method_48963().method_48830());
         if (fakePlayer.method_6067() >= damage) {
            fakePlayer.method_6073(fakePlayer.method_6067() - damage);
         } else {
            float damage2 = damage - fakePlayer.method_6067();
            fakePlayer.method_6073(0.0F);
            fakePlayer.method_6033(fakePlayer.method_6032() - damage2);
         }

         if (fakePlayer.method_29504()) {
            Astra.POP.onTotemPop(fakePlayer);
            if (fakePlayer.method_6095(mc.field_1687.method_48963().method_48830())) {
               fakePlayer.method_6033(10.0F);
               (new class_2663(fakePlayer, (byte)35)).method_11471(mc.method_1562());
            }
         }
      }

   }

   public static class FakePlayerEntity extends class_745 {
      private final boolean onGround;

      public FakePlayerEntity(class_1657 player, String name) {
         super(Wrapper.mc.field_1687, new GameProfile(UUID.fromString("66666666-6666-6666-6666-666666666666"), name));
         this.method_5719(player);
         this.field_6014 = player.field_6014;
         this.field_5969 = player.field_5969;
         this.field_6036 = player.field_6036;
         this.field_6283 = player.field_6283;
         this.field_6241 = player.field_6241;
         this.field_6251 = player.field_6251;
         this.field_6279 = player.field_6279;
         this.field_42108.method_48567(player.field_42108.method_48566());
         this.field_42108.field_42111 = player.field_42108.method_48569();
         ((ILivingEntity)this).setLeaningPitch(((ILivingEntity)player).getLeaningPitch());
         ((ILivingEntity)this).setLastLeaningPitch(((ILivingEntity)player).getLeaningPitch());
         this.field_5957 = player.method_5799();
         this.method_5660(player.method_5715());
         this.method_18380(player.method_18376());
         this.method_5729(7, player.method_6128());
         this.onGround = player.method_24828();
         this.method_24830(this.onGround);
         this.method_31548().method_7377(player.method_31548());
         this.method_52544(player.method_6067());
         this.method_6033(player.method_6032());
         this.method_5857(player.method_5829());
      }

      public boolean method_24828() {
         return this.onGround;
      }

      public boolean method_7325() {
         return false;
      }

      public boolean method_7337() {
         return false;
      }
   }

   private static record PlayerState(double x, double y, double z, float yaw, float pitch) {
   }
}
