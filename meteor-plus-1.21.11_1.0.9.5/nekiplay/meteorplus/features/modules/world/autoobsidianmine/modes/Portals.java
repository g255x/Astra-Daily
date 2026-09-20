package nekiplay.meteorplus.features.modules.world.autoobsidianmine.modes;

import baritone.api.BaritoneAPI;
import baritone.api.command.manager.ICommandManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.player.AutoEat;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.Pool;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.meteorclient.utils.world.Dimension;
import nekiplay.meteorplus.features.modules.world.autoobsidianmine.AutoObsidianFarmMode;
import nekiplay.meteorplus.features.modules.world.autoobsidianmine.AutoObsidianFarmModes;
import nekiplay.meteorplus.utils.BlockHelper;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2680;

public class Portals extends AutoObsidianFarmMode {
   private final Shape shape;
   private final Mode mode;
   private final Double range;
   private final Integer maxBlocksPerTick;
   private final SortMode sortMode;
   private final Pool<class_2338.class_2339> blockPosPool;
   private final List<class_2338.class_2339> blocks;
   private boolean firstBlock;
   private final class_2338.class_2339 lastBlockPos;
   private int timer;
   private int noBlockTimer;
   private final class_2338.class_2339 pos1;
   private final class_2338.class_2339 pos2;
   private class_238 box;
   private int maxh;
   private int maxv;
   private boolean baritoneBreakSaved;
   private boolean baritonePlaceSaved;
   private int commandDelay;
   private boolean isMine;

   public Portals() {
      super(AutoObsidianFarmModes.Portals_Vanila);
      this.shape = Portals.Shape.Cube;
      this.mode = Portals.Mode.All;
      this.range = (double)5.5F;
      this.maxBlocksPerTick = 1;
      this.sortMode = Portals.SortMode.Closest;
      this.blockPosPool = new Pool(class_2338.class_2339::new);
      this.blocks = new ArrayList();
      this.lastBlockPos = new class_2338.class_2339();
      this.pos1 = new class_2338.class_2339();
      this.pos2 = new class_2338.class_2339();
      this.maxh = 0;
      this.maxv = 0;
      this.baritoneBreakSaved = false;
      this.baritonePlaceSaved = false;
      this.commandDelay = 0;
      this.isMine = false;
   }

   public void onActivate() {
      this.commandDelay = (Integer)this.settings.delayCommand.get();
      this.firstBlock = true;
      this.timer = 0;
      this.noBlockTimer = 0;
      if ((Boolean)this.settings.noBaritoneBreaking.get()) {
         this.baritoneBreakSaved = (Boolean)BaritoneAPI.getSettings().allowBreak.value;
      }

      if ((Boolean)this.settings.noBaritonePlacing.get()) {
         this.baritonePlaceSaved = (Boolean)BaritoneAPI.getSettings().allowPlace.value;
      }

   }

   public void onDeactivate() {
      if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().hasPath() || BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) {
         BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("stop");
      }

      if ((Boolean)this.settings.noBaritoneBreaking.get()) {
         BaritoneAPI.getSettings().allowBreak.value = this.baritoneBreakSaved;
      }

