package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DeathEvent;
import dev.luminous.api.events.impl.EntitySpawnEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.RemoveEntityEvent;
import dev.luminous.api.events.impl.TotemEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_1291;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_2708;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_3419;

public class Tips extends Module {
   public static Tips INSTANCE;
   public final BooleanSetting visualRange = this.add((new BooleanSetting("VisualRange", false)).setParent());
   public final BooleanSetting friends;
   public final BooleanSetting popCounter;
   public final BooleanSetting deathCoords;
   public final BooleanSetting serverLag;
   public final BooleanSetting lagBack;
   public final BooleanSetting potion;
   public final BooleanSetting resistanceLevelCheck;
   private final SliderSetting yOffset;
   final DecimalFormat df;
   final int color;
   private final Timer lagTimer;
   private final Timer lagBackTimer;
   int turtles;

   public Tips() {
      super("Tips", Module.Category.Misc);
      BooleanSetting var10006 = this.visualRange;
      Objects.requireNonNull(var10006);
      this.friends = this.add(new BooleanSetting("Friends", false, var10006::isOpen));
      this.popCounter = this.add(new BooleanSetting("PopCounter", true));
      this.deathCoords = this.add(new BooleanSetting("DeathCoords", true));
      this.serverLag = this.add(new BooleanSetting("ServerLag", true));
      this.lagBack = this.add(new BooleanSetting("LagBack", true));
      this.potion = this.add((new BooleanSetting("Potion", true)).setParent());
      var10006 = this.potion;
      Objects.requireNonNull(var10006);
      this.resistanceLevelCheck = this.add(new BooleanSetting("ResistanceLevelCheck", true, var10006::isOpen));
      BooleanSetting var10008 = this.potion;
      Objects.requireNonNull(var10008);
      this.yOffset = this.add(new SliderSetting("YOffset", 0, -200, 200, var10008::isOpen));
      this.df = new DecimalFormat("0.0");
      this.color = (new Color(190, 0, 0)).getRGB();
      this.lagTimer = new Timer();
      this.lagBackTimer = new Timer();
      this.turtles = 0;
      this.setChinese("提示");
      INSTANCE = this;
   }

