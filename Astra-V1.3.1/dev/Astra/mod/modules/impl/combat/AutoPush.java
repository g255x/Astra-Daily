package dev.Astra.mod.modules.impl.combat;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.api.utils.math.MathUtil;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.EntityUtil;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.api.utils.world.BlockPosX;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.core.impl.RotationManager;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.player.PacketMine;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2318;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2665;
import net.minecraft.class_2680;

public class AutoPush extends Module {
   public static AutoPush INSTANCE;
   private final BooleanSetting torch = this.add(new BooleanSetting("Torch", false));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true));
   private final BooleanSetting yawDeceive = this.add(new BooleanSetting("YawDeceive", true));
   private final BooleanSetting pistonPacket = this.add(new BooleanSetting("PistonPacket", false));
   private final BooleanSetting powerPacket = this.add(new BooleanSetting("PowerPacket", true));
   private final BooleanSetting noEating = this.add(new BooleanSetting("EatingPause", true));
   private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true));
   private final BooleanSetting allowWeb = this.add(new BooleanSetting("AllowWeb", true));
   private final SliderSetting updateDelay = this.add(new SliderSetting("Delay", 100, 0, 1000));
   private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true));
   private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false));
   private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
   private final SliderSetting range = this.add(new SliderSetting("Range", (double)5.0F, (double)0.0F, (double)6.0F));
   private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", (double)5.0F, (double)0.0F, (double)6.0F));
   private final SliderSetting surroundCheck = this.add(new SliderSetting("SurroundCheck", 2, 0, 4));
   private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
   private final Timer timer = new Timer();

   public AutoPush() {
      super("AutoPush", Module.Category.Combat);
      this.setChinese("活塞推人");
      INSTANCE = this;
   }

   public static void pistonFacing(class_2350 i) {
      if (i == class_2350.field_11034) {
         Astra.ROTATION.snapAt(-90.0F, 5.0F);
      } else if (i == class_2350.field_11039) {
         Astra.ROTATION.snapAt(90.0F, 5.0F);
      } else if (i == class_2350.field_11043) {
         Astra.ROTATION.snapAt(180.0F, 5.0F);
      } else if (i == class_2350.field_11035) {
         Astra.ROTATION.snapAt(0.0F, 5.0F);
      }

   }

   public void onEnable() {
      AutoCrystal.INSTANCE.lastBreakTimer.reset();
   }

   boolean isTargetHere(class_2338 pos, class_1297 target) {
      return (new class_238(pos)).method_994(target.method_5829());
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passedMs(this.updateDelay.getValue())) {
         if (!this.selfGround.getValue() || mc.field_1724.method_24828()) {
            if (this.findBlock(this.getBlockType()) != -1 && this.findClass(class_2665.class) != -1) {
               if (!this.noEating.getValue() || !mc.field_1724.method_6115()) {
                  if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                     for(class_1657 player : CombatUtil.getEnemies(this.range.getValue())) {
                        if (this.canPush(player)) {
                           float[] offset;
                           for(float x : offset = new float[]{-0.25F, 0.0F, 0.25F}) {
                              for(float z : offset) {
                                 BlockPosX playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + (double)0.5F, player.method_23321() + (double)z);

                                 for(class_2350 i : class_2350.values()) {
                                    class_2338 pos;
                                    if (i != class_2350.field_11036 && i != class_2350.field_11033 && this.isTargetHere(pos = playerPos.method_10093(i), player) && mc.field_1687.method_39454(player, new class_238(pos))) {
                                       if (this.tryPush(playerPos.method_10093(i.method_10153()), i)) {
                                          this.timer.reset();
                                          return;
                                       }

                                       if (this.tryPush(playerPos.method_10093(i.method_10153()).method_10084(), i)) {
                                          this.timer.reset();
                                          return;
                                       }
                                    }
                                 }
                              }
                           }

                           if (!mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317(), player.method_23318() + (double)2.5F, player.method_23321())))) {
                              for(class_2350 i : class_2350.values()) {
                                 if (i != class_2350.field_11036 && i != class_2350.field_11033) {
                                    class_2338 pos2 = EntityUtil.getEntityPos(player).method_10093(i);
                                    class_238 box = player.method_5829().method_997(new class_243((double)i.method_10148(), (double)i.method_10164(), (double)i.method_10165()));
                                    if (this.getBlock(pos2.method_10084()) != class_2246.field_10379 && !mc.field_1687.method_39454(player, box.method_989((double)0.0F, (double)1.0F, (double)0.0F)) && !this.isTargetHere(pos2, player)) {
                                       if (this.tryPush(EntityUtil.getEntityPos(player).method_10093(i.method_10153()).method_10084(), i)) {
                                          this.timer.reset();
                                          return;
                                       }

                                       if (this.tryPush(EntityUtil.getEntityPos(player).method_10093(i.method_10153()), i)) {
                                          this.timer.reset();
                                          return;
                                       }
                                    }
                                 }
                              }
                           }

                           for(float x : offset) {
                              for(float z : offset) {
                                 BlockPosX playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + (double)0.5F, player.method_23321() + (double)z);

                                 for(class_2350 i : class_2350.values()) {
                                    if (i != class_2350.field_11036 && i != class_2350.field_11033 && this.isTargetHere(playerPos.method_10093(i), player)) {
                                       if (this.tryPush(playerPos.method_10093(i.method_10153()).method_10084(), i)) {
                                          this.timer.reset();
                                          return;
                                       }

                                       if (this.tryPush(playerPos.method_10093(i.method_10153()), i)) {
                                          this.timer.reset();
                                          return;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }

                  }
               }
            } else {
               if (this.autoDisable.getValue()) {
                  this.disable();
               }

            }
         }
      }
   }

   private boolean tryPush(class_2338 piston, class_2350 direction) {
      if (!mc.field_1687.method_22347(piston.method_10093(direction))) {
         return false;
      } else {
         if (this.isTrueFacing(piston, direction) && this.facingCheck(piston) && BlockUtil.clientCanPlace(piston, false)) {
            boolean canPower = false;
            if (BlockUtil.getPlaceSide(piston, this.placeRange.getValue()) != null) {
               CombatUtil.modifyPos = piston;
               CombatUtil.modifyBlockState = class_2246.field_10560.method_9564();

               for(class_2350 direction2 : class_2350.values()) {
                  if (this.getBlock(piston.method_10093(direction2)) == this.getBlockType()) {
                     canPower = true;
                     break;
                  }
               }

               for(class_2350 direction3 : class_2350.values()) {
                  if (canPower) {
                     break;
                  }

                  if (BlockUtil.canPlace(piston.method_10093(direction3), this.placeRange.getValue())) {
                     canPower = true;
                  }
               }

               CombatUtil.modifyPos = null;
               if (canPower) {
                  int pistonSlot = this.findClass(class_2665.class);
                  class_2350 side = BlockUtil.getPlaceSide(piston);
                  if (side != null) {
                     if (this.rotate.getValue()) {
                        Astra.ROTATION.lookAt(piston.method_10093(side), side.method_10153());
                     }

                     if (this.yawDeceive.getValue()) {
                        pistonFacing(direction.method_10153());
                     }

                     int old = mc.field_1724.method_31548().field_7545;
                     this.doSwap(pistonSlot);
                     BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                     if (this.inventory.getValue()) {
                        this.doSwap(pistonSlot);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(old);
                     }

                     if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                        Astra.ROTATION.lookAt(piston.method_10093(side), side.method_10153());
                     }

                     if (this.rotate.getValue()) {
                        Astra.ROTATION.snapBack();
                     }

                     for(class_2350 i : class_2350.values()) {
                        if (this.getBlock(piston.method_10093(i)) == this.getBlockType()) {
                           if (this.mine.getValue()) {
                              PacketMine.INSTANCE.mine(piston.method_10093(i));
                           }

                           if (this.autoDisable.getValue()) {
                              this.disable();
                           }

                           return true;
                        }
                     }

                     for(class_2350 i : class_2350.values()) {
                        if ((i != class_2350.field_11036 || !this.torch.getValue()) && BlockUtil.canPlace(piston.method_10093(i), this.placeRange.getValue())) {
                           int oldSlot = mc.field_1724.method_31548().field_7545;
                           int powerSlot = this.findBlock(this.getBlockType());
                           this.doSwap(powerSlot);
                           BlockUtil.placeBlock(piston.method_10093(i), this.rotate.getValue(), this.powerPacket.getValue());
                           if (this.inventory.getValue()) {
                              this.doSwap(powerSlot);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(oldSlot);
                           }

                           if (this.mine.getValue()) {
                              PacketMine.INSTANCE.mine(piston.method_10093(i));
                           }

                           return true;
                        }
                     }

                     return true;
                  }
               }
            } else {
               class_2350 powerFacing = null;

               for(class_2350 i : class_2350.values()) {
                  if (i != class_2350.field_11036 || !this.torch.getValue()) {
                     if (powerFacing != null) {
                        break;
                     }

                     CombatUtil.modifyPos = piston.method_10093(i);
                     CombatUtil.modifyBlockState = this.getBlockType().method_9564();
                     if (BlockUtil.getPlaceSide(piston) != null) {
                        powerFacing = i;
                     }

                     CombatUtil.modifyPos = null;
                     if (powerFacing != null && !BlockUtil.canPlace(piston.method_10093(powerFacing))) {
                        powerFacing = null;
                     }
                  }
               }

               if (powerFacing != null) {
                  int oldSlot = mc.field_1724.method_31548().field_7545;
                  int powerSlot = this.findBlock(this.getBlockType());
                  this.doSwap(powerSlot);
                  BlockUtil.placeBlock(piston.method_10093(powerFacing), this.rotate.getValue(), this.powerPacket.getValue());
                  if (this.inventory.getValue()) {
                     this.doSwap(powerSlot);
                     EntityUtil.syncInventory();
                  } else {
                     this.doSwap(oldSlot);
                  }

                  CombatUtil.modifyPos = piston.method_10093(powerFacing);
                  CombatUtil.modifyBlockState = this.getBlockType().method_9564();
                  int n = this.findClass(class_2665.class);
                  class_2350 side2 = BlockUtil.getPlaceSide(piston);
                  if (side2 != null) {
                     if (this.rotate.getValue()) {
                        Astra.ROTATION.lookAt(piston.method_10093(side2), side2.method_10153());
                     }

                     if (this.yawDeceive.getValue()) {
                        pistonFacing(direction.method_10153());
                     }

                     int old = mc.field_1724.method_31548().field_7545;
                     this.doSwap(n);
                     BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                     if (this.inventory.getValue()) {
                        this.doSwap(n);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(old);
                     }

                     if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                        Astra.ROTATION.lookAt(piston.method_10093(side2), side2.method_10153());
                     }

                     if (this.rotate.getValue()) {
                        Astra.ROTATION.snapBack();
                     }
                  }

                  CombatUtil.modifyPos = null;
                  return true;
               }
            }
         }

         if (mc.field_1687.method_8320(piston).method_26204() instanceof class_2665 && this.getBlockState(piston).method_11654(class_2318.field_10927) == direction) {
            for(class_2350 direction4 : class_2350.values()) {
               if (this.getBlock(piston.method_10093(direction4)) == this.getBlockType()) {
                  if (this.autoDisable.getValue()) {
                     this.disable();
                     return true;
                  }

                  return false;
               }
            }

            for(class_2350 direction5 : class_2350.values()) {
               if ((direction5 != class_2350.field_11036 || !this.torch.getValue()) && BlockUtil.canPlace(piston.method_10093(direction5), this.placeRange.getValue())) {
                  int oldSlot = mc.field_1724.method_31548().field_7545;
                  int powerSlot = this.findBlock(this.getBlockType());
                  this.doSwap(powerSlot);
                  BlockUtil.placeBlock(piston.method_10093(direction5), this.rotate.getValue(), this.powerPacket.getValue());
                  if (this.inventory.getValue()) {
                     this.doSwap(powerSlot);
                     EntityUtil.syncInventory();
                  } else {
                     this.doSwap(oldSlot);
                  }

                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean facingCheck(class_2338 pos) {
      if (!ClientSetting.INSTANCE.lowVersion.getValue()) {
         return true;
      } else {
         class_2350 direction = MathUtil.getDirectionFromEntityLiving(pos, mc.field_1724);
         return direction != class_2350.field_11036 && direction != class_2350.field_11033;
      }
   }

   private boolean isTrueFacing(class_2338 pos, class_2350 facing) {
      if (this.yawDeceive.getValue()) {
         return true;
      } else {
         class_2350 side = BlockUtil.getPlaceSide(pos);
         if (side == null) {
            return false;
         } else {
            class_243 directionVec = new class_243((double)pos.method_10263() + (double)0.5F + (double)side.method_10163().method_10263() * (double)0.5F, (double)pos.method_10264() + (double)0.5F + (double)side.method_10163().method_10264() * (double)0.5F, (double)pos.method_10260() + (double)0.5F + (double)side.method_10163().method_10260() * (double)0.5F);
            float[] rotation = RotationManager.getRotation(directionVec);
            return MathUtil.getFacingOrder(rotation[0], rotation[1]).method_10153() == facing;
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

   public int findBlock(class_2248 blockIn) {
      return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(blockIn) : InventoryUtil.findBlock(blockIn);
   }

   public int findClass(Class<?> clazz) {
      return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(clazz) : InventoryUtil.findClass(clazz);
   }

   private Boolean canPush(class_1657 player) {
      if (this.onlyGround.getValue() && !player.method_24828()) {
         return false;
      } else if (!this.allowWeb.getValue() && Astra.PLAYER.isInWeb(player)) {
         return false;
      } else {
         float[] offset = new float[]{-0.25F, 0.0F, 0.25F};
         int progress = 0;
         if (mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317() + (double)1.0F, player.method_23318() + (double)0.5F, player.method_23321())))) {
            ++progress;
         }

         if (mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317() - (double)1.0F, player.method_23318() + (double)0.5F, player.method_23321())))) {
            ++progress;
         }

         if (mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317(), player.method_23318() + (double)0.5F, player.method_23321() + (double)1.0F)))) {
            ++progress;
         }

         if (mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317(), player.method_23318() + (double)0.5F, player.method_23321() - (double)1.0F)))) {
            ++progress;
         }

         for(float x : offset) {
            for(float z : offset) {
               BlockPosX playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + (double)0.5F, player.method_23321() + (double)z);

               for(class_2350 i : class_2350.values()) {
                  class_2338 pos;
                  if (i != class_2350.field_11036 && i != class_2350.field_11033 && this.isTargetHere(pos = playerPos.method_10093(i), player)) {
                     if (mc.field_1687.method_39454(player, new class_238(pos))) {
                        return true;
                     }

                     if ((double)progress > this.surroundCheck.getValue() - (double)1.0F) {
                        return true;
                     }
                  }
               }
            }
         }

         if (!mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317(), player.method_23318() + (double)2.5F, player.method_23321())))) {
            for(class_2350 i : class_2350.values()) {
               if (i != class_2350.field_11036 && i != class_2350.field_11033) {
                  class_2338 pos = EntityUtil.getEntityPos(player).method_10093(i);
                  class_238 box = player.method_5829().method_997(new class_243((double)i.method_10148(), (double)i.method_10164(), (double)i.method_10165()));
                  if (this.getBlock(pos.method_10084()) != class_2246.field_10379 && !mc.field_1687.method_39454(player, box.method_989((double)0.0F, (double)1.0F, (double)0.0F)) && !this.isTargetHere(pos, player) && mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317(), player.method_23318() + (double)0.5F, player.method_23321())))) {
                     return true;
                  }
               }
            }
         }

         return (double)progress > this.surroundCheck.getValue() - (double)1.0F || Astra.HOLE.isHard((class_2338)(new BlockPosX(player.method_23317(), player.method_23318() + (double)0.5F, player.method_23321())));
      }
   }

   private class_2248 getBlock(class_2338 pos) {
      return mc.field_1687.method_8320(pos).method_26204();
   }

   private class_2248 getBlockType() {
      return this.torch.getValue() ? class_2246.field_10523 : class_2246.field_10002;
   }

   private class_2680 getBlockState(class_2338 pos) {
      return mc.field_1687.method_8320(pos);
   }
}
