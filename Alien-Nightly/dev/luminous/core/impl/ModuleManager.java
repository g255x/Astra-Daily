package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.Render2DEvent;
import dev.luminous.api.events.impl.Render3DEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.path.BaritoneUtil;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.mod.Mod;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.client.BaritoneModule;
import dev.luminous.mod.modules.impl.client.ClickGui;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.client.ColorsModule;
import dev.luminous.mod.modules.impl.client.Fonts;
import dev.luminous.mod.modules.impl.client.HUD;
import dev.luminous.mod.modules.impl.client.ItemsCounter;
import dev.luminous.mod.modules.impl.client.Notification;
import dev.luminous.mod.modules.impl.client.TextRadar;
import dev.luminous.mod.modules.impl.combat.AntiCrawl;
import dev.luminous.mod.modules.impl.combat.AntiPhase;
import dev.luminous.mod.modules.impl.combat.AntiRegear;
import dev.luminous.mod.modules.impl.combat.AntiWeak;
import dev.luminous.mod.modules.impl.combat.Aura;
import dev.luminous.mod.modules.impl.combat.AutoAnchor;
import dev.luminous.mod.modules.impl.combat.AutoButton;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.impl.combat.AutoEnderChest;
import dev.luminous.mod.modules.impl.combat.AutoMine;
import dev.luminous.mod.modules.impl.combat.AutoPush;
import dev.luminous.mod.modules.impl.combat.AutoRegear;
import dev.luminous.mod.modules.impl.combat.AutoTrap;
import dev.luminous.mod.modules.impl.combat.AutoWeb;
import dev.luminous.mod.modules.impl.combat.AutoXP;
import dev.luminous.mod.modules.impl.combat.Blocker;
import dev.luminous.mod.modules.impl.combat.Burrow;
import dev.luminous.mod.modules.impl.combat.CevBreaker;
import dev.luminous.mod.modules.impl.combat.Criticals;
import dev.luminous.mod.modules.impl.combat.CriticalsPlus;
import dev.luminous.mod.modules.impl.combat.DropAntiRegear;
import dev.luminous.mod.modules.impl.combat.Follower;
import dev.luminous.mod.modules.impl.combat.HoleFiller;
import dev.luminous.mod.modules.impl.combat.HolePush;
import dev.luminous.mod.modules.impl.combat.IQBoost;
import dev.luminous.mod.modules.impl.combat.Offhand;
import dev.luminous.mod.modules.impl.combat.Panic;
import dev.luminous.mod.modules.impl.combat.PearlBlocker;
import dev.luminous.mod.modules.impl.combat.PistonCrystal;
import dev.luminous.mod.modules.impl.combat.Quiver;
import dev.luminous.mod.modules.impl.combat.SelfTrap;
import dev.luminous.mod.modules.impl.combat.Surround;
import dev.luminous.mod.modules.impl.combat.TrapdoorAura;
import dev.luminous.mod.modules.impl.exploit.AntiHunger;
import dev.luminous.mod.modules.impl.exploit.AntiPacket;
import dev.luminous.mod.modules.impl.exploit.AutoSlabPlacer;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.exploit.BowBomb;
import dev.luminous.mod.modules.impl.exploit.ChorusControl;
import dev.luminous.mod.modules.impl.exploit.ChunkESP;
import dev.luminous.mod.modules.impl.exploit.Clip;
import dev.luminous.mod.modules.impl.exploit.DisablerModule;
import dev.luminous.mod.modules.impl.exploit.InfiniteTrident;
import dev.luminous.mod.modules.impl.exploit.MaceSpoof;
import dev.luminous.mod.modules.impl.exploit.NoResourcePack;
import dev.luminous.mod.modules.impl.exploit.PacketControl;
import dev.luminous.mod.modules.impl.exploit.Phase;
import dev.luminous.mod.modules.impl.exploit.PingSpoof;
import dev.luminous.mod.modules.impl.exploit.RocketExtend;
import dev.luminous.mod.modules.impl.exploit.ServerLagger;
import dev.luminous.mod.modules.impl.exploit.SkinFlicker;
import dev.luminous.mod.modules.impl.exploit.TeleportLogger;
import dev.luminous.mod.modules.impl.exploit.XCarry;
import dev.luminous.mod.modules.impl.misc.AutoDrop;
import dev.luminous.mod.modules.impl.misc.AutoEZ;
import dev.luminous.mod.modules.impl.misc.AutoKit;
import dev.luminous.mod.modules.impl.misc.AutoLog;
import dev.luminous.mod.modules.impl.misc.AutoReconnect;
import dev.luminous.mod.modules.impl.misc.BedCrafter;
import dev.luminous.mod.modules.impl.misc.Bot;
import dev.luminous.mod.modules.impl.misc.ChatAppend;
import dev.luminous.mod.modules.impl.misc.ChestStealer;
import dev.luminous.mod.modules.impl.misc.DropShulkerBox;
import dev.luminous.mod.modules.impl.misc.ExtraTab;
import dev.luminous.mod.modules.impl.misc.FakePlayer;
import dev.luminous.mod.modules.impl.misc.Friend;
import dev.luminous.mod.modules.impl.misc.KillEffect;
import dev.luminous.mod.modules.impl.misc.LavaFiller;
import dev.luminous.mod.modules.impl.misc.NoSound;
import dev.luminous.mod.modules.impl.misc.NoTerrainScreen;
import dev.luminous.mod.modules.impl.misc.Nuker;
import dev.luminous.mod.modules.impl.misc.PacketLogger;
import dev.luminous.mod.modules.impl.misc.PopEz;
import dev.luminous.mod.modules.impl.misc.Punctuation;
import dev.luminous.mod.modules.impl.misc.ShulkerViewer;
import dev.luminous.mod.modules.impl.misc.Spammer;
import dev.luminous.mod.modules.impl.misc.Tips;
import dev.luminous.mod.modules.impl.misc.WindowTitle;
import dev.luminous.mod.modules.impl.movement.AntiSlowFall;
import dev.luminous.mod.modules.impl.movement.AntiVoid;
import dev.luminous.mod.modules.impl.movement.AutoWalk;
import dev.luminous.mod.modules.impl.movement.BlockStrafe;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.EntityControl;
import dev.luminous.mod.modules.impl.movement.FakeFly;
import dev.luminous.mod.modules.impl.movement.FastFall;
import dev.luminous.mod.modules.impl.movement.FastSwim;
import dev.luminous.mod.modules.impl.movement.FastWeb;
import dev.luminous.mod.modules.impl.movement.Flatten;
import dev.luminous.mod.modules.impl.movement.Fly;
import dev.luminous.mod.modules.impl.movement.HoleSnap;
import dev.luminous.mod.modules.impl.movement.LongJump;
import dev.luminous.mod.modules.impl.movement.MovementSync;
import dev.luminous.mod.modules.impl.movement.NoJumpDelay;
import dev.luminous.mod.modules.impl.movement.NoSlow;
import dev.luminous.mod.modules.impl.movement.PacketFly;
import dev.luminous.mod.modules.impl.movement.SafeWalk;
import dev.luminous.mod.modules.impl.movement.Scaffold;
import dev.luminous.mod.modules.impl.movement.Speed;
import dev.luminous.mod.modules.impl.movement.Sprint;
import dev.luminous.mod.modules.impl.movement.Step;
import dev.luminous.mod.modules.impl.movement.Strafe;
import dev.luminous.mod.modules.impl.movement.VClip;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.impl.player.AirPlace;
import dev.luminous.mod.modules.impl.player.AntiEffects;
import dev.luminous.mod.modules.impl.player.AutoArmor;
import dev.luminous.mod.modules.impl.player.AutoFuck;
import dev.luminous.mod.modules.impl.player.AutoPot;
import dev.luminous.mod.modules.impl.player.AutoTool;
import dev.luminous.mod.modules.impl.player.FreeLook;
import dev.luminous.mod.modules.impl.player.Freecam;
import dev.luminous.mod.modules.impl.player.InteractTweaks;
import dev.luminous.mod.modules.impl.player.KeyPearl;
import dev.luminous.mod.modules.impl.player.LowArmorAlert;
import dev.luminous.mod.modules.impl.player.NoFall;
import dev.luminous.mod.modules.impl.player.OffFirework;
import dev.luminous.mod.modules.impl.player.PacketEat;
import dev.luminous.mod.modules.impl.player.Replenish;
import dev.luminous.mod.modules.impl.player.Sorter;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.impl.player.TimerModule;
import dev.luminous.mod.modules.impl.player.Yaw;
import dev.luminous.mod.modules.impl.render.Ambience;
import dev.luminous.mod.modules.impl.render.AspectRatio;
import dev.luminous.mod.modules.impl.render.BreakESP;
import dev.luminous.mod.modules.impl.render.CameraClip;
import dev.luminous.mod.modules.impl.render.Chams;
import dev.luminous.mod.modules.impl.render.Crosshair;
import dev.luminous.mod.modules.impl.render.ESP;
import dev.luminous.mod.modules.impl.render.Fov;
import dev.luminous.mod.modules.impl.render.HighLight;
import dev.luminous.mod.modules.impl.render.HoleESP;
import dev.luminous.mod.modules.impl.render.InventoryHUD;
import dev.luminous.mod.modules.impl.render.LogoutSpots;
import dev.luminous.mod.modules.impl.render.MotionCamera;
import dev.luminous.mod.modules.impl.render.NameTag2D;
import dev.luminous.mod.modules.impl.render.NameTags;
import dev.luminous.mod.modules.impl.render.NoRender;
import dev.luminous.mod.modules.impl.render.PhaseESP;
import dev.luminous.mod.modules.impl.render.PlaceRender;
import dev.luminous.mod.modules.impl.render.PlayerInfoHUD;
import dev.luminous.mod.modules.impl.render.PopChams;
import dev.luminous.mod.modules.impl.render.PotCount;
import dev.luminous.mod.modules.impl.render.ShaderModule;
import dev.luminous.mod.modules.impl.render.TotemParticle;
import dev.luminous.mod.modules.impl.render.Tracers;
import dev.luminous.mod.modules.impl.render.Trajectories;
import dev.luminous.mod.modules.impl.render.TwoDESP;
import dev.luminous.mod.modules.impl.render.ViewModel;
import dev.luminous.mod.modules.impl.render.Xray;
import dev.luminous.mod.modules.impl.render.Zoom;
import dev.luminous.mod.modules.settings.Setting;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.class_1109;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_4587;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class ModuleManager implements Wrapper {
   private final ArrayList<Module> modules = new ArrayList();
   private final ArrayList<ToggleBanner> toggleBanners = new ArrayList();
   private boolean notificationMouseWasDown = false;
   private boolean interactiveToggleInBanner = false;

   public ArrayList<Module> getModules() {
      return this.modules;
   }

   public ModuleManager() {
      this.init();
   }

   public void init() {
      if (BaritoneUtil.loaded) {
         this.addModule(new BaritoneModule());
      }

      this.addModule(new Panic());
      this.addModule(new DisablerModule());
      this.addModule(new LongJump());
      this.addModule(new WindowTitle());
      this.addModule(new PotCount());
      this.addModule(new TwoDESP());
      this.addModule(new InventoryHUD());
      this.addModule(new NameTag2D());
      this.addModule(new AntiSlowFall());
      this.addModule(new LowArmorAlert());
      this.addModule(new DropShulkerBox());
      this.addModule(new AutoKit());
      this.addModule(new Bot());
      this.addModule(new AutoDrop());
      this.addModule(new Fonts());
      this.addModule(new NoTerrainScreen());
      this.addModule(new AutoCrystal());
      this.addModule(new Ambience());
      this.addModule(new AntiHunger());
      this.addModule(new AntiVoid());
      this.addModule(new AutoWalk());
      this.addModule(new VClip());
      this.addModule(new ExtraTab());
      this.addModule(new AntiWeak());
      this.addModule(new BedCrafter());
      this.addModule(new Friend());
      this.addModule(new AspectRatio());
      this.addModule(new ChunkESP());
      this.addModule(new Aura());
      this.addModule(new PistonCrystal());
      this.addModule(new AutoSlabPlacer());
      this.addModule(new OffFirework());
      this.addModule(new Follower());
      this.addModule(new PearlBlocker());
      this.addModule(new AutoAnchor());
      this.addModule(new PhaseESP());
      this.addModule(new AutoArmor());
      this.addModule(new Notification());
      this.addModule(new HolePush());
      this.addModule(new DropAntiRegear());
      this.addModule(new ChestStealer());
      this.addModule(new PlayerInfoHUD());
      this.addModule(new AutoMine());
      this.addModule(new AutoLog());
      this.addModule(new PopEz());
      this.addModule(new AutoEnderChest());
      this.addModule(new AutoButton());
      this.addModule(new AutoFuck());
      this.addModule(new IQBoost());
      this.addModule(new AutoEZ());
      this.addModule(new SelfTrap());
      this.addModule(new Sorter());
      this.addModule(new AutoXP());
      this.addModule(new AutoPot());
      this.addModule(new AutoPush());
      this.addModule(new Offhand());
      this.addModule(new Nuker());
      this.addModule(new AutoTrap());
      this.addModule(new AutoWeb());
      this.addModule(new Blink());
      this.addModule(new ChorusControl());
      this.addModule(new BlockStrafe());
      this.addModule(new FastSwim());
      this.addModule(new Blocker());
      this.addModule(new Quiver());
      this.addModule(new BowBomb());
      this.addModule(new BreakESP());
      this.addModule(new Burrow());
      this.addModule(new Punctuation());
      this.addModule(new MaceSpoof());
      this.addModule(new CameraClip());
      this.addModule(new ChatAppend());
      this.addModule(new ClickGui());
      this.addModule(new InfiniteTrident());
      this.addModule(new ColorsModule());
      this.addModule(new AutoRegear());
      this.addModule(new LavaFiller());
      this.addModule(new AntiPhase());
      this.addModule(new Clip());
      this.addModule(new AntiCheat());
      this.addModule(new ItemsCounter());
      this.addModule(new Fov());
      this.addModule(new Criticals());
      this.addModule(new CevBreaker());
      this.addModule(new Crosshair());
      this.addModule(new Chams());
      this.addModule(new AntiPacket());
      this.addModule(new AutoReconnect());
      this.addModule(new ESP());
      this.addModule(new HoleESP());
      this.addModule(new Tracers());
      this.addModule(new MovementSync());
      this.addModule(new ElytraFly());
      this.addModule(new PacketLogger());
      this.addModule(new TeleportLogger());
      this.addModule(new SkinFlicker());
      this.addModule(new EntityControl());
      this.addModule(new NameTags());
      this.addModule(new ShulkerViewer());
      this.addModule(new PingSpoof());
      this.addModule(new FakePlayer());
      this.addModule(new Spammer());
      this.addModule(new MotionCamera());
      this.addModule(new HighLight());
      this.addModule(new FastFall());
      this.addModule(new FastWeb());
      this.addModule(new Flatten());
      this.addModule(new Fly());
      this.addModule(new Yaw());
      this.addModule(new Freecam());
      this.addModule(new FreeLook());
      this.addModule(new TimerModule());
      this.addModule(new Tips());
      this.addModule(new ClientSetting());
      this.addModule(new TextRadar());
      this.addModule(new HUD());
      this.addModule(new NoResourcePack());
      this.addModule(new RocketExtend());
      this.addModule(new HoleFiller());
      this.addModule(new HoleSnap());
      this.addModule(new LogoutSpots());
      this.addModule(new AutoTool());
      this.addModule(new Trajectories());
      this.addModule(new KillEffect());
      this.addModule(new KeyPearl());
      this.addModule(new AntiEffects());
      this.addModule(new NoFall());
      this.addModule(new NoRender());
      this.addModule(new NoSlow());
      this.addModule(new NoSound());
      this.addModule(new AirPlace());
      this.addModule(new Xray());
      this.addModule(new PacketEat());
      this.addModule(new PacketFly());
      this.addModule(new SpeedMine());
      this.addModule(new PacketControl());
      this.addModule(new Phase());
      this.addModule(new PlaceRender());
      this.addModule(new InteractTweaks());
      this.addModule(new PopChams());
      this.addModule(new Replenish());
      this.addModule(new ServerLagger());
      this.addModule(new Scaffold());
      this.addModule(new ShaderModule());
      this.addModule(new AntiCrawl());
      this.addModule(new AntiRegear());
      this.addModule(new SafeWalk());
      this.addModule(new NoJumpDelay());
      this.addModule(new Speed());
      this.addModule(new Sprint());
      this.addModule(new Strafe());
      this.addModule(new Step());
      this.addModule(new Surround());
      this.addModule(new TotemParticle());
      this.addModule(new Velocity());
      this.addModule(new ViewModel());
      this.addModule(new XCarry());
      this.addModule(new Zoom());
      this.addModule(new FakeFly());
      this.addModule(new CriticalsPlus());
      this.addModule(new TrapdoorAura());
      this.modules.sort(Comparator.comparing(Mod::getName));
   }

   public void onKeyReleased(int eventKey) {
      if (eventKey != -1 && eventKey != 0) {
         this.handleKeyEvent(eventKey, false);
      }

   }

   public void onKeyPressed(int eventKey) {
      if (eventKey != -1 && eventKey != 0) {
         this.handleKeyEvent(eventKey, true);
      }

   }

   private void handleKeyEvent(int key, boolean isPressed) {
      for(Module module : this.modules) {
         BindSetting bindSetting = module.getBindSetting();
         if (bindSetting.getValue() == key) {
            if (isPressed && mc.field_1755 == null) {
               module.toggle();
               bindSetting.holding = true;
            } else if (!isPressed && bindSetting.isHoldEnable() && bindSetting.holding) {
               module.toggle();
               bindSetting.holding = false;
            }
         }

         for(Setting setting : module.getSettings()) {
            if (setting instanceof BindSetting bind) {
               if (bind.getValue() == key) {
                  bind.setPressed(isPressed);
               }
            }
         }
      }

   }

   public void onLogin() {
      for(Module module : this.modules) {
         if (module.isOn()) {
            module.onLogin();
         }
      }

   }

   public void onLogout() {
      for(Module module : this.modules) {
         if (module.isOn()) {
            module.onLogout();
         }
      }

   }

   public void onRender2D(class_332 drawContext) {
      for(Module module : this.modules) {
         if (module.isOn()) {
            try {
               module.onRender2D(drawContext, mc.method_60646().method_60637(true));
            } catch (Exception var6) {
               var6.printStackTrace();
               if (ClientSetting.INSTANCE.debug.getValue()) {
                  String var10000 = module.getName();
                  CommandManager.sendMessage("§4An error has occurred (" + var10000 + " [onRender2D]) Message: [" + var6.getMessage() + "]");
               }
            }
         }
      }

      try {
         Alien.EVENT_BUS.post(Render2DEvent.get(drawContext, mc.method_60646().method_60637(true)));
      } catch (Exception var5) {
         var5.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            CommandManager.sendMessage("§4An error has occurred (Render3DEvent) Message: [" + var5.getMessage() + "]");
         }
      }

      this.renderToggleBanners(drawContext);
   }

   public void render3D(class_4587 matrices) {
      GL11.glEnable(2848);

      for(Module module : this.modules) {
         if (module.isOn()) {
            try {
               module.onRender3D(matrices);
            } catch (Exception var6) {
               var6.printStackTrace();
               if (ClientSetting.INSTANCE.debug.getValue()) {
                  String var10000 = module.getName();
                  CommandManager.sendMessage("§4An error has occurred (" + var10000 + " [onRender3D]) Message: [" + var6.getMessage() + "]");
               }
            }
         }
      }

      try {
         Alien.EVENT_BUS.post(Render3DEvent.get(matrices, mc.method_60646().method_60637(true)));
      } catch (Exception var5) {
         var5.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            CommandManager.sendMessage("§4An error has occurred (Render3DEvent) Message: [" + var5.getMessage() + "]");
         }
      }

      GL11.glDisable(2848);
   }

   public void showToggleBanner(Module module, boolean enabled) {
      if (module != null && mc != null && ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.banner.getValue()) {
         if (ClickGui.getInstance() != null && ClickGui.getInstance().disableNotification.getValue()) {
            return;
         }

         if (this.interactiveToggleInBanner) {
            for(int i = this.toggleBanners.size() - 1; i >= 0; --i) {
               ToggleBanner b = (ToggleBanner)this.toggleBanners.get(i);
               if (b.name.equalsIgnoreCase(module.getDisplayName())) {
                  this.toggleBanners.set(i, new ToggleBanner(b.name, enabled, b.start));
                  break;
               }
            }

            this.interactiveToggleInBanner = false;
            return;
         }

         this.toggleBanners.add(new ToggleBanner(module.getDisplayName(), enabled, System.currentTimeMillis()));
         if (this.toggleBanners.size() > 5) {
            this.toggleBanners.remove(0);
         }

         if (ClientSetting.INSTANCE.bannerSound.getValue() && mc.method_1483() != null) {
            mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, ClientSetting.INSTANCE.bannerSoundPitch.getValueFloat()));
         }
      }

   }

   private void renderToggleBanners(class_332 ctx) {
      if (!this.toggleBanners.isEmpty() && mc != null && ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.banner.getValue()) {
         long now = System.currentTimeMillis();
         int sw = mc.method_22683().method_4486();
         int sh = mc.method_22683().method_4502();
         int rawW = mc.method_22683().method_4480();
         int rawH = mc.method_22683().method_4507();
         int mx = (int)(mc.field_1729.method_1603() * (double)sw / (double)Math.max(1, rawW));
         int my = (int)(mc.field_1729.method_1604() * (double)sh / (double)Math.max(1, rawH));
         boolean mouseDown = GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), 0) == 1;
         ArrayList<ToggleBanner> removeList = new ArrayList();
         long fi = (long)ClientSetting.INSTANCE.bannerFade.getValue();
         long hold = (long)ClientSetting.INSTANCE.bannerHold.getValue();
         if (ClientSetting.INSTANCE.onlyOne.getValue() && ClientSetting.INSTANCE.bannerStyle.getValue() == ClientSetting.BannerStyle.iOS) {
            long start = ((ToggleBanner)this.toggleBanners.get(0)).start;
            long elapsed = now - start;
            long total = fi + hold + fi;
            if (elapsed > total) {
               removeList.add((ToggleBanner)this.toggleBanners.get(0));
            }

            double alpha;
            double slide;
            if (elapsed < fi) {
               double p = Math.max((double)0.0F, Math.min((double)1.0F, (double)elapsed / (double)fi));
               double e = Easing.SineOut.ease(p);
               alpha = e;
               slide = (double)10.0F * e;
            } else if (elapsed < fi + hold) {
               alpha = (double)1.0F;
               slide = (double)10.0F;
            } else {
               double p = Math.max((double)0.0F, Math.min((double)1.0F, (double)(elapsed - fi - hold) / (double)fi));
               double e = Easing.SineInOut.ease(p);
               alpha = (double)1.0F - e;
               slide = (double)10.0F - (double)10.0F * e;
            }

            int a = (int)Math.max(0L, Math.min(255L, Math.round(alpha * (double)255.0F)));
            int maxRows = Math.min(3, this.toggleBanners.size());
            int rowH = 14;
            int pad = 10;
            int height = rowH * maxRows + pad * 2;
            int width = Math.max(180, (int)((double)sw * 0.36));
            int x = 14;
            int y = (int)slide;
            Color bg = new Color(255, 255, 255, (int)Math.round((double)a * 0.85));
            float radius = (float)height / 2.0F;
            Alien.BLUR.applyBlur(30.0F, (float)x, (float)y, (float)width, (float)height);
            Render2DUtil.drawPill(ctx.method_51448(), (float)x, (float)y, (float)width, (float)height, bg);
            Render2DUtil.drawRoundedStroke(ctx.method_51448(), (float)x, (float)y, (float)width, (float)height, radius, new Color(220, 224, 230, Math.min(180, a)), 48);
            Render2DUtil.drawGlow(ctx.method_51448(), (float)x - 3.0F, (float)y - 3.0F, (float)width + 6.0F, (float)height + 6.0F, (new Color(255, 255, 255, Math.min(54, a))).getRGB());
            Render2DUtil.drawLine(ctx.method_51448(), (float)x + radius, (float)y + 2.0F, (float)(x + width) - radius, (float)y + 2.0F, (new Color(255, 255, 255, Math.min(90, a))).getRGB());
            Render2DUtil.drawLine(ctx.method_51448(), (float)x + radius, (float)(y + height) - 2.2F, (float)(x + width) - radius, (float)(y + height) - 2.2F, (new Color(120, 130, 140, Math.min(70, a))).getRGB());
            int var10000 = x + pad;
            int gy = y + pad;

            for(int i = 0; i < maxRows; ++i) {
               ToggleBanner b = (ToggleBanner)this.toggleBanners.get(this.toggleBanners.size() - 1 - i);
               Module modCur = this.getModuleByDisplayName(b.name);
               boolean curEnabled = modCur != null && modCur.isOn();
               String text = (curEnabled ? "已开启 " : "已关闭 ") + b.name;
               Color textColor = new Color(30, 30, 30, a);
               int tw = ClickGui.getInstance().font.getValue() ? (int)FontManager.ui.getWidth(text) : (int)TextUtil.getWidth(text);
               int tx = x + (width - tw) / 2;
               double ty = (double)(gy + i * rowH);
               TextUtil.drawString(ctx, text, (double)tx, ty, textColor.getRGB(), ClickGui.getInstance().font.getValue());
               int toggleW = 28;
               int toggleH = 14;
               float swx = (float)(x + width - pad - toggleW);
               float swy = (float)(y + pad + i * rowH) + (float)(rowH - toggleH) / 2.0F;
               Color on = new Color(0, 120, 212, a);
               Color track = curEnabled ? new Color(on.getRed(), on.getGreen(), on.getBlue(), Math.min(180, a)) : new Color(238, 238, 238, Math.min(200, a));
               Render2DUtil.drawRoundedRect(ctx.method_51448(), swx, swy, (float)toggleW, (float)toggleH, 6.0F, track);
               float dotR = (float)toggleH / 2.0F - 2.0F;
               float dotCx = curEnabled ? swx + (float)toggleW - (float)toggleH / 2.0F : swx + (float)toggleH / 2.0F;
               float dotCy = swy + (float)toggleH / 2.0F;
               Render2DUtil.drawCircle(ctx.method_51448(), dotCx, dotCy, dotR, new Color(255, 255, 255, Math.min(230, a)), 80);
               if ((float)mx >= swx && (float)mx <= swx + (float)toggleW && (float)my >= swy && (float)my <= swy + (float)toggleH && mouseDown && !this.notificationMouseWasDown && modCur != null) {
                  this.interactiveToggleInBanner = true;
                  modCur.toggle();
               }
            }

            this.notificationMouseWasDown = mouseDown;
         } else {
            int baseY = 8;
            int height = 16;
            int gap = 4;
            int index = 0;

            for(ToggleBanner b : this.toggleBanners) {
               long elapsedx = now - b.start;
               long totalx = fi + hold + fi;
               if (elapsedx > totalx) {
                  removeList.add(b);
               } else {
                  double alpha;
                  double slide;
                  if (elapsedx < fi) {
                     double p = Math.max((double)0.0F, Math.min((double)1.0F, (double)elapsedx / (double)fi));
                     double e = Easing.SineOut.ease(p);
                     alpha = e;
                     slide = (double)(-height - 8) + (double)8.0F * e;
                  } else if (elapsedx < fi + hold) {
                     alpha = (double)1.0F;
                     slide = (double)8.0F;
                  } else {
                     double p = Math.max((double)0.0F, Math.min((double)1.0F, (double)(elapsedx - fi - hold) / (double)fi));
                     double e = Easing.SineInOut.ease(p);
                     alpha = (double)1.0F - e;
                     slide = (double)8.0F + (double)(-height - 8) * e;
                  }

                  int pad = 10;
                  Module modCur = this.getModuleByDisplayName(b.name);
                  boolean curEnabled = modCur != null && modCur.isOn();
                  String text = (curEnabled ? "已开启 " : "已关闭 ") + b.name;
                  int tw = ClickGui.getInstance().font.getValue() ? (int)FontManager.ui.getWidth(text) : (int)TextUtil.getWidth(text);
                  int width = Math.max(tw + pad * 2 + 8 + 30, 160);
                  int margin = 14;
                  int ay = (int)((double)margin + (ClientSetting.INSTANCE.bannerStack.getValue() == ClientSetting.StackDir.Down ? slide + (double)(index * (height + gap)) : slide - (double)(index * (height + gap))));
                  int a = (int)Math.max(0L, Math.min(255L, Math.round(alpha * (double)255.0F)));
                  int bgAlpha = (int)Math.round((double)a * 0.32);
                  Color bg = new Color(255, 255, 255, Math.min(220, a));
                  Color on = new Color(0, 120, 212, a);
                  new Color(204, 204, 204, a);
                  float radius = 3.5F;
                  Alien.BLUR.applyBlur(30.0F, (float)margin, (float)ay, (float)width, (float)height);
                  Render2DUtil.drawRoundedRect(ctx.method_51448(), (float)margin, (float)ay, (float)width, (float)height, radius, bg);
                  Render2DUtil.drawRoundedStroke(ctx.method_51448(), (float)margin, (float)ay, (float)width, (float)height, radius, new Color(220, 224, 230, Math.min(180, a)), 48);
                  Render2DUtil.drawGlow(ctx.method_51448(), (float)margin - 3.0F, (float)ay - 3.0F, (float)width + 6.0F, (float)height + 6.0F, (new Color(255, 255, 255, Math.min(54, a))).getRGB());
                  Render2DUtil.drawLine(ctx.method_51448(), (float)margin + 2.0F, (float)ay + 2.0F, (float)(margin + width) - 2.0F, (float)ay + 2.0F, (new Color(255, 255, 255, Math.min(90, a))).getRGB());
                  Render2DUtil.drawLine(ctx.method_51448(), (float)margin + 2.0F, (float)(ay + height) - 2.2F, (float)(margin + width) - 2.0F, (float)(ay + height) - 2.2F, (new Color(120, 130, 140, Math.min(70, a))).getRGB());
                  int toggleW = 28;
                  int toggleH = 14;
                  float tx = (float)(margin + width - pad - toggleW);
                  float ty = (float)ay + (float)(height - toggleH) / 2.0F;
                  Color track = curEnabled ? new Color(on.getRed(), on.getGreen(), on.getBlue(), Math.min(180, a)) : new Color(238, 238, 238, Math.min(200, a));
                  Render2DUtil.drawRoundedRect(ctx.method_51448(), tx, ty, (float)toggleW, (float)toggleH, 6.0F, track);
                  float dotR = (float)toggleH / 2.0F - 2.0F;
                  float dotCx = curEnabled ? tx + (float)toggleW - (float)toggleH / 2.0F : tx + (float)toggleH / 2.0F;
                  float dotCy = ty + (float)toggleH / 2.0F;
                  Render2DUtil.drawCircle(ctx.method_51448(), dotCx, dotCy, dotR, new Color(255, 255, 255, Math.min(230, a)), 80);
                  double textY = (double)ay + (double)((float)height - (ClickGui.getInstance().font.getValue() ? FontManager.ui.getFontHeight() : TextUtil.getHeight())) / (double)2.0F;
                  int textAreaW = width - pad * 2 - toggleW;
                  int textX = margin + pad + (textAreaW - tw) / 2;
                  Color textColor = new Color(30, 30, 30, a);
                  TextUtil.drawString(ctx, text, (double)textX, textY, textColor.getRGB(), ClickGui.getInstance().font.getValue());
                  if ((float)mx >= tx && (float)mx <= tx + (float)toggleW && (float)my >= ty && (float)my <= ty + (float)toggleH && mouseDown && !this.notificationMouseWasDown && modCur != null) {
                     this.interactiveToggleInBanner = true;
                     modCur.toggle();
                  }

                  ++index;
               }
            }
         }

         if (!removeList.isEmpty()) {
            this.toggleBanners.removeAll(removeList);
         }

         this.notificationMouseWasDown = mouseDown;
      }

   }

   private void renderPotionListLegacy(class_332 ctx) {
      if (mc.field_1724 != null) {
         int margin = 14;
         int startX = margin + 2;
         int startY = margin + 92;
         int pillH = 14;
         int pillPad = 6;
         int idx = 0;

         for(class_1293 se : mc.field_1724.method_6026()) {
            String name = ((class_1291)se.method_5579().comp_349()).method_5560().getString();
            int ticks = se.method_5584();
            int totalSec = Math.max(0, ticks / 20);
            int mm = totalSec / 60;
            int ss = totalSec % 60;
            String time = String.format("%d:%02d", mm, ss);
            String text = name + " " + time;
            int tw = ClickGui.getInstance().font.getValue() ? (int)FontManager.ui.getWidth(text) : (int)TextUtil.getWidth(text);
            int pillW = tw + pillPad * 2;
            int y = startY + idx * (pillH + 4);
            int a = 180;
            Render2DUtil.drawRoundedRect(ctx.method_51448(), (float)startX, (float)y, (float)pillW, (float)pillH, 4.0F, new Color(255, 255, 255, a));
            Render2DUtil.drawRoundedStroke(ctx.method_51448(), (float)startX, (float)y, (float)pillW, (float)pillH, 4.0F, new Color(220, 224, 230, 160), 48);
            int tx = startX + pillPad;
            double ty = (double)y + (double)((float)pillH - (ClickGui.getInstance().font.getValue() ? FontManager.ui.getFontHeight() : TextUtil.getHeight())) / (double)2.0F;
            TextUtil.drawString(ctx, text, (double)tx, ty, (new Color(30, 30, 30)).getRGB(), ClickGui.getInstance().font.getValue());
            ++idx;
            if (idx >= 5) {
               break;
            }
         }
      }

   }

   private void renderPotionBanners(class_332 ctx) {
   }

   private String formatDuration(int ticks) {
      int totalSec = Math.max(0, ticks / 20);
      int mm = totalSec / 60;
      int ss = totalSec % 60;
      return String.format("%d:%02d", mm, ss);
   }

   private Module getModuleByDisplayName(String name) {
      for(Module m : this.modules) {
         if (m.getDisplayName().equalsIgnoreCase(name) || m.getName().equalsIgnoreCase(name)) {
            return m;
         }
      }

      return null;
   }

   public void addModule(Module module) {
      this.modules.add(module);
   }

   public Module getModuleByName(String string) {
      for(Module module : this.modules) {
         if (module.getName().equalsIgnoreCase(string)) {
            return module;
         }
      }

      return null;
   }

   private static class ToggleBanner {
      public final String name;
      public final boolean enabled;
      public final long start;

      public ToggleBanner(String name, boolean enabled, long start) {
         this.name = name;
         this.enabled = enabled;
         this.start = start;
      }
   }
}
