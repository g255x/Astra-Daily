package nekiplay.meteorplus.settings;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.mixin.ClientPlayerInteractionManagerAccessor;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.config.Config;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.HorizontalDirection;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.world.Dimension;
import nekiplay.meteorplus.features.modules.world.timer.TimerPlus;
import nekiplay.meteorplus.mixinclasses.SpoofMode;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1299;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1959;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2378;
import net.minecraft.class_243;
import net.minecraft.class_2799;
import net.minecraft.class_2960;
import net.minecraft.class_3445;
import net.minecraft.class_3468;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import net.minecraft.class_2799.class_2800;
import org.apache.commons.lang3.StringUtils;
import org.meteordev.starscript.Starscript;
import org.meteordev.starscript.value.Value;
import org.meteordev.starscript.value.ValueMap;

public class ConfigModifier {
   private static ConfigModifier INSTANCE;
   public static final SettingGroup sgMeteorPlus;
   public final Setting<Boolean> positionProtection;
   public final Setting<SpoofMode> spoofMode;
   public final Setting<Integer> x_spoof;
   public final Setting<Integer> z_spoof;
   private static final class_2338.class_2339 BP;
   private static long lastRequestedStatsTime;

   public static ConfigModifier get() {
      if (INSTANCE == null) {
         INSTANCE = new ConfigModifier();
      }

      return INSTANCE;
   }

