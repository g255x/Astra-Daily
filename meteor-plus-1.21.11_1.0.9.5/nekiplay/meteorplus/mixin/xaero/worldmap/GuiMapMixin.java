package nekiplay.meteorplus.mixin.xaero.worldmap;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import java.util.ArrayList;
import java.util.List;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.misc.Names;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.Dimension;
import nekiplay.MixinPlugin;
import nekiplay.meteorplus.features.modules.integrations.MapIntegration;
import net.minecraft.class_1074;
import net.minecraft.class_2338;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.map.MapProcessor;
import xaero.map.gui.GuiMap;
import xaero.map.gui.dropdown.rightclick.RightClickOption;
import xaero.map.misc.Misc;
import xaero.map.region.MapBlock;
import xaero.map.region.MapRegion;
import xaero.map.region.MapTile;
import xaero.map.region.MapTileChunk;

@Mixin({GuiMap.class})
public abstract class GuiMapMixin {
   @Unique
   private final GuiMap guiMap = (GuiMap)this;
   @Shadow(
      remap = false
   )
   private double scale;
   @Shadow(
      remap = false
   )
   private int rightClickX;
   @Shadow(
      remap = false
   )
   private int rightClickY;
   @Shadow(
      remap = false
   )
   private int rightClickZ;
   @Shadow(
      remap = false
   )
   private MapProcessor mapProcessor;
   @Shadow(
      remap = false
   )
   private double cameraX = (double)0.0F;
   @Shadow(
      remap = false
   )
   private double cameraZ = (double)0.0F;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void onInit(CallbackInfo info) {
      this.cameraX = (double)((float)MeteorClient.mc.field_1724.method_23317());
      this.cameraZ = (double)((float)MeteorClient.mc.field_1724.method_23321());
   }

   @Inject(
      method = {"getRightClickOptions"},
      at = {@At("RETURN")},
      remap = false
   )
   private void rightClickOptins(CallbackInfoReturnable<ArrayList<RightClickOption>> cir) {
      Modules modules = Modules.get();
      if (modules != null) {
         MapIntegration mapIntegration = (MapIntegration)modules.get(MapIntegration.class);
         if (mapIntegration != null && mapIntegration.isActive()) {
            ArrayList<RightClickOption> options = (ArrayList)cir.getReturnValue();
            int mouseXPos = (int)Misc.getMouseX(MeteorClient.mc, false);
            int mouseYPos = (int)Misc.getMouseY(MeteorClient.mc, false);
            int mouseFromCentreX = mouseXPos - MeteorClient.mc.method_22683().method_4489() / 2;
            int mouseFromCentreY = mouseYPos - MeteorClient.mc.method_22683().method_4506() / 2;
            double mousePosX = (double)mouseFromCentreX / this.scale + this.cameraX;
            double mousePosZ = (double)mouseFromCentreY / this.scale + this.cameraZ;
            int mouseBlockPosX = (int)Math.floor(mousePosX);
            int mouseBlockPosZ = (int)Math.floor(mousePosZ);
            int renderedCaveLayer = this.mapProcessor.getCurrentCaveLayer();
            MapRegion leafRegion = this.mapProcessor.getLeafMapRegion(renderedCaveLayer, mouseBlockPosX >> 9, mouseBlockPosZ >> 9, false);
            MapTileChunk chunk = leafRegion == null ? null : leafRegion.getChunk(mouseBlockPosX >> 6 & 7, mouseBlockPosZ >> 6 & 7);
            MapTile mouseTile = chunk == null ? null : chunk.getTile(mouseBlockPosX >> 4 & 3, mouseBlockPosZ >> 4 & 3);
            if (mapIntegration.showBlock.get() != null && mouseTile != null) {
               MapBlock block = mouseTile.getBlock(mouseBlockPosX & 15, mouseBlockPosZ & 15);
               MapPixelAccessor pixel = (MapPixelAccessor)block;
               options.addAll(2, List.of(new RightClickOption(Names.get(pixel.getBlockState().method_26204()), options.size(), this.guiMap) {
                  public void onAction(class_437 screen) {
                  }
               }));
            }

            if (!MixinPlugin.isXaeroPlusMapresent) {
               if ((Boolean)mapIntegration.baritoneGoto.get()) {
                  options.addAll(3, List.of(new RightClickOption(class_1074.method_4662("gui.world_map.baritone_goal_here", new Object[0]), options.size(), this.guiMap) {
                     public void onAction(class_437 screen) {
                        GoalBlock goal = new GoalBlock((new class_2338(GuiMapMixin.this.rightClickX, GuiMapMixin.this.rightClickY, GuiMapMixin.this.rightClickZ)).method_10084());
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(goal);
                     }
                  }, new RightClickOption(class_1074.method_4662("gui.world_map.baritone_path_here", new Object[0]), options.size(), this.guiMap) {
                     public void onAction(class_437 screen) {
                        GoalBlock goal = new GoalBlock((new class_2338(GuiMapMixin.this.rightClickX, GuiMapMixin.this.rightClickY, GuiMapMixin.this.rightClickZ)).method_10084());
                        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(goal);
                     }
                  }));
                  if ((Boolean)mapIntegration.baritoneElytra.get() && (Boolean)mapIntegration.baritoneGoto.get() && PlayerUtils.getDimension() == Dimension.Nether && this.rightClickY - 1 > 0 && this.rightClickY < 128) {
                     options.addAll(3, List.of(new RightClickOption(class_1074.method_4662("gui.world_map.baritone_elytra_here", new Object[0]), options.size(), this.guiMap) {
                        public void onAction(class_437 screen) {
                           GoalBlock goal = new GoalBlock((new class_2338(GuiMapMixin.this.rightClickX, GuiMapMixin.this.rightClickY, GuiMapMixin.this.rightClickZ)).method_10084());
                           BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(goal);
                           BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("elytra");
                        }
                     }));
                  }
               }
            } else if ((Boolean)mapIntegration.baritoneElytra.get() && (Boolean)mapIntegration.baritoneGoto.get() && PlayerUtils.getDimension() == Dimension.Nether && this.rightClickY - 1 > 0 && this.rightClickY < 128) {
               options.addAll(3, List.of(new RightClickOption(class_1074.method_4662("gui.world_map.baritone_elytra_here", new Object[0]), options.size(), this.guiMap) {
                  public void onAction(class_437 screen) {
                     GoalBlock goal = new GoalBlock((new class_2338(GuiMapMixin.this.rightClickX, GuiMapMixin.this.rightClickY, GuiMapMixin.this.rightClickZ)).method_10084());
                     BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(goal);
                     BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("elytra");
                  }
               }));
            }
         }
      }

   }
}
