package dev.Astra.mod.modules.impl.render;

import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.ClientTickEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.ParticleEvent;
import dev.Astra.api.events.impl.RenderEntityEvent;
import dev.Astra.api.events.impl.TickEntityEvent;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import java.util.Objects;
import net.minecraft.class_1542;
import net.minecraft.class_434;
import net.minecraft.class_435;
import net.minecraft.class_5904;
import net.minecraft.class_677;
import net.minecraft.class_691;

public class NoRender extends Module {
   public static NoRender INSTANCE;
   public final BooleanSetting potionsIcon = this.add(new BooleanSetting("PotionsIcon", false));
   public final BooleanSetting invisible = this.add(new BooleanSetting("Invisible", true));
   public final BooleanSetting antiTitle = this.add(new BooleanSetting("Title", true));
   public final BooleanSetting weather = this.add(new BooleanSetting("Weather", true));
   public final BooleanSetting fog = this.add(new BooleanSetting("Fog", true));
   public final BooleanSetting fireOverlay = this.add(new BooleanSetting("FireOverlay", true));
   public final BooleanSetting waterOverlay = this.add(new BooleanSetting("WaterOverlay", true));
   public final BooleanSetting blockOverlay = this.add(new BooleanSetting("BlockOverlay", true));
   public final BooleanSetting fastItem = this.add((new BooleanSetting("2DItem", false)).setParent());
   public final BooleanSetting castShadow;
   public final BooleanSetting renderSidesOfItems;
   public final BooleanSetting item;
   public final BooleanSetting armorParts;
   public final BooleanSetting armorTrim;
   public final BooleanSetting fireEntity;
   public final BooleanSetting totem;
   public final BooleanSetting allParticles;
   public final BooleanSetting fireworks;
   public final BooleanSetting explosions;
   public final BooleanSetting portal;
   public final BooleanSetting nausea;
   public final BooleanSetting blindness;
   public final BooleanSetting guiToast;
   public final BooleanSetting noTerrainScreen;
   public final BooleanSetting lightsUpdate;

   public NoRender() {
      super("NoRender", Module.Category.Render);
      BooleanSetting var10006 = this.fastItem;
      Objects.requireNonNull(var10006);
      this.castShadow = this.add(new BooleanSetting("CastShadow", true, var10006::isOpen));
      var10006 = this.fastItem;
      Objects.requireNonNull(var10006);
      this.renderSidesOfItems = this.add(new BooleanSetting("RenderSidesOfItems", true, var10006::isOpen));
      this.item = this.add(new BooleanSetting("Items", false));
      this.armorParts = this.add(new BooleanSetting("ArmorParts", false));
      this.armorTrim = this.add(new BooleanSetting("ArmorTrim", false));
      this.fireEntity = this.add(new BooleanSetting("EntityFire", true));
      this.totem = this.add(new BooleanSetting("Totem", true));
      this.allParticles = this.add(new BooleanSetting("AllParticles", true));
      this.fireworks = this.add(new BooleanSetting("Fireworks", true));
      this.explosions = this.add(new BooleanSetting("Explosions", true));
      this.portal = this.add(new BooleanSetting("Portal", true));
      this.nausea = this.add(new BooleanSetting("Nausea", true));
      this.blindness = this.add(new BooleanSetting("Blindness", true));
      this.guiToast = this.add(new BooleanSetting("GuiToast", false));
      this.noTerrainScreen = this.add(new BooleanSetting("NoTerrainScreen", false));
      this.lightsUpdate = this.add(new BooleanSetting("LightsUpdate", false));
      this.setChinese("禁用渲染");
      INSTANCE = this;
   }

   @EventListener
   public void onPacketReceive(PacketEvent.Receive event) {
      if (event.getPacket() instanceof class_5904 && this.antiTitle.getValue()) {
         event.setCancelled(true);
      }

   }

   @EventListener
   public void onRender(TickEntityEvent event) {
      if (event.getEntity() instanceof class_1542 && this.item.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onRender(RenderEntityEvent event) {
      if (event.getEntity() instanceof class_1542 && this.item.getValue()) {
         event.cancel();
      }

   }

   @EventListener
   public void onParticle(ParticleEvent event) {
      if (this.allParticles.getValue()) {
         event.cancel();
      } else if (this.explosions.getValue() && event.particle instanceof class_691) {
         event.cancel();
      } else if (this.fireworks.getValue() && (event.particle instanceof class_677.class_681 || event.particle instanceof class_677.class_678)) {
         event.cancel();
      }

   }

   @EventListener
   public void onClientTick(ClientTickEvent event) {
      if (!nullCheck()) {
         if (this.noTerrainScreen.getValue() && (mc.field_1755 instanceof class_434 || mc.field_1755 instanceof class_435)) {
            mc.field_1755 = null;
         }

      }
   }
}
