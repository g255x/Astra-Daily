package nekiplay.meteorplus.features.modules.misc;

import java.util.List;
import java.util.Locale;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.player.Reach;
import meteordevelopment.orbit.EventHandler;
import nekiplay.main.events.hud.DebugDrawTextEvent;
import nekiplay.meteorplus.mixinclasses.SpoofMode;
import nekiplay.meteorplus.settings.ConfigModifier;
import net.minecraft.class_124;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_4076;
import net.minecraft.class_239.class_240;

public class CordinateProtector {
   @EventHandler
   private void onDebugF3RenderText(DebugDrawTextEvent event) {
      List<String> lines = event.getLines();
      if ((Boolean)ConfigModifier.get().positionProtection.get()) {
         int index = 0;

         for(Object obj : lines.toArray()) {
            String str = obj.toString();
            if (str.startsWith("XYZ:")) {
               String xyz = String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", MeteorClient.mc.method_1560().method_23317() + (double)(Integer)ConfigModifier.get().x_spoof.get(), MeteorClient.mc.method_1560().method_23318(), MeteorClient.mc.method_1560().method_23321() + (double)(Integer)ConfigModifier.get().z_spoof.get());
               if (ConfigModifier.get().spoofMode.get() == SpoofMode.Fake) {
                  lines.set(index, xyz);
               } else if (ConfigModifier.get().spoofMode.get() == SpoofMode.Sensor) {
                  lines.set(index, "XYZ: *** / *** / ***");
               }
            } else if (str.startsWith("Block: ")) {
               class_2338 blockPos = MeteorClient.mc.method_1560().method_24515();
               blockPos = blockPos.method_10069((Integer)ConfigModifier.get().x_spoof.get(), 0, (Integer)ConfigModifier.get().z_spoof.get());
               String block = String.format(Locale.ROOT, "Block: %d %d %d", blockPos.method_10263(), blockPos.method_10264(), blockPos.method_10260());
               if (ConfigModifier.get().spoofMode.get() == SpoofMode.Fake) {
                  lines.set(index, block);
               } else if (ConfigModifier.get().spoofMode.get() == SpoofMode.Sensor) {
                  lines.set(index, "Block: *** *** ***");
               }
            } else if (str.startsWith("Chunk:")) {
               class_2338 blockPos = MeteorClient.mc.method_1560().method_24515();
               blockPos = blockPos.method_10069((Integer)ConfigModifier.get().x_spoof.get(), 0, (Integer)ConfigModifier.get().z_spoof.get());
               class_1923 chunkPos = new class_1923(blockPos);
               if (ConfigModifier.get().spoofMode.get() == SpoofMode.Fake) {
                  String chunk = String.format(Locale.ROOT, "Chunk: %d %d %d [%d %d in r.%d.%d.mca]", chunkPos.field_9181, class_4076.method_18675(blockPos.method_10264()), chunkPos.field_9180, chunkPos.method_17887(), chunkPos.method_17888(), chunkPos.method_17885(), chunkPos.method_17886());
                  lines.set(index, chunk);
               } else if (ConfigModifier.get().spoofMode.get() == SpoofMode.Sensor) {
                  lines.set(index, "Chunk: *** *** *** [*** *** in ***.***.mca]");
               }
            } else if (str.contains("Targeted Block:")) {
               class_239 blockHitResult = MeteorClient.mc.field_1724.method_5745(((Reach)Modules.get().get(Reach.class)).blockReach(), 1.0F, false);
               if (blockHitResult != null && blockHitResult.method_17783() == class_240.field_1332) {
                  class_124 var10001 = class_124.field_1073;
                  class_2338 blockPos = ((class_3965)blockHitResult).method_17777();
                  blockPos = blockPos.method_10069((Integer)ConfigModifier.get().x_spoof.get(), 0, (Integer)ConfigModifier.get().z_spoof.get());
                  if (ConfigModifier.get().spoofMode.get() == SpoofMode.Fake) {
                     String var10002 = String.valueOf(var10001);
                     lines.set(index, var10002 + "Targeted Block: " + blockPos.method_10263() + ", " + blockPos.method_10264() + ", " + blockPos.method_10260());
                  } else if (ConfigModifier.get().spoofMode.get() == SpoofMode.Sensor) {
                     lines.set(index, String.valueOf(var10001) + "Targeted Block: *** *** ***");
                  }
               }
            } else if (str.contains("Targeted Fluid:")) {
               class_239 blockHitResult = MeteorClient.mc.field_1724.method_5745(((Reach)Modules.get().get(Reach.class)).blockReach(), 1.0F, true);
               if (blockHitResult != null && blockHitResult.method_17783() == class_240.field_1332) {
                  class_124 var10001 = class_124.field_1073;
                  class_2338 blockPos = ((class_3965)blockHitResult).method_17777();
                  blockPos = blockPos.method_10069((Integer)ConfigModifier.get().x_spoof.get(), 0, (Integer)ConfigModifier.get().z_spoof.get());
                  if (ConfigModifier.get().spoofMode.get() == SpoofMode.Fake) {
                     String var25 = String.valueOf(var10001);
                     lines.set(index, var25 + "Targeted Fluid: " + blockPos.method_10263() + ", " + blockPos.method_10264() + ", " + blockPos.method_10260());
                  } else if (ConfigModifier.get().spoofMode.get() == SpoofMode.Sensor) {
                     lines.set(index, String.valueOf(var10001) + "Targeted Fluid: *** *** ***");
                  }
               }
            }

            ++index;
         }
      }

   }
}
