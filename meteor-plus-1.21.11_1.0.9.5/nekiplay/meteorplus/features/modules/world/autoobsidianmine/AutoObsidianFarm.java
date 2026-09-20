package nekiplay.meteorplus.features.modules.world.autoobsidianmine;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.CollisionShapeEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BlockPosSetting;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.MixinPlugin;
import nekiplay.meteorplus.features.modules.world.autoobsidianmine.modes.Cauldrons;
import nekiplay.meteorplus.features.modules.world.autoobsidianmine.modes.Portals;
import net.minecraft.class_2338;
import net.minecraft.class_2596;
import net.minecraft.class_2828;

public class AutoObsidianFarm extends Module {
   private final SettingGroup sgGeneral;
   public final Setting<AutoObsidianFarmModes> workingMode;
   public final Setting<class_2338> mainPortalPosition;
   public final Setting<class_2338> twoPortalPosition;
   public final Setting<String> command;
   public final Setting<class_2338> lavaPlaceLocation;
   public final Setting<Integer> delayCommand;
   public final Setting<Integer> delay;
   public final Setting<Integer> collectDelay;
   public final Setting<Integer> lavaPlaceDelay;
   public final Setting<Integer> range;
   public final Setting<Boolean> solidCauldrons;
   public final Setting<Boolean> bypassSneak;
   public final Setting<Boolean> tpsCheck;
   public final Setting<Boolean> noBaritoneBreaking;
   public final Setting<Boolean> noBaritonePlacing;
   public final Setting<Boolean> rotate;
   public final Setting<Boolean> swingHand;
   public final Setting<Boolean> pauseOnEat;
   private AutoObsidianFarmMode currentMode;

   public AutoObsidianFarm() {
      super(Categories.World, "Auto-obsidian-farm", "Automatically farm obsidian in AFK.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.workingMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("Working mode.")).defaultValue(AutoObsidianFarmModes.Portals_Vanila)).onModuleActivated((modesSetting) -> this.onModeChanged((AutoObsidianFarmModes)modesSetting.get()))).onChanged(this::onModeChanged)).build());
      this.mainPortalPosition = this.sgGeneral.add(((BlockPosSetting.Builder)((BlockPosSetting.Builder)((BlockPosSetting.Builder)(new BlockPosSetting.Builder()).name("portal location 1")).description("the position of the portal to hell")).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Portals_Vanila)).build());
      this.twoPortalPosition = this.sgGeneral.add(((BlockPosSetting.Builder)((BlockPosSetting.Builder)((BlockPosSetting.Builder)(new BlockPosSetting.Builder()).name("portal location 2")).description("portal position in hell for new portal generations")).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Portals_Vanila)).build());
      this.command = this.sgGeneral.add(((StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)((StringSetting.Builder)(new StringSetting.Builder()).name("command")).description("Send command.")).defaultValue("/home")).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Portal_Homes)).build());
      this.lavaPlaceLocation = this.sgGeneral.add(((BlockPosSetting.Builder)((BlockPosSetting.Builder)((BlockPosSetting.Builder)(new BlockPosSetting.Builder()).name("lava-place-location")).description("the position placing lava")).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Cauldrons)).build());
      this.delayCommand = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("command-delay")).description("Ticks delay.")).defaultValue(700)).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Portal_Homes)).build());
      this.delay = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("mining-delay")).description("Mining delay.")).defaultValue(3)).build());
      this.collectDelay = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("collect-delay")).description("Cauldron collecting lava delay.")).defaultValue(8)).build());
      this.lavaPlaceDelay = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("lava-place-delay")).description("Delay for placing lava.")).defaultValue(8)).build());
      this.range = this.sgGeneral.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("range")).description("Cauldron range's.")).defaultValue(5)).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Cauldrons)).build());
      this.solidCauldrons = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("solid-cauldrons")).description("Solid cauldrons.")).defaultValue(false)).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Cauldrons)).build());
      this.bypassSneak = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("bypass-sneak")).description("Bypass sneak interact.")).defaultValue(false)).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Cauldrons)).build());
      this.tpsCheck = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("no-work-if-server-lag")).description("Stop working if server lagging.")).defaultValue(false)).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Cauldrons)).build());
      this.noBaritoneBreaking = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("disable-baritone-breaking-if-not-mine-portal")).description("No break blocks if is not mining portal.")).defaultValue(true)).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Portals_Vanila)).build());
      this.noBaritonePlacing = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("disable-baritone-place")).description("No place blocks.")).defaultValue(true)).visible(() -> this.workingMode.get() == AutoObsidianFarmModes.Portals_Vanila)).build());
      this.rotate = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("rotate")).description("Rotate to breaking block.")).defaultValue(false)).visible(() -> this.workingMode.get() != AutoObsidianFarmModes.Cauldrons)).build());
      this.swingHand = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("swing-hand")).description("Swing hand client side.")).defaultValue(true)).visible(() -> this.workingMode.get() != AutoObsidianFarmModes.Cauldrons)).build());
      this.pauseOnEat = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("pause-on-eat")).description("Pause farming on eat or auto eat.")).defaultValue(true)).build());
   }

   private void onModeChanged(AutoObsidianFarmModes mode) {
      switch (mode) {
         case Portal_Homes:
         case Portals_Vanila:
            if (MixinPlugin.isBaritonePresent) {
               this.currentMode = new Portals();
            } else {
               this.error("This mode need Baritone API (Fabric)", new Object[0]);
            }
            break;
         case Cauldrons:
            this.currentMode = new Cauldrons();
      }

   }

   public String getInfoString() {
      return this.currentMode == null ? "" : this.currentMode.getInfoString();
   }

   public void onActivate() {
      if (this.currentMode != null) {
         this.currentMode.onActivate();
      }

   }

   public void onDeactivate() {
      if (this.currentMode != null) {
         this.currentMode.onDeactivate();
      }

   }

   @EventHandler
   private void onPreTickPost(TickEvent.Post event) {
      if (this.currentMode != null) {
         this.currentMode.onTickEventPost(event);
      }

   }

   @EventHandler
   private void onPreTick(TickEvent.Pre event) {
      if (this.currentMode != null) {
         this.currentMode.onTickEventPre(event);
      }

   }

   @EventHandler
   private void onCollisionShape(CollisionShapeEvent event) {
      if (this.currentMode != null) {
         this.currentMode.onCollisionShape(event);
      }

   }

   @EventHandler
   private void onMovePacket(PacketEvent.Send event) {
      class_2596 var3 = event.packet;
      if (var3 instanceof class_2828 playerMove) {
         if (this.currentMode != null) {
            this.currentMode.onMovePacket(playerMove);
         }
      }

   }
}
