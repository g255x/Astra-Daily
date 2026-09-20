package dev.Astra.mod.modules.impl.render;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.AmbientOcclusionEvent;
import dev.Astra.api.events.impl.ChunkOcclusionEvent;
import dev.Astra.api.events.impl.RenderBlockEntityEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.class_1922;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2404;
import net.minecraft.class_259;
import net.minecraft.class_2680;

public class Xray extends Module {
   public static Xray INSTANCE;
   private static final ThreadLocal<class_2338.class_2339> EXPOSED_POS = ThreadLocal.withInitial(class_2338.class_2339::new);
   private final BooleanSetting ores = this.add(new BooleanSetting("Ores", true));
   private final BooleanSetting mineralBlocks = this.add(new BooleanSetting("Mineral Blocks", true));
   private final BooleanSetting storageBlocks = this.add(new BooleanSetting("Storage", true));
   private final BooleanSetting liquids = this.add(new BooleanSetting("Liquids", true));

   public Xray() {
      super("Xray", Module.Category.Render);
      this.setChinese("矿物透视");
      INSTANCE = this;
   }

   public void onEnable() {
      if (!nullCheck()) {
         mc.field_1769.method_3279();
      }
   }

   public void onDisable() {
      mc.field_1769.method_3279();
   }

   public boolean shouldRender(class_2680 state) {
      if (!this.isOn()) {
         return false;
      } else {
         class_2248 block = state.method_26204();
         if (this.ores.getValue() && this.isOre(block)) {
            return true;
         } else if (this.mineralBlocks.getValue() && this.isMineralBlock(block)) {
            return true;
         } else if (this.storageBlocks.getValue() && this.isStorageBlock(block)) {
            return true;
         } else {
            return this.liquids.getValue() && this.isLiquid(block);
         }
      }
   }

   private boolean isOre(class_2248 block) {
      String name = block.method_9539().replace("block.minecraft.", "").replace("item.minecraft.", "");
      return name.contains("_ore") || name.equals("ancient_debris") || name.equals("nether_quartz_ore");
   }

   private boolean isMineralBlock(class_2248 block) {
      String name = block.method_9539().replace("block.minecraft.", "").replace("item.minecraft.", "");
      if (!name.endsWith("_block")) {
         return false;
      } else {
         String[] minerals = new String[]{"diamond", "gold", "iron", "netherite", "quartz", "redstone", "lapis", "coal", "emerald"};

         for(String m : minerals) {
            if (name.contains(m)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean isStorageBlock(class_2248 block) {
      String name = block.method_9539().replace("block.minecraft.", "").replace("item.minecraft.", "");
      return name.contains("chest") || name.contains("shulker") || name.contains("ender_chest") || name.contains("barrel") || name.contains("hopper") || name.contains("dropper") || name.contains("dispenser") || name.contains("furnace") || name.contains("smoker") || name.contains("blast_furnace");
   }

   private boolean isLiquid(class_2248 block) {
      return block instanceof class_2404;
   }

   public boolean isBlocked(class_2248 block) {
      return this.isOn() && !this.shouldRender(block.method_9564());
   }

   public static boolean shouldBlock(class_2680 state) {
      return INSTANCE.isOn() && !INSTANCE.shouldRender(state);
   }

   public boolean modifyDrawSide(class_2680 state, class_1922 view, class_2338 pos, class_2350 facing, boolean returns) {
      if (returns) {
         return returns;
      } else {
         class_2338 adjPos = pos.method_10093(facing);
         class_2680 adjState = view.method_8320(adjPos);
         return adjState.method_26173(view, adjPos, facing.method_10153()) != class_259.method_1077() || adjState.method_26204() != state.method_26204() || isExposed(adjPos);
      }
   }

   private static boolean isExposed(class_2338 pos) {
      for(class_2350 dir : class_2350.values()) {
         if (!mc.field_1687.method_8320(((class_2338.class_2339)EXPOSED_POS.get()).method_25505(pos, dir)).method_26225()) {
            return true;
         }
      }

      return false;
   }

   @EventListener
   private void onRenderBlockEntity(RenderBlockEntityEvent event) {
      if (this.isBlocked(event.blockEntity.method_11010().method_26204())) {
         event.cancel();
      }

   }

   @EventListener
   private void onChunkOcclusion(ChunkOcclusionEvent event) {
      event.cancel();
   }

   @EventListener
   private void onAmbientOcclusion(AmbientOcclusionEvent event) {
      event.lightLevel = 1.0F;
   }
}
