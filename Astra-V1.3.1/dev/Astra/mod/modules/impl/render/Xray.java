package dev.Astra.mod.modules.impl.render;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.AmbientOcclusionEvent;
import dev.Astra.api.events.impl.ChunkOcclusionEvent;
import dev.Astra.api.events.impl.RenderBlockEntityEvent;
import dev.Astra.mod.gui.windows.WindowBase;
import dev.Astra.mod.gui.windows.WindowsScreen;
import dev.Astra.mod.gui.windows.impl.ItemSelectWindow;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.class_1922;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_259;
import net.minecraft.class_2680;

public class Xray extends Module {
   public static Xray INSTANCE;
   public final BooleanSetting edit = this.add((new BooleanSetting("Edit", false)).injectTask(this::openGui));
   private static final ThreadLocal<class_2338.class_2339> EXPOSED_POS = ThreadLocal.withInitial(class_2338.class_2339::new);

   public Xray() {
      super("Xray", Module.Category.Render);
      this.setChinese("矿物透视");
      INSTANCE = this;
   }

   private void openGui() {
      this.edit.setValueWithoutTask(false);
      if (!nullCheck()) {
         mc.method_1507(new WindowsScreen(new WindowBase[]{new ItemSelectWindow(Astra.XRAY)}));
      }

   }

   public void onEnable() {
      if (!nullCheck()) {
         mc.field_1769.method_3279();
      }
   }

   public void onDisable() {
      mc.field_1769.method_3279();
   }

   public boolean isBlocked(class_2248 block) {
      return !Astra.XRAY.inWhitelist(block.method_9539());
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

   public static boolean shouldBlock(class_2680 state) {
      return INSTANCE.isOn() && INSTANCE.isBlocked(state.method_26204());
   }

   public boolean modifyDrawSide(class_2680 state, class_1922 view, class_2338 pos, class_2350 facing, boolean returns) {
      if (!returns && !this.isBlocked(state.method_26204())) {
         class_2338 adjPos = pos.method_10093(facing);
         class_2680 adjState = view.method_8320(adjPos);
         return adjState.method_26173(view, adjPos, facing.method_10153()) != class_259.method_1077() || adjState.method_26204() != state.method_26204() || isExposed(adjPos);
      } else {
         return returns;
      }
   }

   public static boolean isExposed(class_2338 blockPos) {
      for(class_2350 direction : class_2350.values()) {
         if (!mc.field_1687.method_8320(((class_2338.class_2339)EXPOSED_POS.get()).method_25505(blockPos, direction)).method_26225()) {
            return true;
         }
      }

      return false;
   }
}
