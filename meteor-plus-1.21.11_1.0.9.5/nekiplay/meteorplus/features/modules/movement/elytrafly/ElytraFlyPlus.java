package nekiplay.meteorplus.features.modules.movement.elytrafly;

import java.util.Objects;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.features.modules.movement.elytrafly.modes.Control;
import nekiplay.meteorplus.features.modules.movement.elytrafly.modes.Wasp;
import net.minecraft.class_243;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public class ElytraFlyPlus extends Module {
   private final SettingGroup sgGeneral;
   public final Setting<ElytraFlyModes> flightMode;
   public final Setting<Double> horizontal_wasp;
   public final Setting<Double> fallSpeed_wasp;
   public final Setting<Boolean> smartFall_wasp;
   public final Setting<Double> up_wasp;
   public final Setting<Double> down_wasp;
   public final Setting<Double> speed_control;
   public final Setting<Double> upMultiplier_control;
   public final Setting<Double> downSpeed_control;
   public final Setting<Double> fallSpeed_control;
   public final Setting<Boolean> resetSpeed;
   public final Setting<Boolean> noCrash;
   public final Setting<Integer> crashLookAhead;
   private ElytraFlyMode currentMode;

   public ElytraFlyPlus() {
      super(Categories.Movement, "elytra-fly+", "Gives you more control over your elytra.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.flightMode = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("mode")).description("The mode of flying.")).defaultValue(ElytraFlyModes.Control)).onModuleActivated((flightModesSetting) -> this.onModeChanged((ElytraFlyModes)flightModesSetting.get()))).onChanged(this::onModeChanged)).build());
      this.horizontal_wasp = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("horizontal-speed")).description("How many blocks to move each tick horizontally.")).defaultValue((double)1.0F).min((double)0.0F).sliderRange((double)0.0F, (double)5.0F).visible(() -> this.flightMode.get() == ElytraFlyModes.Wasp)).build());
      this.fallSpeed_wasp = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("fall-speed")).description("How many blocks to fall down each tick.")).defaultValue((double)0.0F).min((double)0.0F).sliderRange((double)0.0F, (double)1.0F).visible(() -> this.flightMode.get() == ElytraFlyModes.Wasp)).build());
      this.smartFall_wasp = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("smart-fall")).description("Only falls down when looking down.")).defaultValue(true)).visible(() -> this.flightMode.get() == ElytraFlyModes.Wasp)).build());
      this.up_wasp = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("up-speed")).description("How many blocks to move up each tick.")).defaultValue((double)1.0F).min((double)0.0F).sliderRange((double)0.0F, (double)5.0F).visible(() -> this.flightMode.get() == ElytraFlyModes.Wasp)).build());
      this.down_wasp = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("down-speed")).description("How many blocks to move down each tick.")).defaultValue((double)1.0F).min((double)0.0F).sliderRange((double)0.0F, (double)5.0F).visible(() -> this.flightMode.get() == ElytraFlyModes.Wasp)).build());
      this.speed_control = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("Speed")).description("How many blocks to move each tick.")).defaultValue((double)1.0F).min((double)0.0F).sliderRange((double)0.0F, (double)5.0F).visible(() -> this.flightMode.get() == ElytraFlyModes.Control)).build());
      this.upMultiplier_control = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("up-multiplier")).description("How many times faster should we fly up.")).defaultValue((double)1.0F).min((double)0.0F).sliderRange((double)0.0F, (double)5.0F).visible(() -> this.flightMode.get() == ElytraFlyModes.Control)).build());
      this.downSpeed_control = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("down-speed")).description("How many blocks to move down each tick.")).defaultValue((double)1.0F).min((double)0.0F).sliderRange((double)0.0F, (double)5.0F).visible(() -> this.flightMode.get() == ElytraFlyModes.Control)).build());
      this.fallSpeed_control = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)((DoubleSetting.Builder)(new DoubleSetting.Builder()).name("fall-speed")).description("How many blocks to fall down each tick.")).defaultValue((double)0.0F).min((double)0.0F).sliderRange((double)0.0F, (double)1.0F).visible(() -> this.flightMode.get() == ElytraFlyModes.Control)).build());
      this.resetSpeed = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("reset-speed")).description("Reset you speed after disable")).defaultValue(false)).build());
      this.noCrash = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("no-crash")).description("Stops you from going into walls.")).defaultValue(false)).build());
      SettingGroup var10001 = this.sgGeneral;
      IntSetting.Builder var10002 = ((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("crash-look-ahead")).description("Distance to look ahead when flying.")).defaultValue(5)).range(1, 15).sliderMin(1);
      Setting var10003 = this.noCrash;
      Objects.requireNonNull(var10003);
      this.crashLookAhead = var10001.add(((IntSetting.Builder)var10002.visible(var10003::get)).build());
      this.currentMode = new Control();
   }

   public void onActivate() {
      this.currentMode.onActivate();
   }

   public void onDeactivate() {
      this.currentMode.onDeactivate();
   }

   @EventHandler
   private void onPlayerMove(PlayerMoveEvent event) {
      this.currentMode.onPlayerMove(event);
      if ((Boolean)this.noCrash.get() && this.mc.field_1724.method_6128()) {
         class_243 lookAheadPos = this.mc.field_1724.method_73189().method_1019(this.mc.field_1724.method_18798().method_1029().method_1021((double)(Integer)this.crashLookAhead.get()));
         class_3959 raycastContext = new class_3959(this.mc.field_1724.method_73189(), new class_243(lookAheadPos.method_10216(), this.mc.field_1724.method_23318(), lookAheadPos.method_10215()), class_3960.field_17558, class_242.field_1348, this.mc.field_1724);
         class_3965 hitResult = this.mc.field_1687.method_17742(raycastContext);
         if (hitResult != null && hitResult.method_17783() == class_240.field_1332) {
            ((IVec3d)event.movement).meteor$set((double)0.0F, this.currentMode.velY, (double)0.0F);
         }
      }

   }

   @EventHandler
   private void onTick(TickEvent.Post event) {
      this.currentMode.onTick();
   }

   @EventHandler
   private void onPreTick(TickEvent.Pre event) {
      this.currentMode.onPreTick();
   }

   @EventHandler
   private void onPacketSend(PacketEvent.Send event) {
      this.currentMode.onPacketSend(event);
   }

   @EventHandler
   private void onPacketReceive(PacketEvent.Receive event) {
      this.currentMode.onPacketReceive(event);
   }

   private void onModeChanged(ElytraFlyModes mode) {
      switch (mode) {
         case Control -> this.currentMode = new Control();
         case Wasp -> this.currentMode = new Wasp();
      }

   }
}
