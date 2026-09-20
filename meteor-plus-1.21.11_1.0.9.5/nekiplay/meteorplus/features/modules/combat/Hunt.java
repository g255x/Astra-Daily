package nekiplay.meteorplus.features.modules.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.pathing.PathManagers;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EntityTypeListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_6025;

public class Hunt extends Module {
   private final SettingGroup sgGeneral;
   private final Setting<Set<class_1299<?>>> entities;
   private final Setting<Boolean> onGround;
   private final Setting<Boolean> customCheck;
   private final ArrayList<class_1297> targets;

   public Hunt() {
      super(Categories.Combat, "Hunt", "Automatic walk to selected entities");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.entities = this.sgGeneral.add(((EntityTypeListSetting.Builder)((EntityTypeListSetting.Builder)(new EntityTypeListSetting.Builder()).name("entities")).description("Entities to attack.")).onlyAttackable().build());
      this.onGround = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("On ground only")).description("Attack entities on ground only")).defaultValue(false)).build());
      this.customCheck = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Custom on ground check")).description("Use a custom on ground check instead of the usual entity.isOnGround. Useful on some servers")).defaultValue(false)).build());
      this.targets = new ArrayList();
   }

   private boolean entityCheck(class_1297 entity) {
      if (!entity.equals(this.mc.field_1724) && !entity.equals(this.mc.method_1560())) {
         if ((!(entity instanceof class_1309) || !((class_1309)entity).method_29504()) && entity.method_5805()) {
            if (!((Set)this.entities.get()).contains(entity.method_5864())) {
               return false;
            } else {
               if (entity instanceof class_6025) {
                  class_6025 tameable = (class_6025)entity;
                  if (tameable.method_35057().method_5667() != null && tameable.method_35057().method_5667().equals(this.mc.field_1724.method_5667())) {
                     return false;
                  }
               }

               if (entity instanceof class_1657) {
                  class_1657 player = (class_1657)entity;
                  if (player.method_68878()) {
                     return false;
                  }

                  if (!Friends.get().shouldAttack(player)) {
                     return false;
                  }

                  AntiBotPlus antiBotPlus = (AntiBotPlus)Modules.get().get(AntiBotPlus.class);
                  Teams teams = (Teams)Modules.get().get(Teams.class);
                  if (antiBotPlus != null && antiBotPlus.isBot((class_1309)player)) {
                     return false;
                  }

                  if (teams != null && teams.isInYourTeam((class_1309)player)) {
                     return false;
                  }
               }

               if ((Boolean)this.onGround.get()) {
                  if (!(Boolean)this.customCheck.get()) {
                     return entity.method_24828();
                  } else {
                     class_1937 world = entity.method_73183();
                     class_243 entityPos = entity.method_73189();
                     class_2338 posBelow = new class_2338((int)entityPos.field_1352, (int)(entityPos.field_1351 - (double)1.0F), (int)entityPos.field_1350);
                     class_2248 blockBelow = world.method_8320(posBelow).method_26204();
                     return blockBelow != class_2246.field_10124 && blockBelow != class_2246.field_10382 && blockBelow != class_2246.field_10164;
                  }
               } else {
                  return true;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void onDeactivate() {
      this.targets.clear();
      PathManagers.get().stop();
   }

   @EventHandler
   private void onTickEvent(TickEvent.Pre event) {
      if (this.mc.field_1687 != null) {
         TargetUtils.getList(this.targets, this::entityCheck, SortPriority.LowestDistance, 25);
         Iterator var2 = this.targets.iterator();
         if (var2.hasNext()) {
            class_1297 entity = (class_1297)var2.next();
            PathManagers.get().moveTo(entity.method_24515());
            return;
         }
      }

   }
}
