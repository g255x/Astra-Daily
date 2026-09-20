package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.RotationEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import net.minecraft.class_1268;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2885;
import net.minecraft.class_3965;

public class AntiPistonPush extends Module {
   public static AntiPistonPush INSTANCE;
   public final BooleanSetting onlyBlock = this.add(new BooleanSetting("OnlyBurrow", true));
   public final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500));
   public final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 2, 1, 10));
   public final BooleanSetting feet = this.add(new BooleanSetting("Feet", true));
   public final BooleanSetting face = this.add(new BooleanSetting("Face", true));
   public final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true));
   public final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   public final BooleanSetting swing = this.add(new BooleanSetting("Swing", true));
   public final BooleanSetting autoburrow = this.add(new BooleanSetting("AutoBurrow", false));
   private final Timer timer = new Timer();
   private boolean webPlaced = false;
   public class_243 directionVec = null;
   int progress = 0;
   final ArrayList<class_2338> pos = new ArrayList();

   public AntiPistonPush() {
      super("AntiPistonPush", Module.Category.Combat);
      this.setChinese("防活塞推");
      INSTANCE = this;
   }

   public String getInfo() {
      return this.pos.isEmpty() ? null : "Working";
   }

   @EventListener
   public void onRotate(RotationEvent event) {
      if (this.rotate.getValue() && this.directionVec != null) {
         Astra.ROTATION.lookAt(this.directionVec);
      }

   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         this.update();
      }

   }

   private void update() {
      if (this.timer.passed((long)this.placeDelay.getValueInt()) && (!this.inventorySwap.getValue() || EntityUtil.inInventory()) && (!this.onlyBlock.getValue() || EntityUtil.isInsideBlock())) {
         this.pos.clear();
         this.progress = 0;
         this.directionVec = null;
         if (this.getWebSlot() != -1 && (Blink.INSTANCE == null || !Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue())) {
            class_243 playerPos = mc.field_1724.method_19538();
            if (this.feet.getValue()) {
               this.placeWeb(new BlockPosX(playerPos.method_10216(), playerPos.method_10214(), playerPos.method_10215()));
            }

            if (this.face.getValue()) {
               this.placeWeb(new BlockPosX(playerPos.method_10216(), playerPos.method_10214() + 1.1, playerPos.method_10215()));
            }

            if (this.webPlaced && !this.isBurrowed() && this.autoburrow.getValue() && AutoWeb.INSTANCE != null && !AutoWeb.INSTANCE.isOn()) {
               AutoWeb.INSTANCE.enable();
               this.webPlaced = false;
            }
         }
      }

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
         } else if (BlockUtil.getPlaceSide(pos) != null && (mc.field_1687.method_22347(pos) || BlockUtil.getBlock(pos) == class_2246.field_10343)) {
            int oldSlot = mc.field_1724.method_31548().field_7545;
            int webSlot = this.getWebSlot();
            if (!this.placeBlock(pos, this.rotate.getValue(), webSlot)) {
               return false;
            } else {
               BlockUtil.placedPos.add(pos);
               ++this.progress;
               this.webPlaced = true;
               if (this.inventorySwap.getValue()) {
                  this.doSwap(webSlot);
                  EntityUtil.syncInventory();
               } else {
                  this.doSwap(oldSlot);
               }

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
      if (rotate) {
         this.directionVec = directionVec;
         Astra.ROTATION.lookAt(directionVec);
      }

      this.doSwap(slot);
      class_3965 result = new class_3965(directionVec, side, pos, false);
      Module.sendSequencedPacket((id) -> new class_2885(class_1268.field_5808, result, id));
      if (this.swing.getValue()) {
         EntityUtil.swingHand(class_1268.field_5808, AntiCheat.INSTANCE.interactSwing.getValue());
      }

      if (rotate) {
         Astra.ROTATION.snapBack();
      }

      return true;
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

   private boolean isBurrowed() {
      class_2338 playerPos = mc.field_1724.method_24515();
      return !mc.field_1687.method_22347(playerPos);
   }
}
