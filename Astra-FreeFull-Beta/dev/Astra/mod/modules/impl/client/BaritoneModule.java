package dev.Astra.mod.modules.impl.client;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.pathing.calc.IPathingControlManager;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.process.IBaritoneProcess;
import baritone.api.process.ICustomGoalProcess;
import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;

public class BaritoneModule extends Module {
   public static BaritoneModule INSTANCE;
   private final SliderSetting rangeConfig = this.add(new SliderSetting("Range", (double)4.5F, (double)1.0F, (double)5.0F));
   private final BooleanSetting placeConfig = this.add(new BooleanSetting("Place", true));
   private final BooleanSetting breakConfig = this.add(new BooleanSetting("Break", true));
   private final BooleanSetting sprintConfig = this.add(new BooleanSetting("Sprint", true));
   private final BooleanSetting inventoryConfig = this.add(new BooleanSetting("UseInventory", true));
   private final BooleanSetting vinesConfig = this.add(new BooleanSetting("Vines", true));
   private final BooleanSetting jump256Config = this.add(new BooleanSetting("JumpAt256", true));
   private final BooleanSetting waterBucketFallConfig = this.add(new BooleanSetting("WaterBucketFall", true));
   private final BooleanSetting parkourConfig = this.add(new BooleanSetting("Parkour", true));
   private final BooleanSetting parkourPlaceConfig = this.add(new BooleanSetting("ParkourPlace", true));
   private final BooleanSetting parkourAscendConfig = this.add(new BooleanSetting("ParkourAscend", true));
   private final BooleanSetting diagonalAscendConfig = this.add(new BooleanSetting("DiagonalAscend", true));
   private final BooleanSetting diagonalDescendConfig = this.add(new BooleanSetting("DiagonalDescend", true));
   private final BooleanSetting mineDownConfig = this.add(new BooleanSetting("MineDownward", true));
   private final BooleanSetting legitMineConfig = this.add(new BooleanSetting("LegitMine", true));
   private final BooleanSetting logOnArrivalConfig = this.add(new BooleanSetting("LogOnArrival", false));
   private final BooleanSetting freeLookConfig = this.add(new BooleanSetting("FreeLook", true));
   private final BooleanSetting antiCheatConfig = this.add(new BooleanSetting("AntiCheat", true));
   private final BooleanSetting strictLiquidConfig = this.add(new BooleanSetting("Strict-Liquid", true));
   private final BooleanSetting censorCoordsConfig = this.add(new BooleanSetting("CensorCoords", true));
   private final BooleanSetting censorCommandsConfig = this.add(new BooleanSetting("CensorCommands", true));
   private final BooleanSetting chatControl = this.add(new BooleanSetting("ChatControl", false));
   private final BooleanSetting debugConfig = this.add(new BooleanSetting("Debug", false));

   public BaritoneModule() {
      super("Baritone", Module.Category.Client);
      Astra.EVENT_BUS.subscribe(this);
      INSTANCE = this;
      this.setChinese("寻路设置");
   }

   public static void forward() {
      class_2350 direction = mc.field_1724.method_5735();
      int x = mc.field_1724.method_31477() + direction.method_10163().method_10263() * 30000000;
      int z = mc.field_1724.method_31479() + direction.method_10163().method_10260() * 30000000;
      cancelEverything();
      IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
      ICustomGoalProcess customGoalProcess;
      if (baritone != null && (customGoalProcess = baritone.getCustomGoalProcess()) != null) {
         customGoalProcess.setGoalAndPath(new GoalXZ(x, z));
      }

   }

   public static boolean isPathing() {
      IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
      return baritone != null && baritone.getPathingBehavior() != null && baritone.getPathingBehavior().isPathing();
   }

   public static void gotoPos(class_2338 pos) {
      IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
      if (baritone != null) {
         ICustomGoalProcess customGoalProcess = baritone.getCustomGoalProcess();
         if (customGoalProcess == null) {
            return;
         }

         customGoalProcess.setGoalAndPath(new GoalBlock(pos.method_10263(), pos.method_10264(), pos.method_10260()));
      }

   }

   public static void mine(class_2248 block) {
      IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
      if (baritone != null) {
         baritone.getMineProcess().mine(new class_2248[]{block});
      }

   }

   public static void cancelEverything() {
      IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
      if (baritone != null && baritone.getPathingBehavior() != null) {
         baritone.getPathingBehavior().cancelEverything();
      }

   }

   public static boolean isActive() {
      IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
      if (baritone != null) {
         ICustomGoalProcess customGoalProcess = baritone.getCustomGoalProcess();
         if (customGoalProcess != null && customGoalProcess.isActive()) {
            return true;
         }

         IPathingControlManager controlManager = baritone.getPathingControlManager();
         if (controlManager != null && controlManager.mostRecentInControl().isPresent()) {
            return ((IBaritoneProcess)controlManager.mostRecentInControl().get()).isActive();
         }
      }

      return false;
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      BaritoneAPI.getSettings().blockReachDistance.value = this.rangeConfig.getValueFloat();
      BaritoneAPI.getSettings().allowPlace.value = this.placeConfig.getValue();
      BaritoneAPI.getSettings().allowBreak.value = this.breakConfig.getValue();
      BaritoneAPI.getSettings().allowSprint.value = this.sprintConfig.getValue();
      BaritoneAPI.getSettings().allowInventory.value = this.inventoryConfig.getValue();
      BaritoneAPI.getSettings().allowVines.value = this.vinesConfig.getValue();
      BaritoneAPI.getSettings().allowJumpAt256.value = this.jump256Config.getValue();
      BaritoneAPI.getSettings().allowWaterBucketFall.value = this.waterBucketFallConfig.getValue();
      BaritoneAPI.getSettings().allowParkour.value = this.parkourConfig.getValue();
      BaritoneAPI.getSettings().allowParkourAscend.value = this.parkourAscendConfig.getValue();
      BaritoneAPI.getSettings().allowParkourPlace.value = this.parkourPlaceConfig.getValue();
      BaritoneAPI.getSettings().allowDiagonalAscend.value = this.diagonalAscendConfig.getValue();
      BaritoneAPI.getSettings().allowDiagonalDescend.value = this.diagonalDescendConfig.getValue();
      BaritoneAPI.getSettings().allowDownward.value = this.mineDownConfig.getValue();
      BaritoneAPI.getSettings().legitMine.value = this.legitMineConfig.getValue();
      BaritoneAPI.getSettings().disconnectOnArrival.value = this.logOnArrivalConfig.getValue();
      BaritoneAPI.getSettings().freeLook.value = this.freeLookConfig.getValue();
      BaritoneAPI.getSettings().antiCheatCompatibility.value = this.antiCheatConfig.getValue();
      BaritoneAPI.getSettings().strictLiquidCheck.value = this.strictLiquidConfig.getValue();
      BaritoneAPI.getSettings().censorCoordinates.value = this.censorCoordsConfig.getValue();
      BaritoneAPI.getSettings().censorRanCommands.value = this.censorCommandsConfig.getValue();
      BaritoneAPI.getSettings().chatControl.value = this.chatControl.getValue();
      BaritoneAPI.getSettings().chatDebug.value = this.debugConfig.getValue();
   }

   public void enable() {
      this.state = true;
   }

   public void disable() {
      this.state = true;
   }

   public boolean isOn() {
      return true;
   }
}
