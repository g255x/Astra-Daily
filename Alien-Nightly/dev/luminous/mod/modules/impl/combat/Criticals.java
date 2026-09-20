package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.IPlayerMoveC2SPacket;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.exploit.BowBomb;
import dev.luminous.mod.modules.impl.exploit.Phase;
import dev.luminous.mod.modules.impl.player.KeyPearl;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import net.minecraft.class_2824.class_5907;

public class Criticals extends Module {
   public static Criticals INSTANCE;
   public final EnumSetting<Mode> mode;
   public final BooleanSetting onlyGround;
   private final BooleanSetting setOnGround;
   private final BooleanSetting blockCheck;
   private final BooleanSetting autoJump;
   private final BooleanSetting mini;
   private final SliderSetting y;
   private final BooleanSetting autoDisable;
   private final BooleanSetting crawlingDisable;
   private final BooleanSetting flight;
   boolean requireJump;

   public Criticals() {
      super("Criticals", Module.Category.Combat);
      this.mode = this.add(new EnumSetting("Mode", Criticals.Mode.OldNCP));
      this.onlyGround = this.add(new BooleanSetting("OnlyGround", true, () -> !this.mode.is(Criticals.Mode.Ground)));
      this.setOnGround = this.add(new BooleanSetting("SetNoGround", false, () -> this.mode.is(Criticals.Mode.Ground)));
      this.blockCheck = this.add(new BooleanSetting("BlockCheck", true, () -> this.mode.is(Criticals.Mode.Ground)));
      this.autoJump = this.add((new BooleanSetting("AutoJump", true, () -> this.mode.is(Criticals.Mode.Ground))).setParent());
      this.mini = this.add(new BooleanSetting("Mini", true, () -> this.mode.is(Criticals.Mode.Ground) && this.autoJump.isOpen()));
      this.y = this.add(new SliderSetting("MotionY", 0.05, (double)0.0F, (double)1.0F, 1.0E-10, () -> this.mode.is(Criticals.Mode.Ground) && this.autoJump.isOpen()));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", true, () -> this.mode.is(Criticals.Mode.Ground)));
      this.crawlingDisable = this.add(new BooleanSetting("CrawlingDisable", true, () -> this.mode.is(Criticals.Mode.Ground)));
      this.flight = this.add(new BooleanSetting("Flight", false, () -> this.mode.is(Criticals.Mode.Ground)));
      this.requireJump = false;
      this.setChinese("刀刀暴击");
      INSTANCE = this;
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   @EventListener
   public void onPacketSend(PacketEvent.Send event) {
      if (!event.isCancelled() && (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue())) {
         if (this.mode.is(Criticals.Mode.Ground)) {
            if (!BowBomb.send && !KeyPearl.throwing && !Phase.INSTANCE.isOn() && this.setOnGround.getValue() && event.getPacket() instanceof class_2828) {
               ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(false);
            }
         } else {
            class_2596 var4 = event.getPacket();
            if (var4 instanceof class_2824) {
               class_2824 packet = (class_2824)var4;
               class_1297 entity;
               if (getInteractType(packet) == class_5907.field_29172 && !((entity = getEntity(packet)) instanceof class_1511) && (!this.onlyGround.getValue() || mc.field_1724.method_24828() || mc.field_1724.method_31549().field_7479) && !mc.field_1724.method_5771() && !mc.field_1724.method_5799() && entity != null) {
                  this.doCrit(entity);
               }
            }
         }
      }

   }

   public void onLogout() {
      if (this.mode.is(Criticals.Mode.Ground) && this.autoDisable.getValue()) {
         this.disable();
      }

   }

   public boolean onEnable() {
      if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
         this.requireJump = true;
         if (this.mode.is(Criticals.Mode.Ground)) {
            if (nullCheck()) {
               if (this.autoDisable.getValue()) {
                  this.disable();
               }
            } else if (MovementUtil.isMoving() && this.autoDisable.getValue()) {
               this.disable();
            } else if (this.crawlingDisable.getValue() && mc.field_1724.method_20448()) {
               this.disable();
            } else if (mc.field_1724.method_24828() && this.autoJump.getValue() && (!this.blockCheck.getValue() || BlockUtil.canCollide(mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10086(2))))) {
               this.jump();
            }
         }
      }

      return false;
   }

   public void jump() {
      if (this.mini.getValue()) {
         MovementUtil.setMotionY(this.y.getValue());
      } else {
         mc.field_1724.method_6043();
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if ((!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) && this.mode.is(Criticals.Mode.Ground)) {
         if (this.crawlingDisable.getValue() && mc.field_1724.method_20448()) {
            this.disable();
         } else if (MovementUtil.isMoving() && this.autoDisable.getValue()) {
            this.disable();
         } else if (this.flight.getValue() && mc.field_1724.field_6017 > 0.0F) {
            MovementUtil.setMotionY((double)0.0F);
            MovementUtil.setMotionX((double)0.0F);
            MovementUtil.setMotionZ((double)0.0F);
            this.requireJump = false;
         } else if (this.blockCheck.getValue() && !BlockUtil.canCollide(mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10086(2)))) {
            this.requireJump = true;
         } else if (mc.field_1724.method_24828() && this.autoJump.getValue() && (this.flight.getValue() || this.requireJump)) {
            this.jump();
            this.requireJump = false;
         }
      }

   }

   public void doCrit(class_1297 entity) {
      switch (((Mode)this.mode.getValue()).ordinal()) {
         case 0:
            mc.field_1724.method_7277(entity);
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.71875E-7, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
            break;
         case 1:
            mc.field_1724.method_7277(entity);
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.062600301692775, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.07260029960661, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
            break;
         case 2:
            mc.field_1724.method_7277(entity);
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)0.0625F, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
            break;
         case 3:
            mc.field_1724.method_7277(entity);
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.058293536E-5, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 9.16580235E-6, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0371854E-7, mc.field_1724.method_23321(), false));
            break;
         case 4:
            mc.field_1724.method_7277(entity);
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.0045, mc.field_1724.method_23321(), true));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.52121E-4, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.3, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.025, mc.field_1724.method_23321(), false));
            break;
         case 5:
            mc.field_1724.method_7277(entity);
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 5.0E-4, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 1.0E-4, mc.field_1724.method_23321(), false));
         case 6:
         default:
            break;
         case 7:
            if (MovementUtil.isMoving() || !MovementUtil.isStatic()) {
               return;
            }

            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), true));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)0.0625F, mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.045, mc.field_1724.method_23321(), false));
      }

   }

   public static class_1297 getEntity(class_2824 packet) {
      return mc.field_1687 == null ? null : mc.field_1687.method_8469(packet.field_12870);
   }

   public static class_2824.class_5907 getInteractType(class_2824 packet) {
      return packet.field_12871.method_34211();
   }

   public static enum Mode {
      UpdatedNCP,
      Strict,
      NCP,
      OldNCP,
      Hypixel2K22,
      Packet,
      Ground,
      BBTT;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{UpdatedNCP, Strict, NCP, OldNCP, Hypixel2K22, Packet, Ground, BBTT};
      }
   }
}