   @EventListener
   public void onAddEntity(EntitySpawnEvent event) {
      if (this.visualRange.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null) {
         String playerName = event.getEntity().method_5476().getString();
         boolean isFriend = Alien.FRIEND.isFriend(playerName);
         if ((!isFriend || this.friends.getValue()) && event.getEntity() != mc.field_1724) {
            CommandManager.sendMessageId((isFriend ? String.valueOf(class_124.field_1075) + playerName : String.valueOf(class_124.field_1068) + playerName) + "§f entered your visual range.", event.getEntity().method_5628() + 777);
            mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0F, 1.9F);
         }
      }

   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent event) {
      if (this.visualRange.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null) {
         String playerName = event.getEntity().method_5476().getString();
         boolean isFriend = Alien.FRIEND.isFriend(playerName);
         if ((!isFriend || this.friends.getValue()) && event.getEntity() != mc.field_1724) {
            CommandManager.sendMessageId((isFriend ? String.valueOf(class_124.field_1075) + playerName : String.valueOf(class_124.field_1068) + playerName) + "§f left your visual range.", event.getEntity().method_5628() + 777);
            mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0F, 1.9F);
         }
      }

   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      if (this.potion.getValue()) {
         this.turtles = InventoryUtil.getPotionCount((class_1291)class_1294.field_5907.comp_349());
      }

   }

   @EventListener
   public void onPacketEvent(PacketEvent.Receive event) {
      this.lagTimer.reset();
      if (event.getPacket() instanceof class_2708) {
         this.lagBackTimer.reset();
      }

   }

   public void onRender2D(class_332 drawContext, float tickDelta) {
      if (this.serverLag.getValue() && this.lagTimer.passedS(1.4)) {
         DecimalFormat var10000 = this.df;
         double var10001 = (double)this.lagTimer.getMs();
         String text = "Server not responding (" + var10000.format(var10001 / (double)1000.0F) + "s)";
         drawContext.method_51433(mc.field_1772, text, mc.method_22683().method_4486() / 2 - mc.field_1772.method_1727(text) / 2, 19, this.color, true);
      }

      if (this.lagBack.getValue() && !this.lagBackTimer.passedS((double)1.5F)) {
         DecimalFormat var6 = this.df;
         double var7 = (double)(1500L - this.lagBackTimer.getMs());
         String text = "Lagback (" + var6.format(var7 / (double)1000.0F) + "s)";
         drawContext.method_51433(mc.field_1772, text, mc.method_22683().method_4486() / 2 - mc.field_1772.method_1727(text) / 2, 28, this.color, true);
      }

      if (this.potion.getValue()) {
         StringBuilder stringBuilder = new StringBuilder();
         if (this.turtles > 0) {
            stringBuilder.append("§e").append(this.turtles);
         }

         if (mc.field_1724.method_6059(class_1294.field_5907) && (!this.resistanceLevelCheck.getValue() || mc.field_1724.method_6112(class_1294.field_5907).method_5578() > 0)) {
            if (!stringBuilder.isEmpty()) {
               stringBuilder.append(" ");
            }

            stringBuilder.append("§9").append(mc.field_1724.method_6112(class_1294.field_5907).method_5584() / 20 + 1);
         }

         if (mc.field_1724.method_6059(class_1294.field_5910)) {
            if (!stringBuilder.isEmpty()) {
               stringBuilder.append(" ");
            }

            stringBuilder.append("§4").append(mc.field_1724.method_6112(class_1294.field_5910).method_5584() / 20 + 1);
         }

         if (mc.field_1724.method_6059(class_1294.field_5904)) {
            if (!stringBuilder.isEmpty()) {
               stringBuilder.append(" ");
            }

            stringBuilder.append("§b").append(mc.field_1724.method_6112(class_1294.field_5904).method_5584() / 20 + 1);
         }

         drawContext.method_51433(mc.field_1772, stringBuilder.toString(), mc.method_22683().method_4486() / 2 - mc.field_1772.method_1727(stringBuilder.toString()) / 2, mc.method_22683().method_4502() / 2 + 9 - this.yOffset.getValueInt(), -1, true);
      }

   }

   @EventListener
   public void onPlayerDeath(DeathEvent event) {
      class_1657 player = event.getPlayer();
      if (this.popCounter.getValue()) {
         if (Alien.POP.popContainer.containsKey(player.method_5477().getString())) {
            int l_Count = (Integer)Alien.POP.popContainer.get(player.method_5477().getString());
            if (l_Count == 1) {
               if (player.equals(mc.field_1724)) {
                  this.sendMessage("§fYou§r died after popping §f" + l_Count + "§r totem.", player.method_5628());
               } else {
                  this.sendMessage("§f" + player.method_5477().getString() + "§r died after popping §f" + l_Count + "§r totem.", player.method_5628());
               }
            } else if (player.equals(mc.field_1724)) {
               this.sendMessage("§fYou§r died after popping §f" + l_Count + "§r totems.", player.method_5628());
            } else {
               this.sendMessage("§f" + player.method_5477().getString() + "§r died after popping §f" + l_Count + "§r totems.", player.method_5628());
            }
         } else if (player.equals(mc.field_1724)) {
            this.sendMessage("§fYou§r died.", player.method_5628());
         } else {
            this.sendMessage("§f" + player.method_5477().getString() + "§r died.", player.method_5628());
         }
      }

      if (this.deathCoords.getValue() && player == mc.field_1724) {
         int var10001 = player.method_31477();
         this.sendMessage("§4You died at " + var10001 + ", " + player.method_31478() + ", " + player.method_31479());
      }

   }

   @EventListener
   public void onTotem(TotemEvent event) {
      if (this.popCounter.getValue()) {
         class_1657 player = event.getPlayer();
         int l_Count = 1;
         if (Alien.POP.popContainer.containsKey(player.method_5477().getString())) {
            l_Count = (Integer)Alien.POP.popContainer.get(player.method_5477().getString());
         }

         if (l_Count == 1) {
            if (player.equals(mc.field_1724)) {
               this.sendMessage("§fYou§r popped §f" + l_Count + "§r totem.", player.method_5628());
            } else {
               this.sendMessage("§f" + player.method_5477().getString() + " §rpopped §f" + l_Count + "§r totems.", player.method_5628());
            }
         } else if (player.equals(mc.field_1724)) {
            this.sendMessage("§fYou§r popped §f" + l_Count + "§r totem.", player.method_5628());
         } else {
            this.sendMessage("§f" + player.method_5477().getString() + " §rhas popped §f" + l_Count + "§r totems.", player.method_5628());
         }
      }

   }

   public void sendMessage(String message, int id) {
      if (!nullCheck()) {
         if (ClientSetting.INSTANCE.messageStyle.getValue() == ClientSetting.Style.Moon) {
            CommandManager.sendMessageId("§f[§3" + this.getName() + "§f] " + message, id);
            return;
         }

         CommandManager.sendMessageId(message, id);
      }

   }
}
