package nekiplay.meteorplus.features.modules.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import meteordevelopment.meteorclient.events.entity.EntityAddedEvent;
import meteordevelopment.meteorclient.events.entity.EntityRemovedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.utils.ColorRemover;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2596;
import net.minecraft.class_2616;
import net.minecraft.class_2777;
import net.minecraft.class_640;

public class AntiBotPlus extends Module {
   private final SettingGroup sgFilters;
   private final Setting<Boolean> tab;
   private final Setting<TabMode> tabMode;
   private final Setting<Boolean> entityID;
   private final Setting<Boolean> color;
   private final Setting<Boolean> ground;
   private final Setting<Boolean> air;
   private final Setting<Boolean> InvalidGround;
   private final Setting<Boolean> swing;
   private final Setting<Boolean> health;
   private final Setting<Boolean> derp;
   private final Setting<Boolean> useHash;
   private ArrayList<UUID> hash;
   private ArrayList<Integer> swings;
   private ArrayList<Integer> grounds;
   private ArrayList<Integer> airs;
   private Map<Integer, Integer> invalidGrounds;

   public AntiBotPlus() {
      super(Categories.Combat, "Anti Bot", "Remove bots.");
      this.sgFilters = this.settings.createGroup("Filters");
      this.tab = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("tab")).description("check tab.")).defaultValue(true)).build());
      SettingGroup var10001 = this.sgFilters;
      EnumSetting.Builder var10002 = (EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("tab-mode")).description("check tab mode.")).defaultValue(AntiBotPlus.TabMode.Contains);
      Setting var10003 = this.tab;
      Objects.requireNonNull(var10003);
      this.tabMode = var10001.add(((EnumSetting.Builder)var10002.visible(var10003::get)).build());
      this.entityID = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("EntityID")).description("check entity id.")).defaultValue(true)).build());
      this.color = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Color")).description("check color.")).defaultValue(false)).build());
      this.ground = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("ground")).description("check ground.")).defaultValue(true)).build());
      this.air = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("air")).description("check air.")).defaultValue(false)).build());
      this.InvalidGround = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Invalid-Ground")).description("check Invalid Ground.")).defaultValue(true)).build());
      this.swing = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Swing")).description("check Swing.")).defaultValue(false)).build());
      this.health = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("health")).description("check health.")).defaultValue(false)).build());
      this.derp = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("derp")).description("check derp.")).defaultValue(true)).build());
      this.useHash = this.sgFilters.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Memorize bots")).description("Prevent attacking and blinking esp.")).defaultValue(true)).build());
      this.hash = new ArrayList();
      this.swings = new ArrayList();
      this.grounds = new ArrayList();
      this.airs = new ArrayList();
      this.invalidGrounds = new HashMap();
   }

   public void onDeactivate() {
      this.hash.clear();
   }

   public boolean isBot(class_1297 entity) {
      if (entity instanceof class_1309 living) {
         return this.isBot(living);
      } else {
         return false;
      }
   }

   public boolean isBot(class_1309 entity) {
      if (!(entity instanceof class_1657)) {
         return false;
      } else if (!this.isActive()) {
         return false;
      } else if ((Boolean)this.useHash.get() && this.hash.contains(entity.method_5667())) {
         return true;
      } else if ((Boolean)this.color.get() && entity.method_5476().getString().replace("§r", "").contains("§")) {
         if ((Boolean)this.useHash.get()) {
            this.hash.add(entity.method_5667());
         }

         return true;
      } else if ((Boolean)this.ground.get() && !this.grounds.contains(entity.method_5628())) {
         if ((Boolean)this.useHash.get()) {
            this.hash.add(entity.method_5667());
         }

         return true;
      } else if ((Boolean)this.InvalidGround.get() && (Integer)this.invalidGrounds.getOrDefault(entity.method_5628(), 0) >= 10) {
         if ((Boolean)this.useHash.get()) {
            this.hash.add(entity.method_5667());
         }

         return true;
      } else if (!(Boolean)this.entityID.get() || entity.method_5628() < 1000000000 && entity.method_5628() > -1) {
         if (!(Boolean)this.derp.get() || !(entity.method_36455() > 90.0F) && !(entity.method_36455() < -90.0F)) {
            if ((Boolean)this.swing.get() && !this.swings.contains(entity.method_5628())) {
               if ((Boolean)this.useHash.get()) {
                  this.hash.add(entity.method_5667());
               }

               return true;
            } else {
               if ((Boolean)this.tab.get()) {
                  String targetname = ColorRemover.GetVerbatim(entity.method_5476().getString());
                  if (this.mc != null && this.mc.method_1562() != null) {
                     for(class_640 info : this.mc.method_1562().method_2880()) {
                        if (info.method_2971() != null) {
                           String networkName = ColorRemover.GetVerbatim(info.method_2971().getString());
                           if (this.tabMode.get() == AntiBotPlus.TabMode.Equals) {
                              if (targetname.equals(networkName)) {
                                 return false;
                              }
                           } else if (this.tabMode.get() == AntiBotPlus.TabMode.Contains_LowerCase) {
                              if (targetname.toLowerCase().contains(networkName.toLowerCase())) {
                                 return false;
                              }
                           } else if (targetname.contains(networkName)) {
                              return false;
                           }
                        }
                     }

                     if ((Boolean)this.useHash.get()) {
                        this.hash.add(entity.method_5667());
                     }

                     return true;
                  }
               }

               return entity.method_5477().getString().isEmpty() || entity.method_5477() == this.mc.field_1724.method_5477();
            }
         } else {
            if ((Boolean)this.useHash.get()) {
               this.hash.add(entity.method_5667());
            }

            return true;
         }
      } else {
         if ((Boolean)this.useHash.get()) {
            this.hash.add(entity.method_5667());
         }

         return true;
      }
   }

   @EventHandler
   private void onEntityAdd(EntityAddedEvent event) {
      this.isBot(event.entity);
   }

   @EventHandler
   private void onEntityRemove(EntityRemovedEvent event) {
      if (this.hash.contains(event.entity.method_5667())) {
         Iterator<UUID> iterator = this.hash.iterator();

         while(iterator.hasNext()) {
            if (iterator.next() == event.entity.method_5667()) {
               iterator.remove();
               return;
            }
         }
      }

   }

   @EventHandler
   private void livingEntityMove(PacketEvent.Receive event) {
      class_2596 entity = event.packet;
      if (entity instanceof class_2777 packet) {
         if (this.mc.field_1687 != null) {
            class_1297 entity = this.mc.field_1687.method_8469(packet.comp_3237());
            if (entity != null) {
               if (entity.method_24828()) {
                  this.grounds.add(entity.method_5628());
               }

               if (!entity.method_24828() && !this.airs.contains(entity.method_5628())) {
                  this.airs.add(entity.method_5628());
               }

               if (entity.method_24828()) {
                  if (entity.field_6036 != entity.method_23318()) {
                     this.invalidGrounds.put(entity.method_5628(), (Integer)this.invalidGrounds.getOrDefault(entity.method_5628(), 0) + 1);
                  }
               } else {
                  int currentVL = (Integer)this.invalidGrounds.getOrDefault(entity.method_5628(), 0) / 2;
                  if (currentVL <= 0) {
                     this.invalidGrounds.remove(entity.method_5628());
                  } else {
                     this.invalidGrounds.replace(entity.method_5628(), currentVL);
                  }
               }
            }
         }
      } else {
         entity = event.packet;
         if (entity instanceof class_2616 packet) {
            if (this.mc.field_1687 != null) {
               class_1297 entity = this.mc.field_1687.method_8469(packet.method_11269());
               if (entity != null && entity instanceof class_1309 && packet.method_11267() == 0 && !this.swings.contains(entity.method_5628())) {
                  this.swings.add(entity.method_5628());
               }
            }
         }
      }

   }

   @EventHandler
   public void worldEvent(GameLeftEvent event) {
      this.clearAll();
   }

   private void clearAll() {
      this.swings.clear();
      this.grounds.clear();
      this.invalidGrounds.clear();
   }

   public static enum TabMode {
      Equals,
      Contains,
      Contains_LowerCase;

      public String toString() {
         return super.toString().replace('_', ' ');
      }

      // $FF: synthetic method
      private static TabMode[] $values() {
         return new TabMode[]{Equals, Contains, Contains_LowerCase};
      }
   }
}
