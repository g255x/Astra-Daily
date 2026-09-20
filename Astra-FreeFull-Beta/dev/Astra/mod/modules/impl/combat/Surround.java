package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.MoveEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.player.MovementUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.core.impl.BreakManager;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.impl.player.PacketMine;
import dev.Astra.mod.modules.settings.enums.Timing;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_742;

public class Surround extends Module {
   public static Surround INSTANCE;
   public final EnumSetting<Page> page;
   public final SliderSetting placeDelay;
   private final BooleanSetting mineDownward;
   public final BooleanSetting extend;
   public final BooleanSetting onlySelf;
   public final BooleanSetting inAir;
   private final Timer timer;
   private final SliderSetting blocksPer;
   private final BooleanSetting packetPlace;
   private final EnumSetting<Timing> timing;
   private final BooleanSetting breakCrystal;
   private final BooleanSetting usingBreak;
   private final BooleanSetting center;
   private final BooleanSetting inventory;
   private final BooleanSetting enderChest;
   private final BooleanSetting godMode;
   private final SliderSetting breakTime;
   private final BooleanSetting failedSkip;
   private final BooleanSetting rotate;
   private final BooleanSetting yawStep;
   private final BooleanSetting whenElytra;
   private final SliderSetting steps;
   private final BooleanSetting checkFov;
   private final SliderSetting fov;
   private final SliderSetting priority;
   private final BooleanSetting detectMining;
   private final BooleanSetting usingPause;
   private final BooleanSetting moveDisable;
   private final BooleanSetting jumpDisable;
   private final BooleanSetting noBlockDisable;
   public class_243 directionVec;
   double startX;
   double startY;
   double startZ;
   int progress;
   private boolean shouldCenter;
   public static final List<class_2338> airList = new ArrayList();

