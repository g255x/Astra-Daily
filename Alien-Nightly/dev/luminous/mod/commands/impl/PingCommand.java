package dev.luminous.mod.commands.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.mod.commands.Command;
import java.util.List;
import net.minecraft.class_2596;
import net.minecraft.class_7439;

public class PingCommand extends Command {
   private long sendTime;

   public PingCommand() {
      super("ping", "");
   }

   public void runCommand(String[] parameters) {
      this.sendTime = System.currentTimeMillis();
      mc.method_1562().method_45730("chat ");
      Alien.EVENT_BUS.subscribe(this);
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }

   @EventListener
   public void onPacketReceive(PacketEvent.Receive e) {
      class_2596 var3 = e.getPacket();
      if (var3 instanceof class_7439 packet) {
         if (packet.comp_763().getString().contains("chat.use") || packet.comp_763().getString().contains("命令") || packet.comp_763().getString().contains("Bad command") || packet.comp_763().getString().contains("No such command") || packet.comp_763().getString().contains("<--[HERE]") || packet.comp_763().getString().contains("Unknown") || packet.comp_763().getString().contains("帮助") || packet.comp_763().getString().contains("执行错误")) {
            this.sendChatMessage("ping: " + (System.currentTimeMillis() - this.sendTime) + "ms");
            Alien.EVENT_BUS.unsubscribe(this);
         }
      }

   }
}
