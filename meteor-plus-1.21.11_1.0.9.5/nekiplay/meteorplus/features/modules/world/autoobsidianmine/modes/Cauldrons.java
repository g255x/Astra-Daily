package nekiplay.meteorplus.features.modules.world.autoobsidianmine.modes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import meteordevelopment.meteorclient.events.world.CollisionShapeEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.player.AutoEat;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.Pool;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.meteorclient.utils.world.TickRate;
import nekiplay.meteorplus.features.modules.world.autoobsidianmine.AutoObsidianFarmMode;
import nekiplay.meteorplus.features.modules.world.autoobsidianmine.AutoObsidianFarmModes;
import nekiplay.meteorplus.utils.RaycastUtils;
import net.minecraft.class_1268;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_2680;
import net.minecraft.class_2868;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;

public class Cauldrons extends AutoObsidianFarmMode {
   private final List<class_2338.class_2339> blocks = new ArrayList();
   private boolean firstBlock;
   private int noBlockTimer;
   private final class_2338.class_2339 lastBlockPos = new class_2338.class_2339();
   private int timer;
   private final Pool<class_2338.class_2339> blockPosPool = new Pool(class_2338.class_2339::new);
   private int collectTimer = 0;
   private int lavaPlaceTimer = 0;
   private final Portals.SortMode sortMode;
   private int placed;

   public Cauldrons() {
      super(AutoObsidianFarmModes.Cauldrons);
      this.sortMode = Portals.SortMode.Closest;
      this.placed = 0;
   }

   public void onActivate() {
      this.firstBlock = true;
      this.timer = 0;
      this.noBlockTimer = 0;
      this.collectTimer = 0;
      this.lavaPlaceTimer = 0;
      this.placed = 0;
   }

   public void onCollisionShape(CollisionShapeEvent event) {
      if ((Boolean)this.settings.solidCauldrons.get()) {
         if (event.state.method_26204() == class_2246.field_10593 || event.state.method_26204() == class_2246.field_27098 || event.state.method_26204() == class_2246.field_27097) {
            event.shape = class_259.method_1077();
         }

      }
   }

   public void onDeactivate() {
      if (this.placed > 0) {
         ChatUtils.info("Farmed obsidian: " + this.placed / 64 + " stacks", new Object[0]);
         if (this.placed >= 1728) {
            ChatUtils.info("Farmed obsidian: " + this.placed / 64 / 27 + " chests", new Object[0]);
         }
      }

   }

   public String getInfoString() {
      return Integer.toString(this.placed);
   }

