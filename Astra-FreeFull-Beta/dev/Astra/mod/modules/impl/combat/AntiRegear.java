package dev.Astra.mod.modules.impl.combat;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.OpenScreenEvent;
import dev.Astra.api.utils.world.BlockUtil;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.misc.AutoRegear;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1713;
import net.minecraft.class_1733;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2480;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_495;

public class AntiRegear extends Module {
   public static AntiRegear INSTANCE;
   private final SliderSetting range = this.add((new SliderSetting("Range", (double)4.5F, (double)1.0F, (double)8.0F, 0.1)).setSuffix("m"));
   private final BooleanSetting pauseRegear = this.add(new BooleanSetting("PauseRegear", true));
   private final List<class_2338> processed = new ArrayList();
   private final List<class_2338> ignored = new ArrayList();
   private State state;
   private class_2338 currentTarget;
   private int progress;
   private long openTime;

   public AntiRegear() {
      super("AntiRegear", Module.Category.Combat);
      this.state = AntiRegear.State.IDLE;
      this.currentTarget = null;
      this.progress = 0;
      this.openTime = 0L;
      this.setChinese("反补给");
      INSTANCE = this;
   }

   public void onEnable() {
      this.processed.clear();
      this.ignored.clear();
      this.state = AntiRegear.State.IDLE;
      this.currentTarget = null;
      this.progress = 0;
   }

   public void onDisable() {
      this.state = AntiRegear.State.IDLE;
      if (mc.field_1724.field_7512 instanceof class_1733) {
         mc.field_1724.method_3137();
      }

   }

   public void addIgnored(class_2338 pos) {
      if (pos != null && !this.ignored.contains(pos)) {
         this.ignored.add(pos);
      }

   }

   private void cleanIgnored() {
      this.ignored.removeIf((pos) -> {
         class_2680 state = mc.field_1687.method_8320(pos);
         return !(state.method_26204() instanceof class_2480);
      });
   }

   @EventListener(
      priority = 1000
   )
   public void onOpenScreen(OpenScreenEvent event) {
      if ((this.state == AntiRegear.State.OPENING || this.state == AntiRegear.State.PROCESSING) && event.screen instanceof class_495) {
         event.cancel();
         event.screen = null;
         if (mc.field_1755 instanceof class_495) {
            mc.field_1755.method_25419();
         }
      }

   }

   @EventListener
   public void onTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (!this.pauseRegear.getValue() || AutoRegear.INSTANCE == null || !AutoRegear.INSTANCE.isOn()) {
            this.cleanIgnored();
            switch (this.state.ordinal()) {
               case 0:
                  class_2338 target = this.findNearestUnprocessedShulker();
                  if (target != null) {
                     this.currentTarget = target;
                     this.openShulker(target);
                     this.state = AntiRegear.State.OPENING;
                     this.openTime = System.currentTimeMillis();
                  }
                  break;
               case 1:
                  if (mc.field_1724.field_7512 instanceof class_1733) {
                     this.state = AntiRegear.State.PROCESSING;
                     this.progress = 0;
                  } else if (System.currentTimeMillis() - this.openTime > 2000L) {
                     if (this.currentTarget != null) {
                        this.processed.add(this.currentTarget);
                     }

                     this.state = AntiRegear.State.IDLE;
                     this.currentTarget = null;
                  }
                  break;
               case 2:
                  boolean done = this.processShulker();
                  if (done) {
                     if (this.currentTarget != null) {
                        this.processed.add(this.currentTarget);
                     }

                     mc.field_1724.method_3137();
                     this.state = AntiRegear.State.IDLE;
                     this.currentTarget = null;
                     this.progress = 0;
                  }
            }

         }
      }
   }

   private class_2338 findNearestUnprocessedShulker() {
      class_2338 playerPos = mc.field_1724.method_24515();
      double rangeSq = this.range.getValue() * this.range.getValue();
      class_2338 best = null;
      double bestDist = Double.MAX_VALUE;

      for(class_2338 pos : BlockUtil.getSphere((float)this.range.getValue() + 1.0F)) {
         if (!this.processed.contains(pos) && !this.ignored.contains(pos)) {
            class_2680 state = mc.field_1687.method_8320(pos);
            if (state.method_26204() instanceof class_2480 && this.canOpen(pos)) {
               double dist = pos.method_10262(playerPos);
               if (dist <= rangeSq && dist < bestDist) {
                  bestDist = dist;
                  best = pos;
               }
            }
         }
      }

      return best;
   }

   private boolean canOpen(class_2338 pos) {
      class_2680 state = mc.field_1687.method_8320(pos);
      if (!(state.method_26204() instanceof class_2480)) {
         return false;
      } else if (state.method_28498(class_2741.field_12525)) {
         class_2350 facing = (class_2350)state.method_11654(class_2741.field_12525);
         class_2338 openDir = pos.method_10093(facing);
         class_2680 openState = mc.field_1687.method_8320(openDir);
         return openState.method_26215() || openState.method_26204() == class_2246.field_10382;
      } else {
         return mc.field_1687.method_22347(pos.method_10084()) || mc.field_1687.method_8320(pos.method_10084()).method_26204() == class_2246.field_10382;
      }
   }

   private void openShulker(class_2338 pos) {
      class_2350 side = BlockUtil.getClickSide(pos);
      if (side != null) {
         BlockUtil.clickBlock(pos, side, false, class_1268.field_5808, AntiCheat.INSTANCE.packetPlace.getValue());
      }
   }

   private boolean processShulker() {
      if (!(mc.field_1724.field_7512 instanceof class_1733)) {
         return true;
      } else {
         class_1733 handler = (class_1733)mc.field_1724.field_7512;
         int totalSlots = 27;

         for(int i = this.progress; i < totalSlots; ++i) {
            if (!handler.method_7611(i).method_7677().method_7960()) {
               mc.field_1761.method_2906(handler.field_7763, i, 0, class_1713.field_7794, mc.field_1724);
               if (!handler.method_7611(i).method_7677().method_7960()) {
                  mc.field_1761.method_2906(handler.field_7763, i, 0, class_1713.field_7790, mc.field_1724);
                  mc.field_1761.method_2906(handler.field_7763, -999, 0, class_1713.field_7790, mc.field_1724);
               }

               this.progress = i + 1;
               return false;
            }
         }

         return true;
      }
   }

   private static enum State {
      IDLE,
      OPENING,
      PROCESSING;

      // $FF: synthetic method
      private static State[] $values() {
         return new State[]{IDLE, OPENING, PROCESSING};
      }
   }
}
