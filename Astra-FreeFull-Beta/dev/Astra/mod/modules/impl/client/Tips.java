package dev.Astra.mod.modules.impl.client;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.DeathEvent;
import dev.Astra.api.events.impl.EntitySpawnEvent;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.RemoveEntityEvent;
import dev.Astra.api.events.impl.TotemEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.math.Timer;
import dev.Astra.api.utils.player.InventoryUtil;
import dev.Astra.core.impl.CommandManager;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_1291;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_2708;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_3419;

public class Tips extends Module {
   public static Tips INSTANCE;
   public final BooleanSetting visualRange = this.add((new BooleanSetting("VisualRange", true)).setParent());
   public final BooleanSetting friends;
   public final BooleanSetting visualRangeMessage;
   public final BooleanSetting visualRangeSound;
   public final BooleanSetting popCounter;
   public final BooleanSetting deathCoords;
   public final BooleanSetting serverLag;
   public final SliderSetting serverLagYOffset;
   public final BooleanSetting lagBack;
   public final SliderSetting lagBackYOffset;
   public final BooleanSetting potion;
   public final BooleanSetting resistanceLevelCheck;
   public final SliderSetting potionYOffset;
   public final BooleanSetting armorDurability;
   public final SliderSetting armorThreshold;
   public final SliderSetting armorYOffset;
   final DecimalFormat df;
   final int color;
   private final Timer lagTimer;
   private final Timer lagBackTimer;
   int turtles;

