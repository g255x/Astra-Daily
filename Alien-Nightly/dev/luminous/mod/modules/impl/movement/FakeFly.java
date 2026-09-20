package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventHandler;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.RotateEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.utils.entity.PauseUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.OffFirework;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_1671;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2708;
import net.minecraft.class_2709;
import net.minecraft.class_2848;
import net.minecraft.class_4050;
import net.minecraft.class_2848.class_2849;

public class FakeFly extends Module {
   public static FakeFly INSTANCE;
   private final EnumSetting<Mode> mode;
   public final BooleanSetting armor;
   private final BooleanSetting stand;
   private final SliderSetting timeout;
   private final BooleanSetting key;
   private final BooleanSetting rotate;
   private final BooleanSetting rocket;
   public final SliderSetting fireworkDelay;
   private final BooleanSetting usingPause;
   private final BooleanSetting same;
   public final SliderSetting horizontalSpeed;
   public final SliderSetting verticalSpeed;
   public final SliderSetting pitch;
   public final SliderSetting accelTime;
   public final BooleanSetting sprintToBoost;
   public final SliderSetting sprintToBoostMaxSpeed;
   public final SliderSetting boostAccelTime;
   private class_243 lastMovement;
   private class_243 currentVelocity;
   private long timeOfLastRubberband;
   private class_243 lastRubberband;
   public boolean fly;
   private final Timer instantFlyTimer;
   private final Timer rocketTimer;
   private final Timer spoofTimer;

   public FakeFly() {
      super("FakeFly", Module.Category.Movement);
      this.mode = this.add(new EnumSetting("Mode", FakeFly.Mode.Control));
      this.armor = this.add(new BooleanSetting("Armor", true));
      this.stand = this.add(new BooleanSetting("Stand", false));
      this.timeout = this.add(new SliderSetting("Timeout", (double)0.5F, 0.1, (double)1.0F, 0.01));
      this.key = this.add(new BooleanSetting("OnlyKeyRocket", false));
      this.rotate = this.add(new BooleanSetting("Rotate", true, () -> this.mode.is(FakeFly.Mode.Control)));
      this.rocket = this.add(new BooleanSetting("Firework", true));
      BooleanSetting var10009 = this.rocket;
      Objects.requireNonNull(var10009);
      this.fireworkDelay = this.add(new SliderSetting("FireworkDelay", 1.2, (double)0.0F, (double)5.0F, 0.1, var10009::getValue));
      BooleanSetting var10006 = this.rocket;
      Objects.requireNonNull(var10006);
      this.usingPause = this.add((new BooleanSetting("UsingPause", false, var10006::getValue)).setParent());
      this.same = this.add(new BooleanSetting("SameHand", false, () -> this.rocket.getValue() && this.usingPause.isOpen()));
      this.horizontalSpeed = this.add(new SliderSetting("HorizontalSpeed", 25, 0, 100, () -> this.mode.is(FakeFly.Mode.Control)));
      this.verticalSpeed = this.add(new SliderSetting("VerticalSpeed", (double)25.0F, (double)0.0F, (double)100.0F, () -> this.mode.is(FakeFly.Mode.Control)));
      this.pitch = this.add(new SliderSetting("Pitch", (double)90.0F, (double)0.0F, (double)90.0F, 0.1, () -> this.mode.is(FakeFly.Mode.Control) && this.rotate.getValue()));
      this.accelTime = this.add(new SliderSetting("AccelerationTime", (double)0.0F, 0.01, (double)2.0F, 0.01, () -> this.mode.is(FakeFly.Mode.Control)));
      this.sprintToBoost = this.add(new BooleanSetting("SprintToBoost", true, () -> this.mode.is(FakeFly.Mode.Control)));
      this.sprintToBoostMaxSpeed = this.add(new SliderSetting("BoostMaxSpeed", (double)100.0F, (double)50.0F, (double)300.0F, () -> this.mode.is(FakeFly.Mode.Control)));
      this.boostAccelTime = this.add(new SliderSetting("BoostAccelTime", (double)0.5F, 0.01, (double)2.0F, 0.01, () -> this.mode.is(FakeFly.Mode.Control)));
      this.lastMovement = class_243.field_1353;
      this.currentVelocity = class_243.field_1353;
      this.timeOfLastRubberband = 0L;
      this.lastRubberband = class_243.field_1353;
      this.fly = false;
      this.instantFlyTimer = new Timer();
      this.rocketTimer = new Timer();
      this.spoofTimer = new Timer();
      this.setChinese("虚假飞行");
      INSTANCE = this;
   }

