package nekiplay.meteorplus.features.modules.world;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.Dimension;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2846;
import net.minecraft.class_634;
import net.minecraft.class_2846.class_2847;

public class BedrockStorageBruteforce extends Module {
   public ArrayList<class_2338> scanned = new ArrayList();
   private final SettingGroup SCSettings;
   public final Setting<Boolean> clear_cache_blocks;
   private final Setting<Integer> range;
   private final Setting<Integer> delaymin;
   private final Setting<Integer> delaymax;
   Thread clickerThread;
   private boolean scan;
   Dimension dim;
   class_2338 last;
   long millis;

   public BedrockStorageBruteforce() {
      super(Categories.World, "bedrock-storage-BF", "Scan storage's in bedrock from -1 to -4 height for 1.12.2.");
      this.SCSettings = this.settings.createGroup("Scanner Settings");
      this.clear_cache_blocks = this.SCSettings.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("clear-cache")).description("clear-saved-cache.")).defaultValue(false)).onChanged((a) -> this.scanned.clear())).build());
      this.range = this.SCSettings.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Scan range")).description("Bruteforce delay min .")).defaultValue(8)).min(3).sliderRange(8, 64).build());
      this.delaymin = this.SCSettings.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Scan delay min")).description("Bruteforce delay min .")).defaultValue(24)).min(0).sliderRange(0, 80).build());
      this.delaymax = this.SCSettings.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Scan delay max")).description("Bruteforce delay max .")).defaultValue(25)).min(0).sliderRange(0, 80).build());
      this.clickerThread = null;
      this.scan = true;
      this.last = null;
      this.millis = 0L;
   }

   private void stop() {
      if (this.clickerThread != null && this.clickerThread.isAlive()) {
      }

      this.scan = false;
   }

   public void onDeactivate() {
      this.stop();
   }

   private boolean isAllowScan(class_2338 pos) {
      if (this.mc.field_1687 != null && !this.scanned.contains(pos)) {
         class_2680 state = this.mc.field_1687.method_8320(pos);
         class_2248 block = state.method_26204();
         return block != class_2246.field_10124 && block != class_2246.field_9987 && block != class_2246.field_10164 && block != class_2246.field_10382;
      } else {
         return false;
      }
   }

   public void onActivate() {
      this.dim = PlayerUtils.getDimension();
      this.scan = true;
      this.clickerThread = new Thread(() -> {
         while(this.scan) {
            assert this.mc.field_1724 != null;

            class_2338 playerPos = this.mc.field_1724.method_24515();
            int ranger = (Integer)this.range.get();
            int x = Utils.random(playerPos.method_10263() - ranger, playerPos.method_10263() + ranger);
            int y = Utils.random(1, 4);
            int z = Utils.random(playerPos.method_10260() - ranger, playerPos.method_10260() + ranger);
            class_2338 posible = new class_2338(x, y, z);
            if (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= this.millis && this.isAllowScan(posible)) {
               class_634 conn = this.mc.method_1562();
               if (conn != null) {
                  this.last = posible;
                  this.scanned.add(posible);
                  class_2846 abortPacket = new class_2846(class_2847.field_12968, posible, class_2350.field_11036, 0);
                  conn.method_52787(abortPacket);
                  class_2846 abortPacket2 = new class_2846(class_2847.field_12971, posible, class_2350.field_11036, 0);
                  conn.method_52787(abortPacket2);
                  this.millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + (long)Utils.random((Integer)this.delaymin.get(), (Integer)this.delaymax.get());
               }
            }
         }

      });
      this.clickerThread.start();
      this.millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
   }

   @EventHandler
   private void onRender(Render3DEvent event) {
      if (this.last != null) {
         class_2338 bp = this.last;

         assert this.mc.field_1687 != null;

         class_2680 state = this.mc.field_1687.method_8320(bp);
         class_265 shape = state.method_26218(this.mc.field_1687, bp);
         SettingColor color = new SettingColor(255, 255, 255);
         if (shape.method_1110()) {
            return;
         }

         for(class_238 b : shape.method_1090()) {
            event.renderer.box((double)bp.method_10263() + b.field_1323, (double)bp.method_10264() + b.field_1322, (double)bp.method_10260() + b.field_1321, (double)bp.method_10263() + b.field_1320, (double)bp.method_10264() + b.field_1325, (double)bp.method_10260() + b.field_1324, new SettingColor(255, 255, 255, 255), color, ShapeMode.Lines, 0);
         }

         event.renderer.line(RenderUtils.center.field_1352, RenderUtils.center.field_1351, RenderUtils.center.field_1350, (double)bp.method_10263(), (double)bp.method_10264(), (double)bp.method_10260(), color);
      }

   }

   @EventHandler
   private void onGameLeft(GameLeftEvent event) {
      this.scanned.clear();
      this.stop();
   }

   @EventHandler
   private void onTickPre(TickEvent.Pre event) {
      if (PlayerUtils.getDimension() != this.dim) {
         this.dim = PlayerUtils.getDimension();
         this.scanned.clear();
      }

   }
}
