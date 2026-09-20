package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.PredictUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.impl.movement.Velocity;
import dev.Astra.mod.modules.settings.enums.Timing;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2885;
import net.minecraft.class_3965;

public class AutoWeb extends Module {
   public static AutoWeb INSTANCE;
   public static boolean force = false;
   public static boolean ignore = false;
   public final EnumSetting<Page> page;
   public final SliderSetting placeDelay;
   public final SliderSetting blocksPer;
   public final SliderSetting predictTicks;
   public final SliderSetting maxWebs;
   public final SliderSetting offset;
   public final SliderSetting placeRange;
   public final SliderSetting targetRange;
   final ArrayList<class_2338> pos;
   private final BooleanSetting preferAnchor;
   private final BooleanSetting detectMining;
   private final EnumSetting<Timing> timing;
   private final BooleanSetting feet;
   private final BooleanSetting feetExtend;
   private final BooleanSetting face;
   private final BooleanSetting down;
   private final BooleanSetting inventorySwap;
   private final BooleanSetting usingPause;
   private final BooleanSetting rotate;
   private final BooleanSetting yawStep;
   private final BooleanSetting whenElytra;
   private final SliderSetting steps;
   private final BooleanSetting checkFov;
   private final SliderSetting fov;
   private final SliderSetting priority;
   private final Timer timer;
   public class_243 directionVec;
   int progress;

