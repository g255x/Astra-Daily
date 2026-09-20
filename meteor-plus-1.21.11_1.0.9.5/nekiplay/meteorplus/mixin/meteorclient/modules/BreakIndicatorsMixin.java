package nekiplay.meteorplus.mixin.meteorclient.modules;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.mixin.ClientPlayerInteractionManagerAccessor;
import meteordevelopment.meteorclient.mixin.WorldRendererAccessor;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.BreakIndicators;
import meteordevelopment.meteorclient.systems.modules.world.PacketMine;
import meteordevelopment.meteorclient.utils.render.NametagUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3191;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({BreakIndicators.class})
public class BreakIndicatorsMixin extends Module {
   @Unique
   private final SettingGroup sgPercentageRenderPlus;
   @Unique
   public final Setting<Boolean> percentageRender;
   @Unique
   public final Setting<Boolean> packetMine;
   @Unique
   private final Setting<SettingColor> percentageColor;

   public BreakIndicatorsMixin(Category category, String name, String description, String... aliases) {
      super(category, name, description, aliases);
      this.sgPercentageRenderPlus = this.settings.createGroup("Meteor+ Percentage Render");
      this.percentageRender = this.sgPercentageRenderPlus.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("enable-percentage-render")).description("Enable percentage text render.")).defaultValue(true)).build());
      SettingGroup var10001 = this.sgPercentageRenderPlus;
      BoolSetting.Builder var10002 = (BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("packet-mine")).description("Render packet mine blocks.")).defaultValue(true);
      Setting var10003 = this.percentageRender;
      Objects.requireNonNull(var10003);
      this.packetMine = var10001.add(((BoolSetting.Builder)var10002.visible(var10003::get)).build());
      var10001 = this.sgPercentageRenderPlus;
      ColorSetting.Builder var6 = ((ColorSetting.Builder)((ColorSetting.Builder)(new ColorSetting.Builder()).name("percentage-color")).description("The color for the percentage text.")).defaultValue(new SettingColor(25, 252, 25, 150));
      var10003 = this.percentageRender;
      Objects.requireNonNull(var10003);
      this.percentageColor = var10001.add(((ColorSetting.Builder)var6.visible(var10003::get)).build());
   }

   @Unique
   @EventHandler
   private void on2DRender(Render2DEvent event) {
      Map<Integer, class_3191> blocks = ((WorldRendererAccessor)this.mc.field_1769).meteor$getBlockBreakingInfos();
      float ownBreakingStage = ((ClientPlayerInteractionManagerAccessor)this.mc.field_1761).meteor$getBreakingProgress();
      class_2338 ownBreakingPos = ((ClientPlayerInteractionManagerAccessor)this.mc.field_1761).meteor$getCurrentBreakingBlockPos();
      if (ownBreakingPos != null && ownBreakingStage > 0.0F) {
         double shrinkFactor = (double)1.0F - (double)ownBreakingStage;
         class_2680 state = this.mc.field_1687.method_8320(ownBreakingPos);
         class_265 shape = state.method_26218(this.mc.field_1687, ownBreakingPos);
         if (shape == null || shape.method_1110()) {
            return;
         }

         class_238 orig = shape.method_1107();
         this.renderBlock(event, ownBreakingPos, shrinkFactor, orig);
      }

      blocks.values().forEach((info) -> {
         class_2338 pos = info.method_13991();
         int stage = info.method_13988();
         if (!pos.equals(ownBreakingPos)) {
            class_2680 state = this.mc.field_1687.method_8320(pos);
            class_265 shape = state.method_26218(this.mc.field_1687, pos);
            if (shape != null && !shape.method_1110()) {
               class_238 orig = shape.method_1107();
               double shrinkFactor = (double)(9 - (stage + 1)) / (double)9.0F;
               double progress = (double)1.0F - shrinkFactor;
               this.renderBlock(event, pos, shrinkFactor, orig);
            }
         }
      });
      if ((Boolean)this.packetMine.get() && !((PacketMine)Modules.get().get(PacketMine.class)).blocks.isEmpty()) {
         this.renderPacket(event, ((PacketMine)Modules.get().get(PacketMine.class)).blocks);
      }

   }

   @Unique
   private void renderBlock(Render2DEvent event, class_2338 pos, double shrinkFactor, class_238 orig) {
      Vector3d vector3d = new Vector3d((double)pos.method_10263() + orig.method_1005().field_1352, (double)pos.method_10264() + orig.method_1005().field_1351, (double)pos.method_10260() + orig.method_1005().field_1350);
      if ((Boolean)this.percentageRender.get() && NametagUtils.to2D(vector3d, (double)1.0F, true)) {
         TextRenderer text = TextRenderer.get();
         NametagUtils.begin(vector3d, event.drawContext);
         text.beginBig();
         Object[] var10001 = new Object[]{shrinkFactor * (double)100.0F};
         String label = String.format("%1$,.0f", var10001) + "%";
         double hologramWidth = text.getWidth(label, true);
         double heightDown = text.getHeight(true);
         double widthHalf = hologramWidth / (double)2.0F;
         double hX = -widthHalf;
         double hY = -heightDown;
         text.render(label, hX, hY, (Color)this.percentageColor.get(), true);
         text.end();
         NametagUtils.end(event.drawContext);
      }

   }

   @Unique
   private void renderPacket(Render2DEvent event, List<PacketMine.MyBlock> blocks) {
      for(PacketMine.MyBlock block : blocks) {
         if (block.mining && block.progress() != Double.POSITIVE_INFINITY) {
            class_265 shape = block.blockState.method_26218(this.mc.field_1687, block.blockPos);
            if (shape == null || shape.method_1110()) {
               return;
            }

            class_238 orig = shape.method_1107();
            double progressNormalised = Math.min((double)1.0F, block.progress());
            double shrinkFactor = (double)1.0F - progressNormalised;
            class_2338 pos = block.blockPos;
            this.renderBlock(event, pos, shrinkFactor, orig);
         }
      }

   }
}
