package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
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
         Alien.ROTATION.snapAt(-90.0F, 5.0F);
      } else if (i == class_2350.field_11039) {
         Alien.ROTATION.snapAt(90.0F, 5.0F);
      } else if (i == class_2350.field_11043) {
         Alien.ROTATION.snapAt(180.0F, 5.0F);
      } else if (i == class_2350.field_11035) {
         Alien.ROTATION.snapAt(0.0F, 5.0F);
      }

   }

   public boolean onEnable() {
      AutoCrystal.INSTANCE.lastBreakTimer.reset();
      return false;
   }

   boolean isTargetHere(class_2338 pos, class_1297 target) {
      return (new class_238(pos)).method_994(target.method_5829());
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passedMs(this.updateDelay.getValue()) && (!this.selfGround.getValue() || mc.field_1724.method_24828())) {
         if (this.findBlock(this.getBlockType()) != -1 && this.findClass(class_2665.class) != -1) {
            if ((!this.noEating.getValue() || !mc.field_1724.method_6115()) && (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue())) {
               for(class_1657 player : CombatUtil.getEnemies(this.range.getValue())) {
                  if (this.canPush(player)) {
                     float[] offset = new float[]{-0.25F, 0.0F, 0.25F};

                     for(float x : offset) {
                        for(float z : offset) {
                           BlockPosX playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + (double)0.5F, player.method_23321() + (double)z);

                           for(class_2350 i : class_2350.values()) {
                              if (i != class_2350.field_11036 && i != class_2350.field_11033) {
                                 class_2338 pos = playerPos.method_10093(i);
                                 if (this.isTargetHere(pos, player) && mc.field_1687.method_39454(player, new class_238(pos))) {
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
                     }

                     if (!mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317(), player.method_23318() + (double)2.5F, player.method_23321())))) {
                        for(class_2350 ix : class_2350.values()) {
                           if (ix != class_2350.field_11036 && ix != class_2350.field_11033) {
                              class_2338 pos = EntityUtil.getEntityPos(player).method_10093(ix);
                              class_238 box = player.method_5829().method_997(new class_243((double)ix.method_10148(), (double)ix.method_10164(), (double)ix.method_10165()));
                              if (this.getBlock(pos.method_10084()) != class_2246.field_10379 && !mc.field_1687.method_39454(player, box.method_989((double)0.0F, (double)1.0F, (double)0.0F)) && !this.isTargetHere(pos, player)) {
                                 if (this.tryPush(EntityUtil.getEntityPos(player).method_10093(ix.method_10153()).method_10084(), ix)) {
                                    this.timer.reset();
                                    return;
                                 }

                                 if (this.tryPush(EntityUtil.getEntityPos(player).method_10093(ix.method_10153()), ix)) {
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

                           for(class_2350 ixx : class_2350.values()) {
                              if (ixx != class_2350.field_11036 && ixx != class_2350.field_11033) {
                                 class_2338 pos = playerPos.method_10093(ixx);
                                 if (this.isTargetHere(pos, player)) {
                                    if (this.tryPush(playerPos.method_10093(ixx.method_10153()).method_10084(), ixx)) {
                                       this.timer.reset();
                                       return;
                                    }

                                    if (this.tryPush(playerPos.method_10093(ixx.method_10153()), ixx)) {
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
         } else if (this.autoDisable.getValue()) {
            this.disable();
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

               for(class_2350 i : class_2350.values()) {
                  if (this.getBlock(piston.method_10093(i)) == this.getBlockType()) {
                     canPower = true;
                     break;
                  }
               }

               for(class_2350 ix : class_2350.values()) {
                  if (canPower) {
                     break;
                  }

                  if (BlockUtil.canPlace(piston.method_10093(ix), this.placeRange.getValue())) {
                     canPower = true;
                  }
               }

               CombatUtil.modifyPos = null;
               if (canPower) {
                  int pistonSlot = this.findClass(class_2665.class);
                  class_2350 side = BlockUtil.getPlaceSide(piston);
                  if (side != null) {
                     if (this.rotate.getValue()) {
                        Alien.ROTATION.lookAt(piston.method_10093(side), side.method_10153());
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
                        Alien.ROTATION.lookAt(piston.method_10093(side), side.method_10153());
                     }

                     if (this.rotate.getValue()) {
                        Alien.ROTATION.snapBack();
                     }

                     for(class_2350 ix : class_2350.values()) {
                        if (this.getBlock(piston.method_10093(ix)) == this.getBlockType()) {
                           if (this.mine.getValue()) {
                              SpeedMine.INSTANCE.mine(piston.method_10093(ix));
                           }

                           if (this.autoDisable.getValue()) {
                              this.disable();
                           }

                           return true;
                        }
                     }

                     for(class_2350 ixx : class_2350.values()) {
                        if ((ixx != class_2350.field_11036 || !this.torch.getValue()) && BlockUtil.canPlace(piston.method_10093(ixx), this.placeRange.getValue())) {
                           int oldSlot = mc.field_1724.method_31548().field_7545;
                           int powerSlot = this.findBlock(this.getBlockType());
                           this.doSwap(powerSlot);
                           BlockUtil.placeBlock(piston.method_10093(ixx), this.rotate.getValue(), this.powerPacket.getValue());
                           if (this.inventory.getValue()) {
                              this.doSwap(powerSlot);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(oldSlot);
                           }

                           if (this.mine.getValue()) {
                              SpeedMine.INSTANCE.mine(piston.method_10093(ixx));
                           }

                           return true;
                        }
                     }

                     return true;
                  }
               }
            } else {
               class_2350 powerFacing = null;

               for(class_2350 ixxx : class_2350.values()) {
                  if (ixxx != class_2350.field_11036 || !this.torch.getValue()) {
                     if (powerFacing != null) {
                        break;
                     }

                     CombatUtil.modifyPos = piston.method_10093(ixxx);
                     CombatUtil.modifyBlockState = this.getBlockType().method_9564();
                     if (BlockUtil.getPlaceSide(piston) != null) {
                        powerFacing = ixxx;
                     }

                     CombatUtil.modifyPos = null;
                     if (powerFacing != null && !BlockUtil.canPlace(piston.method_10093(powerFacing))) {
                        powerFacing = null;
                     }
                  }
               }

               if (powerFacing != null) {
                  int oldSlotx = mc.field_1724.method_31548().field_7545;
                  int powerSlotx = this.findBlock(this.getBlockType());
                  this.doSwap(powerSlotx);
                  BlockUtil.placeBlock(piston.method_10093(powerFacing), this.rotate.getValue(), this.powerPacket.getValue());
                  if (this.inventory.getValue()) {
                     this.doSwap(powerSlotx);
                     EntityUtil.syncInventory();
                  } else {
                     this.doSwap(oldSlotx);
                  }

                  CombatUtil.modifyPos = piston.method_10093(powerFacing);
                  CombatUtil.modifyBlockState = this.getBlockType().method_9564();
                  int pistonSlot = this.findClass(class_2665.class);
                  class_2350 side = BlockUtil.getPlaceSide(piston);
                  if (side != null) {
                     if (this.rotate.getValue()) {
                        Alien.ROTATION.lookAt(piston.method_10093(side), side.method_10153());
                     }

                     if (this.yawDeceive.getValue()) {
                        pistonFacing(direction.method_10153());
                     }

                     int oldx = mc.field_1724.method_31548().field_7545;
                     this.doSwap(pistonSlot);
                     BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                     if (this.inventory.getValue()) {
                        this.doSwap(pistonSlot);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(oldx);
                     }

                     if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                        Alien.ROTATION.lookAt(piston.method_10093(side), side.method_10153());
                     }

                     if (this.rotate.getValue()) {
                        Alien.ROTATION.snapBack();
                     }
                  }

                  CombatUtil.modifyPos = null;
                  return true;
               }
            }
         }

         class_2680 state = mc.field_1687.method_8320(piston);
         if (state.method_26204() instanceof class_2665 && this.getBlockState(piston).method_11654(class_2318.field_10927) == direction) {
            for(class_2350 ixxxx : class_2350.values()) {
               if (this.getBlock(piston.method_10093(ixxxx)) == this.getBlockType()) {
                  if (this.autoDisable.getValue()) {
                     this.disable();
                     return true;
                  }

                  return false;
               }
            }

            for(class_2350 ixxxxx : class_2350.values()) {
               if ((ixxxxx != class_2350.field_11036 || !this.torch.getValue()) && BlockUtil.canPlace(piston.method_10093(ixxxxx), this.placeRange.getValue())) {
                  int oldSlotxx = mc.field_1724.method_31548().field_7545;
                  int powerSlotxx = this.findBlock(this.getBlockType());
                  this.doSwap(powerSlotxx);
                  BlockUtil.placeBlock(piston.method_10093(ixxxxx), this.rotate.getValue(), this.powerPacket.getValue());
                  if (this.inventory.getValue()) {
                     this.doSwap(powerSlotxx);
                     EntityUtil.syncInventory();
                  } else {
                     this.doSwap(oldSlotxx);
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
      } else if (!this.allowWeb.getValue() && Alien.PLAYER.isInWeb(player)) {
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
                  if (i != class_2350.field_11036 && i != class_2350.field_11033) {
                     class_2338 pos = playerPos.method_10093(i);
                     if (this.isTargetHere(pos, player)) {
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
         }

         if (!mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317(), player.method_23318() + (double)2.5F, player.method_23321())))) {
            for(class_2350 ix : class_2350.values()) {
               if (ix != class_2350.field_11036 && ix != class_2350.field_11033) {
                  class_2338 pos = EntityUtil.getEntityPos(player).method_10093(ix);
                  class_238 box = player.method_5829().method_997(new class_243((double)ix.method_10148(), (double)ix.method_10164(), (double)ix.method_10165()));
                  if (this.getBlock(pos.method_10084()) != class_2246.field_10379 && !mc.field_1687.method_39454(player, box.method_989((double)0.0F, (double)1.0F, (double)0.0F)) && !this.isTargetHere(pos, player) && mc.field_1687.method_39454(player, new class_238(new BlockPosX(player.method_23317(), player.method_23318() + (double)0.5F, player.method_23321())))) {
                     return true;
                  }
               }
            }
         }

         return (double)progress > this.surroundCheck.getValue() - (double)1.0F || Alien.HOLE.isHard((class_2338)(new BlockPosX(player.method_23317(), player.method_23318() + (double)0.5F, player.method_23321())));
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