      if ((Boolean)this.settings.noBaritonePlacing.get()) {
         BaritoneAPI.getSettings().allowPlace.value = this.baritonePlaceSaved;
      }

   }

   public void onTickEventPre(TickEvent.Pre event) {
      if (this.commandDelay <= (Integer)this.settings.delayCommand.get()) {
         ++this.commandDelay;
      }

      if (this.mc.field_1724 != null && this.mc.field_1687 != null) {
         if (!this.mc.field_1724.method_6115() && (!((AutoEat)Modules.get().get(AutoEat.class)).isActive() || !((AutoEat)Modules.get().get(AutoEat.class)).eating) || !(Boolean)this.settings.pauseOnEat.get()) {
            if (this.settings.workingMode.get() == AutoObsidianFarmModes.Portal_Homes) {
               if (PlayerUtils.getDimension() == Dimension.Overworld) {
                  this.commandDelay = 0;
                  this.isMine = false;
                  if (!BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().hasPath() && !BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) {
                     BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("goto nether_portal");
                  }
               } else if (PlayerUtils.getDimension() == Dimension.Nether) {
                  List<class_2338> obsidians = this.getPortalBlocks();
                  this.isMine = true;
                  if ((obsidians.isEmpty() || this.blocks.isEmpty()) && this.mc.field_1724 != null && this.commandDelay >= (Integer)this.settings.delayCommand.get()) {
                     ChatUtils.sendPlayerMsg((String)this.settings.command.get());
                     this.commandDelay = 0;
                  }
               }
            } else if (this.settings.workingMode.get() == AutoObsidianFarmModes.Portals_Vanila) {
               if (PlayerUtils.getDimension() == Dimension.Overworld) {
                  class_2338 to = (class_2338)this.settings.twoPortalPosition.get();
                  class_2338 dis = BlockHelper.opposite(to, Dimension.Nether);
                  double distance = Math.sqrt(PlayerUtils.squaredDistanceTo((double)dis.method_10263(), this.mc.field_1724.method_23318(), (double)dis.method_10260()));
                  if (distance <= (double)20.0F) {
                     List<class_2338> obsidians = this.getPortalBlocks();
                     class_2248 down = this.mc.field_1687.method_8320(this.mc.field_1724.method_24515().method_10069(0, -1, 0)).method_26204();
                     if (obsidians.isEmpty()) {
                        this.isMine = false;
                     } else if (down == class_2246.field_10540) {
                        this.isMine = true;
                     }
                  } else {
                     this.isMine = false;
                  }

                  if (!this.isMine) {
                     if ((Boolean)this.settings.noBaritoneBreaking.get()) {
                        BaritoneAPI.getSettings().allowBreak.value = false;
                     }

                     if (!BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().hasPath() && !BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) {
                        to = (class_2338)this.settings.mainPortalPosition.get();
                        ICommandManager var10000 = BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager();
                        int var10001 = to.method_10263();
                        var10000.execute("goto " + var10001 + " " + to.method_10264() + " " + to.method_10260());
                     }
                  } else if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().hasPath() || BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing() || BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getPath().isPresent()) {
                     BaritoneAPI.getSettings().allowBreak.value = true;
                     BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("stop");
                  }
               } else if (PlayerUtils.getDimension() == Dimension.Nether) {
                  this.isMine = false;
                  if ((Boolean)this.settings.noBaritoneBreaking.get()) {
                     BaritoneAPI.getSettings().allowBreak.value = false;
                  }

                  if (!BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().hasPath() && !BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) {
                     class_2338 to = (class_2338)this.settings.twoPortalPosition.get();
                     ICommandManager var34 = BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager();
                     int var35 = to.method_10263();
                     var34.execute("goto " + var35 + " " + to.method_10264() + " " + to.method_10260());
                  }
               }
            }

            if (this.timer > 0) {
               --this.timer;
            } else {
               assert this.mc.field_1724 != null;

               double pX = this.mc.field_1724.method_23317();
               double pY = this.mc.field_1724.method_23318();
               double pZ = this.mc.field_1724.method_23321();
               double rangeSq = Math.pow(this.range, (double)2.0F);
               if (this.shape == Portals.Shape.UniformCube) {
                  Math.round(this.range);
               }

               int r = (int)Math.round(this.range);
               if (this.shape == Portals.Shape.UniformCube) {
                  double pX_ = pX + (double)1.0F;
                  this.pos1.method_10102(pX_ - (double)r, pY - (double)r + (double)1.0F, pZ - (double)r + (double)1.0F);
                  this.pos2.method_10102(pX_ + (double)r - (double)1.0F, pY + (double)r, pZ + (double)r);
               } else {
                  int direction = Math.round(this.mc.field_1724.method_5802().field_1342 % 360.0F / 90.0F);
                  direction = direction != 4 && direction != -4 ? direction : 0;
                  direction = direction == -2 ? 2 : (direction == -1 ? 3 : (direction == -3 ? 1 : direction));
                  int range_down = 0;
                  int range_right = 2;
                  int range_forward = 2;
                  this.pos1.method_10102(pX - (double)range_forward, Math.ceil(pY) - (double)range_down, pZ - (double)range_right);
                  int range_up = 6;
                  int range_back = 2;
                  int range_left = 2;
                  this.pos2.method_10102(pX + (double)range_back + (double)1.0F, Math.ceil(pY + (double)range_up + (double)1.0F), pZ + (double)range_left + (double)1.0F);
                  if (direction == 2) {
                     double var28 = pX + (double)1.0F;
                     double pZ_ = pZ + (double)1.0F;
                     this.pos1.method_10102(var28 - (double)(range_left + 1), Math.ceil(pY) - (double)range_down, pZ_ - (double)(range_forward + 1));
                     this.pos2.method_10102(var28 + (double)range_right, Math.ceil(pY + (double)range_up + (double)1.0F), pZ_ + (double)range_back);
                  } else if (direction == 3) {
                     double var29 = pX + (double)1.0F;
                     this.pos1.method_10102(var29 - (double)(range_back + 1), Math.ceil(pY) - (double)range_down, pZ - (double)range_left);
                     this.pos2.method_10102(var29 + (double)range_forward, Math.ceil(pY + (double)range_up + (double)1.0F), pZ + (double)range_right + (double)1.0F);
                  } else if (direction == 0) {
                     double var31 = pZ + (double)1.0F;
                     double var30 = pX + (double)1.0F;
                     this.pos1.method_10102(var30 - (double)(range_right + 1), Math.ceil(pY) - (double)range_down, var31 - (double)(range_back + 1));
                     this.pos2.method_10102(var30 + (double)range_left, Math.ceil(pY + (double)range_up + (double)1.0F), var31 + (double)range_forward);
                  }

                  this.maxh = 1 + Math.max(Math.max(Math.max(range_back, range_right), range_forward), range_left);
                  this.maxv = 1 + Math.max(range_up, range_down);
               }

               if (this.mode == Portals.Mode.Flatten) {
                  this.pos1.method_33098((int)Math.floor(pY));
               }

               this.box = new class_238(this.pos1.method_46558(), this.pos2.method_46558());
               BlockIterator.register(Math.max((int)Math.ceil(this.range + (double)1.0F), this.maxh), Math.max((int)Math.ceil(this.range), this.maxv), (blockPos, blockState) -> {
                  boolean toofarSphere = Utils.squaredDistance(pX, pY, pZ, (double)blockPos.method_10263() + (double)0.5F, (double)blockPos.method_10264() + (double)0.5F, (double)blockPos.method_10260() + (double)0.5F) > rangeSq;
                  boolean toofarUniformCube = maxDist(Math.floor(pX), Math.floor(pY), Math.floor(pZ), (double)blockPos.method_10263(), (double)blockPos.method_10264(), (double)blockPos.method_10260()) >= this.range;
                  boolean toofarCube = !this.box.method_1006(class_243.method_24953(blockPos));
                  if (BlockUtils.canBreak(blockPos, blockState) && (!toofarSphere || this.shape != Portals.Shape.Sphere) && (!toofarUniformCube || this.shape != Portals.Shape.UniformCube) && (!toofarCube || this.shape != Portals.Shape.Cube)) {
                     if (blockState.method_26204() == class_2246.field_10540 || blockState.method_26204() == class_2246.field_10036) {
                        this.blocks.add(((class_2338.class_2339)this.blockPosPool.get()).method_10101(blockPos));
                     }

                  }
               });
               BlockIterator.after(() -> {
                  if (this.isMine) {
                     if (this.sortMode != Portals.SortMode.None) {
                        if (this.sortMode != Portals.SortMode.Closest && this.sortMode != Portals.SortMode.Furthest) {
                           if (this.sortMode == Portals.SortMode.TopDown) {
                              this.blocks.sort(Comparator.comparingDouble((value) -> (double)(-1 * value.method_10264())));
                           }
                        } else {
                           this.blocks.sort(Comparator.comparingDouble((value) -> Utils.squaredDistance(pX, pY, pZ, (double)value.method_10263() + (double)0.5F, (double)value.method_10264() + (double)0.5F, (double)value.method_10260() + (double)0.5F) * (double)(this.sortMode == Portals.SortMode.Closest ? 1 : -1)));
                        }
                     }

                     if (this.blocks.isEmpty()) {
                        if (this.noBlockTimer++ >= (Integer)this.settings.delay.get()) {
                           this.firstBlock = true;
                        }

                        return;
                     }

                     this.noBlockTimer = 0;
                     if (!this.firstBlock && !this.lastBlockPos.equals(this.blocks.get(0))) {
                        this.timer = (Integer)this.settings.delay.get();
                        this.firstBlock = false;
                        this.lastBlockPos.method_10101((class_2382)this.blocks.get(0));
                        if (this.timer > 0) {
                           return;
                        }
                     }

                     int count = 0;

                     for(class_2338 block : this.blocks) {
                        if (count >= this.maxBlocksPerTick) {
                           break;
                        }

                        if ((Boolean)this.settings.rotate.get()) {
                           this.rotate(block);
                        }

                        boolean canInstaMine = BlockUtils.canInstaBreak(block);
                        BlockUtils.breakBlock(block, (Boolean)this.settings.swingHand.get());
                        this.lastBlockPos.method_10101(block);
                        ++count;
                        if (!canInstaMine) {
                           break;
                        }
                     }

                     this.firstBlock = false;

                     for(class_2338.class_2339 blockPos : this.blocks) {
                        this.blockPosPool.free(blockPos);
                     }

                     this.blocks.clear();
                  }

               });
            }
         }
      }
   }

   private void rotate(class_2338 target) {
      Rotations.rotate(Rotations.getYaw(target), Rotations.getPitch(target), (Runnable)null);
   }

   private List<class_2338> getPortalBlocks() {
      List<class_2338> temp = new ArrayList();

      for(int i = 0; i < 6; ++i) {
         for(int i2 = -4; i2 < 4; ++i2) {
            for(int i3 = -4; i3 < 4; ++i3) {
               assert this.mc.field_1724 != null;

               assert this.mc.field_1687 != null;

               class_2338 pos = this.mc.field_1724.method_24515().method_10069(i2, i, i3);
               class_2680 state = this.mc.field_1687.method_8320(pos);
               if (state.method_26204() == class_2246.field_10540) {
                  temp.add(pos);
               }
            }
         }
      }

      return temp;
   }

   public static double maxDist(double x1, double y1, double z1, double x2, double y2, double z2) {
      double dX = Math.ceil(Math.abs(x2 - x1));
      double dY = Math.ceil(Math.abs(y2 - y1));
      double dZ = Math.ceil(Math.abs(z2 - z1));
      return Math.max(Math.max(dX, dY), dZ);
   }

   public static enum Mode {
      All,
      Flatten,
      Smash;

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{All, Flatten, Smash};
      }
   }

   public static enum SortMode {
      None,
      Closest,
      Furthest,
      TopDown;

      // $FF: synthetic method
      private static SortMode[] $values() {
         return new SortMode[]{None, Closest, Furthest, TopDown};
      }
   }

   public static enum Shape {
      Cube,
      UniformCube,
      Sphere;

      // $FF: synthetic method
      private static Shape[] $values() {
         return new Shape[]{Cube, UniformCube, Sphere};
      }
   }
}
