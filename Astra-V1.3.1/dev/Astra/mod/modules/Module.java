package dev.Astra.mod.modules;

import dev.Astra.Astra;
import dev.Astra.core.impl.CommandManager;
import dev.Astra.mod.Mod;
import dev.Astra.mod.modules.impl.client.AntiCheat;
import dev.Astra.mod.modules.impl.client.BaritoneModule;
import dev.Astra.mod.modules.impl.client.ClickGui;
import dev.Astra.mod.modules.impl.client.ClientSetting;
import dev.Astra.mod.modules.impl.client.ColorsModule;
import dev.Astra.mod.modules.impl.client.hud.HudSetting;
import dev.Astra.mod.modules.settings.Setting;
import dev.Astra.mod.modules.settings.impl.BindSetting;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.EnumSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import dev.Astra.mod.modules.settings.impl.StringSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_4587;
import net.minecraft.class_7202;
import net.minecraft.class_7204;

public abstract class Module extends Mod {
   public final BooleanSetting drawn;
   private final List<Setting> settings;
   private final String description;
   private final Category category;
   private final BindSetting bindSetting;
   protected boolean state;
   private String chinese;

   public Module(String name, Category category) {
      this(name, "", category);
   }

   public Module(String name, String description, Category category) {
      super(name);
      this.settings = new ArrayList();
      this.category = category;
      this.description = description;
      this.bindSetting = this.add(new BindSetting("Key", this.isGuiModule() ? 344 : -1));
      this.drawn = this.add(new BooleanSetting("Drawn", !this.hideInModuleList()));
   }

   private boolean isGuiModule() {
      return this instanceof ClickGui;
   }

   private boolean hideInModuleList() {
      return this instanceof ColorsModule || this instanceof BaritoneModule || this instanceof AntiCheat || this instanceof ClientSetting || this instanceof HudSetting || this.getName().equals("Info");
   }

   public void setChinese(String chinese) {
      this.chinese = chinese;
   }

   public String getArrayName() {
      String var10000 = this.getDisplayName();
      return var10000 + this.getArrayInfo();
   }

   public String getArrayInfo() {
      return this.getInfo() == null ? "" : " §7[§f" + this.getInfo() + "§7]";
   }

   public String getInfo() {
      return null;
   }

   public String getDisplayName() {
      return ClientSetting.INSTANCE.chinese.getValue() && this.chinese != null ? this.chinese : this.getName();
   }

   public String getDescription() {
      return this.description;
   }

   public Category getCategory() {
      return this.category;
   }

   public BindSetting getBindSetting() {
      return this.bindSetting;
   }

   public boolean isOn() {
      return this.state;
   }

   public boolean isOff() {
      return !this.isOn();
   }

   public void toggle() {
      if (this.isOn()) {
         this.disable();
      } else {
         this.enable();
      }

   }

   public void enable() {
      if (!this.state) {
         if (!nullCheck() && this.drawn.getValue() && ClientSetting.INSTANCE.toggle.getValue()) {
            int id = ClientSetting.INSTANCE.onlyOne.getValue() ? -1 : this.hashCode();
            switch ((ClientSetting.Style)ClientSetting.INSTANCE.messageStyle.getValue()) {
               case Mio -> CommandManager.sendMessageId("§2[+] §f" + this.getDisplayName(), id);
               case Debug -> CommandManager.sendMessageId(this.getCategory().name().toLowerCase() + "." + this.getDisplayName().toLowerCase() + ".§aenable", id);
               case Lowercase -> CommandManager.sendMessageId(this.getDisplayName().toLowerCase() + " §aenabled", id);
               case Melon -> CommandManager.sendMessageId("§b" + this.getDisplayName() + " §aEnabled.", id);
               case Normal -> CommandManager.sendMessageId("§f" + this.getDisplayName() + " §aEnabled", id);
               case Future -> CommandManager.sendMessageId("§7" + this.getDisplayName() + " toggled §aon", id);
               case Chinese -> CommandManager.sendMessageId(this.getDisplayName() + " §a开启", id);
               case Moon -> CommandManager.sendChatMessageWidthIdNoSync("§f[§b" + ClientSetting.INSTANCE.hackName.getValue() + "§f] [§3" + this.getDisplayName() + "§f] §7toggled §aon", id);
               case Earth -> CommandManager.sendChatMessageWidthIdNoSync("§l" + this.getDisplayName() + " §aenabled.", id);
            }
         }

         if (Astra.MODULE != null) {
            Astra.MODULE.showToggleBanner(this, true);
         }

         this.state = true;
         Astra.EVENT_BUS.subscribe(this);
         this.onToggle();
         this.onEnable();
      }
   }

