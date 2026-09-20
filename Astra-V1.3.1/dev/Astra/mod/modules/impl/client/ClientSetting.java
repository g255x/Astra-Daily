package dev.Astra.mod.modules.impl.client;

import dev.Astra.api.utils.math.Animation;
import dev.Astra.api.utils.math.Easing;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.awt.Color;

public class ClientSetting extends Module {
   public static final Animation animation = new Animation();
   public static ClientSetting INSTANCE;
   public final EnumSetting<Page> page;
   public final BooleanSetting lowVersion;
   public final BooleanSetting crawl;
   public final BooleanSetting rotations;
   public final BooleanSetting sync;
   public final BooleanSetting titleFix;
   public final BooleanSetting fuckFPSLimit;
   private final BooleanSetting portalGui;
   public final BooleanSetting optimizedCalc;
   public final BooleanSetting mioCompatible;
   public final StringSetting prefix;
   public final BooleanSetting chinese;
   public final BooleanSetting titleOverride;
   public final StringSetting windowTitle;
   public final BooleanSetting debug;
   public final BooleanSetting caughtException;
   public final BooleanSetting log;
   private final BooleanSetting hotbar;
   public final SliderSetting hotbarTime;
   public final EnumSetting<Easing> animEase;
   public final BooleanSetting darkening;
   public final StringSetting hackName;
   public final ColorSetting color;
   public final EnumSetting<Style> messageStyle;
   public final BooleanSetting toggle;
   public final BooleanSetting onlyOne;
   public final BooleanSetting banner;
   public final EnumSetting<BannerStyle> bannerStyle;
   public final EnumSetting<StackDir> bannerStack;
   public final SliderSetting bannerFade;
   public final SliderSetting bannerHold;
   public final BooleanSetting bannerSound;
   public final SliderSetting bannerSoundPitch;
   public final BooleanSetting keepHistory;
   public final BooleanSetting infiniteChat;
   public final BooleanSetting hideIndicator;
   public final SliderSetting animationTime;
   public final EnumSetting<Easing> ease;
   public final SliderSetting animateOffset;
   public final BooleanSetting fade;

