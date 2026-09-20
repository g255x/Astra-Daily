package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.PredictUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_2246;
import net.minecraft.class_2338;

public class HoleFiller extends Module {
   public static HoleFiller INSTANCE;
   public final SliderSetting placeDelay = this.add((new SliderSetting("PlaceDelay", (double)200.0F, (double)0.0F, (double)500.0F)).setSuffix("ms"));
   public final BooleanSetting inAirPause = this.add(new BooleanSetting("InAirPause", false));
   private final Timer timer = new Timer();
   private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 4, 1, 8));
   private final SliderSetting placeRange = this.add((new SliderSetting("PlaceRange", (double)4.5F, (double)0.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final SliderSetting enemyRange = this.add((new SliderSetting("EnemyRange", (double)6.0F, (double)0.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final SliderSetting holeRange = this.add((new SliderSetting("HoleRange", (double)2.0F, (double)0.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final SliderSetting selfRange = this.add((new SliderSetting("SelfRange", (double)2.0F, (double)0.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final SliderSetting predictTicks = this.add((new SliderSetting("Predict", 4, 1, 8)).setSuffix("ticks"));
   private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", true));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true));
   private final BooleanSetting breakCrystal = this.add((new BooleanSetting("Break", true)).setParent());
   private final BooleanSetting usingPauseCrystal;
   private final BooleanSetting usingPause;
   private final BooleanSetting inventory;
   private final BooleanSetting web;
   int progress;

   public HoleFiller() {
      super("HoleFiller", Module.Category.Combat);
      BooleanSetting var10006 = this.breakCrystal;
      Objects.requireNonNull(var10006);
      this.usingPauseCrystal = this.add(new BooleanSetting("UsingPause", true, var10006::isOpen));
      this.usingPause = this.add(new BooleanSetting("UsingPause", true));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true));
      this.web = this.add(new BooleanSetting("Web", false));
      this.progress = 0;
      this.setChinese("自动填坑");
      INSTANCE = this;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passed((long)this.placeDelay.getValue())) {
         if (!this.inventory.getValue() || EntityUtil.inInventory()) {
            this.progress = 0;
            if (this.getBlock() != -1) {
               if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                  if (!this.usingPause.getValue() || !mc.field_1724.method_6115()) {
                     if (!this.inAirPause.getValue() || mc.field_1724.method_24828()) {
                        CombatUtil.getEnemies(this.enemyRange.getValue()).stream().flatMap((enemy) -> BlockUtil.getSphere(this.holeRange.getValueFloat(), PredictUtil.getPos(enemy, this.predictTicks.getValueInt())).stream()).filter((pos) -> pos.method_46558().method_1022(mc.field_1724.method_19538()) > this.selfRange.getValue() && (Astra.HOLE.isHole(pos, true, true, false) || Astra.HOLE.isDoubleHole(pos))).distinct().forEach(this::tryPlaceBlock);
                     }
                  }
               }
            }
         }
      }
   }

   private void tryPlaceBlock(class_2338 pos) {
      if (pos != null) {
         if (!this.detectMining.getValue() || !Astra.BREAK.isMining(pos)) {
            if ((double)this.progress < this.blocksPer.getValue()) {
               int block = this.getBlock();
               if (block != -1) {
                  if (BlockUtil.canPlace(pos, this.placeRange.getValue(), true)) {
                     if (this.breakCrystal.getValue()) {
                        CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.usingPauseCrystal.getValue());
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