   public void onTickEventPost(TickEvent.Post event) {
      if (this.mc.field_1724 != null && this.mc.field_1687 != null && this.mc.field_1761 != null) {
         if (!this.mc.field_1724.method_6115() && (!((AutoEat)Modules.get().get(AutoEat.class)).isActive() || !((AutoEat)Modules.get().get(AutoEat.class)).eating) || !(Boolean)this.settings.pauseOnEat.get()) {
            if (!((double)TickRate.INSTANCE.getTimeSinceLastTick() >= 1.7) || !(Boolean)this.settings.tpsCheck.get()) {
               class_2338 placing = (class_2338)this.settings.lavaPlaceLocation.get();
               if (!(this.mc.field_1724.method_5707(placing.method_46558()) >= (double)((Integer)this.settings.range.get() + (Integer)this.settings.range.get() + 1))) {
                  BlockIterator.register((Integer)this.settings.range.get(), (Integer)this.settings.range.get(), (blockPos, blockState) -> {
                     class_2248 block = blockState.method_26204();
                     if (block == class_2246.field_27098 || block == class_2246.field_10540 || block == class_2246.field_10593) {
                        this.blocks.add(((class_2338.class_2339)this.blockPosPool.get()).method_10101(blockPos));
                     }

                  });
                  BlockIterator.after(() -> {
                     if (this.mc.field_1724 != null && this.mc.field_1724.method_31548() != null) {
                        if (this.blocks.isEmpty()) {
                           if (this.noBlockTimer++ >= (Integer)this.settings.delay.get()) {
                              this.firstBlock = true;
                           }

                        } else {
                           this.noBlockTimer = 0;
                           double pX = this.mc.field_1724.method_23317();
                           double pY = this.mc.field_1724.method_23318();
                           double pZ = this.mc.field_1724.method_23321();
                           this.blocks.sort(Comparator.comparingDouble((value) -> Utils.squaredDistance(pX, pY, pZ, (double)value.method_10263() + (double)0.5F, (double)value.method_10264() + (double)0.5F, (double)value.method_10260() + (double)0.5F) * (double)(this.sortMode == Portals.SortMode.Closest ? 1 : -1)));
                           if (!this.firstBlock && !this.lastBlockPos.equals(this.blocks.get(0))) {
                              this.timer = (Integer)this.settings.delay.get();
                              this.firstBlock = false;
                              this.lastBlockPos.method_10101((class_2382)this.blocks.get(0));
                              if (this.timer > 0) {
                                 return;
                              }
                           }

                           class_2680 state = this.mc.field_1687.method_8320(placing);
                           if (state.method_26204() == class_2246.field_10540) {
                              if (BlockUtils.canBreak(placing)) {
                                 this.rotate(placing, (Runnable)null);
                                 this.lavaPlaceTimer = 0;
                                 BlockUtils.breakBlock(placing, true);
                              }
                           } else {
                              FindItemResult bucket = InvUtils.findInHotbar(new class_1792[]{class_1802.field_8550});
                              FindItemResult lavaBucket = InvUtils.findInHotbar(new class_1792[]{class_1802.field_8187});
                              if (lavaBucket.found() && this.mc.field_1724.method_73189().method_1022(placing.method_46558()) <= (double)((Integer)this.settings.range.get() + 1)) {
                                 if (state.method_26204() != class_2246.field_10164) {
                                    if (this.lavaPlaceTimer >= (Integer)this.settings.lavaPlaceDelay.get()) {
                                       double yaw = Rotations.getYaw(placing);
                                       double pitch = Rotations.getPitch(placing);
                                       class_243 pos = this.mc.field_1724.method_33571();
                                       class_239 result = RaycastUtils.bucketRaycast(pos, (float)pitch, (float)yaw, class_242.field_1348);
                                       if (result.method_17783() == class_240.field_1332) {
                                          class_3965 blockHitResult = (class_3965)result;
                                          class_2338 blockPos = blockHitResult.method_17777();
                                          class_2350 direction = blockHitResult.method_17780();
                                          class_2338 blockPos2 = blockPos.method_10093(direction);
                                          if (blockPos2.equals(placing)) {
                                             Rotations.rotate(yaw, pitch, 10, true, () -> {
                                                InvUtils.swap(lavaBucket.slot(), true);
                                                this.mc.field_1761.method_2919(this.mc.field_1724, class_1268.field_5808);
                                                ++this.placed;
                                                InvUtils.swapBack();
                                                this.lavaPlaceTimer = 0;
                                             });
                                          }
                                       }
                                    } else {
                                       ++this.lavaPlaceTimer;
                                    }
                                 }
                              } else if (bucket.found()) {
                                 for(class_2338 block : this.blocks) {
                                    class_2680 state2 = this.mc.field_1687.method_8320(block);
                                    if (state2.method_26204() == class_2246.field_27098 && this.mc.field_1724.method_73189().method_1022(block.method_46558()) <= (double)((Integer)this.settings.range.get() + 1)) {
                                       if (this.collectTimer >= (Integer)this.settings.collectDelay.get()) {
                                          this.mc.field_1724.method_31548().method_61496(bucket.slot());
                                          this.mc.field_1724.field_3944.method_52787(new class_2868(bucket.slot()));
                                          this.rotate(block, () -> {
                                             class_243 hitPos = class_243.method_24953(block);
                                             class_2350 side = class_2350.field_11033;
                                             class_3965 bhr = new class_3965(hitPos, class_2350.field_11036, block, false);
                                             boolean isSneaking = false;
                                             if ((Boolean)this.settings.bypassSneak.get()) {
                                                this.mc.field_1724.method_5660(false);
                                                isSneaking = true;
                                             }

                                             BlockUtils.interact(bhr, class_1268.field_5808, true);
                                             if ((Boolean)this.settings.bypassSneak.get() && isSneaking) {
                                                this.mc.field_1724.method_5660(true);
                                             }

                                             this.collectTimer = 0;
                                          });
                                       } else {
                                          ++this.collectTimer;
                                       }
                                       break;
                                    }
                                 }

                                 this.firstBlock = false;

                                 for(class_2338.class_2339 blockPos : this.blocks) {
                                    this.blockPosPool.free(blockPos);
                                 }

                                 this.blocks.clear();
                              }
                           }

                        }
                     }
                  });
               }
            }
         }
      }
   }

   private void rotate(class_2338 target, Runnable action) {
      Rotations.rotate(Rotations.getYaw(target), Rotations.getPitch(target), action);
   }
}
