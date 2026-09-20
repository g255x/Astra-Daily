package nekiplay.meteorplus.mixin.meteorclient.modules;

import java.util.List;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.render.NoRender;
import meteordevelopment.orbit.EventHandler;
import nekiplay.main.events.hud.DebugDrawTextEvent;
import nekiplay.main.events.hud.RenderArmorBarEvent;
import nekiplay.main.events.hud.RenderExperienceBarEvent;
import nekiplay.main.events.hud.RenderExperienceLevelEvent;
import nekiplay.main.events.hud.RenderFoodBarEvent;
import nekiplay.main.events.hud.RenderHealthBarEvent;
import nekiplay.main.events.hud.RenderMountHealthBarEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(
   value = {NoRender.class},
   remap = false,
   priority = 1001
)
public class NoRenderMixin extends Module {
   @Unique
   private final SettingGroup noRenderMeteorPlusSetting;
   @Shadow
   private final SettingGroup sgHUD;
   @Unique
   private final Setting<Boolean> noPosition;
   @Unique
   private final Setting<Boolean> noPositionBlock;
   @Unique
   private final Setting<Boolean> noPositionChunk;
   @Unique
   private final Setting<Boolean> noTargetBlockPosition;
   @Unique
   private final Setting<Boolean> noTargetFluidPosition;
   @Unique
   private final Setting<Boolean> experienceBar;
   @Unique
   private final Setting<Boolean> experienceLevel;
   @Unique
   private final Setting<Boolean> healthBar;
   @Unique
   private final Setting<Boolean> mountHealthBar;
   @Unique
   private final Setting<Boolean> foodBar;
   @Unique
   private final Setting<Boolean> armorBar;

   public NoRenderMixin(Category category, String name, String description) {
      super(category, name, description);
      this.noRenderMeteorPlusSetting = this.settings.createGroup("F3");
      this.sgHUD = this.settings.getGroup("HUD");
      this.noPosition = this.noRenderMeteorPlusSetting.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("position")).defaultValue(false)).build());
      this.noPositionBlock = this.noRenderMeteorPlusSetting.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("position-block")).defaultValue(false)).build());
      this.noPositionChunk = this.noRenderMeteorPlusSetting.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("position-chunk")).defaultValue(false)).build());
      this.noTargetBlockPosition = this.noRenderMeteorPlusSetting.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("target-block-position")).defaultValue(false)).build());
      this.noTargetFluidPosition = this.noRenderMeteorPlusSetting.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("target-fluid-position")).defaultValue(false)).build());
      this.experienceBar = this.sgHUD.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("experience-bar")).defaultValue(false)).build());
      this.experienceLevel = this.sgHUD.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("experience-level")).defaultValue(false)).build());
      this.healthBar = this.sgHUD.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("health-bar")).defaultValue(false)).build());
      this.mountHealthBar = this.sgHUD.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("mount-health-bar")).defaultValue(false)).build());
      this.foodBar = this.sgHUD.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("food-bar")).defaultValue(false)).build());
      this.armorBar = this.sgHUD.add(((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("armor-bar")).defaultValue(false)).build());
   }

   @Unique
   @EventHandler
   private void onMountHealthBarRender(RenderMountHealthBarEvent event) {
      if ((Boolean)this.mountHealthBar.get()) {
         event.setCancelled(true);
      }

   }

   @Unique
   @EventHandler
   private void onArmorBarRender(RenderArmorBarEvent event) {
      if ((Boolean)this.armorBar.get()) {
         event.setCancelled(true);
      }

   }

   @Unique
   @EventHandler
   private void onFoodBarRender(RenderFoodBarEvent event) {
      if ((Boolean)this.foodBar.get()) {
         event.setCancelled(true);
      }

   }

   @Unique
   @EventHandler
   private void onHealthBarRender(RenderHealthBarEvent event) {
      if ((Boolean)this.healthBar.get()) {
         event.setCancelled(true);
      }

   }

   @Unique
   @EventHandler
   private void onExperienceBarRender(RenderExperienceBarEvent event) {
      if ((Boolean)this.experienceBar.get()) {
         event.setCancelled(true);
      }

   }

   @Unique
   @EventHandler
   private void onExperienceLevelRender(RenderExperienceLevelEvent event) {
      if ((Boolean)this.experienceLevel.get()) {
         event.setCancelled(true);
      }

   }

   @Unique
   @EventHandler
   private void onDebugF3RenderText(DebugDrawTextEvent event) {
      List<String> lines = event.getLines();
      if (event.isLeft()) {
         if ((Boolean)this.noPosition.get()) {
            lines.removeIf((s) -> s.contains("XYZ:"));
         }

         if ((Boolean)this.noPositionBlock.get()) {
            lines.removeIf((s) -> s.contains("Block:"));
         }

         if ((Boolean)this.noPositionChunk.get()) {
            lines.removeIf((s) -> s.contains("Chunk:"));
         }
      } else {
         if ((Boolean)this.noTargetBlockPosition.get()) {
            lines.removeIf((s) -> s.contains("Targeted Block:"));
         }

         if ((Boolean)this.noTargetFluidPosition.get()) {
            lines.removeIf((s) -> s.contains("Targeted Fluid:"));
         }
      }

   }
}