   public Surround() {
      super("Surround", Module.Category.Combat);
      this.page = this.add(new EnumSetting("Page", Surround.Page.General));
      this.placeDelay = this.add((new SliderSetting("PlaceDelay", 100, 0, 500, () -> this.page.is(Surround.Page.General))).setSuffix("ms"));
      this.mineDownward = this.add(new BooleanSetting("MineDownward", true, () -> this.page.is(Surround.Page.General)));
      this.extend = this.add(new BooleanSetting("Extend", true, () -> this.page.is(Surround.Page.General))).setParent();
      this.onlySelf = this.add(new BooleanSetting("OnlySelf", true, () -> this.page.is(Surround.Page.General) && this.extend.isOpen()));
      this.inAir = this.add(new BooleanSetting("InAir", true, () -> this.page.is(Surround.Page.Check)));
      this.timer = new Timer();
      this.blocksPer = this.add((new SliderSetting("BlocksPer", 4, 1, 8, () -> this.page.is(Surround.Page.General))).setSuffix("blocks"));
      this.packetPlace = this.add(new BooleanSetting("PacketPlace", true, () -> this.page.is(Surround.Page.General)));
      this.timing = this.add(new EnumSetting("Timing", Timing.All, () -> this.page.is(Surround.Page.General)));
      this.breakCrystal = this.add((new BooleanSetting("Break", true, () -> this.page.is(Surround.Page.General))).setParent());
      this.usingBreak = this.add(new BooleanSetting("UsingBreak", false, () -> this.page.is(Surround.Page.General) && this.breakCrystal.isOpen()));
      this.center = this.add(new BooleanSetting("Center", false, () -> this.page.is(Surround.Page.General)));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(Surround.Page.General)));
      this.enderChest = this.add(new BooleanSetting("EnderChest", false, () -> this.page.is(Surround.Page.General)));
      this.godMode = this.add(new BooleanSetting("GodMode", true, () -> this.page.is(Surround.Page.GodMode)));
      this.breakTime = this.add((new SliderSetting("BreakTime", 1.2, (double)0.0F, (double)3.0F, 0.1, () -> this.page.is(Surround.Page.GodMode))).setSuffix("s"));
      this.failedSkip = this.add(new BooleanSetting("FailedSkip", false, () -> this.page.is(Surround.Page.GodMode)));
      this.rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.getValue() == Surround.Page.Rotate));
      this.yawStep = this.add(new BooleanSetting("YawStep", false, () -> this.page.getValue() == Surround.Page.Rotate));
      this.whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.page.getValue() == Surround.Page.Rotate));
      this.steps = this.add(new SliderSetting("Steps", (double)1.0F, (double)0.0F, (double)1.0F, 0.01, () -> this.page.getValue() == Surround.Page.Rotate));
      this.checkFov = this.add(new BooleanSetting("OnlyLooking", false, () -> this.page.getValue() == Surround.Page.Rotate));
      this.fov = this.add((new SliderSetting("Fov", (double)10.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.page.getValue() == Surround.Page.Rotate)).setSuffix("°"));
      this.priority = this.add(new SliderSetting("Priority", 60, 0, 100, () -> this.page.getValue() == Surround.Page.Rotate));
      this.detectMining = this.add(new BooleanSetting("DetectMining", false, () -> this.page.is(Surround.Page.Check)));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.page.is(Surround.Page.Check)));
      this.moveDisable = this.add(new BooleanSetting("MoveDisable", false, () -> this.page.is(Surround.Page.Check)));
      this.jumpDisable = this.add(new BooleanSetting("JumpDisable", false, () -> this.page.is(Surround.Page.Check)));
      this.noBlockDisable = this.add(new BooleanSetting("NoBlockDisable", false, () -> this.page.is(Surround.Page.Check)));
      this.directionVec = null;
      this.startX = (double)0.0F;
      this.startY = (double)0.0F;
      this.startZ = (double)0.0F;
      this.progress = 0;
      this.shouldCenter = true;
      this.setChinese("围脚");
      INSTANCE = this;
   }

   public static boolean selfIntersectPos(class_2338 pos) {
      return mc.field_1724.method_5829().method_994(new class_238(pos));
   }

   public static boolean otherIntersectPos(class_2338 pos) {
      for(class_742 player : Astra.THREAD.getPlayers()) {
         if (player.method_5829().method_994(new class_238(pos))) {
            return true;
         }
      }

      return false;
   }

   public static class_241 getRotationTo(class_243 posFrom, class_243 posTo) {
      class_243 vec3d = posTo.method_1020(posFrom);
      return getRotationFromVec(vec3d);
   }

   private static class_241 getRotationFromVec(class_243 vec) {
      double d = vec.field_1352;
      double d2 = vec.field_1350;
      double xz = Math.hypot(d, d2);
      d2 = vec.field_1350;
      double d3 = vec.field_1352;
      double yaw = normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - (double)90.0F);
      double pitch = normalizeAngle(Math.toDegrees(-Math.atan2(vec.field_1351, xz)));
      return new class_241((float)yaw, (float)pitch);
   }

   private static double normalizeAngle(double angleIn) {
      double angle = angleIn % (double)360.0F;
      if (angle >= (double)180.0F) {
         angle -= (double)360.0F;
      }

      if (angle < (double)-180.0F) {
         angle += (double)360.0F;
      }

      return angle;
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.directionVec != null && this.rotate.getValue() && this.yawStep.getValue()) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         airList.clear();
         if (!this.inventory.getValue() || EntityUtil.inInventory()) {
            if ((!this.timing.is(Timing.Pre) || !event.isPost()) && (!this.timing.is(Timing.Post) || !event.isPre())) {
               if (this.timer.passed((long)this.placeDelay.getValue())) {
                  if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
                     this.directionVec = null;
                     this.progress = 0;
                     if (!MovementUtil.isMoving() && !mc.field_1690.field_1903.method_1434()) {
                        this.startX = mc.field_1724.method_23317();
                        this.startY = mc.field_1724.method_23318();
                        this.startZ = mc.field_1724.method_23321();
                     }

                     double distanceToStart = (double)class_3532.method_15355((float)mc.field_1724.method_5649(this.startX, this.startY, this.startZ));
                     if (this.getBlock() == -1) {
                        if (this.noBlockDisable.getValue()) {
                           this.disable();
                        }

                     } else if ((!this.moveDisable.getValue() || !(distanceToStart > (double)1.0F)) && (!this.jumpDisable.getValue() || !mc.field_1724.field_3913.field_3904)) {
                        if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                           if (this.inAir.getValue() || mc.field_1724.method_24828()) {
                              this.doSurround(new BlockPosX(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321()));
                              this.doSurround(new BlockPosX(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.8, mc.field_1724.method_23321()));
                           }
                        }
                     } else {
                        this.disable();
                     }
                  }
               }
            }
         }
      }
   }

   public void doSurround(class_2338 pos) {
      for(class_2350 i : class_2350.values()) {
         if (i != class_2350.field_11036) {
            class_2338 offsetPos = pos.method_10093(i);
            if (BlockUtil.getPlaceSide(offsetPos) != null) {
               this.tryPlaceBlock(offsetPos);
            } else if (BlockUtil.canReplace(offsetPos)) {
               this.tryPlaceBlock(this.getHelperPos(offsetPos));
            }

            if ((selfIntersectPos(offsetPos) || !this.onlySelf.getValue() && otherIntersectPos(offsetPos)) && this.extend.getValue()) {
               for(class_2350 i2 : class_2350.values()) {
                  if (i2 != class_2350.field_11036) {
                     class_2338 offsetPos2 = offsetPos.method_10093(i2);
                     if (selfIntersectPos(offsetPos2) || !this.onlySelf.getValue() && otherIntersectPos(offsetPos2)) {
                        for(class_2350 i3 : class_2350.values()) {
                           if (i3 != class_2350.field_11036) {
                              this.tryPlaceBlock(offsetPos2);
                              class_2338 offsetPos3 = offsetPos2.method_10093(i3);
                              this.tryPlaceBlock(BlockUtil.getPlaceSide(offsetPos3) == null && BlockUtil.canReplace(offsetPos3) ? this.getHelperPos(offsetPos3) : offsetPos3);
                           }
                        }
                     }

                     this.tryPlaceBlock(BlockUtil.getPlaceSide(offsetPos2) == null && BlockUtil.canReplace(offsetPos2) ? this.getHelperPos(offsetPos2) : offsetPos2);
                  }
               }
            }
         }
      }

   }

   public void onEnable() {
      if (!nullCheck()) {
         this.startX = mc.field_1724.method_23317();
         this.startY = mc.field_1724.method_23318();
         this.startZ = mc.field_1724.method_23321();
         this.shouldCenter = true;
      } else {
         if (this.moveDisable.getValue() || this.jumpDisable.getValue()) {
            this.disable();
         }

      }
   }

   private boolean shouldYawStep() {
      return this.whenElytra.getValue() || !mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() : false;
   }

   @EventListener(
      priority = -1
   )
   public void onMove(MoveEvent event) {
      if (!nullCheck() && this.center.getValue() && !mc.field_1724.method_6128()) {
         class_2338 blockPos = EntityUtil.getPlayerPos(true);
         if (mc.field_1724.method_23317() - (double)blockPos.method_10263() - (double)0.5F <= 0.2 && mc.field_1724.method_23317() - (double)blockPos.method_10263() - (double)0.5F >= -0.2 && mc.field_1724.method_23321() - (double)blockPos.method_10260() - (double)0.5F <= 0.2 && mc.field_1724.method_23321() - (double)0.5F - (double)blockPos.method_10260() >= -0.2) {
            if (this.shouldCenter && (mc.field_1724.method_24828() || MovementUtil.isMoving())) {
               event.setX((double)0.0F);
               event.setZ((double)0.0F);
               this.shouldCenter = false;
            }
         } else if (this.shouldCenter) {
            class_243 centerPos = EntityUtil.getPlayerPos(true).method_46558();
            float rotation = getRotationTo(mc.field_1724.method_19538(), centerPos).field_1343;
            float yawRad = rotation / 180.0F * (float)Math.PI;
            double dist = mc.field_1724.method_19538().method_1022(new class_243(centerPos.field_1352, mc.field_1724.method_23318(), centerPos.field_1350));
            double cappedSpeed = Math.min(0.2873, dist);
            double x = (double)(-((float)Math.sin((double)yawRad))) * cappedSpeed;
            double z = (double)((float)Math.cos((double)yawRad)) * cappedSpeed;
            event.setX(x);
            event.setZ(z);
         }

      }
   }

   private void tryPlaceBlock(class_2338 pos) {
      if (pos != null) {
         if (this.godMode.getValue()) {
            for(BreakManager.BreakData breakData : Astra.BREAK.breakMap.values()) {
               if (breakData.getEntity() != null && (!this.failedSkip.getValue() || !breakData.failed) && breakData.pos.equals(pos)) {
                  if ((double)breakData.timer.getMs() < this.breakTime.getValue() * (double)1000.0F) {
                     return;
                  }

                  airList.add(pos);
                  break;
               }
            }
         }

         if (!this.detectMining.getValue() || !Astra.BREAK.isMining(pos)) {
            if ((double)this.progress < this.blocksPer.getValue()) {
               class_2338 self = EntityUtil.getPlayerPos(true);
               if (!this.mineDownward.getValue() || !Objects.equals(PacketMine.getBreakPos(), self.method_10074()) || !Objects.equals(PacketMine.getBreakPos(), pos)) {
                  int block = this.getBlock();
                  if (block != -1) {
                     class_2350 side = BlockUtil.getPlaceSide(pos);
                     if (side != null) {
                        class_243 directionVec = new class_243((double)pos.method_10263() + (double)0.5F + (double)side.method_10163().method_10263() * (double)0.5F, (double)pos.method_10264() + (double)0.5F + (double)side.method_10163().method_10264() * (double)0.5F, (double)pos.method_10260() + (double)0.5F + (double)side.method_10163().method_10260() * (double)0.5F);
                        if (BlockUtil.canPlace(pos, (double)6.0F, true)) {
                           if (!this.rotate.getValue() || this.faceVector(directionVec)) {
                              if (this.breakCrystal.getValue()) {
                                 CombatUtil.attackCrystal(pos, this.rotate.getValue(), !this.usingBreak.getValue());
                              } else if (BlockUtil.hasEntity(pos, false)) {
                                 return;
                              }

                              int old = mc.field_1724.method_31548().field_7545;
                              this.doSwap(block);
                              BlockUtil.placedPos.add(pos);
                              if (BlockUtil.allowAirPlace()) {
                                 BlockUtil.airPlace(pos, false, class_1268.field_5808, this.packetPlace.getValue());
                              } else {
                                 BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), false, class_1268.field_5808, this.packetPlace.getValue());
                              }

                              this.timer.reset();
                              if (this.inventory.getValue()) {
                                 this.doSwap(block);
                                 EntityUtil.syncInventory();
                              } else {
                                 this.doSwap(old);
                              }

                              if (this.rotate.getValue() && !this.yawStep.getValue()) {
                                 Astra.ROTATION.snapBack();
                              }

                              ++this.progress;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean faceVector(class_243 directionVec) {
      if (!this.yawStep.getValue()) {
         Astra.ROTATION.lookAt(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         if (Astra.ROTATION.inFov(directionVec, this.fov.getValueFloat())) {
            return true;
         } else {
            return !this.checkFov.getValue();
         }
      }
   }

   private void doSwap(int slot) {
      if (this.inventory.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private int getBlock() {
      if (this.inventory.getValue()) {
         return InventoryUtil.findBlockInventorySlot(class_2246.field_10540) == -1 && this.enderChest.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10443) : InventoryUtil.findBlockInventorySlot(class_2246.field_10540);
      } else {
         return InventoryUtil.findBlock(class_2246.field_10540) == -1 && this.enderChest.getValue() ? InventoryUtil.findBlock(class_2246.field_10443) : InventoryUtil.findBlock(class_2246.field_10540);
      }
   }

   public class_2338 getHelperPos(class_2338 pos) {
      for(class_2350 i : class_2350.values()) {
         if ((!this.detectMining.getValue() || !Astra.BREAK.isMining(pos.method_10093(i))) && BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) && BlockUtil.canPlace(pos.method_10093(i))) {
            return pos.method_10093(i);
         }
      }

      return null;
   }

   public static enum Page {
      General,
      Rotate,
      Check,
      GodMode;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Rotate, Check, GodMode};
      }
   }
}
