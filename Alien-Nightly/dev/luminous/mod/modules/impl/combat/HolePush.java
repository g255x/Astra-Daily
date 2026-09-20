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
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
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

public class HolePush extends Module {
   public static HolePush INSTANCE;
   private static final float[] OFFSETS = new float[]{-0.25F, 0.0F, 0.25F};
   private static final class_2350[] HORIZONTAL_DIRECTIONS;
   private final EnumSetting<Page> page;
   private final BooleanSetting torch;
   private final BooleanSetting rotate;
   private final BooleanSetting yawDeceive;
   private final BooleanSetting allowWeb;
   public final BindSetting allowKey;
   private final BooleanSetting noEating;
   private final BooleanSetting selfGround;
   private final BooleanSetting onlyGround;
   private final BooleanSetting onlyNoInside;
   private final BooleanSetting onlyInside;
   private final SliderSetting surroundCheck;
   private final BooleanSetting syncCrystal;
   private final BooleanSetting pistonPacket;
   private final BooleanSetting powerPacket;
   private final BooleanSetting mine;
   private final BooleanSetting pauseCity;
   private final SliderSetting updateDelay;
   private final BooleanSetting autoDisable;
   private final SliderSetting range;
   private final SliderSetting placeRange;
   private final BooleanSetting inventory;
   private final Timer timer;

