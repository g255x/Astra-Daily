package dev.Astra.mod.commands.impl;

import dev.Astra.Astra;
import dev.Astra.mod.commands.Command;
import java.util.ArrayList;
import java.util.List;

public class FriendCommand extends Command {
   public FriendCommand() {
      super("friend", "[name/reset/list] | [add/remove] [name]");
   }

   public void runCommand(String[] parameters) {
      if (parameters.length == 0) {
         this.sendUsage();
      } else {
         switch (parameters[0]) {
            case "reset":
               Astra.FRIEND.friendList.clear();
               this.sendChatMessage("§fFriends list got reset");
               return;
            case "list":
               if (Astra.FRIEND.friendList.isEmpty()) {
                  this.sendChatMessage("§fFriends list is empty");
                  return;
               } else {
                  StringBuilder friends = new StringBuilder();
                  int time = 0;
                  boolean first = true;
                  boolean start = true;

                  for(String name : Astra.FRIEND.friendList) {
                     if (!first) {
                        friends.append(", ");
                     }

                     friends.append(name);
                     first = false;
                     ++time;
                     if (time > 3) {
                        this.sendChatMessage((start ? "§eFriends §a" : "§a") + String.valueOf(friends));
                        friends = new StringBuilder();
                        start = false;
                        first = true;
                        time = 0;
                     }
                  }

                  if (first) {
                     this.sendChatMessage("§a" + String.valueOf(friends));
                  }

                  return;
               }
            case "add":
               if (parameters.length == 2) {
                  Astra.FRIEND.add(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Astra.FRIEND.isFriend(parameters[1]) ? " §ahas been friended" : " §chas been unfriended"));
                  return;
               }

               this.sendUsage();
               return;
            case "remove":
            case "del":
               if (parameters.length == 2) {
                  Astra.FRIEND.remove(parameters[1]);
                  this.sendChatMessage("§f" + parameters[1] + (Astra.FRIEND.isFriend(parameters[1]) ? " §ahas been friended" : " §chas been unfriended"));
                  return;
               }

               this.sendUsage();
               return;
            default:
               if (parameters.length == 1) {
                  this.sendChatMessage("§f" + parameters[0] + (Astra.FRIEND.isFriend(parameters[0]) ? " §ais friended" : " §cisn't friended"));
               } else {
                  this.sendUsage();
               }
         }
      }
   }

   public String[] getAutocorrect(int count, List<String> seperated) {
      if (count != 1) {
         return null;
      } else {
         String input = ((String)seperated.getLast()).toLowerCase();
         ArrayList<String> correct = new ArrayList();

         for(String x : List.of("add", "remove", "list", "reset")) {
            if (input.equalsIgnoreCase(Astra.getPrefix() + "friend") || x.toLowerCase().startsWith(input)) {
               correct.add(x);
            }
         }

         int numCmds = correct.size();
         String[] commands = new String[numCmds];
         int i = 0;

         for(String x : correct) {
            commands[i++] = x;
         }

         return commands;
      }
   }
}