   public ClientSetting() {
      super("ClientSetting", Module.Category.Client);
      this.page = this.add(new EnumSetting("Page", ClientSetting.Page.Game));
      this.lowVersion = this.add(new BooleanSetting("1.12", false, () -> this.page.is(ClientSetting.Page.Game)));
      this.crawl = this.add(new BooleanSetting("Crawl", true, () -> this.page.is(ClientSetting.Page.Game)));
      this.rotations = this.add((new BooleanSetting("ShowRotations", true, () -> this.page.is(ClientSetting.Page.Game))).setParent());
      this.sync = this.add(new BooleanSetting("Sync", false, () -> this.page.is(ClientSetting.Page.Game) && this.rotations.isOpen()));
      this.titleFix = this.add(new BooleanSetting("TitleFix", true, () -> this.page.is(ClientSetting.Page.Game)));
      this.fuckFPSLimit = this.add(new BooleanSetting("FuckFPSLimit", false, () -> this.page.is(ClientSetting.Page.Game)));
      this.portalGui = this.add(new BooleanSetting("BlockTickNausea", true, () -> this.page.is(ClientSetting.Page.Game)));
      this.optimizedCalc = this.add(new BooleanSetting("OptimizedCalc", false, () -> this.page.is(ClientSetting.Page.Game)));
      this.mioCompatible = this.add(new BooleanSetting("MioCompatible", false, () -> this.page.is(ClientSetting.Page.Game)));
      this.prefix = this.add(new StringSetting("Prefix", ";", () -> this.page.is(ClientSetting.Page.Misc)));
      this.chinese = this.add(new BooleanSetting("Chinese", false, () -> this.page.is(ClientSetting.Page.Misc)));
      this.titleOverride = this.add((new BooleanSetting("TitleOverride", true, () -> this.page.is(ClientSetting.Page.Misc))).setParent());
      this.windowTitle = this.add(new StringSetting("WindowTitle", "Astra Client", () -> this.page.is(ClientSetting.Page.Misc) && this.titleOverride.isOpen()));
      this.debug = this.add(new BooleanSetting("DebugException", true, () -> this.page.is(ClientSetting.Page.Misc)));
      this.caughtException = this.add((new BooleanSetting("CaughtException", false, () -> this.page.is(ClientSetting.Page.Misc))).setParent());
      this.log = this.add(new BooleanSetting("Log", true, () -> this.page.is(ClientSetting.Page.Misc) && this.caughtException.isOpen()));
      this.hotbar = this.add(new BooleanSetting("HotbarAnim", true, () -> this.page.is(ClientSetting.Page.Gui)));
      this.hotbarTime = this.add(new SliderSetting("HotbarTime", 100, 0, 1000, () -> this.page.is(ClientSetting.Page.Gui)));
      this.animEase = this.add(new EnumSetting("AnimEase", Easing.CubicInOut, () -> this.page.is(ClientSetting.Page.Gui)));
      this.darkening = this.add(new BooleanSetting("Darkening", true, () -> this.page.is(ClientSetting.Page.Gui)));
      this.hackName = this.add(new StringSetting("Notification", "[Astra Client]", () -> this.page.getValue() == ClientSetting.Page.Notification));
      this.color = this.add(new ColorSetting("Color", new Color(255, 38, 38), () -> this.page.getValue() == ClientSetting.Page.Notification));
      this.messageStyle = this.add(new EnumSetting("Style", ClientSetting.Style.Mio, () -> this.page.getValue() == ClientSetting.Page.Notification));
      this.toggle = this.add((new BooleanSetting("ModuleToggle", true, () -> this.page.getValue() == ClientSetting.Page.Notification)).setParent());
      this.onlyOne = this.add(new BooleanSetting("OnlyOne", false, () -> this.page.getValue() == ClientSetting.Page.Notification && this.toggle.isOpen()));
      this.banner = this.add((new BooleanSetting("ToggleBanner", true, () -> this.page.getValue() == ClientSetting.Page.Notification)).setParent());
      this.bannerStyle = this.add(new EnumSetting("BannerStyle", ClientSetting.BannerStyle.iOS, () -> this.page.getValue() == ClientSetting.Page.Notification && this.banner.isOpen()));
      this.bannerStack = this.add(new EnumSetting("BannerStack", ClientSetting.StackDir.Down, () -> this.page.getValue() == ClientSetting.Page.Notification && this.banner.isOpen()));
      this.bannerFade = this.add(new SliderSetting("BannerFade", 160, 0, 1000, () -> this.page.getValue() == ClientSetting.Page.Notification && this.banner.isOpen()));
      this.bannerHold = this.add(new SliderSetting("BannerHold", 1000, 0, 3000, () -> this.page.getValue() == ClientSetting.Page.Notification && this.banner.isOpen()));
      this.bannerSound = this.add((new BooleanSetting("BannerSound", true, () -> this.page.getValue() == ClientSetting.Page.Notification && this.banner.isOpen())).setParent());
      this.bannerSoundPitch = this.add(new SliderSetting("BannerPitch", (double)1.0F, (double)0.5F, (double)2.0F, 0.05, () -> this.page.getValue() == ClientSetting.Page.Notification && this.banner.isOpen() && this.bannerSound.getValue()));
      this.keepHistory = this.add(new BooleanSetting("KeepHistory", true, () -> this.page.getValue() == ClientSetting.Page.ChatHud));
      this.infiniteChat = this.add(new BooleanSetting("InfiniteChat", true, () -> this.page.getValue() == ClientSetting.Page.ChatHud));
      this.hideIndicator = this.add(new BooleanSetting("HideIndicator", true, () -> this.page.getValue() == ClientSetting.Page.ChatHud));
      this.animationTime = this.add(new SliderSetting("AnimationTime", 300, 0, 1000, () -> this.page.getValue() == ClientSetting.Page.ChatHud));
      this.ease = this.add(new EnumSetting("Ease", Easing.CubicInOut, () -> this.page.getValue() == ClientSetting.Page.ChatHud));
      this.animateOffset = this.add(new SliderSetting("Offset", -40, -200, 100, () -> this.page.getValue() == ClientSetting.Page.ChatHud));
      this.fade = this.add(new BooleanSetting("Fade", true, () -> this.page.getValue() == ClientSetting.Page.ChatHud));
      this.setChinese("客户端设置");
      INSTANCE = this;
   }

   public boolean portalGui() {
      return this.isOn() && this.portalGui.getValue();
   }

   public boolean hotbar() {
      return this.isOn() && this.hotbar.getValue();
   }

   public void enable() {
      this.state = true;
   }

   public void disable() {
      this.state = true;
   }

   public boolean isOn() {
      return true;
   }

   public static enum Page {
      Game,
      Gui,
      Misc,
      Notification,
      ChatHud;

      // $FF: synthetic method
      private static Page[] $values() {
         return new Page[]{Game, Gui, Misc, Notification, ChatHud};
      }
   }

   public static enum Style {
      Mio,
      Debug,
      Lowercase,
      Normal,
      Future,
      Earth,
      Moon,
      Melon,
      Chinese,
      None;

      // $FF: synthetic method
      private static Style[] $values() {
         return new Style[]{Mio, Debug, Lowercase, Normal, Future, Earth, Moon, Melon, Chinese, None};
      }
   }

   public static enum BannerStyle {
      iOS,
      Classic;

      // $FF: synthetic method
      private static BannerStyle[] $values() {
         return new BannerStyle[]{iOS, Classic};
      }
   }

   public static enum StackDir {
      Down,
      Up;

      // $FF: synthetic method
      private static StackDir[] $values() {
         return new StackDir[]{Down, Up};
      }
   }
}