   public HolePush() {
      super("HolePush", Module.Category.Combat);
      this.page = this.add(new EnumSetting("Page", HolePush.Page.General));
      this.torch = this.add(new BooleanSetting("Torch", false, () -> this.page.is(HolePush.Page.General)));
      this.rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.is(HolePush.Page.Rotate)));
      this.yawDeceive = this.add(new BooleanSetting("YawDeceive", true, () -> this.page.is(HolePush.Page.Rotate)));
      this.allowWeb = this.add(new BooleanSetting("AllowWeb", true, () -> this.page.is(HolePush.Page.Check)));
      this.allowKey = this.add(new BindSetting("AllowKey", -1, () -> !this.allowWeb.getValue() && this.page.is(HolePush.Page.Check)));
      this.noEating = this.add(new BooleanSetting("EatingPause", true, () -> this.page.is(HolePush.Page.Check)));
      this.selfGround = this.add(new BooleanSetting("SelfGround", true, () -> this.page.is(HolePush.Page.Check)));
      this.onlyGround = this.add(new BooleanSetting("OnlyGround", false, () -> this.page.is(HolePush.Page.Check)));
      this.onlyNoInside = this.add(new BooleanSetting("OnlyNoInside", false, () -> this.page.is(HolePush.Page.Check)));
      this.onlyInside = this.add(new BooleanSetting("OnlyInside", true, () -> this.page.is(HolePush.Page.Check)));
      this.surroundCheck = this.add(new SliderSetting("SurroundCheck", 0, 0, 4, () -> this.page.is(HolePush.Page.Check)));
      this.syncCrystal = this.add(new BooleanSetting("SyncCrystal", true, () -> this.page.is(HolePush.Page.Check)));
      this.pistonPacket = this.add(new BooleanSetting("PistonPacket", true, () -> this.page.is(HolePush.Page.General)));
      this.powerPacket = this.add(new BooleanSetting("PowerPacket", true, () -> this.page.is(HolePush.Page.General)));
      this.mine = this.add(new BooleanSetting("Mine", true, () -> this.page.is(HolePush.Page.General)));
      this.pauseCity = this.add(new BooleanSetting("PauseCity", true, () -> this.mine.isOpen() && this.page.is(HolePush.Page.General)));
      this.updateDelay = this.add(new SliderSetting("Delay", 200, 0, 1000, () -> this.page.is(HolePush.Page.General)));
      this.autoDisable = this.add(new BooleanSetting("AutoDisable", true, () -> this.page.is(HolePush.Page.General)));
      this.range = this.add(new SliderSetting("Range", 4.7, (double)0.0F, (double)6.0F, () -> this.page.is(HolePush.Page.General)));
      this.placeRange = this.add(new SliderSetting("PlaceRange", 4.7, (double)0.0F, (double)6.0F, () -> this.page.is(HolePush.Page.General)));
      this.inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(HolePush.Page.General)));
      this.timer = new Timer();
      this.setChinese("坑推");
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

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.timer.passedMs(this.updateDelay.getValue())) {
         if (!this.selfGround.getValue() || mc.field_1724.method_24828()) {
            class_2248 blockType = this.getBlockType();
            int powerSlot = this.findBlock(blockType);
            int pistonSlot = this.findClass(class_2665.class);
            if (powerSlot != -1 && pistonSlot != -1) {
               if (!this.syncCrystal.getValue() || AutoCrystal.INSTANCE == null || AutoCrystal.INSTANCE.crystalPos == null) {
                  if (!this.noEating.getValue() || !mc.field_1724.method_6115()) {
                     if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                        for(class_1657 player : CombatUtil.getEnemies(this.range.getValue())) {
                           if (this.canPush(player)) {
                              class_2338 basePos = EntityUtil.getEntityPos(player);

                              for(class_2350 i : HORIZONTAL_DIRECTIONS) {
                                 class_2338 pos2;
                                 if (this.isTargetHere(pos2 = basePos.method_10093(i), player) && mc.field_1687.method_39454(player, new class_238(pos2))) {
                                    if (this.tryPush(basePos.method_10093(i.method_10153()), i)) {
                                       this.timer.reset();
                                       return;
                                    }

                                    if (this.tryPush(basePos.method_10093(i.method_10153()).method_10084(), i)) {
                                       this.timer.reset();
                                       return;
                                    }
                                 }
                              }

                              for(float x : OFFSETS) {
                                 for(float z : OFFSETS) {
                                    BlockPosX playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + (double)0.5F, player.method_23321() + (double)z);

                                    for(class_2350 i : HORIZONTAL_DIRECTIONS) {
                                       class_2338 pos;
                                       if (this.isTargetHere(pos = playerPos.method_10093(i), player) && mc.field_1687.method_39454(player, new class_238(pos))) {
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
                                 for(class_2350 i : HORIZONTAL_DIRECTIONS) {
                                    class_2338 pos3 = basePos.method_10093(i);
                                    class_238 box = player.method_5829().method_997(new class_243((double)i.method_10148(), (double)i.method_10164(), (double)i.method_10165()));
                                    if (this.getBlock(pos3.method_10084()) != class_2246.field_10379 && !mc.field_1687.method_39454(player, box.method_989((double)0.0F, (double)1.0F, (double)0.0F)) && !this.isTargetHere(pos3, player)) {
                                       if (this.tryPush(basePos.method_10093(i.method_10153()).method_10084(), i)) {
                                          this.timer.reset();
                                          return;
                                       }

                                       if (this.tryPush(basePos.method_10093(i.method_10153()), i)) {
                                          this.timer.reset();
                                          return;
                                       }
                                    }
                                 }
                              }

                              for(float x : OFFSETS) {
                                 for(float z : OFFSETS) {
                                    BlockPosX playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + (double)0.5F, player.method_23321() + (double)z);

                                    for(class_2350 i : HORIZONTAL_DIRECTIONS) {
                                       if (this.isTargetHere(playerPos.method_10093(i), player)) {
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
      class_2248 blockType = this.getBlockType();
      if (!mc.field_1687.method_22347(piston.method_10093(direction))) {
         return false;
      } else {
         if (this.isTrueFacing(piston, direction) && this.facingCheck(piston) && BlockUtil.clientCanPlace(piston, false)) {
            boolean canPower = false;
            if (BlockUtil.getPlaceSide(piston, this.placeRange.getValue()) != null) {
               CombatUtil.modifyPos = piston;
               CombatUtil.modifyBlockState = class_2246.field_10560.method_9564();

               for(class_2350 direction2 : class_2350.values()) {
                  if (this.getBlock(piston.method_10093(direction2)) == blockType) {
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
                  class_2350 side = BlockUtil.getPlaceSide(piston, this.placeRange.getValue());
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

                     for(class_2350 i : class_2350.values()) {
                        if (this.getBlock(piston.method_10093(i)) == blockType) {
                           if (this.shouldMine()) {
                              SpeedMine.INSTANCE.mine(piston.method_10093(i));
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
                           int powerSlot = this.findBlock(blockType);
                           this.doSwap(powerSlot);
                           BlockUtil.placeBlock(piston.method_10093(i), this.rotate.getValue(), this.powerPacket.getValue());
                           if (this.inventory.getValue()) {
                              this.doSwap(powerSlot);
                              EntityUtil.syncInventory();
                           } else {
                              this.doSwap(oldSlot);
                           }

                           if (this.shouldMine()) {
                              SpeedMine.INSTANCE.mine(piston.method_10093(i));
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
                     CombatUtil.modifyBlockState = blockType.method_9564();
                     if (BlockUtil.getPlaceSide(piston, this.placeRange.getValue()) != null) {
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
                  int powerSlot = this.findBlock(blockType);
                  this.doSwap(powerSlot);
                  BlockUtil.placeBlock(piston.method_10093(powerFacing), this.rotate.getValue(), this.powerPacket.getValue());
                  if (this.inventory.getValue()) {
                     this.doSwap(powerSlot);
                     EntityUtil.syncInventory();
                  } else {
                     this.doSwap(oldSlot);
                  }

                  CombatUtil.modifyPos = piston.method_10093(powerFacing);
                  CombatUtil.modifyBlockState = blockType.method_9564();
                  int pistonSlot = this.findClass(class_2665.class);
                  class_2350 side2 = BlockUtil.getPlaceSide(piston, this.placeRange.getValue());
                  if (side2 != null) {
                     if (this.rotate.getValue()) {
                        Alien.ROTATION.lookAt(piston.method_10093(side2), side2.method_10153());
                     }

                     if (this.yawDeceive.getValue()) {
                        pistonFacing(direction.method_10153());
                     }

                     int old2 = mc.field_1724.method_31548().field_7545;
                     this.doSwap(pistonSlot);
                     BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                     if (this.inventory.getValue()) {
                        this.doSwap(pistonSlot);
                        EntityUtil.syncInventory();
                     } else {
                        this.doSwap(old2);
                     }

                     if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                        Alien.ROTATION.lookAt(piston.method_10093(side2), side2.method_10153());
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

         if (mc.field_1687.method_8320(piston).method_26204() instanceof class_2665 && this.getBlockState(piston).method_11654(class_2318.field_10927) == direction) {
            for(class_2350 direction4 : class_2350.values()) {
               if (this.getBlock(piston.method_10093(direction4)) == blockType) {
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
                  int powerSlot = this.findBlock(blockType);
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

   public String getInfo() {
      if (this.syncCrystal.getValue() && AutoCrystal.INSTANCE != null && AutoCrystal.INSTANCE.crystalPos != null) {
         return "WaitSync";
      } else {
         return !this.allowWeb.getValue() && !this.allowKey.isPressed() ? "SmartPush" : "CrazyDog";
      }
   }

   private boolean shouldMine() {
      if (!this.mine.getValue()) {
         return false;
      } else if (!this.pauseCity.getValue()) {
         return true;
      } else {
         class_2338 breakPos = SpeedMine.getBreakPos();
         return breakPos != null && breakPos.equals(SpeedMine.secondPos);
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

   private boolean burrowUpdate(class_1657 player) {
      for(float x : new float[]{0.0F, 0.3F, -0.3F}) {
         for(float z : new float[]{0.0F, 0.3F, -0.3F}) {
            BlockPosX pos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + (double)1.5F, player.method_23321() + (double)z);
            class_2248 block = mc.field_1687.method_8320(pos).method_26204();
            if ((new class_238(pos)).method_994(player.method_5829()) && (block == class_2246.field_10540 || block == class_2246.field_23152 || block == class_2246.field_9987 || block == class_2246.field_10443)) {
               return true;
            }
         }
      }

      return false;
   }

   private Boolean canPush(class_1657 player) {
      if (this.onlyGround.getValue() && !player.method_24828()) {
         return false;
      } else if (!this.allowWeb.getValue() && Alien.PLAYER.isInWeb(player) && !this.allowKey.isPressed()) {
         return false;
      } else if (this.onlyNoInside.getValue() && this.burrowUpdate(player)) {
         return false;
      } else if (this.onlyInside.getValue() && !this.burrowUpdate(player)) {
         return false;
      } else {
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

         class_238 baseBox = new class_238(new BlockPosX(player.method_23317(), player.method_23318() + (double)0.5F, player.method_23321()));

         for(float x : OFFSETS) {
            for(float z : OFFSETS) {
               BlockPosX playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + (double)0.5F, player.method_23321() + (double)z);

               for(class_2350 i : HORIZONTAL_DIRECTIONS) {
                  class_2338 pos;
                  if (this.isTargetHere(pos = playerPos.method_10093(i), player)) {
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
            class_2338 playerBasePos = EntityUtil.getEntityPos(player);

            for(class_2350 i : HORIZONTAL_DIRECTIONS) {
               class_2338 pos = playerBasePos.method_10093(i);
               class_238 box = player.method_5829().method_997(new class_243((double)i.method_10148(), (double)i.method_10164(), (double)i.method_10165()));
               if (this.getBlock(pos.method_10084()) != class_2246.field_10379 && !mc.field_1687.method_39454(player, box.method_989((double)0.0F, (double)1.0F, (double)0.0F)) && !this.isTargetHere(pos, player) && mc.field_1687.method_39454(player, baseBox)) {
                  return true;
               }
            }
         }

         return (double)progress > this.surroundCheck.getValue() - (double)1.0F || Alien.HOLE.isHard((class_2338)(new BlockPosX(player.method_23317(), player.method_23318() + (double)0.5F, player.method_23321())));
      }
   }

   private boolean isTargetHere(class_2338 pos, class_1297 target) {
      return (new class_238(pos)).method_994(target.method_5829());
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

   static {
      HORIZONTAL_DIRECTIONS = new class_2350[]{class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034};
   }

   public static enum Page {
      General,
      Rotate,
      Check;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{General, Rotate, Check};
      }
   }
}
