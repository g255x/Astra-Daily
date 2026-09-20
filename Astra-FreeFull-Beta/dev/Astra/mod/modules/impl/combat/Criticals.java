package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.asm.accessors.IPlayerInteractEntityC2SPacket;
import dev.Astra.asm.accessors.IPlayerMoveC2SPacket;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.exploit.BowBomb;
import dev.Astra.mod.modules.impl.exploit.Phase;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.impl.player.AutoPearl;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import net.minecraft.class_2350.class_2352;
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
   private final BooleanSetting flight;
   private final SliderSetting freezeDelay;
   private final BooleanSetting pauseOnMove;
   private final BooleanSetting crawlPause;
   private final BooleanSetting onlyInBlock;
   private final BooleanSetting headBlock;
   private final BooleanSetting onlyAura;
   private final BooleanSetting pauseElytraFly;
   boolean requireJump;
   private boolean paused;
   private boolean crawlingPaused;
   private boolean elytraPaused;
   private boolean frozen;
   private boolean freezeDelaying;
   private long freezeDelayStart;
   private class_243 freezePos;

   public Criticals() {
      super("Criticals", Module.Category.Combat);
      this.mode = this.add(new EnumSetting("Mode", Criticals.Mode.NCP));
      this.onlyGround = this.add(new BooleanSetting("OnlyGround", true, () -> !this.mode.is(Criticals.Mode.Ground) && !this.mode.is(Criticals.Mode.Freeze)));
      this.setOnGround = this.add(new BooleanSetting("SetNoGround", false, () -> this.mode.is(Criticals.Mode.Ground)));
      this.blockCheck = this.add(new BooleanSetting("BlockCheck", true, () -> this.mode.is(Criticals.Mode.Ground)));
      this.autoJump = this.add((new BooleanSetting("AutoJump", true, () -> this.mode.is(Criticals.Mode.Ground))).setParent());
      this.mini = this.add(new BooleanSetting("Mini", true, () -> this.mode.is(Criticals.Mode.Ground) && this.autoJump.isOpen()));
      this.y = this.add((new SliderSetting("MotionY", 0.05, (double)0.0F, (double)1.0F, 0.001, () -> this.mode.is(Criticals.Mode.Ground) && this.autoJump.isOpen())).setSuffix("blocks"));
      this.flight = this.add(new BooleanSetting("Flight", false, () -> this.mode.is(Criticals.Mode.Ground)));
      this.freezeDelay = this.add((new SliderSetting("FreezeDelay", (double)10.0F, (double)0.0F, (double)500.0F, (double)1.0F, () -> this.mode.is(Criticals.Mode.Freeze))).setSuffix("ms"));
      this.pauseOnMove = this.add(new BooleanSetting("PauseOnMove", true));
      this.crawlPause = this.add(new BooleanSetting("CrawlPause", false));
      this.onlyInBlock = this.add(new BooleanSetting("OnlyInBlock", true, () -> !this.mode.is(Criticals.Mode.Ground) && !this.mode.is(Criticals.Mode.Freeze)));
      this.headBlock = this.add(new BooleanSetting("HeadBlock", false));
      this.onlyAura = this.add(new BooleanSetting("OnlyAura", true, () -> this.mode.is(Criticals.Mode.Ground)));
      this.pauseElytraFly = this.add(new BooleanSetting("PauseElytraFly", true, () -> this.mode.is(Criticals.Mode.Ground)));
      this.requireJump = false;
      this.paused = false;
      this.crawlingPaused = false;
      this.elytraPaused = false;
      this.frozen = false;
      this.freezeDelaying = false;
      this.freezeDelayStart = 0L;
      this.freezePos = null;
      this.setChinese("刀刀暴击");
      INSTANCE = this;
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   private boolean isInBlock() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         double w = (double)mc.field_1724.method_17681();
         return this.isCornerStuck(mc.field_1724.method_23317() - w * 0.35, mc.field_1724.method_23321() + w * 0.35) || this.isCornerStuck(mc.field_1724.method_23317() - w * 0.35, mc.field_1724.method_23321() - w * 0.35) || this.isCornerStuck(mc.field_1724.method_23317() + w * 0.35, mc.field_1724.method_23321() - w * 0.35) || this.isCornerStuck(mc.field_1724.method_23317() + w * 0.35, mc.field_1724.method_23321() + w * 0.35);
      } else {
         return false;
      }
   }

   private boolean isCornerStuck(double x, double z) {
      class_2338 pos = class_2338.method_49637(x, mc.field_1724.method_23318(), z);
      if (this.wouldCollideAt(pos)) {
         double d = x - (double)pos.method_10263();
         double e = z - (double)pos.method_10260();

         for(class_2350 dir : new class_2350[]{class_2350.field_11039, class_2350.field_11034, class_2350.field_11043, class_2350.field_11035}) {
            double g = dir.method_10166().method_10172(d, (double)0.0F, e);
            double h = dir.method_10171() == class_2352.field_11056 ? (double)1.0F - g : g;
            if (!(h < (double)0.0F) && !this.wouldCollideAt(pos.method_10093(dir))) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean wouldCollideAt(class_2338 pos) {
      class_238 box = mc.field_1724.method_5829();
      class_238 box2 = (new class_238((double)pos.method_10263(), box.field_1322, (double)pos.method_10260(), (double)pos.method_10263() + (double)1.0F, box.field_1325, (double)pos.method_10260() + (double)1.0F)).method_1011(1.0E-7);
      return mc.field_1687.method_39454(mc.field_1724, box2);
   }

   private boolean hasHeadBlock() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         class_2338 headPos = class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)mc.field_1724.method_17682() + 0.3, mc.field_1724.method_23321());
         return !mc.field_1687.method_8320(headPos).method_26220(mc.field_1687, headPos).method_1110();
      } else {
         return false;
      }
   }

   private boolean isAuraTargetingPlayer() {
      return Aura.target != null && Aura.target instanceof class_1657;
   }

   private boolean shouldGroundActivate() {
      if (!this.mode.is(Criticals.Mode.Ground)) {
         return false;
      } else if (!this.paused && !this.crawlingPaused && !this.elytraPaused) {
         if (this.onlyAura.getValue() && !this.isAuraTargetingPlayer()) {
            return false;
         } else if (this.pauseElytraFly.getValue() && ElytraFly.INSTANCE != null && ElytraFly.INSTANCE.isOn()) {
            return false;
         } else {
            return !this.headBlock.getValue() || this.hasHeadBlock();
         }
      } else {
         return false;
      }
   }

   @EventListener
   public void onPacketSend(PacketEvent.Send event) {
      if (!event.isCancelled()) {
         if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
            if (this.mode.is(Criticals.Mode.Ground)) {
               if (!this.shouldGroundActivate()) {
                  return;
               }
            } else {
               if (!this.mode.is(Criticals.Mode.Freeze)) {
                  boolean checkInBlock = this.onlyInBlock.getValue();
                  boolean checkHead = this.headBlock.getValue();
                  if (checkInBlock || checkHead) {
                     boolean inBlock = this.isInBlock();
                     boolean headBlocked = this.hasHeadBlock();
                     if ((!checkInBlock || !inBlock) && (!checkHead || !headBlocked)) {
                        return;
                     }
                  }
               }

               if (this.paused || this.crawlingPaused) {
                  return;
               }
            }

            if (this.mode.is(Criticals.Mode.Ground)) {
               if (!BowBomb.send) {
                  if (!AutoPearl.throwing && !Phase.INSTANCE.isOn()) {
                     if (this.flight.getValue() && event.getPacket() instanceof class_2828) {
                        event.cancel();
                     } else if (this.setOnGround.getValue()) {
                        if (event.getPacket() instanceof class_2828) {
                           ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(false);
                        }

                     }
                  }
               }
            } else if (this.mode.is(Criticals.Mode.Freeze) && this.frozen) {
               if (event.getPacket() instanceof class_2828) {
                  event.cancel();
               }

            } else {
               class_2596<?> packet2 = event.getPacket();
               if (packet2 instanceof class_2824) {
                  class_2824 packet = (class_2824)packet2;
                  if (getInteractType(packet) == class_5907.field_29172) {
                     class_1297 entity = getEntity(packet);
                     if (!(entity instanceof class_1511) && (!this.onlyGround.getValue() || mc.field_1724.method_24828() || mc.field_1724.method_31549().field_7479) && !mc.field_1724.method_5771() && !mc.field_1724.method_5799() && entity != null) {
                        mc.field_1724.method_7277(entity);
                        this.doCrit(entity);
                     }
                  }
               }

            }
         }
      }
   }

   public void onLogout() {
   }

   public void onEnable() {
      if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
         this.requireJump = true;
         this.paused = false;
         this.crawlingPaused = false;
         this.elytraPaused = false;
         if (this.mode.is(Criticals.Mode.Freeze)) {
            if (!nullCheck()) {
               this.freezePos = mc.field_1724.method_19538();
               mc.field_1724.method_6043();
            }
         } else {
            if (this.mode.is(Criticals.Mode.Ground)) {
               if (nullCheck()) {
                  return;
               }

               if (this.flight.getValue()) {
                  this.freezePos = mc.field_1724.method_19538();
               }

               if (!this.shouldGroundActivate()) {
                  return;
               }

               if (mc.field_1724.method_24828() && this.autoJump.getValue() && (!this.blockCheck.getValue() || BlockUtil.canCollide(mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10086(2))))) {
                  this.jump();
               }
            }

         }
      }
   }

   public void jump() {
      if (this.mini.getValue()) {
         MovementUtil.setMotionY(this.y.getValue());
      } else {
         mc.field_1724.method_6043();
      }

   }

   public void onDisable() {
      this.frozen = false;
      this.freezeDelaying = false;
      this.freezeDelayStart = 0L;
      this.freezePos = null;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
         boolean isMoving = MovementUtil.isMoving();
         if (this.pauseOnMove.getValue()) {
            if (isMoving && !this.paused) {
               this.paused = true;
               this.requireJump = false;
            } else if (!isMoving && this.paused) {
               this.paused = false;
               if (this.mode.is(Criticals.Mode.Ground) && !this.crawlingPaused && !this.elytraPaused && (!this.onlyAura.getValue() || this.isAuraTargetingPlayer())) {
                  this.requireJump = true;
                  if (mc.field_1724.method_24828() && this.autoJump.getValue() && (!this.blockCheck.getValue() || BlockUtil.canCollide(mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10086(2))))) {
                     this.jump();
                     this.requireJump = false;
                  }
               }
            }
         }

         boolean isCrawling = mc.field_1724.method_20448();
         if (this.crawlPause.getValue()) {
            if (isCrawling && !this.crawlingPaused) {
               this.crawlingPaused = true;
               this.requireJump = false;
            } else if (!isCrawling && this.crawlingPaused) {
               this.crawlingPaused = false;
               if (this.mode.is(Criticals.Mode.Ground) && !this.paused && !this.elytraPaused && (!this.onlyAura.getValue() || this.isAuraTargetingPlayer())) {
                  this.requireJump = true;
                  if (mc.field_1724.method_24828() && this.autoJump.getValue() && (!this.blockCheck.getValue() || BlockUtil.canCollide(mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10086(2))))) {
                     this.jump();
                     this.requireJump = false;
                  }
               }
            }
         }

         boolean isElytraFlying = ElytraFly.INSTANCE != null && ElytraFly.INSTANCE.isOn();
         if (this.pauseElytraFly.getValue()) {
            if (isElytraFlying && !this.elytraPaused) {
               this.elytraPaused = true;
               this.requireJump = false;
            } else if (!isElytraFlying && this.elytraPaused) {
               this.elytraPaused = false;
               if (this.mode.is(Criticals.Mode.Ground) && !this.paused && !this.crawlingPaused && (!this.onlyAura.getValue() || this.isAuraTargetingPlayer())) {
                  this.requireJump = true;
                  if (mc.field_1724.method_24828() && this.autoJump.getValue() && (!this.blockCheck.getValue() || BlockUtil.canCollide(mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10086(2))))) {
                     this.jump();
                     this.requireJump = false;
                  }
               }
            }
         }

         if (!this.mode.is(Criticals.Mode.Ground)) {
            if (this.mode.is(Criticals.Mode.Freeze)) {
               if (!this.frozen) {
                  if (mc.field_1724.method_18798().field_1351 < (double)0.0F) {
                     if (!this.freezeDelaying) {
                        this.freezeDelaying = true;
                        this.freezeDelayStart = System.currentTimeMillis();
                     } else if (System.currentTimeMillis() - this.freezeDelayStart >= (long)this.freezeDelay.getValue()) {
                        this.frozen = true;
                        this.freezeDelaying = false;
                     }
                  }
               } else {
                  if (MovementUtil.isMoving()) {
                     this.toggle();
                     return;
                  }

                  mc.field_1724.method_18800((double)0.0F, (double)0.0F, (double)0.0F);
                  mc.field_1724.method_23327(this.freezePos.field_1352, this.freezePos.field_1351, this.freezePos.field_1350);
               }
            }

         } else if (this.shouldGroundActivate()) {
            if (this.flight.getValue()) {
               if (this.freezePos == null) {
                  this.freezePos = mc.field_1724.method_19538();
               }

               mc.field_1724.method_18800((double)0.0F, (double)0.0F, (double)0.0F);
               mc.field_1724.method_23327(this.freezePos.field_1352, this.freezePos.field_1351, this.freezePos.field_1350);
               this.requireJump = false;
            } else {
               this.freezePos = null;
               if (this.blockCheck.getValue() && !BlockUtil.canCollide(mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10086(2)))) {
                  this.requireJump = true;
               } else if (mc.field_1724.method_24828() && this.autoJump.getValue() && this.requireJump) {
                  this.jump();
                  this.requireJump = false;
               }
            }

         }
      }
   }

   public void doCrit(class_1297 entity) {
      if (this.mode.getValue() == Criticals.Mode.NCP) {
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 2.71875E-7, mc.field_1724.method_23321(), false));
         mc.method_1562().method_52787(new class_2828.class_2829(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), false));
      } else if (this.mode.getValue() == Criticals.Mode.GrimV3) {
         mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321(), Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch(), true));
         mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)0.0625F, mc.field_1724.method_23321(), Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch(), false));
         mc.method_1562().method_52787(new class_2828.class_2830(mc.field_1724.method_23317(), mc.field_1724.method_23318() + (double)0.04535F, mc.field_1724.method_23321(), Astra.ROTATION.getLastYaw(), Astra.ROTATION.getLastPitch(), false));
      }

   }

   public static class_1297 getEntity(class_2824 packet) {
      return mc.field_1687 == null ? null : mc.field_1687.method_8469(((IPlayerInteractEntityC2SPacket)packet).getEntityId());
   }

   public static class_2824.class_5907 getInteractType(class_2824 packet) {
      final class_2824.class_5907[] result = new class_2824.class_5907[1];
      packet.method_34209(new class_2824.class_5908() {
         public void method_34219(class_1268 hand) {
            result[0] = class_5907.field_29171;
         }

         public void method_34220(class_1268 hand, class_243 pos) {
            result[0] = class_5907.field_29173;
         }

         public void method_34218() {
            result[0] = class_5907.field_29172;
         }
      });
      return result[0];
   }

   public static enum Mode {
      NCP,
      GrimV3,
      Ground,
      Freeze;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{NCP, GrimV3, Ground, Freeze};
      }
   }
}