   public Tips() {
      super("Tips", Module.Category.Client);
      BooleanSetting var10006 = this.visualRange;
      Objects.requireNonNull(var10006);
      this.friends = this.add(new BooleanSetting("Friends", true, var10006::isOpen));
      var10006 = this.visualRange;
      Objects.requireNonNull(var10006);
      this.visualRangeMessage = this.add(new BooleanSetting("Message", false, var10006::isOpen));
      var10006 = this.visualRange;
      Objects.requireNonNull(var10006);
      this.visualRangeSound = this.add(new BooleanSetting("Sound", true, var10006::isOpen));
      this.popCounter = this.add(new BooleanSetting("PopCounter", true));
      this.deathCoords = this.add(new BooleanSetting("DeathCoords", true));
      this.serverLag = this.add((new BooleanSetting("ServerLag", true)).setParent());
      BooleanSetting var10008 = this.serverLag;
      Objects.requireNonNull(var10008);
      this.serverLagYOffset = this.add(new SliderSetting("ServerLagYOffset", 140, -200, 200, var10008::isOpen));
      this.lagBack = this.add((new BooleanSetting("LagBack", true)).setParent());
      var10008 = this.lagBack;
      Objects.requireNonNull(var10008);
      this.lagBackYOffset = this.add(new SliderSetting("LagBackYOffset", 160, -200, 200, var10008::isOpen));
      this.potion = this.add((new BooleanSetting("Potion", true)).setParent());
      var10006 = this.potion;
      Objects.requireNonNull(var10006);
      this.resistanceLevelCheck = this.add(new BooleanSetting("ResistanceLevelCheck", true, var10006::isOpen));
      var10008 = this.potion;
      Objects.requireNonNull(var10008);
      this.potionYOffset = this.add(new SliderSetting("PotionYOffset", -40, -200, 200, var10008::isOpen));
      this.armorDurability = this.add((new BooleanSetting("ArmorDurability", true)).setParent());
      BooleanSetting var10009 = this.armorDurability;
      Objects.requireNonNull(var10009);
      this.armorThreshold = this.add(new SliderSetting("ArmorThreshold", (double)20.0F, (double)0.0F, (double)100.0F, (double)1.0F, var10009::isOpen));
      var10008 = this.armorDurability;
      Objects.requireNonNull(var10008);
      this.armorYOffset = this.add(new SliderSetting("ArmorYOffset", 30, -200, 200, var10008::isOpen));
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
         boolean isFriend = Astra.FRIEND.isFriend(playerName);
         if ((!isFriend || this.friends.getValue()) && event.getEntity() != mc.field_1724) {
            if (this.visualRangeMessage.getValue()) {
               CommandManager.sendMessageId((isFriend ? String.valueOf(class_124.field_1075) + playerName : String.valueOf(class_124.field_1068) + playerName) + "§f entered your visual range.", event.getEntity().method_5628() + 777);
            }

            if (this.visualRangeSound.getValue()) {
               mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0F, 1.9F);
            }

         }
      }
   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent event) {
      if (this.visualRange.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null) {
         String playerName = event.getEntity().method_5476().getString();
         boolean isFriend = Astra.FRIEND.isFriend(playerName);
         if ((!isFriend || this.friends.getValue()) && event.getEntity() != mc.field_1724) {
            if (this.visualRangeMessage.getValue()) {
               CommandManager.sendMessageId((isFriend ? String.valueOf(class_124.field_1075) + playerName : String.valueOf(class_124.field_1068) + playerName) + "§f left your visual range.", event.getEntity().method_5628() + 777);
            }

            if (this.visualRangeSound.getValue()) {
               mc.field_1687.method_8396(mc.field_1724, mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0F, 1.9F);
            }

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
      int screenWidth = mc.method_22683().method_4486();
      int screenHeight = mc.method_22683().method_4502();
      int centerY = screenHeight / 2 + 9;
      if (this.serverLag.getValue() && this.lagTimer.passedS((double)1.0F)) {
         DecimalFormat var10000 = this.df;
         double var10001 = (double)this.lagTimer.getMs();
         String text = "Server not responding (" + var10000.format(var10001 / (double)1000.0F) + "s)";
         int x = screenWidth / 2 - mc.field_1772.method_1727(text) / 2;
         int y = centerY - this.serverLagYOffset.getValueInt();
         drawContext.method_51433(mc.field_1772, text, x, y, this.color, true);
      }

      if (this.lagBack.getValue() && !this.lagBackTimer.passedS((double)1.0F)) {
         DecimalFormat var23 = this.df;
         double var24 = (double)(1000L - this.lagBackTimer.getMs());
         String text = "Lagback (" + var23.format(var24 / (double)1000.0F) + "s)";
         int x = screenWidth / 2 - mc.field_1772.method_1727(text) / 2;
         int y = centerY - this.lagBackYOffset.getValueInt();
         drawContext.method_51433(mc.field_1772, text, x, y, this.color, true);
      }

      if (this.potion.getValue()) {
         List<TextSegment> segments = new ArrayList();
         if (this.turtles > 0) {
            segments.add(new TextSegment(String.valueOf(this.turtles), -256));
         }

         if (mc.field_1724.method_6059(class_1294.field_5907) && (!this.resistanceLevelCheck.getValue() || mc.field_1724.method_6112(class_1294.field_5907).method_5578() > 0)) {
            if (!segments.isEmpty()) {
               segments.add(new TextSegment(" ", -1));
            }

            String time = String.valueOf(mc.field_1724.method_6112(class_1294.field_5907).method_5584() / 20 + 1);
            segments.add(new TextSegment(time, -5730616));
         }

         if (mc.field_1724.method_6059(class_1294.field_5910)) {
            if (!segments.isEmpty()) {
               segments.add(new TextSegment(" ", -1));
            }

            String time = String.valueOf(mc.field_1724.method_6112(class_1294.field_5910).method_5584() / 20 + 1);
            segments.add(new TextSegment(time, -16121));
         }

         if (!segments.isEmpty()) {
            float totalWidth = 0.0F;

            for(TextSegment seg : segments) {
               totalWidth += (float)mc.field_1772.method_1727(seg.text);
            }

            float startX = (float)(screenWidth / 2) - totalWidth / 2.0F;
            float currentX = startX;
            int y = centerY - this.potionYOffset.getValueInt();

            for(TextSegment seg : segments) {
               drawContext.method_51433(mc.field_1772, seg.text, (int)currentX, y, seg.color, true);
               currentX += (float)mc.field_1772.method_1727(seg.text);
            }
         }
      }

      if (this.armorDurability.getValue() && mc.field_1724 != null) {
         this.renderArmorWarning(drawContext, centerY);
      }

   }

   private void renderArmorWarning(class_332 drawContext, int centerY) {
      class_1657 player = mc.field_1724;
      if (player != null) {
         int threshold = this.armorThreshold.getValueInt();
         boolean lowDurability = false;
         int[] slots = new int[]{3, 2, 1, 0};

         for(int slot : slots) {
            class_1799 stack = (class_1799)player.method_31548().field_7548.get(slot);
            if (!stack.method_7960() && stack.method_7963()) {
               int maxDamage = stack.method_7936();
               int currentDamage = stack.method_7919();
               int remaining = maxDamage - currentDamage;
               int percent = (int)((float)remaining / (float)maxDamage * 100.0F);
               if (percent < threshold) {
                  lowDurability = true;
                  break;
               }
            }
         }

         if (lowDurability) {
            String text = "Armor durability too low!";
            int textColor = 16733525;
            int x = mc.method_22683().method_4486() / 2 - mc.field_1772.method_1727(text) / 2;
            int y = centerY - this.armorYOffset.getValueInt();
            drawContext.method_51433(mc.field_1772, text, x, y, textColor, true);
         }

      }
   }

   @EventListener
   public void onPlayerDeath(DeathEvent event) {
      class_1657 player = event.getPlayer();
      if (this.popCounter.getValue()) {
         if (Astra.POP.popContainer.containsKey(player.method_5477().getString())) {
            int l_Count = (Integer)Astra.POP.popContainer.get(player.method_5477().getString());
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
         if (Astra.POP.popContainer.containsKey(player.method_5477().getString())) {
            l_Count = (Integer)Astra.POP.popContainer.get(player.method_5477().getString());
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

   private static class TextSegment {
      String text;
      int color;

      TextSegment(String text, int color) {
         this.text = text;
         this.color = color;
      }
   }
}
