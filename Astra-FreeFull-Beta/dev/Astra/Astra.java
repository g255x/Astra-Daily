package dev.Astra;

import dev.Astra.api.events.eventbus.EventBus;
import dev.Astra.api.events.impl.InitEvent;
import dev.Astra.core.impl.BreakManager;
import dev.Astra.core.impl.CommandManager;
import dev.Astra.core.impl.ConfigManager;
import dev.Astra.core.impl.FPSManager;
import dev.Astra.core.impl.FriendManager;
import dev.Astra.core.impl.HoleManager;
import dev.Astra.core.impl.ModuleManager;
import dev.Astra.core.impl.PlayerManager;
import dev.Astra.core.impl.PopManager;
import dev.Astra.core.impl.RotationManager;
import dev.Astra.core.impl.ServerManager;
import dev.Astra.core.impl.ShaderManager;
import dev.Astra.core.impl.ThreadManager;
import dev.Astra.core.impl.TimerManager;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.misc.KillEffect;
import java.io.File;
import java.lang.invoke.MethodHandles;
import net.fabricmc.api.ModInitializer;
import net.minecraft.class_310;

public class Astra implements ModInitializer {
   public static final String NAME = "Astra Client";
   public static final String CONFIG_DIR = "astra";
   public static final EventBus EVENT_BUS = new EventBus();
   public static HoleManager HOLE;
   public static PlayerManager PLAYER;
   public static ModuleManager MODULE;
   public static CommandManager COMMAND;
   public static ConfigManager CONFIG;
   public static RotationManager ROTATION;
   public static BreakManager BREAK;
   public static PopManager POP;
   public static FriendManager FRIEND;
   public static TimerManager TIMER;
   public static ShaderManager SHADER;
   public static FPSManager FPS;
   public static ServerManager SERVER;
   public static ThreadManager THREAD;
   public static boolean loaded = false;
   public static long initTime;

   public static String getPrefix() {
      return ClientSetting.INSTANCE.prefix.getValue();
   }

   public static void save() {
      CONFIG.save();
      FRIEND.save();
      System.out.println("[Astra Client] Saved");
   }

   private void register() {
      EVENT_BUS.registerLambdaFactory((lookupInMethod, klass) -> (MethodHandles.Lookup)lookupInMethod.invoke((Object)null, klass, MethodHandles.lookup()));
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         if (loaded) {
            save();
         }

      }));
   }

   public void onInitialize() {
      this.register();
      MODULE = new ModuleManager();
      CONFIG = new ConfigManager();
      HOLE = new HoleManager();
      COMMAND = new CommandManager();
      FRIEND = new FriendManager();
      ROTATION = new RotationManager();
      BREAK = new BreakManager();
      PLAYER = new PlayerManager();
      POP = new PopManager();
      TIMER = new TimerManager();
      SHADER = new ShaderManager();
      FPS = new FPSManager();
      SERVER = new ServerManager();
      CONFIG.load();
      KillEffect.registerSounds();
      THREAD = new ThreadManager();
      initTime = System.currentTimeMillis();
      loaded = true;
      EVENT_BUS.post(new InitEvent());
      String var10002 = class_310.method_1551().field_1697.getPath();
      File folder = new File(var10002 + File.separator + "astra" + File.separator + "cfg");
      if (!folder.exists()) {
         folder.mkdirs();
      }

   }
}
