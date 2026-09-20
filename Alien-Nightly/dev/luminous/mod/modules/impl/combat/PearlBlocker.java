package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.EntitySpawnedEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1684;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;

public class PearlBlocker extends Module {
   public static PearlBlocker INSTANCE;
   private final BooleanSetting ignoreFriends = this.add(new BooleanSetting("IgnoreFriends", true));
   private final SliderSetting range = this.add(new SliderSetting("Range", (double)10.0F, (double)1.0F, (double)30.0F));
   private final BooleanSetting obsidianOnly = this.add(new BooleanSetting("ObsidianOnly", true));
   private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true));
   private final BooleanSetting predictive = this.add(new BooleanSetting("Predictive", true));
   private final BooleanSetting debug = this.add(new BooleanSetting("Debug", false));
   private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
   private final BooleanSetting silentRotation = this.add(new BooleanSetting("SilentRotation", false));
   private final EnumSetting wallSize;
   private final SliderSetting placeCooldown;
   private final SliderSetting rotateCooldown;
   private final Map<String, Long> lastPlaceTime;
   private long lastRotateTime;
   private float[] lastRotation;

   public PearlBlocker() {
      super("PearlBlocker", Module.Category.Combat);
      this.wallSize = this.add(new EnumSetting("WallSize", PearlBlocker.WallSize.SMALL));
      this.placeCooldown = this.add(new SliderSetting("PlaceCooldown", (double)80.0F, (double)50.0F, (double)1000.0F));
      this.rotateCooldown = this.add(new SliderSetting("RotateCooldown", (double)60.0F, (double)30.0F, (double)1000.0F));
      this.lastPlaceTime = new HashMap();
      this.lastRotateTime = 0L;
      this.lastRotation = null;
      this.setChinese("珍珠阻挡");
      INSTANCE = this;
   }

   public void onDisable() {
      this.lastPlaceTime.clear();
      this.lastRotateTime = 0L;
      this.lastRotation = null;
   }

   @EventListener
   public void onEntitySpawn(EntitySpawnedEvent event) {
      if (!nullCheck()) {
         class_1297 e = event.getEntity();
         if (e instanceof class_1684) {
            class_1684 pearl = (class_1684)e;
            class_1297 owner = pearl.method_24921();
            if (owner instanceof class_1657) {
               class_1657 player = (class_1657)owner;
               if (player != mc.field_1724) {
                  if (this.ignoreFriends.getValue()) {
                     try {
                        if (Alien.FRIEND != null && Alien.FRIEND.getClass().getMethod("contains", String.class).invoke(Alien.FRIEND, player.method_5477().getString()).equals(Boolean.TRUE)) {
                           return;
                        }
                     } catch (Exception var11) {
                     }
                  }

                  double maxRange = this.range.getValue();
                  if (!(mc.field_1724.method_5858(pearl) > maxRange * maxRange)) {
                     String name = player.method_5477().getString();
                     long now = System.currentTimeMillis();
                     if (!((double)(now - (Long)this.lastPlaceTime.getOrDefault(name, 0L)) < this.placeCooldown.getValue())) {
                        if (this.debug.getValue()) {
                           this.sendMessage("Blocking pearl in flight from: " + name);
                        }

                        if (this.blockPearlPath(pearl)) {
                           this.lastPlaceTime.put(name, now);
                        }

                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (!nullCheck() && this.predictive.getValue()) {
         long now = System.currentTimeMillis();
         double maxRange = this.range.getValue();
         Iterator var6 = mc.field_1687.method_18456().iterator();

         while(true) {
            class_1657 player;
            while(true) {
               if (!var6.hasNext()) {
                  return;
               }

               player = (class_1657)var6.next();
               if (player != mc.field_1724 && !(mc.field_1724.method_5858(player) > maxRange * maxRange)) {
                  if (!this.ignoreFriends.getValue()) {
                     break;
                  }

                  try {
                     if (Alien.FRIEND == null || !Alien.FRIEND.getClass().getMethod("contains", String.class).invoke(Alien.FRIEND, player.method_5477().getString()).equals(Boolean.TRUE)) {
                        break;
                     }
                  } catch (Exception var9) {
                     break;
                  }
               }
            }

            if (this.isHoldingPearl(player) && this.isLikelyToThrow(player)) {
               String name = player.method_5477().getString();
               if (!((double)(now - (Long)this.lastPlaceTime.getOrDefault(name, 0L)) < this.placeCooldown.getValue())) {
                  if (this.debug.getValue()) {
                     this.sendMessage("Predictive block: " + name);
                  }

                  if (this.blockPredictive(player)) {
                     this.lastPlaceTime.put(name, now);
                  }
               }
            }
         }
      }
   }

   private boolean isHoldingPearl(class_1657 player) {
      return player.method_6047().method_7909() == class_1802.field_8634 || player.method_6079().method_7909() == class_1802.field_8634;
   }

   private boolean isLikelyToThrow(class_1657 player) {
      return player.method_36455() < -30.0F || !player.method_24828() || player.method_18798().method_1027() > 0.2;
   }

   private boolean blockPredictive(class_1657 player) {
      boolean placed = false;
      class_243 pos = player.method_19538();
      class_243 look = player.method_5720().method_1029();
      int radius = this.isWallBig() ? 2 : 1;

      for(int d = 2; d <= 4; ++d) {
         class_243 centerVec = pos.method_1019(look.method_1021((double)d));
         class_2338 center = class_2338.method_49638(centerVec);
         if (this.placeWallAt(center, radius)) {
            placed = true;
         }

         if (placed) {
            break;
         }
      }

      return placed;
   }

   private boolean blockPearlPath(class_1684 pearl) {
      class_243 vel = pearl.method_18798();
      if (vel.method_1027() < 0.01) {
         return false;
      } else {
         class_243 dir = vel.method_1029();
         class_243 pos = pearl.method_19538();
         int radius = this.isWallBig() ? 2 : 1;

         for(int d = 3; d <= 7; ++d) {
            class_243 future = pos.method_1019(dir.method_1021((double)d));
            class_2338 center = class_2338.method_49638(future);
            if (this.placeWallAt(center, radius)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean placeWallAt(class_2338 center, int radius) {
      boolean any = false;

      for(int x = -radius; x <= radius; ++x) {
         for(int z = -radius; z <= radius; ++z) {
            for(int y = 0; y <= 1; ++y) {
               class_2338 target = center.method_10069(x, y, z);
               if (this.placeBlockSmart(target)) {
                  any = true;
               }
            }
         }
      }

      return any;
   }

   private boolean isWallBig() {
      Object v = this.wallSize.getValue();
      if (v == null) {
         return false;
      } else {
         String s = v.toString().toLowerCase();
         return s.equals("big") || s.contains("5x5") || s.equals("large");
      }
   }

   private boolean isWallSmall() {
      Object v = this.wallSize.getValue();
      if (v == null) {
         return false;
      } else {
         String s = v.toString().toLowerCase();
         return s.equals("Small") || s.contains("3x3") || s.equals("Smaller");
      }
   }

   private boolean placeBlockSmart(class_2338 pos) {
      if (mc.field_1687 != null && mc.field_1724 != null) {
         class_2338 me = mc.field_1724.method_24515();
         if (!me.equals(pos) && !me.method_10084().equals(pos)) {
            if (!BlockUtil.canReplace(pos)) {
               return false;
            } else {
               int slot;
               if (this.obsidianOnly.getValue()) {
                  slot = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
                  if (slot == -1) {
                     if (this.debug.getValue()) {
                        this.sendMessage("No obsidian found for PearlBlocker");
                     }

                     return false;
                  }
               } else {
                  slot = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
                  if (slot == -1) {
                     slot = InventoryUtil.findItem(class_1802.field_8634);
                     if (slot == -1) {
                        if (this.debug.getValue()) {
                           this.sendMessage("No suitable block found for PearlBlocker");
                        }

                        return false;
                     }
                  }
               }

               int oldSlot = mc.field_1724.method_31548().field_7545;
               if (this.inventorySwap.getValue()) {
                  InventoryUtil.inventorySwap(slot, oldSlot);
               } else {
                  InventoryUtil.switchToSlot(slot);
               }

               if (this.rotate.getValue()) {
                  class_243 center = class_243.method_24953(pos);
                  float[] rotations = RotationManager.getRotation(center);
                  long now = System.currentTimeMillis();
                  if (this.silentRotation.getValue()) {
                     try {
                        if (this.lastRotation == null || Math.abs(rotations[0] - this.lastRotation[0]) > 2.0F || Math.abs(rotations[1] - this.lastRotation[1]) > 2.0F || (double)(now - this.lastRotateTime) > this.rotateCooldown.getValue()) {
                           Alien.ROTATION.snapAt(rotations[0], rotations[1]);
                           this.lastRotateTime = now;
                           this.lastRotation = rotations;
                        }
                     } catch (Exception var12) {
                        Alien.ROTATION.lookAt(center);
                     }
                  } else {
                     Alien.ROTATION.lookAt(center);
                  }
               }

               try {
                  BlockUtil.placeBlock(pos, this.rotate.getValue());
               } catch (Exception var11) {
                  if (this.inventorySwap.getValue()) {
                     InventoryUtil.inventorySwap(slot, oldSlot);
                  } else {
                     InventoryUtil.switchToSlot(oldSlot);
                  }

                  return false;
               }

               if (this.inventorySwap.getValue()) {
                  InventoryUtil.inventorySwap(slot, oldSlot);

                  try {
                     Thread.sleep(1L);
                  } catch (InterruptedException var10) {
                  }
               } else {
                  InventoryUtil.switchToSlot(oldSlot);
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean nullCheck() {
      return mc.field_1724 == null || mc.field_1687 == null;
   }

   private static enum WallSize {
      SMALL,
      BIG;

      // $FF: synthetic method
      private static WallSize[] $values() {
         return new WallSize[]{SMALL, BIG};
      }
   }
}
