package dev.Astra.api.utils.combat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1291;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_6880;

public class PotionTracker {
   private static final Map<Integer, List<PotionEffectInfo>> PLAYER_EFFECTS = new ConcurrentHashMap();

   public static void addEffect(int playerId, class_6880<class_1291> effect, int level, long durationMs) {
      if (durationMs > 0L) {
         long expireTime = System.currentTimeMillis() + durationMs;
         List<PotionEffectInfo> list = (List)PLAYER_EFFECTS.computeIfAbsent(playerId, (k) -> new CopyOnWriteArrayList());
         list.removeIf((info) -> info.effect.equals(effect));
         list.add(new PotionEffectInfo(effect, level, expireTime));
      }
   }

   public static int getStrengthLevel(class_1657 player) {
      if (player == null) {
         return 0;
      } else {
         List<PotionEffectInfo> effects = (List)PLAYER_EFFECTS.get(player.method_5628());
         if (effects != null && !effects.isEmpty()) {
            long now = System.currentTimeMillis();

            for(PotionEffectInfo info : effects) {
               if (info.expireTime >= now && info.effect.method_55838(class_1294.field_5910)) {
                  return info.level;
               }
            }

            return 0;
         } else {
            return 0;
         }
      }
   }

   public static int getResistanceLevel(class_1657 player) {
      if (player == null) {
         return 0;
      } else {
         List<PotionEffectInfo> effects = (List)PLAYER_EFFECTS.get(player.method_5628());
         if (effects != null && !effects.isEmpty()) {
            long now = System.currentTimeMillis();

            for(PotionEffectInfo info : effects) {
               if (info.expireTime >= now && info.effect.method_55838(class_1294.field_5907)) {
                  return info.level;
               }
            }

            return 0;
         } else {
            return 0;
         }
      }
   }

   public static long getStrengthRemainingSeconds(class_1657 player) {
      if (player == null) {
         return 0L;
      } else {
         List<PotionEffectInfo> effects = (List)PLAYER_EFFECTS.get(player.method_5628());
         if (effects != null && !effects.isEmpty()) {
            long now = System.currentTimeMillis();

            for(PotionEffectInfo info : effects) {
               if (info.expireTime >= now && info.effect.method_55838(class_1294.field_5910)) {
                  return (info.expireTime - now) / 1000L;
               }
            }

            return 0L;
         } else {
            return 0L;
         }
      }
   }

   public static long getResistanceRemainingSeconds(class_1657 player) {
      if (player == null) {
         return 0L;
      } else {
         List<PotionEffectInfo> effects = (List)PLAYER_EFFECTS.get(player.method_5628());
         if (effects != null && !effects.isEmpty()) {
            long now = System.currentTimeMillis();

            for(PotionEffectInfo info : effects) {
               if (info.expireTime >= now && info.effect.method_55838(class_1294.field_5907)) {
                  return (info.expireTime - now) / 1000L;
               }
            }

            return 0L;
         } else {
            return 0L;
         }
      }
   }

   public static boolean hasStrength(class_1657 player) {
      return getStrengthLevel(player) > 0;
   }

   public static boolean hasResistance(class_1657 player) {
      return getResistanceLevel(player) > 0;
   }

   public static void tick() {
      long now = System.currentTimeMillis();
      PLAYER_EFFECTS.values().forEach((list) -> list.removeIf((info) -> info.expireTime < now));
      PLAYER_EFFECTS.entrySet().removeIf((entry) -> ((List)entry.getValue()).isEmpty());
   }

   public static void clear() {
      PLAYER_EFFECTS.clear();
   }

   public static void clearPlayer(int playerId) {
      PLAYER_EFFECTS.remove(playerId);
   }

   public static Map<Integer, List<PotionEffectInfo>> getEffects() {
      return PLAYER_EFFECTS;
   }

   private static class PotionEffectInfo {
      public class_6880<class_1291> effect;
      public int level;
      public long expireTime;

      public PotionEffectInfo(class_6880<class_1291> effect, int level, long expireTime) {
         this.effect = effect;
         this.level = level;
         this.expireTime = expireTime;
      }
   }
}
