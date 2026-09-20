package dev.Astra.mod.commands.impl;

import dev.Astra.Astra;
import dev.Astra.api.events.eventbus.EventListener;
import dev.Astra.api.events.impl.PacketEvent;
import dev.Astra.mod.commands.Command;
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
      Astra.EVENT_BUS.subscribe(this);
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      return null;
   }

   @EventListener
   public void onPacketReceive(PacketEvent.Receive e) {
      class_2596<?> packet2 = e.getPacket();
      class_7439 packet;
      if (packet2 instanceof class_7439 && ((packet = (class_7439)packet2).comp_763().getString().contains("chat.use") || packet.comp_763().getString().contains("命令") || packet.comp_763().getString().contains("Bad command") || packet.comp_763().getString().contains("No such command") || packet.comp_763().getString().contains("<--[HERE]") || packet.comp_763().getString().contains("Unknown") || packet.comp_763().getString().contains("帮助") || packet.comp_763().getString().contains("执行错误"))) {
         this.sendChatMessage("ping: " + (System.currentTimeMillis() - this.sendTime) + "ms");
         Astra.EVENT_BUS.unsubscribe(this);
      }

   }
}