   public String getInfo() {
      return ((Mode)this.mode.getValue()).name();
   }

   public boolean onEnable() {
      if (nullCheck()) {
         return false;
      } else {
         this.currentVelocity = mc.field_1724.method_18798();
         this.fly = false;
         if (!mc.field_1724.method_7337()) {
            mc.field_1724.method_31549().field_7478 = false;
         }

         mc.field_1724.method_31549().field_7479 = false;
         this.spoofTimer.reset();
         return false;
      }
   }

   public void onDisable() {
      if (!nullCheck()) {
         InventoryUtil.silentSwapEquipChestplate();
         EntityUtil.syncInventory();
         if (!mc.field_1724.method_7337()) {
            mc.field_1724.method_31549().field_7478 = false;
         }

         mc.field_1724.method_31549().field_7479 = false;
         this.sync();
      }
   }

   private void sync() {
      mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12984));
      mc.field_1724.method_5660(false);
   }

   @EventHandler
   public void onUpdate() {
      if (!nullCheck()) {
         if (mc.field_1724.method_24828()) {
            this.fly = false;
         }

         if (!mc.field_1724.method_6128()) {
            if (!mc.field_1724.method_24828() && mc.field_1724.method_18798().method_10214() < (double)0.0F) {
               if (!this.instantFlyTimer.passedMs((double)((long)((double)1000.0F * this.timeout.getValue())))) {
                  return;
               }

               this.instantFlyTimer.reset();
               this.fly = true;
            }
         } else {
            this.fly = true;
         }

      }
   }

   @EventHandler(
      priority = -100
   )
   private void onRotate(RotateEvent event) {
      if (!nullCheck()) {
         if (this.fly) {
            if (!this.mode.is(FakeFly.Mode.Legit)) {
               if (this.rotate.getValue()) {
                  event.setYaw(Sprint.getSprintYaw(mc.field_1724.method_36454()));
                  if (mc.field_1690.field_1903.method_1434()) {
                     if (MovementUtil.isMoving()) {
                        event.setPitch(-(this.pitch.getValueFloat() / 2.0F));
                     } else {
                        event.setPitch(-this.pitch.getValueFloat());
                     }
                  } else if (mc.field_1690.field_1832.method_1434()) {
                     if (MovementUtil.isMoving()) {
                        event.setPitch(this.pitch.getValueFloat() / 2.0F);
                     } else {
                        event.setPitch(this.pitch.getValueFloat());
                     }
                  }

               }
            }
         }
      }
   }

   @EventHandler
   private void onTick(TickEvent event) {
      if (!event.isPre()) {
         if (!nullCheck()) {
            if (this.fly) {
               boolean isUsingFirework = this.getIsUsingFirework();
               if (isUsingFirework || InventoryUtil.findItemInventorySlot(class_1802.field_8639) != -1) {
                  class_243 desiredVelocity = new class_243((double)0.0F, (double)0.0F, (double)0.0F);
                  double yaw = Math.toRadians((double)mc.field_1724.method_36454());
                  double pitch = Math.toRadians((double)mc.field_1724.method_36455());
                  class_243 direction = (new class_243(-Math.sin(yaw) * Math.cos(pitch), -Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch))).method_1029();
                  if (mc.field_1690.field_1894.method_1434()) {
                     desiredVelocity = desiredVelocity.method_1019(direction.method_18805(this.getHorizontalSpeed() / (double)20.0F, (double)0.0F, this.getHorizontalSpeed() / (double)20.0F));
                  }

                  if (mc.field_1690.field_1881.method_1434()) {
                     desiredVelocity = desiredVelocity.method_1019(direction.method_18805(-this.getHorizontalSpeed() / (double)20.0F, (double)0.0F, -this.getHorizontalSpeed() / (double)20.0F));
                  }

                  if (mc.field_1690.field_1913.method_1434()) {
                     desiredVelocity = desiredVelocity.method_1019(direction.method_18805(this.getHorizontalSpeed() / (double)20.0F, (double)0.0F, this.getHorizontalSpeed() / (double)20.0F).method_1024(((float)Math.PI / 2F)));
                  }

                  if (mc.field_1690.field_1849.method_1434()) {
                     desiredVelocity = desiredVelocity.method_1019(direction.method_18805(this.getHorizontalSpeed() / (double)20.0F, (double)0.0F, this.getHorizontalSpeed() / (double)20.0F).method_1024((-(float)Math.PI / 2F)));
                  }

                  if (mc.field_1690.field_1903.method_1434()) {
                     desiredVelocity = desiredVelocity.method_1031((double)0.0F, (double)this.verticalSpeed.getValueFloat() / (double)20.0F, (double)0.0F);
                  }

                  if (mc.field_1690.field_1832.method_1434()) {
                     desiredVelocity = desiredVelocity.method_1031((double)0.0F, (double)(-this.verticalSpeed.getValueFloat()) / (double)20.0F, (double)0.0F);
                  }

                  this.currentVelocity = new class_243(mc.field_1724.method_18798().field_1352, this.currentVelocity.field_1351, mc.field_1724.method_18798().field_1350);
                  class_243 velocityDifference = desiredVelocity.method_1020(this.currentVelocity);
                  double maxDelta = this.getHorizontalSpeed() / (double)20.0F / (this.getHorizontalAccelTime() * (double)20.0F);
                  if (velocityDifference.method_1027() > maxDelta * maxDelta) {
                     velocityDifference = velocityDifference.method_1029().method_1021(maxDelta);
                  }

                  this.currentVelocity = this.currentVelocity.method_1019(velocityDifference);
                  class_238 boundingBox = mc.field_1724.method_5829();
                  double playerFeetY = boundingBox.field_1322;
                  class_238 groundBox = new class_238(boundingBox.field_1323, playerFeetY - 0.1, boundingBox.field_1321, boundingBox.field_1320, playerFeetY, boundingBox.field_1324);

                  for(class_2338 pos : class_2338.method_10094((int)Math.floor(groundBox.field_1323), (int)Math.floor(groundBox.field_1322), (int)Math.floor(groundBox.field_1321), (int)Math.floor(groundBox.field_1320), (int)Math.floor(groundBox.field_1325), (int)Math.floor(groundBox.field_1324))) {
                     class_2680 blockState = mc.field_1687.method_8320(pos);
                     if (blockState.method_26212(mc.field_1687, pos)) {
                        double blockTopY = (double)pos.method_10264() + (double)1.0F;
                        double distanceToBlock = playerFeetY - blockTopY;
                        if (distanceToBlock >= (double)0.0F && distanceToBlock < 0.1 && this.currentVelocity.field_1351 < (double)0.0F) {
                           this.currentVelocity = new class_243(this.currentVelocity.field_1352, 0.1, this.currentVelocity.field_1350);
                        }
                     }
                  }

                  if (this.armor.getValue()) {
                     InventoryUtil.silentSwapEquipElytra();
                     this.sync();
                  }

                  if (!mc.field_1724.method_6128() || this.armor.getValue()) {
                     mc.method_1562().method_52787(new class_2848(mc.field_1724, class_2849.field_12982));
                  }

                  if (this.rocketTimer.passedS((double)this.fireworkDelay.getValueFloat()) && this.rocket.getValue() && (MovementUtil.isMoving() || !this.key.getValue()) && (!this.usingPause.getValue() || !PauseUtil.checkPause(this.same.getValue()))) {
                     OffFirework.INSTANCE.off();
                     this.rocketTimer.reset();
                  }

                  if (this.stand.getValue()) {
                     mc.field_1724.method_23670();
                     mc.field_1724.method_18380(class_4050.field_18076);
                  }

                  if (this.armor.getValue()) {
                     InventoryUtil.silentSwapEquipChestplate();
                     this.sync();
                  }
               }

            }
         }
      }
   }

   @EventHandler
   private void onPlayerMove(MoveEvent event) {
      if (!this.mode.is(FakeFly.Mode.Legit)) {
         if (this.fly) {
            if (this.getIsUsingFirework() || InventoryUtil.findItemInventorySlot(class_1802.field_8639) != -1) {
               if (this.lastMovement == null) {
                  this.lastMovement = new class_243(event.getX(), event.getY(), event.getZ());
               }

               class_243 newMovement = this.currentVelocity;
               mc.field_1724.method_18799(newMovement);
               event.setX(newMovement.field_1352);
               event.setY(newMovement.field_1351);
               event.setZ(newMovement.field_1350);
               this.lastMovement = newMovement;
            }

         }
      }
   }

   @EventHandler
   public void onPacketReceive(PacketEvent.Receive event) {
      if (this.fly) {
         class_2596 var3 = event.getPacket();
         if (var3 instanceof class_2708) {
            class_2708 packet = (class_2708)var3;
            if (packet.method_11733().contains(class_2709.field_12400)) {
               this.currentVelocity = new class_243(packet.method_11734(), this.currentVelocity.field_1351, this.currentVelocity.field_1350);
            }

            if (packet.method_11733().contains(class_2709.field_12398)) {
               this.currentVelocity = new class_243(this.currentVelocity.field_1352, packet.method_11735(), this.currentVelocity.field_1350);
            }

            if (packet.method_11733().contains(class_2709.field_12403)) {
               this.currentVelocity = new class_243(this.currentVelocity.field_1352, this.currentVelocity.field_1351, packet.method_11738());
            }

            if (!packet.method_11733().contains(class_2709.field_12400) && !packet.method_11733().contains(class_2709.field_12398) && !packet.method_11733().contains(class_2709.field_12403)) {
               if (System.currentTimeMillis() - this.timeOfLastRubberband < 100L) {
                  this.currentVelocity = (new class_243(packet.method_11734(), packet.method_11735(), packet.method_11738())).method_1020(this.lastRubberband);
               }

               this.timeOfLastRubberband = System.currentTimeMillis();
               this.lastRubberband = new class_243(packet.method_11734(), packet.method_11735(), packet.method_11738());
            }
         }

      }
   }

   private boolean getIsUsingFirework() {
      boolean usingFirework = false;

      for(class_1297 entity : mc.field_1687.method_18112()) {
         if (entity instanceof class_1671 firework) {
            if (firework.method_24921() != null && firework.method_24921().equals(mc.field_1724)) {
               usingFirework = true;
            }
         }
      }

      return usingFirework;
   }

   private double getHorizontalSpeed() {
      if (mc.field_1690.field_1867.method_1434() && this.sprintToBoost.getValue()) {
         double horizontalVelocity = this.currentVelocity.method_37267();
         return Math.clamp(horizontalVelocity * 1.3 * (double)20.0F, this.horizontalSpeed.getValue(), this.sprintToBoostMaxSpeed.getValue());
      } else {
         return this.horizontalSpeed.getValue();
      }
   }

   private double getHorizontalAccelTime() {
      return this.currentVelocity.method_37267() > this.horizontalSpeed.getValue() ? this.boostAccelTime.getValue() : this.accelTime.getValue();
   }

   public static enum Mode {
      Control,
      Legit;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{Control, Legit};
      }
   }
}