   public void disable() {
      if (this.state) {
         if (!nullCheck() && this.drawn.getValue() && ClientSetting.INSTANCE.toggle.getValue()) {
            int id = ClientSetting.INSTANCE.onlyOne.getValue() ? -1 : this.hashCode();
            switch ((ClientSetting.Style)ClientSetting.INSTANCE.messageStyle.getValue()) {
               case Mio -> CommandManager.sendMessageId("§4[-] §f" + this.getDisplayName(), id);
               case Debug -> CommandManager.sendMessageId(this.getCategory().name().toLowerCase() + "." + this.getDisplayName().toLowerCase() + ".§cdisable", id);
               case Lowercase -> CommandManager.sendMessageId(this.getDisplayName().toLowerCase() + " §cdisabled", id);
               case Melon -> CommandManager.sendMessageId("§b" + this.getDisplayName() + " §cDisabled.", id);
               case Normal -> CommandManager.sendMessageId("§f" + this.getDisplayName() + " §cDisabled", id);
               case Future -> CommandManager.sendMessageId("§7" + this.getDisplayName() + " toggled §coff", id);
               case Chinese -> CommandManager.sendMessageId(this.getDisplayName() + " §c关闭", id);
               case Moon -> CommandManager.sendChatMessageWidthIdNoSync("§f[§b" + ClientSetting.INSTANCE.hackName.getValue() + "§f] [§3" + this.getDisplayName() + "§f] §7toggled §coff", id);
               case Earth -> CommandManager.sendChatMessageWidthIdNoSync("§l" + this.getDisplayName() + " §cdisabled.", id);
            }
         }

         if (Astra.MODULE != null) {
            Astra.MODULE.showToggleBanner(this, false);
         }

         this.state = false;
         Astra.EVENT_BUS.unsubscribe(this);
         this.onToggle();
         this.onDisable();
      }
   }

   public void sendMessage(String message) {
      CommandManager.sendMessage(message);
   }

   public void setState(boolean state) {
      if (this.state != state) {
         if (state) {
            this.enable();
         } else {
            this.disable();
         }

      }
   }

   public boolean setBind(String rkey) {
      if (rkey.equalsIgnoreCase("none")) {
         this.bindSetting.setValue(-1);
         return true;
      } else {
         int key;
         try {
            key = class_3675.method_15981("key.keyboard." + rkey.toLowerCase()).method_1444();
         } catch (NumberFormatException var4) {
            if (!nullCheck()) {
               this.sendMessage("§4Bad bind!");
            }

            return false;
         }

         if (rkey.equalsIgnoreCase("none")) {
            key = -1;
         }

         if (key == 0) {
            return false;
         } else {
            this.bindSetting.setValue(key);
            return true;
         }
      }
   }

   public void onDisable() {
   }

   public void onEnable() {
   }

   public void onToggle() {
   }

   public void onLogin() {
   }

   public void onLogout() {
   }

   public void onRender2D(class_332 drawContext, float tickDelta) {
   }

   public void onRender3D(class_4587 matrixStack) {
   }

   public void addSetting(Setting setting) {
      this.settings.add(setting);
   }

   public StringSetting add(StringSetting setting) {
      this.addSetting(setting);
      return setting;
   }

   public ColorSetting add(ColorSetting setting) {
      this.addSetting(setting);
      return setting;
   }

   public SliderSetting add(SliderSetting setting) {
      this.addSetting(setting);
      return setting;
   }

   public BooleanSetting add(BooleanSetting setting) {
      this.addSetting(setting);
      return setting;
   }

   public <T extends Enum<T>> EnumSetting<T> add(EnumSetting<T> setting) {
      this.addSetting(setting);
      return setting;
   }

   public BindSetting add(BindSetting setting) {
      this.addSetting(setting);
      return setting;
   }

   public List<Setting> getSettings() {
      return this.settings;
   }

   public static boolean nullCheck() {
      return mc.field_1724 == null || mc.field_1724.field_3913 == null || mc.field_1687 == null;
   }

   public static void sendSequencedPacket(class_7204 packetCreator) {
      if (mc.method_1562() != null && mc.field_1687 != null) {
         class_7202 pendingUpdateManager = mc.field_1687.method_41925().method_41937();

         try {
            int i = pendingUpdateManager.method_41942();
            mc.method_1562().method_52787(packetCreator.predict(i));
         } catch (Throwable var5) {
            if (pendingUpdateManager != null) {
               try {
                  pendingUpdateManager.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (pendingUpdateManager != null) {
            pendingUpdateManager.close();
         }

      }
   }

   public static enum Category {
      Combat {
         public String getIcon() {
            return "b";
         }
      },
      Misc {
         public String getIcon() {
            return "[";
         }
      },
      Render {
         public String getIcon() {
            return "a";
         }
      },
      Movement {
         public String getIcon() {
            return "8";
         }
      },
      Player {
         public String getIcon() {
            return "5";
         }
      },
      Exploit {
         public String getIcon() {
            return "6";
         }
      },
      Client {
         public String getIcon() {
            return "7";
         }
      };

      public abstract String getIcon();

      public String getDisplayName() {
         if (ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.chinese.getValue()) {
            switch (this.ordinal()) {
               case 0 -> {
                  return "战斗类";
               }
               case 1 -> {
                  return "杂项";
               }
               case 2 -> {
                  return "渲染类";
               }
               case 3 -> {
                  return "移动类";
               }
               case 4 -> {
                  return "玩家类";
               }
               case 5 -> {
                  return "漏洞类";
               }
               case 6 -> {
                  return "客户端类";
               }
            }
         }

         return this.name();
      }

      // $FF: synthetic method
      private static Category[] $values() {
         return new Category[]{Combat, Misc, Render, Movement, Player, Exploit, Client};
      }
   }
}
