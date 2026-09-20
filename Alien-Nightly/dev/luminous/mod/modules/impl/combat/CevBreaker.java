package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;

public class CevBreaker extends Module {
   public static CevBreaker INSTANCE;
   private final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", (double)5.0F, (double)0.0F, (double)8.0F, 0.1));
   private final SliderSetting breakRange = this.add(new SliderSetting("BreakRange", (double)5.0F, (double)0.0F, (double)8.0F, 0.1));
   private final SliderSetting delay = this.add((new SliderSetting("Delay", 100, 0, 500)).setSuffix("ms"));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting ground = this.add(new BooleanSetting("Ground", true));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting top = this.add(new BooleanSetting("Top", false));
   private final BooleanSetting bevel = this.add(new BooleanSetting("Bevel", true));
   private final Timer timer = new Timer();
   private class_1657 target = null;

   public CevBreaker() {
      super("CevBreaker", Module.Category.Combat);
      this.setChinese("自动炸头");
      INSTANCE = this;
   }

   public static boolean canPlaceCrystal(class_2338 pos) {
      return mc.field_1687.method_22347(pos) && BlockUtil.noEntityBlockCrystal(pos, false) && BlockUtil.noEntityBlockCrystal(pos.method_10084(), false) && (!ClientSetting.INSTANCE.lowVersion.getValue() || mc.field_1687.method_22347(pos.method_10084()));
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if ((!this.inventory.getValue() || EntityUtil.inInventory()) && (!this.ground.getValue() || mc.field_1724.method_24828())) {
         SpeedMine.INSTANCE.crystal.setValue(true);
         if ((this.target = CombatUtil.getClosestEnemy(this.targetRange.getValue())) != null) {
            class_2338 targetPos = EntityUtil.getEntityPos(this.target);
            if (SpeedMine.getBreakPos() != null) {
               for(class_2350 facing : class_2350.values()) {
                  if (facing != class_2350.field_11033) {
                     if (facing == class_2350.field_11036) {
                        if (!this.top.getValue()) {
                           continue;
                        }
                     } else if (!this.bevel.getValue()) {
                        continue;
                     }

                     class_2338 pos = targetPos.method_10086(1).method_10093(facing);
                     if (!(pos.method_10084().method_46558().method_1022(mc.field_1724.method_19538()) > this.breakRange.getValue()) && SpeedMine.getBreakPos().equals(targetPos.method_10086(1).method_10093(facing))) {
                        if (canPlaceCrystal(targetPos.method_10086(2).method_10093(facing))) {
                           if (mc.field_1687.method_22347(pos)) {
                              if (BlockUtil.canPlace(pos)) {
                                 if (!this.timer.passedMs(this.delay.getValue())) {
                                    return;
                                 }

                                 this.placeBlock(pos);
                                 this.timer.reset();
                                 return;
                              }
                           } else if (this.getBlock(pos) == class_2246.field_10540) {
                              SpeedMine.INSTANCE.mine(pos);
                              this.timer.reset();
                              return;
                           }
                        } else if (BlockUtil.hasCrystal(targetPos.method_10086(2).method_10093(facing))) {
                           if (mc.field_1687.method_22347(pos)) {
                              return;
                           }

                           if (this.getBlock(pos) == class_2246.field_10540) {
                              SpeedMine.INSTANCE.mine(pos);
                              this.timer.reset();
                              return;
                           }
                        }
                     }
                  }
               }
            }

            for(class_2350 facingx : class_2350.values()) {
               if (facingx != class_2350.field_11033) {
                  if (facingx == class_2350.field_11036) {
                     if (!this.top.getValue()) {
                        continue;
                     }
                  } else if (!this.bevel.getValue()) {
                     continue;
                  }

                  class_2338 pos = targetPos.method_10086(1).method_10093(facingx);
                  if (!(pos.method_10084().method_46558().method_1022(mc.field_1724.method_19538()) > this.breakRange.getValue())) {
                     if (canPlaceCrystal(targetPos.method_10086(2).method_10093(facingx))) {
                        if (mc.field_1687.method_22347(pos)) {
                           if (BlockUtil.canPlace(pos)) {
                              if (!this.timer.passedMs(this.delay.getValue())) {
                                 return;
                              }

                              this.placeBlock(pos);
                              this.timer.reset();
                              break;
                           }
                        } else if (this.getBlock(pos) == class_2246.field_10540) {
                           SpeedMine.INSTANCE.mine(pos);
                           this.timer.reset();
                           break;
                        }
                     } else if (BlockUtil.hasCrystal(targetPos.method_10086(2).method_10093(facingx))) {
                        if (mc.field_1687.method_22347(pos)) {
                           break;
                        }

                        if (this.getBlock(pos) == class_2246.field_10540) {
                           SpeedMine.INSTANCE.mine(pos);
                           this.timer.reset();
                           break;
                        }
                     }
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
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
   }

   private void placeBlock(class_2338 pos) {
      int block;
      if ((block = this.getBlock()) != -1) {
         int oldSlot = mc.field_1724.method_31548().field_7545;
         if (BlockUtil.canPlace(pos)) {
            if (BlockUtil.allowAirPlace()) {
               this.doSwap(block);
               BlockUtil.placedPos.add(pos);
               BlockUtil.airPlace(pos, this.rotate.getValue());
               if (this.inventory.getValue()) {
                  this.doSwap(block);
                  EntityUtil.syncInventory();
               } else {
                  this.doSwap(oldSlot);
               }

               return;
            }

            class_2350 side = BlockUtil.getPlaceSide(pos);
            if (side == null) {
               return;
            }

            this.doSwap(block);
            BlockUtil.placedPos.add(pos);
            BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), this.rotate.getValue());
            if (this.inventory.getValue()) {
               this.doSwap(block);
               EntityUtil.syncInventory();
            } else {
               this.doSwap(oldSlot);
            }
         }
      }

   }

   public String getInfo() {
      return this.target != null ? this.target.method_5477().getString() : null;
   }

   private class_2248 getBlock(class_2338 pos) {
      return mc.field_1687.method_8320(pos).method_26204();
   }
}
