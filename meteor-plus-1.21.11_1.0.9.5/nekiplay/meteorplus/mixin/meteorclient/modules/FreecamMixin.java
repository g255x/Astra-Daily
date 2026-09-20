package nekiplay.meteorplus.mixin.meteorclient.modules;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalBlock;
import java.util.Objects;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.Cancellable;
import meteordevelopment.meteorclient.events.meteor.KeyEvent;
import meteordevelopment.meteorclient.events.meteor.MouseClickEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.KeybindSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.movement.Blink;
import meteordevelopment.meteorclient.systems.modules.render.Freecam;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.utils.RaycastUtils;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({Freecam.class})
public class FreecamMixin {
   @Unique
   private final Freecam freecam = (Freecam)this;
   @Unique
   private final SettingGroup freecamMeteorPlusSetting;
   @Unique
   private final Setting<Boolean> moveBaritoneControl;
   @Unique
   private final Setting<Keybind> baritoneMoveKey;
   @Unique
   private final Setting<Boolean> blinkBaritoneControl;
   @Unique
   private final Setting<Keybind> baritoneMoveBlinkKey;
   @Unique
   private final Setting<Keybind> baritoneStopKey;
   @Unique
   private final Blink blink;
   @Unique
   private boolean isBlinkMoving;

   public FreecamMixin() {
      this.freecamMeteorPlusSetting = this.freecam.settings.createGroup("Meteor+");
      this.moveBaritoneControl = this.freecamMeteorPlusSetting.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("baritone-move-control")).description("click bind to set the destination on the selected block. Right mouse click to cancel.")).build());
      SettingGroup var10001 = this.freecamMeteorPlusSetting;
      KeybindSetting.Builder var10002 = (KeybindSetting.Builder)((KeybindSetting.Builder)(new KeybindSetting.Builder()).name("baritone-move-keybind")).description("The bind for move.");
      Setting var10003 = this.moveBaritoneControl;
      Objects.requireNonNull(var10003);
      this.baritoneMoveKey = var10001.add(((KeybindSetting.Builder)((KeybindSetting.Builder)var10002.visible(var10003::get)).defaultValue(Keybind.fromKey(0))).build());
      this.blinkBaritoneControl = this.freecamMeteorPlusSetting.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("baritone-blink-move-control")).description("Click bind to move to point in blink.")).build());
      var10001 = this.freecamMeteorPlusSetting;
      var10002 = (KeybindSetting.Builder)((KeybindSetting.Builder)(new KeybindSetting.Builder()).name("baritone-blink-move-keybind")).description("The bind for move in blink.");
      var10003 = this.blinkBaritoneControl;
      Objects.requireNonNull(var10003);
      this.baritoneMoveBlinkKey = var10001.add(((KeybindSetting.Builder)var10002.visible(var10003::get)).build());
      this.baritoneStopKey = this.freecamMeteorPlusSetting.add(((KeybindSetting.Builder)((KeybindSetting.Builder)((KeybindSetting.Builder)((KeybindSetting.Builder)(new KeybindSetting.Builder()).name("baritone-stop-keybind")).description("The bind for stop baritone actions.")).visible(() -> (Boolean)this.blinkBaritoneControl.get() || (Boolean)this.moveBaritoneControl.get())).defaultValue(Keybind.fromKey(1))).build());
      this.blink = (Blink)Modules.get().get(Blink.class);
      this.isBlinkMoving = false;
   }

   @Unique
   private class_2338 tryGetValidPos(class_2338 pos) {
      assert MeteorClient.mc.field_1687 != null;

      class_2680 state = MeteorClient.mc.field_1687.method_8320(pos);
      class_2248 block = state.method_26204();
      return block != class_2246.field_10112 && block != class_2246.field_10479 && block != class_2246.field_10214 && block != class_2246.field_28411 && block != class_2246.field_10428 && block != class_2246.field_10477 && block != class_2246.field_28680 && block != class_2246.field_10336 && block != class_2246.field_10099 && block != class_2246.field_10411 && block != class_2246.field_10284 && block != class_2246.field_10231 && block != class_2246.field_42735 && block != class_2246.field_40277 && block != class_2246.field_10121 && block != class_2246.field_22104 && block != class_2246.field_10330 && block != class_2246.field_10544 && block != class_2246.field_37554 && block != class_2246.field_22105 && block != class_2246.field_10088 && block != class_2246.field_10401 && block != class_2246.field_10391 && block != class_2246.field_42736 && block != class_2246.field_40261 && block != class_2246.field_10187 && block != class_2246.field_22106 && block != class_2246.field_10265 && block != class_2246.field_10587 && block != class_2246.field_37552 && block != class_2246.field_22107 && block != class_2246.field_10251 && block != class_2246.field_10559 && block != class_2246.field_22121 && block != class_2246.field_22114 && block != class_2246.field_10182 && block != class_2246.field_10449 && block != class_2246.field_10086 && block != class_2246.field_10226 && block != class_2246.field_10573 && block != class_2246.field_10270 && block != class_2246.field_10048 && block != class_2246.field_10156 && block != class_2246.field_10315 && block != class_2246.field_10554 && block != class_2246.field_9995 && block != class_2246.field_10548 && block != class_2246.field_42734 && block != class_2246.field_42750 && block != class_2246.field_10424 && block != class_2246.field_9974 && block != class_2246.field_43228 && block != class_2246.field_42749 && block != class_2246.field_10341 && block != class_2246.field_10293 && block != class_2246.field_10609 && block != class_2246.field_10247 && block != class_2246.field_46287 && block != class_2246.field_46286 && block != class_2246.field_10217 && block != class_2246.field_10385 && block != class_2246.field_10575 && block != class_2246.field_10108 && block != class_2246.field_42727 && block != class_2246.field_10160 && block != class_2246.field_10276 && block != class_2246.field_10394 && block != class_2246.field_10167 && block != class_2246.field_10546 && block != class_2246.field_10425 && block != class_2246.field_10025 ? pos.method_10084() : pos;
   }

   @Unique
   private @Nullable class_2338 rayCastClicked() {
      class_2338 blockPos = null;
      class_243 rotationVector = RaycastUtils.getRotationVector((float)this.freecam.getPitch(MeteorClient.mc.method_61966().method_60637(true)), (float)this.freecam.getYaw(MeteorClient.mc.method_61966().method_60637(true)));
      class_243 pos = new class_243(this.freecam.pos.x, this.freecam.pos.y, this.freecam.pos.z);
      class_239 result = RaycastUtils.raycast(pos, rotationVector, (double)256.0F, MeteorClient.mc.method_61966().method_60637(true), true);
      if (result.method_17783() == class_240.field_1332) {
         class_3965 blockHitResult = (class_3965)result;
         blockPos = blockHitResult.method_17777();
      }

      return blockPos;
   }

   @Unique
   private void Work(Cancellable event) {
      if (((Keybind)this.baritoneMoveBlinkKey.get()).isPressed() && MeteorClient.mc.field_1755 == null) {
         class_2338 clicked = this.rayCastClicked();
         if ((Boolean)this.blinkBaritoneControl.get()) {
            if (clicked == null) {
               return;
            }

            if (MeteorClient.mc.field_1687 == null) {
               return;
            }

            class_2680 state = MeteorClient.mc.field_1687.method_8320(clicked);
            if (state.method_26215()) {
               return;
            }

            this.isBlinkMoving = true;
            GoalBlock goal = new GoalBlock(this.tryGetValidPos(clicked));
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath((Goal)null);
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(goal);
            event.cancel();
         }
      }

      if (((Keybind)this.baritoneMoveKey.get()).isPressed() && MeteorClient.mc.field_1755 == null) {
         class_2338 clicked = this.rayCastClicked();
         if (clicked == null) {
            return;
         }

         if (MeteorClient.mc.field_1687 == null) {
            return;
         }

         class_2680 state = MeteorClient.mc.field_1687.method_8320(clicked);
         if (state.method_26215()) {
            return;
         }

         if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().forceCancel();
         }

         GoalBlock goal = new GoalBlock(this.tryGetValidPos(clicked));
         BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath((Goal)null);
         BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(goal);
         event.cancel();
      }

      if (((Keybind)this.baritoneStopKey.get()).isPressed() && MeteorClient.mc.field_1755 == null) {
         BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().forceCancel();
         if (this.blink != null && this.blink.isActive()) {
            this.blink.toggle();
            this.isBlinkMoving = false;
         }

         event.cancel();
      }

   }

   @Unique
   @EventHandler
   private void onKeyEvent(KeyEvent event) {
      if (MeteorClient.mc.field_1687 != null && event.action == KeyAction.Press) {
         this.Work(event);
      }

   }

   @Unique
   @EventHandler
   private void onMouseClickEvent(MouseClickEvent event) {
      if (MeteorClient.mc.field_1687 != null && event.action == KeyAction.Press) {
         this.Work(event);
      }

   }

   @Unique
   @EventHandler
   private void onTickEvent(TickEvent.Pre event) {
      if (MeteorClient.mc.field_1687 != null && (Boolean)this.blinkBaritoneControl.get() && this.blink != null) {
         if (this.isBlinkMoving && (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().hasPath() || BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) && !this.blink.isActive()) {
            this.blink.toggle();
         }

         if (this.isBlinkMoving && (!BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().hasPath() || !BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) && this.blink.isActive()) {
            this.blink.toggle();
            this.isBlinkMoving = false;
         }
      }

   }
}
