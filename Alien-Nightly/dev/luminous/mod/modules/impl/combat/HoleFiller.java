package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.PredictUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_2246;
import net.minecraft.class_2338;

public class HoleFiller extends Module {
   public static HoleFiller INSTANCE;
   public final SliderSetting placeDelay = this.add((new SliderSetting("PlaceDelay", 50, 0, 500)).setSuffix("ms"));
   public final BooleanSetting inAirPause = this.add(new BooleanSetting("InAirPause", true));
   private final Timer timer = new Timer();
   private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8));
   private final SliderSetting placeRange = this.add((new SliderSetting("PlaceRange", (double)5.0F, (double)0.0F, (double)8.0F)).setSuffix("m"));
   private final SliderSetting enemyRange = this.add((new SliderSetting("EnemyRange", (double)6.0F, (double)0.0F, (double)8.0F)).setSuffix("m"));
   private final SliderSetting holeRange = this.add((new SliderSetting("HoleRange", (double)2.0F, (double)0.0F, (double)8.0F)).setSuffix("m"));
   private final SliderSetting selfRange = this.add((new SliderSetting("SelfRange", (double)2.0F, (double)0.0F, (double)8.0F)).setSuffix("m"));
   private final SliderSetting predictTicks = this.add((new SliderSetting("Predict", 1, 1, 8)).setSuffix("tick"));
   private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", false));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true));
   private final BooleanSetting breakCrystal = this.add((new BooleanSetting("Break", true)).setParent());
   private final BooleanSetting eatPause;
   private final BooleanSetting usingPause;
   private final BooleanSetting inventory;
   private final BooleanSetting web;
   int progress;

   public HoleFiller() {
      super("HoleFiller", Module.Category.Combat);
      BooleanSetting var10006 = this.breakCrystal;
      Objects.requireNonNull(var10006);
      this.eatPause = this.add(new BooleanSetting("EatingPause", true, var10006::isOpen));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true));
      this.web = this.add(new BooleanSetting("Web", true));
      this.progress = 0;
      this.setChinese("自动填坑");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passed((long)this.placeDelay.getValue()) && (!this.inventory.getValue() || EntityUtil.inInventory())) {
         this.progress = 0;
         if (this.getBlock() != -1 && (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) && (!this.usingPause.getValue() || !mc.field_1724.method_6115()) && (!this.inAirPause.getValue() || mc.field_1724.method_24828())) {
            CombatUtil.getEnemies(this.enemyRange.getValue()).stream().flatMap((enemy) -> BlockUtil.getSphere(this.holeRange.getValueFloat(), PredictUtil.getPos(enemy, this.predictTicks.getValueInt())).stream()).filter((pos) -> pos.method_46558().method_1022(mc.field_1724.method_19538()) > this.selfRange.getValue() && (Alien.HOLE.isHole(pos, true, true, false) || Alien.HOLE.isDoubleHole(pos))).distinct().forEach(this::tryPlaceBlock);
         }
      }

   }

   private void tryPlaceBlock(class_2338 pos) {
      if (pos != null && (!this.detectMining.getValue() || !Alien.BREAK.isMining(pos)) && (double)this.progress < this.blocksPer.getValue()) {
         int block = this.getBlock();
         if (block != -1 && BlockUtil.canPlace(pos, this.placeRange.getValue(), true)) {
            if (this.breakCrystal.getValue()) {
               CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.eatPause.getValue());
            } else if (BlockUtil.hasEntity(pos, false)) {
               return;
            }

            int old = mc.field_1724.method_31548().field_7545;
            this.doSwap(block);
            BlockUtil.placeBlock(pos, this.rotate.getValue(), this.packetPlace.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(block);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(old);
            }

            ++this.progress;
            this.timer.reset();
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
         return this.web.getValue() && InventoryUtil.findBlockInventorySlot(class_2246.field_10343) != -1 ? InventoryUtil.findBlockInventorySlot(class_2246.field_10343) : InventoryUtil.findBlockInventorySlot(class_2246.field_10540);
      } else {
         return this.web.getValue() && InventoryUtil.findBlock(class_2246.field_10343) != -1 ? InventoryUtil.findBlock(class_2246.field_10343) : InventoryUtil.findBlock(class_2246.field_10540);
      }
   }
}
