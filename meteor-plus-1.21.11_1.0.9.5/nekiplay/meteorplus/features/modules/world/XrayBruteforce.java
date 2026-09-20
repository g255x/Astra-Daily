package nekiplay.meteorplus.features.modules.world;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.entity.player.BreakBlockEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.meteor.KeyEvent;
import meteordevelopment.meteorclient.events.meteor.MouseClickEvent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.BlockUpdateEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.BlockDataSetting;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.GenericSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.KeybindSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.render.blockesp.ESPBlock;
import meteordevelopment.meteorclient.systems.modules.render.blockesp.ESPBlockData;
import meteordevelopment.meteorclient.systems.modules.render.blockesp.ESPChunk;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.misc.UnorderedArrayList;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.RainbowColors;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.Dimension;
import meteordevelopment.meteorclient.utils.world.TickRate;
import meteordevelopment.orbit.EventHandler;
import nekiplay.meteorplus.utils.GenerationBlock;
import nekiplay.meteorplus.utils.xraybruteforce.XBlock;
import nekiplay.meteorplus.utils.xraybruteforce.XChunk;
import nekiplay.meteorplus.utils.xraybruteforce.XGroup;
import net.minecraft.class_1923;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2791;
import net.minecraft.class_2846;
import net.minecraft.class_634;
import net.minecraft.class_2846.class_2847;

public class XrayBruteforce extends Module {
   private final SettingGroup sgGeneral;
   private final SettingGroup sgSVisual;
   private final SettingGroup sgRVisual;
   private final SettingGroup sgSRenderer;
   private final SettingGroup sgSaver;
   private final SettingGroup sgDelayer;
   public final Setting<Boolean> autoSave;
   public final Setting<Boolean> save;
   public final Setting<Boolean> load;
   public final Setting<Boolean> new_render;
   private final Setting<List<class_2248>> whblocks;
   private final Setting<ESPBlockData> defaultBlockConfig;
   private final Setting<Map<class_2248, ESPBlockData>> blockConfigs;
   private final Setting<PacketMode> packet_first;
   private final Setting<PacketMode> packet_two;
   private final Setting<GenerationType> generationType;
   public final Setting<Boolean> clear_cache_blocks;
   public final Setting<Boolean> clear_cache_ores;
   public final Setting<Integer> rescanerDelay;
   public final Setting<Boolean> tps_sync;
   public final Setting<Boolean> fps_sync;
   public final Setting<Boolean> auto_height;
   public final Setting<Boolean> auto_dimension;
   public final Setting<Boolean> expanded;
   private final Setting<ScanPriority> scanPriority;
   private final Setting<Integer> cavesRange;
   private final Setting<Integer> cavesRangeY;
   private final Setting<Integer> range;
   private final Setting<Integer> y_range;
   private final Setting<Integer> delaymin;
   private final Setting<Integer> delaymax;
   private final Setting<Boolean> pauseBind;
   private final Setting<Keybind> pausekeybind;
   private final Setting<Boolean> outline;
   private final Setting<SettingColor> outlineColor;
   private final Setting<Boolean> tracer;
   private final Setting<SettingColor> tracerColor;
   private final Setting<Integer> clusterRange;
   private class_2338 currentScanBlock;
   private boolean pause_toggle;
   private final List<XGroup> groups;
   public HashMap<class_2338, XBlock> oresV3;
   private final Long2ObjectMap<XChunk> chunks;
   public static final List<RenderOre> ores = new ArrayList();
   public ArrayList<BlockScanned> need_rescan;
   private int renderedBlocks;
   private boolean lagging;
   private List<class_2338> blocks;
   long timescan;
   long millis;
   private Thread clickerThread;
   private Thread exposedthread;
   private boolean scan;
   private static final List<class_2338> scanned = new ArrayList();

