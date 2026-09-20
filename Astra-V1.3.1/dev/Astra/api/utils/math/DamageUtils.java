package dev.Astra.api.utils.math;

import dev.Astra.Astra;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.combat.CombatUtil;
import dev.Astra.core.impl.PlayerManager;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.combat.Criticals;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.function.BiFunction;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1322;
import net.minecraft.class_1324;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_1922;
import net.minecraft.class_1927;
import net.minecraft.class_1934;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3483;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_5134;
import net.minecraft.class_5321;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_8103;
import net.minecraft.class_9285;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_9362;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2902.class_2903;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.apache.commons.lang3.mutable.MutableInt;

public class DamageUtils {
   public static final RaycastFactory HIT_FACTORY = (context, blockPos) -> {
      class_2680 blockState = Wrapper.mc.field_1687.method_8320(blockPos);
      return blockState.method_26204().method_9520() < 600.0F ? null : blockState.method_26220(Wrapper.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
   };

   public static float calculateDamage(class_2338 pos, class_1309 entity) {
      return explosionDamage(entity, (class_1309)null, new class_243((double)pos.method_10263() + (double)0.5F, (double)(pos.method_10264() + 1), (double)pos.method_10260() + (double)0.5F), 12.0F);
   }

   public static float calculateDamage(class_243 pos, class_1309 entity) {
      return explosionDamage(entity, (class_1309)null, pos, 12.0F);
   }

   public static float explosionDamage(class_1309 target, class_243 targetPos, class_238 targetBox, class_243 explosionPos, float power, RaycastFactory raycastFactory) {
      double modDistance = getDistance(targetPos.field_1352, targetPos.field_1351, targetPos.field_1350, explosionPos.field_1352, explosionPos.field_1351, explosionPos.field_1350);
      if (modDistance > (double)power) {
         return 0.0F;
      } else {
         double exposure = (double)getExposure(explosionPos, targetBox, raycastFactory);
         double impact = ((double)1.0F - modDistance / (double)power) * exposure;
         float damage = (float)((int)((impact * impact + impact) / (double)2.0F * (double)7.0F * (double)12.0F + (double)1.0F));
         return calculateReductionsExplosion(damage, target, Wrapper.mc.field_1687.method_48963().method_48807((class_1927)null));
      }
   }

   public static float anchorDamage(class_1309 target, class_1309 predict, class_243 anchor) {
      return overridingExplosionDamage(target, predict, anchor, 10.0F, class_2338.method_49638(anchor), class_2246.field_10124.method_9564());
   }

   public static float overridingExplosionDamage(class_1309 target, class_1309 predict, class_243 explosionPos, float power, class_2338 overridePos, class_2680 overrideState) {
      return explosionDamage(target, predict, explosionPos, power, getOverridingHitFactory(overridePos, overrideState));
   }

   private static float explosionDamage(class_1309 target, class_1309 predict, class_243 explosionPos, float power, RaycastFactory raycastFactory) {
      if (target == null) {
         return 0.0F;
      } else {
         class_1657 player;
         return target instanceof class_1657 && getGameMode(player = (class_1657)target) == class_1934.field_9220 ? 0.0F : explosionDamage(target, predict != null ? predict.method_19538() : target.method_19538(), predict != null ? predict.method_5829() : target.method_5829(), explosionPos, power, raycastFactory);
      }
   }

   public static float explosionDamage(class_1309 target, class_1309 predict, class_243 explosionPos, float power) {
      if (target == null) {
         return 0.0F;
      } else {
         class_1657 player;
         return target instanceof class_1657 && getGameMode(player = (class_1657)target) == class_1934.field_9220 ? 0.0F : explosionDamage(target, predict != null ? predict.method_19538() : target.method_19538(), predict != null ? predict.method_5829() : target.method_5829(), explosionPos, power, HIT_FACTORY);
      }
   }

   public static RaycastFactory getOverridingHitFactory(class_2338 overridePos, class_2680 overrideState) {
      return (context, blockPos) -> {
         class_2680 blockState;
         if (blockPos.equals(overridePos)) {
            blockState = overrideState;
         } else {
            blockState = Wrapper.mc.field_1687.method_8320(blockPos);
            if (blockState.method_26204().method_9520() < 600.0F) {
               return null;
            }
         }

         return blockState.method_26220(Wrapper.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
      };
   }

   public static float getAttackDamage(class_1309 attacker, class_1309 target) {
      float itemDamage = (float)attacker.method_45325(class_5134.field_23721);
      class_1282 damageSource;
      if (attacker instanceof class_1657 player) {
         damageSource = Wrapper.mc.field_1687.method_48963().method_48802(player);
      } else {
         damageSource = Wrapper.mc.field_1687.method_48963().method_48812(attacker);
      }

      class_1293 effect = attacker.method_6112(class_1294.field_5910);
      if (effect != null) {
         itemDamage += 3.0F * (float)(effect.method_5578() + 1);
      }

      float damage = modifyAttackDamage(attacker, target, attacker.method_59958(), damageSource, itemDamage);
      return calculateReductions(damage, target, damageSource);
   }

   public static float getAttackDamage(class_1309 attacker, class_1309 target, class_1799 weapon) {
      class_1324 original = attacker.method_5996(class_5134.field_23721);
      class_1324 copy = new class_1324(class_5134.field_23721, (o) -> {
      });
      copy.method_6192(original.method_6201());

      for(class_1322 modifier2 : original.method_6195()) {
         copy.method_26835(modifier2);
      }

      copy.method_6200(class_1792.field_8006);
      class_9285 attributeModifiers = (class_9285)weapon.method_57824(class_9334.field_49636);
      if (attributeModifiers != null) {
         attributeModifiers.method_57482(class_1304.field_6173, (entry, modifier) -> {
            if (entry == class_5134.field_23721) {
               copy.method_55696(modifier);
            }

         });
      }

      float itemDamage = (float)copy.method_6194();
      class_1282 damageSource;
      if (attacker instanceof class_1657 player) {
         damageSource = Wrapper.mc.field_1687.method_48963().method_48802(player);
      } else {
         damageSource = Wrapper.mc.field_1687.method_48963().method_48812(attacker);
      }

      float damage = modifyAttackDamage(attacker, target, weapon, damageSource, itemDamage);
      return calculateReductions(damage, target, damageSource);
   }

   private static float modifyAttackDamage(class_1309 attacker, class_1309 target, class_1799 weapon, class_1282 damageSource, float damage) {
      Object2IntOpenHashMap enchantments = new Object2IntOpenHashMap();
      getEnchantments(weapon, enchantments);
      float enchantDamage = 0.0F;
      int sharpness = getEnchantmentLevel((Object2IntMap)enchantments, class_1893.field_9118);
      if (sharpness > 0) {
         enchantDamage += 1.0F + 0.5F * (float)(sharpness - 1);
      }

      int baneOfArthropods;
      if ((baneOfArthropods = getEnchantmentLevel((Object2IntMap)enchantments, class_1893.field_9112)) > 0 && target.method_5864().method_20210(class_3483.field_48285)) {
         enchantDamage += 2.5F * (float)baneOfArthropods;
      }

      int impaling;
      if ((impaling = getEnchantmentLevel((Object2IntMap)enchantments, class_1893.field_9106)) > 0 && target.method_5864().method_20210(class_3483.field_48284)) {
         enchantDamage += 2.5F * (float)impaling;
      }

      int smite;
      if ((smite = getEnchantmentLevel((Object2IntMap)enchantments, class_1893.field_9123)) > 0 && target.method_5864().method_20210(class_3483.field_49931)) {
         enchantDamage += 2.5F * (float)smite;
      }

      if (attacker instanceof class_1657 playerEntity) {
         float charge = playerEntity.method_7261(0.5F);
         damage *= 0.2F + charge * charge * 0.8F;
         enchantDamage *= charge;
         class_1792 item2 = weapon.method_7909();
         class_9362 item;
         float bonusDamage;
         if (item2 instanceof class_9362 && (bonusDamage = (item = (class_9362)item2).method_58403(target, damage, damageSource)) > 0.0F) {
            int density = getEnchantmentLevel(weapon, class_1893.field_50157);
            if (density > 0) {
               bonusDamage += 0.5F * attacker.field_6017;
            }

            damage += bonusDamage;
         }

         if (charge > 0.9F && (attacker.field_6017 > 0.0F || attacker == Wrapper.mc.field_1724 && Criticals.INSTANCE.isOn() && !Criticals.INSTANCE.mode.is(Criticals.Mode.Ground) && (Wrapper.mc.field_1724.method_24828() || !Criticals.INSTANCE.onlyGround.getValue())) && (!attacker.method_24828() || attacker == Wrapper.mc.field_1724 && Criticals.INSTANCE.isOn() && !Criticals.INSTANCE.mode.is(Criticals.Mode.Ground)) && !attacker.method_6101() && !attacker.method_5799() && !attacker.method_6059(class_1294.field_5919) && !attacker.method_5765()) {
            damage *= 1.5F;
         }
      }

      return damage + enchantDamage;
   }

   public static float fallDamage(class_1309 entity) {
      if (entity instanceof class_1657 player) {
         if (player.method_31549().field_7479) {
            return 0.0F;
         }
      }

      if (!entity.method_6059(class_1294.field_5906) && !entity.method_6059(class_1294.field_5902)) {
         int surface = Wrapper.mc.field_1687.method_8500(entity.method_24515()).method_12032(class_2903.field_13197).method_12603(entity.method_31477() & 15, entity.method_31479() & 15);
         if (entity.method_31478() >= surface) {
            return fallDamageReductions(entity, surface);
         } else {
            class_3965 raycastResult = Wrapper.mc.field_1687.method_17742(new class_3959(entity.method_19538(), new class_243(entity.method_23317(), (double)Wrapper.mc.field_1687.method_31607(), entity.method_23321()), class_3960.field_17558, class_242.field_36338, entity));
            return raycastResult.method_17783() == class_240.field_1333 ? 0.0F : fallDamageReductions(entity, raycastResult.method_17777().method_10264());
         }
      } else {
         return 0.0F;
      }
   }

   private static float fallDamageReductions(class_1309 entity, int surface) {
      int fallHeight = (int)(entity.method_23318() - (double)surface + (double)entity.field_6017 - (double)3.0F);
      class_1293 jumpBoostInstance = entity.method_6112(class_1294.field_5913);
      if (jumpBoostInstance != null) {
         fallHeight -= jumpBoostInstance.method_5578() + 1;
      }

      return calculateReductions((float)fallHeight, entity, Wrapper.mc.field_1687.method_48963().method_48827());
   }

   public static float calculateReductionsExplosion(float damage, class_1309 entity, class_1282 damageSource) {
      if (damageSource.method_5514()) {
         switch (Wrapper.mc.field_1687.method_8407()) {
            case field_5805 -> damage = Math.min(damage / 2.0F + 1.0F, damage);
            case field_5807 -> damage *= 1.5F;
         }
      }

      damage = class_1280.method_5496(entity, damage, damageSource, getArmor(entity), (float)getARMOR_TOUGHNESS(entity));
      damage = resistanceReduction(entity, damage);
      damage = class_1280.method_5497(damage, (float)getProtectionAmount(entity.method_5661()));
      return Math.max(damage, 0.0F);
   }

   public static float calculateReductions(float damage, class_1309 entity, class_1282 damageSource) {
      if (damageSource.method_5514()) {
         switch (Wrapper.mc.field_1687.method_8407()) {
            case field_5805 -> damage = Math.min(damage / 2.0F + 1.0F, damage);
            case field_5807 -> damage *= 1.5F;
         }
      }

      damage = class_1280.method_5496(entity, damage, damageSource, getArmor(entity), (float)getARMOR_TOUGHNESS(entity));
      damage = resistanceReduction(entity, damage);
      damage = protectionReduction(entity, damage, damageSource);
      return Math.max(damage, 0.0F);
   }

   public static double getARMOR_TOUGHNESS(class_1309 entity) {
      class_1657 player;
      PlayerManager.EntityAttribute entityAttribute;
      return entity instanceof class_1657 && (entityAttribute = (PlayerManager.EntityAttribute)Astra.PLAYER.map.get(player = (class_1657)entity)) != null ? entityAttribute.toughness() : entity.method_45325(class_5134.field_23725);
   }

   private static float getArmor(class_1309 entity) {
      class_1657 player;
      PlayerManager.EntityAttribute entityAttribute;
      return entity instanceof class_1657 && (entityAttribute = (PlayerManager.EntityAttribute)Astra.PLAYER.map.get(player = (class_1657)entity)) != null ? (float)entityAttribute.armor() : (float)Math.floor(entity.method_45325(class_5134.field_23724));
   }

   private static float protectionReduction(class_1309 player, float damage, class_1282 source) {
      if (source.method_48789(class_8103.field_42242)) {
         return damage;
      } else {
         int damageProtection = 0;

         for(class_1799 stack : player.method_56674()) {
            Object2IntOpenHashMap enchantments = new Object2IntOpenHashMap();
            getEnchantments(stack, enchantments);
            int protection = getEnchantmentLevel((Object2IntMap)enchantments, class_1893.field_9111);
            if (protection > 0) {
               damageProtection += protection;
            }

            int fireProtection;
            if ((fireProtection = getEnchantmentLevel((Object2IntMap)enchantments, class_1893.field_9095)) > 0 && source.method_48789(class_8103.field_42246)) {
               damageProtection += 2 * fireProtection;
            }

            int blastProtection;
            if ((blastProtection = getEnchantmentLevel((Object2IntMap)enchantments, class_1893.field_9107)) > 0 && source.method_48789(class_8103.field_42249)) {
               damageProtection += 2 * blastProtection;
            }

            int projectileProtection;
            if ((projectileProtection = getEnchantmentLevel((Object2IntMap)enchantments, class_1893.field_9096)) > 0 && source.method_48789(class_8103.field_42247)) {
               damageProtection += 2 * projectileProtection;
            }

            int featherFalling;
            if ((featherFalling = getEnchantmentLevel((Object2IntMap)enchantments, class_1893.field_9129)) > 0 && source.method_48789(class_8103.field_42250)) {
               damageProtection += 3 * featherFalling;
            }
         }

         return class_1280.method_5497(damage, (float)damageProtection);
      }
   }

   public static int getProtectionAmount(Iterable<class_1799> equipment) {
      MutableInt mutableInt = new MutableInt();
      equipment.forEach((i) -> mutableInt.add(getProtectionAmount(i)));
      return mutableInt.intValue();
   }

   public static int getProtectionAmount(class_1799 stack) {
      int modifierBlast = class_1890.method_8225((class_6880)Wrapper.mc.field_1687.method_30349().method_30530(class_1893.field_9107.method_58273()).method_40264(class_1893.field_9107).get(), stack);
      int modifier = class_1890.method_8225((class_6880)Wrapper.mc.field_1687.method_30349().method_30530(class_1893.field_9111.method_58273()).method_40264(class_1893.field_9111).get(), stack);
      return modifierBlast * 2 + modifier;
   }

   private static float resistanceReduction(class_1309 player, float damage) {
      class_1293 resistance = player.method_6112(class_1294.field_5907);
      if (resistance != null) {
         int lvl = resistance.method_5578() + 1;
         damage *= 1.0F - (float)lvl * 0.2F;
      }

      return Math.max(damage, 0.0F);
   }

   private static float getExposure(class_243 source, class_238 box, RaycastFactory raycastFactory) {
      if (ClientSetting.INSTANCE.optimizedCalc.getValue()) {
         int miss = 0;
         int hit = 0;

         for(int k = 0; k <= 1; ++k) {
            for(int l = 0; l <= 1; ++l) {
               for(int m = 0; m <= 1; ++m) {
                  double n = class_3532.method_16436((double)k, box.field_1323, box.field_1320);
                  class_243 vec3d = new class_243(n, class_3532.method_16436((double)l, box.field_1322, box.field_1325), class_3532.method_16436((double)m, box.field_1321, box.field_1324));
                  if (raycast(vec3d, source, CombatUtil.terrainIgnore) == class_240.field_1333) {
                     ++miss;
                  }

                  ++hit;
               }
            }
         }

         return (float)miss / (float)hit;
      } else {
         double xDiff = box.field_1320 - box.field_1323;
         double yDiff = box.field_1325 - box.field_1322;
         double zDiff = box.field_1324 - box.field_1321;
         double xStep = (double)1.0F / (xDiff * (double)2.0F + (double)1.0F);
         double yStep = (double)1.0F / (yDiff * (double)2.0F + (double)1.0F);
         double zStep = (double)1.0F / (zDiff * (double)2.0F + (double)1.0F);
         if (xStep > (double)0.0F && yStep > (double)0.0F && zStep > (double)0.0F) {
            int misses = 0;
            int hits = 0;
            double xOffset = ((double)1.0F - Math.floor((double)1.0F / xStep) * xStep) * (double)0.5F;
            double zOffset = ((double)1.0F - Math.floor((double)1.0F / zStep) * zStep) * (double)0.5F;
            xStep *= xDiff;
            yStep *= yDiff;
            zStep *= zDiff;
            double startX = box.field_1323 + xOffset;
            double startY = box.field_1322;
            double startZ = box.field_1321 + zOffset;
            double endX = box.field_1320 + xOffset;
            double endY = box.field_1325;
            double endZ = box.field_1324 + zOffset;

            for(double x = startX; x <= endX; x += xStep) {
               for(double y = startY; y <= endY; y += yStep) {
                  for(double z = startZ; z <= endZ; z += zStep) {
                     class_243 position = new class_243(x, y, z);
                     if (raycast(new ExposureRaycastContext(position, source), raycastFactory) == null) {
                        ++misses;
                     }

                     ++hits;
                  }
               }
            }

            return (float)misses / (float)hits;
         } else {
            return 0.0F;
         }
      }
   }

   public static class_239.class_240 raycast(class_243 start, class_243 end, boolean ignoreTerrain) {
      return (class_239.class_240)class_1922.method_17744(start, end, (Object)null, (innerContext, blockPos) -> {
         class_2680 blockState = Wrapper.mc.field_1687.method_8320(blockPos);
         if (blockState.method_26204().method_9520() < 600.0F && ignoreTerrain) {
            return null;
         } else {
            class_3965 hitResult = blockState.method_26220(Wrapper.mc.field_1687, blockPos).method_1092(start, end, blockPos);
            return hitResult == null ? null : hitResult.method_17783();
         }
      }, (innerContext) -> class_240.field_1333);
   }

   public static class_3965 raycast(ExposureRaycastContext context, RaycastFactory raycastFactory) {
      return (class_3965)class_1922.method_17744(context.start(), context.end(), context, raycastFactory, (ctx) -> null);
   }

   public static int getEnchantmentLevel(class_1799 itemStack, class_5321<class_1887> enchantment) {
      if (itemStack.method_7960()) {
         return 0;
      } else {
         Object2IntArrayMap itemEnchantments = new Object2IntArrayMap();
         getEnchantments(itemStack, itemEnchantments);
         return getEnchantmentLevel((Object2IntMap)itemEnchantments, enchantment);
      }
   }

   public static int getEnchantmentLevel(Object2IntMap<class_6880<class_1887>> itemEnchantments, class_5321<class_1887> enchantment) {
      ObjectIterator var2 = Object2IntMaps.fastIterable(itemEnchantments).iterator();

      while(var2.hasNext()) {
         Object2IntMap.Entry entry = (Object2IntMap.Entry)var2.next();
         if (((class_6880)entry.getKey()).method_40225(enchantment)) {
            return entry.getIntValue();
         }
      }

      return 0;
   }

   public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
      return Math.sqrt(squaredDistance(x1, y1, z1, x2, y2, z2));
   }

   public static class_1934 getGameMode(class_1657 player) {
      if (player == null) {
         return null;
      } else {
         class_640 playerListEntry = Wrapper.mc.method_1562().method_2871(player.method_5667());
         return playerListEntry == null ? null : playerListEntry.method_2958();
      }
   }

   public static double squaredDistanceTo(class_1297 entity) {
      return squaredDistanceTo(entity.method_23317(), entity.method_23318(), entity.method_23321());
   }

   public static double squaredDistanceTo(class_2338 blockPos) {
      return squaredDistanceTo((double)blockPos.method_10263(), (double)blockPos.method_10264(), (double)blockPos.method_10260());
   }

   public static double squaredDistanceTo(double x, double y, double z) {
      return squaredDistance(Wrapper.mc.field_1724.method_23317(), Wrapper.mc.field_1724.method_23318(), Wrapper.mc.field_1724.method_23321(), x, y, z);
   }

   public static double squaredDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
      double f = x1 - x2;
      double g = y1 - y2;
      double h = z1 - z2;
      return org.joml.Math.fma(f, f, org.joml.Math.fma(g, g, h * h));
   }

   public static void getEnchantments(class_1799 itemStack, Object2IntMap<class_6880<class_1887>> enchantments) {
      enchantments.clear();
      if (!itemStack.method_7960()) {
         for(Object2IntMap.Entry<class_6880<class_1887>> entry : itemStack.method_7909() == class_1802.field_8598 ? ((class_9304)itemStack.method_57824(class_9334.field_49643)).method_57539() : itemStack.method_58657().method_57539()) {
            enchantments.put((class_6880)entry.getKey(), entry.getIntValue());
         }
      }

   }

   public static record ExposureRaycastContext(class_243 start, class_243 end) {
   }

   @FunctionalInterface
   public interface RaycastFactory extends BiFunction<ExposureRaycastContext, class_2338, class_3965> {
   }
}