   public ConfigModifier() {
      this.positionProtection = sgMeteorPlus.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("position-protection")).description("Set fake position in F3 and in mods.")).defaultValue(false)).onChanged((a) -> this.changedProtection())).build());
      SettingGroup var10001 = sgMeteorPlus;
      EnumSetting.Builder var10002 = (EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("protection-mode")).defaultValue(SpoofMode.Sensor);
      Setting var10003 = this.positionProtection;
      Objects.requireNonNull(var10003);
      this.spoofMode = var10001.add(((EnumSetting.Builder)((EnumSetting.Builder)var10002.visible(var10003::get)).onChanged((a) -> this.changedProtection())).build());
      this.x_spoof = sgMeteorPlus.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("x-add")).defaultValue(100000)).visible(() -> (Boolean)this.positionProtection.get() && this.spoofMode.get() == SpoofMode.Fake)).build());
      this.z_spoof = sgMeteorPlus.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("z-add")).defaultValue(100000)).visible(() -> (Boolean)this.positionProtection.get() && this.spoofMode.get() == SpoofMode.Fake)).build());
      Starscript ss = MeteorStarscript.ss;
      ss.set("meteorplus", (new ValueMap()).set("modules", (new ValueMap()).set("timerplus", (new ValueMap()).set("charge", () -> Value.number(TimerPlus.getPercentage())))));
   }

   private void changedProtection() {
      Starscript ss = MeteorStarscript.ss;
      if ((Boolean)this.positionProtection.get()) {
         if (this.spoofMode.get() == SpoofMode.Fake) {
            ss.set("camera", (new ValueMap()).set("pos", (new ValueMap()).set("_toString", () -> this.posString(false, true)).set("x", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1352 + (double)(Integer)get().x_spoof.get())).set("y", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1351)).set("z", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1350 + (double)(Integer)get().z_spoof.get()))).set("opposite_dim_pos", (new ValueMap()).set("_toString", () -> this.posString(true, true)).set("x", () -> this.oppositeX(true)).set("y", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1351)).set("z", () -> this.oppositeZ(true))).set("yaw", () -> yaw(true)).set("pitch", () -> pitch(true)).set("direction", () -> direction(true)));
            ss.set("player", (new ValueMap()).set("_toString", () -> Value.string(MeteorClient.mc.method_1548().method_1676())).set("health", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.method_6032() : (double)0.0F)).set("absorption", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.method_6067() : (double)0.0F)).set("hunger", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.method_7344().method_7586() : (double)0.0F)).set("speed", () -> Value.number(Utils.getPlayerSpeed().method_37267())).set("speed_all", (new ValueMap()).set("_toString", () -> Value.string(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().toString() : "")).set("x", () -> Value.number(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().field_1352 : (double)0.0F)).set("y", () -> Value.number(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().field_1351 : (double)0.0F)).set("z", () -> Value.number(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().field_1350 : (double)0.0F))).set("breaking_progress", () -> Value.number(MeteorClient.mc.field_1761 != null ? (double)((ClientPlayerInteractionManagerAccessor)MeteorClient.mc.field_1761).meteor$getBreakingProgress() : (double)0.0F)).set("biome", ConfigModifier::biome).set("dimension", () -> Value.string(PlayerUtils.getDimension().name())).set("opposite_dimension", () -> Value.string(PlayerUtils.getDimension().opposite().name())).set("pos", (new ValueMap()).set("_toString", () -> this.posString(false, false)).set("x", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23317() + (double)(Integer)get().x_spoof.get() : (double)0.0F)).set("y", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23318() : (double)0.0F)).set("z", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23321() + (double)(Integer)get().z_spoof.get() : (double)0.0F))).set("opposite_dim_pos", (new ValueMap()).set("_toString", () -> this.posString(true, false)).set("x", () -> this.oppositeX(false)).set("y", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23318() : (double)0.0F)).set("z", () -> this.oppositeZ(false))).set("yaw", () -> yaw(false)).set("pitch", () -> pitch(false)).set("direction", () -> direction(false)).set("hand", () -> MeteorClient.mc.field_1724 != null ? MeteorStarscript.wrap(MeteorClient.mc.field_1724.method_6047()) : Value.null_()).set("offhand", () -> MeteorClient.mc.field_1724 != null ? MeteorStarscript.wrap(MeteorClient.mc.field_1724.method_6079()) : Value.null_()).set("hand_or_offhand", ConfigModifier::handOrOffhand).set("get_item", ConfigModifier::getItem).set("count_items", ConfigModifier::countItems).set("xp", (new ValueMap()).set("level", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.field_7520 : (double)0.0F)).set("progress", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.field_7510 : (double)0.0F)).set("total", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.field_7495 : (double)0.0F))).set("has_potion_effect", ConfigModifier::hasPotionEffect).set("get_potion_effect", ConfigModifier::getPotionEffect).set("get_stat", ConfigModifier::getStat));
         } else {
            ss.set("camera", (new ValueMap()).set("pos", (new ValueMap()).set("_toString", () -> this.posString(false, true)).set("x", () -> Value.number((double)0.0F)).set("y", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1351)).set("z", () -> Value.number((double)0.0F))).set("opposite_dim_pos", (new ValueMap()).set("_toString", () -> this.posString(true, true)).set("x", () -> Value.number((double)0.0F)).set("y", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1351)).set("z", () -> Value.number((double)0.0F))).set("yaw", () -> yaw(true)).set("pitch", () -> pitch(true)).set("direction", () -> direction(true)));
            ss.set("player", (new ValueMap()).set("_toString", () -> Value.string(MeteorClient.mc.method_1548().method_1676())).set("health", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.method_6032() : (double)0.0F)).set("absorption", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.method_6067() : (double)0.0F)).set("hunger", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.method_7344().method_7586() : (double)0.0F)).set("speed", () -> Value.number(Utils.getPlayerSpeed().method_37267())).set("speed_all", (new ValueMap()).set("_toString", () -> Value.string(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().toString() : "")).set("x", () -> Value.number((double)0.0F)).set("y", () -> Value.number(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().field_1351 : (double)0.0F)).set("z", () -> Value.number((double)0.0F))).set("breaking_progress", () -> Value.number(MeteorClient.mc.field_1761 != null ? (double)((ClientPlayerInteractionManagerAccessor)MeteorClient.mc.field_1761).meteor$getBreakingProgress() : (double)0.0F)).set("biome", ConfigModifier::biome).set("dimension", () -> Value.string(PlayerUtils.getDimension().name())).set("opposite_dimension", () -> Value.string(PlayerUtils.getDimension().opposite().name())).set("pos", (new ValueMap()).set("_toString", () -> this.posString(false, false)).set("x", () -> Value.number((double)0.0F)).set("y", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23318() : (double)0.0F)).set("z", () -> Value.number((double)0.0F))).set("opposite_dim_pos", (new ValueMap()).set("_toString", () -> this.posString(true, false)).set("x", () -> Value.number((double)0.0F)).set("y", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23318() : (double)0.0F)).set("z", () -> Value.number((double)0.0F))).set("yaw", () -> yaw(false)).set("pitch", () -> pitch(false)).set("direction", () -> direction(false)).set("hand", () -> MeteorClient.mc.field_1724 != null ? MeteorStarscript.wrap(MeteorClient.mc.field_1724.method_6047()) : Value.null_()).set("offhand", () -> MeteorClient.mc.field_1724 != null ? MeteorStarscript.wrap(MeteorClient.mc.field_1724.method_6079()) : Value.null_()).set("hand_or_offhand", ConfigModifier::handOrOffhand).set("get_item", ConfigModifier::getItem).set("count_items", ConfigModifier::countItems).set("xp", (new ValueMap()).set("level", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.field_7520 : (double)0.0F)).set("progress", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.field_7510 : (double)0.0F)).set("total", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.field_7495 : (double)0.0F))).set("has_potion_effect", ConfigModifier::hasPotionEffect).set("get_potion_effect", ConfigModifier::getPotionEffect).set("get_stat", ConfigModifier::getStat));
         }
      } else {
         ss.set("camera", (new ValueMap()).set("pos", (new ValueMap()).set("_toString", () -> this.posString(false, true)).set("x", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1352)).set("y", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1351)).set("z", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1350))).set("opposite_dim_pos", (new ValueMap()).set("_toString", () -> this.posString(true, true)).set("x", () -> this.oppositeX(true)).set("y", () -> Value.number(MeteorClient.mc.field_1773.method_19418().method_71156().field_1351)).set("z", () -> this.oppositeZ(true))).set("yaw", () -> yaw(true)).set("pitch", () -> pitch(true)).set("direction", () -> direction(true)));
         ss.set("player", (new ValueMap()).set("_toString", () -> Value.string(MeteorClient.mc.method_1548().method_1676())).set("health", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.method_6032() : (double)0.0F)).set("absorption", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.method_6067() : (double)0.0F)).set("hunger", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.method_7344().method_7586() : (double)0.0F)).set("speed", () -> Value.number(Utils.getPlayerSpeed().method_37267())).set("speed_all", (new ValueMap()).set("_toString", () -> Value.string(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().toString() : "")).set("x", () -> Value.number(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().field_1352 : (double)0.0F)).set("y", () -> Value.number(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().field_1351 : (double)0.0F)).set("z", () -> Value.number(MeteorClient.mc.field_1724 != null ? Utils.getPlayerSpeed().field_1350 : (double)0.0F))).set("breaking_progress", () -> Value.number(MeteorClient.mc.field_1761 != null ? (double)((ClientPlayerInteractionManagerAccessor)MeteorClient.mc.field_1761).meteor$getBreakingProgress() : (double)0.0F)).set("biome", ConfigModifier::biome).set("dimension", () -> Value.string(PlayerUtils.getDimension().name())).set("opposite_dimension", () -> Value.string(PlayerUtils.getDimension().opposite().name())).set("pos", (new ValueMap()).set("_toString", () -> this.posString(false, false)).set("x", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23317() : (double)0.0F)).set("y", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23318() : (double)0.0F)).set("z", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23321() : (double)0.0F))).set("opposite_dim_pos", (new ValueMap()).set("_toString", () -> this.posString(true, false)).set("x", () -> this.oppositeX(false)).set("y", () -> Value.number(MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23318() : (double)0.0F)).set("z", () -> this.oppositeZ(false))).set("yaw", () -> yaw(false)).set("pitch", () -> pitch(false)).set("direction", () -> direction(false)).set("hand", () -> MeteorClient.mc.field_1724 != null ? MeteorStarscript.wrap(MeteorClient.mc.field_1724.method_6047()) : Value.null_()).set("offhand", () -> MeteorClient.mc.field_1724 != null ? MeteorStarscript.wrap(MeteorClient.mc.field_1724.method_6079()) : Value.null_()).set("hand_or_offhand", ConfigModifier::handOrOffhand).set("get_item", ConfigModifier::getItem).set("count_items", ConfigModifier::countItems).set("xp", (new ValueMap()).set("level", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.field_7520 : (double)0.0F)).set("progress", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.field_7510 : (double)0.0F)).set("total", () -> Value.number(MeteorClient.mc.field_1724 != null ? (double)MeteorClient.mc.field_1724.field_7495 : (double)0.0F))).set("has_potion_effect", ConfigModifier::hasPotionEffect).set("get_potion_effect", ConfigModifier::getPotionEffect).set("get_stat", ConfigModifier::getStat));
      }

   }

   private Value oppositeX(boolean camera) {
      double x = (double)0.0F;
      if ((Boolean)this.positionProtection.get()) {
         x = camera ? MeteorClient.mc.field_1773.method_19418().method_71156().field_1352 + (double)(Integer)get().x_spoof.get() : (MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23317() + (double)(Integer)get().x_spoof.get() : (double)0.0F);
      } else {
         x = camera ? MeteorClient.mc.field_1773.method_19418().method_71156().field_1352 : (MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23317() : (double)0.0F);
      }

      Dimension dimension = PlayerUtils.getDimension();
      if (dimension == Dimension.Overworld) {
         x /= (double)8.0F;
      } else if (dimension == Dimension.Nether) {
         x *= (double)8.0F;
      }

      return Value.number(x);
   }

   private Value oppositeZ(boolean camera) {
      double z = (double)0.0F;
      if ((Boolean)this.positionProtection.get()) {
         z = camera ? MeteorClient.mc.field_1773.method_19418().method_71156().field_1350 + (double)(Integer)get().z_spoof.get() : (MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23321() + (double)(Integer)get().z_spoof.get() : (double)0.0F);
      } else {
         z = camera ? MeteorClient.mc.field_1773.method_19418().method_71156().field_1350 : (MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_23321() : (double)0.0F);
      }

      Dimension dimension = PlayerUtils.getDimension();
      if (dimension == Dimension.Overworld) {
         z /= (double)8.0F;
      } else if (dimension == Dimension.Nether) {
         z *= (double)8.0F;
      }

      return Value.number(z);
   }

   private static Value yaw(boolean camera) {
      float yaw;
      if (camera) {
         yaw = MeteorClient.mc.field_1773.method_19418().method_19330();
      } else {
         yaw = MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_36454() : 0.0F;
      }

      yaw %= 360.0F;
      if (yaw < 0.0F) {
         yaw += 360.0F;
      }

      if (yaw > 180.0F) {
         yaw -= 360.0F;
      }

      return Value.number((double)yaw);
   }

   private static Value pitch(boolean camera) {
      float pitch;
      if (camera) {
         pitch = MeteorClient.mc.field_1773.method_19418().method_19329();
      } else {
         pitch = MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_36455() : 0.0F;
      }

      pitch %= 360.0F;
      if (pitch < 0.0F) {
         pitch += 360.0F;
      }

      if (pitch > 180.0F) {
         pitch -= 360.0F;
      }

      return Value.number((double)pitch);
   }

   private static Value direction(boolean camera) {
      float yaw;
      if (camera) {
         yaw = MeteorClient.mc.field_1773.method_19418().method_19330();
      } else {
         yaw = MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_36454() : 0.0F;
      }

      return MeteorStarscript.wrap(HorizontalDirection.get(yaw));
   }

   private static Value biome() {
      if (MeteorClient.mc.field_1724 != null && MeteorClient.mc.field_1687 != null) {
         BP.method_10102(MeteorClient.mc.field_1724.method_23317(), MeteorClient.mc.field_1724.method_23318(), MeteorClient.mc.field_1724.method_23321());
         class_2960 id = ((class_2378)MeteorClient.mc.field_1687.method_30349().method_46759(class_7924.field_41236).get()).method_10221((class_1959)MeteorClient.mc.field_1687.method_23753(BP).comp_349());
         return id == null ? Value.string("Unknown") : Value.string((String)Arrays.stream(id.method_12832().split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" ")));
      } else {
         return Value.string("");
      }
   }

   private static Value handOrOffhand() {
      if (MeteorClient.mc.field_1724 == null) {
         return Value.null_();
      } else {
         class_1799 itemStack = MeteorClient.mc.field_1724.method_6047();
         if (itemStack.method_7960()) {
            itemStack = MeteorClient.mc.field_1724.method_6079();
         }

         return itemStack != null ? MeteorStarscript.wrap(itemStack) : Value.null_();
      }
   }

   private static Value ping() {
      if (MeteorClient.mc.method_1562() != null && MeteorClient.mc.field_1724 != null) {
         class_640 playerListEntry = MeteorClient.mc.method_1562().method_2871(MeteorClient.mc.field_1724.method_5667());
         return Value.number(playerListEntry != null ? (double)playerListEntry.method_2959() : (double)0.0F);
      } else {
         return Value.number((double)0.0F);
      }
   }

   private Value posString(boolean opposite, boolean camera) {
      class_243 pos;
      if (camera) {
         pos = MeteorClient.mc.field_1773.method_19418().method_71156();
      } else {
         pos = MeteorClient.mc.field_1724 != null ? MeteorClient.mc.field_1724.method_73189() : class_243.field_1353;
      }

      double x = pos.field_1352;
      double z = pos.field_1350;
      if (opposite) {
         Dimension dimension = PlayerUtils.getDimension();
         if (dimension == Dimension.Overworld) {
            x /= (double)8.0F;
            z /= (double)8.0F;
         } else if (dimension == Dimension.Nether) {
            x *= (double)8.0F;
            z *= (double)8.0F;
         }
      }

      return this.posString(x, pos.field_1351, z);
   }

   private Value posString(double x, double y, double z) {
      if ((Boolean)this.positionProtection.get()) {
         return this.spoofMode.get() == SpoofMode.Fake ? Value.string(String.format("X: %.0f Y: %.0f Z: %.0f", x + (double)(Integer)get().x_spoof.get(), y, z + (double)(Integer)get().z_spoof.get())) : Value.string(String.format("X: 0 Y: %.0f Z: 0", y));
      } else {
         return Value.string(String.format("X: %.0f Y: %.0f Z: %.0f", x, y, z));
      }
   }

   private static Value getItem(Starscript ss, int argCount) {
      if (argCount != 1) {
         ss.error("player.get_item() requires 1 argument, got %d.", new Object[]{argCount});
      }

      int i = (int)ss.popNumber("First argument to player.get_item() needs to be a number.");
      return MeteorClient.mc.field_1724 != null ? MeteorStarscript.wrap(MeteorClient.mc.field_1724.method_31548().method_5438(i)) : Value.null_();
   }

   private static Value countItems(Starscript ss, int argCount) {
      if (argCount != 1) {
         ss.error("player.count_items() requires 1 argument, got %d.", new Object[]{argCount});
      }

      String idRaw = ss.popString("First argument to player.count_items() needs to be a string.");
      class_2960 id = class_2960.method_12829(idRaw);
      if (id == null) {
         return Value.number((double)0.0F);
      } else {
         class_1792 item = (class_1792)class_7923.field_41178.method_63535(id);
         if (item != class_1802.field_8162 && MeteorClient.mc.field_1724 != null) {
            int count = 0;

            for(int i = 0; i < MeteorClient.mc.field_1724.method_31548().method_5439(); ++i) {
               class_1799 itemStack = MeteorClient.mc.field_1724.method_31548().method_5438(i);
               if (itemStack.method_7909() == item) {
                  count += itemStack.method_7947();
               }
            }

            return Value.number((double)count);
         } else {
            return Value.number((double)0.0F);
         }
      }
   }

   private static Value hasPotionEffect(Starscript ss, int argCount) {
      if (argCount < 1) {
         ss.error("player.has_potion_effect() requires 1 argument, got %d.", new Object[]{argCount});
      }

      if (MeteorClient.mc.field_1724 == null) {
         return Value.bool(false);
      } else {
         class_2960 name = MeteorStarscript.popIdentifier(ss, "First argument to player.has_potion_effect() needs to a string.");
         Optional<class_6880.class_6883<class_1291>> effect = class_7923.field_41174.method_10223(name);
         if (effect.isEmpty()) {
            return Value.null_();
         } else {
            class_1293 effectInstance = MeteorClient.mc.field_1724.method_6112((class_6880)effect.get());
            return Value.bool(effectInstance != null);
         }
      }
   }

   private static Value getPotionEffect(Starscript ss, int argCount) {
      if (argCount < 1) {
         ss.error("player.get_potion_effect() requires 1 argument, got %d.", new Object[]{argCount});
      }

      if (MeteorClient.mc.field_1724 == null) {
         return Value.null_();
      } else {
         class_2960 name = MeteorStarscript.popIdentifier(ss, "First argument to player.get_potion_effect() needs to a string.");
         Optional<class_6880.class_6883<class_1291>> effect = class_7923.field_41174.method_10223(name);
         if (effect.isEmpty()) {
            return Value.null_();
         } else {
            class_1293 effectInstance = MeteorClient.mc.field_1724.method_6112((class_6880)effect.get());
            return effectInstance == null ? Value.null_() : MeteorStarscript.wrap(effectInstance);
         }
      }
   }

   private static Value getStat(Starscript ss, int argCount) {
      if (argCount < 1) {
         ss.error("player.get_stat() requires 1 argument, got %d.", new Object[]{argCount});
      }

      if (MeteorClient.mc.field_1724 == null) {
         return Value.number((double)0.0F);
      } else {
         long time = System.currentTimeMillis();
         if ((double)(time - lastRequestedStatsTime) / (double)1000.0F >= (double)1.0F && MeteorClient.mc.method_1562() != null) {
            MeteorClient.mc.method_1562().method_52787(new class_2799(class_2800.field_12775));
            lastRequestedStatsTime = time;
         }

         String type = argCount > 1 ? ss.popString("First argument to player.get_stat() needs to be a string.") : "custom";
         class_2960 name = MeteorStarscript.popIdentifier(ss, (argCount > 1 ? "Second" : "First") + " argument to player.get_stat() needs to be a string.");
         class_3445 var10000;
         switch (type) {
            case "mined":
               var10000 = class_3468.field_15427.method_14956((class_2248)class_7923.field_41175.method_63535(name));
               break;
            case "crafted":
               var10000 = class_3468.field_15370.method_14956((class_1792)class_7923.field_41178.method_63535(name));
               break;
            case "used":
               var10000 = class_3468.field_15372.method_14956((class_1792)class_7923.field_41178.method_63535(name));
               break;
            case "broken":
               var10000 = class_3468.field_15383.method_14956((class_1792)class_7923.field_41178.method_63535(name));
               break;
            case "picked_up":
               var10000 = class_3468.field_15392.method_14956((class_1792)class_7923.field_41178.method_63535(name));
               break;
            case "dropped":
               var10000 = class_3468.field_15405.method_14956((class_1792)class_7923.field_41178.method_63535(name));
               break;
            case "killed":
               var10000 = class_3468.field_15403.method_14956((class_1299)class_7923.field_41177.method_63535(name));
               break;
            case "killed_by":
               var10000 = class_3468.field_15411.method_14956((class_1299)class_7923.field_41177.method_63535(name));
               break;
            case "custom":
               name = (class_2960)class_7923.field_41183.method_63535(name);
               var10000 = name != null ? class_3468.field_15419.method_14956(name) : null;
               break;
            default:
               var10000 = null;
         }

         class_3445<?> stat = var10000;
         return Value.number(stat != null ? (double)MeteorClient.mc.field_1724.method_3143().method_15025(stat) : (double)0.0F);
      }
   }

   static {
      sgMeteorPlus = Config.get().settings.createGroup("Meteor+");
      BP = new class_2338.class_2339();
      lastRequestedStatsTime = 0L;
   }
}