   public XrayBruteforce() {
      super(Categories.World, "xray-bruteForce", "Bypasses anti-xray.");
      this.sgGeneral = this.settings.getDefaultGroup();
      this.sgSVisual = this.settings.createGroup("Scaner");
      this.sgRVisual = this.settings.createGroup("Scaner Render Visuals");
      this.sgSRenderer = this.settings.createGroup("Scanned Renderer");
      this.sgSaver = this.settings.createGroup("Scanned Saver");
      this.sgDelayer = this.settings.createGroup("Scanned Delayer");
      this.autoSave = this.sgSaver.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Auto save")).description("Save rendered ores.")).defaultValue(true)).build());
      this.save = this.sgSaver.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Save")).description("Save rendered ores.")).defaultValue(false)).onChanged((c) -> {
         if (this.isActive()) {
            Thread saveth = new Thread(() -> {
               for(RenderOre ore : (RenderOre[])ores.toArray(new RenderOre[0])) {
                  this.saveRenderOre(ore);
               }

               this.info("Saving complete", new Object[0]);
            });
            saveth.start();
         }

      })).build());
      this.load = this.sgSaver.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Load")).description("Load rendered ores.")).defaultValue(false)).onChanged((c) -> {
         if (this.isActive()) {
            this.loadSaveOres();
            this.info("Loaded", new Object[0]);
         }

      })).build());
      this.new_render = this.sgSRenderer.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("New Render")).description("New Render.")).defaultValue(true)).build());
      this.whblocks = this.sgGeneral.add(((BlockListSetting.Builder)((BlockListSetting.Builder)((BlockListSetting.Builder)(new BlockListSetting.Builder()).name("whitelist")).description("Which blocks to show x-rayed.")).defaultValue(new class_2248[]{class_2246.field_10442, class_2246.field_29029, class_2246.field_22109}).onChanged((v) -> scanned.clear())).build());
      this.defaultBlockConfig = this.sgGeneral.add(((GenericSetting.Builder)((GenericSetting.Builder)((GenericSetting.Builder)(new GenericSetting.Builder()).name("whitelist-default-config")).description("Default block config.")).defaultValue(new ESPBlockData(ShapeMode.Lines, new SettingColor(0, 255, 200), new SettingColor(0, 255, 200, 25), false, new SettingColor(0, 255, 200, 125)))).build());
      this.blockConfigs = this.sgGeneral.add(((BlockDataSetting.Builder)((BlockDataSetting.Builder)((BlockDataSetting.Builder)(new BlockDataSetting.Builder()).name("whitelist-block-configs")).description("Config for each block.")).defaultData(this.defaultBlockConfig).onChanged((v) -> {
         synchronized(ores) {
            for(RenderOre ore : ores) {
               ESPBlockData data = this.getBlockData(ore.block);
               ore.linecolor = data.lineColor;
               ore.sidecolor = data.sideColor;
               ore.tracercolor = data.tracerColor;
               ore.shapeMode = data.shapeMode;
            }

         }
      })).build());
      this.packet_first = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("packet-#1")).description("First packet.")).defaultValue(XrayBruteforce.PacketMode.Start)).build());
      this.packet_two = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("packet-#2")).description("Two packet.")).defaultValue(XrayBruteforce.PacketMode.Abort)).build());
      this.generationType = this.sgGeneral.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("Generation-type")).description("Ores generation type.")).defaultValue(XrayBruteforce.GenerationType.Old)).build());
      this.clear_cache_blocks = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Clear-cache")).description("Clear saved cache.")).defaultValue(false)).build());
      this.clear_cache_ores = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Clear-rendered-ores-cache")).description("Clear saved ores cache.")).defaultValue(false)).build());
      this.rescanerDelay = this.sgDelayer.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("rescaner-delay")).description("Deley for rechecking blobk.")).defaultValue(2000)).build());
      this.tps_sync = this.sgDelayer.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("TPS-sync")).description("TPS sync scaning.")).defaultValue(true)).build());
      this.fps_sync = this.sgDelayer.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("FPS-sync")).description("FPS sync scaning.")).defaultValue(false)).build());
      this.auto_height = this.sgSVisual.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Auto-height")).description("Auto detect height.")).defaultValue(false)).build());
      SettingGroup var10001 = this.sgSVisual;
      BoolSetting.Builder var10002 = (BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("Auto-dimension")).description("Auto detect dimension.")).defaultValue(true);
      Setting var10003 = this.auto_height;
      Objects.requireNonNull(var10003);
      this.auto_dimension = var10001.add(((BoolSetting.Builder)var10002.visible(var10003::get)).build());
      this.expanded = this.sgSVisual.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("exposed")).description("Scan exposed blocks.")).defaultValue(true)).build());
      this.scanPriority = this.sgSVisual.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)(new EnumSetting.Builder()).name("Scan priority")).description("Ores generation type.")).defaultValue(XrayBruteforce.ScanPriority.Normal)).build());
      this.cavesRange = this.sgSVisual.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Caves range")).description("Caves range.")).defaultValue(2)).visible(() -> this.scanPriority.get() == XrayBruteforce.ScanPriority.Caves)).build());
      this.cavesRangeY = this.sgSVisual.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Caves Y range")).description("Caves y range.")).defaultValue(2)).visible(() -> this.scanPriority.get() == XrayBruteforce.ScanPriority.Caves)).build());
      this.range = this.sgSVisual.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("range")).description("Bruteforce range.")).defaultValue(40)).min(3).sliderRange(3, 512).build());
      this.y_range = this.sgSVisual.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("y-range")).description("Bruteforce range.")).defaultValue(13)).min(3).sliderRange(3, 255).build());
      this.delaymin = this.sgDelayer.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Scan delay min")).description("Bruteforce delay min .")).defaultValue(30)).min(0).sliderRange(0, 150).build());
      this.delaymax = this.sgDelayer.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Scan delay max")).description("Bruteforce delay max .")).defaultValue(35)).min(0).sliderRange(0, 150).build());
      this.pauseBind = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("pause-bind")).description("Allow use pause bind.")).defaultValue(true)).build());
      var10001 = this.sgGeneral;
      KeybindSetting.Builder var4 = (KeybindSetting.Builder)((KeybindSetting.Builder)((KeybindSetting.Builder)(new KeybindSetting.Builder()).name("pause-keybind")).description("The bind for pause.")).defaultValue(Keybind.fromKey(88));
      var10003 = this.pauseBind;
      Objects.requireNonNull(var10003);
      this.pausekeybind = var10001.add(((KeybindSetting.Builder)var4.visible(var10003::get)).build());
      this.outline = this.sgRVisual.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("outline")).description("Outline to block.")).defaultValue(true)).build());
      var10001 = this.sgRVisual;
      ColorSetting.Builder var5 = ((ColorSetting.Builder)((ColorSetting.Builder)(new ColorSetting.Builder()).name("outline-color")).description("Outline color to block.")).defaultValue(new SettingColor(255, 255, 0, 255));
      var10003 = this.outline;
      Objects.requireNonNull(var10003);
      this.outlineColor = var10001.add(((ColorSetting.Builder)var5.visible(var10003::get)).build());
      this.tracer = this.sgRVisual.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)(new BoolSetting.Builder()).name("tracer")).description("Tracer to block.")).defaultValue(true)).build());
      var10001 = this.sgRVisual;
      var5 = ((ColorSetting.Builder)((ColorSetting.Builder)(new ColorSetting.Builder()).name("tracer-color")).description("Tracer color to block.")).defaultValue(new SettingColor(255, 255, 0, 255));
      var10003 = this.tracer;
      Objects.requireNonNull(var10003);
      this.tracerColor = var10001.add(((ColorSetting.Builder)var5.visible(var10003::get)).build());
      this.clusterRange = this.sgSVisual.add(((IntSetting.Builder)((IntSetting.Builder)((IntSetting.Builder)(new IntSetting.Builder()).name("Cluster range")).description("Cluster range.")).defaultValue(2)).min(1).sliderRange(1, 2).build());
      this.pause_toggle = true;
      this.groups = new UnorderedArrayList();
      this.oresV3 = new HashMap();
      this.chunks = new Long2ObjectOpenHashMap();
      this.need_rescan = new ArrayList();
      this.renderedBlocks = 0;
      this.lagging = false;
      this.blocks = new ArrayList();
      this.timescan = 0L;
      this.millis = 0L;
      this.exposedthread = null;
      this.scan = false;
      RainbowColors.register(this::onTickRainbow);
   }

   private void loadSaveOres() {
      Thread loadth = new Thread(() -> {
         File dir = new File(MeteorClient.FOLDER, "xray-bruteforce");
         if (dir.exists()) {
            File dir2 = new File(dir, Utils.getWorldName());
            if (dir2.exists()) {
               File[] arrFiles = dir2.listFiles();

               for(File file : Arrays.asList(arrFiles)) {
                  FileReader fr = null;

                  try {
                     fr = new FileReader(file);
                     BufferedReader reader = new BufferedReader(fr);

                     try {
                        String json = reader.readLine();
                        Gson gson = new Gson();
                        Type type = (new TypeToken<Map<String, Integer>>() {
                        }).getType();
                        Map<String, Integer> read = (Map)gson.fromJson(json, type);
                        class_2338 pos = new class_2338((Integer)read.get("X"), (Integer)read.get("Y"), (Integer)read.get("Z"));
                        if (EntityUtils.isInRenderDistance(pos)) {
                           this.addBlock(pos, true);
                        }
                     } catch (IOException e) {
                        e.printStackTrace();
                     }
                  } catch (FileNotFoundException e) {
                     e.printStackTrace();
                  }
               }
            }
         }

      });
      loadth.start();
   }

   private void saveRenderOre(RenderOre ore) {
      File dir = new File(MeteorClient.FOLDER, "xray-bruteforce");
      if (!dir.exists()) {
         dir.mkdir();
      }

      File dir2 = new File(dir, Utils.getWorldName());
      if (!dir2.exists()) {
         dir2.mkdir();
      }

      Gson gson = new Gson();
      Map<String, Integer> map = new LinkedHashMap();
      map.put("X", ore.blockPos.method_10263());
      map.put("Y", ore.blockPos.method_10264());
      map.put("Z", ore.blockPos.method_10260());
      String json = gson.toJson(map);
      String var10002 = dir2.getPath();
      int var10003 = ore.blockPos.method_10263();
      File file = new File(var10002, "x={" + var10003 + "}, y={" + ore.blockPos.method_10264() + "}, {z={" + ore.blockPos.method_10260() + "}");
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

   private boolean isPressed() {
      return ((Keybind)this.pausekeybind.get()).isPressed() && (Boolean)this.pauseBind.get();
   }

   private void onTickRainbow() {
      if (this.isActive()) {
         try {
            if (this.defaultBlockConfig != null && this.blockConfigs != null && this.defaultBlockConfig.get() != null) {
               ((ESPBlockData)this.defaultBlockConfig.get()).tickRainbow();
               if (this.blockConfigs.get() != null) {
                  Collection<ESPBlockData> datas = ((Map)this.blockConfigs.get()).values();
                  if (datas.size() > 0) {
                     for(ESPBlockData blockData : datas) {
                        if (blockData != null) {
                           blockData.tickRainbow();
                        }
                     }
                  }
               }
            }
         } catch (NullPointerException var4) {
         }
      }

   }

   @EventHandler
   private void onKeyEvent(KeyEvent event) {
      if (event.action == KeyAction.Press && this.isPressed()) {
         this.pause_toggle = !this.pause_toggle;
         if (this.pause_toggle) {
            this.currentScanBlock = null;
            this.info("§cPaused", new Object[0]);
         } else {
            this.info("§aUn paused", new Object[0]);
         }
      }

   }

   @EventHandler
   private void onMouseClickEvent(MouseClickEvent event) {
      if (event.action == KeyAction.Press && this.isPressed()) {
         this.pause_toggle = !this.pause_toggle;
         if (this.pause_toggle) {
            this.currentScanBlock = null;
            this.info("§cPaused", new Object[0]);
         } else {
            this.info("§aUn paused", new Object[0]);
         }
      }

   }

   public XBlock getSBlockData(class_2338 pos) {
      return this.oresV3.containsKey(pos) ? (XBlock)this.oresV3.get(pos) : null;
   }

   public XGroup newGroup(class_2248 block) {
      synchronized(this.chunks) {
         XGroup group = new XGroup(block);
         this.groups.add(group);
         return group;
      }
   }

   public void removeGroup(XGroup group) {
      synchronized(this.chunks) {
         this.groups.remove(group);
      }
   }

   public XBlock getBlock(int x, int y, int z) {
      XChunk chunk = (XChunk)this.chunks.get(class_1923.method_8331(x >> 4, z >> 4));
      return chunk == null ? null : chunk.get(x, y, z);
   }

   private boolean setColors(RenderOre ore) {
      if (ore != null && ore.block != null && ore.linecolor == null && ore.sidecolor == null && ore.tracercolor == null && ore.shapeMode == null && ore.sBlock == null) {
         assert this.mc.field_1687 != null;

         class_2680 state = this.mc.field_1687.method_8320(ore.blockPos);
         if (ore.block == class_2246.field_10124) {
            ore.block = state.method_26204();
         }

         ESPBlockData blockdata = this.getBlockData(state.method_26204());
         XBlock sbp = new XBlock(ore.blockPos.method_10263(), ore.blockPos.method_10264(), ore.blockPos.method_10260());
         ore.sBlock = sbp;
         ore.sBlock.update();
         ore.linecolor = blockdata.lineColor;
         ore.sidecolor = blockdata.sideColor;
         ore.tracercolor = blockdata.tracerColor;
         ore.shapeMode = blockdata.shapeMode;
         return true;
      } else {
         return false;
      }
   }

   private RenderOre getRenderOre(class_2338 pos) {
      synchronized(ores) {
         for(RenderOre cur : ores) {
            if (cur != null && cur.block != null && cur.blockPos != null && cur.blockPos.equals(pos)) {
               return cur;
            }
         }

         return null;
      }
   }

   private void removeRenderOre(class_2338 pos) {
      synchronized(ores) {
         ores.removeIf((cur) -> cur != null && cur.block != null && cur.blockPos != null && cur.blockPos.equals(pos));
      }
   }

   private void addRenderBlock(class_2338 blockPos) {
      synchronized(ores) {
         RenderOre ore = this.getRenderOre(blockPos);
         if (ore == null) {
            RenderOre ne = new RenderOre(blockPos);
            ne.blockPos = blockPos;
            ores.add(ne);
         } else if (ore.sBlock != null) {
            ore.sBlock.update();
         }

      }
   }

   private void addNeedRescan(class_2338 pos, int delay) {
      synchronized(this.need_rescan) {
         BlockScanned scanned = new BlockScanned();
         scanned.pos = pos;
         scanned.rescanTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + (long)delay;
         this.need_rescan.add(scanned);
      }
   }

   @EventHandler
   public void blockUpdateEvent(BlockUpdateEvent event) {
      if (!event.oldState.method_26215() && scanned.contains(event.pos) && ((List)this.whblocks.get()).contains(event.newState.method_26204()) && !this.pause_toggle) {
         this.addBlock(event.pos, true);

         for(class_2338 pos2 : this.getBlocks(event.pos, (Integer)this.cavesRange.get(), (Integer)this.cavesRangeY.get())) {
            this.addBlock(pos2, true);
         }
      }

      if (event.newState.method_26215()) {
         RenderOre render = this.getRenderOre(event.pos);
         if (render != null) {
            synchronized(ores) {
               ores.remove(render);
            }
         }
      }

   }

   @EventHandler
   private void minedBlock(BreakBlockEvent event) {
      (new Thread(() -> {
         RenderOre ore = this.getRenderOre(event.blockPos);
         if (ore != null) {
            ore.block = class_2246.field_10124;
            this.updateRenderedOres();
            synchronized(ores) {
               ores.remove(ore);
            }
         }

      })).start();
   }

   private void addBlock(class_2338 pos, Boolean ignore) {
      if (!scanned.contains(pos) && !ignore) {
         this.blocks.add(pos);
         scanned.add(pos);
      } else if (ignore) {
         this.blocks.add(pos);
         if (!scanned.contains(pos)) {
            scanned.add(pos);
         }
      }

   }

   public ESPBlockData getBlockData(class_2248 block) {
      ESPBlockData blockData = (ESPBlockData)((Map)this.blockConfigs.get()).get(block);
      return blockData == null ? (ESPBlockData)this.defaultBlockConfig.get() : blockData;
   }

   private void updateRenderedOres() {
      if (ores.size() > 0) {
         for(RenderOre pos : (RenderOre[])ores.toArray(new RenderOre[0])) {
            class_2680 state = this.mc.field_1687.method_8320(pos.blockPos);
            if (state.method_26204() == pos.block) {
               pos.sBlock.update();
            }
         }
      }

   }

   private void renderOres(Render3DEvent event) {
      int renderBlocks = 0;
      if (ores.size() > 0) {
         for(RenderOre pos : (RenderOre[])ores.toArray(new RenderOre[0])) {
            if (this.setColors(pos) && (Boolean)this.autoSave.get()) {
               Thread saveth = new Thread(() -> {
                  for(RenderOre ore : (RenderOre[])ores.toArray(new RenderOre[0])) {
                     this.saveRenderOre(ore);
                  }

               });
               saveth.start();
            }

            if (!(Boolean)this.new_render.get()) {
               if (EntityUtils.isInRenderDistance(pos.blockPos) && pos.block != null && ((List)this.whblocks.get()).contains(pos.block)) {
                  this.renderOreBlock(event, pos);
                  ++renderBlocks;
               }
            } else if (pos.sBlock != null && EntityUtils.isInRenderDistance(pos.blockPos) && pos.block != null && ((List)this.whblocks.get()).contains(pos.block)) {
               pos.sBlock.render(event, pos);
               if (pos.sBlock.group != null) {
                  pos.sBlock.group.render(event);
               }

               ++renderBlocks;
            }
         }
      }

      this.renderedBlocks = renderBlocks;
   }

   private void renderOreBlock(Render3DEvent event, RenderOre ore) {
      if (ore.block != null && this.mc.field_1687 != null && ore.tracercolor != null && ore.sidecolor != null && ore.linecolor != null) {
         class_2680 state = this.mc.field_1687.method_8320(ore.blockPos);
         class_265 shape = state.method_26218(this.mc.field_1687, ore.blockPos);
         ESPBlockData blockdata = this.getBlockData(ore.block);
         if (shape.method_1110()) {
            return;
         }

         for(class_238 b : shape.method_1090()) {
            event.renderer.box((double)ore.blockPos.method_10263() + b.field_1323, (double)ore.blockPos.method_10264() + b.field_1322, (double)ore.blockPos.method_10260() + b.field_1321, (double)ore.blockPos.method_10263() + b.field_1320, (double)ore.blockPos.method_10264() + b.field_1325, (double)ore.blockPos.method_10260() + b.field_1324, ore.sidecolor, ore.linecolor, blockdata.shapeMode, 0);
         }

         if (blockdata.tracer) {
            event.renderer.line(RenderUtils.center.field_1352, RenderUtils.center.field_1351, RenderUtils.center.field_1350, (double)ore.blockPos.method_10263(), (double)ore.blockPos.method_10264(), (double)ore.blockPos.method_10260(), ore.tracercolor);
         }
      }

   }

   @EventHandler
   private void render2d(Render2DEvent event) {
      this.lagging = false;
   }

   @EventHandler
   private void onRender(Render3DEvent event) {
      if ((Boolean)this.clear_cache_blocks.get()) {
         scanned.clear();
         this.need_rescan.clear();
         this.clear_cache_blocks.set(false);
         this.info("Cache checked blocks cleared", new Object[0]);
      }

      if ((Boolean)this.clear_cache_ores.get()) {
         ores.clear();
         this.clear_cache_ores.set(false);
         this.info("Cache render ores cleared", new Object[0]);
      }

      if (this.currentScanBlock != null) {
         class_2338 bp = this.currentScanBlock;

         assert this.mc.field_1687 != null;

         class_2680 state = this.mc.field_1687.method_8320(bp);
         class_265 shape = state.method_26218(this.mc.field_1687, bp);
         if (shape.method_1110()) {
            return;
         }

         if ((Boolean)this.outline.get()) {
            for(class_238 b : shape.method_1090()) {
               event.renderer.box((double)bp.method_10263() + b.field_1323, (double)bp.method_10264() + b.field_1322, (double)bp.method_10260() + b.field_1321, (double)bp.method_10263() + b.field_1320, (double)bp.method_10264() + b.field_1325, (double)bp.method_10260() + b.field_1324, new SettingColor(255, 255, 255, 255), (Color)this.outlineColor.get(), ShapeMode.Lines, 0);
            }
         }

         if ((Boolean)this.tracer.get()) {
            event.renderer.line(RenderUtils.center.field_1352, RenderUtils.center.field_1351, RenderUtils.center.field_1350, (double)bp.method_10263(), (double)bp.method_10264(), (double)bp.method_10260(), (Color)this.tracerColor.get());
         }
      }

   }

   @EventHandler
   private void onRenderOres(Render3DEvent event) {
      this.renderOres(event);
   }

   private void addCaves(class_2791 chunk) {
      if (this.scanPriority.get() == XrayBruteforce.ScanPriority.Caves) {
         ArrayList<class_2248> caf = new ArrayList();
         caf.add(class_2246.field_10124);
         ESPChunk s = ESPChunk.searchChunk(chunk, caf);
         if (s.blocks != null) {
            ObjectIterator var4 = s.blocks.values().iterator();

            while(var4.hasNext()) {
               ESPBlock sBlock = (ESPBlock)var4.next();

               for(class_2248 bp : (List)this.whblocks.get()) {
                  boolean newGeneration = this.generationType.get() == XrayBruteforce.GenerationType.New;
                  GenerationBlock generationBlock = GenerationBlock.getGenerationBlock(bp, newGeneration);
                  if (generationBlock != null) {
                     class_2338 pos = new class_2338(sBlock.x, sBlock.y, sBlock.z);
                     if (sBlock.y >= generationBlock.min_height && sBlock.y <= generationBlock.max_height) {
                        for(class_2338 pos2 : this.getBlocks(pos, (Integer)this.cavesRange.get(), (Integer)this.cavesRangeY.get())) {
                           if (!this.isExposedOre(pos2) && pos2.method_10264() >= generationBlock.min_height && pos2.method_10264() <= generationBlock.max_height) {
                              this.addBlock(pos2, false);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   private void addExposedBlocks() {
      if (this.mc.field_1687 != null) {
         for(class_2791 chunk : Utils.chunks()) {
            if ((Boolean)this.expanded.get()) {
               ESPChunk s = ESPChunk.searchChunk(chunk, (List)this.whblocks.get());
               if (s.blocks != null) {
                  ObjectIterator var5 = s.blocks.values().iterator();

                  while(var5.hasNext()) {
                     ESPBlock sBlock = (ESPBlock)var5.next();
                     class_2338 pos = new class_2338(sBlock.x, sBlock.y, sBlock.z);
                     if (((List)this.whblocks.get()).contains(this.mc.field_1687.method_8320(pos).method_26204()) && this.isExposedOre(pos)) {
                        if ((Boolean)this.auto_dimension.get()) {
                           GenerationBlock generationBlock = GenerationBlock.getGenerationBlock(this.mc.field_1687.method_8320(pos).method_26204(), false);
                           if (generationBlock != null && generationBlock.dimension == PlayerUtils.getDimension()) {
                              this.addBlock(pos, false);

                              for(class_2338 pos2 : this.getBlocks(pos, (Integer)this.clusterRange.get(), (Integer)this.clusterRange.get())) {
                                 this.addBlock(pos2, false);
                              }
                           }
                        } else {
                           this.addBlock(pos, false);

                           for(class_2338 pos2 : this.getBlocks(pos, (Integer)this.clusterRange.get(), (Integer)this.clusterRange.get())) {
                              this.addBlock(pos2, false);
                           }
                        }
                     }
                  }
               }
            }

            this.addCaves(chunk);

            try {
               Thread.sleep(5L);
            } catch (InterruptedException var12) {
            }
         }
      }

   }

   private boolean isExposedBlock(class_2248 block) {
      return block == class_2246.field_10124 || block == class_2246.field_10382 || block == class_2246.field_10164;
   }

   private boolean isExposedBlock(class_2680 state) {
      class_2248 block = state.method_26204();
      return this.isExposedBlock(block);
   }

   private boolean isExposedOre(class_2338 pos) {
      if (this.mc.field_1687 != null) {
         class_2680 def = this.mc.field_1687.method_8320(pos);
         if (((List)this.whblocks.get()).contains(def.method_26204())) {
            if (this.isExposedBlock(this.mc.field_1687.method_8320(pos.method_10069(0, 1, 0)))) {
               return true;
            }

            if (this.isExposedBlock(this.mc.field_1687.method_8320(pos.method_10069(0, -1, 0)))) {
               return true;
            }

            if (this.isExposedBlock(this.mc.field_1687.method_8320(pos.method_10069(1, 0, 0)))) {
               return true;
            }

            if (this.isExposedBlock(this.mc.field_1687.method_8320(pos.method_10069(-1, 0, 0)))) {
               return true;
            }

            if (this.isExposedBlock(this.mc.field_1687.method_8320(pos.method_10069(0, 0, 1)))) {
               return true;
            }

            if (this.isExposedBlock(this.mc.field_1687.method_8320(pos.method_10069(0, 0, -1)))) {
               return true;
            }
         }
      }

      return false;
   }

   public String getInfoString() {
      return this.pause_toggle ? "paused" : this.renderedBlocks + "b, " + this.timescan + "t";
   }

   private boolean send(class_2338 blockpos) {
      boolean sucess = true;
      if (blockpos == null) {
         sucess = false;
      }

      class_634 conn = this.mc.method_1562();
      if (conn == null) {
         sucess = false;
      }

      if (this.mc.field_1687 == null) {
         sucess = false;
      } else {
         class_2680 state = this.mc.field_1687.method_8320(blockpos);
         if (state.method_26204() == class_2246.field_10099 || state.method_26204() == class_2246.field_10336 || state.method_26204() == class_2246.field_10124 || state.method_26204() == class_2246.field_10164 || state.method_26204() == class_2246.field_10382) {
            sucess = false;
         }
      }

      if (!EntityUtils.isInRenderDistance(blockpos)) {
         sucess = false;
      }

      if (sucess) {
         this.currentScanBlock = blockpos;
         class_2846 packet_one = this.getPacket(blockpos, this.packet_first);
         if (packet_one != null) {
            conn.method_52787(packet_one);
         }

         class_2846 packet_tw = this.getPacket(blockpos, this.packet_two);
         if (packet_tw != null) {
            conn.method_52787(packet_tw);
         }

         this.addNeedRescan(blockpos, (Integer)this.rescanerDelay.get());
      } else {
         this.currentScanBlock = null;
      }

      if (sucess && !scanned.contains(blockpos)) {
         scanned.add(blockpos);
      }

      return sucess;
   }

   private class_2846 getPacket(class_2338 blockpos, Setting<PacketMode> setting) {
      if (setting.get() == XrayBruteforce.PacketMode.Abort) {
         class_2846 abort = new class_2846(class_2847.field_12971, new class_2338(blockpos), class_2350.field_11036, 0);
         return abort;
      } else if (setting.get() == XrayBruteforce.PacketMode.Start) {
         class_2846 start = new class_2846(class_2847.field_12968, new class_2338(blockpos), class_2350.field_11036, 0);
         return start;
      } else if (setting.get() == XrayBruteforce.PacketMode.Stop) {
         class_2846 stop = new class_2846(class_2847.field_12973, new class_2338(blockpos), class_2350.field_11036, 0);
         return stop;
      } else {
         return null;
      }
   }

   private void checker() {
      float timeSinceLastTick = TickRate.INSTANCE.getTimeSinceLastTick();
      if (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= this.millis && !this.pause_toggle) {
         if (this.blocks != null && this.blocks.size() > 0) {
            if ((Boolean)this.tps_sync.get() && timeSinceLastTick <= 1.0F) {
               this.work();
            } else if (!(Boolean)this.tps_sync.get()) {
               this.work();
            }
         } else if ((Boolean)this.tps_sync.get() && timeSinceLastTick <= 1.0F) {
            this.addRandomBlock();
            this.work();
         } else if (!(Boolean)this.tps_sync.get()) {
            this.addRandomBlock();
            this.work();
         }
      }

   }

   private void work() {
      try {
         long start = System.currentTimeMillis();
         Iterator<class_2338> blocksIterator = this.blocks.iterator();
         if (blocksIterator.hasNext()) {
            if ((Boolean)this.fps_sync.get()) {
               if (!this.lagging) {
                  class_2338 block = (class_2338)blocksIterator.next();
                  if (this.send(block)) {
                     this.millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + (long)Utils.random((Integer)this.delaymin.get(), (Integer)this.delaymax.get());
                     this.lagging = true;
                  }

                  blocksIterator.remove();
               }
            } else {
               class_2338 block = (class_2338)blocksIterator.next();
               blocksIterator.remove();
               if (this.send(block)) {
                  this.millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + (long)Utils.random((Integer)this.delaymin.get(), (Integer)this.delaymax.get());
                  this.lagging = true;
               }
            }
         }

         long finish = System.currentTimeMillis();
         this.timescan = finish - start;
      } catch (Exception var6) {
      }

   }

   private void addRandomBlock() {
      assert this.mc.field_1724 != null;

      int x = Utils.random(this.mc.field_1724.method_24515().method_10263() - (Integer)this.range.get(), this.mc.field_1724.method_24515().method_10263() + (Integer)this.range.get());
      int z = Utils.random(this.mc.field_1724.method_24515().method_10260() - (Integer)this.range.get(), this.mc.field_1724.method_24515().method_10260() + (Integer)this.range.get());
      if ((Boolean)this.auto_height.get()) {
         List<class_2248> findBlocks = (List)this.whblocks.get();
         boolean newGeneration = false;
         if (this.generationType.get() == XrayBruteforce.GenerationType.New) {
            newGeneration = true;
         }

         for(class_2248 block : findBlocks) {
            GenerationBlock b = GenerationBlock.getGenerationBlock(block, newGeneration);
            if (b != null && b.block == block) {
               int y = Utils.random(b.min_height, b.max_height);
               if ((Boolean)this.auto_dimension.get() && PlayerUtils.getDimension() == b.dimension) {
                  this.addBlock(new class_2338(x, y, z), false);
               } else if (!(Boolean)this.auto_dimension.get()) {
                  this.addBlock(new class_2338(x, y, z), false);
               }
            } else if (b == null) {
               int y = Utils.random(this.mc.field_1724.method_24515().method_10264() - (Integer)this.y_range.get(), this.mc.field_1724.method_24515().method_10264() + (Integer)this.y_range.get());
               if (!scanned.contains(new class_2338(x, y, z))) {
                  this.addBlock(new class_2338(x, y, z), false);
               }
            }
         }
      } else {
         int y = Utils.random(this.mc.field_1724.method_24515().method_10264() - (Integer)this.y_range.get(), this.mc.field_1724.method_24515().method_10264() + (Integer)this.y_range.get());
         if (!scanned.contains(new class_2338(x, y, z))) {
            if ((Boolean)this.auto_dimension.get() && PlayerUtils.getDimension() == Dimension.Overworld) {
               this.addBlock(new class_2338(x, y, z), false);
            } else if (!(Boolean)this.auto_dimension.get()) {
               this.addBlock(new class_2338(x, y, z), false);
            }
         }
      }

   }

   @EventHandler
   private void onGameLeft(GameLeftEvent event) {
      this.need_rescan.clear();
   }

   private void reScaner() {
      synchronized(this.need_rescan) {
         float timeSinceLastTick = TickRate.INSTANCE.getTimeSinceLastTick();
         Iterator<BlockScanned> iterator = this.need_rescan.iterator();

         while(iterator.hasNext()) {
            BlockScanned blockscanned = (BlockScanned)iterator.next();
            if (timeSinceLastTick <= 1.0F) {
               if (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() >= blockscanned.rescanTime && this.mc.field_1687 != null) {
                  class_2680 state = this.mc.field_1687.method_8320(blockscanned.pos);
                  if (((List)this.whblocks.get()).contains(state.method_26204())) {
                     this.addRenderBlock(blockscanned.pos);

                     for(class_2338 pos : this.getBlocks(blockscanned.pos, (Integer)this.clusterRange.get(), (Integer)this.clusterRange.get())) {
                        this.addBlock(pos, true);
                        if (!scanned.contains(pos)) {
                           this.addNeedRescan(pos, (Integer)this.rescanerDelay.get());
                        }
                     }
                  } else {
                     RenderOre ore = this.getRenderOre(blockscanned.pos);
                     if (ore != null) {
                        this.removeRenderOre(blockscanned.pos);
                     }
                  }

                  iterator.remove();
               }
            } else if (timeSinceLastTick > 1.0F) {
               int[] piArray = String.valueOf(1.0F / timeSinceLastTick).replaceAll("\\D", "").chars().map(Character::getNumericValue).toArray();
               blockscanned.rescanTime += (long)Arrays.stream(piArray).skip(1L).max().getAsInt();
            }
         }

      }
   }

   public void onActivate() {
      this.scan = true;
      this.millis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
      this.clickerThread = new Thread(() -> {
         this.addRandomBlock();

         while(this.scan) {
            this.checker();
            this.reScaner();
         }

      });
      this.clickerThread.start();
      this.exposedthread = new Thread(() -> {
         while(this.scan) {
            this.addExposedBlocks();
            this.updateRenderedOres();

            try {
               Thread.sleep(25L);
            } catch (InterruptedException var2) {
            }
         }

      });
      this.exposedthread.start();
   }

   public void onDeactivate() {
      this.scan = false;
      this.blocks.clear();
      this.currentScanBlock = null;
      if (this.clickerThread != null) {
         this.clickerThread = null;
      }

      if (this.exposedthread != null) {
         this.exposedthread = null;
      }

   }

   private List<class_2338> getBlocks(class_2338 startPos, int y_radius, int radius) {
      List<class_2338> temp = new ArrayList();

      for(int dy = -y_radius; dy <= y_radius; ++dy) {
         if (startPos.method_10264() + dy >= -60 && startPos.method_10264() + dy <= 360) {
            for(int dz = -radius; dz <= radius; ++dz) {
               for(int dx = -radius; dx <= radius; ++dx) {
                  class_2338 blockPos = startPos.method_10069(dx, dy, dz);
                  this.mc.field_1687.method_8320(blockPos);
                  boolean isInRenderDistance = EntityUtils.isInRenderDistance(blockPos);
                  boolean isBlockPosNotInList = !scanned.contains(blockPos);
                  if (isInRenderDistance && isBlockPosNotInList) {
                     temp.add(blockPos);
                  }
               }
            }
         }
      }

      return temp;
   }

   public static enum PacketMode {
      None,
      Start,
      Abort,
      Stop;

      // $FF: synthetic method
      private static PacketMode[] $values() {
         return new PacketMode[]{None, Start, Abort, Stop};
      }
   }

   public static enum ScanPriority {
      Caves,
      Normal;

      // $FF: synthetic method
      private static ScanPriority[] $values() {
         return new ScanPriority[]{Caves, Normal};
      }
   }

   public class RenderOre {
      public class_2248 block;
      public class_2338 blockPos;
      public XBlock sBlock;
      public SettingColor linecolor;
      public SettingColor sidecolor;
      public SettingColor tracercolor;
      public ShapeMode shapeMode;

      public RenderOre(class_2338 pos) {
         this.block = class_2246.field_10124;
         this.blockPos = null;
         this.sBlock = null;
         this.linecolor = null;
         this.sidecolor = null;
         this.tracercolor = null;
         this.shapeMode = null;
         this.blockPos = pos;
      }
   }

   public static enum GenerationType {
      Old,
      New;

      // $FF: synthetic method
      private static GenerationType[] $values() {
         return new GenerationType[]{Old, New};
      }
   }

   public class BlockScanned {
      public class_2338 pos;
      public long rescanTime;
   }
}
