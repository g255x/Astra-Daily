package dev.Astra.core.impl;

import dev.Astra.Astra;
import dev.Astra.api.events.impl.Render2DEvent;
import dev.Astra.api.events.impl.Render3DEvent;
import dev.Astra.api.utils.Wrapper;
import dev.Astra.api.utils.path.BaritoneUtil;
import dev.Astra.api.utils.render.Render2DUtil;
import dev.Astra.api.utils.render.TextUtil;
import dev.Astra.mod.Mod;
import dev.Astra.mod.gui.clickgui.ClickGuiScreen;
import dev.Astra.mod.modules.HudModule;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.client.BaritoneModule;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.client.ColorsModule;
import dev.Astra.mod.modules.impl.client.Fonts;
import dev.Astra.mod.modules.impl.client.NotificationsHud;
import dev.Astra.mod.modules.impl.client.Tips;
import dev.Astra.mod.modules.impl.combat.AntiCrawl;
import dev.Astra.mod.modules.impl.combat.AntiPhase;
import dev.Astra.mod.modules.impl.combat.AntiPistonPush;
import dev.Astra.mod.modules.impl.combat.AntiRegear;
import dev.Astra.mod.modules.impl.combat.AntiWeak;
import dev.Astra.mod.modules.impl.combat.Aura;
import dev.Astra.mod.modules.impl.combat.AutoAnchor;
import dev.Astra.mod.modules.impl.combat.AutoCrystal;
import dev.Astra.mod.modules.impl.combat.AutoMend;
import dev.Astra.mod.modules.impl.combat.AutoPush;
import dev.Astra.mod.modules.impl.combat.AutoTrap;
import dev.Astra.mod.modules.impl.combat.AutoWeb;
import dev.Astra.mod.modules.impl.combat.Blocker;
import dev.Astra.mod.modules.impl.combat.Breaker;
import dev.Astra.mod.modules.impl.combat.Burrow;
import dev.Astra.mod.modules.impl.combat.CevBreaker;
import dev.Astra.mod.modules.impl.combat.Criticals;
import dev.Astra.mod.modules.impl.combat.Follower;
import dev.Astra.mod.modules.impl.combat.ForceEat;
import dev.Astra.mod.modules.impl.combat.HoleFiller;
import dev.Astra.mod.modules.impl.combat.Offhand;
import dev.Astra.mod.modules.impl.combat.PistonCrystal;
import dev.Astra.mod.modules.impl.combat.Quiver;
import dev.Astra.mod.modules.impl.combat.SelfTrap;
import dev.Astra.mod.modules.impl.combat.Surround;
import dev.Astra.mod.modules.impl.exploit.AntiHunger;
import dev.Astra.mod.modules.impl.exploit.AntiPacket;
import dev.Astra.mod.modules.impl.exploit.Blink;
import dev.Astra.mod.modules.impl.exploit.BowBomb;
import dev.Astra.mod.modules.impl.exploit.ChorusControl;
import dev.Astra.mod.modules.impl.exploit.ChunkESP;
import dev.Astra.mod.modules.impl.exploit.Clip;
import dev.Astra.mod.modules.impl.exploit.InfiniteTrident;
import dev.Astra.mod.modules.impl.exploit.MaceSpoof;
import dev.Astra.mod.modules.impl.exploit.PacketControl;
import dev.Astra.mod.modules.impl.exploit.Phase;
import dev.Astra.mod.modules.impl.exploit.PingSpoof;
import dev.Astra.mod.modules.impl.exploit.RocketExtend;
import dev.Astra.mod.modules.impl.exploit.ServerLagger;
import dev.Astra.mod.modules.impl.exploit.SkinFlicker;
import dev.Astra.mod.modules.impl.exploit.TeleportLogger;
import dev.Astra.mod.modules.impl.exploit.XCarry;
import dev.Astra.mod.modules.impl.hud.ArmorHudModule;
import dev.Astra.mod.modules.impl.hud.ArrayListHudModule;
import dev.Astra.mod.modules.impl.hud.CoordsHudModule;
import dev.Astra.mod.modules.impl.hud.HudSetting;
import dev.Astra.mod.modules.impl.hud.InfoHudModule;
import dev.Astra.mod.modules.impl.hud.ItemCounterHudModule;
import dev.Astra.mod.modules.impl.hud.TextRadar;
import dev.Astra.mod.modules.impl.hud.WaterMarkHudModule;
import dev.Astra.mod.modules.impl.misc.AddFriend;
import dev.Astra.mod.modules.impl.misc.AutoEZ;
import dev.Astra.mod.modules.impl.misc.AutoKit;
import dev.Astra.mod.modules.impl.misc.AutoLog;
import dev.Astra.mod.modules.impl.misc.AutoReconnect;
import dev.Astra.mod.modules.impl.misc.AutoRegear;
import dev.Astra.mod.modules.impl.misc.ChatAppend;
import dev.Astra.mod.modules.impl.misc.ExtraTab;
import dev.Astra.mod.modules.impl.misc.FakePlayer;
import dev.Astra.mod.modules.impl.misc.KillEffect;
import dev.Astra.mod.modules.impl.misc.LavaFiller;
import dev.Astra.mod.modules.impl.misc.NoSound;
import dev.Astra.mod.modules.impl.misc.Nuker;
import dev.Astra.mod.modules.impl.misc.PacketLogger;
import dev.Astra.mod.modules.impl.misc.PopEz;
import dev.Astra.mod.modules.impl.misc.ShulkerViewer;
import dev.Astra.mod.modules.impl.misc.Spammer;
import dev.Astra.mod.modules.impl.movement.AntiVoid;
import dev.Astra.mod.modules.impl.movement.AutoWalk;
import dev.Astra.mod.modules.impl.movement.ElytraFly;
import dev.Astra.mod.modules.impl.movement.EntityControl;
import dev.Astra.mod.modules.impl.movement.FastFall;
import dev.Astra.mod.modules.impl.movement.FastSwim;
import dev.Astra.mod.modules.impl.movement.FastWeb;
import dev.Astra.mod.modules.impl.movement.Flatten;
import dev.Astra.mod.modules.impl.movement.Fly;
import dev.Astra.mod.modules.impl.movement.HoleSnap;
import dev.Astra.mod.modules.impl.movement.NoSlow;
import dev.Astra.mod.modules.impl.movement.PacketFly;
import dev.Astra.mod.modules.impl.movement.SafeWalk;
import dev.Astra.mod.modules.impl.movement.Scaffold;
import dev.Astra.mod.modules.impl.movement.Speed;
import dev.Astra.mod.modules.impl.movement.Sprint;
import dev.Astra.mod.modules.impl.movement.Step;
import dev.Astra.mod.modules.impl.movement.Strafe;
import dev.Astra.mod.modules.impl.movement.TpAura;
import dev.Astra.mod.modules.impl.movement.VClip;
import dev.Astra.mod.modules.impl.movement.Velocity;
import dev.Astra.mod.modules.impl.player.AirPlace;
import dev.Astra.mod.modules.impl.player.AntiEffects;
import dev.Astra.mod.modules.impl.player.AutoArmor;
import dev.Astra.mod.modules.impl.player.AutoPearl;
import dev.Astra.mod.modules.impl.player.AutoPot;
import dev.Astra.mod.modules.impl.player.AutoTool;
import dev.Astra.mod.modules.impl.player.FreeLook;
import dev.Astra.mod.modules.impl.player.Freecam;
import dev.Astra.mod.modules.impl.player.IQBoost;
import dev.Astra.mod.modules.impl.player.InteractTweaks;
import dev.Astra.mod.modules.impl.player.NoFall;
import dev.Astra.mod.modules.impl.player.PacketEat;
import dev.Astra.mod.modules.impl.player.PacketMine;
import dev.Astra.mod.modules.impl.player.Replenish;
import dev.Astra.mod.modules.impl.player.Sorter;
import dev.Astra.mod.modules.impl.player.TimerModule;
import dev.Astra.mod.modules.impl.player.Yaw;
import dev.Astra.mod.modules.impl.render.Ambience;
import dev.Astra.mod.modules.impl.render.AspectRatio;
import dev.Astra.mod.modules.impl.render.BreakESP;
import dev.Astra.mod.modules.impl.render.CameraClip;
import dev.Astra.mod.modules.impl.render.Chams;
import dev.Astra.mod.modules.impl.render.Crosshair;
import dev.Astra.mod.modules.impl.render.ESP;
import dev.Astra.mod.modules.impl.render.Fov;
import dev.Astra.mod.modules.impl.render.HighLight;
import dev.Astra.mod.modules.impl.render.HoleESP;
import dev.Astra.mod.modules.impl.render.LogoutSpots;
import dev.Astra.mod.modules.impl.render.MotionCamera;
import dev.Astra.mod.modules.impl.render.NameTags;
import dev.Astra.mod.modules.impl.render.NoRender;
import dev.Astra.mod.modules.impl.render.PhaseESP;
import dev.Astra.mod.modules.impl.render.PlaceRender;
import dev.Astra.mod.modules.impl.render.PopChams;
import dev.Astra.mod.modules.impl.render.ShaderModule;
import dev.Astra.mod.modules.impl.render.Skybox;
import dev.Astra.mod.modules.impl.render.TotemParticle;
import dev.Astra.mod.modules.impl.render.Tracers;
import dev.Astra.mod.modules.impl.render.Trajectories;
import dev.Astra.mod.modules.impl.render.ViewModel;
import dev.Astra.mod.modules.impl.render.Xray;
import dev.Astra.mod.modules.impl.render.Zoom;
import dev.Astra.mod.modules.settings.Setting;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.lwjgl.opengl.GL11;

