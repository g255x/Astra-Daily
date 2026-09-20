package nekiplay.meteorplus.features.modules.world.customblocks;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Collectors;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.world.BlockUpdateEvent;
import meteordevelopment.meteorclient.events.world.ChunkDataEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.MeteorPlusAddon;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2791;
import net.minecraft.class_2818;
import net.minecraft.class_2902.class_2903;

public class CustomBlocksModule extends Module {
   public Gson gson = new Gson();
   public HashMap<PosData, CustomBlockData> allBlocks = new HashMap();
   private final SettingGroup settingsGroup;
   public final Setting<Boolean> noSetIfAir;

   public CustomBlocksModule() {
      super(Categories.World, "Custom Blocks", "Place custom blocks in world, extension for Holograms");
      this.settingsGroup = this.settings.getDefaultGroup();
      this.noSetIfAir = this.settingsGroup.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("no-set-if-air")).description("No set custom block if original block equals air.")).defaultValue(true)).build());
   }

   public void onActivate() {
      super.onActivate();
      this.createDefault();
      this.load();

      for(class_2791 chunk : Utils.chunks()) {
         this.updateChunkData(chunk);
      }

   }

   private void updateChunkData(class_2791 chunk) {
      String dimension = PlayerUtils.getDimension().name();

      for(int x = chunk.method_12004().method_8326(); x <= chunk.method_12004().method_8327(); ++x) {
         for(int z = chunk.method_12004().method_8328(); z <= chunk.method_12004().method_8329(); ++z) {
            int height = chunk.method_12032(class_2903.field_13202).method_12603(x - chunk.method_12004().method_8326(), z - chunk.method_12004().method_8328());

            for(int y = this.mc.field_1687.method_31607(); y < height; ++y) {
               class_2338 pos = new class_2338(x, y, z);
               class_2680 bs = chunk.method_8320(pos);
               class_2248 original = bs.method_26204();
               CustomBlockData customBlockData = (CustomBlockData)this.allBlocks.get(new PosData(pos));
               if (customBlockData != null && customBlockData.dimension.equals(dimension) && this.isValidBlockForReplace(original)) {
                  this.setBlock(pos, customBlockData);
               }
            }
         }
      }

   }

   @EventHandler
   private void onChunkData(ChunkDataEvent event) {
      class_2818 chunk = event.chunk();
      this.updateChunkData(chunk);
   }

   private boolean isValidBlockForReplace(class_2248 block) {
      if (block != class_2246.field_10124 && (Boolean)this.noSetIfAir.get()) {
         return true;
      } else {
         return !(Boolean)this.noSetIfAir.get();
      }
   }

   private void setBlock(class_2338 pos, CustomBlockData data) {
      class_2680 block = class_2248.method_9531(data.block_id);
      this.mc.field_1687.method_8501(pos, block);
   }

   @EventHandler
   private void onBlockUpdate(BlockUpdateEvent event) {
      String dimension = PlayerUtils.getDimension().name();
      CustomBlockData customBlockData = (CustomBlockData)this.allBlocks.get(new PosData(event.pos));
      if (customBlockData != null && customBlockData.dimension.equals(dimension)) {
         class_2248 original = event.newState.method_26204();
         if (this.isValidBlockForReplace(original)) {
            this.setBlock(event.pos, customBlockData);
         }
      }

   }

   private void load() {
      File dir = new File(MeteorClient.FOLDER, "customblocks");
      if (dir.exists()) {
         String world_name = Utils.getWorldName();
         File dir2 = new File(dir, world_name);
         if (dir2.exists()) {
            this.allBlocks.clear();
            File[] files = dir2.listFiles();
            if (files != null) {
               for(File file : files) {
                  if (file.exists()) {
                     MeteorPlusAddon.LOG.info("[Meteor+] Loading hologram: " + file.getName());

                     try {
                        BufferedReader reader = Files.newBufferedReader(Path.of(file.toURI()), StandardCharsets.UTF_8);

                        try {
                           String json = (String)reader.lines().collect(Collectors.joining());
                           CustomBlockData hologramData = (CustomBlockData)this.gson.fromJson(json, CustomBlockData.class);
                           if (hologramData != null) {
                              for(PosData posData : hologramData.positions) {
                                 this.allBlocks.put(posData, hologramData);
                              }

                              MeteorPlusAddon.LOG.info("[Meteor+] Success loaded custom block: " + file.getName());
                           }
                        } catch (JsonSyntaxException e) {
                           MeteorPlusAddon.LOG.error("[Meteor+] Error in custom block: " + String.valueOf(e));
                        }
                     } catch (IOException e) {
                        MeteorPlusAddon.LOG.error("[Meteor+] Error in custom block: " + String.valueOf(e));
                     }
                  }
               }
            }
         }
      }

   }

   private void createDefault() {
      File dir = new File(MeteorClient.FOLDER, "customblocks");
      if (!dir.exists()) {
         dir.mkdir();
      }

      String world_name = Utils.getWorldName();
      File dir2 = new File(dir, world_name);
      if (!dir2.exists()) {
         dir2.mkdir();
         CustomBlockData hologramData = new CustomBlockData(new class_2338(0, 64, 0), 1);
         hologramData.dimension = PlayerUtils.getDimension().name();
         hologramData.world = world_name;
         String json = this.gson.toJson(hologramData);
         File file = new File(dir2.getPath(), "0.json");
         if (!file.exists()) {
            try {
               file.createNewFile();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }

         try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(json);
            printWriter.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

   }
}
