package nekiplay.meteorplus.utils.xraybruteforce;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.blockesp.ESPBlockData;
import meteordevelopment.meteorclient.utils.misc.UnorderedArrayList;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import nekiplay.meteorplus.features.modules.world.XrayBruteforce;
import net.minecraft.class_2248;

public class XGroup {
   private static final XrayBruteforce search = (XrayBruteforce)Modules.get().get(XrayBruteforce.class);
   private final class_2248 block;
   public final UnorderedArrayList<XBlock> blocks = new UnorderedArrayList();
   private double sumX;
   private double sumY;
   private double sumZ;

   public XGroup(class_2248 block) {
      this.block = block;
   }

   public void add(XBlock block, boolean removeFromOld, boolean splitGroup) {
      this.blocks.add(block);
      this.sumX += (double)block.x;
      this.sumY += (double)block.y;
      this.sumZ += (double)block.z;
      if (block.group != null && removeFromOld) {
         block.group.remove(block, splitGroup);
      }

      block.group = this;
   }

   public void add(XBlock block) {
      this.add(block, true, true);
   }

   public void remove(XBlock block, boolean splitGroup) {
      this.blocks.remove(block);
      this.sumX -= (double)block.x;
      this.sumY -= (double)block.y;
      this.sumZ -= (double)block.z;
      if (this.blocks.isEmpty()) {
         search.removeGroup(block.group);
      } else if (splitGroup) {
         this.trySplit(block);
      }

   }

   public void remove(XBlock block) {
      this.remove(block, true);
   }

   private void trySplit(XBlock block) {
      Set<XBlock> neighbours = new ObjectOpenHashSet(6);

      for(int side : XBlock.SIDES) {
         if ((block.neighbours & side) == side) {
            XBlock neighbour = block.getSideBlock(side);
            if (neighbour != null) {
               neighbours.add(neighbour);
            }
         }
      }

      if (neighbours.size() > 1) {
         Set<XBlock> remainingBlocks = new ObjectOpenHashSet(this.blocks);
         Queue<XBlock> blocksToCheck = new ArrayDeque();
         blocksToCheck.offer((XBlock)this.blocks.get(0));
         remainingBlocks.remove(this.blocks.get(0));
         neighbours.remove(this.blocks.get(0));

         label86:
         while(!blocksToCheck.isEmpty()) {
            XBlock b = (XBlock)blocksToCheck.poll();

            for(int side : XBlock.SIDES) {
               if ((b.neighbours & side) == side) {
                  XBlock neighbour = b.getSideBlock(side);
                  if (neighbour != null && remainingBlocks.contains(neighbour)) {
                     blocksToCheck.offer(neighbour);
                     remainingBlocks.remove(neighbour);
                     neighbours.remove(neighbour);
                     if (neighbours.isEmpty()) {
                        break label86;
                     }
                  }
               }
            }
         }

         if (neighbours.size() > 0) {
            XGroup group = search.newGroup(this.block);
            group.blocks.ensureCapacity(remainingBlocks.size());
            UnorderedArrayList var10000 = this.blocks;
            Objects.requireNonNull(remainingBlocks);
            var10000.removeIf(remainingBlocks::contains);

            for(XBlock b : remainingBlocks) {
               group.add(b, false, false);
               this.sumX -= (double)b.x;
               this.sumY -= (double)b.y;
               this.sumZ -= (double)b.z;
            }

            if (neighbours.size() > 1) {
               block.neighbours = 0;

               for(XBlock b : neighbours) {
                  int x = b.x - block.x;
                  if (x == 1) {
                     block.neighbours |= 8;
                  } else if (x == -1) {
                     block.neighbours |= 128;
                  }

                  int y = b.y - block.y;
                  if (y == 1) {
                     block.neighbours |= 512;
                  } else if (y == -1) {
                     block.neighbours |= 16384;
                  }

                  int z = b.z - block.z;
                  if (z == 1) {
                     block.neighbours |= 2;
                  } else if (z == -1) {
                     block.neighbours |= 32;
                  }
               }

               group.trySplit(block);
            }
         }

      }
   }

   public void merge(XGroup group) {
      this.blocks.ensureCapacity(this.blocks.size() + group.blocks.size());

      for(XBlock block : group.blocks) {
         this.add(block, false, false);
      }

      search.removeGroup(group);
   }

   public void render(Render3DEvent event) {
      ESPBlockData blockData = search.getBlockData(this.block);
      if (blockData.tracer) {
         event.renderer.line(RenderUtils.center.field_1352, RenderUtils.center.field_1351, RenderUtils.center.field_1350, this.sumX / (double)this.blocks.size() + (double)0.5F, this.sumY / (double)this.blocks.size() + (double)0.5F, this.sumZ / (double)this.blocks.size() + (double)0.5F, blockData.tracerColor);
      }

   }
}
