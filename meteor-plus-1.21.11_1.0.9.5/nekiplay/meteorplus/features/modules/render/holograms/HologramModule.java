package nekiplay.meteorplus.features.modules.render.holograms;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.render.NametagUtils;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.world.Dimension;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.MeteorPlusAddon;
import net.minecraft.class_1792;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import org.joml.Vector3d;
import org.meteordev.starscript.Script;

public class HologramModule extends Module {
   public Gson gson = new Gson();
   public List<HologramDataListed> allHolograms = new ArrayList();
   public List<HologramDataListed> inWorldHolograms = new ArrayList();
   public HashMap<String, Script> scripts = new HashMap();

   public HologramModule() {
      super(Categories.Render, "holograms", "Create own holograms");
   }

   public void onActivate() {
      super.onActivate();
      this.createDefault();
      this.load();
   }

   @EventHandler
   private void onTick(TickEvent.Post event) {
      this.inWorldHolograms.clear();
      Dimension dim = PlayerUtils.getDimension();

      for(HologramDataListed hologramData : this.allHolograms) {
         if (hologramData.dimension.equals(dim.name())) {
            this.inWorldHolograms.add(hologramData);
         }
      }

   }

   @EventHandler
   private void on2DRender(Render2DEvent event) {
      class_243 camera_pos = this.mc.field_1773.method_19418().method_71156();

      for(HologramDataListed hologramData : this.inWorldHolograms) {
         Vector3d pos = new Vector3d(hologramData.x, hologramData.y, hologramData.z);
         if (pos.distance(camera_pos.field_1352, camera_pos.field_1351, camera_pos.field_1350) <= hologramData.max_render_distance && NametagUtils.to2D(pos, hologramData.scale)) {
            TextRenderer text = TextRenderer.get();
            NametagUtils.begin(pos);
            String hologram_text = hologramData.text;
            double hologramWidth = text.getWidth(hologram_text, true);
            double heightDown = text.getHeight(true);
            double widthHalf = hologramWidth / (double)2.0F;
            double hX = -widthHalf;
            double hY = -heightDown;
            Script script = (Script)this.scripts.get(hologram_text);
            text.beginBig();
            if (script != null) {
               text.render(MeteorStarscript.run(script), hX, hY, hologramData.color, true);

               for(HologramData hologramData1 : hologramData.other_holograms) {
                  text.render(MeteorStarscript.run((Script)this.scripts.get(hologramData1.text)), hX - hologramData1.x, hY - hologramData1.y, hologramData1.color, true);
                  if (hologramData1.item_id != 0) {
                     class_1792 item = class_1792.method_7875(hologramData1.item_id);
                     RenderUtils.drawItem(event.drawContext, item.method_7854(), (int)((double)((int)hX) - hologramData1.x), (int)((double)0.0F - hologramData1.y), (float)hologramData1.item_scale, true);
                  }
               }

               if (hologramData.item_id != 0) {
                  class_1792 item = class_1792.method_7875(hologramData.item_id);
                  RenderUtils.drawItem(event.drawContext, item.method_7854(), (int)hX, 0, (float)hologramData.item_scale, true);
               }
            }

            text.end();
            NametagUtils.end();
         }
      }

   }

   private void loadScripts(HologramDataListed hologramDataListed) {
      if (!this.scripts.containsKey(hologramDataListed.text)) {
         this.scripts.put(hologramDataListed.text, MeteorStarscript.compile(hologramDataListed.text));
      }

      for(HologramData hologramData1 : hologramDataListed.other_holograms) {
         if (!this.scripts.containsKey(hologramData1.text)) {
            this.scripts.put(hologramData1.text, MeteorStarscript.compile(hologramData1.text));
         }
      }

   }

   private void load() {
      File dir = new File(MeteorClient.FOLDER, "holograms");
      if (dir.exists()) {
         String world_name = Utils.getWorldName();
         File dir2 = new File(dir, world_name);
         if (dir2.exists()) {
            this.allHolograms.clear();
            this.scripts.clear();
            File[] files = dir2.listFiles();
            if (files != null) {
               for(File file : files) {
                  if (file.exists()) {
                     MeteorPlusAddon.LOG.info("[Meteor+] Loading hologram: " + file.getName());

                     try {
                        BufferedReader reader = Files.newBufferedReader(Path.of(file.toURI()), StandardCharsets.UTF_8);

                        try {
                           String json = (String)reader.lines().collect(Collectors.joining());
                           HologramDataListed hologramData = (HologramDataListed)this.gson.fromJson(json, HologramDataListed.class);
                           if (hologramData != null) {
                              this.loadScripts(hologramData);
                              this.allHolograms.add(hologramData);
                              MeteorPlusAddon.LOG.info("[Meteor+] Success loaded hologram: " + file.getName());
                           }
                        } catch (Exception e) {
                           MeteorPlusAddon.LOG.error("[Meteor+] Error in hologram: " + String.valueOf(e));
                        }
                     } catch (IOException e) {
                        MeteorPlusAddon.LOG.error("[Meteor+] Error in hologram: " + String.valueOf(e));
                     }
                  }
               }
            }
         }
      }

   }

   private void createDefault() {
      File dir = new File(MeteorClient.FOLDER, "holograms");
      if (!dir.exists()) {
         dir.mkdir();
      }

      String world_name = Utils.getWorldName();
      File dir2 = new File(dir, world_name);
      if (!dir2.exists()) {
         dir2.mkdir();
         HologramDataListed hologramData = new HologramDataListed(new class_2338(0, 64, 0), "Spawn", PlayerUtils.getDimension(), Color.RED, (double)16.0F);
         HologramData hologramData2 = new HologramData(new class_2338(0, 15, 0), PlayerUtils.getDimension().name(), PlayerUtils.getDimension(), Color.RED, (double)16.0F);
         hologramData.other_holograms.add(hologramData2);
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
