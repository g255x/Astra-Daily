package nekiplay.meteorplus.features.modules.world;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import meteordevelopment.meteorclient.events.entity.player.BreakBlockEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_634;
import net.minecraft.class_746;
import net.minecraft.class_2846.class_2847;

public class GhostBlockFixer extends Module {
   private final SettingGroup GBSettings;
   private final Setting<Integer> delay;
   private final Setting<Integer> range;
   private final ArrayDeque<class_2338> blocks;
   private long millis;

   public GhostBlockFixer() {
      super(Categories.World, "auto-ghost-block-fixer", "Automatically fix ghost blocks.");
      this.GBSettings = this.settings.getDefaultGroup();
      this.delay = this.GBSettings.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Fixer delay")).description("Delay for block fixing.")).defaultValue(50)).range(1, 250).sliderRange(1, 250).build());
      this.range = this.GBSettings.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Fixer range")).description("Range to block.")).defaultValue(6)).range(1, 80).sliderRange(1, 80).build());
      this.blocks = new ArrayDeque();
      this.millis = 0L;
   }

   @EventHandler
   public void onBlockBreak(BreakBlockEvent block) {
      this.blocks.add(block.blockPos);
   }

   public void onActivate() {
      this.millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
   }

   public void onDeactivate() {
      this.blocks.clear();
   }

   @EventHandler
   public void onLeave(GameLeftEvent event) {
      this.blocks.clear();
   }

   @EventHandler
   public void onTick(TickEvent.Post event) {
      if (!this.blocks.isEmpty()) {
         class_634 conn = this.mc.method_1562();
         class_746 player = this.mc.field_1724;
         if (conn != null && player != null && LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= this.millis) {
            class_2338 block = (class_2338)this.blocks.peek();

            assert block != null;

            assert this.mc.field_1687 != null;

            double distance = this.mc.field_1724.method_5649((double)block.method_10263(), (double)block.method_10264(), (double)block.method_10260());
            class_2680 state = this.mc.field_1687.method_8320(block);
            if (distance <= (double)(Integer)this.range.get() && state.method_26215()) {
               this.millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + (long)(Integer)this.delay.get();
               class_2846 packet = new class_2846(class_2847.field_12971, block, class_2350.field_11036, 0);
               conn.method_52787(packet);
               this.blocks.remove();
            } else if (!state.method_26215()) {
               this.blocks.remove();
            }
         }
      }

   }
}