public class ModuleManager implements Wrapper {
   private final ArrayList<Module> modules = new ArrayList();

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

      this.addModule(new AutoKit());
      this.addModule(new ForceEat());
      this.addModule(new Fonts());
      this.addModule(new AutoCrystal());
      this.addModule(new Ambience());
      this.addModule(new AntiHunger());
      this.addModule(new AntiVoid());
      this.addModule(new AutoWalk());
      this.addModule(new VClip());
      this.addModule(new ExtraTab());
      this.addModule(new AntiWeak());
      this.addModule(new AddFriend());
      this.addModule(new AspectRatio());
      this.addModule(new Follower());
      this.addModule(new ChunkESP());
      this.addModule(new ColorsModule());
      this.addModule(new Aura());
      this.addModule(new PistonCrystal());
      this.addModule(new AutoAnchor());
      this.addModule(new PhaseESP());
      this.addModule(new AutoArmor());
      this.addModule(new Breaker());
      this.addModule(new AutoLog());
      this.addModule(new AntiPistonPush());
      this.addModule(new AutoEZ());
      this.addModule(new PopEz());
      this.addModule(new SelfTrap());
      this.addModule(new Sorter());
      this.addModule(new AutoMend());
      this.addModule(new AutoPot());
      this.addModule(new AutoPush());
      this.addModule(new Offhand());
      this.addModule(new Nuker());
      this.addModule(new AutoTrap());
      this.addModule(new AutoWeb());
      this.addModule(new Blink());
      this.addModule(new ChorusControl());
      this.addModule(new FastSwim());
      this.addModule(new Blocker());
      this.addModule(new Quiver());
      this.addModule(new BowBomb());
      this.addModule(new BreakESP());
      this.addModule(new Burrow());
      this.addModule(new MaceSpoof());
      this.addModule(new CameraClip());
      this.addModule(new ChatAppend());
      this.addModule(new ClickGui());
      this.addModule(new InfiniteTrident());
      this.addModule(new AutoRegear());
      this.addModule(new LavaFiller());
      this.addModule(new AntiPhase());
      this.addModule(new Clip());
      this.addModule(new AntiCheat());
      this.addModule(new ItemCounterHudModule("Items", "物品", 100, 100));
      this.addModule(new Fov());
      this.addModule(new Criticals());
      this.addModule(new CevBreaker());
      this.addModule(new Crosshair());
      this.addModule(new Chams());
      this.addModule(new AntiPacket());
      this.addModule(new AutoReconnect());
      this.addModule(new ESP());
      this.addModule(new HoleESP());
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
      this.addModule(new HudSetting());
      this.addModule(new NotificationsHud());
      this.addModule(new TextRadar());
      this.addModule(new ArmorHudModule());
      this.addModule(new WaterMarkHudModule());
      this.addModule(new ArrayListHudModule());
      this.addModule(new CoordsHudModule());
      this.addModule(new InfoHudModule());
      this.addModule(new RocketExtend());
      this.addModule(new HoleFiller());
      this.addModule(new HoleSnap());
      this.addModule(new LogoutSpots());
      this.addModule(new AutoTool());
      this.addModule(new Trajectories());
      this.addModule(new KillEffect());
      this.addModule(new AutoPearl());
      this.addModule(new AntiEffects());
      this.addModule(new IQBoost());
      this.addModule(new NoFall());
      this.addModule(new NoRender());
      this.addModule(new NoSlow());
      this.addModule(new NoSound());
      this.addModule(new AirPlace());
      this.addModule(new Xray());
      this.addModule(new PacketEat());
      this.addModule(new PacketFly());
      this.addModule(new PacketMine());
      this.addModule(new PacketControl());
      this.addModule(new Phase());
      this.addModule(new PlaceRender());
      this.addModule(new InteractTweaks());
      this.addModule(new PopChams());
      this.addModule(new Replenish());
      this.addModule(new ServerLagger());
      this.addModule(new Scaffold());
      this.addModule(new ShaderModule());
      this.addModule(new Skybox());
      this.addModule(new AntiCrawl());
      this.addModule(new AntiRegear());
      this.addModule(new SafeWalk());
      this.addModule(new Speed());
      this.addModule(new Sprint());
      this.addModule(new Strafe());
      this.addModule(new Step());
      this.addModule(new TpAura());
      this.addModule(new Surround());
      this.addModule(new TotemParticle());
      this.addModule(new Velocity());
      this.addModule(new ViewModel());
      this.addModule(new Tracers());
      this.addModule(new XCarry());
      this.addModule(new Zoom());
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
            BindSetting bind;
            if (setting instanceof BindSetting && (bind = (BindSetting)setting).getValue() == key) {
               bind.setPressed(isPressed);
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
      boolean skipHudModules = false;
      if (mc.field_1755 instanceof ClickGuiScreen) {
         skipHudModules = ((ClickGuiScreen)mc.field_1755).getPage() == ClickGuiScreen.Page.Hud;
      }

      for(Module module : this.modules) {
         if (module.isOn() && (!skipHudModules || !(module instanceof HudModule))) {
            try {
               module.onRender2D(drawContext, mc.method_60646().method_60637(true));
            } catch (Exception e) {
               e.printStackTrace();
               if (ClientSetting.INSTANCE.debug.getValue()) {
                  String var10000 = module.getName();
                  CommandManager.sendMessage("§4An error has occurred (" + var10000 + " [onRender2D]) Message: [" + e.getMessage() + "]");
               }
            }
         }
      }

      try {
         Astra.EVENT_BUS.post(Render2DEvent.get(drawContext, mc.method_60646().method_60637(true)));
      } catch (Exception e) {
         e.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            CommandManager.sendMessage("§4An error has occurred (Render3DEvent) Message: [" + e.getMessage() + "]");
         }
      }

   }

   public void render3D(class_4587 matrices) {
      GL11.glEnable(2848);

      for(Module module : this.modules) {
         if (module.isOn()) {
            try {
               module.onRender3D(matrices);
            } catch (Exception e) {
               e.printStackTrace();
               if (ClientSetting.INSTANCE.debug.getValue()) {
                  String var10000 = module.getName();
                  CommandManager.sendMessage("§4An error has occurred (" + var10000 + " [onRender3D]) Message: [" + e.getMessage() + "]");
               }
            }
         }
      }

      try {
         Astra.EVENT_BUS.post(Render3DEvent.get(matrices, mc.method_60646().method_60637(true)));
      } catch (Exception e) {
         e.printStackTrace();
         if (ClientSetting.INSTANCE.debug.getValue()) {
            CommandManager.sendMessage("§4An error has occurred (Render3DEvent) Message: [" + e.getMessage() + "]");
         }
      }

      GL11.glDisable(2848);
   }

   public void showToggleBanner(Module module, boolean enabled) {
      NotificationsHud.addModuleNotification(module.getDisplayName(), enabled);
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
            boolean customFont = FontManager.isCustomFontEnabled();
            int tw = customFont ? (int)FontManager.ui.getWidth(text) : (int)TextUtil.getWidth(text);
            int pillW = tw + pillPad * 2;
            int y = startY + idx * (pillH + 4);
            int keyCodec = 180;
            Render2DUtil.drawRoundedRect(ctx.method_51448(), (float)startX, (float)y, (float)pillW, (float)pillH, 4.0F, new Color(255, 255, 255, keyCodec));
            Render2DUtil.drawRoundedStroke(ctx.method_51448(), (float)startX, (float)y, (float)pillW, (float)pillH, 4.0F, new Color(220, 224, 230, 160), 48);
            int tx = startX + pillPad;
            double ty = (double)y + (double)((float)pillH - (customFont ? FontManager.ui.getFontHeight() : TextUtil.getHeight())) / (double)2.0F;
            TextUtil.drawString(ctx, text, (double)tx, ty, (new Color(30, 30, 30)).getRGB(), customFont);
            ++idx;
            if (idx >= 5) {
               break;
            }
         }

      }
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
}