   public AutoWeb() {
      super("AutoWeb", Module.Category.Combat);
      this.page = this.add(new EnumSetting("Page", AutoWeb.Page.General));
      this.placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, () -> this.page.getValue() == AutoWeb.Page.General));
      this.blocksPer = this.add(new SliderSetting("BlocksPer", 2, 1, 10, () -> this.page.getValue() == AutoWeb.Page.General));
      this.predictTicks = this.add(new SliderSetting("PredictTicks", (double)2.0F, (double)0.0F, (double)50.0F, (double)1.0F, () -> this.page.getValue() == AutoWeb.Page.General));
      this.maxWebs = this.add(new SliderSetting("MaxWebs", (double)2.0F, (double)1.0F, (double)8.0F, (double)1.0F, () -> this.page.getValue() == AutoWeb.Page.General));
      this.offset = this.add(new SliderSetting("Offset", (double)0.25F, (double)0.0F, 0.3, 0.01, () -> this.page.getValue() == AutoWeb.Page.General));
      this.placeRange = this.add(new SliderSetting("PlaceRange", (double)5.0F, (double)0.0F, (double)6.0F, 0.1, () -> this.page.getValue() == AutoWeb.Page.General));
      this.targetRange = this.add(new SliderSetting("TargetRange", (double)8.0F, (double)0.0F, (double)8.0F, 0.1, () -> this.page.getValue() == AutoWeb.Page.General));
      this.pos = new ArrayList();
      this.preferAnchor = this.add(new BooleanSetting("PreferAnchor", true, () -> this.page.getValue() == AutoWeb.Page.General));
      this.detectMining = this.add(new BooleanSetting("DetectMining", true, () -> this.page.getValue() == AutoWeb.Page.General));
      this.timing = this.add(new EnumSetting("Timing", Timing.All, () -> this.page.getValue() == AutoWeb.Page.General));
      this.feet = this.add(new BooleanSetting("Feet", true, () -> this.page.getValue() == AutoWeb.Page.General));
      this.feetExtend = this.add(new BooleanSetting("FeetExtend", true, () -> this.page.getValue() == AutoWeb.Page.General));
      this.face = this.add(new BooleanSetting("Face", true, () -> this.page.getValue() == AutoWeb.Page.General));
      this.down = this.add(new BooleanSetting("Down", true, () -> this.page.getValue() == AutoWeb.Page.General));
      this.inventorySwap = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.getValue() == AutoWeb.Page.General));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.page.getValue() == AutoWeb.Page.General));
      this.rotate = this.add((new BooleanSetting("Rotate", true, () -> this.page.getValue() == AutoWeb.Page.Rotate)).setParent());
      this.yawStep = this.add((new BooleanSetting("YawStep", false, () -> this.rotate.isOpen() && this.page.getValue() == AutoWeb.Page.Rotate)).setParent());
      this.whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == AutoWeb.Page.Rotate));
      this.steps = this.add(new SliderSetting("Steps", 0.3, 0.1, (double)1.0F, 0.01, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == AutoWeb.Page.Rotate));
      this.checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == AutoWeb.Page.Rotate));
      this.fov = this.add(new SliderSetting("Fov", (double)20.0F, (double)0.0F, (double)360.0F, 0.1, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.checkFov.getValue() && this.page.getValue() == AutoWeb.Page.Rotate));
      this.priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == AutoWeb.Page.Rotate));
      this.timer = new Timer();
      this.directionVec = null;
      this.progress = 0;
      this.setChinese("蜘蛛网光环");
      INSTANCE = this;
   }

   public String getInfo() {
      return this.pos.isEmpty() ? null : "Working";
   }

   private boolean shouldYawStep() {
      if (this.whenElytra.getValue() || !mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying())) {
         return this.yawStep.getValue() && !Velocity.INSTANCE.noRotation();
      } else {
         return false;
      }
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.rotate.getValue() && this.shouldYawStep() && this.directionVec != null) {
         event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
      }

   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if ((!this.timing.is(Timing.Pre) || !event.isPost()) && (!this.timing.is(Timing.Post) || !event.isPre())) {
            if (force) {
               ignore = true;
            }

            this.update();
            ignore = false;
         }
      }
   }

   public void onDisable() {
      force = false;
   }

   private void update() {
      if (this.timer.passed((long)this.placeDelay.getValueInt())) {
         if (!this.inventorySwap.getValue() || EntityUtil.inInventory()) {
            this.pos.clear();
            this.progress = 0;
            this.directionVec = null;
            if (!this.preferAnchor.getValue() || AutoAnchor.INSTANCE.currentPos == null) {
               if (this.getWebSlot() != -1) {
                  if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                     if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
                        label171:
                        for(class_1657 player : CombatUtil.getEnemies(this.targetRange.getValue())) {
                           class_243 playerPos = this.predictTicks.getValue() > (double)0.0F ? PredictUtil.getPos(player, this.predictTicks.getValueInt()) : player.method_19538();
                           int webs = 0;
                           if (this.feet.getValue() && this.placeWeb(new BlockPosX(playerPos.method_10216(), playerPos.method_10214(), playerPos.method_10215()))) {
                              ++webs;
                           }

                           if (this.down.getValue()) {
                              this.placeWeb(new BlockPosX(playerPos.method_10216(), playerPos.method_10214() - 0.8, playerPos.method_10215()));
                           }

                           ArrayList<BlockPosX> list = new ArrayList();

                           for(float x : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                              for(float z : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                 for(float y : new float[]{0.0F, 1.0F, -1.0F}) {
                                    BlockPosX pos = new BlockPosX(playerPos.method_10216() + (double)x, playerPos.method_10214() + (double)y, playerPos.method_10215() + (double)z);
                                    if (!list.contains(pos)) {
                                       list.add(pos);
                                       if (this.isTargetHere(pos, player) && mc.field_1687.method_8320(pos).method_26204() == class_2246.field_10343 && !Astra.BREAK.isMining(pos)) {
                                          ++webs;
                                       }
                                    }
                                 }
                              }
                           }

                           if (!((float)webs >= this.maxWebs.getValueFloat()) || ignore) {
                              boolean skip = false;
                              if (this.feetExtend.getValue()) {
                                 label145:
                                 for(float x : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                    for(float z : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                       BlockPosX pos = new BlockPosX(playerPos.method_10216() + (double)x, playerPos.method_10214(), playerPos.method_10215() + (double)z);
                                       if (this.isTargetHere(pos, player) && this.placeWeb(pos)) {
                                          ++webs;
                                          if ((float)webs >= this.maxWebs.getValueFloat()) {
                                             skip = true;
                                             break label145;
                                          }
                                       }
                                    }
                                 }
                              }

                              if (!skip && this.face.getValue()) {
                                 for(float x : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                    for(float z : new float[]{0.0F, this.offset.getValueFloat(), -this.offset.getValueFloat()}) {
                                       BlockPosX pos = new BlockPosX(playerPos.method_10216() + (double)x, playerPos.method_10214() + 1.1, playerPos.method_10215() + (double)z);
                                       if (this.isTargetHere(pos, player) && this.placeWeb(pos)) {
                                          ++webs;
                                          if ((float)webs >= this.maxWebs.getValueFloat()) {
                                             continue label171;
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }

                     }
                  }
               }
            }
         }
      }
   }

   private boolean isTargetHere(class_2338 pos, class_1657 target) {
      return (new class_238(pos)).method_994(target.method_5829());
   }

   private boolean placeWeb(class_2338 pos) {
      if (this.pos.contains(pos)) {
         return false;
      } else {
         this.pos.add(pos);
         if (this.progress >= this.blocksPer.getValueInt()) {
            return false;
         } else if (this.getWebSlot() == -1) {
            return false;
         } else if (this.detectMining.getValue() && Astra.BREAK.isMining(pos)) {
            return false;
         } else if (BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) != null && (mc.field_1687.method_22347(pos) || ignore && BlockUtil.getBlock(pos) == class_2246.field_10343) && pos.method_10264() < 320) {
            int oldSlot = mc.field_1724.method_31548().field_7545;
            int webSlot = this.getWebSlot();
            if (!this.placeBlock(pos, this.rotate.getValue(), webSlot)) {
               return false;
            } else {
               BlockUtil.placedPos.add(pos);
               ++this.progress;
               if (this.inventorySwap.getValue()) {
                  this.doSwap(webSlot);
                  EntityUtil.syncInventory();
               } else {
                  this.doSwap(oldSlot);
               }

               force = false;
               this.timer.reset();
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public boolean placeBlock(class_2338 pos, boolean rotate, int slot) {
      class_2350 side = BlockUtil.getPlaceSide(pos);
      if (side == null) {
         return BlockUtil.allowAirPlace() ? this.clickBlock(pos, class_2350.field_11033, rotate, slot) : false;
      } else {
         return this.clickBlock(pos.method_10093(side), side.method_10153(), rotate, slot);
      }
   }

   public boolean clickBlock(class_2338 pos, class_2350 side, boolean rotate, int slot) {
      class_243 directionVec = new class_243((double)pos.method_10263() + (double)0.5F + (double)side.method_10163().method_10263() * (double)0.5F, (double)pos.method_10264() + (double)0.5F + (double)side.method_10163().method_10264() * (double)0.5F, (double)pos.method_10260() + (double)0.5F + (double)side.method_10163().method_10260() * (double)0.5F);
      if (rotate && !this.faceVector(directionVec)) {
         return false;
      } else {
         this.doSwap(slot);
         EntityUtil.swingHand(class_1268.field_5808, AntiCheat.INSTANCE.interactSwing.getValue());
         class_3965 result = new class_3965(directionVec, side, pos, false);
         Module.sendSequencedPacket((id) -> new class_2885(class_1268.field_5808, result, id));
         if (rotate && !this.shouldYawStep()) {
            Astra.ROTATION.snapBack();
         }

         return true;
      }
   }

   private boolean faceVector(class_243 directionVec) {
      if (!this.shouldYawStep()) {
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
      if (this.inventorySwap.getValue()) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      } else {
         InventoryUtil.switchToSlot(slot);
      }

   }

   private int getWebSlot() {
      return this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10343) : InventoryUtil.findBlock(class_2246.field_10343);
   }

   public static enum Page {
      General,
      Rotate;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Rotate};
      }
   }
}
