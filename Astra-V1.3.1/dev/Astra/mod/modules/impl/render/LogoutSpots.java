package dev.Astra.mod.modules.impl.render;

import com.google.common.collect.Maps;
import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.api.events.impl.UpdateEvent;
import dev.Astra.api.utils.render.ModelPlayer;
import dev.Astra.api.utils.render.Render3DUtil;
import dev.Astra.asm.accessors.IEntity;
import dev.Astra.mod.modules.Module;
import dev.Astra.mod.modules.impl.client.hud.TextRadar;
import dev.Astra.mod.modules.settings.impl.BooleanSetting;
import dev.Astra.mod.modules.settings.impl.ColorSetting;
import dev.Astra.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_1657;
import net.minecraft.class_1934;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2703;
import net.minecraft.class_4587;
import net.minecraft.class_742;
import net.minecraft.class_7828;
import net.minecraft.class_2703.class_5893;

public class LogoutSpots extends Module {
   private final ColorSetting box = this.add((new ColorSetting("Box", new Color(255, 255, 255, 100))).injectBoolean(true));
   private final ColorSetting fill = this.add((new ColorSetting("Fill", new Color(255, 255, 255, 100))).injectBoolean(true));
   private final ColorSetting text = this.add((new ColorSetting("Text", new Color(255, 255, 255, 255))).injectBoolean(true));
   private final ColorSetting chamsFill = this.add((new ColorSetting("ChamsFill", new Color(255, 255, 255, 100))).injectBoolean(true));
   private final ColorSetting chamsLine = this.add((new ColorSetting("ChamsLine", new Color(255, 255, 255, 100))).injectBoolean(true));
   final Map<UUID, class_1657> playerCache = Maps.newConcurrentMap();
   final Map<UUID, ModelPlayer> logoutCache = Maps.newConcurrentMap();
   private final BooleanSetting health = this.add(new BooleanSetting("Health", true));
   private final BooleanSetting totem = this.add(new BooleanSetting("Totem", true));
   private final BooleanSetting message = this.add(new BooleanSetting("Message", true));
   private final SliderSetting distance = this.add(new SliderSetting("Distance", (double)128.0F, (double)0.0F, (double)512.0F, (double)1.0F));

   public LogoutSpots() {
      super("LogoutSpots", Module.Category.Render);
      this.setChinese("退出记录");
   }

   @EventListener
   public void onPacketReceive(PacketEvent.Receive event) {
      if (!nullCheck()) {
         Object object = event.getPacket();
         if (object instanceof class_2703) {
            class_2703 packet = (class_2703)object;
            if (packet.method_46327().contains(class_5893.field_29136)) {
               for(class_2703.class_2705 addedPlayer : packet.method_46330()) {
                  if (addedPlayer.comp_1110() != class_1934.field_9219) {
                     for(UUID uuid : this.logoutCache.keySet()) {
                        if (uuid.equals(addedPlayer.comp_1107().getId())) {
                           class_1657 player = ((ModelPlayer)this.logoutCache.get(uuid)).player;
                           if (this.message.getValue()) {
                              mc.execute(() -> {
                                 String var10001 = player.method_5477().getString();
                                 this.sendMessage("§f" + var10001 + " §rLogged back at §f" + player.method_31477() + ", " + player.method_31478() + ", " + player.method_31479());
                              });
                           }

                           this.logoutCache.remove(uuid);
                        }
                     }
                  }
               }
            }
         } else {
            Object var11 = event.getPacket();
            if (var11 instanceof class_7828) {
               class_7828 playerRemoveS2CPacket = (class_7828)var11;

               for(UUID uuid2 : playerRemoveS2CPacket.comp_1105()) {
                  for(UUID uuid : this.playerCache.keySet()) {
                     if (uuid.equals(uuid2)) {
                        class_1657 player = (class_1657)this.playerCache.get(uuid);
                        if (!this.logoutCache.containsKey(uuid) && player != null) {
                           ModelPlayer modelPlayer = new ModelPlayer(player);
                           if (this.message.getValue()) {
                              mc.execute(() -> {
                                 String var10001 = player.method_5477().getString();
                                 this.sendMessage("§f" + var10001 + " §rLogged out at §f" + player.method_31477() + ", " + player.method_31478() + ", " + player.method_31479());
                              });
                           }

                           this.logoutCache.put(uuid, modelPlayer);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public void onDisable() {
      this.playerCache.clear();
      this.logoutCache.clear();
   }

   @EventListener
   public void onUpdate(UpdateEvent event) {
      this.playerCache.clear();

      for(class_742 player : Astra.THREAD.getPlayers()) {
         if (player != null && !player.equals(mc.field_1724)) {
            this.playerCache.put(player.method_7334().getId(), player);
         }
      }

   }

   public void onRender3D(class_4587 matrixStack) {
      double maxDistance = this.distance.getValue();
      class_243 playerPos = mc.field_1724.method_19538();

      for(ModelPlayer data : this.logoutCache.values()) {
         class_1657 player = data.player;
         double distToPlayer = playerPos.method_1022(player.method_19538());
         if (!(distToPlayer > maxDistance)) {
            class_238 box = ((IEntity)player).getDimensions().method_30757(player.method_19538());
            if (this.box.booleanValue) {
               Render3DUtil.drawBox(matrixStack, box, this.box.getValue());
            }

            if (this.fill.booleanValue) {
               Render3DUtil.drawFill(matrixStack, box, this.fill.getValue());
            }

            if (this.chamsFill.booleanValue || this.chamsLine.booleanValue) {
               data.render(matrixStack, this.chamsFill, this.chamsLine);
            }

            if (this.text.booleanValue) {
               String var10000 = player.method_5477().getString();
               Render3DUtil.drawText3D(var10000 + (this.health.getValue() ? String.valueOf(TextRadar.getHealthColor(player)) + " " + round2((double)(player.method_6032() + player.method_6067())) : "") + (this.totem.getValue() && Astra.POP.getPop(player) > 0 ? String.valueOf(TextRadar.getPopColor(Astra.POP.getPop(player))) + " -" + Astra.POP.getPop(player) : ""), new class_243(player.method_23317(), ((IEntity)player).getDimensions().method_30757(player.method_19538()).field_1325 + (double)0.5F, player.method_23321()), this.text.getValue());
            }
         }
      }

   }

   public static float round2(double value) {
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(1, RoundingMode.HALF_UP);
      return bd.floatValue();
   }
}
